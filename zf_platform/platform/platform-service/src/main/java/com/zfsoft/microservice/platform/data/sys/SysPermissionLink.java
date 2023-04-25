package com.zfsoft.microservice.platform.data.sys;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 权限链接
 * @author zxx
 * @date 2020/9/10 10:18 上午
 */
@Data
@ToString
public class SysPermissionLink {
    /**
     * 新增验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新验证规则组
     */
    public interface UPDATE_GROUP{};

    /**
     * 主键
     */
    private Long id;

    /**
     * 业务主键
     */
    @NotNull(message = "业务主键不能为空",groups = { UPDATE_GROUP.class})
    private String permissionLinkOid;

    /**
     * 权限关联
     */
    private String permissionOid;

    /**
     * 权限链接
     */
    @Length(min = 0,max = 250,message = "权限链接长度为1-250",groups = {INSERT_GROUP.class, UPDATE_GROUP.class})
    private String perLink;

    /**
     * 说明
     */
    @Length(min = 0,max = 500,message = "说明长度为1-500",groups = {INSERT_GROUP.class, UPDATE_GROUP.class})
    private String memo;

    /**
     * 修改时间
     */
    private Date modifyDate;

}
