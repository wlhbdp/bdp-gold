package com.platform


import java.time.ZoneOffset

import com.platform.conf.{AppConfig, SparkETLConf, SparkSessionSingleton}
import com.platform.etl.{CommonEtl, HxDbBusinessApplyEtl}
import com.platform.utils.{DateTimeUtils, TableMapParser}

import scala.collection.mutable.ArrayBuffer


object HiveEtl {

  def main(args: Array[String]): Unit = {
    val os = System.getProperty("os.name")
    if (os.contains("Windows")) {
      println("OS:" + os)
      println("当前系统为Windows系统，需要下载winutils.exe，并设置hadoop.home.dir变量")
      System.setProperty("hadoop.home.dir", "C:\\env-dev\\winutils")
    }

    println("usage:")
    println("[Main class]=>com.platform.HiveEtl")
    println("[VM options]=>-Dspark.master=local[*]")
    println("[Program arguments]=><day> <taskName...>")
    println("args:")
    for(index <- args.indices) {
      println(s"args[$index]=" + args(index))
    }

    if (args.length < 2) {
      sys.error("please input program arguments!")
      sys.exit(1)
    }
    val timeType = args(0)
    val taskNames = ArrayBuffer[String]()
    for (index <- 1 until args.length) {
      if (args(index).startsWith("/")) {
        taskNames.append(args(index).split("/").last)
      } else {
        taskNames.append(args(index))
      }
    }
    val timeMap = timeType match {
      case "hour"=> DateTimeUtils.getHourRange
      case "day"=> DateTimeUtils.getDayRange
      case "week"=> DateTimeUtils.getWeekRange
      case "month"=> DateTimeUtils.getMonthRange
      case "year"=>DateTimeUtils.getYearRange
      case _=> throw new Exception("时间类型值错误「hour,day,week,month,year」");
    }

    val startDateTime = timeMap("startDateTime").toInstant(ZoneOffset.of("+8")).toEpochMilli
    val endDateTime = timeMap("endDateTime").toInstant(ZoneOffset.of("+8")).toEpochMilli

    val sparkConf = SparkETLConf.initSparkConf()
    val sparkSession = SparkSessionSingleton.getInstance(sparkConf)
    sparkSession.sql(AppConfig.SPARK_SQL_USER_DB)

    val filePaths = ArrayBuffer[String]("table_mappings","/scripts")
    val tableMappings = TableMapParser.parseTableMapInfo(filePaths, args.slice(1,args.length).to[ArrayBuffer])

    for (taskName <- taskNames) {
      println("=====>taskName:" + taskName)
      val tableMapping = tableMappings.get(taskName).orNull

      if (tableMapping != null && !tableMapping.isEmpty) {
        //通用逻辑处理入口
        CommonEtl().process(sparkSession, timeType, timeMap, tableMapping)
      } else {
        //特殊逻辑处理入口
        taskName match {
          case "HxDbBusinessApplyEtl" => HxDbBusinessApplyEtl().clean(sparkSession,timeType,startDateTime,endDateTime)
          case _ => sys.error("There's no such task==> " + taskName)
        }
      }
    }

    sparkSession.stop()
  }

}
