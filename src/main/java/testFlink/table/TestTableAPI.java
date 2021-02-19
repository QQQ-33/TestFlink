package testFlink.table;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.*;
import org.apache.flink.table.api.bridge.java.BatchTableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.descriptors.Csv;
import org.apache.flink.table.descriptors.FileSystem;
import org.apache.flink.table.descriptors.Kafka;
import org.apache.flink.table.descriptors.Schema;
import org.apache.flink.table.functions.ScalarFunction;
import org.apache.flink.types.Row;
import testFlink.beans.SensorReading;
import testFlink.source.SensorSource;

import static org.apache.flink.table.api.Expressions.*;

public class TestTableAPI {
    public static StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    public static void main(String[] args)throws Exception {
//        fromStreamEnv();
//        fromTableEnv();
        testTableFromFile();
    }


    /**
     * 从 streamEnv 读取数据
     * @throws Exception
     */
    public static void fromStreamEnv() throws Exception{
        DataStream<SensorReading> input = env.addSource(new SensorSource());

        // 创建 table api 的执行环境
        // 需要引入 flink table planner blink
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        // 包装 DataStream -> Table
        Table dataTable = tableEnv.fromDataStream(input);
        /* 直接操作 table */
        Table resultTable = dataTable.select("id, temperature")
                .where("id = 'sensor_1'");

        /* 当作数据库表来写 sql， 需要先注册成表 */
        tableEnv.createTemporaryView("sensor", dataTable);
        String sql = "select id, temperature from sensor where id = 'sensor_1'";
        Table sqlResult = tableEnv.sqlQuery(sql);

        /* 打印结果 */
        tableEnv.toAppendStream(resultTable, Row.class).print("result");
        tableEnv.toAppendStream(sqlResult, Row.class).print("sqlResult");

        env.execute();
    }

    /**
     * 直接使用 tableEnv 读取数据
     * @throws Exception
     */
    public static void fromTableEnv() throws Exception{
        // 创建 table api 的执行环境
        env.setParallelism(1);

        // 1.10 版本之前的流处理写法
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        EnvironmentSettings oldStreamSettings = EnvironmentSettings.newInstance()
                .useOldPlanner()
                .inStreamingMode()
                .build();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(streamEnv, oldStreamSettings);

        // 1.10 版本之前的批处理写法
        ExecutionEnvironment batchEnv = ExecutionEnvironment.getExecutionEnvironment();
        BatchTableEnvironment oldBatchTable = BatchTableEnvironment.create(batchEnv);

        // 1.10 之后，新版本写法, 基于 Blink 的 planner
        EnvironmentSettings blinkStreamSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamTableEnvironment blinkStreamTableEnv = StreamTableEnvironment.create(streamEnv, blinkStreamSettings);

        // 1.10 之后，新版本写法, 基于 Blink 的 planner
        EnvironmentSettings blinkBatchSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inBatchMode()
                .build();
        TableEnvironment blinkBatchTable = TableEnvironment.create(blinkBatchSettings);

    }

    public static void testTableFromFile()throws Exception{
        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        String inputPath = "C:\\Users\\qk965\\TomWorkspace\\TestFlink\\src\\main\\resources\\testData\\sensorData";
        //1. 获取 table
        // 从文件读取数据
        tableEnv.connect(new FileSystem().path(inputPath))
                .withFormat(new Csv())
                .withSchema(new Schema()
                        .field("id", DataTypes.STRING())
                        .field("temp", DataTypes.DOUBLE())
                        .field("ts", DataTypes.BIGINT())
                ).createTemporaryTable("inputTable");
        // 连接外部文件注册输出表
        // 聚合结果不支持追加写入
        tableEnv.executeSql("create table outputTable (" +
                "id STRING," +
                "temp DOUBLE) with (" +
                "'connector'='filesystem'," +
                "'path'='file:///C:\\Users\\qk965\\TomWorkspace\\TestFlink\\src\\main\\resources\\testData\\result'," +
                "'format'='csv')");

        Table inputTable = tableEnv.from("inputTable");
//        tableEnv.toAppendStream(inputTable, Row.class).print();
        //2. 简单转换
        Table result = inputTable
                .select($("id"),$("temp"))
                .filter($("id").isEqual("sensor_1"));
        tableEnv.toAppendStream(result, Row.class).print("result");
        //3. 聚合统计
        Table aggResult = inputTable.groupBy($("id"))
                .select($("id"),
                        $("id").count().as("count"),
                        $("temp").avg().as("temp_avg")
                );
//         聚合结果需要用 toRetractStream()
        tableEnv.toRetractStream(aggResult, Row.class).print("aggResult");

        //4. 输出结果到文件
        tableEnv.executeSql("insert into outputTable select id, temp from inputTable");

        env.execute();
    }

    public static void testKafkaPipeline() throws Exception{
        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        // 从 kafka 读取
        // ！connect 方法即将被删除，使用 DDL语句创建 table
        tableEnv.connect(
                new Kafka()
                .version("0.11")
                .topic("sensor")
                .property("zookeeper.connect", "localhost:2181")
                .property("bootstrap.servers", "localhost:9092")
            )
            .withFormat(new Csv())
            .withSchema(new Schema()
                    .field("id", DataTypes.STRING())
                    .field("temp", DataTypes.DOUBLE())
                    .field("ts", DataTypes.BIGINT())
            ).createTemporaryTable("inputTable");

        Table sensorTable = tableEnv.from("inputTable");

        Table resultTable = sensorTable.select($("id"), $("temp"))
                .filter($("id").isEqual("sensor_1"));
        tableEnv.toAppendStream(resultTable, Row.class).print();
        // 输出到 kafka
        tableEnv.connect(
                new Kafka()
                        .version("0.11")
                        .topic("sinktest")
                        .property("zookeeper.connect", "localhost:2181")
                        .property("bootstrap.servers", "localhost:9092")
        )
                .withFormat(new Csv())
                .withSchema(new Schema()
                        .field("id", DataTypes.STRING())
                        .field("temp", DataTypes.DOUBLE())
                        .field("ts", DataTypes.BIGINT())
                ).createTemporaryTable("outputTable");
        resultTable.insertInto("outputTable");

        env.execute();
    }
    public static void testWindow() throws Exception{
        env.setParallelism(1);
        DataStream<SensorReading> input = env.addSource(new SensorSource());

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        Table dataTable = tableEnv.fromDataStream(input);
        // Group Window
        dataTable.window(Tumble.over(lit(1).minutes()).on("rt").as("w"))
                .groupBy($("id"), $("w"))
                .select($("id"), $("temperature").avg(), $("w").end());
        // Over Window
        dataTable.window(Over.partitionBy($("id")).orderBy($("temperature")).preceding(UNBOUNDED_RANGE).as("w"))
                .select($("id"), $("temperature").avg(), $("w").end());
    }



}
