package com.zfsoft.ha.data.requestData;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //登录接口请求实体类
 * @Author: Wangyh
 * @Date: 2022/9/29 14:37
 */
@Data
@ToString
public class HaLoginRequestDat {
    private String account;
    private String pcode;
    private String checkCode;
    private String haType;
}
