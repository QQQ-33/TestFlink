package com.tom.yang.learnflink.streamapi

import com.tom.yang.learnflink.streamapi.SinkDemo.env
import org.apache.flink.api.common.functions.RichReduceFunction
import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.assigners.{EventTimeSessionWindows, SlidingProcessingTimeWindows, TumblingProcessingTimeWindows}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.{GlobalWindow, TimeWindow}
import org.apache.flink.util.Collector

/**
 * Window
 * 窗口
 * 可以把无限的数据流进行切分，得到有限的数据集进行处理也就是得到有界流
 * 窗口就是将无限流切割为有限流的一种方式，它会将数据分发到有限大小的 bucket 中进行分析
 *
 * TimeWindow(默认从整点开始)
 *    滚动时间窗口 => 将数据依固定的窗口长度对数据进行切分，时间对齐，窗口长度固定，没有重叠  (window size)， 左闭右开
 *    滑动时间窗口 => 广义的窗口形式，固定的窗口长度，滑动间隔，可以有重叠 (window size, window slide)
 *    会话窗口 => 一段时间没有接收到新数据就生成新的窗口，没有时间对齐 (session gap)
 * CountWindow
 *    滚动计数窗口
 *    滑动计数窗口
 *
 * window 操作必须在 keyBy 之后才能使用 (对 keyBy 之后每个 key 的数据开窗)
 * 或者使用 windowAll (对所有数据整体开窗，不推荐使用，因为不能并行)
 *
 * window() 接收一个 WindowAssigner，用于将每条数据分发到正确的 window 中
 *
 * .timeWindow(Time.seconds(15))
 * .timeWindow(Time.seconds(15), Time.seconds(5))
 * .window(EventTimeSessionWindows.withGap(Time.minutes(10)))
 */
object WindowDemo extends App{
  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
  env.setParallelism(1)

  val inputStream: DataStream[String] = env.readTextFile("C:\\Users\\qk965\\TomWorkspace\\TestFlink\\src\\main\\resources\\sensor.txt")
  val dataStream: DataStream[SensorReading] = inputStream.map(line => {
    val fields: Array[String] = line.split(",")
    SensorReading(fields(0), fields(1).toLong, fields(2).toDouble)
  })
  // 滚动窗口
  val window1: WindowedStream[SensorReading, String, TimeWindow] = dataStream.keyBy(_.id).timeWindow(Time.seconds(10))
  // 底层一点的 APi 创建滚动窗口，第一个参数为窗口大小，第二个参数为偏移量，偏移量主要作用是修正时区，这个偏移量是根据本地时间和UTC时间确定的
  // 注意计算 start time 时是 timestamp - (timestamp - offset + slide) % slide
//  val result1 = dataStream.keyBy(_.id).window(TumblingProcessingTimeWindows.of(Time.days(1), Time.hours(-8)))
  val window2: WindowedStream[SensorReading, String, TimeWindow] = dataStream.keyBy(_.id).timeWindow(Time.seconds(10), Time.seconds(5))
  // session window
  val window3: WindowedStream[SensorReading, String, TimeWindow] = dataStream.keyBy(_.id).window(EventTimeSessionWindows.withGap(Time.minutes(5)))
  // count window
  val window4: WindowedStream[SensorReading, String, GlobalWindow] = dataStream.keyBy(_.id).countWindow(10)
  val window5: WindowedStream[SensorReading, String, GlobalWindow]  = dataStream.keyBy(_.id).countWindow(10, 2)

  /**
   * 利用窗口进行计算  (Window Function)
   *
   * 增量聚合函数
   *    每条数据到来就进行计算，保持一个简单的状态
   *    ReduceFunction, AggregateFunction
   * 全窗口函数
   *    先把窗口所有数据收集起来，等到计算的时候遍历所有数据
   *    ProcessWindowFunction
   *
   * 其他 API
   * .trigger()触发器，定义 window 什么时候关闭，触发计算并计算结果
   * .evictor()移除器，与 .trigger() 同时自定义，用于定义何时清除无用的数据
   * .allowedLateness() 允许处理迟到的数据
   * .sideOutputLateData() 将迟到的数据放入侧输入流，用于保证数据完整性
   * .getSideOutput() 获取侧输入流
   *
   * start time 的计算
   * 滚动窗口，按照 window size 取余的整倍数
   * 滑动窗口，按照 window slide 取余的整倍数
   */

  window1.allowedLateness(Time.seconds(10))
  // 窗口运算输出的结果仅在窗口中生效，不适用于全局
  window1.reduce( (pre, s) => {
    SensorReading(s.id, s.timestamp, Math.max(pre.temperature, pre.temperature))
  })
  // 全窗口函数 def apply[R: TypeInformation](function: WindowFunction[T, R, K, W]): DataStream[R] = {}
  window2.apply(new MyWindowFunction())
  env.execute(" window test")
}

class MyWindowFunction extends WindowFunction[SensorReading, (Long, Int), String, TimeWindow] {
  // 自定义全 window 函数， 注意入参和出参类型
  // key 是前面一步 keyBy 返回的类型, 一定注意keyBy的调用方式, keyBy("id") 返回的是 Tuple
  // input 可以获取到 window 内所有数据， out 负责收集返回结果
  override def apply(key: String, window: TimeWindow, input: Iterable[SensorReading], out: Collector[(Long, Int)]): Unit = {
    // 输出一个当前窗口时间，以及元素的个数
    out.collect((window.getStart, input.size))
  }
}

