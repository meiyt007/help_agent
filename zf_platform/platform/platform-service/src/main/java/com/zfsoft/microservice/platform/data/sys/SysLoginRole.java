package com.zfsoft.microservice.platform.data.sys;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 用户角色
 * @author zxx
 * @date 2020/9/14 10:42 上午
 */
@Data
@ToString
public class SysLoginRole {

    /**
     * 新增验证组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新验证组
     */
    public interface UPDATE_GROUP{};

    /**
     * 主键
     */
    private Long id;

    /* 业务主键 */
    @NotNull(message = "业务主键不能为空",groups = { UPDATE_GROUP.class})
    private String loginRoleOid;

    /**
     * 登录主键
     */
    private String loginOid;

    /**
     * 角色主键
     */
    private String roleOid;

    /**
     * 修改时间
     */
    private Date modifyDate;

    /**
     * 应用主键
     */
    private String appOid;

}
