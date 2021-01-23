package com.platform.warpper;

import com.platform.service.system.impl.ConstantFactory;

import java.util.Map;

/**
 * 部门列表的包装
 *
 * @author wlhbdp
 * @date 2020年4月25日 18:10:31
 */
public class DeptWarpper extends BaseControllerWarpper {

    public DeptWarpper(Object list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {

        Long pid = (Long) map.get("pid");

        if (pid == null || pid.intValue() == 0 ) {
            map.put("pName", "--");
        } else {
            map.put("pName", ConstantFactory.me().getDeptName(pid));
        }
    }

}
