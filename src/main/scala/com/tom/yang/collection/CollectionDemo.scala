package com.tom.yang.collection

import scala.collection.mutable.ArrayBuffer

/**
 * Scala 的集合
 * 不可变集合 scala.collection.immutable 可以安全的并发访问(限制了集合的变化)
 * 可变集合 scala.collection.mutable
 *
 * 默认采用不可变集合，并且通常情况应该优先使用不可变集合，几乎所有的集合都分为可不变和不可变
 * 集合分为三大类(均扩展自 Iterable)：
 * 序列 Seq  集合 Set  映射 Map
 *
 * 不可变集合：
 * 集合本身不能动态变化，也就是不能扩容，类似 Java 的数组，集合的内容可以变化
 *
 * 可变集合：
 * 集合本身可以动态变化，可以扩容，类似 Java 的 List
 *
 * 1. Set 和 Map 同 Java 中基本相同
 * 2. Seq 在 Java 中没有， List 属于 Seq 但这里同 Java 不是同一个概念
 * 3. 1 to 3 使用的是 Seq 下 IndexedSeq 的 Vector
 * 4. String 是属于 IndexedSeq 的
 * 5. Seq 下的 LinearSeq 包含了很多经典的数据结构，Queue Stack
 * 6. IndexedSeq 与 LinearSeq 的主要区别是:
 *  IndexedSeq 通过下标访问
 *  LinearSeq 有头有尾，通过遍历来查找元素
 * 7. 可变集合比不可变集合更加丰富，增加了 Buffer 集合，常用的有 ArrayBuffer ListBuffer，并且也提供了可变的线程安全的集合
 *
 */
object CollectionDemo {
  def main(args: Array[String]): Unit = {


  }
}

/**
 * 数组
 */
object ArrayDemo {
  def main(args: Array[String]): Unit = {
    // 不可变数组, 等同于 Java 的数组
    // 这里声明了一个数组，并且可以通过泛型控制类型，长度为10
    // 没有赋值的情况下，数据有默认值
    val arr1 = new Array[Int](10) // 底层 int[] arr1 = new int[10]
    // 数组的访问通过小括号，下标从 0 开始
    arr1(1) = 7
    print("arr1: ")
    for(i <- arr1) print(s"${i} ")
    println()

    // 通过 Array 的 apply 方法, 自动推断类型
    val arr2 = Array(1, 2, 3)
    print("arr2: ")
    for(i <- arr2) print(s"${i} ")
    println()

  }
}
object ArrayBufferDemo {
  def main(args: Array[String]): Unit = {
    // 可变数组, 需要 import scala.collection.mutable.ArrayBuffer
    val ab1 = ArrayBuffer[Int](1, 2, 2, 3)
    // 追加若干元素
    ab1.append(7) // append 在底层会重新分配空间进行扩容，内存地址会发生变化
    // java.lang.System.arraycopy(array, 0, newArray, 0, size0)
//    ab1.append(7, 6, 5)
    ab1 += 1 // 追加一个元素
    print("ab1: ")
    ab1.foreach( i => print(s"$i "))
    println()

    // 按照 index 移除元素
    ab1.remove(2)
//    ab1 -= 2 // 移除指定的元素，相同的元素全部移除
    print("ab1: ")
    ab1.foreach( i => print(s"$i "))
    println()

    // 修改指定 index 的元素
    ab1(0) = 6
    print("ab1: ")
    ab1.foreach( i => print(s"$i "))
    println()

    // 定长数组与变长数组互转
    val arr = Array[Int](1, 2)
    val ab = ArrayBuffer[Int](3, 4)
    // 转换不改变原数组，而是生成一个新数组
    val abt = arr.toBuffer // 定长转变长
    val arrt = ab.toArray // 变长转定长
  }
}
object MultiDimArray {
  def main(args: Array[String]): Unit = {
    // 多维数组
    // ofDim 表示是多维数组，第一维度长度 3，第二维度长度 4
    val am = Array.ofDim[Int](3, 4)
    am(0)(1) = 5
    // indices 返回一个 Range[0, length), 左闭右开
    for(i <- am.indices; j <- am(i).indices){
      print(s"${am(i)(j)} ")
      if(j == am(i).length - 1) println()
    }
//    for(i <- am){
//      for(j <- i){
//        print(s"$j ")
//      }
//      println()
//    }
  }
}


