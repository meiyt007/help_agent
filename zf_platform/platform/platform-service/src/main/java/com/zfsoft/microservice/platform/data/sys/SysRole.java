package com.zfsoft.microservice.platform.data.sys;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 角色
 * @author zxx
 * @date 2020/9/12 9:48 上午
 */
@Data
@ToString
public class SysRole {

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
    private String roleOid;
    /**
     * 所属应用
     */
    @NotNull(message = "所属应用不能为空",groups = {INSERT_GROUP.class})
    private String appOid;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空",groups = {INSERT_GROUP.class})
    @Length(min = 1,max = 200,message = "角色名称长度为1-200",groups = {INSERT_GROUP.class})
    private String name;

    /**
     * 启禁用状态
     */
    private Integer isAble;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 备注
     */
    private String note;

    /**
     * 删除状态
     */
    private Integer isDelete;

    /**
     * 修改时间
     */
    private Date modifyDate;

    /**
     * 权限列表
     */
    private List<SysRolePermission> sysRolePermissionList;

    /**
     * 登录角色
     */
    private List<SysLoginRole> sysLoginRoleList;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 权限主键
     */
    private String[] permissionIds;

    /**
     * 用户主键
     */
    private List<String> userOids;

}
