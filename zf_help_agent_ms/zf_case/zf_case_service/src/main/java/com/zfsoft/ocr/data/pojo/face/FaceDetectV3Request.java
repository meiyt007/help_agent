package com.zfsoft.ocr.data.pojo.face;

import com.zfsoft.ocr.data.pojo.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 人脸检测接口，请求信息
 *
 * @Auther cbc
 * @Date 2019/6/22 13:17
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@SuppressWarnings("serial")
@ApiModel(description= "人脸检测接口请求参数")
public class FaceDetectV3Request extends BaseRequest {

    @ApiModelProperty(value = "人脸图像",required=true)
    private String base64ImageData;

    public String getBase64ImageData() {
        return base64ImageData;
    }

    public void setBase64ImageData(String base64ImageData) {
        this.base64ImageData = base64ImageData;
    }
    
}
