package com.tom.yang.homework
import scala.collection.mutable.ListBuffer
import scala.math._


object HomeWork {
  def main(args: Array[String]): Unit = {
//    val h = compose(f, g)
//    println(h(2))
//    println(h(0))
//    println(h(1))
//    println(values(x => x * x, -5, 5))
//    val arr =Array[Int](1, 333, 4, 6, 5, 9, 32, 0, 2)
//    println(arr.reduceLeft(findMax))
//    println(factorial(9))

  }
  // 高阶函数，给定两个函数，合并成一个函数
  def f(x: Double) = if(x > 0) Some(sqrt(x)) else None
  def g(x: Double) = if(x != 1) Some(1 / (x - 1)) else None
  def compose(f1: Double => Option[Double], f2: Double => Option[Double]): Double => Option[Double] = {
    (x: Double) => {
      if(f1(x) == None || f2(x) == None){
        None
      } else {
        f2(x)
      }
    }
  }
  // 编写函数，输出集合，集合包含给定区间内函数的输入和输出
  def values(fun: Int => Int, low: Int, high: Int): Seq[(Int, Int)] = {
    val list = new ListBuffer[(Int, Int)]
//    (low to high).foreach(x => list += new Tuple2[Int, Int](x, fun(x)))
    (low to high).foreach(x => list.append((x, fun(x))))
    list
  }
  // 利用 reduceLeft 得到数组最大元素
  def findMax(a: Int, b: Int): Int = {
    if(a > b) a else b
  }
  // 利用 reduceLeft 实现阶乘
  def factorial(n: Int): Int = {
    (1 to n).reduceLeft(_ * _)
  }
}
