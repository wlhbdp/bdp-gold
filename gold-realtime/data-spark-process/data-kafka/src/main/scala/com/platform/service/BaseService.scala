package com.platform.service

import com.platform.db.BaseDB
import org.apache.kafka.common.TopicPartition
import org.slf4j.{Logger, LoggerFactory}

object BaseService {

  val logger: Logger = LoggerFactory.getLogger(getClass)

  /**
    * Retrieve committed kafka topic, partition_num, from_offset from DB
    *
    * @return kafka topic, partition_num, from_offset
    */
  def getOffsetFromDB : String = {
    BaseDB.findOffset
  }

  /**
    * save first setup offset
    */
  def saveOffset(topic: String, partition_num: Int, latest_offset: Long) = {
    BaseDB.saveOffsetToDB(topic, partition_num, latest_offset)
  }


  def updateOffset(endOffset : String,startOffset:String) : Int = {
    BaseDB.updateOffsetDB(endOffset,startOffset);
  }


  def findOffsetCount : Int = {
    BaseDB.findOffsetCount;
  }

}
