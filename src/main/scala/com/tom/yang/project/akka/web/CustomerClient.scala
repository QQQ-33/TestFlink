package com.tom.yang.project.akka.web

import akka.actor.{Actor, ActorRef, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.io.StdIn

/**
 * 客户端
 * 创建 ActorSystem
 * 创建 CustomerActor，需要得到 YellowChickenServerRef
 * 启动 CustomerActor
 */
class CustomerClient(serverHost: String, serverPort: Int, serverActorSystem: String, serverActorName: String) extends Actor{
  // 定义 server 端 ActorSystem 的 Ref， 注意不是 ActorRef 类型，是 ActorSelection
  var serverActorRef: ActorSelection = _
  // 重写 preStart 方法，对 Actor 进行初始化， 这个方法会在 Actor 运行前执行
  override def preStart(): Unit = {
    // 通过 context 获取 server 的 ActorSystem 的引用，
    // 注意URL中对用位置使用的变量在server端创建服务的时候的值
    serverActorRef = context.actorSelection(s"akka.tcp://${serverActorSystem}@${serverHost}:${serverPort}/user/${serverActorName}")
    println(serverActorRef)
  }
  override def receive: Receive = {
    case "start" => println("客户端运行，可以开始提问...")
    case "exit" => {
      context.stop(self)
      context.system.terminate()
    }
    case msg: String => {
      // 将消息发送给 Server， 最好不要直接发送字串，将字串封装到 case class 中(也就是自定义通讯协议)
      serverActorRef ! ClientMessage(msg)
    }
    case ServerMessage(msg) => println(s"收到小黄鸡回复: ${msg}")

  }
}
object CustomerClient extends App{
  // 创建 Actor 的时候需要进行网络参数配置
  val (host, port ,serverHost, serverPort, serverActorSystem, serverActorName) = ("127.0.0.1", 9998, "127.0.0.1", 9999, "Server", "YellowChickenServer")
  // stripMargin 将行分隔符去掉，默认是 |
  val config = ConfigFactory.parseString(
  s"""
      |akka.actor.provider="akka.remote.RemoteActorRefProvider"
      |akka.remote.netty.tcp.hostname=$host
      |akka.remote.netty.tcp.port=$port
      |""".stripMargin)
  val clientActorSystem: ActorSystem = ActorSystem("Client1", config)
  val clientActorRef: ActorRef = clientActorSystem.actorOf(Props(new CustomerClient(serverHost,serverPort,serverActorSystem,serverActorName)), "ClientActor1")
  clientActorRef ! "start"
  var isStop = false
  while (!isStop){
    val msg = StdIn.readLine()
    clientActorRef ! msg
    if(msg == "exit"){
      isStop = true
    }
  }

}
