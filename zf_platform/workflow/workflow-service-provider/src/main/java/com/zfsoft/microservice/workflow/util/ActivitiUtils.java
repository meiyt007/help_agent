package com.zfsoft.microservice.workflow.util;

import cn.hutool.core.util.StrUtil;
import org.activiti.bpmn.model.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: Activiti 工具类
 * @author: wuxx
 * @Date: 2021/1/28 15:45
 **/
public class ActivitiUtils {

    /**
     * 用户任务节点的类型
     */
    private static final String USER_TASK_TYPE = "userTask";

    /**
     * 获取流程走过的线
     *
     * @param bpmnModel                 流程对象模型
     * @param processDefinitionEntity   流程定义对象
     * @param historicActivityInstances 历史流程已经执行的节点，并已经按执行的先后顺序排序
     * @return java.util.List<java.lang.String>
     * @author wuxx
     * @date 2021/1/28 15:45
     * @version V1.0.0-RELEASE
     */
    public static List<String> getHighLightedFlows(BpmnModel bpmnModel,
                                                   ProcessDefinitionEntity processDefinitionEntity,
                                                   List<HistoricActivityInstance> historicActivityInstances) {
        // 定义用以保存高亮的线 flowId
        List<String> highFlows = new ArrayList<>();

        // 传入参数校验
        if (CollectionUtils.isEmpty(historicActivityInstances)) {
            return highFlows;
        }

        // 遍历历史节点
        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {
            // 取出已执行的节点
            HistoricActivityInstance historicActivityInstance = historicActivityInstances.get(i);

            // 用以保存后续开始时间相同的节点
            List<FlowNode> sameStartTimeNodes = new ArrayList<FlowNode>();

            // 获取下一个节点（用于连线）
            FlowNode sameActivityImpl = getNextFlowNode(bpmnModel, historicActivityInstances, i, historicActivityInstance);

            // 将后面第一个节点放在时间相同节点的集合里
            if (sameActivityImpl != null) {
                sameStartTimeNodes.add(sameActivityImpl);
            }

            // 循环后面节点，看是否有与此后继节点开始时间相同的节点，有则添加到后继节点集合
            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
                // 后续第一个节点
                HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);
                // 后续第二个节点
                HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);
                if (activityImpl1.getStartTime().getTime() != activityImpl2.getStartTime().getTime()) {
                    break;
                }

                // 如果第一个节点和第二个节点开始时间相同保存
                FlowNode sameActivityImpl2 = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityImpl2.getActivityId());
                sameStartTimeNodes.add(sameActivityImpl2);
            }

            // 得到节点定义的详细信息
            FlowNode activityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(i).getActivityId());
            // 取出节点的所有出去的线，对所有的线进行遍历
            List<SequenceFlow> pvmTransitions = activityImpl.getOutgoingFlows();
            for (SequenceFlow pvmTransition : pvmTransitions) {
                // 获取节点
                FlowNode pvmActivityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(pvmTransition.getTargetRef());

                // 不是后继节点
                if (!sameStartTimeNodes.contains(pvmActivityImpl)) {
                    continue;
                }

                // 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                highFlows.add(pvmTransition.getId());
            }
        }

        //返回高亮的线
        return highFlows;
    }


    /**
     * 获取下一个节点信息
     *
     * @param bpmnModel                 流程模型
     * @param historicActivityInstances 历史节点
     * @param i                         当前已经遍历到的历史节点索引（找下一个节点从此节点后）
     * @param historicActivityInstance  当前遍历到的历史节点实例
     * @return org.activiti.bpmn.model.FlowNode
     * @author wuxx
     * @date 2021/1/28 15:45
     * @version V1.0.0-RELEASE
     */
    private static FlowNode getNextFlowNode(BpmnModel bpmnModel,
                                            List<HistoricActivityInstance> historicActivityInstances,
                                            int i,
                                            HistoricActivityInstance historicActivityInstance) {
        // 保存后一个节点
        FlowNode sameActivityImpl = null;

        // 如果当前节点不是用户任务节点，则取排序的下一个节点为后续节点
        if (!USER_TASK_TYPE.equals(historicActivityInstance.getActivityType())) {
            // 是最后一个节点，没有下一个节点
            if (i == historicActivityInstances.size()) {
                return null;
            }
            // 不是最后一个节点，取下一个节点为后继节点
            // 找到紧跟在后面的一个节点
            sameActivityImpl = (FlowNode) bpmnModel
                    .getMainProcess()
                    .getFlowElement(historicActivityInstances.get(i + 1).getActivityId());
            // 返回
            return sameActivityImpl;
        }

        // 遍历后续节点，获取当前节点后续节点
        for (int k = i + 1; k <= historicActivityInstances.size() - 1; k++) {
            // 后续节点
            HistoricActivityInstance activityImp2_ = historicActivityInstances.get(k);
            // 都是userTask，且主节点与后续节点的开始时间相同，说明不是真实的后继节点
            if (USER_TASK_TYPE.equals(activityImp2_.getActivityType())
                    && historicActivityInstance.getStartTime().getTime()
                    == activityImp2_.getStartTime().getTime()) {
                continue;
            }
            // 找到紧跟在后面的一个节点
            sameActivityImpl = (FlowNode) bpmnModel
                    .getMainProcess()
                    .getFlowElement(historicActivityInstances.get(k).getActivityId());
            break;
        }

        return sameActivityImpl;
    }


    /**
     * @param bpmnModel                    bpmnModel 流程模型
     * @param historicActivityInstanceList 当前遍历到的历史节点实例集合
     * @description: 根据实列获取已经完成的线id集合
     * @author: wuxx
     * @Date: 2021/1/29 9:26
     **/
    public static List<String> getHistoricActivityInstanceIdList(BpmnModel bpmnModel, List<HistoricActivityInstance> historicActivityInstanceList, List<String> havingAndEndList) {
        List<HistoricActivityInstance> startEventList = historicActivityInstanceList
                .stream()
                .filter(historic -> historic.getActivityType().equals("startEvent"))
                .collect(Collectors.toList());
        //正在进行的环节
        List<String> havingEventList = historicActivityInstanceList
                .stream()
                .filter(historic -> historic.getEndTime() == null)
                .map(HistoricActivityInstance::getActivityId)
                .collect(Collectors.toList());
        HistoricActivityInstance startEventInstance = startEventList.get(0);
        // 得到节点定义的详细信息
        FlowNode activityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(startEventInstance.getActivityId());
        List<SequenceFlow> outcomingFlows = activityImpl.getOutgoingFlows();
        List<String> newList = new ArrayList<>();
        List<String> stringList = getActivityInstanceIdList(outcomingFlows, havingEventList, newList, havingAndEndList);
        return stringList;
    }

    /**
     * @param outcomingFlows  下个环节flow
     * @param havingEventList 正在进行的节点
     * @param stringList      返回线的集合
     * @description: 获取环节的线ID
     * @author: wuxx
     * @Date: 2021/1/29 9:25
     **/
    public static List<String> getActivityInstanceIdList(List<SequenceFlow> outcomingFlows, List<String> havingEventList, List<String> stringList, List<String> havingAndEndList) {
        if (null == outcomingFlows) {
            return stringList;
        }
        for (SequenceFlow flow : outcomingFlows) {
            String id = flow.getId();
            String targetRef = flow.getTargetRef();
            if (null == targetRef) {
                break;
            }
            FlowElement flowElement = flow.getTargetFlowElement();
            List<SequenceFlow> outgoingFlows = null;
            if (flowElement instanceof EndEvent) {
                if (havingAndEndList.contains(flowElement.getId())) {
                    stringList.add(id);
                    break;
                } else {
                    break;
                }
            }
            if (!havingAndEndList.contains(flowElement.getId())) {
                continue;
            }
            if (flowElement instanceof UserTask) {//节点
                outgoingFlows = ((UserTask) flowElement).getOutgoingFlows();
            } else if (flowElement instanceof ExclusiveGateway) { //如果流向线路为排他网关
                if (havingEventList.contains(flow.getSourceRef())) {
                    continue;
                }
                outgoingFlows = ((ExclusiveGateway) flowElement).getOutgoingFlows();
            } else if (flowElement instanceof ParallelGateway) {//如果流向线路为并行网关
                if (havingEventList.contains(flow.getSourceRef())) {
                    continue;
                }
                outgoingFlows = ((ParallelGateway) flowElement).getOutgoingFlows();
            }
            stringList.add(id);
            getActivityInstanceIdList(outgoingFlows, havingEventList, stringList, havingAndEndList);
        }
        return stringList;
    }


    /**
     * 获取流程查看的xml
     * @author zhongxx
     * @date 2021-02-22 09:35
     * @param bpmnXml
     * @return xml
     */
    public static String genViewXml(String bpmnXml) {
        Document document = Jsoup.parse(bpmnXml, "", Parser.xmlParser());
        //3.获取元素对象 Element
        Elements elements = document.getElementsByTag("definitions");
        Element element = elements.get(0);
        //获取流程位置图形定义
        Elements bpmnDiagram = element.getElementsByTag("bpmndi:BPMNDiagram");
        //获取线位置
        Elements bpmnPlane = bpmnDiagram.get(0).getElementsByTag("bpmndi:BPMNPlane");

        //获取流程过程
        Elements process = element.getElementsByTag("process");
        //获取网关过程
        Elements gateways = process.get(0).getElementsByTag("parallelGateway");
        gateways.addAll(process.get(0).getElementsByTag("exclusiveGateway"));
        if (gateways.size() > 0) {
            Element gateway = gateways.get(0);
            String getewayId = gateway.attr("id").trim();
            //网关内部出线定义查询
            Elements incoming = process.get(0).getElementsByAttributeValue("targetRef", getewayId);
            Elements outgoing = process.get(0).getElementsByAttributeValue("sourceRef", getewayId);

            if (incoming.size() >= outgoing.size()) {
                //输入线大于输出线
                //更改线定义
                for (int i = 0; i < incoming.size(); i++) {
                    //处理线的起始点、结束点的定义
                    Elements incomSequenceFlow = process.get(0).getElementsByAttributeValue("id", incoming.get(i).attr("id").trim());
                    Elements outSequenceFlow = process.get(0).getElementsByAttributeValue("id", outgoing.get((i + 1) % outgoing.size()).attr("id").trim());
                    incomSequenceFlow.get(0).attr("targetRef", outSequenceFlow.get(0).attr("targetRef"));
                    if(StrUtil.isNotBlank(outSequenceFlow.get(0).attr("name"))){
                        incomSequenceFlow.get(0).attr("name", outSequenceFlow.get(0).attr("name"));
                    }
                    //去除条件
                    Elements conditionExpression = incomSequenceFlow.get(0).getElementsByTag("conditionExpression");
                    if(conditionExpression.size()>0){
                        conditionExpression.remove();
                    }
                    //处理线在图中的位置
                    Elements gatewayShape = bpmnPlane.get(0).getElementsByAttributeValue("bpmnElement", getewayId);
                    Elements gatewayBounds = gatewayShape.get(0).getElementsByTag("omgdc:Bounds");
                    float gatewayX = Float.parseFloat(gatewayBounds.get(0).attr("x"));
                    float gatewayY = Float.parseFloat(gatewayBounds.get(0).attr("y"));
                    String incomSequenceFlowId = incoming.get(i).attr("id").trim();
                    String outgoSequenceFlowId = outgoing.get((i + 1) % outgoing.size()).attr("id").trim();
                    Elements incomEdges = bpmnPlane.get(0).getElementsByAttributeValue("bpmnElement", incomSequenceFlowId);
                    Elements outgoEdges = bpmnPlane.get(0).getElementsByAttributeValue("bpmnElement", outgoSequenceFlowId);
                    String incomEdgehtml = incomEdges.get(0).html();
                    String outgoEdgeHtml = outgoEdges.get(0).html();
                    incomEdgehtml = incomEdgehtml.replaceAll(String.valueOf(gatewayX), String.valueOf(gatewayX + 25));
                    incomEdgehtml = incomEdgehtml.replaceAll(String.valueOf(gatewayY + 50), String.valueOf(gatewayY + 25));
                    incomEdgehtml = incomEdgehtml.replaceAll(String.valueOf(gatewayY), String.valueOf(gatewayY + 25));
                    outgoEdgeHtml = outgoEdgeHtml.replaceAll(String.valueOf(gatewayX + 50), String.valueOf(gatewayX + 25));
                    incomEdges.get(0).html(incomEdgehtml + outgoEdgeHtml);
                }

                for (Element out : outgoing) {
                    //输出线图，去除
                    String outgoSequenceFlowId = out.attr("id").trim();
                    bpmnPlane.get(0).getElementsByAttributeValue("bpmnElement", outgoSequenceFlowId).remove();
                    //输出线定义，去除
                    process.get(0).getElementsByAttributeValue("id", outgoSequenceFlowId).remove();
                }

            } else {
                //输出线大于输入线
                //更改线定义
                for (int i = 0; i < outgoing.size(); i++) {
                    //处理线的起始点、结束点的定义
                    Elements outSequenceFlow = process.get(0).getElementsByAttributeValue("id", outgoing.get(i).attr("id").trim());
                    Elements incomSequenceFlow = process.get(0).getElementsByAttributeValue("id", incoming.get((i + 1) % incoming.size()).attr("id").trim());
                    outSequenceFlow.get(0).attr("sourceRef", incomSequenceFlow.get(0).attr("sourceRef"));
                    if(StrUtil.isNotBlank(incomSequenceFlow.get(0).attr("name"))){
                        outSequenceFlow.get(0).attr("name", incomSequenceFlow.get(0).attr("name"));
                    }
                    //
                    Elements conditionExpression = outSequenceFlow.get(0).getElementsByTag("conditionExpression");
                    if(conditionExpression.size()>0){
                        conditionExpression.remove();
                    }

                    //处理线在图中的位置
                    Elements gatewayShape = bpmnPlane.get(0).getElementsByAttributeValue("bpmnElement", getewayId);
                    Elements gatewayBounds = gatewayShape.get(0).getElementsByTag("omgdc:Bounds");
                    float gatewayX = Float.parseFloat(gatewayBounds.get(0).attr("x"));
                    float gatewayY = Float.parseFloat(gatewayBounds.get(0).attr("y"));
                    String outgoSequenceFlowId = outgoing.get(i).attr("id").trim();
                    String incomSequenceFlowId = incoming.get((i + 1) % incoming.size()).attr("id").trim();
                    Elements incomEdges = bpmnPlane.get(0).getElementsByAttributeValue("bpmnElement", incomSequenceFlowId);
                    Elements outgoEdges = bpmnPlane.get(0).getElementsByAttributeValue("bpmnElement", outgoSequenceFlowId);
                    String incomEdgehtml = incomEdges.get(0).html();
                    String outgoEdgeHtml = outgoEdges.get(0).html();
                    incomEdgehtml = incomEdgehtml.replaceAll(String.valueOf(gatewayX), String.valueOf(gatewayX + 25));
                    incomEdgehtml = incomEdgehtml.replaceAll(String.valueOf(gatewayY + 50), String.valueOf(gatewayY + 25));
                    incomEdgehtml = incomEdgehtml.replaceAll(String.valueOf(gatewayY), String.valueOf(gatewayY + 25));
                    outgoEdgeHtml = outgoEdgeHtml.replaceAll(String.valueOf(gatewayX + 50), String.valueOf(gatewayX + 25));
                    outgoEdges.get(0).html(incomEdgehtml + outgoEdgeHtml);
                }

                for (Element incom : incoming) {
                    //输出线图，去除
                    String incomSequenceFlowId = incom.attr("id").trim();
                    bpmnPlane.get(0).getElementsByAttributeValue("bpmnElement", incomSequenceFlowId).remove();
                    //输出线定义，去除
                    process.get(0).getElementsByAttributeValue("id", incomSequenceFlowId).remove();
                }
            }
            gateway.remove();
            bpmnPlane.get(0).getElementsByAttributeValue("bpmnElement", getewayId).remove();
            return genViewXml(document.html());
        }
        return document.html();
    }

    /**
     * 处理流程线的定义与图的定义
     * @author zhongxx
     * @date 2021-02-22 10:46
     * @param maxSequenceFlowId 多的线的id
     * @param minSequenceFlowId 少的线的id
     * @param incomSequenceFlowId 流入线的id
     * @param outGoSequenceFlowId 流出线的id
     * @param attrKey 属性key
     * @param getewayId 网关id
     * @param process 流程过程定义
     * @param bpmnPlane 流程图形定义
     */
    public static void formatSequenceFlow(String maxSequenceFlowId,String minSequenceFlowId,String incomSequenceFlowId,String outGoSequenceFlowId,String attrKey,String getewayId,Elements process,Elements bpmnPlane){
        //处理线的起始点、结束点的定义
        Elements maxSequenceFlow = process.get(0).getElementsByAttributeValue("id", maxSequenceFlowId);
        Elements minSequenceFlow = process.get(0).getElementsByAttributeValue("id", minSequenceFlowId);
        maxSequenceFlow.get(0).attr(attrKey, minSequenceFlow.get(0).attr(attrKey));
        if(StrUtil.isNotBlank(minSequenceFlow.get(0).attr("name"))){
            maxSequenceFlow.get(0).attr("name", minSequenceFlow.get(0).attr("name"));
        }
        //去除条件
        Elements conditionExpression = maxSequenceFlow.get(0).getElementsByTag("conditionExpression");
        if(conditionExpression.size()>0){
            conditionExpression.remove();
        }
        //处理线在图中的位置
        Elements gatewayShape = bpmnPlane.get(0).getElementsByAttributeValue("bpmnElement", getewayId);
        Elements gatewayBounds = gatewayShape.get(0).getElementsByTag("omgdc:Bounds");
        float gatewayX = Float.parseFloat(gatewayBounds.get(0).attr("x"));
        float gatewayY = Float.parseFloat(gatewayBounds.get(0).attr("y"));
//        String incomSequenceFlowId = incoming.get(i).attr("id").trim();
//        String outgoSequenceFlowId = outgoing.get((i + 1) % outgoing.size()).attr("id").trim();
        Elements incomEdges = bpmnPlane.get(0).getElementsByAttributeValue("bpmnElement", incomSequenceFlowId);
        Elements outgoEdges = bpmnPlane.get(0).getElementsByAttributeValue("bpmnElement", outGoSequenceFlowId);
        String incomEdgehtml = incomEdges.get(0).html();
        String outgoEdgeHtml = outgoEdges.get(0).html();
        incomEdgehtml = incomEdgehtml.replaceAll(String.valueOf(gatewayX), String.valueOf(gatewayX + 25));
        incomEdgehtml = incomEdgehtml.replaceAll(String.valueOf(gatewayY + 50), String.valueOf(gatewayY + 25));
        incomEdgehtml = incomEdgehtml.replaceAll(String.valueOf(gatewayY), String.valueOf(gatewayY + 25));
        outgoEdgeHtml = outgoEdgeHtml.replaceAll(String.valueOf(gatewayX + 50), String.valueOf(gatewayX + 25));
        incomEdges.get(0).html(incomEdgehtml + outgoEdgeHtml);
    }

    //自动美化测试
    public static String formatBpmnXml(String new_bpmb_xml) {
        //String new_bpmb_xml = document.html();
        //System.err.println(new_bpmb_xml);
        //创建转换对象
        /*BpmnModel bpmnModel = null;
        BpmnXMLConverter converter = new BpmnXMLConverter();
        StringReader stringReader = new StringReader(new_bpmb_xml);
        try {
            // XMLStreamReader读取XML资源
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(stringReader);
            bpmnModel = converter.convertToBpmnModel(reader);
            new BpmnAutoLayout(bpmnModel).execute();
        } catch (Exception e) {

        }
        byte[] bytes = converter.convertToXML(bpmnModel);
        return new String(bytes);*/
        return null;
    }

}
