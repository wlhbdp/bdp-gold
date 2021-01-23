package com.platform.bean

import java.util

import scala.beans.BeanProperty


/**
  *
  * author: GaoPan
  * create: 2019-05-08 17:46
  */
class TemplateV2 {
  private var taskName: String = _

  private var dataSource: DataSource = _

  private var targetDest: TargetDest = _

  private var steps:util.ArrayList[Step] = _

  private var version: String = _

  private var extra: util.HashMap[String, Object] = _

  def getTaskName:String = this.taskName

  def setTaskName(taskName:String):Unit = {this.taskName = taskName}

  def getDataSource:DataSource = this.dataSource

  def setDataSource(dataSource: DataSource):Unit = {this.dataSource = dataSource}

  def getTargetDest:TargetDest = this.targetDest

  def setTargetDest(targetDest:TargetDest):Unit = {this.targetDest = targetDest}

  def getSteps:util.ArrayList[Step] = this.steps

  def setSteps(steps:util.ArrayList[Step]):Unit = {this.steps = steps}

  def getVersion:String = this.version

  def setVersion(version: String):Unit = {this.version = version}

  def getExtra:util.HashMap[String, Object] = this.extra

  def setExtra(extra: util.HashMap[String, Object]):Unit = {this.extra = extra}
}

class DataSource {
  @BeanProperty var sourceName: String = _

  @BeanProperty var dbName: String = _
}

class TargetDest {
  @BeanProperty var targetName:String = _
  @BeanProperty var dbName:String = _
  @BeanProperty var tableName:String = _
  @BeanProperty var columns: util.ArrayList[TableColumn] = _
}

class TableColumn {
  @BeanProperty var fieldName: String = _
  @BeanProperty var fieldType: String = _
}

class Step {
  @BeanProperty var sql:String = _
  @BeanProperty var tempView:String = _
  @BeanProperty var macros: util.HashMap[String,String] = _
}


