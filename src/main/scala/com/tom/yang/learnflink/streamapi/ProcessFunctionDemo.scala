package com.tom.yang.learnflink.streamapi

import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

/**
 * ProcessFunction API (底层 API)
 * HighLevel Api无法访问 Timestamp 和 watermark 信息
 * 普通的算子只能获取当前数据，或者当前聚合的状态
 * RichFunction 可以重写生命周期方法，获取运行时上线文 getRuntimeContext
 * ProcessFunction 是唯一可以获取时间信息的 API，它继承自 RichFunction
 *    可以获取当前的 timestamp watermark
 *    可以注册定时事件，指定某个时间点发生的操作
 *    可以输出侧输出流
 *
 * Flink 提供了 8 种 ProcessFunction：
 *    ProcessFunction
 *    KeyedProcessFunction
 *    CoProcessFunction
 *    ProcessJoinFunction
 *    BroadcastProcessFunction
 *    KeyedBroadcastProcessFunction
 *    ProcessWindowFunction
 *    ProcessAllWindowFunction
 */
object ProcessFunctionDemo extends App {
  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
  env.setParallelism(1)
  env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
  // 生成随机数据
  val inputStream: DataStream[SensorReading] = env.addSource(new MySensorSource())
  // 检测每个传感器的温度在 10秒之内是否连续上升，是就报警
  val warningStream: DataStream[String] = inputStream
    .keyBy(_.id)
    .process(new TempIncrWarning(6000L))// 利用自定义 ProcessFunction 实现
  warningStream.print()
  env.execute()
}

/**
 * 自定义 KeyedProcessFunction[K, I, O]
 * K 当前 key 的类型
 * I 输入的 event 的类型
 * O 输出的结果的类型
 */
class TempIncrWarning(interval: Long) extends KeyedProcessFunction[String, SensorReading, String] {
  // 状态管理
  // 这里保存前一次的数据, 需要用到 ValueState[T]， 利用 getRuntimeContext.getState 来获取
  lazy val lastTempState: ValueState[Double] = getRuntimeContext.getState(new ValueStateDescriptor[Double]("lastTemp", classOf[Double]))
  // 保存定时器的timestamp 方便删除定时器
  lazy val timerState: ValueState[Long] = getRuntimeContext.getState(new ValueStateDescriptor[Long]("currentTimer", classOf[Long]))

  // 处理数据的方法
  // ctx 当前 KeyedProcessFunction 的上下文
  override def processElement(value: SensorReading, ctx: KeyedProcessFunction[String, SensorReading, String]#Context, out: Collector[String]): Unit = {
    // 向侧输出流输出数据
//    ctx.output(OutputTag("side1"), value)
    // 获取当前 watermark
//    ctx.timerService().currentWatermark()
    // 获取当前 processing timestamp
//    ctx.timerService().currentProcessingTime()
    // 注册基于 EventTime 的定时器， 这里只定义何时(一个具体的 timestamp)触发定时器
//    ctx.timerService().registerEventTimeTimer(1598410100202L)
    // 删除基于 EventTime 的定时器
//    ctx.timerService().deleteEventTimeTimer(1598410100202L)
    // 向下游输出数据, 注意 KeyedProcessFunction[String, SensorReading, String] 定义的类型
//    out.collect(true)

    /**
     * 业务处理
     * 如果收到上升的温度就注册一个定时器
     * 如果收到下降的就删除定时器
     */

    //获取状态
    val lastTemp = lastTempState.value()
    val timer = timerState.value()
    // 更新状态
    lastTempState.update(value.temperature)

    // 注意 timer 没有的情况下取到的默认值， 对于数值类型，取到的是 0
    if(value.temperature > lastTemp && timer == 0){
      val ts = ctx.timerService().currentProcessingTime() + interval
      // 注册定时器时注意时间语义，如果用 EventTime 那么就是根据 watermark的时间触发
      ctx.timerService().registerProcessingTimeTimer(ts)
      // 更新 timer 的 timestamp
      timerState.update(ts)
    } else if(value.temperature < lastTemp) {
      // 清除 timer
      ctx.timerService().deleteProcessingTimeTimer(timer)
      // 清空 state
      timerState.clear()
    }
  }
  // 定时器触发时，执行的操作
  override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[String, SensorReading, String]#OnTimerContext, out: Collector[String]): Unit = {
    out.collect(s"${timestamp} >>> ${ctx.getCurrentKey()} : 温度值 ${interval/1000}s 内连续上升")
    // 清除当前状态
    timerState.clear()
  }
}
