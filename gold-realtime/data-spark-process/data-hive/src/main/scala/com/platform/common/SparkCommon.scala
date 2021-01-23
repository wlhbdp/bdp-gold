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



  /**
    * spark公共配置
    **/
  val SPARK_CONFIG_CHECK_POINT = "spark.commons.checkpoint"
  //app name
  val SPARK_CONFIG_APP_NAME = "spark.commons.appName"

  //log level
  val SPARK_CONFIG_SET_LOG_LEVEL = "spark.commons.logLevel"

  //是否开始自定义管理内存分配
  val SPARK_CONFIG_MEMORY_USE_LEGACY_MODE = "spark.memory.useLegacyMode"

  //序列化方式
  val SPARK_CONFIG_SERIALIZER_TYPE= "spark.serializer"


  // set user memory在整个 executor memory 的占比。
  val SPARK_CONFIG_MEMORY_FRACTION = "spark.memory.fraction";

  /**
    * shuffle 操作相关
    */

  //内存用来存储的比例,这部分内存中有多少可以用于Memory Store管理RDD Cache的数据
  val SPARK_CONFIG_MEMORY_STORAGE_FRACTION= "spark.storage.memoryFraction"

  //内存分配中execution比例大小
  val SPARK_CONFIG_EXECUTOR_MEMORY_FRACTION = "spark.shuffle.memoryFraction"

  //sparkStreaming.Shuffle.manager的类型(hash,sort)
  val SPARK_SHUFFLE_TYPE = "spark.shuffle.manager"

  //spark.shuffle缓存大小,默认32K 内存充足可以调到64,减少shuffle write过程中溢写磁盘文件的次数，也就可以减少磁盘IO次数
  val SPARK_SHUFFLE_BUFFILE_SIZE = "spark.shuffle.file.buffer"

  //spark shuffle read 拉取属于自己数据时重试的次数
  val SPARK_SHUFFLE_BUFFILE_MAXTRY = "spark.shuffle.io.maxRetries"

  //spark shuffle read 拉取属于自己数据时重试的间隔
  val SPARK_SHUFFLE_BUFFILE_FETCH_INTERVAL = "spark.shuffle.io.retryWait"

  /**
    * spark-sql
    */

  val SPARK_SQL_CASESENSITIVE = "spark.sql.caseSensitive";

  val SPARK_SQL_USER_DB = "spark.sql.user.db";

  /**
    * hive
    */
  val HIVE_EXEC_DYNAMIC_PARTITION = "hive.exec.dynamic.partition";

  val HIVE_EXEC_DYNAMIC_PARTITION_MODE = "hive.exec.dynamic.partition.mode";

  val HIVE_EXEC_COMPRESS_OUTPUT = "hive.exec.compress.output";

  val HIVE_INPUT_FORMAT = "hive.input.format";

  val HIVE_MERGE_MAPFILES = "hive.merge.mapfiles";

  val HIVE_MERGE_MAPREDFILES = "hive.merge.mapredfiles";

  val HIVE_MERGE_SIZE_PER_TASK = "hive.merge.size.per.task";

  val HIVE_MERGE_SMALLFILES_AVGSIZE = "hive.merge.smallfiles.avgsize";

  val MAPRED_MAX_SPLIT_SIZE = "mapred.max.split.size";

  /**
    * mysql
    */

  val MYSQL_JDBC_URL = "mysql.jdbc.url";

  val MYSQL_CONNECT_BATCH_SIZE = "mysql.connect.batch.size";

  val MYSQL_JDBC_USERNAME = "mysql.jdbc.username";

  val MYSQL_JDBC_PASSWORD = "mysql.jdbc.password";

  val MYSQL_JDBC_DRIVER = "mysql.jdbc.driver";

  val APP_RUN_MODE = "app.mode.standalone"

  val APP_ROOT_PATH = "app.root.path"
}
