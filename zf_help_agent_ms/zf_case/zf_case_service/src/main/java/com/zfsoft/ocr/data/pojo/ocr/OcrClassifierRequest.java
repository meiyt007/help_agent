package com.zfsoft.ocr.data.pojo.ocr;


import com.zfsoft.ocr.data.pojo.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * ocr分类器识别请求信息
 * 
 * @author chenbw
 * @date 2020年7月16日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@ApiModel(description = " ocr分类器识别，请求信息")
public class OcrClassifierRequest extends BaseRequest {


    @ApiModelProperty(value = "分类器id", required = true)
    private Integer classifierId;

    @ApiModelProperty(value = "图片base64字符串", required = true)
    private String imgBase64;




    /**
     * 获取 @ApiModelProperty(value = "图片base64字符串" required = true)
     *
     * @return imgBase64 @ApiModelProperty(value = "图片base64字符串" required = true)
     */
    public String getImgBase64() {
        return this.imgBase64;
    }

    /**
     * 设置 @ApiModelProperty(value = "图片base64字符串" required = true)
     *
     * @param imgBase64 @ApiModelProperty(value = "图片base64字符串" required = true)
     */
    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

    /**
     * 获取 @ApiModelProperty(value = "分类器id" required = true)
     *
     * @return classifierId @ApiModelProperty(value = "分类器id" required = true)
     */
    public Integer getClassifierId() {
        return this.classifierId;
    }

    /**
     * 设置 @ApiModelProperty(value = "分类器id" required = true)
     *
     * @param classifierId @ApiModelProperty(value = "分类器id" required = true)
     */
    public void setClassifierId(Integer classifierId) {
        this.classifierId = classifierId;
    }
}
