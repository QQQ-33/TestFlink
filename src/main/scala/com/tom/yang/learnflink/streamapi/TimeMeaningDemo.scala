package com.tom.yang.learnflink.streamapi

import com.tom.yang.learnflink.streamapi.WindowDemo.env
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._

/**
 * 时间语义
 *
 * Event Time       事件创建的时间
 * Ingesting Time   数据进入 Flink 的时间
 * Processing Time  执行操作算子的本地系统时间，与机器相关
 *
 * 通常业务中经常关心的是 Event Time， 处理数据乱序，迟到数据
 * 结合 Processing Time 可以计算数据延迟
 */
object TimeMeaningDemo extends App{
  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
  env.setParallelism(1)
  // 设置时间语义, TimeCharacteristic 枚举类，提供了 3 种时间语义
  env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
  // 如果使用了 Event Time 还需指定数据中那个字段是 Event Time
  // .assignAscendingTimestamps() 传入一个字段提取器，类似 KeyBy 传入的那个, 注意 Flink 使用的是毫秒数
  // .assignTimestampsAndWatermarks()
  val inputStream: DataStream[String] = env.readTextFile("C:\\Users\\qk965\\TomWorkspace\\TestFlink\\src\\main\\resources\\sensor.txt")
  val dataStream: DataStream[SensorReading] = inputStream.map(line => {
    val fields: Array[String] = line.split(",")
    SensorReading(fields(0), fields(1).toLong, fields(2).toDouble)
  }).assignAscendingTimestamps(_.timestamp * 1000L)


}
