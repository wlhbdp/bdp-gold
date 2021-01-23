package com.platform.conf

import com.platform.common.SparkCommon
import com.platform.utils.PropertyUtils

object AppConfig {

  private val appConfigProps = PropertyUtils.init("application.properties")

  /**
    * 设置spark app的名字
    */
  val APP_NAME: String = appConfigProps.getProperty(SparkCommon.SPARK_CONFIG_APP_NAME);

  /**
    * check point url
    */
  val CHECK_POINT : String =  appConfigProps.getProperty(SparkCommon.SPARK_CONFIG_CHECK_POINT);
  /**
    * spark日志级别
    */
  val LOG_LEVEL : String = appConfigProps.getProperty(SparkCommon.SPARK_CONFIG_SET_LOG_LEVEL);


  /**
    * 内存for存储比例
    */
  val MEMORY_FOR_STORAGE_FRACTION : String = appConfigProps.getProperty(SparkCommon.SPARK_CONFIG_MEMORY_STORAGE_FRACTION);

  /**
    * 内存exceute的比例
    */
  val MEMORY_FOR_SHUFFLE_FRACTION : String = appConfigProps.getProperty(SparkCommon.SPARK_CONFIG_EXECUTOR_MEMORY_FRACTION);

  /**
    * set user memory在整个 executor memory 的占比。
    */
  val MEMORY_FRACTION : String = appConfigProps.getProperty(SparkCommon.SPARK_CONFIG_MEMORY_FRACTION);
  /**
    * 是否开启调整内存存储比例功能
    */
  val MEMORY_LEGACY_MODE : String = appConfigProps.getProperty(SparkCommon.SPARK_CONFIG_MEMORY_USE_LEGACY_MODE);


  /**
    * shuffle类型
    */
  val SHUFFLE_TYPE : String = appConfigProps.getProperty(SparkCommon.SPARK_SHUFFLE_TYPE);

  /**
    * shuffle缓存大小
    */
  val SHUFFLE_BUFF_SIZE : String = appConfigProps.getProperty(SparkCommon.SPARK_SHUFFLE_BUFFILE_SIZE);

  /**
    * shuffle read 拉取数据的最大次数
    */
  val SHUFFLE_FETCH_DATA_TIMES : String = appConfigProps.getProperty(SparkCommon.SPARK_SHUFFLE_BUFFILE_MAXTRY);

  /**
    * shuffle read 拉取数据的间隔
    */

  val SHUFFLE_FECTH_DATA_INTERVAL : String = appConfigProps.getProperty(SparkCommon.SPARK_SHUFFLE_BUFFILE_FETCH_INTERVAL);

  /**
    * spark 程序序列化方式
    */
  val SERIALIZER_TYPE : String = appConfigProps.getProperty(SparkCommon.SPARK_CONFIG_SERIALIZER_TYPE);



  /**
    * spark-sql
    */

  val SPARK_SQL_CASESENSITIVE = appConfigProps.getProperty(SparkCommon.SPARK_SQL_CASESENSITIVE);

  val SPARK_SQL_USER_DB = appConfigProps.getProperty(SparkCommon.SPARK_SQL_USER_DB);

  /**
    * hive
    */
  val HIVE_EXEC_DYNAMIC_PARTITION = appConfigProps.getProperty(SparkCommon.HIVE_EXEC_DYNAMIC_PARTITION);

  val HIVE_EXEC_DYNAMIC_PARTITION_MODE = appConfigProps.getProperty(SparkCommon.HIVE_EXEC_DYNAMIC_PARTITION_MODE);

  val HIVE_EXEC_COMPRESS_OUTPUT = appConfigProps.getProperty(SparkCommon.HIVE_EXEC_COMPRESS_OUTPUT);

  val HIVE_INPUT_FORMAT = appConfigProps.getProperty(SparkCommon.HIVE_INPUT_FORMAT);

  val HIVE_MERGE_MAPFILES = appConfigProps.getProperty(SparkCommon.HIVE_MERGE_MAPFILES);

  val HIVE_MERGE_MAPREDFILES = appConfigProps.getProperty(SparkCommon.HIVE_MERGE_MAPREDFILES);

  val HIVE_MERGE_SIZE_PER_TASK = appConfigProps.getProperty(SparkCommon.HIVE_MERGE_SIZE_PER_TASK);

  val HIVE_MERGE_SMALLFILES_AVGSIZE = appConfigProps.getProperty(SparkCommon.HIVE_MERGE_SMALLFILES_AVGSIZE);

  val MAPRED_MAX_SPLIT_SIZE = appConfigProps.getProperty(SparkCommon.MAPRED_MAX_SPLIT_SIZE);

  /**
    * mysql
    */
  val MYSQL_JDBC_URL = appConfigProps.getProperty(SparkCommon.MYSQL_JDBC_URL);

  val MYSQL_JDBC_DRIVER = appConfigProps.getProperty(SparkCommon.MYSQL_JDBC_DRIVER);

  val MYSQL_JDBC_USERNAME = appConfigProps.getProperty(SparkCommon.MYSQL_JDBC_USERNAME);

  val MYSQL_JDBC_PASSWORD = appConfigProps.getProperty(SparkCommon.MYSQL_JDBC_PASSWORD);

  val MYSQL_CONNECT_BATCH_SIZE: String = appConfigProps.getProperty(SparkCommon.MYSQL_CONNECT_BATCH_SIZE);

  /**
    * App
    */
  val RUN_MODE_STANDALONE: Boolean = {
    appConfigProps.getProperty(SparkCommon.APP_RUN_MODE, "false").toBoolean
  }

  val APP_ROOT_PATH = appConfigProps.getProperty(SparkCommon.APP_ROOT_PATH, "/spark-task")

}
