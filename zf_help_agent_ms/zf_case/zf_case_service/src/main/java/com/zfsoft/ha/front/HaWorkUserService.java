package com.zfsoft.ha.front;

import com.zfsoft.ha.data.HaAppointment;
import com.zfsoft.ha.data.HaWorkUser;
import com.zfsoft.ha.data.requestData.HaCompanyHistroyRequestData;
import com.zfsoft.ha.data.requestData.HaStartTurnRequestVo;
import com.zfsoft.ha.data.requestData.HaWorkServiceRequestData;
import com.zfsoft.ha.data.responseData.*;
import com.zfsoft.ha.data.vo.*;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 帮代办人员相关接口
 *
 * @author kangax
 * @version 1.0
 * @date 2022/7/20 上午11:39
 */
@RequestMapping("/ha/workUser")
public interface    HaWorkUserService {

    /**
     * @description: 获取当前帮代办人员等待人数
     * @return: ApiResultSet<QueueNumVo> 返回等待人数信息
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getQueueNum", method = {RequestMethod.GET})
    ApiResultSet<QueueNumVo> getQueueNum();

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getQueueAllNum", method = {RequestMethod.GET})
    ApiResultSet<QueueAllNumVo> getQueueAllNum();

    /**
     * @description: 查询当前帮代办人下的办事队列
     * @param haWorkQueueVo 办事队列vo
     * @return: ApiResultSet<List < HaWorkQueueVo>> 返回办事队列list
     * @author: kangax
     * @date: 2022-07-27 23:27
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryWorkQueueList", method = {RequestMethod.GET})
    ApiResultSet<List<HaWorkQueueVo>> queryWorkQueueList(HaWorkQueueVo haWorkQueueVo);

    /**
     * @description: 查询当前帮代办人员的所有的办事队列数据
     * @param haWorkQueueVo 办事队列请求参数
     * @author: kangax
     * @date: 2022-08-17 11:34
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryAllWorkQueueList", method = {RequestMethod.GET})
    ApiResultSet<PageResult> queryAllWorkQueueList(HaWorkQueueVo haWorkQueueVo,
                                                   @RequestParam(value = "beginTime", required = false) String beginTime,
                                                   @RequestParam(value = "endTime", required = false) String endTime,
                                                   @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize) throws Exception;

    /**
     * @description: 查询当前帮代办人员的所有的办事队列数据
     * @param haWorkQueueVo 办事队列请求参数
     * @author: zhaobf
     * @date: 2022-11-15 15:34
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryAllWorkQueueListBylogin", method = {RequestMethod.GET})
    ApiResultSet<PageResult> queryAllWorkQueueListBylogin(HaWorkQueueVo haWorkQueueVo,
                                                   @RequestParam(value = "beginTime", required = false) String beginTime,
                                                   @RequestParam(value = "endTime", required = false) String endTime,
                                                   @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize) throws Exception;
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getTempServiceList", method = {RequestMethod.GET})
    ApiResultSet<PageResult<HaWorkServiceVo>> getTempServiceList(HaWorkQueueVo haWorkQueueVo,
                                                                 @RequestParam(value = "beginTime", required = false) String beginTime,
                                                                 @RequestParam(value = "endTime", required = false) String endTime,
                                                                 @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                 @RequestParam(value = "pageSize", required = false) Integer pageSize) throws Exception;

    /**
     * @description: 根据身份证号码获取用户的帮办队列queue记录
     * @param haWorkQueueVo 办事队列请求参数
     * @author: zhaobf
     * @date: 2022-09-02 9:38
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryQueueHistoryList", method = {RequestMethod.GET})
    ApiResultSet<PageResult> queryQueueHistoryList(HaWorkQueueVo haWorkQueueVo,
                                                   @RequestParam(value = "beginTime", required = false) String beginTime,
                                                   @RequestParam(value = "endTime", required = false) String endTime,
                                                   @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize) throws Exception;
    /**
     * @description: 根据经办人信息查询经办人暂存服务办件
     * @param haWorkQueueVo 办事队列请求参数
     * @author: zhaobf
     * @date: 2022-09-07 15:38
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryHelpServiceHistoryList", method = {RequestMethod.GET})
    ApiResultSet<PageResult> queryServiceHistoryList(HaWorkQueueVo haWorkQueueVo,
                                                   @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * @description: 根据企业信息查询企业服务办件
     * @param hacompReqData 企业历史办件请求参数
     * @author: zhaobf
     * @date: 2022-11-16 15:38
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/queryCompanyHistoryList")
    ApiResultSet<PageResult> queryCompanyHistoryList(@RequestBody HaCompanyHistroyRequestData hacompReqData);

    /**
     * @description: 获取分组列表信息
     * @author: kangax
     * @date: 2022-08-11 14:02
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getGroupList", method = {RequestMethod.GET})
    ApiResultSet<List<HaWorkUserGroupResponseData>> getGroupList();

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getGroupLeaderList", method = {RequestMethod.GET})
    ApiResultSet<List<HaWorkUserGroupResponseData>> getGroupLeaderList();

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getGroupListAllUser", method = {RequestMethod.GET})
    ApiResultSet<List<HaWorkUserGroupResponseData>> getGroupListAllUser();

    /**
     * @description: 获取某个用户的办事队列
     * @param workUserId 工作人员编号
     * @return: HaWorkQueueVo 办事队列详细信息
     * @author: kangax
     * @date: 2022-08-11 14:34
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryWorkQueueListByUser", method = {RequestMethod.GET})
    ApiResultSet<List<HaWorkQueueVo>> queryWorkQueueListByUser(@RequestParam Long workUserId);

    /**
     * @description: 根据分组查询查看所有用户的服务信息
     * @param groupId 分组编号
     * @param name 工作人员姓名
     * @author: kangax
     * @date: 2022-08-11 15:19
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getAllUserService", method = {RequestMethod.GET})
    ApiResultSet getAllUserService(Long groupId, String name);

    /**
     * @description: 根据分组查询查看所有用户的服务信息
     * @param groupId 分组编号
     * @param name 工作人员姓名
     * @author: kangax
     * @date: 2022-08-11 15:19
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getAllUserServiceByGroupSplit", method = {RequestMethod.GET})
    ApiResultSet getAllUserServiceByGroupSplit(Long groupId,Long groupSplitId, String name);
    /**
     * @description: 根据分组和姓名 （模糊查询） 查看所有用户的服务信息
     * @param groupId 分组编号
     * @param name 工作人员姓名
     * @author: zhaobf
     * @date: 2022-12-12 14:45
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getAllUserServiceByName", method = {RequestMethod.GET})
    ApiResultSet getAllUserServiceByName(Long groupId, String name);

    /**
     * @description: 当前帮代办人员接待下一位
     * @return: piResultSet<NextServiceResponseData> 返回当前帮代办人员接待下一位信息
     * @author: kangax
     * @date: 2022-07-27 19:35
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/nextService", method = {RequestMethod.GET})
    ApiResultSet<NextServiceResponseData> nextService();

    /**
     * @description: 帮代办人员服务转派
     * @author: kangax
     * @date: 2022-07-27 19:08
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/serviceTurn", method = {RequestMethod.POST})
    ApiResultSet serviceTurn(HaStartTurnRequestVo haStartTurnRequestVo);

    /**
     * @description: 完成服务
     * @param haWorkServiceRequestData 帮代办服务请求参数
     * @author: kangax
     * @date: 2022-07-27 18:36
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/completeService", method = {RequestMethod.POST})
    ApiResultSet completeService(HaWorkServiceRequestData haWorkServiceRequestData);

    /**
     * @description: 结束本次服务 参数比结束服务增加两个，nextServiceAdvise adviseMemo
     * @param haWorkServiceRequestData 帮代办服务请求参数
     * @author: kangax
     * @date: 2022-07-27 18:01
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/endService", method = {RequestMethod.POST})
    ApiResultSet endService(HaWorkServiceRequestData haWorkServiceRequestData);

    /**
     * @description: 帮代办人员帮助办事人叫号
     * @param queueId       队列编号
     * @param agentTakeNumber 排队号
     * @author: kangax
     * @date: 修改时间：2022-07-27 17:17
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/windowCall", method = {RequestMethod.POST})
    ApiResultSet workUserHelpWindowCall(@RequestParam(value = "queueId", required = true) Long queueId,
                                        @RequestParam(value = "agentTakeNumber", required = false) String agentTakeNumber);

    /**
     * @description: 选择办事人进行接待服务
     * @param queueId 队列编号
     * @author: kangax
     * @date: 修改时间 2022-07-27 16:15
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/service", method = {RequestMethod.GET})
    ApiResultSet<NextServiceResponseData> chooseUserService(@RequestParam(value = "queueId", required = true) Long queueId);

    /**
     * @description: 转派服务是否审核
     * @param turnRecordId 转派记录编号
     * @return: HaTurnIsAcceptResponseData 转派记录详细信息
     * @author: kangax
     * @date: 2022-08-03 10:02
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/turnIsVerify", method = {RequestMethod.POST})
    ApiResultSet<HaTurnIsAcceptResponseData> turnIsVerify(@RequestParam(value = "turnRecordId", required = true) Long turnRecordId);


    /**
     * @description: 转派服务是否审核
     * @param name 转派记录编号
     * @return: HaTurnIsAcceptResponseData 转派记录详细信息
     * @author: kangax
     * @date: 2022-08-03 10:02
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/getServiceTurnByNoVerify")
    ApiResultSet<PageResult> getServiceTurnByNoVerify(String name,Integer pageNum, Integer pageSize);

    /**
     * @description: 根据转派编号获取办事队列信息
     * @param turnRecordId 转派记录编号
     * @return HaGetWorkQueueByTurnIdResponseData 返回办事队列详细信息
     * @author: kangax
     * @date: 2022-08-04 10:10
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getMessesByTurn", method = {RequestMethod.GET})
    ApiResultSet<HaGetWorkQueueByTurnIdResponseData> getMessesByTurn(@RequestParam(value = "turnRecordId", required = true) Long turnRecordId);

    /**
     * @description: 转派服务审核
     * @param turnRecordId 转派记录编号
     * @param turnStatus   转派状态，2-接收，3-退回
     * @param rollbackMemo 退回原因，当退回时，必须填写退回原因
     * @author: kangax
     * @date: 2022-08-04 13:37
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/turnServiceVerify", method = {RequestMethod.POST})
    ApiResultSet turnServiceVerify(@RequestParam(value = "turnRecordId", required = true) Long turnRecordId,
                                   @RequestParam(value = "turnStatus", required = true) String turnStatus,
                                   @RequestParam(value = "rollbackMemo", required = true) String rollbackMemo,
                                   @RequestParam(value = "workUserId", required = true) Long workUserId);

    /****************************************赵冰峰start*********************************************************/
    /**
     * @description: 是否有新的办事人员等待服务
     * @return: ApiResultSet<HaveNewMessesResponseData> 返回是否有办事人员等待服务
     * @author: zhaobf
     * @date: 修改时间 2022-08-03 15:50
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/isHaveNewService", method = {RequestMethod.GET})
    ApiResultSet<Map<String, Map<String, Object>>> isHaveNewMesses();

    /**
     * ***查看用户组的服务信息
     *
     * @author: zhaobf
     * @Date: 2022/8/4 10:35
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getUserGroupService", method = {RequestMethod.GET})
    ApiResultSet getUserGroupService();

    /**
     * @param name          姓名
     * @param cardNo        身份证号码
     * @param companyName   企业名称
     * @param queueStatus   排队状态;1-扫码排队中，2-导服已分配
     * @param serviceStatus 服务状态;1-等待服务，2-服务中，3-服务完成
     * @description: **查询用户组的办事队列
     * @author: zhaobf
     * @Date: 2022/8/4 10:35
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryWorkQueueGroup", method = {RequestMethod.POST})
    ApiResultSet queryWorkQueueList(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "cardNo", required = false) String cardNo,
            @RequestParam(value = "companyName", required = false) String companyName,
            @RequestParam(value = "queueStatus", required = false) String queueStatus,
            @RequestParam(value = "serviceStatus", required = false) String serviceStatus);


    /**
     * 帮代办预约
     * @param haAppointment 预约信息
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/appointmentNext", method = {RequestMethod.POST})
    ApiResultSet appointmentNext(@RequestBody HaAppointment haAppointment);

    /**
     * 统计服务数据
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/statServiceNum", method = {RequestMethod.GET})
    ApiResultSet statServiceNum();

    /**
     * 获取队列服务数量
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queueServiceList", method = {RequestMethod.GET})
    ApiResultSet<PageResult> queueServiceList(String queueId,String serviceStatus,Integer pageNum, Integer pageSize);

    /**
     * 获取转派服务记录
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getTurnService", method = {RequestMethod.GET})
    ApiResultSet<PageResult> getTurnService(HaWorkTurnRecordVo haWorkTurnRecordVo,
                                            @RequestParam(value = "beginTime", required = false) String beginTime,
                                            @RequestParam(value = "endTime", required = false) String endTime,
                                            Integer pageNum,
                                            Integer pageSize) throws Exception;


    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryCompanyName",method = {RequestMethod.GET})
    ApiResultSet<List<HaQlCaseAppayResponseData>> selectByName( String name);

    /****************************************赵冰峰end*********************************************************/

    /**
     * 更新帮办服务
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/updateService", method = {RequestMethod.POST})
    ApiResultSet updateService(@RequestParam(value = "haWorkServiceId")  long haWorkServiceId, String serviceStatus);

    /**
     * 获取委办局分组信息和委办局人员信息
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getBureauGroupList", method = {RequestMethod.GET})
    ApiResultSet<List<HaWorkUserGroupResponseData>> getBureauGroupList();

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getStreetist", method = {RequestMethod.GET})
    ApiResultSet<List<HaWorkUser>> getStreetist();

    /**
     * 获取当前用户组长副组长人员信息
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getCurrentGroupLeaderList", method = {RequestMethod.GET})
    ApiResultSet<List<HaWorkUserResponseData>> getCurrentGroupLeaderList();
}
