package com.zfsoft.microservice.workflow.manager;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.microservice.workflow.data.WorkflowBussFlowStep;
import com.zfsoft.microservice.workflow.data.WorkflowLink;
import com.zfsoft.microservice.workflow.feign.SysUserFeignService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.*;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 工作流处理的类
 * @author: wuxx
 * @Date: 2021/1/15 11:16
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "workflow:workflowUtils")
public class WorkflowUtilsManager {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private SysUserFeignService sysUserFeignService;

    @Autowired
    private WorkflowBussFlowStepManager workflowBussFlowStepManager;

    @Autowired
    private WorkflowLinkManager workflowLinkManager;
    /**
     * @description: 根据任务id获取下一个环节的节点前的线条件
     * @param taskId 任务id
     * @author: wuxx
     * @Date: 2021/2/1 16:03
     **/
    public List<Map<String, Object>> queryNextTaskFlowConditionByTaskId(String taskId){
        List<Map<String, Object>> mapList = new ArrayList<>();
        //流程定义Id
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processDefId = task.getProcessDefinitionId();
        //当前流程节点Id,在任务表里面对应TASK_DEF_KEY_字段，可以根据taskId获取该字段数据
        String currId = task.getTaskDefinitionKey();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefId);
        org.activiti.bpmn.model.Process process = bpmnModel.getProcesses().get(0);
        List<SequenceFlow> flowElementsOfType = process.findFlowElementsOfType(SequenceFlow.class);
        List<SequenceFlow> flowElements = flowElementsOfType.stream().filter(flowElement -> {
            return flowElement.getSourceRef().equals(currId);}).collect(Collectors.toList());
        if(null!=flowElements && flowElements.size()>0){
            List<SequenceFlow> flowElementsCondition = flowElementsOfType.stream().filter(flowElement -> {
                return flowElement.getSourceRef().equals(flowElements.get(0).getTargetRef());}).collect(Collectors.toList());
            for(SequenceFlow flow:flowElementsCondition){
                if(StrUtil.isNotEmpty(flow.getConditionExpression())){
                    Map<String, Object> map = new HashMap<>();
                    map.put("id",flow.getId());
                    map.put("name",flow.getName());
                    map.put("conditionExpression",flow.getConditionExpression());
                    map.put("targetRef",flow.getTargetRef());
                    map.put("targetRefName",flow.getTargetFlowElement().getName());
                    mapList.add(map);
                }
            }
        }
        return mapList;
    }



    /**
     * @description: 获取下一个环节的节点用户oid和环节
     * @param taskId 任务id
     * @param flag true查询后续所有环节，false 查询后续一个环节（默认false）
     * @author: wuxx
     * @Date: 2021/1/15 10:03
     **/
    public List<Map<String, Object>> queryNextTaskByTaskId(String taskId,Boolean flag){
        //流程定义Id
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(null==task){
            return  null;
        }
        String processDefId = task.getProcessDefinitionId();
        //当前流程节点Id,在任务表里面对应TASK_DEF_KEY_字段，可以根据taskId获取该字段数据
        String currId = task.getTaskDefinitionKey();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefId);
        org.activiti.bpmn.model.Process process = bpmnModel.getProcesses().get(0);
        //获取所有普通任务节点
        List<UserTask> userTaskList = process.findFlowElementsOfType(UserTask.class);
        Collection<FlowElement> flowElements = process.getFlowElements();
        List<Map<String, Object>>userNextTaskList = new ArrayList<>();
        for(UserTask userTasks:userTaskList) {
            //获取任务节点Id
            String flowId = userTasks.getId();
            //如果遍历的某个任务节点Id等于下 一个节点的Id
            if (flowId.equals(currId)) {
                List<SequenceFlow> outgoingFlowsInner = userTasks.getOutgoingFlows();
                for(SequenceFlow sqInner:outgoingFlowsInner) {
                    String refIdInner = sqInner.getTargetRef();
                    userNextTaskList =  this.getNextFlowTask(refIdInner,flowElements,userTaskList,userNextTaskList,flag,processDefId);
                }
            }
        }
       // System.out.println(userNextTaskList);
        return userNextTaskList;
    }

    /**
     * @description: 获取后续环节审核人以及环节
     * @param nextId 环节流程中当前活动节点
     * @param flowElements 配置文件对象
     * @param userTaskList 用户任务集合
     * @param userNextTaskList 返回用户id以及环节集合
     * @param flag true查询后续所有环节，false 查询后续一个环节（默认false）
     * @author: wuxx
     * @Date: 2021/1/15 15:12
     **/
    public List<Map<String, Object>> getNextFlowTask(String nextId,Collection<FlowElement> flowElements,List<UserTask> userTaskList,List<Map<String, Object>> userNextTaskList,Boolean flag,String processDefId){
        if(null==flag){
            flag = false;
        }
        for(FlowElement flowElement:flowElements) {
            if(nextId.equals(flowElement.getId())){
                if(flowElement instanceof UserTask) {//节点
                    for(UserTask userTasks:userTaskList) {
                        //获取任务节点Id
                        String flowId = userTasks.getId();
                        //如果遍历的某个任务节点Id等于下 一个节点的Id
                        if (flowId.equals(nextId)) {
                            Map<String, Object> userNextTaskMap = this.getUserNextTaskMap(flowId, userTasks, processDefId);
                            if(null!=userNextTaskMap){
                                userNextTaskList.add(userNextTaskMap);
                            }
                            //获取下一个环节
                            if(flag){
                                List<SequenceFlow> outgoingFlowsInner = userTasks.getOutgoingFlows();
                                for(SequenceFlow sqInner:outgoingFlowsInner) {
                                    String refIdInner = sqInner.getTargetRef();
                                    userNextTaskList = this.getNextFlowTask(refIdInner,flowElements,userTaskList,userNextTaskList,flag,processDefId);
                                }
                            }
                        }
                    }
                }else if(flowElement instanceof ExclusiveGateway){//如果流向线路为排他网关
                    List<SequenceFlow> outgoingFlows = ((ExclusiveGateway) flowElement).getOutgoingFlows();
                    //String userOids = "";
                    for(SequenceFlow sq:outgoingFlows){
                        String refId = sq.getTargetRef();
                        for(UserTask userTasks:userTaskList) {
                            //获取任务节点Id
                            String flowId = userTasks.getId();
                            //如果遍历的某个任务节点Id等于下 一个节点的Id
                            if (flowId.equals(refId)) {
                                Map<String, Object> userNextTaskMap = this.getUserNextTaskMap(flowId, userTasks, processDefId);
                                if(null!=userNextTaskMap){
                                    userNextTaskList.add(userNextTaskMap);
                                }

                            }
                        }
                       /* if(userOids.contains(",")){
                            userOids = userOids.substring(0,userOids.length()-1);
                        }*/
                        //获取下一个环节
                        if(flag){
                            userNextTaskList = this.getNextFlowTask(refId,flowElements,userTaskList,userNextTaskList,flag,processDefId);
                        }
                    }
                }else if(flowElement instanceof ParallelGateway){//如果流向线路为并行网关
                    List<SequenceFlow> outgoingFlows = ((ParallelGateway) flowElement).getOutgoingFlows();
                    for(SequenceFlow sq:outgoingFlows){
                        String refId = sq.getTargetRef();
                        for(UserTask userTasks:userTaskList) {
                            //获取任务节点Id
                            String flowId = userTasks.getId();
                            //如果遍历的某个任务节点Id等于下 一个节点的Id
                            if (flowId.equals(refId)) {
                                Map<String, Object> userNextTaskMap = this.getUserNextTaskMap(flowId, userTasks, processDefId);
                                if(null!=userNextTaskMap){
                                    userNextTaskList.add(userNextTaskMap);
                                }

                            }
                        }
                        //获取下一个环节
                        if(flag){
                            userNextTaskList = this.getNextFlowTask(refId,flowElements,userTaskList,userNextTaskList,flag,processDefId);
                        }
                    }

                }
            }
        }
        return userNextTaskList;
    }


    /**
     * @description: 获取下一个环节用户审核信息
     * @param flowId 节点id
     * @param userTasks 任务
     * @param processDefId 部署id
     * @author: wuxx
     * @Date: 2021/2/26 10:09
     **/
    private Map<String, Object> getUserNextTaskMap(String flowId, UserTask userTasks ,String processDefId){
        WorkflowBussFlowStep step = workflowBussFlowStepManager.getWorkflowBussFlowStepByActivityIdAndDefId(flowId, processDefId);
        if(null!=step){
            WorkflowLink link = workflowLinkManager.getWorkflowLinkByLinkOid(step.getLinkOid());
            Map<String, Object> map = new HashMap();
            map.put("userOids",step.getHandleUserOids());
            map.put("userNames",step.getHandleUserNames());
            map.put("handlePostOids",StrUtil.isNotEmpty(step.getHandlePostOids())?step.getHandlePostOids():"");
            map.put("handlePostNames",StrUtil.isNotEmpty(step.getHandlePostNames())?step.getHandlePostNames():"");
            map.put("activityId",flowId);
            map.put("taskName",userTasks.getName());
            map.put("linkName",link.getName());
            map.put("linkCode",link.getCode());
            return map;
        }else{
            //获取下一个任务节点的执行人
            String assignee = userTasks.getAssignee();
            if(StrUtil.isEmpty(assignee)){
                List<String> candidateUsers = userTasks.getCandidateUsers();
                assignee = StringUtils.join(candidateUsers,",");
            }
            if(StrUtil.isNotEmpty(assignee)){
                Map<String, Object> map = new HashMap();
                String[] splitUser = assignee.split(",");
                List<String> userNames = new ArrayList<>();
                for(String oid:splitUser){
                    SysUser sysUser = sysUserFeignService.getSysUserByUserOid(oid).getData();
                    if(null!=sysUser){
                        userNames.add(sysUser.getName());
                    }
                }
                map.put("userOids",assignee);
                map.put("userNames",StringUtils.join(userNames,","));
                map.put("activityId",userTasks.getId());
                map.put("taskName",userTasks.getName());
                map.put("handlePostOids","");
                map.put("handlePostNames","");
                map.put("linkName","");
                map.put("linkCode","");
                return map;
            }
        }
        return null;
    }
    /**
     * @description: 创建箭头
     * @param from 来自
     * @param to 下个节点
     * @author: wuxx
     * @Date: 2021/2/8 15:20
     **/
    private SequenceFlow createSequenceFlow(String from, String to,String waypoints) {
        SequenceFlow flow = new SequenceFlow();
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        List<Integer> list = new ArrayList<>();
        //[{'original':{'x':910,'y':330},'x':910,'y':330},{'original':{'x':910,'y':380},'x':910,'y':380},{'original':{'x':690,'y':380},'x':690,'y':380},{'original':{'x':690,'y':330},'x':690,'y':330}]
        //x1, y1, x2, y2, x3, y3
        if(StrUtil.isNotEmpty(waypoints)){
            JSONArray jsonArray = JSONUtil.parseArray(waypoints);
            jsonArray.forEach(original ->{
                JSONObject jsonObject = JSONUtil.parseObj(original);
                int x = Integer.valueOf(jsonObject.get("x").toString());
                list.add(x);
                int y = Integer.valueOf(jsonObject.get("y").toString());
                list.add(y);
            });
        }
        flow.setWaypoints(list);
        flow.setId(IdUtil.simpleUUID());
        return flow;
    }

    /**
     * @description:  处理驳回和退回节点的xml
     * @param bpmnXml xml文件
     * @param processDefId 实例化id
     * @author: wuxx
     * @Date: 2021/2/8 15:30
     **/
    public String getRejectAndFallbackBpmnXml(String bpmnXml,String processDefId){

        Document document = Jsoup.parse(bpmnXml, "", Parser.xmlParser());
        //3.获取元素对象 Element
        Elements elements = document.getElementsByTag("definitions");
        Element element = elements.get(0);
        Elements elementsByTagProcess = element.getElementsByTag("process");
        Element elementProcess = elementsByTagProcess.get(0);
        Elements elementsByTagUserTask = elementProcess.getElementsByTag("userTask");
        WorkflowBussFlowStep workflowBussFlowStep = new WorkflowBussFlowStep();
        workflowBussFlowStep.setProcessDefId(processDefId);
        List<WorkflowBussFlowStep> workflowBussFlowSteps = workflowBussFlowStepManager.queryWorkflowBussFlowStepList(workflowBussFlowStep);
        //驳回
        List<WorkflowBussFlowStep> rejectTaskList =workflowBussFlowSteps.stream().filter(step->null!=step.getRejectTaskId()).collect(Collectors.toList());
        Map<String, String> rejectMap = new HashMap();
        if(null!=rejectTaskList && rejectTaskList.size()>0){
            for(WorkflowBussFlowStep rejectStep : rejectTaskList){
                elementsByTagUserTask.forEach(ele->{
                    String activityId = ele.attr("id");
                    if(activityId.equals(rejectStep.getActivityId())){
                        String rejectTaskIds = rejectStep.getRejectTaskId();
                        String rejectWayPoints = rejectStep.getRejectWayPoints();
                        if(StrUtil.isNotBlank(rejectTaskIds) && StrUtil.isNotBlank(rejectWayPoints)){
                            String[] splitRejectTaskId = rejectTaskIds.split(",");
                            String [] splitRejectWayPoint = rejectWayPoints.split("~");
                            for(int i =0;i<splitRejectTaskId.length;i++) {
                                Element elementFlow = elementProcess.appendElement("sequenceFlow");
                                elementFlow.attr("id","Flow_REJECT_"+rejectStep.getActivityId()+splitRejectTaskId[i]);
                                elementFlow.attr("sourceRef",rejectStep.getActivityId());
                                elementFlow.attr("flowClass","bh");
                                elementFlow.attr("targetRef",splitRejectTaskId[i]);
                                rejectMap.put("Flow_REJECT_"+rejectStep.getActivityId()+splitRejectTaskId[i],splitRejectWayPoint[i]);
                                String name = "驳回";
                                for(Element userTaskEle:elementsByTagUserTask){
                                    String userTaskId = userTaskEle.attr("id");
                                    if(userTaskId.equals(splitRejectTaskId[i])){
                                        name = elementFlow.attr("name") + "驳回至"+userTaskEle.attr("name");
                                        break;
                                    }
                                }
                                elementFlow.attr("name",name);
                                elementProcess.appendChild(elementFlow);
                            }
                        }
                    }

                });
            }
        }
        //退回
        List<WorkflowBussFlowStep> fallbackTaskList =workflowBussFlowSteps.stream().filter(step->null!=step.getFallbackTaskId()).collect(Collectors.toList());
        if(null!=fallbackTaskList && fallbackTaskList.size()>0){
            for(WorkflowBussFlowStep rejectStep : fallbackTaskList){
                elementsByTagUserTask.forEach(ele->{
                    String activityId = ele.attr("id");
                    if(activityId.equals(rejectStep.getActivityId())){
                        String fallbackTaskIds = rejectStep.getFallbackTaskId();
                        String fallbackWayPoints = rejectStep.getFallbackWayPoints();
                        if(StrUtil.isNotBlank(fallbackTaskIds) && StrUtil.isNotBlank(fallbackWayPoints)){
                            String[] splitFallbackTaskId = fallbackTaskIds.split(",");
                            String[] splitfallbackWayPoint = fallbackWayPoints.split("~");
                            for(int i =0;i<splitFallbackTaskId.length;i++) {
                                //Elements elementsFallback = elementProcess.getElementsByAttributeValueMatching("targetRef", splitFallbackTaskId[i]);
                                Element elementFlow = elementProcess.appendElement("sequenceFlow");
                                elementFlow.attr("id","Flow_FALLBACK_"+rejectStep.getActivityId()+splitFallbackTaskId[i]);
                                elementFlow.attr("sourceRef",rejectStep.getActivityId());
                                elementFlow.attr("targetRef",splitFallbackTaskId[i]);
                                elementFlow.attr("flowClass","th");
                                rejectMap.put("Flow_FALLBACK_"+rejectStep.getActivityId()+splitFallbackTaskId[i],splitfallbackWayPoint[i]);
                                String name = "退回";
                                for(Element userTaskEle:elementsByTagUserTask){
                                    String userTaskId = userTaskEle.attr("id");
                                    if(userTaskId.equals(splitFallbackTaskId[i])){
                                        name = elementFlow.attr("name") + "退回至"+userTaskEle.attr("name");
                                        break;
                                    }
                                }
                                elementFlow.attr("name",name);
                                elementProcess.appendChild(elementFlow);
                            }
                        }
                    }

                });
            }
        }

        //bpmndi:BPMNDiagram
        Element elementBPMNDiagram = element.getElementsByTag("bpmndi:BPMNDiagram").get(0);
        Element elementBPMNPlane= elementBPMNDiagram.getElementsByTag("bpmndi:BPMNPlane").get(0);
        for (Map.Entry<String, String> entry : rejectMap.entrySet()) {
            Element elementShape = elementBPMNPlane.appendElement("bpmndi:BPMNEdge");
            elementShape.attr("bpmnElement",entry.getKey());
            elementShape.attr("id","BPMNEdge_"+entry.getKey());
            elementBPMNPlane.appendChild(elementShape);
            String waypoints = entry.getValue();
            if(StrUtil.isNotEmpty(waypoints)){
                JSONArray jsonArray = JSONUtil.parseArray(waypoints);
                jsonArray.forEach(original ->{
                    JSONObject jsonObject = JSONUtil.parseObj(original);
                    String x = jsonObject.get("x").toString();
                    String y = jsonObject.get("y").toString();
                    Element elementWaypoint = elementShape.appendElement("omgdi:waypoint");
                    elementWaypoint.attr("x",x+".0");
                    elementWaypoint.attr("y",y+".0");
                    elementShape.appendChild(elementWaypoint);
                });
            }
        }

        String new_bpmb_xml = document.html();
        //System.err.println(new_bpmb_xml);
        return new_bpmb_xml;

    }
}
