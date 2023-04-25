package com.zfsoft.microservice.workflow.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.microservice.workflow.data.WorkflowBussFlowStep;
import com.zfsoft.microservice.workflow.manager.ProcessDesignManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @description: activiti 编辑model控制类 11
 * @author: wuxx
 * @Date: 2021/1/18 13:14
 **/
@RestController
@Slf4j
@RequestMapping("/security/workflow/model")
public class ProcessDesignController {

    @Autowired
    private ProcessDesignManager processDesignManager;

    @Autowired
    private ProcessDesignManager modelEditorJsonRestResource;

    /**
     * 保存模型内容
     * @param key
     * @param name 名称
     * @param category 类别
     * @param descp 描述
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ApiResultSet createModel(@RequestParam(required = false) String id,@RequestParam String key, @RequestParam String name,@RequestParam(required = false) String category, @RequestParam(required = false) String descp) throws UnsupportedEncodingException {
        processDesignManager.createModel(id,key, name,category, descp);
        return new ApiResultSet<>();
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Model> listModel() {
        List<Model> listModel = processDesignManager.listModel();
        return  listModel;
    }

    /**
     * @description: 分页查询model数据
     * @param name 名称
     * @param pageNum 页码
     * @param pageSize 页数
     * @author: wuxx
     * @Date: 2021/1/18 16:25
     **/
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ApiResultSet<PageResult<Model>> pageModel(String name, Integer pageNum, Integer pageSize) {
        PageResult<Model> pageModel = processDesignManager.pageModel(name, pageNum, pageSize);
        ApiResultSet<PageResult<Model>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageModel);
        return  apiResultSet;
    }

    /**
     * @description: 根据主键获取model对象
     * @author: wuxx
     * @Date: 2021/1/18 16:25
     **/
    @RequestMapping(value = "/getOne/{modelId}", method = RequestMethod.GET)
    public ApiResultSet getOne(@PathVariable String modelId) {
        Model model = processDesignManager.getOneModel(modelId);
        return new ApiResultSet<>(model);
    }

    /**
     * @description:  保存设计好的bpmn文件
     * @param bpmnDataJson bpmnJSON
     * @author: wuxx
     * @Date: 2021/1/20 14:15
     **/
    @RequestMapping(value = "/{infoOid}/xml/save", method = RequestMethod.POST)
    public ApiResultSet saveModelXml(@PathVariable(value = "infoOid") String infoOid, @RequestBody JSONObject bpmnDataJson) {
        Map values = bpmnDataJson.getJSONObject("bpmnDataJson").toJavaObject(Map.class);
        modelEditorJsonRestResource.saveModelXml(infoOid, values);
        return new ApiResultSet<>();
    }

    /**
     * @description:  删除模型
     * @param modelId 模型主键
     * @author: wuxx
     * @Date: 2021/1/20 14:16
     **/
    @ResponseBody
    @PostMapping(value = "/delete/{modelId}")
    public ApiResultSet flowDelete(@PathVariable(name = "modelId") String modelId){
        processDesignManager.deleteModel(modelId);
        return new ApiResultSet<>();
    }

    /**
     * 根据生成的ID获取模型流程编辑器
     * @param modelId
     * @return
     */
    @RequestMapping(value = "/{modelId}/xml", method = RequestMethod.GET)
    public ApiResultSet getEditorXml(@PathVariable String modelId) {
        JSONObject editorXml = modelEditorJsonRestResource.getEditorXml(modelId);
        return new ApiResultSet<>(editorXml);
    }

    /**
     * @description:  查看部署的流程图
     * @param modelId 模型主键
     * @author: wuxx
     * @Date: 2021/1/20 14:16
     **/
    @RequestMapping(value = "flowView/{modelId}", method = RequestMethod.GET)
    public ApiResultSet flowView(@PathVariable String modelId) {
        JSONObject editorXml = modelEditorJsonRestResource.getEditorXml(modelId);
        return new ApiResultSet<>(editorXml);
    }

    /**
     * @description:  根据modelId查看部署的流程图
     * @param modelId 流程信息业务主键
     * @author: wuxx
     * @Date: 2021/2/23 14:16
     **/
    @RequestMapping(value = "flowViewByModelId/{modelId}", method = RequestMethod.GET)
    public ApiResultSet flowViewByModelId(@PathVariable String modelId) {
        JSONObject editorXml = modelEditorJsonRestResource.getEditorXmlByModelId(modelId);
        return new ApiResultSet<>(editorXml);
    }

    /**
     * @description:  根据infoOid查看部署的流程图
     * @param infoOid 流程信息业务主键0
     * @author: wuxx
     * @Date: 2021/2/20 14:16
     **/
    @RequestMapping(value = "flowViewByInfoOid/{infoOid}", method = RequestMethod.GET)
    public ApiResultSet flowViewByInfoOid(@PathVariable String infoOid) {
        JSONObject editorXml = modelEditorJsonRestResource.getEditorXmlByInfoOid(infoOid);
        return new ApiResultSet<>(editorXml);
    }

    /**
     * @description:  根据流程部署主键查看部署的流程图
     * @param processDefId 流程部署主键
     * @author: wuxx
     * @Date: 2021/2/20 14:16
     **/
    @RequestMapping(value = "flowViewByProcessDefId/{processDefId}", method = RequestMethod.GET)
    public ApiResultSet flowViewByProcessDefId(@PathVariable String processDefId) {
        JSONObject editorXml = modelEditorJsonRestResource.flowViewByProcessDefId(processDefId);
        return new ApiResultSet<>(editorXml);
    }

    /**
     * @description:  部署模型
     * @param modelId 模型主键
     * @author: wuxx
     * @Date: 2021/1/20 14:16
     **/
    @PostMapping(value = "/deploy/{modelId}")
    public ApiResultSet deploy(@PathVariable(name = "modelId") String modelId) throws Exception {
        String deployModel = processDesignManager.deployModel(modelId);
        return new ApiResultSet<>(deployModel);
    }

    /**
     * @description: 根据流程实例ID获取流程图已办的节点
     * @param processInstanceId 流程实例ID
     * @author: wuxx
     * @Date: 2021/1/28 15:48
     **/
    @GetMapping(value="/viewFlow/{processInstanceId}")
    public ApiResultSet viewFlow(@PathVariable(name = "processInstanceId") String processInstanceId) {
        Map<String, Object> map = processDesignManager.viewFlow(processInstanceId);
        return new ApiResultSet<>(map);
    }

    /**
     * @description: 根据流程实例ID和任务环节id 获取环节的办理信息
     * @param processInstanceId
     * @param activityId
     * @author: wuxx
     * @Date: 2021/1/29 10:46
     **/
    @GetMapping(value="/getWorkflowTaskVoByInstanceIdAndActivityId")
    public ApiResultSet getWorkflowTaskVoByInstanceIdAndActivityId(
            @RequestParam(name = "processInstanceId") String processInstanceId,
            @RequestParam(name = "activityId") String activityId) {
        if(StrUtil.isEmpty(activityId) || StrUtil.isEmpty(processInstanceId)){
            return new ApiResultSet<>();
        }
        Map<String, Object> workflowTaskMapVoList = processDesignManager.getWorkflowTaskVoByInstanceIdAndActivityId(processInstanceId, activityId);
        return new ApiResultSet<>(workflowTaskMapVoList);
    }

    /**
     * @description: 根据流程实例ID和任务环节id 获取环节的办理信息
     * @param processInstanceId
     * @param activityId
     * @author: wuxx
     * @Date: 2021/1/29 10:46
     **/
    @GetMapping(value="/getInnerViewFlowByProcessInstanceIdAndActivityId")
    public ApiResultSet getInnerViewFlow(
            @RequestParam(name = "processInstanceId") String processInstanceId,
            @RequestParam(name = "activityId") String activityId) {
        if(StrUtil.isEmpty(activityId) || StrUtil.isEmpty(processInstanceId)){
            return new ApiResultSet<>();
        }
        Map<String, Object> workflowTaskMapVoList = processDesignManager.getInnerViewFlowByProcessInstanceIdAndActivityId(processInstanceId, activityId);
        return new ApiResultSet<>(workflowTaskMapVoList);
    }

    /**
     * @description: 根据流程实例ID和任务环节id 获取环节Step办理信息
     * @param processInstanceId 实列oid
     * @param activityId 环节oid
     * @author: wuxx
     * @Date: 2021/1/29 10:46
     **/
    @GetMapping(value="/getWorkflowStepByInstanceIdAndActivityId")
    public ApiResultSet getWorkflowStepByInstanceIdAndActivityId(
            @RequestParam(name = "processInstanceId") String processInstanceId,
            @RequestParam(name = "activityId") String activityId) {
        if(StrUtil.isEmpty(activityId) || StrUtil.isEmpty(processInstanceId)){
            return new ApiResultSet<>();
        }

        WorkflowBussFlowStep step = processDesignManager.getWorkflowStepByInstanceIdAndActivityId(processInstanceId, activityId);
        return new ApiResultSet<>(step);
    }

    /**
     * @description: 根据流程实例ID和任务环节id 获取环节Step办理信息
     * @param infoOid 流程oid
     * @param activityId 环节oid
     * @author: wuxx
     * @Date: 2021/2/22 10:46
     **/
    @GetMapping(value="/getWorkflowBussFlowStepByInfoIdAndActivityId")
    public ApiResultSet getWorkflowBussFlowStepByInfoIdAndActivityId(
            @RequestParam(name = "infoOid") String infoOid,
            @RequestParam(name = "activityId") String activityId) {
        if(StrUtil.isEmpty(activityId) || StrUtil.isEmpty(infoOid)){
            return new ApiResultSet<>();
        }
        WorkflowBussFlowStep step = processDesignManager.getWorkflowBussFlowStepByInfoIdAndActivityId(infoOid, activityId);
        return new ApiResultSet<>(step);
    }

    /**
     * @description: 根据流程部署ID和任务环节id 获取环节Step办理信息
     * @param processDefId 流程部署ID
     * @param activityId 环节oid
     * @author: wuxx
     * @Date: 2021/2/22 10:46
     **/
    @GetMapping(value="/getWorkflowBussFlowStepByProcessDefIdAndActivityId")
    public ApiResultSet getWorkflowBussFlowStepByProcessDefIdAndActivityId(
            @RequestParam(name = "processDefId") String processDefId,
            @RequestParam(name = "activityId") String activityId) {
        if(StrUtil.isEmpty(activityId) || StrUtil.isEmpty(processDefId)){
            return new ApiResultSet<>();
        }
        WorkflowBussFlowStep step = processDesignManager.getWorkflowBussFlowStepByProcessDefIdAndActivityId(processDefId, activityId);
        return new ApiResultSet<>(step);
    }

}



