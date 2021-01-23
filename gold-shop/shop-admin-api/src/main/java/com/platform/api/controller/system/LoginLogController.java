package com.platform.api.controller.system;

import com.platform.utils.StringUtil;
import com.platform.web.controller.BaseController;
import com.platform.bean.constant.factory.PageFactory;
import com.platform.bean.entity.system.LoginLog;
import com.platform.bean.enumeration.Permission;
import com.platform.bean.vo.front.Rets;
import com.platform.bean.vo.query.SearchFilter;
import com.platform.service.system.LoginLogService;
import com.platform.utils.BeanUtil;
import com.platform.utils.DateUtil;
import com.platform.utils.factory.Page;
import com.platform.warpper.LogWarpper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 登录日志controller
 *
 * @author wlhbdp
 * @version 2020/10/5 0005
 */
@RestController
@RequestMapping("/loginLog")
public class LoginLogController extends BaseController {
    @Autowired
    private LoginLogService loginlogService;
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions(value = {Permission.LOGIN_LOG})
    public Object list(@RequestParam(required = false) String beginTime,
                       @RequestParam(required = false) String endTime,
                       @RequestParam(required = false) String logName) {
        Page<LoginLog> page = new PageFactory<LoginLog>().defaultPage();
        if(StringUtil.isNotEmpty(beginTime)) {
            page.addFilter("createTime", SearchFilter.Operator.GTE, DateUtil.parseDate(beginTime));
        }
        if(StringUtil.isNotEmpty(endTime)) {
            page.addFilter("createTime", SearchFilter.Operator.LTE, DateUtil.parseDate(endTime));
        }
        page.addFilter( "logname", SearchFilter.Operator.LIKE, logName);
        Page pageResult = loginlogService.queryPage(page);
        pageResult.setRecords((List<LoginLog>) new LogWarpper(BeanUtil.objectsToMaps(pageResult.getRecords())).warp());
        return Rets.success(pageResult);

    }


    /**
     * 清空日志
     */
    @RequestMapping(method = RequestMethod.DELETE)
    @RequiresPermissions(value = {Permission.LOGIN_LOG_CLEAR})
    public Object clear() {
        loginlogService.clear();
        return Rets.success();
    }
}
