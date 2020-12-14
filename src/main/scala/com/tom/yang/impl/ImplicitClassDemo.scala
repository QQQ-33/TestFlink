package com.tom.yang.impl

/**
 * 隐式类
 * 隐式类也可以扩展类库，比隐式方法扩展类库更加方便，在集合中发挥了重要作用
 * 1. 类的构造参数只能有一个
 * 2. 隐式类只能定义在 class object package 中，本身不能是顶级的对象
 * 3. case class 不能是隐式类
 * 4. 同一作用域内不能重名
 */
object ImplicitClassDemo {
  def main(args: Array[String]): Unit = {
    /**
     * 隐式类由关键字 implicit 声明
     * 它的构造函数只能有一个参数
     * 当它出现在作用域中时，编译器会自动将 Class4 转为 Class3 来扩展 Class4
     * 底层是用 Class4 构建一个 Class3
     */
    implicit class Class3(val c: Class4) {
      def addSuffix(): String ={
        s"implicit Class3"
      }
    }
    val c = new Class4
    c.say()
    println(c.addSuffix())
  }
}
class Class4 {
  def say(): Unit = {
    println("Class4 say")
  }
}

/**
 * 隐式转换发生的时机：
 * 1. 当方法中的参数的类型和调用时的参数类型不匹配
 * 2. 对象调用的方法或成员不存在于当前类型中
 *
 * 隐式解析的机制：
 * 1. 首先在当前作用域下查找隐式的实体： 隐式值  隐式方法  隐式类
 * 2. 当前作用域没有，会继续在隐式参数的类型的作用域查找。类型的作用域指与该类型相关的全部伴生模块
 *
 * 隐式转换要保证识别的唯一性
 * 隐式转换不能发生在其本身
 */
