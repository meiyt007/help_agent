package com.zfsoft.ocr.data.pojo.ocr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * ocr手写体识别，识别字符串信息
 *
 * @Auther dusd
 * @Date 2019/6/22 17:29
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@ApiModel(description = "ocr手写体识别，识别字符详细信息，包含每个文字的预测值")
public class OcrHandWrittenItemWordsResponse {

    @ApiModelProperty(value = "单个文字")
    private String character;

    @ApiModelProperty(value = "文字相似度")
    private Double confidence;

    /**
     * 获取 @ApiModelProperty(value = "单个文字")
     *
     * @return character @ApiModelProperty(value = "单个文字")
     */
    public String getCharacter() {
        return this.character;
    }

    /**
     * 设置 @ApiModelProperty(value = "单个文字")
     *
     * @param character @ApiModelProperty(value = "单个文字")
     */
    public void setCharacter(String character) {
        this.character = character;
    }

    /**
     * 获取 @ApiModelProperty(value = "文字相似度")
     *
     * @return confidence @ApiModelProperty(value = "文字相似度")
     */
    public Double getConfidence() {
        return this.confidence;
    }

    /**
     * 设置 @ApiModelProperty(value = "文字相似度")
     *
     * @param confidence @ApiModelProperty(value = "文字相似度")
     */
    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }
}
