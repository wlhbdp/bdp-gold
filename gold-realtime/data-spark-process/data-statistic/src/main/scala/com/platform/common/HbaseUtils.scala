package com.platform.common

import java.math.BigDecimal

import org.apache.commons.lang3.StringUtils
import org.apache.hadoop.hbase.util.Bytes

object HbaseUtils {

  def toBytes(value: String): Array[Byte] = {
    var result: String = null
    if (StringUtils.isBlank(value)) {
      result = ""
    } else {
      result = value.trim()
    }
    Bytes.toBytes(result)
  }

  def toBytes(value: BigDecimal): Array[Byte] = {
    var result: BigDecimal = null
    if (null == value) {
      result = new BigDecimal(0)
    } else {
      result = value
    }
    Bytes.toBytes(result)
  }

  def main(args: Array[String]): Unit = {
    print(Bytes.toBytes(""))
  }
}
