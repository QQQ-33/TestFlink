package com.tom.yang.oop

object PackageDemo {
  def main(args: Array[String]): Unit = {

  }
}

/**
 * scala 中的 package
 * 基本用法与 Java 相同，用于区分同名的 class 和管理文件 控制访问范围
 *
 * scala 源码中指定的 package 路径与源文件的路径可以不一致，但是编译后的 .class 文件会被自动放置在 package 指定的路径
 *
 * scala 默认引入的包：
 *  java.lang.*
 *  scala._
 *  predef._
 */
/**
 * scala 特殊的 package 用法，用 {} 来包含 package 的内容
 * 这种用法使 scala 可以在一个源码文件中创建多个包的内容
 *
 * 定义在同一个 {} 的子包，可以直接使用父包的内容
 * 同一个包中(无论父子)的的同名的类，根据使用的地方采取就近原则使用。可以一写出全类名来指定使用的具体类
 * 父包访问子包的类，需要 import
 *
 * scala 可以在父包中定义包对象, 每一个包都可以定义唯一的一个包对象，包对象的名字需要和子包的名字一致
 * 包对象中的变量和方法可以在对应的包中随意使用
 *
 * 底层是编译出了 package.class
 */
package com.tom.yang {
  // 需要和子包的名字一致
  package object oop {
    val a: Int = 100
    def say(): Unit = {
      println("package object say()")
    }
  }
  package oop {
    object c1 {}
    class c1 {}
    trait t1 {}
  }
  package oop2{
    object c2 {}
    class c2 {}
    trait t2 {}
  }

}

/**
 * 可见性
 * Java :
 *            同类  同包  子类  不同包
 *  public    √     √     √     √
 *  protected √     √     √     ×
 *  默认      √     √     ×     ×
 *  private   √     ×     ×     ×
 *
 *  Scala :
 *  1. 属性的默认访问权限为 private ,但是scala会根据 var/val 生成 get/set 方法。
 *  2. 类和方法的默认访问权限是 public
 *  3. private 属性只有类内和 object 伴生对象可以访问
 *  4. protected 同包不能访问，只有子类能访问
 *  5. scala 没有 public 关键字，无法显示的定义成 public
 *  6. 包访问权限，范围修饰符后边增加限定，相当于扩大了访问范围 private[oop] ， 这里oop包及其子包可以访问
 */

/**
 * 包的引用
 * 1. import 可以出现在任何地方，作用范围在语句块的范围内，提高效率
 * 2. 下划线 _ 引入整个包的类 import scala.beans._ 子包的内容依然需要 import
 * 3. 包选择器，一次引用多个类 import scala.collection.mutable.{HashMap, HahSet}
 * 4. 类型别名 import java.util.{HashMap => JavaHashMap, List} 将 Java 的 HashMap 重命名成 JavaHashMap ，仅在当前源码文件生效
 * 5. 隐藏不需要的类 import java.util.{HashMap => _, _} 将 Java 的 HashMap 重命名成 _ 表示不需要HashMap，后一个 _ 表示引入其余所有类
 */
