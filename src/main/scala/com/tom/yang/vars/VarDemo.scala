package com.tom.yang.vars

import scala.io.StdIn

object VarDemo {
  def main(args: Array[String]): Unit = {
//    val num = 2
//    println(num.isInstanceOf[Int])

    /**
     * var val
     * 通常获取/创建一个对象后，读取/修改对象的属性，很少改变对象本身
     * val 没有线程安全问题，效率更高， 能使用 val 时尽量使用 val
     */
    /**
     * Unit 等于 void 唯一的实例值 ()
     * Null 等于 null 唯一的实例值 null 只能赋值给 AnyRef 及其子类
     * Nothing 常用于返回异常的方法或函数的返回值类型
     */
//    println(3 - math.pow(math.sqrt(3), 2))
      val s: String = "hello"
//    println(s.charAt(0))
//    println(s.charAt(s.length - 1))
    /**
     * 类型转换
     * 使用基本类型的转换方法
     * 将运算的结果赋值给变量，编译器会考察值的范围以及类型
     * 将一个值直接赋值给变量，编译器只考察值的范围
     * val c: Char = 'a' + 1
     * val c1: Char = 97 + 1
     * val c2: Char = 98
     * val c3: Char = 99999
     *
     * 低精度可以赋值给高精度，高精度需要强转为低精度
     */
//    val num: Int = 2.2.toInt
//    println(num)
    /**
     * 用户输入
     */
      println("please write your name:")
      val name = StdIn.readLine();
      println(name)

  }

}
