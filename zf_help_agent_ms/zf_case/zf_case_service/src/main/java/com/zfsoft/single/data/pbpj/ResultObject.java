package com.zfsoft.single.data.pbpj;

/**
 *平板评价接口提供结果类
 * @author zly
 */
public class ResultObject {

	/**
	 * 叫号总人数
	 */
	private Integer total;
	/**
	 * 结果代码
	 */
	private String resultCode;
	/**
	 * 结果描述
	 */
	private Object resuleMessage;
	/**
	 * 获取结果代码  
	 * @return resultCode 结果代码  
	 */
	public String getResultCode() {
		return resultCode;
	}
	/**
	 * 设置结果代码
	 * @param resultCode 结果代码  
	 */
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	/**
	 * 获取结果描述  
	 * @return resuleMessage 结果描述  
	 */
	public Object getResuleMessage() {
		return resuleMessage;
	}
	/**
	 * 设置结果描述
	 * @param resuleMessage 结果描述  
	 */
	public void setResuleMessage(Object resuleMessage) {
		this.resuleMessage = resuleMessage;
	}

	public Integer getTotal() {
		return total;
	}

	public ResultObject setTotal(Integer total) {
		this.total = total;
		return this;
	}
}
