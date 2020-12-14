package com.tom.yang.design

import scala.io.StdIn

/**
 * 设计模式分为 3 类， 23种：
 * 创建型模式
 * 结构型模式
 * 行为型模式
 */
/**
 * 简单工厂模式，由一个工厂对象决定创建出哪一种产品的实例
 * 例子：
 *    披萨店，制作各种 pizza，
 */
abstract class Pizza {
  var name: String = _
  def prepare()
  def cut(): Unit = {
    println(s"cutting ${name}")
  }
  def bake(): Unit = {
    println(s"baking ${name}")
  }
  def box(): Unit = {
    println(s"boxing ${name}")
  }
}
class GreekPizza extends Pizza {
  override def prepare(): Unit = {
    this.name = "希腊 Pizza"
    println(s"preparing ${name}")
  }
}
class PepperPizza extends Pizza {
  override def prepare(): Unit = {
    this.name = "胡椒 Pizza"
    println(s"preparing ${name}")
  }
}
class CheesePizza extends Pizza {
  override def prepare(): Unit = {
    this.name = "奶酪 Pizza"
    println(s"preparing ${name}")
  }
}
/**
 * Pizza 工厂，根据客户的 order 来制作不同的 pizza
 * 封装了产生 pizza 的细节，未来有新的 pizza 只需要修改这个类
 */
object PizzaFactory {
  def getPizza(name: String): Pizza = {
    name match {
      case "greek" => new GreekPizza
      case "pepper" => new PepperPizza
      case "cheese" => new CheesePizza
    }
  }
}

/**
 * pizza 店
 */
object PizzaStore {
  def main(args: Array[String]): Unit = {
    do{
      println("请选择一个 pizza ")
      var name = StdIn.readLine();
      val pizza: Pizza = PizzaFactory.getPizza(name)
      pizza.prepare()
      pizza.bake()
      pizza.cut()
      pizza.box()
    } while (true)

  }
}
///////////////////////////////////////////////////////////////////////////////////
/**
 * 工厂模式方法
 * 对象的创建移至具体的子类
 * 例子：
 *    披萨店，制作各种 区域的 pizza
 * 将点餐的部分抽象出来
 * 工厂类来决定使用哪个区域的 PizzaStore， PizzaStore 生产 Pizza
 */

