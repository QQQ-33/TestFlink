package com.tom.yang.learnflink.streamapi

import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.flink.api.common.serialization.{SimpleStringEncoder, SimpleStringSchema}
import org.apache.flink.configuration.Configuration
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011

/**
 * Sink
 *
 */
object SinkDemo extends App{

  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
  env.setParallelism(1)

  val inputStream: DataStream[String] = env.readTextFile("C:\\Users\\qk965\\TomWorkspace\\TestFlink\\src\\main\\resources\\sensor.txt")
  val dataStream: DataStream[String] = inputStream.map(line => {
    val fields: Array[String] = line.split(",")
    SensorReading(fields(0), fields(1).toLong, fields(2).toDouble).toString
  })

  // 通常 source 和 sink 是必须的，print() 也是一个 sink
//  dataStream.print()
//  dataStream.writeToSocket("localhost", 6666, new SimpleStringSchema())

  // file Sink
  // 注意流中的数据类型
  //  val fileSink: StreamingFileSink[String] = StreamingFileSink.forRowFormat(
//    new Path("C:/Users/qk965/TomWorkspace/TestFlink/src/main/resources/out.txt"),
//    new SimpleStringEncoder[String]("UTF-8")).build()
//  dataStream.addSink(fileSink)

  // kafka Sink
  // 注意流中的数据类型
//  val kafkaSink: FlinkKafkaProducer011[String] = new FlinkKafkaProducer011[String]("localhost:9092","testSink", new SimpleStringSchema())
//  dataStream.addSink(kafkaSink)

  // ES Sink

  // jdbc Sink 利用自定义sink将数据写入数据库，这里用 mysql 举例
  // 需要引入依赖 mysql-connector-java 版本参考 mysql 的版本
//  dataStream.addSink(new MyJdbcSink())
  env.execute();
}

class MyJdbcSink extends RichSinkFunction[SensorReading]{
  // 首先定义 mysql 连接，定义预编译的语句
  var connection: Connection = _
  var insertStmt: PreparedStatement = _
  var updateStmt: PreparedStatement = _

  // 在 open 方法中，创建连接以及预编译语句
  override def open(parameters: Configuration): Unit = {
    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123456");
    insertStmt = connection.prepareStatement("insert into temp (id, temperature, createTime ) values(?,?,?)");
    updateStmt = connection.prepareStatement("update temp set temperature = ? where id = ?");
  }
  // 在 invoke 中重写如何向 mysql 中写入数据
  override def invoke(value: SensorReading, context: SinkFunction.Context[_]): Unit = {
    updateStmt.setDouble(1, value.temperature)
    updateStmt.setString(2, value.id)
    updateStmt.execute()
    if(updateStmt.getUpdateCount == 0){
      insertStmt.setString(1, value.id)
      insertStmt.setDouble(2, value.temperature)
      insertStmt.setLong(3, value.timestamp)
      insertStmt.execute()
    }
  }

  // 在 close 中关闭连接
  override def close(): Unit = {
    insertStmt.close()
    updateStmt.close()
    connection.close()
  }
}
