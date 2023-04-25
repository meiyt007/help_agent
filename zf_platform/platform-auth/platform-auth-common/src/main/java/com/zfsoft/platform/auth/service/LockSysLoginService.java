package com.zfsoft.platform.auth.service;

/**
 * @author: kkfan
 * @create: 2021-03-23 18:38:07
 * @description: 账户锁定
 */
public interface LockSysLoginService {
    void lockSysLogin(String userName);
}
