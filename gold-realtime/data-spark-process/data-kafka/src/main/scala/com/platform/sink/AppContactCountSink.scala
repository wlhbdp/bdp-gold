package com.platform.sink

import java.sql.PreparedStatement

import com.alibaba.druid.pool.DruidPooledConnection
import com.platform.conf.ConnectionFactory
import org.apache.spark.sql.{ForeachWriter, Row}
import org.slf4j.LoggerFactory

class AppContactCountSink() extends ForeachWriter[Row]{

  @transient lazy val logger = LoggerFactory.getLogger(getClass);

  var connection: DruidPooledConnection = _
  var statement: PreparedStatement = _

  private val sql = "INSERT INTO app_contact_count(token,search_count) VALUES (?, ?)"


  def open(partitionId: Long, version: Long): Boolean = {
    connection = ConnectionFactory.getConnection
    connection.setAutoCommit(false)
    statement = connection.prepareStatement(sql)
    true
  }

  def process(row: Row): Unit = {
    statement.setString(1,row.getString(0));
    statement.setLong(2,row.getLong(1));
    statement.addBatch();
  }

  def close(errorOrNull: Throwable): Unit = {
    statement.executeBatch
    connection.commit()
    ConnectionFactory.closeConnection(null,statement,connection);
  }
}
