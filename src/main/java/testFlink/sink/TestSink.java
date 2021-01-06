package testFlink.sink;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import testFlink.beans.SensorReading;
import testFlink.source.SensorSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

public class TestSink {
    private static final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    public static void main(String[] args)throws Exception {
        env.setParallelism(1);

        env.execute();
    }
    // 引入 kafka connector
    private static void testKafkaSink()throws Exception {
        DataStream<SensorReading> input = env.addSource(new SensorSource());
        DataStream<String> result = input.map(SensorReading::toString);

        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");

        result.addSink(new FlinkKafkaProducer011<String>(
            "sensor",
            new SimpleStringSchema(),
            props
        ));
    }
    // 引入 redis connector
//    private static void testRedisSink() throws Exception {
//        DataStream<SensorReading> input = env.addSource(new SensorSource());
//        // Jedis 连接
//        FlinkJedisPoolConfig config = new FlinkJedisPoolConfig.Builder()
//                .setHost("")
//                .setPort(6379)
//                .build();
//
//
//        input.addSink(new RedisSink<>(config, new RedisMapper<SensorReading>() {
//            // 定义保存到 redis 的命令
//            @Override
//            public RedisCommandDescription getCommandDescription() {
//                return new RedisCommandDescription(RedisCommand.HSET, "sensor_tmp");
//            }
//
//            @Override
//            public String getKeyFromData(SensorReading s) {
//                return s.getId();
//            }
//
//            @Override
//            public String getValueFromData(SensorReading s) {
//                return s.getTemperature() + "";
//            }
//        }));
//
//    }
    // 引入 ES connector
//    private static void testESSink() throws Exception {
//        DataStream<SensorReading> input = env.addSource(new SensorSource());
//        ArrayList<HttpHost> hosts = new ArrayList<>();
//        hosts.add(new HttpHost("", 9200));
//        input.addSink(new ElasticsearchSink.Builder<SensorReading>(hosts, new MyESSinkFunction()).build());
//    }

//    private static class MyESSinkFunction implements ElasticsearchSinkFunction<SensorReading> {
//        /**
//         * 具体如何写入 ES
//         * @param element 收到的数据
//         * @param ctx
//         * @param indexer
//         */
//        @Override
//        public void process(SensorReading element, RuntimeContext ctx, RequestIndexer indexer) {
//            // 定义写入的 source
//            HashMap<String, String> dataSource = new HashMap<>();
//            dataSource.put("id", element.getId());
//            dataSource.put("temp", element.getTemperature().toString());
//            dataSource.put("ts", element.getTimestamp().toString());
//            // 定义写入 ES 的命令
//            IndexRequest indexRequest = Requests
//                    .indexRequest()
//                    .index("sensor")
//                    .source(dataSource);
//            indexer.add(indexRequest);
//        }
//    }
    // 引入 对应 JDBC 的 connector
    private static void testJDBCSink() throws Exception {
        DataStream<SensorReading> input = env.addSource(new SensorSource());
        input.addSink(new MyJDBCSinkFunction());
    }

    private static class MyJDBCSinkFunction extends RichSinkFunction<SensorReading> {
        Connection conn = null;
        PreparedStatement insertPs = null;
        PreparedStatement updatePs = null;
        @Override
        public void open(Configuration parameters) throws Exception {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test","username","password");
            insertPs = conn.prepareStatement("insert into sensor_temp (id, temp) values (?,?)");
            updatePs = conn.prepareStatement("update sensor_temp set temp = ? where id = ?");
        }

        @Override
        public void close() throws Exception {
            super.close();
        }

        @Override
        public void invoke(SensorReading value, Context context) throws Exception {
            updatePs.setDouble(1, value.getTemperature());
            updatePs.setString(2, value.getId());
            updatePs.execute();
            if(updatePs.getUpdateCount() == 0){
                insertPs.setString(1, value.getId());
                insertPs.setDouble(2, value.getTemperature());
                insertPs.execute();
            }
        }
    }
}
