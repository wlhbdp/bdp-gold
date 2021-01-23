package com.platform.dao.system;


import com.platform.bean.entity.system.User;
import com.platform.dao.BaseRepository;

/**
 * Created  on 2018/3/21 0021.
 *
 * @author wlhbdp
 */
public interface UserRepository extends BaseRepository<User,Long> {
    User findByAccount(String account);

    User findByAccountAndStatusNot(String account, Integer status);
}
