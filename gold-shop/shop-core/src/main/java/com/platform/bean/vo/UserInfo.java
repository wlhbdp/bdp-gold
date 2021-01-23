package com.platform.bean.vo;

import com.platform.utils.StringUtil;
import lombok.Data;

import java.util.Date;

/**
 * @author wlhbdp
 * @date ：Created in 11/6/2020 11:29 AM
 */
@Data
public class UserInfo {
    private String mobile;
    private String avatar;
    private String nickName;
    private Date lastLoginTime;
    private String gender;
    private String wechatNickName;
    private String wechatHeadImgUrl;
    private Boolean refreshWechatInfo = true;

    public String getNickName() {
        return StringUtil.isEmpty(nickName)?wechatNickName:nickName;
    }
}
