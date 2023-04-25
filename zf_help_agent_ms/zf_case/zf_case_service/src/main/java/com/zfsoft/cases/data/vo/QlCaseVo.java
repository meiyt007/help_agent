package com.zfsoft.cases.data.vo;

import com.zfsoft.microservice.platform.data.sys.SysHoliday;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 已办查询参数表
 *
 * @author wangwg
 * @date  2020-11-4
 */
@Data
public class QlCaseVo {

    private String caseOid;

    /**
     *办件编号
     */
    private String caseNumber;

    /**
     *机构id
     */
    private String organOid;

    /**
     *申请项目名称
     */
    private String projectName;

    /**
     *申请人
     */
    private String applyUserName;

    /**
     *事项类型
     */
    private String serviceType;

    /**
     * 办件状态
     */
    private Integer  caseStatus;
    /**
     * 来源
     */
    private Integer sourceType;
    /**
     * 应用
     */
    private Integer sourceApp;

    /**
     *登记开始时间
     */
    private String startDate;

    /**
     *登记结束时间
     */
    private String endDate;

    /**
     * 超时状态
     */
    private String overTime;
    /**
     * 登记人
     */
    private String registerUser;

    private List<SysHoliday> holidays;

    //退件状态
    private String returnStatus;
    //告知状态
    private String informStatus;

    //所有授权的事项主键
    private List serviceOids;

    private String credentialNumber;

    private String serviceName;

    // 网络用户ID
    private String netUserId;
}
