package com.tom.yang.mymatch

/**
 * 样例类
 */
object CaseClassDemo {
  def main(args: Array[String]): Unit = {
    /**
     * 样例类的最佳实践 1
     * 用模式匹配来进行类型匹配，并绑定属性的值（将样例类中属性的值绑定到 case 的变量中）
     * 本质上是利用 unapply 方法将对象属性提取出来
     */
    for(amt <- Array(Dollar(999.9),Currency(1000.0),NoAmount)){
      val res = amt match {
        case Dollar(v) => "$ " + v // 根据 case class 的构造进行 unapply 将属性的值赋予 v
        case Currency(v) => "￥" + v
        case NoAmount => "NoAmount"
      }
      println(res)
    }

    /**
     * 最佳实践2
     * copy 复制一个对象，属性的值完全相同
     */
    val amt = Currency(3000.0)
    val amt2 = amt.copy()
    // copy 时也可以指定属性的值
    val amt3 = amt.copy(value = 6666.6)
    println(amt)
    println(amt2)
    println(amt3)

    /**
     * 最佳实践3
     * 匹配嵌套结构
     * 例子：
     *    商品可以捆绑打折销售
     *    商品可以是单品或者多个商品
     *    打折时按照扣 x 元计算
     *    统计所有捆绑商品打折后最终的价格
     */
    val sale = Bundle("书籍", 0.9f, Book("漫画", 40), Bundle("文学作品", 0.8f, Book("《三国》", 80), Book("《聊斋》", 50)))
    // 尝试取出 "漫画" , 使用 _ 忽略不想看到的值，但一定要用 _ 占位， _* 表示剩余的所有
    val res = sale match {
      case Bundle(_, _, Book(desc, _), _*) => desc
    }
    println(res)// 成功取出了 "漫画" ，但是这种方式必须预先知道嵌套结构
    // @ 表示法， 将嵌套的值绑定到变量
    val res2 = sale match {
      case Bundle(_, _, art @ Book(_, _),rest @ _*) => (art, rest)// art 绑定了 "漫画" rest 绑定了 "文学作品"
    }
    println(res2)
    // 最终结果
    val res3 = getPrice(sale)
    println(s"total: ${Math.ceil(res3*100)/100}")
  }

  /**
   * 嵌套结构的解析，无需预先知道嵌套的完整结构
   * 仅需对对象进行模式匹配
   * 匹配到 Book 返回 price
   * 匹配到 Bundle 用 @ 将它包含的所有内容取出来，递归调用来解析结构
   */
  def getPrice(item: Item): Double = {
    item match {
      case Book(_, price) => price
      // 对 Bundle 包含的内容绑定到 its ，并进行递归调用
      case Bundle(_, dis ,its @ _*) => its.map(getPrice(_)).sum * dis
    }
  }
}

/**
 * 样例类是 class
 * 使用关键字 case class 声明，为了 match 而特别优化的类
 * 1. 构造器中的参数全部为 val 的，除非显示的声明为 var
 * 2. case class 的伴生对象中自动生成 apply 方法，方便对象创建
 * 3. 提供了 unapply 方法用于 match
 * 4. 自动生成了 toString equals hashCode copy 方法
 *
 * case class 默认实现了序列化
 * 序列化是将 数据 => 字符串， 并可以进行反序列化，方便在网络中传输
 */
abstract class Amount
case class Dollar(value: Double) extends Amount
case class Currency(value: Double) extends Amount
case object NoAmount extends Amount

abstract class Item
case class Book(desc: String, price: Double) extends Item
case class Bundle(desc: String, disCount: Float, items: Item*) extends Item

/**
 * 密封类
 * 使用 sealed 修饰 case 类的超类，可以让 case class 仅在同一个源文件被使用
 * 密封类就是不能再其他源文件中定义子类
 */

abstract sealed class AAA
case class BBB() extends AAA
