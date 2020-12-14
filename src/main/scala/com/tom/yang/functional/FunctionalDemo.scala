package com.tom.yang.functional

import scala.collection.mutable
import scala.io.StdIn

object FunctionalDemo {
  def main(args: Array[String]): Unit = {
//    println(getResult(1, 2, '%'))
//    test(4)

//    for(i <- 1 to 6){
//      println(fabnaqie(i))
//    }
//    println(getPeach(10,1))
//    dynamicParams(1, 2, 3, 4)
    printAngle()
  }

  // 方法定义
  def getResult(n1: Int, n2: Int, oper: Char): Any = {
    oper match {
      case '+' => n1 + n2
      case '-' => n1 - n2
      case '*' => n1 * n2
      case '/' => n1 / n2
      case _ => null
    }
  }

  // 递归， 必须定义返回值类型，无法使用返回值类型推断
  def test(n: Int): Unit = {
    if(n > 2){
      test(n - 1)
    }
    println(n)
  }
  def fabnaqie(n: Int): Int = {
    if(n == 1 || n == 2){
      1
    } else {
      fabnaqie(n - 2) + fabnaqie(n - 1)
    }
  }
  def getPeach(n: Int, sum: Int): Int = {
    if(n < 2){
      sum
    } else {
      getPeach(n - 1, (sum + 1) * 2)
    }
  }

  // 如果方法参数列表后直接写大括号，表示方法没有返回值，相当于返回 Unit ，即使 return 也不生效
  def sum2(n1: Int, n2: Int){
    return n1 + n2 // 可以编译，但依然没有返回值
  }
  // 可变参数, 必须放在参数列表的最后
  // nums 放在 Seq[T] 中
  def dynamicParams(nums: Int*): Unit = {
    println(nums.isInstanceOf[Seq[Int]])
    for(i <- nums){
      println(i)
    }
  }

  /**
   * 练习题
   */
  def printAngle(): Unit = {
    var num: Int = 0
    do {
      println("请输入需要打印的行数, 必须为偶数")
      num = StdIn.readInt();
    } while (num % 2 != 0)

    for(i <- 1 to num){
      for(j <- 0 to num * 2 - 1){
        if(j > num - i && j < num + i ){
          print("*")
        } else {
          print(" ")
        }
      }
      println()
    }
  }

}

