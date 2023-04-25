package com.zfsoft.superwindow.data.pbpj;


/**
 *平板评价接口提供用户类
 * @author zly
 */
public class UserObject {
	/**主键*/
	private String oid;
	/**账号*/
	private String code;
	/**用户名称*/
	private String name;
	/**用户密码*/
	private String pass;
	/**职位*/
	private String position;
	/**
	 * 是否启用评价器
	 * 0-不启用 1-启用
	 */
	private String pjqFlag;
	/**
	 * 人员的历史评价得分
	 */
	private String historyPj;
	/**
	 * 服务星级  默认星级为3   反之为计算星级
	 */
	private String xjFlag;
	/**
	 * 人员照片
	 */
	private String userImg;
	
	private String mobile;
	
	private OrganObject organ;
	/**
	 * 区划ID
	 */
	private String areaId;
	/**
	 * 区划name
	 */
	private String areaName;

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	/**
	 * 获取oid  
	 * @return oid oid  
	 */
	public String getOid() {
		return oid;
	}

	/**
	 * 设置oid
	 * @param oid oid  
	 */
	public void setOid(String oid) {
		this.oid = oid;
	}

	/**
	 * 获取code  
	 * @return code code  
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置code
	 * @param code code  
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取name  
	 * @return name name  
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置name
	 * @param name name  
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取pass  
	 * @return pass pass  
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * 设置pass
	 * @param pass pass  
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}

	/**
	 * 获取是否启用评价器0-不启用1-启用  
	 * @return pjqFlag 是否启用评价器0-不启用1-启用  
	 */
	public String getPjqFlag() {
		return pjqFlag;
	}

	/**
	 * 设置是否启用评价器0-不启用1-启用
	 * @param pjqFlag 是否启用评价器0-不启用1-启用  
	 */
	public void setPjqFlag(String pjqFlag) {
		this.pjqFlag = pjqFlag;
	}

	/**
	 * 获取人员的历史评价得分  
	 * @return historyPj 人员的历史评价得分  
	 */
	public String getHistoryPj() {
		return historyPj;
	}

	/**
	 * 设置人员的历史评价得分
	 * @param historyPj 人员的历史评价得分  
	 */
	public void setHistoryPj(String historyPj) {
		this.historyPj = historyPj;
	}

	/**
	 * 获取服务星级默认星级为3反之为计算星级  
	 * @return xjFlag 服务星级默认星级为3反之为计算星级  
	 */
	public String getXjFlag() {
		return xjFlag;
	}

	/**
	 * 设置服务星级默认星级为3反之为计算星级
	 * @param xjFlag 服务星级默认星级为3反之为计算星级  
	 */
	public void setXjFlag(String xjFlag) {
		this.xjFlag = xjFlag;
	}

	/**
	 * 获取人员照片  
	 * @return userImg 人员照片  
	 */
	public String getUserImg() {
		return userImg;
	}

	/**
	 * 设置人员照片
	 * @param userImg 人员照片  
	 */
	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	/**
	 * 获取organ  
	 * @return organ organ  
	 */
	public OrganObject getOrgan() {
		return organ;
	}

	/**
	 * 设置organ
	 * @param organ organ  
	 */
	public void setOrgan(OrganObject organ) {
		this.organ = organ;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
}
