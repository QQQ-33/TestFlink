package com.tom.yang.highfunction

object ParameterInfer {
  def main(args: Array[String]): Unit = {
    /**
     * 参数的类型推断
     * 1. 参数的类型可以省略
     * 2. 参数列表只有一个参数时，()可以省略
     * 3. 变量在 => 右边只出现一次时，可以用 _ 代替
     */
    val list = List(1, 2, 3, 4, 5)
    // 逐步简化
    println(list.map((x: Int) => x + 1))
    println(list.map((x) => x + 1))
    println(list.map(x => x + 1))
    println(list.map(_ + 1))

    //
    println(list.reduce((n1: Int, n2: Int) => n1 + n2))
    println(list.reduce((n1, n2) => n1 + n2))
    println(list.reduce(_ + _))
  }
}
