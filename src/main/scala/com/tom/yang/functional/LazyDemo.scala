package com.tom.yang.functional
/**
 * 惰性计算
 * 尽可能延迟表达式的求值，将耗时的计算推迟到绝对需要的是时候，提高代码效率
 *
 * 在定义变量时如果使用了 lazy， 那么这个变量的值将会在用到时才会赋值
 * 如果是方法的返回值，那么将延迟方法的调用
 * lazy 不能修饰 var 变量
 */
object LazyDemo {
  def main(args: Array[String]): Unit = {
//    lazy val res = 15;
    // 声明为 lazy 的函数调用，在没有用到这个值时，不会调用函数
//    lazy val  res = sum(7, 8);
//    println(res)
    // 没有声明为 lazy 的函数调用，即使没用到这个值，也会调用函数
//    val res = sum(7, 8);
//    println(res)
  }

  def sum(n1: Int, n2: Int): Int = {
    println("sum 方法调用了")
    n1 + n2
  }
}
