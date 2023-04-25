package com.zfsoft.platform.auth.manager;

import com.zfsoft.platform.auth.feign.SysUserFeignService;
import com.zfsoft.platform.auth.service.LockSysLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: kkfan
 * @create: 2021-03-23 18:40:02
 * @description: 账户锁定方法
 */
@Service
public class LockSysLoginManage implements LockSysLoginService {

    @Autowired
    private SysUserFeignService sysUserFeignService;

    @Override
    public void lockSysLogin(String userName) {
        this.sysUserFeignService.lockSysLogin(userName);
    }
}
