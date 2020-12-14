package com.tom.yang.highfunction

/**
 * 高阶函数
 * 可以传入一个函数当作参数的函数称作高阶函数
 * 高阶函数也可以返回一个函数作为返回值
 */
object HigherFunctionDemo {
  def main(args: Array[String]): Unit = {
    def minus(a: Int) = {
      (b: Int) => a - b
    }
    val f1 = minus(3) // f1 相当于 (b: Int) => 3 - b
    println(f1)
    println(f1(2))
  }
}
