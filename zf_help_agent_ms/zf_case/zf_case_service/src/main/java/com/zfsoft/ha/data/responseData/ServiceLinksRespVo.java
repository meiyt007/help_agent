package com.zfsoft.ha.data.responseData;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //提供万行的，办理环节响应类
 * @Author: Wangyh
 * @Date: 2022/9/26 19:03
 */
@Data
@ToString
public class ServiceLinksRespVo {
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
    private String  postDuty;
    /**
     * 办件人员主键集合
     */
    private String  handleUserOids;
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
    private String handleTime;
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
    private String specialFlag;
    /**
     * 排序
     */
    private String linkSort;
    /**
     * 特别程序时限
     */
    private String specialTime;
    /**
     * 特别程序法定依据
     */
    private String specialBasis;
}
