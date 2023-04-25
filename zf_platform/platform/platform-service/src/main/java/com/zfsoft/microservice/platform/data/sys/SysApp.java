package com.zfsoft.microservice.platform.data.sys;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @description: 应用信息表
 * @author: wuxx
 * @Date: 2020/9/10 10:35
 **/
@Data
@ToString
public class SysApp {

    /**
     * 新增区划，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新区划，验证规则组
     */
    public interface UPDATE_GROUP{};

    /* 主键 */
    @NotNull(message = "id不能为空",groups = {UPDATE_GROUP.class})
    private Long id;

    /* 应用业务主键 */
    @NotNull(message = "应用业务主键不能为空",groups = {UPDATE_GROUP.class})
    private String appOid;

    /* 应用名称 */
    @NotNull(message = "应用名称不能为空",groups = {INSERT_GROUP.class})
    @Length(min = 1,max = 20,message = "应用名称长度为1-10",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    private String name;

    /* 应用显示名称 */
    @NotNull(message = "应用显示名称不能为空",groups = {INSERT_GROUP.class})
    @Length(min = 1,max = 10,message = "应用显示名称长度为1-10",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    private String displayName;

    /* 应用访问根地址 */
    @NotNull(message = "应用访问根地址不能为空",groups = {INSERT_GROUP.class})
    @Length(min = 1,max = 100,message = "应用访问根地址长度为1-100",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    private String accessRootAddr;

    /* 应用登录后首页地址 */
    @NotNull(message = "应用登录后首页地址不能为空",groups = {INSERT_GROUP.class})
    @Length(min = 1,max = 100,message = "应用登录后首页地址长度为1-100",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    private String loginSuccessAddr;

    /* 应用远程注册地址 */
    @NotNull(message = "应用远程注册地址不能为空",groups = {INSERT_GROUP.class})
    @Length(min = 1,max = 100,message = "应用远程注册地址长度为1-100",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    private String remoteRegistryAddr;

    /* 创建时间 */
    private Date createDate;

    /* 描述 */
    private String memo;

    /* 是否其他应用 */
    private Integer isOther;

    /* 图标地址 */
    //@NotNull(message = "图标地址不能为空",groups = {INSERT_GROUP.class})
    private String iconName;

    /* 上级应用编号 */
    private String parentOid;

    /* 上级应用编号 */
    private String parentName;
    /* 排序号 */
    @NotNull(message = "排序号不能为空",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    private Integer sort;

    /* 启禁用状态 */
    private Integer isAble;

    /* 删除状态 */
    private Integer isDelete;

    /* 修改时间 */
    private Date modifyDate;

}
