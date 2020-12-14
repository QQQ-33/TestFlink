package com.tom.yang.project.akka.actor

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import scala.util.Random

object ActorGame {
  def main(args: Array[String]): Unit = {
    val actorFactory = ActorSystem("actorFactory")
    val bActorRef = actorFactory.actorOf(Props[BActor],"bActor")
    val aActorRef = actorFactory.actorOf(Props(new AActor(bActorRef)), "aActor")
    aActorRef ! "start"
  }
}

/**
 * 获取 ActorRef 的方式
 * 1. 构建 Actor 时传入 ActorRef
 * 2. 同过 sender() 方法获取消息发送方
 * @param actorRef
 */
class AActor(actorRef: ActorRef) extends Actor {
  val bActorRef: ActorRef = actorRef
  var num: Int = 1
  override def receive: Receive = {
    case "start" => {
      println("战斗开始...")
      num = 1
      self ! "fight"
    }
    case "fight" => {
      // 给 BActor 发送消息，这里需要获取 BActorRef
      if(num > 10){
        println(s"AActor(黄飞鸿) 打的不错，甘拜下风")
        self ! "exit"
      } else {
        println(s"AActor(黄飞鸿) 看我佛山无影脚 第 $num 脚")
        Thread.sleep(new Random().nextInt(1000))
        num += 1
        bActorRef ! "fight"
      }
    }
    case "exit" => {
      context.stop(self)
      context.system.terminate()
    }
  }
}

class BActor extends Actor {
  var num: Int = 1
  override def receive: Receive = {
    case "start" => {
      println("战斗开始...")
      num = 1
      self ! "fight"
    }
    case "fight" => {
      if(num > 10){
        println(s"BActor(乔峰) 打的不错，甘拜下风")
        self ! "exit"
      } else {
        println(s"BActor(乔峰) 看我降龙十八掌 第 $num 掌")
        Thread.sleep(new Random().nextInt(1000))
        num += 1
        // 这里通过 sender() 获取消息发送方的 ActorRef
        sender() ! "fight"
      }
    }
    case "exit" => {
      context.stop(self)
      context.system.terminate()
    }
  }
}
