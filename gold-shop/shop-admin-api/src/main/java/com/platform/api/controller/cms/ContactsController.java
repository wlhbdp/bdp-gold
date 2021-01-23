package com.platform.api.controller.cms;

import com.platform.web.controller.BaseController;
import com.platform.bean.constant.factory.PageFactory;
import com.platform.bean.entity.cms.Contacts;
import com.platform.bean.enumeration.Permission;
import com.platform.bean.vo.front.Rets;
import com.platform.bean.vo.query.SearchFilter;
import com.platform.service.cms.ContactsService;
import com.platform.utils.DateUtil;
import com.platform.utils.factory.Page;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 邀约信息管理
 */
@RestController
@RequestMapping("/contacts")
public class ContactsController extends BaseController {
    @Autowired
    private ContactsService contactsService;
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions(value = {Permission.CONTACTS})
    public Object list(@RequestParam(required = false) String userName,
                       @RequestParam(required = false) String mobile,
                       @RequestParam(required = false) String startDate,
                       @RequestParam(required = false) String endDate

    ) {
        Page<Contacts> page = new PageFactory<Contacts>().defaultPage();
        page.addFilter("createTime", SearchFilter.Operator.GTE, DateUtil.parse(startDate,"yyyyMMddHHmmss"));
        page.addFilter("createTime", SearchFilter.Operator.LTE, DateUtil.parse(endDate,"yyyyMMddHHmmss"));
        page.addFilter("userName", SearchFilter.Operator.EQ,userName);
        page.addFilter("mobile", SearchFilter.Operator.EQ,mobile);
        page = contactsService.queryPage(page);
        return Rets.success(page);
    }
}
