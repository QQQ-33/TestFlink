package com.tom.yang.oop

/**
 * 面向对象编程
 */
object OOPDemo {
  def main(args: Array[String]): Unit = {
//    val account = new Account("00000000", 888.8, "888888")
//    try {
//      account.queryInfo("888888")
//      println(account.withDraw("888888", 20.0))
//    } catch {
//      case e: RuntimeException => println(e.getMessage)
//    }
    val child = new Child("Tom");
  }
}

class Account(account: String, initBalance: Double, initPwd: String){
  val accountNo: String = account
  var balance: Double = initBalance
  var pwd: String = initPwd

  @throws[RuntimeException]("信息异常")
  def queryInfo(pwd: String): Unit = {
    if(!this.pwd.equals(pwd)){
      throw new RuntimeException("密码错误")
    } else {
      println(s"accountNo: ${accountNo} balance: ${balance}")
    }

  }
  @throws[RuntimeException]("信息异常")
  def withDraw(pwd: String, money: Double): Double = {
    if(!this.pwd.equals(pwd)){
      throw new RuntimeException("密码错误")
    }
    if(balance < money){
      throw new RuntimeException("余额不足")
    }
    balance -= money
    money
  }
}

/**
 * 继承
 * scala 也是单继承，但是可以动态混入 trait
 * class 子类 extends 父类 with ...
 *
 *  默认及 protected 的属性及方法，子类可以继承，private的属性及方法无法继承
 *  其实属性都继承了，只是 scala 编译时 private 生成的 get/set 是 private 的
 *
 *  scala 编译后的代码只有 public 和 private ，protected 的限制是编译器级别的
 */

class Father{
  println("Father constructor method")
  def method: Unit = {

  }
  def this(name: String) {
    this()
    println(s"Father's name is ${name}")
  }
}

class Child extends Father {
  var name: String = _
  println("Child main constructor method")
  // 方法重写, 关键字 override
  override def method: Unit = {
    // 明确的调用父类的方法
    super.method
  }
  def this(name: String) {
    this()
    this.name = name;
    println("Child sub constructor method")
  }
}

/**
 * 构造器的执行过程
 * super 主构造器
 * super 辅助构造器
 * this 主构造器
 * this 辅助构造器
 *
 * 子类中，只有主构造器能调用父类的构造器，辅助构造器不能调用， 不能直接 super() 来调用父构造器
 */
class Child2 extends Father("Tom") {
  // Child2 的主构造器可以调用任意一个父类的构造器
}
