package com.platform.conf

import java.util
import java.util.Properties

import com.platform.common.SparkCommon
import com.platform.service.BaseService
import org.slf4j.{Logger, LoggerFactory}
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.clients.consumer.ConsumerConfig



case class KafkaManager() {

  val logger: Logger = LoggerFactory.getLogger(getClass)

  val conf: Config = ConfigFactory.load

  def initOffset()  ={
    val groupId = AppConfig.KAFKA_GROUPID;
    val topics = conf.getString(SparkCommon.SPARK_STREAMING_KAFKA_REALTIME_TOPICS).split(SparkCommon.DATA_FIELD_SUB_SPACE)
    for (topic <- topics){
      val topicCount = BaseService().findOffsetCount(groupId,topic);
      if (topicCount==0){
        val flag = kafkaOffsetInitDB(groupId,topic);
        if (!flag){
          throw new Exception("init offset db error");
        }
      }
    }
    BaseService().getOffsetFromDB(groupId);
  }



  //消费者配置
  def getConsumerConfigs : Properties = {
    val props = new Properties();
    val brokers = AppConfig.KAFKA_BROKERS;
    val groupId = AppConfig.KAFKA_GROUPID
    val offset = AppConfig.KAFKA_OFFSET
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offset)
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, java.lang.Boolean.FALSE)
    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000")
    props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000")
    props
  }


  //初始化数据库
  def kafkaOffsetInitDB(groupId:String,topic:String): Boolean = {
    var bool = true
    try {
      val nums = conf.getInt(SparkCommon.SPARK_CONFIG_NUM_RDD_PARTITIONS)
      val consumer = new KafkaConsumer(getConsumerConfigs)
      val partitions =  new util.HashSet[TopicPartition]();
      for (i <- 0 until nums){
        val actualTopicPartition =  new TopicPartition(topic,i);
        partitions.add(actualTopicPartition);
        consumer.assign(partitions);
        consumer.seekToEnd(partitions);
        val actualPosition = consumer.position(actualTopicPartition);
        BaseService().saveOffset(topic, i, actualPosition,groupId);
      }
    } catch {
      case e: Exception =>
        logger.error("kafka offset init error", e)
        bool = false
    }
    bool
  }

}
