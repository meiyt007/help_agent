package com.zfsoft.microservice.platform.data.sys;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @description:  组织机构信息表
 * @author: wuxx
 * @Date: 2020/9/7 9:58
 **/
@Data
@ToString
public class SysOrgan {

    /**
     * 新增，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新，验证规则组
     */
    public interface UPDATE_GROUP{};

    /* 主键 */
    @NotNull(message = "id不能为空",groups = {UPDATE_GROUP.class})
    private Long id;

    /**
     * 业务主键
     */
    @NotNull(message = "业务主键不能为空",groups = { UPDATE_GROUP.class})
    private String organOid;

    /* 所属区划OID */
    @NotNull(message = "所属区划不能为空",groups = {INSERT_GROUP.class})
    private String districtOid;

    /* 所属区划名称 */
    private String districtName;

    /* 代码 */
//    @NotNull(message = "组织机构代码不能为空",groups = {INSERT_GROUP.class})
//    @Length(min = 1,max = 25,message = "组织机构代码长度为1-25",groups = {INSERT_GROUP.class})
    private String code;

    /* 名称 */
    @NotNull(message = "组织机构名称不能为空",groups = {INSERT_GROUP.class})
    @Length(min = 1,max = 100,message = "组织机构名称长度为1-100",groups = {INSERT_GROUP.class})
    private String name;

    /**
     * 部门编码
     */
    private String deptCode;

    /* 全称 */
    @NotNull(message = "组织机构全称不能为空",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    @Length(min = 1,max = 50,message = "组织机构全称长度为1-50",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    private String fullName;

    /* 简码 */
//    @NotNull(message = "组织机构简码不能为空",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
//    @Length(min = 1,max = 25,message = "组织机构简码长度为1-25",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    private String simpleCode;

    /* 地址 */
//    @NotNull(message = "组织机构地址不能为空",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
//    @Length(min = 1,max = 200,message = "组织机构地址长度为1-200",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    private String address;

    /* 上级组织机构OID */
    private String parentOid;

    /* 上级组织机构名称 */
    private String parentName;

    /* 电话 */
//    @NotNull(message = "组织机构电话不能为空",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
//    @Length(min = 1,max = 13,message = "组织机构电话长度为1-13",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    private String telphone;

    /* 类型 */
    private String typeDictOid;
    /* 类型名称 */
    private String typeDictName;

    /* 路径 */
    private String path;

    /* 排序号 */
    @NotNull(message = "排序号不能为空",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    private Integer sort;

    /* 启禁用状态 */
    private Integer isAble;

    /* 删除状态 */
    private Integer isDelete;

    /* 社会信用代码 */
    @Length(min = 0,max = 18,message = "组织机构电话长度为18",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    private String uniteCode;

    /* 修改日期 */
    private Date modifyDate;

    /** 子部门 */
    private List<SysOrgan> children = new ArrayList<SysOrgan>();

    /* 屏蔽状态 0-未屏蔽 1-已屏蔽 改字段只是提供前端展示或者不展示的，所以无法使用启禁用字段逻辑*/
    private String shield;
}
