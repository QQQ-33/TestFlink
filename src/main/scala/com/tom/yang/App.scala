package com.tom.yang

import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.util.Random

/**
 * Hello world!
 *
 */
object App {
  def main(args: Array[String]): Unit = {
    // Any 所有类型的超类
    // AnyRef 所有引用类型的超类 等于 Object Any的子类
    // AnyVal 所有值的超类 Any的子类

    // Byte Char Short Int Long Float Double Boolean

    // Unit 等于 void
    // Null 等于 null 所有 AnyRef 的子类
    // Nothing 所有类型的子类

    /**
     * 定义变量
     * val | var 变量名(: 数据类型) = 变量值
     * 数据类型可以不指定，scala 会自动推断
     */
    val name: String = "Tom";
    var age = 18;
    // name = "Yang"; val 等于 final 不可以改变值
    age = 28;

    /**
     * 字符串的输出
     */
//    println("My name is " + name + ", I'm " + age + " years old");
    // 在字符串中直接引用变量, 变量可以进行简单的公式运算
//    println(s"My name is $name, I'm $age years old");
//    println(s"My name is $name, I'm ${age - 10} years old");
    // 类似 printf 格式化输出子串，可以直接引用变量，或者后续插入值，针对每个变量可以指定格式
//    println(f"My name is $name%s, I'm $age%d years old");

    /**
     * 条件判断
     */
    if(age > 18){
//      println("old");
    } else {
//      println("young");
    }

    val arr: Array[Int] = Array(1, 2, 3, 4, 5);
    for(i <- arr){
//      print(s"$i "); // 1 ~ 5
    }
//    println();
    for (i <- 1 to 5){
//      print(s"$i "); // 1 ~ 5
    }
//    println();
    for (i <- 1 until 5){
//      print(s"$i "); // 1 ~ 4
    }
//    println();
    for (i <- 1 to 5 if i % 2 == 0){
//      print(s"$i "); // 2 4
    }
//    println();
    for (i <- 1 to 3; j <- 1 to 3 if i != j){
//      print(s"${i * 10 + j} "); // 12 13 21 23 31 32
    }
//    println();
    // yield 关键字，以当前循环构建一个新的集合
    val arr2 = for (a <- 1 to 6 if a % 2 == 0) yield a
    for(i <- arr2){
//      print(s"$i "); // 2 4 6
    }
//    println();

    /**
     * 运算符 + - * / % & |  >> << >>>
     */
    val a = 1;
    val b = 2;
    // 运算符其实都是方法
//    println(a + b)
//    println(a.+(b))
//    println(sum(a, b));

    /**
     * 方法
     */
    def sum(a: Int, b: Int): Int = {
      return a + b;
    }
    /**
     * 函数
     */
    val f = (a: Int, b: Int) => a + b;
//    println(f(a, b));
    // 函数与方法的主要区别数 函数可以当成参数传递
    val f2 = (a: Int, b: Int) => a * b;
    def wrappedSum(f: (Int, Int) => Int, a: Int, b: Int): Int ={
      return f(a, b);
    }
//    println(wrappedSum(f, a, b));
    // 方法转成函数
    val f3 = sum _
    // 可变参数
    def dynamicParams(params: Int*) = {
      for(i <- params){
//        print(s"$i ");
      }
    }
    dynamicParams(1,2,3,4,5);
//    println();
  }

  /**
   * 方法定义
   * def 方法名( 参数: 类型, 参数: 类型 ...): 返回值类型 = {...}
   */
//  def sum(a: Int, b: Int): Int = {
//    return a + b;
//  }
  /**
   * 数组
   * 定长数组 Array[T]
   * 不定长数组 ArrayBuffer[T]
   */
  val arr = new Array[Int](7);
  // toBuffer 将数组转换为数组缓冲(可变长数组)，以便显示数组的内容
//  println(arr.toBuffer); // ArrayBuffer(0, 0, 0, 0, 0, 0, 0)
  // 不使用 new 关键字，相当于直接向数组内放入元素， 下面相当于一个长度为1的数组，内容为一个10
  val arr1 = Array[Int](10);
//  println(arr1.toBuffer); // ArrayBuffer(10)
  val arr2 = Array[String]("Hadoop", "EFK", "Flink");
//  println(arr2.toBuffer); // ArrayBuffer(Hadoop, EFK, Flink)

  // 使用 ArrayBuffer 需要 import scala.collection.mutable.ArrayBuffer
  val ab = ArrayBuffer[Int]();
  ab += 1; // 追加元素
//  println(ab)
  ab += (2, 3, 4, 5); // 追加多个元素
//  println(ab)
  ab ++= Array(6, 7); // 追加数组
//  println(ab)
  ab ++= ArrayBuffer(8, 9);
//  println(ab)
  ab.insert(0, -1, 0); // 插入元素，可以一次插入多个，第一个参数为index
//  println(ab)
  ab.remove(8, 2); // 删除元素，起始 index 删除的元素个数
//  println(ab);

  // 遍历数组
  for(i <- ab){
//    print(s"$i ");
  }
//  println()
  for(i <- (0 until ab.length).reverse){
//    print(s"${ab(i)} ");
  }
//  println()
  /**
   * 数组的产生
   * new 一个
   * yield 一个
   * map 函数
   */
  val arr0 = Array[Int](1, 2, 3, 4, 5, 6, 7);
  val ay = for(i <- arr0 if i % 2 == 0) yield i * 10;
//  println(ay.toBuffer)
  val am = arr0.filter(_ % 2 == 0).map( _ * 10);
//  println(am.toBuffer)
  /**
   * 集合
   * 总体分为可变集合，不可变集合
   * mutable
   * immutable
   */
  // List 链表，head + tail 组成， Nil为空值
  val l1 = List(1, 2, 3)
  val l2 = 0 :: l1; // 在L1前面插入0并返回新的链表
  val l3 = l1.::(0);
  val l4 = 0 +: l1;
  val l5 = l1.+:(0);

  val lb = ListBuffer(1, 2, 3);// 可变链表
  /**
   * Map Set 基本等同于 Java 中的 Map 和 Set
   */
}
/**
 * 类
 */
class App{}
// 构造函数等同于 class 的声明
// 成员变量可以有默认值
// 主构造器与 class 的声明在一起，会执行所有类定义的语句
//class Point(var x: Int, var y: Int){
//  // 辅助构造器
//  def this(x: Int){
//    this(x, 10);// 第一行必须调用其他构造器
//  }
//  def move(dx: Int, dy: Int): Unit = {
//    x = x + dx;
//    y = y + dy;
//  }
//  override def toString: String = {
//    s"($x, $y)";
//  }
//}
class Point {
  private var _x = 0;
  private var _y = 0;
  private val bound = 1000;

  // getter & setter, 注意 setter 的特殊语法 x_= 等号前不能有空格
  def x: Int = _x; // getter
  def x_= (value: Int): Unit = { // setter
    if(Math.abs(value) < bound){
      _x = value;
    } else {
      printWarning();
    }
  }
  def y: Int = _y;
  def y_= (value: Int): Unit = {
    if(Math.abs(value) < bound){
      _y = value;
    } else {
      printWarning();
    }
  }

  def printWarning(): Unit = println("Warning: Out of bounds");
}

/**
 * Trait
 * 类似 java 8 的 Interface，没有成员变量，不能实例化，可以有默认实现
 * 可以被 extends
 */
trait Iterator[A] {
  def hasNext: Boolean;
  def next(): A;
}

class InIterator(to: Int) extends Iterator[Int] {
  private var current = 0;

  override def hasNext: Boolean = current < to;

  override def next(): Int = {
    if(hasNext) {
      val t = current;
      current += 1;
      t
    } else {
      0
    }
  }
}

/**
 * 伴生对象(单例对象)
 * 有且只有一个实例，只有第一次使用的时候才创建
 */
// 单例对象 类似Java中的静态类，类的方法和成员可以在其他类中访问，而不用new
// 类中的变量和方法都是 static 的，所有实例共享
// 与 class 同名的单例对象称作这个类的伴生对象
// 伴生类与伴生对象可以互相访问私有成员
class Dog(name: String){
  def say(): Unit ={
    println(s"My name is ${this.name}, ${Dog.subFix}!");
  }
}
object Dog{
  private val subFix: String = "汪汪汪";

  def apply(name: String): Dog = new Dog(name);

  def apply(): Dog = {
    new Dog("我没有名字")
  }
}

/**
 * apply 方法
 * 使用 类名(param1, param2 ...) 这种用法，其实是调用这个类的 apply 方法
 * 例如: val arr = Array(1, 2, 3)
 * 他可以简化调用构造函数的方式来创建实例对象，语法更加简洁
 */
// val dog = Dog("apply dog");
/**
 * 元组
 * 可以容纳多个元素的类，不可变对象，可容纳 2 - 22 个元素，每种元组有固定的类型
 */
class TestTuple{
  Tuple22
  // 这里可以不用显示的指定是几元组
  val tuple = ("string", 25): Tuple2[String, Int];
  def test: Unit = {
    // 取元素时，下标从 1 开始
    println(tuple._1)
    println(tuple._2)
  }
  // 解构赋值, name 和 age 被分别赋予 tuple 中的第一个和第二个值
  val (name, age) = tuple

}


/**
 * 模式匹配 和 样例类 Case class
 *
 */object TestMatch {
  case class CaseClassDemo();
  def main(args: Array[String]): Unit = {
    /**
     * 模式匹配
     * 检查某个值是否匹配一个模式，匹配成功
     * 匹配成功的值可以被解构。
     * Java Switch case 的升级版，用于替代 if / else
     * 关键字 match case
     */
    val arr = Array("string", 1, 2.2, CaseClassDemo)
    val value = arr(Random.nextInt(4));
    value match {
      case x: Int => println(s"Int value ${x}");
    }
  }
}


/**
 * 高阶函数
 * 参数为函数，或者返回值是函数的方法或函数
 */
/**
 * 柯里化
 * 一个方法可以定义多个参数列表，当只调用方法时只指定了某个参数列表， 将会返回一个新的函数，该函数接收剩余的参数列表
 */
