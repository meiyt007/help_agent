package com.zfsoft.ocr.data.pojo.speech;


import com.zfsoft.ocr.data.pojo.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 语音base64转文字请求信息
 * @author chenbw
 * @date 2019年6月25日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@ApiModel(description = "语音base64转文字请求信息")
public class SpeechContentByBase64Request extends BaseRequest {

    @ApiModelProperty(value = "语音base64", required = true)
    private String base64;

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

}
