package com.platform.service.shop;


import com.platform.bean.constant.cache.Cache;
import com.platform.bean.entity.shop.AttrVal;
import com.platform.bean.vo.query.SearchFilter;
import com.platform.dao.shop.AttrValRepository;
import com.platform.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttrValService extends BaseService<AttrVal,Long, AttrValRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private AttrValRepository attrValRepository;
    @CacheEvict(value = Cache.APPLICATION, key = "#root.targetClass.simpleName+':'+#idCategory")
    public List<AttrVal> queryBy(Long idCategory) {
        return queryAll(SearchFilter.build("idCategory",idCategory));
    }
}

