package com.zfsoft.ha.outer.scanning.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * page result data object definition
 * @Auther: lijun
 * @Date: 2020-08-24 14:56
 */
public class PageResult<T> {
    private int pageNum;
    private int pageSize;
    private long total;
    private List<T> data;

    public PageResult(int pageNum,int pageSize,long total){
        this.pageNum=pageNum;
        this.pageSize=pageSize;
        this.total=total;
        this.data = new ArrayList<>();
    }
    public PageResult(){

    }
    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
