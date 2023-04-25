package org.activiti.engine.impl.identity;

import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;

/**
 * @ClassName Authentication
 * @Description: 重写activiti的权限验证方法
 * @Author wuxx
 * @Date 2021/1/13
 **/
public abstract class Authentication {
    static ThreadLocal<String> authenticatedUserIdThreadLocal = new ThreadLocal();

    public Authentication() {
    }

    public static void setAuthenticatedUserId(String authenticatedUserId) {
        authenticatedUserIdThreadLocal.set(authenticatedUserId);
    }

    public static String getAuthenticatedUserId() {
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        if(null==loginUser){
            return authenticatedUserIdThreadLocal.get();
        }
        return loginUser.getUserOid();
    }
}

