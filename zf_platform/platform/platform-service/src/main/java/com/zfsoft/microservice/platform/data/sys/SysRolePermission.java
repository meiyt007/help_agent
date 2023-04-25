package com.zfsoft.microservice.platform.data.sys;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 角色权限
 * @author zxx
 * @date 2020/9/12 10:26 上午
 */
@Data
@ToString
public class SysRolePermission {

    /**
     * 主键
     */
    private Long oid;

    /**
     * 角色关联
     */
    private String roleOid;

    /**
     * 权限关联
     */
    private String permissionOid;

    /**
     * 修改时间
     */
    private Date modifyDate;

}
