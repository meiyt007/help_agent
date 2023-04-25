package com.zfsoft.superwindow.dbaccess.data;

import java.util.Date;

/**
 * @author sky
 * @version 1.0
 * @description: TODO
 * @date 2021/8/16 9:10
 */
public class DbHandleServiceStar {

    private Long id;
    /**
     * 办理人编号
     */
    private String code;
    /**
     * 平均星数
     */
    private Integer starValue;

    private Date createDate;

    private String createUser;

    /**
     * 虚拟业务记录表主键
     */
    private  String virtualBusinessOid;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getStarValue() {
        return starValue;
    }

    public void setStarValue(Integer starValue) {
        this.starValue = starValue;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getVirtualBusinessOid() {
        return virtualBusinessOid;
    }

    public void setVirtualBusinessOid(String virtualBusinessOid) {
        this.virtualBusinessOid = virtualBusinessOid;
    }
}
