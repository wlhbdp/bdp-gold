package com.platform.db

import java.sql.{PreparedStatement, Timestamp}
import java.util.Date

import com.alibaba.druid.pool.DruidPooledConnection
import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import com.platform.bean.TemplateV2
import com.platform.conf.AppConfig
import com.platform.utils.ConnectionFactory
import org.apache.commons.lang3.time.DateUtils
import org.apache.spark.sql.DataFrame
import org.slf4j.{Logger, LoggerFactory}

/**
  *
  * author: GaoPan
  * create: 2019-05-07 16:36
  */
case class MySQLDAL() {
  private val logger:Logger = LoggerFactory.getLogger(getClass)

  def save(df:DataFrame, mapping: JSONObject):Unit = {
    var connection:DruidPooledConnection = null
    var ps:PreparedStatement = null
    val (tableName, createSql, typeName, fieldOrder) = buildCreateSqlAndFieldInfo(mapping)

    try {
      connection = ConnectionFactory.getConnection
      connection.setAutoCommit(false)
      ps = connection.prepareStatement(createSql)

      val batchSize = AppConfig.MYSQL_CONNECT_BATCH_SIZE
      var size = 0

      df.collect().foreach(row => {
        if (AppConfig.RUN_MODE_STANDALONE) {
          println("row: " + row)
        }
        size += 1
        import scala.collection.JavaConversions._
        for (entry <- typeName.entrySet()) {
          val fieldName = entry.getKey
          val fieldType = entry.getValue
          fieldType match {
            case "string" => ps.setString(fieldOrder.getIntValue(fieldName), String.valueOf(row.getAs[String](fieldName)))
            case "long" => ps.setLong(fieldOrder.getIntValue(fieldName), row.getAs[Long](fieldName).longValue())
            case "int" => ps.setInt(fieldOrder.getIntValue(fieldName), row.getAs[Int](fieldName))
            case "double" => ps.setDouble(fieldOrder.getIntValue(fieldName), row.getAs[Double](fieldName))
            case "date" => try {
              val date:Date = DateUtils.parseDate(row.getAs(fieldName).toString,"yyyyMMddHHmmss")
              ps.setTimestamp(fieldOrder.getIntValue(fieldName), new Timestamp(date.getTime))
            } catch {
              case e:Exception =>
                logger.error("set date:" + e.getMessage)
                ps.setDate(fieldOrder.getIntValue(fieldName), row.getAs(fieldName))
            }
          }
        }
        ps.addBatch()
        if (size == Integer.parseInt(batchSize)) {
          ps.executeBatch()
          ps.clearBatch()
          size = 0
        }
      })
      ps.executeBatch()
      ps.clearBatch()
      connection.commit()
    } catch {
      case exception: Exception =>
        logger.error(s"save data to $tableName failure! $exception")
        throw exception
    } finally {
      ConnectionFactory.closeConnection(null, ps, connection)
    }
  }

  private def buildCreateSqlAndFieldInfo(mapping: JSONObject):(String, String, JSONObject, JSONObject) = {
    val (targetName, dbName, tableName, columns) = parseSqlInfo(mapping)
    val fields = new StringBuilder("")
    val placeHolders = new StringBuilder("")
    val typeName = new JSONObject()
    val fieldOrder = new JSONObject()

    for (index <- 0 until columns.size()) {
      val col:JSONObject = columns.getJSONObject(index)
      val fieldName = col.getString("name")
      val fieldType = col.getString("type")
      fields.append(fieldName)
      placeHolders.append("?")
      if (index < columns.size()-1) {
        fields.append(",")
        placeHolders.append(",")
      }
      typeName.put(fieldName, fieldType)
      fieldOrder.put(fieldName, index+1)
    }
    val sql = s"insert into $dbName.$tableName($fields) values($placeHolders)"
    println(s"Finally insert result into $targetName:\n$sql")
    println("typeName:" + typeName)
    println("fieldOrder: " + fieldOrder)
    (tableName,sql,typeName, fieldOrder)
  }

  private def parseSqlInfo(mapping: JSONObject):(String,String,String,JSONArray) = {
    val version = mapping.getString("version")
    version match {
      case "1.0" => ("MySQL", "`fox-data-bi`", mapping.getString("tableName"), mapping.getJSONArray("columns"))
      case "2.0" =>
        val jsonConfig:TemplateV2 = JSON.parseObject(mapping.toJSONString, classOf[TemplateV2])
        val targetDest = jsonConfig.getTargetDest
        val columns = targetDest.getColumns
        val jsonArray: JSONArray = new JSONArray()
        for(n <- 0 until columns.size()) {
          val jsonObj = new JSONObject()
          jsonObj.put("name", columns.get(n).getFieldName)
          jsonObj.put("type", columns.get(n).getFieldType)
          jsonArray.add(jsonObj)
        }
        (targetDest.getTargetName, targetDest.getDbName, targetDest.getTableName, jsonArray)
      case _ => throw new RuntimeException(s"Unsupported version:$version, please check the json script!")
    }
  }
}
