package com.platform

import java.io.FileNotFoundException

import com.platform.conf.{AppConfig, ConnectionFactory, KafkaManager, StreamingConf}
import com.platform.stream.{StreamingDataHandler, StreamingManager}
import org.apache.spark.SparkException
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.slf4j.LoggerFactory



object RiskRuleStreaming {

  @transient lazy val logger = LoggerFactory.getLogger(getClass);



  //创建StreamingContext(StreamingContext是所有Spark Streaming功能的主入口点)
  def createStreaming(): StreamingContext = {
    val ssc = new StreamingContext(StreamingConf.initStreamingConf(), Seconds(AppConfig.SCHEDULING_INTERVAL));
    ssc.checkpoint(AppConfig.CHECK_POINT);
    ssc.sparkContext.setLogLevel(AppConfig.LOG_LEVEL);
    createStreamingHandler(ssc);
    ssc
  }

  //获取kafka数据并做处理
  def createStreamingHandler(ssc: StreamingContext) = {
    val fromOffsets = KafkaManager().initOffset();
    val ruleRiskStreaming = StreamingManager().createAndAggregateStream(ssc, fromOffsets, StreamingConf.getkafkaParams);
    if (fromOffsets == null || ruleRiskStreaming == null) {
      throw new Exception();
    }
    //RDD处理
    val streamDataHandler = new StreamingDataHandler();
    streamDataHandler.creditDataHandler(ruleRiskStreaming);
  }

  def main(args: Array[String]) {
    //读取配置文件
    try {
      StreamingConf.checkConfig();
    } catch {
      case fx: FileNotFoundException => {
        logger.error("file properties not found!!!", fx)
      }
      case ex: Exception => {
        logger.error(s"get config properites or mysql connect have failed!!!", ex)
        System.exit(1)
      }
    }

    //程序启动
    try {
      //创建streamingContext
      val contextSsc = createStreaming();
      ConnectionFactory.getDataSource;
      contextSsc.start()
      logger.info("start streaming success!!!")
      contextSsc.awaitTermination()
    } catch {
      case se: SparkException => {
        logger.error("catch SparkException !!", se)
      }
      case fe: FileNotFoundException => {
        logger.error("catch FileNotFoundException !!", fe)
      }
      case e: Exception => {
        logger.error("system error !!",e)
      }
    }
  }
}
