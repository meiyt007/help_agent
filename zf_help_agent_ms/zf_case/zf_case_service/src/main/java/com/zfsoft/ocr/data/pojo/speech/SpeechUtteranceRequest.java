package com.zfsoft.ocr.data.pojo.speech;

import com.zfsoft.ocr.data.pojo.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @description
 * @author lmz
 * @date 2019-07-16 13:04:04
 * @copyright 上海卓繁信息技术股份有限公司版权所有 .
 */
@ApiModel(description = "unit请求信息")
public class SpeechUtteranceRequest extends BaseRequest {

    @ApiModelProperty(value = "会话ID", required = false)
    private String sessionId;

    @ApiModelProperty(value = "请求的对话内容", required = true)
    private String content;

    /**
     * @return the sessionId
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

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

