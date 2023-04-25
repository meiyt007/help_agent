package com.zfsoft.single.dbaccess.data.ywbl;

import java.util.Date;

public class DbCaseRqhb {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_case_rqhb.ID
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_case_rqhb.RQHB_OID
     *
     * @mbggenerated
     */
    private String rqhbOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_case_rqhb.CASE_OID
     *
     * @mbggenerated
     */
    private String caseOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_case_rqhb.CASE_MATERIAL_OID
     *
     * @mbggenerated
     */
    private String caseMaterialOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_case_rqhb.CREATE_DATE
     *
     * @mbggenerated
     */
    private Date createDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_case_rqhb.MODIFY_DATE
     *
     * @mbggenerated
     */
    private Date modifyDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_case_rqhb.CREATE_USER
     *
     * @mbggenerated
     */
    private String createUser;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_case_rqhb.ID
     *
     * @return the value of t_case_rqhb.ID
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_case_rqhb.ID
     *
     * @param id the value for t_case_rqhb.ID
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_case_rqhb.RQHB_OID
     *
     * @return the value of t_case_rqhb.RQHB_OID
     *
     * @mbggenerated
     */
    public String getRqhbOid() {
        return rqhbOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_case_rqhb.RQHB_OID
     *
     * @param rqhbOid the value for t_case_rqhb.RQHB_OID
     *
     * @mbggenerated
     */
    public void setRqhbOid(String rqhbOid) {
        this.rqhbOid = rqhbOid == null ? null : rqhbOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_case_rqhb.CASE_OID
     *
     * @return the value of t_case_rqhb.CASE_OID
     *
     * @mbggenerated
     */
    public String getCaseOid() {
        return caseOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_case_rqhb.CASE_OID
     *
     * @param caseOid the value for t_case_rqhb.CASE_OID
     *
     * @mbggenerated
     */
    public void setCaseOid(String caseOid) {
        this.caseOid = caseOid == null ? null : caseOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_case_rqhb.CASE_MATERIAL_OID
     *
     * @return the value of t_case_rqhb.CASE_MATERIAL_OID
     *
     * @mbggenerated
     */
    public String getCaseMaterialOid() {
        return caseMaterialOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_case_rqhb.CASE_MATERIAL_OID
     *
     * @param caseMaterialOid the value for t_case_rqhb.CASE_MATERIAL_OID
     *
     * @mbggenerated
     */
    public void setCaseMaterialOid(String caseMaterialOid) {
        this.caseMaterialOid = caseMaterialOid == null ? null : caseMaterialOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_case_rqhb.CREATE_DATE
     *
     * @return the value of t_case_rqhb.CREATE_DATE
     *
     * @mbggenerated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_case_rqhb.CREATE_DATE
     *
     * @param createDate the value for t_case_rqhb.CREATE_DATE
     *
     * @mbggenerated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_case_rqhb.MODIFY_DATE
     *
     * @return the value of t_case_rqhb.MODIFY_DATE
     *
     * @mbggenerated
     */
    public Date getModifyDate() {
        return modifyDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_case_rqhb.MODIFY_DATE
     *
     * @param modifyDate the value for t_case_rqhb.MODIFY_DATE
     *
     * @mbggenerated
     */
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_case_rqhb.CREATE_USER
     *
     * @return the value of t_case_rqhb.CREATE_USER
     *
     * @mbggenerated
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_case_rqhb.CREATE_USER
     *
     * @param createUser the value for t_case_rqhb.CREATE_USER
     *
     * @mbggenerated
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }
}