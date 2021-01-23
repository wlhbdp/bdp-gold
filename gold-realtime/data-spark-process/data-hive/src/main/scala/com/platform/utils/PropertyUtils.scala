package com.platform.utils

import java.util.Properties

import org.slf4j.{Logger, LoggerFactory}

/**
  * 配置文件工具
  * Created by cai.chen on 2018/11/06.
  */
object PropertyUtils {

  @transient lazy private val logger: Logger = LoggerFactory.getLogger(PropertyUtils.getClass)

  private val pro = new Properties();

  /**
    * 初始化配置文件
    * @param propertyFileName 配置文件名称
    * @return 配置文件对象
    */
  def init(propertyFileName: String): Properties = {
    try {
      val inputStream = PropertyUtils.getClass.getClassLoader.getResourceAsStream(propertyFileName)
      pro.load(inputStream)
      pro
    } catch {
      case ex: Exception =>
        logger.error(s"加载配置[$propertyFileName]异常==>{}",ex)
        throw new RuntimeException(s"加载配置[$propertyFileName]异常", ex)
    }
  }

}
