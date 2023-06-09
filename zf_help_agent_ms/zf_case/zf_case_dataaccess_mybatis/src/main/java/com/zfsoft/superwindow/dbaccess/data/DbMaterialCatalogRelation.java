package com.zfsoft.superwindow.dbaccess.data;

import java.util.Date;

public class DbMaterialCatalogRelation {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_material_catalog_relation.ID
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_material_catalog_relation.MATERIAL_CATALOG_RELATION_OID
     *
     * @mbggenerated
     */
    private String materialCatalogRelationOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_material_catalog_relation.MATERIAL_CATALOG_OID
     *
     * @mbggenerated
     */
    private String materialCatalogOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_material_catalog_relation.RELATION_OID
     *
     * @mbggenerated
     */
    private String relationOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_material_catalog_relation.CREATE_DATE
     *
     * @mbggenerated
     */
    private Date createDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_material_catalog_relation.MODIFY_DATE
     *
     * @mbggenerated
     */
    private Date modifyDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_material_catalog_relation.DEL_FLAG
     *
     * @mbggenerated
     */
    private Integer delFlag;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_material_catalog_relation.ID
     *
     * @return the value of t_material_catalog_relation.ID
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_material_catalog_relation.ID
     *
     * @param id the value for t_material_catalog_relation.ID
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_material_catalog_relation.MATERIAL_CATALOG_RELATION_OID
     *
     * @return the value of t_material_catalog_relation.MATERIAL_CATALOG_RELATION_OID
     *
     * @mbggenerated
     */
    public String getMaterialCatalogRelationOid() {
        return materialCatalogRelationOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_material_catalog_relation.MATERIAL_CATALOG_RELATION_OID
     *
     * @param materialCatalogRelationOid the value for t_material_catalog_relation.MATERIAL_CATALOG_RELATION_OID
     *
     * @mbggenerated
     */
    public void setMaterialCatalogRelationOid(String materialCatalogRelationOid) {
        this.materialCatalogRelationOid = materialCatalogRelationOid == null ? null : materialCatalogRelationOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_material_catalog_relation.MATERIAL_CATALOG_OID
     *
     * @return the value of t_material_catalog_relation.MATERIAL_CATALOG_OID
     *
     * @mbggenerated
     */
    public String getMaterialCatalogOid() {
        return materialCatalogOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_material_catalog_relation.MATERIAL_CATALOG_OID
     *
     * @param materialCatalogOid the value for t_material_catalog_relation.MATERIAL_CATALOG_OID
     *
     * @mbggenerated
     */
    public void setMaterialCatalogOid(String materialCatalogOid) {
        this.materialCatalogOid = materialCatalogOid == null ? null : materialCatalogOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_material_catalog_relation.RELATION_OID
     *
     * @return the value of t_material_catalog_relation.RELATION_OID
     *
     * @mbggenerated
     */
    public String getRelationOid() {
        return relationOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_material_catalog_relation.RELATION_OID
     *
     * @param relationOid the value for t_material_catalog_relation.RELATION_OID
     *
     * @mbggenerated
     */
    public void setRelationOid(String relationOid) {
        this.relationOid = relationOid == null ? null : relationOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_material_catalog_relation.CREATE_DATE
     *
     * @return the value of t_material_catalog_relation.CREATE_DATE
     *
     * @mbggenerated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_material_catalog_relation.CREATE_DATE
     *
     * @param createDate the value for t_material_catalog_relation.CREATE_DATE
     *
     * @mbggenerated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_material_catalog_relation.MODIFY_DATE
     *
     * @return the value of t_material_catalog_relation.MODIFY_DATE
     *
     * @mbggenerated
     */
    public Date getModifyDate() {
        return modifyDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_material_catalog_relation.MODIFY_DATE
     *
     * @param modifyDate the value for t_material_catalog_relation.MODIFY_DATE
     *
     * @mbggenerated
     */
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_material_catalog_relation.DEL_FLAG
     *
     * @return the value of t_material_catalog_relation.DEL_FLAG
     *
     * @mbggenerated
     */
    public Integer getDelFlag() {
        return delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_material_catalog_relation.DEL_FLAG
     *
     * @param delFlag the value for t_material_catalog_relation.DEL_FLAG
     *
     * @mbggenerated
     */
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}