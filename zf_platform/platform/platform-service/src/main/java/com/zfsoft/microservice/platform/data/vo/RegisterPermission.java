package com.zfsoft.microservice.platform.data.vo;


import java.io.Serializable;


/**
 * 客户现场注册权限
 * @author WangJinye
 * @date 2016-11-18
 */
public class RegisterPermission implements Serializable {

    private static final long serialVersionUID = 9147265534530468277L;
    /**权限代码*/
    private String code;
    /**是否授权*/
    private String isAuth;
    
    
    public RegisterPermission(String code, String isAuth) {
        super();
        this.code = code;
        this.isAuth = isAuth;
    }

    public RegisterPermission() {
        super();
    }

    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getIsAuth() {
        return isAuth;
    }
    
    public void setIsAuth(String isAuth) {
        this.isAuth = isAuth;
    }
    
    
}
