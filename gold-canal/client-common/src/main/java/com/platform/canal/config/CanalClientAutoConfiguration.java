package com.platform.canal.config;

import com.alibaba.otter.canal.client.kafka.KafkaCanalConnector;
import com.platform.canal.properties.CanalProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author wlhbdp
 * @ClassName: CanalClientAutoConfiguration
 * @Description: CanalClientAutoConfiguration
 */
@EnableConfigurationProperties(CanalProperties.class)
@org.springframework.context.annotation.Configuration
public class CanalClientAutoConfiguration {


    @Autowired
    private CanalProperties canalProperties;


    @Bean
    @ConditionalOnMissingBean(KafkaCanalConnector.class)
    public KafkaCanalConnector kafkaCanalConnector() {
        return new KafkaCanalConnector(canalProperties.getMqServers(), canalProperties.getTopic(), canalProperties.getPartition(), canalProperties.getGroupId(),true);
    }

}
