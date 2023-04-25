package com.zfsoft.service.sxService.data;

import lombok.Data;
import lombok.ToString;

/**
 * 实施清单-环节特殊程序
 */
@Data
@ToString
public class SxServiceLinkSpecial {
    /**
     * 主健
     */
    private Long id;

    /**
     * 业务主键
     */
    private String linkSpecialOid;

    /**
     * T环节主键
     */
    private String serviceLinkOid;

    /**
     * 特别程序类型主键,关联字典表
     */
    private String specialTypeOid;

    /**
     * 当选择其它时，自行输入的名称
     */
    private String specialOtherName;

    /**
     * 特别程序时限单位天
     */
    private Long specialTime;

    /**
     * 特别程序时限类型W：工作日N:自然日
     */
    private String specialTimeType;

    /**
     * 删除状态(0否、1是)
     */
    private Short delFlag;

}