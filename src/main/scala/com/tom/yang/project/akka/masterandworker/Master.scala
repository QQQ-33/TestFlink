package com.tom.yang.project.akka.masterandworker

import java.util.Date

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._
import scala.collection.mutable

class Master extends Actor{
  val workers: mutable.HashMap[String, WorkerInfo] = new mutable.HashMap()
  val maxTimeout = 6000
  override def receive: Receive = {
    case "start" => {
      println("Master started")
      println("开始 Worker 的心跳检测")
      import context.dispatcher
      context.system.scheduler.schedule(0 millis, 10000 millis, self, CheckTimeout)
    }
    case RegisterWorkerInfo(id, cpu, ram) => {
      if(!workers.contains(id)){
        workers(id) = new WorkerInfo(id, cpu, ram)
      }
      sender() ! RegisterWorkerInfo
    }
    case HeartBeatInfo(id) => {
      println(s"收到 ${id} 的心跳")
      if(workers.contains(id)){
        val worker = workers(id)
        worker.time = System.currentTimeMillis()
      }
    }
    case CheckTimeout => {
      val workerInfos = workers.values
      workerInfos.filter( info => (System.currentTimeMillis() - info.time) > maxTimeout).foreach(worker => {
        println(s"Worker ${worker.id} 已经 ${maxTimeout / 1000}s 没有心跳，将它移除")
        workers.remove(worker.id)
      })
      println(s"当前 ${workers.values.size} 个存活")
    }
  }
}

/**
 * Master 相关参数动态指定
 * Host
 * Port
 * ActorSystemName
 * ActorName
 */
object Master {
  def main(args: Array[String]): Unit = {
    if(args.length != 4){
      println("请输入参数 host port systemName actorName")
      sys.exit()
    }
    val host = args(0)
    val port = args(1)
    val systemName = args(2)
    val actorName = args(3)

    val config = ConfigFactory.parseString(
      s"""
         |akka.actor.provider="akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname=$host
         |akka.remote.netty.tcp.port=$port
         |""".stripMargin)
    val actorSystem = ActorSystem(systemName, config)
    val actorRef = actorSystem.actorOf(Props[Master], actorName)
    actorRef ! "start"
  }

}
