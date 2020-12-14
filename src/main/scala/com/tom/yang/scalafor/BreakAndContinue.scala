package com.tom.yang.scalafor
import util.control.Breaks._
object BreakAndContinue {
  def main(args: Array[String]): Unit = {
    // scala 去除了 break 和 continue 关键字，用 break() 方法替代
    // 注意 import util.control.Breaks._
    var num = 1

    /**
     * breakable 高阶函数
     * 源码: 帮我们处理了 break() 抛出的异常
     * try {
     *  op
     * } catch {
     *  case ex: BreakControl =>
     *  if (ex ne breakException) throw ex
     * }
     */
    breakable {
      while (num <= 20){
        num += 1
        if(num == 15){
          // 源码: def break(): Nothing = { throw breakException }
          // 使用抛异常的方式来中断循环
          break()
        }
      }
    }
    // 由于有 breakbale 所以 break() 抛出的异常没有中断整个程序
    println(s"break ${num}")
    // continue 用循环守卫或者 if 判断来实现
  }
}
