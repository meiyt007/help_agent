package com.zfsoft.ocr.data.pojo.ocr;

import com.zfsoft.ocr.data.pojo.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * ocr印刷体识别，请求信息
 * 
 * @author chenbw
 * @date 2019年6月26日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@ApiModel(description = "ocr印刷体识别，请求信息")
public class OcrHandPrintRequest extends BaseRequest {

    /**
     * 图片base64字符串
     */
    @ApiModelProperty(value = "图片base64字符串", required = true)
    private String imgBase64;

    public String getImgBase64() {
        return this.imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }
}
