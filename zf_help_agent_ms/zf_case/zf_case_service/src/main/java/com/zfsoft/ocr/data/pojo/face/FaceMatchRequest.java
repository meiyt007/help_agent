package com.zfsoft.ocr.data.pojo.face;

import com.zfsoft.ocr.data.pojo.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 人脸比对接口，请求信息
 *
 * @Auther cbc
 * @Date 2019/6/22 13:17
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@SuppressWarnings("serial")
@ApiModel(description= "人脸比对接口请求参数")
public class FaceMatchRequest extends BaseRequest {

    @ApiModelProperty(value = "人脸图像",required=true)
    private String base64ImageOne;

    @ApiModelProperty(value = "身份证头像",required=true)
    private String base64ImageTwo;

    public String getBase64ImageOne() {
        return base64ImageOne;
    }

    public void setBase64ImageOne(String base64ImageOne) {
        this.base64ImageOne = base64ImageOne;
    }

    public String getBase64ImageTwo() {
        return base64ImageTwo;
    }

    public void setBase64ImageTwo(String base64ImageTwo) {
        this.base64ImageTwo = base64ImageTwo;
    }

    
}
