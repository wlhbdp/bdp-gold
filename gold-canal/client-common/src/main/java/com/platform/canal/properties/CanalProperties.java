package com.platform.canal.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wlhbdp
 * @ClassName: CanalProperties
 * @Description: canal配置文件
 */
@ConfigurationProperties(prefix = "fox.canal")
@Data
public class CanalProperties {


    /**
     * mq server
     */
    private String mqServers;

    /**
     * mq topic
     */
    private String topic;

    /**
     * mq partition
     */
    private Integer partition;

    /**
     * mq consumer groupId
     */
    private String groupId;

    /**
     * 是否为json格式
     * 如果设置为false,对应MQ收到的消息为protobuf格式
     * 需要通过CanalMessageDeserializer进行解码
     */
    private boolean flatMessage;


    /**
     * 白名单列表
     */
    private String whiteList;



}
