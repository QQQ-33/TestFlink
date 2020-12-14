package com.tom.yang.highfunction

/**
 * 闭包
 * 闭包就是一个函数，和与其相关的引用环境组合的一个整体
 */
object ClosureDemo {
  def main(args: Array[String]): Unit = {
    def minus(x: Int) = (y: Int) => x - y
    // fun 就是一个闭包。它是一个函数，但是它绑定了 20 这个外部传入的参数
    val fun = minus(20)
    // 当调用 fun 时，20这个参数的影响总是存在，它已经是fun函数的一部分
    println(fun(8))

    /**
     * 练习：
     * 1. 写一个函数，makeSuffix(suffix: String) 接收一个后缀名(例如.jpg .csv)，返回一个闭包
     * 2. 调用闭包，传入文件名，如果没有指定的后缀，返回文件名.suffix 否在只返回文件名
     */
    def makeSuffix(suffix: String) = {
      (fileName: String) => {
        if(fileName.endsWith(suffix)){
          fileName
        } else {
          fileName + suffix
        }
      }
    }
    val f = makeSuffix(".csv")
    println(f("FC"))
  }


}
