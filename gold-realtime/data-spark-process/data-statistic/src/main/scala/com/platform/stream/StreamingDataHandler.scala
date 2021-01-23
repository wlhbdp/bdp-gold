package com.platform.stream


import com.platform.conf.AppConfig
import com.platform.service.BaseService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{CanCommitOffsets, HasOffsetRanges}
import org.slf4j.LoggerFactory



case class StreamingDataHandler() {

  @transient lazy val logger = LoggerFactory.getLogger(getClass)


  def creditDataHandler(stream:InputDStream[ConsumerRecord[String,String]]):Unit ={

    val cacheStream = stream.flatMap{x=>
      DataAnalyticsOperate().originDataHandler(x.value())
    }.filter(ele =>{!ele.isEmpty})

    cacheStream.persist(StorageLevel.MEMORY_ONLY_SER_2)

    /**
      * 过滤出App通讯录数据
      */
    val appContactRecordDataStream = cacheStream.filter(x=>DataAnalyticsOperate().filterFuncForAppContactData(x)).cache()


    /**
      * 处理App通讯录数据
      */
    
    appContactRecordDataStream.foreachRDD(rdd => {
      if(!rdd.isEmpty()){
        DataAnalyticsOperate().processAppContactRecordData(rdd);
      }
    })


    /**
      * 过滤出BBE数据
      */
    val bbeRecordDataStream = cacheStream.filter(e => DataAnalyticsOperate().filterFuncForBbeData(e)).cache()

    /**
      * 处理BBE数据
      */
    bbeRecordDataStream.foreachRDD(rdd => {
      if(!rdd.isEmpty()){
        DataAnalyticsOperate().processBbeData(rdd);
      }
    })

    /**
      * 更新offset
      */
    stream.foreachRDD{rdd=>
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges]
      logger.debug(s"Kafka Offsets directly get from Kafka ${offsetRanges.offsetRanges.mkString("[", " , ", "]")}")
      BaseService().updateOffset(rdd,AppConfig.KAFKA_GROUPID);
      stream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges.offsetRanges)
    }
  }

}
