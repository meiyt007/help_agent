package com.zfsoft.ha.managers;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.cases.util.DateUtil;
import com.zfsoft.ha.constant.RedisConstants;
import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.HaVideoAccess;
import com.zfsoft.ha.data.HaVideoChatRecord;
import com.zfsoft.ha.data.HaVideoRoom;
import com.zfsoft.ha.data.vo.CreateRoomVo;
import com.zfsoft.ha.data.vo.HaVideoAccessVo;
import com.zfsoft.ha.data.vo.HaVideoChatRecordVo;
import com.zfsoft.ha.dbaccess.dao.*;
import com.zfsoft.ha.dbaccess.data.*;
import com.zfsoft.ha.dbaccess.data.example.DbHaVideoAccessExample;
import com.zfsoft.ha.dbaccess.data.example.DbHaVideoChatRecordExample;
import com.zfsoft.ha.util.CaRandom;
import com.zfsoft.ha.util.CalSig;
import com.zfsoft.ha.util.HaDockingHolder;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.zfsoft.ha.constant.Constants.*;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2023/3/13 14:14
 */
@Service
@Slf4j
//@Transactional
public class HaVideoConsultationManager {
    @Resource
    private HaWorkQueueManager haWorkQueueManager;
    /**
     * 房间出入记录
     **/
    @Resource
    private DbHaVideoAccessMapper dbHaVideoAccessMapper;

    /**
     * 视频咨询房间
     **/
    @Resource
    private DbHaVideoRoomMapper dbHaVideoRoomMapper;

    /**
     * 聊天记录内容表
     **/
    @Resource
    private DbHaVideoChatRecordMapper dbHaVideoChatRecordMapper;

    /**
     * 队列DB实现接口
     */
    @Resource
    private DbHaWorkQueueMapper dbHaWorkQueueMapper;
    /**
     * redis
     */
    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private DbHaWorkUserMapper dbHaWorkUserMapper;

    @Resource
    private DbHaVideoCloudRecordMapper dbHaVideoCloudRecordMapper;

    @Value("${zfsoft.interWeb.url}")
    private  String interWebUrl;

    @Value("${zfsoft.interWeb.baijiayun.recordStream}")
    private  boolean recordStream;

    @Value("${zfsoft.interWeb.baijiayun.streamMix}")
    private  String streamMix;

    @Value("${zfsoft.interWeb.baijiayun.streamMixStart}")
    private  String streamMixStart;

    @Value("${zfsoft.interWeb.baijiayun.streamMixStop}")
    private  String streamMixStop;

    @Value("${zfsoft.interWeb.baijiayun.streamQuery}")
    private  String streamQuery;

    /*********************************wangyuhang start**********************************/
    /**
     * @param roomId 房间主键
     * @description: 结束视频帮办
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    public ApiResultSet endVideoHelp(String roomId) {
        log.info("结束视频帮办方法，参数roomId：{}", roomId);
        HaLoginUserInfo workUser = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        ApiResultSet apiResultSet = new ApiResultSet();
        //1修改视频咨询表中 ①当前房间人数改为0②更新人，更新时间 ③房间关闭时间 ④房间状态 改为关闭
        int updateRoom = this.updateRoom(Long.valueOf(roomId));
        if (updateRoom != 1) {
            apiResultSet.setCode(500);
            apiResultSet.setMessage("视频咨询接口失败");
            return apiResultSet;
        }
        //2根据房间主键查询房间出入记录表集合
        List<HaVideoAccess> videoAccessList = this.updateRoomAccess(Long.valueOf(roomId));
        //3.队列表：①service_status服务状态 修改为完成 ②service_end_time服务结束时间;最终的服务结束时间 ③service_duration 服务时长
        //这个接口失败有可能是开始时间没有值
//        int updateQueue =this.updateQueue(Long.valueOf(roomId));
//        if(updateQueue !=1){
//            apiResultSet.setCode(500);
//            apiResultSet.setMessage("队列接口失败");
//            return apiResultSet;
//        }
        //3.1.根据房间主键查询房间信息表
        DbHaVideoRoom dbHaVideoRoom = dbHaVideoRoomMapper.selectByPrimaryKey(Long.valueOf(roomId));
        dbHaVideoRoom.setRoomStatus(ROOM_STATUS_GB);
        dbHaVideoRoom.setUpdateBy(workUser!=null?String.valueOf(workUser.getId()):"");
        dbHaVideoRoom.setUpdateDate(new Date());
//        //3.2.根据队列ID查询队列信息
//        DbHaWorkQueue dbHaWorkQueue = dbHaWorkQueueMapper.selectByPrimaryKey(dbHaVideoRoom.getQueueOid());
//        HaWorkServiceRequestData haWorkServiceRequestData = new HaWorkServiceRequestData();
//        haWorkServiceRequestData.setWorkQueueId(dbHaWorkQueue.getId());
//        haWorkServiceRequestData.setNextServiceAdvise("3");
//        haWorkServiceRequestData.setAdviseMemo("视频帮办结束服务");
//        //调用原先的结束本次服务接口
//        haWorkQueueManager.endService(haWorkServiceRequestData);
        String res = videoRecordStop(dbHaVideoRoom.getId(), dbHaVideoRoom.getRoomNumber());
        // 云端录制结束
        if (StrUtil.isNotBlank(res)) {
            log.warn("云端录制：" + res);
        }
        return ApiResultSet.ok("接口调用成功");
    }

    /**
     * 是否有新的消息
     *
     * @return Map<String, Object> 是否有新的消息
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<HaVideoChatRecordVo> isHaveNewMesses(Long roomId,String videoNum) throws ServiceException {
        Map<String, Object> map = new HashMap<>(); //返回map集合
        List<HaVideoChatRecordVo> videoChatRecordList;
        //获取上一次记录的时间
        Date videoLatestTime = (Date) redisTemplate.opsForValue().get(RedisConstants.VIDEO_LATEST_TIME + roomId + videoNum);
        log.info("videoLatestTime:{}", videoLatestTime);
        if (videoLatestTime != null) {
            DbHaVideoChatRecordExample dbHaVideoChatRecordExample = new DbHaVideoChatRecordExample();
            DbHaVideoChatRecordExample.Criteria criteria = dbHaVideoChatRecordExample.createCriteria();
            criteria.andCreateDateGreaterThan(videoLatestTime);
            criteria.andRoomOidEqualTo(roomId);
            dbHaVideoChatRecordExample.setOrderByClause("SEND_DATE asc");
            dbHaVideoChatRecordExample.setOrderByClause("ID asc");
            List<DbHaVideoChatRecord> dbHaVideoChatRecords = dbHaVideoChatRecordMapper.selectByExample(dbHaVideoChatRecordExample);
            //循环遍历
            videoChatRecordList = dbHaVideoChatRecords.stream().map(dbHaVideoChatRecord -> {
                HaVideoChatRecordVo haVideoChatRecord = new HaVideoChatRecordVo();
                BeanUtils.copyProperties(dbHaVideoChatRecord, haVideoChatRecord);
                return haVideoChatRecord;
            }).collect(Collectors.toList());
            //循环遍历
            if (videoChatRecordList.size() > 0) {
                //记录这一次的时间戳
                redisTemplate.opsForValue().set(RedisConstants.VIDEO_LATEST_TIME + roomId + videoNum, DateUtil.getDate(), RedisConstants.LATEST_TTL, TimeUnit.HOURS);
            }
        } else {
            DbHaVideoChatRecordExample dbHaVideoChatRecordExample = new DbHaVideoChatRecordExample();
            DbHaVideoChatRecordExample.Criteria criteria = dbHaVideoChatRecordExample.createCriteria();
            criteria.andRoomOidEqualTo(roomId);
            dbHaVideoChatRecordExample.setOrderByClause("CREATE_DATE desc");
            List<DbHaVideoChatRecord> dbHaVideoChatRecords = dbHaVideoChatRecordMapper.selectByExample(dbHaVideoChatRecordExample);
            //循环遍历
            videoChatRecordList = dbHaVideoChatRecords.stream().map(dbHaVideoChatRecord -> {
                HaVideoChatRecordVo haVideoChatRecord = new HaVideoChatRecordVo();
                BeanUtils.copyProperties(dbHaVideoChatRecord, haVideoChatRecord);
                return haVideoChatRecord;
            }).collect(Collectors.toList());
            if (videoChatRecordList.size() > 0) {
                //记录这一次的时间戳
                redisTemplate.opsForValue().set(RedisConstants.VIDEO_LATEST_TIME + roomId + videoNum, DateUtil.getDate(), RedisConstants.LATEST_TTL, TimeUnit.HOURS);
            }
        }
        return videoChatRecordList;
    }

    /**
     * @return
     * @description: 根据房间主键查询修改队列信息
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public int updateQueue(Long roomId) throws ServiceException {
        //1.根据房间主键查询房间信息表
        DbHaVideoRoom dbHaVideoRoom = dbHaVideoRoomMapper.selectByPrimaryKey(roomId);
        //2.根据队列ID查询队列信息
        DbHaWorkQueue dbHaWorkQueue = dbHaWorkQueueMapper.selectByPrimaryKey(dbHaVideoRoom.getQueueOid());
        dbHaWorkQueue.setServiceStatus("3");
        dbHaWorkQueue.setServiceEndTime(DateUtil.getDate());
        dbHaWorkQueue.setServiceDuration(Math.toIntExact((dbHaWorkQueue.getServiceEndTime().getTime() - dbHaWorkQueue.getServiceBeginTime().getTime()) / (1000 * 60)));
        int index = dbHaWorkQueueMapper.updateByPrimaryKeySelective(dbHaWorkQueue);
        return index;
    }

    /**
     * @return
     * @description: 根据房间主键查询修改房间信息
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public int updateRoom(Long roomId) throws ServiceException {
        //1.根据房间主键查询房间信息表
        DbHaVideoRoom dbHaVideoRoom = dbHaVideoRoomMapper.selectByPrimaryKey(roomId);
        // ①当前房间人数改为0 ②更新人，更新时间 ③房间关闭时间 ④房间状态 改为关闭
        dbHaVideoRoom.setRoomNum("0");
        dbHaVideoRoom.setClosDate(DateUtil.getDate());
        dbHaVideoRoom.setRoomStatus("2"); //房间状态;1-开启状态、2-关闭状态 3-等待中（申请人在创建队列时是等待中)）
//        dbHaVideoRoom.setCreateBy();
        dbHaVideoRoom.setCreateDate(DateUtil.getDate());
        int index = dbHaVideoRoomMapper.updateByPrimaryKeySelective(dbHaVideoRoom);
        return index;
    }

    /**
     * @return
     * @description: 根据房间主键查询修改房间信息
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<HaVideoAccess> updateRoomAccess(Long roomId) throws ServiceException {
        //获取当前用户信息
        HaLoginUserInfo haLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        //1.根据房间主键查询房间出入记录表信息   List<DbHaVideoAccess> selectByExample(DbHaVideoAccessExample example);
        DbHaVideoAccessExample dbHaVideoAccessExample = new DbHaVideoAccessExample();
        DbHaVideoAccessExample.Criteria criteria = dbHaVideoAccessExample.createCriteria();
        criteria.andRoomOidEqualTo(roomId);
        dbHaVideoAccessExample.setOrderByClause("CREATE_DATE desc");
        List<DbHaVideoAccess> haVideoAccessList = dbHaVideoAccessMapper.selectByExample(dbHaVideoAccessExample);
        //循环遍历
        List<HaVideoAccess> haVideoAccesses = haVideoAccessList.stream().map(dbHaVideoAccess -> {
            HaVideoAccess haVideoAccess = new HaVideoAccess();
            dbHaVideoAccess.setExistStatus(VIDEO_ROOM_QUIT);
            dbHaVideoAccess.setOutDate(DateUtil.getDate());
            dbHaVideoAccess.setUpdateBy(haLoginUserInfo!=null ? haLoginUserInfo.getName() : "");
            dbHaVideoAccess.setUpdateDate(DateUtil.getDate());
            dbHaVideoAccessMapper.updateByPrimaryKeySelective(dbHaVideoAccess);
            BeanUtils.copyProperties(dbHaVideoAccess, haVideoAccess);
            return haVideoAccess;
        }).collect(Collectors.toList());
        return haVideoAccesses;
    }

    /*********************************wangyuhang end*********************************************/

    /*********************************zhaobf start*****************************************/

    public HaVideoRoom getHaVideoRoomByqueueId(Long queueId) {
        HaVideoRoom ha = new HaVideoRoom();
        DbHaVideoRoom db = dbHaVideoRoomMapper.selectByQueueId(queueId);
        if (db != null) {
            BeanUtils.copyProperties(db, ha);
        }
        return ha;
    }

    public HaVideoRoom getHaVideoRoomByNum(String roomNumber) {
        HaVideoRoom ha = null;
        DbHaVideoRoom db = dbHaVideoRoomMapper.selectRoomNum(roomNumber);
        if (db != null) {
            ha = new HaVideoRoom();
            BeanUtils.copyProperties(db, ha);
        }
        return ha;
    }

    /**
     * @param haVideoRoom 参数配置实体类
     * @return Map<String, Object> 获取修改或保存视频房间表
     * @description: 参数配置的新增或者修改
     */
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public HaVideoRoom saveOrUpdateHaVideoRoom(HaVideoRoom haVideoRoom) throws Exception {
//        Map map = new HashMap();
        HaVideoRoom result = new HaVideoRoom();
        int index = 0;
        if (haVideoRoom.getId() != null) {
            //修改
            DbHaVideoRoom dbHaVideoRoom = dbHaVideoRoomMapper.queryById(haVideoRoom.getId());
            BeanUtils.copyProperties(haVideoRoom, dbHaVideoRoom);
            dbHaVideoRoom.setUpdateDate(new Date());
            index = dbHaVideoRoomMapper.updateByPrimaryKeySelective(dbHaVideoRoom);
//            map.put("index", index);
//            map.put("type", "1");//type 1代表修改
            BeanUtils.copyProperties(dbHaVideoRoom, result);
        } else {
            DbHaVideoRoom dbHaVideoRoom = new DbHaVideoRoom();
            BeanUtils.copyProperties(haVideoRoom, dbHaVideoRoom);
            dbHaVideoRoom.setCreateDate(new Date());
            dbHaVideoRoom.setUpdateDate(new Date());
            index = dbHaVideoRoomMapper.insert(dbHaVideoRoom);
//            map.put("index", index);
//            map.put("type", "2");//type 1代表新增
            BeanUtils.copyProperties(dbHaVideoRoom, result);
        }
        return result;
    }

    /**
     * @param haVideoAccess 参数配置实体类
     * @return Map<String, Object> 获取修改或保存房间出入记录
     * @description: 参数配置的新增或者修改
     */
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public HaVideoAccess saveOrUpdateHaVideoAccess(HaVideoAccess haVideoAccess) {
//        Map map = new HashMap();
        HaVideoAccess result = new HaVideoAccess();
        int index = 0;
        if (haVideoAccess.getId() != null) {
            //修改
            DbHaVideoAccess dbHaVideoAccess = dbHaVideoAccessMapper.selectByPrimaryKey(haVideoAccess.getId());
            BeanUtils.copyProperties(haVideoAccess, dbHaVideoAccess);
            dbHaVideoAccess.setUpdateDate(new Date());
            index = dbHaVideoAccessMapper.updateByPrimaryKeySelective(dbHaVideoAccess);
//            map.put("index", index);
//            map.put("type", "1");//type 1代表修改
            BeanUtils.copyProperties(dbHaVideoAccess, result);
        } else {
            //新增
            DbHaVideoAccess dbHaVideoAccess = new DbHaVideoAccess();
            BeanUtils.copyProperties(haVideoAccess, dbHaVideoAccess);
            dbHaVideoAccess.setCreateDate(new Date());
            dbHaVideoAccess.setUpdateDate(new Date());
            index = dbHaVideoAccessMapper.insert(dbHaVideoAccess);
//            map.put("index", index);
//            map.put("type", "2");//type 1代表新增
            BeanUtils.copyProperties(dbHaVideoAccess, result);
        }
        return result;
    }

    /**
     * @param haVideoChatRecord 参数配置实体类
     * @return Map<String, Object> 获取修改或保存聊天记录
     * @description: 参数配置的新增或者修改
     */
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public Map<String, Object> saveOrUpdateHaVideoChatRecord(HaVideoChatRecord haVideoChatRecord) {
        Map<String, Object> map = new HashMap();
        int index = 0;
        if (haVideoChatRecord.getId() != null) {
            //修改
            DbHaVideoChatRecord dbHaVideoChatRecord = dbHaVideoChatRecordMapper.selectByPrimaryKey(haVideoChatRecord.getId());
            BeanUtils.copyProperties(haVideoChatRecord, dbHaVideoChatRecord);
            dbHaVideoChatRecord.setUpdateDate(DateUtil.getDate());
            index = dbHaVideoChatRecordMapper.updateByPrimaryKeySelective(dbHaVideoChatRecord);
            map.put("index", index);
            map.put("type", "1");//type 1代表修改
        } else {
            DbHaVideoChatRecord dbHaVideoChatRecord = new DbHaVideoChatRecord();
            BeanUtils.copyProperties(haVideoChatRecord, dbHaVideoChatRecord);
            dbHaVideoChatRecord.setCreateDate(DateUtil.getDate());
            dbHaVideoChatRecord.setUpdateDate(DateUtil.getDate());

            //判断是否是信息确认  确认状态 1-待确认，2-信息无误，3-信息有误
            if (haVideoChatRecord.getContentType().equals("3") ) {
                dbHaVideoChatRecord.setCheckCode("1");
                //存入redis中
//                redisTemplate.opsForValue().set(RedisConstants.ChECK_CODE + String.valueOf(haVideoChatRecord.getId()), haVideoChatRecord.getInformationStatus());
            }
            index = dbHaVideoChatRecordMapper.insert(dbHaVideoChatRecord);
            map.put("index", index);
            map.put("type", "2");//type 1代表新增

        }
        return map;
    }


    /**
     * Description: 获取视频咨询用户编号
     *
     * @author zhaobf
     * date: 2023/3/13 14:31
     * @copyright 版权由上海卓繁信息技术股份有限公司拥有
     */
    public Integer getVideoUserNum() {
        log.info("获取视频咨询用户编号");
        //从redis中获取上一次取号数
        Integer userNum = (Integer) redisTemplate.opsForValue().get(RedisConstants.VIDEO_USER_NUM);
        if (userNum == null) { //今天第一条数据
            userNum = 10000;
            //记录当前查询时间存入redis
            redisTemplate.opsForValue().set(RedisConstants.VIDEO_USER_NUM, userNum);
        } else {
            userNum += 1;
            //记录当前查询时间存入redis
            redisTemplate.opsForValue().set(RedisConstants.VIDEO_USER_NUM, userNum);
        }
        return userNum;
    }


    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    public Map<String, Object> joinRoom(String accessId, String existStatus) {
        Map<String, Object> map = new HashMap<>();
        HaLoginUserInfo workUser = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        DbHaVideoAccess dbHaVideoAccess = dbHaVideoAccessMapper.selectByPrimaryKey(Long.valueOf(accessId));
        if (dbHaVideoAccess == null) {
            map.put("state", false);
            map.put("message", "根据房间出入记录id找不到房间出入记录");
            return map;
        }
        DbHaVideoRoom dbHaVideoRoom = dbHaVideoRoomMapper.queryById(dbHaVideoAccess.getRoomOid());
        if (dbHaVideoRoom == null) {
            map.put("state", false);
            map.put("message", "根据房间id找不到房间");
            return map;
        }
        // 接通操作需要判断房间状态
        if (VIDEO_ROOM_THROUGH.equals(existStatus)) {
            if (ROOM_STATUS_GB.equals(dbHaVideoRoom.getRoomStatus())) {
                map.put("state", false);
                map.put("message", "该房间已关闭，无法进入");
                return map;
            }
            if (VIDEO_ROOM_THROUGH.equals(dbHaVideoAccess.getExistStatus())) {
                map.put("state", false);
                map.put("message", "该用户已经进入房间，请勿重新加入");
                return map;
            }
            List<DbHaVideoAccess> dbHaVideoAccesses = dbHaVideoAccessMapper.selectIsHaveNewCall(workUser.getId(), VIDEO_ROOM_THROUGH, DateUtil.getTodayStartTime(), DateUtil.getEndTime());
            if (CollUtil.isNotEmpty(dbHaVideoAccesses)) {
                map.put("state", false);
                map.put("message", "当前正在进行其他视频帮办，请完成后再接听");
                return map;
            }
        }

        //修改状态从呼叫中改为接通（2）或拒绝（3）
        dbHaVideoAccess.setExistStatus(existStatus);
        dbHaVideoAccessMapper.updateByPrimaryKeySelective(dbHaVideoAccess);

        map.put("state", true);
        map.put("roomNum", dbHaVideoRoom.getRoomNum());
        map.put("roomName", dbHaVideoRoom.getRoomName());
        map.put("sig", dbHaVideoAccess.getSig());
        map.put("videoNum", dbHaVideoAccess.getVideoNum());
        map.put("videoAppId", CalSig.getAppid());
        map.put("queueId", dbHaVideoRoom.getQueueOid());
        map.put("screenVideoNum",dbHaVideoAccess.getVideoNum()+"1");
        try {
            map.put("screenVideoSig",CalSig.genSign(dbHaVideoAccess.getVideoNum() + "1",dbHaVideoRoom.getRoomNumber(),86400));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("屏幕共享参数错误");
        }
        map.put("existStatus", existStatus);
        dbHaVideoRoomMapper.addNumbById(dbHaVideoAccess.getRoomOid());
        return map;
    }

    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    public Map<String, Object> callHelp(HaVideoAccess haVideoAccess) throws Exception {
        Map<String, Object> map = new HashMap<>();
        HaLoginUserInfo workUser = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        DbHaVideoRoom dbHaVideoRoom = dbHaVideoRoomMapper.queryById(haVideoAccess.getRoomOid());
        if (dbHaVideoRoom == null) {
            map.put("state", false);
            map.put("message", "根据房间id找不到房间");
            return map;
        }
        DbHaVideoAccess dbHaVideoAccess = dbHaVideoAccessMapper.selectByRoomIdAndWorkUserId(haVideoAccess.getRoomOid(), haVideoAccess.getWorkUserId(), VIDEO_ROOM_CALL);
        if (dbHaVideoAccess != null) {
            map.put("state", false);
            map.put("message", "当前帮办老师已经在呼叫中");
            return map;
        }
        DbHaVideoAccess dbHaVideoAccess2 = dbHaVideoAccessMapper.selectByRoomIdAndWorkUserId(haVideoAccess.getRoomOid(), haVideoAccess.getWorkUserId(), VIDEO_ROOM_THROUGH);
        if (dbHaVideoAccess2 != null) {
            map.put("state", false);
            map.put("message", "当前帮办老师已经在房间中");
            return map;
        }
        haVideoAccess.setExistStatus(VIDEO_ROOM_CALL);
        haVideoAccess.setInDate(new Date());
        Integer videoUserNum = getVideoUserNum();
        haVideoAccess.setVideoNum(Long.valueOf(videoUserNum));
        // 视频用户sign要后面加0生成
        haVideoAccess.setSig(CalSig.genSign(videoUserNum + "0", dbHaVideoRoom.getRoomNumber(), 86400));
        haVideoAccess.setCallUserId(workUser.getId());
        haVideoAccess.setCallUserName(workUser.getName());
        HaVideoAccess result = saveOrUpdateHaVideoAccess(haVideoAccess);
        map.put("state", true);
        map.put("accessId", result.getId());

        return map;
    }

    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    public Map<String, Object> callWait(String roomId) throws Exception {
        Map<String, Object> map = new HashMap<>(6);
        DbHaVideoRoom dbHaVideoRoom = dbHaVideoRoomMapper.queryById(Long.valueOf(roomId));
        map.put("queueId", dbHaVideoRoom.getQueueOid());
        map.put("roomId", dbHaVideoRoom.getId());
        map.put("roomNumber", dbHaVideoRoom.getRoomNumber());
        map.put("roomStatus", dbHaVideoRoom.getRoomStatus());
        DbHaVideoAccess haVideoAccess = dbHaVideoAccessMapper.selectByPrimaryKey(Long.valueOf(dbHaVideoRoom.getSponsorId()));
        map.put("callUserId", haVideoAccess.getCallUserId());
        List<HaVideoAccessVo> videoAccessVoList = new ArrayList<>();
        List<DbHaVideoAccess> dbHaVideoAccessList = dbHaVideoAccessMapper.selectListByRoomId(Long.valueOf(roomId), dbHaVideoRoom.getUpdateDate());
        dbHaVideoAccessList.forEach(dbHaVideoAccess -> {
            // 呼叫中的人员
            if (VIDEO_ROOM_CALL.equals(dbHaVideoAccess.getExistStatus())) {
                DbHaWorkUser dbHaWorkUser = dbHaWorkUserMapper.queryById(dbHaVideoAccess.getWorkUserId());
                Integer callTimeout = dbHaWorkUser.getCallTimeout();
                // 若是呼叫超时，则更新状态为超时
                if (callTimeout != null && callTimeout != 0 &&
                        cn.hutool.core.date.DateUtil.between(new Date(), dbHaVideoAccess.getCreateDate(), DateUnit.SECOND) > callTimeout) {
                    dbHaVideoAccess.setExistStatus(VIDEO_ROOM_TIMEOUT);
                    dbHaVideoAccessMapper.updateByPrimaryKey(dbHaVideoAccess);
                }
            }
            HaVideoAccessVo haVideoAccessVo = new HaVideoAccessVo();
            BeanUtils.copyProperties(dbHaVideoAccess, haVideoAccessVo);
            videoAccessVoList.add(haVideoAccessVo);
        });
        map.put("accessList", videoAccessVoList);
        return map;
    }

    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    public Map<String, Object> exitRoom(String accessId) {
        Map<String, Object> map = new HashMap<>();
        HaLoginUserInfo workUser = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        String currentUser = HaDockingHolder.getCurrentUser();
        DbHaVideoAccess dbHaVideoAccess = dbHaVideoAccessMapper.selectByPrimaryKey(Long.valueOf(accessId));
        if (dbHaVideoAccess == null) {
            map.put("state", false);
            map.put("message", "根据房间出入记录id找不到房间出入记录");
            return map;
        }
        DbHaVideoRoom dbHaVideoRoom = dbHaVideoRoomMapper.queryById(dbHaVideoAccess.getRoomOid());
        if (dbHaVideoRoom == null) {
            map.put("state", false);
            map.put("message", "根据房间id找不到房间");
            return map;
        }
        if (VIDEO_ROOM_QUIT.equals(dbHaVideoAccess.getExistStatus())) {
            map.put("state", false);
            map.put("message", "该用户已经退出房间");
            return map;
        }
        //修改状态从呼叫中改为退出
        dbHaVideoAccess.setExistStatus(VIDEO_ROOM_QUIT);
        dbHaVideoAccess.setOutDate(new Date());
        dbHaVideoAccess.setUpdateBy(workUser!=null?workUser.getName():currentUser);
        dbHaVideoAccess.setUpdateDate(new Date());
        dbHaVideoAccessMapper.updateByPrimaryKeySelective(dbHaVideoAccess);

        map.put("state", true);
        map.put("existStatus", dbHaVideoAccess.getExistStatus());
        dbHaVideoRoomMapper.subNumbById(dbHaVideoAccess.getRoomOid());
        return map;

    }

    public Map<String, Object> isHaveNewCall() {
        Map<String, Object> map = new HashMap<>();
        HaLoginUserInfo workUser = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        // 人员在房间的状态 1-呼叫中、2-接通、3-拒绝、4-退出、5-超时
        String existStatus = "1,2";
        List<DbHaVideoAccess> dbHaVideoAccesses = dbHaVideoAccessMapper.selectIsHaveNewCall(workUser.getId(), existStatus, DateUtil.getTodayStartTime(), DateUtil.getEndTime());
        List<HaVideoAccessVo> callList = new ArrayList<>(2);
        List<HaVideoAccessVo> touchList = new ArrayList<>(2);
        dbHaVideoAccesses.forEach(dbHaVideoAccess -> {
            HaVideoAccessVo haVideoAccessVo = new HaVideoAccessVo();
            BeanUtils.copyProperties(dbHaVideoAccess, haVideoAccessVo);
            haVideoAccessVo.setVideoAppId(CalSig.getAppid());
            haVideoAccessVo.setVideoNum(Long.valueOf(dbHaVideoAccess.getVideoNum() + "0"));
            haVideoAccessVo.setScreenVideoNum(Long.valueOf(dbHaVideoAccess.getVideoNum() + "1"));

            DbHaVideoRoom dbHaVideoRoom = dbHaVideoRoomMapper.queryById(dbHaVideoAccess.getRoomOid());
            if (dbHaVideoRoom != null) {
                haVideoAccessVo.setRoomNumber(dbHaVideoRoom.getRoomNumber());
                haVideoAccessVo.setQueueId(dbHaVideoRoom.getQueueOid());
                try {
                    haVideoAccessVo.setScreenVideoSig(CalSig.genSign(dbHaVideoAccess.getVideoNum() + "1",dbHaVideoRoom.getRoomNumber(),86400));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ServiceException("屏幕共享参数错误");
                }
            }
            // 查询房间未关闭状态的
            if (dbHaVideoRoom != null && !ROOM_STATUS_GB.equals(dbHaVideoRoom.getRoomStatus())) {
                if (VIDEO_ROOM_CALL.equals(haVideoAccessVo.getExistStatus())) {
                    callList.add(haVideoAccessVo);
                } else {
                    touchList.add(haVideoAccessVo);
                }
            }
        });
        map.put("accessCallList", callList);
        map.put("accessTouchList", touchList);
        return map;
    }

    /**
     * 是否存在已接通的视频帮办信息
     * @return
     * @throws ServiceException
     */
    public Map<String, Object> isHaveExistCall() throws ServiceException{
        Map<String, Object> map = new HashMap<>();
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        List<DbHaVideoAccess> dbHaVideoAccesses = dbHaVideoAccessMapper.selectIsHaveNewCall(currentHaLoginUserInfo.getId(), VIDEO_ROOM_THROUGH, DateUtil.getTodayStartTime(), DateUtil.getEndTime());
        if (dbHaVideoAccesses == null || dbHaVideoAccesses.size() == 0) {
            map.put("haveExistCall", "0");
        } else {
            map.put("haveExistCall", "1");
            List<HaVideoAccessVo> list = new ArrayList<>();
            dbHaVideoAccesses.forEach(dbHaVideoAccess -> {
                HaVideoAccessVo haVideoAccessVo = new HaVideoAccessVo();
                BeanUtils.copyProperties(dbHaVideoAccess, haVideoAccessVo);
                haVideoAccessVo.setVideoAppId(CalSig.getAppid());
                DbHaVideoRoom dbHaVideoRoom = dbHaVideoRoomMapper.queryById(dbHaVideoAccess.getRoomOid());
                if (dbHaVideoRoom != null) {
                    haVideoAccessVo.setRoomNumber(dbHaVideoRoom.getRoomNumber());
                    haVideoAccessVo.setQueueId(dbHaVideoRoom.getQueueOid());
                }
                list.add(haVideoAccessVo);
            });
            map.put("accessList", list);
        }
        return map;
    }

    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    public Map<String, Object> endCall(String accessId) {
        Map<String, Object> map = new HashMap<>();
        HaLoginUserInfo workUser = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        //修改房间出入记录数据
        DbHaVideoAccess dbHaVideoAccess = dbHaVideoAccessMapper.selectByPrimaryKey(Long.valueOf(accessId));
        if (dbHaVideoAccess == null) {
            map.put("state", false);
            map.put("message", "根据房间出入记录id找不到房间出入记录");
            return map;
        }
        dbHaVideoAccess.setExistStatus(VIDEO_ROOM_QUIT);
        dbHaVideoAccess.setOutDate(new Date());
        dbHaVideoAccess.setUpdateBy(workUser.getName());
        dbHaVideoAccess.setUpdateDate(new Date());
        dbHaVideoAccessMapper.updateByPrimaryKeySelective(dbHaVideoAccess);
        //修改房间数据
        DbHaVideoRoom dbHaVideoRoom = dbHaVideoRoomMapper.queryById(dbHaVideoAccess.getRoomOid());
        if (dbHaVideoRoom == null) {
            map.put("state", false);
            map.put("message", "根据房间id找不到房间");
            return map;
        }
        dbHaVideoRoom.setRoomNum("0");
        dbHaVideoRoom.setClosDate(new Date());
        dbHaVideoRoom.setRoomStatus("2"); //房间状态;1-开启状态、2-关闭状态 3-等待中（申请人在创建队列时是等待中)）
//        dbHaVideoRoom.setCreateBy();
        dbHaVideoRoom.setCreateDate(DateUtil.getDate());
        dbHaVideoRoomMapper.updateByPrimaryKeySelective(dbHaVideoRoom);
        //修改队列数据
        DbHaWorkQueue dbHaWorkQueue = dbHaWorkQueueMapper.selectByPrimaryKey(dbHaVideoRoom.getQueueOid());
        dbHaWorkQueue.setServiceStatus("3");
        dbHaWorkQueueMapper.updateByPrimaryKeySelective(dbHaWorkQueue);
        map.put("state", true);
        return map;
    }

    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    public ApiResultSet checkCodeResult(String recordId, String checkCode) {

        DbHaVideoChatRecord dbHaVideoChatRecord = dbHaVideoChatRecordMapper.selectByPrimaryKey(Long.valueOf(recordId));
        if (dbHaVideoChatRecord == null) {
            return new ApiResultSet(ApiResultSet.DIRTY_DATA_TITLE, "根据聊天主键找不到聊天记录");
        }
        dbHaVideoChatRecord.setCheckCode(checkCode);
        dbHaVideoChatRecordMapper.updateByPrimaryKeySelective(dbHaVideoChatRecord);
        return ApiResultSet.ok("请求成功", "");
    }

    /*********************************zhaobf end*****************************************/

    /**
     * 帮办老师发起帮办,创建房间，并呼叫组长或委办局老师
     * @param workUserId 组长或委办局老师的主键
     * @param queueId 队列主键
     * @param userName 用户名称
     * @param userType 用户类型 1-帮办人员，2-帮办组长，3-委办局老师，4-办事人
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public CreateRoomVo startHelp(Long workUserId, Long queueId, String userName, String userType) throws Exception {
        HaLoginUserInfo workUser = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        // 检验当天是否有开启状态的房间
        List<DbHaVideoRoom> dbHaVideoRooms = dbHaVideoRoomMapper.selectBySponsorAndStatus(workUser.getId().toString(),
                ROOM_STATUS_KQ, VIDEO_ROOM_THROUGH, DateUtil.getBeginADay(), DateUtil.getEndADay());
        if (CollUtil.isNotEmpty(dbHaVideoRooms)) {
            throw new ServiceException("当前存在已开启的视频帮办，请完成后再呼叫");
        }
        //1.创建视频咨询房间
        DbHaVideoRoom dbHaVideoRoom = dbHaVideoRoomMapper.selectByQueueId(queueId);
        HaVideoRoom haVideoRoom = new HaVideoRoom();
        String roomNumber = null;
        if (dbHaVideoRoom == null) {
            haVideoRoom.setQueueOid(queueId);
            haVideoRoom.setRoomType(ROOM_TYPE_BBRY);
            haVideoRoom.setSponsorName(workUser.getName());
            //haVideoRoom.setSponsorId(); //先创建房间，没有房间出入记录表
            haVideoRoom.setSponsorDate(String.valueOf(new Date()));
            boolean fla = true;
            while(fla){
                String rm = CaRandom.GetRandomString(10);
                //查询是否有重复房间号
                HaVideoRoom haVideoRoomByqueueId = getHaVideoRoomByNum(rm);
                if(haVideoRoomByqueueId ==null){
                    roomNumber = rm;
                    fla =false;
                }
            }
            haVideoRoom.setRoomNumber(roomNumber); //房间号
            haVideoRoom.setRoomStatus(ROOM_STATUS_KQ);
            haVideoRoom.setPeakNum("0");
            haVideoRoom.setRoomNum("0");
            haVideoRoom.setCreateBy(workUser.getName());
            haVideoRoom.setCreateDate(new Date());
            haVideoRoom = saveOrUpdateHaVideoRoom(haVideoRoom);
        } else {
            // 房间不为空，则复用之前的房间
            BeanUtils.copyProperties(dbHaVideoRoom, haVideoRoom);
            haVideoRoom.setRoomStatus(ROOM_STATUS_KQ);
            haVideoRoom.setPeakNum("0");
            haVideoRoom.setRoomNum("0");
            haVideoRoom.setUpdateBy(workUser.getName());
            haVideoRoom.setUpdateDate(new Date());
            roomNumber = haVideoRoom.getRoomNumber();
        }
        haVideoRoom = saveOrUpdateHaVideoRoom(haVideoRoom);
        // 2帮办人员创建自己房间出入记录
        HaVideoAccess haVideoAccess = new HaVideoAccess();
        haVideoAccess.setRoomOid(haVideoRoom.getId());
        haVideoAccess.setWorkUserId(workUser.getId());
        Integer videoUserNum = getVideoUserNum();
        haVideoAccess.setVideoNum(Long.valueOf(videoUserNum));
        haVideoAccess.setUserName(workUser.getName());
        // 视频用户sign要后面加0生成
        haVideoAccess.setSig(CalSig.genSign(videoUserNum + "0",roomNumber,86400));
        haVideoAccess.setUserType(VIDEO_USER_WORK);
        haVideoAccess.setExistStatus(VIDEO_ROOM_THROUGH);
        haVideoAccess.setInDate(new Date());
        haVideoAccess.setCreateBy(workUser.getName());
        haVideoAccess.setCreateDate(new Date());
        haVideoAccess.setCallUserId(workUser.getId());
        haVideoAccess.setCallUserName(workUser.getName());
        haVideoAccess = saveOrUpdateHaVideoAccess(haVideoAccess);
        //3重新插入发起人id
        haVideoRoom.setSponsorId(String.valueOf(haVideoAccess.getId()));
        //房间名称
        haVideoRoom.setRoomName(haVideoAccess.getId());
        //加入房间，正在房间人数和房间最大人数加一
        dbHaVideoRoomMapper.addNumbById(haVideoRoom.getId());
        saveOrUpdateHaVideoRoom(haVideoRoom);
        // 4.帮办人员创建组长或委办局老师房间出入记录
        HaVideoAccess haVideoAccess2 = new HaVideoAccess();
        haVideoAccess2.setRoomOid(haVideoRoom.getId());
        haVideoAccess2.setWorkUserId(workUserId);
        videoUserNum = getVideoUserNum();
        haVideoAccess2.setVideoNum(Long.valueOf(videoUserNum));
        haVideoAccess2.setUserName(userName);
        // 视频用户sign要后面加0生成
        haVideoAccess2.setSig(CalSig.genSign(videoUserNum + "0",roomNumber,86400));
        haVideoAccess2.setUserType(userType);
        haVideoAccess2.setExistStatus(VIDEO_ROOM_CALL);
        //haVideoAccess2.setInDate(new Date());
        haVideoAccess2.setCreateBy(workUser.getName());
        haVideoAccess2.setCreateDate(new Date());
        haVideoAccess2.setCallUserId(workUser.getId());
        haVideoAccess2.setCallUserName(workUser.getName());
        saveOrUpdateHaVideoAccess(haVideoAccess2);
        //加入房间，正在房间人数和房间最大人数加一
        dbHaVideoRoomMapper.addNumbById(haVideoRoom.getId());
        //5.封装返回实体类
        CreateRoomVo createRoomVo = new CreateRoomVo();
        createRoomVo.setQueueId(haVideoRoom.getQueueOid());
        createRoomVo.setRoomId(haVideoRoom.getId());
        createRoomVo.setRoomNumber(haVideoRoom.getRoomNumber());
        createRoomVo.setRoomName(haVideoRoom.getRoomName());
        createRoomVo.setRoomStatus(haVideoRoom.getRoomStatus());
        createRoomVo.setRoomNum(haVideoRoom.getRoomNum());
        createRoomVo.setWorkNumber(workUser.getWorkNumber());
        createRoomVo.setWorkUserId(workUser.getId());
        createRoomVo.setWorlUserName(workUser.getName());
        //createRoomVo.setWindowsNumber(workUser.);
        createRoomVo.setAppId(CalSig.getAppid());
        createRoomVo.setAccessId(haVideoAccess.getId());
        // 视频用户id加0，sign要后面加0生成
        createRoomVo.setVideoNum(Long.valueOf(haVideoAccess.getVideoNum() + "0"));
        createRoomVo.setSig(CalSig.genSign(haVideoAccess.getVideoNum() + "0",roomNumber,86400));
        // 屏幕共享用户id加1，sign要后面加1生成
        createRoomVo.setScreenVideoNum(Long.valueOf(haVideoAccess.getVideoNum() + "1"));
        createRoomVo.setScreenVideoSig(CalSig.genSign(haVideoAccess.getVideoNum() + "1",roomNumber,86400));
        // 云端录制开始
        String res = videoRecordStart(haVideoRoom.getId(), haVideoRoom.getRoomNumber(), haVideoAccess.getVideoNum().toString());
        if (StrUtil.isNotBlank(res)) {
            log.warn("云端录制：" + res);
        }
        return createRoomVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public String videoRecordStart(Long roomId, String videoRoomId, String videoUserId) {
        try {
            if (!recordStream) {
                return "云端视频录制功能未开启！";
            }
            HaLoginUserInfo workUser = HaLoginUserHolder.getCurrentHaLoginUserInfo();
            Map<String,Object> mixParam = new HashMap<>(2);
            mixParam.put("roomId", videoRoomId);
            mixParam.put("userId", videoUserId + "0");
            String mixResult = HttpUtil.post(interWebUrl + streamMix, mixParam);
            JSONObject mix = JSON.parseObject(mixResult);
            String taskId;
            if ((Integer)mix.get("code") == 200) {
                JSONObject data = JSON.parseObject(mix.get("data").toString());
                taskId = data.get("task_id").toString();
            } else {
                throw new ServiceException("获取混流录制录制资源错误：" + mix.get("msg"));
            }
            Map<String,Object> startParam = new HashMap<>(2);
            startParam.put("roomId", videoRoomId);
            startParam.put("taskId", taskId);
            String startResult = HttpUtil.post(interWebUrl + streamMixStart, startParam);
            JSONObject start = JSON.parseObject(startResult);
            if ((Integer)start.get("code") != 200) {
                throw new ServiceException("开始混流录制错误：" + start.get("msg"));
            }
            DbHaVideoCloudRecord videoCloudRecord = dbHaVideoCloudRecordMapper.selectByRoomIdAndRoomNumber(roomId, Long.valueOf(videoRoomId));
            if (videoCloudRecord == null) {
                DbHaVideoCloudRecord vcr = new DbHaVideoCloudRecord();
                vcr.setRoomOid(roomId);
                vcr.setRoomNumber(Long.valueOf(videoRoomId));
                vcr.setVideoNum(videoUserId);
                vcr.setTaskId(taskId);
                vcr.setDownloadStatus(0);
                vcr.setStartDate(new Date());
                vcr.setCreateBy(workUser.getName());
                vcr.setCreateDate(new Date());
                dbHaVideoCloudRecordMapper.insert(vcr);
            } else {
                videoCloudRecord.setVideoNum(videoCloudRecord.getVideoNum() + "," + videoUserId);
                videoCloudRecord.setDownloadStatus(0);
                videoCloudRecord.setUpdateBy(workUser.getName());
                videoCloudRecord.setUpdateDate(new Date());
                dbHaVideoCloudRecordMapper.updateByPrimaryKey(videoCloudRecord);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public String videoRecordStop(Long roomId, String videoRoomId) {
        try {
            if (!recordStream) {
                return "云端视频录制功能未开启！";
            }
            HaLoginUserInfo workUser = HaLoginUserHolder.getCurrentHaLoginUserInfo();
            DbHaVideoCloudRecord videoCloudRecord = dbHaVideoCloudRecordMapper.selectByRoomIdAndRoomNumber(roomId, Long.valueOf(videoRoomId));
            if (videoCloudRecord == null) {
                return "未开始混流录制！";
            }
            Map<String,Object> stopParam = new HashMap<>(2);
            stopParam.put("roomId", videoRoomId);
            stopParam.put("taskId", videoCloudRecord.getTaskId());
            String startResult = HttpUtil.post(interWebUrl + streamMixStop, stopParam);
            JSONObject stop = JSON.parseObject(startResult);
            if ((Integer)stop.get("code") != 200) {
                throw new ServiceException("结束混流录制错误：" + stop.get("msg"));
            }
            videoCloudRecord.setStopDate(new Date());
            videoCloudRecord.setUpdateBy(workUser.getName());
            videoCloudRecord.setUpdateDate(new Date());
            dbHaVideoCloudRecordMapper.updateByPrimaryKey(videoCloudRecord);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
        return null;
    }

    public String queryVideoRecord(Long queueId) {
        DbHaVideoRoom videoRoom = dbHaVideoRoomMapper.selectByQueueId(queueId);
        if (videoRoom != null) {
            DbHaVideoCloudRecord videoCloudRecord = dbHaVideoCloudRecordMapper
                    .selectByRoomIdAndRoomNumber(videoRoom.getId(), Long.valueOf(videoRoom.getRoomNumber()));
            if (videoCloudRecord != null && StrUtil.isNotBlank(videoCloudRecord.getDownloadUrl())) {
                return videoCloudRecord.getDownloadUrl();
            }
        }
        return null;
    }
}
