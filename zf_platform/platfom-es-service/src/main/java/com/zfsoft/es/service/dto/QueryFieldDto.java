package com.zfsoft.es.service.dto;

/**
 * @author: kkfan
 * @create: 2020-01-15 23:01:39
 * @description:    查询字段实体
 */
public class QueryFieldDto {

    /** 查询字段名 */
    private String fieldName;

    /** 查询字段值 */
    private Object fieldValue;


    /** 分词器 */
    private String analyzer;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer(String analyzer) {
        this.analyzer = analyzer;
    }
}
