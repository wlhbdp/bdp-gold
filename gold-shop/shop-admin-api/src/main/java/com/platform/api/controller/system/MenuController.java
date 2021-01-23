package com.platform.api.controller.system;

import com.platform.bean.constant.state.MenuStatus;
import com.platform.bean.core.AuthorizationUser;
import com.platform.bean.core.BussinessLog;
import com.platform.bean.entity.system.Menu;
import com.platform.bean.enumeration.Permission;
import com.platform.bean.exception.ApplicationException;
import com.platform.bean.exception.ApplicationExceptionEnum;
import com.platform.bean.vo.front.Rets;
import com.platform.bean.vo.node.MenuNode;
import com.platform.bean.vo.node.Node;
import com.platform.bean.vo.node.RouterMenu;
import com.platform.bean.vo.node.ZTreeNode;
import com.platform.cache.TokenCache;
import com.platform.service.system.LogObjectHolder;
import com.platform.service.system.MenuService;
import com.platform.service.system.impl.ConstantFactory;
import com.platform.utils.HttpUtil;
import com.platform.utils.Maps;
import com.platform.utils.StringUtil;
import com.platform.web.controller.BaseController;
import com.google.common.collect.Lists;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * MenuController
 *
 * @author wlhbdp
 * @version 2020/9/12 0012
 */
@RestController
@RequestMapping("/menu")
public class MenuController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(MenuController.class);
    @Autowired
    private MenuService menuService;
    @Autowired
    private TokenCache tokenCache;

    @RequestMapping(value = "/listForRouter", method = RequestMethod.GET)
    public Object listForRouter() {
        AuthorizationUser shiroUser = tokenCache.getUser(HttpUtil.getToken());

        List<RouterMenu> list = menuService.getSideBarMenus(shiroUser.getRoleList());
        return Rets.success(list);
    }
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @RequiresPermissions(value = {Permission.MENU})
    public Object list() {
        List<MenuNode> list = menuService.getMenus();
        return Rets.success(list);
    }

    @RequestMapping(method = RequestMethod.POST)
    @BussinessLog(value = "编辑菜单", key = "name")
    @RequiresPermissions(value = {Permission.MENU_EDIT})
    public Object save(@ModelAttribute @Valid Menu menu) {
        //判断是否存在该编号
        if(menu.getId()==null) {
            String existedMenuName = ConstantFactory.me().getMenuNameByCode(menu.getCode());
            if (StringUtil.isNotEmpty(existedMenuName)) {
                throw new ApplicationException(ApplicationExceptionEnum.EXISTED_THE_MENU);
            }
            menu.setStatus(MenuStatus.ENABLE.getCode());
        }

        //设置父级菜单编号
        menuService.menuSetPcode(menu);
        if(menu.getId()==null){
            menuService.insert(menu);
        }else {
            Menu old = menuService.get(menu.getId());
            LogObjectHolder.me().set(old);
            menuService.update(menu);
        }
        return Rets.success();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @BussinessLog(value = "删除菜单", key = "id")
    @RequiresPermissions(value = {Permission.MENU_DEL})
    public Object remove(@RequestParam Long id) {
        logger.info("id:{}", id);
        if (id == null) {
            throw new ApplicationException(ApplicationExceptionEnum.REQUEST_NULL);
        }
        //演示环境不允许删除初始化的菜单
        if(id.intValue()<70){
            return Rets.failure("演示环境不允许删除初始菜单");
        }
        //缓存菜单的名称
        LogObjectHolder.me().set(ConstantFactory.me().getMenuName(id));
        menuService.delMenuContainSubMenus(id);
        return Rets.success();
    }

    /**
     * 获取菜单树
     */
    @RequestMapping(value = "/menuTreeListByRoleId", method = RequestMethod.GET)
    @RequiresPermissions(value = {Permission.MENU})
    public Object menuTreeListByRoleId(Integer roleId) {
        List<Long> menuIds = menuService.getMenuIdsByRoleId(roleId);
        List<ZTreeNode> roleTreeList = null;
        if (menuIds==null||menuIds.isEmpty()) {
            roleTreeList = menuService.menuTreeList(null);
        } else {
            roleTreeList = menuService.menuTreeList(menuIds);

        }
        List<Node> list = menuService.generateMenuTreeForRole(roleTreeList);

        //element-ui中tree控件中如果选中父节点会默认选中所有子节点，所以这里将所有非叶子节点去掉
        Map<Long,ZTreeNode> map = com.platform.utils.Lists.toMap(roleTreeList,"id");
        Map<Long,List<ZTreeNode>> group = com.platform.utils.Lists.group(roleTreeList,"pId");
        for(Map.Entry<Long,List<ZTreeNode>> entry:group.entrySet()){
            if(entry.getValue().size()>1){
                roleTreeList.remove(map.get(entry.getKey()));
            }
        }

        List<Long> checkedIds = Lists.newArrayList();
        for (ZTreeNode zTreeNode : roleTreeList) {
            if (zTreeNode.getChecked() != null && zTreeNode.getChecked()
                    &&zTreeNode.getpId().intValue()!=0) {
                checkedIds.add(zTreeNode.getId());
            }
        }
        return Rets.success(Maps.newHashMap("treeData", list, "checkedIds", checkedIds));
    }
}
