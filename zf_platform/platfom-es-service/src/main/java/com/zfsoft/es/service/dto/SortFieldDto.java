package com.zfsoft.es.service.dto;

import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

/**
 * @author: kkfan
 * @create: 2020-01-15 22:50:08
 * @description:    排序实体
 */
public class SortFieldDto {

    /** 排序字段 */
    private String fieldName;

    /** 排序类型 asc 正序 desc倒序 */
    private String sortType;

    public String getFieldName() {
        return fieldName;
    }

    public SortFieldDto setFieldName(String fieldName) {
        this.fieldName = fieldName == null ? null : fieldName.trim();
        return this;
    }

    public String getSortType() {
        return sortType;
    }

    public SortFieldDto setSortType(String sortType) {
        if(StringUtils.equalsAnyIgnoreCase(sortType, "ASC", "DESC")) {
            this.sortType = sortType;
        } else {
            throw new IllegalArgumentException(MessageFormat.format("sortType类型错误, asc -- 正序 desc -- 倒叙，输入：{0}", sortType));
        }
        return this;
    }
}
