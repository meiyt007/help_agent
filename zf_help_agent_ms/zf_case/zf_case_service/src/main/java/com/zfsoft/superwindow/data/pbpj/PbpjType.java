package com.zfsoft.superwindow.data.pbpj;

import java.util.List;
import java.util.Map;

/**
 * @评价器信息
 * 
 * @author : zly
 * @date : 2017-06-21
 */
public class PbpjType{
	
	/**评价值*/
	private String pjTypeId;
	/**评价名称*/
	private String pjTypeName;
	/**评价值*/
	private String pjNumValue;
	/**评价值*/
	private String pjTypeValue;
	/**附件id*/
	private String imgUrl;
	/**是否删除*/
	private String mydFlag;
	/**原因*/
	private List<Map<String,String>> reasonList;
	
	public String getPjTypeId() {
		return pjTypeId;
	}
	public void setPjTypeId(String pjTypeId) {
		this.pjTypeId = pjTypeId;
	}
	public String getPjTypeName() {
		return pjTypeName;
	}
	public void setPjTypeName(String pjTypeName) {
		this.pjTypeName = pjTypeName;
	}
	public String getPjNumValue() {
		return pjNumValue;
	}
	public void setPjNumValue(String pjNumValue) {
		this.pjNumValue = pjNumValue;
	}
	public String getPjTypeValue() {
		return pjTypeValue;
	}
	public void setPjTypeValue(String pjTypeValue) {
		this.pjTypeValue = pjTypeValue;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getMydFlag() {
		return mydFlag;
	}
	public void setMydFlag(String mydFlag) {
		this.mydFlag = mydFlag;
	}
	public List<Map<String, String>> getReasonList() {
		return reasonList;
	}
	public void setReasonList(List<Map<String, String>> reasonList) {
		this.reasonList = reasonList;
	}
	
	
}
