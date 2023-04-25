package com.zfsoft.ha.managers;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.zfsoft.cases.util.BaseStaticParameter;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.cases.util.DateUtil;
import com.zfsoft.ha.constant.Constants;
import com.zfsoft.ha.constant.RedisConstants;
import com.zfsoft.ha.data.*;
import com.zfsoft.ha.data.TripartiteVo.HaWorkUserList;
import com.zfsoft.ha.data.requestData.HaQueueRequestData;
import com.zfsoft.ha.data.responseData.HaWorkQueueResponseData2;
import com.zfsoft.ha.data.vo.HaWorkQueueVo;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkQueueMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaMessageMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkUserMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkUserScheduleMapper;
import com.zfsoft.ha.dbaccess.data.*;
import com.zfsoft.ha.dbaccess.data.example.DbHaWorkQueueExample;
import com.zfsoft.ha.dbaccess.data.example.DbHaWorkUserExample;
import com.zfsoft.ha.dbaccess.data.example.DbHaWorkUserScheduleExample;
import com.zfsoft.ha.dbaccess.data.vo.DbHaWorkQueueVo;
import com.zfsoft.ha.dbaccess.data.vo.DbWorkOrQueuVo;
import com.zfsoft.ha.dbaccess.data.vo.HaWorkQueueResponseVo;
import com.zfsoft.ha.dbaccess.data.vo.DbHaWorkUserVo;
import com.zfsoft.ha.front.controller.QueueController;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description //队列接口实现
 * @Author: Wangyh
 * @Date: 2022/7/20 11:14
 */
@Service
@Slf4j
public class QueueManager {
    /**
     * 队列DB实现接口
     */
    @Resource
    private DbHaWorkQueueMapper dbHaWorkQueueMapper;

    /**
     * 用户接口DB实现接口
     */
    @Resource
    private DbHaWorkUserMapper dbHaWorkUserMapper;

    /**
     * 消息接口DB实现接口
     */
    @Resource
    private DbHaMessageMapper dbHaMessageMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private HaWorkUserServiceManager userServiceManager;

    @Resource
    private DbHaWorkUserScheduleMapper dbHaWorkUserScheduleMapper;

    /**
     * 帮代办评价manager
     */
    @Resource
    private HaEvalManager haEvalManager;
    /**
     * 根据队列Id查询相关信息
     * @param id
     * @return
     * @throws ServiceException
     */
    public DbWorkOrQueuVo queryWorkQueueVo(Long id) throws ServiceException {
        DbWorkOrQueuVo dbWorkOrQueuVo =  dbHaWorkQueueMapper.queryWorkAndQueuByid(id);
        //等待中的人数
        int waitNum = dbHaWorkQueueMapper.countWorkQueueByServiceStatus(dbWorkOrQueuVo.getServiceWorkUserId(), Constants.WAIT, DateUtil.getBeginADay(), DateUtil.getEndADay());
        Integer estimatedWaitingTime = dbWorkOrQueuVo.getAvgServiceTime() * waitNum; //预计等待时长
        dbWorkOrQueuVo.setWaitingNum(waitNum);
        dbWorkOrQueuVo.setEsuimateTime(estimatedWaitingTime);
        return dbWorkOrQueuVo;
    }

    /**
     * 是否有新的办事人员扫码
     *
     * @return Map<String, Object> 查询是否有新的办事人员扫码详细信息
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public Map<String, Object> isHaveNewMesses() throws ServiceException {
        int count = 0;//声明初始化数量
        Map<String, Object> map = new HashMap<>(); //返回map集合
        //获取当前用户信息
        HaLoginUserInfo haLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        //获取上一次记录的时间
        Date latestTime = (Date) redisTemplate.opsForValue().get(RedisConstants.LATEST_TIME);
        log.info("latestTime:{}", latestTime);
        if (latestTime != null) {
            count = dbHaWorkQueueMapper.isHaveNewMessesLatestTime(latestTime, haLoginUserInfo.getId());
            if (count > 0) {
                map.put("haveNewMesses", true);
                //获取上一次记录的时间
                redisTemplate.opsForValue().set(RedisConstants.LATEST_TIME, DateUtil.getDate(), RedisConstants.LATEST_TTL, TimeUnit.HOURS);
            } else {
                map.put("haveNewMesses", false);
            }
        } else {
            count = dbHaWorkQueueMapper.isHaveNewMesses();
            if (count > 0) {
                redisTemplate.opsForValue().set(RedisConstants.LATEST_TIME, DateUtil.getDate(), RedisConstants.LATEST_TTL, TimeUnit.HOURS);
                map.put("haveNewMesses", true);
            } else {
                map.put("haveNewMesses", false);
            }
        }
        return map;
    }

    /**
     * 查询办事队列
     *
     * @param name          姓名
     * @param cardNo        身份证号码
     * @param companyName   企业名称
     * @param queueStatus   排队状态;1-扫码排队中，2-导服已分配
     * @param serviceStatus 服务状态;1-等待服务，2-服务中，3-服务完成
     * @return List<HaWorkQueueVo> 获取当前登录用户查询办事队列列表详情
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<HaWorkQueueVo> queryWorkQueueList(
            String name, String cardNo, String companyName, String queueStatus, String serviceStatus) throws ServiceException {
        List<DbHaWorkQueueVo> dbHaWorkQueueList = dbHaWorkQueueMapper.selectByQueueAndUser(name, cardNo, companyName, queueStatus, serviceStatus);
        //循环遍历将DbHaWorkQueueVo对象list集合复制到HaWorkQueueVo对象list集合中
        List<HaWorkQueueVo> messageList = dbHaWorkQueueList.stream().map(dbHaWorkQueueService -> {
            HaWorkQueueVo haWorkQueueVo = new HaWorkQueueVo();
            BeanUtils.copyProperties(dbHaWorkQueueService, haWorkQueueVo);
            return haWorkQueueVo;
        }).collect(Collectors.toList());
        return messageList;
    }

    /**
     * 查询办事队列
     *
     * @param name          姓名
     * @param cardNo        身份证号码
     * @param companyName   企业名称
     * @param queueStatus   排队状态;1-扫码排队中，2-导服已分配
     * @param serviceStatus 服务状态;1-等待服务，2-服务中，3-服务完成
     * @return List<HaWorkQueueVo> 获取当前登录用户查询办事队列列表详情
     * @author zhaobf
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<HaWorkQueueVo> queryWorkQueueGroupList(
            String name, String cardNo, String companyName, String queueStatus, String serviceStatus) throws ServiceException {
        //获取当前用户信息
        HaLoginUserInfo haLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        List<DbHaWorkQueueVo> dbHaWorkQueueList = dbHaWorkQueueMapper.selectByQueueAndUserGroup(name, cardNo, companyName, queueStatus, serviceStatus, haLoginUserInfo.getId());
        //循环遍历将DbHaWorkQueueVo对象list集合复制到HaWorkQueueVo对象list集合中
        List<HaWorkQueueVo> messageList = dbHaWorkQueueList.stream().map(dbHaWorkQueueService -> {
            HaWorkQueueVo haWorkQueueVo = new HaWorkQueueVo();
            BeanUtils.copyProperties(dbHaWorkQueueService, haWorkQueueVo);
            return haWorkQueueVo;
        }).collect(Collectors.toList());
        return messageList;
    }


    /**
     * @param //Token在header中
     * @return DbHaWorkQueueVo 获取当前登录用统计队列数据详情
     * @description: 统计队列数据
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public DbHaWorkQueueVo getQueueNum() throws ServiceException {
        Date start = DateUtil.getTodayStartTime(); //获取当天零点时间
        Date end = DateUtil.getTodayEndTime(); //获取当天23:59:59秒时间
        DbHaWorkQueueVo dbHaWorkQueueVo = dbHaWorkQueueMapper.selectQueueNums(start, end);
        return dbHaWorkQueueVo;
    }

    /**
     * @return DbHaWorkQueueVo 获取当前登录用统计队列数据详情
     * @description: 统计总服务人数
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public Integer getTotalServiceNum() throws ServiceException {
        //获取当前用户信息
        Integer TotalServiceNum = dbHaWorkQueueMapper.selectTotalServiceNum();
        return TotalServiceNum;
    }
    /**
     * @description:
     * 获取所有的圆桌办事人员名字及其目前所登录的帮办区域
     * @author: Wangyh
     * @Date: 2023/4/7 13:10
     **/
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<HaWorkUserList> getWorkUserListDp() throws ServiceException {
        DbHaWorkUserExample thaWorkUserExample = new DbHaWorkUserExample();
        DbHaWorkUserExample.Criteria criteria = thaWorkUserExample.createCriteria();
                criteria.andUserTypeEqualTo("2"); //用户类型;1-导服人员，2-帮代办人员，3委办局老师
        //  criteria.andHaTypeEqualTo("2"); //'帮代办人员类型;1-快捷帮办，2-圆桌帮办'
                criteria.andStatusNotEqualTo("1"); //状态;1-离线、2-忙碌、3-空闲、4-服务中
                criteria.andDeleteStatusEqualTo("1"); //1-未删除，2-已删除
        thaWorkUserExample.setOrderByClause("CREATE_DATE desc");
        List<DbHaWorkUser> dbUserServices = dbHaWorkUserMapper.selectByExample(thaWorkUserExample);
        List<HaWorkUserList> workUserList = dbUserServices.stream().map(dbUserServic -> {
            HaWorkUserList haWorkUserList = new HaWorkUserList();
            BeanUtils.copyProperties(dbUserServic, haWorkUserList);
            if (StrUtil.isNotEmpty(dbUserServic.getStatus()) && dbUserServic.getStatus().equals(BaseStaticParameter.STATUS_LX)) {
                haWorkUserList.setStatus(BaseStaticParameter.STATUS_LXZW);
            } else if (StrUtil.isNotEmpty(dbUserServic.getStatus()) && dbUserServic.getStatus().equals(BaseStaticParameter.STATUS_ML)) {
                haWorkUserList.setStatus(BaseStaticParameter.STATUS_MLZW);
            } else if (StrUtil.isNotEmpty(dbUserServic.getStatus()) && dbUserServic.getStatus().equals(BaseStaticParameter.STATUS_KX)) {
                haWorkUserList.setStatus(BaseStaticParameter.STATUS_KXZW);
            } else if (StrUtil.isNotEmpty(dbUserServic.getStatus()) && dbUserServic.getStatus().equals(BaseStaticParameter.STATUS_FUWZ)) {
                haWorkUserList.setStatus(BaseStaticParameter.STATUS_FUWZZW);
            }
            BigDecimal evalScore = haEvalManager.getWorkUserEvalScore(dbUserServic.getId());
            BigDecimal b2 = new BigDecimal(5);
            String as = String.valueOf(evalScore.divide(b2,2, BigDecimal.ROUND_HALF_UP));
            DecimalFormat df = new DecimalFormat("0%");
            BigDecimal d=new BigDecimal(String.valueOf(as));
            String percent=df.format(d);
            haWorkUserList.setEvalScore(percent);
            return haWorkUserList;
        }).collect(Collectors.toList());
        return workUserList;
    }
    /**
     * @param name   姓名
     * @param haType 帮代办类型，1-快捷帮办，2-圆桌帮办
     * @return List<HaWorkQueueResponseData> 获取帮代办人员列表详情
     * @description: 获取帮代办人员列表
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<HaWorkQueueResponseVo> getWorkUserList(String name, String haType) throws ServiceException {
        Date start = DateUtil.getTodayStartTime(); //获取当天零点时间
        Date end = DateUtil.getTodayEndTime(); //获取当天23:59:59秒时间
        List<HaWorkQueueResponseVo> dbHaWorkQueueVoList = dbHaWorkQueueMapper.getWorkUserList(name, haType, null,start, end);
        for (HaWorkQueueResponseVo haWorkQueueResponseVo : dbHaWorkQueueVoList) {
            int avgfMillis =0;
            DbHaWorkQueueExample dbHaWorkQueueExample = new DbHaWorkQueueExample();
            DbHaWorkQueueExample.Criteria queueCriteria = dbHaWorkQueueExample.createCriteria();
            queueCriteria.andQueueStatusEqualTo("2");
            queueCriteria.andServiceWorkUserIdEqualTo(haWorkQueueResponseVo.getId());
            queueCriteria.andCreateDateBetween(DateUtil.getTodayStartTime(),DateUtil.getTodayEndTime());
            List<DbHaWorkQueue>  dbHaWorkQueueList = dbHaWorkQueueMapper.selectByExample(dbHaWorkQueueExample);
            int millisDiff =0;//声明员工总等待时长
            for (DbHaWorkQueue dbHaWorkQueue : dbHaWorkQueueList) { //遍历一个员工所服务的所有队列集合
                millisDiff += DateUtil.dateDiff('m', dbHaWorkQueue.getServiceBeginTime(), dbHaWorkQueue.getCreateDate()); //获取所属工作人员单条等待时长，单位分钟
            }
            if(millisDiff !=0){
                int millisAvg = millisDiff/dbHaWorkQueueList.size();//获得员工平均等待时长9
                avgfMillis += millisAvg;
            }
            haWorkQueueResponseVo.setAvgWaitingTime(avgfMillis);
        }
        //循环遍历将DbHaWorkQueueVo对象list集合遍历除所需参数重新封装到dbHaWorkQueueVoList集合中
//        List<HaWorkQueueResponseData> dbHaWorkQueueVos = dbHaWorkQueueVoList.stream().map(thaQueueVoService -> {
//            HaWorkQueueResponseData haWorkQueueVo = new HaWorkQueueResponseData();
//            BeanUtils.copyProperties(thaQueueVoService, haWorkQueueVo);
//            return haWorkQueueVo;
//        }).collect(Collectors.toList());
        return dbHaWorkQueueVoList;
    }

    /**
     * @param workUserId 用户id  根据用户id获取该用户组的所有服务列表
     * @return List<HaWorkQueueResponseData> 获取帮代办人员列表详情
     * @description: 获取帮代办人员列表
     * @author zhaobf
     * @Date: 2022/8/4 9:58
     **/
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<HaWorkQueueResponseData2> getWorkUserGroupList(Long workUserId) throws ServiceException {
        Date start = DateUtil.getTodayStartTime(); //获取当天零点时间
        Date end = DateUtil.getTodayEndTime(); //获取当天23:59:59秒时间
        List<DbHaWorkUserVo> dbHaWorkQueueVoList = dbHaWorkQueueMapper.getWorkUserGroupList(workUserId, start, end);
        //循环遍历将DbHaWorkQueueVo对象list集合遍历除所需参数重新封装到dbHaWorkQueueVoList集合中
        List<HaWorkQueueResponseData2> dbHaWorkQueueVos = dbHaWorkQueueVoList.stream().map(thaQueueVoService -> {
            HaWorkQueueResponseData2 haWorkQueueVo = new HaWorkQueueResponseData2();
            BeanUtils.copyProperties(thaQueueVoService, haWorkQueueVo);
            return haWorkQueueVo;
        }).collect(Collectors.toList());
        return dbHaWorkQueueVos;
    }


    /**
     * @param workUserId       员工id
     * @param queueId          队列id
     * @param distributeStatus 分配状态;1-指定人员，2-随机分配，3-窗口办理
     * @return DbHaWorkUser 获取人员信息详情
     * @description: 获取帮代办人员列表
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public DbHaWorkUser distributeWorkUser(Long workUserId, Long queueId, String distributeStatus, String windowsNumbe) throws ServiceException {
        //获取当前用户信息
        HaLoginUserInfo haLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        dbHaWorkQueueMapper.update(workUserId, queueId, distributeStatus, haLoginUserInfo.getId(), haLoginUserInfo.getName(), new Date(), windowsNumbe);
        DbHaWorkUser dbThaWorkUser = null;
        if (!distributeStatus.equals("3")) { //判断是否为窗口办理
            dbThaWorkUser = dbHaWorkUserMapper.queryById(workUserId);
            //如果不是窗口办理，需要给消息表发送一条消息
            DbHaMessage dbHaMessage = new DbHaMessage();
            dbHaMessage.setTitle("分配帮代办人员");
            dbHaMessage.setContent("您有一条新的分配消息,请及时查收");
            dbHaMessage.setSendUserId(haLoginUserInfo.getId()); //发送人
            dbHaMessage.setSendTime(new Date());
            dbHaMessage.setReceiveUserId(workUserId); //接口人
            dbHaMessage.setReadStatus("1");
            dbHaMessage.setDeleteStatus("1");
            dbHaMessage.setCreateBy(haLoginUserInfo.getName());//创建人
            dbHaMessage.setCreateDate(new Date());//当前时间
            //除窗口办理以外，其他办理状态需要给在消息表新增一条消息
            int insert = dbHaMessageMapper.insert(dbHaMessage);
        }
        return dbThaWorkUser;
    }

    public DbHaWorkUser distributeWorkUser(Long workUserId, String distributeStatus) throws ServiceException {
        //获取当前用户信息
//        HaLoginUserInfo haLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
//        dbHaWorkQueueMapper.update(workUserId, queueId, distributeStatus, haLoginUserInfo.getId(), haLoginUserInfo.getName(), new Date(), windowsNumbe);
        DbHaWorkUser dbThaWorkUser = null;
        if (!distributeStatus.equals("3")) { //判断是否为窗口办理
            dbThaWorkUser = dbHaWorkUserMapper.queryById(workUserId);
            //如果不是窗口办理，需要给消息表发送一条消息
            DbHaMessage dbHaMessage = new DbHaMessage();
            dbHaMessage.setTitle("分配帮代办人员");
            dbHaMessage.setContent("您有一条新的分配消息,请及时查收");
            dbHaMessage.setSendUserId((long) -1); //发送人
            dbHaMessage.setSendTime(new Date());
            dbHaMessage.setReceiveUserId(workUserId); //接口人
            dbHaMessage.setReadStatus("1");
            dbHaMessage.setDeleteStatus("1");
            dbHaMessage.setCreateBy("-1");//创建人
            dbHaMessage.setCreateDate(new Date());//当前时间
            //除窗口办理以外，其他办理状态需要给在消息表新增一条消息
            int insert = dbHaMessageMapper.insert(dbHaMessage);
        }
        return dbThaWorkUser;
    }

    /**
     * 窗口叫号
     *
     * @param queueId
     * @param windowsNumbe
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public void windowCall(Long queueId, String windowsNumbe) throws ServiceException {
        //获取当前用户信息
        HaLoginUserInfo haLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        //队列编号是唯一的
        DbHaWorkQueue dbHaWorkQueue = dbHaWorkQueueMapper.selectByPrimaryKey(queueId);
        if (dbHaWorkQueue != null) {
            dbHaWorkQueue.setWindowsNumber(windowsNumbe); //排队号
            dbHaWorkQueue.setUpdateBy(haLoginUserInfo.getName()); //更新人姓名
            dbHaWorkQueue.setUpdateDate(new Date()); //修改时间
            dbHaWorkQueue.setDistributeUserId(haLoginUserInfo.getId()); //分配人
            dbHaWorkQueueMapper.updateByPrimaryKeySelective(dbHaWorkQueue);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public String savePushNumber(HaQueueRequestData haQueueRequestData)  throws ServiceException{
        try{
            if (haQueueRequestData == null) {
                return "预约信息不能为空";
            }

            if (StringUtil.isEmpty(haQueueRequestData.getName())) {
                return "预约人姓名不能为空！";
            }

            if (StringUtil.isEmpty(haQueueRequestData.getCardNo())) {
                return "预约人身份证号不能为空！";
            }

            if (StringUtil.isEmpty(haQueueRequestData.getPhone())) {
                return "预约人手机号不能为空！";
            }
            if (StringUtil.isEmpty(haQueueRequestData.getServiceType())) {
                return "服务类型不能为空";
            }

            DbHaWorkQueue dbHaWorkQueue = new DbHaWorkQueue();
            com.zfsoft.platform.utils.bean.BeanUtils.copyProperties(haQueueRequestData, dbHaWorkQueue);

            dbHaWorkQueue.setUpdateDate(new Date()); //修改时间
            dbHaWorkQueue.setDetectsServiceTime(new Date());//设置检测服务时间
            dbHaWorkQueue.setCreateDate(new Date()); //创建时间
            Map<String, Object> map = new HashMap<>();
            if (StringUtils.isNotEmpty(haQueueRequestData.getServiceType())) {
                switch (haQueueRequestData.getServiceType()) {
                    case "1":
                        map.putAll( getDistributeWorkUserByHaTyoe("1"));
                        dbHaWorkQueue.setDistributeStatus("2");
                        dbHaWorkQueue.setServiceWorkUserId((Long) map.get("workUserId"));
                        dbHaWorkQueue.setServiceStatus("1");
                        break;
                    case "2":
                        map.putAll( getDistributeWorkUserByHaTyoe("2"));
                        dbHaWorkQueue.setDistributeStatus("2");
                        dbHaWorkQueue.setServiceWorkUserId((Long) map.get("workUserId"));
                        dbHaWorkQueue.setServiceStatus("1");
                        break;
                    case "3":
                        dbHaWorkQueue.setDistributeStatus("3");
                        dbHaWorkQueue.setServiceStatus("3");     //-- 服务状态为 3 已完成
                        dbHaWorkQueue.setServiceDuration(0);  //服务时长为0
                        dbHaWorkQueue.setServiceBeginTime(new Date());//开始时间为当前时间
                        dbHaWorkQueue.setServiceEndTime(new Date());//结束时间为当前时间
                        break;
                    default:
                        return "服务类型错误，只能为1,2,3";
                }
            }
            //设置分配人为系统
            dbHaWorkQueue.setDistributeUserId((long) -1);
            //分配时间
            dbHaWorkQueue.setDistributeTime(DateUtil.getDate());
            dbHaWorkQueue.setQueueStatus("2");
            int result = dbHaWorkQueueMapper.insert(dbHaWorkQueue);
            //判断数据是否保存成功
            if (result < 0) {
                return "排队叫号失败，请重试！";
            }
        }catch (ServiceException e){
            throw new ServiceException("排队叫号分配出错");
        }

        return null;

    }
    public Map<String,Object> getDistributeUser(Map<Long, Integer> map, String distributeStatus){
        Map<String, Object> mapReq = new HashMap<>();
        //取Map中Value最小值
        Integer minId = QueueController.getMinValue(map);
        List<Long> listId = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : map.entrySet()) {
            if (entry.getValue().equals(minId)) {
                listId.add(entry.getKey());
            }
        }
        //随机取出最小id集合中的一个
        if (listId.size() > 0) {
            Long userId = QueueController.getRandomListId(listId);
            if (userId != null) {
                DbHaWorkUser dbThaWorkUser = distributeWorkUser(userId, distributeStatus);
                mapReq.put("workUserId", userId);
                mapReq.put("workUserName", dbThaWorkUser.getName());
            }
        }
        return mapReq;
    }
    public Map<String, Object>  getDistributeWorkUserByHaTyoe(String haType) {
        Map<String, Object> map = new HashMap<>();
        /**
         * 优先级是1.空闲 2.服务中 3.忙碌 再根据每个状态中，服务人数最少的去分配
         */
        Map<Long, Integer> mapStatus2 = new HashMap<>(); //状态为忙碌的集合
        Map<Long, Integer> mapStatus3 = new HashMap<>();//状态为空闲的集合
        Map<Long, Integer> mapStatus4 = new HashMap<>();//状态为服务中的集合
        Map<String, Map<Long, Integer>> mapList = new HashMap<>();
        List<HaWorkUser> haWorkUser = userServiceManager.getMinMaxServiceNumByHaType(haType);
        log.debug("thaWorkUser:{}", haWorkUser);

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
            map.putAll(this.getDistributeUser(mapList.get("空闲"), "2"));
        } else if (mapList.get("服务中").size() > 0) {
            map.putAll(this.getDistributeUser(mapList.get("服务中"), "2"));
        } else if (mapList.get("忙碌").size() > 0) {
            map.putAll(this.getDistributeUser(mapList.get("忙碌"), "2"));
        }
        return map;
    }

    /**
     * @return DbHaWorkQueueVo 获取当前登录用统计队列数据详情
     * @description: 获取排队人数及预计耗时接口 -万行
     * @author: Wangyh
     * @Date: 2022/9/27 21:10
     **/
    public HaWorkQueueResponseVo queryWorkUserVo(String workUserID) throws ServiceException {
        //查询开始时间00:00:00
        Date startTime = DateUtil.getTodayStartTime();
        //查询结束时间23:59:59
        Date endTime = DateUtil.getTodayEndTime();
        HaWorkQueueResponseVo haWorkQueueResponseVo = dbHaWorkQueueMapper.getWorkUser(workUserID,startTime,endTime);
        return haWorkQueueResponseVo;
    }

    /**
     * @return DbHaWorkQueueVo 获取当天排班集合
     * @description: 获取排队人数及预计耗时接口 -万行
     * @author: Wangyh
     * @Date: 2022/9/27 21:10
     **/
    public Integer queryWorkUserScheduleList(Date date) throws ServiceException {
        DbHaWorkUserScheduleExample dbHaWorkUserScheduleExample = new DbHaWorkUserScheduleExample();
        DbHaWorkUserScheduleExample.Criteria criteria = dbHaWorkUserScheduleExample.createCriteria();
        criteria.andWorkDateEqualTo(date);
        criteria.andScheduleStatusEqualTo("1");//可预约的
        List<DbHaWorkUserSchedule> dbHaWorkUserSchedules = dbHaWorkUserScheduleMapper.selectByExample(dbHaWorkUserScheduleExample);
        List<Long> workUserId = dbHaWorkUserSchedules.stream().map(dbHaWorkUserSchedule -> dbHaWorkUserSchedule.getWorkUserId()).collect(Collectors.toList());
        Integer avgfMillis =0;//声明当天排班员工的平均等待时长总和
        for(int i=0;i<workUserId.size();i++){ //所有排班人员的集合
            DbHaWorkQueueExample dbHaWorkQueueExample = new DbHaWorkQueueExample();
            DbHaWorkQueueExample.Criteria queueCriteria = dbHaWorkQueueExample.createCriteria();
            queueCriteria.andQueueStatusEqualTo("2");
            queueCriteria.andServiceWorkUserIdEqualTo(workUserId.get(i));
            queueCriteria.andCreateDateBetween(DateUtil.getTodayStartTime(),DateUtil.getTodayEndTime());
            List<DbHaWorkQueue>  dbHaWorkQueueList = dbHaWorkQueueMapper.selectByExample(dbHaWorkQueueExample);
            Integer millisDiff =0;//声明员工总等待时长
            for(int j = 0;j<dbHaWorkQueueList.size();j++){ //遍历一个员工所服务的所有队列集合
                 DbHaWorkQueue dbHaWorkQueue = dbHaWorkQueueList.get(j);
                 millisDiff += DateUtil.dateDiff('m',dbHaWorkQueue.getServiceBeginTime(),dbHaWorkQueue.getCreateDate()); //获取所属工作人员单条等待时长，单位分钟
            }
            if(millisDiff !=0){
                Integer millisAvg = millisDiff/dbHaWorkQueueList.size();//获得员工平均等待时长
                avgfMillis += millisAvg;
            }
        }
        Integer avgWait = 0;
        if(avgfMillis !=0){
             avgWait = avgfMillis/workUserId.size(); //获取所有当天排班员工的平均等待时长
            }

        return avgWait;
    }


    public HaWorkQueue getQueueById(Long queueId) {
        DbHaWorkQueue dbHaWorkQueue = dbHaWorkQueueMapper.selectByPrimaryKey(queueId);
        HaWorkQueue haWorkQueue = new HaWorkQueue();
        BeanUtils.copyProperties(dbHaWorkQueue, haWorkQueue);
        return haWorkQueue;
    }


    public static void main(String[] args) {
        BigDecimal evalScore = new BigDecimal(3.46);
        BigDecimal b2 = new BigDecimal(5);
        String as = String.valueOf(evalScore.divide(b2,2, BigDecimal.ROUND_HALF_UP));
        DecimalFormat df = new DecimalFormat("0%");
        BigDecimal d=new BigDecimal(String.valueOf(as));
        String percent=df.format(d);
        System.out.println("percent====="+percent);
    }
}
