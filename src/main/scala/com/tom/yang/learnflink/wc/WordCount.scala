package com.tom.yang.learnflink.wc

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
 * 基础案例
 * WordCount
 */
object WordCount {
  def main(args: Array[String]): Unit = {
//    batchWordCount()
    streamWordCount(args)
  }

  def batchWordCount(): Unit = {
    // 创建 批处理 的执行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

    // 读取文件数据
    val inputDataSet: DataSet[String] = env.readTextFile("C:\\Users\\qk965\\TomWorkspace\\TestFlink\\src\\main\\resources\\word.txt")

    // 基于 DataSet 进行转换和统计:
    //    分词，得到所有 word
    //    转换为二元组
    //    以二元组第一个元素作为分组的 key
    //    聚合二元组第二个元素的值
    // 需要引入 Flink 的隐式转换 import org.apache.flink.api.scala._
    val resultDataSet: AggregateDataSet[(String, Int)] = inputDataSet.flatMap(_.split(" ")).map((_, 1)).groupBy(0).sum(1)
    resultDataSet.print()
  }

  def streamWordCount(args: Array[String]): Unit = {
    // 创建 流式处理 的执行环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 获取运行时参数
    val params: ParameterTool = ParameterTool.fromArgs(args);
    val host: String = params.get("host")
    val port: Int = params.getInt("port")

    // 接收 socket 文本流  (nc -lk 7777 打开 7777 端口发送消息)
    val inputDataStream: DataStream[String] = env.socketTextStream(host, port);

    // 定义转换操作
    // 分组的操作使用 keyBy
    val resultDataStream: DataStream[(String, Int)] = inputDataStream.flatMap(_.split(" ")).map((_, 1)).keyBy(_._1).sum(1)
    resultDataStream.print()

    // 流式处理一定要加上 env.execute() 启动任务
    env.execute("streamWordCount")
  }
}
