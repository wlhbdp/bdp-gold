package com.platform.rdd

import com.alibaba.fastjson.JSONObject
import com.platform.service.AppContactService
import org.apache.commons.lang3.StringUtils
import org.apache.spark.rdd.RDD
import org.slf4j.{Logger, LoggerFactory}

/**
  * 通讯录
  */
case class RDDAppContactLoanWords() {


  private val logger: Logger = LoggerFactory.getLogger(getClass)

  def clean(json : RDD[JSONObject]): Unit = {
    try{
      val result = json.filter({
        x =>
          val phoneName = x.getString("phoneName");
          if (StringUtils.isNotBlank(phoneName)){
            if(phoneName.contains("贷")){
              true
            }else{
              false
            }
          }else{
            false
          }
      }).map(
        x => (
          x.getString("token"),1L)
      ).reduceByKey(_ + _)

      AppContactService().saveAppContactLoanWordsCount(result);

    }catch{
      case ex: Exception => {
        logger.error("app通讯录高风险词汇计算失败!",ex);
      }
    }
  }



}
