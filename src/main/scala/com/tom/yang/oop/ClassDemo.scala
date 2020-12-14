package com.tom.yang.oop

/**
 * object 编译后生成两个.class 文件一个 ClassDemo$.class 一个 ClassDemo.class
 */
object ClassDemo {
  def main(args: Array[String]): Unit = {
    val cat = new Cat();
    // 设定属性时，其实是在调用 setter 方法
//    cat.name = "小白"
//    cat.age = 18
//    cat.color = "white"
    // 访问属性时，其实是在调用 getter 方法
//    println(cat.name)
//    println(cat.age)
//    println(cat.color)
    println(cat.value)
    cat.value = 123
    println(cat.value)
  }
}
// class 无法声明为 public 的，默认就是 public 的
// 一个.scala 中可以包含多个 class
class Cat {
  // 类的属性，默认为 private
  // 对于 var 同时生成 setter & getter
  // 对于 val 只生成 getter
  // 查看编译后的 .class 文件可以发现就是 getter 和 setter 和 private 的属性

  var name: String = "" // 必须要赋予初始值
  var age: Int = _ // _ 表示默认值，对应各个类型的默认值
  var color: String = _ // 默认值 null
  /* 重写 setter 和 getter 注意 setter 的特殊语法 */
  var _value: Int = _
  def value: Int = _value

  def value_= (value: Int): Unit = {
    _value = value
  }
}
