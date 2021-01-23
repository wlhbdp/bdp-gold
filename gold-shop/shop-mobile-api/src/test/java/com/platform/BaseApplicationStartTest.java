package com.platform;

import com.platform.dao.BaseRepositoryFactoryBean;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MobileApiApplication.class)
@EntityScan(basePackages="com.platform.bean.entity")
@EnableJpaRepositories(basePackages= "com.platform.dao", repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
@ComponentScan(basePackages = "com.platform")
@TestPropertySource(locations = {"classpath:application-test.properties"})

public class BaseApplicationStartTest {
    protected final Logger log= LoggerFactory.getLogger(getClass());

}
