package testFlink.transform;


import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import testFlink.beans.SensorReading;

public class TransformTest {
    private static final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    public static void main(String[] args)throws Exception {
        env.setParallelism(1);
//        test1();
//        test2();
        test3();

        env.execute();
    }

    /**
     * 基本转换操作
     */
    private static void test1()throws Exception{
        DataStream<String> input = env.readTextFile("C:\\Users\\qk965\\TomWorkspace\\TestFlink\\src\\main\\resources\\testData\\sensorData");
        // map
        DataStream<Integer> mapStream = input.map(new MapFunction<String, Integer>() {
            @Override
            public Integer map(String s) throws Exception {
                return s.length();
            }
        });
        // flatMap
//        DataStream<String> flatMapStream = input.flatMap(new FlatMapFunction<String, String>() {
//            @Override
//            public void flatMap(String s, Collector<String> collector) throws Exception {
//                String[] fields = s.split(",");
//                for (String f: fields){
//                    collector.collect(f);
//                }
//            }
//        });
        DataStream<String> flatMapStream = input.flatMap(new TestFunction());
        // filter e.g. 筛选 sensor_1 的数据
        DataStream<String> filterStream = input.filter(new FilterFunction<String>() {
            @Override
            public boolean filter(String s) throws Exception {
                return s.startsWith("sensor_1");
            }
        });
        mapStream.print("mapStream");
        flatMapStream.print("flatMapStream");
        filterStream.print("filterStream");
    }

    /**
     * 简单的聚合：滚动聚合操作
     *
     * 必须对 stream 进行 keyBy 后才能进行聚合操作
     * DataStream => KeyedStream
     * 基于指定 key 的 hashCode 进行分区
     * sum min max minBy maxBy
     *
     */
    private static void test2()throws Exception{
        DataStream<String> input = env.readTextFile("C:\\Users\\qk965\\TomWorkspace\\TestFlink\\src\\main\\resources\\testData\\sensorData");
        DataStream<SensorReading> dataStream = input.map( line -> {
            String[] data = line.split(",");
            return new SensorReading(data[0], Double.valueOf(data[1]), Long.valueOf(data[2]));
        });
        // 分组, <InputClass, KeyClass>
        KeyedStream<SensorReading, String> keyedStream = dataStream.keyBy(SensorReading::getId);
        // 除了求最大值的字段外，其余字段都是当前的数据
        DataStream<SensorReading> maxStream = keyedStream.max("temperature");
        // 所有字段都跟随最大值变化
        DataStream<SensorReading> maxByStream = keyedStream.maxBy("temperature");
//        maxStream.print("max");
        maxByStream.print("maxBy");
    }

    /**
     * 复杂聚合：
     * 必须先进行分组 keyBy
     * @throws Exception
     */
    private static void test3() throws Exception{
        DataStream<String> input = env.readTextFile("C:\\Users\\qk965\\TomWorkspace\\TestFlink\\src\\main\\resources\\testData\\sensorData");
        DataStream<SensorReading> dataStream = input.map( line -> {
            String[] data = line.split(",");
            return new SensorReading(data[0], Double.valueOf(data[1]), Long.valueOf(data[2]));
        });
        // 分组, <InputClass, KeyClass>
        KeyedStream<SensorReading, String> keyedStream = dataStream.keyBy(SensorReading::getId);
        // reduce 保留最大温度，最新的timestamp  v1 -> 当前数据，v2 -> 之前的数据
//        keyedStream.reduce(new ReduceFunction<SensorReading>() {
//            @Override
//            public SensorReading reduce(SensorReading v1, SensorReading v2) throws Exception {
//                new SensorReading(v1.getId(),
//                        Math.max(v1.getTemperature(), v2.getTemperature()),
//                        Math.max(v1.getTimestamp(), v2.getTimestamp()));
//            }
//        });
        DataStream<SensorReading> result = keyedStream.reduce( (currentData, newData) -> (new SensorReading(currentData.getId(),
                    Math.max(currentData.getTemperature(), newData.getTemperature()),
                    Math.max(currentData.getTimestamp(), newData.getTimestamp()))
        ));
        result.print();
    }
}
