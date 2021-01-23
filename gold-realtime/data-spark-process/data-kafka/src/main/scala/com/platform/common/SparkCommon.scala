package com.platform.common

object SparkCommon {


  /**
    * 字符串操作符
    */
  val DATA_FIELD_SUB_SPACE = ","
  val DATA_FIELD_OR_SPACE = "-"
  val DATA_FIELD_SPACE = "_"
  val NODE_PATH_SUB_SPACE = "/"
  val STREAM_ELEM_DATA_SPLIT = "\t"
  val AREA_CODE_SPLIT = "="

  /**
    * 数据存储key value
    */
  val DATA_CONSTANTS_FIVE_MIN_SECONDS = 300
  val DATA_CONSTANTS_ONE_HOUR_SECONDS = 3600
  val DATA_CONSTANTS_ONE_DAY_SECONDS = 86400 //TIME_INTERVAL_

  val DATE_TIME_FORMAT_MINUTE = "yyyy-MM-dd hhmm"
  val DATE_TIME_FORMAT_SIMPLE_MILLISECOND = "yyyyMMddHHmmss"
  val DATE_TIME_FORMAT_SIMPLE = "yyyyMMdd"
  val DATE_TIME_FORMAT_MILLISECOND = "yyyy-MM-dd HH:mm:ss"



  //设置spark Streaming 应用程序的名字
  val APP_NAME : String = "spark.streaming.appName";

  /**
    * spark公共配置
    **/
  val SPARK_CONFIG_CHECK_POINT = "spark.commons.checkpoint"
  //app name
  val SPARK_CONFIG_APP_NAME = "spark.commons.appName"

  //log level
  val SPARK_CONFIG_SET_LOG_LEVEL = "spark.commons.logLevel"

  //num partitions
  val SPARK_CONFIG_NUM_RDD_PARTITIONS = "spark.commons.RDD.num"

  //是否开始自定义管理内存分配
  val SPARK_CONFIG_MEMORY_USE_LEGACY_MODE = "spark.memory.useLegacyMode"

  //序列化方式
  val SPARK_CONFIG_SERIALIZER_TYPE= "spark.serializer"


  // set user memory在整个 executor memory 的占比。
  val SPARK_CONFIG_MEMORY_FRACTION = "spark.memory.fraction";

  /**
    * shuffle 操作相关
    */


  val SPARK_CONFIG_MEMORY_STORAGE_FRACTION= "spark.storage.memoryFraction"

  //内存分配中execution比例大小
  val SPARK_CONFIG_EXECUTOR_MEMORY_FRACTION = "spark.shuffle.memoryFraction"


  val SPARK_SHUFFLE_TYPE = "spark.shuffle.manager"

  val SPARK_SHUFFLE_BUFFILE_SIZE = "spark.shuffle.file.buffer"

  val SPARK_SHUFFLE_BUFFILE_MAXTRY = "spark.shuffle.io.maxRetries"

  val SPARK_SHUFFLE_BUFFILE_FETCH_INTERVAL = "spark.shuffle.io.retryWait"


  /**
    * kafka的相关配置参数
    */
  val SPARK_STREAMING_KAFKA_REALTIME_TOPICS = "spark.streaming.realTime.kafkaTopics"
  val SPARK_STREAMING_KAFKA_BROKERS = "spark.streaming.kafkaBrokers"
  val SPARK_STREAMING_KAFKA_BROKERS_PRAMA = "metadata.broker.list"

  /**
    * mysql 相关配置
    *
    */
  val DB_JDBC_URL = "spark.straming.jdbc.url"
  val DB_USER_NAME = "spark.streaming.jdbc.username"
  val DB_PASSWORD = "spark.streaming.jdbc.password"
  val DB_DRIVER_CLASS_NAME = "spark.streaming.jdbc.driver"

  val MYSQL_CONNECT_POOL_INIT_SIZE = 20
  val MYSQL_CONNECT_MAX_ACTIVE = 200
  val MYSQL_CONNECT_MIN_IDLES = 50
  val MYSQL_CONNECT_MAX_IDLES = 200
  val MYSQL_CONNECT_MAX_MAIT_TIME = 1000
  val MYSQL_EVICT_IDLE_CONNECT_MIN_TIME: Long = 5 * 60 * 1000
  val MYSQL_EVICT_CONNECT_TIME: Long = 10 * 60 * 1000

  /**
    * db name
    */
  val DB_TABLE_KAFKA_OFFSET = "table.kafka.offset"



}
