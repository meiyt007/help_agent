package com.zfsoft.ocr.data.pojo.face;


import com.zfsoft.ocr.data.pojo.BaseResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 人脸注册接口响应信息
 *
 * @Auther cbc
 * @Date 2019/6/22 13:27
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@SuppressWarnings("serial")
@ApiModel(description= "人脸注册接口响应信息")
public class FaceSearchResponse extends BaseResponse {

    /** 人脸搜索-人脸token */
    @ApiModelProperty(value = "人脸token")
    private String faceToken;

    /** 人脸搜索-人脸库组id */
    @ApiModelProperty(value = "人脸库组id")
    private String groupId;

    /** 人脸搜索-人脸比对分数 */
    @ApiModelProperty(value = "人脸比对分数")
    private Double score;

    /** 人脸搜索-人脸库用户id */
    @ApiModelProperty(value = "人脸库用户id")
    private String userId;

    /** 人脸搜索-人脸库人脸信息 */
    @ApiModelProperty(value = "人脸库人脸信息")
    private String userInfo;

    public String getFaceToken() {
        return faceToken;
    }

    public void setFaceToken(String faceToken) {
        this.faceToken = faceToken;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }


}
