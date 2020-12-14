package com.tom.yang.mymatch



/**
 * 模式匹配
 * 十分重要！！！
 *
 * match 关键字，每个分支使用 case 执行函数，默认匹配使用 case _
 */
object MatchDemo {
  def main(args: Array[String]): Unit = {
    val oper = "+"
    val a = 10
    val b = 20
    /**
     * 1. 关键字 match
     * 2. 匹配成功则执行 => 后边接的代码块， => 相当于 Java switch case 中的 :
     * 3. 匹配的顺序从上至下，无需 break，仅执行当前分支
     * 4. 默认的匹配是 case _ => {} 的代码块
     * 5. 必须有至少 case _ => {}
     */
    val res = oper match {
      case "+" => a + b
      case "-" => a - b
      case "*" => a * b
      case "/" => a / b
      case _ => println("no operator error")
    }
    println(res)

    /**
     * 匹配守卫
     * 匹配过程中的条件判断
     */
    var r1 = 0
    var r2 = 0
    for(c <- "+-1"){
      c match {
        case '+' => r1 = 1
        case '-' => r1 = -1
        // 如果 _ 后有 if 条件匹配，那么 _ 表示忽略当前 case 的值，将值带入后续的 if 判断
        // 也就是带有守卫的 _ 不再代表默认匹配，要和默认匹配区分开
        case _ if(c.toString.equals("1")) => r2 = 1
        case _ if(c > 10) => println("c > 10")
        case _ => r1 = 2
      }
      println(s"$c r1: $r1 r2: $r2")
    }

    /**
     * 模式匹配中的变量
     * 将模式匹配的是赋值给一个变量
     */
    val ch = 'U'
    // match 是一个表达式，所以可以有返回值
    val res2 = ch match {
      // 直接使用 ch
      case '+' => println("ch: " + ch)
      // 将 ch 赋值给 myChar
//      case myChar => println(myChar)
      case 'U' => ch + " OK"
      case _ => println("default")
    }
    println(res2)

    /**
     * 类型匹配
     * 避免使用 isInstanceOf 和 asInstanceOf
     * 在 try/catch 时用到的就是类型匹配
     * 1. 类型匹配时，泛型不同也算作不同类型
     * 2. 进行类型匹配之前，编译器会预判断是否有能匹配到的类型，如果没有则直接报错
     * 3. case _ : BigInt 这个语句并不表示默认匹配， _ 只是省略了变量名， 默认匹配一定是 case _ => 这种没有任何附件条件的匹配
     */
    val aa = 3
    val obj = if(aa == 1){
      aa
    } else if (aa == 2){
      Map[String, Int]("aa" -> aa)
    } else if (aa == 3){
      Map[Int, String](aa -> "")
    } else if (aa == 4){
      Array[String](aa + "")
    } else if (aa == 5){
      Array[Int](aa)
    } else if (aa == 6){
      BigInt(aa)
    }
    val res3 = obj match {
      case a: Int => a
      case b: Map[String, Int] => s"对象类型: Map[String, Int]"
      case c: Map[Int, String] => s"对象类型: Map[Int, String]"
      case d: Array[String] => s"对象类型: Array[String]"
      case e: Array[Int] => s"对象类型: Array[Int]"
      case f: BigInt => s"对象类型: BigInt"
      case _ => "no type matched"
    }
    println(res3)

    /**
     * 数组匹配
     */
    println("============== Array match ==============")
    for (arr <- Array(Array(0), Array(1, 0), Array(0, 1, 0), Array(1, 1, 0), Array(1, 1, 0, 1))){
        val res = arr match {
          // 匹配只有一个元素且元素为 0 的数组
          case Array(0) => "0"
          // 匹配两个元素的数组
          case Array(x, y) => s"x: $x y: $y"
          // 匹配以 0 开头的数组
          case Array(0, _*) => "以 0 开头的数组"
          case Array(_, 1) => "以 1 开头的数组"
          case _ => "Nothing Matched"
        }
       println(s"${arr.toBuffer}: $res")
     }

    /**
     * 列表匹配
     */
    println("============== List match ==============")
    for(list <- Array(List(0), List(0, 0, 0), List(1, 0, 0))){
      val res = list match {
        case 0 :: Nil => "0"
        case x :: y :: Nil => s"x: ${x} y: ${y}"
        case 0 :: tail => "0..."
        case _ => "nothing match"
      }
      println(res)
    }

    /**
     * 元组匹配
     */
    for(pair <- Array((0, 1), (1, 0), (1, 1), (1, 0, 2))){
      val res = pair match {
        case (0, _) => "0 ..."
        case (y, 0) => s"$y"
        case _ => "no match"
      }
    }
  }
}
