package com.zfsoft.microservice.platform.data.sys;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 权限
 * @author zxx
 * @date 2020/9/10 10:18 上午
 */
@Data
@ToString
public class SysPermission {
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
    private String permissionOid;

    /**
     * 应用主键
     */
    private String appOid;

    /**
     * 模块名称
     */
    @NotNull(message = "模块名称不能为空",groups = {INSERT_GROUP.class})
    @Length(min = 1,max = 30,message = "模块名称长度为1-10",groups = {INSERT_GROUP.class})
    private String name;

    /**
     * 是否外链
     */
    @NotNull(message = "是否外链不能为空",groups = {INSERT_GROUP.class})
    private Integer isFrame;

    /**
     * 路由
     */
    @Length(min = 0,max = 100,message = "路由长度为1-100",groups = {INSERT_GROUP.class, UPDATE_GROUP.class})
    private String router;

    /**
     * 模块链接
     */
    @Length(min = 0,max = 250,message = "模块链接长度为1-250",groups = {INSERT_GROUP.class, UPDATE_GROUP.class})
    private String hyperLink;

    /**
     * 外部链接
     */
    private String outLink;

    /**
     * 权限字符串
     */
    private String str;

    /**
     * 父级主键
     */
    private String parentOid;

    /**
     * 模块说明
     */
    private String memo;

    /**
     * 启禁用
     */
    private Integer isAble;

    /**
     * 删除状态
     */
    private Integer isDelete;

    /**
     * 排序号
     */
    @NotNull(message = "排序号不能为空",groups = {INSERT_GROUP.class})
    private Integer sort;

    /**
     * 模块类型 : 菜单或按钮
     */
    @NotNull(message = "模块类型不能为空",groups = {INSERT_GROUP.class})
    private String functionType;

    /**
     * 图标名称
     */
    private String iconName;

    /**
     * 编码
     */
    private String code;

    /**
     * 权限等级
     */
    private Integer perLevel;

    /**
     * 修改时间
     */
    private Date modifyDate;

    /**
     * 权限链接集合
     */
    private List<SysPermissionLink> childrenLink;

    /**
     * 子类
     */
    private List<SysPermission> children;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 父级名称
     */
    private String parentName;

    /**
     * 是否是个路由地址
     */
    private Boolean isRouter;

}
