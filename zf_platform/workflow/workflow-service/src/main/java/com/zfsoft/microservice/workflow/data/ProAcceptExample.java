package com.zfsoft.microservice.workflow.data;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 项目受理实例
 */
@Data
@ToString
public class ProAcceptExample {
    /**
     * 新增验证规则组
     */
    public interface INSERT_GROUP{}

    /**
     * 更新验证规则组
     */
    public interface UPDATE_GROUP{}

    /* 主键 */
    private Long id;

    /* 业务主键 */
    @NotNull(message = "业务主键不能为空",groups = {ProAcceptExample.UPDATE_GROUP.class})
    private String acceptOid;

    /**
     * 办件编号
     */
    private String projectNo;

    /** 项目名称 */
    private String projectName;

    /**
     * 流程模型标识
     */
    private String infoOid;

    /**
     * 流程实例标识
     */
    private String processInstanceId;

    /**
     * 申请人
     */
    private String applyerName;


    /** 创建时间 */
    private Date createDate;

    /** 删除状态 */
    private Integer isDelete;

}
