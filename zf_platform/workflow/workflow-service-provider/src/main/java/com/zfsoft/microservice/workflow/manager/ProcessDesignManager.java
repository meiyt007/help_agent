package com.zfsoft.microservice.workflow.manager;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.microservice.platform.data.sys.SysRole;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.microservice.workflow.data.*;
import com.zfsoft.microservice.workflow.feign.SysOrganFeignService;
import com.zfsoft.microservice.workflow.feign.SysRoleFeignService;
import com.zfsoft.microservice.workflow.feign.SysUserFeignService;
import com.zfsoft.microservice.workflow.service.WorkflowService;
import com.zfsoft.microservice.workflow.util.ActivitiUtils;
import com.zfsoft.microservice.workflow.util.BpmnConverterUtil;
import com.zfsoft.microservice.workflow.util.LimitDateCalc;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 根据生成的ID获取模型流程编辑器
 * @author: wuxx
 * @Date: 2021/1/18 13:35
 **/
@Service
public class ProcessDesignManager implements ModelDataJsonConstants {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessDesignManager.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private WorkflowBussFlowStepManager workflowBussFlowStepMapper;

    @Autowired
    private WorkflowBussInfoManager workflowBussInfoManager;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private WorkflowTypeManager workflowTypeManager;

    @Autowired
    private ProcessDesignManager processDesignManager;

    @Autowired
    private WorkflowUtilsManager workflowUtilsManager;
    /**
     * 注入历史 service
     */
    @Autowired
    private HistoryService historyService;
    @Autowired
    private SysUserFeignService sysUserFeignService;
    @Autowired
    private WorkflowBussFlowStepManager workflowBussFlowStepManager;
    @Autowired
    private HistoryTaskManager historyTaskManager;
    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private WorkflowFallbackManager workflowFallbackManager;

    @Autowired
    private SysOrganFeignService sysOrganFeignService;

    @Autowired
    private SysRoleFeignService sysRoleFeignService;

    /**
     * 保存模型
     * @param key
     * @param name 名称
     * @param category 类别
     * @param descp 描述
     * @throws UnsupportedEncodingException
     */
    public String createModel(String id,String key,String name,String category, String descp) throws UnsupportedEncodingException {
        //初始化一个空模型
        /*Model modelData = repositoryService.getModel(id);
        if(null == modelData) {
            modelData = repositoryService.newModel();
            int revision = 1;
            modelData.setVersion(revision);
        }else {
            if(StrUtil.isNotEmpty(modelData.getDeploymentId())){
                modelData.setVersion(modelData.getVersion() + 1);
            }
        }*/
        //初始化一个空模型
        Model modelData = repositoryService.newModel();
        modelData.setVersion(1);
        //设置一些默认信息
        String modelName = name;
        String description = descp;
        String modelKey = key;
        //modelNode.put(ModelDataJsonConstants.MODEL_NAME,modelName);
        //modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
        modelData.setName(modelName);
        modelData.setKey(modelKey);
        //modelData.setMetaInfo(modelNode.toString());
        modelData.setCategory(category);
        repositoryService.saveModel(modelData);
        return modelData.getId();
    }

    /**
     * 查询模型
     * @return
     */
    public List<Model> listModel() {
        return repositoryService.createModelQuery().list();
    }

    public PageResult<Model> pageModel(String name, Integer pageNum, Integer pageSize) {
        //分页参数
        if (null == pageNum || pageNum <= 1) {
            pageNum = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        long total = repositoryService.createModelQuery().count();
        List<Model> list = repositoryService.createModelQuery().orderByLastUpdateTime().desc().list();
        PageResult<Model> pageResult = new PageResult<>(pageNum,pageSize,Integer.valueOf(total+""));
        pageResult.setData(list);
        return pageResult;
    }

    public Model getOneModel(String modelId) {
        return repositoryService.createModelQuery().modelId(modelId).singleResult();
    }

    /**
     * @description:  删除模型
     * @param modelId 模型主键
     * @author: wuxx
     * @Date: 2021/1/20 14:16
     **/
    public void deleteModel(String modelId) {
        repositoryService.deleteModel(modelId);
    }

    /**
     * @description: 模型保存
     * @author: wuxx
     * @Date: 2021/1/18 13:35
     **/
    public String saveModelXml(String infoOid, Map<String, String> values) {
        try {
            WorkflowBussInfo info = this.workflowBussInfoManager.getWorkflowBussInfoByInfoOid(infoOid);
            if(null==info){
                throw new ResultInfoException("流程信息不能为空！");
            }
            WorkflowType workflowType = this.workflowTypeManager.getWorkflowTypeByTypeOid(info.getTypeOid());
            String modelId = this.processDesignManager.createModel(info.getModelId()+"", info.getInfoOid(), info.getWorkflowName(), workflowType.getName(), info.getWorkflowMemo());
            info.setModelId(Integer.parseInt(modelId));
            Model model = repositoryService.getModel(modelId);
            if(model==null){
                throw new ResultInfoException("model信息新增失败！");
            }
            //获取流程定义id
            String processDefId = null;
            /*if(StrUtil.isNotEmpty(model.getDeploymentId())){
                //获取部署的定义
                ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(model.getDeploymentId()).singleResult();
                processDefId = processDefinition.getId();
            }*/
            String bpmnXml = values.get("bpmn_xml");
            //System.err.println(bpmnXml);
            Document document = Jsoup.parse(bpmnXml, "",Parser.xmlParser());
            //3.获取元素对象 Element
            Elements elements = document.getElementsByTag("bpmn2:definitions");
            Element element = elements.get(0);
            Elements elementsByTagProcess = element.getElementsByTag("bpmn2:process");
            Element elementProcess = elementsByTagProcess.get(0);
            String processDefinitionKey = elementProcess.attr("id");
            info.setProcessKey(processDefinitionKey);
            Elements elementsByTagUserTask = elementProcess.getElementsByTag("bpmn2:userTask");
            Elements elementsByTagSequenceFlow = elementProcess.getElementsByTag("bpmn2:sequenceFlow");
            Elements elementsByTagCallActivity = elementProcess.getElementsByTag("bpmn2:callActivity");
            //用户标签
            String finalProcessDefId = processDefId;
            List<String> flowIdList = new ArrayList<>();
            elementsByTagUserTask.addAll(elementsByTagCallActivity);
            elementsByTagUserTask.forEach(ele->{
                String activityId = ele.attr("id");
                //环节表的业务oid
                WorkflowBussFlowStep step = workflowBussFlowStepMapper.getWorkflowBussFlowStepByInfoOidAndActivityIdAndDefIdIsNull(infoOid,activityId);
                if(null==step){
                    step = new WorkflowBussFlowStep();
                }
                String name = ele.attr("name");
                //String assignee = ele.attr("activiti:assignee");
                //String candidategroups = ele.attr("activiti:candidategroups");
                String timeLimit = ele.attr("timeLimit");
                String timelimithour = ele.attr("timelimithour");
                String handleType = ele.attr("handleType");
                String handleUserType = ele.attr("handleUserType");
                String handleUserNames = ele.attr("handleUserNames");
                String handlePostNames = ele.attr("handlePostNames");
                String handleUserXml = ele.attr("handleUserOids");
                //handleUserOids 包含了区划、组织机构oid，排除
                List<String> userOidList = new ArrayList<>();
                String[] splitUserStr = handleUserXml.split("~");
                String usernames = "";
                for (String oids:splitUserStr){
                    if(oids.contains("USER-")){
                        oids = oids.replaceAll("USER-","");
                        userOidList.add(oids);
                        if(StrUtil.isBlank(handleUserNames)){
                            SysUser sysUser = sysUserFeignService.getSysUserByUserOid(oids).getData();
                            String userName = null!=sysUser?sysUser.getName():"";
                            usernames = usernames + userName +",";
                        }
                    }
                }
                if(StrUtil.isBlank(handleUserNames)){
                    if(StrUtil.isNotBlank(usernames)){
                        usernames = usernames.substring(0,usernames.length()-1);
                    }
                    handleUserNames = usernames;
                }
                String handleUserOids = String.join(",",userOidList);
                String handlePostXml = ele.attr("handlePostOids");
                //handlePostOids 包含了区划、组织机构oid，排除
                List<String> postOidList = new ArrayList<>();
                String[] splitPostStr = handlePostXml.split("~");
                for (String oids:splitPostStr){
                    if(oids.contains("POST-")){
                        postOidList.add(oids.substring(5,oids.length()));
                    }
                }

                //角色-新增
                String handleRoleXml = ele.attr("handleRoleOids");
                List<String> roleOidList = new ArrayList<>();
                List<String> roleNameList = new ArrayList<>();
                if(StrUtil.isNotEmpty(handleRoleXml)){
                    String[] splitRoleStr = handleRoleXml.split("~");
                    for (String oids:splitRoleStr){
                        if(oids.contains("ROLE-")){
                            String OID = oids.replace("ROLE-", "");
                            roleOidList.add(OID);
                            SysRole data = sysRoleFeignService.getSysRoleByRoleOid(OID).getData();
                            roleNameList.add(null!=data?data.getName():"");
                        }
                    }
                }
                String handleRoleOids = String.join(",",roleOidList);
                String handleRoleNames = String.join(",",roleNameList);

                //机构-新增
                String handleOrganXml = ele.attr("handleOrganOids");
                List<String> organOidList = new ArrayList<>();
                List<String> organNameList = new ArrayList<>();
                if(StrUtil.isNotEmpty(handleOrganXml)){
                    String[] splitOrganStr = handleOrganXml.split("~");
                    for (String oids:splitOrganStr){
                        if(oids.contains("ORGAN-")){
                            String OID = oids.replace("ORGAN-", "");
                            organOidList.add(OID);
                            SysOrgan data = sysOrganFeignService.getSysOrganByOrganOid(OID).getData();
                            organNameList.add(null!=data?data.getName():"");
                        }
                    }
                }
                String handleOrganOids = String.join(",",organOidList);
                String handleOrganNames = String.join(",",organNameList);



                String handlePostOids = String.join(",",postOidList);
                String innerFlowOid = ele.attr("innerFlowOid");
                if(StrUtil.isNotEmpty(innerFlowOid) && innerFlowOid.contains("FLOW-")){
                    innerFlowOid = innerFlowOid.substring(5,innerFlowOid.length());
                }
                //驳回
                String rejectTaskIds = ele.attr("rejectTaskId");
                String rejectWayPoints = ele.attr("rejectWayPoints");
                //退回
                String fallbackTaskIds = ele.attr("fallbackTaskId");
                String fallbackWayPoints = ele.attr("fallbackWayPoints");
                List rejectFlowIdList = new ArrayList();
                List fallbackFlowIdList = new ArrayList();
                if(StrUtil.isNotEmpty(rejectTaskIds) || StrUtil.isNotEmpty(fallbackTaskIds)){
                    String[] splitRejectTaskIds = rejectTaskIds.split(",");
                    for (Element eleFlow :elementsByTagSequenceFlow){
                       //System.out.println(eleFlow);
                       String flowId = eleFlow.attr("id");
                       String sourceRef = eleFlow.attr("sourceRef");
                       String targetRef = eleFlow.attr("targetRef");
                       String flowClass = eleFlow.attr("flowClass");
                        //驳回
                        for(String rejectTaskId : splitRejectTaskIds ){
                            if(StrUtil.isNotEmpty(rejectTaskId) && sourceRef.equals(activityId)
                                    && rejectTaskId.contains(targetRef) && "bh".equals(flowClass)){
                                if(!flowIdList.contains(flowId)){
                                    flowIdList.add(flowId);
                                }
                                rejectFlowIdList.add(flowId);
                                eleFlow.remove();
                            }
                        }

                        //退回
                        String[] splitFallbackTaskIds = fallbackTaskIds.split(",");
                        for(String fallbackTaskId : splitFallbackTaskIds ){
                            if(StrUtil.isNotEmpty(fallbackTaskId) && sourceRef.equals(activityId)
                                    && fallbackTaskId.contains(targetRef) && "th".equals(flowClass)){
                                if(!flowIdList.contains(flowId)){
                                    flowIdList.add(flowId);
                                }
                                fallbackFlowIdList.add(flowId);
                                eleFlow.remove();
                            }
                        }

                   }
                    //处理驳回退回节点
                    //bpmndi:BPMNDiagram
                    Element elementBPMNDiagram = element.getElementsByTag("bpmndi:BPMNDiagram").get(0);
                    Element elementBPMNPlane= elementBPMNDiagram.getElementsByTag("bpmndi:BPMNPlane").get(0);
                    Elements elementsBPMNEdges = elementBPMNPlane.getElementsByTag("bpmndi:BPMNEdge");
                    //驳回集合
                    List<String> rejectWayPointList = new ArrayList<>();
                    //退回集合
                    List<String> fallbackWayPointList = new ArrayList<>();
                    for(Element edges:elementsBPMNEdges){
                        String bpmnElement = edges.attr("bpmnElement");
                        //驳回
                        if(null!=rejectFlowIdList && rejectFlowIdList.contains(bpmnElement)){
                            Elements elementsWaypoint = edges.getElementsByTag("di:waypoint");
                            List<Map<String, String>> mapList = new ArrayList<>();
                            for(Element point : elementsWaypoint){
                                Map<String, String> map = new HashMap();
                                String x = point.attr("x");
                                String y = point.attr("y");
                                map.put("x",x);
                                map.put("y",y);
                                mapList.add(map);
                            }
                            String rejectWayPoint = JSONUtil.toJsonStr(mapList);
                            rejectWayPointList.add(rejectWayPoint);
                        }
                        //退回
                        if(null!=fallbackFlowIdList && fallbackFlowIdList.contains(bpmnElement)){
                            Elements elementsWaypoint = edges.getElementsByTag("di:waypoint");
                            List<Map<String, String>> mapList = new ArrayList<>();
                            for(Element point : elementsWaypoint){
                                Map<String, String> map = new HashMap();
                                String x = point.attr("x");
                                String y = point.attr("y");
                                map.put("x",x);
                                map.put("y",y);
                                mapList.add(map);
                            }
                            fallbackWayPointList.add(JSONUtil.toJsonStr(mapList));
                        }
                    }
                    rejectWayPoints =  String.join("~",rejectWayPointList);
                    fallbackWayPoints =  String.join("~",fallbackWayPointList);
                }
                String linkOid = ele.attr("linkOid");
                //保存环节参数信息
                step.setActivityId(activityId);
                step.setName(name);
                step.setLinkOid(linkOid);
                step.setInfoOid(info.getInfoOid());
                //step.setInfoOid(model.getId());
                step.setProcessDefId(finalProcessDefId);
                step.setTimeLimit(StrUtil.isNotEmpty(timeLimit)?Integer.valueOf(timeLimit):null);
                step.setTimeLimitHour(StrUtil.isNotEmpty(timelimithour)?Integer.valueOf(timelimithour):null);
                step.setHandleType(StrUtil.isNotEmpty(handleType)?handleType:null);
                step.setHandleUserType(StrUtil.isNotEmpty(handleUserType)?handleUserType:null);
                step.setHandleUserOids(StrUtil.isNotEmpty(handleUserOids)?handleUserOids:null);
                step.setHandlePostOids(StrUtil.isNotEmpty(handlePostOids)?handlePostOids:null);
                step.setHandleUserNames(handleUserNames);
                step.setHandlePostNames(handlePostNames);
                //角色
                step.setHandleRoleOids(StrUtil.isNotEmpty(handleRoleOids)?handleRoleOids:null);
                step.setHandleRoleNames(StrUtil.isNotEmpty(handleRoleNames)?handleRoleNames:null);
                //机构
                step.setHandleOrganOids(StrUtil.isNotEmpty(handleOrganOids)?handleOrganOids:null);
                step.setHandleOrganNames(StrUtil.isNotEmpty(handleOrganNames)?handleOrganNames:null);

                step.setInnerFlowOid(StrUtil.isNotEmpty(innerFlowOid)?innerFlowOid:null);
                step.setRejectTaskId(StrUtil.isNotEmpty(rejectTaskIds)?rejectTaskIds:null);
                step.setFallbackTaskId(StrUtil.isNotEmpty(fallbackTaskIds)?fallbackTaskIds:null);
                step.setRejectWayPoints(StrUtil.isNotEmpty(rejectWayPoints)?rejectWayPoints:null);
                step.setFallbackWayPoints(StrUtil.isNotEmpty(fallbackWayPoints)?fallbackWayPoints:null);
                workflowBussFlowStepMapper.saveWorkflowBussFlowStep(step);

                Elements tag = ele.getElementsByTag("bpmn2:multiInstanceLoopCharacteristics");
                //移除并联元素
                tag.remove();
                Elements tagextensionElements = ele.getElementsByTag("bpmn2:extensionElements");
                //移除并联元素
                tagextensionElements.remove();
                //环节跳过标识
                ele.attr("activiti:skipExpression","${skipExpression == 1}");

                List allOidList = new ArrayList();
                allOidList.addAll(userOidList);
                allOidList.addAll(postOidList);
                allOidList.addAll(roleOidList);
                allOidList.addAll(organOidList);
                //上一个环节指定需要传handleUserOids(只针对未设置办理人或者办理岗位的情况，否则按照设置的来)
                if(BaseStaticParameter.STR_TWO.equals(handleUserType) && allOidList.size()==0){
                    //环节办理类型 -任意人办理
                    ele.removeAttr("activiti:assignee");
                    ele.attr("activiti:candidateUsers","${handleUserOids}");
                }else{
                    //第一种情况  设置了岗位、角色、机构（有可能设置审批人）
                    if(null!=allOidList && allOidList.size() > 0){
                        //环节办理类型 -全部人办理
                        if(StrUtil.isNotEmpty(handleType) && BaseStaticParameter.STR_TWO.equals(handleType)){
                            ele.removeAttr("activiti:candidateUsers");
                            ele.attr("activiti:assignee","${assigneeUser}");
                            ele.attr("activiti:candidateGroups",name);
                            Element elementLoop = ele.appendElement("bpmn2:multiInstanceLoopCharacteristics");
                            elementLoop.attr("isSequential","false");
                            elementLoop.attr("activiti:elementVariable","assigneeUser");
                            elementLoop.attr("activiti:collection","${userTaskService.getUsersByStepOid('"+handleType+"','"+step.getStepOid()+"')}");
                            ele.appendChild(elementLoop);
                            Element elementCondition = elementLoop.appendElement("bpmn2:completionCondition");
                            elementCondition.html("${nrOfCompletedInstances/nrOfInstances >= 1}");
                            elementLoop.appendChild(elementCondition);
                        }else{
                            //环节办理类型 -任意人办理
                            ele.attr("activiti:candidateUsers","${userTaskService.getUsersByStepOid('"+handleType+"','"+step.getStepOid()+"')}");
                        }
                    }else if(null!=userOidList && userOidList.size() > 0){
                        //只设置了用户，未设置岗位
                        //只设置一个人
                        if(null!=userOidList && userOidList.size() == 1){
                            ele.removeAttr("activiti:candidateUsers");
                            ele.attr("activiti:assignee",handleUserOids);
                        }else if(null!=userOidList && userOidList.size() > 1){
                            //环节办理类型 多人办理
                            if(StrUtil.isNotEmpty(handleType) && BaseStaticParameter.STR_TWO.equals(handleType)){
                                //全部人员办理
                                ele.removeAttr("activiti:candidateUsers");
                                ele.attr("activiti:assignee","${assigneeUser}");
                                ele.attr("activiti:candidateGroups",name);
                                Element elementLoop = ele.appendElement("bpmn2:multiInstanceLoopCharacteristics");
                                elementLoop.attr("isSequential","false");
                                elementLoop.attr("activiti:elementVariable","assigneeUser");
                                elementLoop.attr("activiti:collection","${userTaskService.getUsersByStepOid('"+handleType+"','"+step.getStepOid()+"')}");
                                ele.appendChild(elementLoop);
                                Element elementCondition = elementLoop.appendElement("bpmn2:completionCondition");
                                elementCondition.html("${nrOfCompletedInstances/nrOfInstances >= 1}");
                                elementLoop.appendChild(elementCondition);
                            }else{
                                //环节办理类型 -任意人办理
                                ele.removeAttr("activiti:assignee");
                                ele.attr("activiti:candidateUsers",handleUserOids);

                            }
                        }
                    }

                }

                //处理多余的字段
                ele.removeAttr("linkOid");
                ele.removeAttr("timelimit");
                ele.removeAttr("timelimithour");
                ele.removeAttr("handletype");
                ele.removeAttr("handleusertype");
                ele.removeAttr("handleUserOids");
                ele.removeAttr("handlePostOids");
                ele.removeAttr("innerFlowOid");
                ele.removeAttr("rejectTaskId");
                ele.removeAttr("fallbackTaskId");
                ele.removeAttr("operatingType");
                ele.removeAttr("timeUnit");
                ele.removeAttr("handleUserNames");
                ele.removeAttr("handlePostNames");

                ele.removeAttr("handleRoleOids");
                ele.removeAttr("handleRoleNames");
                ele.removeAttr("handleOrganOids");
                ele.removeAttr("handleOrganNames");
                ele.removeAttr("rejectWaypoints");
                ele.removeAttr("fallbackWaypoints");
            });
            //线标签
            elementsByTagSequenceFlow.forEach(ele->{
                /*if ("th".equals(ele.attr("flowClass")) || "bh".equals(ele.attr("flowClass"))){
                    ele.remove();
                }*/
                //ele.removeAttr("flowClass");
                //ele.removeAttr("startTaskId");
                ele.removeAttr("condition");
            });

            String new_bpmb_xml = document.html();
            if(flowIdList.size()>0){
                for (String flowId :flowIdList){
                    new_bpmb_xml = new_bpmb_xml.replaceAll("<bpmn2:incoming>"+flowId+"</bpmn2:incoming>","");
                    new_bpmb_xml = new_bpmb_xml.replaceAll("<bpmn2:outgoing>"+flowId+"</bpmn2:outgoing>","");
                }
            }
            //System.err.println(new_bpmb_xml);
            //保存流程编辑器的xml内容
            info.setBpmnXml(values.get("bpmn_xml"));
            //修改状态为未发布
            info.setIsPublish(BaseStaticParameter.N);
            info.setDeploymentId(null);
            workflowBussInfoManager.saveOrUpdateWorkflowBussInfo(info);
            //System.err.println(new_bpmb_xml);
            repositoryService.addModelEditorSource(model.getId(),new_bpmb_xml.getBytes("utf-8"));
            /*repositoryService.addModelEditorSourceExtra(model.getId(),
                    values.get("svg_xml").getBytes("utf-8"));*/
            return modelId;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResultInfoException("保存流程编辑异常！", e);
        }
    }

    /**
     * 根据生成的ID获取模型流程编辑器
     * @param modelId
     * @return
     */
    public JSONObject getEditorXml(String modelId) {
        JSONObject jsonObject =  new JSONObject();
        //获取流程信息
        WorkflowBussInfo info = workflowBussInfoManager.getWorkflowBussInfoByModelId(Integer.valueOf(modelId));
        if(null != info){
            String bpmnXml =  info.getBpmnXml();
            jsonObject.put("bpmnXml", StrUtil.isEmpty(bpmnXml)?"":bpmnXml);
            jsonObject.put(MODEL_NAME, info.getWorkflowName());
            jsonObject.put(MODEL_ID, info.getModelId());
            jsonObject.put("infoOid",info.getInfoOid());
            return jsonObject;
        }
        Model model = repositoryService.getModel(modelId);
        if (model != null) {
            try {
                if (StringUtils.isNotEmpty(model.getMetaInfo())) {
                    jsonObject = JSON.parseObject(model.getMetaInfo());
                } else {
                    jsonObject = new JSONObject();
                    jsonObject.put(MODEL_NAME, model.getName());
                }
                jsonObject.put(MODEL_ID, model.getId());
                byte[] bytes = repositoryService.getModelEditorSource(model.getId());
                String bpmnXml = "";
                if(null!=bytes){
                    bpmnXml = new String(repositoryService.getModelEditorSource(model.getId()));
                }
                try {
                    //将json流程转为标准xml流程图 解决初始化报错问题
                    JSONObject editorJsonNode = JSON.parseObject(bpmnXml);
                    bpmnXml = BpmnConverterUtil.converterJsonToWebXml(editorJsonNode.toJSONString());
                }catch (Exception e){
                    //初始化使用的是json
                }
                jsonObject.put("bpmnXml", bpmnXml);
            } catch (Exception e) {
                throw new ResultInfoException("无法读取model信息", e);
            }
        } else {
            throw new ResultInfoException("未找到对应模型信息");
        }
        return jsonObject;
    }

    /**
     * @description:  根据infoOid查看部署的流程图
     * @param infoOid 流程信息业务主键
     * @author: wuxx
     * @Date: 2021/2/20 14:16
     **/
    public JSONObject getEditorXmlByInfoOid(String infoOid) {
        JSONObject jsonObject =  new JSONObject();
        //获取流程信息
        WorkflowBussInfo info = workflowBussInfoManager.getWorkflowBussInfoByInfoOid(infoOid);
        if(null != info){
            String bpmnXml =  info.getBpmnXml();
            jsonObject.put("bpmnXml", StrUtil.isEmpty(bpmnXml)?"":bpmnXml);
            jsonObject.put(MODEL_NAME, info.getWorkflowName());
            jsonObject.put(MODEL_ID, info.getModelId());
            jsonObject.put("infoOid",info.getInfoOid());
            jsonObject.put("timeLimit",info.getTimeLimit());
            jsonObject.put("timeUnit",info.getTimeUnit());
            return jsonObject;
        }
        return jsonObject;
    }

    /**
     * @description:  根据modelId查看部署的流程图
     * @param modelId 流程信息业务主键
     * @author: wuxx
     * @Date: 2021/2/23 14:16
     **/
    public JSONObject getEditorXmlByModelId(String modelId) {
        JSONObject jsonObject =  new JSONObject();
        //获取流程信息
        Model model = repositoryService.getModel(modelId);
        if (model != null) {
            try {
                if (StringUtils.isNotEmpty(model.getMetaInfo())) {
                    jsonObject = JSON.parseObject(model.getMetaInfo());
                } else {
                    jsonObject = new JSONObject();
                    jsonObject.put(MODEL_NAME, model.getName());
                }
                jsonObject.put(MODEL_ID, model.getId());
                jsonObject.put("infoOid",model.getKey());
                byte[] bytes = repositoryService.getModelEditorSource(model.getId());
                String bpmnXml = "";
                if(null!=bytes){
                    bpmnXml = new String(repositoryService.getModelEditorSource(model.getId()));
                }
                try {
                    //将json流程转为标准xml流程图 解决初始化报错问题
                    JSONObject editorJsonNode = JSON.parseObject(bpmnXml);
                    bpmnXml = BpmnConverterUtil.converterJsonToWebXml(editorJsonNode.toJSONString());
                }catch (Exception e){
                    //初始化使用的是json
                }
                jsonObject.put("bpmnXml", bpmnXml);
            } catch (Exception e) {
                throw new ResultInfoException("无法读取model信息", e);
            }
        } else {
            throw new ResultInfoException("未找到对应模型信息");
        }
        return jsonObject;
    }

    /**
     * @description:  根据流程部署主键查看部署的流程图
     * @param processDefId 流程部署主键
     * @author: wuxx
     * @Date: 2021/2/20 14:16
     **/
    public JSONObject flowViewByProcessDefId(String processDefId) {
        JSONObject jsonObject =  new JSONObject();
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefId).singleResult();
        //获取流程信息
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefId);
        if (bpmnModel != null) {
            try {
                //创建转换对象
                BpmnXMLConverter converter = new BpmnXMLConverter();
                //把bpmnModel对象转换成字符
                byte[] bytes = converter.convertToXML(bpmnModel);
                String bpmnXml = new String(bytes);
                String resourceName = definition.getResourceName();
                resourceName = resourceName.replaceAll(".bpmn","");
                jsonObject.put(MODEL_NAME, resourceName);
                jsonObject.put("bpmnXml", workflowUtilsManager.getRejectAndFallbackBpmnXml(bpmnXml,processDefId));
            } catch (Exception e) {
                throw new ResultInfoException("无法读取model信息", e);
            }
        } else {
            throw new ResultInfoException("未找到对应模型信息");
        }
        return jsonObject;
    }

    /**
     * @description:  部署模型
     * @param modelId 模型主键
     * @author: wuxx
     * @Date: 2021/1/20 14:16
     **/
    public String deployModel(String modelId) throws Exception {
        // 获取模型
        Model modelData = repositoryService.getModel(modelId);
        byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());
        if(null == bytes) {
            throw new ResultInfoException("模型数据为空，请先设计流程并成功保存");
        }
        String bpmnXml = new String(bytes);
        //创建转换对象
        BpmnXMLConverter converter = new BpmnXMLConverter();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        StringReader stringReader = new StringReader(bpmnXml);
        XMLStreamReader reader = factory.createXMLStreamReader(stringReader);
        //将xml文件转换成BpmnModel
        BpmnModel bpmnModel = converter.convertToBpmnModel(reader);
        Collection<FlowElement> flowElements = bpmnModel.getProcesses().get(0).getFlowElements();
        boolean startFlag = false;
        boolean endFlag = false;
        boolean userFlag = false;
        for(FlowElement flowElement:flowElements) {
            if (flowElement instanceof StartEvent) {//开始节点
                startFlag = true;
            }
            if (flowElement instanceof UserTask) {//结束节点
                userFlag = true;
            }
            if (flowElement instanceof EndEvent) {//用户节点
                endFlag = true;
            }
        }
        if(!(startFlag && endFlag)){
            throw new ResultInfoException("数据模型不符合要求，请至少设计一条主线程流");
        }
        //发布流程
        String dataJson = workflowService.initDeploymentBPMN(modelData.getName(), bpmnXml).getData();
        Map resultMap = JSONUtil.toBean(dataJson, Map.class);
        if(null==resultMap.get("id")){
            throw new ResultInfoException("部署失败！");
        }
        modelData.setDeploymentId(resultMap.get("id").toString());
        repositoryService.saveModel(modelData);
        WorkflowBussInfo info = workflowBussInfoManager.getWorkflowBussInfoByModelId(Integer.valueOf(modelId));
        if(null==info){
            throw new ResultInfoException("流程信息不能为空！");
        }
        //info.setBpmnXml(bpmnXml);
        info.setIsPublish(BaseStaticParameter.Y);
        if(StringUtils.isNotEmpty(modelId)) {
            info.setModelId(Integer.parseInt(modelId));
        }
        this.workflowBussInfoManager.saveOrUpdateWorkflowBussInfo(info);
        String processDefId = null;
        if(StrUtil.isNotEmpty(modelData.getDeploymentId())){
            //查询processDefId 是空的step数据
            //获取部署的定义
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(modelData.getDeploymentId()).singleResult();
            processDefId = processDefinition.getId();
            List<WorkflowBussFlowStep> flowStepList = workflowBussFlowStepMapper.queryWorkflowBussFlowStepByInfOidAndDefIdIsNull(info.getInfoOid());
            if(null!=flowStepList && flowStepList.size()>0){
                for (WorkflowBussFlowStep step:flowStepList){
                    step.setProcessDefId(processDefId);
                    workflowBussFlowStepMapper.saveWorkflowBussFlowStep(step);
                }
            }
        }
        return "success";
    }

    /**
     * @description: 根据流程实例ID获取流程图已办的节点
     * @param processInstanceId 流程实例ID
     * @author: wuxx
     * @Date: 2021/1/28 15:48
     **/
    public Map<String, Object> viewFlow(String processInstanceId) {
        /*
         *  获取流程实例
         */
        HistoricProcessInstance processInstance = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if(processInstance == null) {
           // log.error("流程实例ID:{}没查询到流程实例！", processInstanceId);
            return null;
        }
        if(StrUtil.isNotBlank(processInstance.getDeleteReason()) && processInstance.getDeleteReason().startsWith("删除")){
            return null;
        }
        // 根据流程对象获取流程对象模型
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        //创建转换对象
        BpmnXMLConverter converter = new BpmnXMLConverter();
        //把bpmnModel对象转换成字符
        byte[] bytes = converter.convertToXML(bpmnModel);
        String bpmnXml = new String(bytes);

        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstance.getId())
                .orderByHistoricTaskInstanceStartTime().asc().list();
        List<WorkflowTaskVo> workflowTaskVoList = new ArrayList<>();
        for(HistoricTaskInstance instance:historicTaskInstanceList){
            WorkflowTaskVo workflowTaskVo = new WorkflowTaskVo();
            workflowTaskVo.setActivityId(instance.getTaskDefinitionKey());
            workflowTaskVo.setStartDate(instance.getStartTime());
            workflowTaskVo.setEndDate(instance.getEndTime());
            workflowTaskVo.setProcessInstanceId(instance.getProcessInstanceId());
            //workflowTaskVo.setTaskId(instance.getTaskId());
            workflowTaskVo.setUserId(instance.getAssignee());
            if(StrUtil.isNotEmpty(instance.getAssignee())){
                SysUser sysUser = sysUserFeignService.getSysUserByUserOid(instance.getAssignee()).getData();
                workflowTaskVo.setUserName(null!=sysUser?sysUser.getName():"");
            }
            workflowTaskVo.setLimitDate(instance.getDueDate());
            workflowTaskVo.setTaskId(instance.getId());
            workflowTaskVo.setDeleteReason(instance.getDeleteReason());
            workflowTaskVo.setHandleStatus(checkHandleStatusByDate(workflowTaskVo,processInstance.getId()));
            workflowTaskVo.setDeleteReason(instance.getDeleteReason());
            workflowTaskVo.setHandleResult(checkHandleResult(workflowTaskVo));
            workflowTaskVoList.add(workflowTaskVo);
        }

        /*
         *  查看已执行的节点集合
         *  获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
         */
        // 构造历史流程查询
        HistoricActivityInstanceQuery historyInstanceQuery = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstance.getId());

        // 查询历史节点
        List<HistoricActivityInstance> historicActivityInstanceList = historyInstanceQuery
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();
        List<HistoricActivityInstance> callActivityList = historicActivityInstanceList
                .stream()
                .filter(historic -> historic.getActivityType().equals("callActivity"))
                .collect(Collectors.toList());
        for(HistoricActivityInstance instance:callActivityList){
            WorkflowTaskVo workflowTaskVo = new WorkflowTaskVo();
            workflowTaskVo.setActivityId(instance.getActivityId());
            workflowTaskVo.setStartDate(instance.getStartTime());
            workflowTaskVo.setEndDate(instance.getEndTime());
            workflowTaskVo.setProcessInstanceId(instance.getProcessInstanceId());
            //workflowTaskVo.setTaskId(instance.getTaskId());
            workflowTaskVo.setUserId("0");
            workflowTaskVo.setLimitDate(null);
            workflowTaskVo.setTaskId(instance.getId());
            workflowTaskVo.setDeleteReason(instance.getDeleteReason());
            workflowTaskVo.setHandleStatus(checkHandleStatusByDate(workflowTaskVo,processInstance.getId()));
            workflowTaskVo.setDeleteReason(instance.getDeleteReason());
            workflowTaskVo.setHandleResult(checkHandleResult(workflowTaskVo));
            workflowTaskVoList.add(workflowTaskVo);
        }
        //查询正在办理的环节
        List<String> havingList = historicActivityInstanceList
                .stream()
                .filter(historic ->historic.getEndTime() == null)
                .map(HistoricActivityInstance::getActivityId)
                .collect(Collectors.toList());
        if(historicActivityInstanceList == null || historicActivityInstanceList.size() == 0) {
            //log.info("流程实例ID:{}没有历史节点信息！", processInstanceId);
            return null;
        }
        Boolean over = historyTaskManager.checkProcessOver(processInstanceId);
        if(!over){
            ProcessInstance instance = processRuntime.processInstance(processInstanceId);
            if(!WorkflowStaticParameter.WORK_FLOW_SRATUS_RUNNING.equals(instance.getStatus().name())){
                havingList = new ArrayList<>();
                //暂停
                if(WorkflowStaticParameter.WORK_FLOW_SRATUS_SUSPENDED.equals(instance.getStatus().name())){
                    for(WorkflowTaskVo taskVo : workflowTaskVoList){
                        if(BaseStaticParameter.STR_TWO.equals(taskVo.getHandleStatus())
                        || BaseStaticParameter.STR_FOUR.equals(taskVo.getHandleStatus())){
                            taskVo.setHandleStatus(BaseStaticParameter.STR_SEVEN);
                        }
                    }
                }

            }
        }

        // 已执行的节点ID集合(将historicActivityInstanceList中元素的activityId字段取出封装到executedActivityIdList)
        List<String> executedActivityIdList = historicActivityInstanceList
                .stream()
                .map(HistoricActivityInstance::getActivityId)
                .collect(Collectors.toList());
        //查询正在办理或者已经办理的环节
        List<String> havingAndEndList = historicActivityInstanceList
                .stream()
                .map(HistoricActivityInstance::getActivityId)
                .collect(Collectors.toList());
        List<String> flowIds = ActivitiUtils.getHistoricActivityInstanceIdList(bpmnModel, historicActivityInstanceList,havingAndEndList);
        Map<String, Object> map= new HashMap<>();
        List<String> allFlow = new ArrayList<>();
        allFlow.addAll(flowIds);
        allFlow.addAll(executedActivityIdList);
        map.put("havingList",havingList);
        map.put("allFlow",allFlow);
        //map.put("bpmnXml",workflowUtilsManager.getRejectAndFallbackBpmnXml(bpmnXml,processInstance.getProcessDefinitionId()));
        map.put("bpmnXml",ActivitiUtils.genViewXml(workflowUtilsManager.getRejectAndFallbackBpmnXml(bpmnXml,processInstance.getProcessDefinitionId())));
        String bpmnName = bpmnModel.getSourceSystemId().replaceAll(".bpmn","");
        map.put("flowName",StrUtil.isNotEmpty(processInstance.getName())?processInstance.getName():bpmnName);
        map.put("workflowTaskVoList",workflowTaskVoList);

        map.put("processDefId",processInstance.getProcessDefinitionId());
        return map;
    }




    /**
     * @description: 根据流程实例ID和任务环节id 获取环节的办理信息
     * @param processInstanceId
     * @param activityId
     * @author: wuxx
     * @Date: 2021/1/29 10:46
     **/
    public Map<String, Object> getWorkflowTaskVoByInstanceIdAndActivityId(
            @RequestParam(name = "processInstanceId") String processInstanceId,
            @RequestParam(name = "activityId") String activityId) {
        /*
         *  查看已执行的节点集合
         *  获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
         */
        // 查询历史任务节点
        List<HistoricTaskInstance> historicActivityInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceStartTime().asc().list();
        List<HistoricTaskInstance> instanceEventList = historicActivityInstanceList
                .stream()
                .filter(historic ->historic.getTaskDefinitionKey().equals(activityId))
                .collect(Collectors.toList());
        Map<String, Object> reusltTaskMap = new HashMap<>();
        TreeMap<String, Object> taskMap = new TreeMap<>();
        List<WorkflowTaskVo> workflowTaskVoList = new ArrayList<>();
        Set<String> list = new HashSet<>();
        for(HistoricTaskInstance instance:instanceEventList){
            WorkflowTaskVo workflowTaskVo = new WorkflowTaskVo();
            workflowTaskVo.setActivityId(instance.getTaskDefinitionKey());
            workflowTaskVo.setStartDate(instance.getStartTime());
            list.add(DateUtil.format(instance.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
            workflowTaskVo.setEndDate(instance.getEndTime());
            workflowTaskVo.setProcessInstanceId(instance.getProcessInstanceId());
            //workflowTaskVo.setTaskId(instance.getTaskId());
            workflowTaskVo.setUserId(instance.getAssignee());
            if(StrUtil.isNotEmpty(instance.getAssignee())){
                SysUser sysUser = sysUserFeignService.getSysUserByUserOid(instance.getAssignee()).getData();
                workflowTaskVo.setUserName(null!=sysUser?sysUser.getName():"");
            }
            workflowTaskVo.setLimitDate(instance.getDueDate());
            workflowTaskVo.setDeleteReason(instance.getDeleteReason());
            //workflowTaskVo.setHandleResult(checkHandleResult(workflowTaskVo));
            if(StrUtil.isNotEmpty(instance.getProcessDefinitionId())){
                WorkflowBussFlowStep taskStep = workflowBussFlowStepManager.getWorkflowBussFlowStepByActivityIdAndDefId(activityId, instance.getProcessDefinitionId());
                if(null!=taskStep){
                    workflowTaskVo.setTimeLimit(taskStep.getTimeLimit());
                    workflowTaskVo.setTimeLimitHour(taskStep.getTimeLimitHour());
                    workflowTaskVo.setTimeUnit(taskStep.getTimeUnit());
                    workflowTaskVo.setHandleUserNames(taskStep.getHandleUserNames());
                    workflowTaskVo.setHandlePostNames(taskStep.getHandlePostNames());
                    workflowTaskVo.setHandleRoleNames(taskStep.getHandleRoleNames());
                    workflowTaskVo.setHandleOrganNames(taskStep.getHandleOrganNames());
                }
            }
            workflowTaskVo.setTaskId(instance.getId());
            workflowTaskVo.setHandleStatus(checkHandleStatusByDate(workflowTaskVo,processInstanceId));
            workflowTaskVo.setHandleResult(checkHandleResult(workflowTaskVo));
            if(StrUtil.isNotEmpty(instance.getDeleteReason())){
                String deleteReason = instance.getDeleteReason();
                String[] stringInfos = deleteReason.split("~");
                if(stringInfos.length==3){
                    workflowTaskVo.setDeleteReason(stringInfos[0]);
                    workflowTaskVo.setUserId(stringInfos[1]);
                    workflowTaskVo.setUserName(stringInfos[2]);
                }
            }
            workflowTaskVoList.add(workflowTaskVo);
        }
        if(list.size()>0){
            workflowTaskVoList = workflowTaskVoList.stream().sorted((vo, vo1) -> {
                return vo.getStartDate().getTime() > vo1.getStartDate().getTime()?1:-1;
            }).collect(Collectors.toList());
            for(String str : list){
                List<WorkflowTaskVo> taskVoList = workflowTaskVoList.stream().filter(taskVo -> DateUtil.format(taskVo.getStartDate(), "yyyy-MM-dd HH:mm:ss").equals(str)).collect(Collectors.toList());
                taskMap.put(str,taskVoList);
            }
        }
        reusltTaskMap.put("taskMap",taskMap);
        reusltTaskMap.put("taskMapSize",taskMap.size());
        if(taskMap.size()>0){
            List<WorkflowTaskVo> lastTaskVoList = (List<WorkflowTaskVo>) taskMap.lastEntry().getValue();
            if(lastTaskVoList.size()>0){
                WorkflowTaskVo fristVo = lastTaskVoList.size()>0?lastTaskVoList.get(lastTaskVoList.size()-1):null;
                //超期办理
                List<WorkflowTaskVo> taskingOverList = lastTaskVoList.stream().filter(taskVo -> (taskVo.getHandleStatus().equals("4"))).collect(Collectors.toList());
                //正常办理
                List<WorkflowTaskVo> taskingList = lastTaskVoList.stream().filter(taskVo -> (taskVo.getHandleStatus().equals("2"))).collect(Collectors.toList());
                //审核不通过 he 驳回 he 跳过
                List<WorkflowTaskVo> taskVoList = lastTaskVoList.stream().filter(taskVo -> (taskVo.getHandleStatus().equals("10")
                        || taskVo.getHandleStatus().equals("9") || taskVo.getHandleStatus().equals("6"))).collect(Collectors.toList());
                if(taskingOverList.size()>0){
                    reusltTaskMap.put("workflowTaskVo",taskingOverList.size()>0?taskingOverList.get(0):fristVo);
                }else if(taskingList.size()>0){
                    reusltTaskMap.put("workflowTaskVo",taskingList.size()>0?taskingList.get(0):fristVo);
                }else if(taskVoList.size()>0){
                    reusltTaskMap.put("workflowTaskVo",taskVoList.size()>0?taskVoList.get(0):fristVo);
                }else {
                    reusltTaskMap.put("workflowTaskVo",fristVo);
                }

            }
        }
        return reusltTaskMap;
    }


    /**
     * @description: 根据流程实例ID获取内嵌流程图
     * @param processInstanceId 流程实例ID
     * @author: wuxx
     * @Date: 2021/2/20 15:48
     **/
    public Map<String, Object> getInnerViewFlowByProcessInstanceIdAndActivityId(String processInstanceId,String activityId) {
        Map<String, Object> map= new HashMap<>();
        HistoricProcessInstance processInstance = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if(processInstance == null) {
            // log.error("流程实例ID:{}没查询到流程实例！", processInstanceId);
            return null;
        }

        /*
         *  查看已执行的节点集合
         *  获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
         */
        // 构造历史流程查询
        HistoricActivityInstanceQuery historyInstanceQuery = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstance.getId());

        // 查询历史节点
        List<HistoricActivityInstance> historicActivityInstanceList = historyInstanceQuery
                .orderByHistoricActivityInstanceStartTime()
                .activityId(activityId)
                .asc()
                .list();
        String innerFlowOid = null;
        if(StrUtil.isNotEmpty(processInstance.getProcessDefinitionId())){
            WorkflowBussFlowStep taskStep = workflowBussFlowStepManager.getWorkflowBussFlowStepByActivityIdAndDefId(activityId, processInstance.getProcessDefinitionId());
            map.put("taskStep",taskStep);
            innerFlowOid = taskStep.getInnerFlowOid();
        }
        if(historicActivityInstanceList == null || historicActivityInstanceList.size() == 0) {
            //log.info("流程实例ID:{}没有历史节点信息！", processInstanceId);
            if(StrUtil.isNotEmpty(innerFlowOid)){
                WorkflowBussInfo byInfoOid = workflowBussInfoManager.getWorkflowBussInfoByInfoOid(innerFlowOid);
                map.put("flowName",byInfoOid.getWorkflowName());
                map.put("taskMapSize",0);
                map.put("innerFlowOid",innerFlowOid);
            }
            return map;
        }

        String calledProcessInstanceId = historicActivityInstanceList.get(0).getCalledProcessInstanceId();
        map.put("calledProcessInstanceId",calledProcessInstanceId);
        if(StrUtil.isNotEmpty(calledProcessInstanceId)){
            HistoricProcessInstance callProcessInstance = historyService
                    .createHistoricProcessInstanceQuery()
                    .processInstanceId(calledProcessInstanceId)
                    .singleResult();
            if(processInstance == null) {
                // log.error("流程实例ID:{}没查询到流程实例！", processInstanceId);
                if(StrUtil.isNotEmpty(innerFlowOid)){
                    WorkflowBussInfo byInfoOid = workflowBussInfoManager.getWorkflowBussInfoByInfoOid(innerFlowOid);
                    map.put("flowName",byInfoOid.getWorkflowName());
                    map.put("taskMapSize",0);
                }
                return map;
            }
            WorkflowBussFlowStep taskStep = new WorkflowBussFlowStep();
            taskStep.setProcessDefId(callProcessInstance.getProcessDefinitionId());
            List<WorkflowBussFlowStep> flowStepList = workflowBussFlowStepManager.queryWorkflowBussFlowStepList(taskStep);
            if(null!=flowStepList && flowStepList.size()>0){
                WorkflowBussInfo byInfoOid = workflowBussInfoManager.getWorkflowBussInfoByInfoOid(flowStepList.get(0).getInfoOid());
                map.put("flowName",byInfoOid.getWorkflowName());
            }
        }

        List<HistoricActivityInstance> callActivityList = historicActivityInstanceList
                .stream()
                .filter(historic -> historic.getActivityId().equals(activityId))
                .collect(Collectors.toList());
        TreeMap<String, Object> taskMap = new TreeMap<>();
        List<WorkflowTaskVo> workflowTaskVoList = new ArrayList<>();
        Set<String> list = new HashSet<>();
        for(HistoricActivityInstance instance:callActivityList){
            WorkflowTaskVo workflowTaskVo = new WorkflowTaskVo();
            workflowTaskVo.setActivityId(instance.getActivityId());
            workflowTaskVo.setStartDate(instance.getStartTime());
            list.add(DateUtil.format(instance.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
            workflowTaskVo.setEndDate(instance.getEndTime());
            workflowTaskVo.setProcessInstanceId(instance.getProcessInstanceId());
            workflowTaskVo.setUserId("0");
            workflowTaskVo.setDeleteReason(instance.getDeleteReason());
            if(StrUtil.isNotEmpty(instance.getProcessDefinitionId())){
                WorkflowBussFlowStep taskStep = workflowBussFlowStepManager.getWorkflowBussFlowStepByActivityIdAndDefId(activityId, instance.getProcessDefinitionId());
                if(null!=taskStep){
                    workflowTaskVo.setTimeLimit(taskStep.getTimeLimit());
                    workflowTaskVo.setTimeLimitHour(taskStep.getTimeLimitHour());
                    workflowTaskVo.setTimeUnit(taskStep.getTimeUnit());
                    Date newDueDate = LimitDateCalc.dateCalc(instance.getStartTime(),taskStep.getTimeLimit(), taskStep.getTimeUnit(), taskStep.getTimeLimitHour());
                    workflowTaskVo.setLimitDate(newDueDate);
                }
            }
            workflowTaskVo.setHandleStatus(checkHandleStatusByDate(workflowTaskVo,processInstanceId));
            workflowTaskVo.setHandleResult(checkHandleResult(workflowTaskVo));
            workflowTaskVoList.add(workflowTaskVo);
        }
        if(list.size()>0){
            workflowTaskVoList = workflowTaskVoList.stream().sorted((vo, vo1) -> {
                return vo.getStartDate().getTime() > vo1.getStartDate().getTime()?1:-1;
            }).collect(Collectors.toList());
            for(String str : list){
                List<WorkflowTaskVo> taskVoList = workflowTaskVoList.stream().filter(taskVo -> DateUtil.format(taskVo.getStartDate(), "yyyy-MM-dd HH:mm:ss").equals(str)).collect(Collectors.toList());
                taskMap.put(str,taskVoList);
            }
        }
        map.put("taskMap",taskMap);
        map.put("taskMapSize",taskMap.size());
        map.put("workflowTaskVo",workflowTaskVoList.size()>0?workflowTaskVoList.get(workflowTaskVoList.size()-1):null);
        return map;
    }


    /**
     * @description: 检测流程办理状态（1.等待办理 2.正在办理 3.按时办理 4.超期未办理  5.超期办理 6跳过 7.暂停 8终止流程,9 驳回退回 10审核不通过）
     * @param workflowTaskVo 流程vo
     * @author: wuxx
     * @Date: 2021/1/29 11:37
     **/
    private String checkHandleStatusByDate(WorkflowTaskVo workflowTaskVo,String processInstanceId){
        //判断是否驳回退回状态
        if(StrUtil.isNotBlank(workflowTaskVo.getTaskId())){
            WorkflowFallback fallback = workflowFallbackManager.getWorkflowFallbackByTaskId(workflowTaskVo.getTaskId(),null);
            //判断是否退回状态
            if(null != fallback && 2 == fallback.getFallbackType()){
                //6跳过
                return BaseStaticParameter.STR_SIX;
            }else if(null != fallback){
                //9.驳回退回
                return BaseStaticParameter.STR_NINE;
            }
        }
        if(StrUtil.isNotEmpty(workflowTaskVo.getDeleteReason())){
            String[] strings = workflowTaskVo.getDeleteReason().split("~");
            if(strings.length>0){
                String reason = strings[0];
                if("中止".equals(reason)){
                    //8.中止流程
                    return BaseStaticParameter.STR_EIGHT;
                }else if("审核不通过".equals(reason)){
                    //10.审核不通过
                    return BaseStaticParameter.STR_TEN;
                }
            }
        }
        String limitDate = DateUtil.format(workflowTaskVo.getLimitDate(), "yyyy-MM-dd HH:mm:ss");
        String startDate = DateUtil.format(workflowTaskVo.getStartDate(), "yyyy-MM-dd HH:mm:ss");
        if(null==workflowTaskVo.getEndDate()){
            Boolean over = historyTaskManager.checkProcessOver(processInstanceId);
            if(!over){
                ProcessInstance instance = processRuntime.processInstance(processInstanceId);
                if(!WorkflowStaticParameter.WORK_FLOW_SRATUS_RUNNING.equals(instance.getStatus().name())){
                    if(WorkflowStaticParameter.WORK_FLOW_SRATUS_SUSPENDED.equals(instance.getStatus().name())){
                        //7.暂停
                        return BaseStaticParameter.STR_SEVEN;
                    }

                }
            }
           if(null!=workflowTaskVo.getLimitDate()
                    && !limitDate.equals(startDate)
                    && workflowTaskVo.getLimitDate().before(new Date())){
                //4.超期未办理
                return BaseStaticParameter.STR_FOUR;
            }else {
                //2.正在办理
                return BaseStaticParameter.STR_TWO;
            }
        }else {
            if(StrUtil.isEmpty(workflowTaskVo.getUserId())){
                //6.跳过
                return BaseStaticParameter.STR_SIX;
            }else{
                if(null!=workflowTaskVo.getLimitDate()
                        && !limitDate.equals(startDate)
                        && workflowTaskVo.getLimitDate().before(workflowTaskVo.getEndDate())){
                    //5.超期办理
                    return BaseStaticParameter.STR_FIVE;
                }else {
                    //3.按时办理
                    return BaseStaticParameter.STR_THREE;
                }
            }
        }
    }


    /**
     * @description: 获取办理状态 （0未办理  1审核通过  2审核不通过 3驳回 4退回 5跳过）
     * @param workflowTaskVo
     * @author: wuxx
     * @Date: 2021/2/25 13:44
     **/
    private String checkHandleResult(WorkflowTaskVo workflowTaskVo){
        //判断是否驳回退回状态
        if(StrUtil.isNotBlank(workflowTaskVo.getTaskId())){
           WorkflowFallback tgfallback = workflowFallbackManager.getWorkflowFallbackByTaskId(workflowTaskVo.getTaskId(),2);
            //判断是否跳过
            if(null != tgfallback ){
                //5.跳过
                return BaseStaticParameter.STR_FIVE;
            }
            //判断是否退回状态
            WorkflowFallback bhfallback = workflowFallbackManager.getWorkflowFallbackByTaskId(workflowTaskVo.getTaskId(),BaseStaticParameter.N);
            if(null != bhfallback){
                //3.驳回
                return BaseStaticParameter.STR_THREE;
            }
            WorkflowFallback thfallback = workflowFallbackManager.getWorkflowFallbackByTaskId(workflowTaskVo.getTaskId(),BaseStaticParameter.Y);
            if(null != thfallback){
                //4.退回
                return BaseStaticParameter.STR_FOUR;
            }
        }
        if(StrUtil.isNotEmpty(workflowTaskVo.getDeleteReason())){
            String[] strings = workflowTaskVo.getDeleteReason().split("~");
            if(strings.length>0){
                String flag = strings[0];
                if("中止".equals(flag)){
                    return null;
                }else if("审核不通过".equals(flag)){
                    return BaseStaticParameter.STR_TWO;
                }
            }
        }
        if(BaseStaticParameter.STR_TWO.equals(workflowTaskVo.getHandleStatus()) || BaseStaticParameter.STR_FOUR.equals(workflowTaskVo.getHandleStatus())){
            return BaseStaticParameter.STR_ZERO;
        }
        if(BaseStaticParameter.STR_THREE.equals(workflowTaskVo.getHandleStatus()) || BaseStaticParameter.STR_FIVE.equals(workflowTaskVo.getHandleStatus())){
            return BaseStaticParameter.STR_ONE;
        }
        if(BaseStaticParameter.STR_SEVEN.equals(workflowTaskVo.getHandleStatus())){
            return null;
        }
        return BaseStaticParameter.STR_ONE;
    }

    /**
     * @description: 根据流程实例ID和任务环节id 获取环节Step办理信息
     * @param processInstanceId 实列oid
     * @param activityId 环节oid
     * @author: wuxx
     * @Date: 2021/1/29 10:46
     **/
    public WorkflowBussFlowStep getWorkflowStepByInstanceIdAndActivityId(String processInstanceId,String activityId) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        String processDefinitionId = historicProcessInstance.getProcessDefinitionId();
        WorkflowBussFlowStep taskStep = workflowBussFlowStepManager.getWorkflowBussFlowStepByActivityIdAndDefId(activityId, processDefinitionId);
        return  taskStep;
    }

    /**
     * @description: 根据流程实例ID和任务环节id 获取环节Step办理信息
     * @param infoOid 流程oid
     * @param activityId 环节oid
     * @author: wuxx
     * @Date: 2021/2/22 10:46
     **/
    public WorkflowBussFlowStep getWorkflowBussFlowStepByInfoIdAndActivityId(String infoOid,String activityId) {
        WorkflowBussInfo workflowBussInfo = workflowBussInfoManager.getWorkflowBussInfoByInfoOid(infoOid);
        Model model = repositoryService.getModel(workflowBussInfo.getModelId() + "");
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().deploymentId(model.getDeploymentId()).latestVersion().singleResult();
        WorkflowBussFlowStep taskStep = null;
        if(null!=workflowBussInfo){
            taskStep = this.getWorkflowBussFlowStepByProcessDefIdAndActivityId(definition.getId(),activityId);
        }
        if(null == taskStep){
            taskStep = workflowBussFlowStepManager.getWorkflowBussFlowStepByInfoIdAndActivityId(infoOid, activityId);
        }
        return taskStep;
    }

    /**
     * @description: 根据流程部署ID和任务环节id 获取环节Step办理信息
     * @param processDefId 流程部署ID
     * @param activityId 环节oid
     * @author: wuxx
     * @Date: 2021/2/22 10:46
     **/
    public WorkflowBussFlowStep getWorkflowBussFlowStepByProcessDefIdAndActivityId(String processDefId,String activityId) {
        WorkflowBussFlowStep taskStep = workflowBussFlowStepManager.getWorkflowBussFlowStepByActivityIdAndDefId(activityId,processDefId);
        return  taskStep;
    }

    /**
     * 根据生成的ID获取模型流程编辑器
     * @param modelId
     * @return
     */
    public JSONObject getEditorSourceExtraXml(@PathVariable String modelId) {
        JSONObject jsonObject = null;
        Model model = repositoryService.getModel(modelId);
        if (model != null) {
            try {
                if (StringUtils.isNotEmpty(model.getMetaInfo())) {
                    jsonObject = JSON.parseObject(model.getMetaInfo());
                } else {
                    jsonObject = new JSONObject();
                    jsonObject.put(MODEL_NAME, model.getName());
                }
                jsonObject.put(MODEL_ID, model.getId());
                byte[] bytes = repositoryService.getModelEditorSourceExtra(model.getId());
                String svgXml = "";
                if(null!=bytes){
                    svgXml = new String(repositoryService.getModelEditorSourceExtra(model.getId()));
                }
                try {
                    //将json流程转为标准xml流程图 解决初始化报错问题
                    JSONObject editorJsonNode = JSON.parseObject(svgXml);
                    svgXml = BpmnConverterUtil.converterJsonToWebXml(editorJsonNode.toJSONString());
                }catch (Exception e){
                    //初始化使用的是json
                }
                jsonObject.put("svgXml", svgXml);
            } catch (Exception e) {
                LOG.error("创建model的json串失败", e);
                throw new ActivitiException("无法读取model信息", e);
            }
        } else {
            LOG.error("创建model的json串失败[{}]", modelId);
            throw new ActivitiException("未找到对应模型信息");
        }
        return jsonObject;
    }
}
