package com.tom.yang.design

object WordCountDemo {
  def main(args: Array[String]): Unit = {
    val lines = Array[String]("Hello Tom","Hello Spark","Hello Spark","Spark Flink Scala")
    val wordCount = lines.flatMap(_.split(" ")).map((_, 1)).groupBy(_._1).map(t => (t._1, t._2.size)).toList.sortBy(t => t._2).reverse
    println(wordCount)
  }
}

