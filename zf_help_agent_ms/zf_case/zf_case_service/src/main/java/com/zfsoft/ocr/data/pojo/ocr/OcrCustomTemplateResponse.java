package com.zfsoft.ocr.data.pojo.ocr;

import com.zfsoft.ocr.data.pojo.BaseResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


/**
 * ocr自定义模板识别，响应信息 （卡证也用这个）
 * 
 * @author chenbw
 * @date 2019年6月25日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@ApiModel(description = "ocr自定义模板识别，响应信息")
public class OcrCustomTemplateResponse extends BaseResponse {
    
    /**
     * 识别区块内容列表
     */
    @ApiModelProperty(value = "识别区块内容列表")
    private List<OcrCustomTemplateItemResponse> ocrCustomTemplateItemResponseList;

    public List<OcrCustomTemplateItemResponse> getOcrCustomTemplateItemResponseList() {
        return ocrCustomTemplateItemResponseList;
    }

    public void setOcrCustomTemplateItemResponseList(
            List<OcrCustomTemplateItemResponse> ocrCustomTemplateItemResponseList) {
        this.ocrCustomTemplateItemResponseList = ocrCustomTemplateItemResponseList;
    }

}
