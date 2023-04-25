package com.zfsoft.ocr.data.pojo.speech;


import com.zfsoft.ocr.data.pojo.BaseResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 语音base64转文字响应信息
 *
 * @author chenbw
 * @date 2019年6月25日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@ApiModel(description = "语音base64转文字响应信息")
public class SpeechContentByBase64Response extends BaseResponse {

    @ApiModelProperty(value = "识别语音内容")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
