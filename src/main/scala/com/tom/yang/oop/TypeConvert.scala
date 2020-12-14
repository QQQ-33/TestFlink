package com.tom.yang.oop

/**
 * 类型检查 类型转换
 */
object TypeConvert {
  def main(args: Array[String]): Unit = {
    // 获取 class 的类，相当于 Java 的 String.class
//    println(classOf[String])
    val s = "Tom"
    // 获取类的完全限定名
//    println(s.getClass.getName)
    var f = new TestFather
    var c = new TestChild
    // 将子类赋值给父类，会自动向下转型
    f = c
//    f.m
    // 将父类赋值给子类，不会自动向下转型，需要做类型转换
//    c = f // 这里不做类型转换会直接报错
    // asInstanceOf[T] 不会改变当前对象的类型，而是会返回一个新的类型
    c = f.asInstanceOf[TestChild]
    c.m
    // 类型判断
    println(c.isInstanceOf[TestChild])
    println(f.isInstanceOf[TestChild])

  }
}

class TestFather {
  def m: Unit = {
    println("TestFather")
  }
}
class TestChild extends TestFather {
  override def m: Unit = {
    println("TestChild")
  }
}
