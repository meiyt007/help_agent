package com.zfsoft.ocr.data.pojo.ocr;


import com.zfsoft.ocr.data.pojo.BaseResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


/**
 * ocr印刷体识别，响应信息
 * 
 * @author chenbw
 * @date 2019年6月26日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@ApiModel(description = "ocr印刷体识别，响应信息")
public class OcrHandPrintResponse extends BaseResponse {

    /**
     * 唯一的log id，用于问题定位
     */
    @ApiModelProperty(value = "唯一的log id，用于问题定位")
    private String logId;

    /**
     * 识别内容列表
     */
    @ApiModelProperty(value = "识别内容列表")
    private List<OcrHandPrintItemResponse> ocrHandPrintItemResponseList;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public List<OcrHandPrintItemResponse> getOcrHandPrintItemResponseList() {
        return ocrHandPrintItemResponseList;
    }

    public void setOcrHandPrintItemResponseList(List<OcrHandPrintItemResponse> ocrHandPrintItemResponseList) {
        this.ocrHandPrintItemResponseList = ocrHandPrintItemResponseList;
    }

}
