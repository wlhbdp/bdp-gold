package com.platform.conf

import com.platform.common.SparkCommon
import com.typesafe.config.{Config, ConfigFactory}

object AppConfig {

  val conf: Config = ConfigFactory.load;

  /**
    * 设置spark app的名字
    */
  val APP_NAME : String =  conf.getString(SparkCommon.SPARK_CONFIG_APP_NAME);

  /**
    * check point url
    */
  val CHECK_POINT : String =  conf.getString(SparkCommon.SPARK_CONFIG_CHECK_POINT);
  /**
    * spark日志级别
    */
  val LOG_LEVEL : String = conf.getString(SparkCommon.SPARK_CONFIG_SET_LOG_LEVEL);

  /**
    * spark RDD 分区数
    */
  val NUM_RDD : Int = conf.getInt(SparkCommon.SPARK_CONFIG_NUM_RDD_PARTITIONS);

  /**
    * 内存for存储比例
    */
  val MEMORY_FOR_STORAGE_FRACTION : String = conf.getString(SparkCommon.SPARK_CONFIG_MEMORY_STORAGE_FRACTION);

  /**
    * 内存exceute的比例
    */
  val MEMORY_FOR_SHUFFLE_FRACTION : String = conf.getString(SparkCommon.SPARK_CONFIG_EXECUTOR_MEMORY_FRACTION);

  /**
    * set user memory在整个 executor memory 的占比。
    */
  val MEMORY_FRACTION : String = conf.getString(SparkCommon.SPARK_CONFIG_MEMORY_FRACTION);
  /**
    * 是否开启调整内存存储比例功能
    */
  val MEMORY_LEGACY_MODE : String = conf.getString(SparkCommon.SPARK_CONFIG_MEMORY_USE_LEGACY_MODE);


  /**
    * kafka topics
    */
  val REAL_TIME_KAFKA_TOPICS : String = conf.getString(SparkCommon.SPARK_STREAMING_KAFKA_REALTIME_TOPICS);

  /**
    * kafka brokers
    */
  val KAFKA_BROKERS : String = conf.getString(SparkCommon.SPARK_STREAMING_KAFKA_BROKERS);


  /**
    * shuffle类型
    */
  val SHUFFLE_TYPE : String = conf.getString(SparkCommon.SPARK_SHUFFLE_TYPE);

  /**
    * shuffle缓存大小
    */
  val SHUFFLE_BUFF_SIZE : String = conf.getString(SparkCommon.SPARK_SHUFFLE_BUFFILE_SIZE);

  /**
    * shuffle read 拉取数据的最大次数
    */
  val SHUFFLE_FETCH_DATA_TIMES : String = conf.getString(SparkCommon.SPARK_SHUFFLE_BUFFILE_MAXTRY);

  /**
    * shuffle read 拉取数据的间隔
    */

  val SHUFFLE_FECTH_DATA_INTERVAL : String = conf.getString(SparkCommon.SPARK_SHUFFLE_BUFFILE_FETCH_INTERVAL);

  /**
    * spark 程序序列化方式
    */
  val SERIALIZER_TYPE : String = conf.getString(SparkCommon.SPARK_CONFIG_SERIALIZER_TYPE);

  /**
    * jdbc url
    */
  val DB_JDBC_URL : String = conf.getString(SparkCommon.DB_JDBC_URL);

  /**
    * jdbc username
    */
  val DB_USER_NAME : String = conf.getString(SparkCommon.DB_USER_NAME);

  /**
    * jdbc password
    */
  val DB_PASSWORD :String = conf.getString(SparkCommon.DB_PASSWORD)

  /**
    * db driver
    */
  val DB_DRIVER_CLASS_NAME : String = conf.getString(SparkCommon.DB_DRIVER_CLASS_NAME);

  /**
    * offset保存表
    */
  val TABLE_KAFKA_OFFSET : String = conf.getString(SparkCommon.DB_TABLE_KAFKA_OFFSET);



}
