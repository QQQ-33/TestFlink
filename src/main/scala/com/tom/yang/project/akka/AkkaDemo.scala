package com.tom.yang.project.akka

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

/**
 * Akka 是 JVM 上构建高并发，分布式和容错应用的的框架
 * Akka 由 scala 语言写成，提供了 scala 和 Java 的开发接口
 * Akka 主要提供了可以轻松写出高效稳定的并发程序，不用过多考虑线程、锁和资源的竞争
 *
 * Akka 基于 Actor 模型
 * 在 Actor 系统中，所有事物都是 Actor ，Actor 与 Actor 之间只能通过 message 通信
 * 一个 Actor 给另一个 Actor 发送的消息是有顺序的
 * 怎么处理消息是由接收方的 Actor 决定的，发送方的 Actor 可以同步或者异步
 * ActorSystem 负责创建 Actor，它是单例的
 * Actor 模型是 异步，非阻塞，高性能的事件驱动模型，且非常轻量化，1GB 内存就可以容纳百万个 Actor
 *
 * ActorSystem 创建 Actor
 * ActorRef 是 Actor 的代理或者引用，消息需要通过 ActorRef 来发送，不能直接通过 Actor 发送，用哪个 ActorRef 发送消息就表示要把消息发给谁
 * 发出的消息进入 Dispatcher Message 消息分发器（相当于线程池）它将消息分发至对应 Actor 的 Mailbox
 * Mailbox 相当于消息队列 FIFO
 * Actor 通过 receive 方法获取消息
 * 每一个消息就是一个 Message 对象，它继承了 Runnable
 * 整个模型只需要专注 Actor 的开发即可
 */
object AkkaDemo {
  def main(args: Array[String]): Unit = {

  }
}

/**
 * 使用 Akka 需要引入 Akka 的依赖，注意对应 scala 版本
 * Akka 简单例子
 * SayHelloActor 自己给自己发送消息
 */
/**
 * 1. extends Actor 的类就是一个 Actor 需要重写 receive 方法
 * 2. receive 会被这个 Actor 的 Mailbox 调用
 * 3. receive 是一个偏函数  type Receive = PartialFunction[Any, Unit]
 */
class SayHelloActor extends Actor {
  /**
   * 回忆下偏函数的写法
   * 1. isDefinedAt 用于判断
   * 2. apply 用于执行
   * 3. 上述两步可以用 case 进行简化， 整个偏函数相当于一个 match
   * @return
   */
  override def receive: Receive = {
    case "Hello" => println("receive Hello, Hello too :)")
    case "ok" => println("receive ok, ok too :)")
//    case "exit" => {
//      println("receive exit")
//      context.stop(self) // 停止当前 Actor
//      context.system.terminate() // 停止 ActorSystem
//    }
    case _ => println("Nothing received")
  }
}
object SayHelloActor {
  // 1. 创建 ActorSystem, 名字随便取
  private val actorFactory = ActorSystem("actorFactory")
  // 2. 创建 Actor 的同时，返回 ActorRef
  //  Props[SayHelloActor] 创建了一个 SayHelloActor 实例，底层使用的反射
  //  sayHelloActor 是 Actor 的名字，每个 Actor 的名字不要重复
  //  返回的 sayHelloActorRef 即是 SayHelloActor 的引用
  private val sayHelloActorRef: ActorRef = actorFactory.actorOf(Props[SayHelloActor], "sayHelloActor")

  def main(args: Array[String]): Unit = {
    // 给自己发送消息 , ! 即为发送消息
    sayHelloActorRef ! "Hello"
    sayHelloActorRef ! "ok"

    // 退出 ActorSystem
    sayHelloActorRef ! "exit"
  }
}
