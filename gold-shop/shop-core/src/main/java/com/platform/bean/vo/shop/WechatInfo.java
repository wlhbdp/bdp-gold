package com.platform.bean.vo.shop;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wlhbdp
 * @date ：Created in 2020/3/10 14:06
 */
@Data
public class WechatInfo implements Serializable {
    private String nickName;
    private String headUrl;
    private String openId;
}
