package com.tom.yang.collection

import scala.collection.mutable


/**
 * 集合的操作
 */
object CollectionOperation {
  def main(args: Array[String]): Unit = {
    /**
     * map 映射
     * 将集合中的每个元素应用一个操作，并返回一个新的集合
     * 1. 将集合的元素进行遍历
     * 2. 将遍历到的每个元素传递给 function 进行调用
     * 3. 将 function 返回的元素放入新的集合
     * 4. 返回新的集合
     */
    val list = List(1, 2, 3)
    // 自动将传入的方法转换为了函数
    val list2 = list.map(_ * 2)
    println(list)
    println(list2)

    /**
     * flatMap
     * 将集合中的元素扁平化，并返回新的集合
     */
    val list3 = List("Alice", "Bob", "Tom") // 别忘记 String 属于 Seq 的，也是集合
    val list4 = list3.flatMap(_.toUpperCase())
    println(list4) // List(A, L, I, C, E, B, O, B, T, O, M)

    /**
     * filter
     * 遍历集合，将符合条件（将元素带入传入的函数并返回true）的元素放入新集合并返回
     */
    val list5 = list3.filter(_.startsWith("A"))
    println(list5)

    /**
     * reduceLeft (reduce 底层调用的是 reduceLeft)
     * 化简操作，接受一个函数，
     * 这个函数需要两个参数，并返回一个结果
     * reduceLeft 会从第一个元素开始递归调用函数，并返回最终结果
     */
    val res = list.reduceLeft(_ + _) // 等同于 .sum
    println(res)

    /**
     * reduceRight
     * 与 reduceLeft 基本相同，但是运算的方向相反，注意递归的时候 第二个 参数是来自前一步的运算
     */
    val res1 = list.reduceRight(_ - _)
    println(res1) // 2 仔细想想怎递归调用的

    /**
     * foldLeft
     * 同 reduceLeft 基本相同
     * 将上一步函数的返回值作为下一步函数调用的参数
     * 区别是可以指定一个初始的集合（或元素）
     */
    val list6 = List(1, 2, 3, 4)
    val res2 = list6.foldLeft(5)(_ - _)
    // 同 reduceRight 一样，只不过可以指定初始值
    val res3 = list6.foldRight(5)(_ - _)
    println(res2)
    println(res3)
    // 折叠的缩写 /: :\ 原则是 :对着集合 /\对着元素
//    val res2 = (5 /: list6)(_ - _) // 等价于 list6.foldLeft(5)(_ - _)
//    val res3 = (list6 :\ 5)(_ - _) // 等价于 list6.foldRight(5)(_ - _)

    /**
     * scan
     * 扫描，对集合做 fold操作，但是所有中间结果会放在一个集合中返回
     * 可以用来观察运算过程
     */
    val res4 = (1 to 5).scanLeft(5)(_ + _) // 返回一个集合，集合内是运算的每一个中间结果
    println(res4)
    val res5 = (1 to 5).scanRight(5)(_ + _)
    println(res5)

    /**
     * zip
     * 拉链操作，将两个 List 合并为一个对偶元组
     * 两个集合的元素个数不对应会按照数量少的算
     */
    val list7 = List("a", "b", "c")
    val list8 = List(1, 2, 3)
    val res6: List[(String, Int)] = list7.zip(list8)
    println(res6)
  }


}
object TestOperation {
  def main(args: Array[String]): Unit = {
    // 将子串的字符打散放入 ArrayBuffer
    val str = "AAAAAAABBBBBBCCCCCDDDDDD"
    import scala.collection.mutable.ArrayBuffer
    val arr = new ArrayBuffer[Char]()
    val res = str.foldLeft(arr)( (arr, c) => {arr.append(c); arr})
    println(res)
    // 统计字母个数
    import scala.collection.mutable.Map
    val map = mutable.Map[Char, Int]()
    val res2 = str.foldLeft(map)((map, c) => {
      map += (c -> (map.getOrElse(c, 0) + 1))
      map
    })
    println(res2)

    val sentence = List("hello tom", "hello hadoop", "hello tom learn scala")
    val map2 = mutable.Map[String, Int]()
    val res3 = sentence.map(_.split(" ")).flatMap(s => s).foldLeft(map2)((m, s) => {
      m += (s -> (m.getOrElse(s, 0) + 1))
      m
    })
    println(res3)


  }
}
