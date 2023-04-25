package com.zfsoft.ha.front.controller;

import com.zfsoft.cases.manager.QlCaseApplayManager;
import com.zfsoft.cases.manager.QlCaseManager;
import com.zfsoft.cases.util.DateUtil;
import com.zfsoft.ha.constant.Constants;
import com.zfsoft.ha.constant.RedisConstants;
import com.zfsoft.ha.data.HaAppointment;
import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.HaWorkTurnRecord;
import com.zfsoft.ha.data.HaWorkUser;
import com.zfsoft.ha.data.requestData.HaCompanyHistroyRequestData;
import com.zfsoft.ha.data.requestData.HaStartTurnRequestVo;
import com.zfsoft.ha.data.requestData.HaWorkServiceRequestData;
import com.zfsoft.ha.data.responseData.*;
import com.zfsoft.ha.data.vo.*;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkQueueMapper;
import com.zfsoft.ha.dbaccess.data.vo.DbHaWorkQueueVo;
import com.zfsoft.ha.dbaccess.data.vo.DbHaWorkUserVo;
import com.zfsoft.ha.dbaccess.data.vo.HaWorkQueueResponseVo;
import com.zfsoft.ha.front.HaWorkUserService;
import com.zfsoft.ha.managers.*;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.bean.BeanUtils;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import com.zfsoft.superwindow.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.zfsoft.ha.constant.Constants.GROUP_DEPUTY_LEADER;
import static com.zfsoft.ha.constant.Constants.GROUP_LEADER;

/**
 * 帮代办人员相关接口
 *
 * @author kangax
 * @version 1.0
 * @date 2022/7/20 上午11:45
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HaWorkUserController implements HaWorkUserService {
    /**
     * redis
     */
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 办事队列manager
     */
    private final HaWorkQueueManager haWorkQueueManager;

    /**
     * 帮代办服务表
     */
    private final HaWorkServiceManager haWorkServiceManager;

    /**
     * 分组，manager
     */
    private final HaWorkGroupServiceManager haWorkGrouoServiceManager;

    /**
     * 办事转派记录manager
     */
    private final HaWorkTurnRecordManager haWorkTurnRecordManager;

    /**
     * 办事队列service层
     */

    private final QueueManager queueManager;

    /**
     * 预约信息service层
     */
    private final HaAppointmentManager haAppointmentManager;

    private final QlCaseManager qlCaseManager;
    /**
     *  登录接口数据层实现
     */
    private final HaWorkUserLoginManager haWorkUserLoginManager;

    @Resource
    private QlCaseApplayManager qlCaseApplayManager;

    @Resource
    private DbHaWorkQueueMapper dbHaWorkQueueMapper;

    /**
     * @description: 获取等待人数
     * @params： []
     * @return: ApiResultSet<QueueNumVo> 返回等待人数信息
     * @author: kangax
     * @date: 2022-07-27 23:30
     */
    @Override
    public ApiResultSet<QueueNumVo> getQueueNum() {
        //获取当前登录用户信息
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        log.info("进入获取等待人数Controller，当前操作用户： {}", currentHaLoginUserInfo.getName());
        QueueNumVo queueNumVo = haWorkQueueManager.getQueueNum(currentHaLoginUserInfo);
        return ApiResultSet.ok("查询成功", queueNumVo);
    }

    @Override
    public ApiResultSet<QueueAllNumVo> getQueueAllNum() {
        //获取当前登录用户信息
        log.info("进入获取当日大厅所有人数");
        QueueAllNumVo queueNumVo = haWorkQueueManager.getQueueAllNum();
        return ApiResultSet.ok("查询成功", queueNumVo);
    }


    /**
     * @param haWorkQueueVo 办事队列vo
     * @description: 查询当前帮代办人下办事队列list
     * @return: ApiResultSet<List < HaWorkQueueVo>> 返回办事队列list
     * @date: 2022/07/27 22:54 修改
     */
    @Override
    public ApiResultSet queryWorkQueueList(HaWorkQueueVo haWorkQueueVo) {
        log.info("进入查询当前帮代办人下办事队列Controller,haWorkQueueVo：{}", haWorkQueueVo);
        List<HaWorkQueueVo> list = haWorkQueueManager.queryWorkQueueListByHaWorkQueue(haWorkQueueVo);
        return ApiResultSet.ok("查询成功", list);
    }

    /**
     * @description: 获取全部的办事队列数据
     * @param haWorkQueueVo 办事队列请求参数
     *  @author: kangax
     * @date: 2022-08-17 13:58
     */
    @Override
    public ApiResultSet<PageResult> queryAllWorkQueueList(HaWorkQueueVo haWorkQueueVo, String beginTime, String endTime, Integer pageNum, Integer pageSize) throws Exception {
        log.info("进入获取全部办事队列的数据，参数haQueueRequestData:  {}", haWorkQueueVo);
        PageResult<HaWorkQueueVo> resultSet = haWorkQueueManager.queryAllWorkQueueListWithPage(haWorkQueueVo, beginTime, endTime, pageNum, pageSize);
        return ApiResultSet.ok("请求成功", resultSet);
    }

    /**
     * @description: 获取当前帮办人全部的办事队列数据
     * @param haWorkQueueVo 办事队列请求参数
     *  @author: kangax
     * @date: 2022-08-17 13:58
     */
    @Override
    public ApiResultSet<PageResult> queryAllWorkQueueListBylogin(HaWorkQueueVo haWorkQueueVo, String beginTime, String endTime, Integer pageNum, Integer pageSize) throws Exception {
        log.info("进入获取全部办事队列的数据，参数haQueueRequestData:  {}", haWorkQueueVo);
        //获取当前登录用户信息
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        //当前帮代办人员id作为查询条件
        haWorkQueueVo.setServiceWorkUserId(currentHaLoginUserInfo.getId());
        //分页参数
        PageResult<HaWorkQueueVo> resultSet = haWorkQueueManager.queryAllWorkQueueListWithPage(haWorkQueueVo, beginTime, endTime, pageNum, pageSize);
        return ApiResultSet.ok("请求成功", resultSet);
    }

    @Override
    public ApiResultSet<PageResult<HaWorkServiceVo>> getTempServiceList(HaWorkQueueVo haWorkQueueVo, String beginTime, String endTime, Integer pageNum, Integer pageSize) throws Exception {
        log.info("进入获取暂存数据，参数haQueueRequestData:  {}", haWorkQueueVo);
        PageResult<HaWorkServiceVo> resultSet = haWorkQueueManager.getTempServiceList(haWorkQueueVo, beginTime, endTime, pageNum, pageSize);
        return ApiResultSet.ok("请求成功", resultSet);
    }

    /**
     * @description: 根据身份证号码获取用户的帮办队列记录
     * @param haWorkQueueVo 办事队列请求参数
     * @author: zhaobf
     * @date: 2022-09-02 9:38
     */
    @Override
    public ApiResultSet<PageResult> queryQueueHistoryList(HaWorkQueueVo haWorkQueueVo, String beginTime, String endTime, Integer pageNum, Integer pageSize) throws Exception {
        log.info("进入获取全部办事队列的数据，haWorkQueueVo:  {}", haWorkQueueVo);
        PageResult<HaWorkQueueVo> resultSet = haWorkQueueManager.queryQueueHistoryListWithPage(haWorkQueueVo, beginTime, endTime, pageNum, pageSize);
        return ApiResultSet.ok("请求成功", resultSet);
    }

    /**
     * @description: 根据身份证号码获取用户的帮办服务service记录
     * @param haWorkQueueVo 办事队列请求参数
     * @author: zhaobf
     * @date: 2022-09-07 15:38
     */
    @Override
    public ApiResultSet<PageResult> queryServiceHistoryList(HaWorkQueueVo haWorkQueueVo, Integer pageNum, Integer pageSize) {
        log.info("进入获取全部办事队列的数据，haWorkQueueVo:  {}", haWorkQueueVo);
        PageResult<HaWorkServiceVo> resultSet = haWorkQueueManager.queryServiceHistoryListWithPage(haWorkQueueVo, pageNum, pageSize);
        return ApiResultSet.ok("请求成功", resultSet);
    }
    /**
     * @description: 根据企业统一社会行用代码或者身份证号码获取企业办件历史信息
     * @author: zhaobf
     * @date: 2022-09-07 15:38
     */
    @Override
    public ApiResultSet<PageResult> queryCompanyHistoryList(HaCompanyHistroyRequestData hacompReqData) {
        log.info("进入根据企业统一社会行用代码或者身份证号码获取企业办件历史信息，HaCompanyHistroyRequestData:  {}", hacompReqData);
        PageResult<HaWorkServiceVo> resultSet = haWorkQueueManager.queryCompanyHistoryList(hacompReqData);
        return ApiResultSet.ok("请求成功", resultSet);
    }

    /**
     * @return
     * @description: 获取分组列表
     * @author: kangax
     * @date: 2022-08-11 14:06
     */
    @Override
    public ApiResultSet<List<HaWorkUserGroupResponseData>> getGroupList() {
        log.info("进入获取分组列表Controller,获取分组和各组组长");
        List<HaWorkUserGroupResponseData> haWorkUserGroupResponseDataList = haWorkGrouoServiceManager.getGroupList(GROUP_LEADER);
        return ApiResultSet.ok("查询成功", haWorkUserGroupResponseDataList);
    }

    @Override
    public ApiResultSet<List<HaWorkUserGroupResponseData>> getGroupLeaderList() {
        log.info("进入获取分组列表Controller,获取分组和各组组长");
        List<HaWorkUserGroupResponseData> result = haWorkGrouoServiceManager.getGroupList(GROUP_LEADER);
        List<HaWorkUserGroupResponseData> result2 = haWorkGrouoServiceManager.getGroupList(GROUP_DEPUTY_LEADER);
        for (HaWorkUserGroupResponseData haResp : result) {
            HaWorkUserGroupResponseData ha2 = result2.stream().filter(e -> e.getGroupId().equals(haResp.getGroupId())).findFirst().orElse(null);
            if(ha2!=null){
                List<HaWorkUser> haWorkUsers = haResp.getHaWorkUsers();
                haWorkUsers.addAll(ha2.getHaWorkUsers());
                haResp.setHaWorkUsers(haWorkUsers);
            }
        }
        return ApiResultSet.ok("查询成功", result);
    }

    @Override
    public ApiResultSet<List<HaWorkUserGroupResponseData>> getGroupListAllUser() {
        log.info("进入获取分组列表Controller,获取所有分组和组下成员");
        List<HaWorkUserGroupResponseData> haWorkUserGroupResponseDataList = haWorkGrouoServiceManager.getGroupList("");
        return ApiResultSet.ok("查询成功", haWorkUserGroupResponseDataList);
    }

    /**
     * @param workUserId 工作人员编号
     * @description: 获取某个用户的办事队列
     * @return: HaWorkQueueVo 办事队列的详细信息
     * @author: kangax
     * @date: 2022-08-11 14:36
     */
    @Override
    public ApiResultSet<List<HaWorkQueueVo>> queryWorkQueueListByUser(Long workUserId) {
        log.info("进入查询某个用户的办事队列Controller,参数 workUserId： {}", workUserId);
        List<HaWorkQueueVo> list = haWorkQueueManager.queryWorkQueueListByUser(workUserId);
        return ApiResultSet.ok("请求成功", list);
    }

    /**
     * @description: 根据分组查询查看所有用户的服务信息
     * @params：[groupId, name]
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     * @author: kangax
     * @date: 2022-08-11 15:22
     */
    @Override
    public ApiResultSet getAllUserService(Long groupId, String name) {
        log.info("进入根据分组查询所有用户的服务信息Controller,参数，groupId: {}, name: {}", groupId, name);
        List<HaWorkQueueResponseVo> haWorkQueueResponseVoList = haWorkQueueManager.getAllUserService(groupId, name);
        return ApiResultSet.ok("查询成功", haWorkQueueResponseVoList);
    }

    @Override
    public ApiResultSet getAllUserServiceByName(Long groupId, String name) {
        log.info("进入根据分组查询所有用户的服务信息Controller,参数，groupId: {}, name: {}", groupId, name);
        List<HaWorkQueueResponseVo> haWorkQueueResponseVoList = haWorkQueueManager.getAllUserServiceByName(groupId, name);
        return ApiResultSet.ok("查询成功", haWorkQueueResponseVoList);
    }
    @Override
    public ApiResultSet getAllUserServiceByGroupSplit(Long groupId,Long groupSplitId ,String name) {
        log.info("进入根据分组查询所有用户的服务信息Controller,参数，groupId: {},groupSplitId:{} name: {}", groupId,groupSplitId, name);
        List<HaWorkQueueResponseVo> haWorkQueueResponseVoList = new ArrayList<>();
        if(groupSplitId==null){
             haWorkQueueResponseVoList = haWorkQueueManager.getAllUserService(groupId, name);
        }else{
            haWorkQueueResponseVoList = haWorkQueueManager.getAllUserServiceByGroupSplit(groupId,groupSplitId, name);
        }
        return ApiResultSet.ok("查询成功", haWorkQueueResponseVoList);
    }
    /**
     * @description: 帮代办人员接待下一位
     * @author: kangax
     * @date: 2022-07-27 19:53
     */
    @Override
    public ApiResultSet<NextServiceResponseData> nextService() {
        //当天开始时间
        Date beginADay = DateUtil.getBeginADay();
        //当天结束时间
        Date endADay = DateUtil.getEndADay();
        //获取当前登录用户
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        log.info("进入帮代办人员接待下一位Controller,当前操作用户:{}", currentHaLoginUserInfo.getName());
        try {
            //忙碌状态不能再次接待
            DbHaWorkUserVo helpWorkUserById = haWorkUserLoginManager.loginByAccounAndId(null,currentHaLoginUserInfo.getId());
            if("2".equals(helpWorkUserById.getStatus())){
                throw new ServiceException("当前帮代办人员忙碌，不能接待");
            }
            /* 判断下一位是视频用户的话查询当天还有没有正在服务的视频帮办，有的话则不允许接待 */
            //查询当前帮代办人员的待服务列表中，获取等待时间最长的一位进行服务
            DbHaWorkQueueVo dbHaWorkQueueVo = dbHaWorkQueueMapper.queryHaWorkQueueByServiceStatusAndWaitTime(currentHaLoginUserInfo.getId(), Constants.WAIT, beginADay, endADay);
            if (dbHaWorkQueueVo != null && "5".equals(dbHaWorkQueueVo.getTakeNumberType())) {
                // 查询当天的服务信息列表
                DbHaWorkQueueVo workQueueVo = new DbHaWorkQueueVo();
                //当前帮代办人员id作为查询条件
                workQueueVo.setServiceWorkUserId(currentHaLoginUserInfo.getId());
                //当天开始时间
                workQueueVo.setBeginADay(beginADay);
                //当天结束时间
                workQueueVo.setEndADay(endADay);
                //排队状态;1-扫码排队中，2-导服已分配
                workQueueVo.setQueueStatus("2");
                //服务状态;1-等待服务，2-服务中，3-服务完成
                workQueueVo.setServiceStatus("2");
                //取号类型：1-扫码取号，2-预约取号，3-普通取号, 4手机取号, 5-视频取号
                workQueueVo.setTakeNumberType("5");
                List<DbHaWorkQueueVo> dbHaWorkQueueVoList = dbHaWorkQueueMapper.queryHaWokQueueByHaQueue(workQueueVo);
                if (dbHaWorkQueueVoList != null && dbHaWorkQueueVoList.size() > 0) {
                    throw new ServiceException("正在接听其他视频，请完成后再接待视频帮办");
                }
            }
            NextServiceResponseData nextServiceResponseData = haWorkQueueManager.getNextService(currentHaLoginUserInfo);
            if (nextServiceResponseData != null) {
                log.info("帮代办人员接待下一位成功,当前操作用户：{}", currentHaLoginUserInfo.getName());
                return ApiResultSet.ok("帮代办人员接待下一位成功", nextServiceResponseData);
            } else {
                log.info("当前没有等待的办事人,当前操作用户：{}", currentHaLoginUserInfo.getName());
                return ApiResultSet.ok("当前没有等待的办事人");
            }
        } catch (ServiceException e) {
            log.error("帮办人员接待下一位异常！errorMessage:{}", e.getMessage());
            return new ApiResultSet(ApiResultSet.UNKNOWN_ERROR, "帮办人员接待下一位异常！" + e.getMessage());
        }
    }

    /**
     * @description: 帮代办服务人员服务转派
     * @params：[ workUserId 帮代办人员编号 , queueId 队列编号
     * ,turnMemo 转派原因]
     * @author: kangax
     * @date: 2022-07-27 19:21
     */
    @Override
    public ApiResultSet serviceTurn(@Valid HaStartTurnRequestVo haStartTurnRequestVo) {
        log.info("进入帮代办服务人员服务转派Controller,参数： haStartTurnRequestVo:{} ",haStartTurnRequestVo);
        //获取当前登录用户
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();

        try {
            Map<String, Object> resMap = haWorkQueueManager.serviceTurn(haStartTurnRequestVo);
            log.info("服务转派成功,当前操作用户：{}", currentHaLoginUserInfo.getName());
            return ApiResultSet.ok("服务转派成功", resMap);
        } catch (ServiceException e) {
            log.error("服务转派失败！errorMessage:{}", e.getMessage());
            return new ApiResultSet(ApiResultSet.UNKNOWN_ERROR, "服务转派出现异常, " + e.getMessage());
        }
    }

    /**
     * @description: 完成服务
     * @params： [ haWorkServiceRequestData 帮代办服务请求封装类]
     * @author: kangax
     * @date: 2022-07-27 18:43
     */
    @Override
    public ApiResultSet completeService(HaWorkServiceRequestData haWorkServiceRequestData) {
        log.info("进入帮代办人员完成服务Controller,参数haWorkServiceRequestData：{}", haWorkServiceRequestData);
        //获取当前登录用户
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        try {
            Long haWorkServiceId = haWorkServiceManager.completeService(haWorkServiceRequestData);
            Map<String,Object> map = new HashMap<>();
            map.put("haWorkServiceId",haWorkServiceId);
            log.info("完成服务成功,当前操作用户：{}", currentHaLoginUserInfo.getName());
            return ApiResultSet.ok("完成服务成功",map);
        } catch (ServiceException e) {
            log.error("完成服务失败！errorMessage:{}", e.getMessage());
            return new ApiResultSet(ApiResultSet.UNKNOWN_ERROR, "完成服务出现异常"+e.getMessage());
        }
    }

    /**
     * @description: 帮代办人员结束本次服务
     * @params：[ haWorkServiceRequestData 帮代办服务请求封装类]
     * @author: kangax
     * @date: 2022-07-27 18:07
     */
    @Override
    public ApiResultSet endService(HaWorkServiceRequestData haWorkServiceRequestData) {
        log.info("进入帮代办人员结束本次服务Controller,参数：{}", haWorkServiceRequestData);
        //获取当前登录用户
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        if (haWorkServiceRequestData == null) {
            return new ApiResultSet(ApiResultSet.PARAM_VALID_ERROR, "结束本次服务失败,参数异常！");
        }
        try {
            haWorkQueueManager.endService(haWorkServiceRequestData);
            log.info("结束本次服务成功,当前操作用户：{}", currentHaLoginUserInfo.getName());
            return ApiResultSet.ok("结束本次服务成功");
        } catch (ServiceException e) {
            log.error("结束本次服务失败：errorMessage:{}", e.getMessage());
            return new ApiResultSet(ApiResultSet.UNKNOWN_ERROR, "结束本次服务失败");
        }
    }

    /**
     * @description: 帮办人员帮助办事人叫号
     * @params：[queueId 办事队列编号, windowsNumber 排队号]
     * @author: kangax
     * @date: 修改时间：2022-07-27 17:22
     */
    @Override
    public ApiResultSet workUserHelpWindowCall(Long queueId, String agentTakeNumber) {
        log.info("进入帮办人员帮助办事人叫号Controller,queueId:{},windowsNumber:{}", queueId, agentTakeNumber);
        Map<String, Object> map = new HashMap<>(2);
        try {
            String res = haWorkQueueManager.workUserHelpWindowCall(queueId, agentTakeNumber);
            //保存排队号成功返回排队号
            map.put("windowsNumber", res);
        } catch (ServiceException e) {
            log.error("帮办人员帮助办事人叫号失败,errorMessage:{}", e.getMessage());
            return new ApiResultSet<>(ApiResultSet.UNKNOWN_ERROR, "帮办人员帮助办事人叫号失败");
        }
        return ApiResultSet.ok("请求成功", map);
    }

    /**
     * @description: 选择办事人进行接待服务
     * @params：[queueId 队列编号]
     * @author: kangax
     * @date: 修改时间：2022-07-27 17:19
     */
    @Override
    public ApiResultSet<NextServiceResponseData> chooseUserService(Long queueId) {
        log.info("进入选择办事人进行接待服务Controller参数，queueId:{}", queueId);
        if (queueId == null) {
            return new ApiResultSet<>(ApiResultSet.PARAM_VALID_ERROR, "办事队列编号不能为空");
        }
        try {
            NextServiceResponseData serviceResponseData = haWorkQueueManager.chooseUserService(queueId);
            if (serviceResponseData != null) {
                return ApiResultSet.ok("选择办事人服务成功", serviceResponseData);
            } else {
                return new ApiResultSet<>(ApiResultSet.DIRTY_DATA_TITLE, "选择办事人接待失败！查询当前队列信息为空");
            }
        } catch (ServiceException e) {
            log.error("选择办事人接待服务异，ErrorMessage:{}", e.getMessage());
            return new ApiResultSet<>(ApiResultSet.DIRTY_DATA_TITLE, e.getMessage());
        }
    }

    /**
     * @param turnRecordId 转派记录编号
     * @description: 转派服务是否接收
     * @return: HaTurnIsAcceptResponseData 返回转派记录详细信息
     * @author: kangax
     * @date: 2022-08-03 10:04
     */
    @Override
    public ApiResultSet<HaTurnIsAcceptResponseData> turnIsVerify(Long turnRecordId) {
        log.info("进入检测转派记录是否接收Controller，参数：turnRecordId:  {}", turnRecordId);
        if (turnRecordId == null) {
            log.error("请求参数turnRecordId为空！");
            return new ApiResultSet<>(ApiResultSet.PARAM_VALID_ERROR, "turnRecordId不能为空");
        }
        try {
            HaTurnIsAcceptResponseData haTurnIsAcceptResponseData = haWorkTurnRecordManager.turnIsAccept(turnRecordId);
            log.info("检测转派记录是否接收，请求成功，响应参数：{}", haTurnIsAcceptResponseData);
            return ApiResultSet.ok("请求成功", haTurnIsAcceptResponseData);
        } catch (ServiceException e) {
            log.error("查询服务是否接收出现异常，errorMessage: {}", e.getMessage());
            return new ApiResultSet<>(ApiResultSet.UNKNOWN_ERROR, "查询服务是否接收出现异常," + e.getMessage());
        }
    }

    @Override
    public ApiResultSet<PageResult> getServiceTurnByNoVerify(String name, Integer pageNum, Integer pageSize) {
        log.info("进入获取全部办事队列的数据，name:  {}", name);
        //获取当前登录用户信息
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        PageResult<HaWorkTurnRecordVo> resultSet = haWorkTurnRecordManager.queryWorkTrunRecordListWithPage(null,currentHaLoginUserInfo.getId(),name, pageNum, pageSize,Constants.TURN_STATUS_WAit_ACCEPT);
        return ApiResultSet.ok("请求成功", resultSet);
    }

    /**
     * @param turnRecordId 转派记录编号
     * @return HaGetWorkQueueByTurnIdResponseData 根据转派编号获取办事队列详细信息
     * @description: 根据转派编号获取办事队列信息
     * @author: kangax
     * @date: 2022-08-04 10:16
     */
    @Override
    public ApiResultSet<HaGetWorkQueueByTurnIdResponseData> getMessesByTurn(Long turnRecordId) {
        log.info("进入根据转派编号获取办事队列信息controller，参数 turnRecordId:  {}", turnRecordId);
        if (turnRecordId == null) {
            log.error("请求参数turnRecordId为空！");
            return new ApiResultSet<>(ApiResultSet.PARAM_VALID_ERROR, "turnRecordId不能为空");
        }
        try {
            HaGetWorkQueueByTurnIdResponseData haGetWorkQueueByTurnIdResponseData = haWorkTurnRecordManager.getMessesByTurn(turnRecordId);
            log.info("根据转派编号获取办事队列信息请求成功，响应参数：{}", haGetWorkQueueByTurnIdResponseData);
            return ApiResultSet.ok("请求成功", haGetWorkQueueByTurnIdResponseData);
        } catch (ServiceException e) {
            log.error("根据转派编号获取办事队列信息出现异常，errorMessage: {}", e.getMessage());
            return new ApiResultSet<>(ApiResultSet.UNKNOWN_ERROR, "根据转派编号获取办事队列信息出现异常," + e.getMessage());
        }

    }

    /**
     * @param turnRecordId 转派记录编号
     * @param turnStatus   转派状态，2-审核通过，3-审核不通过
     * @param rollbackMemo 退回原因，当退回时，必须填写退回原因
     * @description: 转派服务接收
     * @author: kangax
     * @date: 2022-08-04 13:41
     */
    @Override
    public ApiResultSet turnServiceVerify(Long turnRecordId, String turnStatus, String rollbackMemo, Long workUserId) {
        log.info("进入接收转派服务controller,参数，turnRecordId: {}, turnStatus: {}, rollbackMemo: {}, workUserId: {}", turnRecordId, turnStatus, rollbackMemo, workUserId);
        if (turnRecordId == null) {
            log.error("请求参数turnRecordId为空！");
            return new ApiResultSet<>(ApiResultSet.PARAM_VALID_ERROR, "turnRecordId不能为空");
        }
        if (StringUtils.isNotEmpty(turnStatus)) {
            if (Constants.TURN_STATUS_BACK.equals(turnStatus)) {
                //当服务接收状态为退回时，必须有退回原因
                if (StringUtils.isEmpty(rollbackMemo)) {
                    log.error("请求参数turnStatus为审核不通过时， 退回原因 rollbackMemo为空！");
                    return new ApiResultSet<>(ApiResultSet.PARAM_VALID_ERROR, "审核不通过时时退回原因不能为空");
                }
            }else if(Constants.TURN_STATUS_ACCEPT.equals(turnStatus)){
                if (StringUtils.isEmpty(workUserId+"")) {
                    log.error("请求参数turnStatus为审核通过时， 目标服务人员 workUserId 为空！");
                    return new ApiResultSet<>(ApiResultSet.PARAM_VALID_ERROR, "审核通过时目标服务人员能为空");
                }
            }else if(Constants.TURN_STATUS_WAit_ACCEPT.equals(turnStatus)){
                return new ApiResultSet<>(ApiResultSet.PARAM_VALID_ERROR, "审核状态没有改变");
            }
        } else {
            log.error("请求参数turnStatus为空！");
            return new ApiResultSet<>(ApiResultSet.PARAM_VALID_ERROR, "turnStatus不能为空");
        }
        try {
            Map<String, Object> resMap = new HashMap<>();
            String flag = haWorkQueueManager.serviceAccept(turnRecordId, turnStatus, rollbackMemo, workUserId);
            resMap.put("turnStatus", flag);
            return ApiResultSet.ok("请求成功", resMap);
        } catch (ServiceException e) {
            log.error("接收转派服务出现异常，errorMessage: {}", e.getMessage());
            return new ApiResultSet<>(ApiResultSet.UNKNOWN_ERROR, "接收转派服务异常," + e.getMessage());
        }
    }


/****************************************赵冰峰start*********************************************************/
    /**
     * @description: 是否有新的办事人到来等待服务
     * @author: kangax
     * @date: 2022-07-26 14:19
     */
    @Override
    public ApiResultSet<Map<String, Map<String, Object>>> isHaveNewMesses() {
        //获取当前登录用户信息
        HaLoginUserInfo loginUser = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        log.info("进入查询是否有新服务等待处理Controller,当前操作用户：{}", loginUser.getName());
        Map<String, Map<String, Object>> resMap = new HashMap<>();
        try {
            Date lastSelectTime = (Date) redisTemplate.opsForValue().get(RedisConstants.LAST_SELECT_QUEUE_TIME + loginUser.getId());
            Map<String, Object> messesService = new HashMap<>();
            Map<String, Object> turnService = new HashMap<>();
            //上次查询的时间不为空则当作参数进行查询，为空则获取当天零点作为参数查询
            int count = haWorkQueueManager.isHaveNewMesses(loginUser.getId(), lastSelectTime != null && DateUtil.isNow(lastSelectTime)? lastSelectTime : DateUtil.getBeginADay());
            int countTurn = haWorkTurnRecordManager.isHaveNewMesses(loginUser.getId(), DateUtil.getBeginADay());
            if (count > 0) {
                log.info("已查询到有新的办事人等待服务,当前帮办人员Name：{}", loginUser.getName());
                messesService.put("haveNewMesses", "1");
                //记录当前查询时间存入redis
//                redisTemplate.opsForValue().set(Constants.LAST_SELECT_QUEUE_TIME + loginUser.getId(), DateUtil.getDate(), 1, TimeUnit.DAYS);
            } else {
                redisTemplate.opsForValue().set(RedisConstants.LAST_SELECT_QUEUE_TIME + loginUser.getId(), DateUtil.getDate(), RedisConstants.LAST_SELECT_QUEUE_TTL, TimeUnit.DAYS);
                log.info("未查询到有新的办事人等待,当前帮办人员Name：{}", loginUser.getName());
                messesService.put("haveNewMesses", "2");
            }
            /**
             * 转派服务 20220803  zhaobf 修改
             */
            if (countTurn > 0) {
                log.info("已查询到有新的转派等待服务,当前帮办人员Name：{}", loginUser.getName());
                List<HaWorkTurnRecord> dbQlCaseApplays = haWorkTurnRecordManager.queryByVerifyWorkUserIdAndBeginTime(loginUser.getId(), DateUtil.getBeginADay());
                turnService.put("haveTurnService", "1");
                turnService.put("turnRecordId", dbQlCaseApplays.get(0).getId());
                //记录当前查询时间存入redis

            } else {
                log.info("未查询到有新的转派等待,当前帮办人员Name：{}", loginUser.getName());

                turnService.put("haveTurnService", "2");
                turnService.put("turnRecordId", "");
            }
            resMap.put("messesService", messesService);
            resMap.put("turnService", turnService);
        } catch (Exception e) {
            log.error("查询是否有新的办事人到来等待服务失败：{}", e.getMessage());
            return new ApiResultSet<>(ApiResultSet.UNKNOWN_ERROR, "查询异常");
        }


        return ApiResultSet.ok("请求成功", resMap);
    }

    /**
     * @return ApiResultSet 获取帮代办人员列表详情
     * @description: 获取用户组列表
     * @author: zhaobf
     * @Date: 2022/8/4 10:30
     **/
    @Override
    public ApiResultSet getUserGroupService() {
        log.info("获取用户组列表");
        List<HaWorkQueueResponseData2> dbHaWorkQueueVos = null;
        //获取当前登录用户信息
        HaLoginUserInfo loginUser = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        try {
            dbHaWorkQueueVos = queueManager.getWorkUserGroupList(loginUser.getId());
        } catch (Exception e) {
            log.error("获取用户组列表方法错误：", e);
            return new ApiResultSet(500, "获取用户组列表方法错误", e.getMessage());
        }
        return ApiResultSet.ok("获取用户组列表成功", dbHaWorkQueueVos);
    }

    /**
     * 查询办事队列
     *
     * @param name          姓名
     * @param cardNo        身份证号码
     * @param companyName   企业名称
     * @param queueStatus   排队状态;1-扫码排队中，2-导服已分配
     * @param serviceStatus 服务状态;1-等待服务，2-服务中，3-服务完成
     * @return List<HaWorkQueueVo> 获取查询办事队列列表详情
     * @Date: 2022/8/4 10:30
     */
    @Override
    public ApiResultSet queryWorkQueueList(String name, String cardNo, String companyName,
                                           String queueStatus, String serviceStatus) {
        log.info("查询办事队列，name:{},cardNo:{},companyName:{},queueStatus:{},serviceStatus:{}", name, cardNo, companyName, queueStatus, serviceStatus);
        try {
            List<HaWorkQueueVo> workQueueList = queueManager.queryWorkQueueGroupList(name, cardNo, companyName, queueStatus, serviceStatus);
            log.debug("workQueueList:{}", workQueueList);
            return ApiResultSet.ok("查询办事队列成功", workQueueList);
        } catch (Exception e) {
            log.error("查询办事队列方法错误：", e);
            return new ApiResultSet<>(500, "查询办事队列方法错误", e.getMessage());
        }
    }

    /**
     * 帮代办预约
     *
     * @param haAppointment
     * @return
     */
    @Override
    public ApiResultSet appointmentNext(HaAppointment haAppointment) {
        log.info("进入帮代办预约,haAppointment:{}", haAppointment);
        HaAppointment save = haAppointmentManager.save(haAppointment);
        return ApiResultSet.ok("请求成功", "");
    }

    /**
     * 统计服务数据
     * @return
     */
    @Override
    public ApiResultSet statServiceNum() {
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        Map<String, Integer> map = new HashMap<>();
        int tempServiceNum = haWorkServiceManager.getTempServiceNum(currentHaLoginUserInfo.getId());
        int turnCount = haWorkTurnRecordManager.getTurnCount(currentHaLoginUserInfo.getId());
        HaWorkQueueVo serviceNum = haWorkQueueManager.getServiceNum(currentHaLoginUserInfo.getId());
        map.put("serviceingNum", serviceNum == null ? 0 : serviceNum.getServiceingNum());
        map.put("tempServiceNum", tempServiceNum);
        map.put("finishServiceNum", serviceNum == null ? 0 : serviceNum.getFinishServiceNum());
        map.put("turnServiceNum", turnCount);
        return ApiResultSet.ok("请求成功", map);
    }

    /**
     * 获取队列服务数量
     * @param serviceStatus 服务类型
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ApiResultSet<PageResult> queueServiceList(String queueId,String serviceStatus,Integer pageNum, Integer pageSize) {
        log.info("进入获取全部办事队列的数据，queueId:{},serviceStatus:  {}",queueId, serviceStatus);
        PageResult<HaWorkQueueResponseData3> resultSet = haWorkQueueManager.queueServiceListWithPage(queueId,serviceStatus, pageNum, pageSize);
        return ApiResultSet.ok("请求成功", resultSet);
    }

    /**
     * 获取转派服务记录
     * @return
     */
    @Override
    public ApiResultSet<PageResult> getTurnService(HaWorkTurnRecordVo haWorkTurnRecordVo, String beginTime, String endTime,Integer pageNum, Integer pageSize) throws Exception {
        log.info("进入获取转派服务记录");
        //获取当前登录用户信息
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        haWorkTurnRecordVo.setHandleWorkUserId(currentHaLoginUserInfo.getId());
        PageResult<HaWorkTurnRecordVo> resultSet = haWorkTurnRecordManager.queryWorkTrunServiceListWithPage(haWorkTurnRecordVo,beginTime,endTime,pageNum,pageSize);
        return ApiResultSet.ok("请求成功", resultSet);
    }


    @Override
    public ApiResultSet<List<HaQlCaseAppayResponseData>> selectByName(String name) {
        List<HaQlCaseAppayResponseData> qlCaseApplay = qlCaseApplayManager.selectByName(name);
        ApiResultSet<List<HaQlCaseAppayResponseData>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(qlCaseApplay);
        return apiResultSet;
    }

    /**
     * 更新帮办服务
     * @return
     */
    @Override
    public ApiResultSet updateService(long haWorkServiceId, String serviceStatus) throws ServiceException{
        log.info("进入更新帮办服务");
        haWorkServiceManager.updateService(haWorkServiceId,serviceStatus);
        return ApiResultSet.ok("更新帮办服务成功");
    }

/****************************************赵冰峰end*********************************************************/


    @Override
    public ApiResultSet<List<HaWorkUserGroupResponseData>> getBureauGroupList() {
        log.info("进入获取分组列表Controller,获取委办局分组信息和委办局人员信息");
        List<HaWorkUserGroupResponseData> haWorkUserGroupResponseDataList = new ArrayList<>();
        try {
            haWorkUserGroupResponseDataList = haWorkGrouoServiceManager.getBureauGroupList();
        } catch (Exception e) {
            log.error("获取委办局分组信息和委办局人员信息接口错误：", e);
            return new ApiResultSet(ApiResultSet.UNKNOWN_ERROR, "获取委办局分组信息和委办局人员信息接口错误", e.getMessage());
        }
        return ApiResultSet.ok("查询成功", haWorkUserGroupResponseDataList);
    }

    @Override
    public ApiResultSet<List<HaWorkUser>> getStreetist() {
        log.info("进入获取分组列表Controller,获取街道信息和街道人员信息");
        List<HaWorkUser> result = new ArrayList<>();
        try {
            result = haWorkGrouoServiceManager.getStreetist();
        } catch (Exception e) {
            log.error("获取委办局分组信息和委办局人员信息接口错误：", e);
            return new ApiResultSet(ApiResultSet.UNKNOWN_ERROR, "获取街道信息和街道人员信息接口错误", e.getMessage());
        }
        return ApiResultSet.ok("查询成功", result);
    }

    @Override
    public ApiResultSet<List<HaWorkUserResponseData>> getCurrentGroupLeaderList() {
        log.info("进入获取分组列表Controller,获取当前用户组长副组长人员信息");
        List<HaWorkUserResponseData> haWorkUserResponseDataList = new ArrayList<>();
        try {
            haWorkUserResponseDataList = haWorkGrouoServiceManager.getCurrentGroupLeaderList();
        } catch (Exception e) {
            log.error("获取当前用户组长副组长人员信息接口错误：", e);
            return new ApiResultSet(ApiResultSet.UNKNOWN_ERROR, "获取当前用户组长副组长人员信息接口错误", e.getMessage());
        }
        return ApiResultSet.ok("查询成功", haWorkUserResponseDataList);
    }

}
