package com.tom.yang.collection

import scala.collection.mutable

/**
 * set 不重复元素的集合
 * set 分为 mutable 和 immutable
 */
object SetDemo {
  def main(args: Array[String]): Unit = {
    val set1 = Set(1, 2, 3, 1)
    println(set1) // 重复的元素只能有一个
    /**
     * 可变的 Set 需要 import
     */
    import scala.collection.mutable.Set
    val set2 = mutable.Set(1, 2, 3, 1) // 元素是无序的
    println(set2)
    // 添加和删除
    set2 += 4
    set2 -= 1
    set2.add(5)
    set2.remove(0)
    println(set2)

    // 遍历
    for(i <- set2){
      println(i)
    }
  }
}
