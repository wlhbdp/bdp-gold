package com.platform.dao.shop;


import com.platform.bean.entity.shop.ShopUser;
import com.platform.dao.BaseRepository;


public interface ShopUserRepository extends BaseRepository<ShopUser,Long> {

    ShopUser findByMobile(String mobile);

    ShopUser findByWechatOpenId(String openId);
}

