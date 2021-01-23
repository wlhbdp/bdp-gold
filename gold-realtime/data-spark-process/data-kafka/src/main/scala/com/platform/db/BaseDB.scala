package com.platform.db

import java.sql.{PreparedStatement, ResultSet}

import com.alibaba.druid.pool.DruidPooledConnection
import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import com.platform.common.SparkCommon
import com.platform.conf.KafkaManager.conf
import com.platform.conf.{AppConfig, ConnectionFactory}
import org.slf4j.{Logger, LoggerFactory}




object BaseDB {

  val logger: Logger = LoggerFactory.getLogger(BaseDB.getClass)

  def main(args: Array[String]): Unit = {
    findOffset
  }


  //查询offset
  def findOffset  : String = {
    var connect : DruidPooledConnection = null;
    var pstmt : PreparedStatement = null;
    var rs : ResultSet = null;
    var offsetResult = "";
    try {
      connect = ConnectionFactory.getConnection
      pstmt = connect.prepareStatement(s"""select topic, partition_num, from_offset from ${AppConfig.TABLE_KAFKA_OFFSET}""");
      rs = pstmt.executeQuery();
      var topicMap = new JSONObject();
      var offsetObj =  new JSONObject();
      while(rs.next()){
        val topicName = rs.getString("topic");
        val topicObjDefault = topicMap.getOrDefault(topicName,new JSONObject());
        offsetObj = topicObjDefault.asInstanceOf[JSONObject];
        if (null==offsetObj && offsetObj.size()==0){
          offsetObj = new JSONObject();
        }else{
          offsetObj.put(rs.getInt("partition_num").toString,rs.getLong("from_offset"));
          topicMap.put(topicName,offsetObj);
        }
      }

      offsetResult = topicMap.toJSONString;
      logger.info(s"Kafka Offsets query from DB is ${offsetResult}")
    } catch {
      case e: Exception => {
        logger.error(s"find offset from db error $e")
        throw new IllegalStateException(
          s"find offset from db error $e")
      }
    }finally {
      ConnectionFactory.closeConnection(rs, pstmt, connect);
    }
    offsetResult
  }


  //保存offset
  def saveOffsetToDB(topic:String,partition_num:Int,latest_offset:Long) : Unit ={
    var connect : DruidPooledConnection = null;
    var pstmt : PreparedStatement = null;
    var successCount = 0;
    try {
      connect = ConnectionFactory.getConnection
      pstmt = connect.prepareStatement(s"""insert into ${AppConfig.TABLE_KAFKA_OFFSET}(topic,partition_num,from_offset) values(?,?,?)""");
      pstmt.setString(1, topic);
      pstmt.setInt(2, partition_num);
      pstmt.setLong(3, latest_offset);
      successCount = pstmt.executeUpdate();
      if (successCount<=0) {
        logger.error(s"save offset to db error : success count zero!");
        throw new Exception();
      }
    } catch {
      case e: Exception => {
        e.printStackTrace()
        logger.error(s"save offset to db error $e");
        throw new IllegalStateException(
          s"save offset to db error $e")
      }
    }finally {
      ConnectionFactory.closeConnection(null, pstmt, connect);
    }
  }


  //更新offset
  def updateOffsetDB(endOffset : String,startOffset:String):Int = {
    var connect: DruidPooledConnection = null;
    var pstmt : PreparedStatement = null;
    var successCount = 0;
    try {
      connect = ConnectionFactory.getConnection
      connect.setAutoCommit(false)
      pstmt = connect.prepareStatement("""update kafka_offset set from_offset = ? , update_time=now() where topic = ? and partition_num = ? and from_offset = ?""")

      val offset = JSON.parseObject(endOffset);
      val startOffsetObj = JSON.parseObject(startOffset);
      val topics = conf.getString(SparkCommon.SPARK_STREAMING_KAFKA_REALTIME_TOPICS).split(SparkCommon.DATA_FIELD_SUB_SPACE)
      var topicOffsets = new JSONObject();
      for (topic <- topics) {
        val topicInfo = offset.getOrDefault(topic,new JSONObject());
        topicOffsets = topicInfo.asInstanceOf[JSONObject];
        if (null != topicOffsets && topicOffsets.size()>0){
          val jsonKey = topicOffsets.keySet().iterator();
          while (jsonKey.hasNext){
            val key = jsonKey.next();
            pstmt.setLong(1,topicOffsets.getLong(key))
            pstmt.setString(2, topic)
            pstmt.setInt(3, key.toInt)
            pstmt.setLong(4, startOffsetObj.getJSONObject(topic).getLong(key))
            successCount = pstmt.executeUpdate();
            if (successCount != 1) {
              logger.error(
                s"""Got $successCount rows affected instead of 1 when attempting to update kafka offsets in DB
                   |for ${topic} ${key.toInt} ${startOffsetObj.getJSONObject(topic).getLong(key)} -> ${topicOffsets.getLong(key)}.
                   |Was a partition repeated after a worker failure?""".stripMargin)
              throw new IllegalStateException(
                s"""Got $successCount rows affected instead of 1 when attempting to update kafka offsets in DB
                   |for ${topic} ${key.toInt} ${startOffsetObj.getJSONObject(topic).getLong(key)} -> ${topicOffsets.getLong(key)}.
                   |Was a partition repeated after a worker failure?""".stripMargin)
            }

          }
        }
      }

      connect.commit()
    } catch {
      case e: Exception => {
        logger.error(s"update offset to db error $e");
        throw new IllegalStateException(
          s"update offset to db error $e")
      }
    }finally {
      ConnectionFactory.closeConnection(null, pstmt, connect);
    }
    successCount
  }

  //查询db是否存在记录
  def findOffsetCount : Int = {
    var count = 0;
    var connect : DruidPooledConnection = null;
    var pstmt : PreparedStatement = null;
    var rs : ResultSet = null;
    try {
      connect = ConnectionFactory.getConnection
      pstmt = connect.prepareStatement(s"""select count(*) from ${AppConfig.TABLE_KAFKA_OFFSET}""");
      rs = pstmt.executeQuery();
      while(rs.next()){
        count = rs.getInt(1);
      }
    } catch {
      case e: Exception => {
        logger.error(s"find offset from db error $e")
        throw new IllegalStateException(
          s"find offset from db error $e")
      }
    }finally {
      ConnectionFactory.closeConnection(rs, pstmt, connect);
    }
    count;
  }


}
