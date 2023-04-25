package com.zfsoft.microservice.workflow.service;

import cn.hutool.json.JSONObject;
import com.zfsoft.microservice.workflow.data.WorkflowBussInfo;
import com.zfsoft.microservice.workflow.data.dto.CompleteTaskVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.workflow.TaskInfo;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: 工作流的对外有关用户任务接口服务
 * @author: wuxx
 * @Date: 2021/1/13 9:05
 **/
@RequestMapping("/security/workflow/userTask")
public interface UserTaskService {

    /**
     * @description:  根据名称、区划、组织机构查询流程的信息列表
     * @param workflowName 流程名称
     * @param districtOid 区划oid
     * @param organOid 组织机构oid
     * @param typeCode 所属类型Code
     * @author: wuxx
     * @Date: 2022/2/2 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/infoList",method = {RequestMethod.GET})
    ApiResultSet<List<WorkflowBussInfo>> queryWorkflowBussInfoWithParams(@RequestParam(value="workflowName",required=false)String workflowName,
                                                                         @RequestParam(value="districtOid",required=false)String districtOid,
                                                                         @RequestParam(value="organOid",required=false)String organOid,
                                                                         @RequestParam(value="typeCode",required=false)String typeCode);
    /**
     * 根据流程类型操作业务Id获取流程类型操作信息
     * @param infoOid 流程业务Id
     * @return
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/getWorkflowBussInfoByInfoOid/{infoOid}")
    ApiResultSet<WorkflowBussInfo> getWorkflowBussInfoByInfoOid(@PathVariable("infoOid") String infoOid);

    /**
     * 根据流程编码获取流程类型操作信息
     * @param infoCode 流程code
     * @return
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/getWorkflowBussInfoByInfoCode")
    ApiResultSet<WorkflowBussInfo> getWorkflowBussInfoByInfoCode(@RequestParam("infoCode") String infoCode);

    /**
     * 启动流程
     * @author: zhongxx
     * @Date: 2021-02-05 16:14
     * @param infoOid 流程信息逻辑唯一标识
     * @param businessKey 流程业务标识
     * @param variableMapJson 参数
     * @return
     */
    @ProcessFeignCalledResult
    @PostMapping( value = "/startProcessInstance")
    ApiResultSet<Map<String, Object>> startProcessInstance(@RequestParam(value = "infoOid",required = false) String infoOid,
                                                           @RequestParam(value = "userId",required = false) String userId,
                                                           @RequestParam(value = "businessKey",required = false) String businessKey,
                                                           @RequestParam(value = "variableMapJson", required = false) String variableMapJson);


    /**
     * 启动流程
     * @author: zhongxx
     * @Date: 2021-02-05 16:14
     * @param infoOid 流程信息逻辑唯一标识
     * @param businessKey 流程业务标识
     * @param variableJSONObject 参数
     * @return
     */
    @ProcessFeignCalledResult
    @PostMapping( value = "/startProcessInstanceByVariable")
    ApiResultSet<Map<String, Object>> startProcessInstanceByVariable(@RequestParam(value = "infoOid",required = false) String infoOid,
                                                           @RequestParam(value = "userId",required = false) String userId,
                                                           @RequestParam(value = "businessKey",required = false) String businessKey,
                                                           @RequestParam(value = "variableJSONObject", required = false) JSONObject variableJSONObject);


    /**
     * 查询任务列表
     *
     * @param appOid      应用唯一标识
     * @param typeCode     流程类型编码
     * @param infoOid     流程信息唯一标识
     * @param businessKey 业务信息管理标识
     * @param userOid     用户唯一标识
     * @return List<TaskInfo>
     * @author zhongxx
     * @date 2021-02-07 14:17
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/queryToDoTasksList")
    ApiResultSet<List<TaskInfo>> queryToDoTasksList(@RequestParam(value = "appOid",required = false) String appOid,
                                            @RequestParam(value = "typeCode",required = false) String typeCode,
                                            @RequestParam(value = "infoOid",required = false) String infoOid,
                                            @RequestParam(value = "businessKey",required = false) String businessKey,
                                            @RequestParam(value = "userOid",required = false) String userOid);

    /**
     * @description: 根据实例oid查询待办任务
     * @param processInstanceId 实例oid
     * @author: wuxx
     * @Date: 2021/6/23 9:36
     **/
    @ProcessFeignCalledResult
    @GetMapping(value = "/queryToDoTasksListByprocessInstanceId")
    ApiResultSet<List<TaskInfo>> queryToDoTasksListByprocessInstanceId(@RequestParam(value = "processInstanceId") String processInstanceId);

    /**
     * 根据流程编码查询用户任务列表
     *
     * @param infoCode     流程信息编码
     * @param businessKey 业务信息管理标识
     * @param userOid     用户唯一标识
     * @return List<TaskInfo>
     * @author wuxx
     * @date 2021-02-24 14:17
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/queryToDoTasksListByCode")
    ApiResultSet<List<TaskInfo>> queryToDoTasksListByCode(@RequestParam(value = "infoCode") String infoCode,
                                                          @RequestParam(value = "userOid") String userOid,
                                                          @RequestParam(required = false) String businessKey );

    /**
     * 分页查询任务列表
     *
     * @param appOid      应用唯一标识
     * @param typeCode     流程类型编码
     * @param infoOid     流程信息唯一标识
     * @param businessKey 业务信息管理标识
     * @param userOid     用户唯一标识
     * @param pageNumber  页码
     * @param pageSize    每页数量
     * @return PageResult<TaskInfo>
     * @author zhongxx
     * @date 2021-02-07 14:18
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/queryToDoTasksListPage")
    ApiResultSet<PageResult<TaskInfo>> queryToDoTasksListPage(@RequestParam(value = "appOid",required = false) String appOid,
                                                  @RequestParam(required = false) String typeCode,
                                                  @RequestParam(required = false) String infoOid,
                                                  @RequestParam(required = false) String businessKey,
                                                  @RequestParam(value = "userOid") String userOid,
                                                  @RequestParam(value = "pageNumber",required = false) Integer pageNumber,
                                                  @RequestParam(value = "pageSize",required = false) Integer pageSize);

    /**
     * 根据流程编码分页查询任务列表
     *
     * @param infoCode     流程信息编码
     * @param userOid     用户唯一标识
     * @param pageNumber  页码
     * @param pageSize    每页数量
     * @return PageResult<TaskInfo>
     * @author zhongxx
     * @date 2021-02-07 14:18
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/queryToDoTasksListPageByCode")
    ApiResultSet<PageResult<TaskInfo>> queryToDoTasksListPageByCode(@RequestParam(value = "infoCode") String infoCode,
                                                              @RequestParam(value = "userOid") String userOid,
                                                              @RequestParam(required = false) String businessKey,
                                                              @RequestParam(value = "pageNumber",required = false) Integer pageNumber,
                                                              @RequestParam(value = "pageSize",required = false) Integer pageSize);

    /**
     * 获取任务信息
     * @author zhongxx
     * @date 2021-02-08 11:06
     * @param taskId 任务唯一标识
     * @return TaskInfo
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getTaskInfo",method = {RequestMethod.GET})
    ApiResultSet<TaskInfo>  getTaskInfo(@RequestParam(value = "taskId") String taskId);

    /**
     * @description: 查看当前登录人的待办列表,返回JSON 集合
     * @author: wuxx
     * @Date: 2021/1/13 10:48
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getUserToTasks",method = {RequestMethod.GET})
    ApiResultSet<String> getUserToTasks(@RequestParam(value="pageNum",required = false)Integer pageNum,
                                         @RequestParam(value="pageSize",required = false)Integer pageSize);

    /**
     * @description: 完成任务并执行下一个环节
     * @param taskId 任务id
     * @param userId 执行用户id
     * @param variableMapJson 参数,作为后续判断条件(非必填)
     * @author: wuxx
     * @Date: 2021/1/13 14:30
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/completeTask",method = {RequestMethod.POST})
    ApiResultSet<Boolean> completeTask(@RequestParam(value = "taskId") String taskId,@RequestParam(value = "userId",required = false) String userId,@RequestParam(value = "variableMapJson",required = false) String variableMapJson);

    /**
     * @description: 传递参数完成任务并执行下一个环节
     * @author: wuxx
     * @Date: 2021/1/13 14:30
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/completeTaskByCompleteTaskVo",method = {RequestMethod.POST})
    ApiResultSet<Boolean> completeTaskByCompleteTaskVo(@RequestBody CompleteTaskVo completeTaskVo);

    /**
     * @description: 跳过某一个环节的流程
     * @param taskId 任务id
     * @author: wuxx
     * @Date: 2021/1/15 09:30
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/skipTask",method = {RequestMethod.POST})
    ApiResultSet<Boolean> skipTask(@RequestParam(value = "taskId") String taskId,@RequestParam(value = "userId",required = false) String userId);

    /**
     * @description:  暂停流程实例
     * @param processInstanceId 流程实列id
     * @author: wuxx
     * @Date: 2021/1/13 14:30
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/suspendProcessInstance",method = {RequestMethod.POST})
    ApiResultSet<Boolean> suspendProcessInstance(@RequestParam(value = "processInstanceId") String processInstanceId);

    /**
     * @description:  激活流程实例
     * @param processInstanceId 流程实列id
     * @author: wuxx
     * @Date: 2021/1/13 14:30
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/resumeProcessInstance",method = {RequestMethod.POST})
    ApiResultSet<Boolean> resumeProcessInstance(@RequestParam(value = "processInstanceId") String processInstanceId);


    /**
     * @description:  删除流程实例
     * @param processInstanceId 流程实列id
     * @param deleteReason 删除原因
     * @author: wuxx
     * @Date: 2021/1/13 14:30
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/deleteProcessInstance",method = {RequestMethod.POST})
    ApiResultSet<Boolean> deleteProcessInstance(@RequestParam(value = "processInstanceId") String processInstanceId,
                                                @RequestParam(value = "userId",required = false) String userId,
                                                @RequestParam(value = "deleteReason") String deleteReason);

    /**
     * @description: 终止流程实例
     * @param processInstanceId 流程实列id
     * @author: wuxx
     * @Date: 2021/2/19 14:30
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/endProcessInstance",method = {RequestMethod.POST})
    ApiResultSet<Boolean> endProcessInstance(@RequestParam(value = "processInstanceId") String processInstanceId,@RequestParam(value = "userId") String userId);

    /**
     * @description: 审核不通过
     * @param processInstanceId 流程实列id
     * @author: wuxx
     * @Date: 2021/2/19 14:30
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/noPassProcessInstance",method = {RequestMethod.POST})
    ApiResultSet<Boolean> noPassProcessInstance(@RequestParam(value = "processInstanceId") String processInstanceId,@RequestParam(value = "userId") String userId
            ,@RequestParam(value = "taskId") String taskId);

    /**
     * @description: 根据用户主键查询已办任务,返回JSON 集合
     *  @param userOid 用户业务主键
     * @author: wuxx
     * @Date: 2021/1/14 10:48
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryHistoryTaskByUserOid",method = {RequestMethod.GET})
    ApiResultSet<String> queryHistoryTaskByUserOid(@RequestParam("userOid") String userOid,@RequestParam(value="pageNum",required = false)Integer pageNum,
                                       @RequestParam(value="pageSize",required = false)Integer pageSize);

    /**
     * @description: 根据流程实例ID查询历史
     * @param processInstanceId 实例ID
     * @author: wuxx
     * @Date: 2021/1/14 16:00
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getHistoricTaskInstanceByPiId",method = {RequestMethod.GET})
    ApiResultSet<String> getHistoricTaskInstanceByPiId(@RequestParam(value = "processInstanceId") String processInstanceId);

    /**
     * @description: 获取下一个环节的节点
     * @param taskId 任务id
     * @param flag true查询后续所有环节，false 查询后续一个环节（默认false）
     * @author: wuxx
     * @Date: 2021/1/15 10:03
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryNextTaskByTaskId",method = {RequestMethod.GET})
    ApiResultSet<List<Map<String, Object>>> queryNextTaskByTaskId(@RequestParam(value = "taskId") String taskId, @RequestParam(value = "flag",required = false) Boolean flag);

    /**
     * @description: 根据任务id获取下一个环节的节点前的线条件
     * @param taskId 任务id
     * @author: wuxx
     * @Date: 2021/2/1 16:03
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryNextTaskFlowConditionByTaskId",method = {RequestMethod.GET})
    ApiResultSet<List<Map<String, Object>>> queryNextTaskFlowConditionByTaskId(@RequestParam(value = "taskId") String taskId);

    /**
     * @description: 根据流程实例ID查询当前流程是否结束(true 结束，false 未结束)
     * @param processInstanceId 流程实例ID
     * @author: wuxx
     * @Date: 2021/1/15 16:47
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/checkProcessOver",method = {RequestMethod.GET})
    ApiResultSet<Boolean> checkProcessOver(@RequestParam(value="processInstanceId")String processInstanceId);

    /**
     * @description:  驳回到流程图绘制的环节
     * @param taskId 任务id
     * @param targetNodeId 环节activiti的id
     * @author: wuxx
     * @Date: 2021/2/8 10:13
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/taskReject",method = {RequestMethod.POST})
    ApiResultSet<Boolean> taskReject(@RequestParam(value="taskId") String taskId,@RequestParam(value="targetNodeId") String targetNodeId,
                                     @RequestParam(value = "userId") String userId);

       /**
     * @description:  退回到流程图绘制的环节
     * @param taskId 任务id
     * @param targetNodeId 环节activiti的id
     * @author: wuxx
     * @Date: 2021/2/8 10:13
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/taskFallback",method = {RequestMethod.POST})
    ApiResultSet<Boolean> taskFallback(@RequestParam(value="taskId") String taskId,@RequestParam(value="targetNodeId") String targetNodeId,@RequestParam(value = "userId") String userId);

    /**
     * @description: 更新应办结时间by办结时间
     * @param taskId 任务id
     * @param dueDate 应办结时间
     * @author: wuxx
     * @Date: 2021/2/9 13:22
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/updateDueDateByTaskId",method = {RequestMethod.POST})
    ApiResultSet<Boolean> updateDueDateByTaskId(@RequestParam(value="taskId") String taskId,@RequestParam(value="dueDate") Date dueDate);

    /**
     * @description: 更新应办结时间by天数
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
    @ProcessFeignCalledResult
    @RequestMapping( value = "/updateDueDateByTime",method = {RequestMethod.POST})
    ApiResultSet<Boolean> updateDueDateByTime(@RequestParam(value="taskId") String taskId,
                                                @RequestParam(value="timeLimitDay",required = false) Integer timeLimitDay,
                                                @RequestParam(value="timeUnit",required = false)String timeUnit,
                                                @RequestParam(value="timeLimitHour",required = false)Integer timeLimitHour);



    /**
     * @description:  （根据任务id）切换审核人员-针对审核人员离职或其他无法正常审核的情况
     * @param taskId 任务id
     * @param oldUserOid 旧的审核人员
     * @param newUserOid 新的审核人员
     * @author: wuxx
     * @Date: 2022/3/21 10:19
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/changeTaskAuditUserOid",method = {RequestMethod.POST})
    ApiResultSet<Boolean> changeTaskAuditUserOid(@RequestParam(value="taskId",required = false) String taskId,
                                             @RequestParam(value="oldUserOid") String oldUserOid,
                                             @RequestParam(value = "newUserOid") String newUserOid);


    /**
     * @description: 获取所有已办理的环节点
     * @param taskId 任务id
     * @param processInstanceId 实例id
     * @author: wuxx
     * @Date: 2022/3/24 10:02
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getAllFlowNodeActivitiId",method = {RequestMethod.GET})
    ApiResultSet<List<String>> getAllFlowNodeActivitiId(@RequestParam(value="taskId",required = false) String taskId,
                                                       @RequestParam(value = "processInstanceId",required = false) String processInstanceId);

}
