package com.zfsoft.microservice.workflow.service;

import com.zfsoft.microservice.workflow.data.WorkflowBussFlowStep;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName WorkflowBussFlowTypeService
 * @Description 工作流业务信息流程环节表服务定义接口
 * @Author wuxx
 * @Date 2021-01-25 18:33
 * @Version V1.0
 **/
@RequestMapping("/security/workflow/step")
public interface WorkflowBussFlowStepService {
    /**
     * 增加一个新流程环节参数
     *
     * @param workflowBussFlowStep 新流程环节参数
     * @return
     */
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    ApiResultSet<WorkflowBussFlowStep> saveWorkflowBussFlowStep(@RequestBody WorkflowBussFlowStep workflowBussFlowStep);

    /**
     * 删除指定Id的流程环节参数信息
     *
     * @param id 流程环节参数id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = {RequestMethod.POST})
    ApiResultSet<Integer> deleteWorkflowBussFlowStepById(@PathVariable("id") Long id);

    /**
     * @description: 根据主键获取流程类型信息
     * @param stepOid 业务主键
     * @author: wuxx
     * @Date: 2021/1/25 18:17
     **/
    @RequestMapping(value = "/getWorkflowBussFlowStepByStepOid", method = {RequestMethod.GET})
    ApiResultSet<WorkflowBussFlowStep> getWorkflowBussFlowStepByStepOid(@RequestParam("stepOid") String stepOid);

    /**
     * @description:  根据用户任务的xmlid查询信息
     * @param activityId 用户任务的id
     * @param processDefId  对应activiti的act_re_procdef 表ID
     * @author: wuxx
     * @Date: 2021/01/25 15:17
     **/
    @RequestMapping(value = "/getWorkflowBussFlowStepByActivityIdAndDefId", method = {RequestMethod.GET})
    ApiResultSet<WorkflowBussFlowStep> getWorkflowBussFlowStepByActivityIdAndDefId(
            @RequestParam("activityId") String activityId,
            @RequestParam("processDefId") String processDefId);

    /**
     * 根据流程环节参数Id获取流程环节参数信息
     *
     * @param id 流程环节参数Id
     * @return
     */
    @RequestMapping(value = "/getOne/{id}", method = {RequestMethod.GET})
    ApiResultSet<WorkflowBussFlowStep> getWorkflowBussFlowStepById(@PathVariable("id") Long id);

    /**
     * @description:  查询流程环节参数列表
     * @param infoOid  流程设计的主键
     * @param processDefId  对应activiti的act_re_procdef 表ID
     * @author: wuxx
     * @Date: 2021/1/25 18:01
     **/
    @GetMapping("/list")
    ApiResultSet queryWorkflowBussFlowStepList(@RequestParam(value = "infoOid", required = false) String infoOid,
                                               @RequestParam(value = "processDefId", required = false) String processDefId);

    /**
     * @description:  根据procdef Id查询列表信息
     * @param processDefId  对应activiti的act_re_procdef 表ID
     * @author: wuxx
     * @Date: 2021/02/8 15:17
     **/
    @GetMapping(value = "/queryWorkflowBussFlowStepByProcessDefId")
    ApiResultSet<List<WorkflowBussFlowStep>> queryWorkflowBussFlowStepByProcessDefId(@RequestParam("processDefId") String processDefId);
}
