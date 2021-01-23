package com.platform.stream

import com.platform.query.app.AppContactCountQuery
import org.apache.spark.sql.execution.streaming.FileStreamSource.Timestamp
import org.apache.spark.sql.{Dataset, SparkSession}

object DataAnalyticsOperate {

  /**
    * 处理App通讯录数据
    * @param dataSet
    */
  def processAppContact(dataSet: Dataset[(String,String)]) = {
    AppContactCountQuery().clean(dataSet);
  }


}
