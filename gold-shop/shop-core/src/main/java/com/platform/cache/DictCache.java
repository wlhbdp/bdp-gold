package com.platform.cache;

import com.platform.bean.entity.system.Dict;

import java.util.List;

/**
 * 字典缓存
 *
 * @author wlhbdp
 */
public interface DictCache  extends Cache{

    List<Dict> getDictsByPname(String dictName);
    String getDict(Long dictId);
}
