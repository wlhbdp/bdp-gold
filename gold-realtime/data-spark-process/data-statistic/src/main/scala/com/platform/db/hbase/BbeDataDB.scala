package com.platform.db.hbase

import java.util

import com.alibaba.fastjson.JSONObject
import com.platform.common.{DateUtils, HbaseColConstant, HbaseConstant, HbaseUtils, SparkCommon}
import com.platform.conf.HbaseFactory
import org.apache.commons.lang3.StringUtils
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.util.{Bytes, MD5Hash}
import org.apache.spark.rdd.RDD
import org.slf4j.{Logger, LoggerFactory}

/**
  * 把BBE考勤数据保存到hbase
  */
case class BbeDataDB() {

  private val logger: Logger = LoggerFactory.getLogger(getClass)

  def saveComputedTimeData(rdd: RDD[(JSONObject, Long)]): Unit = {
    val putList: util.List[Put] = new util.ArrayList[Put]
    var put: Put = null

    //整理需要要保存的数据
    rdd.collect.foreach(e => {
      try {
        val timeData = e._1

        val empNo = timeData.getString(HbaseColConstant.EMP_NO_COL)
        val workDate = timeData.getString(HbaseColConstant.WORK_DATE_COL)

        val rowKey = new StringBuffer(MD5Hash.getMD5AsHex(HbaseUtils.toBytes(empNo)))
          .append("-")
          .append(getWorkDateMilli(workDate))
        put = new Put(Bytes.toBytes(rowKey.toString), getWorkDateMilli(workDate))
        put.addColumn(HbaseConstant.FAMILY_BBE_WOKE_ATTENDANCE_TIME, Bytes.toBytes(HbaseColConstant.EMP_NO_COL), HbaseUtils.toBytes(empNo))
        put.addColumn(HbaseConstant.FAMILY_BBE_WOKE_ATTENDANCE_TIME, Bytes.toBytes(HbaseColConstant.WORK_DATE_COL), HbaseUtils.toBytes(workDate))
        put.addColumn(HbaseConstant.FAMILY_BBE_WOKE_ATTENDANCE_TIME, Bytes.toBytes(HbaseColConstant.DURATION_COL), HbaseUtils.toBytes(timeData.getString(HbaseColConstant.DURATION_COL)))
        putList.add(put)
      } catch {
        case e: Exception => {
          logger.error(s"saveAppContactCount to db error $e");
        }
      }
    })

    //保存数据
    HbaseFactory.addPuts(HbaseConstant.TABLE_BBE_WOKE_ATTENDANCE_TIME, putList)

  }

  def saveInvalidData(rdd: RDD[(JSONObject, Long)]): Unit = {

    val putList: util.List[Put] = new util.ArrayList[Put]
    var put: Put = null

    //整理需要要保存的数据
    rdd.collect.foreach(e => {
      try {
        val invalidData = e._1
        val empNo = invalidData.getString(HbaseColConstant.EMP_NO_COL)
        val workDate = invalidData.getString(HbaseColConstant.WORK_DATE_COL)
        val rowKey = new StringBuffer(MD5Hash.getMD5AsHex(HbaseUtils.toBytes(empNo)))
          .append("-")
          .append(getWorkDateMilli(workDate))
        put = new Put(Bytes.toBytes(rowKey.toString), getWorkDateMilli(workDate))
        put.addColumn(HbaseConstant.FAMILY_BBE_WOKE_NOT_ATTENDANCE, Bytes.toBytes(HbaseColConstant.EMP_NO_COL), HbaseUtils.toBytes(empNo))
        put.addColumn(HbaseConstant.FAMILY_BBE_WOKE_NOT_ATTENDANCE, Bytes.toBytes(HbaseColConstant.WORK_DATE_COL), HbaseUtils.toBytes(workDate))
        put.addColumn(HbaseConstant.FAMILY_BBE_WOKE_NOT_ATTENDANCE, Bytes.toBytes(HbaseColConstant.IS_WORK_COL), HbaseUtils.toBytes(invalidData.getString(HbaseColConstant.IS_WORK_COL)))
        put.addColumn(HbaseConstant.FAMILY_BBE_WOKE_NOT_ATTENDANCE, Bytes.toBytes(HbaseColConstant.TAG_COL), HbaseUtils.toBytes(invalidData.getString(HbaseColConstant.TAG_COL)))
        putList.add(put)
      } catch {
        case e: Exception => {
          logger.error(s"saveAppContactCount to db error $e")
        }
      }
    })

    HbaseFactory.addPuts(HbaseConstant.TABLE_BBE_WOKE_NOT_ATTENDANCE, putList)
  }

  private def getWorkDateMilli(workDate: String): Long = {
    if (StringUtils.isNotBlank(workDate)) {
      DateUtils.dateToMilli(workDate, SparkCommon.DATE_TIME_FORMAT_DAY)
    } else {
      System.currentTimeMillis()
    }
  }

}
