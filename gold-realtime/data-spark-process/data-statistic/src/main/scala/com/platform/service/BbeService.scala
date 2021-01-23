package com.platform.service

import com.alibaba.fastjson.JSONObject
import com.platform.db.hbase.BbeDataDB
import org.apache.spark.rdd.RDD


case class BbeService() {

  /**
    * 保存计算出的工时的结果
    */
  def saveComputedTime(result: RDD[(JSONObject, Long)]): Unit = {
    BbeDataDB().saveComputedTimeData(result)
  }

  def saveInvalidData(result: RDD[(JSONObject, Long)]): Unit = {
    BbeDataDB().saveInvalidData(result)
  }


}
