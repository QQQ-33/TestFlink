package com.tom.yang.project.customercrm.service

import com.tom.yang.project.customercrm.bean.Customer

import scala.collection.mutable.ListBuffer
import util.control.Breaks._
class CustomerService {
  var customerList: ListBuffer[Customer] = new ListBuffer[Customer]()
  var num: Int = 0
  def showCustomerList(): Unit = {
    this.customerList.foreach(println(_))
  }

  def addCustomer(customer: Customer): Unit = {
    num += 1
    customer.id = num
    customerList.append(customer)
  }

  def deleteCustomer(id: Int): Unit = {
    breakable {
      for(index <- 0 until customerList.size){
        if(customerList(index).id == id) {
          customerList.remove(index)
          break()
        }
      }
    }
  }
}
