package com.zfsoft.ocr.data.pojo.face;

import com.zfsoft.ocr.data.pojo.BaseResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 人脸检测接口响应信息
 *
 * @Auther cbc
 * @Date 2019/6/22 13:27
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@SuppressWarnings("serial")
@ApiModel(description= "人脸检测接口响应信息")
public class FaceDetectV3Response extends BaseResponse {

    @ApiModelProperty(value = "人脸个数")
    private Integer faceNum;

    public Integer getFaceNum() {
        return faceNum;
    }

    public void setFaceNum(Integer faceNum) {
        this.faceNum = faceNum;
    }
}
