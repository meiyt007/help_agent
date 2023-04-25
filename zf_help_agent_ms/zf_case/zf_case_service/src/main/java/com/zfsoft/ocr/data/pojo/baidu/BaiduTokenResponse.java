package com.zfsoft.ocr.data.pojo.baidu;


import com.zfsoft.ocr.data.pojo.BaseResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 百度token响应信息
 * 
 * @author chenbw
 * @date 2019年6月25日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@ApiModel(description = "百度token响应信息")
public class BaiduTokenResponse extends BaseResponse {

    @ApiModelProperty(value = "token字符串")
    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

}
