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
public class FaceAddResponse extends BaseResponse {

    @ApiModelProperty(value = "头像位置-宽")
    private String posWidth;

    @ApiModelProperty(value = "头像位置-顶端")
    private String posTop;

    @ApiModelProperty(value = "头像位置-左端")
    private String posLeft;

    @ApiModelProperty(value = "头像位置-高 ")
    private String posHeight;

    @ApiModelProperty(value = "头像位置-旋转")
    private String rotation;

    @ApiModelProperty(value = "FACETOKEN")
    private String faceToken;

    public String getPosWidth() {
        return posWidth;
    }

    public void setPosWidth(String posWidth) {
        this.posWidth = posWidth;
    }

    public String getPosTop() {
        return posTop;
    }

    public void setPosTop(String posTop) {
        this.posTop = posTop;
    }

    public String getPosLeft() {
        return posLeft;
    }

    public void setPosLeft(String posLeft) {
        this.posLeft = posLeft;
    }

    public String getPosHeight() {
        return posHeight;
    }

    public void setPosHeight(String posHeight) {
        this.posHeight = posHeight;
    }

    public String getRotation() {
        return rotation;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation;
    }

    public String getFaceToken() {
        return faceToken;
    }

    public void setFaceToken(String faceToken) {
        this.faceToken = faceToken;
    }


    
    
}
