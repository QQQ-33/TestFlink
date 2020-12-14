package com.tom.yang.project.customercrm.bean

class Customer {
  var id: Int = _
  var name: String = _
  var gender: Char = _
  var age: Short = _
  var tel: String = _
  var email: String = _


  def this(id: Int, name: String, gender: Char, age: Short, tel: String, email: String){
    this
    this.id = id
    this.name = name
    this.gender = gender
    this.age = age
    this.tel = tel
    this.email = email
  }

  override def toString = id + "\t\t" + name + "\t\t" + gender + "\t\t" + age + "\t\t" + tel + "\t\t" + email
}
