package com.zfsoft.ocr.data.pojo.ocr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * ocr手写体识别，识别字符串所在位置
 *
 * @Auther gaolh
 * @Date 2019-7-4 15:15:09
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@ApiModel(description = "识别结果位置")
public class OcrHandWrittenItemcoordResponse {

    @ApiModelProperty(value = "识别结果X坐标")
    private Integer x;
    @ApiModelProperty(value = "识别结果Y坐标")
    private Integer y;
    @ApiModelProperty(value = "识别结果宽度")
    private Integer width;
    @ApiModelProperty(value = "识别结果高度")
    private Integer height;

    /**
     * 获取 @ApiModelProperty(value = "识别结果X坐标")
     *
     * @return x @ApiModelProperty(value = "识别结果X坐标")
     */
    public Integer getX() {
        return this.x;
    }

    /**
     * 设置 @ApiModelProperty(value = "识别结果X坐标")
     *
     * @param x @ApiModelProperty(value = "识别结果X坐标")
     */
    public void setX(Integer x) {
        this.x = x;
    }

    /**
     * 获取 @ApiModelProperty(value = "识别结果Y坐标")
     *
     * @return y @ApiModelProperty(value = "识别结果Y坐标")
     */
    public Integer getY() {
        return this.y;
    }

    /**
     * 设置 @ApiModelProperty(value = "识别结果Y坐标")
     *
     * @param y @ApiModelProperty(value = "识别结果Y坐标")
     */
    public void setY(Integer y) {
        this.y = y;
    }

    /**
     * 获取 @ApiModelProperty(value = "识别结果宽度")
     *
     * @return width @ApiModelProperty(value = "识别结果宽度")
     */
    public Integer getWidth() {
        return this.width;
    }

    /**
     * 设置 @ApiModelProperty(value = "识别结果宽度")
     *
     * @param width @ApiModelProperty(value = "识别结果宽度")
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * 获取 @ApiModelProperty(value = "识别结果高度")
     *
     * @return height @ApiModelProperty(value = "识别结果高度")
     */
    public Integer getHeight() {
        return this.height;
    }

    /**
     * 设置 @ApiModelProperty(value = "识别结果高度")
     *
     * @param height @ApiModelProperty(value = "识别结果高度")
     */
    public void setHeight(Integer height) {
        this.height = height;
    }
}
