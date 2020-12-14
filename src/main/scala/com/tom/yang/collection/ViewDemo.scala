package com.tom.yang.collection

/**
 * View 产生一个总是被 lazy 执行的集合
 * 主要应用在对集合中的操作不立即执行，只有使用时才执行
 */
object ViewDemo {
  def main(args: Array[String]): Unit = {
    // 找出 1-100 所有逆序相同的数字
    def isEquals(num: Int): Boolean = {
      num.toString.equals(num.toString.reverse)
    }
    // 常规的集合
    val res = (1 to 100).filter(isEquals(_))
    println(res)
    // 附加需求，用到数据时，再产生数据
    // 使用 view 来处理
    val res2 = (1 to 100).view.filter(isEquals(_))
    println(res2) // SeqViewF(...)， 没有用到集合的内容时，返回的时 view
    print("view: ")
    for(num <- res2) {
      print(s"${num}, ") // 这里才真正的用到的元素
    }
    println()
  }
}
