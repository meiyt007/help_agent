package com.zfsoft.microservice.platform.controller.unauthentication;

import com.zfsoft.microservice.platform.manager.sys.SysLoginManager;
import com.zfsoft.microservice.platform.service.unauthentication.SysLoginJobService;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录任务Controller
 * @author zhongxx
 * @date 2021-03-11 11:31
 */
@RestController
public class SysLoginJobController  implements SysLoginJobService {

    @Autowired
    private SysLoginManager sysLoginManager;

    @Override
    public ApiResultSet delockSysLoginList() {
        return sysLoginManager.delockSysLoginList();
    }
}
