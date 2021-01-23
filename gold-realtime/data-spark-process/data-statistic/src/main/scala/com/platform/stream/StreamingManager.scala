package com.platform.stream


import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.KafkaException
import org.apache.kafka.common.TopicPartition
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.ConsumerStrategies
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies
import org.slf4j.Logger
import org.slf4j.LoggerFactory

case class StreamingManager() {

  val logger: Logger = LoggerFactory.getLogger(getClass)

  /**
    * Apply spark Direct Kafka API to create DStream.
    * Aggregate(SUM) traffic per batch
    *
    * @param ssc           StreamingContext
    * @param dbFromOffsets offsetMap retrieved from DB
    * @param kafkaParams   kafka configuration params
    * @return ssc StreamingContext
    */
  def createAndAggregateStream(ssc: StreamingContext, dbFromOffsets: Map[TopicPartition, Long], kafkaParams: Map[String, Object]): InputDStream[ConsumerRecord[String, String]] = {
    var stream : InputDStream[ConsumerRecord[String, String]] = null;
    try {
      stream = KafkaUtils.createDirectStream(
        ssc,
        LocationStrategies.PreferConsistent,
        ConsumerStrategies.Assign[String, String](dbFromOffsets.keys.toList, kafkaParams, dbFromOffsets)
      )
    } catch {
      case e: KafkaException => logger.error(s"Create KafkaStream error. $e")
      case e: Exception => logger.error(s"Aggregation of KafkaStream error. $e")
    }
    stream
  }

}
