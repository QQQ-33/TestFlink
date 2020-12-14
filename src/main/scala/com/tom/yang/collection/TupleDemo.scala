package com.tom.yang.collection

/**
 * 元组
 * 可以存放不同类型数据的容器，将多个无关数据封装为一个整体，一个元组最多只能有 22 个元素
 * 方便组织松散的数据
 */
object TupleDemo {
  def main(args: Array[String]): Unit = {
    // 元组的创建只需要简单的将数据括起来
    // 元组的类型会根据元组内的元素个数自动确定，Tuple1 -- Tuple22
    val tuple1 = (1, "2", 3.0, 4)// Tuple4
    println(tuple1)

    // 元组的访问，下标从 1 开始
    println(tuple1._1)

    // 元组的遍历，不能直接遍历，需要提供一个 iterator
    // productIterator
    for(item <- tuple1.productIterator){
      println(s"item: ${item}")
    }

  }
}
