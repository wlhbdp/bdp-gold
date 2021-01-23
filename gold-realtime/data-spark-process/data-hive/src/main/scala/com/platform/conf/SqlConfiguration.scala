package com.platform.conf

import com.alibaba.fastjson.JSONObject

case class SqlConfiguration(sql:String, tempView:String, macros:JSONObject) {

}