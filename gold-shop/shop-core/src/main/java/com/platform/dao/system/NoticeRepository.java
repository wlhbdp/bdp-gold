package com.platform.dao.system;


import com.platform.bean.entity.system.Notice;
import com.platform.dao.BaseRepository;

import java.util.List;

/**
 * Created  on 2018/3/21 0021.
 *
 * @author wlhbdp
 */
public interface NoticeRepository extends BaseRepository<Notice,Long> {
    List<Notice> findByTitleLike(String name);
}
