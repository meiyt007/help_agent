package com.zfsoft.superwindow.dbaccess.data;

import java.util.Date;

/**
 * (DbComboEvaluationOption)实体类
 *
 * @author wangwg
 * @since 2021-02-25
 */
public class DbEvaluationOption {
    /**
     * 主键
     */
    private Long id;
    /**
     * 评价选项id
     */
    private String optionOid;
    /**
     * 评价标准id
     */
    private String standardOid;
    /**
     * 评价选项内容
     */
    private String optionContent;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 是否有效0否1是
     */
    private Integer delFlag;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionOid() {
        return optionOid;
    }

    public void setOptionOid(String optionOid) {
        this.optionOid = optionOid;
    }

    public String getStandardOid() {
        return standardOid;
    }

    public void setStandardOid(String standardOid) {
        this.standardOid = standardOid;
    }

    public String getOptionContent() {
        return optionContent;
    }

    public void setOptionContent(String optionContent) {
        this.optionContent = optionContent;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

}