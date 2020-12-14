package com.tom.yang.highfunction

/**
 * 抽象控制
 * 一个函数，参数是函数，且这个参数函数没有参数和返回值
 */
object AbstractControl {
  def main(args: Array[String]): Unit = {
    // myRunnable 是抽象控制
    // fun 是一个没有参数，没有返回值的函数
    def myRunnable(fun: () => Unit) = {
      new Thread{
        override def run(): Unit = {
          fun()
        }
      }.start()
    }
    myRunnable{
      () => { // 这里看着还是不简洁
        println("Start")
        Thread.sleep(3000)
        println("Finish")
      }
    }
    // 注意写法， 参数省略了参数列表的()
    def myRunnable2(fun:  => Unit) = {
      new Thread{
        override def run(): Unit = {
          fun // 这里只写 fun 集合
        }
      }.start()
    }
    myRunnable2 {
      println("Start")
      Thread.sleep(3000)
      println("Finish")
    }

    // 抽象控制，手写一个类似 while 的函数
    // cond 和 code 均为函数
    def until(cond: => Boolean)(code: => Unit): Unit ={
      if(cond){
        code
        until(cond)(code)
      }
    }
    var x = 10
    until(x > 0){ // 条件是一个函数
      println(x)// 代码块也是一个函数
      x -= 1
    }
  }
}
