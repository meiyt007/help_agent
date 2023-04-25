package com.zfsoft.ha.data.TripartiteVo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description //获取工作流列表Vo
 * @Author: Wangyh
 * @Date: 2022/12/7 10:41
 */
@Slf4j
@Data
public class SearchWorkflowVo {
    /**
     * 要展示的页码，默认1
     */
    private Integer page;
    /**
     * 每页展示数量，默认10
     */
    private Integer size;
    /**
     * 所属部门ID，对应部门表
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
     * 情形ID，多个逗号隔开
     */
    private String statusId;
    /**
     * 情形名称，多个逗号隔开
     */
    private String statusName;
    /**
     * 事项库ID，多个逗号隔开
     */
    private String databaseID;
    /**
     * 工作流ID
     */
    private String workflowId;
}
