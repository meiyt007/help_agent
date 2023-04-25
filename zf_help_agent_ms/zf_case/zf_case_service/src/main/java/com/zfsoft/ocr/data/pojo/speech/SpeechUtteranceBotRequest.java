package com.zfsoft.ocr.data.pojo.speech;

import com.zfsoft.ocr.data.pojo.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @description
 * @author lmz
 * @date 2019-07-16 13:31:29
 * @copyright 上海卓繁信息技术股份有限公司版权所有 .
 */
@ApiModel(description = "闲聊场景请求信息")
public class SpeechUtteranceBotRequest extends BaseRequest {

    @ApiModelProperty(value = "请求的对话内容", required = true)
    private String content;

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }



}

