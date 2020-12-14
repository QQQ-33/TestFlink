package com.tom.yang.collection

import scala.collection.mutable

/**
 * Scala 的 Map 有 mutable 和 immutable 两大类
 * 不可变的Map是有序的，Map 中的元素底层是 Tuple2
 */
object MapDemo {
  def main(args: Array[String]): Unit = {
    /**
     * 不可变的 Map
     * 1. 默认创建的是不可变的 Map
     * 2. K/V 的类型可以是任意类型
     * 3. 每个键值对在底层其实是 Tuple2
     * 4. 元素顺序同声明的顺序一致
     */
    val map = Map("a" -> 10, "b" -> 20, "c" -> 30)
    println(s"map1 ${map}")
    /**
     * 可变的 Map
     * 需要 import
     * 元素是无序的
     */
    import scala.collection.mutable.Map
    val map2 = Map("a" -> 10, "b" -> 20, "c" -> 30)
    println(s"map2 ${map2}")

    // 空 Map
    val map3 = new mutable.HashMap[String, Int]()
    println(s"map3 ${map3}")

    // 以对偶元组的方式创建
    val map4 = Map(("a", 1), ("b", 2), ("c", 3))
    println(s"map4 ${map4}")

    // 获取元素， 同 Array 和 List 一样
    println(map4("a")) // 1
//    println(map4("aaaa")) // NoSuchElementException

    // get() 方法，返回一个 Some 或者 None 对象, 需要进一步的操作
    println(map4.get("b"))
    println(map4("b")) // 需要再调用 get 来获取真正的值
    println(map4.get("aaa")) // None 对象不能 get

    // 先判断再取值，避免抛出异常
    if(map4.contains("aaa")){
      println(s"get key aaa : ${map4("aaa")}")
    } else {
      println(s"no key aaa")
    }

    // 一步到位，有就返回值，没有就返回默认值，注意默认值的类型要匹配泛型
    println("getOrElse=========")
    println(map4.getOrElse("a",0)) // 有就正常返回AD
    println(map4.getOrElse("aaa",0)) // 没有就给默认值

    // 新增和修改，有就修改，没有就插入
    // 只有 mutable 的 Map 才能修改值
    map4("a") = 11
    map4("aa") = 12
    println(map4)
    // 同直接赋值一样
    map4 += ("d" -> 4)
    map4 += ("a" -> 13)
    println(map4)

    // 删除元素
    map4 -= ("d")
    map4 -= ("e") // 即使 key 没有也不会报错
    println(map4)

    // 遍历 map
    println("-------- (k, v) -------")
    for((k, v) <- map4){
      println(s"key: ${k} value: ${v}")
    }
    println("-------- keys -------")
    for(k <- map4.keys){
      println(s"key: ${k} value: ${map4(k)}")
    }
    println("-------- values -------")
    for(v <- map4.values){
      println(s" value: ${v}")
    }
    println("-------- tuple2 -------")
    for(t <- map4){
      println(s"key: ${t._1} value: ${t._2}")
    }
  }
}

