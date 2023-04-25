package com.zfsoft.ocr.data.pojo.speech;

import com.zfsoft.ocr.data.pojo.BaseResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @description
 * @author lmz
 * @date 2019-07-16 13:18:06
 * @copyright 上海卓繁信息技术股份有限公司版权所有 .
 */
@ApiModel(description = "unit响应信息")
public class SpeechUtteranceResponse extends BaseResponse {

    @ApiModelProperty(value = "会话id")
    private String sessionId;

    @ApiModelProperty(value = "意图")
    private String mainExe;

    @ApiModelProperty(value = "语音识别文本")
    private String voiceText;

    @ApiModelProperty(value = "返回的闲聊数据")
    private String data;

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
     * @return the mainExe
     */
    public String getMainExe() {
        return mainExe;
    }

    /**
     * @param mainExe the mainExe to set
     */
    public void setMainExe(String mainExe) {
        this.mainExe = mainExe;
    }

    /**
     * @return the voiceText
     */
    public String getVoiceText() {
        return voiceText;
    }

    /**
     * @param voiceText the voiceText to set
     */
    public void setVoiceText(String voiceText) {
        this.voiceText = voiceText;
    }

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}

