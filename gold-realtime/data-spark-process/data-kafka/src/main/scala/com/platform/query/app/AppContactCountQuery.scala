package com.platform.query.app

import java.util.concurrent.TimeUnit

import com.platform.conf.AppConfig
import com.platform.sink.AppContactCountSink
import org.apache.spark.sql.execution.streaming.FileStreamSource.Timestamp
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.functions.{col, get_json_object, window}
import org.apache.spark.sql.streaming.Trigger
import org.slf4j.LoggerFactory


case class AppContactCountQuery() {

  @transient lazy val logger = LoggerFactory.getLogger(getClass);



  def clean(dataSet : Dataset[(String,String)])= {

    try{

      val count = dataSet.groupBy(get_json_object(col("data"),path = "$.token")).count();

      val writers = new AppContactCountSink();

      count.writeStream
        .trigger(Trigger.ProcessingTime(5, TimeUnit.SECONDS))
        .foreach(writers)
        .outputMode("Update")
        .queryName(getClass.getName)
        .option("checkpointLocation", AppConfig.CHECK_POINT)
        .start().awaitTermination;
    }catch {
      case ex: Exception => {
        logger.error("统计App通讯录数量失败", ex);
      }
    }
  }

}
