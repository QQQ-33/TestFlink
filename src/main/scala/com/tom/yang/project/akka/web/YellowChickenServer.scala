package com.tom.yang.project.akka.web

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
 * 服务端
 * 创建 ActorSystem
 * 创建 Actor，使用 YellowChickenServer 配置
 * 启动 YellowChickenServer
 */
class YellowChickenServer extends Actor{
  override def receive: Receive = {
    case "start" => println("小黄鸡开始工作啦...")
    case ClientMessage(msg) => {
      val replay = msg match {
        case "学费" => "￥20000"
        case "科目" => "Java Scala JavaScript"
        case "地址" => "北京昌平XXX路"
        case _ => "我不知道哦！"
      }
      sender() ! ServerMessage(replay)
    }

  }
}
object YellowChickenServer extends App{

  // 创建 Actor 的时候需要进行网络参数配置
  val host = "127.0.0.1"
  val port = "9999"
  val config = ConfigFactory.parseString(
    s"""
      |akka.actor.provider="akka.remote.RemoteActorRefProvider"
      |akka.remote.netty.tcp.hostname=$host
      |akka.remote.netty.tcp.port=$port
      |""".stripMargin)
  // 这里起的名字，会在客户端被使用，注意将 config 传入进去
  val server: ActorSystem = ActorSystem("Server", config)
  val yellowChickenServerRef: ActorRef = server.actorOf(Props[YellowChickenServer], "YellowChickenServer")
  yellowChickenServerRef ! "start"
}
