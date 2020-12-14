package com.tom.yang.learnflink.streamapi

import java.time.Duration

import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, TimestampAssigner, TimestampAssignerSupplier, Watermark, WatermarkGenerator, WatermarkGeneratorSupplier, WatermarkOutput, WatermarkStrategy}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._

/**
 * WaterMark 水印(水位线)
 * 数据乱序的影响：
 * 当 Flink 以 EventTime 模式处理数据流时，它会根据数据里的时间戳来处理基于时间的算子
 * 由于网络/分布式等原因，会导致乱序数据的产生，导致窗口运算结果不准确
 *
 * 遇到一个 timestamp 达到了窗口关闭时间，不应该立即触发窗口关闭，而是等待一段时间，再关闭窗口
 * Watermark 是一种衡量 EventTime 进展的机制，可以设定延迟触发
 * 处理乱序事件，通常用 watermark 结合 window 来使用
 * watermark 用于表示 timestamp 小于 watermark 的数据都已经到达了，因此 window 的关闭由 watermark 触发
 * watermark 让程序平衡了延迟和结果的正确性，可以根据数据最大的乱序程度来确定 watermark
 *
 * Watermark 的实现：
 * watermark 是一条数据记录，只包含 timestamp
 * 它是单调递增的，确保数据流的 EventTime 时间向前推进，与数据流的 timestamp 相关
 * 当到达设定的延迟时间时 Flink 会向数据流中插入 watermark，表示当前的 EventTime
 *
 * Watermark 的传递：
 * watermark 需要 fanout 给下游，保证下游所有分区都知道当前的 EvenTime
 * 由于上游可能由多个 partition，所以对于每个分区需要记录下 watermark
 * 并且向下游广播所有 watermark 中最小的那个
 * 收到 watermark => 更新对应分区的 watermark => 查看所有分区最小的 watermark 是否有变化 => 有变化就向下游广播新的 watermark(没有变化就不用广播)
 */
object WaterMarkDemo extends App{
  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
  env.setParallelism(1)
  env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
  val inputStream: DataStream[String] = env.readTextFile("C:\\Users\\qk965\\TomWorkspace\\TestFlink\\src\\main\\resources\\sensor.txt")
  val dataStream: DataStream[SensorReading] = inputStream.map(line => {
    val fields: Array[String] = line.split(",")
    SensorReading(fields(0), fields(1).toLong, fields(2).toDouble)
  })
    .assignTimestampsAndWatermarks(WatermarkStrategy
    .forBoundedOutOfOrderness(Duration.ofSeconds(30))// 固定延迟的 watermark
    .withTimestampAssigner(new SerializableTimestampAssigner[SensorReading] {
      override def extractTimestamp(element: SensorReading, recordTimestamp: Long): Long = element.timestamp
    }))
    // 自定义
//    .assignTimestampsAndWatermarks(new MyWatermarkAssigner(Duration.ofSeconds(30)))

  // def assignTimestampsAndWatermarks(watermarkStrategy: WatermarkStrategy[T]): DataStream[T] = {}
  // 需要实现 WatermarkStrategy ，或者使用静态方法快速设置 watermark

  // 旧版本的实现，.assignTimestampsAndWatermarks 有两种实现
  // AssignerWithPeriodicWatermarks   周期性生成 watermark
  // AssignerWithPunctuatedWatermarks 不间断生成 watermark
}
// 自定义 WatermarkAssigner ，只需要重写两个方法
class MyWatermarkAssigner(delay: Duration) extends WatermarkStrategy[SensorReading]{
  // 如何生成 Watermark
  override def createWatermarkGenerator(context: WatermarkGeneratorSupplier.Context): WatermarkGenerator[SensorReading] = {
    new MyWatermarkGenerator(delay)
  }
  // 如何生成 EventTime, 默认实现是取 Process Time
  override def createTimestampAssigner(context: TimestampAssignerSupplier.Context): TimestampAssigner[SensorReading] = {
    (element: SensorReading, recordTimestamp: Long) => element.timestamp
  }
}
// 自定义 WatermarkGenerator
class MyWatermarkGenerator(delay: Duration) extends WatermarkGenerator[SensorReading] {
  // 当前最大的 event time
  private var maxTimestamp: Long = Long.MinValue
  // watermark 延迟多久, 通过构造器传入

  // output.emitWatermark(new Watermark(timestamp)) 发送 watermark
  // 收到 event 时更新 watermark， 代替 AssignerWithPunctuatedWatermarks
  override def onEvent(event: SensorReading, eventTimestamp: Long, output: WatermarkOutput): Unit = {
    maxTimestamp = Math.max(maxTimestamp, event.timestamp * 1000L)
  }
  // 周期性更新 watermark 代替 AssignerWithPeriodicWatermarks
  // 默认 200 ms， 可以通过 env.getConfig.setAutoWatermarkInterval(5000) 设定
  override def onPeriodicEmit(output: WatermarkOutput): Unit = {
    output.emitWatermark(new Watermark(maxTimestamp - delay.toMillis()))
  }
}
