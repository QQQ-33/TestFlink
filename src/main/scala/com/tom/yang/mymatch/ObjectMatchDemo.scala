package com.tom.yang.mymatch

/**
 * 对象匹配
 * case 中对象的 unapply 方法（对象提取器）返回 Some 集合则为匹配成功
 * 返回 None 则为匹配失败
 */
object ObjectMatchDemo {
  def main(args: Array[String]): Unit = {
//    val num: Double = 9.0
    val num: Double = Square(5.0)
    num match {
      /**
       * 类似构建对象时直接使用 类名() 会调用 apply
       * 在 case 时直接使用 类名() 会调用 unapply
       * unapply 方法返回 Some 时表示匹配成功，并且会将这个值赋值给当前 case 的变量
       * unapply 方法返回 None 时表示没有匹配成功
       *
       */
      case Square(n) => println(n) // 会返回构建 Square 时传入的值
      case _ => println("nothing matched")
    }
    val names = "Allison,Tom,Bob"
    names match {
      /**
       * 这里会调用 unapplySeq 方法
       * 注意 case 接收返回值的变量的个数需要与 unapplySeq 返回的值的个数一致，否则不会匹配成功
       */
      case Names(first, second, third) => println(s"$first $second $third")
      case _ => println("nothing matched")
    }

    /**
     * 变量的匹配
     * case 语句中的匹配，都可以独立执行
     * 类似 JS 中的解构赋值
     */
    val (x, y) = (1, 2) // 将 1 赋值给 x， 2 赋值给y
    println(s"$x $y")
    val arr = Array(1, 2, 3)
    val Array(a, b, _*) = arr // 提取数组的前两个元素
    println(s"$a $b ")


  }
}

object Square {
  /**
   * unapply() 方法，同 apply() 类似，对象提取器
   * 可以自行实现如何拆解对象
   * 入参可以是任意类型，返回值必须是 Option
   * 利用 Some/None 判断是否匹配成功
   */

  def unapply(z: Double): Option[Double] = {
    println("unapply...")
    Some(math.sqrt(z))
  }
  def apply(z: Double): Double = {
    println("apply...")
    z * z
  }

}
object Names{
  /**
   * 返回多个返回值的 unapplySeq
   * 使用方式同 unapply 相同，区别是返回一个 Seq
   */
  def unapplySeq(str: String): Option[Seq[String]] = {
    println("unapplySeq...")
    if(str.contains(",")) {
      Some(str.split(","))
    } else {
      None
    }
  }
}
