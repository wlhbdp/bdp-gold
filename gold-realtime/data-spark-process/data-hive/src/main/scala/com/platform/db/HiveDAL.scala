package com.platform.db

import com.alibaba.fastjson.{JSON, JSONObject}
import com.platform.bean.TemplateV2
import org.apache.spark.sql.{DataFrame, SaveMode}


/**
  *
  * author: GaoPan
  * create: 2019-05-08 14:45
  */
case class HiveDAL() {

  def save(df:DataFrame,mapping: JSONObject): Unit = {
    val jsonConfig:TemplateV2 = JSON.parseObject(mapping.toJSONString, classOf[TemplateV2])
    val targetDest = jsonConfig.getTargetDest
    val dbName = targetDest.getDbName
    val tableName = targetDest.getTableName
    val columns = targetDest.getColumns
    val extra = jsonConfig.getExtra
    //打印要保存的表字段信息

    if (extra == null || extra.isEmpty) {
      //默认如果extra为空的话，则以无分区、parquet格式追加存储数据(需要用hive来格式化，否则会报错)
      df.write.mode(SaveMode.Append).format("hive").saveAsTable(s"$dbName.$tableName")
    } else {
      val saveMode:String = extra.getOrDefault("saveMode", "append").toString
      val saveFormat:String = extra.getOrDefault("saveFormat", "parquet").toString
      val partitionFieldName = extra.get("partitionFieldName")
      if (partitionFieldName == null) {
        df.write.mode(saveMode).format(saveFormat).saveAsTable(s"$dbName.$tableName")
      } else {
//        val ptStrategy = extra.getOrDefault("partitionStrategy", "month").toString
        df.write.mode(saveMode).format(saveFormat).partitionBy(partitionFieldName.toString)
          .saveAsTable(s"$dbName.$tableName")
      }
    }
  }
}
