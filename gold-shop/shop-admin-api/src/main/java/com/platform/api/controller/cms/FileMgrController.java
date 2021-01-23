package com.platform.api.controller.cms;

import com.platform.web.controller.BaseController;
import com.platform.bean.constant.factory.PageFactory;
import com.platform.bean.entity.system.FileInfo;
import com.platform.bean.enumeration.Permission;
import com.platform.bean.vo.front.Rets;
import com.platform.bean.vo.query.SearchFilter;
import com.platform.service.system.FileService;
import com.platform.utils.StringUtil;
import com.platform.utils.factory.Page;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fileMgr")
public class FileMgrController extends BaseController {

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @RequiresPermissions(value = {Permission.FILE})
    public Object list(@RequestParam(required = false) String originalFileName
    ) {
        Page<FileInfo> page = new PageFactory<FileInfo>().defaultPage();
        if (StringUtil.isNotEmpty(originalFileName)) {
            page.addFilter(SearchFilter.build("originalFileName", SearchFilter.Operator.LIKE, originalFileName));
        }
        page = fileService.queryPage(page);
        return Rets.success(page);
    }
}
