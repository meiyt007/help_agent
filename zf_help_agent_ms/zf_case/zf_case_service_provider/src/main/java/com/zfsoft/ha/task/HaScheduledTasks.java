package com.zfsoft.ha.task;

import com.zfsoft.cases.util.DateUtil;
import com.zfsoft.ha.constant.Constants;
import com.zfsoft.ha.data.HaPerformanceRegulartimeRecord;
import com.zfsoft.ha.data.HaWorkTurnRecord;
import com.zfsoft.ha.dbaccess.dao.DbHaOnlineMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkQueueMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkTurnRecordMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkUserMapper;
import com.zfsoft.ha.dbaccess.data.DbHaOnline;
import com.zfsoft.ha.dbaccess.data.DbHaWorkQueue;
import com.zfsoft.ha.dbaccess.data.DbHaWorkTurnRecord;
import com.zfsoft.ha.dbaccess.data.DbHaWorkUser;
import com.zfsoft.ha.dbaccess.data.example.DbHaWorkUserExample;
import com.zfsoft.ha.managers.HaPerformanceRegulartimeRecordManager;
import com.zfsoft.ha.managers.HaWorkTurnRecordManager;
import com.zfsoft.ha.managers.HaWorkUserScheduleManager;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.zfsoft.ha.constant.Constants.DELETE_STATUS_NO;
import static com.zfsoft.ha.constant.RedisConstants.TURN_TIMEOUT;

/**
 * @Description //定时任务类
 * 如果想把某个定时任务全关闭 把对应方法上的@Scheduled注释<br>
 * @Author: Wangyh
 * @Date: 2022/8/11 13:42
 */

@Slf4j
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
public class HaScheduledTasks {

    /**
     * 用户在线时长接口db实现层
     */
    @Resource
    private DbHaOnlineMapper dbHaOnlineMapper;

    /**
     * 帮代办人员mapper
     */
    @Resource
    private DbHaWorkUserMapper dbThaWorkUserMapper;

    /**
     * 办事队列mapper
     */
    @Resource
    private DbHaWorkQueueMapper dbHaWorkQueueMapper;

    /**
     * 转派服务service
     */
    @Resource
    private HaWorkTurnRecordManager haWorkTurnRecordManager;
    /**
     * 转派服务mapper
     */
    @Resource
    private DbHaWorkTurnRecordMapper dbHaWorkTurnRecordMapper;

    @Resource
    private HaWorkUserScheduleManager haWorkUserScheduleManager;

    @Resource
    private HaPerformanceRegulartimeRecordManager haPerformanceRegulartimeRecordManager;

    /**
     * 工作时间每隔5分钟，从用户在线时长表中获取退出时间，当5分钟内没有收到在线数据时，修改人员的状态为离线
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 */5 * * * ?")
    @Async("onlineStatusTaskExecutor")    // 通过注解指定使用的线程池
    public void onlineStatusTaskExecutor() throws ServiceException {
        System.out.println("在线帮代办人员状态定时任务开始================================================================");
        log.info("在线帮代办人员状态定时任务");
        Date date = new Date();//获取当前时间
        SimpleDateFormat df = new SimpleDateFormat("HH");
        String str = df.format(date);
        int a = Integer.parseInt(str);
        /**
         * 判断是否满足工作日时间 早上8:00 晚上8:00
         */
        if (a >= 8 && a <= 20) {
            //获取5分钟以为的在线时长表数据集合
            List<DbHaOnline> dbHaOnlineList = dbHaOnlineMapper.queryFiveMinuteList(DateUtil.beforeFiveMinute(), new Date());
            //获取Online在线时长表IDlist集合
            List<Long> dbHaOnlineIdList = dbHaOnlineList.stream().map(DbHaOnline::getWorkUserId).collect(Collectors.toList());
            DbHaWorkUserExample thaWorkUserExample = new DbHaWorkUserExample();
            DbHaWorkUserExample.Criteria criteria = thaWorkUserExample.createCriteria();
            criteria.andDeleteStatusEqualTo("1"); //1-未删除，2-已删除
            thaWorkUserExample.setOrderByClause("CREATE_DATE desc");
            List<DbHaWorkUser> dbHaWorkUserList = dbThaWorkUserMapper.selectByExample(thaWorkUserExample);
            //获取所有未删除用户IDlist
            List<Long> workUserIdList = dbHaWorkUserList.stream().map(DbHaWorkUser::getId).collect(Collectors.toList());
            List<Long> AnB = workUserIdList.stream().filter(dbHaOnlineIdList::contains).collect(Collectors.toList());
            System.out.println("交集：" + AnB);
            for (int i = 0; i < AnB.size(); i++) {
                workUserIdList.remove(AnB.get(i));
            }
            log.info("workUserIdList,{}", workUserIdList);
            System.out.println("==========================" + workUserIdList);
            /**
             * 循环遍历更新不在“在线时长表"中的用户状态，状态改为离线 1
             */
            for (int i = 0; i < workUserIdList.size(); i++) {
//                DbHaWorkUser dbHaWorkUser = new DbHaWorkUser();
                DbHaWorkUser dbHaWorkUser =  dbThaWorkUserMapper.queryById(workUserIdList.get(i));
//                dbHaWorkUser.setId(workUserIdList.get(i));
                dbHaWorkUser.setStatus("1");
                dbThaWorkUserMapper.update(dbHaWorkUser);
            }
        }
        System.out.println("在线帮代办人员状态定时任务结束================================================================");
    }

    /**
     * 转派服务
     * 每5分钟去判断转派的办件，处理时间是否超过30s
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 */5 * * * ?")
    @Async("onlineStatusTaskExecutor")
    public void turnStatusTaskExecutor(){
//        System.out.println("转派服务审核状态定时任务开始================================================================");
        log.info("转派服务审核状态定时定时任务");
        Date date = new Date();//获取当前时间
        SimpleDateFormat df = new SimpleDateFormat("HH");
        String str = df.format(date);
        int a = Integer.parseInt(str);
        /**
         * 判断是否满足工作日时间 早上8:00 晚上8:00
         */
        if (a >= 8 && a <= 20) {
            HaWorkTurnRecord haWorkTurnRecord = new HaWorkTurnRecord();
            haWorkTurnRecord.setTurnStatus("1");
            List<HaWorkTurnRecord> haWorkTurnRecords = haWorkTurnRecordManager.queryTurnByExample(haWorkTurnRecord);
            if(haWorkTurnRecords.size()>0){
                int count = 0;
                for (HaWorkTurnRecord workTurnRecord : haWorkTurnRecords) {
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
                        DbHaWorkTurnRecord dbHaWorkTurnRecord = new DbHaWorkTurnRecord();
                        BeanUtils.copyProperties(workTurnRecord, dbHaWorkTurnRecord);
                        dbHaWorkTurnRecordMapper.updateByPrimaryKeySelective(dbHaWorkTurnRecord);
                        count++;
                        //根据ID查询原服务队列
                        DbHaWorkQueue dbHaWorkQueueById = dbHaWorkQueueMapper.selectByPrimaryKey(workTurnRecord.getWorkQueueId());
                        //设置办件状态为待服务
                        dbHaWorkQueueById.setServiceStatus(Constants.WAIT);
                        dbHaWorkQueueById.setDetectsServiceTime(new Date());
                        dbHaWorkQueueMapper.updateByPrimaryKeySelective(dbHaWorkQueueById);
                    }
                }
                log.info("转派服务审核状态定时定时任务:"+haWorkTurnRecords.size()+"条未处理；"+count+"条做超时处理；");
            }else{
                log.info("转派服务审核状态定时定时任务没有更新的数据");
            }
        }
//        System.out.println("转派服务审核状态定时定时任务结束================================================================");
    }
    /**
     * 排班定时生成  每月15号自动生成下个月的排班
     */
    @Scheduled(cron = "0 0 0 15 * ?")
    @Async("onlineStatusTaskExecutor")    // 通过注解指定使用的线程池
    public void haWorkUserScheduleTaskExecutor() throws ServiceException {
        System.out.println("排班定时生成定时任务开始================================================================");
        log.info("排班定时生成定时任务");
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        //每月15号生成下个月的排班
        calendar.add(Calendar.MONTH,1);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int init = haWorkUserScheduleManager.init(year + "-" + (month < 10 ? "0" + month : month));
        System.out.println("排班定时生成定时任务结束================================================================:"+init);
    }

    /**
     * 办事人定时任务
     * 当天未服务完成的人员（导服未分配），将排队状态设置为超时未处理，服务状态设置为超时未处理，同时建议内容，设置为超时未处理
     * 每天下班后执行一次，可以设置为晚上20-23点之间  每天23点执行一次：0 0 23 * * ?
     * @throws Exception
     */
    @Scheduled(cron = "0 0 23 * * ?")
    @Async("handlingAgentTaskExecutor")    // 通过注解指定使用的线程池
    public void handlingAgentTaskExecutor() throws ServiceException {
        System.out.println("办事人定时任务开始================================================================");
        log.info("办事人定时任务");
        //查询开始时间00:00:00
        Date startTime = DateUtil.getTodayStartTime();
        //查询结束时间22:00:00
        Date endTime = DateUtil.getEndTime();
        List<DbHaWorkQueue> dbHaWorkQueueList = dbHaWorkQueueMapper.queryQueueList(startTime,endTime);
        for(int i =0;i<dbHaWorkQueueList.size();i++){
            DbHaWorkQueue dbHaWorkQueue = new DbHaWorkQueue();
            dbHaWorkQueue.setId(dbHaWorkQueueList.get(i).getId());
            dbHaWorkQueue.setQueueStatus("3"); //将排队状态设置为超时未处理
            dbHaWorkQueue.setServiceStatus("4"); //服务状态设置为超时未处理
            dbHaWorkQueue.setAdviseMemo("超时未处理"); //同时建议内容，设置为超时未处理
            dbHaWorkQueueMapper.updateByPrimaryKeySelective(dbHaWorkQueue);
        }
        System.out.println("办事人定时任务结束================================================================");
    }

    /**
     * 每天下午五点半更新当天服务得结束时间
     * 每天下午五点半定时执行当天涉及到的未结束服务的服务中的  0 30 17 * * ?
     */
    @Scheduled(cron = "0 0 23 * * ?")
//    @Scheduled(cron = "0/5 * * * * ? ")//5秒定时
    @Async("serviceTimeEndTaskExecutor")    // 通过注解指定使用的线程池
    public void serviceTimeEndTaskExecutor() throws ServiceException {
        log.info("每天下午五点半更新服务得结束时间定时任务开始======================================");
        //查询开始时间00:00:00
        Date startTime = DateUtil.getTodayStartTime();
//        查询开始时间00:00:00(昨天的零点到今天的23点59分59秒)
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(startTime);
//        calendar.add(Calendar.DAY_OF_MONTH,-1);
//        startTime = calendar.getTime();
        //查询结束时间22:00:00
        Date endTime = DateUtil.getEndTime();
        List<DbHaWorkQueue> dbHaWorkQueueList = dbHaWorkQueueMapper.queryQueueListByServiceStatus(startTime,endTime);
        for (DbHaWorkQueue dbHaWorkQueue : dbHaWorkQueueList) {
            //服务状态设置为服务完成
            dbHaWorkQueue.setServiceStatus(Constants.END_SERVICE);
            //办事队列修改人
            dbHaWorkQueue.setUpdateBy("系统定时修改");
            //办事队列修改时间
            dbHaWorkQueue.setUpdateDate(DateUtil.getTodaySeventeenTime(dbHaWorkQueue.getServiceBeginTime()));
            //设置服务结束时间 ：为当天下午五点
            dbHaWorkQueue.setServiceEndTime(DateUtil.getTodaySeventeenTime(dbHaWorkQueue.getServiceBeginTime()));
            //设置服务时间
            dbHaWorkQueue.setServiceDuration(Math.toIntExact((dbHaWorkQueue.getServiceEndTime().getTime() - dbHaWorkQueue.getFirstServiceBeginTime().getTime()) / 1000));
            dbHaWorkQueueMapper.updateByPrimaryKeySelective(dbHaWorkQueue);
            updateHaPerformanceRegulartimeRecord(dbHaWorkQueue);//更新绩效常规时长记录埋点数据
        }
        log.info("每天下午五点半更新服务得结束时间定时任务结束=========================================");
    }
    /**
     * 修改更新绩效常规时长记录埋点数据
     * @param dbHaWorkQueue 帮代办人员的待服务
     * @author: dingsn
     */
    private void updateHaPerformanceRegulartimeRecord(DbHaWorkQueue dbHaWorkQueue) {
        log.info("定时任务更新常规绩效时长记录数据");
        try {
            HaPerformanceRegulartimeRecord haPerformanceRegulartimeRecordVo = new HaPerformanceRegulartimeRecord();
            haPerformanceRegulartimeRecordVo.setWorkUserOid(dbHaWorkQueue.getServiceWorkUserId());
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
                    haPerformanceRegulartimeRecord.setServiceEndTime(DateUtil.getTodaySeventeenTime(dbHaWorkQueue.getServiceBeginTime()));
                }
                //设置服务时间
//                haPerformanceRegulartimeRecord.setServiceDuration(dbHaWorkQueue.getServiceDuration());
                haPerformanceRegulartimeRecord.setServiceDuration(Math.toIntExact((haPerformanceRegulartimeRecord.getServiceEndTime().getTime() - haPerformanceRegulartimeRecord.getServiceBeginTime().getTime()) / (1000*60)));
                haPerformanceRegulartimeRecordManager.saveOrUpdatePerformanceRegulartimeRecord(haPerformanceRegulartimeRecord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
