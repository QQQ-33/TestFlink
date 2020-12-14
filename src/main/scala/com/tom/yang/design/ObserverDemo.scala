package com.tom.yang.design

import scala.collection.mutable.ListBuffer

/**
 * 观察者模式
 * 气象站每天可以把测到的温度，湿度，气压等以公告的形式发布出去
 * 设计开放型API，便于其他第三方公司也能接入气象站获取信息
 * 提供温度，气压，湿度的接口
 * 测量数据更新时，实时通知第三方
 */
object ObserverDemo {
  def main(args: Array[String]): Unit = {
    val w: WeatherDataSt = new WeatherDataSt()
    val cc: CurrentConditions = new CurrentConditions()
    w.registerObserver(cc)
    w.setData(10.1f, 20.2f, 30.3f)
  }
}

/**
 * 被观察者：
 * 三个方法：
 *     注册观察者，
 *     删除观察者
 *     发出通知
 */
trait Subject {
  def registerObserver(o: Observer)
  def removeObserver(o: Observer)
  def notifyObservers()
}

/**
 * 观察者
 * 一个方法，用于接收被观察者的消息：
 *
 */
trait Observer {
  def update(temperature: Float, pressure: Float, humidity: Float)
}

class CurrentConditions extends Observer {
  private var temperature: Float = _
  private var pressure: Float = _
  private var humidity: Float = _

  override def update(temperature: Float, pressure: Float, humidity: Float): Unit = {
    this.temperature = temperature
    this.pressure = pressure
    this.humidity = humidity
    display()
  }

  def display(): Unit = {
    println(s"temperature [${temperature}] pressure [${pressure}] humidity [${humidity}]")
  }
}

class WeatherDataSt extends Subject {
  private var temperature: Float = _
  private var pressure: Float = _
  private var humidity: Float = _
  private val observers = new ListBuffer[Observer]()
  def setData(temperature: Float, pressure: Float, humidity: Float): Unit = {
    this.temperature = temperature
    this.pressure = pressure
    this.humidity = humidity
    notifyObservers()
  }
  override def registerObserver(o: Observer): Unit = {
    observers += o
  }

  override def removeObserver(o: Observer): Unit = {
    if(observers.contains(o)){
      observers -= o
    }
  }

  override def notifyObservers(): Unit = {
    for(o <- observers){
      o.update(this.temperature, this.pressure, this.humidity)
    }
  }
}
