package com.zfsoft.cases.dbaccess.data;

import java.io.Serializable;

/**
 * 记录办件信息与选择材料情形选型关系表(QlCaseSituationTitleValRelation)实体类
 *
 * @author wangwg
 * @date  2020-11-30
 */
public class DbQlCaseSituationTitleValRelation implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 业务主键
     */
    private String relationOid;

    /**
     * 办件业务主键
     */
    private String caseOid;

    /**
     * 情形业务主键
     */
    private String situationOid;

    /**
     * 标题业务主键
     */
    private String titleOid;

    /**
     * 选项值业务主键
     */
    private String valueOid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRelationOid() {
        return relationOid;
    }

    public void setRelationOid(String relationOid) {
        this.relationOid = relationOid;
    }

    public String getCaseOid() {
        return caseOid;
    }

    public void setCaseOid(String caseOid) {
        this.caseOid = caseOid;
    }

    public String getSituationOid() {
        return situationOid;
    }

    public void setSituationOid(String situationOid) {
        this.situationOid = situationOid;
    }

    public String getTitleOid() {
        return titleOid;
    }

    public void setTitleOid(String titleOid) {
        this.titleOid = titleOid;
    }

    public String getValueOid() {
        return valueOid;
    }

    public void setValueOid(String valueOid) {
        this.valueOid = valueOid;
    }
}
