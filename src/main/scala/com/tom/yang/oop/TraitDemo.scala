package com.tom.yang.oop

object TraitDemo {
  def main(args: Array[String]): Unit = {
    val c1 = new C2()
    val c2 = new C4()
    c1.getConnect()
    c2.getConnect()

  }
}

/**
 * trait 特质
 * 相当于 Java 的 interface
 * 从面向对象的角度看，接口并不是面向对象的范畴，所以 Scala 采用 trait 来代替接口
 * trait 类似 interface + abstract class
 * Java 中的所有 interface 都可以当作 Scala 中的 trait 使用，比如 Cloneable Serializable 等
 *
 * Scala 没有 implement 并且也是单继承，所以如果有父类必须要先 extends
 * class extends 父类 with trait1 with trait2 ...
 *
 */

/**
 * trait 既可以定义抽象方法，也可以写实现的方法
 * 一个类可以 实现/继承 多个 trait
 */
trait DBConnection {
  /**
   * 当 trait 中只有抽象方法时，底层被编译为抽象方法和接口，并由 class 实现，这等同于Java的实现方式
   */
  def getConnect()

  /**
   * 当 trait 中既有抽象方法也有实现方法时
   * 1. 一个 trait 对应两个 .class 文件 一个被编译为接口 trait.class (包含所有方法声明，包括抽象的和实现的)
   * 2. 一个被编译为抽象类 trait$class.class (只包含实现的方法声明，但是被定义为静态，静态的实现同 object 一样使用 MODULE$ )
   * 3. 实现接口的类 只 implements trait.class 的接口，要调用实现方法时，通过静态方式调用 trait$class.方法,
   * 4. class 的构造函数会调用 trait$class 的初始化进行静态类型绑定
   * 5. class 中对于方法的实现：抽象方法调用本类的实现，实现方法通过静态调用 trait$class 的实现
   */
  def getDefaultConnect(): Unit = {
    println("Connect to SQLServer")
  }
}

class F1 {}
class C2 extends F1 with DBConnection {
  override def getConnect(): Unit = {
    println("Connect to Mysql")
  }
}

class F2 {}
class C4 extends F2 with DBConnection {
  override def getConnect(): Unit = {
    println("Connect to PostgreSQL")
  }
}

/**
 * 动态混入
 * 在不改变原有类和继承关系的基础上对类进行扩展
 */
object MixInDemo {
  def main(args: Array[String]): Unit = {
    // 在创建对象实例时，动态混入特质来扩展对象
    val c = new NormalClass with Additional
    c.insert(1)
    // 抽象类的动态混入，先 with 再实现抽象方法
    val c1 = new AbsClass with Additional {
      override def say(): Unit = {
        println("say")
      }
    }
    c1.insert(2)
    c1.say()
  }
}
trait Additional {
  def insert(num: Int): Unit = {
    println(s"insert num ${num}")
  }
}
class NormalClass {

}
abstract class AbsClass {
  def say()
}

/**
 * 特质叠加
 */

object AddTrait {
  def main(args: Array[String]): Unit = {
    // 同时混入 Trait3 Trait4
    // 初始化的顺序是按照混入的顺序(从左至右)加载的，并且虽然 Trait3 Trait4 都继承了 Trait2 但是 Trait2 只初始化一次
    val c1 = new NormalClass2 with Trait3 with Trait4

    // 方法的执行按照从右至左的顺序执行，
    // 当方法中有 super 调用时，先看 with 的顺序中，父级是否有同名方法，有的话就调用这个
    // 如果 with 顺序中没有同名方法，就去找自己 extends 的父级调用方法
    c1.insert(1)
  }
}

/**
 * 注意混入之后方法的调用
 */
trait Trait1 {
  println("Trait1")
  def insert(num: Int)
}
trait Trait2 extends Trait1{
  println("Trait2")
  override def insert(num: Int): Unit = {
    println(s"insert into list ${num}")
  }
}
trait Trait3 extends Trait2{
  println("Trait3")
  override def insert(num: Int): Unit = {
    println(s"insert into DB ${num}")
    super.insert(num)
  }
}
trait Trait4 extends Trait2{
  println("Trait4")
  override def insert(num: Int): Unit = {
    println(s"insert into File ${num}")
    // 这里的 super 的指向的是动态混入时的父级（从左至右排列），不一定是 Trait2
    super.insert(num)
    // 调用 super 时也可以指定super指向谁
    super[Trait2].insert(num)
  }
}
class NormalClass2 {}

/**
 * 动态混入中的实例字段底层是直接将字段 加入至 class 中的
 * 抽象字段需要在混入时具体实现
 */

/**
 * 自身类型
 * 为了解决循环依赖，限制混入该特质的类的类型
 */
trait SelfTypeDemo {
  // 这里声明自己属于一个 Exception，那么混入该特质的类必须也是 Exception 的子类
  this: Exception =>
  def log(): Unit = {
    println(getMessage)
  }
}
