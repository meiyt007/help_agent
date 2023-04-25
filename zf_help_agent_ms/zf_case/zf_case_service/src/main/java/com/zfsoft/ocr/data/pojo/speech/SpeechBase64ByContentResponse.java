package com.zfsoft.ocr.data.pojo.speech;

import com.zfsoft.ocr.data.pojo.BaseResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 文字转语音base64响应
 * @author chenbw
 * @date 2019年6月24日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@ApiModel(description = "文字转语音base64响应信息")
public class SpeechBase64ByContentResponse extends BaseResponse {

    @ApiModelProperty(value = "base64字符串")
    private String base64;

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

}
