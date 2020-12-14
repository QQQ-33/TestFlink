package com.tom.yang.scalafor
/**
 * for 循环
 */
object ForDemo {
  def main(args: Array[String]): Unit = {
/*======================================*/
    // start to end 双闭区间
//    for(i <- 1 to 10){
//      println(s"to ${i}")
//    }
    // 遍历集合或数组
//    val list = List(1, 2.0, "hello");
//    for(item <- list){
//      println(item)
//    }
/*======================================*/
    // start until end 左闭右开区间
//    for (i <- 1 until 10){
//      println(s"until ${i}")
//    }
/*======================================*/
    // 循环守卫 对循环进行条件判断，注意条件判断前没有分号
    // 代替 continue => 条件判断为true 则执行，false 则 continue
//    for(i <- 1 to 10 if i % 2 == 0){
//      println(s"if ${i}")
//    }
/*======================================*/
    // 引入变量, 不同表达式注意用分号区分
//    for(i <- 1 to 3; j = 4 - i){
//      println(s"i: ${i} j: ${j}")
//    }
/*======================================*/
    // 嵌套循环
//    for(i <- 1 to 3; j <- 1 to 3){ // 嵌套循环的简写
//      println(s"i: ${i} j: ${j}")
//    }
    // 等同于
//    for (i <- 1 to 3){
//      for (j <- 1 to 3){
//        println(s"i: ${i} j: ${j}")
//      }
//    }
/*======================================*/
    // yield 循环返回值 => 将集合内的元素进行处理后返回给新的集合
    // 利用循环返回一个新的序列 Seq[T]
    // 每次循环必然返回一个值，没有返回值则返回()
//    val res = for(i <- 1 to 10) yield {
//      if(i % 2 == 0) i
//    }
//    println(res.isInstanceOf[Seq[Int]])
//    println(res.toBuffer)
/*======================================*/
    // 循环步长的控制
    // 源码: def apply(start: Int, end: Int, step: Int): Range = new Range(start, end, step)
//    for (i <- Range(1, 10, 2)){
//      println(s"Range ${i}")
//    }
//    for (i <- 1 to 10 if i % 2 == 1){
//      println(s"if ${i}")
//    }
/*======================================*/
    // 小练习
//    var count = 0
//    var sum = 0
//    for (i <- 1 to 100 if i % 9 == 0){
////      println(s"9 times ${i}")
//      count += 1
//      sum += i
//    }
//    println(s"count = ${count} sum = ${sum}")
//    for(i <- 0 to 6; j = 6 - i){
//      println(s"${i} + ${j} = ${i + j}")
//    }
  }
}
