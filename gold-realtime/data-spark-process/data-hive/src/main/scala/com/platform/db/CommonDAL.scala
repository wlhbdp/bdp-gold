package com.platform.db

import com.alibaba.fastjson.{JSON, JSONObject}
import com.platform.bean.TemplateV2
import org.apache.spark.sql.DataFrame


/**
  *
  * author: GaoPan
  * create: 2019-03-06 21:39
  */
case class CommonDAL() {

  def save(df:DataFrame,timeType:String,mapping: JSONObject):Unit = {
    getTargetName(mapping) match {
      case "MySQL" => MySQLDAL().save(df, mapping)
      case "Hive" => HiveDAL().save(df, mapping)
    }
  }

  /**
    * 获取数据保存的目标源名称
    * @param mapping json格式的配置文件
    * @return
    */
  private def getTargetName(mapping: JSONObject):String = {
    val version:String = mapping.getString("version")
    version match {
      case "1.0" => "MySQL"
      case "2.0" =>
        val jsonConfig:TemplateV2 = JSON.parseObject(mapping.toJSONString, classOf[TemplateV2])
        val targetDest = jsonConfig.getTargetDest
        targetDest.getTargetName
      case _ => throw new RuntimeException(s"Unsupported version:$version, please check the json script!")
    }
  }

}

