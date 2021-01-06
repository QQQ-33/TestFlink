package testFlink.process;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import testFlink.beans.SensorReading;
import testFlink.source.SensorSource;

public class TestProcessFunction {
    public static StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    public static void main(String[] args)throws Exception {
        testKeyedProcessFunction();
    }

    public static void testKeyedProcessFunction() throws Exception{
        env.setParallelism(1);
        DataStream<SensorReading> input = env.addSource(new SensorSource());
        input.keyBy(s -> s.getId())
//                .process(new MyProcess())
                .process(new TempConsIncreWarning(10))
                .print();

        env.execute();

    }

    // 自定义 KeyedProcessFunction<K, I, O>   key，输入，输出
    public static class MyProcess extends KeyedProcessFunction<String, SensorReading, Integer> {
        ValueState<Long> timeTs = null;
        // 利用 state 记录当前 timer 的时间戳方便后续的删除等操作
        @Override
        public void open(Configuration parameters) throws Exception {
            timeTs = getRuntimeContext().getState(new ValueStateDescriptor<Long>("ts-time", Long.class));
        }

        // 业务逻辑
        @Override
        public void processElement(SensorReading value, Context context, Collector<Integer> collector) throws Exception {
            collector.collect(value.getId().length());
            // Context
            //当前时间语义下的时间戳
            context.timestamp();
            // 当前的key
            context.getCurrentKey();
            // 定义任意输出的位置，比如可以输出到侧输出流
            // 需要传入 OutputTag<X>
//            context.output();
            // 定时器相关
            // 当前的处理时间 (long 值)
            context.timerService().currentProcessingTime();
            // 当前的 watermark (long 值)
            context.timerService().currentWatermark();
            /*
            * 定时器
            * 需要传入一个 long 时间戳
            * 区分不同的时间语义
            * 定时器只针对当前 key 有效
            * */
            // 注册基于 EventTime 的定时器
            long ts = context.timerService().currentProcessingTime() + 1000L;
//            context.timerService().registerEventTimeTimer((value.getTimestamp() + 10) * 1000 + 1000L);
            // 注册基于 ProcessingTime 的定时器
            context.timerService().registerProcessingTimeTimer(ts);
            timeTs.update(ts);
            // 删除定时器，删除哪个的依据是时间戳
//            context.timerService().deleteEventTimeTimer((value.getTimestamp() + 10) * 1000 + 1000L);
//            context.timerService().deleteProcessingTimeTimer(timeTs.value());
        }

        // 定时器触发时的业务逻辑
        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<Integer> out) throws Exception {
            // 触发的定时器的 timestamp
            // ctx 这里依然可以取到上下文
            // out 输出
            System.out.println();
        }
    }
    /*
        实例测试 (状态编程 + 定时器)
        监控温度传感器的温度值，在 10 秒钟之内连续上升，则报警
     */
    public static class TempConsIncreWarning extends KeyedProcessFunction<String, SensorReading, String>{

        // 监控的时间长度
        private int interval ;
        // 上一次的温度 state
        private ValueState<Double> lastTempState;
        // 计时器的 ts
        private ValueState<Long> timerTsState;

        TempConsIncreWarning(int interval){
            this.interval = interval;
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            lastTempState = getRuntimeContext().getState(new ValueStateDescriptor<Double>("last-temp", Double.class));
            timerTsState = getRuntimeContext().getState(new ValueStateDescriptor<Long>("timer-ts", Long.class));
        }

        @Override
        public void processElement(SensorReading sensorReading, Context context, Collector<String> collector) throws Exception {
            Double lastTemp = lastTempState.value();
            Long timeTs = timerTsState.value();
            // 如果温度上升，当没有定时器时，注册定时器监控下个十秒的定时器
            if(null != lastTemp && sensorReading.getTemperature() > lastTemp && timeTs == null){
                long ts = context.timerService().currentProcessingTime() + interval * 1000L;
                context.timerService().registerProcessingTimeTimer(ts);
                timerTsState.update(ts);
            } else if(null != lastTemp && sensorReading.getTemperature() < lastTemp && timeTs != null){
                context.timerService().deleteProcessingTimeTimer(timeTs);
                timerTsState.clear();
            }
            lastTempState.update(sensorReading.getTemperature());
        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
            out.collect("传感器：" + ctx.getCurrentKey() + " 温度值连续 " + interval + " s上升");
            timerTsState.clear();
        }

        @Override
        public void close() throws Exception {
            lastTempState.clear();
            timerTsState.clear();
        }
    }

}
