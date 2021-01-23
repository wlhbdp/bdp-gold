package com.platform.api.controller.system;

import com.platform.bean.core.BussinessLog;
import com.platform.bean.entity.system.Dept;
import com.platform.bean.enumeration.Permission;
import com.platform.bean.exception.ApplicationException;
import com.platform.bean.exception.ApplicationExceptionEnum;
import com.platform.bean.vo.front.Rets;
import com.platform.bean.vo.node.DeptNode;
import com.platform.service.system.DeptService;
import com.platform.service.system.LogObjectHolder;
import com.platform.utils.BeanUtil;
import com.platform.web.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * DeptContoller
 *
 * @author wlhbdp
 * @version 2020/9/15 0015
 */
@RestController
@RequestMapping("/dept")
public class DeptContoller extends BaseController {
    private Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private DeptService deptService;
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions(value = {Permission.DEPT})
    public Object list(){
        List<DeptNode> list = deptService.queryAllNode();
        return Rets.success(list);
    }
    @RequestMapping(method = RequestMethod.POST)
    @BussinessLog(value = "编辑部门", key = "simplename")
    @RequiresPermissions(value = {Permission.DEPT_EDIT})
    public Object save(@ModelAttribute @Valid Dept dept){
        if (BeanUtil.isOneEmpty(dept, dept.getSimplename())) {
            throw new ApplicationException(ApplicationExceptionEnum.REQUEST_NULL);
        }
        if(dept.getId()!=null){
            Dept old = deptService.get(dept.getId());
            LogObjectHolder.me().set(old);
            old.setPid(dept.getPid());
            old.setSimplename(dept.getSimplename());
            old.setFullname(dept.getFullname());
            old.setNum(dept.getNum());
            old.setTips(dept.getTips());
            deptService.deptSetPids(old);
            deptService.update(old);
        }else {
            deptService.deptSetPids(dept);
            deptService.insert(dept);
        }
        return Rets.success();
    }
    @RequestMapping(method = RequestMethod.DELETE)
    @BussinessLog(value = "删除部门", key = "id")
    @RequiresPermissions(value = {Permission.DEPT_DEL})
    public Object remove(@RequestParam  Long id){
        logger.info("id:{}",id);
        if (id == null) {
            throw new ApplicationException(ApplicationExceptionEnum.REQUEST_NULL);
        }
        deptService.deleteDept(id);
        return Rets.success();
    }
}
