# Spark-Hive-ETL

## spark sql书写注意事项

1. json文件中配置的sql注意在末尾不要加';'
2. 在sql内部，如果需要插入宏变量，在select部分用`#{variable_name}`，在where部分则使用`${variable_name}`.
3. json配置文件中，`taskName`和`tableName`不一定要保持一致。`taskName`必须和json配置文件名保持一致，而`tableName`须和目标数据源中表名保持一致。

## 生成json配置文件

- 首先确定json配置文件的格式，将其保存在`resources/templates`目录下；
- 其次，将模板文件复制到`https://jsonschema.net/`该网站生成对应的schema校验文件，也保存到`resources/templates`目录下；

## 开发环境下运行任务的配置
重构后的spark离线sql框架支持在jar包外部配置sql脚本，方便进行快速开发，不用每次开发一个sql脚本都重新打jar包。

> 注意：
> - 开发环境：在`Program Arguments`中添加参数时，如果脚本是在项目之外，则需添加脚本前缀`/scripts`，比如：`hour salary_b_count /scripts/test`
> - 测试、生产环境：在配置shell脚本时，也需要注意，如果脚本是在项目之外，要添加脚本前缀`/scripts`

## 任务调度脚本编写
可参考`resources/templates`目录下`task_script.sh`文件编写；

## 运行结果写入Hive
当需要将运行结果写入hive表中时，可能需要配置额外的参数`extra`：
- saveMode: 保存方式，默认是`append`,支持`overwrite, append, ignore, error(errorifexists)`等；
- saveFormat: 数据的落地文件格式，默认是`parquet`，支持`parquet, csv, json, orc`等；
- partitionFieldName: 分区字段名称；
- partitionStrategy: 分区策略，如按day(天)，按month(月)；