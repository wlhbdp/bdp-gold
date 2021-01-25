# 总项目 https://github.com/wlhbdp/bdp-ecology
# 2、大数据生态解决方案金铺数据分析 bdp-gold

2.1 个性化推荐系统 gold-recommender

2.2 日志收集系统 gold-logclient gold-logserver

2.3 人群画像系统 gold-profile

2.4 数据传输系统

2.5 实时计算系统

2.6 反作弊系统 gold-anti-fraud

2.7 多维度分析系统 gold-multianaly

2.8 商场系统 gold-shop

    埋点：
        前端埋点，后端起一个服务，实时消费kafka队列的消息，然后做流计算统计
        前端调用埋点api到后端上报到kafka数据一致，前端调用失败 后端上报失败，失败重传 数据格式校验
        android开发埋点：https://github.com/foolchen/AndroidTracker
       
        
13 技术交流:
    
    微信交流群：https://my-macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/design/wechat_group/bdp_%E4%BA%A4%E6%B5%81%E7%BE%A4.jpg
    个人微信：https://my-macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/wechat/%E5%BE%AE%E4%BF%A1.jpg