package com.zfsoft.ha.outer.scanning.pojo;
import com.github.pagehelper.Page;
/**
 * restfull API method return object definition
 * @Auther: lijun
 * @Date: 2020-08-24 14:56
 */
public class ApiResultSet<T> {
    /**
     * 执行成功代码
     */
    public final static int SUCCESS = 200;

    /**
     * 未知错误代码
     */
    public final static int UNKNOWN_ERROR = 500;

    /**
     * 参数验证错误代码
     */
    public final static int PARAM_VALID_ERROR= 501;

    /**
     * 脏数据错误
     */
    public final static int DIRTY_DATA_ERROR= 502;

    /**
     * 提示语
     */
    public final static int DIRTY_DATA_TITLE= 503;

    public ApiResultSet(){
        this(SUCCESS);
    }

    public static ApiResultSet ok() {
        return new ApiResultSet(SUCCESS, "调用成功", null);
    }


    public static ApiResultSet ok(String message) {
        return new ApiResultSet(SUCCESS, message, null);
    }

    public static <T> ApiResultSet ok(T data) {
        if (data instanceof Page) {
            PageResult<T> pageResult = new PageResult<>(((Page) data).getPageNum(), ((Page) data).getPageSize(), ((Page) data).getTotal());
            pageResult.setData(((Page) data).getResult());
            return new ApiResultSet(pageResult);
        }
        return new ApiResultSet(data);
    }
    public static <T> ApiResultSet ok(String message,T data) {
        if (data instanceof Page) {
            PageResult<T> pageResult = new PageResult<>(((Page) data).getPageNum(), ((Page) data).getPageSize(), ((Page) data).getTotal());
            pageResult.setData(((Page) data).getResult());
            return new ApiResultSet(message,pageResult);
        }
        return new ApiResultSet(message,data);
    }



    public ApiResultSet(int code){
        this(code,null,null);
    }

    public ApiResultSet(int code, String message, String stackTrace){
        this.setCode(code);
        this.setMessage(message);
        this.setStackTrace(stackTrace);
    }
    public ApiResultSet(int code, String message){
        this.setCode(code);
        this.setMessage(message);
    }
    public ApiResultSet(T data){
        this.code = SUCCESS;
        this.data = data;
    }

    public ApiResultSet(String message, T data){
        this.code = SUCCESS;
        this.message = message;
        this.data = data;
    }

    private int code;

    private String message;

    private String stackTrace;

    private T data;

    private String time;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String toFullMessage(){
        StringBuilder sb = new StringBuilder();
        sb.append("调用远程服务出错，错误代码："+ code);
        sb.append("\r\n");
        sb.append(message);
        sb.append("\r\n");
        sb.append(stackTrace);
        return sb.toString();
    }
}
