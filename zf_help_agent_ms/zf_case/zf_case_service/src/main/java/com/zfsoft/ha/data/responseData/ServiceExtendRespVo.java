package com.zfsoft.ha.data.responseData;

import com.zfsoft.service.sxSys.data.SxSysAtta;
import lombok.Data;
import lombok.ToString;

/**
 * @Description //提供万行的，事项扩展响应类
 * @Author: Wangyh
 * @Date: 2022/9/26 19:25
 */
@Data
@ToString
public class ServiceExtendRespVo {
    /**
     * 扩展信息业务主键
     */
    private String extendOid;
    /**
     * 事项业务主键
     */
    private String serviceOid;
    /**
     * 审批内容
     */
    private String exerciseContent;
    /**
     * 法定时限
     */
    private Long legalLimit;
    /**
     * 法定时限类型
     */
    private String legalLimitType;
    /**
     * 法定时限类型名称
     */
    private String legalLimitTypeName;
    /**
     * 承诺时限
     */
    private Long promiseLimit;
    /**
     * 承诺时限类型
     */
    private String promiseLimitType;
    /**
     * 办理流程图
     */
    private String handleFlow;
    /**
     * 结果名称
     */
    private String resultName;
    /**
     * 结果样本
     */
    private String resultSampleAddr;
    /**
     * 结果样本类型0证照 1批文 2其它
     */
    private Short resultSampleType;
    /**
     * 审查标准
     */
    private String censorStandard;
    /**
     * 权限划分标准
     */
    private String dividStandard;
    /**
     * 办理时间
     */
    private String hanleTimeRange;
}
