package com.platform.etl


import java.time.Instant

import com.platform.service.HxDbBusinessApplyService
import com.platform.utils.DateTimeUtils
import org.apache.spark.sql.SparkSession


case class HxDbBusinessApplyEtl() {

  def clean(sparkSql:SparkSession,timeType:String,startDateTime:Long,endDateTime:Long): Unit ={

    //过滤mysql_hxdb_code_library
    sparkSql.sql("select itemno,itemname as channel from mysql_hxdb_code_library where " +
      " codeno='SystemChannelFlag' group by itemno,itemname")
      .createOrReplaceTempView("etl_code_library")

    //过滤mysql_hxdb_business_apply
    sparkSql.sql("select ecl.channel,eba.serialno,eba.approvestatus from (" +
      " select ba.serialno,ba.systemchannelflag,ba.approvestatus from " +
      " mysql_hxdb_business_apply ba where logts>="+startDateTime+" and logts<"+endDateTime+") eba" +
      " join etl_code_library ecl on eba.systemchannelflag=ecl.itemno")
      .createOrReplaceTempView("etl_business_apply")



    sparkSql.sql("select r.channel,count(*) as total from (" +
     " select t.serialno,t.channel from etl_business_apply t group by t.serialno,t.channel" +
     " ) r group by r.channel")
     .createOrReplaceTempView("etl_business_apply_r1")

   sparkSql.sql("select clba.channel,clba.approvestatus,count(*) as status_count from etl_business_apply clba" +
     "  group by clba.channel,clba.approvestatus")
     .createOrReplaceTempView("etl_business_apply_count")


   sparkSql.sql("select result.channel," +
     " sum(case result.approvestatus when '03' then result.status_count else 0 end) as apply_adopt," +
     " sum(case result.approvestatus when '04' then result.status_count else 0 end) as apply_refuse," +
     " sum(case result.approvestatus when '00' then result.status_count else 0 end) as apply_cancel," +
     " sum(case result.approvestatus when '02' then result.status_count else 0 end) as apply_ing from" +
     " etl_business_apply_count result group by result.channel")
     .createOrReplaceTempView("etl_business_apply_r2")


   val df = sparkSql.sql("select r2.*,r1.total,cast(r2.apply_adopt*100/r1.total as decimal(18,2)) " +
     " as apply_percent,"+DateTimeUtils.getDateTimeFormatter(startDateTime)+" " +
     " as begin_time,"+DateTimeUtils.getDateTimeFormatter(endDateTime)+" " +
     " as end_time from etl_business_apply_r1 r1 join etl_business_apply_r2 r2 " +
     " on r1.channel=r2.channel")

    new HxDbBusinessApplyService().saveBusinessApplyChannel(df,timeType)


    //sparkSql.sql()
    //sparkSql.sql("");
  }

}
