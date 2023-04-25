package com.zfsoft.outer.interWeb.pojo;

import lombok.Data;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2023/3/6 10:17
 */
@Data
public class smsParams {
    private String token;
    private String phone;
    private String message;
    private String displayName;
    private String displayOu;
    private String unit;
    private String msgusage;
}
