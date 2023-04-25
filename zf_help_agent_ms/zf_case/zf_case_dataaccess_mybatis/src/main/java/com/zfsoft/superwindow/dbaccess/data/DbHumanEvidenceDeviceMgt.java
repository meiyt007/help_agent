package com.zfsoft.superwindow.dbaccess.data;

import java.util.Date;

public class DbHumanEvidenceDeviceMgt {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_human_evidence_device_mgt.ID
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_human_evidence_device_mgt.HUMAN_EVIDENCE_DEVICE_MGT_OID
     *
     * @mbggenerated
     */
    private String humanEvidenceDeviceMgtOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_human_evidence_device_mgt.SALT
     *
     * @mbggenerated
     */
    private String salt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_human_evidence_device_mgt.IP_ADDRESS
     *
     * @mbggenerated
     */
    private String ipAddress;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_human_evidence_device_mgt.MAC_ADDRESS
     *
     * @mbggenerated
     */
    private String macAddress;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_human_evidence_device_mgt.DEVICE_ID
     *
     * @mbggenerated
     */
    private String deviceId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_human_evidence_device_mgt.BINDING_WINDOW
     *
     * @mbggenerated
     */
    private String bindingWindow;

    private String bindingWindowNum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_human_evidence_device_mgt.CREATE_TIME
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_human_evidence_device_mgt.IS_DELETE
     *
     * @mbggenerated
     */
    private Integer isDelete;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_human_evidence_device_mgt.MODIFY_DATE
     *
     * @mbggenerated
     */
    private Date modifyDate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_human_evidence_device_mgt.ID
     *
     * @return the value of t_human_evidence_device_mgt.ID
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_human_evidence_device_mgt.ID
     *
     * @param id the value for t_human_evidence_device_mgt.ID
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_human_evidence_device_mgt.HUMAN_EVIDENCE_DEVICE_MGT_OID
     *
     * @return the value of t_human_evidence_device_mgt.HUMAN_EVIDENCE_DEVICE_MGT_OID
     *
     * @mbggenerated
     */
    public String getHumanEvidenceDeviceMgtOid() {
        return humanEvidenceDeviceMgtOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_human_evidence_device_mgt.HUMAN_EVIDENCE_DEVICE_MGT_OID
     *
     * @param humanEvidenceDeviceMgtOid the value for t_human_evidence_device_mgt.HUMAN_EVIDENCE_DEVICE_MGT_OID
     *
     * @mbggenerated
     */
    public void setHumanEvidenceDeviceMgtOid(String humanEvidenceDeviceMgtOid) {
        this.humanEvidenceDeviceMgtOid = humanEvidenceDeviceMgtOid == null ? null : humanEvidenceDeviceMgtOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_human_evidence_device_mgt.SALT
     *
     * @return the value of t_human_evidence_device_mgt.SALT
     *
     * @mbggenerated
     */
    public String getSalt() {
        return salt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_human_evidence_device_mgt.SALT
     *
     * @param salt the value for t_human_evidence_device_mgt.SALT
     *
     * @mbggenerated
     */
    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_human_evidence_device_mgt.IP_ADDRESS
     *
     * @return the value of t_human_evidence_device_mgt.IP_ADDRESS
     *
     * @mbggenerated
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_human_evidence_device_mgt.IP_ADDRESS
     *
     * @param ipAddress the value for t_human_evidence_device_mgt.IP_ADDRESS
     *
     * @mbggenerated
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress == null ? null : ipAddress.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_human_evidence_device_mgt.MAC_ADDRESS
     *
     * @return the value of t_human_evidence_device_mgt.MAC_ADDRESS
     *
     * @mbggenerated
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_human_evidence_device_mgt.MAC_ADDRESS
     *
     * @param macAddress the value for t_human_evidence_device_mgt.MAC_ADDRESS
     *
     * @mbggenerated
     */
    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress == null ? null : macAddress.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_human_evidence_device_mgt.DEVICE_ID
     *
     * @return the value of t_human_evidence_device_mgt.DEVICE_ID
     *
     * @mbggenerated
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_human_evidence_device_mgt.DEVICE_ID
     *
     * @param deviceId the value for t_human_evidence_device_mgt.DEVICE_ID
     *
     * @mbggenerated
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_human_evidence_device_mgt.BINDING_WINDOW
     *
     * @return the value of t_human_evidence_device_mgt.BINDING_WINDOW
     *
     * @mbggenerated
     */
    public String getBindingWindow() {
        return bindingWindow;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_human_evidence_device_mgt.BINDING_WINDOW
     *
     * @param bindingWindow the value for t_human_evidence_device_mgt.BINDING_WINDOW
     *
     * @mbggenerated
     */
    public void setBindingWindow(String bindingWindow) {
        this.bindingWindow = bindingWindow == null ? null : bindingWindow.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_human_evidence_device_mgt.CREATE_TIME
     *
     * @return the value of t_human_evidence_device_mgt.CREATE_TIME
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_human_evidence_device_mgt.CREATE_TIME
     *
     * @param createTime the value for t_human_evidence_device_mgt.CREATE_TIME
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_human_evidence_device_mgt.IS_DELETE
     *
     * @return the value of t_human_evidence_device_mgt.IS_DELETE
     *
     * @mbggenerated
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_human_evidence_device_mgt.IS_DELETE
     *
     * @param isDelete the value for t_human_evidence_device_mgt.IS_DELETE
     *
     * @mbggenerated
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_human_evidence_device_mgt.MODIFY_DATE
     *
     * @return the value of t_human_evidence_device_mgt.MODIFY_DATE
     *
     * @mbggenerated
     */
    public Date getModifyDate() {
        return modifyDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_human_evidence_device_mgt.MODIFY_DATE
     *
     * @param modifyDate the value for t_human_evidence_device_mgt.MODIFY_DATE
     *
     * @mbggenerated
     */
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getBindingWindowNum() {
        return bindingWindowNum;
    }

    public void setBindingWindowNum(String bindingWindowNum) {
        this.bindingWindowNum = bindingWindowNum;
    }
}