package com.zfsoft.ocr.data.pojo.ocr;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * ocr印刷体(带位置信息)识别，识别字符串信息
 * 
 * @author chenbw
 * @date 2019年6月26日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@ApiModel(description = "ocr印刷体识别，识别字符串信息")
public class OcrHandPrintLocationItemResponse {

    @ApiModelProperty(value = "表示定位位置的长方形的宽度")
    private String width;

    @ApiModelProperty(value = "表示定位位置的长方形的高度")
    private String height;

    @ApiModelProperty(value = "表示定位位置的长方形左上顶点的垂直坐标")
    private String top;

    @ApiModelProperty(value = "表示定位位置的长方形左上顶点的水平坐标")
    private String left;

    @ApiModelProperty(value = "识别内容")
    private String words;

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

}