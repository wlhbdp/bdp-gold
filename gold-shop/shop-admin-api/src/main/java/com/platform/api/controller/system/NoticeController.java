package com.platform.api.controller.system;

import com.platform.web.controller.BaseController;
import com.platform.bean.entity.system.Notice;
import com.platform.bean.vo.front.Rets;
import com.platform.service.system.NoticeService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * NoticeController
 *
 * @author wlhbdp
 * @version 2020/12/17 0017
 */
@RestController
@RequestMapping("/notice")
public class NoticeController extends BaseController {
    @Autowired
    private NoticeService noticeService;
    /**
     * 获取通知列表
     */
    @RequestMapping(value = "/list")
    public Object list(String condition) {
        List<Notice> list = null;
        if(Strings.isNullOrEmpty(condition)) {
            list =  noticeService.queryAll();
        }else{
            list = noticeService.findByTitleLike(condition);
        }
        return Rets.success(list);
    }
}
