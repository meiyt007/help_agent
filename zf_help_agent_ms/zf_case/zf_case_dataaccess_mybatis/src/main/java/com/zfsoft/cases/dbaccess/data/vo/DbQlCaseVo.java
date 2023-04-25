package com.zfsoft.cases.dbaccess.data.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 已办查询参数表
 *
 * @author wangwg
 * @date  2020-11-4
 */
@Data
@ToString
public class DbQlCaseVo {
    /**
     *办件主键
     */
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
     * 申请人证件号码
     */
    private String credentialNumber;

    /**
     *事项类型
     */
    private String serviceType;

    /**
     *申请人
     */
    private Date createDate;

    private Integer caseStatus;

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

    //退件状态
    private String returnStatus;
    //告知状态
    private String informStatus;
    //数据权限
    private List<Integer> dataAuthor;

    //权限过滤的用户oid集合
    private List<String> filterUserOidList;

    //所有授权的事项主键
    private List serviceOids;

    private String serviceName;

    private String netUserId;

    // 是否已预填
    private int prepFill;


    /**
     *万达-发证方式：自取，物流，无结果物
     */
    private String  certWay;
    /**
     * 万达-配送公司，取值为EMS、顺丰速递、其他
     */
    private String  expressCompany;

}
