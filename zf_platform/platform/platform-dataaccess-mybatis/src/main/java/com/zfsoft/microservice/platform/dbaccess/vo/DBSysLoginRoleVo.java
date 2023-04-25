package com.zfsoft.microservice.platform.dbaccess.vo;

/**
 * 用户角色VO
 * @author wuxx
 * @date 2020/9/23 10:42 上午
 */
public class DBSysLoginRoleVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 登录主键
     */
    private String loginOid;

    /**
     * 角色主键
     */
    private String roleOid;

    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 应用主键
     */
    private String appOid;

    /**
     * 应用名称
     */
    private String appName;


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginOid() {
        return loginOid;
    }

    public void setLoginOid(String loginOid) {
        this.loginOid = loginOid;
    }

    public String getRoleOid() {
        return roleOid;
    }

    public void setRoleOid(String roleOid) {
        this.roleOid = roleOid;
    }

    public String getAppOid() {
        return appOid;
    }

    public void setAppOid(String appOid) {
        this.appOid = appOid;
    }
}
