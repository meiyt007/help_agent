package com.zfsoft.ocr.data.pojo.speech;


import com.zfsoft.ocr.data.pojo.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 语音base64请求信息
 *
 * @author chenbw
 * @date 2019年6月24日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@ApiModel(description = "文字转语音base64请求信息")
public class SpeechBase64ByContentRequest extends BaseRequest {

    @ApiModelProperty(value = "文字内容", required = true)
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
