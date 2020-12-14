package com.tom.yang.collection

/**
 * 操作符
 */
object OperatorDemo {
  def main(args: Array[String]): Unit = {
    // 关键字变量名需要用 `` 括住
    val `val` = 1
    // 操作符重载， A 操作符 B 等价于 A.操作符B
    val a = 1
    val b = 1
    val r = a + b // 中置操作符的方式
    val r2 = a.+(b) // .+ 的方式相当于调用方法

    val op = new OperatorRedef
    op + 1 // 中置操作符
    op.+(1) // 同一个操作符，自动重载，两种方式调用
    println(op.num)
    op++  // 后置操作符
    op.++()
    println(op.num)
    !op // 前置操作符
    println(op.num)
  }
}

/**
 * 手写操作符
 */
class OperatorRedef {
  var num: Int = 0
  // 操作符重载（中置操作符）， 看上面的调用方式
  def +(n: Int): Unit = {
    this.num += n
  }
  // 操作符重载（后置操作符），参数列表指明不需要传参
  def ++(unit: Unit): Unit = {
    this.num += 1
  }
  // 操作符重载（前置操作符），unary_ 是关键字，方法名是固定的，后边有 + - ! ~ 四个符号可选
  def unary_!(): Unit = {
    this.num = -this.num
  }

}
