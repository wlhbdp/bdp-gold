package com.platform.etl

import java.time.{LocalDateTime, ZoneOffset}

import com.alibaba.fastjson.JSONObject
import com.platform.conf.SqlConfiguration
import com.platform.db.CommonDAL
import com.platform.utils.DateTimeUtils
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.slf4j.LoggerFactory

import scala.collection.mutable

/**
  *
  * author: GaoPan
  * create: 2019-03-07 01:29
  */
case class CommonEtl() {

  private val logger = LoggerFactory.getLogger(getClass)


  def process(sparkSql:SparkSession, timeType:String, timeMap:mutable.HashMap[String,LocalDateTime],
              tableMapping:JSONObject):Unit = {
    var sparkSession:SparkSession = sparkSql
    //json文件格式校验、解析
    val sqlConfigurations = JSONScriptProcessor.parseSqlConf(tableMapping)
    if (sqlConfigurations.nonEmpty) {
      var df = sparkSession.emptyDataFrame
      for (sqlConf <- sqlConfigurations) {
        val t = processSingleStep(sparkSession, sqlConf, timeMap)
        sparkSession = t._1
        df = t._2
      }
      println("The last step:" + sqlConfigurations(sqlConfigurations.size-1).tempView)

      CommonDAL().save(df, timeType, tableMapping)
      df.unpersist()
    }
  }

  def processSingleStep(sparkSession:SparkSession, sqlConfiguration: SqlConfiguration,
                        timeMap:mutable.HashMap[String, LocalDateTime]): (SparkSession, DataFrame) = {
    var sql = sqlConfiguration.sql

    import scala.collection.JavaConversions._
    for(entry <- sqlConfiguration.macros.entrySet()) {
      val key = entry.getKey
      val value = entry.getValue.toString
      if (value.nonEmpty) {
        sql = sql.replace(key, value)
      } else {
        val startDateTime = timeMap("startDateTime").toInstant(ZoneOffset.of("+8")).toEpochMilli
        val endDateTime = timeMap("endDateTime").toInstant(ZoneOffset.of("+8")).toEpochMilli
        sql = sql.replace("${startDateTime}", startDateTime.toString)
        sql = sql.replace("${endDateTime}", endDateTime.toString)

        sql = sql.replace("#{begin_time}", DateTimeUtils.getDateTimeFormatter(startDateTime))
        sql = sql.replace("#{end_time}", DateTimeUtils.getDateTimeFormatter(endDateTime))

        sql = sql.replace("#{biz_date}", DateTimeUtils.getBizDateFormatStr("yyyyMMdd"))
        sql = sql.replace("#{biz_hour}", DateTimeUtils.getBizHour().toString)

        sql = sql.replace("#{partition_month}", DateTimeUtils.getPtMonth)
        sql = sql.replace("#{partition_day}", DateTimeUtils.getPtDay)
      }
    }

    sql = processSql(sql, Array("${startDateTime}", "${endDateTime}"))

    println(s"Begin to execute sql: $sql")
    logger.info(s"Begin to execute sql: $sql")
    var df:DataFrame = sparkSession.emptyDataFrame
    df = sparkSession.sql(sql)

    if (sqlConfiguration.tempView != null && sqlConfiguration.tempView.nonEmpty) {
      df.createOrReplaceTempView(sqlConfiguration.tempView.trim)
    }
    (sparkSession, df)
  }

  def processSql(sql:String, kw:Array[String]) :String = {
//    kw.foreach(word => newSql = newSql.replace(word.toLowerCase, word))
    sql.trim
  }
}