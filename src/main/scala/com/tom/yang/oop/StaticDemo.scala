package com.tom.yang.oop

/**
 * 静态属性 静态方法
 */
object StaticDemo {
  def main(args: Array[String]): Unit = {
    // 底层调用的是 SelfClass$.MODULE$.info()
    SelfClass.info()
  }
}

/**
 * Java 中的 static 属性和方法都是通过 class 来访问的，这种方式不是面向对象的
 * Scala 将静态的内容放入了 class 的半生对象 object 中，来模拟 Java 的静态属性和方法
 * 1. 同一个源码文件中，名字相同的 class 和 object 互为伴生类和伴生对象
 * 2. 非静态的内容写在 class 中， 静态的内容写在 object 中
 * 3. class 编译后生成 类名.class， object 编译后生成 类名$.class
 * 4. object 的内容可以通过 类名.属性 类名.方法 直接使用
 * 5. 只要有 object， 必然会编译出 类名.class 和 类名$.class，
 *    类名$.class 中构造方法 private 化，静态代码块将唯一实例绑定在 public final static MODULE$ 上
 *    类名.class 中生成同名的属性(方法)，调用 类名$.MODULE$.属性(方法)
 */
// 伴生类
class SelfClass {

}
// 伴生对象
// 静态的内容都放在 伴生对象中
object SelfClass {
  val name: String = "Tom"
  def info(): Unit = {
    println(s"My name is ${name}")
  }
}


