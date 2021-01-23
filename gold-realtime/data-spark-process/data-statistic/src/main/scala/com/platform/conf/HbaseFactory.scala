package com.platform.conf


import java.util

import com.platform.common.{HbaseConstant, SparkCommon}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client.{Connection, Put, Table}
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.slf4j.{Logger, LoggerFactory}

object HbaseFactory {

  private val logger: Logger = LoggerFactory.getLogger(getClass)

  private var connect: Connection = _


  private def getConfiguration: Configuration = {
    val configuration = HBaseConfiguration.create()
    configuration.set(SparkCommon.HBASE_ZOOKEEPER_QUORUM, AppConfig.HBASE_ZK_QUORUM)
    configuration.set(SparkCommon.HBASE_ZOOKEEPER_PORT, AppConfig.HBASE_ZK_PORT)
    //configuration.set(SparkCommon.HBASE_CLIENT_POOL_TYPE,AppConfig.HBASE_CLT_POOL_TYPE)
    //configuration.set(SparkCommon.HBASE_CLIENT_POOL_SIZE,AppConfig.HBASE_CLT_POOL_SIZE)
    //configuration.set(SparkCommon.HBASE_ZNODE_PARENT,AppConfig.HBASE_ZN_PARENT)
    configuration
  }


  def getConnection(): Connection = {
    val configuration: Configuration = getConfiguration
    if (null == connect) {
      this.synchronized {
        //connection是有效的直接返回
        if (null != connect) {
          if (!connect.isClosed && !connect.isAborted) {
            return connect
          }
        }

        //关闭hbase连接
        try {
          closeConnection(null, connect)
        } catch {
          case e: Exception =>
            logger.error(s"hbase connection close error $e")
        }

        //创建hbase连接
        try {
          import org.apache.hadoop.hbase.client.ConnectionFactory
          connect = ConnectionFactory.createConnection(configuration)
        } catch {
          case e: Exception =>
            logger.error(s"hbase create connection error: $e")
        }

      }
    }

    connect

  }

  def addPuts(tableName: String, putList: util.List[Put]): Boolean = {
    var table: Table = null
    try {
      connect = getConnection()
      table = connect.getTable(TableName.valueOf(tableName))
      table.put(putList)
      true
    } catch {
      case e: Exception =>
        logger.error(s"$tableName  save hbase error $e");
        false
    }
  }


  def closeConnection(table: Table, connect: Connection): Unit = {
    if (null != table) {
      table.close()
    }

    if (null != connect) {
      connect.close()
    }
  }


}
