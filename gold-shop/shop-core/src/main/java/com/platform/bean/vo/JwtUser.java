package com.platform.bean.vo;

import com.platform.bean.core.AuthorizationUser;
import com.platform.bean.entity.shop.ShopUser;
import com.platform.bean.entity.system.User;
import lombok.Data;

/**
 * 由于前后端使用一个基础模块：shop-core公用了一些权限处理代码，所以封装奔雷用来做前后端处理登录的用户信息实体类
 * @author wlhbdp
 * @date ：Created in 11/4/2020 8:16 PM
 */
@Data
public class JwtUser {
    public static  int MANAGER= 0;
    public static  int FRONT_USER= 1;
    private String userName;
    private String password;
    private Long id;

    /**
     * 刷新token的时候使用的用户信息
     * @param user
     */
    public JwtUser(AuthorizationUser user){
        this.userName = user.getAccount();
        this.password = user.getPassword();
        this.id = user.getId();
    }

    /**
     * 前端登录用户信息
     * @param user
     */
    public JwtUser(ShopUser user){
        this.userName = user.getMobile();
        this.id = user.getId();
        this.password = user.getPassword();
    }

    /**
     * 后端登录用户信息
     * @param user
     */
    public JwtUser(User user){
        this.userName = user.getAccount();
        this.id = user.getId();
        this.password = user.getPassword();
    }
}
