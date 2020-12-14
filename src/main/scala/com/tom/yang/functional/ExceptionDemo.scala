package com.tom.yang.functional

/**
 * 异常处理
 * try {} catch {} finally{}
 * 基本结构与 Java 相同， 但是 catch 只有一个，在 catch 中使用 case 来匹配不同的异常(模式匹配)
 * case 的顺序依然是范围小的要写在前面
 * finally 同 Java 相同
 * scala 没有编译期异常，只有运行时异常
 */
object ExceptionDemo {
  def main(args: Array[String]): Unit = {
//    try {
//      val r = 10 / 0
//    } catch {
//      case e: ArithmeticException => {
//        println("catch ArithmeticException")
//      }
//      case e: Exception => {
//        println("catch Exception")
//      }
//    } finally {
//      println("finally run...")
//    }
    try {
//      testException
      testException2
    } catch {
      case e: RuntimeException => {
        e.printStackTrace()
        println("catch RuntimeException")
      }
    } finally {
      println("finally run...")
    }
  }

  /**
   * Nothing 类型常用于抛出异常的方法
   * 这里主动抛出异常
   */
  def testException: Nothing = {
    // 这里使用 new 来手动抛出异常
    throw new RuntimeException("throw a Exception")
  }

  // 使用 @throws[T] 来声明可能抛出的异常, 等同于 Java 中在方法声明中的 throws 声明
  @throws[ArithmeticException]("方法可能抛出 ArithmeticException")
  def testException2: Any = {
    10 / 0
  }


}
