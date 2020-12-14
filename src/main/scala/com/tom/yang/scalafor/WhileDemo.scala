package com.tom.yang.scalafor

import scala.io.StdIn

object WhileDemo {
  /**
   * 推荐使用 for 循环， 除非 for 循环搞不定
   */
  def main(args: Array[String]): Unit = {
//    var i = 0;
//    while (i < 10){
//      println(s"tom ${i + 1}")
//      i += 1
//    }
//    var tmpSum = 0.0;
//    var totalSum = 0.0;
//    for(i <- 1 to 3){
//      tmpSum = 0.0
//      for(j <- 1 to 5){
//        println(s"请输入 ${i} 班 ${j} 同学成绩")
//        tmpSum += StdIn.readDouble();
//      }
//      println(f"%d 班的平均分 = %.2f", i, tmpSum / 5)
//      totalSum += tmpSum
//    }
//    println(s"所有班级的平均分 = ${totalSum / 15}")
    for(i <- 1 to 9){
      for(j <- 1 to i){
        print(s"${j} x ${i} = ${i * j}\t")
      }
      println()
    }
  }
}
