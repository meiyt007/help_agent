package com.zfsoft.ocr.data.pojo.ocr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * ocr手写体识别，识别字符串信息
 *
 * @Auther dusd
 * @Date 2019/6/22 17:29
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@ApiModel(description = "ocr手写体识别，识别字符串信息")
public class OcrHandWrittenItemResponse {

    @ApiModelProperty(value = "识别内容")
    private String content;

    @ApiModelProperty(value = "识别内容所在位置")
    private OcrHandWrittenItemcoordResponse itemcoord;

    @ApiModelProperty(value = "识别内容单个文字的相似度")
    private List<OcrHandWrittenItemWordsResponse> words;


    /**
     * 获取 @ApiModelProperty(value = "识别内容")
     *
     * @return content @ApiModelProperty(value = "识别内容")
     */
    public String getContent() {
        return this.content;
    }

    /**
     * 设置 @ApiModelProperty(value = "识别内容")
     *
     * @param content @ApiModelProperty(value = "识别内容")
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取 @ApiModelProperty(value = "识别内容所在位置")
     *
     * @return itemcoord @ApiModelProperty(value = "识别内容所在位置")
     */
    public OcrHandWrittenItemcoordResponse getItemcoord() {
        return this.itemcoord;
    }

    /**
     * 设置 @ApiModelProperty(value = "识别内容所在位置")
     *
     * @param itemcoord @ApiModelProperty(value = "识别内容所在位置")
     */
    public void setItemcoord(OcrHandWrittenItemcoordResponse itemcoord) {
        this.itemcoord = itemcoord;
    }

    /**
     * 获取 @ApiModelProperty(value = "识别内容单个文字的相似度")
     *
     * @return words @ApiModelProperty(value = "识别内容单个文字的相似度")
     */
    public List<OcrHandWrittenItemWordsResponse> getWords() {
        return this.words;
    }

    /**
     * 设置 @ApiModelProperty(value = "识别内容单个文字的相似度")
     *
     * @param words @ApiModelProperty(value = "识别内容单个文字的相似度")
     */
    public void setWords(List<OcrHandWrittenItemWordsResponse> words) {
        this.words = words;
    }
}
