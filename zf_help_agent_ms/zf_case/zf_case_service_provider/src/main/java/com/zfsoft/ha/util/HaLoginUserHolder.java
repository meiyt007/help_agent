package com.zfsoft.ha.util;

import com.zfsoft.ha.data.HaLoginUserInfo;

/**
 * 用户登录信息工具类
 *
 * @author kangax
 * @version 1.0
 * @date 2022/7/25 11:10
 */
public class HaLoginUserHolder {
    /**
     * 本地线程变量：用于保存登录用户信息
     */
    private static final ThreadLocal<HaLoginUserInfo> HaLoginUserThreadLocal = new ThreadLocal<>();

    /**
     * @description: 添加当前登录用户方法
     * @return: void
     * @author: kangax
     * @date: 2022-07-27 11:07
     */
    public static void addCurrentHaUserInfo(HaLoginUserInfo haLoginUserInfo) {
        HaLoginUserThreadLocal.set(haLoginUserInfo);
    }

    /**
     * @description: 获取当前登录用户方法
     * @return: com.zfsoft.ha.data.LoginUserInfo
     * @author: kangax
     * @date: 2022-07-27 11:16
     */
    public static HaLoginUserInfo getCurrentHaLoginUserInfo() {
        return HaLoginUserThreadLocal.get();
    }

    /**
     * @description: 防止内存泄露方法
     * @return: void
     * @author: kangax
     * @date: 2022-07-27 11:17
     */
    public static void removeHaThreadLocal() {
        HaLoginUserThreadLocal.remove();
    }

}
