package com.zfsoft.ocr.data.pojo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 基础响应信息
 *
 * @Auther dusd
 * @Date 2019/6/22 11:37
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@lombok.Data
public class BaseResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "响应代码",position = -1000)
    private Integer code;
    @ApiModelProperty(value = "响应结果说明",position = -999)
    private String message;

}
