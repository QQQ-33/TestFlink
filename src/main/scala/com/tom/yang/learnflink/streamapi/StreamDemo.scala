package com.tom.yang.learnflink.streamapi

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011

import scala.util.Random

object StreamDemo {
  def main(args: Array[String]): Unit = {
    // 获取执行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    // 设置整体的并行度
    env.setParallelism(1)

    // 创建 source
  }
}

object SourceTest extends App{
  // 获取执行环境
  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

  // 设置整体的并行度
  env.setParallelism(1)

  // 创建 source
  //1. 从集合中读取数据
  val stream1: DataStream[SensorReading] = env.fromCollection(List[SensorReading](
    SensorReading("sensor_1", 1547718199, 35.8),
    SensorReading("sensor_2", 1547718201, 15.4),
    SensorReading("sensor_3", 1547718202, 6.7),
    SensorReading("sensor_4", 1547718205, 38.1),
    SensorReading("sensor_1", 1547718207, 30.6),
    SensorReading("sensor_2", 1547718212, 37.2),
    SensorReading("sensor_3", 1547718215, 33.5),
    SensorReading("sensor_4", 1547718218, 38.1)
  ))
  // 2. 从文件中读取数据
  val stream2: DataStream[String] = env.readTextFile("C:\\Users\\qk965\\TomWorkspace\\TestFlink\\src\\main\\resources\\sensor.txt")
  // 3. socket 文本流
//  env.socketTextStream("localhost", 6666)

  // 4. 从 kafka 等消息平台读取数据流，需要引入相关的 dependency
//  val properties = new Properties() // kafka consumer 的配置
//  properties.setProperty("bootstrap.servers","localhost:9092")
//  properties.setProperty("group.id","consumer-group")
//  properties.setProperty("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer")
//  properties.setProperty("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer")
//  properties.setProperty("auto.offset.reset","latest")
//  val stream4: DataStream[String] = env.addSource(new FlinkKafkaConsumer011[String]("sensor",new SimpleStringSchema(), properties))

  // 5. 自定义 source， 实现一个 sourceFunction， 自动生成测试数据
  val stream5: DataStream[SensorReading] = env.addSource(new MySensorSource())


//  stream1.print()
//  stream2.print()
    stream5.print()
  env.execute("source test")
}
// 模拟传感器数据
case class SensorReading(id: String, timestamp: Long, temperature: Double)

/**
 * 自定义的 source，需要实现 SourceFunction
 */
class MySensorSource() extends SourceFunction[SensorReading] {
  // 定义一个 flag 表示，数据源是否正常运行
  var running: Boolean = true;

  // 获取数据时调用的方法，正常从 sourceContext 获取，这里随机生成测试数据
  override def run(sourceContext: SourceFunction.SourceContext[SensorReading]): Unit = {
    // 随机数生成器
    val random = new Random()
    // 随机生成 10 个传感器温度值，且不断在之前的温度基础上更新（随机上下波动）
    // 初始化 10 个传感器初始温度
    var currTemps = (1 to 10).map((i) => {
      // random.nextGaussian() 按照正态分布生成随机数
      (s"Sensor-${i}", 60 + random.nextGaussian() * 20)
    })
    // 无限循环生成随机数据
    while (running){
      // 随机生成温度的波动
      currTemps = currTemps.map(sensor => (sensor._1, sensor._2 + random.nextGaussian()))
      val timestamp = System.currentTimeMillis()

      // 包装成样例类， 用 sourceContext 发出数据
      currTemps.foreach( sensor => sourceContext.collect(SensorReading(sensor._1, timestamp, sensor._2)))

      // 定义生成的间隔时间
      Thread.sleep(2000)
    }
  }

  // 停止获取数据时调用该方法
  override def cancel(): Unit = {
    running = false;
  }
}
