package com.zfsoft.single.dbaccess.data.ywbl;

import java.util.Date;

/**
 * 情绪分析记录实体类
 *
 * @author wangwg
 * @since 2021-06-17
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

}
