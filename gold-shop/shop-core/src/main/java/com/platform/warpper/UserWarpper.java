package com.platform.warpper;

import com.platform.service.system.impl.ConstantFactory;
import com.platform.utils.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * 用户管理的包装类
 *
 * @author wlhbdp
 * @date 2020年2月13日 下午10:47:03
 */
public class UserWarpper extends BaseControllerWarpper {

    public UserWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        map.put("sexName", ConstantFactory.me().getSexName((Integer) map.get("sex")));
        if(StringUtil.isNotNullOrEmpty(map.get("roleid"))) {
            map.put("roleName", ConstantFactory.me().getRoleName((String) map.get("roleid")));
        }
        if(StringUtil.isNotNullOrEmpty(map.get("deptid"))) {
            map.put("deptName", ConstantFactory.me().getDeptName((Long) map.get("deptid")));
        }
        map.put("statusName", ConstantFactory.me().getStatusName((Integer) map.get("status")));

    }

}
