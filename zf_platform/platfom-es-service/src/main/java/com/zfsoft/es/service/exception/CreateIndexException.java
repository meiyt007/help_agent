package com.zfsoft.es.service.exception;

/**
 * @author: kkfan
 * @create: 2021-01-15 14:09:58
 * @description: 索引创建异常
 */
public class CreateIndexException extends RuntimeException {
    public CreateIndexException(String errorCode, Throwable message) {
        super(errorCode, message);
    }
    public CreateIndexException(String message) {
        super(message);
    }
    public CreateIndexException() {
        super();
    }
}
