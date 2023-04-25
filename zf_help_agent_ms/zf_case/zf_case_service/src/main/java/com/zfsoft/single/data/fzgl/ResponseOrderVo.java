package com.zfsoft.single.data.fzgl;

/**
 * 运单接口响应节点信息
 *
 * @author fangga
 * @date 2017年11月29日
 */
public class ResponseOrderVo {
	private static final long serialVersionUID = 1L;

	/**
	 * 物流订单号
	 */
	private String txLogisticID;
	/**
	 * 物流运单号
	 */
	private String mailNo;
	/**
	 * 此运单号是否成功
	 */
	private String success;
	/**
	 * 错误信息
	 */
	private String errorMsg;
	/**
	 * 错误代码
	 */
	private String errorCode;

	public String getTxLogisticID() {
		return txLogisticID;
	}

	public void setTxLogisticID(String txLogisticID) {
		this.txLogisticID = txLogisticID;
	}

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
