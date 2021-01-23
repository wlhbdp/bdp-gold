package com.platform.utils

import java.io.{File, FileInputStream, InputStream}

import com.alibaba.fastjson.{JSON, JSONObject}
import com.platform.conf.AppConfig
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.io.Source


/**
  *
  * author: GaoPan
  * create: 2019-03-06 17:09
  */
object TableMapParser {

  val logger: Logger = LoggerFactory.getLogger(getClass)

  def parseTableMapInfo(filePaths: ArrayBuffer[String], fileNames:ArrayBuffer[String]):mutable.HashMap[String, JSONObject] = {
    val tableMap = new mutable.HashMap[String, JSONObject]()

    if (filePaths == null || filePaths.isEmpty) {
      sys.error("Task file path is empty!")
      sys.exit(1)
    }

    def parseJSONConfigFile(path: String, isSavedInner:Boolean): Any = {
      try {
        val tableJSON: JSONObject = fileToJson(path, isSavedInner)
        val tableName = tableJSON.getString("taskName")
        tableMap.put(tableName, tableJSON)
      } catch {
        case e: Exception =>
          logger.error(s"Read file error===> " + e)
      }
    }

    val length = filePaths.size
    for (fileName <- fileNames) {
      //特殊处理json配置文件储存在resources下的文件
      if (!fileName.startsWith("/")) {
        val path = filePaths(0) + File.separator + fileName + ".json"
        parseJSONConfigFile(path, isSavedInner = true)
      } else {
        for (filePath <- filePaths.slice(1, length)) {
          var fp = filePath
          val names = fileName.split("/")
          if (fp.startsWith("/")) {
            fp = if (AppConfig.RUN_MODE_STANDALONE) {
              //开发环境下从外部文件夹读取配置文件路径
              System.getProperty("user.dir") + "/spark-hive-etl" + fp + File.separator + names.last + ".json"
            } else {
              //集群环境下从外部文件夹读取配置文件夹
              names.last + ".json"
            }
          }

          if (AppConfig.RUN_MODE_STANDALONE) {
            println("filePath:" + fp)
          }
          parseJSONConfigFile(fp, isSavedInner = false)
        }
      }

    }

    tableMap
  }

  def fileToJson(file: String, isSavedInner:Boolean): JSONObject = {
    var ins:InputStream = null
    if (isSavedInner) {
      ins = TableMapParser.getClass.getClassLoader.getResourceAsStream(file)
    } else {
      ins = new FileInputStream(file)
    }
    import java.nio.charset.{Charset, CodingErrorAction}
    val decoder = Charset.forName("UTF-8").newDecoder
    decoder.onMalformedInput(CodingErrorAction.IGNORE)
    val bufferedSource = Source.fromInputStream(ins)(decoder)
    val content:StringBuilder = new StringBuilder()

    bufferedSource.getLines().foreach(line => content.append(line.trim))
    if (AppConfig.RUN_MODE_STANDALONE) {
      println("fileContent: \n" + content.toString())
    }

    JSON.parseObject(content.toString())
  }
}
