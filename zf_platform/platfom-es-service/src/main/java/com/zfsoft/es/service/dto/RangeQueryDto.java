package com.zfsoft.es.service.dto;

/**
 * @author: kkfan
 * @create: 2020-01-15 23:01:39
 * @description:    事件范围查询字段实体
 */
public class RangeQueryDto {

    /** 查询字段名 */
    private String fieldName;

    /** 查询开始值 */
    private Object startTime;

    /** 查询结束值 */
    private Object endTime;

    public String getFieldName() {
        return fieldName;
    }

    public RangeQueryDto setFieldName(String fieldName) {
        this.fieldName = fieldName == null ? null : fieldName.trim();
        return this;
    }

    public Object getStartTime() {
        return startTime;
    }

    public RangeQueryDto setStartTime(Object startTime) {
        this.startTime = startTime;
        return this;
    }

    public Object getEndTime() {
        return endTime;
    }

    public RangeQueryDto setEndTime(Object endTime) {
        this.endTime = endTime;
        return this;
    }
}
