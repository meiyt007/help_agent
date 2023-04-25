package com.zfsoft.service.sxService.data;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 实施清单-办理环节
 */
@Data
@ToString
public class SxServiceLink {
    /**
     * 主健
     */
    private Long id;

    /**
     * 业务主键
     */
    private String serviceLinkOid;

    /**
     * 所属事项
     */
    private String serviceOid;

    /**
     * 环节名称
     */
    private String linkName;

    /**
     * 所属办理环节
     */
    private String linkOid;
    /**
     * 所属办理岗位
     */
    private String postOids;

    /**
     * 所属办理岗位名称集合
     */
    private String postNames;

    /**
     * 岗位职责
     */
    private String postDuty;

    /**
     * 办件人员主键集合
     */
    private String handleUserOids;

    /**
     * 办件人员名称集合
     */
    private String handleUserNames;

    /**
     * 办件人员电话集合
     */
    private String handleUserPhone;

    /**
     * 办理时限
     */
    private BigDecimal handleTime;

    /**
     * 时限单位
     */
    private String timeUnit;
    /**
     * 人员资质集合
     */
    private String handleUserQualified;

    /**
     * 说明
     */
    private String serviceDescribe;

    /**
     * 是否存在特别程序(0否、1是)
     */
    private Short specialFlag;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 删除状态(0否、1是)
     */
    private Short delFlag;

    /**
     * 排序
     */
    private String linkSort;

    /**
     * 特别程序时限
     */
    private Integer specialTime;
    /**
     * 特别程序法定依据
     */
    private String specialBasis;

    List<SxServiceLinkSpecial> serviceLinkSpecials = new ArrayList<SxServiceLinkSpecial>();
}
