package com.zfsoft.microservice.workflow.manager;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.zfsoft.microservice.workflow.data.WorkflowBussInfo;
import com.zfsoft.microservice.workflow.data.WorkflowHistoryVo;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import org.activiti.editor.language.json.converter.util.CollectionUtils;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: kkfan
 * @create: 2021-02-01 11:41:58
 * @description: 流程历史
 */
@Service
public class WorkflowHistoryManager {

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private WorkflowBussInfoManager workflowBussInfoManager;

    @Autowired
    private RepositoryService repositoryService;

    public PageResult queryWorkflowHistoryWithPage(String infoOid, Integer pageNum, Integer pageSize) {
        if(StringUtils.isEmpty(infoOid)) {
            throw new ResultInfoException("流程信息业务主键不能为空！");
        }
        WorkflowBussInfo workflowBussInfo = this.workflowBussInfoManager.getWorkflowBussInfoByInfoOid(infoOid);
        if(null == workflowBussInfo) {
            throw new ResultInfoException("流程信息不能为空！");
        }
        //开启流程之前先寻找最新版本的部署得历史流程
        List<ProcessDefinition> definitionList = repositoryService.createProcessDefinitionQuery().processDefinitionKey("Process_"+infoOid).orderByProcessDefinitionVersion().desc()
                .listPage((pageNum - 1) * pageSize, pageSize);
        List<WorkflowHistoryVo> workflowHistoryVoList = new ArrayList<>();
        for(ProcessDefinition definition:definitionList){
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(definition.getDeploymentId()).singleResult();
            WorkflowHistoryVo historyVo = WorkflowHistoryVo.builder()
                    .id(definition.getId())
                    .workflowInfoId(workflowBussInfo.getId())
                    .workflowInfoOid(workflowBussInfo.getInfoOid())
                    .deployTime(DateUtil.format(deployment.getDeploymentTime(), "yyyy-MM-dd HH:mm:ss"))
                    .deployStatus((StringUtils.isNotEmpty(workflowBussInfo.getModelId().toString()) && StringUtils.equals(definition.getId(), workflowBussInfo.getModelId().toString())) ? "启用" : "禁用")
                    .processDefId(definition.getId())
                    .version(definition.getVersion())
                    .processKey(definition.getKey())
                    .deploymentId(definition.getDeploymentId())
                    .name(workflowBussInfo.getWorkflowName())
                    .build();
            workflowHistoryVoList.add(historyVo);
        }


       /* List<Model> modelList = this.processEngine.getRepositoryService()
                .createModelQuery()
                .modelKey(infoOid)
                .orderByLastUpdateTime()
                .desc()
                .listPage((pageNum - 1) * pageSize, pageSize);
        Integer totalSize = this.processEngine.getRepositoryService()
                .createModelQuery()
                .modelKey(infoOid)
                .list()
                .size();
        List<WorkflowHistoryVo> workflowHistoryVoList = Optional.ofNullable(modelList)
                .orElseGet(Lists::newArrayList)
                .stream()
                .map(model -> WorkflowHistoryVo.builder()
                        .id(model.getId())
                        .workflowInfoId(workflowBussInfo.getId())
                        .workflowInfoOid(workflowBussInfo.getInfoOid())
                        .deployTime(DateUtil.format(model.getCreateTime(), "yyyy-MM-dd HH:mm:ss"))
                        .deployStatus((StringUtils.isNotEmpty(workflowBussInfo.getModelId().toString()) && StringUtils.equals(model.getId(), workflowBussInfo.getModelId().toString())) ? "启用" : "禁用")
                        .modelId(model.getId())
                        .version(model.getVersion())
                        .processKey(model.getKey())
                        .deploymentId(model.getDeploymentId())
                        .name(workflowBussInfo.getWorkflowName())
                        .build())
                .collect(Collectors.toList());*/

        Integer totalSize = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionKey("Process_"+infoOid)
                .list()
                .size();
        PageResult<WorkflowHistoryVo> pageResult = new PageResult<>(pageNum, pageSize, totalSize);
        pageResult.setData(workflowHistoryVoList);
        return pageResult;
    }

    public void deleteHistoryByModelId(String modelId) {
        if(StringUtils.isEmpty(modelId)) {
            throw new ResultInfoException("模型主键不能为空！");
        }
        Model model = this.processEngine.getRepositoryService().createModelQuery().modelId(modelId).singleResult();
        if(StringUtils.isNotEmpty(model.getDeploymentId())) {
            this.processEngine.getRepositoryService().deleteDeployment(model.getDeploymentId(), true);
        }
        this.processEngine.getRepositoryService().deleteModel(modelId);
    }

    public void deleteModelByWorkflowInfoOid(String infoOid) {
        if(StringUtils.isEmpty(infoOid)) {
            throw new ResultInfoException("流程信息业务主键不能为空！");
        }
        List<Model> modelList = this.processEngine.getRepositoryService()
                .createModelQuery()
                .modelKey(infoOid)
                .list();
        Optional.ofNullable(modelList)
                .orElseGet(Lists::newArrayList)
                .forEach(model -> this.deleteHistoryByModelId(model.getId()));
    }

}
