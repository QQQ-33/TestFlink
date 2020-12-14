package com.tom.yang.oop

import scala.beans.BeanProperty


object ConstructorDemo {
  def main(args: Array[String]): Unit = {
//    val person: Person = new Person()
//    println(person)
    val p3 = new Person3()
    p3.setName("Tom")
    p3.setAge(16)
  }
}
/**
 * class 的构造器
 * 完成对对象实例的初始化，没有返回值
 *
 * 默认构造方法为无参构造器
 * 构造器可以重载
 *
 * 同 Java 一样，如果有父类，也会调用父类的构造器
 */
// 类名后直接跟参数列表是主构造器
// 主构造器的参数列表如果没有任何修饰符， 那么入参相当于局部变量，不是类的属性， 外部无法访问
// 如果用 val 修饰参数，则参数为私有的只读的属性，外部可以访问
// 如果用 var 修饰参数，则参数为私有的可读写的属性，外部可以访问
class Person(inName: String, inAge: Int) {
  // 主构造器的参数名和class内的属性名要区分开，并且可以直接赋值
  var name: String = inName
  var age: Int = inAge
  // 定义在 class 内部的语句在对象初始化时也会被执行
  // 且会先于构造器执行
  println("========= class 内部语句 =========")

  // this(参数列表) 为辅助构造器
  def this() {
    // 辅助构造器第一行必须调用主构造器, 不可以调其他辅助构造器
    this("Tom", 18)
    println("无参构造器初始化")
  }
  def this(name: String) {
    this(name, 18)
    println("name 构造器初始化")
  }

  // 重写 toString
  override def toString: String = {
    s"name: ${this.name} age: ${this.age}"
  }
}

// 构造器也可以私有化，直接在构造器前加 private
class Person2 private(){
  // 辅助构造器私有化
  private def this(name: String){
    this()
  }
}

// 如果想使用 Java 那样的规范化 get/set 方法可以使用 @BeanProperty 注解
// @BeanProperty 不影响 scala 默认生成的 getter/setter， 这两个可以同时使用
class Person3 {
  @BeanProperty
  var name: String = _
  @BeanProperty
  var age: Int = _
}
