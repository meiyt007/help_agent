package com.zfsoft.microservice.platform.data.vo;

import lombok.Data;

import java.util.List;

/**
 * @description:  用户的vo
 * @author: wuxx
 * @Date: 2021/6/3 13:16
 **/
@Data
public class SysUserVo {

    /**
     * 业务主键
     */
    private String userOid;

    /* 所属用户 */
    private String name;

    /**
     * 区划oid
     */
    private String districtOid;

    /**
     * 组织机构oid
     */
    private String organOid;

}
