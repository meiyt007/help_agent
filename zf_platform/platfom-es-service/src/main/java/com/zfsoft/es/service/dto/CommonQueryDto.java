package com.zfsoft.es.service.dto;


import java.util.List;

/**
 * @author: kkfan
 * @create: 2020-01-15 22:39:11
 * @description:    查询实体
 */
public class CommonQueryDto {

    /** 索引名称 */
    private String indexName;

    /** 索引类型 */
    private String type;

    /** 查询开始时间 默认根据modified字段范围查询 如自定义的字段 请使用RangeQueryDto */
    private String startTime;

    /** 查询结束时间 默认根据modified字段范围查询 如自定义的字段 请使用RangeQueryDto */
    private String endTime;

    /** 当前页 */
    private Integer currentPageNo;

    /** 每页大小 */
    private Integer pageSize;

    /** 高亮字段 */
    private List<String> highlightField;

    /** 排序字段 */
    private List<SortFieldDto> sortFieldDtoList;

    /** 查询字段 */
    private List<QueryFieldDto> queryFields;

    /** 范围查询信息 */
    private List<RangeQueryDto> rangeQueryDtoList;

    public String getIndexName() {
        return indexName;
    }

    public CommonQueryDto setIndexName(String indexName) {
        this.indexName = indexName == null ? null : indexName.trim();
        return this;
    }

    public String getType() {
        return type;
    }

    public CommonQueryDto setType(String type) {
        this.type = type == null ? null : type.trim();
        return this;
    }

    public String getStartTime() {
        return startTime;
    }

    public CommonQueryDto setStartTime(String startTime) {
        this.startTime = startTime == null ? null : startTime.trim();
        return this;
    }

    public String getEndTime() {
        return endTime;
    }

    public CommonQueryDto setEndTime(String endTime) {
        this.endTime = endTime == null ? null : endTime.trim();
        return this;
    }

    public Integer getCurrentPageNo() {
        return currentPageNo;
    }

    public CommonQueryDto setCurrentPageNo(Integer currentPageNo) {
        this.currentPageNo = currentPageNo;
        return this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public CommonQueryDto setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public List<String> getHighlightField() {
        return highlightField;
    }

    public CommonQueryDto setHighlightField(List<String> highlightField) {
        this.highlightField = highlightField;
        return this;
    }

    public List<SortFieldDto> getSortFieldDtoList() {
        return sortFieldDtoList;
    }

    public CommonQueryDto setSortFieldDtoList(List<SortFieldDto> sortFieldDtoList) {
        this.sortFieldDtoList = sortFieldDtoList;
        return this;
    }

    public List<QueryFieldDto> getQueryFields() {
        return queryFields;
    }

    public CommonQueryDto setQueryFields(List<QueryFieldDto> queryFields) {
        this.queryFields = queryFields;
        return this;
    }

    public List<RangeQueryDto> getRangeQueryDtoList() {
        return rangeQueryDtoList;
    }

    public CommonQueryDto setRangeQueryDtoList(List<RangeQueryDto> rangeQueryDtoList) {
        this.rangeQueryDtoList = rangeQueryDtoList;
        return this;
    }
}
