package com.tom.yang.impl

/**
 * 隐式值
 * 隐式变量，关键字 implicit
 * 将变量标记为隐式值，调用方法时如果省略了某些参数，编译器会在作用域内搜索隐式值作为缺省参数
 */
object ImplicitValueDemo {
  def main(args: Array[String]): Unit = {
    // 首先要定义隐式变量
    // 同一作用域内，要保证隐式值的识别性唯一
    implicit val str: String = "Tom"
    // 方法中也要标明隐式变量
    // 隐式参数只能在第一个位置上
    def hello(implicit name: String): Unit = {
      println(s"${name} hello")
    }
    // 此时调用方法可以不传参数，编译器会从作用域查找隐式值
    // 底层依然是编译器帮我们把隐式值带入方法进行了调用
    hello

    // 隐式值的优先级比默认值高，因为编译器会帮我们用隐式值调用方法
    def hello2(implicit name: String = "Default"): Unit = {
      println(s"hello ${name}")
    }
    hello2
    implicit val age: Int = 18
    // 当没有隐式值时，默认值依然可以生效
    def hello3(implicit name: String = "Default"): Unit = {
      println(s"hello ${name}")
    }
    hello3

    // 优先级: 传值 -> 隐式值 -> 默认值
  }
}


