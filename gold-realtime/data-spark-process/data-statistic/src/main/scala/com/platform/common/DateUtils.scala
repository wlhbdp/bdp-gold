package com.platform.common

import org.apache.commons.lang3.StringUtils

object DateUtils {
  /**
    * 2019-08-27 workDate
    * 2019-08-27 08:12:03 start_work_time
    *
    * @param time   字符串形式的日期
    * @param format 日期的格式
    * @return
    */

  def dateToMilli(time: String, format: String): Long = {
    if (StringUtils.isBlank(format)) {
      throw new IllegalArgumentException("format error,format" + format)
    }
    if (StringUtils.isBlank(time) || time.matches(format)) {
      throw new IllegalArgumentException("time error,time:" + time + ",format:" + format)
    }
    import java.text.SimpleDateFormat
    import java.util.Date
    val newTime: Date = new SimpleDateFormat(format).parse(time)
    newTime.getTime
  }

  def getDiffHour(start: String, end: String, format: String): Double = {
    val diffMilli: Double = dateToMilli(end, format) - dateToMilli(start, format)
    diffMilli / 3600000
  }


}
