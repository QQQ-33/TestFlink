package com.tom.yang.collection

import org.apache.commons.collections.list.SynchronizedList

/**
 * 不可变的集合，本身是线程安全的
 * 对于可变的集合，可以使用 Synchronize 开头的集合
 */
object ThreadSafeCollectionDemo {
  def main(args: Array[String]): Unit = {
//    val sf = new SynchronizedList(1,2,3)
  }
}

/**
 * 并行集合，区别于串行集合，充分利用多核 CPU 的计算资源
 * Scala 在并行集合中应用的主要算法：
 *  Divide and conquer: 分治算法， 通过 splitter（分解器）， combiners（组合器）等抽象层来实现，
 *    主要原理是将计算工作分解为很多任务，分发给一些处理器去完成，并将他们的处理结果合并返回
 *  Work stealin 算法: 任务调度的负载均衡
 */
object ParallelCollectionDemo {
  def main(args: Array[String]): Unit = {
    // 开启并行只需要 集合.par
    println("串行")
    (1 to 10).foreach( i => print(Thread.currentThread.getName + s" $i "))
    println()
    println("并行")
    (1 to 10).par.foreach( i => println(Thread.currentThread.getName + s" $i "))
    /**
     * 通过观察结果可以发现，串行集合是通过 main 线程进行处理的，且处理是有序的
     * 并行集合是通过 scala-execution 创建的线程去处理，且返回的结果是无序的
     */
    val res = (1 to 100).map(_ => Thread.currentThread.getName).distinct
    val res2 = (1 to 100).par.map(_ => Thread.currentThread.getName).distinct
    println(res) // main
    println(res2) // 8个不同的线程，根据CPU 的核数来分配最适当的线程数
  }
}
