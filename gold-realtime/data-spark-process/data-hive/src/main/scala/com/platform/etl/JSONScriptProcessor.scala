package com.platform.etl

import com.alibaba.fastjson.JSONObject
import com.platform.conf.SqlConfiguration
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable.ArrayBuffer

/**
  *
  * author: GaoPan
  * create: 2019-05-08 10:16
  */
object JSONScriptProcessor {
  private val logger:Logger = LoggerFactory.getLogger(getClass)

  def parseSqlConf(tableMapping:JSONObject): ArrayBuffer[SqlConfiguration] = {
    val steps = tableMapping.getJSONArray("steps")
    val version = tableMapping.getString("version")
    val sqlConfigurations = ArrayBuffer[SqlConfiguration]()

    if (version == null) {
      throw new RuntimeException(s"Json config script is not valid, please check it. [$tableMapping]")
    } else {
        for (n <- 0 until steps.size()) {
          val data = steps.getJSONObject(n)
          val sqlConf = SqlConfiguration(data.getString("sql"), data.getString("tempView"), data.getJSONObject("macros"))
          sqlConfigurations.+=(sqlConf)
        }

    }
    sqlConfigurations
  }

}
