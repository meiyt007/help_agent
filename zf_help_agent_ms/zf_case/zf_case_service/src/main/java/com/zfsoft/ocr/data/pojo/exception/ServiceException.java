package com.zfsoft.ocr.data.pojo.exception;

/**
 * 业务异常
 *
 * @Auther dusd
 * @Date 2019/10/11 14:53
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@lombok.Data
public class ServiceException extends RuntimeException {
    private Integer code;

    private String errorMessage;

    public ServiceException(Integer code, String errorMessage) {
        super(errorMessage);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public ServiceException(String errorMessage) {
        super(errorMessage);
        this.code = 500;
        this.errorMessage = errorMessage;
    }
}
