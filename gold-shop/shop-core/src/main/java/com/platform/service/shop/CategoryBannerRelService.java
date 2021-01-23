package com.platform.service.shop;


import com.platform.bean.entity.shop.CategoryBannerRel;
import com.platform.dao.shop.CategoryBannerRelRepository;

import com.platform.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryBannerRelService extends BaseService<CategoryBannerRel,Long,CategoryBannerRelRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CategoryBannerRelRepository categoryBannerRelRepository;

}

