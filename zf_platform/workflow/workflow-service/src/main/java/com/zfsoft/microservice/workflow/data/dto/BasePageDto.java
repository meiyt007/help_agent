package com.zfsoft.microservice.workflow.data.dto;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.groups.Default;

/**
 * @author: kkfan
 * @create: 2021-01-26 10:34:00
 * @description: 基础分页实体
 */
public class BasePageDto {

    public interface UPDATE extends Default {}
    public interface INSERT extends Default {}
    public interface QUERY extends Default {}
    public interface PAGE_QUERY extends Default {}

    @Min(value = 1, message = "当前页不能小于1", groups = {PAGE_QUERY.class})
    @Max(value = 99999, message = "当前页不能大于99999", groups = {PAGE_QUERY.class})
    private Integer pageNum;

    @Min(value = 1, message = "每页大小不能小于1", groups = {PAGE_QUERY.class})
    @Max(value = 999, message = "每页大小不能大于999", groups = {PAGE_QUERY.class})
    private Integer pageSize;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum == null ? 1 : pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize == null ? 10 : pageSize;
    }
}
