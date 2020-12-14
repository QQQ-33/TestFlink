package com.tom.yang.collection

/**
 * Stream 流
 * stream 是一个集合，可以存放无限多的元素，但这些元素不会一次性产生出来，而是需要用到多大的区间就动态的产生，末尾的元素遵循 lazy 的规则
 */
object StreamDemo {
  def main(args: Array[String]): Unit = {
    val s1 = numStream(1)
    println(s1)
    println(s1.head)
    println(s1.tail) // 对流执行 tail 操作，会生成新的数据
    println(s1)

    // 所有集合的变换操作都可以在流上应用
    // 主要应用是产生无穷多的元素
  }

  def numStream(n: BigInt): Stream[BigInt] = n #:: numStream(n + 1)
}
