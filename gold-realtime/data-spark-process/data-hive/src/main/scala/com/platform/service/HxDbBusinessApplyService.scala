package com.platform.service

import com.platform.db.HxDbBusinessApplyDB
import org.apache.spark.sql.DataFrame

case class HxDbBusinessApplyService() {

  def saveBusinessApplyChannel(df:DataFrame,timeType:String): Unit ={
    new HxDbBusinessApplyDB().saveBusinessApplyChannel(df,timeType);
  }
}
