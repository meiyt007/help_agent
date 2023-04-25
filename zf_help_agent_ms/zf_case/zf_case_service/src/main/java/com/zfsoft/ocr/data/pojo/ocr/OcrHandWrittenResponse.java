package com.zfsoft.ocr.data.pojo.ocr;


import com.zfsoft.ocr.data.pojo.BaseResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * ocr手写体识别，响应信息
 *
 * @Auther dusd
 * @Date 2019/6/22 17:29
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@ApiModel(description= "ocr手写体识别，响应信息")
public class OcrHandWrittenResponse extends BaseResponse {

    @ApiModelProperty(value = "识别内容列表")
    private List<OcrHandWrittenItemResponse> ocrHandWrittenItemResponseList;

    /**
     * 获取 @ApiModelProperty(value = "识别内容列表")
     *
     * @return ocrHandWrittenItemResponseList @ApiModelProperty(value = "识别内容列表")
     */
    public List<OcrHandWrittenItemResponse> getOcrHandWrittenItemResponseList() {
        return this.ocrHandWrittenItemResponseList;
    }

    /**
     * 设置 @ApiModelProperty(value = "识别内容列表")
     *
     * @param ocrHandWrittenItemResponseList @ApiModelProperty(value = "识别内容列表")
     */
    public void setOcrHandWrittenItemResponseList(List<OcrHandWrittenItemResponse> ocrHandWrittenItemResponseList) {
        this.ocrHandWrittenItemResponseList = ocrHandWrittenItemResponseList;
    }
}
