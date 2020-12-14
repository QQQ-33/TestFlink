package com.tom.yang.oop

/**
 * 重写
 */
object OverrideDemo {
  def main(args: Array[String]): Unit = {
    // def 和 val 重写
    val o1: A = new B()
    val o2: B = new B()
    println(s"o1: ${o1.age} o2: ${o2.age}")
    println(s"o1: ${o1.a} o2: ${o2.a}")
    // var 重写
    val o3: C = new D()
    val o4: D = new D()
    println(s"o3: ${o3.c} o4: ${o4.c}")
    // 匿名子类, 实现抽象类的所有抽象成员
    val o5: NoNameClass = new NoNameClass {
      override def say(): Unit = {
        println("大师兄，师傅被妖怪抓走啦！")
      }
    }
    o5.say()
  }
}

/**
 * Java 中只有方法的重写，没有属性的重写
 * 成员变量不能像方法一样被重写，子类定义的跟父类同名的字段，相当于子类重新定义了一个字段，父类的字段被隐藏
 * 可以通过将 子类赋值给父类的类型来访问父类的字段
 * 或者将子类类型强转为父类类型来访问父类字段
 * Java 的动态绑定
 * 1. 调用的是方法，JVM会将该方法和对象的内存地址绑定
 * 2. 调用的是属性，则没有动态绑定机制，在哪里调用就返回对应的值
 *
 * Scala 中由于所有属性都是private的，属性的访问都是通过 public 属性名() 的方法来访问属性，所以相当于重写了方法
 * def 只能重写另一个 def
 * val 只能重写另一个 val 或者不带参数的 def , 这里参数列表必须为空，且返回值类型必须同属性的类型
 * var 只能重写抽象的 var (含有抽象属性的类，必须声明为抽象类，抽象属性底层不会生成属性的声明，而是生成两个抽象方法)
 */
class A {
  // public age()
  val age: Int = 10
  def a: Int = {
    age
  }
}

class B extends A {
  // 重写字段必须用关键字 override
  // 相当于重写了 public age()， 这样打印 age 的时候无论指向父类还是子类，打印的都是子类的 age
  override val age: Int = 20
  // val 可以重写一个无参的 def, 本质上还是由于 属性生成的 public a() 方法重写了父类的 public a()
  override val a: Int = 2
}

/**
 * 抽象类：
 *  1. 抽象方法只需要省略方法体即可
 *  2. 抽象属性是只没有初始值的属性
 *  3. 抽象类不行能直接实例化，除非在实例化时，实现所有的抽象方法抽象属性。
 *  4. 继承抽象类的类，必须实现所有抽象方法和抽象属性，除非他自己也是 abstract 的
 *  5. 抽象类可以有实现的方法
 */
abstract class C {
  // 抽象属性
  var c: Int
  // 抽象方法
  def m()
}

class D extends C {
  // override 可以省略， 本质上是对方法的重写
  var c: Int = 4
  // 实现抽象方法
  def m(): Unit = {
    println(c)
  }
}

/**
 * 匿名子类
 * 同 Java 一样，通过包含带有定义或重写的代码块，创建一个匿名子类
 */
abstract class NoNameClass {
  def say(): Unit
}
