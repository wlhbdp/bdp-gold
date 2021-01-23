package com.platform.db.mysql

import java.sql.PreparedStatement

import com.alibaba.druid.pool.DruidPooledConnection
import com.platform.conf.ConnectionFactory
import org.apache.spark.rdd.RDD
import org.slf4j.{Logger, LoggerFactory}

/**
  * app通讯录相关规则保存db
  */
case class AppContactDB() {

  val logger: Logger = LoggerFactory.getLogger(getClass)

  def saveAppContactCount(rdd: RDD[(String, Long)]) = {
    rdd.foreachPartition(result => {
      var connect: DruidPooledConnection = null;
      var pstmt: PreparedStatement = null;
      try {
        connect = ConnectionFactory.getConnection
        connect.setAutoCommit(false)
        val sql = "INSERT INTO app_contact_count(token,search_count) VALUES (?, ?)"
        pstmt = connect.prepareStatement(sql)
        result.foreach(record => {
          pstmt.setString(1, record._1);
          pstmt.setLong(2, record._2);
          pstmt.addBatch();
        })
        pstmt.executeBatch()
        connect.commit()
      } catch {
        case e: Exception => {
          logger.error(s"saveAppContactCount to db error $e");
        }
      } finally {
        ConnectionFactory.closeConnection(null, pstmt, connect)
      }
    })
  }


  def saveAppContactLoanWordsCount(rdd: RDD[(String, Long)]) = {
    rdd.foreachPartition(result => {
      var connect: DruidPooledConnection = null;
      var pstmt: PreparedStatement = null;
      try {
        connect = ConnectionFactory.getConnection
        connect.setAutoCommit(false)
        val sql = "INSERT INTO app_contact_loan_count(token,search_count) VALUES (?, ?)"
        pstmt = connect.prepareStatement(sql)
        result.foreach(record => {
          pstmt.setString(1, record._1);
          pstmt.setLong(2, record._2);
          pstmt.addBatch();
        })
        pstmt.executeBatch()
        connect.commit()
      } catch {
        case e: Exception => {
          logger.error(s"saveAppContactLoanWordsCount to db error $e");
        }
      } finally {
        ConnectionFactory.closeConnection(null, pstmt, connect)
      }
    })
  }


  def saveAppContactSensitiveWords(rdd: RDD[(String, Long)]) = {
    rdd.foreachPartition(result => {
      var connect: DruidPooledConnection = null;
      var pstmt: PreparedStatement = null;
      try {
        connect = ConnectionFactory.getConnection
        connect.setAutoCommit(false)
        val sql = "INSERT INTO app_contact_sensitive_count(token,search_count) VALUES (?, ?)"
        pstmt = connect.prepareStatement(sql)
        result.foreach(record => {
          pstmt.setString(1, record._1);
          pstmt.setLong(2, record._2);
          pstmt.addBatch();
        })
        pstmt.executeBatch()
        connect.commit()
      } catch {
        case e: Exception => {
          logger.error(s"saveAppContactSensitiveWords to db error $e");
        }
      } finally {
        ConnectionFactory.closeConnection(null, pstmt, connect)
      }
    })
  }


}
