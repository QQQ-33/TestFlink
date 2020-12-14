package com.tom.yang.learnflink.streamapi

import java.time.{LocalDateTime, ZoneOffset}
import java.time.format.DateTimeFormatter

import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

object ProcessFunctionTest extends App {
  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
  env.setParallelism(1)
  env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)

  val randomDataStream: DataStream[SensorReading] = env.addSource(new MySensorSource())

  val warningStream: DataStream[String] = randomDataStream.keyBy(_.id).process(new TestWarningFunction(6000L))

  warningStream.print()
  env.execute()
}

class TestWarningFunction(interval: Long) extends KeyedProcessFunction[String, SensorReading, String]{
  lazy val lastTempuratureState: ValueState[Double] = getRuntimeContext.getState(new ValueStateDescriptor[Double]("lastTemp", classOf[Double]))
  lazy val currentTimerState: ValueState[Long] = getRuntimeContext.getState(new ValueStateDescriptor[Long]("currentTimer", classOf[Long]))


  override def processElement(value: SensorReading, ctx: KeyedProcessFunction[String, SensorReading, String]#Context, out: Collector[String]): Unit = {
    val lastTemp = lastTempuratureState.value()
    val currentTimer = currentTimerState.value()

    lastTempuratureState.update(value.temperature)
    if(value.temperature > lastTemp && currentTimer == 0){
      val newTimer = ctx.timerService().currentProcessingTime() + interval
      ctx.timerService().registerProcessingTimeTimer(newTimer)
      currentTimerState.update(newTimer)
    } else if(value.temperature < lastTemp) {
      ctx.timerService().deleteProcessingTimeTimer(currentTimer)
      currentTimerState.clear()
    }
  }

  override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[String, SensorReading, String]#OnTimerContext, out: Collector[String]): Unit = {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val time = LocalDateTime.ofEpochSecond(timestamp/1000, 0 , ZoneOffset.ofHours(8))
    out.collect(s"${time.format(formatter)} ${ctx.getCurrentKey()} 温度连续 ${interval/1000}s 升高")
  }
}
