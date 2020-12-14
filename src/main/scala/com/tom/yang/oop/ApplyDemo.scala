package com.tom.yang.oop

/**
 * apply 方法
 * 在伴生对象中提供 apply 方法，那么就可以通过 类名(参数) 直接创建类的实例
 * apply 方法简化了对象实例的创建，也可以结合设计模式来使用
 * apply 方法可以重载
 */
object ApplyDemo {
  def main(args: Array[String]): Unit = {
    // 使用 apply 方法创建实例
    val c1: Apply1 = Apply1()
    val c2: Apply1 = Apply1("Jerry")
    println(c1.name)
    println(c2.name)
  }

}

class Apply1 {
  var name: String = _
  def this(name: String){
    this()
    this.name = name
  }
}
object Apply1 {
  // apply 方法
  def apply(inName: String): Apply1 = {
    println("apply run")
    new Apply1(inName)
  }
  // apply 方法重载
  def apply(): Apply1 = {
    println("apply run")
    new Apply1("Tom")
  }
}
