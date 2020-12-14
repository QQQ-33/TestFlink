package com.tom.yang.impl

/**
 * 隐式转换
 * (某些数据类型之间自定义的相互转化)
 *
 */
object ImplicitDemo {
  def main(args: Array[String]): Unit = {
    /**
     * 隐式转换函数: 关键字 implicit 定义的单参数的函数
     * 要使用隐式转换函数，应该在同一作用域中
     * 隐式转换函数的函数名可以任意，识别隐式转换函数能否适用，是通过方法的签名来实现的
     * 隐式转换函数可以有任意多个，但是同一作用域要保证可识别性的唯一
     */
    implicit def f1(num: Double): Int = {
      num.toInt
    }
    implicit def f2(num: Float): Int = {
      num.toInt
    }
    val num: Int = 3.5
    val num2: Int = 3.5f
    println(num)
    println(num2)

    /**
     * 隐式转换函数扩展类库
     */
    val c = new Class1
    c.insert()
    // 本来 Class1 没有 delete() 方法， 通过隐式转换函数，将 Class1 视为 Class2，扩展了 Class1 的功能
    implicit def addDelete(cc: Class1): Class2 = {
      new Class2
    }
    // 本质上是编译器帮我们调用了隐式转换方法 addDelete$(c).delete()
    c.delete()// 这里执行的依然是 Class2 的 delete() 方法
  }
}

class Class1 {
  def insert(): Unit = {
    println("Class1 insert")
  }
}

class Class2 {
  def delete(): Unit = {
    println("Class2 delete")
  }
}

/**
 * 可以将隐式转换函数写入一个单独的文件，在需要的位置整体引入
 */
