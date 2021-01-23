package com.platform.warpper;

import com.platform.service.system.impl.ConstantFactory;

import java.util.Map;

/**
 * 部门列表的包装
 *
 * @author wlhbdp
 * @date 2020年4月25日 18:10:31
 */
public class NoticeWrapper extends BaseControllerWarpper {

    public NoticeWrapper(Object list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        Long creater = (Long) map.get("createBy");
        map.put("createrName", ConstantFactory.me().getUserNameById(creater));
    }

}
