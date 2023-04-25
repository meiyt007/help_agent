package com.zfsoft.microservice.platform.service.unauthentication;

import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 登录任务Service
 * @author zhongxx
 * @date 2021-03-11 11:22
 */
@RequestMapping("/unauthentication")
public interface SysLoginJobService {


        /**
         * 解锁用户列表任务
         * @return
         */
        @GetMapping(value = "/syslogin/job/delock")
        ApiResultSet delockSysLoginList();


}
