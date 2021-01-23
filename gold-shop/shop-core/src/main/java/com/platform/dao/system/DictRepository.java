
package com.platform.dao.system;


import com.platform.bean.entity.system.Dict;
import com.platform.dao.BaseRepository;

import java.util.List;

public interface DictRepository  extends BaseRepository<Dict,Long> {
    List<Dict> findByPid(Long pid);
    List<Dict> findByNameAndPid(String name,Long pid);

    List<Dict> findByNameLike(String name);
}
