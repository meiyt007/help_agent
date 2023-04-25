package com.zfsoft.microservice.workflow.manager;

import cn.hutool.json.JSONUtil;
import com.zfsoft.microservice.workflow.util.PageUtils;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 工作流历史记录类
 * @author: wuxx
 * @Date: 2021/1/13 9:16
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "workflow:historyTask")
public class HistoryTaskManager {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ProcessEngine processEngine;
    /**
     * @description: 根据用户oid查询历史记录
     * @author: wuxx
     * @Date: 2021/1/13 10:48
     **/
    public String queryHistoryTaskByUserOid(String userOid,Integer pageNumber, Integer pageSize){
        try {
            //分页参数
            if (null == pageNumber || pageNumber <= 1) {
                pageNumber = 1;
            }
            if (null == pageSize || pageSize <= 0) {
                pageSize = 10;
            }
            List<HistoricTaskInstance> list = historyService
                    .createHistoricTaskInstanceQuery()
                    .orderByHistoricTaskInstanceEndTime().desc()
                    .taskAssignee(userOid)
                    .list();
            int totalSize = list.size();
            list = PageUtils.startPage(list,pageNumber,pageSize);
            Map<String, Object> jsonMap = new HashMap<>();
            List<Map<String,Object>> mapList = new ArrayList<>();
            for(HistoricTaskInstance taskInstance : list){
                Map<String,Object> reulstMap = new HashMap<>();
                reulstMap.put("HistoryTaskId",taskInstance.getId());
                reulstMap.put("name",taskInstance.getName());
                reulstMap.put("processInstanceId",taskInstance.getProcessInstanceId());
                String processInstanceId = taskInstance.getProcessInstanceId();
                HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
                String superProcessInstanceId = historicProcessInstance.getSuperProcessInstanceId();
                reulstMap.put("superProcessInstanceId",superProcessInstanceId);
                reulstMap.put("taskDefinitionKey",taskInstance.getTaskDefinitionKey());
                reulstMap.put("startTime",taskInstance.getStartTime());
                reulstMap.put("endTime",taskInstance.getEndTime());
                mapList.add(reulstMap);
            }
            jsonMap.put("data",mapList);
            jsonMap.put("totalSize",totalSize);
            return JSONUtil.toJsonStr(jsonMap);
        }catch (Exception e){
            e.printStackTrace();
            return null;
            //throw new ResultInfoException("根据用户oid查询历史记录异常！",e);
        }
    }

    /**
     * @description: 根据流程实例ID查询历史
     * @author: wuxx
     * @Date: 2021/1/13 11:00
     **/
    public String getHistoricTaskInstanceByPiId(String processInstanceId){
        try {
            List<HistoricTaskInstance> list = historyService
                    .createHistoricTaskInstanceQuery()
                    .orderByHistoricTaskInstanceEndTime().asc()
                    .processInstanceId(processInstanceId)
                    .list();
            List<Map<String,Object>> mapList = new ArrayList<>();
            for(HistoricTaskInstance taskInstance : list){
                Map<String,Object> reulstMap = new HashMap<>();
                reulstMap.put("historyTaskId",taskInstance.getId());
                reulstMap.put("name",taskInstance.getName());
                reulstMap.put("processInstanceId",taskInstance.getProcessInstanceId());
                reulstMap.put("taskDefinitionKey",taskInstance.getTaskDefinitionKey());
                reulstMap.put("startTime",taskInstance.getStartTime());
                reulstMap.put("endTime",taskInstance.getEndTime());
                mapList.add(reulstMap);
            }
            return JSONUtil.toJsonStr(mapList);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResultInfoException("根据流程实例ID查询历史异常！",e);
        }
    }

    /**
     * @description: 根据流程实例ID查询当前流程是否结束(true 结束，false 未结束)
     * @param processInstanceId 流程实例ID
     * @author: wuxx
     * @Date: 2021/1/15 16:47
     **/
    public Boolean checkProcessOver(String processInstanceId) {
        ProcessInstance pi=processEngine.getRuntimeService() // 获取运行时Service
                .createProcessInstanceQuery() // 创建流程实例查询
                .processInstanceId(processInstanceId) // 用流程实例id查询
                .singleResult();
        if(pi!=null){
            return false;
        }else{
            /**查询历史，获取流程的相关信息*/
            HistoricProcessInstance hpi = historyService
                    .createHistoricProcessInstanceQuery()//
                    .processInstanceId(processInstanceId)//使用流程实例ID查询
                    .singleResult();
            if(null!=hpi){
                return true;
            }else {
                throw new ResultInfoException("流程实例ID不存在启动的流程实例");
            }

        }
    }
}
