package com.zfsoft.microservice.platform.data.vo;


import lombok.Data;

/**
 * 用户角色VO
 * @author wuxx
 * @date 2020/9/23 10:42 上午
 */
@Data
public class SysLoginRoleVo {

    /**
     * 主键
     */
    private Long oid;

    /**
     * 登录主键
     */
    private Long loginOid;

    /**
     * 角色主键
     */
    private Long roleOid;

    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 应用主键
     */
    private Long appOid;

    /**
     * 应用名称
     */
    private String appName;

}
