package com.zfsoft.superwindow.service.interapi.data;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 电子证照查询实体
 */
@Data
@ToString
public class ApiResultElecQuery implements Serializable {
    /**
     * 证照类型名称
     */
    private String certificateType;
    /**
     * 证照名称
     */
    private String certificateName;
    /**
     * 持证主体
     */
    private String certificateHolder;
    /**
     * 持证主体代码
     */
    private String certificateHolderCode;
    /**
     * 持证主体代码类型
     */
    private String certificateHolderType;
    /**
     * 证照颁发机构
     */
    private String issueDept;
    /**
     * 证照编号
     */
    private String certificateNumber;
    /**
     * 证照颁发机构代码
     */
    private String issueDeptCode;
    /**
     * 证照颁发日期
     */
    private String issueDate;
    /**
     * 证照有效期起始
     */
    private String certificateValidateStart;
    /**
     * 证照有效期截止
     */
    private String certificateValidateEnd;
    /**
     * 数字签名
     */
    private String digitalSign;
    /**
     * 照面信息集合
     */
    private List<Map<String,String>> surface;

}
