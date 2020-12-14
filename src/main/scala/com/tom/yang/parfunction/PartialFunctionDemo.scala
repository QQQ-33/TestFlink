package com.tom.yang.parfunction

/**
 * 偏函数
 * 对符合某个条件，而不是所有情况进行逻辑操作时，适合使用偏函数
 * 将包在大括号内的一组 case 语句封装为函数，称作偏函数。
 * 偏函数是 Scala 中的一个特质 PartialFunction
 */
object PartialFunctionDemo {
  def main(args: Array[String]): Unit = {
    // 需求，将所有数字元素 + 1 返回新集合
    // 方案 1 应用了大量类型判断和类型转换
    val list = List(1, 2, 3, 4, "aa")
    val res = list.filter(i => i.isInstanceOf[Int]).map(i => i.asInstanceOf[Int] + 1)
    println(res)
    // 方案 2 利用类型匹配，但是结尾有一个 Unit 空值
    val res2 = list.map( (i: Any) => {
      i match {
        case x: Int => x + 1
        case _ =>
      }
    })
    println(res2)
    // 方案 3 偏函数
    // 定义一个偏函数需要继承或实现 PartialFunction
    // 偏函数的执行流程分为二步：
    // 1. 使用 isDefinedAt 进行过滤
    // 2. 对满足条件的元素调用 apply
    // 对比一个 Function 接收某一类型的元素，并对所有元素进行操作，
    // PartialFunction 接收某一类型的元素，但并不一定对所有元素都执行操作
    // PartialFunction[-A, +B] 该偏函数的入参为 Any及其子类 出参为 Int及其超类
    val fun = new PartialFunction[Any, Int] {
      // isDefinedAt 用于对元素做判断
      override def isDefinedAt(x: Any): Boolean = x.isInstanceOf[Int]

      // apply 对于满足条件的元素做操作
      override def apply(v1: Any): Int = v1.asInstanceOf[Int] + 1
    }
    // 注意 map 不支持偏函数，这里需要使用 collect
    val res3 = list.collect(fun)
    println(res3)

    // 方案 4 简化的偏函数
    // 上面的偏函数可以用 case 写成简化形式
    def fun2: PartialFunction[Any, Int] = {
      case x: Int => x + 1
    }
    val res4 = list.collect(fun2)
    println(res4)

    // 方案 5 进一步简化偏函数, 注意 collect 后边接{} 也就是可以将偏函数的函数体直接写在这
    println(list.collect{case i: Int => i + 1})
  }
}
