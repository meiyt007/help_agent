package com.zfsoft.platform.common.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description //黄埔接口数据响应封装
 * @Author: Wangyh
 * @Date: 2022/7/18 11:00
 */
@ApiModel(description= "数据响应封装")
public class ApiResultSetHP<T> {

    @ApiModelProperty(value = "success", required=true)
    private boolean success;
//    @ApiModelProperty(value = "token", required=false)
//    private String token;;
    @ApiModelProperty(value = "详细错误日志", required=true)
    private String message;

    //该字段只有消息模块一个接口用到，其他接口可以忽略此字段
//    @ApiModelProperty(value = "未读消息数量", required=false)
//    private Integer noReadNum;
    @ApiModelProperty(value = "数据", required=false)
    private T data;

    public ApiResultSetHP(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ApiResultSetHP(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiResultSetHP() {

    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public Integer getNoReadNum() {
//        return noReadNum;
//    }
//
//    public void setNoReadNum(Integer noReadNum) {
//        this.noReadNum = noReadNum;
//    }
}
