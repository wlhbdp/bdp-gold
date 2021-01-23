package com.platform.db.mysql

import java.sql.{PreparedStatement, ResultSet}

import com.alibaba.druid.pool.DruidPooledConnection
import com.platform.conf.{AppConfig, ConnectionFactory}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.kafka010.HasOffsetRanges
import org.slf4j.{Logger, LoggerFactory}

case class BaseDB() {

  val logger: Logger = LoggerFactory.getLogger(getClass)


  //查询offset

  def findOffset(groupId: String): Map[TopicPartition, Long] = {
    var fromOffsets: Map[TopicPartition, Long] = Map();
    var connect: DruidPooledConnection = null;
    var pstmt: PreparedStatement = null;
    var rs: ResultSet = null;
    try {
      connect = ConnectionFactory.getConnection
      pstmt = connect.prepareStatement(s"""select topic, partition_num, from_offset from ${AppConfig.TABLE_KAFKA_OFFSET} where group_id = ?""");
      pstmt.setString(1, groupId);

      rs = pstmt.executeQuery();
      while (rs.next()) {
        fromOffsets += (new TopicPartition(rs.getString("topic"), rs.getInt("partition_num")) -> rs.getLong("from_offset"));
      }
      logger.info(s"Kafka Offsets query from DB is ${fromOffsets.mkString("[", " , ", "]")}")
    } catch {
      case e: Exception => {
        logger.error(s"findOffset from db error $e")
        throw new IllegalStateException(
          s"findOffset from db error $e")
      }
    } finally {
      ConnectionFactory.closeConnection(rs, pstmt, connect);
    }
    fromOffsets
  }


  def saveOffsetToDB(topic: String, partition_num: Int, latest_offset: Long, groupId: String): Unit = {
    var connect: DruidPooledConnection = null;
    var pstmt: PreparedStatement = null;
    var successCount = 0;
    try {
      connect = ConnectionFactory.getConnection
      pstmt = connect.prepareStatement(s"""insert into ${AppConfig.TABLE_KAFKA_OFFSET}(topic,partition_num,from_offset,group_id) values(?,?,?,?)""");
      pstmt.setString(1, topic);
      pstmt.setInt(2, partition_num);
      pstmt.setLong(3, latest_offset);
      pstmt.setString(4, groupId);

      successCount = pstmt.executeUpdate();
      if (successCount <= 0) {
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
    } finally {
      ConnectionFactory.closeConnection(null, pstmt, connect);
    }
  }

  def updateOffsetDB(rdd: RDD[ConsumerRecord[String, String]], groupId: String) = {
    val hasOffsetRanges = rdd.asInstanceOf[HasOffsetRanges];
    var connect: DruidPooledConnection = null;
    var pstmt: PreparedStatement = null;
    var successCount = 0;
    try {
      connect = ConnectionFactory.getConnection
      connect.setAutoCommit(false)

      pstmt = connect.prepareStatement(s"""update ${AppConfig.TABLE_KAFKA_OFFSET} set from_offset = ? , update_time=now() where group_id= ? and topic = ? and partition_num = ? and from_offset = ?""")
      hasOffsetRanges.offsetRanges.foreach(record => {
        successCount = 0;
        pstmt.setLong(1, record.untilOffset);
        pstmt.setString(2, groupId);
        pstmt.setString(3, record.topic);
        pstmt.setInt(4, record.partition);
        pstmt.setLong(5, record.fromOffset);
        successCount = pstmt.executeUpdate();
        if (successCount != 1) {
          logger.error(
            s"""Got $successCount rows affected instead of 1 when attempting to update kafka offsets in DB
               |for ${record.topic} ${record.partition} ${record.fromOffset} -> ${record.untilOffset}.
               |Was a partition repeated after a worker failure?""".stripMargin)
          throw new IllegalStateException(
            s"""Got $successCount rows affected instead of 1 when attempting to update kafka offsets in DB
               |for ${record.topic} ${record.partition} ${record.fromOffset} -> ${record.untilOffset}.
               |Was a partition repeated after a worker failure?""".stripMargin)
        }
      })
      connect.commit()
    } catch {
      case e: Exception => {
        logger.error(s"update offset to db error $e");
        throw new IllegalStateException(
          s"update offset to db error $e")
      }
    } finally {
      ConnectionFactory.closeConnection(null, pstmt, connect);
    }
  }

  //查询db是否存在记录
  def findOffsetCount(groupId: String, topic: String): Int = {
    var count = 0;
    var connect: DruidPooledConnection = null;
    var pstmt: PreparedStatement = null;
    var rs: ResultSet = null;
    try {
      connect = ConnectionFactory.getConnection
      pstmt = connect.prepareStatement(s"""select count(*) from ${AppConfig.TABLE_KAFKA_OFFSET} where group_id = ? and topic = ?""");
      pstmt.setString(1, groupId)
      pstmt.setString(2, topic)
      rs = pstmt.executeQuery();
      while (rs.next()) {
        count = rs.getInt(1);
      }
    } catch {
      case e: Exception => {
        logger.error(s"findOffsetCount from db error $e")
        throw new IllegalStateException(
          s"findOffsetCount from db error $e")
      }
    } finally {
      ConnectionFactory.closeConnection(rs, pstmt, connect);
    }
    count;
  }


}
