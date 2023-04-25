package com.zfsoft.microservice.platform.data.vo;

import lombok.Data;

import java.util.List;

/**
 * @description:  登录管理列表的vo
 * @author: wuxx
 * @Date: 2020/9/23 13:16
 **/
@Data
public class SysLoginVo {

    private Long id;
    /**
     * 业务主键
     */
    private String loginOid;
    private String userOid;
    private String account;
    private String oldPassword;
    private String password;
    private String confirmPassword;
    private Integer lockStatus;
    private Integer isAble;

    /* 所属用户 */
    private String userName;
    /**
     * 区划oid
     */
    private String districtOid;

    /**
     * 区划名称
     */
    private String districtName;
    /**
     * 组织机构oid
     */
    private String organOid;

    /**
     * 机构名称
     */
    private String organName;

    /**
     * 登录角色
     */
    private List<SysLoginRoleVo> sysLoginRoleList;

    /**
     * 旧密码
     */
    private String previous;
}
