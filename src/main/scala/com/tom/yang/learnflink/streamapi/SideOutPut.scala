package com.tom.yang.learnflink.streamapi

import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

/**
 * Process function 还可以将数据输出至侧输出流
 * 代替流的 split
 */
object SideOutPutTest extends  App {
  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
  env.setParallelism(1)

  val inputStream: DataStream[SensorReading] = env.addSource(new MySensorSource())
  val highStream: DataStream[SensorReading] = inputStream.process(new LowTempProcess(60))
  val lowStream: DataStream[(String, Double, Long)] = highStream.getSideOutput(new OutputTag[(String, Double, Long)]("low-temp"))

  highStream.print("high-temp")
  lowStream.print("low-temp")

  env.execute("SideOutPutTest")
}

class LowTempProcess(limit: Double) extends ProcessFunction[SensorReading, SensorReading]{
  // 普通的 ProcessFunction 不接收 key
  override def processElement(value: SensorReading, ctx: ProcessFunction[SensorReading, SensorReading]#Context, out: Collector[SensorReading]): Unit = {
    if(value.temperature > limit){
      out.collect(value)
    } else {
      ctx.output(new OutputTag[(String, Double,Long)]("low-temp"), (value.id, value.temperature, value.timestamp))
    }
  }
}
