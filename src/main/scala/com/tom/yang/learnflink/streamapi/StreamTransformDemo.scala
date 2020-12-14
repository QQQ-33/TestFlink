package com.tom.yang.learnflink.streamapi

import org.apache.flink.api.common.functions.{MapFunction, RichMapFunction}
import org.apache.flink.api.java.functions.KeySelector
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala._

/**
 * Flink 的转换算子
 * 读取 source 后，sink之前的所有操作都是转换算子
 *
 * DataStream =(keyBy)> KeyedStream =(map/reduce/aggregate)> DataStream
 *
 */
object StreamTransformDemo extends App {
  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

  env.setParallelism(1)
  // 原始数据流
  val stream: DataStream[String] = env.readTextFile("C:\\Users\\qk965\\TomWorkspace\\TestFlink\\src\\main\\resources\\sensor.txt")
  // 基本转换
  // 对象数据流
  val dataStream: DataStream[SensorReading] = stream.map(line => {
    val fields: Array[String] = line.split(",")
    SensorReading(fields(0), fields(1).toLong, fields(2).toDouble)
  })
  // 集合转换
//  dataStream.keyBy(sensor => sensor.id)
//  val result = dataStream.keyBy(new MySensorSelector()).sum("temperature")
//  val result = dataStream.keyBy(sensor => sensor.id).minBy("temperature")
  // 找出每组 最大的时间，最小的温度
//  val result = dataStream.keyBy(sensor => sensor.id).reduce( (current, sensor) => {
//    SensorReading(current.id, Math.max(current.timestamp, sensor.timestamp), Math.min(current.temperature, sensor.temperature))
//  })
//  result.print()

  // 分流转换
  // split 将一个 DataStream 从逻辑上划分成多个 DataStream, select 从划分的流中获取流
  // 用 process function 的 side output 来代替 split => select
  val splitStream: SplitStream[SensorReading] =dataStream.split(data => {
    if(data.temperature > 30){
      Seq("High")
    } else {
      Seq("Low")
    }
  })
  val highStream: DataStream[SensorReading] = splitStream.select("High")
  val lowStream: DataStream[SensorReading] = splitStream.select("Low")
  highStream.print()
  lowStream.print()
  // 合流转换
  // connect, 不同类型的流也可以合并, 但是只能合并2条流
//  val warningStream: DataStream[(String, Double)] = highStream.map(data => (data.id, data.temperature))
//  val connectedStream: ConnectedStreams[(String, Double), SensorReading] = warningStream.connect(lowStream)
//
//  val result: DataStream[Object] = connectedStream.map(
//    warning => (warning._1, warning._2, "high temp warning"),
//    lowTemp => (lowTemp.id, "normal")
//  )
  // union 只能合并相同类型的流，但是可以无限合并流

  // 自定义UDF
  dataStream.map(new MyMapper())

  env.execute()
}
// 基本转换 map flatMap filter
// 基本转换算子可以与 state 一起进行运算

// keyBy 重分区，应用聚合算子前必须执行的操作
/**
 * keyBy，基于 key 的 hashCode 进行重分区
 * DataStream => KeyedStream
 */
class MySensorSelector extends KeySelector[SensorReading, String] {
  override def getKey(value: SensorReading): String = {
    value.id
  }
}

// 滚动聚合算子， 所有滚动聚合算子都会调用 aggregate 算子
// sum min max minBy maxBy reduce
// 一般聚合算子
// 多流转换算子
/**
 * 自定义UDF
 */
class MyMapper extends MapFunction[SensorReading, (String, Double)] {
  override def map(value: SensorReading): (String, Double) = {
    (value.id, value.temperature)
  }
}

class MyRichMapper extends RichMapFunction[SensorReading, Int] {
//  getRuntimeContext
  // 仅调用一次
  override def open(parameters: Configuration): Unit = {

  }
  override def map(value: SensorReading): Int = {
    value.timestamp.toInt
  }

  override def close(): Unit = {

  }
}
