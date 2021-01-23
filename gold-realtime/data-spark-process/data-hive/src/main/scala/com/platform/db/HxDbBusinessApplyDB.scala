package com.platform.db

import java.sql.PreparedStatement

import com.alibaba.druid.pool.DruidPooledConnection
import com.platform.conf.AppConfig
import com.platform.utils.ConnectionFactory
import org.apache.spark.sql.DataFrame
import org.slf4j.{Logger, LoggerFactory}

case class HxDbBusinessApplyDB() {

  val logger: Logger = LoggerFactory.getLogger(getClass)

  def saveBusinessApplyChannel(df:DataFrame,timeType:String): Unit ={
    var connect: DruidPooledConnection = null
    var pstmt : PreparedStatement = null
    val tableName = "business_apply_channel_count_"+timeType
    try{
      connect =  ConnectionFactory.getConnection
      connect.setAutoCommit(false)
      val sql = s"insert into $tableName(channel,apply_adopt,apply_refuse,apply_cancel,apply_ing,apply_percent,begin_time,end_time) values(?,?,?,?,?,?,?,?) "
      pstmt = connect.prepareStatement(sql)
      val batchSize = AppConfig.MYSQL_CONNECT_BATCH_SIZE
      var size = 0
      df.collect().foreach(r=>{
        size=size+1
        pstmt.setString(1,r.getAs[String]("channel"))
        pstmt.setLong(2,r.getAs[Long]("apply_adopt"))
        pstmt.setLong(3,r.getAs[Long]("apply_refuse"))
        pstmt.setLong(4,r.getAs[Long]("apply_cancel"))
        pstmt.setLong(5,r.getAs[Long]("apply_ing"))
        pstmt.setString(6,r.getAs[java.math.BigDecimal]("apply_percent").toString)
        pstmt.setString(7,r.getAs[Long]("begin_time").toString)
        pstmt.setString(8,r.getAs[Long]("end_time").toString)
        pstmt.addBatch()
        if (size == batchSize.toInt){
          pstmt.executeBatch()
          pstmt.clearBatch()
        }
      })
      pstmt.executeBatch()
      pstmt.clearBatch()
      connect.commit()

    }catch {
      case e: Exception =>
        logger.error(s"saveBusinessApplyChannel to db error $e")
    }finally {
      ConnectionFactory.closeConnection(null, pstmt, connect)
    }
  }

}
