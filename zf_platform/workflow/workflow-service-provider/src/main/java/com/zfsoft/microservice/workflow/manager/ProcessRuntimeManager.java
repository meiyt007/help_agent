package com.zfsoft.microservice.workflow.manager;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.zfsoft.microservice.workflow.data.WorkflowBussInfo;
import com.zfsoft.microservice.workflow.data.WorkflowType;
import com.zfsoft.microservice.workflow.util.AuthenticationUtil;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: activiti7 新特性接口的实现类
 * @author: wuxx
 * @Date: 2021/1/13 9:16
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "workflow:processRuntime")
public class ProcessRuntimeManager{

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private TaskRuntimeManager taskRuntimeManager;

    @Autowired
    private WorkflowBussInfoManager workflowBussInfoManager;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private WorkflowTypeManager workflowTypeManager;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;
    /**
     * 启动流程实例
     * @author: zhongxx
     * @Date: 2021-02-05 16:34
     * @param infoOid 流程信息唯一标识
     * @param businessKey 业务标识
     * @param variableMap 参数(非必填)
     * @return
     */
    public Map<String, Object> startProcessInstance(String infoOid, String businessKey, Map<String, Object> variableMap) {
        WorkflowBussInfo workflowBussInfo = workflowBussInfoManager.getWorkflowBussInfoByInfoOid(infoOid);
        if(null==workflowBussInfo || BaseStaticParameter.Y != workflowBussInfo.getIsPublish()){
            throw new ResultInfoException("流程信息不存在！");
        }
        WorkflowType type = workflowTypeManager.getWorkflowTypeByTypeOid(workflowBussInfo.getTypeOid());
        Model model = repositoryService.createModelQuery().modelId(String.valueOf(workflowBussInfo.getModelId())).singleResult();
        if(null==model){
            throw new ResultInfoException("流程实例不存在！");
        }
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(model.getDeploymentId()).singleResult();
        String key = "APP" + workflowBussInfo.getAppOid() + "^^^^TYPE" + type.getCode() + "^^^^INFO" + workflowBussInfo.getInfoOid() + "^^^^BUSINESS" + businessKey;
        ProcessInstance pi = null;
        if (null != variableMap) {
            pi = processRuntime.start(ProcessPayloadBuilder
                    .start()
                    .withProcessDefinitionId(processDefinition.getId())
                    .withProcessDefinitionKey(processDefinition.getKey())
                    .withVariables(variableMap)
                    .withName(workflowBussInfo.getWorkflowName())
                    .withBusinessKey(key)
                    .build());

        } else {
            pi = processRuntime.start(ProcessPayloadBuilder
                    .start()
                    .withProcessDefinitionId(processDefinition.getId())
                    .withProcessDefinitionKey(processDefinition.getKey())
                    .withName(workflowBussInfo.getWorkflowName())
                    .withBusinessKey(key)
                    .build());
        }
        Map<String, Object> reulstMap = new HashMap<>(6);
        reulstMap.put("processInstanceId", pi.getId());
        reulstMap.put("name", pi.getName());
        reulstMap.put("startDate", pi.getStartDate());
        //  RUNNING（运行）PAUSED（暂停）STOPPED（停止）DESTROYED（销毁）
        reulstMap.put("status", pi.getStatus());
        reulstMap.put("processDefinitionId", pi.getProcessDefinitionId());
        //reulstMap.put("processDefinitionKey", pi.getProcessDefinitionKey());
        //更新应办理时间
        //测试性能暂时去除
        taskRuntimeManager.updateDueDateByTaskIdOrInstanceId(null, pi.getId());
        return reulstMap;
    }

    /**
     * @description: 查看所有的流程实列
     * @author: wuxx
     * @Date: 2021/1/13 10:48
     **/
    public String queryAllProcessInstances(Integer pageNumber, Integer pageSize) {
        try {
            //分页参数
            if (null == pageNumber || pageNumber <= 1) {
                pageNumber = 1;
            }
            if (null == pageSize || pageSize <= 0) {
                pageSize = 10;
            }
            Page<ProcessInstance> processInstancePage = processRuntime
                    .processInstances(Pageable.of(pageNumber, pageSize));
            List<ProcessInstance> list = processInstancePage.getContent();
            list.sort((y, x) -> x.getProcessDefinitionVersion() - y.getProcessDefinitionVersion());
            List<Map<String, Object>> definitionsList = new ArrayList<>();
            for (ProcessInstance pi : list) {
                Map<String, Object> reulstMap = new HashMap<>();
                reulstMap.put("processInstanceId", pi.getId());
                reulstMap.put("name", pi.getName());
                reulstMap.put("startDate", pi.getStartDate());
                reulstMap.put("status", pi.getStatus());
                reulstMap.put("processDefinitionId", pi.getProcessDefinitionId());
                reulstMap.put("processDefinitionKey", pi.getProcessDefinitionKey());
                definitionsList.add(reulstMap);
            }
            return JSONUtil.toJsonStr(definitionsList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResultInfoException("查看所有的流程实列异常！", e);
        }
    }

    /**
     * @param processInstanceId 流程实列id
     * @description: 暂停流程实例
     * @author: wuxx
     * @Date: 2021/1/13 14:30
     **/
    public boolean suspendProcessInstance(String processInstanceId) {
        try {
            processRuntime.suspend(ProcessPayloadBuilder
                    .suspend()
                    .withProcessInstanceId(processInstanceId)
                    .build()
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResultInfoException("暂停流程实例异常！", e);
        }
    }

    /**
     * @param processInstanceId 流程实列id
     * @description: 激活流程实例
     * @author: wuxx
     * @Date: 2021/1/13 14:30
     **/
    public boolean resumeProcessInstance(String processInstanceId) {
        try {
            processRuntime.resume(ProcessPayloadBuilder
                    .resume()
                    .withProcessInstanceId(processInstanceId)
                    .build()
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResultInfoException("激活流程实例异常！", e);
        }
    }

    /**
     * @param processInstanceId 流程实列id
     * @param deleteReason      删除原因
     * @description: 删除流程实例
     * @author: wuxx
     * @Date: 2021/1/13 14:30
     **/
    public boolean deleteProcessInstance(String processInstanceId, String deleteReason) {
        try {
            //当前待审批节点定义Id集合
            //List<org.activiti.engine.task.Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
            processRuntime.delete(ProcessPayloadBuilder
                    .delete()
                    .withProcessInstanceId(processInstanceId)
                    .withReason(deleteReason)
                    .build()
            );
            //删除所有的历史记录
            //historyService.deleteHistoricProcessInstance(processInstanceId);
            //删除待办历史记录
            /*if(null!=taskList && taskList.size()>0){
                for(org.activiti.engine.task.Task task:taskList){
                    historyService.deleteHistoricTaskInstance(task.getId());
                }
            }*/
            //判断是否有内嵌流程，存在内嵌流程，中止父类
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
                            taskRuntimeManager.completeTask(taskNew.getId(),null);
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
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResultInfoException("流程实例processInstanceId不存在！", e);
        }
    }


}
