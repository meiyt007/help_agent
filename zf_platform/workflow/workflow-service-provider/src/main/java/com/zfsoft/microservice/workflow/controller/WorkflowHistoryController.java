package com.zfsoft.microservice.workflow.controller;

import com.zfsoft.microservice.workflow.data.WorkflowBussInfo;
import com.zfsoft.microservice.workflow.data.WorkflowHistoryVo;
import com.zfsoft.microservice.workflow.manager.WorkflowHistoryManager;
import com.zfsoft.microservice.workflow.service.WorkflowHistoryService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author: kkfan
 * @create: 2021-02-01 11:38:38
 * @description: 流程历史
 */
@RestController
public class WorkflowHistoryController implements WorkflowHistoryService {

    @Autowired
    private WorkflowHistoryManager workflowHistoryManager;

    @Override
    public ApiResultSet<Integer> deleteHistoryByModelId(String modelId) {
        this.workflowHistoryManager.deleteHistoryByModelId(modelId);
        return new ApiResultSet<>();
    }

    @Override
    public ApiResultSet<WorkflowBussInfo> getWorkflowHistoryById(String oid) {
        return null;
    }

    @Override
    public ApiResultSet queryWorkflowHistoryWithPage(String infoOid, Integer pageNum, Integer pageSize) {
        PageResult<WorkflowHistoryVo> pageResult = this.workflowHistoryManager.queryWorkflowHistoryWithPage(infoOid, pageNum, pageSize);
        return new ApiResultSet(pageResult);
    }
}
