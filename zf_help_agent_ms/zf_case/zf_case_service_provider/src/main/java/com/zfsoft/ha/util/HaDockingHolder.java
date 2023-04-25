package com.zfsoft.ha.util;

/**
* Description: 第三方接口信息类

* @author zhaobf
* date: 2023/3/27 15:43
* @copyright 版权由上海卓繁信息技术股份有限公司拥有
*/
public class HaDockingHolder {
    /**
     * 本地线程变量：用于保存登录用户信息
     */
    private static final ThreadLocal<String> HaLoginUserThreadLocal = new ThreadLocal<>();

    /**
     * @description: 添加当前登录用户方法
     * @return: void
     * @author: kangax
     * @date: 2022-07-27 11:07
     */
    public static void addCurrentUser(String userName) {
        HaLoginUserThreadLocal.set(userName);
    }

    /**
     * @description: 获取当前登录用户方法
     * @return: com.zfsoft.ha.data.LoginUserInfo
     * @author: kangax
     * @date: 2022-07-27 11:16
     */
    public static String getCurrentUser() {
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
