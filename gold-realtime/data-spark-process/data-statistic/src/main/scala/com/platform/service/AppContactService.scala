package com.platform.service

import com.platform.db.mysql.AppContactDB
import org.apache.spark.rdd.RDD

case class AppContactService() {


  /**
    * 保存app通讯录数量计算
    * @param result
    */
  def saveAppContactCount(result: RDD[(String,Long)]): Unit ={
    AppContactDB().saveAppContactCount(result);
  }

  def saveAppContactLoanWordsCount(result: RDD[(String,Long)]): Unit ={
    AppContactDB().saveAppContactLoanWordsCount(result);
  }

  def saveAppContactSensitiveWords(result: RDD[(String,Long)]): Unit ={
    AppContactDB().saveAppContactSensitiveWords(result);
  }



}
