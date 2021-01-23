package com.platform.service.task.job;

import com.platform.bean.entity.system.Cfg;
import com.platform.service.system.CfgService;
import com.platform.service.task.JobExecuter;
import org.nutz.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * HelloJob
 *
 * @author wlhbdp
 * @version 2020/12/30 0030
 */
@Component
public class HelloJob extends JobExecuter {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CfgService cfgService;
    @Override
    public void execute(Map<String, Object> dataMap) throws Exception {
        Cfg cfg = cfgService.get(1L);
        cfg.setCfgDesc("应用名称");
        cfgService.update(cfg);
        logger.info("hello :"+ Json.toJson(dataMap));
    }
}
