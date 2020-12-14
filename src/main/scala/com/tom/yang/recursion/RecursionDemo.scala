package com.tom.yang.recursion

import java.util.Date

/**
 * 递归
 */
object RecursionDemo {
  def main(args: Array[String]): Unit = {
    /**
     * 执行效率比较
     */
    val startTime: Long = new Date().getTime()
    var num = BigInt(1)
    var res = BigInt(0)
    val max = BigInt(99999999l)
    while ( num < max){
      res += num
      num += 1
    }
    val endTime: Long = new Date().getTime()
    println(s"result: ${res} time: ${endTime - startTime}")

    val startTime2: Long = new Date().getTime()
    val res2 = getSum(BigInt(1), BigInt(0))
    val endTime2: Long = new Date().getTime()
    println(s"result2: ${res2} time: ${endTime2 - startTime2}")

    // 通过比较，递归和循环的效率相差不大
  }
  def getSum(num: BigInt, sum: BigInt): BigInt = {
    if(num < BigInt(99999999l)){
      getSum(num + 1, sum + num)
    } else {
      sum
    }
  }

  // 递归求最大值
  def getMax(list: List[Int]): Int = {
    if(list.isEmpty){
      throw new java.util.NoSuchElementException
    } else if(list.size == 1){
      list.head
    } else if(list.head > getMax(list.tail)){
      list.head
    } else {
      getMax(list.tail)
    }
  }

  // 递归反转字串
  def reverseString(s: String): String = {
    if(s.length == 1){
      s
    } else {
      reverseString(s.tail) + s.head
    }
  }
  // 递归求阶乘
  def factorial(num: BigInt): BigInt = {
    if(num == 0){
      1
    } else {
      factorial(num - 1) * num
    }
  }

  /**
   * 递归求斐波那契数列
   * 这里存在递归陷阱，在同一层递归，调用了两次递归计算
   * 在这种情况下，递归的调用次数会指数型增长
   */
  def fbn(n: BigInt): BigInt = {
    if(n == 1 || n == 2){
      1
    } else {
      fbn(n - 1) + fbn(n - 2) // 这里会造成递归调用次数的指数增长
    }
  }
}
