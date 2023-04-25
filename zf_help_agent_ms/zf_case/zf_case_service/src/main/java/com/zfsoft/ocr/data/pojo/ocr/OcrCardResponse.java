package com.zfsoft.ocr.data.pojo.ocr;

import com.zfsoft.ocr.data.pojo.BaseResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


/***
* @Description:  ocr卡证识别，响应信息
* @Author:liangss
* @Date:2021/10/29
* @Param:
*/
@ApiModel(description = "ocr卡证识别，响应信息")
public class OcrCardResponse extends BaseResponse {
    
    /**
     * 识别区块内容列表
     */
    @ApiModelProperty(value = "识别区块内容列表")
    private List<OcrCardItemResponse> ocrCardItemResponseList;

    public List<OcrCardItemResponse> getOcrCardItemResponseList() {
        return ocrCardItemResponseList;
    }

    public void setOcrCardItemResponseList(
            List<OcrCardItemResponse> ocrCardItemResponseList) {
        this.ocrCardItemResponseList = ocrCardItemResponseList;
    }

}
