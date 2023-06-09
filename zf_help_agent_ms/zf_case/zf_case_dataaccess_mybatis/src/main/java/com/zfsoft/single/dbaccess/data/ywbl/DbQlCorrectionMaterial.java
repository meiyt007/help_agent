package com.zfsoft.single.dbaccess.data.ywbl;

import java.util.Date;

public class DbQlCorrectionMaterial {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ql_correction_material.ID
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ql_correction_material.CORRECTION_MATERIAL_OID
     *
     * @mbggenerated
     */
    private String correctionMaterialOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ql_correction_material.CORRECTION_OID
     *
     * @mbggenerated
     */
    private String correctionOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ql_correction_material.CASE_MATERIAL_OID
     *
     * @mbggenerated
     */
    private String caseMaterialOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ql_correction_material.CREATE_DATE
     *
     * @mbggenerated
     */
    private Date createDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ql_correction_material.MODIFY_DATE
     *
     * @mbggenerated
     */
    private Date modifyDate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ql_correction_material.ID
     *
     * @return the value of t_ql_correction_material.ID
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ql_correction_material.ID
     *
     * @param id the value for t_ql_correction_material.ID
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ql_correction_material.CORRECTION_MATERIAL_OID
     *
     * @return the value of t_ql_correction_material.CORRECTION_MATERIAL_OID
     *
     * @mbggenerated
     */
    public String getCorrectionMaterialOid() {
        return correctionMaterialOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ql_correction_material.CORRECTION_MATERIAL_OID
     *
     * @param correctionMaterialOid the value for t_ql_correction_material.CORRECTION_MATERIAL_OID
     *
     * @mbggenerated
     */
    public void setCorrectionMaterialOid(String correctionMaterialOid) {
        this.correctionMaterialOid = correctionMaterialOid == null ? null : correctionMaterialOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ql_correction_material.CORRECTION_OID
     *
     * @return the value of t_ql_correction_material.CORRECTION_OID
     *
     * @mbggenerated
     */
    public String getCorrectionOid() {
        return correctionOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ql_correction_material.CORRECTION_OID
     *
     * @param correctionOid the value for t_ql_correction_material.CORRECTION_OID
     *
     * @mbggenerated
     */
    public void setCorrectionOid(String correctionOid) {
        this.correctionOid = correctionOid == null ? null : correctionOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ql_correction_material.CASE_MATERIAL_OID
     *
     * @return the value of t_ql_correction_material.CASE_MATERIAL_OID
     *
     * @mbggenerated
     */
    public String getCaseMaterialOid() {
        return caseMaterialOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ql_correction_material.CASE_MATERIAL_OID
     *
     * @param caseMaterialOid the value for t_ql_correction_material.CASE_MATERIAL_OID
     *
     * @mbggenerated
     */
    public void setCaseMaterialOid(String caseMaterialOid) {
        this.caseMaterialOid = caseMaterialOid == null ? null : caseMaterialOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ql_correction_material.CREATE_DATE
     *
     * @return the value of t_ql_correction_material.CREATE_DATE
     *
     * @mbggenerated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ql_correction_material.CREATE_DATE
     *
     * @param createDate the value for t_ql_correction_material.CREATE_DATE
     *
     * @mbggenerated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ql_correction_material.MODIFY_DATE
     *
     * @return the value of t_ql_correction_material.MODIFY_DATE
     *
     * @mbggenerated
     */
    public Date getModifyDate() {
        return modifyDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ql_correction_material.MODIFY_DATE
     *
     * @param modifyDate the value for t_ql_correction_material.MODIFY_DATE
     *
     * @mbggenerated
     */
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
}