package com.zfsoft.service.sxService.data;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 实施清单 - 受理条件
 */
@Data
@ToString
public class SxAcceptCondition {
    /**
     * 主健
     */
    private Long id;

    /**
     * 业务主键
     */
    private String conditionOid;

    /**
     * 是否删除(0否、1是)
     */
    private Short delFlag;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * T所属事项
     */
    private String serviceOid;

    /**
     * 所属机构
     */
    private String organOid;

    /**
     * 排序号
     */
    private Long sort;

    /**
     * 受理内容
     */
    private String conditionText;

}