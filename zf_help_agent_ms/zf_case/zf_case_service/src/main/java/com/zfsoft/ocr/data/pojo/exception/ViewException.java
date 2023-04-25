package com.zfsoft.ocr.data.pojo.exception;

/**
 * 跳转页面异常处理 非ResponseBody
 *
 * @Auther dusd
 * @Date 2019/10/12 14:10
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@lombok.Data
public class ViewException extends RuntimeException {
    private Integer code;

    private String errorMessage;

    public ViewException(Integer code, String errorMessage) {
        super(errorMessage);
        this.code = code;
        this.errorMessage = errorMessage;
    }
}
