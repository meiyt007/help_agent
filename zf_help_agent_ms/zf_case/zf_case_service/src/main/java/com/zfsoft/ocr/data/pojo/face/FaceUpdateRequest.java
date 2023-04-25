package com.zfsoft.ocr.data.pojo.face;

import com.zfsoft.ocr.data.pojo.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 人脸更新接口，请求信息
 *
 * @Auther cbw
 * @Date 2019/12/11 16:27
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@SuppressWarnings("serial")
@ApiModel(description= "人脸更新接口请求参数")
public class FaceUpdateRequest extends BaseRequest {

    @ApiModelProperty(value = "用户id",required=true)
    private String userId;

    @ApiModelProperty(value = "人脸图像",required=true)
    private String base64ImageData;

    @ApiModelProperty(value = "用户组ID",required=true)
    private String groupId;


    /**
     * 获取 @ApiModelProperty(value = "用户id"required=true)
     *
     * @return userId @ApiModelProperty(value = "用户id"required=true)
     */
    public String getUserId() {
        return this.userId;
    }

    /**
     * 设置 @ApiModelProperty(value = "用户id"required=true)
     *
     * @param userId @ApiModelProperty(value = "用户id"required=true)
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取 @ApiModelProperty(value = "人脸图像"required=true)
     *
     * @return base64ImageData @ApiModelProperty(value = "人脸图像"required=true)
     */
    public String getBase64ImageData() {
        return this.base64ImageData;
    }

    /**
     * 设置 @ApiModelProperty(value = "人脸图像"required=true)
     *
     * @param base64ImageData @ApiModelProperty(value = "人脸图像"required=true)
     */
    public void setBase64ImageData(String base64ImageData) {
        this.base64ImageData = base64ImageData;
    }

    /**
     * 获取 @ApiModelProperty(value = "用户组ID"required=true)
     *
     * @return groupId @ApiModelProperty(value = "用户组ID"required=true)
     */
    public String getGroupId() {
        return this.groupId;
    }

    /**
     * 设置 @ApiModelProperty(value = "用户组ID"required=true)
     *
     * @param groupId @ApiModelProperty(value = "用户组ID"required=true)
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
