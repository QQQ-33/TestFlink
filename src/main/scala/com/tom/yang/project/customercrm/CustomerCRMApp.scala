package com.tom.yang.project.customercrm

import com.tom.yang.project.customercrm.view.CustomerView

object CustomerCRMApp {
  def main(args: Array[String]): Unit = {
    new CustomerView().mainMenu();
  }
}
