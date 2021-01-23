package com.platform.stream

import com.platform.common.DataTypeCommon
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.slf4j.LoggerFactory


case class StreamingDataHandler() {

  @transient lazy val logger = LoggerFactory.getLogger(getClass)


  def creditDataHandler(dataFrame: DataFrame,spark: SparkSession) = {

    import org.apache.spark.sql.functions._
    import spark.implicits._


    val result = dataFrame.selectExpr("CAST(value AS STRING)")
      .select(
        get_json_object(col("value"), path = "$.dataType").alias("dataType"),
        col("value").alias("data")
      ).as[(String,String)]


    /**
      * 过滤出App通讯录数据
      */
    val appContact = result.filter(x=>{
      x._1.equals(DataTypeCommon.RISK_RULE_DATA_FIELD_APP_CONTACT)
    })

    /**
      *处理App通讯录数据
      */

    DataAnalyticsOperate.processAppContact(appContact)

  }

}
