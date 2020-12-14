package com.tom.yang.collection

/**
 * Scala 的 List 是一个 object 可以直接存放数据，它属于 Seq， 默认是不可变的
 */
object ListDemo {
  def main(args: Array[String]): Unit = {
    // 默认情况下，使用的是 scala.collection.immutable.List，且它没有可变的List
    // ListBuffer 是可变的 List
    val list1 = List(1, 2, 3)
    println(list1)
    // Nil 表示空集合，经常使用，本身也是个集合，只是内容是空的
    val list2 = Nil
    println(list2)

    // 获取 list 的元素，同数组相同，索引从 0 开始
    println(list1(1))

    // 向 List 中追加元素，会返回一个新的 List， 原 list 没有变化
    // : 对着list + 对着元素
    val list3 = list1 :+ 4 // 后追加
    println(s"list3: ${list3}")
    val list4 = 4 +: list1 // 前追加
    println(s"list4: ${list4}")

    /**
     * :: 符号
     * 将元素放入一个集合并返回新的集合,
     * 运算的顺序是从右向左，所以最右侧一定要是一个集合元素
     *  :+ 和 +: 底层调用的也是 ::
     */
    val list5 = 4 :: 5 :: list1 :: 6 :: Nil
    println(s"list5: ${list5}")

    /**
     * ::: 符号
     * 将 list 的所有元素放入一个集合并返回
     * 注意运算的顺序是从右向左，所以 ::: 要放在被展开的 list 右侧
     */
    val list6 = 4 :: list5 ::: 6 :: list1 ::: Nil
    println(s"list6: ${list6}")

  }
}
object ListBufferDemo {
  def main(args: Array[String]): Unit = {
    /**
     * ListBuffer 属于 Seq 下的 Buffer 下
     * 可变的 List
     */
    import scala.collection.mutable.ListBuffer
    val lb1 = ListBuffer(1, 2, 3)
    println(lb1(1))
    for(i <- lb1){
      print(s"$i ")
    }
    println()

    // 追加元素，注意这里没有产生新集合
    val lb2 = new ListBuffer[Int]
    lb2 += 4
    lb2.append(5, 6)
    println(lb2)

    // 追加一个 list
    lb1 ++= lb2
    println(s"lb1: ${lb1}")
    println(s"lb1: ${lb2}")

    // 结合两个 list 产生新的 list
    val lb3 = lb1 ++ lb2
    println(s"lb3: ${lb3}")

    // 产生新集合，lb1 没有变化
    val lb4 = lb1 :+ 5
    println(s"lb4: ${lb4}")

    // 删除元素, 索引从 0 开始
    lb4.remove(1)
    println(s"lb4: ${lb4}")

  }
}
