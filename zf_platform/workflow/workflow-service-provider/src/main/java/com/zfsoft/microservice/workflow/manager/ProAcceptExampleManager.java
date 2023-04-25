package com.zfsoft.microservice.workflow.manager;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.microservice.workflow.config.MyKeyGenerator;
import com.zfsoft.microservice.workflow.data.*;
import com.zfsoft.microservice.workflow.dbaccess.dao.DbProAcceptExampleMapper;
import com.zfsoft.microservice.workflow.dbaccess.data.DbProAcceptExample;
import com.zfsoft.microservice.workflow.dbaccess.data.DbProAcceptExampleExample;
import com.zfsoft.microservice.workflow.feign.SysUserFeignService;
import com.zfsoft.microservice.workflow.util.AuthenticationUtil;
import com.zfsoft.microservice.workflow.util.PageUtils;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.Builder;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.common.workflow.TaskResult;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import de.odysseus.el.util.SimpleContext;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.el.ExpressionFactoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import java.util.*;

@Service
public class ProAcceptExampleManager {

    @Autowired
    private RepositoryService repositoryService;

    @Resource
    private DbProAcceptExampleMapper dbProAcceptExampleMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessRuntimeManager processRuntimeManager;

    @Autowired
    private TaskRuntimeManager taskRuntimeManager;

    @Autowired
    private WorkflowBussInfoManager workflowBussInfoManager;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private SysUserFeignService sysUserFeignService;

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private WorkflowBussFlowStepManager workflowBussFlowStepManager;

    public ApiResultSet<TaskResult> save(ProAcceptExample proAcceptExample) {
        WorkflowBussInfo info = workflowBussInfoManager.getWorkflowBussInfoByInfoOid(proAcceptExample.getInfoOid());
        if (proAcceptExample != null && proAcceptExample.getId() == null) {
            MyKeyGenerator k = new MyKeyGenerator();
            Comparable<?> key = k.generateKey();
            Long id = (Long) key;
            proAcceptExample.setId(id);
            proAcceptExample.setProjectNo(String.valueOf(id));
            proAcceptExample.setAcceptOid(IdUtil.simpleUUID());
            proAcceptExample.setCreateDate(new Date());
            proAcceptExample.setIsDelete(BaseStaticParameter.N);
        }
        DbProAcceptExample dbProAcceptExample = new DbProAcceptExample();
        BeanUtils.copyProperties(proAcceptExample,dbProAcceptExample);
        dbProAcceptExampleMapper.insert(dbProAcceptExample);
        Map<String, Object> jsonObject = processRuntimeManager.startProcessInstance(info.getInfoOid(), proAcceptExample.getProjectNo(), null);
        //JSONObject jsonObject = JSONObject.parseObject(instance);
        dbProAcceptExample.setProcessInstanceId(jsonObject.get("processInstanceId").toString());
        dbProAcceptExampleMapper.updateByPrimaryKeySelective(dbProAcceptExample);
        return new ApiResultSet<>();
    }


    /**
     * 查询model列表
     *
     * @param id
     * @param name
     * @param category
     * @return
     * @author zxx
     * @date 2021-01-27 15:38
     */
    public List<Model> model(String id, String name, String category) {
        ModelQuery modelQuery = repositoryService.createModelQuery();
        if (StrUtil.isNotBlank(id)) {
            modelQuery.modelId(id);
        }
        if (StrUtil.isNotBlank(name)) {
            modelQuery.modelNameLike(name);
        }
        if (StrUtil.isNotBlank(category)) {
            modelQuery.modelCategoryLike(category);
        }
        return repositoryService.createModelQuery().orderByLastUpdateTime().desc().list();
    }

    //待办任务
    public PageResult page(String name, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        int startNum = pageSize * (pageNumber - 1);
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery.taskCandidateOrAssigned(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());

        if (StrUtil.isNotBlank(name)) {
            taskQuery.processInstanceBusinessKeyLike(StrUtil.format("%{}%", "BUSINESS" + name));
        }
        taskQuery.orderByTaskCreateTime().desc();
        long count = taskQuery.count();
        List<Task> taskList = taskQuery.listPage(startNum, pageSize);
        //List<Task> taskList = taskQuery.list();
        List<ProProcessExample> proProcessExamples = new ArrayList<>();
        //设置当前登录人
        AuthenticationUtil.setAuthentication(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
        taskList.forEach(task -> {
            ProcessInstance instance = processRuntime.processInstance(task.getProcessInstanceId());
            if(WorkflowStaticParameter.WORK_FLOW_SRATUS_SUSPENDED.equals(instance.getStatus().name())){
                return;
            }
            DbProAcceptExampleExample dbProAcceptExampleExample = new DbProAcceptExampleExample();
            dbProAcceptExampleExample.createCriteria().andProcessInstanceIdEqualTo(task.getProcessInstanceId());
            dbProAcceptExampleExample.setOrderByClause("ID DESC");
            List<DbProAcceptExample> dbProAcceptExamples = dbProAcceptExampleMapper.selectByExample(dbProAcceptExampleExample);
            DbProAcceptExample dbProAcceptExample = null!=dbProAcceptExamples && dbProAcceptExamples.size()>0?dbProAcceptExamples.get(0):null;
            ProAcceptExample mongoTemplateOne = null;
            if(null != dbProAcceptExample){
                mongoTemplateOne = new ProAcceptExample();
                BeanUtils.copyProperties(dbProAcceptExample,mongoTemplateOne);
            }
            ProProcessExample proProcessExample = null;
            if (mongoTemplateOne != null) {
                proProcessExample = Builder.of(ProProcessExample::new)
                        .with(ProProcessExample::setTaskId, task.getId())
                        .with(ProProcessExample::setProjectNo, mongoTemplateOne.getProjectNo())
                        .with(ProProcessExample::setApplyerName, mongoTemplateOne.getApplyerName())
                        .with(ProProcessExample::setProcessName, task.getName())
                        .with(ProProcessExample::setCreateDate, task.getCreateTime())
                        .with(ProProcessExample::setProjectName, mongoTemplateOne.getProjectName())
                        .with(ProProcessExample::setProcessInstanceId, task.getProcessInstanceId())
                        .build();
            }else{
                proProcessExample = Builder.of(ProProcessExample::new)
                        .with(ProProcessExample::setTaskId, task.getId())
                        .with(ProProcessExample::setProjectNo, task.getId())
                        .with(ProProcessExample::setApplyerName, task.getAssignee())
                        .with(ProProcessExample::setProcessName, task.getName())
                        .with(ProProcessExample::setCreateDate, task.getCreateTime())
                        .with(ProProcessExample::setProjectName, "任务id:"+task.getId())
                        .with(ProProcessExample::setProcessInstanceId, task.getProcessInstanceId())
                        .build();
            }
            proProcessExamples.add(proProcessExample);
        });
        List<ProProcessExample> list = PageUtils.startPage(proProcessExamples,pageNumber,pageSize);
        PageResult<ProProcessExample> page = new PageResult<>(pageNumber, pageSize, count);
        page.setData(list);
        return page;
    }

    //已办任务
    public PageResult completedTasksPage(String name, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        int startNum = pageSize * (pageNumber - 1);
        String userOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
        List<HistoricTaskInstance> taskList = null;
        long count = 0L;
        if(StrUtil.isNotBlank(name)){
            taskList = historyService
                    .createHistoricTaskInstanceQuery()
                    .processInstanceBusinessKeyLike(StrUtil.format("%{}%", "BUSINESS" + name))
                    .orderByHistoricTaskInstanceEndTime().finished().desc()
                    .taskAssignee(userOid).listPage(startNum,pageSize);
            count = historyService.createHistoricTaskInstanceQuery().processInstanceBusinessKeyLike(StrUtil.format("%{}%", "BUSINESS" + name)).finished().taskAssignee(userOid).count();
        }else {
            taskList = historyService
                    .createHistoricTaskInstanceQuery()
                    //.processInstanceBusinessKeyLike(StrUtil.format("%{}%", "BUSINESS" + name))
                    .orderByHistoricTaskInstanceEndTime().finished().desc()
                    .taskAssignee(userOid).listPage(startNum,pageSize);
            count = historyService.createHistoricTaskInstanceQuery().finished().taskAssignee(userOid).count();
        }
        List<ProProcessExample> proProcessExamples = new ArrayList<>();
        for(HistoricTaskInstance task: taskList){
            /*if(null == task.getEndTime()){
                continue;
            }*/

            DbProAcceptExampleExample dbProAcceptExampleExample = new DbProAcceptExampleExample();
            dbProAcceptExampleExample.createCriteria().andProcessInstanceIdEqualTo(task.getProcessInstanceId());
            dbProAcceptExampleExample.setOrderByClause("ID DESC");
            List<DbProAcceptExample> dbProAcceptExamples = dbProAcceptExampleMapper.selectByExample(dbProAcceptExampleExample);
            DbProAcceptExample dbProAcceptExample = null!=dbProAcceptExamples && dbProAcceptExamples.size()>0?dbProAcceptExamples.get(0):null;
            ProAcceptExample mongoTemplateOne = null;
            if(null != dbProAcceptExample){
                mongoTemplateOne = new ProAcceptExample();
                BeanUtils.copyProperties(dbProAcceptExample,mongoTemplateOne);
            }
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            String superProcessInstanceId = historicProcessInstance.getSuperProcessInstanceId();
            ProProcessExample proProcessExample = null;
            if (mongoTemplateOne != null) {
                proProcessExample = Builder.of(ProProcessExample::new)
                        .with(ProProcessExample::setTaskId, task.getId())
                        .with(ProProcessExample::setProjectNo, mongoTemplateOne.getProjectNo())
                        .with(ProProcessExample::setApplyerName, mongoTemplateOne.getApplyerName())
                        .with(ProProcessExample::setProcessName, task.getName())
                        .with(ProProcessExample::setCreateDate, task.getCreateTime())
                        .with(ProProcessExample::setEndDate, task.getEndTime())
                        .with(ProProcessExample::setProjectName, mongoTemplateOne.getProjectName())
                        .with(ProProcessExample::setProcessInstanceId, task.getProcessInstanceId())
                        .build();
            }else{
                proProcessExample = Builder.of(ProProcessExample::new)
                        .with(ProProcessExample::setTaskId, task.getId())
                        .with(ProProcessExample::setProjectNo, task.getProcessInstanceId())
                        .with(ProProcessExample::setApplyerName, task.getAssignee())
                        .with(ProProcessExample::setProcessName, task.getName())
                        .with(ProProcessExample::setCreateDate, task.getCreateTime())
                        .with(ProProcessExample::setEndDate, task.getEndTime())
                        .with(ProProcessExample::setProjectName, "任务id:"+task.getId())
                        .with(ProProcessExample::setProcessInstanceId, task.getProcessInstanceId())
                        .with(ProProcessExample::setSuperProcessInstanceId, superProcessInstanceId)
                        .build();
            }
            proProcessExamples.add(proProcessExample);
        }
        /*int count = proProcessExamples.size();
        List<ProProcessExample> list = PageUtils.startPage(proProcessExamples,pageNumber,pageSize);*/
        PageResult<ProProcessExample> page = new PageResult<>(pageNumber, pageSize, Integer.valueOf(count+""));
        page.setData(proProcessExamples);
        return page;
    }
    /**
     * 下一步
     *
     * @param proProcessExample
     * @return
     */
    public ApiResultSet<String> nextStep(ProProcessExample proProcessExample) {
        if (proProcessExample != null && proProcessExample.getId() == null) {
            MyKeyGenerator k = new MyKeyGenerator();
            Comparable<?> key = k.generateKey();
            Long id = (Long) key;
            proProcessExample.setId(id);
            proProcessExample.setProjectNo(String.valueOf(id));
            proProcessExample.setProcessOid(IdUtil.simpleUUID());
        }
        Map<String, Object> variableMap = null;
        if (StrUtil.isNotBlank(proProcessExample.getHandleParamKey())) {
            variableMap = new HashMap<>();
            String handleParamKeys = proProcessExample.getHandleParamKey();
            String handleParamValues= proProcessExample.getHandleParamValue();
            if(StrUtil.isNotBlank(handleParamKeys) && StrUtil.isNotBlank(handleParamValues)){
                String[] handleParamKeyArr = handleParamKeys.split(",");
                String[] handleParamValueArr = handleParamValues.split(",");
                for(int i=0;i<handleParamKeyArr.length;i++){
                    variableMap.put(handleParamKeyArr[i], handleParamValueArr[i]);
                }
            }
        }
        boolean b = taskRuntimeManager.completeTask(proProcessExample.getTaskId(), variableMap);
        ApiResultSet<String> apiResultSet = new ApiResultSet<String>();
        if (b) {
            //mongoTemplate.save(proProcessExample);
            DbProAcceptExample dbProAcceptExample = new DbProAcceptExample();
            BeanUtils.copyProperties(proProcessExample,dbProAcceptExample);
            dbProAcceptExampleMapper.insert(dbProAcceptExample);
            return apiResultSet;
        } else {
            apiResultSet.setCode(ApiResultSet.DIRTY_DATA_TITLE);
            apiResultSet.setMessage("失败！");
            return apiResultSet;
        }

    }

    /**
     * @param processInstanceId 流程实列id
     * @description: 不通过流程实例
     * @author: wuxx
     * @Date: 2021/1/13 14:30
     **/
    public String noPassProcessInstance(String processInstanceId,String taskId) {
        try {
            String noPassReason="审核不通过";
            String userId = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
            noPassReason = noPassReason +"~" + userId + "~" + this.getLoginUserName(userId);
            taskRuntimeManager.noPassProcessInstance(processInstanceId,noPassReason,taskId);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResultInfoException("不通过流程实例异常！", e);
        }
    }

    /**
     * @description: 根据用户oid获取名称
     * @param userId 用户oid
     * @author: wuxx
     * @Date: 2021/2/25 16:08
     **/
    public String getLoginUserName(String userId){
        if(StrUtil.isNotEmpty(userId)){
            SysUser user = sysUserFeignService.getSysUserByUserOid(userId).getData();
            if(null!=user){
                return user.getName();
            }
        }
        return null;
    }

    /**
     * @description:  驳回
     * @param proProcessExample
     * @author: wuxx
     * @Date: 2021/2/8 13:19
     **/
    public ApiResultSet<String> rejectTaskStep(ProProcessExample proProcessExample) {
        if (proProcessExample != null && proProcessExample.getId() == null) {
            MyKeyGenerator k = new MyKeyGenerator();
            Comparable<?> key = k.generateKey();
            Long id = (Long) key;
            proProcessExample.setId(id);
            proProcessExample.setProjectNo(String.valueOf(id));
            proProcessExample.setProcessOid(IdUtil.simpleUUID());
        }
        Map<String, Object> variableMap = null;
        if (StrUtil.isNotBlank(proProcessExample.getHandleParamKey())) {
            variableMap = new HashMap<>();
            variableMap.put(proProcessExample.getHandleParamKey(), proProcessExample.getHandleParamValue());
        }
        String userId = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
        String rejectTaskMes = taskRuntimeManager.rejectTask(proProcessExample.getTaskId(),proProcessExample.getRejectTaskId(), variableMap,userId);
        ApiResultSet<String> apiResultSet = new ApiResultSet<String>();
        if (StrUtil.isEmpty(rejectTaskMes)) {
            //mongoTemplate.save(proProcessExample);
            DbProAcceptExample dbProAcceptExample = new DbProAcceptExample();
            BeanUtils.copyProperties(proProcessExample,dbProAcceptExample);
            dbProAcceptExampleMapper.insert(dbProAcceptExample);
            return apiResultSet;
        } else {
            apiResultSet.setCode(ApiResultSet.DIRTY_DATA_TITLE);
            apiResultSet.setMessage(rejectTaskMes);
            return apiResultSet;
        }

    }

    /**
     * @description: 驳回
     * @param proProcessExample
     * @author: wuxx
     * @Date: 2021/2/8 13:19
     **/
    public ApiResultSet<String> fallbackTaskStep(ProProcessExample proProcessExample) {
        if (proProcessExample != null && proProcessExample.getId() == null) {
            MyKeyGenerator k = new MyKeyGenerator();
            Comparable<?> key = k.generateKey();
            Long id = (Long) key;
            proProcessExample.setId(id);
            proProcessExample.setProjectNo(String.valueOf(id));
            proProcessExample.setProcessOid(IdUtil.simpleUUID());
        }
        Map<String, Object> variableMap = null;
        if (StrUtil.isNotBlank(proProcessExample.getHandleParamKey())) {
            variableMap = new HashMap<>();
            variableMap.put(proProcessExample.getHandleParamKey(), proProcessExample.getHandleParamValue());
        }
        String userId = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
        String rejectTaskMes = taskRuntimeManager.fallbackTask(proProcessExample.getTaskId(),proProcessExample.getFallbackTaskId(), variableMap,userId);
        ApiResultSet<String> apiResultSet = new ApiResultSet<String>();
        if (StrUtil.isEmpty(rejectTaskMes)) {
            //mongoTemplate.save(proProcessExample);
            DbProAcceptExample dbProAcceptExample = new DbProAcceptExample();
            BeanUtils.copyProperties(proProcessExample,dbProAcceptExample);
            dbProAcceptExampleMapper.insert(dbProAcceptExample);
            return apiResultSet;
        } else {
            apiResultSet.setCode(ApiResultSet.DIRTY_DATA_TITLE);
            apiResultSet.setMessage(rejectTaskMes);
            return apiResultSet;
        }

    }


    /**
     * 任务条件获取
     *
     * @param taskId
     * @return
     */
    public Map<String, Object> getTaskCondition(String taskId) {
        Map<String, Object> map = new HashMap<>();
        List<String> taskCondition = new ArrayList<>();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        // 取得已提交的任务
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
                .taskId(task.getId()).singleResult();
        // 获得流程定义
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(historicTaskInstance.getProcessDefinitionId());
        //获得当前流程的活动ID
        ExecutionQuery executionQuery = runtimeService.createExecutionQuery();
        Execution execution = executionQuery.executionId(historicTaskInstance.getExecutionId()).singleResult();
        String activityId = execution.getActivityId();
        UserTask userTask = null;
        //根据活动节点获取当前的组件信息
        FlowNode flowNode = getFlowNode(processDefinition.getId(), activityId);
        //获取该节点之后的流向
        List<SequenceFlow> sequenceFlowListOutGoing = flowNode.getOutgoingFlows();
        // 获取的下个节点不一定是userTask的任务节点，所以要判断是否是任务节点
        if (sequenceFlowListOutGoing.size() > 1) {
            // 如果有1条以上的出线，表示有分支，需要判断分支的条件才能知道走哪个分支
            // 遍历节点的出线得到下个activityId
//                activityId = getNextActivityId(execution.getId(),
//                        task.getProcessInstanceId(), sequenceFlowListOutGoing);
            // return null;

            // 只有1条出线,直接取得下个节点
            SequenceFlow sequenceFlow = sequenceFlowListOutGoing.get(1);
            // 下个节点
            FlowElement flowElement = sequenceFlow.getTargetFlowElement();
            if (flowElement instanceof UserTask) {
                // 下个节点为UserTask时
                userTask = (UserTask) flowElement;
                List<SequenceFlow> outgoingFlows = userTask.getOutgoingFlows();
                outgoingFlows.forEach(outgoingFlow -> {
                    if (StrUtil.isNotBlank(outgoingFlow.getConditionExpression())) {
                        taskCondition.add(outgoingFlow.getConditionExpression());
                    }
                });
            } else if (flowElement instanceof ExclusiveGateway) {
                // 下个节点为排它网关时
                ExclusiveGateway exclusiveGateway = (ExclusiveGateway) flowElement;
                List<SequenceFlow> outgoingFlows = exclusiveGateway.getOutgoingFlows();
                outgoingFlows.forEach(outgoingFlow -> {
                    if (StrUtil.isNotBlank(outgoingFlow.getConditionExpression())) {
                        taskCondition.add(outgoingFlow.getConditionExpression());
                    }
                });
            }
        } else if (sequenceFlowListOutGoing.size() == 1) {
            // 只有1条出线,直接取得下个节点
            SequenceFlow sequenceFlow = sequenceFlowListOutGoing.get(0);
            // 下个节点
            FlowElement flowElement = sequenceFlow.getTargetFlowElement();
            if (flowElement instanceof UserTask) {
                // 下个节点为UserTask时
                userTask = (UserTask) flowElement;
                List<SequenceFlow> outgoingFlows = userTask.getOutgoingFlows();
                outgoingFlows.forEach(outgoingFlow -> {
                    if (StrUtil.isNotBlank(outgoingFlow.getConditionExpression())) {
                        taskCondition.add(outgoingFlow.getConditionExpression());
                    }
                });
            } else if (flowElement instanceof ExclusiveGateway) {
                // 下个节点为排它网关时
                ExclusiveGateway exclusiveGateway = (ExclusiveGateway) flowElement;
                List<SequenceFlow> outgoingFlows = exclusiveGateway.getOutgoingFlows();
                outgoingFlows.forEach(outgoingFlow -> {
                    if (StrUtil.isNotBlank(outgoingFlow.getConditionExpression())) {
                        taskCondition.add(outgoingFlow.getConditionExpression());
                    }
                });
            }
        } else {
            // 没有出线，则表明是结束节点
            //return null;
        }
        //获取当前节点信息
        WorkflowBussFlowStep step = workflowBussFlowStepManager.getWorkflowBussFlowStepByActivityIdAndDefId(task.getTaskDefinitionKey(), task.getProcessDefinitionId());
        map.put("taskCondition",taskCondition);
        //map.put("step",step);
        //驳回
        if(StrUtil.isNotBlank(step.getRejectTaskId())){
            String[] splitTaskId = step.getRejectTaskId().split(",");
            Map<String, String> mapBack = new HashMap();
            for (String rejectTaskId : splitTaskId){
                WorkflowBussFlowStep selectStep = new WorkflowBussFlowStep();
                selectStep.setInfoOid(step.getInfoOid());
                selectStep.setProcessDefId(step.getProcessDefId());
                selectStep.setActivityId(rejectTaskId);
                List<WorkflowBussFlowStep> bussFlowStepList = workflowBussFlowStepManager.queryWorkflowBussFlowStepList(selectStep);
                String stepName = bussFlowStepList.size()>0?bussFlowStepList.get(0).getName():null;
                mapBack.put(rejectTaskId,"驳回至"+stepName);
            }
            map.put("rejectTaskIdMap",mapBack);
        }
        //退回
        if(StrUtil.isNotBlank(step.getFallbackTaskId())){
            String[] splitTaskId = step.getFallbackTaskId().split(",");
            Map<String, String> mapBack = new HashMap();
            for (String fallbackTaskId : splitTaskId){
                WorkflowBussFlowStep selectStep = new WorkflowBussFlowStep();
                selectStep.setInfoOid(step.getInfoOid());
                selectStep.setProcessDefId(step.getProcessDefId());
                selectStep.setActivityId(fallbackTaskId);
                List<WorkflowBussFlowStep> bussFlowStepList = workflowBussFlowStepManager.queryWorkflowBussFlowStepList(selectStep);
                String stepName = bussFlowStepList.size()>0?bussFlowStepList.get(0).getName():null;
                mapBack.put(fallbackTaskId,"退回至"+stepName);
            }
            map.put("fallbackTaskIdMap",mapBack);
        }
        return map;
    }


    /**
     * 获取当前任务节点的下一个任务节点
     *
     * @param task 当前任务节点
     * @return 下个任务节点
     * @throws Exception
     */
    public FlowElement getNextUserFlowElement(Task task) throws Exception {
        // 取得已提交的任务
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(task.getId()).singleResult();
        // 获得流程定义
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(historicTaskInstance.getProcessDefinitionId());
        //获得当前流程的活动ID
        ExecutionQuery executionQuery = runtimeService.createExecutionQuery();
        Execution execution = executionQuery.executionId(historicTaskInstance.getExecutionId()).singleResult();
        String activityId = execution.getActivityId();
        UserTask userTask = null;
        while (true) {
            //根据活动节点获取当前的组件信息
            FlowNode flowNode = getFlowNode(processDefinition.getId(), activityId);
            if (flowNode == null) {
                return null;
            }
            //获取该节点之后的流向
            List<SequenceFlow> sequenceFlowListOutGoing = flowNode.getOutgoingFlows();
            // 获取的下个节点不一定是userTask的任务节点，所以要判断是否是任务节点
            if (sequenceFlowListOutGoing.size() > 1) {
                // 如果有1条以上的出线，表示有分支，需要判断分支的条件才能知道走哪个分支
                // 遍历节点的出线得到下个activityId
                activityId = getNextActivityId(execution.getId(),
                        task.getProcessInstanceId(), sequenceFlowListOutGoing);
            } else if (sequenceFlowListOutGoing.size() == 1) {
                // 只有1条出线,直接取得下个节点
                SequenceFlow sequenceFlow = sequenceFlowListOutGoing.get(0);
                // 下个节点
                FlowElement flowElement = sequenceFlow.getTargetFlowElement();
                if (flowElement instanceof UserTask) {
                    // 下个节点为UserTask时
                    userTask = (UserTask) flowElement;
                    System.out.println("下个任务为:" + userTask.getName());
                    return userTask;
                } else if (flowElement instanceof ExclusiveGateway) {
                    // 下个节点为排它网关时
                    ExclusiveGateway exclusiveGateway = (ExclusiveGateway) flowElement;
                    List<SequenceFlow> outgoingFlows = exclusiveGateway.getOutgoingFlows();
                    // 遍历网关的出线得到下个activityId
                    activityId = getNextActivityId(execution.getId(), task.getProcessInstanceId(), outgoingFlows);
                }
            } else {
                // 没有出线，则表明是结束节点
                return null;
            }
        }
    }

    /**
     * 根据活动节点和流程定义ID获取该活动节点的组件信息
     */
    public FlowNode getFlowNode(String processDefinitionId, String flowElementId) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(flowElementId);
        return flowNode;
    }

    /**
     * 根据el表达式取得满足条件的下一个activityId
     *
     * @param executionId
     * @param processInstanceId
     * @param outgoingFlows
     * @return
     */
    public String getNextActivityId(String executionId,
                                    String processInstanceId,
                                    List<SequenceFlow> outgoingFlows) {
        String activityId = null;
        // 遍历出线
        for (SequenceFlow outgoingFlow : outgoingFlows) {
            // 取得线上的条件
//            String conditionExpression = outgoingFlow.getConditionExpression();
//            // 取得所有变量
//            Map<String, Object> variables = runtimeService.getVariables(executionId);
//            String variableName = "";
//            // 判断网关条件里是否包含变量名
//            for (String s : variables.keySet()) {
//                if (conditionExpression.contains(s)) {
//                    // 找到网关条件里的变量名
//                    variableName = s;
//                }
//            }
//            String conditionVal = getVariableValue(variableName, processInstanceId);
//            // 判断el表达式是否成立
//            if (isCondition(variableName, conditionExpression, conditionVal)) {
//                // 取得目标节点
//                FlowElement targetFlowElement = outgoingFlow.getTargetFlowElement();
//                activityId = targetFlowElement.getId();
//                continue;
//            }
        }
        return activityId;
    }

    /**
     * 取得流程变量的值
     *
     * @param variableName      变量名
     * @param processInstanceId 流程实例Id
     * @return
     */
    public String getVariableValue(String variableName, String processInstanceId) {
        Execution execution = runtimeService
                .createExecutionQuery().processInstanceId(processInstanceId).list().get(0);
        Object object = runtimeService.getVariable(execution.getId(), variableName);
        return object == null ? "" : object.toString();
    }

    /**
     * 根据key和value判断el表达式是否通过
     *
     * @param key   el表达式key
     * @param el    el表达式
     * @param value el表达式传入值
     * @return
     */
    public boolean isCondition(String key, String el, String value) {
        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext context = new SimpleContext();
        context.setVariable(key, factory.createValueExpression(value, String.class));
        ValueExpression e = factory.createValueExpression(context, el, boolean.class);
        return (Boolean) e.getValue(context);
    }


}
