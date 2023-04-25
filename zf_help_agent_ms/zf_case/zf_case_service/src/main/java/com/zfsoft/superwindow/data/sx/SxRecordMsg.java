package com.zfsoft.superwindow.data.sx;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: qiaol
 * @Description:
 * @Date: 2022/5/16 16:56
 */
@Data
@ToString
public class SxRecordMsg implements Serializable {

    private static final long serialVersionUID = 1469779403093993411L;

    /** 成功 */
    public static final String RECORD_STATUS_SUCCESS = "0";

    /** 失败 */
    public static final String RECORD_STATUS_FAILED = "1";

    /** 业务主键 */
    private String recordOid;

    /** 办件主键 */
    private String caseOid;

    /** 预检规则主键 */
    private String rulesOid;

    /** 接口code值 */
    private String interApiCode;

    /** 创建时间 */
    private Date createDate;

    /** 修改时间 */
    private Date modifyDate;

    /** 记录类型 */
    private String ruleType;

    /** 是否成功：0：成功，1：失败 */
    private String status;

    /** 失败原因 */
    private String message;
}
