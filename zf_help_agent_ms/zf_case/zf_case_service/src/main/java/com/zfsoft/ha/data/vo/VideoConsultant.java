package com.zfsoft.ha.data.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
@Data
@ToString
public class VideoConsultant {
    /**
     * 主键
     */
    private Long id;

    /**
     *姓名
     */
    private String name;

    /**
     *账号
     */
    private String account;

    /**
     *手机号
     */
    private String phone;

    /**
     *身份证号码
     */
    private String idNo;

    /**
     *登录密码
     */
    private String password;

    /**
     *工号
     */
    private String workNumber;

    /**
     *房间号
     */
    private String roomNumber;

    /**
     *部门
     */
    private String organOid;

    /**
     *排序号
     */
    private String sort;

    /**
     *头像
     */
    private String image;

    /**
     *盐值，用于密码加密
     */
    private String salt;

    /**
     *电子邮箱
     */
    private String email;

    /**
     *备注
     */
    private String memo;

    /**
     *类型(预留字段)
     */
    private String userType;

    /**
     *删除状态
     */
    private String deleteStatus;

    /**
     *创建时间
     */
    private Date createDate;

    /**
     *更新时间
     */
    private Date updateDate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_consultant.ID
     *
     * @return the value of t_ha_video_consultant.ID
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_consultant.ID
     *
     * @param id the value for t_ha_video_consultant.ID
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_consultant.NAME
     *
     * @return the value of t_ha_video_consultant.NAME
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_consultant.NAME
     *
     * @param name the value for t_ha_video_consultant.NAME
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_consultant.ACCOUNT
     *
     * @return the value of t_ha_video_consultant.ACCOUNT
     *
     * @mbg.generated
     */
    public String getAccount() {
        return account;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_consultant.ACCOUNT
     *
     * @param account the value for t_ha_video_consultant.ACCOUNT
     *
     * @mbg.generated
     */
    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_consultant.PHONE
     *
     * @return the value of t_ha_video_consultant.PHONE
     *
     * @mbg.generated
     */
    public String getPhone() {
        return phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_consultant.PHONE
     *
     * @param phone the value for t_ha_video_consultant.PHONE
     *
     * @mbg.generated
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_consultant.ID_NO
     *
     * @return the value of t_ha_video_consultant.ID_NO
     *
     * @mbg.generated
     */
    public String getIdNo() {
        return idNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_consultant.ID_NO
     *
     * @param idNo the value for t_ha_video_consultant.ID_NO
     *
     * @mbg.generated
     */
    public void setIdNo(String idNo) {
        this.idNo = idNo == null ? null : idNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_consultant.PASSWORD
     *
     * @return the value of t_ha_video_consultant.PASSWORD
     *
     * @mbg.generated
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_consultant.PASSWORD
     *
     * @param password the value for t_ha_video_consultant.PASSWORD
     *
     * @mbg.generated
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_consultant.WORK_NUMBER
     *
     * @return the value of t_ha_video_consultant.WORK_NUMBER
     *
     * @mbg.generated
     */
    public String getWorkNumber() {
        return workNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_consultant.WORK_NUMBER
     *
     * @param workNumber the value for t_ha_video_consultant.WORK_NUMBER
     *
     * @mbg.generated
     */
    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber == null ? null : workNumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_consultant.ROOM_NUMBER
     *
     * @return the value of t_ha_video_consultant.ROOM_NUMBER
     *
     * @mbg.generated
     */
    public String getRoomNumber() {
        return roomNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_consultant.ROOM_NUMBER
     *
     * @param roomNumber the value for t_ha_video_consultant.ROOM_NUMBER
     *
     * @mbg.generated
     */
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber == null ? null : roomNumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_consultant.ORGAN_OID
     *
     * @return the value of t_ha_video_consultant.ORGAN_OID
     *
     * @mbg.generated
     */
    public String getOrganOid() {
        return organOid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_consultant.ORGAN_OID
     *
     * @param organOid the value for t_ha_video_consultant.ORGAN_OID
     *
     * @mbg.generated
     */
    public void setOrganOid(String organOid) {
        this.organOid = organOid == null ? null : organOid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_consultant.SORT
     *
     * @return the value of t_ha_video_consultant.SORT
     *
     * @mbg.generated
     */
    public String getSort() {
        return sort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_consultant.SORT
     *
     * @param sort the value for t_ha_video_consultant.SORT
     *
     * @mbg.generated
     */
    public void setSort(String sort) {
        this.sort = sort == null ? null : sort.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_consultant.IMAGE
     *
     * @return the value of t_ha_video_consultant.IMAGE
     *
     * @mbg.generated
     */
    public String getImage() {
        return image;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_consultant.IMAGE
     *
     * @param image the value for t_ha_video_consultant.IMAGE
     *
     * @mbg.generated
     */
    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_consultant.SALT
     *
     * @return the value of t_ha_video_consultant.SALT
     *
     * @mbg.generated
     */
    public String getSalt() {
        return salt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_consultant.SALT
     *
     * @param salt the value for t_ha_video_consultant.SALT
     *
     * @mbg.generated
     */
    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_consultant.EMAIL
     *
     * @return the value of t_ha_video_consultant.EMAIL
     *
     * @mbg.generated
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_consultant.EMAIL
     *
     * @param email the value for t_ha_video_consultant.EMAIL
     *
     * @mbg.generated
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_consultant.MEMO
     *
     * @return the value of t_ha_video_consultant.MEMO
     *
     * @mbg.generated
     */
    public String getMemo() {
        return memo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_consultant.MEMO
     *
     * @param memo the value for t_ha_video_consultant.MEMO
     *
     * @mbg.generated
     */
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_consultant.USER_TYPE
     *
     * @return the value of t_ha_video_consultant.USER_TYPE
     *
     * @mbg.generated
     */
    public String getUserType() {
        return userType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_consultant.USER_TYPE
     *
     * @param userType the value for t_ha_video_consultant.USER_TYPE
     *
     * @mbg.generated
     */
    public void setUserType(String userType) {
        this.userType = userType == null ? null : userType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_consultant.DELETE_STATUS
     *
     * @return the value of t_ha_video_consultant.DELETE_STATUS
     *
     * @mbg.generated
     */
    public String getDeleteStatus() {
        return deleteStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_consultant.DELETE_STATUS
     *
     * @param deleteStatus the value for t_ha_video_consultant.DELETE_STATUS
     *
     * @mbg.generated
     */
    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus == null ? null : deleteStatus.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_consultant.CREATE_DATE
     *
     * @return the value of t_ha_video_consultant.CREATE_DATE
     *
     * @mbg.generated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_consultant.CREATE_DATE
     *
     * @param createDate the value for t_ha_video_consultant.CREATE_DATE
     *
     * @mbg.generated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_ha_video_consultant.UPDATE_DATE
     *
     * @return the value of t_ha_video_consultant.UPDATE_DATE
     *
     * @mbg.generated
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_ha_video_consultant.UPDATE_DATE
     *
     * @param updateDate the value for t_ha_video_consultant.UPDATE_DATE
     *
     * @mbg.generated
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
