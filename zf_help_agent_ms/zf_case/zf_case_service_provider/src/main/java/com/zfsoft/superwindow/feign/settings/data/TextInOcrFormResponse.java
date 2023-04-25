package com.zfsoft.superwindow.feign.settings.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/***
 * @Description:textInOcr卡证识别对接表单服务，响应信息
 * @Author:wangyg
 * @Date:2022/6/1
 */
@ApiModel(description = "ocr卡证识别对接表单服务，响应信息")
@Data
public class TextInOcrFormResponse {

    @ApiModelProperty(value = "响应代码")
    private Integer code;

    @ApiModelProperty(value = "响应结果说明")
    private String message;

    @ApiModelProperty(value = "响应结果说明")
    private String data;
}
