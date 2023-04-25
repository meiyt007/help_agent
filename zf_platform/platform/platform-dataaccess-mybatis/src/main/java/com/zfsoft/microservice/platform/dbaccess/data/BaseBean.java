package com.zfsoft.microservice.platform.dbaccess.data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description:  基础的bean
 * @author: wuxx
 * @Date: 2020/8/28 10:41
 **/
public class BaseBean {

    /**
     * 修改时间 此字段用于做时间戳记录 在新增和修改时 时间会更新
     */
    private Date modifyDate;

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = new Date();
    }
}
