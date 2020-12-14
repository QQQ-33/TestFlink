package com.tom.yang.project.customercrm.view

import com.tom.yang.project.customercrm.bean.Customer
import com.tom.yang.project.customercrm.service.CustomerService

import scala.io.StdIn

class CustomerView {
  var loop = true
  var key = ' '
  val viewStr =
    """
      ------------------ 客户信息管理软件 ------------------
                           1. 添加客户
                           2. 修改客户
                           3. 删除客户
                           4. 客户列表
                           5. 退    出
                          请选择(1 - 5):
    """
  val service: CustomerService = new CustomerService()
  def mainMenu(): Unit ={
    do {

      println(viewStr)
      key = StdIn.readChar()
      key match {
        case '1' => addCustomer()
        case '2' => println("修改")
        case '3' => println("删除")
        case '4' => listCustomer()
        case '5' => println("退出"); loop = false
      }
    } while (loop)
  }

  def addCustomer(): Unit = {
    println("------------------ 添加客户 ------------------")
    println("姓名:")
    val name = StdIn.readLine()
    println("性别:")
    val gender = StdIn.readChar()
    println("年龄:")
    val age = StdIn.readShort()
    println("电话:")
    val tel = StdIn.readLine()
    println("邮箱:")
    val email = StdIn.readLine()
    val customer = new Customer(0, name, gender, age, tel, email)
    service.addCustomer(customer)
    println("------------------ 添加完成 ------------------")
  }

  def deleteCustomer(id: Int): Any = {
    println("------------------ 删除客户 ------------------")
    println("请输入删除客户的id(退出输入-1):")

    val id = StdIn.readLine()
    do {
      id match {
        case "-1" => println("------------------ 退出删除 ------------------")
        case _ =>  {
          println("是否确认删除Y/N:")
          val isConfirm = StdIn.readChar().isLower
          if(isConfirm == 'y'){
//            service.deleteCustomer(x)
            println("------------------ 删除完成 ------------------")
          }
        }
      }
    } while (id == "-1")



  }
  def listCustomer(): Unit = {
    println("------------------ 客户列表 ------------------")
    println("编号\t\t姓名\t\t性别\t\t年龄\t\t电话\t\t邮箱")
    service.showCustomerList()
    println("------------------ 客户列表 ------------------")
  }

}
