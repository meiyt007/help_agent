package com.zfsoft.platform.common.security.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;

/**
 * @Auther: lijun@sstcsoft.com
 * @Date: 2019-10-15 10:35
 */

@Data
@ToString(includeFieldNames=true)
@NoArgsConstructor
public class CurrentLoginUser {
    public static final String HEADER_KEY = "CUSTOM-REQUEST-HEADER";
    private String clientId;
    private String userName;
    /**
     * 登录oid
     **/
    private String loginOid;

    private String userOid;

    /**
     * 区划oid
     **/
    private String districtOid;
    /**
     * 区划名称
     **/
    private String districtName;
    /**
     * 组织机构oid
     **/
    private String organOid;
    /**
     * 组织机构名称
     **/
    private String organName;

    /**
     * 用户类型对应数据字典code
     **/
    private String userCode;

    private Collection<String> authorities;

    /**
     * 后缓咨询状态
     */
    private Integer adviStatus;

    /**
     * 数据权限（1全部 2本人 3本部门 4本区划）
     */
    private Integer dataAuthority;
}
