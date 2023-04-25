package com.zfsoft.ha.managers;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.ha.data.HaWorkUser;
import com.zfsoft.ha.data.HaWorkUserSchedule;
import com.zfsoft.ha.data.vo.HaWorkUserScheduleVo;
import com.zfsoft.ha.data.vo.HaWorkUserSchedule_AppVo;
import com.zfsoft.ha.data.vo.HaWorkUserVo_Schedule;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkUserMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkUserScheduleMapper;
import com.zfsoft.ha.dbaccess.data.DbHaWorkUserSchedule;
import com.zfsoft.ha.dbaccess.data.example.DbHaWorkUserScheduleExample;
import com.zfsoft.ha.dbaccess.data.vo.DbHaWorkUserScheduleVo;
import com.zfsoft.ha.dbaccess.data.vo.DbHaWorkUserSchedule_AppVo;
import com.zfsoft.ha.dbaccess.data.vo.DbHaWorkUserVo;
import com.zfsoft.ha.util.CalenderUtils;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.bean.BeanUtils;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.zfsoft.cases.util.DateUtil.PATTEN_DEFAULT_YMD;
import static com.zfsoft.ha.constant.Constants.SCHEDULE_AM_AND_PM;
import static com.zfsoft.ha.constant.Constants.SCHEDULE_NICE;

/**
 * 工作人员排班service层  业务层
 * @author zhaobf
 * @version 1.0
 * @date 2022/9/23 10:36
 */
@Service
public class HaWorkUserScheduleManager{
    /**
     * 工作人员排班
     */
    @Resource
    DbHaWorkUserScheduleMapper dbHaWorkUserScheduleMapper;

    @Resource
    HaWorkUserServiceManager haWorkUserServiceManager;

    @Resource
    DbHaWorkUserMapper dbHaWorkUserMapper;


    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public int init(String yearMonth) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        int countNumb  =  0;
        try {
            Date parse = simpleDateFormat.parse(yearMonth);
            //初始化过的排班信息不能再初始化。
            int count = dbHaWorkUserScheduleMapper.countByYearMonthAfter(yearMonth);
            if(count>0){
                return -500;
            }
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(parse);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1;
            //获取所有帮办人
            List<HaWorkUser> haWorkUsers = haWorkUserServiceManager.queryAllWorkUser();

            //获取当月所有天数
            int num = CalenderUtils.getDaysByYearMonth(calendar.get( Calendar.YEAR),calendar.get(Calendar.MONTH)) ;

            Set<String> jjr = CalenderUtils.JJR(year, month - 1);
            for (HaWorkUser haWorkUser : haWorkUsers) {
                for (int i = 1; i <= num; i++) {
                    DbHaWorkUserSchedule dbHaWorkUserSchedule = new DbHaWorkUserSchedule();
                    dbHaWorkUserSchedule.setYearMonth(year+"-"+(month<10?"0"+month:month));
                    dbHaWorkUserSchedule.setCreateDate(new Date());
                    dbHaWorkUserSchedule.setUpdateDate(new Date());
                    dbHaWorkUserSchedule.setScheduleStatus("1".equals(haWorkUser.getApponStatus())?"1":"2");
                    dbHaWorkUserSchedule.setWorkUserId(haWorkUser.getId());
                    calendar.set(Calendar.DATE,i);
//                System.out.println(calendar.getTime());
                    String week = CalenderUtils.WEEKMAP.get(String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)));
                    String amOrPm;

                    SimpleDateFormat sf = new SimpleDateFormat(PATTEN_DEFAULT_YMD);
                    //获取今天的日期
                    String nowDay = sf.format(calendar.getTime());
                    if(jjr.contains(nowDay)){
                        amOrPm=SCHEDULE_NICE;
                        dbHaWorkUserSchedule.setScheduleStatus("2");
                    }else{
                        amOrPm=SCHEDULE_AM_AND_PM;
                    }
//                    String amOrPm = CalenderUtils.SCHEDULE.get(String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)));
                    System.out.println(week);

                    dbHaWorkUserSchedule.setAmOrPm(amOrPm);
                    dbHaWorkUserSchedule.setWorkDate(calendar.getTime());
                    dbHaWorkUserSchedule.setDayOfWeek(week);

                    countNumb  += dbHaWorkUserScheduleMapper.insertSelective(dbHaWorkUserSchedule);
                }
            }

        } catch ( Exception e) {
            e.printStackTrace();
            throw new ServiceException("初始化排班消息失败");
        }
        return countNumb ;
    }


    public PageResult<HaWorkUserVo_Schedule> queryScheduleList(String yearMonth, Long groupId, String workUserName, Integer pageNum, Integer pageSize) {
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        //获取所有帮办人
        Page<DbHaWorkUserVo> DbHaWorkUserList = (Page<DbHaWorkUserVo>) dbHaWorkUserMapper.queryNameAndGroup(workUserName,groupId);;
        List<HaWorkUserVo_Schedule> haWorkUserList = DbHaWorkUserList.stream().map(haWorkUserVo -> {
            HaWorkUserVo_Schedule haWorkUser = new HaWorkUserVo_Schedule();
            org.springframework.beans.BeanUtils.copyProperties(haWorkUserVo, haWorkUser);
            return haWorkUser;
        }).collect(Collectors.toList());
        for (HaWorkUserVo_Schedule haWorkUser : haWorkUserList) {
            //查询
            List<DbHaWorkUserSchedule> sche = dbHaWorkUserScheduleMapper.queryList(yearMonth, haWorkUser.getId());
            List<HaWorkUserSchedule> haWorkUserSchedules = sche.stream().map(dbHaMassesNucleic1 -> {
                HaWorkUserSchedule haMassesNucleic1 = new HaWorkUserSchedule();
                BeanUtils.copyProperties(dbHaMassesNucleic1, haMassesNucleic1);
                return haMassesNucleic1;
            }).collect(Collectors.toList());
            haWorkUser.setHaWorkUserScheduleList(haWorkUserSchedules);
        }

        PageResult< HaWorkUserVo_Schedule > pageResult = new PageResult<>(DbHaWorkUserList.getPageNum(), DbHaWorkUserList.getPageSize(), DbHaWorkUserList.getTotal());


        pageResult.setData(haWorkUserList);
        return pageResult;

    }
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public int update(HaWorkUserSchedule sd) {
        DbHaWorkUserSchedule dsd = dbHaWorkUserScheduleMapper.selectByPrimaryKey(sd.getId());
        dsd.setScheduleStatus(sd.getScheduleStatus());
        dsd.setAmOrPm(sd.getAmOrPm());
        return  dbHaWorkUserScheduleMapper.updateByPrimaryKeySelective(dsd);
    }

    /**
     * 获取一个区间内的帮办人员的排班信息
     * @param workUserId
     * @param startDate
     * @param endDate
     * @return
     */
    public List<HaWorkUserSchedule_AppVo> queryScheAppList(Long workUserId, String startDate,String endDate,Long scheduleId) {
        List<DbHaWorkUserSchedule_AppVo> dbHaWorkUserSchedule_appVos = dbHaWorkUserScheduleMapper.queryScheAppList(workUserId, startDate, endDate,null);
        List<HaWorkUserSchedule_AppVo> haWorkUserSchedule_appVos = BeanUtils.copyListProperties(dbHaWorkUserSchedule_appVos, HaWorkUserSchedule_AppVo::new);

        return haWorkUserSchedule_appVos;
    }

    public HaWorkUserSchedule getByScheduleId(Long scheduleId){
        DbHaWorkUserSchedule dbHaWorkUserSchedule = dbHaWorkUserScheduleMapper.selectByPrimaryKey(scheduleId);
        if(dbHaWorkUserSchedule==null){
            return null;
        }
        HaWorkUserSchedule haWorkUserSchedule = new HaWorkUserSchedule();
        BeanUtils.copyProperties(dbHaWorkUserSchedule,haWorkUserSchedule);
        return haWorkUserSchedule;
    }

    public List<HaWorkUserScheduleVo> queryListByDayAndGroupidAndScheduleStatus(Date workDate, String groupId, String haType, String apm) {
        List<DbHaWorkUserScheduleVo> dbHaWorkUserScheduleVos = dbHaWorkUserScheduleMapper.queryListByDayAndGroupidAndScheduleStatus(workDate, groupId, haType,apm);
        return BeanUtils.copyListProperties(dbHaWorkUserScheduleVos, HaWorkUserScheduleVo::new);
    }

    public List<HaWorkUserScheduleVo> queryUserScheAndQueueListByDay(Date workDate, String groupId, String haType, String apm,Date beginADay, Date endADay) {
        List<DbHaWorkUserScheduleVo> dbHaWorkUserScheduleVos = dbHaWorkUserScheduleMapper.queryUserScheAndQueueListByDay(workDate, groupId, haType,apm,beginADay,endADay);
        return BeanUtils.copyListProperties(dbHaWorkUserScheduleVos, HaWorkUserScheduleVo::new);
    }

    public int delect(String yearMonth) {
        DbHaWorkUserScheduleExample dbHaWorkUserScheduleExample = new DbHaWorkUserScheduleExample();
        DbHaWorkUserScheduleExample.Criteria criteria = dbHaWorkUserScheduleExample.createCriteria();
        criteria.andYearMonthEqualTo(yearMonth);
        return dbHaWorkUserScheduleMapper.deleteByExample(dbHaWorkUserScheduleExample);
    }
}
