package testFlink.transform;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;
import testFlink.beans.SensorReading;
import testFlink.source.SensorSource;

import java.util.Collections;

public class MultiStreamTransform {
    private static final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    public static void main(String[] args)throws Exception {
        env.setParallelism(1);

        env.execute();
    }

    /**
     * split & select
     * @throws Exception
     */
    private static void test() throws Exception{
        DataStream<SensorReading> input = env.addSource(new SensorSource());
        // 分流(已废弃，使用 sideOutPutStream) split  温度分流
        SplitStream<SensorReading> splitStream = input.split(new OutputSelector<SensorReading>() {
            @Override
            public Iterable<String> select(SensorReading sensorReading) {
                return (sensorReading.getTemperature() > 0)? Collections.singletonList("high") : Collections.singletonList("low");
            }
        });
        DataStream<SensorReading> highStream = splitStream.select("high");
        DataStream<SensorReading> lowStream = splitStream.select("low");
        highStream.print("highStream");
        lowStream.print("lowStream");
        /**
         * 合流
         * connect
         * 连接两个流，内部的数据类型保持各自的类型
         * coMap/coFlatMap
         * 对两个流各自的变量进行处理，合并成一个数据类型
         */
        DataStream<Tuple3<String,Double,Long>> tupleStream = highStream.map(new MapFunction<SensorReading, Tuple3<String, Double, Long>>() {
            @Override
            public Tuple3<String, Double, Long> map(SensorReading value) throws Exception {
                return Tuple3.of(value.getId(), value.getTemperature(), value.getTimestamp());
            }
        });
        // 合流之后各自保持数据类型
        ConnectedStreams<Tuple3<String, Double, Long>, SensorReading> connectedStream = tupleStream.connect(lowStream);
        DataStream<Tuple2<String, Double>> map = connectedStream.map(new CoMapFunction<Tuple3<String, Double, Long>, SensorReading, Tuple2<String, Double>>() {
            @Override
            public Tuple2<String, Double> map1(Tuple3<String, Double, Long> t) throws Exception {
                return Tuple2.of(t.f0, t.f1);
            }

            @Override
            public Tuple2<String, Double> map2(SensorReading sensorReading) throws Exception {
                return Tuple2.of(sensorReading.getId(), sensorReading.getTemperature());
            }
        });
    }

    /**
     * connect
     * 连接两个流，内部的数据类型保持各自的类型
     * coMap/coFlatMap
     * 对两个流各自的变量进行处理，合并成一个数据类型
     * @throws Exception
     */
    private static void test2()throws Exception{
    }
}
