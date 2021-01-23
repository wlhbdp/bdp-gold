
package com.platform.dao.system;

import com.platform.bean.entity.system.Cfg;
import com.platform.dao.BaseRepository;

/**
 * 全局参数dao
 *
 * @author wlhbdp
 * @date ：Created in 2019/6/29 12:50
 */
public interface CfgRepository extends BaseRepository<Cfg,Long> {

    Cfg findByCfgName(String cfgName);
}
