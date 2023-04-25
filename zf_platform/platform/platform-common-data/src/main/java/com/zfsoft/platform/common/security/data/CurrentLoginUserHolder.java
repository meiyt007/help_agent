package com.zfsoft.platform.common.security.data;

import com.zfsoft.platform.common.data.BaseStaticParameter;

/**
 * @Auther: lijun@sstcsoft.com
 * @Date: 2019-10-15 10:11
 */
public class CurrentLoginUserHolder {

    //获取当前登录用户信息
    private static ThreadLocal<CurrentLoginUser> loginUser = new ThreadLocal<>();

    //获取当前登录用户主键
    public static String loginUserOid = null;

    /**
     * 获取当前登录用户信息
     * @return
     */
    public static CurrentLoginUser getCurrentLoginUser(){
        return loginUser.get();
    }

    /**
     * 设置登录用户信息
     * @param currentLoginUser
     */
    public static void setCurrentLoginUser(CurrentLoginUser currentLoginUser){
        loginUser.set(currentLoginUser);
    }

    /**
     * @description: 是否是公司管理人员 true - 是 false - 不是
     * @author: wuxx
     * @Date: 2020/10/26 15:00
     **/
    public static boolean getIsAdminUser() {
        CurrentLoginUser currentLoginUser = getCurrentLoginUser();
        if (null!=currentLoginUser && null!=currentLoginUser.getUserCode() && !"".equals(currentLoginUser.getUserCode())
                && currentLoginUser.getUserCode().trim().equals(BaseStaticParameter.ADMIN_PERSONEL)){
            return true;
        }
        return false;
    }

    /**
     * @description: 是否是公司实施人员 true - 是 false - 不是
     * @author: wuxx
     * @Date: 2020/10/26 15:00
     **/
    public static boolean getIsCompanyUser() {
        CurrentLoginUser currentLoginUser = getCurrentLoginUser();
        if (null!=currentLoginUser && null!=currentLoginUser.getUserCode() && !"".equals(currentLoginUser.getUserCode())
                && currentLoginUser.getUserCode().trim().equals(BaseStaticParameter.IMPLEMENT_PERSONEL)){
            return true;
        }
        return false;
    }

    /**
     * @description: 是否是公司业务人员 true - 是 false - 不是
     * @author: wuxx
     * @Date: 2020/10/26 15:00
     **/
    public static boolean getIsBusinessUser() {
        CurrentLoginUser currentLoginUser = getCurrentLoginUser();
        if (null!=currentLoginUser && null!=currentLoginUser.getUserCode() && !"".equals(currentLoginUser.getUserCode())
                && currentLoginUser.getUserCode().trim().equals(BaseStaticParameter.BUSINESS_PERSONEL)){
            return true;
        }
        return false;
    }

    /**
     * @description: 是否是公司梳理人员 true - 是 false - 不是
     * @author: wuxx
     * @Date: 2020/10/30 15:00
     **/
    public static boolean getIsCardingUser() {
        CurrentLoginUser currentLoginUser = getCurrentLoginUser();
        if (null!=currentLoginUser && null!=currentLoginUser.getUserCode() && !"".equals(currentLoginUser.getUserCode())
                && currentLoginUser.getUserCode().trim().equals(BaseStaticParameter.CARDING_PERSONEL)){
            return true;
        }
        return false;
    }

    /**
     * @description: 获取当前登录人得数据权限（1全部 2本人 3本部门 4本区划） 默认权限
     * @author: wuxx
     * @Date: 2021/1/6 9:30
     **/
    public static Integer getLoginUserDataAuthority() {
        CurrentLoginUser currentLoginUser = getCurrentLoginUser();
        if (null!=currentLoginUser){
            Integer dataAuthority = null==currentLoginUser.getDataAuthority()?1:currentLoginUser.getDataAuthority();
            return dataAuthority;
        }
        return null;
    }

}
