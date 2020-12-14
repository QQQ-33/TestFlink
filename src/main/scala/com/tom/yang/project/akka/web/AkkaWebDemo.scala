package com.tom.yang.project.akka.web

class AkkaWebDemo {
  def main(args: Array[String]): Unit = {

  }
}

/**
 * Akka 网络编程
 * 案例：
 *    服务器端监听 9999 端口
 *    客户端可以通过键盘输入，发送消息给服务端
 *    服务端发送响应的消息给对应的客户端
 * 架构：
 *    服务器 127.0.0.1:9999
 *    客户1 127.0.0.1:9998
 *    客户2 127.0.0.1:9997
 *    客户3 127.0.0.1:9996
 *
 * 协议：
 *    通常 Scala 中网络通信的协议使用 case class 来实现
 */
