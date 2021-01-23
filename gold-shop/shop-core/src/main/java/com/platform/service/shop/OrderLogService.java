package com.platform.service.shop;


import com.platform.bean.entity.shop.OrderLog;
import com.platform.dao.shop.OrderLogRepository;

import com.platform.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderLogService extends BaseService<OrderLog,Long,OrderLogRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private OrderLogRepository orderLogRepository;

}

