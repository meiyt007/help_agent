package com.zfsoft.ha.data;

import lombok.Data;

import java.util.Date;

/**
* Description: 办件快递实体类

* @author zhaobf
* date: 2023/4/7 15:15
* @copyright 版权由上海卓繁信息技术股份有限公司拥有
*/
@Data
public class HaCaseExpress {

    /**
     * 主键
     */
    private Long id;


    /**
     * 办件id
     */
    private String qlCaseId;


    /**
     * 快递编号
     */
    private String caseExpressNum;


    private String createBy;


    private Date createDate;


    private String updateBy;


    private Date updateDate;


}