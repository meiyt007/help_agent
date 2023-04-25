package com.zfsoft.microservice.platform.dbaccess.data.sys;

import java.util.Date;

public class DbSysApp {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sys_app.ID
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sys_app.APP_OID
     *
     * @mbggenerated
     */
    private String appOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sys_app.NAME
     *
     * @mbggenerated
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sys_app.DISPLAY_NAME
     *
     * @mbggenerated
     */
    private String displayName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sys_app.ACCESS_ROOT_ADDR
     *
     * @mbggenerated
     */
    private String accessRootAddr;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sys_app.LOGIN_SUCCESS_ADDR
     *
     * @mbggenerated
     */
    private String loginSuccessAddr;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sys_app.REMOTE_REGISTRY_ADDR
     *
     * @mbggenerated
     */
    private String remoteRegistryAddr;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sys_app.MEMO
     *
     * @mbggenerated
     */
    private String memo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sys_app.IS_OTHER
     *
     * @mbggenerated
     */
    private Integer isOther;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sys_app.ICON_NAME
     *
     * @mbggenerated
     */
    private String iconName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sys_app.PARENT_OID
     *
     * @mbggenerated
     */
    private String parentOid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sys_app.CREATE_DATE
     *
     * @mbggenerated
     */
    private Date createDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sys_app.SORT
     *
     * @mbggenerated
     */
    private Integer sort;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sys_app.IS_ABLE
     *
     * @mbggenerated
     */
    private Integer isAble;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sys_app.IS_DELETE
     *
     * @mbggenerated
     */
    private Integer isDelete;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_sys_app.MODIFY_DATE
     *
     * @mbggenerated
     */
    private Date modifyDate;


    /**
     * @description: 上级实体类名称
     * @author: wuxx
     * @Date: 2020/9/10 15:21
     **/
    private String parentName;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sys_app.ID
     *
     * @return the value of t_sys_app.ID
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sys_app.ID
     *
     * @param id the value for t_sys_app.ID
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sys_app.APP_OID
     *
     * @return the value of t_sys_app.APP_OID
     *
     * @mbggenerated
     */
    public String getAppOid() {
        return appOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sys_app.APP_OID
     *
     * @param appOid the value for t_sys_app.APP_OID
     *
     * @mbggenerated
     */
    public void setAppOid(String appOid) {
        this.appOid = appOid == null ? null : appOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sys_app.NAME
     *
     * @return the value of t_sys_app.NAME
     *
     * @mbggenerated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sys_app.NAME
     *
     * @param name the value for t_sys_app.NAME
     *
     * @mbggenerated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sys_app.DISPLAY_NAME
     *
     * @return the value of t_sys_app.DISPLAY_NAME
     *
     * @mbggenerated
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sys_app.DISPLAY_NAME
     *
     * @param displayName the value for t_sys_app.DISPLAY_NAME
     *
     * @mbggenerated
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName == null ? null : displayName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sys_app.ACCESS_ROOT_ADDR
     *
     * @return the value of t_sys_app.ACCESS_ROOT_ADDR
     *
     * @mbggenerated
     */
    public String getAccessRootAddr() {
        return accessRootAddr;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sys_app.ACCESS_ROOT_ADDR
     *
     * @param accessRootAddr the value for t_sys_app.ACCESS_ROOT_ADDR
     *
     * @mbggenerated
     */
    public void setAccessRootAddr(String accessRootAddr) {
        this.accessRootAddr = accessRootAddr == null ? null : accessRootAddr.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sys_app.LOGIN_SUCCESS_ADDR
     *
     * @return the value of t_sys_app.LOGIN_SUCCESS_ADDR
     *
     * @mbggenerated
     */
    public String getLoginSuccessAddr() {
        return loginSuccessAddr;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sys_app.LOGIN_SUCCESS_ADDR
     *
     * @param loginSuccessAddr the value for t_sys_app.LOGIN_SUCCESS_ADDR
     *
     * @mbggenerated
     */
    public void setLoginSuccessAddr(String loginSuccessAddr) {
        this.loginSuccessAddr = loginSuccessAddr == null ? null : loginSuccessAddr.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sys_app.REMOTE_REGISTRY_ADDR
     *
     * @return the value of t_sys_app.REMOTE_REGISTRY_ADDR
     *
     * @mbggenerated
     */
    public String getRemoteRegistryAddr() {
        return remoteRegistryAddr;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sys_app.REMOTE_REGISTRY_ADDR
     *
     * @param remoteRegistryAddr the value for t_sys_app.REMOTE_REGISTRY_ADDR
     *
     * @mbggenerated
     */
    public void setRemoteRegistryAddr(String remoteRegistryAddr) {
        this.remoteRegistryAddr = remoteRegistryAddr == null ? null : remoteRegistryAddr.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sys_app.MEMO
     *
     * @return the value of t_sys_app.MEMO
     *
     * @mbggenerated
     */
    public String getMemo() {
        return memo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sys_app.MEMO
     *
     * @param memo the value for t_sys_app.MEMO
     *
     * @mbggenerated
     */
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sys_app.IS_OTHER
     *
     * @return the value of t_sys_app.IS_OTHER
     *
     * @mbggenerated
     */
    public Integer getIsOther() {
        return isOther;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sys_app.IS_OTHER
     *
     * @param isOther the value for t_sys_app.IS_OTHER
     *
     * @mbggenerated
     */
    public void setIsOther(Integer isOther) {
        this.isOther = isOther;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sys_app.ICON_NAME
     *
     * @return the value of t_sys_app.ICON_NAME
     *
     * @mbggenerated
     */
    public String getIconName() {
        return iconName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sys_app.ICON_NAME
     *
     * @param iconName the value for t_sys_app.ICON_NAME
     *
     * @mbggenerated
     */
    public void setIconName(String iconName) {
        this.iconName = iconName == null ? null : iconName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sys_app.PARENT_OID
     *
     * @return the value of t_sys_app.PARENT_OID
     *
     * @mbggenerated
     */
    public String getParentOid() {
        return parentOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sys_app.PARENT_OID
     *
     * @param parentOid the value for t_sys_app.PARENT_OID
     *
     * @mbggenerated
     */
    public void setParentOid(String parentOid) {
        this.parentOid = parentOid == null ? null : parentOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sys_app.CREATE_DATE
     *
     * @return the value of t_sys_app.CREATE_DATE
     *
     * @mbggenerated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sys_app.CREATE_DATE
     *
     * @param createDate the value for t_sys_app.CREATE_DATE
     *
     * @mbggenerated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sys_app.SORT
     *
     * @return the value of t_sys_app.SORT
     *
     * @mbggenerated
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sys_app.SORT
     *
     * @param sort the value for t_sys_app.SORT
     *
     * @mbggenerated
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sys_app.IS_ABLE
     *
     * @return the value of t_sys_app.IS_ABLE
     *
     * @mbggenerated
     */
    public Integer getIsAble() {
        return isAble;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sys_app.IS_ABLE
     *
     * @param isAble the value for t_sys_app.IS_ABLE
     *
     * @mbggenerated
     */
    public void setIsAble(Integer isAble) {
        this.isAble = isAble;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sys_app.IS_DELETE
     *
     * @return the value of t_sys_app.IS_DELETE
     *
     * @mbggenerated
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sys_app.IS_DELETE
     *
     * @param isDelete the value for t_sys_app.IS_DELETE
     *
     * @mbggenerated
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_sys_app.MODIFY_DATE
     *
     * @return the value of t_sys_app.MODIFY_DATE
     *
     * @mbggenerated
     */
    public Date getModifyDate() {
        return modifyDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_sys_app.MODIFY_DATE
     *
     * @param modifyDate the value for t_sys_app.MODIFY_DATE
     *
     * @mbggenerated
     */
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}