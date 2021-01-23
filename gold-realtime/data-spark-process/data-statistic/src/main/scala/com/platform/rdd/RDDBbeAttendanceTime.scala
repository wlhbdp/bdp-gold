package com.platform.rdd

import com.alibaba.fastjson.JSONObject
import com.platform.common.{DateUtils, HbaseColConstant, SparkCommon}
import com.platform.service.BbeService
import com.platform.stream.DataAnalyticsOperate
import org.apache.spark.rdd.RDD
import org.slf4j.{Logger, LoggerFactory}

/**
  * 计算考勤时长
  */
case class RDDBbeAttendanceTime() {

  private val DIFF_TIME: Double = 1.5
  private val WORK_TIME: Double = 8

  private val logger: Logger = LoggerFactory.getLogger(getClass)


  def clean(attendances: RDD[JSONObject]): Unit = {
    try {

      val result = attendances.filter(attendance => {
        DataAnalyticsOperate().hasValidAttendance(attendance)
      })
        .map(e => {
          //计算工作时长
          val startWorkTime: String = e.getString(HbaseColConstant.START_WORK_TIME)
          val endWorkTime: String = e.getString(HbaseColConstant.END_WORK_TIME)
          val duration: String = computeDuration(startWorkTime, endWorkTime)
          //封装对象
          val obj: JSONObject = new JSONObject()
          obj.put(HbaseColConstant.EMP_NO_COL, e.getString(HbaseColConstant.EMP_NO_COL))
          obj.put(HbaseColConstant.WORK_DATE_COL, e.getString(HbaseColConstant.WORK_DATE_COL))
          obj.put(HbaseColConstant.DURATION_COL, duration)

          (obj, 1L)
        }).reduceByKey(_ + _)

      BbeService().saveComputedTime(result);

    } catch {
      case ex: Exception => {
        logger.error("计算工作时长失败!", ex);
      }
    }
  }


  def computeDuration(startTime: String, endTime: String): String = {
    val durationHour: Double = DateUtils.getDiffHour(startTime, endTime, SparkCommon.DATE_TIME_FORMAT_MILLISECOND)
    val resultHour: Double = durationHour - DIFF_TIME
    if (resultHour < 0) {
      return "0"
    }

    if (resultHour > WORK_TIME) {
      return WORK_TIME.toString
    }

    Math.floor(resultHour).toString

  }

}
