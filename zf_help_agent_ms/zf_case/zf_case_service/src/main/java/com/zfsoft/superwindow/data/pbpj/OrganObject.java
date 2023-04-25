package com.zfsoft.superwindow.data.pbpj;

/**
 *平板评价接口提供机构类
 * @author zly
 */
public class OrganObject {
	/**机构主键*/
	private String oid;
	/**机构名称*/
	private String orgname;

	/**
	 * 获取orgname  
	 * @return orgname orgname  
	 */
	public String getOrgname() {
		return orgname;
	}

	/**
	 * 设置orgname
	 * @param orgname orgname  
	 */
	public void setOrgname(String orgname) {
		this.orgname = orgname;
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
	
}
