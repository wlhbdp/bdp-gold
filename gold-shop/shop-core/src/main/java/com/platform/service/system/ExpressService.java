package com.platform.service.system;


import com.platform.bean.entity.system.Express;
import com.platform.dao.system.ExpressRepository;

import com.platform.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpressService extends BaseService<Express,Long,ExpressRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ExpressRepository expressRepository;

    public void changeDisabled(Long id, Boolean disabled) {
        Express express = get(id);
        express.setDisabled(disabled);
        update(express);
    }
}

