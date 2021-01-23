package com.platform.conf

import com.platform.common.SparkCommon
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.slf4j.LoggerFactory;

object StreamingConf {

  val logger = LoggerFactory.getLogger(getClass);

  def checkConfig() = {
    //内存for存储比例
    if (AppConfig.MEMORY_FOR_STORAGE_FRACTION == null) {
      logger.error(s"内存for存储的比例值没有配置！！！");
      throw new Exception("未设置内存for存储的比例值！！！")
    }

    //kafka
    if (AppConfig.KAFKA_BROKERS == null) {
      logger.error(s"未设置Kafka-broker！！！");
      throw new Exception("未设置Kafka-broker！！！")
    }

    if (AppConfig.REAL_TIME_KAFKA_TOPICS == null) {
      logger.error(s"未设置Kafka-topic！！！");
      throw new Exception("未设置Kafka-topic！！！")
    }

    //mysql
    if (AppConfig.DB_USER_NAME == null || AppConfig.DB_PASSWORD == null) {
      logger.error(s"The mysql userName or password must be set!!!");
      throw new Exception("The mysql userName or password must be set!!!")
    }

  }


  def initStreamingConf():SparkConf = {
    val sparkConf = new SparkConf().setAppName(AppConfig.APP_NAME);
    //sparkConf.setMaster("local[*]")

    sparkConf.set(SparkCommon.SPARK_CONFIG_SERIALIZER_TYPE, AppConfig.SERIALIZER_TYPE)
    sparkConf.set(SparkCommon.SPARK_CONFIG_MEMORY_STORAGE_FRACTION,AppConfig.MEMORY_FOR_STORAGE_FRACTION)
    sparkConf.set(SparkCommon.SPARK_CONFIG_EXECUTOR_MEMORY_FRACTION,AppConfig.MEMORY_FOR_SHUFFLE_FRACTION.toString)
    sparkConf.set(SparkCommon.SPARK_CONFIG_MEMORY_USE_LEGACY_MODE,AppConfig.MEMORY_LEGACY_MODE)
    sparkConf.set(SparkCommon.SPARK_CONFIG_MEMORY_FRACTION,AppConfig.MEMORY_FRACTION)
    sparkConf.set(SparkCommon.SPARK_SHUFFLE_TYPE, AppConfig.SHUFFLE_TYPE)
    sparkConf.set(SparkCommon.SPARK_SHUFFLE_BUFFILE_SIZE,AppConfig.SHUFFLE_BUFF_SIZE)
    sparkConf.set(SparkCommon.SPARK_SHUFFLE_BUFFILE_MAXTRY,AppConfig.SHUFFLE_FETCH_DATA_TIMES)
    sparkConf.set(SparkCommon.SPARK_SHUFFLE_BUFFILE_FETCH_INTERVAL,AppConfig.SHUFFLE_FECTH_DATA_INTERVAL)
    sparkConf.set(SparkCommon.SPARK_STREAMING_BACKPRESSURE_ENABLED,AppConfig.BACKPRESSURE_ENABLED)
    sparkConf.set(SparkCommon.SPARK_STREAMING_KAFKA_MAX_RATE_PER_PARTITION,AppConfig.KAFKA_MAX_RATE_PER_PARTITION);
    return sparkConf;
  }



  //Kafka的参数设置
  def getkafkaParams: Map[String, Object] = {
    val kafkaParams = Map[String, Object](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> AppConfig.KAFKA_BROKERS,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.GROUP_ID_CONFIG -> AppConfig.KAFKA_GROUPID,
      ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> AppConfig.KAFKA_OFFSET,
      ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> (false: java.lang.Boolean)
    )

    kafkaParams
  }






}
