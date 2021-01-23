package com.platform


import com.platform.conf._
import com.platform.stream.{SparkSessionSingleton, StreamListener, StreamingDataHandler, StreamingManager}
import org.slf4j.LoggerFactory


object RiskRuleStreaming {

  @transient lazy val logger = LoggerFactory.getLogger(getClass);


  def main(args: Array[String]): Unit = {
    try{
      //初始化spark配置
      val sparkConf = StreamingConf.initStreamingConf();
      //获取spark session
      val sparkSession = SparkSessionSingleton.getInstance(sparkConf);
      //设置日志级别
      sparkSession.sparkContext.setLogLevel(AppConfig.LOG_LEVEL);

      //添加streaming batch 监听
      //StreamListener().addStreamingListener(sparkSession);

      //初始化查询
      val partitionsAndOffsets = KafkaManager.initOffset();
      //读取kafka数据
      val dataFrame =  StreamingManager().readStream(partitionsAndOffsets,sparkSession);
      //处理数据
      StreamingDataHandler().creditDataHandler(dataFrame,sparkSession);


    }catch {
      case ex: Exception => {
        logger.error("run risk role job exception", ex);
      }
    }
  }



}
