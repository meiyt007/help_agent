package com.zfsoft.platform.utils.validate;

/**
 * @description:  验证返回结果的异常
 * @author: wuxx
 * @Date: 2020/9/1 9:06
 **/
public class ResultInfoException extends RuntimeException{

    public ResultInfoException(){
        super();
    }

    public ResultInfoException(String message){
        super(message);
    }

    public ResultInfoException(String message, Throwable cause){
        super(message,cause);
    }
}
