package com.platform.stream

import com.platform.conf.AppConfig
import org.apache.spark.sql.{DataFrame, SparkSession}

case class StreamingManager() {


  def readStream(partitionsAndOffsets : String,spark : SparkSession):DataFrame = {
    val df = spark.readStream
        .format("kafka")
        .option("kafka.bootstrap.servers",AppConfig.KAFKA_BROKERS)
        .option("subscribe",AppConfig.REAL_TIME_KAFKA_TOPICS)
        .option("failOnDataLoss",true)
        .load()
    df
  }


}
