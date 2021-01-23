package com.platform.conf

import com.platform.common.SparkCommon
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkSessionSingleton {

  @transient private var instance: SparkSession = _
  def getInstance(sparkConf: SparkConf): SparkSession = {
    if (instance == null) {
      instance = SparkSession
        .builder
        .config(sparkConf)
        .enableHiveSupport()
        .getOrCreate();
      instance.sparkContext.setLogLevel(AppConfig.LOG_LEVEL);
      instance.sparkContext.setCheckpointDir(AppConfig.CHECK_POINT)

      instance.sqlContext.setConf(SparkCommon.SPARK_SQL_CASESENSITIVE, AppConfig.SPARK_SQL_CASESENSITIVE)

      //使用动态分区
      instance.sqlContext.setConf(SparkCommon.HIVE_EXEC_DYNAMIC_PARTITION, AppConfig.HIVE_EXEC_DYNAMIC_PARTITION)
      instance.sqlContext.setConf(SparkCommon.HIVE_EXEC_DYNAMIC_PARTITION_MODE, AppConfig.HIVE_EXEC_DYNAMIC_PARTITION_MODE)
      instance.sqlContext.setConf(SparkCommon.HIVE_EXEC_COMPRESS_OUTPUT, AppConfig.HIVE_EXEC_COMPRESS_OUTPUT)

      instance.sqlContext.setConf(SparkCommon.MAPRED_MAX_SPLIT_SIZE, AppConfig.MAPRED_MAX_SPLIT_SIZE)
      instance.sqlContext.setConf(SparkCommon.HIVE_INPUT_FORMAT, AppConfig.HIVE_INPUT_FORMAT)
      instance.sqlContext.setConf(SparkCommon.HIVE_MERGE_MAPFILES, AppConfig.HIVE_MERGE_MAPFILES)
      instance.sqlContext.setConf(SparkCommon.HIVE_MERGE_MAPREDFILES, AppConfig.HIVE_MERGE_MAPREDFILES)
      instance.sqlContext.setConf(SparkCommon.HIVE_MERGE_SIZE_PER_TASK, AppConfig.HIVE_MERGE_SIZE_PER_TASK)
      instance.sqlContext.setConf(SparkCommon.HIVE_MERGE_SMALLFILES_AVGSIZE, AppConfig.HIVE_MERGE_SMALLFILES_AVGSIZE)

    }
    instance
  }

}
