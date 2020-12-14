package com.tom.yang.highfunction

/**
 * 函数柯里化
 * 1. 接收多个参数的函数，可以转化为接收单个参数的函数，这个转化的过程叫做柯里化
 * 2. 柯里化证明了函数其实只需要一个参数
 * 3. 柯里化也就是消元求解的过程
 */
object CurryFunctionDemo {
  def main(args: Array[String]): Unit = {
    // 函数可以有多个参数列表
    def mulCurry(a: Int)(b: Int) = a * b
    println(mulCurry(3)(8))
    // 柯里化的思想，一个函数处理一件事
    // 例如： 比较两个字串，忽略大小写，是否相等
    // 划分为两步：
    // 1. 全部转大写
    // 2. 比较是否相等

    // 简略的写法，但是没有体现柯里化的思想
    def isEqual(s1: String)(s2: String): Boolean = {
      s1.toLowerCase == s2.toLowerCase
    }

    // 下面的函数，体现了柯里化的思想，一个函数完成一个功能
    def isStringEqual(s1: String, s2: String): Boolean = {
      s1.equals(s2)
    }
    // 这里用了个隐式类，对 String 类型做了扩展
    implicit class CheckStringIgnoreCase(s: String){
      def checkString(ss: String)(f: (String, String) => Boolean): Boolean = {
        f(s.toLowerCase(), ss.toLowerCase())
      }
    }
    val s = "Tom"
    println(s.checkString("Bob")(isStringEqual))
    println(s.checkString("toM")(isStringEqual))

    // 使用匿名函数，简化 isStringEqual 的声明, 注意 _ 只能逐一替代 => 左边的参数，每个 _ 代表一个参数
    println(s.checkString("TOM")((s,ss) => s.equals(ss)))
    println(s.checkString("tom")(_.equals(_)))

  }
}
