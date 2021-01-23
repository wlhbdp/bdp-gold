package com.platform.service

import com.platform.db.mysql.BaseDB
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.spark.rdd.RDD
import org.slf4j.{Logger, LoggerFactory}

case class BaseService() {

  val logger: Logger = LoggerFactory.getLogger(getClass)

  /**
    * Retrieve committed kafka topic, partition_num, from_offset from DB
    *
    * @return kafka topic, partition_num, from_offset
    */
  def getOffsetFromDB(groupId:String): Map[TopicPartition, Long] = {
    BaseDB().findOffset(groupId)
  }

  /**
    * save first setup offset
    */
  def saveOffset(topic: String, partition_num: Int, latest_offset: Long,groupId:String) = {
    BaseDB().saveOffsetToDB(topic, partition_num, latest_offset,groupId)
  }


  def updateOffset(rdd: RDD[ConsumerRecord[String, String]],groupId:String) = {
    BaseDB().updateOffsetDB(rdd,groupId);
  }


  def findOffsetCount(groupId:String,topic:String) : Int = {
    BaseDB().findOffsetCount(groupId,topic);
  }

}
