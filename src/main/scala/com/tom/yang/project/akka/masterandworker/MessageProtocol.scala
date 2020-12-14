package com.tom.yang.project.akka.masterandworker


// Worker 向 Master 注册的信息
case class RegisterWorkerInfo(id: String, cpu: Int, ram: Int)
// Master 向 Worker 回复注册成功的实例
case object RegisterWorkerInfo
// 向 Master 发送心跳
case object SendHeartBeat
// 心跳信息
case class HeartBeatInfo(id: String)
// Master 定期检查心跳超时
case object CheckTimeout
// Master 用于保存 Worker 的信息
class WorkerInfo(val id: String, val cpu: Int, val ram: Int ){
  var time: Long = System.currentTimeMillis()
}
