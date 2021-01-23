package com.platform.rdd

import com.alibaba.fastjson.JSONObject
import com.platform.common.{HbaseColConstant, SparkCommon}
import com.platform.service.BbeService
import com.platform.stream.DataAnalyticsOperate
import org.apache.commons.lang3.StringUtils
import org.apache.spark.rdd.RDD
import org.slf4j.{Logger, LoggerFactory}

case class RDDBbeInvalidAttendance() {

  private val logger: Logger = LoggerFactory.getLogger(getClass)


  def clean(attendances: RDD[JSONObject]): Unit = {
    try {

      val result = attendances.filter(attendance => {
        !DataAnalyticsOperate().hasValidAttendance(attendance)
      })
        .map(e => {
          val tag: String = makeTag(e)
          //封装对象
          val obj: JSONObject = new JSONObject()
          obj.put(HbaseColConstant.EMP_NO_COL, e.getString(HbaseColConstant.EMP_NO_COL))
          obj.put(HbaseColConstant.WORK_DATE_COL, e.getString(HbaseColConstant.WORK_DATE_COL))
          obj.put(HbaseColConstant.IS_WORK_COL, e.getString(HbaseColConstant.IS_WORK_COL))
          obj.put(HbaseColConstant.TAG_COL, tag)
          (obj, 1L)
        }).reduceByKey(_ + _)

      BbeService().saveInvalidData(result);

    } catch {
      case ex: Exception => {
        logger.error("计算工作时长失败!", ex);
      }
    }
  }

  def makeTag(invalidData: JSONObject): String = {

    var startTag = false
    var endTag = false
    val startWorkTime = invalidData.getString(HbaseColConstant.START_WORK_TIME)
    if (StringUtils.isBlank(startWorkTime)) {
      logger.warn(s"start_work_time invalid:$startWorkTime")
      startTag = true
    }


    val endWorkTime = invalidData.getString(HbaseColConstant.END_WORK_TIME)
    if (StringUtils.isBlank(endWorkTime)) {
      logger.warn(s"end_work_time invalid:$endWorkTime")
      endTag = true
    }

    if (startTag && endTag) {
      return SparkCommon.ALL_WORK_TIME_INVALID_TAG
    }

    if (startTag) {
      return SparkCommon.START_WORK_TIME_INVALID_TAG
    }

    if (endTag) {
      return SparkCommon.END_WORK_TIME_INVALID_TAG
    }

    null
  }

}
