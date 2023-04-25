package com.zfsoft.es.service.entity;

import java.util.List;

/**
 * @author: kkfan
 * @create: 2021-01-14 17:46:07
 * @description: 分页实体
 */
public class Page {
    /** 当前页 */
    private Integer currentPage;

    /** 每页大小 */
    private Integer pageSize;

    /** 总记录数 */
    private Integer total;

    /** 结果 */
    private List resList;

    public Page() {};

    public Page(Integer currentPage, Integer pageSize, Integer total, List resList) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.total = total;
        this.resList = resList;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List getResList() {
        return resList;
    }

    public void setResList(List resList) {
        this.resList = resList;
    }
}
