package testFlink.window;

import org.apache.commons.collections.IteratorUtils;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import testFlink.beans.SensorReading;
import testFlink.source.SensorSource;

import java.time.Duration;

public class TestWindow {
    private static final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    public static void main(String[] args)throws Exception {
        test2();
//        test3();
    }

    public static void test()throws Exception{
        DataStream<SensorReading> input = env.addSource(new SensorSource());
        KeyedStream<SensorReading, String> keyedStream = input.keyBy(s -> s.getId());
        // 时间窗口
//        keyedStream.window(TumblingProcessingTimeWindows.of(Time.seconds(15)));
        // 简写，底层根据当前的时间语义来定义 Event 或者 Processing window
        // 根据传入的参数个数来决定是 Tumbling 还是 sliding
        keyedStream.timeWindow(Time.seconds(15));
        // 创建 session window
//        keyedStream.window(EventTimeSessionWindows.withGap(Time.minutes(1)));
        // 计数窗口
        keyedStream.countWindow(10, 2);
        env.execute();
    }

    /**
     * window function
     * 增量聚合函数
     */
    public static void test2() throws Exception{
        DataStream<SensorReading> input = env.addSource(new SensorSource());
        KeyedStream<SensorReading, String> keyedStream = input.keyBy(s -> s.getId());

        DataStream<Integer> result = keyedStream.timeWindow(Time.seconds(10))
            // AggregateFunction<IN, ACC, OUT> 输入，中间结果，输出
            .aggregate(new AggregateFunction<SensorReading, Integer, Integer>() {
                // 初始值
                @Override
                public Integer createAccumulator() {
                    return 0;
                }

                @Override
                public Integer add(SensorReading sensorReading, Integer accumulator) {
                    return accumulator + 1;
                }

                @Override
                public Integer getResult(Integer accumulator) {
                    return accumulator;
                }

                @Override
                public Integer merge(Integer a, Integer b) {
                    return a + b;
                }
            });
        result.print("test window function");
        env.execute();
    }

    /**
     * 全窗口函数
     */
    public static void test3() throws Exception {
        DataStream<SensorReading> input = env.addSource(new SensorSource());
        KeyedStream<SensorReading, String> keyedStream = input.keyBy(s -> s.getId());

        // WindowFunction<IN, OUT, KEY, W extends Window>
        // 输入，输出，当前的key，窗口
        DataStream<Tuple2<String, Integer>> result = keyedStream.timeWindow(Time.seconds(10))
            .apply(new WindowFunction<SensorReading, Tuple2<String, Integer>, String, TimeWindow>() {
                // key 的值， 窗口，当前窗口所有的值，collector
                @Override
                public void apply(String s, TimeWindow timeWindow, Iterable<SensorReading> iterable, Collector<Tuple2<String, Integer>> collector) throws Exception {
                    Integer count = IteratorUtils.toList(iterable.iterator()).size();
                    collector.collect(Tuple2.of(s, count));
                }
            });
        result.print("test window function");
        env.execute();
    }

    public static void test4()throws Exception{
        DataStream<SensorReading> input = env.addSource(new SensorSource());
        KeyedStream<SensorReading, String> keyedStream = input.keyBy(s -> s.getId());
        OutputTag<SensorReading> tag = new OutputTag<>("late");
        // WindowFunction<IN, OUT, KEY, W extends Window>
        // 输入，输出，当前的key，窗口
        SingleOutputStreamOperator<SensorReading> result = keyedStream.timeWindow(Time.seconds(10))
            // 何时触发计算
//          .trigger()
            // 何时移除某些数据
//          .evictor()
            // 允许迟到数据， 达到窗口时会先触发一次计算，1分钟之后再次进行计算
            // 这里如何定义迟到？ 需要先指定时间语义，这里只支持 event-time
            .allowedLateness(Time.minutes(1))
            // 迟到数据进入侧输出liu
            .sideOutputLateData(tag)
            .sum("temperature");
        // 获取迟到的数据
        // 注意只有 SingleOutputStreamOperator 才有这个方法
        result.getSideOutput(tag);
    }

    /**
     * 时间语义
     */
    public static void test5()throws Exception{
        // 设置时间语义， 后续还需要指定哪个字段是 EventTime
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        DataStream<SensorReading> input = env.addSource(new SensorSource());
        KeyedStream<SensorReading, String> keyedStream = input.keyBy(s -> s.getId());
    }

    /**
     * watermark
     */
    public static void test6()throws Exception{
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStream<SensorReading> input = env.addSource(new SensorSource())
                // 设置 watermark 以及 EventTime 如何从 source 中获取
                .assignTimestampsAndWatermarks(WatermarkStrategy
                        // 乱序数据 watermark
                        .<SensorReading>forBoundedOutOfOrderness(Duration.ofSeconds(10))
                        // 非乱序数据 watermark
//                        .<SensorReading>forMonotonousTimestamps()
                        // Event Time s为当前的数据，timestamp为当前的时间戳
                        .withTimestampAssigner((s, timestamp) -> s.getTimestamp()));
        KeyedStream<SensorReading, String> keyedStream = input.keyBy(s -> s.getId());

    }
}
