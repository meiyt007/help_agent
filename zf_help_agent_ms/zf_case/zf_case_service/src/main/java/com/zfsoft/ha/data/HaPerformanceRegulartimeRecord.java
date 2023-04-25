package com.zfsoft.ha.data;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description 绩效常规时长记录表实体类
 * @author dingsn
 * @date 2022/10/31  10:30
 */
@Data
@ToString
public class HaPerformanceRegulartimeRecord {
    /**
     * 新增，验证规则组
     */
    public interface INSERT_GROUP{};

    /** 主键 */
    @NotNull(message = "id不能为空",groups = {HaPerformanceRegulartimeRecord.INSERT_GROUP.class})
    private Long id;

    /** 帮办人员id */
    private Long workUserOid;

    /** 办事人姓名 */
    private String caseWorkerName;

    /** 办事人所属企业名称 */
    private String companyName;

    /** 办理事项id */
    private String serviceOid;

    /** 办理事项名称 */
    private String serviceName;

    /** 办件Oid */
    private String caseOid;

    /** 办件编号 */
    private String caseNumber;

    /**
     * 服务开始时间
     */
    private Date serviceBeginTime;

    /**
     * 服务结束时间;最终的服务结束时间，如果有转派，那就是转派人员的结束时间
     */
    private Date serviceEndTime;

    /**
     * 服务时长;单位：分钟
     */
    private Integer serviceDuration;
    /**
     * 服务人数
     */
    private String servicePeopleNum;
    /**
     * 帮办人员管理系数
     */
    private String manageFactor;

    /** 备注 */
    private String memo;

    /** 删除状态;1-未删除，2-已删除 */
    private String deleteStatus;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    private Date createDate;

    /** 更新人 */
    private String updateBy;

    /** 更新时间 */
    private Date updateDate;
}
