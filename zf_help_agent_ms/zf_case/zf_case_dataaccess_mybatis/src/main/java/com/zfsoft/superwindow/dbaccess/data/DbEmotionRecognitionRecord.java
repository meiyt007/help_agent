package com.zfsoft.superwindow.dbaccess.data;

import java.util.Date;

/**
 * @author sky
 * @version 1.0
 * @description: TODO
 * @date 2021/8/13 9:23
 */
public class DbEmotionRecognitionRecord  {

    private Long id;
    /**
     * 办件人编码
     */
    private String code;
    /**
     * 第几张图片
     */
    private Integer photoNum;
    /**
     * 情绪（angre disgust fear happy neutral sad surprise）
     */
    private String emotion;
    /**
     * 置信程度
     */
    private String emotionDegree;

    private String emotionTop;

    private String emotionRight;

    private String emotionBottom;

    private String emotionLeft;

    private Date createDate;

    private String createUser;

    //add
    private String  score;//情绪分数

    private String  virtualBusinessOid;//虚拟业务记录表主键

    private String  location;//坐标

    private Integer types;//0 上传到服务器，1 不上传

    private String   picAddress;//图片地址

    private String   caseOid;//办件表主键

    public String getCaseOid() {
        return caseOid;
    }

    public void setCaseOid(String caseOid) {
        this.caseOid = caseOid;
    }

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

    public Integer getPhotoNum() {
        return photoNum;
    }

    public void setPhotoNum(Integer photoNum) {
        this.photoNum = photoNum;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getEmotionDegree() {
        return emotionDegree;
    }

    public void setEmotionDegree(String emotionDegree) {
        this.emotionDegree = emotionDegree;
    }

    public String getEmotionTop() {
        return emotionTop;
    }

    public void setEmotionTop(String emotionTop) {
        this.emotionTop = emotionTop;
    }

    public String getEmotionRight() {
        return emotionRight;
    }

    public void setEmotionRight(String emotionRight) {
        this.emotionRight = emotionRight;
    }

    public String getEmotionBottom() {
        return emotionBottom;
    }

    public void setEmotionBottom(String emotionBottom) {
        this.emotionBottom = emotionBottom;
    }

    public String getEmotionLeft() {
        return emotionLeft;
    }

    public void setEmotionLeft(String emotionLeft) {
        this.emotionLeft = emotionLeft;
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getVirtualBusinessOid() {
        return virtualBusinessOid;
    }

    public void setVirtualBusinessOid(String virtualBusinessOid) {
        this.virtualBusinessOid = virtualBusinessOid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getTypes() {
        return types;
    }

    public void setTypes(Integer types) {
        this.types = types;
    }

    public String getPicAddress() {
        return picAddress;
    }

    public void setPicAddress(String picAddress) {
        this.picAddress = picAddress;
    }
}

