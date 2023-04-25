package com.zfsoft.ocr.data.pojo.ocr;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * ocr印刷体识别，识别字符串信息
 * 
 * @author chenbw
 * @date 2019年6月26日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@ApiModel(description = "ocr印刷体识别，识别字符串信息")
public class OcrHandPrintItemResponse {

    @ApiModelProperty(value = "识别内容")
    private String words;

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

}