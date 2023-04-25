package com.zfsoft.single.dbaccess.data;

import java.util.Date;

public class DbSignatureFlowRecord {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_signature_flow_record.ID
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_signature_flow_record.OID
     *
     * @mbg.generated
     */
    private String oid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_signature_flow_record.CASE_OID
     *
     * @mbg.generated
     */
    private String caseOid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_signature_flow_record.MATERIAL_OID
     *
     * @mbg.generated
     */
    private String materialOid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_signature_flow_record.DOWNLOAD_URL
     *
     * @mbg.generated
     */
    private String downloadUrl;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_signature_flow_record.FILE_KEY
     *
     * @mbg.generated
     */
    private String fileKey;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_signature_flow_record.FLOW_ID
     *
     * @mbg.generated
     */
    private String flowId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_signature_flow_record.STATUS
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_signature_flow_record.DEL_FLAG
     *
     * @mbg.generated
     */
    private Integer delFlag;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_signature_flow_record.CREATE_DATE
     *
     * @mbg.generated
     */
    private Date createDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_signature_flow_record.MODIFY_DATE
     *
     * @mbg.generated
     */
    private Date modifyDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_signature_flow_record.ATTA_OIDS
     *
     * @mbg.generated
     */
    private String attaOids;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_signature_flow_record.ID
     *
     * @return the value of t_signature_flow_record.ID
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_signature_flow_record.ID
     *
     * @param id the value for t_signature_flow_record.ID
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_signature_flow_record.OID
     *
     * @return the value of t_signature_flow_record.OID
     *
     * @mbg.generated
     */
    public String getOid() {
        return oid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_signature_flow_record.OID
     *
     * @param oid the value for t_signature_flow_record.OID
     *
     * @mbg.generated
     */
    public void setOid(String oid) {
        this.oid = oid == null ? null : oid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_signature_flow_record.CASE_OID
     *
     * @return the value of t_signature_flow_record.CASE_OID
     *
     * @mbg.generated
     */
    public String getCaseOid() {
        return caseOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_signature_flow_record.CASE_OID
     *
     * @param caseOid the value for t_signature_flow_record.CASE_OID
     *
     * @mbg.generated
     */
    public void setCaseOid(String caseOid) {
        this.caseOid = caseOid == null ? null : caseOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_signature_flow_record.MATERIAL_OID
     *
     * @return the value of t_signature_flow_record.MATERIAL_OID
     *
     * @mbg.generated
     */
    public String getMaterialOid() {
        return materialOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_signature_flow_record.MATERIAL_OID
     *
     * @param materialOid the value for t_signature_flow_record.MATERIAL_OID
     *
     * @mbg.generated
     */
    public void setMaterialOid(String materialOid) {
        this.materialOid = materialOid == null ? null : materialOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_signature_flow_record.DOWNLOAD_URL
     *
     * @return the value of t_signature_flow_record.DOWNLOAD_URL
     *
     * @mbg.generated
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_signature_flow_record.DOWNLOAD_URL
     *
     * @param downloadUrl the value for t_signature_flow_record.DOWNLOAD_URL
     *
     * @mbg.generated
     */
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl == null ? null : downloadUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_signature_flow_record.FILE_KEY
     *
     * @return the value of t_signature_flow_record.FILE_KEY
     *
     * @mbg.generated
     */
    public String getFileKey() {
        return fileKey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_signature_flow_record.FILE_KEY
     *
     * @param fileKey the value for t_signature_flow_record.FILE_KEY
     *
     * @mbg.generated
     */
    public void setFileKey(String fileKey) {
        this.fileKey = fileKey == null ? null : fileKey.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_signature_flow_record.FLOW_ID
     *
     * @return the value of t_signature_flow_record.FLOW_ID
     *
     * @mbg.generated
     */
    public String getFlowId() {
        return flowId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_signature_flow_record.FLOW_ID
     *
     * @param flowId the value for t_signature_flow_record.FLOW_ID
     *
     * @mbg.generated
     */
    public void setFlowId(String flowId) {
        this.flowId = flowId == null ? null : flowId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_signature_flow_record.STATUS
     *
     * @return the value of t_signature_flow_record.STATUS
     *
     * @mbg.generated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_signature_flow_record.STATUS
     *
     * @param status the value for t_signature_flow_record.STATUS
     *
     * @mbg.generated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_signature_flow_record.DEL_FLAG
     *
     * @return the value of t_signature_flow_record.DEL_FLAG
     *
     * @mbg.generated
     */
    public Integer getDelFlag() {
        return delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_signature_flow_record.DEL_FLAG
     *
     * @param delFlag the value for t_signature_flow_record.DEL_FLAG
     *
     * @mbg.generated
     */
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_signature_flow_record.CREATE_DATE
     *
     * @return the value of t_signature_flow_record.CREATE_DATE
     *
     * @mbg.generated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_signature_flow_record.CREATE_DATE
     *
     * @param createDate the value for t_signature_flow_record.CREATE_DATE
     *
     * @mbg.generated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_signature_flow_record.MODIFY_DATE
     *
     * @return the value of t_signature_flow_record.MODIFY_DATE
     *
     * @mbg.generated
     */
    public Date getModifyDate() {
        return modifyDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_signature_flow_record.MODIFY_DATE
     *
     * @param modifyDate the value for t_signature_flow_record.MODIFY_DATE
     *
     * @mbg.generated
     */
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_signature_flow_record.ATTA_OIDS
     *
     * @return the value of t_signature_flow_record.ATTA_OIDS
     *
     * @mbg.generated
     */
    public String getAttaOids() {
        return attaOids;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_signature_flow_record.ATTA_OIDS
     *
     * @param attaOids the value for t_signature_flow_record.ATTA_OIDS
     *
     * @mbg.generated
     */
    public void setAttaOids(String attaOids) {
        this.attaOids = attaOids == null ? null : attaOids.trim();
    }
}