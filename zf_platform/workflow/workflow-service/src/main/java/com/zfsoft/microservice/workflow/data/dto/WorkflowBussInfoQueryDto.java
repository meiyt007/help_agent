package com.zfsoft.microservice.workflow.data.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author: kkfan
 * @create: 2021-01-26 10:33:22
 * @description: 流程信息查询实体
 */
@Data
public class WorkflowBussInfoQueryDto extends BasePageDto {
    @Length(max = 32, message = "区划主键最大32位", groups = {PAGE_QUERY.class, QUERY.class})
    private String districtOid;
    @Length(max = 32, message = "机构主键最大32位", groups = {PAGE_QUERY.class, QUERY.class})
    private String organOid;
    @Length(max = 32, message = "流程名称最大50个字", groups = {PAGE_QUERY.class, QUERY.class})
    private String workflowName;
    @Length(max = 32, message = "应用主键最大32位", groups = {PAGE_QUERY.class, QUERY.class})
    private String appOid;
    @Length(max = 32, message = "类型主键最大32位", groups = {PAGE_QUERY.class, QUERY.class})
    private String typeOid;
    @Length(max = 50, message = "流程编码主键最大50位", groups = {PAGE_QUERY.class, QUERY.class})
    private String infoCode;
    //是否发布
    private Integer isPublish;
}
