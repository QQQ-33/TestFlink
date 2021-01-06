package testFlink.source;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import testFlink.beans.SensorReading;

import java.util.Arrays;
import java.util.Properties;

public class FlinkSourceTest {
    private static final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    public static void main(String[] args)throws Exception  {
//        testCollectionSource();
//        testFileSource();
//        testKafkaSource();
        testCustomerSource();
    }

    private static void testCollectionSource() throws Exception {
        DataStream<SensorReading> stream = env.fromCollection(Arrays.asList(
            new SensorReading("sensor_1", 35.8, 1547718199L),
            new SensorReading("sensor_2", 15.4, 1547718201L),
            new SensorReading("sensor_3", 6.7, 1547718202L),
            new SensorReading("sensor_4", 38.1, 1547718205L)
        ));
        // print() 时可以传入参数表示当前打印的 stream 的名称
        stream.print("data from collection");
        DataStream<Integer> stream1 = env.fromElements(1,3,5,7,9);
        stream1.print("data from elements");
        env.execute();
    }
    private static void testFileSource() throws Exception {
        DataStream<String> stream = env.readTextFile("C:\\Users\\qk965\\TomWorkspace\\TestFlink\\src\\main\\resources\\testData\\sensorData");
        // 读进来是一行一行的字串, 需要自己封装成 bean
        DataStream<SensorReading> stream2 = stream.map(new MapFunction<String, SensorReading>() {
            @Override
            public SensorReading map(String line) throws Exception {
                String[] data = line.split(",");
                return new SensorReading(data[0], Double.valueOf(data[1]), Long.valueOf(data[2]));
            }
        });
        stream2.print();
        env.execute();
    }

    private static void testKafkaSource() throws Exception{
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
//        props.setProperty("group.id", "consumer-group");
//        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        props.setProperty("auto.offset.reset", "latest");
        // 需要引入 flink-connector-kafka 依赖
        env.addSource(new FlinkKafkaConsumer011<String>(
            "sensor",
            new SimpleStringSchema(),
            props
        ));
    }

    private static void testCustomerSource() throws Exception{
        DataStream<SensorReading> stream = env.addSource(new SensorSource());
        stream.print("data");
        env.execute();

    }
}
