package com.zfsoft.ocr.data.pojo.ocr;

import com.zfsoft.ocr.data.pojo.BaseResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * ocr识别图片数量响应信息
 *
 * @Auther dusd
 * @Date 2019/6/22 13:27
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@ApiModel(description= "ocr识别图片数量响应信息")
public class OcrSealCountResponse extends BaseResponse {

    @ApiModelProperty(value = "印章数量")
    private Integer count;

    /**
     * 获取 @ApiModelProperty(value = "印章数量")
     *
     * @return count @ApiModelProperty(value = "印章数量")
     */
    public Integer getCount() {
        return this.count;
    }

    /**
     * 设置 @ApiModelProperty(value = "印章数量")
     *
     * @param count @ApiModelProperty(value = "印章数量")
     */
    public void setCount(Integer count) {
        this.count = count;
    }
}
