package com.zfsoft.ocr.data.pojo.ocr;


import com.zfsoft.ocr.data.pojo.BaseResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


/**
 * ocr分类器识别，响应信息
 *
 * @author chenbw
 * @date 2020年7月17日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@ApiModel(description = "ocr分类器识别，响应信息")
public class OcrClassifierResponse extends BaseResponse {

    /**
     * 分类结果对应的模板id
     */
    @ApiModelProperty(value = "分类结果对应的模板id")
    private String templateId;

    /**
     * 识别区块内容列表
     */
    @ApiModelProperty(value = "识别区块内容列表")
    private List <OcrCustomTemplateItemResponse> ocrCustomTemplateItemResponseList;


    /**
     * 获取 分类结果对应的模板id
     *
     * @return templateId 分类结果对应的模板id
     */
    public String getTemplateId() {
        return this.templateId;
    }

    /**
     * 设置 分类结果对应的模板id
     *
     * @param templateId 分类结果对应的模板id
     */
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    /**
     * 获取 识别区块内容列表
     *
     * @return ocrCustomTemplateItemResponseList 识别区块内容列表
     */
    public List <OcrCustomTemplateItemResponse> getOcrCustomTemplateItemResponseList() {
        return this.ocrCustomTemplateItemResponseList;
    }

    /**
     * 设置 识别区块内容列表
     *
     * @param ocrCustomTemplateItemResponseList 识别区块内容列表
     */
    public void setOcrCustomTemplateItemResponseList(List <OcrCustomTemplateItemResponse> ocrCustomTemplateItemResponseList) {
        this.ocrCustomTemplateItemResponseList = ocrCustomTemplateItemResponseList;
    }
}
