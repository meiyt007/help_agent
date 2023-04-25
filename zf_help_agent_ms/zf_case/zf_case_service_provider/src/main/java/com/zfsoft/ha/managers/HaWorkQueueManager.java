package com.zfsoft.ha.managers;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.util.DateUtil;
import com.zfsoft.cases.util.DateUtils;
import com.zfsoft.cases.util.SendHPSmsUtil;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.ha.constant.Constants;
import com.zfsoft.ha.constant.RedisConstants;
import com.zfsoft.ha.data.*;
import com.zfsoft.ha.data.TripartiteVo.NumberOfServiceVo;
import com.zfsoft.ha.data.TripartiteVo.QuickAndRoundNumVo;
import com.zfsoft.ha.data.requestData.*;
import com.zfsoft.ha.data.responseData.HaScanHelpInfoResponseData;
import com.zfsoft.ha.data.responseData.HaTakeNumHelpResponseData;
import com.zfsoft.ha.data.responseData.HaWorkQueueResponseData3;
import com.zfsoft.ha.data.responseData.NextServiceResponseData;
import com.zfsoft.ha.data.vo.*;
import com.zfsoft.ha.dbaccess.dao.*;
import com.zfsoft.ha.dbaccess.data.*;
import com.zfsoft.ha.dbaccess.data.example.DbHaWorkQueueExample;
import com.zfsoft.ha.dbaccess.data.vo.*;
import com.zfsoft.ha.util.*;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.bean.BeanUtils;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.zfsoft.ha.constant.Constants.*;
import static com.zfsoft.ha.constant.RedisConstants.TURN_TIMEOUT;

/**
 * 帮代办人员及办事队列,实现层
 *
 * @author kangax
 * @version 1.0
 * @date 2022/7/20 上午11:57
 */
@Service
@Slf4j
public class HaWorkQueueManager {
    /**
     * 办事队列mapper
     */
    @Resource
    private DbHaWorkQueueMapper dbHaWorkQueueMapper;

    /**
     * 帮代办人员mapper
     */
    @Resource
    private DbHaWorkUserMapper dbThaWorkUserMapper;

    /**
     * 办事转派记录mapper
     */
    @Resource
    private DbHaWorkTurnRecordMapper dbHaWorkTurnRecordMapper;

    /**
     * 消息mapper
     */
    @Resource
    private DbHaMessageMapper dbHaMessageMapper;

    /**
     * 帮代办服务表mapper
     */
    @Resource
    private DbHaWorkServiceMapper dbHaWorkServiceMapper;

    /**
     *  登录接口数据层实现
     */

    @Resource
    private HaWorkUserLoginManager haWorkUserLoginManager;
    /**
     * 用户表实现
     */
    @Resource
    private HaWorkUserServiceManager userServiceManager;

    /**
     * 排班
     */
    @Resource
    private HaWorkUserScheduleManager haWorkUserScheduleManager;

    @Resource
    private HaAppointmentManager haAppointmentManager;


    /**
     * 队列接口实现
     */
    @Resource
    private QueueManager queueManager;

    /**
     * redis
     */
    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private HaPerformanceRegulartimeRecordManager haPerformanceRegulartimeRecordManager;



    /**
     * 视频咨询service层
     */
    @Resource
    private HaVideoConsultationManager haVideoConsultationManager;

    /**视频咨询房间**/
    @Resource
    private DbHaVideoRoomMapper dbHaVideoRoomMapper;

    @Resource
    private DbHaVideoAccessMapper dbHaVideoAccessMapper;

    /**
     * 根据身份证查询当前办事人的排队情况
     * selectByIdNo
     * @param IdNo 身份证号码
     * @date 2022/11/2 13:31
     */
    public List<HaWorkQueue> selectByIdNo(String IdNo) throws ServiceException {
        DbHaWorkQueueExample example = new DbHaWorkQueueExample();
        DbHaWorkQueueExample.Criteria criteria = example.createCriteria();
        criteria.andDistributeTimeBetween(DateUtil.getTodayStartTime(),DateUtil.getTodayEndTime());
        criteria.andServiceStatusEqualTo("1");
        criteria.andCardNoEqualTo(IdNo);
        example.setOrderByClause("CREATE_DATE desc");
        List<DbHaWorkQueue> dbHaWorkQueues = dbHaWorkQueueMapper.selectByExample(example);
        List<HaWorkQueue> haQuestionList = dbHaWorkQueues.stream().map(dbHaWorkQueue1 -> {
            HaWorkQueue haWorkQueue1 = new HaWorkQueue();
            BeanUtils.copyProperties(dbHaWorkQueue1, haWorkQueue1);
            return haWorkQueue1;
        }).collect(Collectors.toList());
        return haQuestionList;
    }

    /**
     * @description: 获取办事队列中：等待中的人数、服务中的人数、今日服务人数
     * @params： [haLoginUserInfo 当前登录用户信息]
     * @return: QueueNumVo返回当前等待人数详细信息
     * @author: kangax
     * @date: 2022-07-27 23:31
     */
    public QueueNumVo getQueueNum(HaLoginUserInfo haLoginUserInfo) {
        QueueNumVo queueNumVo = new QueueNumVo();
        //等待中的人数
        int waitNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatus(haLoginUserInfo.getId(), Constants.WAIT, DateUtil.getBeginADay(), DateUtil.getEndADay());
        //服务中的人数
        int serviceIngNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatus(haLoginUserInfo.getId(), Constants.SERVICE_ING, DateUtil.getBeginADay(), DateUtil.getEndADay());
        //今日服务人数
        int todayServiceNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatusAndEndTime(haLoginUserInfo.getId(), Constants.END_SERVICE, DateUtil.getBeginADay(), DateUtil.getEndADay());
        //获取所在组的id
        Long groupId = haLoginUserInfo.getGroupId();
        //根据组的ID查询当前组的所有人
        List<DbHaWorkUser> dbHaWorkUserList = dbThaWorkUserMapper.selectByGroupId(groupId, Constants.DELETE_STATUS_NO,null);
        //获取用户IDlist
        List<Long> workUserIdList = dbHaWorkUserList.stream().map(DbHaWorkUser::getId).collect(Collectors.toList());
        //组内等待中的人数
        int waitIngGroupNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatusAndWorkUserIds(workUserIdList, Constants.WAIT, DateUtil.getBeginADay(), DateUtil.getEndADay());
        //组内服务中的人数
        int inServiceGroupNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatusAndWorkUserIds(workUserIdList, Constants.SERVICE_ING, DateUtil.getBeginADay(), DateUtil.getEndADay());
        //组内今日已服务人数
        int todayServiceGroupNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatusAndEndTimeAndWorkUserIds(workUserIdList, Constants.END_SERVICE, DateUtil.getBeginADay(), DateUtil.getEndADay());
        queueNumVo.setWaitIngNum(waitNum);
        queueNumVo.setInServiceNum(serviceIngNum);
        queueNumVo.setTodayServiceNum(todayServiceNum);
        queueNumVo.setWaitIngGroupNum(waitIngGroupNum);
        queueNumVo.setInServiceGroupNum(inServiceGroupNum);
        queueNumVo.setTodayServiceGroupNum(todayServiceGroupNum);
        return queueNumVo;
    }
    public NumberOfServiceVo getTodayAnaToyearNum() {
        NumberOfServiceVo numberOfServiceVo = new NumberOfServiceVo();
        List<Long> kj = dbThaWorkUserMapper.selectByHaType("1", DELETE_STATUS_NO).stream().map(DbHaWorkUser::getId).collect(Collectors.toList());;
        //快捷今日已服务人数
        int todayServiceKJAllNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatusAndEndTimeAndWorkUserIds(kj, Constants.END_SERVICE, DateUtil.getBeginADay(), DateUtil.getEndADay());
        //快捷今年已服务人数
        int toYearServiceKJAllNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatusAndEndTimeAndWorkUserIds(kj, Constants.END_SERVICE, DateUtil.getBeginAYear(), DateUtil.getEndADay());

        List<Long> yz = dbThaWorkUserMapper.selectByHaType("2", DELETE_STATUS_NO).stream().map(DbHaWorkUser::getId).collect(Collectors.toList());;
        //圆桌今日已服务人数
        int todayServiceYZAllNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatusAndEndTimeAndWorkUserIds(yz, Constants.END_SERVICE, DateUtil.getBeginADay(), DateUtil.getEndADay());
        //圆桌今年已服务人数
        int toYearServiceYZAllNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatusAndEndTimeAndWorkUserIds(yz, Constants.END_SERVICE, DateUtil.getBeginAYear(), DateUtil.getEndADay());
        numberOfServiceVo.setTodayServiceKJAllNum(todayServiceKJAllNum);
        numberOfServiceVo.setToYearServiceKJAllNum(toYearServiceKJAllNum);
        numberOfServiceVo.setTodayServiceYZAllNum(todayServiceYZAllNum);
        numberOfServiceVo.setToYearServiceYZAllNum(toYearServiceYZAllNum);
        return numberOfServiceVo;
    }
    public QuickAndRoundNumVo getQuickAndRoundNum() {
        QuickAndRoundNumVo queueAllNumVo = new QuickAndRoundNumVo();
        List<Long> kj = dbThaWorkUserMapper.selectByHaType("1", DELETE_STATUS_NO).stream().map(DbHaWorkUser::getId).collect(Collectors.toList());;
        //快捷等待中的人数
        int waitIngKJAllNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatusAndWorkUserIds(kj, Constants.WAIT, DateUtil.getBeginADay(), DateUtil.getEndADay());
        List<Long> yz = dbThaWorkUserMapper.selectByHaType("2", DELETE_STATUS_NO).stream().map(DbHaWorkUser::getId).collect(Collectors.toList());;
        //圆桌等待中的人数
        int waitIngYZAllNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatusAndWorkUserIds(yz, Constants.WAIT, DateUtil.getBeginADay(), DateUtil.getEndADay());
        queueAllNumVo.setWaitIngKJAllNum(waitIngKJAllNum);
        queueAllNumVo.setWaitIngYZAllNum(waitIngYZAllNum);
        return queueAllNumVo;
    }
    public QueueAllNumVo getQueueAllNum() {
        QueueAllNumVo queueAllNumVo = new QueueAllNumVo();
        //等待中的人数
        int waitNum = dbHaWorkQueueMapper.countAllWorkQueueByServiceStatus(Constants.WAIT, DateUtil.getBeginADay(), DateUtil.getEndADay());
        //服务中的人数
        int serviceIngNum = dbHaWorkQueueMapper.countAllWorkQueueByServiceStatus(Constants.SERVICE_ING, DateUtil.getBeginADay(), DateUtil.getEndADay());
        //今日服务人数
        int todayServiceNum = dbHaWorkQueueMapper.countAllWorkQueueByServiceStatus(Constants.END_SERVICE, DateUtil.getBeginADay(), DateUtil.getEndADay());

        List<Long> kj = dbThaWorkUserMapper.selectByHaType("1", DELETE_STATUS_NO).stream().map(DbHaWorkUser::getId).collect(Collectors.toList());;
        //快捷等待中的人数
        int waitIngKJAllNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatusAndWorkUserIds(kj, Constants.WAIT, DateUtil.getBeginADay(), DateUtil.getEndADay());
        //快捷服务中的人数
        int inServiceKJAllNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatusAndWorkUserIds(kj, Constants.SERVICE_ING, DateUtil.getBeginADay(), DateUtil.getEndADay());
        //快捷今日已服务人数
        int todayServiceKJAllNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatusAndEndTimeAndWorkUserIds(kj, Constants.END_SERVICE, DateUtil.getBeginADay(), DateUtil.getEndADay());

        List<Long> yz = dbThaWorkUserMapper.selectByHaType("2", DELETE_STATUS_NO).stream().map(DbHaWorkUser::getId).collect(Collectors.toList());;
        //圆桌等待中的人数
        int waitIngYZAllNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatusAndWorkUserIds(yz, Constants.WAIT, DateUtil.getBeginADay(), DateUtil.getEndADay());
        //圆桌服务中的人数
        int inServiceYZAllNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatusAndWorkUserIds(yz, Constants.SERVICE_ING, DateUtil.getBeginADay(), DateUtil.getEndADay());
        //圆桌今日已服务人数
        int todayServiceYZAllNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatusAndEndTimeAndWorkUserIds(yz, Constants.END_SERVICE, DateUtil.getBeginADay(), DateUtil.getEndADay());

        queueAllNumVo.setWaitIngAllNum(waitNum);
        queueAllNumVo.setWaitIngKJAllNum(waitIngKJAllNum);
        queueAllNumVo.setWaitIngYZAllNum(waitIngYZAllNum);
        queueAllNumVo.setInServiceAllNum(serviceIngNum);
        queueAllNumVo.setInServiceKJAllNum(inServiceKJAllNum);
        queueAllNumVo.setInServiceYZAllNum(inServiceYZAllNum);
        queueAllNumVo.setTodayServiceAllNum(todayServiceNum);
        queueAllNumVo.setTodayServiceKJAllNum(todayServiceKJAllNum);
        queueAllNumVo.setTodayServiceYZAllNum(todayServiceYZAllNum);

        return queueAllNumVo;
    }
    /**
     * @description: 查询办事队列list
     * @params： [haWorkQueueVo 办事队列vo]
     * @return: List<HaWorkQueueVo> 返回办队列list
     * @author: kangax
     * @date: 2022-07-27 22:58
     */
    public List<HaWorkQueueVo> queryWorkQueueListByHaWorkQueue(HaWorkQueueVo haWorkQueueVo) {
        //获取当前用户信息
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        DbHaWorkQueueVo dbHaWorkQueueVo = new DbHaWorkQueueVo();
        BeanUtils.copyProperties(haWorkQueueVo, dbHaWorkQueueVo);
        //当前帮代办人员id作为查询条件
        dbHaWorkQueueVo.setServiceWorkUserId(currentHaLoginUserInfo.getId());
        //当天开始时间
        dbHaWorkQueueVo.setBeginADay(DateUtil.getBeginADay());
        //当天结束时间
        dbHaWorkQueueVo.setEndADay(DateUtil.getEndADay());
        List<DbHaWorkQueueVo> dbHaWorkQueueVoList = dbHaWorkQueueMapper.queryHaWokQueueByHaQueue(dbHaWorkQueueVo);
        //循环取出当前办事队列转并转换成队列list
        List<HaWorkQueueVo> haWorkQueueVoList = dbHaWorkQueueVoList.stream().map(dbHaWorkQueueVo1 -> {
            HaWorkQueueVo haWorkQueueVo1 = new HaWorkQueueVo();
            BeanUtils.copyProperties(dbHaWorkQueueVo1, haWorkQueueVo1);
            String datePoor = DateUtils.getDatePoor2(new Date(), haWorkQueueVo1.getCreateDate());
            haWorkQueueVo1.setWaitTime(datePoor);
            // 添加视频取号的房间信息
            if ("5".equals(dbHaWorkQueueVo1.getTakeNumberType())) { //2-视频取号
                //如果是视频咨询的用户，根据队列id获取视频咨询房间表里的房间号，房间名称
                HaVideoRoom haVideoRoom = haVideoConsultationManager.getHaVideoRoomByqueueId(dbHaWorkQueueVo1.getId());
                DbHaVideoAccess dbHaVideoAccess = dbHaVideoAccessMapper.selectByRoomIdAndWorkUserId(haVideoRoom.getId(), currentHaLoginUserInfo.getId(), null);
                if (dbHaVideoAccess != null) {
                    HaVideoAccessVo haVideoAccessVo = new HaVideoAccessVo();
                    BeanUtils.copyProperties(dbHaVideoAccess, haVideoAccessVo);
                    haVideoAccessVo.setVideoAppId(CalSig.getAppid());
                    haVideoAccessVo.setRoomNumber(haVideoRoom.getRoomNumber());
                    haWorkQueueVo1.setHaVideoAccess(haVideoAccessVo);
                }
            }
            return haWorkQueueVo1;
        }).collect(Collectors.toList());
        return haWorkQueueVoList;
    }

    /**
     * @param haLoginUserInfo 当前登录用户信息
     * @description: 帮代办人员接待下一位
     * 1.从当前帮代办人员的待服务列表中，获取等待时间最长的一位进行服务
     * 2.当接待服务成功时，工作人员的服务人数加1，当达到最大服务人数时，
     * 状态为2-忙碌，当服务人数大于0时，状态为4-服务中，当服务人数为0时，状态为3-空闲
     * @return: NextServiceResponseData 返回办事人接待下一位信息
     * @author: kangax
     * @date: 修改时间 2022-07-27 22:32
     */
    @Transactional(rollbackFor = ServiceException.class)
    public NextServiceResponseData getNextService(HaLoginUserInfo haLoginUserInfo) throws ServiceException {
        NextServiceResponseData responseData = null;
        //当天开始时间
        Date beginADay = DateUtil.getBeginADay();
        //当天结束时间
        Date endADay = DateUtil.getEndADay();
        //查询当前帮代办人员的待服务列表中，获取等待时间最长的一位进行服务
        DbHaWorkQueueVo dbHaWorkQueueVo = dbHaWorkQueueMapper.queryHaWorkQueueByServiceStatusAndWaitTime(haLoginUserInfo.getId(), Constants.WAIT, beginADay, endADay);
        if (dbHaWorkQueueVo != null) {
            responseData = new NextServiceResponseData();
            DbHaWorkQueue dbHaWorkQueue = new DbHaWorkQueue();
            //设置服务开始时间
            dbHaWorkQueueVo.setServiceBeginTime(DateUtil.getDate());
            //设置第一次服务开始时间
            if (dbHaWorkQueueVo.getFirstServiceBeginTime() == null) {
                dbHaWorkQueueVo.setFirstServiceBeginTime(DateUtil.getDate());
            }
            BeanUtils.copyProperties(dbHaWorkQueueVo, dbHaWorkQueue);
            //设置服务状态 服务中
            dbHaWorkQueue.setServiceStatus(Constants.SERVICE_ING);
            //设置更新时间
            dbHaWorkQueue.setUpdateDate(DateUtil.getDate());
            //设置更新人
            dbHaWorkQueue.setUpdateBy(haLoginUserInfo.getName());
            dbHaWorkQueueMapper.updateByPrimaryKeySelective(dbHaWorkQueue);
            BeanUtils.copyProperties(dbHaWorkQueueVo, responseData);
            responseData.setQueueId(dbHaWorkQueueVo.getId());
            //接待服务成功工作人员的服务人数加1，当达到最大服务人数时修改服务人员的状态
            //当前服务人数+1
            Long serviceWorkUserId = dbHaWorkQueueVo.getServiceWorkUserId();
            dbThaWorkUserMapper.updateCurrentServiceNumPlusOneById(serviceWorkUserId);
            //更新工作人员的状态
            updateServiceWorkStatus(serviceWorkUserId);
            //新增埋点（常规绩效时长记录）
            addHaPerformanceRegulartimeRecord(haLoginUserInfo, dbHaWorkQueue);
            /** 20230313 zhaobf 视频咨询取号船舰房间出入记录 start**/
            if ("5".equals(dbHaWorkQueueVo.getTakeNumberType())) { //2-视频取号
                //如果是视频咨询的用户，根据队列id获取视频咨询房间表里的房间号，房间名称，插入一条房间出入记录
                HaVideoRoom haVideoRoomByqueueId = haVideoConsultationManager.getHaVideoRoomByqueueId(dbHaWorkQueueVo.getId());

                HaVideoAccess haVideoAccess = new HaVideoAccess();
                haVideoAccess.setRoomOid(haVideoRoomByqueueId.getId());
                haVideoAccess.setUserType(VIDEO_USER_WORK);
                Integer videoUserNum = haVideoConsultationManager.getVideoUserNum();
                haVideoAccess.setVideoNum(Long.valueOf(videoUserNum));
                haVideoAccess.setWorkUserId(haLoginUserInfo.getId());
                haVideoAccess.setUserName(haLoginUserInfo.getName());
                haVideoAccess.setCreateBy(haLoginUserInfo.getName());
                try {
                    // 视频用户sign要后面加0生成
                    haVideoAccess.setSig(CalSig.genSign(videoUserNum + "0", haVideoRoomByqueueId.getRoomNumber() + "", 86400));
                } catch (Exception e) {
                    throw new ServiceException("视频帮办获取用户身份标识错误");
                }
                haVideoAccess.setExistStatus(VIDEO_ROOM_THROUGH);
                haVideoAccess.setInDate(new Date());
                haVideoAccess.setCallUserId(haLoginUserInfo.getId());
                haVideoAccess.setCallUserName(haLoginUserInfo.getName());
                HaVideoAccess result = haVideoConsultationManager.saveOrUpdateHaVideoAccess(haVideoAccess);
                HaVideoAccessVo haVideoAccessVo = new HaVideoAccessVo();
                BeanUtils.copyProperties(result, haVideoAccessVo);
                haVideoAccessVo.setVideoAppId(CalSig.getAppid());
                haVideoAccessVo.setRoomNumber(haVideoRoomByqueueId.getRoomNumber());
                // 视频用户id加0
                haVideoAccessVo.setVideoNum(Long.valueOf(haVideoAccessVo.getVideoNum() + "0"));
                // 屏幕共享用户id加1，sign要后面加1生成
                haVideoAccessVo.setScreenVideoNum(Long.valueOf(haVideoAccess.getVideoNum() + "1"));
                try {
                    haVideoAccessVo.setScreenVideoSig(CalSig.genSign(haVideoAccess.getVideoNum() + "1",haVideoRoomByqueueId.getRoomNumber(),86400));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ServiceException("屏幕共享生产错误");
                }
                responseData.setHaVideoAccess(haVideoAccessVo);
            }
            /** 20230313 zhaobf 视频咨询取号船舰房间出入记录 end**/


        }
        return responseData;
    }

    /**
     * 新增绩效常规时长记录埋点数据
     * @param haLoginUserInfo  当前登录用户信息（帮办人员信息）
     * @param dbHaWorkQueue  当前帮代办人员的待服务列表中，获取等待时间最长的一位办事人
     * @author: dingsn
     */
    @Transactional(rollbackFor = Exception.class)
    public void addHaPerformanceRegulartimeRecord(HaLoginUserInfo haLoginUserInfo, DbHaWorkQueue dbHaWorkQueue) {
        log.info("新增绩效常规时长记录埋点数据");
        HaPerformanceRegulartimeRecord haPerformanceRegulartimeRecord = new HaPerformanceRegulartimeRecord();
        try {
            haPerformanceRegulartimeRecord.setWorkUserOid(haLoginUserInfo.getId());
            haPerformanceRegulartimeRecord.setCaseWorkerName(dbHaWorkQueue.getName());
            haPerformanceRegulartimeRecord.setCompanyName(dbHaWorkQueue.getCompanyName());
            haPerformanceRegulartimeRecord.setServiceBeginTime(dbHaWorkQueue.getServiceBeginTime());
            haPerformanceRegulartimeRecord.setServicePeopleNum("1");
            haPerformanceRegulartimeRecord.setDeleteStatus(DELETE_STATUS_NO);
            switch (haLoginUserInfo.getGroupPost()) {/** 分组职务;1-组长，2-副组长，3-组员 */
                case "1" :
                    haPerformanceRegulartimeRecord.setManageFactor("1.5");
                    break;
                case "2" :
                    haPerformanceRegulartimeRecord.setManageFactor("1.2");
                    break;
                case "3" :
                    haPerformanceRegulartimeRecord.setManageFactor("1");
                    break;
                default:
                    break;

            }
            haPerformanceRegulartimeRecordManager.saveOrUpdatePerformanceRegulartimeRecord(haPerformanceRegulartimeRecord);
        } catch (Exception e) {
            log.error("新增保存绩效常规时长记录出错，{}",e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 修改更新绩效常规时长记录埋点数据
     * @param dbHaWorkQueue 帮代办人员的待服务
     * @author: dingsn
     */
    private void updateHaPerformanceRegulartimeRecord(DbHaWorkQueue dbHaWorkQueue) {
        log.info("转派时更新常规绩效时长记录数据");
        try {
            //获取当前登录用户主键
            Long currentHaLoginUserId = HaLoginUserHolder.getCurrentHaLoginUserInfo().getId();
            HaPerformanceRegulartimeRecord haPerformanceRegulartimeRecordVo = new HaPerformanceRegulartimeRecord();
            haPerformanceRegulartimeRecordVo.setWorkUserOid(currentHaLoginUserId);
            haPerformanceRegulartimeRecordVo.setCaseWorkerName(dbHaWorkQueue.getName());
            haPerformanceRegulartimeRecordVo.setCompanyName(dbHaWorkQueue.getCompanyName());
            haPerformanceRegulartimeRecordVo.setServiceBeginTime(dbHaWorkQueue.getServiceBeginTime());
            haPerformanceRegulartimeRecordVo.setServicePeopleNum("1");
            haPerformanceRegulartimeRecordVo.setDeleteStatus(DELETE_STATUS_NO);
            HaPerformanceRegulartimeRecord haPerformanceRegulartimeRecord = haPerformanceRegulartimeRecordManager.selectPerformanceRegulartimeRecordByHaPerformanceRegulartimeRecord(haPerformanceRegulartimeRecordVo);
            if (haPerformanceRegulartimeRecord != null) {
                log.info("查询数据主键={}", haPerformanceRegulartimeRecord.getId());
                if (dbHaWorkQueue.getServiceEndTime() != null) {//结束服务请求参数
                    haPerformanceRegulartimeRecord.setServiceEndTime(dbHaWorkQueue.getServiceEndTime());
                } else {//帮代办服务人员转派
                    haPerformanceRegulartimeRecord.setServiceEndTime(DateUtil.getDate());
                }
                //设置服务时间
                haPerformanceRegulartimeRecord.setServiceDuration(Math.toIntExact((haPerformanceRegulartimeRecord.getServiceEndTime().getTime() - haPerformanceRegulartimeRecord.getServiceBeginTime().getTime()) / (1000*60)));
                haPerformanceRegulartimeRecordManager.saveOrUpdatePerformanceRegulartimeRecord(haPerformanceRegulartimeRecord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description: 帮代办服务人员转派
     * @param haStartTurnRequestVo    队列编号
     * @author: kangax
     * @date: 2022-07-27 19:24
     * @modify: zhaobf 2022-08-25 14:53
     */
    @Transactional(rollbackFor = ServiceException.class)
    public Map<String, Object> serviceTurn( HaStartTurnRequestVo haStartTurnRequestVo) throws ServiceException {
        Map<String, Object> map = new HashMap<>();
        //获取当前登录用户信息
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        //根据ID查询原服务队列
        DbHaWorkQueue dbHaWorkQueueById = dbHaWorkQueueMapper.selectByPrimaryKey(haStartTurnRequestVo.getQueueId());
        //转派操作服务时判断当前操作人是否正确
        if (!currentHaLoginUserInfo.getId().equals(dbHaWorkQueueById.getServiceWorkUserId())) {
            throw new ServiceException("该办事队列不能当前操作用户转派");
        }
        //办事转派记录
        DbHaWorkTurnRecord dbHaWorkTurnRecord = new DbHaWorkTurnRecord();
        //判断当前服务队列是否存在
        if (dbHaWorkQueueById == null) {
            log.error("根据队列id未查询到办事队列！");
            throw new ServiceException("根据队列id未查询到办事队列");
        }

        //设置办件状态为转派中
        dbHaWorkQueueById.setServiceStatus(Constants.TURN_SERVICE);
        dbHaWorkQueueMapper.updateByPrimaryKeySelective(dbHaWorkQueueById);
        /**
         * 现在的业务转派时需要指定目标组的组长
         *转派不指定组长时使用
        DbHaWorkUser groupLeader = null;
        //根据组的ID查询当前组的所有人
        List<DbHaWorkUser> dbHaWorkUserList = dbThaWorkUserMapper.selectByGroupId(haStartTurnRequestVo.getGroupId(), Constants.DELETE_STATUS_NO,null);
        //TODO 预留  根据排班找组长  排班模块还没写好
        Optional<DbHaWorkUser> first = dbHaWorkUserList.stream().filter(dbHaWorkUser -> dbHaWorkUser.getGroupPost().equals("1")).findFirst();
        if(first.isPresent()){
            groupLeader = first.get();
        }else{
            log.error("根据token未查询到本小组组长");
            throw new ServiceException("根据token未查询到本小组组长");
        }
         */
        //检测当前服务是否在转派中
        List<DbHaWorkTurnRecord> dbHaWorkTurnRecordList = dbHaWorkTurnRecordMapper.queryByQueueIdAndVerifyWorkUserId(haStartTurnRequestVo.getQueueId(), Long.valueOf(haStartTurnRequestVo.getGroupLeaderId()), DateUtil.getBeginADay(), DateUtil.getEndADay());
        //所有的转派服务
        if (dbHaWorkTurnRecordList.size() > 0) {
            int count = 0;
            for (DbHaWorkTurnRecord workTurnRecord : dbHaWorkTurnRecordList) {
                //获取转派状态
                String turnStatus = workTurnRecord.getTurnStatus();
                //处理当30秒时间转派人员未处理则进行退回操作
                //获取转派时间
                Date turnTime = workTurnRecord.getTurnTime();
                //转派时间戳+30秒
                long turnTimeStamp = turnTime.getTime() + TURN_TIMEOUT;
                //获取当前时间时间戳
                long nowTimeStamp = new Date().getTime();
                if (nowTimeStamp > turnTimeStamp && Constants.TURN_STATUS_WAit_ACCEPT.equals(turnStatus)) {
                    //如果大于30秒，退回转派件，更新转派
                    workTurnRecord.setTurnStatus(Constants.TURN_STATUS_BACK);
                    workTurnRecord.setServiceWorkUserId(workTurnRecord.getOldServiceWorkUserId());
                    workTurnRecord.setRollbackMemo("超时未处理");
                    workTurnRecord.setHandleTime(new Date());
                    dbHaWorkTurnRecordMapper.updateByPrimaryKeySelective(workTurnRecord);
                   //设置办件状态为待服务
                    dbHaWorkQueueById.setServiceStatus(Constants.WAIT);
                    dbHaWorkQueueById.setDetectsServiceTime(new Date());
                    dbHaWorkQueueMapper.updateByPrimaryKeySelective(dbHaWorkQueueById);
                }else if (nowTimeStamp <= turnTimeStamp && Constants.TURN_STATUS_WAit_ACCEPT.equals(turnStatus)){
                    count++;
                }
            }
            //说明有转派记录
            if(count>0){
                //如果list中的转派状态有未处理的就不可以转派
                throw new ServiceException("无法转派，当前服务正在转派中。");
            }
            //说明有转派记录
            //获取所有记录转派状态list
//            List<String> turnStatusList = dbHaWorkTurnRecordList.stream().map(DbHaWorkTurnRecord::getTurnStatus).collect(Collectors.toList());
//            for (String turnStatus : turnStatusList) {
//                //如果list中的转派状态有未处理的就不可以转派
//                if (Constants.TURN_STATUS_WAit_ACCEPT.equals(turnStatus)) {
//                    throw new ServiceException("无法转派，当前服务正在转派中。");
//                }
//            }
        }
        //获取帮代办人员转派配置：1、手动接收 2、自动接收
//        String turnConfig = dbHaWorkUserById.getTurnConfig();
        //队列主键
        dbHaWorkTurnRecord.setWorkQueueId(haStartTurnRequestVo.getQueueId());
        //当前登录人员为服务转派发起人员
        dbHaWorkTurnRecord.setHandleWorkUserId(currentHaLoginUserInfo.getId());
        //服务人员编号
//        dbHaWorkTurnRecord.setServiceWorkUserId();
        //原服务人员编号
        dbHaWorkTurnRecord.setOldServiceWorkUserId(dbHaWorkQueueById.getServiceWorkUserId());
        //转派组长id
        dbHaWorkTurnRecord.setVerifyWorkUserId(Long.valueOf(haStartTurnRequestVo.getGroupLeaderId()));
        //转派时间
        dbHaWorkTurnRecord.setTurnTime(new Date());
        //转派原因
        dbHaWorkTurnRecord.setTurnMemo(haStartTurnRequestVo.getTurnMemo());
        //创建时间
        dbHaWorkTurnRecord.setCreateDate(new Date());
        //创建人
        dbHaWorkTurnRecord.setCreateBy(currentHaLoginUserInfo.getName());
        //转派状态   待审核
        dbHaWorkTurnRecord.setTurnStatus(Constants.TURN_STATUS_WAit_ACCEPT);
        //服务开始时间
        dbHaWorkTurnRecord.setServiceBeginTime(dbHaWorkQueueById.getServiceBeginTime());
//        if (Constants.TURN_CONFIG_AUTO_ACCEPT.equals(turnConfig)) {
//            //当是自动接收时设置转派状态为：2.接收
//            dbHaWorkTurnRecord.setTurnStatus(Constants.TURN_STATUS_ACCEPT);
//            //自动接收时设置处理时间 为当前时间
//            dbHaWorkTurnRecord.setHandleTime(new Date());
//        } else {
//            //转派配置默认为手动接收，当不是自动接收时都是手动接收
//            //当是手动接收时设置转派状态为：1.待接收
//            dbHaWorkTurnRecord.setTurnStatus(Constants.TURN_STATUS_WAit_ACCEPT);
//        }
        //保存办事转派记录表
        int insertTurnRecord = dbHaWorkTurnRecordMapper.insert(dbHaWorkTurnRecord);
        if (insertTurnRecord > 0) {
            //保存办事转派记录表成功
            Long oldServiceWorkUserId = dbHaWorkTurnRecord.getOldServiceWorkUserId();
            Long verifyWorkUserId = dbHaWorkTurnRecord.getVerifyWorkUserId();
            //发送消息给转派人
            sendMessageForTurnService(oldServiceWorkUserId, verifyWorkUserId);

            //返回当前转派记录编号
            Long turnRecordId = dbHaWorkTurnRecord.getId();
            //返回当前转派状态
            String turnStatus = dbHaWorkTurnRecord.getTurnStatus();
            updateHaPerformanceRegulartimeRecord(dbHaWorkQueueById);
            map.put("turnRecordId", turnRecordId);
            map.put("turnStatus", turnStatus);
            return map;
        } else {
            log.error("保存办事转派记录失败！");
            throw new ServiceException("保存办事转派记录失败！");
        }


    }

    /**
     * @param receiveWorkUserId 接收人id
     * @param sendWorkUserId    发送人id
     * @description: 办事转派发送消息
     * @return: int 返回执行行数
     * @author: kangax
     * @date: 2022-08-03 16:07
     */
    private int sendMessageForTurnService(Long sendWorkUserId, Long receiveWorkUserId) {
        DbHaMessage dbHaMessage = new DbHaMessage();
        dbHaMessage.setTitle("帮办转派提醒");
        dbHaMessage.setContent("您好！组内有新的转派申请，注意审核");
        dbHaMessage.setSendUserId(sendWorkUserId);//发送人
        dbHaMessage.setSendTime(new Date());
        dbHaMessage.setReceiveUserId(receiveWorkUserId);//接收人
        dbHaMessage.setReadStatus("1");//未读取
        dbHaMessage.setDeleteStatus("1");
        dbHaMessage.setCreateDate(new Date());
        int insertMessageIndex = dbHaMessageMapper.insert(dbHaMessage);
        return insertMessageIndex;
    }

    /**
     * 更新帮代办工作人员状态
     *
     * @param serviceWorkUserId 工作人员编号
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateServiceWorkStatus(Long serviceWorkUserId) {
        //查询当前工作人员的服务人数
        DbHaWorkUser dbThaWorkUser = dbThaWorkUserMapper.queryById(serviceWorkUserId);
        int currentServiceNum = dbThaWorkUser.getCurrentServiceNum() == null ? 0 : dbThaWorkUser.getCurrentServiceNum();
        if (currentServiceNum >= dbThaWorkUser.getMaxServiceNum()) {
            //更新状态为忙碌 状态为2
            dbThaWorkUser.setStatus(Constants.WORK_USER_STATUS_BUSY);
            dbThaWorkUserMapper.update(dbThaWorkUser);
        } else if (currentServiceNum == 0) {
            //更新状态为空闲 状态为3
            dbThaWorkUser.setStatus(Constants.WORK_USER_STATUS_FREE);
            dbThaWorkUserMapper.update(dbThaWorkUser);
        } else if (currentServiceNum >= 1) {
            //更新状态为服务中 状态为4
            dbThaWorkUser.setStatus(Constants.WORK_USER_IN_SERVICE);
            dbThaWorkUserMapper.update(dbThaWorkUser);
        }
    }

    /**
     * @param haWorkServiceRequestData 结束服务请求参数
     * @description: 结束本次服务，当办事人服务完成后调用，结束服务时，服务人员的当前服务人员减1
     * @return: void
     * @author: kangax
     * @date: 2022-07-27 18:32
     */
    @Transactional(rollbackFor = ServiceException.class)
    public void endService(HaWorkServiceRequestData haWorkServiceRequestData) throws ServiceException {
        //获取当前用户信息
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        //保存或修改下次建议
        DbHaWorkQueue dbHaWorkQueue = dbHaWorkQueueMapper.selectByPrimaryKey(haWorkServiceRequestData.getWorkQueueId());
        if (dbHaWorkQueue == null) {
            throw new ServiceException("根据队列编号未查询到队列！！");
        }
        //判断当前操作用是否正确
        if (!dbHaWorkQueue.getServiceWorkUserId().equals(currentHaLoginUserInfo.getId())) {
            throw new ServiceException("当前办事队列不属于当前操作用户");
        }
        //服务状态设置为服务完成
        dbHaWorkQueue.setServiceStatus(Constants.END_SERVICE);
        //下次服务建议
        dbHaWorkQueue.setNextServiceAdvise(haWorkServiceRequestData.getNextServiceAdvise());
        //分配建议
        dbHaWorkQueue.setDistributionAdvise(haWorkServiceRequestData.getDistributionAdvise());
        //建议内容
        dbHaWorkQueue.setAdviseMemo(haWorkServiceRequestData.getAdviseMemo());
        //办事队列修改人
        dbHaWorkQueue.setUpdateBy(currentHaLoginUserInfo.getName());
        //办事队列修改时间
        dbHaWorkQueue.setUpdateDate(DateUtil.getDate());
        //设置服务结束时间
        dbHaWorkQueue.setServiceEndTime(DateUtil.getDate());
        //设置服务时间
        dbHaWorkQueue.setServiceDuration(Math.toIntExact((dbHaWorkQueue.getServiceEndTime().getTime() - dbHaWorkQueue.getFirstServiceBeginTime().getTime()) / 1000));
        dbHaWorkQueueMapper.updateByPrimaryKeySelective(dbHaWorkQueue);
        //根据队列编号和当前用户查询是否有转派记录
        List<DbHaWorkTurnRecord> dbHaWorkTurnRecordList = dbHaWorkTurnRecordMapper.queryByQueueIdAndWorkUserId(haWorkServiceRequestData.getWorkQueueId(), currentHaLoginUserInfo.getId(), DateUtil.getBeginADay(), DateUtil.getEndADay());
        if (dbHaWorkTurnRecordList.size() > 0) {
            //获取办事转派记录ID
            List<Long> turnRecordIds = dbHaWorkTurnRecordList.stream().map(DbHaWorkTurnRecord::getId).collect(Collectors.toList());
            //更新服务结束时间
            int count = dbHaWorkTurnRecordMapper.updateByIds(turnRecordIds, currentHaLoginUserInfo.getName(), dbHaWorkQueue.getServiceEndTime(), dbHaWorkQueue.getServiceDuration());
            if (count > 0) {
                log.info("批量更新办事转派记录服务结束时间成功！");
            }
        }
        //修改成功
        //结束本次服务成功，服务人员的当前服务人数减1
        dbThaWorkUserMapper.updateCurrentServiceNumReduceOneById(dbHaWorkQueue.getServiceWorkUserId());
        //更新工作人员的状态
        updateServiceWorkStatus(dbHaWorkQueue.getServiceWorkUserId());
        //绩效统计方法
        updateHaPerformanceRegulartimeRecord(dbHaWorkQueue);

    }

    /**
     * 帮办人员帮助办事人叫号
     *
     * @param queueId       队列编号
     * @param agentTakeNumber 帮代办人员帮助办事人取的号
     * @author: kangax
     * @date: 2022/7/26 13:21
     */
    @Transactional(rollbackFor = ServiceException.class)
    public String workUserHelpWindowCall(Long queueId, String agentTakeNumber) throws ServiceException {
        //获取当前操作用户
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        DbHaWorkQueue dbHaWorkQueue = dbHaWorkQueueMapper.selectByPrimaryKey(queueId);
        if (dbHaWorkQueue == null) {
            throw new ServiceException("未查询到该办事队列");
        }

        //调用外部窗口取号接口获取窗口号 todo
        String result = HttpUtil.get(InterUrlUtil.CALL_NUMBER_URL);

        //插入排队号
        dbHaWorkQueue.setWindowsNumber(agentTakeNumber);
        //更新人
        dbHaWorkQueue.setUpdateBy(currentHaLoginUserInfo.getName());
        //更新时间
        dbHaWorkQueue.setUpdateDate(DateUtil.getDate());
        dbHaWorkQueueMapper.updateByPrimaryKeySelective(dbHaWorkQueue);
        return dbHaWorkQueue.getWindowsNumber();
    }

    /**
     * @param turnRecordId 转派记录编号
     * @param turnStatus   转派状态，2-接收，3-退回
     * @param rollbackMemo 退回原因，当退回时，必须填写退回原因
     * @description: 办事转派服务接收
     * @author: kangax
     * @date: 2022-08-04 14:04
     * @modify: zhaobf 2022-08-25 16:23
     */
    public String serviceAccept(Long turnRecordId, String turnStatus, String rollbackMemo, Long workUserId) throws ServiceException {
        //获取当前登录用户信息
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        //根据转派记录编号获取转派记录
        DbHaWorkTurnRecord dbHaWorkTurnRecord = dbHaWorkTurnRecordMapper.selectByPrimaryKey(turnRecordId);
        //判断当前转派记录是否存在
        if (dbHaWorkTurnRecord == null) {
            log.error("办事转派审核：根据转派记录id未查询到转派记录！");
            throw new ServiceException("根据转派记录id未查询到转派记录");
        }
        //判断当前转派记录是否属于当前操作用户
        if (!dbHaWorkTurnRecord.getVerifyWorkUserId().equals(currentHaLoginUserInfo.getId())) {
            throw new ServiceException("当前转派服务不属于当前用户，不能进行审核");
        }
        //获取办事当前办事队列
        DbHaWorkQueue dbHaWorkQueue = dbHaWorkQueueMapper.selectByPrimaryKey(dbHaWorkTurnRecord.getWorkQueueId());
        //判断当前办事队列记录是否存在
        if (dbHaWorkQueue == null) {
            log.error("办事转派审核：根据办事队列记录id未查询到办事队列！");
            throw new ServiceException("根据办事队列记录id未查询到办事队列");
        }
        //获取服务当前转派审核状态
        String currentTurnStatus = dbHaWorkTurnRecord.getTurnStatus();
        //转派审核，验证是否已经处理
        if (!Constants.TURN_STATUS_WAit_ACCEPT.equals(currentTurnStatus)) {
            //如果当前服务转派状态不是待接收，那就是已经处理 直接返回
            return currentTurnStatus;
        }
        //设置处理时间
        dbHaWorkTurnRecord.setHandleTime(new Date());
        //设置更新人
        dbHaWorkTurnRecord.setUpdateBy(currentHaLoginUserInfo.getName());
        //设置更新时间
        dbHaWorkTurnRecord.setUpdateDate(new Date());
        //设置更新时间
        dbHaWorkTurnRecord.setUpdateDate(new Date());
        //设置转派审核状态
        dbHaWorkTurnRecord.setTurnStatus(turnStatus);
        //设置退回原因
        dbHaWorkTurnRecord.setRollbackMemo(rollbackMemo);
        //设置转派目标用户
        dbHaWorkTurnRecord.setServiceWorkUserId(workUserId);
        //更新办事转派记录
        dbHaWorkTurnRecordMapper.updateByPrimaryKeySelective(dbHaWorkTurnRecord);
        //如果是审核状态进行以下处理：1.更新办事队列表中帮代办人员编号 2.更新服务人员的当前服务人数 3.更新服务人员的状态
        if (Constants.TURN_STATUS_ACCEPT.equals(turnStatus)) {
            //设置服务状态  重置为等待服务
            dbHaWorkQueue.setServiceStatus(Constants.WAIT);
            //办事队列服务人员编号
            dbHaWorkQueue.setServiceWorkUserId(workUserId);
            //办事队列更新时间
            dbHaWorkQueue.setUpdateDate(new Date());
            /**
             * 转派服务重置检测时间
             */
            dbHaWorkQueue.setDetectsServiceTime(new Date());
            //办事队列更新人
            dbHaWorkQueue.setUpdateBy(currentHaLoginUserInfo.getName());
            //更新办事队列
            dbHaWorkQueueMapper.updateByPrimaryKeySelective(dbHaWorkQueue);
            //原服务人员当前服务人数-1
            dbThaWorkUserMapper.updateCurrentServiceNumReduceOneById(dbHaWorkTurnRecord.getOldServiceWorkUserId());
//            //现服务人员当前服务人数+1
//            dbThaWorkUserMapper.updateCurrentServiceNumPlusOneById(currentHaLoginUserInfo.getId());
            //更新原服务人员的状态
            updateServiceWorkStatus(dbHaWorkTurnRecord.getOldServiceWorkUserId());
            //更新转派服务人员的状态
//            updateServiceWorkStatus(currentHaLoginUserInfo.getId());
        }else  if (Constants.TURN_STATUS_BACK.equals(turnStatus)) {
            //办事队列更新时间
            dbHaWorkQueue.setUpdateDate(new Date());
            //设置服务状态  重置为等待服务
            dbHaWorkQueue.setServiceStatus(Constants.WAIT);
            /**
             * 转派服务重置检测时间
             */
            dbHaWorkQueue.setDetectsServiceTime(new Date());
            dbHaWorkQueueMapper.updateByPrimaryKeySelective(dbHaWorkQueue);
        }

        return turnStatus;

    }

    /**
     * 选择办事人进行接待服务
     *
     * @param queueId 办事队列编号
     */
    public NextServiceResponseData chooseUserService(Long queueId) throws ServiceException {
        //获取当前用户信息
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        NextServiceResponseData responseData = new NextServiceResponseData();
        DbHaWorkQueue dbHaWorkQueue = dbHaWorkQueueMapper.selectByPrimaryKey(queueId);
        if (dbHaWorkQueue == null) {
            throw new ServiceException("根据队列id查询办事队列为空");
        }
        //判断当前队列是否是当前操作用的
        if (!dbHaWorkQueue.getServiceWorkUserId().equals(currentHaLoginUserInfo.getId())) {
            throw new ServiceException("当前办事队列不属于该操作用户");
        }
        //忙碌状态不能再次接待
        DbHaWorkUserVo helpWorkUserById = haWorkUserLoginManager.loginByAccounAndId(null,currentHaLoginUserInfo.getId());
        if("2".equals(helpWorkUserById.getStatus())){
            throw new ServiceException("当前帮代办人员忙碌，不能接待");
        }
        /* 判断下一位是视频用户的话查询当天还有没有正在服务的视频帮办，有的话则不允许接待 */
        if ("5".equals(dbHaWorkQueue.getTakeNumberType())) {
            // 查询当天的服务信息列表
            DbHaWorkQueueVo workQueueVo = new DbHaWorkQueueVo();
            //当前帮代办人员id作为查询条件
            workQueueVo.setServiceWorkUserId(currentHaLoginUserInfo.getId());
            //当天开始时间
            workQueueVo.setBeginADay(DateUtil.getBeginADay());
            //当天结束时间
            workQueueVo.setEndADay(DateUtil.getEndADay());
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
        //设置服务状态 服务中
        dbHaWorkQueue.setServiceStatus(Constants.SERVICE_ING);
        //设置服务开始时间
        dbHaWorkQueue.setServiceBeginTime(DateUtil.getDate());
        //设置第一次服务开始时间
        if (dbHaWorkQueue.getFirstServiceBeginTime() == null) {
            dbHaWorkQueue.setFirstServiceBeginTime(DateUtil.getDate());
        }
        //设置更新时间
        dbHaWorkQueue.setUpdateDate(DateUtil.getDate());
        //设置更新人
        dbHaWorkQueue.setUpdateBy(currentHaLoginUserInfo.getName());
        dbHaWorkQueueMapper.updateByPrimaryKeySelective(dbHaWorkQueue);
        BeanUtils.copyProperties(dbHaWorkQueue, responseData);
        responseData.setQueueId(dbHaWorkQueue.getId());
        //接待成功当前工作人员的服务人数加1
        dbThaWorkUserMapper.updateCurrentServiceNumPlusOneById(dbHaWorkQueue.getServiceWorkUserId());
        //更新服务人员的状态
        updateServiceWorkStatus(dbHaWorkQueue.getServiceWorkUserId());

        /** 20230313 zhaobf 视频咨询取号船舰房间出入记录 start**/
        if ("5".equals(dbHaWorkQueue.getTakeNumberType())) { //2-视频取号
            //如果是视频咨询的用户，根据队列id获取视频咨询房间表里的房间号，房间名称，插入一条房间出入记录
            HaVideoRoom haVideoRoomByqueueId = haVideoConsultationManager.getHaVideoRoomByqueueId(queueId);

            HaVideoAccess haVideoAccess = new HaVideoAccess();
            haVideoAccess.setRoomOid(haVideoRoomByqueueId.getId());
            haVideoAccess.setUserType(VIDEO_USER_WORK);
            Integer videoUserNum = haVideoConsultationManager.getVideoUserNum();
            haVideoAccess.setVideoNum(Long.valueOf(videoUserNum));
            haVideoAccess.setWorkUserId(currentHaLoginUserInfo.getId());
            try {
                // 视频用户sign要后面加0生成
                haVideoAccess.setSig(CalSig.genSign(videoUserNum + "0", haVideoRoomByqueueId.getRoomNumber() + "", 86400));
            } catch (Exception e) {
                throw new ServiceException("视频帮办获取用户身份标识错误");
            }
            haVideoAccess.setExistStatus(VIDEO_ROOM_THROUGH);
            haVideoAccess.setInDate(new Date());
            haVideoAccess.setCallUserId(currentHaLoginUserInfo.getId());
            haVideoAccess.setCallUserName(currentHaLoginUserInfo.getName());
            HaVideoAccess result = haVideoConsultationManager.saveOrUpdateHaVideoAccess(haVideoAccess);
            HaVideoAccessVo haVideoAccessVo = new HaVideoAccessVo();
            BeanUtils.copyProperties(result, haVideoAccessVo);
            haVideoAccessVo.setVideoAppId(CalSig.getAppid());
            haVideoAccessVo.setRoomNumber(haVideoRoomByqueueId.getRoomNumber());
            // 视频用户id加0
            haVideoAccessVo.setVideoNum(Long.valueOf(haVideoAccessVo.getVideoNum() + "0"));
            responseData.setHaVideoAccess(haVideoAccessVo);
            // 屏幕共享用户id加1，sign要后面加1生成
            haVideoAccessVo.setScreenVideoNum(Long.valueOf(haVideoAccess.getVideoNum() + "1"));
            try {
                haVideoAccessVo.setScreenVideoSig(CalSig.genSign(haVideoAccess.getVideoNum() + "1",haVideoRoomByqueueId.getRoomNumber(),86400));
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("屏幕共享生产错误");
            }

        }
        /** 20230313 zhaobf 视频咨询取号船舰房间出入记录 end**/
        return responseData;
    }

    /**
     * @description: 查询是否有新的办事人到来等待服务
     * @return: boolean
     * @author: kangax
     * @date: 2022-07-26 14:11
     */
    public int isHaveNewMesses(Long workUserId, Date beginTime) throws ServiceException {
        int count = dbHaWorkQueueMapper.selectCountByBeginTime(workUserId, beginTime);
        return count;
    }

    public List<HaWorkQueue> selectByHaWorkQueue(HaWorkQueue haWorkQueue) throws ServiceException {
        DbHaWorkQueueExample example = new DbHaWorkQueueExample();
        DbHaWorkQueueExample.Criteria criteria = example.createCriteria();
        if (null != haWorkQueue) {
            if (StrUtil.isNotEmpty(haWorkQueue.getName())) {
                criteria.andNameLike("%" + haWorkQueue.getName() + "%");
            }
            if (StrUtil.isNotEmpty(haWorkQueue.getCardNo())) {
                criteria.andCardNoEqualTo(haWorkQueue.getCardNo());
            }
//            criteria.andDeleteStatusEqualTo("N");
            example.setOrderByClause("CREATE_DATE desc");
        }
        List<DbHaWorkQueue> dbHaWorkQueues = dbHaWorkQueueMapper.selectByExample(example);
        List<HaWorkQueue> haQuestionList = dbHaWorkQueues.stream().map(dbHaWorkQueue1 -> {
            HaWorkQueue haWorkQueue1 = new HaWorkQueue();
            BeanUtils.copyProperties(dbHaWorkQueue1, haWorkQueue1);
            return haWorkQueue1;
        }).collect(Collectors.toList());
        return haQuestionList;
    }


    /**
     * 根据id删除办理队列
     *
     * @param id 主键
     */
    @Transactional(rollbackFor = ServiceException.class)
    public int deleteWorkQueueId(String id) throws ServiceException {
        return dbHaWorkQueueMapper.deleteByPrimaryKey(Long.valueOf(id));
    }

    @Transactional(rollbackFor = ServiceException.class)
    public int saveHaWorkQueue(HaWorkQueue haWorkQueue) {
        int index = 0;
        if (haWorkQueue.getId() != null) {
            //修改
            DbHaWorkQueue dbhaWorkQueue = new DbHaWorkQueue();
            org.springframework.beans.BeanUtils.copyProperties(haWorkQueue, dbhaWorkQueue);
            index = dbHaWorkQueueMapper.updateByPrimaryKeySelective(dbhaWorkQueue);
        } else {
            //新增
            DbHaWorkQueue dbhaWorkQueue = new DbHaWorkQueue();
//            haWorkQueue.setId(getId());
            org.springframework.beans.BeanUtils.copyProperties(haWorkQueue, dbhaWorkQueue);
            index = dbHaWorkQueueMapper.insert(dbhaWorkQueue);
        }
        return index;
    }

    /**
     * 查询办事队列list
     *
     * @param haWorkQueueVo
     * @return
     */
    public PageResult<HaWorkQueueVo> queryWorkQueueListWithPage(HaWorkQueueVo haWorkQueueVo) {
        DbHaWorkQueueVo dbHaWorkQueueVo = new DbHaWorkQueueVo();
        BeanUtils.copyProperties(haWorkQueueVo, dbHaWorkQueueVo);
        Page<DbHaWorkQueueVo> dbHaWorkQueues = (Page<DbHaWorkQueueVo>) dbHaWorkQueueMapper.queryHaWokQueueByHaQueue(dbHaWorkQueueVo);
        PageResult<HaWorkQueueVo> pageResult = new PageResult<>(dbHaWorkQueues.getPageNum(), dbHaWorkQueues.getPageSize(), dbHaWorkQueues.getTotal());
        List<HaWorkQueueVo> haWorkQueues = dbHaWorkQueues.stream().map(dbHaUserResource -> {
            HaWorkQueueVo haUserResource = new HaWorkQueueVo();
            org.springframework.beans.BeanUtils.copyProperties(dbHaUserResource, haUserResource);
            return haUserResource;
        }).collect(Collectors.toList());
        pageResult.setData(haWorkQueues);
        return pageResult;

    }

    @Transactional(rollbackFor = ServiceException.class)
    public HaWorkQueue getWorkQueueById(String id) throws ServiceException {
        HaWorkQueue haWorkQueue = new HaWorkQueue();
        DbHaWorkQueue dbHaWorkQueue = dbHaWorkQueueMapper.selectByPrimaryKey(Long.valueOf(id));
        BeanUtils.copyProperties(dbHaWorkQueue, haWorkQueue);
        return haWorkQueue;
    }

    //    @Transactional(rollbackFor = ServiceException.class)
    public HaWorkQueueVo getServiceNum(Long userId) throws ServiceException {
        HaWorkQueueVo haWorkQueue = new HaWorkQueueVo();
        DbHaWorkQueueVo dbHaWorkQueue = dbHaWorkQueueMapper.getServiceNum(userId);
        BeanUtils.copyProperties(dbHaWorkQueue, haWorkQueue);
        return haWorkQueue;
    }

    /**
     * @description: 扫码填写帮代办信息
     * @param requestData 扫码办事请求参数
     * @return: HaScanHelpInfoResponseData 请求响应类
     * @author: kangax
     * @date: 2022-07-28 18:12
     */
    @Transactional(rollbackFor = ServiceException.class)
    public HaScanHelpInfoResponseData scanHelpInfoFillIn(HaScanHelpInfoRequestData requestData) {
        //扫码请求响应类
        HaScanHelpInfoResponseData responseData = new HaScanHelpInfoResponseData();
        //办事队列
        DbHaWorkQueue dbhaWorkQueue = new DbHaWorkQueue();
        BeanUtils.copyProperties(requestData, dbhaWorkQueue);
        if (requestData.getWorkUserId() != null) {
            //设置导服已分配
            dbhaWorkQueue.setQueueStatus("2");
            //设置分配人为系统
            dbhaWorkQueue.setDistributeUserId((long) -1);
            //分配时间
            dbhaWorkQueue.setDistributeTime(DateUtil.getDate());
            //分配状态 指定人员
            dbhaWorkQueue.setDistributeStatus("1");
            //服务状态
            dbhaWorkQueue.setServiceStatus(Constants.WAIT);
            //服务工作人员编号
            dbhaWorkQueue.setServiceWorkUserId(requestData.getWorkUserId());
        } else {
            //设置排队状态为扫码排队中
            dbhaWorkQueue.setQueueStatus("1");
        }
        //设置创建人为-1 因为是办事人插入数据
        dbhaWorkQueue.setCreateBy("-1");
        dbhaWorkQueue.setCardType(StringUtil.isNotEmpty(requestData.getCardType())?requestData.getCardType():"1");
        //设置创建时间
        dbhaWorkQueue.setCreateDate(new Date());
        dbhaWorkQueue.setDetectsServiceTime(new Date());
        dbHaWorkQueueMapper.insert(dbhaWorkQueue);
        //拷贝返回信息
        BeanUtils.copyProperties(dbhaWorkQueue, responseData);
        return responseData;
    }

    /**
     * @description: 获取某个用户的办事队列
     * @param workUserId 工作人员编号
     * @return: HaWorkQueueVo 办事队列详细信息
     * @author: kangax
     * @date: 2022-08-11 14:44
     */
    public List<HaWorkQueueVo> queryWorkQueueListByUser(Long workUserId) {
        //当工作人员编号为空时，返回空
        if (workUserId == null) {
            return null;
        }
        DbHaWorkQueueVo dbHaWorkQueueVo = new DbHaWorkQueueVo();
        dbHaWorkQueueVo.setServiceWorkUserId(workUserId);
        //当天开始时间
        dbHaWorkQueueVo.setBeginADay(DateUtil.getBeginADay());
        //当天结束时间
        dbHaWorkQueueVo.setEndADay(DateUtil.getEndADay());
        List<DbHaWorkQueueVo> dbHaWorkQueueVoList = dbHaWorkQueueMapper.queryHaWokQueueByHaQueue(dbHaWorkQueueVo);
        //循环取出当前办事队列转并转换成队列list
        List<HaWorkQueueVo> haWorkQueueVoList = dbHaWorkQueueVoList.stream().map(dbHaWorkQueueVo1 -> {
            HaWorkQueueVo haWorkQueueVo1 = new HaWorkQueueVo();
            BeanUtils.copyProperties(dbHaWorkQueueVo1, haWorkQueueVo1);
            return haWorkQueueVo1;
        }).collect(Collectors.toList());
        return haWorkQueueVoList;
    }

    /**
     * @description: 根据分组查询查看所有用户的服务信息
     * @param groupId 分组编号
     * @param name 工作人员姓名
     * @author: kangax
     * @date: 2022-08-11 17:24
     */
    public List<HaWorkQueueResponseVo> getAllUserService(Long groupId, String name) {
        return dbHaWorkQueueMapper.getWorkUserList(name, null, groupId, DateUtil.getBeginADay(), DateUtil.getEndADay());
    }

    public List<HaWorkQueueResponseVo> getAllUserServiceByName(Long groupId, String name) {
        return dbHaWorkQueueMapper.getWorkUserListByName(name, null, groupId);
    }
    public List<HaWorkQueueResponseVo> getAllUserServiceByGroupSplit(Long groupId,Long groupSplitId, String name) {
        return dbHaWorkQueueMapper.getAllUserListByGroupSplit(name, null, groupId,groupSplitId,DateUtil.getBeginADay(), DateUtil.getEndADay());
    }


    /**
     * @description: 获取全部的办事队列（帮办人，服务状态等
     * @param haWorkQueueVo 办事队列请求参数
     * @author: kangax
     * @date: 2022-08-17 14:20
     */
    public PageResult<HaWorkQueueVo> queryAllWorkQueueListWithPage(HaWorkQueueVo haWorkQueueVo, String beginTime, String endTime, Integer pageNum, Integer pageSize) throws Exception {

        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        DbHaWorkQueueVo dbHaWorkQueueVo = new DbHaWorkQueueVo();
        BeanUtils.copyProperties(haWorkQueueVo, dbHaWorkQueueVo);

        //来访时间
        dbHaWorkQueueVo.setBeginADay(StringUtil.isEmpty(beginTime)?null:DateUtil.datetimeFormat.parse(beginTime));
        //来访结束时间
        dbHaWorkQueueVo.setEndADay(StringUtil.isEmpty(endTime)?null:DateUtil.datetimeFormat.parse(endTime));
        //查询
        Page<DbHaWorkQueueVo> dbHaWorkQueueVoList = (Page<DbHaWorkQueueVo>) dbHaWorkQueueMapper.queryHaWokQueueByHaQueue(dbHaWorkQueueVo);
        PageResult<HaWorkQueueVo> pageResult = new PageResult<>(dbHaWorkQueueVoList.getPageNum(), dbHaWorkQueueVoList.getPageSize(), dbHaWorkQueueVoList.getTotal());
        List<HaWorkQueueVo> haWorkQueueVos = dbHaWorkQueueVoList.stream().map(dbHaWorkQueueVo1 -> {
            HaWorkQueueVo haWorkQueueVo1 = new HaWorkQueueVo();
            BeanUtils.copyProperties(dbHaWorkQueueVo1, haWorkQueueVo1);
            return haWorkQueueVo1;
        }).collect(Collectors.toList());
        pageResult.setData(haWorkQueueVos);
        return pageResult;
    }

    /**
     * @description: 获取全部的办事队列
     * @param haWorkQueueVo 办事队列请求参数
     * @author: kangax
     * @date: 2022-08-17 14:20
     */
    public PageResult<HaWorkServiceVo> getTempServiceList(HaWorkQueueVo haWorkQueueVo, String beginTime, String endTime, Integer pageNum, Integer pageSize) throws Exception {
        //获取当前登录用户信息
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        DbHaWorkQueueVo dbHaWorkQueueVo = new DbHaWorkQueueVo();
        BeanUtils.copyProperties(haWorkQueueVo, dbHaWorkQueueVo);
        //当前帮代办人员id作为查询条件
        dbHaWorkQueueVo.setServiceWorkUserId(currentHaLoginUserInfo.getId());
        //来访时间
        dbHaWorkQueueVo.setBeginADay(StringUtil.isEmpty(beginTime)?null:DateUtil.datetimeFormat.parse(beginTime));
        //来访结束时间
        dbHaWorkQueueVo.setEndADay(StringUtil.isEmpty(endTime)?null:DateUtil.datetimeFormat.parse(endTime));
        //查询
        Page<DbHaWorkServiceVo> dbHaWorkQueueVoList = (Page<DbHaWorkServiceVo>) dbHaWorkServiceMapper.getTempServiceList(dbHaWorkQueueVo);
        PageResult<HaWorkServiceVo> pageResult = new PageResult<>(dbHaWorkQueueVoList.getPageNum(), dbHaWorkQueueVoList.getPageSize(), dbHaWorkQueueVoList.getTotal());
        List<HaWorkServiceVo> haWorkQueueVos = dbHaWorkQueueVoList.stream().map(dbHaWorkQueueVo1 -> {
            HaWorkServiceVo haWorkQueueVo1 = new HaWorkServiceVo();
            BeanUtils.copyProperties(dbHaWorkQueueVo1, haWorkQueueVo1);
            return haWorkQueueVo1;
        }).collect(Collectors.toList());
        pageResult.setData(haWorkQueueVos);
        return pageResult;
    }

    /**
     * @description: 根据身份证号码获取用户的帮办队列记录
     * @param haWorkQueueVo 办事队列请求参数
     * @author: zhaobf
     * @date: 2022-09-02 9:38
     */
    public PageResult<HaWorkQueueVo> queryQueueHistoryListWithPage(HaWorkQueueVo haWorkQueueVo, String beginTime, String endTime, Integer pageNum, Integer pageSize) throws Exception {

        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        DbHaWorkQueueVo dbHaWorkQueueVo = new DbHaWorkQueueVo();
        BeanUtils.copyProperties(haWorkQueueVo, dbHaWorkQueueVo);
        //来访时间
        dbHaWorkQueueVo.setBeginADay(StringUtil.isEmpty(beginTime)?null:DateUtil.datetimeFormat.parse(beginTime));
        //来访结束时间
        dbHaWorkQueueVo.setEndADay(StringUtil.isEmpty(endTime)?null:DateUtil.datetimeFormat.parse(endTime));
        //查询
        Page<DbHaWorkQueueVo> dbHaWorkQueueVoList = (Page<DbHaWorkQueueVo>) dbHaWorkQueueMapper.queryHaWokQueueByHaQueue(dbHaWorkQueueVo);
        PageResult<HaWorkQueueVo> pageResult = new PageResult<>(dbHaWorkQueueVoList.getPageNum(), dbHaWorkQueueVoList.getPageSize(), dbHaWorkQueueVoList.getTotal());
        List<HaWorkQueueVo> haWorkQueueVos = dbHaWorkQueueVoList.stream().map(dbHaWorkQueueVo1 -> {
            HaWorkQueueVo haWorkQueueVo1 = new HaWorkQueueVo();
            BeanUtils.copyProperties(dbHaWorkQueueVo1, haWorkQueueVo1);
            return haWorkQueueVo1;
        }).collect(Collectors.toList());
        pageResult.setData(haWorkQueueVos);
        return pageResult;
    }

    /**
     * @description: 根据身份证号码获取用户的帮办队列记录
     * @param haWorkQueueVo 办事队列请求参数
     * @author: zhaobf
     * @date: 2022-09-02 9:38
     */
    public PageResult<HaWorkServiceVo> queryServiceHistoryListWithPage(HaWorkQueueVo haWorkQueueVo, Integer pageNum, Integer pageSize) {

        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }

        DbHaWorkQueueVo dbHaWorkQueueVo = new DbHaWorkQueueVo();
        BeanUtils.copyProperties(haWorkQueueVo, dbHaWorkQueueVo);
        //置空服务状态（queue的serviceStatus和service表的serviceStatus不一样）
        dbHaWorkQueueVo.setServiceStatus("");
        //获取所有满足条件的队列的id list
        List<Long> queueIds = dbHaWorkQueueMapper.queryHaWokQueueByHaQueue(dbHaWorkQueueVo).stream().map(DbHaWorkQueueVo::getId).collect(Collectors.toList());
        PageHelper.startPage(pageNum, pageSize);
        Page<DbHaWorkServiceVo> dbHaWorkServices = (Page<DbHaWorkServiceVo>) dbHaWorkServiceMapper.queryHelpServiceHistoryList(queueIds,haWorkQueueVo.getServiceStatus());;
        PageResult<HaWorkServiceVo> pageResult = new PageResult<>(dbHaWorkServices.getPageNum(), dbHaWorkServices.getPageSize(), dbHaWorkServices.getTotal());
        List<HaWorkServiceVo> haWorkServices = dbHaWorkServices.stream().map(dbHaWorkQueueVo1 -> {
            HaWorkServiceVo haWorkQueueVo1 = new HaWorkServiceVo();
            BeanUtils.copyProperties(dbHaWorkQueueVo1, haWorkQueueVo1);
            return haWorkQueueVo1;
        }).collect(Collectors.toList());
        pageResult.setData(haWorkServices);
        return pageResult;
    }


    /**
     * @description: 根据身份证号码获取用户的帮办队列记录
     * @author: zhaobf
     * @date: 2022-11-15 9:38
     */
    public PageResult<HaWorkServiceVo> queryCompanyHistoryList(HaCompanyHistroyRequestData hacompReqData) {

        //分页参数
        if (null == hacompReqData.getPageNum() || hacompReqData.getPageNum() <= 0) {
            hacompReqData.setPageNum(0)  ;
        }
        if (null == hacompReqData.getPageSize() || hacompReqData.getPageSize()  <= 0) {
            hacompReqData.setPageNum(10);
        }
        DbHaCompanyHistroyRequestData dbHaCompReqData = new DbHaCompanyHistroyRequestData();
        BeanUtils.copyProperties(hacompReqData, dbHaCompReqData);
        PageHelper.startPage(hacompReqData.getPageNum(), hacompReqData.getPageSize() );
        Page<DbHaWorkServiceVo> dbHaWorkServices = (Page<DbHaWorkServiceVo>) dbHaWorkServiceMapper.queryHelpCompanyHistoryCaseList(dbHaCompReqData);
        PageResult<HaWorkServiceVo> pageResult = new PageResult<>(dbHaWorkServices.getPageNum(), dbHaWorkServices.getPageSize(), dbHaWorkServices.getTotal());
        List<HaWorkServiceVo> haWorkServices = dbHaWorkServices.stream().map(dbHaWorkQueueVo1 -> {
            HaWorkServiceVo haWorkQueueVo1 = new HaWorkServiceVo();
            BeanUtils.copyProperties(dbHaWorkQueueVo1, haWorkQueueVo1);
            return haWorkQueueVo1;
        }).collect(Collectors.toList());
        pageResult.setData(haWorkServices);
        return pageResult;
    }

    /**
     * @description: 获取全部的办事队列
     * @author: kangax
     * @date: 2022-08-17 14:20
     */
    public PageResult<HaWorkQueueResponseData3> queueServiceListWithPage(String queueId,String serviceStatus, Integer pageNum, Integer pageSize) {
        //获取当前登录用户信息
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        DbHaWorkQueueVo dbHaWorkQueueVo = new DbHaWorkQueueVo();
        //当前帮代办人员id作为查询条件
//        dbHaWorkQueueVo.setServiceWorkUserId(currentHaLoginUserInfo.getId());
        dbHaWorkQueueVo.setServiceStatus(serviceStatus);
        if(StringUtil.isNotEmpty(queueId)) dbHaWorkQueueVo.setId(Long.valueOf(queueId));
        //查询
        Page<DbHaWorkQueueVo> dbHaWorkQueueVoList = (Page<DbHaWorkQueueVo>) dbHaWorkQueueMapper.queueServiceList(dbHaWorkQueueVo);
        PageResult<HaWorkQueueResponseData3> pageResult = new PageResult<>(dbHaWorkQueueVoList.getPageNum(), dbHaWorkQueueVoList.getPageSize(), dbHaWorkQueueVoList.getTotal());
        List<HaWorkQueueResponseData3> haWorkQueueVos = dbHaWorkQueueVoList.stream().map(dbHaWorkQueueVo1 -> {
            HaWorkQueueResponseData3 haWorkQueueVo1 = new HaWorkQueueResponseData3();
            BeanUtils.copyProperties(dbHaWorkQueueVo1, haWorkQueueVo1);
            return haWorkQueueVo1;
        }).collect(Collectors.toList());
        pageResult.setData(haWorkQueueVos);
        return pageResult;
    }
    /**
     * @description: 取号分配帮代办人员信息接口
     * @param requestData 扫码办事请求参数
     * @return: HaScanHelpInfoResponseData 请求响应类
     * @author: kangax
     * @date: 2022-07-28 18:12
     */
    public List<HaTakeNumHelpResponseData> takeNumHelpInfoFillIn(HaTakeNumHelpRequestData requestData) throws Exception {
        List<HaTakeNumHelpResponseData> listResponseData = new ArrayList();
        //扫码请求响应类
        HaTakeNumHelpResponseData responseData = new HaTakeNumHelpResponseData();
        //2-预约取号
        if ("2".equals(requestData.getTakeNumberType())) {
            String[] workUserId = requestData.getWorkUserIds().split(",");
            String[] appoIds = requestData.getAppointmentIds().split(",");
            List<Long>workUserIdList= new ArrayList<>();
            for (String s : workUserId) { //将String数组转成long集合
                workUserIdList.add(StringUtil.toLong(s));
            }
            for (int i = 0; i < workUserIdList.size() ; i++) {
                HaAppointment byId = haAppointmentManager.getById(Long.valueOf(appoIds[i]));
                //页面没刷新时，可能会重复提交取号，如果已经预约取号，则跳过
                if(byId.getAppointmentStatus().equals("2")){
                    continue;
                }
                Long aLong=workUserIdList.get(i);
                Integer windowsNumbe = this.getTaskNumber();//调用生成排队号方法
                responseData = this.insertQueue(requestData, String.valueOf(windowsNumbe), aLong);
                responseData.setQueueStatus("2");
//                responseData.setWindowsNumber(String.valueOf(windowsNumbe));
                HaWorkUserVo haWorkUserVo = new HaWorkUserVo();
                DbHaWorkUserVo helpWorkUserById = haWorkUserLoginManager.loginByAccounAndId(null, aLong);
                BeanUtils.copyProperties(helpWorkUserById, haWorkUserVo);

                //等待中的人数
                int waitNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatus(aLong, Constants.WAIT, DateUtil.getBeginADay(), DateUtil.getEndADay());
                //服务中的人数
                int serviceIngNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatus(aLong, Constants.SERVICE_ING, DateUtil.getBeginADay(), DateUtil.getEndADay());
                haWorkUserVo.setWaitingNum(waitNum);
                haWorkUserVo.setInServiceNum(serviceIngNum);
                responseData.setHaWorkUser(haWorkUserVo);
                responseData.setWindowsNumber("YZX"+String.format("%4d", windowsNumbe).replace(" ", "0"));

                listResponseData.add(responseData);
//                HaAppointment byId = haAppointmentManager.getById(Long.valueOf(appoIds[i]));
                byId.setAppointmentStatus("2");
                haAppointmentManager.save(byId);


//                DbHaWorkUser dbHaWorkUser = dbThaWorkUserMapper.queryById(aLong);
                String message = requestData.getName()+"您好，您的帮办取号号票为"+responseData.getWindowsNumber()+"，" +
                        "请前往"+haWorkUserVo.getServicePostion()+"，"+haWorkUserVo.getLoginLocationName()+"进行等待，" +
                        "分配的帮办人员为"+haWorkUserVo.getGroupName()+":"+haWorkUserVo.getName()+"，" +
                        "前方等待人数为"+waitNum+"人，" +
                        "预计等待时间"+haWorkUserVo.getEsuimateTime()+"。";
                SendHPSmsUtil.sendHPSms("取号机取号通知",requestData.getPhone(),message, String.valueOf(aLong),haWorkUserVo.getName());


            }
        }
        //1-扫码取号 或 3-普通取号 4 -手机号取号 5-视频咨询取号
        else {
            Integer windowsNumbe = this.getTaskNumber();//调用生成排队号方法
            Date beginADay = DateUtil.getNYR();
            //结果为“0”是上午 结果为“1”是下午
            int index = new GregorianCalendar().get(GregorianCalendar.AM_PM);
            HaWorkUserVo chooseWorkUser =new HaWorkUserVo();
            //指定帮办人员
            if(!requestData.getWorkUserIds().isEmpty()){
                chooseWorkUser=this.getHaWorkUserVo(Long.valueOf(requestData.getWorkUserIds()));
            }else{
                //随机分配  不分配给组长
                //获取当前选择的组别的所有的当天在上班的帮办人员
                List<HaWorkUserScheduleVo> haWorkUserScheduleVos = haWorkUserScheduleManager.queryListByDayAndGroupidAndScheduleStatus(beginADay,requestData.getGroupId(),requestData.getHaType(), String.valueOf(index));
                if(haWorkUserScheduleVos.size()==0){
                    return listResponseData;
                }
                //获取当前选择的组别的所有的当天在上班的帮办人员中的在线人员
                List<HaWorkUserScheduleVo> collect = haWorkUserScheduleVos.stream().filter(x -> !"1".equals((x.getStatus()))).collect(Collectors.toList());
                if(collect.size()==0){
                    return listResponseData;
                }
                //封装所有的帮办人员等待人数，服务人数到vo
                List<HaWorkUserVo> haWorkUserVos = getWaitAndserviceIngNum(collect);
                //获取最少的等待和服务中的人数,作为随机分配的人员
                Optional<HaWorkUserVo> min = haWorkUserVos.stream().min(Comparator.comparing(e -> (e.getWaitingNum() + e.getInServiceNum())));
                if(min.isPresent()){
                    chooseWorkUser=min.get();
                }else{
                    return listResponseData;
                }
            }
            //新建队列
            responseData = this.insertQueue(requestData, String.valueOf(windowsNumbe), chooseWorkUser.getId());
            responseData.setHaWorkUser(chooseWorkUser);
            responseData.setQueueStatus("2");
            responseData.setWindowsNumber("ZX"+String.format("%4d", windowsNumbe).replace(" ", "0"));
            listResponseData.add(responseData);
            if ("5".equals(requestData.getTakeNumberType())) {
                //1.创建视频咨询房间
                HaVideoRoom haVideoRoom = new HaVideoRoom();
                haVideoRoom.setQueueOid(responseData.getId());
                haVideoRoom.setRoomType(ROOM_TYPE_JBR);
                haVideoRoom.setSponsorName(responseData.getName());
                //haVideoRoom.setSponsorId(); //先创建房间，没有房间出入记录表
                haVideoRoom.setSponsorDate(String.valueOf(new Date()));
                String roomNum = null;
                boolean fla = true;
                while (fla) {
                    String roomNumber = CaRandom.GetRandomString(10);
                    //查询是否有重复房间号
                    HaVideoRoom haVideoRoomByqueueId = haVideoConsultationManager.getHaVideoRoomByNum(roomNumber);
                    if (haVideoRoomByqueueId == null) {
                        roomNum = roomNumber;
                        fla = false;
                    }
                }
                haVideoRoom.setRoomNumber(roomNum); //房间号
                haVideoRoom.setRoomStatus(ROOM_STATUS_DD);
                haVideoRoom.setPeakNum("0");
                haVideoRoom.setRoomNum("0");
                haVideoRoom.setCreateBy(responseData.getName());
                haVideoRoom.setCreateDate(new Date());
                HaVideoRoom result = haVideoConsultationManager.saveOrUpdateHaVideoRoom(haVideoRoom);
                // 2视频咨询取号船舰房间出入记录
                HaVideoRoom haVideoRoomByqueueId = haVideoConsultationManager.getHaVideoRoomByqueueId(responseData.getId());
                HaVideoAccess haVideoAccess = new HaVideoAccess();
                haVideoAccess.setRoomOid(result.getId());
                haVideoAccess.setWorkUserId(responseData.getHaWorkUser().getId());
                Integer videoUserNum = haVideoConsultationManager.getVideoUserNum();
                haVideoAccess.setVideoNum(Long.valueOf(videoUserNum));
                haVideoAccess.setUserName(responseData.getName());
                haVideoAccess.setSig(CalSig.genSign(videoUserNum + "0", roomNum, 86400));
                haVideoAccess.setUserType(VIDEO_USER_PEOPLE);
                haVideoAccess.setExistStatus(VIDEO_ROOM_THROUGH);
                haVideoAccess.setInDate(new Date());
                haVideoAccess.setCreateBy(responseData.getName());
                haVideoAccess.setCreateDate(new Date());
                haVideoAccess.setCallUserId(responseData.getHaWorkUser().getId());
                haVideoAccess.setCallUserName(responseData.getHaWorkUser().getName());
                HaVideoAccess haVideoAccess1 = haVideoConsultationManager.saveOrUpdateHaVideoAccess(haVideoAccess);
                responseData.setHaVideoAccess(haVideoAccess1);
                //3重新插入发起人id
                result.setSponsorId(String.valueOf(haVideoAccess1.getId()));
                result.setRoomName(haVideoAccess1.getId());//房间名称
                dbHaVideoRoomMapper.addNumbById(result.getId()); //加入房间，正在房间人数和房间最大人数加一
                HaVideoRoom UpdataResult = haVideoConsultationManager.saveOrUpdateHaVideoRoom(result);
                //4.封装返回实体类
                CreateRoomVo createRoomVo = new CreateRoomVo();
                createRoomVo.setQueueId(UpdataResult.getQueueOid());
                createRoomVo.setRoomId(UpdataResult.getId());
                createRoomVo.setRoomNumber(UpdataResult.getRoomNumber());
                createRoomVo.setRoomName(UpdataResult.getRoomName());
                createRoomVo.setRoomStatus(UpdataResult.getRoomStatus());
                createRoomVo.setRoomNum(UpdataResult.getRoomNum());
                createRoomVo.setWorkNumber(responseData.getHaWorkUser().getWorkNumber());
                createRoomVo.setWorkUserId(responseData.getHaWorkUser().getId());
                createRoomVo.setWorlUserName(responseData.getHaWorkUser().getName());
                createRoomVo.setWindowsNumber(responseData.getWindowsNumber());
                createRoomVo.setSig(haVideoAccess1.getSig());
                createRoomVo.setAppId(CalSig.getAppid());//固定值
                // 视频用户id加0
                createRoomVo.setVideoNum(Long.valueOf(haVideoAccess1.getVideoNum() + "0"));
                createRoomVo.setAccessId(haVideoAccess1.getId());
                responseData.setCreateRoomVo(createRoomVo);
            }
            String message = requestData.getName()+"您好，您的帮办取号号票为"+responseData.getWindowsNumber()+"，" +
                    "请前往"+chooseWorkUser.getServicePostion()+","+chooseWorkUser.getLoginLocationName()+"进行等待，" +
                    "分配的帮办人员为"+chooseWorkUser.getGroupName()+":"+chooseWorkUser.getName()+"，" +
                    "前方等待人数为"+chooseWorkUser.getWaitingNum()+"人，" +
                    "预计等待时间"+(chooseWorkUser.getWaitingNum()*chooseWorkUser.getAvgServiceTime())+"分钟。";
            SendHPSmsUtil.sendHPSms("取号机取号通知",requestData.getPhone(),message, String.valueOf(chooseWorkUser.getId()),chooseWorkUser.getName());


        }
        return listResponseData;
    }
    //封装所有的帮办人员等待，服务人数到vo
    public List<HaWorkUserVo> getWaitAndserviceIngNum(List<HaWorkUserScheduleVo> haWorkUserScheduleVos) {
        List<HaWorkUserVo> haWorkUserVos = new ArrayList<>();
        for (HaWorkUserScheduleVo haWorkUserScheduleVo : haWorkUserScheduleVos) {
            HaWorkUserVo haWorkUserVo = getHaWorkUserVo(haWorkUserScheduleVo.getWorkUserId());
            if (haWorkUserVo == null) continue;
            //随机分配不分配组长
            if(GROUP_LEADER.equals(haWorkUserVo.getGroupPost()) ){
                continue;
            }
            if(TAKE_NUM_NO.equals(haWorkUserVo.getTakeNumStatus()) ){
                continue;
            }
            haWorkUserVos.add(haWorkUserVo);
        }
        return haWorkUserVos;
    }
    //获取指定用户id的HaWorkUserVo封装类
    public HaWorkUserVo getHaWorkUserVo(Long workUserId) {
        HaWorkUserVo haWorkUserVo = new HaWorkUserVo();
        DbHaWorkUserVo helpWorkUserById = haWorkUserLoginManager.loginByAccounAndId(null, workUserId);
        if(helpWorkUserById==null){
            return null;
        }

        BeanUtils.copyProperties(helpWorkUserById, haWorkUserVo);
        //等待中的人数
        int waitNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatus(workUserId, Constants.WAIT, DateUtil.getBeginADay(), DateUtil.getEndADay());
        //服务中的人数
        int serviceIngNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatus(workUserId, Constants.SERVICE_ING, DateUtil.getBeginADay(), DateUtil.getEndADay());
        haWorkUserVo.setWaitingNum(waitNum);
        haWorkUserVo.setInServiceNum(serviceIngNum);
        return haWorkUserVo;
    }


    /** 弃用，改为根据当前的接待人数+服务人数取最小
     * 扫码取号：需要传 分组编号，帮办类型：圆桌帮办、快捷帮办，系统随机分配帮办人员给客户
     * 优先级是1.空闲 2.服务中 3.忙碌 再根据每个状态中，服务人数最少的去分配

     Map<Long, Integer> mapStatus2 = new HashMap<>(); //状态为忙碌的集合
     Map<Long, Integer> mapStatus3 = new HashMap<>();//状态为空闲的集合
     Map<Long, Integer> mapStatus4 = new HashMap<>();//状态为服务中的集合
     Map<String, Map<Long, Integer>> mapList = new HashMap<>();
     //查询状态为除了1.离线，服务人数最小的帮办人员集合
     List<HaWorkUser> haWorkUser = userServiceManager.getMinMaxServiceNumSM(requestData.getGroupId(),requestData.getHaType());
     if(haWorkUser.size()==0){
     return listResponseData;
     }
     for (HaWorkUser haWorkUser1 : haWorkUser) {
     switch (haWorkUser1.getStatus()) {
     //"1-离线、2-忙碌、3-空闲、4-服务中")
     case "2":
     mapStatus2.put(haWorkUser1.getId(), haWorkUser1.getMaxServiceNum());
     break;
     case "3":
     mapStatus3.put(haWorkUser1.getId(), haWorkUser1.getMaxServiceNum());
     break;
     case "4":
     mapStatus4.put(haWorkUser1.getId(), haWorkUser1.getMaxServiceNum());
     break;
     default:
     break;
     }
     }
     mapList.put("忙碌", mapStatus2);
     mapList.put("空闲", mapStatus3);
     mapList.put("服务中", mapStatus4);

     if (mapList.get("空闲").size() > 0) {
     responseData = this.getDistributeUser(mapList.get("空闲"), String.valueOf(windowsNumbe), requestData);
     responseData.setQueueStatus("2");
     responseData.setWindowsNumber(String.valueOf(windowsNumbe));
     } else if (mapList.get("服务中").size() > 0) {
     responseData = this.getDistributeUser(mapList.get("服务中"), String.valueOf(windowsNumbe), requestData);
     responseData.setQueueStatus("2");
     responseData.setWindowsNumber(String.valueOf(windowsNumbe));
     } else if (mapList.get("忙碌").size() > 0) {
     responseData = this.getDistributeUser(mapList.get("忙碌"), String.valueOf(windowsNumbe), requestData);
     responseData.setQueueStatus("2");
     responseData.setWindowsNumber(String.valueOf(windowsNumbe));
     }
     listResponseData.add(responseData);
     }
     */


    /**
     * 随机分配
     * @param map
     * @param windowsNumbe
     * @param requestData
     * @throws ServiceException
     */
    public HaTakeNumHelpResponseData  getDistributeUser(Map<Long, Integer> map, String windowsNumbe,HaTakeNumHelpRequestData requestData) throws ServiceException{
        HaTakeNumHelpResponseData responseData =null;
        //取Map中Value最小值
        Integer minId = getMinValue(map);
        List<Long> listId = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : map.entrySet()) {
            if (entry.getValue() == minId) {
                listId.add(entry.getKey());
            }
        }
        //随机取出最小id集合中的一个
        if (listId.size() > 0) {
            Long userId = getRandomListId(listId);
            responseData = this.insertQueue(requestData,windowsNumbe, userId);
            HaWorkUserVo haWorkUserVo = new HaWorkUserVo();
            DbHaWorkUserVo helpWorkUserById = haWorkUserLoginManager.loginByAccounAndId(null,userId);
            org.springframework.beans.BeanUtils.copyProperties(helpWorkUserById, haWorkUserVo);
            //等待中的人数
            int waitNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatus(userId, Constants.WAIT, DateUtil.getBeginADay(), DateUtil.getEndADay());
            //服务中的人数
            int serviceIngNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatus(userId, Constants.SERVICE_ING, DateUtil.getBeginADay(), DateUtil.getEndADay());
            haWorkUserVo.setWaitingNum(waitNum);
            haWorkUserVo.setInServiceNum(serviceIngNum);
            responseData.setHaWorkUser(haWorkUserVo);
        }
        return responseData;
    }

    /**
     * 插入队列一条信息
     * @param requestData
     * @param windowsNumbe
     * @param userId
     */
    @Transactional(rollbackFor = Exception.class)
    public HaTakeNumHelpResponseData insertQueue(HaTakeNumHelpRequestData requestData,String windowsNumbe,Long userId){
        //创建办事队列
        DbHaWorkQueue dbhaWorkQueue = new DbHaWorkQueue();
        dbhaWorkQueue.setName(requestData.getName());//姓名
        dbhaWorkQueue.setCardType(StringUtil.isNotEmpty(requestData.getCardType())?requestData.getCardType():"1");//设置证件类型
        dbhaWorkQueue.setCardNo(requestData.getCardNo()); //身份证号码
        dbhaWorkQueue.setPhone(requestData.getPhone());//手机号
        dbhaWorkQueue.setCompanyName(requestData.getCompanyName());//企业名称
        dbhaWorkQueue.setCompanyCode(requestData.getCompanyCode()); //统一社会信用代码
        dbhaWorkQueue.setWindowsNumber(windowsNumbe);//排队号
        dbhaWorkQueue.setTakeNumberType(requestData.getTakeNumberType());//取号类型
        dbhaWorkQueue.setQueueStatus("2");//排队状态
        dbhaWorkQueue.setDistributeStatus("2");//设置分配人为系统
        dbhaWorkQueue.setDistributeUserId((long) -1);//分配人
        dbhaWorkQueue.setDistributeTime(DateUtil.getDate());//分配时间
        dbhaWorkQueue.setServiceWorkUserId(userId);//服务工作人员编号
        dbhaWorkQueue.setServiceStatus(Constants.WAIT);//服务状态
        dbhaWorkQueue.setCreateBy("-1");//设置创建人为-1 因为是办事人插入数据
        dbhaWorkQueue.setCreateDate(new Date());//设置创建时间
        dbhaWorkQueue.setDetectsServiceTime(new Date());//设置检测服务时间
        dbHaWorkQueueMapper.insert(dbhaWorkQueue);
        HaTakeNumHelpResponseData responseData = new HaTakeNumHelpResponseData();
        responseData.setId(dbhaWorkQueue.getId());
        responseData.setName(dbhaWorkQueue.getName());
        responseData.setCardType(dbhaWorkQueue.getCardType());
        responseData.setCardNo(dbhaWorkQueue.getCardNo());
        responseData.setPhone(dbhaWorkQueue.getPhone());
        responseData.setCompanyName(dbhaWorkQueue.getCompanyName());
        responseData.setCompanyCode(dbhaWorkQueue.getCompanyCode());
        responseData.setTakeNumberType(dbhaWorkQueue.getTakeNumberType());
        return responseData;
    }

    //取Map中Value最小值
    public static Integer getMinValue(Map<Long, Integer> map) {
        Collection<Integer> c = map.values();
        Object[] obj = c.toArray();
        Arrays.sort(obj);
        //最小value
        Integer minValue = (Integer) obj[0];
        return minValue;
    }

    //随机取出同一个状态下，分配人数最小相同的id集合中的一个
    public static Long getRandomListId(List<Long> list) {
        Random random = new Random();
        int num = random.nextInt(list.size());
        return list.get(num);
    }

    /**
     * @description: 取号
     * @author: Wangyh
     * @Date: 2022/8/29 21:10
     * @return 返回取号，号码
     */
    public Integer getTaskNumber() {
        log.info("取号");
        Map<String,Object> map = new HashMap<>();
        //获取当前系统时间,年月日
        String data = DateUtil.getDateAndTime("yyyyMMdd");
        //从redis中获取上一次取号数
        Integer taskNumber = (Integer) redisTemplate.opsForValue().get(RedisConstants.TASK_NUMBER+data);
        if(taskNumber==null){ //今天第一条数据
            taskNumber = 1;
            //记录当前查询时间存入redis
            redisTemplate.opsForValue().set(RedisConstants.TASK_NUMBER+data, taskNumber, RedisConstants.TASK_NUMBER_TTL, TimeUnit.DAYS);
        }else{
            taskNumber+=1;
            //记录当前查询时间存入redis
            redisTemplate.opsForValue().set(RedisConstants.TASK_NUMBER+data, taskNumber, RedisConstants.TASK_NUMBER_TTL, TimeUnit.DAYS);
        }
        return taskNumber;
    }
}
