package com.platform.service.task;


import com.platform.bean.entity.system.TaskLog;
import com.platform.dao.system.TaskLogRepository;
import com.platform.service.BaseService;
import org.springframework.stereotype.Service;

/**
 * 定时任务日志服务类
 * @author wlhbdp
 * @date 2020-08-13
 */
@Service
public class TaskLogService extends BaseService<TaskLog,Long,TaskLogRepository> {
}
