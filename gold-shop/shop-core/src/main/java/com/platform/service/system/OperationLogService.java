package com.platform.service.system;

import com.platform.bean.entity.system.OperationLog;
import com.platform.dao.system.OperationLogRepository;
import com.platform.service.BaseService;
import org.springframework.stereotype.Service;

/**
 * Created  on 2018/3/26 0026.
 *
 * @author wlhbdp
 */
@Service
public class OperationLogService extends BaseService<OperationLog,Long,OperationLogRepository> {

}
