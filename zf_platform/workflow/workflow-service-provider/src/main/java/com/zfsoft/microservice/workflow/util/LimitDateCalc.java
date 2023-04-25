package com.zfsoft.microservice.workflow.util;

import cn.hutool.core.date.DateUtil;
import com.zfsoft.microservice.workflow.feign.PlatformServiceFeignService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 * @ClassName LimitDateCalc
 * @Description: 节假日日期计算类
 * @Author wuxx
 * @Date 2021/2/1
 **/
@Component
public class LimitDateCalc {

    private static PlatformServiceFeignService platformServiceFeignService;

    @Autowired
    private void setPlatformServiceFeignService(PlatformServiceFeignService serviceFeignService) {
        this.platformServiceFeignService = serviceFeignService;
    }

    /**
     * 根据日期以及天数、单位计算日期
     *
     * @param date
     *            需要计算的日期
     * @param timeLimitDay
     *            天数
     * @param timeUnit
     *            单位，N-自然日，W-工作日
     * @param timeLimitHour
     *            小时数
     * @return 计算后的日期
     */
    public static Date dateCalc(Date date, Integer timeLimitDay, String timeUnit, Integer timeLimitHour) {
        Date calDate = null;
        // 计算天数，当天数为0时，不计算
        if (timeLimitDay != null && timeLimitDay > 0) {
            if (BaseStaticParameter.HOLIDAY_NATURE.equals(timeUnit)) {
                //calDate = DateUtil.addDate(date, timeLimit);
                calDate = DateUtil.offsetDay(date, timeLimitDay);
            } else {
                // 工作日日期计算，目前设置为自然日，以后扩展
                calDate = dateCalcWork(date, timeLimitDay);
            }
        } else {
            calDate = new Date(date.getTime());
        }
        if(null == calDate){
            calDate = date;
        }
        // 计算小时数
        if (timeLimitHour != null && timeLimitHour > 0) {
            // 小时数通过分钟进行计算
            calDate = DateUtil.offsetHour(calDate, timeLimitHour);
        }

        return calDate;
    }


    /**
     * 以工作日方式计算结束日期
     *
     * @param date
     *            需要计算的日期
     * @param timeLimit
     *            需要添加的天数
     * @return
     */
    private static Date dateCalcWork(Date date, Integer timeLimit) {
        Date calDate = null;
        if (timeLimit > 0) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            //H-节假日
            Set<String> holidayDateHMap = null;
            //W-工作日
            Set<String> holidayDateWMap = null;
            ApiResultSet<Set<String>> resultHSet = platformServiceFeignService.querySysHolidaySetByHolidayType(BaseStaticParameter.HOLIDAY_DATE_1);
            if(200 == resultHSet.getCode()){
                holidayDateHMap = resultHSet.getData();
            }
            ApiResultSet<Set<String>> resultWSet = platformServiceFeignService.querySysHolidaySetByHolidayType(BaseStaticParameter.HOLIDAY_DATE_2);
            if(200 == resultWSet.getCode()){
                holidayDateWMap = resultWSet.getData();
            }
            for (int i = 0; i < timeLimit; i++) {
                cal.setTime(dateCalcWorkAddOne(cal.getTime(), holidayDateHMap, holidayDateWMap));
            }
            calDate = cal.getTime();
        }
        return calDate;
    }

    /**
     * 为日期加1天
     *
     * @param date
     *            日期
     * @return 加1天的日期
     */
    private static Date dateCalcWorkAddOne(Date date, Set<String> holidayDateHMap, Set<String> holidayDateWMap) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        Date calcDate = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String calcDateStr = sdf.format(calcDate);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        // 周一二三四五
        int max = 5;
        int min = 1;
        if (w <= max && w >= min) {
            if (holidayDateHMap.contains(calcDateStr)) {
                return dateCalcWorkAddOne(calcDate, holidayDateHMap, holidayDateWMap);
            }
        } else { // 周六日
            if (!holidayDateWMap.contains(calcDateStr)) {
                return dateCalcWorkAddOne(calcDate, holidayDateHMap, holidayDateWMap);
            }
        }
        return calcDate;
    }

}
