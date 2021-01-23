package com.platform.stream


import com.alibaba.fastjson.{JSON, JSONException, JSONObject}
import com.platform.common.{DataTypeCommon, HbaseColConstant, SparkCommon}
import com.platform.rdd._
import org.apache.commons.lang3.StringUtils
import org.apache.spark.rdd.RDD
import org.slf4j.{Logger, LoggerFactory}




case class DataAnalyticsOperate() {

  private val logger: Logger = LoggerFactory.getLogger(getClass)


  def originDataHandler(line: String): Option[JSONObject] = {
    var res: Option[JSONObject] = None
    try {
      res = line match {
        case null => None
        case _ => {
          val json = JSON.parseObject(line)
          Some(json)
        }
      }

    } catch {
      case ex: JSONException => {
        logger.error("数据转换Json失败", ex);
      }
    }
    res
  }


  /**
    * 过滤BBE考勤数据
    *
    * @param data
    * @return
    */
  def filterFuncForBbeData(data: JSONObject): Boolean = {
    val res = data.getString(DataTypeCommon.RISK_RULE_DATA_FIELD_TYPE) match {
      case DataTypeCommon.RISK_RULE_DATA_FIELD_BBE => true
      case _ => false
    }
    res
  }

  /**
    * 过滤出有效的考勤数据
    *
    * @param attendance
    * @return
    */
  def hasValidAttendance(attendance: JSONObject): Boolean = {
    val empNo = attendance.getString(HbaseColConstant.EMP_NO_COL)
    if (StringUtils.isBlank(empNo)) {
      logger.warn(s"emp_no invalid:$empNo")
      return false
    }

    val workDate = attendance.getString(HbaseColConstant.WORK_DATE_COL)
    if (StringUtils.isBlank(workDate) || !workDate.matches(SparkCommon.DATE_TIME_FORMAT_DAY_REGEX)) {
      logger.warn(s"work_date invalid:$workDate")
      return false
    }

    val startWorkTime = attendance.getString(HbaseColConstant.START_WORK_TIME)
    if (StringUtils.isBlank(startWorkTime)) {
      logger.warn(s"start_work_time invalid:$startWorkTime")
      return false
    }

    val endWorkTime = attendance.getString(HbaseColConstant.END_WORK_TIME)
    if (StringUtils.isBlank(endWorkTime)) {
      logger.warn(s"end_work_time invalid:$endWorkTime")
      return false
    }

    val isWork = attendance.getString(HbaseColConstant.IS_WORK_COL)
    if (StringUtils.isBlank(isWork) || !HbaseColConstant.IS_WORK_COL_VALID_VALUE.equals(isWork.toUpperCase().trim)) {
      logger.warn(s"isWork invalid:$isWork")
      return false
    }

    true
  }


  /**
    * 过滤app通讯录数据
    *
    * @param data
    * @return
    */
  def filterFuncForAppContactData(data: JSONObject): Boolean = {
    val res = data.getString(DataTypeCommon.RISK_RULE_DATA_FIELD_TYPE) match {
      case DataTypeCommon.RISK_RULE_DATA_FIELD_APP_CONTACT => true
      case _ => false
    }
    res
  }

  /**
    * 处理app通讯录相关规则数据
    * @param words
    */
  def processAppContactRecordData(words: RDD[JSONObject]): Unit = {
    RDDAppContactCount().clean(words);
    RDDAppContactLoanWords().clean(words);
    RDDAppContactSensitiveWords().clean(words);
  }


  /**
    * 处理BBE考勤数据
    *
    * @param words
    * @return
    */
  def processBbeData(words: RDD[JSONObject]): Unit = {
    RDDBbeAttendanceTime().clean(words)
    RDDBbeInvalidAttendance().clean(words)
  }

}
