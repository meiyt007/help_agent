package com.zfsoft.platform.utils.validate;

/**
 * @Auther: lijun@sstcsoft.com
 * @Date: 2019/9/11 14:25
 */
public class ParamValidException extends RuntimeException{

    public ParamValidException(){
        super();
    }

    public ParamValidException(String message){
        super(message);
    }

    public ParamValidException(String message, Throwable cause){
        super(message,cause);
    }
}
