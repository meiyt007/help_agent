package com.zfsoft.superwindow.dbaccess.data;

import java.util.Date;

public class DbMaterialCatalogElement {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_material_catalog_element.ID
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_material_catalog_element.MATERIAL_CATALOG_ELEMENT_OID
     *
     * @mbggenerated
     */
    private String materialCatalogElementOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_material_catalog_element.MATERIAL_CATALOG_OID
     *
     * @mbggenerated
     */
    private String materialCatalogOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_material_catalog_element.MATERIAL_CATALOG_PARENT_OID
     *
     * @mbggenerated
     */
    private String materialCatalogParentOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_material_catalog_element.MATERIAL_CATALOG_NAME
     *
     * @mbggenerated
     */
    private String materialCatalogName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_material_catalog_element.ELEMENT_CODE
     *
     * @mbggenerated
     */
    private String elementCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_material_catalog_element.ELEMENT_NAME
     *
     * @mbggenerated
     */
    private String elementName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_material_catalog_element.CREATE_DATE
     *
     * @mbggenerated
     */
    private Date createDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_material_catalog_element.MODIFY_DATE
     *
     * @mbggenerated
     */
    private Date modifyDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_material_catalog_element.DEL_FLAG
     *
     * @mbggenerated
     */
    private Integer delFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_material_catalog_element.CARD_CATALOGUE_ELEMENT_OID
     *
     * @mbggenerated
     */
    private String cardCatalogueElementOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_material_catalog_element.CARD_CATALOGUE_ELEMENT_CODE
     *
     * @mbggenerated
     */
    private String cardCatalogueElementCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_material_catalog_element.CARD_CATALOGUE_ELEMENT_NAME
     *
     * @mbggenerated
     */
    private String cardCatalogueElementName;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_material_catalog_element.ID
     *
     * @return the value of t_material_catalog_element.ID
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_material_catalog_element.ID
     *
     * @param id the value for t_material_catalog_element.ID
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_material_catalog_element.MATERIAL_CATALOG_ELEMENT_OID
     *
     * @return the value of t_material_catalog_element.MATERIAL_CATALOG_ELEMENT_OID
     *
     * @mbggenerated
     */
    public String getMaterialCatalogElementOid() {
        return materialCatalogElementOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_material_catalog_element.MATERIAL_CATALOG_ELEMENT_OID
     *
     * @param materialCatalogElementOid the value for t_material_catalog_element.MATERIAL_CATALOG_ELEMENT_OID
     *
     * @mbggenerated
     */
    public void setMaterialCatalogElementOid(String materialCatalogElementOid) {
        this.materialCatalogElementOid = materialCatalogElementOid == null ? null : materialCatalogElementOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_material_catalog_element.MATERIAL_CATALOG_OID
     *
     * @return the value of t_material_catalog_element.MATERIAL_CATALOG_OID
     *
     * @mbggenerated
     */
    public String getMaterialCatalogOid() {
        return materialCatalogOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_material_catalog_element.MATERIAL_CATALOG_OID
     *
     * @param materialCatalogOid the value for t_material_catalog_element.MATERIAL_CATALOG_OID
     *
     * @mbggenerated
     */
    public void setMaterialCatalogOid(String materialCatalogOid) {
        this.materialCatalogOid = materialCatalogOid == null ? null : materialCatalogOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_material_catalog_element.MATERIAL_CATALOG_PARENT_OID
     *
     * @return the value of t_material_catalog_element.MATERIAL_CATALOG_PARENT_OID
     *
     * @mbggenerated
     */
    public String getMaterialCatalogParentOid() {
        return materialCatalogParentOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_material_catalog_element.MATERIAL_CATALOG_PARENT_OID
     *
     * @param materialCatalogParentOid the value for t_material_catalog_element.MATERIAL_CATALOG_PARENT_OID
     *
     * @mbggenerated
     */
    public void setMaterialCatalogParentOid(String materialCatalogParentOid) {
        this.materialCatalogParentOid = materialCatalogParentOid == null ? null : materialCatalogParentOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_material_catalog_element.MATERIAL_CATALOG_NAME
     *
     * @return the value of t_material_catalog_element.MATERIAL_CATALOG_NAME
     *
     * @mbggenerated
     */
    public String getMaterialCatalogName() {
        return materialCatalogName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_material_catalog_element.MATERIAL_CATALOG_NAME
     *
     * @param materialCatalogName the value for t_material_catalog_element.MATERIAL_CATALOG_NAME
     *
     * @mbggenerated
     */
    public void setMaterialCatalogName(String materialCatalogName) {
        this.materialCatalogName = materialCatalogName == null ? null : materialCatalogName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_material_catalog_element.ELEMENT_CODE
     *
     * @return the value of t_material_catalog_element.ELEMENT_CODE
     *
     * @mbggenerated
     */
    public String getElementCode() {
        return elementCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_material_catalog_element.ELEMENT_CODE
     *
     * @param elementCode the value for t_material_catalog_element.ELEMENT_CODE
     *
     * @mbggenerated
     */
    public void setElementCode(String elementCode) {
        this.elementCode = elementCode == null ? null : elementCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_material_catalog_element.ELEMENT_NAME
     *
     * @return the value of t_material_catalog_element.ELEMENT_NAME
     *
     * @mbggenerated
     */
    public String getElementName() {
        return elementName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_material_catalog_element.ELEMENT_NAME
     *
     * @param elementName the value for t_material_catalog_element.ELEMENT_NAME
     *
     * @mbggenerated
     */
    public void setElementName(String elementName) {
        this.elementName = elementName == null ? null : elementName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_material_catalog_element.CREATE_DATE
     *
     * @return the value of t_material_catalog_element.CREATE_DATE
     *
     * @mbggenerated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_material_catalog_element.CREATE_DATE
     *
     * @param createDate the value for t_material_catalog_element.CREATE_DATE
     *
     * @mbggenerated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_material_catalog_element.MODIFY_DATE
     *
     * @return the value of t_material_catalog_element.MODIFY_DATE
     *
     * @mbggenerated
     */
    public Date getModifyDate() {
        return modifyDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_material_catalog_element.MODIFY_DATE
     *
     * @param modifyDate the value for t_material_catalog_element.MODIFY_DATE
     *
     * @mbggenerated
     */
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_material_catalog_element.DEL_FLAG
     *
     * @return the value of t_material_catalog_element.DEL_FLAG
     *
     * @mbggenerated
     */
    public Integer getDelFlag() {
        return delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_material_catalog_element.DEL_FLAG
     *
     * @param delFlag the value for t_material_catalog_element.DEL_FLAG
     *
     * @mbggenerated
     */
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_material_catalog_element.CARD_CATALOGUE_ELEMENT_OID
     *
     * @return the value of t_material_catalog_element.CARD_CATALOGUE_ELEMENT_OID
     *
     * @mbggenerated
     */
    public String getCardCatalogueElementOid() {
        return cardCatalogueElementOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_material_catalog_element.CARD_CATALOGUE_ELEMENT_OID
     *
     * @param cardCatalogueElementOid the value for t_material_catalog_element.CARD_CATALOGUE_ELEMENT_OID
     *
     * @mbggenerated
     */
    public void setCardCatalogueElementOid(String cardCatalogueElementOid) {
        this.cardCatalogueElementOid = cardCatalogueElementOid == null ? null : cardCatalogueElementOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_material_catalog_element.CARD_CATALOGUE_ELEMENT_CODE
     *
     * @return the value of t_material_catalog_element.CARD_CATALOGUE_ELEMENT_CODE
     *
     * @mbggenerated
     */
    public String getCardCatalogueElementCode() {
        return cardCatalogueElementCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_material_catalog_element.CARD_CATALOGUE_ELEMENT_CODE
     *
     * @param cardCatalogueElementCode the value for t_material_catalog_element.CARD_CATALOGUE_ELEMENT_CODE
     *
     * @mbggenerated
     */
    public void setCardCatalogueElementCode(String cardCatalogueElementCode) {
        this.cardCatalogueElementCode = cardCatalogueElementCode == null ? null : cardCatalogueElementCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_material_catalog_element.CARD_CATALOGUE_ELEMENT_NAME
     *
     * @return the value of t_material_catalog_element.CARD_CATALOGUE_ELEMENT_NAME
     *
     * @mbggenerated
     */
    public String getCardCatalogueElementName() {
        return cardCatalogueElementName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_material_catalog_element.CARD_CATALOGUE_ELEMENT_NAME
     *
     * @param cardCatalogueElementName the value for t_material_catalog_element.CARD_CATALOGUE_ELEMENT_NAME
     *
     * @mbggenerated
     */
    public void setCardCatalogueElementName(String cardCatalogueElementName) {
        this.cardCatalogueElementName = cardCatalogueElementName == null ? null : cardCatalogueElementName.trim();
    }
}