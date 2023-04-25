package com.zfsoft.ocr.data.pojo.ocr;

import com.zfsoft.ocr.data.pojo.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * ocr识别图片数量，请求信息
 *
 * @Auther dusd
 * @Date 2019/6/22 13:17
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@ApiModel(description= "ocr识别图片数量请求参数")
public class OcrSealCountRequest extends BaseRequest {

    @ApiModelProperty(value = "图片base64字符串",required = true)
    private String imgBase64;

    /**
     * 获取 @ApiModelProperty(value = "图片base64字符串")
     *
     * @return imgBase64 @ApiModelProperty(value = "图片base64字符串")
     */
    public String getImgBase64() {
        return this.imgBase64;
    }

    /**
     * 设置 @ApiModelProperty(value = "图片base64字符串")
     *
     * @param imgBase64 @ApiModelProperty(value = "图片base64字符串")
     */
    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }
}
