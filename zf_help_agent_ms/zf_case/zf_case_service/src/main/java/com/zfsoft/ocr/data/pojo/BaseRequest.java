package com.zfsoft.ocr.data.pojo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 基础请求信息
 *
 * @Auther dusd
 * @Date 2019/6/22 13:23
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@lombok.Data
public class BaseRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "身份验证标识",required = true,position = -1000)
    private String token;

}
