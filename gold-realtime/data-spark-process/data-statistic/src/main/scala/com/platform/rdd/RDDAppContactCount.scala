package com.platform.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
import org.slf4j.{Logger, LoggerFactory}
import com.alibaba.fastjson.JSONObject
import com.platform.common.DataTypeCommon
import com.platform.service.AppContactService


/**
  * 计算app通讯录数量
  */
case class RDDAppContactCount() {

  private val logger: Logger = LoggerFactory.getLogger(getClass)

  def clean(json : RDD[JSONObject]): Unit = {
    try{
      val result = json.map(
        x => (
          x.getString("token"),1L)
      ).reduceByKey(_ + _)

      AppContactService().saveAppContactCount(result);

    }catch{
      case ex: Exception => {
        logger.error("app通讯录数量计算失败!",ex);
      }
    }
  }


}
