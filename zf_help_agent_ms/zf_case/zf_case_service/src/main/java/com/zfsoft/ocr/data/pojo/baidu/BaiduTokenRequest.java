package com.zfsoft.ocr.data.pojo.baidu;

import com.zfsoft.ocr.data.pojo.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 百度token请求信息
 * 
 * @author chenbw
 * @date 2019年6月25日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@ApiModel(description = "百度token请求信息")
public class BaiduTokenRequest extends BaseRequest {

    /**
     * 应用的API Key
     */
    @ApiModelProperty(value = "识别应用中的API Key")
    private String clientId;

    /**
     * 应用的Secret Key
     */
    @ApiModelProperty(value = "识别应用中的Secret Key")
    private String clientSecret;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

}
