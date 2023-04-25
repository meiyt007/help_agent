package com.zfsoft.cases.dbaccess.data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据字典表(SysDict)实体类
 *
 * @author wangwg
 * @date  2020-10-22
 */
public class DbSysDict implements Serializable {
    private static final long serialVersionUID = -86397407472148442L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 代码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 上级字典主键
     */
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
    private Integer isAble;
    /**
     * 删除状态（否0、是1）
     */
    private Integer isDelete;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 排序号
     */
    private Integer sort;
    /**
     * 创建用户
     */
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
    private String nationCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentOid() {
        return parentOid;
    }

    public void setParentOid(String parentOid) {
        this.parentOid = parentOid;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getIsAble() {
        return isAble;
    }

    public void setIsAble(Integer isAble) {
        this.isAble = isAble;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getCreateUserOid() {
        return createUserOid;
    }

    public void setCreateUserOid(String createUserOid) {
        this.createUserOid = createUserOid;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getNationCode() {
        return nationCode;
    }

    public void setNationCode(String nationCode) {
        this.nationCode = nationCode;
    }

}