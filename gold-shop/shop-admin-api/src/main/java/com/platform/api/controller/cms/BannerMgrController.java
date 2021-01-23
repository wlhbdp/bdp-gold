package com.platform.api.controller.cms;

import com.platform.utils.JsonUtil;
import com.platform.web.controller.BaseController;
import com.platform.bean.core.BussinessLog;

import com.platform.bean.entity.cms.Banner;
import com.platform.bean.enumeration.Permission;
import com.platform.bean.vo.front.Rets;
import com.platform.bean.vo.query.SearchFilter;
import com.platform.service.cms.BannerService;
import com.platform.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * banner管理
 */
@RestController
@RequestMapping("/banner")
public class BannerMgrController extends BaseController {
    @Autowired
    private BannerService bannerService;

    @RequestMapping(method = RequestMethod.POST)
    @BussinessLog(value = "编辑banner", key = "title")
    @RequiresPermissions(value = {Permission.BANNER_EDIT})
    public Object save(@ModelAttribute @Valid Banner banner) {
        if(StringUtil.isNotEmpty(banner.getParam())){
            if(!JsonUtil.isJson(banner.getParam())){
                return Rets.failure("参数必须为json格式");
            }
        }
        if(banner.getId()==null){
            bannerService.insert(banner);
        }else {
            bannerService.update(banner);
        }
        return Rets.success();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @BussinessLog(value = "删除banner", key = "id")
    @RequiresPermissions(value = {Permission.BANNER_DEL})
    public Object remove(Long id) {
        bannerService.deleteById(id);
        return Rets.success();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @RequiresPermissions(value = {Permission.BANNER})
    public Object list(@RequestParam(required = false) String title) {
        SearchFilter filter = null;
        if(StringUtil.isNotEmpty(title)){
             filter =  SearchFilter.build("title", SearchFilter.Operator.LIKE,title);
        }
        List<Banner> list = bannerService.queryAll(filter);
        return Rets.success(list);
    }
}
