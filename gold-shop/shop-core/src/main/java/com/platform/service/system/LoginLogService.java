package com.platform.service.system;


import com.platform.bean.entity.system.LoginLog;
import com.platform.dao.system.LoginLogRepository;
import com.platform.service.BaseService;
import org.springframework.stereotype.Service;

/**
 * Created  on 2018/3/26 0026.
 *
 * @author wlhbdp
 */
@Service
public class LoginLogService extends BaseService<LoginLog,Long,LoginLogRepository> {

}
