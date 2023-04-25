package com.zfsoft.ocr.data.pojo.ocr;


import com.zfsoft.ocr.data.pojo.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * ocr自定义模板识别
 * 
 * @author chenbw
 * @date 2019年6月25日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@ApiModel(description = " ocr自定义模板识别，请求信息")
public class OcrCustomTemplateRequest extends BaseRequest {

    @ApiModelProperty(value = "百度模板id", required = false)
    private String templateId;

    @ApiModelProperty(value = "阿里模板id", required = false)
    private String templateIdAli;

    @ApiModelProperty(value = "图片base64字符串", required = true)
    private String imgBase64;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getImgBase64() {
        return this.imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

    /**
     * 获取 @ApiModelProperty(value = "阿里模板id" required = true)
     *
     * @return templateIdAli @ApiModelProperty(value = "阿里模板id" required = true)
     */
    public String getTemplateIdAli() {
        return this.templateIdAli;
    }

    /**
     * 设置 @ApiModelProperty(value = "阿里模板id" required = true)
     *
     * @param templateIdAli @ApiModelProperty(value = "阿里模板id" required = true)
     */
    public void setTemplateIdAli(String templateIdAli) {
        this.templateIdAli = templateIdAli;
    }
}
