package com.zfsoft.outer.inter.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //18.4获取工作流列表响应类
 * @Author: Wangyh
 * @Date: 2022/12/7 11:14
 */
@Data
@ToString
public class ResponseWorkflowCatVo {
    /**
     * 工作流ID
     */
    private String workflowId;
    /**
     * 所属部门ID
     */
    private String organNodeId;
    /**
     * 所属部门名称
     */
    private String organName;
    /**
     * 事项ID
     */
    private String itemId;
    /**
     * 事项名称
     */
    private String itemName;
    /**
     * 情形ID
     */
    private String statusId;
    /**
     * 情形名称
     */
    private String statusName;
    /**
     * 描述
     */
    private String workflowDetail;
    /**
     * 事项库名称
     */
    private String itemDbName;


}
