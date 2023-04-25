package com.zfsoft.superwindow.data.pbpj;



/**
 * @平板语音管理
 * 
 * @author : zhuly
 * @date : 2017-06-20
 */
public class PbpjVoiceManage {
	/**主键*/
	private String oid;
	/**内容*/
	private String sysContent;
	/**数量*/
	private String sysNum;
	/**标示*/
	private String sysRemark;
	/**状态*/
	private String status;
	
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getSysContent() {
		return sysContent;
	}
	public void setSysContent(String sysContent) {
		this.sysContent = sysContent;
	}
	public String getSysNum() {
		return sysNum;
	}
	public void setSysNum(String sysNum) {
		this.sysNum = sysNum;
	}
	public String getSysRemark() {
		return sysRemark;
	}
	public void setSysRemark(String sysRemark) {
		this.sysRemark = sysRemark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	
}
