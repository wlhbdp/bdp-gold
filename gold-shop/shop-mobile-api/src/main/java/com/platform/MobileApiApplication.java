package com.platform;

import com.platform.config.WxProperties;
import com.platform.dao.BaseRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * ApiApplication
 *
 * @author wlhbdp
 * @version 2020/9/11 0011
 */
@EnableCaching
@SpringBootApplication
@EntityScan(basePackages="com.platform.bean.entity")
@EnableJpaRepositories(basePackages = "com.platform.dao", repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
@EnableJpaAuditing
@EnableConfigurationProperties(WxProperties.class)
public class MobileApiApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MobileApiApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(MobileApiApplication.class);
    }
}
