package com.platform.stream

import com.platform.RiskRuleStreaming.getClass
import com.platform.service.BaseService
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.StreamingQueryListener
import org.apache.spark.sql.streaming.StreamingQueryListener.{QueryProgressEvent, QueryStartedEvent, QueryTerminatedEvent}
import org.slf4j.LoggerFactory

case class StreamListener() {

  @transient lazy val logger = LoggerFactory.getLogger(getClass);


  def addStreamingListener(sparkSession : SparkSession) =  {
    sparkSession.streams.addListener(new StreamingQueryListener {
      override def onQueryStarted(event: QueryStartedEvent): Unit = {
        logger.info("Query started: " + event.id)
      }

      override def onQueryProgress(event: QueryProgressEvent): Unit = {
        val d = event.progress.sources
        for (i <-0 until d.length){
          BaseService.updateOffset(d(i).endOffset,d(i).startOffset)
        }
      }

      override def onQueryTerminated(event: QueryTerminatedEvent): Unit = {
        logger.info("Query started: " + event.id)
      }
    })

  }
}
