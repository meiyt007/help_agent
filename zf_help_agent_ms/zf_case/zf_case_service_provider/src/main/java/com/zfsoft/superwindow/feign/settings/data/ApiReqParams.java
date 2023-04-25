package com.zfsoft.superwindow.feign.settings.data;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: qiaol
 * @Description:
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
     *  办事人类型 法人、 个人
     */
    private String type;

    /**
     *  主体代码（身份证号、统一信用代码）
     */
    private String uniqueCode;
}
