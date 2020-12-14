package com.tom.yang.project.akka.masterandworker

import java.util.UUID

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._

class Worker(masterHost: String, masterPort: Int, masterSystem: String, masterActor: String) extends Actor{
  var masterProxy: ActorSelection = _
  val id = UUID.randomUUID().toString
  val heartBeatInterval = 3000
  override def preStart(): Unit = {
    masterProxy = context.actorSelection(s"akka.tcp://${masterSystem}@${masterHost}:${masterPort}/user/${masterActor}")
  }
  override def receive: Receive = {
    case "start" => {
      println("Worker started")
      masterProxy ! RegisterWorkerInfo(id, 4, 4)
    }
    case RegisterWorkerInfo => {
      println(s"Worker : ${id} 注册成功")
      println("开始发送心跳")

      /**
       * 定时调度器，定时发送消息
       * 1. 延时多久发送 0 表示不延时
       * 2. 间隔多久发送
       * 3. 发送的目标
       * 4. 发送的内容
       */
      import context.dispatcher
      // 注意引入时间单位 millis
      context.system.scheduler.schedule(0 millis, heartBeatInterval millis, self, SendHeartBeat)
    }
    case SendHeartBeat => {
      println(s"Worker : ${id} 向 Master 发送心跳")
      masterProxy ! HeartBeatInfo(id)
    }
  }
}
object Worker extends App {
  if(args.length != 8){
    println("请输入参数 host port systemName actorName masterHost masterPort masterSystemName masterActorName")
    sys.exit()
  }
  val host = args(0)
  val port = args(1)
  val systemName = args(2)
  val actorName = args(3)
  val masterHost = args(4)
  val masterPort = args(5)
  val masterSystemName = args(6)
  val masterActorName = args(7)
  val config = ConfigFactory.parseString(
    s"""
       |akka.actor.provider="akka.remote.RemoteActorRefProvider"
       |akka.remote.netty.tcp.hostname=$host
       |akka.remote.netty.tcp.port=$port
       |""".stripMargin)
  val actorSystem = ActorSystem(systemName, config)
  val actorRef = actorSystem.actorOf(Props(new Worker(masterHost, masterPort.toInt, masterSystemName, masterActorName)), actorName)
  actorRef ! "start"

}
