package com.zfsoft.superwindow.data.wgpj;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 办件签名记录实体类
 *
 * @author wangwg
 * @date 2021-08-16
 */
@Data
@ToString
public class CaseSignRecord {

    private Long id;
    /**
     * 签名主键
     */
    private String signOid;
    /**
     * 办件主键
     */
    private String caseOid;
    /**
     * 申请人证件号
     */
    private String applyCarno;
    /**
     * 签名路径
     */
    private String signUrl;
    /**
     * 签名时间
     */
    private Date createDate;

}
