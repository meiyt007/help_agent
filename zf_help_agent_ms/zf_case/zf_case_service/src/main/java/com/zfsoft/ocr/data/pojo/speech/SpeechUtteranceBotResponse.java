package com.zfsoft.ocr.data.pojo.speech;

import com.zfsoft.ocr.data.pojo.BaseResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @description
 * @author lmz
 * @date 2019-07-16 13:40:02
 * @copyright 上海卓繁信息技术股份有限公司版权所有 .
 */
@ApiModel(description = "闲聊场景响应信息")
public class SpeechUtteranceBotResponse extends BaseResponse {

    @ApiModelProperty(value = "返回的闲聊数据")
    private String data;

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }



}

