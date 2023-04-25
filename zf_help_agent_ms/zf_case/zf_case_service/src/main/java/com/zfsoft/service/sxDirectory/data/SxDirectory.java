package com.zfsoft.service.sxDirectory.data;


import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @description:  目录清单信息表
 * @author: wangxl
 * @Date: 2020/10/25
 **/
@Data
@ToString
public class SxDirectory {

    /**
     * 新增，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新，验证规则组
     */
    public interface UPDATE_GROUP{};

    /* 主键  */
   // @NotNull(message = "id不能为空",groups = {UPDATE_GROUP.class})
    private Long id;

    /* 业务主健  */
   // @NotNull(message = "配置项代码不能为空",groups = {INSERT_GROUP.class})
   // @Length(min = 1,max = 25,message = "配置项代码长度为1-25",groups = {INSERT_GROUP.class})
	private String directoryOid;

    /* 目录清单名称  */
	private String directoryName;

    /* 基本编码  */
	private String basicCode;

    /* 目录父类OID  */
	private String directoryParentOid;

    /* 事项类型oid  */
	private String serviceTypeOid;

    /* 事项状态  */
	private Short directoryStatus;

    /* 是否主项  */
	private Short mainItemFlag;
	/* 数据来源*/
	private Short infoSource;
	/* 是否存在子项*/
	private Short existChildItem;
	/**
	 * 有效开始时间
	 */
	private Date plannedcanceldatestart;

	/**
	 * 有效结束时间
	 */
	private Date plannedcanceldateend;

	/**
	 * 附件oid
	 */
	private String attaOids;
	/**
	 * 行使层级字典值
	 */
	private String levelDicts;

	/**
	 * 行使层级名称
	 */
	private String levelName;
	/**
	 * 操作类型
	 */
	private Short createType;
	/**
	 * 顶级区划机构
	 */
	private String organOid;
	/**
	 * 版本号
	 */
	private Integer versionNumber;
	/**
	 * 是否纳入国家目录
	 */
	private Short ifIntoNation;
	/**
	 * 是否国家目录
	 */
	private Short ifNation;
	/**
	 * 创建人
	 */
	private String createUser;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 是否删除
	 */
	private Short delFlag;
	/**
	 * 设立根据
	 */
	private String setAccord;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDirectoryOid() {
		return directoryOid;
	}

	public void setDirectoryOid(String directoryOid) {
		this.directoryOid = directoryOid;
	}

	public String getDirectoryName() {
		return directoryName;
	}

	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}

	public String getBasicCode() {
		return basicCode;
	}

	public void setBasicCode(String basicCode) {
		this.basicCode = basicCode;
	}

	public String getDirectoryParentOid() {
		return directoryParentOid;
	}

	public void setDirectoryParentOid(String directoryParentOid) {
		this.directoryParentOid = directoryParentOid;
	}

	public String getServiceTypeOid() {
		return serviceTypeOid;
	}

	public void setServiceTypeOid(String serviceTypeOid) {
		this.serviceTypeOid = serviceTypeOid;
	}

	public Short getDirectoryStatus() {
		return directoryStatus;
	}

	public void setDirectoryStatus(Short directoryStatus) {
		this.directoryStatus = directoryStatus;
	}

	public Short getMainItemFlag() {
		return mainItemFlag;
	}

	public void setMainItemFlag(Short mainItemFlag) {
		this.mainItemFlag = mainItemFlag;
	}

	public Short getInfoSource() {
		return infoSource;
	}

	public void setInfoSource(Short infoSource) {
		this.infoSource = infoSource;
	}

	public Short getExistChildItem() {
		return existChildItem;
	}

	public void setExistChildItem(Short existChildItem) {
		this.existChildItem = existChildItem;
	}

	public Date getPlannedcanceldatestart() {
		return plannedcanceldatestart;
	}

	public void setPlannedcanceldatestart(Date plannedcanceldatestart) {
		this.plannedcanceldatestart = plannedcanceldatestart;
	}

	public Date getPlannedcanceldateend() {
		return plannedcanceldateend;
	}

	public void setPlannedcanceldateend(Date plannedcanceldateend) {
		this.plannedcanceldateend = plannedcanceldateend;
	}

	public String getAttaOids() {
		return attaOids;
	}

	public void setAttaOids(String attaOids) {
		this.attaOids = attaOids;
	}

	public String getLevelDicts() {
		return levelDicts;
	}

	public void setLevelDicts(String levelDicts) {
		this.levelDicts = levelDicts;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public Short getCreateType() {
		return createType;
	}

	public void setCreateType(Short createType) {
		this.createType = createType;
	}

	public String getOrganOid() {
		return organOid;
	}

	public void setOrganOid(String organOid) {
		this.organOid = organOid;
	}

	public Integer getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	public Short getIfIntoNation() {
		return ifIntoNation;
	}

	public void setIfIntoNation(Short ifIntoNation) {
		this.ifIntoNation = ifIntoNation;
	}

	public Short getIfNation() {
		return ifNation;
	}

	public void setIfNation(Short ifNation) {
		this.ifNation = ifNation;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Short getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Short delFlag) {
		this.delFlag = delFlag;
	}

	public String getSetAccord() {
		return setAccord;
	}

	public void setSetAccord(String setAccord) {
		this.setAccord = setAccord;
	}
}
