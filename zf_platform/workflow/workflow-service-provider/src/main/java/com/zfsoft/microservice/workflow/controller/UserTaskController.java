package com.zfsoft.microservice.workflow.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.microservice.workflow.data.WorkflowBussInfo;
import com.zfsoft.microservice.workflow.data.dto.CompleteTaskVo;
import com.zfsoft.microservice.workflow.feign.SysUserFeignService;
import com.zfsoft.microservice.workflow.manager.*;
import com.zfsoft.microservice.workflow.service.UserTaskService;
import com.zfsoft.microservice.workflow.util.AuthenticationUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.common.workflow.TaskInfo;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @ClassName UserTaskController
 * @Description 工作流用户任务类接口管理的实现类
 * @Author wuxx
 * @Date 2021-01-14 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class UserTaskController implements UserTaskService {

    @Autowired
    private ProcessRuntimeManager processRuntimeManager;

    @Autowired
    private TaskRuntimeManager taskRuntimeManager ;

    @Autowired
    private HistoryTaskManager historyTaskManager;

    @Autowired
    private WorkflowUtilsManager workflowUtilsManager;

    @Autowired
    private WorkflowBussInfoManager workflowBussInfoManager;

    @Autowired
    private SysUserFeignService sysUserFeignService;

    @Override
    public ApiResultSet<List<WorkflowBussInfo>> queryWorkflowBussInfoWithParams(String workflowName, String districtOid, String organOid, String typeCode) {
        List<WorkflowBussInfo> workflowBussInfoList = workflowBussInfoManager.queryWorkflowBussInfoWithParams(workflowName, districtOid, organOid, typeCode);
        return new ApiResultSet<>(workflowBussInfoList);
    }

    @Override
    public ApiResultSet<WorkflowBussInfo> getWorkflowBussInfoByInfoOid(String infoOid) {
        WorkflowBussInfo workflowBussInfo = workflowBussInfoManager.getWorkflowBussInfoByInfoOid(infoOid);
        return new ApiResultSet<>(workflowBussInfo);
    }

    @Override
    public ApiResultSet<WorkflowBussInfo> getWorkflowBussInfoByInfoCode(String infoCode) {
        WorkflowBussInfo workflowBussInfo = workflowBussInfoManager.getWorkflowBussInfoByInfoCode(infoCode);
        return new ApiResultSet<>(null == workflowBussInfo ?new WorkflowBussInfo():workflowBussInfo);
    }

    /**
     * @description:  用户启动流程,返回JSON流程实列
     * @param infoOid 流程信息唯一标识
     * @param businessKey 业务表主键
     * @param variableMapJson 参数mapjson
     * @author: wuxx
     * @Date: 2021/1/13 14:26
     **/
    @Override
    public ApiResultSet startProcessInstance(String infoOid,String userId, String businessKey, String variableMapJson){
        if(StrUtil.isBlank(infoOid)){
            throw new ResultInfoException("流程业务主键infoOid不能为空！");
        }
        if(StrUtil.isBlank(businessKey)){
            throw new ResultInfoException("唯一标识businessKey不能为空！");
        }
        if(StrUtil.isBlank(userId)){
            throw new ResultInfoException("操作用户主键userId不能为空！");
        }
        Map<String, Object> variableMap = null;
        if(StrUtil.isNotEmpty(variableMapJson)){
            try {
                variableMap = JSON.parseObject(variableMapJson, HashMap.class);
            }catch (Exception e){

            }
        }
        //设置当前登录人（针对feign接口调用方式）
        if(StrUtil.isNotEmpty(userId)){
            AuthenticationUtil.setAuthentication(userId);
        }
        return new ApiResultSet<>(processRuntimeManager.startProcessInstance(infoOid,businessKey,variableMap));
    }

    /**
     * @description:  用户启动流程,返回JSON流程实列
     * @param infoOid 流程信息唯一标识
     * @param businessKey 业务表主键
     * @param variableJSONObject 参数mapjson
     * @author: wuxx
     * @Date: 2022/3/14 14:26
     **/
    @Override
    public ApiResultSet startProcessInstanceByVariable(String infoOid,String userId, String businessKey, JSONObject variableJSONObject){
        if(StrUtil.isBlank(infoOid)){
            throw new ResultInfoException("流程业务主键infoOid不能为空！");
        }
        if(StrUtil.isBlank(businessKey)){
            throw new ResultInfoException("唯一标识businessKey不能为空！");
        }
        if(StrUtil.isBlank(userId)){
            throw new ResultInfoException("操作用户主键userId不能为空！");
        }
        Map<String, Object> variableMap = null;
        if(null!=variableJSONObject){
            try {
                variableMap = JSON.parseObject(JSONUtil.toJsonStr(variableJSONObject), HashMap.class);
            }catch (Exception e){

            }
        }
        //设置当前登录人（针对feign接口调用方式）
        if(StrUtil.isNotEmpty(userId)){
            AuthenticationUtil.setAuthentication(userId);
        }
        return new ApiResultSet<>(processRuntimeManager.startProcessInstance(infoOid,businessKey,variableMap));
    }

    @Override
    public ApiResultSet<List<TaskInfo>> queryToDoTasksList(String appOid, String typeCode, String infoOid, String businessKey, String userOid) {
        if(StrUtil.isBlank(appOid)){
            throw new ResultInfoException("应用主键appOid不能为空！");
        }
        if(StrUtil.isBlank(userOid)){
            throw new ResultInfoException("操作用户主键userOid不能为空！");
        }
        List<TaskInfo> taskInfos = taskRuntimeManager.queryTasks(appOid, typeCode, infoOid, businessKey, userOid);
        return new ApiResultSet<>(taskInfos);
    }

    @Override
    public ApiResultSet<List<TaskInfo>> queryToDoTasksListByprocessInstanceId(String processInstanceId) {
        if(StrUtil.isBlank(processInstanceId)){
            throw new ResultInfoException("流程实例id不能为空！");
        }
        List<TaskInfo> taskInfos = taskRuntimeManager.queryToDoTasksListByprocessInstanceId(processInstanceId);
        return new ApiResultSet<>(taskInfos);
    }

    @Override
    public ApiResultSet<List<TaskInfo>> queryToDoTasksListByCode(String infoCode, String userOid, String businessKey) {
        if(StrUtil.isBlank(infoCode)){
            throw new ResultInfoException("参数appOid不能为空！");
        }
        if(StrUtil.isBlank(userOid)){
            throw new ResultInfoException("用户主键userOid不能为空！");
        }
        WorkflowBussInfo workflowBussInfo = workflowBussInfoManager.getWorkflowBussInfoByInfoCode(infoCode);
        String infoOid =null;
        if(null!=workflowBussInfo){
            infoOid = workflowBussInfo.getInfoOid();
        }else {
            return new ApiResultSet<>(new ArrayList<>());
        }
        List<TaskInfo> taskInfos = taskRuntimeManager.queryTasks(null, null, infoOid, businessKey, userOid);
        return new ApiResultSet<>(taskInfos);
    }

    @Override
    public ApiResultSet<PageResult<TaskInfo>> queryToDoTasksListPage(String appOid, String typeCode, String infoOid, String businessKey, String userOid, Integer pageNumber, Integer pageSize) {
        if(StrUtil.isBlank(appOid)){
            throw new ResultInfoException("应用主键appOid不能为空！");
        }
        if(StrUtil.isBlank(userOid)){
            throw new ResultInfoException("用户主键userOid不能为空！");
        }
        if(null == pageNumber){
            pageNumber = 1;
        }
        if(null == pageSize){
            pageSize = 10;
        }
        PageResult<TaskInfo> pageResult = taskRuntimeManager.queryTasks(appOid, typeCode, infoOid, businessKey, userOid, pageNumber, pageSize);
        return new ApiResultSet<>(pageResult);
    }

    @Override
    public ApiResultSet<PageResult<TaskInfo>> queryToDoTasksListPageByCode(String infoCode, String userOid, String businessKey, Integer pageNumber, Integer pageSize) {
        if(StrUtil.isBlank(infoCode)){
            throw new ResultInfoException("参数infoCode不能为空！");
        }
        if(StrUtil.isBlank(userOid)){
            throw new ResultInfoException("用户主键userOid不能为空！");
        }
        if(null == pageNumber){
            pageNumber= 1;
        }
        if(null == pageSize){
            pageSize= 10;
        }
        WorkflowBussInfo workflowBussInfo = workflowBussInfoManager.getWorkflowBussInfoByInfoCode(infoCode);
        String infoOid =null;
        if(null!=workflowBussInfo){
            infoOid = workflowBussInfo.getInfoOid();
        }else {
            return new ApiResultSet<>(new PageResult<>());
        }
        PageResult<TaskInfo> pageResult = taskRuntimeManager.queryTasks(null, null, infoOid, businessKey, userOid, pageNumber, pageSize);
        return new ApiResultSet<>(pageResult);
    }

    @Override
    public ApiResultSet<TaskInfo> getTaskInfo(String taskId) {
        if(StrUtil.isBlank(taskId)){
            throw new ResultInfoException("用户任务taskId不能为空！");
        }
        TaskInfo taskInfo = taskRuntimeManager.getTaskInfo(taskId);
        return new ApiResultSet<>(taskInfo);
    }


    /**
     * @description: 查看我的待办列表,返回JSON 集合
     * @author: wuxx
     * @Date: 2021/1/13 10:48
     **/
    @Override
    public ApiResultSet<String> getUserToTasks(Integer pageNum, Integer pageSize) {
        return new ApiResultSet<>(taskRuntimeManager.getUserToTasks(pageNum,pageSize));
    }

    /**
     * @description: 完成任务并执行下一个环节
     * @param taskId 任务id
     * @param userId 执行用户id
     * @param variableMapJson 参数,作为后续判断条件(非必填)
     * @author: wuxx
     * @Date: 2021/1/13 14:30
     **/
    @Override
    public ApiResultSet<Boolean> completeTask(String taskId, String userId, String variableMapJson) {
        if(StrUtil.isBlank(taskId)){
            throw new ResultInfoException("用户任务taskId不能为空！");
        }
        if(StrUtil.isBlank(userId)){
            throw new ResultInfoException("用户主键userId不能为空！");
        }
        //variableMapJson 此方法参数作废  使用completeTaskByCompleteTaskVo 传递参数

        //设置当前登录人（针对feign接口调用方式）
        if(StrUtil.isNotEmpty(userId)){
            AuthenticationUtil.setAuthentication(userId);
        }
        return new ApiResultSet<>(taskRuntimeManager.completeTask(taskId,null));
    }

    /**
     * @description: 传递参数完成任务并执行下一个环节
     * @author: wuxx
     * @Date: 2021/1/13 14:30
     **/
    @Override
    public ApiResultSet<Boolean> completeTaskByCompleteTaskVo(CompleteTaskVo completeTaskVo) {
        if(null == completeTaskVo){
            throw new ResultInfoException("传递参数不能为空！");
        }
        if(StrUtil.isBlank(completeTaskVo.getTaskId())){
            throw new ResultInfoException("用户任务taskId不能为空！");
        }
        if(StrUtil.isBlank(completeTaskVo.getUserId())){
            throw new ResultInfoException("用户主键userId不能为空！");
        }
        Map<String, Object> variableMap = completeTaskVo.getVariableMap();
        System.out.printf(variableMap.toString());
        //设置当前登录人（针对feign接口调用方式）
        if(StrUtil.isNotEmpty(completeTaskVo.getUserId())){
            AuthenticationUtil.setAuthentication(completeTaskVo.getUserId());
        }
        return new ApiResultSet<>(taskRuntimeManager.completeTask(completeTaskVo.getTaskId(),variableMap));
    }

    /**
     * @description: 跳过某一个环节的流程kByTaskId
     * @param taskId 任务id
     * @author: wuxx
     * @Date: 2021/1/15 09:30
     **/
    @Override
    public ApiResultSet<Boolean> skipTask(String taskId, String userId) {
        if(StrUtil.isBlank(taskId)){
            throw new ResultInfoException("用户任务taskId不能为空！");
        }
        if(StrUtil.isBlank(userId)){
            throw new ResultInfoException("用户主键userId不能为空！");
        }

        boolean completeTask = taskRuntimeManager.skipTask(taskId,userId);
        return new ApiResultSet<>(completeTask);
    }


    /**
     * @description:  暂停流程实例
     * @param processInstanceId 流程实列id
     * @author: wuxx
     * @Date: 2021/1/13 14:30
     **/
    @Override
    public ApiResultSet<Boolean> suspendProcessInstance(String processInstanceId) {
        if(StrUtil.isBlank(processInstanceId)){
            throw new ResultInfoException("流程实例processInstanceId不能为空！");
        }
        return new ApiResultSet<>(processRuntimeManager.suspendProcessInstance(processInstanceId));
    }

    /**
     * @description:  激活流程实例
     * @param processInstanceId 流程实列id
     * @author: wuxx
     * @Date: 2021/1/13 14:30
     **/
    @Override
    public ApiResultSet<Boolean> resumeProcessInstance(String processInstanceId) {
        if(StrUtil.isBlank(processInstanceId)){
            throw new ResultInfoException("流程实例processInstanceId不能为空！");
        }
        return new ApiResultSet<>(processRuntimeManager.resumeProcessInstance(processInstanceId));
    }

    /**
     * @description:  删除流程实例
     * @param processInstanceId 流程实列id
     * @param deleteReason 删除原因
     * @author: wuxx
     * @Date: 2021/1/13 14:30
     **/
    @Override
    public ApiResultSet<Boolean> deleteProcessInstance(String processInstanceId,String userId, String deleteReason) {
        if(StrUtil.isBlank(processInstanceId)){
            throw new ResultInfoException("流程实例processInstanceId不能为空！");
        }
        if(StrUtil.isBlank(userId)){
            throw new ResultInfoException("用户主键userId不能为空！");
        }
        if(StrUtil.isBlank(deleteReason)){
            throw new ResultInfoException("删除原因不能为空！");
        }
        deleteReason = "删除~"+deleteReason +"~" + userId + "~" + this.getLoginUserName(userId);
        return new ApiResultSet<>(processRuntimeManager.deleteProcessInstance(processInstanceId,deleteReason));
    }

    /**
     * @description: 终止流程实例
     * @param processInstanceId 流程实列id
     * @author: wuxx
     * @Date: 2021/2/19 14:30
     **/
    @Override
    public ApiResultSet<Boolean> endProcessInstance(String processInstanceId,String userId) {
        if(StrUtil.isBlank(processInstanceId)){
            throw new ResultInfoException("流程实例processInstanceId不能为空！");
        }
        if(StrUtil.isBlank(userId)){
            throw new ResultInfoException("用户主键userId不能为空！");
        }
        String deleteReason= "中止";
        deleteReason = deleteReason +"~" + userId + "~" + this.getLoginUserName(userId);
        return new ApiResultSet<>(processRuntimeManager.deleteProcessInstance(processInstanceId,deleteReason));
    }

    /**
     * @description: 审核不通过
     * @param processInstanceId 流程实列id
     * @author: wuxx
     * @Date: 2021/2/19 14:30
     **/
    @Override
    public ApiResultSet<Boolean> noPassProcessInstance(String processInstanceId,String userId,String taskId) {
        if(StrUtil.isBlank(processInstanceId)){
            throw new ResultInfoException("参数processInstanceId不能为空！");
        }
        if(StrUtil.isBlank(userId)){
            throw new ResultInfoException("参数userId不能为空！");
        }
        if(StrUtil.isBlank(taskId)){
            throw new ResultInfoException("参数taskId不能为空！");
        }
        String deleteReason= "审核不通过";
        deleteReason = deleteReason +"~" + userId + "~" + this.getLoginUserName(userId);
        return new ApiResultSet<>(taskRuntimeManager.noPassProcessInstance(processInstanceId,deleteReason,taskId));
    }

    /**
     * @description: 根据用户oid获取名称
     * @param userId 用户oid
     * @author: wuxx
     * @Date: 2021/2/25 16:08
     **/
    public String getLoginUserName(String userId){
        //设置当前登录人（针对feign接口调用方式）
        if(StrUtil.isNotEmpty(userId)){
            AuthenticationUtil.setAuthentication(userId);
        }else {
            userId = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
        }
        if(StrUtil.isNotEmpty(userId)){
            SysUser user = sysUserFeignService.getSysUserByUserOid(userId).getData();
            if(StrUtil.isNotBlank(user.getName())){
                return user.getName();
            }else{
                throw new ResultInfoException("用户主键userId不存在！");
            }
        }
        return null;
    }
    /**
     * @description: 查看所有的待办任务,返回JSON 集合
     * @author: wuxx
     * @Date: 2021/1/14 10:48
     **/
    //@Override
    @RequestMapping( value = "/queryAllTasks",method = {RequestMethod.GET})
    public ApiResultSet<String> queryAllTasks(Integer pageNum, Integer pageSize) {
        if(null == pageNum){
            pageNum = 1;
        }
        if(null == pageSize){
            pageSize = 10;
        }
        return new ApiResultSet<>(taskRuntimeManager.queryAllTasks(pageNum,pageSize));
    }
    /**
     * @description: 根据用户主键查询已办任务,返回JSON 集合
     *  @param userOid 用户业务主键
     * @author: wuxx
     * @Date: 2021/1/14 10:48
     **/
    @Override
    public ApiResultSet<String> queryHistoryTaskByUserOid(String userOid, Integer pageNum, Integer pageSize) {
        return new ApiResultSet<>(historyTaskManager.queryHistoryTaskByUserOid(userOid,pageNum,pageSize));
    }

    /**
     * @description: 根据流程实例ID查询历史
     * @param processInstanceId 实例ID
     * @author: wuxx
     * @Date: 2021/1/14 16:00
     **/
    @Override
    public ApiResultSet<String> getHistoricTaskInstanceByPiId(String processInstanceId) {
        if(StrUtil.isBlank(processInstanceId)){
            throw new ResultInfoException("流程实例processInstanceId不能为空！");
        }
        return new ApiResultSet<>(historyTaskManager.getHistoricTaskInstanceByPiId(processInstanceId));
    }
    /**
     * @description: 获取下一个环节的节点
     * @param taskId 任务id
     * @param flag true查询后续所有环节，false 查询后续一个环节（默认false）
     * @author: wuxx
     * @Date: 2021/1/15 10:03
     **/
    @Override
    public ApiResultSet<List<Map<String, Object>>> queryNextTaskByTaskId(String taskId,Boolean flag) {
        if(StrUtil.isBlank(taskId)){
            throw new ResultInfoException("用户任务taskId不能为空！");
        }
        if(null == flag){
            flag = false;
        }
        return new ApiResultSet<>(workflowUtilsManager.queryNextTaskByTaskId(taskId,flag));
    }

    @Override
    public ApiResultSet<List<Map<String, Object>>> queryNextTaskFlowConditionByTaskId(String taskId) {
        if(StrUtil.isBlank(taskId)){
            throw new ResultInfoException("用户任务taskId不能为空！");
        }
        return new ApiResultSet<>(workflowUtilsManager.queryNextTaskFlowConditionByTaskId(taskId));
    }

    /**
     * @description: 根据流程实例ID查询当前流程是否结束(true 结束，false 未结束)
     * @param processInstanceId 流程实例ID
     * @author: wuxx
     * @Date: 2021/1/15 16:47
     **/
    @Override
    public ApiResultSet<Boolean> checkProcessOver(String processInstanceId) {
        if(StrUtil.isBlank(processInstanceId)){
            throw new ResultInfoException("流程实例processInstanceId不能为空！");
        }
        return new ApiResultSet<>(historyTaskManager.checkProcessOver(processInstanceId));
    }

    /**
     * @description:  驳回到流程图绘制的环节
     * @param taskId 任务id
     * @param targetNodeId 环节activiti的id
     * @author: wuxx
     * @Date: 2021/2/8 10:13
     **/
    @Override
    public ApiResultSet<Boolean> taskReject(String taskId,String targetNodeId,String userId) {
        if(StrUtil.isBlank(taskId)){
            throw new ResultInfoException("参数taskId不能为空！");
        }
        if(StrUtil.isBlank(targetNodeId)){
            throw new ResultInfoException("参数targetNodeId不能为空！");
        }
        if(StrUtil.isBlank(userId)){
            throw new ResultInfoException("参数userId不能为空！");
        }
        String message = taskRuntimeManager.rejectTask(taskId, targetNodeId, null,userId);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        if(StrUtil.isNotEmpty(message)){
            apiResultSet.setMessage(message);
            apiResultSet.setCode(ApiResultSet.DIRTY_DATA_TITLE);
            return apiResultSet;
        }
        apiResultSet.setData(true);
        return apiResultSet;
    }

    /**
     * @description:  退回到流程图绘制的环节
     * @param taskId 任务id
     * @param targetNodeId 环节activiti的id
     * @author: wuxx
     * @Date: 2021/2/8 10:13
     **/
    @Override
    public ApiResultSet<Boolean> taskFallback(String taskId,String targetNodeId,String userId) {
        if(StrUtil.isBlank(taskId)){
            throw new ResultInfoException("参数taskId不能为空！");
        }
        if(StrUtil.isBlank(targetNodeId)){
            throw new ResultInfoException("参数targetNodeId不能为空！");
        }
        if(StrUtil.isBlank(userId)){
            throw new ResultInfoException("参数userId不能为空！");
        }
        String message = taskRuntimeManager.fallbackTask(taskId, targetNodeId, null,userId);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        if(StrUtil.isNotEmpty(message)){
            apiResultSet.setMessage(message);
            apiResultSet.setCode(ApiResultSet.DIRTY_DATA_TITLE);
            return apiResultSet;
        }
        apiResultSet.setData(true);
        return apiResultSet;
    }

    /**
     * @description: 更新应办结时间
     * @param taskId 任务id
     * @param dueDate 应办结时间
     * @author: wuxx
     * @Date: 2021/2/9 13:22
     **/
    @Override
    public ApiResultSet<Boolean> updateDueDateByTaskId(String taskId, Date dueDate) {
        boolean flag = taskRuntimeManager.updateDueDateByTaskId(taskId, dueDate);
        return new ApiResultSet<>(flag);
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
    @Override
    public ApiResultSet<Boolean> updateDueDateByTime(String taskId, Integer timeLimitDay, String timeUnit, Integer timeLimitHour) {
        boolean flag = taskRuntimeManager.updateDueDateByTime(taskId,timeLimitDay,timeUnit,timeLimitHour);
        return new ApiResultSet<>(flag);
    }

    /**
     * @description:  （根据任务id）切换审核人员-针对审核人员离职或其他无法正常审核的情况
     * @param taskId 任务id
     * @param oldUserOid 旧的审核人员
     * @param newUserOid 新的审核人员
     * @author: wuxx
     * @Date: 2022/3/21 10:19
     **/
    @Override
    public ApiResultSet<Boolean> changeTaskAuditUserOid(String taskId, String oldUserOid, String newUserOid) {
        if(StrUtil.isBlank(oldUserOid)){
            throw new ResultInfoException("参数oldUserOid不能为空！");
        }
        if(StrUtil.isBlank(newUserOid)){
            throw new ResultInfoException("参数newUserOid不能为空！");
        }
        boolean flag = taskRuntimeManager.changeTaskAuditUserOid(taskId,oldUserOid,newUserOid);
        return new ApiResultSet<>(flag);
    }

    @Override
    public ApiResultSet<List<String>> getAllFlowNodeActivitiId(String taskId, String processInstanceId) {
        if(StrUtil.isBlank(taskId) && StrUtil.isBlank(processInstanceId)){
            throw new ResultInfoException("参数taskId和参数processInstanceId不能同时为空！");
        }
        List<String> allFlowNodeActivitiIdMap = taskRuntimeManager.getAllFlowNodeActivitiId(taskId, processInstanceId);
        return new ApiResultSet<>(allFlowNodeActivitiIdMap);
    }
}
