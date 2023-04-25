package com.zfsoft.single.data.yxpz;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: qiaol
 * @Description: 通用接口实体
 * @Date: 2022/5/16 16:56
 */
@Data
@ToString
public class ApiReqParams implements Serializable {

    private static final long serialVersionUID = 1469779403093993411L;

    /**
     *  事项oid
     */
    private String sxServiceOid;

    /**
     *  办事人类型 法人、 个人 （0个人 1法人）
     */
    private String type;

    /**
     *  主体代码（身份证号、统一信用代码）
     */
    private String uniqueCode;

    /**
     *  调用类型 1-内部feign调用
     */
    private String callType = "1";

    /**
     *  接口id
     */
    private String interId;

    /**
     *  办件oid
     */
    private String caseOid;
}
