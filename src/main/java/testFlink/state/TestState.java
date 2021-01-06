package testFlink.state;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.expressions.In;
import org.apache.flink.util.Collector;
import testFlink.beans.SensorReading;
import testFlink.source.SensorSource;

import java.util.Collections;

/**
 * Flink State
 */
public class TestState {
    private static final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    public static void main(String[] args)throws Exception {
//        test();
//        test2();
        test3();

    }

    public static void test()throws Exception{
        DataStream<SensorReading> input = env.addSource(new SensorSource());
        /**
         * 模拟场景：
         * 统计所有分区的数据个数
         */
        DataStream<Integer> result = input.map(new MyCountMapper());
        result.print();
        env.execute();

    }
    public static void test2()throws Exception{
        DataStream<SensorReading> input = env.addSource(new SensorSource());
        /**
         * 模拟场景：
         * 统计当前分区的数据个数
         */
        DataStream<Integer> result = input
                .keyBy(SensorReading::getId)
                .map(new MyKeyCountMapper());
        result.print();
        env.execute();

    }
    /**
     * 算子要使用状态管理，需要实现相应的 State
     * CheckpointedFunction
     * 不常用
     */
    public static class MyCountMapper implements MapFunction<SensorReading, Integer>,
            CheckpointedFunction {
        // 直接使用本地变量时，无法在故障时恢复
        private Integer count = 0;
        private transient ListState<Integer> state;


        @Override
        public Integer map(SensorReading s) throws Exception {
            return null;
        }

        // 保存状态
        @Override
        public void snapshotState(FunctionSnapshotContext functionSnapshotContext) throws Exception {
            state.update(Collections.singletonList(++count));
        }
        // 初始化状态
        // 定义 StateDescriptor
        @Override
        public void initializeState(FunctionInitializationContext context) throws Exception {
            ListStateDescriptor<Integer> descriptor = new ListStateDescriptor<>(
                    "event-count",
                    TypeInformation.of(new TypeHint<Integer>() {}));
            state = context.getOperatorStateStore().getListState(descriptor);
        }
    }
    /**
     * keyed State
     * 键控状态
     *
     */
    public static class MyKeyCountMapper extends RichMapFunction<SensorReading, Integer>{

        private transient ValueState<Integer> keyCountState;

        // 注意在 open 方法中进行初始化
        @Override
        public void open(Configuration parameters) throws Exception {
            ValueStateDescriptor<Integer> descriptor = new ValueStateDescriptor<Integer>(
                    "key-count",
                    TypeInformation.of(Integer.class),
                    0);
            keyCountState = getRuntimeContext().getState(descriptor);
        }

        @Override
        public Integer map(SensorReading sensorReading) throws Exception {
            Integer count = keyCountState.value();
            count++;
            keyCountState.update(count);
            return count;
//            return 0;
        }
    }

    /**
     * 模拟监控两个连续的温度值差值在10度以上则报警
     */
    public static void test3() throws Exception{
        DataStream<SensorReading> input = env.addSource(new SensorSource());
        DataStream<Tuple3<String,Double,Double>> result = input.keyBy(s -> s.getId())
                // 检测温度跳变
                .flatMap(new TempChangeWarning(5.0d));
        result.print("temp");

        env.execute();

    }
    private static class TempChangeWarning extends RichFlatMapFunction<SensorReading, Tuple3<String,Double,Double>> {
        private Double threshold;
        private transient ValueState<Double> preTemp;
        TempChangeWarning(Double threshold){
            this.threshold = threshold;
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            ValueStateDescriptor<Double> descriptor = new ValueStateDescriptor<Double>("sensor-temp", Double.class);
            preTemp = getRuntimeContext().getState(descriptor);
        }

        @Override
        public void flatMap(SensorReading sensorReading, Collector<Tuple3<String, Double, Double>> collector) throws Exception {
            Double lastTemp = preTemp.value();
            if(lastTemp != null){
                double diff = Math.abs(sensorReading.getTemperature() - lastTemp);
                if(diff >= threshold){
                    collector.collect(Tuple3.of(sensorReading.getId(), lastTemp, sensorReading.getTemperature()));
                }
            }
            preTemp.update(sensorReading.getTemperature());
        }
    }
}
