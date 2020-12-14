package com.tom.yang.generic

object GenericDemo {
  def main(args: Array[String]): Unit = {
    val msg = new IntMessage[Int](1)
    println(msg.get)
  }
}

/**
 * 泛型
 */

abstract class Message[T](s: T) {
  def get = s
}

class IntMessage[Int](v: Int) extends Message(v)

/**
 * UpperBounds/LowerBounds
 * 上界/下界
 * 上界：限定 A 为 B 的子类
 *     Java <A extends B> <? extends B>
 *     Scala [A <: B] [_ <: B]
 * 下界：限定 A 为 B 的父类
 *     Java <A super B> <? super B>
 *     Scala [A >: B] [_ >: B]
 *     Scala 对于下界的判定和Java非常不同，
 *     1. 下界可以传入任意类型
 *     2. 对于 B 的父类，按照原本的类型处理
 *     3. 对于 B 的子类，按照B类型处理，但方法的调用依然为原本类型的方法，只是做类型判定时会被当作 B 类型，类似多态
 *     4. 与 B 类型无关的类型也可以传入，但会视为 Object
 *
 */
class CompareTest[T <: Comparable[T]](o1: T, o2: T) {
  def greater = if(o1.compareTo(o2) > 0) o1 else o2
}

/**
 * ViewBounds
 * 视图界定
 * <% 比 <: 的范围要广，它除了可以是子类型，还可以是通过隐式转换的类型
 * <% 还可以使用在参数上
 */
class CompareTest2[T <% Comparable[T]](o1: T, o2: T) {
  def greater = if(o1.compareTo(o2) > 0) o1 else o2
}
object CompareTest2 extends App{
  // 这里没有指定泛型，但是依然可以直接使用
  println(new CompareTest2(10, 20).greater)

  // Float 本来是没有实现 Comparable 的， 这里通过隐式转换将 Float 转换为 Java.lang.Float
  println(new CompareTest2(10.1f, 20.2f).greater)

  // 也可以指定泛型
  println(new CompareTest2[java.lang.Float](10.1f, 20.2f).greater)

}

/**
 * Ordered 继承了 Comparable, 他是内部比较器，在类内容重载 compareTo 函数
 * Ordering 继承了 Comparator<T> 接口，他是一个外部比较器，需要定义一个类来实现比较器
 *
 * ContextBounds
 * 上下文界定
 * 对泛型的界定
 * [T: Ordering] 限定 T 是一个 Ordering，隐式转换也可以
 * implicit comparator: Ordering[T] 表示如果上下文中有 Ordering[T] 类型的参数，则赋给comparator
 */

class CompareTest3[T: Ordering](o1: T, o2: T)(implicit comparator: Ordering[T]) {
  def greater = if(comparator.compare(o1, o2) > 0) o1 else o2
}
object CompareTest3 extends  App{
  implicit val comparator = new Ordering[Int] {
    override def compare(x: Int, y: Int): Int = {
      println("comparator compare")
      x - y
    }
  }
  // 通过打印发现调用了 comparator 的compare
  println(new CompareTest3[Int](10, 20).greater)
}

/**
 * 协变
 * C[+A]: A 是 B 的子类，且 C[A] 是 C[B] 的子类
 *
 * 逆变
 * C[-A]: A 是 B 的子类，且 C[A] 是 C[B] 的父类
 *
 * 不变
 * C[A]: A 是 B 的子类，但是 C[A] 和 C[B] 没有关系
 */
