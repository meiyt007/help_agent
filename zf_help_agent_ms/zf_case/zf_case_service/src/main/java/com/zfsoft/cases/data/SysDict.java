package com.zfsoft.cases.data;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 数据字典表(SysDict)实体类
 *
 * @author wangwg
 * @date  2020-10-22
 */
@Data
@ToString
public class SysDict{
    /**
     * 新增，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新，验证规则组
     */
    public interface UPDATE_GROUP{};
    /**
     * 主键
     */
    @NotNull(message = "id不能为空",groups = {INSERT_GROUP.class})
    private Long id;
    /**
     * 代码
     */
    @NotNull(message = "代码不能为空",groups = {INSERT_GROUP.class})
    private String code;
    /**
     * 名称
     */
    @NotNull(message = "名称不能为空",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    private String name;
    /**
     * 上级字典主键
     */
    @NotNull(message = "上级字典主键不能为空",groups = {INSERT_GROUP.class})
    private String parentOid;
    /**
     * 说明
     */
    private String memo;
    /**
     * 字典主键路径
     */
    private String path;
    /**
     * 启禁用状态（否0、是1）
     */
    @NotNull(message = "启禁用状态不能为空",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    private Integer isAble;
    /**
     * 删除状态（否0、是1）
     */
    @NotNull(message = "删除状态不能为空",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    private Integer isDelete;
    /**
     * 创建时间
     */
    @NotNull(message = "创建时间不能为空",groups = {INSERT_GROUP.class})
    private Date createDate;
    /**
     * 排序号
     */
    @NotNull(message = "排序号不能为空",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    private Integer sort;
    /**
     * 创建用户
     */
    @NotNull(message = "创建用户不能为空",groups = {INSERT_GROUP.class})
    private String createUserOid;
    /**
     * 字典拼音
     */
    private String phonetic;
    /**
     * 简称
     */
    private String abbreviation;
    /**
     * 国家标准编码
     */
    @NotNull(message = "国家标准编码不能为空",groups = {INSERT_GROUP.class,UPDATE_GROUP.class})
    private String nationCode;

}