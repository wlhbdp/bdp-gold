package com.platform.cache;

import com.platform.bean.vo.SpringContextHolder;
import com.platform.service.system.impl.ConstantFactory;

/**
 * @author wlhbdp
 * @date ：Created in 2020/4/26 19:07
 */
public abstract  class BaseCache implements Cache {
    @Override
    public void cache() {
        SpringContextHolder.getBean(ConstantFactory.class).cleanLocalCache();
    }
}
