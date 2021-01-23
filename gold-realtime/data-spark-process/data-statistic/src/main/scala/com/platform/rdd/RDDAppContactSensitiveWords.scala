package com.platform.rdd

import com.alibaba.fastjson.JSONObject
import com.platform.service.AppContactService
import org.apache.commons.lang3.StringUtils
import org.apache.spark.rdd.RDD
import org.slf4j.{Logger, LoggerFactory}

/**
  * app通讯录高风险词汇
  */
case class RDDAppContactSensitiveWords() {


  private val logger: Logger = LoggerFactory.getLogger(getClass)

  def clean(json : RDD[JSONObject]): Unit = {
    try{
      val result = json.filter({
        x =>
          val phoneName = x.getString("phoneName");
          if (StringUtils.isNotBlank(phoneName)){
            if(phoneName.contains("套现") || phoneName.contains("口子") || phoneName.contains("贷款中介") || phoneName.contains("赌")){
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

      AppContactService().saveAppContactSensitiveWords(result);

    }catch{
      case ex: Exception => {
        logger.error("app通讯录高风险词汇计算失败!",ex);
      }
    }
  }




}
