package com.zfsoft.microservice.workflow.service;

import com.zfsoft.microservice.workflow.data.WorkflowBussInfo;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.web.bind.annotation.*;

/**
 * @author: kkfan
 * @create: 2021-02-01 11:02:10
 * @description: 流程历史
 */
@RequestMapping(value = "/security/workflow/workflowHistory")
public interface WorkflowHistoryService {

    /**
     * 删除流程信息
     * @param modelId 部署id
     * @return
     */
    @DeleteMapping(value = "/delete/{modelId}")
    ApiResultSet<Integer> deleteHistoryByModelId(@PathVariable("modelId") String modelId);


    /**
     * 根据流程类型操作Id获取流程类型操作信息
     * @param oid 流程Id
     * @return
     */
    @GetMapping(value = "/getOne/{oid}")
    ApiResultSet<WorkflowBussInfo> getWorkflowHistoryById(@PathVariable("oid") String oid);


    /**
     * 查询流程类型操作信息
     * @param
     * @return
     */
    @GetMapping(value = "/page")
    ApiResultSet queryWorkflowHistoryWithPage(@RequestParam(name = "infoOid", required = false) String infoOid, @RequestParam(name = "pageNum", defaultValue = "1", required = false) Integer pageNum, @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize);

}
