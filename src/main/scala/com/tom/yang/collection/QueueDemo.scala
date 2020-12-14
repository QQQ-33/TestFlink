package com.tom.yang.collection

import scala.collection.mutable

/**
 * 队列
 * 队列是一个有序表，底层可以用数组或链表
 * 访问顺序，先入先出
 * 有 mutable 和 immutable Queue
 */
object QueueDemo {
  def main(args: Array[String]): Unit = {
    val q1 = new mutable.Queue[Int]
    println(q1)
    // 追加一个元素，元素可以重复
    q1 += 10
    println(s"q1: ${q1}")
    // 将 List 的所有元素加入 queue
    q1 ++= List(1, 2, 3)
    println(s"q1: ${q1}")

    // 可以像访问数组一样访问元素，但通常的用法是入队/出队， 注意顺序，先入先出
    // 出队， 从队列首拿出一个元素
    println(q1.dequeue())
    println(s"q1: ${q1}")
    // 入队， 在队列尾追加一个元素
    q1.enqueue(11, 12)
    println(s"q1: ${q1}")

    // 返回队列的元素，但是并不取出元素
    println(q1.head)
    println(q1.last)
    // 返回除第一个元素外剩余元素的队列
    println(q1.tail)
    println(q1.tail.tail)
    // 以上操作没有改变queue
    println(s"q1: ${q1}")
  }
}
