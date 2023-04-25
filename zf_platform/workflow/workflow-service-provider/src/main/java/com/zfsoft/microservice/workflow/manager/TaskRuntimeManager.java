package com.zfsoft.microservice.workflow.manager;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.util.StringUtil;
import com.zfsoft.microservice.workflow.data.WorkflowBussFlowStep;
import com.zfsoft.microservice.workflow.data.WorkflowFallback;
import com.zfsoft.microservice.workflow.data.WorkflowLink;
import com.zfsoft.microservice.workflow.util.AuthenticationUtil;
import com.zfsoft.microservice.workflow.util.BpmnUtil;
import com.zfsoft.microservice.workflow.util.LimitDateCalc;
import com.zfsoft.microservice.workflow.util.PageUtils;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.Builder;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.workflow.TaskInfo;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 任务接口
 * @author: wuxx
 * @Date: 2021/1/13 9:16
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "workflow:taskRuntime")
public class TaskRuntimeManager {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private WorkflowBussFlowStepManager workflowBussFlowStepManager;

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private TaskService taskService;

    @Autowired
    private WorkflowFallbackManager workflowFallbackManager;

    @Autowired
    private WorkflowLinkManager workflowLinkManager;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private ProcessRuntime processRuntime;

    /**
     * 查询任务列表
     *
     * @author zhongxx
     * @date 2021-02-07 11:05
     * @param appOid 应用唯一标识
     * @param typeCode 类型编码
     * @param infoOid 流程信息唯一标识
     * @param businessKey 业务关联标识
     * @param userOid 用户唯一标识
     * @return List
     */
    public List<TaskInfo> queryTasks(String appOid, String typeCode, String infoOid,String businessKey,String userOid){
        TaskQuery taskQuery = taskService.createTaskQuery();
        if(StrUtil.isNotBlank(userOid)){
            taskQuery.taskCandidateOrAssigned(userOid);
        }
        if (StrUtil.isNotBlank(appOid)) {
            taskQuery.processInstanceBusinessKeyLike(StrUtil.format("%{}%", "APP" + appOid.trim()));
        }
        if (StrUtil.isNotBlank(typeCode)) {
            taskQuery.processInstanceBusinessKeyLike(StrUtil.format("%{}%", "TYPE" + typeCode.trim()));
        }
        if (StrUtil.isNotBlank(infoOid)) {
            taskQuery.processInstanceBusinessKeyLike(StrUtil.format("%{}%", "INFO" + infoOid.trim()));
        }
        if (StrUtil.isNotBlank(businessKey)) {
            taskQuery.processInstanceBusinessKeyLike(StrUtil.format("%{}%", "BUSINESS" + businessKey.trim()));
        }
        taskQuery.orderByTaskCreateTime().desc();
        List<org.activiti.engine.task.Task> taskList = taskQuery.list();
        List<TaskInfo> list=new ArrayList<>();
        for(org.activiti.engine.task.Task task : taskList){
            WorkflowBussFlowStep step = workflowBussFlowStepManager.getWorkflowBussFlowStepByActivityIdAndDefId(task.getTaskDefinitionKey(), task.getProcessDefinitionId());
            if(null == step){
                step = workflowBussFlowStepManager.getWorkflowBussFlowStepByActivityId(task.getTaskDefinitionKey());
                if(null == step){
                    continue;
                }
            }
            WorkflowLink link = workflowLinkManager.getWorkflowLinkByLinkOid(step.getLinkOid());
            if(null == link){
                continue;
            }
            TaskInfo taskInfo= Builder.of(TaskInfo::new)
                    .with(TaskInfo::setId,task.getId())
                    .with(TaskInfo::setCreateTime,task.getCreateTime())
                    .with(TaskInfo::setDueTime,task.getDueDate())
                    .with(TaskInfo::setName,task.getName())
                    .with(TaskInfo::setExecutionId,task.getExecutionId())
                    .with(TaskInfo::setProcessInstanceId,task.getProcessInstanceId())
                    .with(TaskInfo::setProcessDefinitionId,task.getProcessDefinitionId())
                    .with(TaskInfo::setTypeCode,link.getCode())
                    .with(TaskInfo::setLinkName,link.getName())
                    .with(TaskInfo::setActionName,link.getActionName())
                    .build();
            list.add(taskInfo);
        }
        /*taskList.forEach(task -> {
            WorkflowBussFlowStep step = workflowBussFlowStepManager.getWorkflowBussFlowStepByActivityIdAndDefId(task.getTaskDefinitionKey(), task.getProcessDefinitionId());
            WorkflowLink link = workflowLinkManager.getWorkflowLinkByLinkOid(step.getLinkOid());
            TaskInfo taskInfo= Builder.of(TaskInfo::new)
                    .with(TaskInfo::setId,task.getId())
                    .with(TaskInfo::setCreateTime,task.getCreateTime())
                    .with(TaskInfo::setDueTime,task.getDueDate())
                    .with(TaskInfo::setName,task.getName())
                    .with(TaskInfo::setExecutionId,task.getExecutionId())
                    .with(TaskInfo::setProcessInstanceId,task.getProcessInstanceId())
                    .with(TaskInfo::setProcessDefinitionId,task.getProcessDefinitionId())
                    .with(TaskInfo::setTypeCode,link.getCode())
                    .with(TaskInfo::setLinkName,link.getName())
                    .with(TaskInfo::setActionName,link.getActionName())
                    .build();
            list.add(taskInfo);
        });*/
        return list;
    }

    public List<TaskInfo> queryToDoTasksListByprocessInstanceId(String processInstanceId){
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery.orderByTaskCreateTime().desc();
        List<org.activiti.engine.task.Task> taskList = taskQuery.processInstanceId(processInstanceId).list();
        List<TaskInfo> list=new ArrayList<>();
        for(org.activiti.engine.task.Task task : taskList){
            WorkflowBussFlowStep step = workflowBussFlowStepManager.getWorkflowBussFlowStepByActivityIdAndDefId(task.getTaskDefinitionKey(), task.getProcessDefinitionId());
            if(null == step){
                step = workflowBussFlowStepManager.getWorkflowBussFlowStepByActivityId(task.getTaskDefinitionKey());
                if(null == step){
                    continue;
                }
            }
            WorkflowLink link = workflowLinkManager.getWorkflowLinkByLinkOid(step.getLinkOid());
            if(null == link){
                continue;
            }
            org.activiti.engine.runtime.ProcessInstance processInstance= processEngine.getRuntimeService() // 获取运行时Service
                    .createProcessInstanceQuery() // 创建流程实例查询
                    .processInstanceId(processInstanceId) // 用流程实例id查询
                    .singleResult();
            String key = processInstance.getBusinessKey();
            // String key = "APP" + workflowBussInfo.getAppOid() + "^^^^TYPE" + type.getCode() + "^^^^INFO" + workflowBussInfo.getInfoOid() + "^^^^BUSINESS" + businessKey;
            String businessKey = key.substring(key.lastIndexOf("BUSINESS")+8);
            String infoOid = key.substring(key.lastIndexOf("INFO")+4,key.lastIndexOf("BUSINESS")-4);
            String userOids = step.getHandleUserOids();
            String postOids =  step.getHandlePostOids();
            TaskInfo taskInfo= Builder.of(TaskInfo::new)
                    .with(TaskInfo::setId,task.getId())
                    .with(TaskInfo::setCreateTime,task.getCreateTime())
                    .with(TaskInfo::setDueTime,task.getDueDate())
                    .with(TaskInfo::setName,task.getName())
                    .with(TaskInfo::setExecutionId,task.getExecutionId())
                    .with(TaskInfo::setProcessInstanceId,task.getProcessInstanceId())
                    .with(TaskInfo::setProcessDefinitionId,task.getProcessDefinitionId())
                    .with(TaskInfo::setTypeCode,link.getCode())
                    .with(TaskInfo::setLinkName,link.getName())
                    .with(TaskInfo::setActionName,link.getActionName())
                    .with(TaskInfo::setBusinessKey,businessKey)
                    .with(TaskInfo::setInfoOid,infoOid)
                    .with(TaskInfo::setUserOids,userOids)
                    .with(TaskInfo::setPostOids,postOids)
                    .with(TaskInfo::setAssignee,task.getAssignee())
                    .with(TaskInfo::setHandleType,step.getHandleType())
                    .build();
            list.add(taskInfo);
        }
        return list;
    }

    /**
     * 分页查询用户列表
     * @author zhongxx
     * @date 2021-02-07 11:10
     * @param appOid 应用唯一标识
     * @param typeCode 类型编码
     * @param infoOid 流程信息唯一标识
     * @param businessKey 业务关联标识
     * @param userOid 用户唯一标识
     * @param pageNumber 页码
     * @param pageSize 每页数量
     * @return PageResult
     */
    public PageResult<TaskInfo> queryTasks(String appOid, String typeCode, String infoOid,String businessKey,String userOid,Integer pageNumber, Integer pageSize){
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        int startNum = pageSize * (pageNumber - 1);
        TaskQuery taskQuery = taskService.createTaskQuery();
        if(StrUtil.isNotBlank(userOid)){
            taskQuery.taskCandidateOrAssigned(userOid);
        }
        if (StrUtil.isNotBlank(appOid)) {
            taskQuery.processInstanceBusinessKeyLike(StrUtil.format("%{}%", "APP" + appOid.trim()));
        }
        if (StrUtil.isNotBlank(typeCode)) {
            taskQuery.processInstanceBusinessKeyLike(StrUtil.format("%{}%", "TYPE" + typeCode.trim()));
        }
        if (StrUtil.isNotBlank(infoOid)) {
            taskQuery.processInstanceBusinessKeyLike(StrUtil.format("%{}%", "INFO" + infoOid.trim()));
        }
        if (StrUtil.isNotBlank(businessKey)) {
            taskQuery.processInstanceBusinessKeyLike(StrUtil.format("%{}%", "BUSINESS" + businessKey.trim()));
        }
        //测试性能暂时去除
        taskQuery.orderByTaskCreateTime().desc();
        long count = taskQuery.count();
        //long count = 0L;
        List<org.activiti.engine.task.Task> taskList = taskQuery.listPage(startNum, pageSize);
        List<TaskInfo> list=new ArrayList<>(pageSize);
        taskList.forEach(task -> {
            WorkflowBussFlowStep step = workflowBussFlowStepManager.getWorkflowBussFlowStepByActivityIdAndDefId(task.getTaskDefinitionKey(), task.getProcessDefinitionId());
            WorkflowLink link = workflowLinkManager.getWorkflowLinkByLinkOid(step.getLinkOid());
            TaskInfo taskInfo= Builder.of(TaskInfo::new)
                    .with(TaskInfo::setId,task.getId())
                    .with(TaskInfo::setTaskDefinitionKey,task.getTaskDefinitionKey())
                    .with(TaskInfo::setCreateTime,task.getCreateTime())
                    .with(TaskInfo::setName,task.getName())
                    .with(TaskInfo::setExecutionId,task.getExecutionId())
                    .with(TaskInfo::setProcessInstanceId,task.getProcessInstanceId())
                    .with(TaskInfo::setProcessDefinitionId,task.getProcessDefinitionId())
                    .with(TaskInfo::setTypeCode,link.getCode())
                    .with(TaskInfo::setLinkName,link.getName())
                    .with(TaskInfo::setActionName,link.getActionName())
                    .build();
            list.add(taskInfo);
        });
        PageResult<TaskInfo> page = new PageResult<>(pageNumber, pageSize, count);
        page.setData(list);
        return page;
    }

    /**
     * 获取任务信息
     *
     * @author zhongxx
     * @date 2021-02-08 10:21
     * @param taskId 任务唯一标识
     * @return
     */
    public TaskInfo getTaskInfo(String taskId){
        org.activiti.engine.task.Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(null==task){
            throw new ResultInfoException("当前任务信息不存在！");
        }
        WorkflowBussFlowStep step = workflowBussFlowStepManager.getWorkflowBussFlowStepByActivityIdAndDefId(task.getTaskDefinitionKey(), task.getProcessDefinitionId());
        WorkflowLink link = workflowLinkManager.getWorkflowLinkByLinkOid(step.getLinkOid());

        TaskInfo taskInfo= Builder.of(TaskInfo::new)
                .with(TaskInfo::setId,task.getId())
                .with(TaskInfo::setTaskDefinitionKey,task.getTaskDefinitionKey())
                .with(TaskInfo::setCreateTime,task.getCreateTime())
                .with(TaskInfo::setName,task.getName())
                .with(TaskInfo::setExecutionId,task.getExecutionId())
                .with(TaskInfo::setProcessInstanceId,task.getProcessInstanceId())
                .with(TaskInfo::setProcessDefinitionId,task.getProcessDefinitionId())
                .with(TaskInfo::setLinkOid,step.getLinkOid())
                .with(TaskInfo::setTypeCode,link.getCode())
                .with(TaskInfo::setLinkName,link.getName())
                .with(TaskInfo::setActionName,link.getActionName())
                .build();
        //驳回
        if(null!=step.getRejectTaskId() && StrUtil.isNotBlank(step.getRejectTaskId())){
            String[] splitRejectTaskId = step.getRejectTaskId().split(",");
            Map<String, String> mapBack = new HashMap();
            for (String rejectTaskId : splitRejectTaskId){
                WorkflowBussFlowStep selectStep = new WorkflowBussFlowStep();
                selectStep.setInfoOid(step.getInfoOid());
                selectStep.setProcessDefId(step.getProcessDefId());
                selectStep.setActivityId(rejectTaskId);
                List<WorkflowBussFlowStep> bussFlowStepList = workflowBussFlowStepManager.queryWorkflowBussFlowStepList(selectStep);
                String stepName = bussFlowStepList.size()>0?bussFlowStepList.get(0).getName():null;
                mapBack.put(rejectTaskId,stepName);
            }
            taskInfo.setRejectTaskIdMap(mapBack);
        }
        //退回
        if(null!=step.getFallbackTaskId() && StrUtil.isNotBlank(step.getFallbackTaskId())){
            String[] splitTaskId = step.getFallbackTaskId().split(",");
            Map<String, String> mapBack = new HashMap();
            for (String fallbackTaskId : splitTaskId){
                WorkflowBussFlowStep selectStep = new WorkflowBussFlowStep();
                selectStep.setInfoOid(step.getInfoOid());
                selectStep.setProcessDefId(step.getProcessDefId());
                selectStep.setActivityId(fallbackTaskId);
                List<WorkflowBussFlowStep> bussFlowStepList = workflowBussFlowStepManager.queryWorkflowBussFlowStepList(selectStep);
                String stepName = bussFlowStepList.size()>0?bussFlowStepList.get(0).getName():null;
                mapBack.put(fallbackTaskId,stepName);
            }
            taskInfo.setFallbackTaskMap(mapBack);
        }
        return taskInfo;
    }



    /**
     * @description: 查看我的待办列表
     * @author: wuxx
     * @Date: 2021/1/13 10:48
     **/
    public String getUserToTasks(Integer pageNumber, Integer pageSize) {
        try {
            //分页参数
            if (null == pageNumber || pageNumber <= 1) {
                pageNumber = 1;
            }
            if (null == pageSize || pageSize <= 0) {
                pageSize = 10;
            }
            int startNum = pageSize * (pageNumber-1);
            Page<Task> tasks = taskRuntime.tasks(Pageable.of(startNum,pageSize));
            List<Task> list=tasks.getContent();
            Map<String, Object> jsonMap = new HashMap<>();
            List<Map<String,Object>> mapList = new ArrayList<>();
            for(Task task : list){
                Map<String,Object> reulstMap = new HashMap<>();
                reulstMap.put("taskId",task.getId());
                reulstMap.put("name",task.getName());
                reulstMap.put("status",task.getStatus());
                reulstMap.put("createdDate",task.getCreatedDate());
                reulstMap.put("processDefinitionId",task.getProcessDefinitionId());
                String processInstanceId = task.getProcessInstanceId();
                HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
                String superProcessInstanceId = historicProcessInstance.getSuperProcessInstanceId();
                reulstMap.put("superProcessInstanceId",superProcessInstanceId);
                reulstMap.put("businessKey",task.getBusinessKey());
                if(task.getAssignee() == null){
                    //候选人为当前登录用户，null的时候需要前端拾取
                    reulstMap.put("assignee",null);
                }else{
                    reulstMap.put("assignee",task.getAssignee());
                }
                mapList.add(reulstMap);
            }
            jsonMap.put("data",mapList);
            jsonMap.put("totalSize",tasks.getTotalItems());
            return JSONUtil.toJsonStr(jsonMap);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResultInfoException("查看我的待办列表异常！",e);
        }
    }

    /**
     * @description: 完成任务并执行下一个环节
     * @param taskId 任务id
     * @param variableMap 参数(非必填)
     * @author: wuxx
     * @Date: 2021/1/13 14:30
     **/
    public boolean completeTask(String taskId,Map<String,Object> variableMap){
        try {
            Task task = taskRuntime.task(taskId);
            if(task.getAssignee() == null){
                //候选人为当前登录用户，null的时候需要前端拾取
                taskRuntime.claim(TaskPayloadBuilder.claim()
                        .withTaskId(task.getId())
                        .build());
            }
            //完成任务
            taskRuntime.complete(TaskPayloadBuilder
                    .complete().withVariables(variableMap)
                    .withTaskId(task.getId())
                    .build());
            //更新下一个环节的应办理时间
            //测试性能暂时去除
            this.updateDueDateByTaskIdOrInstanceId(taskId,null);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            throw new ActivitiException(e.getMessage());
        }
    }

    /**
     * @description: 跳过某一个环节
     * @param taskId 任务主键
     * @param userId 用户主键
     * @author: wuxx
     * @Date: 2021/5/11 11:21
     **/
    public boolean skipTask(String taskId,String userId){
        Map<String, Object> variableMap = new HashMap<>();
        // 允许后面有节点跳过
        variableMap.put("_ACTIVITI_SKIP_EXPRESSION_ENABLED", true);
        variableMap.put("skipExpression", BaseStaticParameter.STR_ONE);
        //设置当前登录人（针对feign接口调用方式）
        if(StrUtil.isNotEmpty(userId)){
            AuthenticationUtil.setAuthentication(userId);
        }
        org.activiti.engine.task.Task newTask = taskService.createTaskQuery().taskId(taskId).singleResult();
        taskService.setVariablesLocal(taskId, variableMap);
        taskService.complete(taskId);
        //更新下一个环节的应办理时间
        this.updateDueDateByTaskIdOrInstanceId(taskId,null);
        //如果当前环节有其他人审批，直接完成并删除记录
        //当前待审批节点定义Id集合
        List<org.activiti.engine.task.Task> taskList = taskService.createTaskQuery().processInstanceId(newTask.getProcessInstanceId()).list();
        if(null!=taskList && taskList.size()>0){
            for(org.activiti.engine.task.Task task:taskList){
                if(task.getTaskDefinitionKey().equals(newTask.getTaskDefinitionKey())){
                    String assignee = task.getAssignee();
                    if(StrUtil.isNotBlank(assignee)){
                        AuthenticationUtil.setAuthentication(assignee);
                        this.completeTask(task.getId(),null);
                        historyService.deleteHistoricTaskInstance(task.getId());
                    }
                }
            }
        }

        //0
        //保存到跳过记录
        //当前待审批节点定义Id集合
        WorkflowFallback workflowFallback = new WorkflowFallback();
        workflowFallback.setTaskId(taskId);
        workflowFallback.setActivityId(newTask.getTaskDefinitionKey());
        workflowFallback.setFallbackType(2);
        WorkflowBussFlowStep step = workflowBussFlowStepManager.getWorkflowBussFlowStepByActivityIdAndDefId(newTask.getTaskDefinitionKey(), newTask.getProcessDefinitionId());
        workflowFallback.setStepOid(null!=step?step.getStepOid():null);
        workflowFallbackManager.saveWorkflowFallback(workflowFallback);
        return true;
    }
    /**
     * @description: 根据任务id或者流程实列id更新应办理时间
     * @param taskId 任务id
     * @param processInstanceId 流程实列id
     * @author: wuxx
     * @Date: 2021/2/5 13:59
     **/
    public boolean updateDueDateByTaskIdOrInstanceId(String taskId,String processInstanceId){
        if(StrUtil.isEmpty(taskId) && StrUtil.isEmpty(processInstanceId) ){
            return false;
        }
        if(StrUtil.isEmpty(processInstanceId)){
            //实列id为空根据任务id获取
            HistoricTaskInstance historicTaskInstance =  historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            processInstanceId = historicTaskInstance.getProcessInstanceId();
        }
        List<org.activiti.engine.task.Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        if(null!=taskList && taskList.size()>0){
            for (org.activiti.engine.task.Task taskNew :taskList){
                if(null==taskNew.getDueDate()){
                    try {
                        WorkflowBussFlowStep step = workflowBussFlowStepManager.getWorkflowBussFlowStepByActivityIdAndDefId(taskNew.getTaskDefinitionKey(), taskNew.getProcessDefinitionId());
                        Date dueDate = LimitDateCalc.dateCalc(taskNew.getCreateTime(), step.getTimeLimit(), step.getTimeUnit(), step.getTimeLimitHour());
                        taskService.setDueDate(taskNew.getId(),dueDate);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        //判断是否有内嵌流程，存在内嵌流程，更新应办结时间
        List<HistoricActivityInstance> instanceList = historyService.createHistoricActivityInstanceQuery().unfinished().processInstanceId(processInstanceId).list();
        String calledProcessInstanceId = null!=instanceList && instanceList.size()>0?instanceList.get(0).getCalledProcessInstanceId():null;
        if(null != calledProcessInstanceId){
            List<org.activiti.engine.task.Task> instanceTaskList = taskService.createTaskQuery().processInstanceId(calledProcessInstanceId).list();
            if(null!=instanceTaskList && instanceTaskList.size()>0){
                for (org.activiti.engine.task.Task taskNew :instanceTaskList){
                    if(null==taskNew.getDueDate()){
                        try {
                            WorkflowBussFlowStep step = workflowBussFlowStepManager.getWorkflowBussFlowStepByActivityIdAndDefId(taskNew.getTaskDefinitionKey(), taskNew.getProcessDefinitionId());
                            Date dueDate = LimitDateCalc.dateCalc(taskNew.getCreateTime(), step.getTimeLimit(), step.getTimeUnit(), step.getTimeLimitHour());
                            taskService.setDueDate(taskNew.getId(),dueDate);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * @description: 查看所有的任务
     * @author: wuxx
     * @Date: 2021/1/13 10:48
     **/
    public String queryAllTasks(Integer pageNumber, Integer pageSize) {
        try {
            //分页参数
            if (null == pageNumber || pageNumber <= 1) {
                pageNumber = 1;
            }
            if (null == pageSize || pageSize <= 0) {
                pageSize = 10;
            }
            List<org.activiti.engine.task.Task> list = taskService.createTaskQuery().list();
            int totalSize = list.size();
            list = PageUtils.startPage(list,pageNumber,pageSize);
            Map<String, Object> jsonMap = new HashMap<>();
            List<Map<String,Object>> mapList = new ArrayList<>();
            for(org.activiti.engine.task.Task task : list){
                Map<String,Object> reulstMap = new HashMap<>();
                reulstMap.put("taskId",task.getId());
                reulstMap.put("name",task.getName());
                reulstMap.put("assignee",task.getAssignee());
                reulstMap.put("taskDefinitionKey",task.getTaskDefinitionKey());
                reulstMap.put("processDefinitionId",task.getProcessDefinitionId());
                mapList.add(reulstMap);
            }
            jsonMap.put("data",mapList);
            jsonMap.put("totalSize",totalSize);
            return JSONUtil.toJsonStr(jsonMap);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResultInfoException("查看所有的任务列表异常！",e);
        }
    }


    /**
     * @description:  驳退回到流程图绘制的环节
     * @param taskId 任务id
     * @author: wuxx
     * @Date: 2021/2/8 10:13
     **/
    public String rejectTask(String taskId,String targetNodeId,Map<String, Object> variables ,String userId){
        if (taskId == null || "".equals(taskId)) {
            return "任务ID不能为空";
        }
        //设置当前登录人（针对feign接口调用方式）
        if(StrUtil.isNotEmpty(userId)){
            AuthenticationUtil.setAuthentication(userId);
        }
        Task taskCurrent = taskRuntime.task(taskId);
        if (null == taskCurrent) {
            return "任务不能为空";
        }
        //获取流程模型
        BpmnModel bpmnModel = repositoryService.getBpmnModel(taskCurrent.getProcessDefinitionId());
        if (null == bpmnModel) {
            return "驳回失败，失败原因：未查到流程信息！";
        }
        String processInstanceId = taskCurrent.getProcessInstanceId();
        FlowElement targetFlowElement = null;
        if (StringUtil.isNotEmpty(targetNodeId)) {
            //找到目标节点元素
            targetFlowElement = bpmnModel.getMainProcess().getFlowElement(targetNodeId);
        } else {
            targetFlowElement = BpmnUtil.startEventNextTaskId(bpmnModel);
        }
        //当前待审批节点定义Id集合
        List<org.activiti.engine.task.Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        if (CollectionUtil.isNotEmpty(taskList)) {
            org.activiti.engine.task.Task backTask = null;
            if(taskList.size() ==1){
                backTask =  taskList.get(0);
            }else{
                for (org.activiti.engine.task.Task task : taskList) {
                    if(StrUtil.isNotBlank(task.getAssignee()) && task.getAssignee().contains(userId)){
                        backTask = task;
                    }else {
                        //去除当前节点其他任务
                        //taskService.deleteCandidateUser(task.getId(), task.getAssignee());
                        String assignee = task.getAssignee();
                        if(StrUtil.isNotBlank(assignee)){
                            AuthenticationUtil.setAuthentication(assignee);
                            this.completeTask(task.getId(),variables);
                            historyService.deleteHistoricTaskInstance(task.getId());
                        }
                    }
                }
            }
            BpmnModel newBpmnModel = bpmnModel;
            List<org.activiti.engine.task.Task> newTaskList = new ArrayList<>();
            if(null!=backTask){
                newTaskList.add(backTask);
                Map<String, List<SequenceFlow>> stringListMap = BpmnUtil.invokeSequenceFlows(newBpmnModel, newTaskList, targetFlowElement);
                //记录原活动方向
                List<SequenceFlow> oriSequenceFlows = new ArrayList<>();
                //当前节点
                oriSequenceFlows.addAll(stringListMap.get(backTask.getTaskDefinitionKey()));
                FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(backTask.getTaskDefinitionKey());
                try {
                    //完成
                    AuthenticationUtil.setAuthentication(userId);
                    this.completeTask(backTask.getId(),variables);
                    //managementService.executeCommand(new CustomTaskCompleteCmd(task.getId(),variables,true));
                    //删除任务
                    //historyService.deleteHistoricTaskInstance(task.getId());
                    //保存到驳回记录
                    WorkflowFallback workflowFallback = new WorkflowFallback();
                    workflowFallback.setTaskId(taskId);
                    workflowFallback.setActivityId(backTask.getTaskDefinitionKey());
                    workflowFallback.setFallbackType(BaseStaticParameter.N);
                    WorkflowBussFlowStep step = workflowBussFlowStepManager.getWorkflowBussFlowStepByActivityIdAndDefId(backTask.getTaskDefinitionKey(), backTask.getProcessDefinitionId());
                    workflowFallback.setStepOid(null!=step?step.getStepOid():null);
                    workflowFallbackManager.saveWorkflowFallback(workflowFallback);
                } catch (Exception e) {
                    e.printStackTrace();
                    return  "流程驳回异常，异常原因：" + e.getMessage();
                } finally {
                    //恢复原方向
                    currentFlowNode.setOutgoingFlows(oriSequenceFlows);
                }
            }
        }
        return null;
    }

    /**
     * @description:  退回到流程图绘制的环节
     * @param taskId 任务id
     * @author: wuxx
     * @Date: 2021/2/8 10:13
     **/
    public String fallbackTask(String taskId,String targetNodeId,Map<String, Object> variables,String userId){
        if (taskId == null || "".equals(taskId)) {
            return "任务ID不能为空";
        }
        Task taskCurrent = taskRuntime.task(taskId);
        if (null == taskCurrent) {
            return "任务不能为空";
        }
        //获取流程模型
        BpmnModel bpmnModel = repositoryService.getBpmnModel(taskCurrent.getProcessDefinitionId());
        if (null == bpmnModel) {
            return "退回失败，失败原因：未查到流程信息！";
        }
        String processInstanceId = taskCurrent.getProcessInstanceId();
        FlowElement targetFlowElement = null;
        if (StringUtil.isNotEmpty(targetNodeId)) {
            //找到目标节点元素
            targetFlowElement = bpmnModel.getMainProcess().getFlowElement(targetNodeId);
        } else {
            targetFlowElement = BpmnUtil.startEventNextTaskId(bpmnModel);
        }
        //当前待审批节点定义Id集合
        List<org.activiti.engine.task.Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        org.activiti.engine.task.Task backTask = null;
        if (CollectionUtil.isNotEmpty(taskList)) {
            if(taskList.size() ==1){
                backTask =  taskList.get(0);
            }else{
                for (org.activiti.engine.task.Task task : taskList) {
                    if(StrUtil.isNotBlank(task.getAssignee()) && task.getAssignee().contains(userId)){
                        backTask = task;
                    }else {
                        //去除当前节点其他任务
                        //taskService.deleteCandidateUser(task.getId(), task.getAssignee());
                        String assignee = task.getAssignee();
                        if(StrUtil.isNotBlank(assignee)){
                            AuthenticationUtil.setAuthentication(assignee);
                            this.completeTask(task.getId(),variables);
                            historyService.deleteHistoricTaskInstance(task.getId());
                        }
                    }
                }
            }
            BpmnModel newBpmnModel = bpmnModel;
            List<org.activiti.engine.task.Task> newTaskList = new ArrayList<>();
            if(null!=backTask){
                newTaskList.add(backTask);
                Map<String, List<SequenceFlow>> stringListMap = BpmnUtil.invokeSequenceFlows(newBpmnModel, newTaskList, targetFlowElement);
                //记录原活动方向
                List<SequenceFlow> oriSequenceFlows = new ArrayList<>();
                //当前节点
                oriSequenceFlows.addAll(stringListMap.get(backTask.getTaskDefinitionKey()));
                FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(backTask.getTaskDefinitionKey());
                try {
                    //完成
                    AuthenticationUtil.setAuthentication(userId);
                    this.completeTask(backTask.getId(),variables);
                    //managementService.executeCommand(new CustomTaskCompleteCmd(task.getId(),variables,true));
                    //删除任务
                    //historyService.deleteHistoricTaskInstance(task.getId());
                    //保存到退回记录
                    WorkflowFallback workflowFallback = new WorkflowFallback();
                    workflowFallback.setTaskId(taskId);
                    workflowFallback.setActivityId(backTask.getTaskDefinitionKey());
                    workflowFallback.setFallbackType(BaseStaticParameter.Y);
                    WorkflowBussFlowStep step = workflowBussFlowStepManager.getWorkflowBussFlowStepByActivityIdAndDefId(backTask.getTaskDefinitionKey(), backTask.getProcessDefinitionId());
                    workflowFallback.setStepOid(null!=step?step.getStepOid():null);
                    workflowFallbackManager.saveWorkflowFallback(workflowFallback);
                } catch (Exception e) {
                    e.printStackTrace();
                    return  "流程退回异常，异常原因：" + e.getMessage();
                } finally {
                    //恢复原方向
                    currentFlowNode.setOutgoingFlows(oriSequenceFlows);
                }
            }
        }
        return null;
    }


    /**
     * @description: 更新应办结时间
     * @param taskId 任务id
     * @param dueDate 应办结时间
     * @author: wuxx
     * @Date: 2021/2/9 13:22
     **/
    public boolean updateDueDateByTaskId(String taskId, Date dueDate){
        if(StrUtil.isEmpty(taskId) || null == dueDate ){
            return false;
        }
        taskService.setDueDate(taskId,dueDate);
        return true;
    }

    /**
     * @description: 更新应办结时间
     * @param taskId 任务id
     * @param timeLimitDay
     *            天数
     * @param timeUnit
     *            单位，N-自然日，W-工作日
     * @param timeLimitHour
     *            小时数
     * @author: wuxx
     * @Date: 2021/2/9 13:22
     **/
    public boolean updateDueDateByTime(String taskId, Integer timeLimitDay, String timeUnit, Integer timeLimitHour) {
        if(StrUtil.isEmpty(taskId) || null == timeUnit ){
            return false;
        }
        org.activiti.engine.task.Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(null == task){
            return false;
        }
        Date dueDate = new Date();
        if(null != task.getDueDate()){
            dueDate = task.getDueDate();
        }
        if(null != timeLimitDay || null != timeLimitHour){
            Date newDueDate = LimitDateCalc.dateCalc(dueDate,timeLimitDay, timeUnit, timeLimitHour);
            taskService.setDueDate(taskId,newDueDate);
        }
        return true;
    }

    /**
     * @description: 审核不通过
     * @param processInstanceId 实例id
     * @param deleteReason 原因
     * @param taskId 任务id
     * @author: wuxx
     * @Date: 2021/5/11 14:29
     **/
    public boolean noPassProcessInstance(String processInstanceId,String deleteReason,String taskId) {
        //当前待审批节点定义Id集合
        List<org.activiti.engine.task.Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        if (CollectionUtil.isNotEmpty(taskList)) {
            org.activiti.engine.task.Task backTask = null;
            if(taskList.size() ==1){
                backTask =  taskList.get(0);
                if(!taskId.equals(backTask.getId())){
                    throw new ResultInfoException("参数taskId错误！");
                }
            }else{
                boolean flag = false;
                for (org.activiti.engine.task.Task task : taskList) {
                    if(taskId.equals(task.getId())){
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    throw new ResultInfoException("参数taskId错误！");
                }
                for (org.activiti.engine.task.Task task : taskList) {
                    if(taskId.equals(task.getId())){
                        backTask = task;
                    }else {
                        //去除当前节点其他任务
                        String assignee = task.getAssignee();
                        if(StrUtil.isNotBlank(assignee)){
                            AuthenticationUtil.setAuthentication(assignee);
                            this.completeTask(task.getId(),null);
                            historyService.deleteHistoricTaskInstance(task.getId());
                        }
                    }
                }
            }
            if(null!=backTask){
                processRuntime.delete(ProcessPayloadBuilder
                        .delete()
                        .withProcessInstanceId(processInstanceId)
                        .withReason(deleteReason)
                        .build()
                );
            }

            //判断是否有内嵌流程，存在内嵌流程，审核不通过父类
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            String calledProcessInstanceId = historicProcessInstance.getSuperProcessInstanceId();
            if(null != calledProcessInstanceId){
                List<org.activiti.engine.task.Task> instanceTaskList = taskService.createTaskQuery().processInstanceId(calledProcessInstanceId).list();
                if(null!=instanceTaskList && instanceTaskList.size()>0){
                    for (org.activiti.engine.task.Task taskNew :instanceTaskList){
                        //去除当前节点其他任务
                        String assignee = taskNew.getAssignee();
                        if(StrUtil.isNotBlank(assignee)){
                            AuthenticationUtil.setAuthentication(assignee);
                            this.completeTask(taskNew.getId(),null);
                            historyService.deleteHistoricTaskInstance(taskNew.getId());
                        }
                    }
                }
                processRuntime.delete(ProcessPayloadBuilder
                        .delete()
                        .withProcessInstanceId(calledProcessInstanceId)
                        .withReason(deleteReason)
                        .build()
                );
            }
            return true;
        }else {
            throw new ResultInfoException("流程实例不存在或者流程已经完成！");
        }

    }

    /**
     * @description:  （根据任务id）切换审核人员-针对审核人员离职或其他无法正常审核的情况
     * @param taskId 任务id
     * @param oldUserOid 旧的审核人员
     * @param newUserOid 新的审核人员
     * @author: wuxx
     * @Date: 2022/3/21 10:19
     **/
    public boolean changeTaskAuditUserOid(String taskId, String oldUserOid, String newUserOid) {
        if(StrUtil.isNotBlank(taskId)){
            Task task = taskRuntime.task(taskId);
            if(null == task){
                throw new ResultInfoException("任务不存在或者已经完成！");
            }
            //先归还任务
            taskService.setAssignee(taskId,null);

            //交办任务给其他以后
            taskService.setAssignee(taskId,newUserOid);

            return true;
        }else {
            //先查询oldUserOid所有的待办任务列表
            //当前待审批节点定义Id集合
            List<org.activiti.engine.task.Task> taskList = taskService.createTaskQuery().taskCandidateOrAssigned(oldUserOid).list();
            for(org.activiti.engine.task.Task task : taskList){
                org.activiti.engine.runtime.ProcessInstance processInstance= processEngine.getRuntimeService() // 获取运行时Service
                        .createProcessInstanceQuery() // 创建流程实例查询
                        .processInstanceId(task.getProcessInstanceId()) // 用流程实例id查询
                        .singleResult();
                //获取到当前流程定义是否为暂停状态   suspended方法为true代表为暂停   false就是运行的
                boolean suspended = processInstance.isSuspended();
                if(!suspended){
                    String id = task.getId();
                    //先归还任务
                    taskService.setAssignee(id,null);

                    //交办任务给其他以后
                    taskService.setAssignee(id,newUserOid);
                }
            }
            return true;
        }

    }

    /**
     * @description: 获取所有已办理的环节点（可用于退回、驳回）
     * @param taskId 任务id
     * @param processInstanceId 实例id
     * @author: wuxx
     * @Date: 2022/3/24 10:02
     **/
    public List<String> getAllFlowNodeActivitiId(String taskId, String processInstanceId) {
        String taskDefinitionKey = null;
        if(StrUtil.isNotBlank(taskId) && StrUtil.isBlank(processInstanceId)){
            org.activiti.engine.task.Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if(task == null) {
                throw new ResultInfoException("未查到流程信息！");
            }
            processInstanceId = task.getProcessInstanceId();
            taskDefinitionKey = task.getTaskDefinitionKey();
        }
        ProcessInstance processInstance = processRuntime.processInstance(processInstanceId);
        if(processInstance == null) {
            throw new ResultInfoException("未查到流程信息！");
        }
        //获取流程模型
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        if (null == bpmnModel) {
            throw new ResultInfoException("未查到流程信息！");
        }
        /*Process process = bpmnModel.getProcesses().get(0);
        Collection<FlowElement> flowElements = process.getFlowElements();
        for(FlowElement element : flowElements){
            if(element instanceof  UserTask){
                UserTask userTask = (UserTask) element;
                System.out.println(userTask.getId()+"===="+userTask.getName());

            }
        }*/

        // 退回的目标节点(当前用户已办理的最新节点)
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId)
            .finished().orderByHistoricTaskInstanceEndTime().desc().list();
        List<String> stringList = new ArrayList<>();
        for(HistoricTaskInstance instance : list){
            if(StrUtil.isNotBlank(taskDefinitionKey) && taskDefinitionKey.equals(instance.getTaskDefinitionKey())){
                continue;
            }
            stringList.add(instance.getTaskDefinitionKey()+"~"+instance.getName());
            //map.put(instance.getTaskDefinitionKey(),instance.getName());
        }
        //去重
        stringList = stringList.stream().distinct().collect(Collectors.toList());
        return stringList;
    }
}
