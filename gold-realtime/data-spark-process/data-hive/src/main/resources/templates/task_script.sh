#!/usr/bin/env bash

###获取当前项目绝对路径###
#project_home=$(dirname $(readlink -f "$0"))"/.."
project_home="$(readlink -f $(cd "`dirname "$0"`"/..; pwd))"
###获取script绝对路径###
script_path=${project_home}"/scripts/"
lib_path=${project_home}"/lib/"
jar_path=${project_home}"/jar/"
###获取项目中script目录下所有的脚本文件
files=$(ls ${script_path});
files=${files// / };
file_arr=(${files});
files_str=""
for ele in ${file_arr[*]}
do
  file_str=${file_str}${script_path}${ele},
done
len=`expr ${#file_str} - 1`
file_str=`expr substr "$file_str" 1 $len`
echo ${file_str}

spark2-submit \
    --class platform.HiveEtl \
    --master yarn \
    --deploy-mode cluster  \
    --name spark_hive_etl_hour \
    --executor-memory 8G \
    --driver-memory 4g \
    --num-executors 4 \
    --executor-cores 2 \
    --files ${file_str} \
    --jars ${lib_path}"fastjson-1.2.7.jar",${lib_path}"druid-1.1.10.jar",${lib_path}"mysql-connector-java-5.1.38.jar",${lib_path}"commons-lang3-3.6.jar",${lib_path}"org.everit.json.schema-1.11.1.jar",${lib_path}"json-20180130.jar",${lib_path}"handy-uri-templates-2.1.6.jar" \
    ${jar_path}"original-spark-hive-etl-1.0.0.jar" hour daily_salary_daily_consume_amt /scripts/test
