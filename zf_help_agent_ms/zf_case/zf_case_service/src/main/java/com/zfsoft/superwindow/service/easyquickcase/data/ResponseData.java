package com.zfsoft.superwindow.service.easyquickcase.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 返回给前台的通用包装
 *
 * @Auther dusd
 * @Date 2019/10/11 14:56
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@Data
@ApiModel(description = "结果响应类")
public class ResponseData<T> {

    public static final String DEFAULT_SUCCESS_MESSAGE = "请求成功";

    public static final Integer DEFAULT_SUCCESS_CODE = 200;

    public static final Integer DEFAULT_ERROR_CODE = 500;

    /**
     * 请求是否成功
     */
    @ApiModelProperty(value = "请求是否成功")
    private Boolean success;

    /**
     * 响应状态码
     */
    @ApiModelProperty(value = "响应状态码")
    private Integer code;

    /**
     * 响应信息
     */
    @ApiModelProperty(value = "响应信息")
    private String message;

    /**
     * 响应对象
     */
    @ApiModelProperty(value = "响应业务数据")
    private T data;

    public ResponseData() {
    }

    public ResponseData(Boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public ResponseData(Boolean success, Integer code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public ResponseData(ServiceException e) {
        this.success = false;
        this.code = e.getCode();
        this.message = e.getMessage();
    }

    public ResponseData success(T data) {
        this.success = true;
        this.code = DEFAULT_SUCCESS_CODE;
        this.message = DEFAULT_SUCCESS_MESSAGE;
        this.data = data;
        return this;
    }

    public ResponseData error(String message) {
        this.success = false;
        this.code = DEFAULT_ERROR_CODE;
        this.message = message;
        return this;
    }

    public ResponseData error(Integer code,String message) {
        this.success = false;
        this.code = code;
        this.message = message;
        return this;
    }


}
