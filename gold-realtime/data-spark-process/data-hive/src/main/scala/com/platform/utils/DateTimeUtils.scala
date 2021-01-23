package com.platform.utils

import java.time._
import java.time.format.DateTimeFormatter
import java.util.{Calendar, Date}

import org.apache.commons.lang.time.DateUtils

import scala.collection.mutable

object DateTimeUtils {


    def getHourRange: mutable.HashMap[String, LocalDateTime] ={
      val map = new mutable.HashMap[String,LocalDateTime]()
      val initDateTime = LocalDateTime.now()
      val minusDateTime = initDateTime.minusHours(1)
      val startDateTime = LocalDateTime.of(minusDateTime.getYear,minusDateTime.getMonth,minusDateTime.getDayOfMonth,minusDateTime.getHour,0,0);
      val endDateTime = LocalDateTime.of(initDateTime.getYear,initDateTime.getMonth,initDateTime.getDayOfMonth,initDateTime.getHour,0,0);
      map.put("startDateTime",startDateTime)
      map.put("endDateTime",endDateTime)
      map
    }

    def getDayRange: mutable.HashMap[String, LocalDateTime] ={
      val map = new mutable.HashMap[String,LocalDateTime]()
      val date= LocalDate.now()
      val zeroHourTime = LocalTime.of(0,0,0,0)
      val startDateTime = LocalDateTime.of(date.minusDays(1),zeroHourTime)
      val endDateTime = LocalDateTime.of(date,zeroHourTime)
      map.put("startDateTime",startDateTime)
      map.put("endDateTime",endDateTime)
      map
    }

    def getWeekRange: mutable.HashMap[String, LocalDateTime] ={
      val map = new mutable.HashMap[String,LocalDateTime]()
      val date= LocalDate.now()
      val zeroHourTime = LocalTime.of(0,0,0,0)
      val firstDayThisWeek = date.minusDays(date.getDayOfWeek.getValue-1)
      val firstDayLastWeek = firstDayThisWeek.minusWeeks(1)
      val startDateTime = LocalDateTime.of(firstDayLastWeek,zeroHourTime)
      val endDateTime = LocalDateTime.of(firstDayThisWeek,zeroHourTime)
      map.put("startDateTime",startDateTime)
      map.put("endDateTime",endDateTime)
      map
    }


    def getMonthRange: mutable.HashMap[String, LocalDateTime] ={
      val map = new mutable.HashMap[String,LocalDateTime]()
      val date= LocalDate.now()
      val zeroHourTime = LocalTime.of(0,0,0,0)
      val fistDayThisMonth = LocalDate.of(date.getYear,date.getMonth,1);
      val fistDayLastMonth = fistDayThisMonth.minusMonths(1);
      val startDateTime = LocalDateTime.of(fistDayLastMonth,zeroHourTime)
      val endDateTime = LocalDateTime.of(fistDayThisMonth,zeroHourTime)
      map.put("startDateTime",startDateTime)
      map.put("endDateTime",endDateTime)
      map
    }

    def getYearRange: mutable.HashMap[String, LocalDateTime] = {
      val map = new mutable.HashMap[String,LocalDateTime]()
      val date= LocalDate.now()
      val zeroHourTime = LocalTime.of(0,0,0,0)
      val fistDayThisYear = LocalDate.of(date.getYear,1,1);
      val fistDayLastYear = fistDayThisYear.minusYears(1);
      val startDateTime = LocalDateTime.of(fistDayLastYear,zeroHourTime)
      val endDateTime = LocalDateTime.of(fistDayThisYear,zeroHourTime)
      map.put("startDateTime",startDateTime)
      map.put("endDateTime",endDateTime)
      map
    }


    def getDateTimeFormatter(millis: Long): String = {
      val instant = Instant.ofEpochMilli(millis)
      val zone = ZoneId.systemDefault
      val localDateTime = LocalDateTime.ofInstant(instant, zone)
      val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
      localDateTime.format(formatter)
    }

    def getBizDateFormatStr(pattern: String):String = {
      val instant = Instant.ofEpochMilli(new Date().getTime)
      val zone = ZoneId.systemDefault
      val localDateTime = LocalDateTime.ofInstant(instant, zone)
      val formatter = DateTimeFormatter.ofPattern(pattern)
      localDateTime.format(formatter)
    }

    def getPtMonth:String = {
      val ins = Calendar.getInstance()
      val year = ins.get(Calendar.YEAR)
      val month = ins.get(Calendar.MONTH) + 1
      if (month < 10) {
        year + "0" + month
      } else {
        year + "" + month
      }
    }

    def getPtDay:String = {
      val ins = Calendar.getInstance()
      val year = ins.get(Calendar.YEAR)
      val month = ins.get(Calendar.MONTH) + 1
      val day = ins.get(Calendar.DAY_OF_MONTH)
      val m = if (month < 10) {
        "0" + month
      } else {
        "" + month
      }
      val d = if (day < 10) {
        "0" + day
      } else {
        "" + day
      }
      year + m + d
    }

    def getBizHour():Long = {
      val hour = DateUtils.getFragmentInHours(Calendar.getInstance, Calendar.DAY_OF_YEAR)
      hour-1
    }

  def main(args: Array[String]): Unit = {
    println(getPtMonth)
    println(getPtDay)
  }
}
