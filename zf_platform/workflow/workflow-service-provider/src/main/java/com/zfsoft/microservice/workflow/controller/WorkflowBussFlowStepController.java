package com.zfsoft.microservice.workflow.controller;

import com.zfsoft.microservice.workflow.data.WorkflowBussFlowStep;
import com.zfsoft.microservice.workflow.manager.WorkflowBussFlowStepManager;
import com.zfsoft.microservice.workflow.service.WorkflowBussFlowStepService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName WorkflowBussFlowStep
 * @Description 工作流业务信息流程环节表的实现类
 * @Author wuxx
 * @Date 2021-01-25 18:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class WorkflowBussFlowStepController implements WorkflowBussFlowStepService {

    @Resource
    private WorkflowBussFlowStepManager workflowBussFlowStepManager;


    /**
     * @param workflowBussFlowStep 流程类型实体类
     * @description: 流程类型的新增或者修改
     * @author: wuxx
     * @Date: 2021/01/25 15:14
     **/
    @Override
    public ApiResultSet<WorkflowBussFlowStep> saveWorkflowBussFlowStep(WorkflowBussFlowStep workflowBussFlowStep) {
        workflowBussFlowStepManager.saveWorkflowBussFlowStep(workflowBussFlowStep);
        ApiResultSet<WorkflowBussFlowStep> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(workflowBussFlowStep);
        return apiResultSet;
    }

    /**
     * @param id 流程类型实体类主键
     * @description: 流程类型的信息的删除
     * @author: wuxx
     * @Date: 2021/01/25 15:14
     **/
    @Override
    public ApiResultSet<Integer> deleteWorkflowBussFlowStepById(Long id) {
        int rows = workflowBussFlowStepManager.deleteWorkflowBussFlowStepById(id);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<Integer>();
        if (BaseStaticParameter.Y == rows){
            return apiResultSet;
        }else {
            apiResultSet.setCode(ApiResultSet.DIRTY_DATA_TITLE);
            apiResultSet.setMessage("删除失败！");
            return apiResultSet;
        }
    }

    /**
     * @description: 根据主键获取流程类型信息
     * @param stepOid 业务主键
     * @author: wuxx
     * @Date: 2021/1/25 18:17
     **/
    @Override
    public ApiResultSet<WorkflowBussFlowStep> getWorkflowBussFlowStepByStepOid(String stepOid) {
        WorkflowBussFlowStep workflowBussFlowStep = workflowBussFlowStepManager.getWorkflowBussFlowStepByStepOid(stepOid);
        return new ApiResultSet<>(workflowBussFlowStep);
    }

    /**
     * @description:  根据用户任务的xmlid查询信息
     * @param activityId 用户任务的id
     * @author: wuxx
     * @Date: 2021/01/25 15:17
     **/
    @Override
    public ApiResultSet<WorkflowBussFlowStep> getWorkflowBussFlowStepByActivityIdAndDefId(String activityId,String processDefId) {
        WorkflowBussFlowStep workflowBussFlowStep = workflowBussFlowStepManager.getWorkflowBussFlowStepByActivityIdAndDefId(activityId,processDefId);
        return new ApiResultSet<>(workflowBussFlowStep);
    }

    /**
     * @param id 流程类型实体类主键
     * @description: 获取流程类型的信息
     * @author: wuxx
     * @Date: 2021/01/25 15:14
     **/
    @Override
    public ApiResultSet<WorkflowBussFlowStep> getWorkflowBussFlowStepById(Long id) {
        WorkflowBussFlowStep workflowBussFlowStep = workflowBussFlowStepManager.getWorkflowBussFlowStepById(id);
        return new ApiResultSet<>(workflowBussFlowStep);
    }


    /**
     * @description:  查询流程环节参数列表
     * @param infoOid  流程设计的主键
     * @param processDefId  对应activiti的act_re_procdef 表ID
     * @author: wuxx
     * @Date: 2021/1/25 18:01
     **/
    @Override
    public ApiResultSet queryWorkflowBussFlowStepList(String infoOid,String processDefId) {
        WorkflowBussFlowStep workflowBussFlowStep = new WorkflowBussFlowStep();
        workflowBussFlowStep.setInfoOid(infoOid);
        workflowBussFlowStep.setProcessDefId(processDefId);
        List<WorkflowBussFlowStep> workflowBussFlowSteps = workflowBussFlowStepManager.queryWorkflowBussFlowStepList(workflowBussFlowStep);
        return new ApiResultSet<>(workflowBussFlowSteps);
    }

    @Override
    public ApiResultSet<List<WorkflowBussFlowStep>> queryWorkflowBussFlowStepByProcessDefId(String processDefId) {
        WorkflowBussFlowStep workflowBussFlowStep = new WorkflowBussFlowStep();
        workflowBussFlowStep.setProcessDefId(processDefId);
        List<WorkflowBussFlowStep> workflowBussFlowSteps = workflowBussFlowStepManager.queryWorkflowBussFlowStepList(workflowBussFlowStep);
        return new ApiResultSet<>(workflowBussFlowSteps);
    }
}
