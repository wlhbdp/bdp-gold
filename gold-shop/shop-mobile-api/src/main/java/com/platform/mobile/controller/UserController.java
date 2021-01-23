package com.platform.mobile.controller;

import com.platform.bean.entity.shop.ShopUser;
import com.platform.bean.entity.system.FileInfo;
import com.platform.bean.vo.UserInfo;
import com.platform.bean.vo.front.Rets;
import com.platform.bean.vo.shop.WechatInfo;
import com.platform.cache.CacheDao;
import com.platform.security.JwtUtil;
import com.platform.service.api.WeixinService;
import com.platform.service.shop.ShopUserService;
import com.platform.service.system.FileService;
import com.platform.utils.MD5;
import com.platform.utils.StringUtil;
import com.platform.web.controller.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author wlhbdp
 * @date ：Created in 11/6/2020 4:20 PM
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private ShopUserService shopUserService;
    @Autowired
    private WeixinService weixinService;
    @Autowired
    private FileService fileService;
    @Autowired
    private CacheDao cacheDao;
    @RequestMapping(value = "/getInfo",method = RequestMethod.GET)
    public Object getInfo(){
        String token = getToken();
        Long idUser = JwtUtil.getUserId(token);
         ShopUser shopUser = shopUserService.get(idUser);
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(shopUser,userInfo);
        WechatInfo wechatInfo = cacheDao.hget(CacheDao.SESSION,"WECHAT_INFO"+shopUser.getId(),WechatInfo.class);
        if(wechatInfo!=null){
            userInfo.setRefreshWechatInfo(false);
        }
        return Rets.success(userInfo);
    }
    @RequestMapping(value = "/updateUserName/{userName}",method = RequestMethod.POST)
    public Object updateUserName(@PathVariable("userName") String userName){
        ShopUser user = shopUserService.getCurrentUser();
        user.setNickName(userName);
        shopUserService.update(user);
        return Rets.success(user);
    }

    @RequestMapping(value = "/updateGender/{gender}",method = RequestMethod.POST)
    public Object updateGender(@PathVariable("gender") String gender){
        ShopUser user = shopUserService.getCurrentUser();
        user.setGender(gender);
        shopUserService.update(user);
        return Rets.success(user);
    }

    @RequestMapping(value = "/updatePassword/{oldPwd}/{password}/{rePassword}",method = RequestMethod.POST)
    public Object updatePassword(@PathVariable("oldPwd") String oldPwd,
                                 @PathVariable("password") String password,
                                 @PathVariable("rePassword") String rePassword){
        if(StringUtil.isEmpty(oldPwd) || StringUtil.isEmpty(password) || StringUtil.isEmpty(rePassword)){
            return  Rets.failure("项目并能为空");
        }
        if(!StringUtil.equals(password,rePassword)){
            return Rets.failure("密码前后不一致");
        }
        ShopUser user = shopUserService.getCurrentUser();
        String oldPasswdMd5 = MD5.md5(oldPwd, user.getSalt());
        if(!StringUtil.equals(oldPasswdMd5,user.getPassword())){
            return Rets.failure("旧密码不正确");
        }
        user.setPassword(MD5.md5(password,user.getSalt()));
        shopUserService.update(user);
        return Rets.success();
    }
    @RequestMapping(value = "sendSmsCode",method = RequestMethod.POST)
    public Object sendSmsCode(@RequestParam String mobile){
        String smsCode = shopUserService.sendSmsCodeForOldMobile(mobile);
        return Rets.success(smsCode);
    }
    @RequestMapping(value = "getWxOpenId",method = RequestMethod.POST)
    public  Object getWxOpenId(String code, HttpServletRequest request) {
        ShopUser user = shopUserService.getCurrentUser();
        boolean wxAuth = weixinService.isAuth(user,code);
        return wxAuth? Rets.success():Rets.failure("获取openid失败");
    }
    @RequestMapping(value = "getWxSign", method = RequestMethod.POST)
    public Object getWxSign(@RequestParam("url") String url) {
        Map<String, String> map = weixinService.getSign(url);
        return Rets.success(map);
    }
    @RequestMapping(value="uploadAvatar",method = RequestMethod.POST)
    public Object uploadAvatar(@RequestPart("file") MultipartFile multipartFile) {

        try {
            FileInfo fileInfo = fileService.upload(multipartFile);
            ShopUser user = shopUserService.getCurrentUser();
            user.setAvatar(String.valueOf(fileInfo.getRealFileName()));
            shopUserService.update(user);
            return Rets.success(fileInfo);
        } catch (Exception e) {
            logger.error("上传头像失败",e);
            return Rets.failure("上传头像失败");
        }
    }
}
