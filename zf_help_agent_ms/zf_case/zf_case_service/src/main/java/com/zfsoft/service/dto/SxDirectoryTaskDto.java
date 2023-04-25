package com.zfsoft.service.dto;

import lombok.Data;
import lombok.ToString;

/*
 * @Description:目录清单待办任务
 * @Author: wangxl
 * @Date: 2021/2/23
 **/
@Data
@ToString
public class SxDirectoryTaskDto {

    private String id;
//    /**
//     * 任务定义key
//     */
//    private String taskDefinitionKey;
     private String auditOid;
    /**
     * 名称
     */
    private String name;
    /**
     * 实例化主键
     */
    private String processInstanceId;
    /**
     * 进程id
     */
    private String executionId;
    /**
     * 定义唯一标识
     */
    private String processDefinitionId;
    private String linkOid;
    private String linkName;
    private String actionName;
    private String typeCode;
//    private String rejectTaskId;
//    private String fallbackTaskId;

    /**
     * 目录清单业务主健
     */
    private String directoryOid;

    /* 目录清单名称  */
    private String directoryName;

    /* 基本编码  */
    private String basicCode;

    /* 目录父类OID  */
    private String directoryParentOid;

    /* 事项类型oid  */
    private String serviceTypeOid;

    /* 事项类型name  */
    private String serviceTypeName;

    /* 事项状态  */
    private Integer directoryStatus;

    /* 是否主项  */
    private Integer mainItemFlag;
    /* 数据来源*/
    private Integer infoSource;
    /* 是否存在子项*/
    private Integer existChildItem;

    private Integer operateType;

    private String levelName;
}
