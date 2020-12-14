package com.tom.yang.oop

object InnerClassDemo {
  def main(args: Array[String]): Unit = {
    // 创建内部类是 对象.内部类
    val c1 = new OuterClass
    val c2 = new OuterClass
    val c3 = new c1.InnerClass
    val c4 = new c2.InnerClass

    // 静态内部类可以直接用 外部类.内部类 创建
    val c5 = new OuterClass.InnerStaticClass
    // 通过类型投影屏蔽外部类型的影响
    c3.test(c3)
    c4.test(c3)// 如果不使用类型投影，这里会报错，因为内部类型与外部类型相关
  }
}

/**
 * Scala 的嵌套类
 *  Scala 内部类的实例和外部类的实例相关联，也就是内部类的内类型不是独立的
 *  可以通过类型投影让编译器忽略外部类的影响
 */

class OuterClass {
  // 外部类的别名，相当于外部类的一个实例
  myOuter =>
  val name: String = "Tom"
  private val sal: Int = 39000

  // 成员内部类
  class InnerClass {
    // 内部类获取外部类的属性
    println(OuterClass.this.name)
    // 利用别名访问外部类
    println(myOuter.sal)
    // # 代表类型投影, 外部类#内部类，屏蔽外部类对内部类的类型影响
    def test(ic: OuterClass#InnerClass): Unit = {
      println(ic)
    }
  }
}
object OuterClass {
  // 静态内部类
  class InnerStaticClass {

  }
}
