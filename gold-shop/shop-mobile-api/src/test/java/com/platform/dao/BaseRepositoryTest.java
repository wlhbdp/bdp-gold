package com.platform.dao;

import com.platform.BaseApplicationStartTest;
import com.platform.dao.system.UserRepository;
import com.platform.vo.UserVo;
import org.junit.Test;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wlhbdp
 * @date ：Created in 2020/3/13 11:48
 */
public class BaseRepositoryTest extends BaseApplicationStartTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void queryBySql(){

        String sql = "select sex,count(1) as count from t_sys_user group by sex";
        List list = userRepository.queryBySql(sql, UserVo.class);
        System.out.println(Json.toJson(list));
    }
    @Test
    public void getBySql(){
        String sql = "select sex,count(1) as count from t_sys_user group by sex  having sex=1";
        UserVo ret = (UserVo) userRepository.getBySql(sql,UserVo.class);
        System.out.println(Json.toJson(ret));
    }

}
