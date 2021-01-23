package com.platform.conf

import com.alibaba.druid.pool.DruidDataSource
import com.typesafe.config.Config
import com.alibaba.druid.pool.DruidPooledConnection
import java.sql.ResultSet
import java.sql.Statement
import com.alibaba.druid.pool.DruidDataSourceFactory
import java.util.Properties


object ConnectionFactory {


  private val jdbcDriverClass : String = AppConfig.DB_DRIVER_CLASS_NAME;
  private val jdbcUrl : String = AppConfig.DB_JDBC_URL;
  private val jdbcUserName : String = AppConfig.DB_USER_NAME;
  private val jdbcPassWord : String = AppConfig.DB_PASSWORD;
  private val jdbcInitialSize : Int = 3;
  private val jdbcMaxActive : Int = 300;
  private val jdbcMinIdle : Int = 0;
  private val jdbcMaxWait : Int = 600000;
  private val jdbcValidationQuery : String ="SELECT 1";
  private val jdbcTestOnBorrow : Boolean = false;
  private val jdbcTestOnReturn : Boolean = false;
  private val jdbcTestWhileIdle : Boolean = true;
  private val poolPreparedStatements : Boolean=false;
  private val maxPoolPreparedStatementPerConnectionSize : Int=200;
  private val timeBetweenEvictionRunsMillis : Int = 60000;
  private val minEvictableIdleTimeMillis :  Int =25200000;
  private val removeAbandoned : Boolean = true;
  private val removeAbandonedTimeout : Int = 1800;
  private val logAbandoned : Boolean = true;

  var ds:DruidDataSource  = null;


  def getDataSource : DruidDataSource ={
    this.synchronized{
      if(ds == null){
        ds = new DruidDataSource();
        ds.setDriverClassName(jdbcDriverClass);
        ds.setUrl(jdbcUrl);
        ds.setUsername(jdbcUserName);
        ds.setPassword(jdbcPassWord);
        ds.setInitialSize(jdbcInitialSize);
        ds.setMaxActive(jdbcMaxActive);
        ds.setMinIdle(jdbcMinIdle);
        ds.setMaxWait(jdbcMaxWait);
        ds.setValidationQuery(jdbcValidationQuery);
        ds.setTestOnBorrow(jdbcTestOnBorrow);
        ds.setTestOnReturn(jdbcTestOnReturn);
        ds.setTestWhileIdle(jdbcTestWhileIdle);
        ds.setPoolPreparedStatements(poolPreparedStatements);
        ds.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        ds.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        ds.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        ds.setRemoveAbandoned(removeAbandoned);
        ds.setRemoveAbandonedTimeout(removeAbandonedTimeout);
        ds.setLogAbandoned(logAbandoned);
      }
      ds
    }
  }

  def getConnection :DruidPooledConnection = {
    var connect:DruidPooledConnection = null
    if(null != ds){
      connect = ds.getConnection
    }else{
      connect = getDataSource.getConnection;
    }
    connect
  }

  def closeConnection(rs:ResultSet,st:Statement,connect:DruidPooledConnection): Unit ={
    if(rs != null){rs.close}
    if(st != null){st.close}
    if(connect != null){connect.close}
  }

}
