package com.zfsoft.cases.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * 日期计算类
 *
 * @author gaolh
 * @date 2016-11-11 13:54:53
 *
 */
public class LimitDateCalc {

    // 节假日集合列表，除周六周日外的节假日
    public static Set<String> holidayDateHMap = new HashSet<String>();

    // 工作日集合列表，除周一二三四五之外的工作日
    public static Set<String> holidayDateWMap = new HashSet<String>();

    // 日期格式化格式
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static Integer MORNING_WORK_ON_TIME_HOUR; // 上午上班时间-时
    public static Integer MORNING_WORK_ON_TIME_MINUTE; // 上午上班时间-分钟
    public static Integer MORNING_WORK_OFF_TIME_HOUR; // 上午下班时间-时
    public static Integer MORNING_WORK_OFF_TIME_MINUTE; // 上午下班时间-分钟
    public static Integer AFTERNOON_WORK_ON_TIME_HOUR; // 下午上班时间-时
    public static Integer AFTERNOON_WORK_ON_TIME_MINUTE; // 下午上班时间-分钟
    public static Integer AFTERNOON_WORK_OFF_TIME_HOUR; // 下午下班时间-时
    public static Integer AFTERNOON_WORK_OFF_TIME_MINUTE; // 下午下班时间-分钟

    {
        // holidayDateHMap.add("2017-1-2");
        // holidayDateWMap.add("2017-1-1");
        Calendar cal = Calendar.getInstance();
        if (BaseStaticParameter.MORNING_WORK_ON_TIME != null) {
            cal.setTime(BaseStaticParameter.MORNING_WORK_ON_TIME);
            MORNING_WORK_ON_TIME_HOUR = cal.get(Calendar.HOUR_OF_DAY);
            MORNING_WORK_ON_TIME_MINUTE = cal.get(Calendar.MINUTE);
        }

        if (BaseStaticParameter.MORNING_WORK_OFF_TIME != null) {
            cal.setTime(BaseStaticParameter.MORNING_WORK_OFF_TIME);
            MORNING_WORK_OFF_TIME_HOUR = cal.get(Calendar.HOUR_OF_DAY);
            MORNING_WORK_OFF_TIME_MINUTE = cal.get(Calendar.MINUTE);
        }

        if (BaseStaticParameter.AFTERNOON_WORK_ON_TIME != null) {
            cal.setTime(BaseStaticParameter.AFTERNOON_WORK_ON_TIME);
            AFTERNOON_WORK_ON_TIME_HOUR = cal.get(Calendar.HOUR_OF_DAY);
            AFTERNOON_WORK_ON_TIME_MINUTE = cal.get(Calendar.MINUTE);
        }

        if (BaseStaticParameter.AFTERNOON_WORK_OFF_TIME != null) {
            cal.setTime(BaseStaticParameter.AFTERNOON_WORK_OFF_TIME);
            AFTERNOON_WORK_OFF_TIME_HOUR = cal.get(Calendar.HOUR_OF_DAY);
            AFTERNOON_WORK_OFF_TIME_MINUTE = cal.get(Calendar.MINUTE);
        }
    }

    /**
     * 根据日期以及天数、单位计算日期
     *
     * @param date
     *            需要计算的日期
     * @param timeLimit
     *            天数
     * @param timeUnit
     *            单位，N-自然日，W-工作日
     * @param timeLimitHour
     *            小时数
     * @return 计算后的日期
     */
    public static Date dateCalc(Date date, Integer timeLimit, String timeUnit,
            Integer timeLimitHour) {
        Date calDate = null;
        // 计算天数，当天数为0时，不计算
        if (timeLimit != null && timeLimit > 0) {
            if (BaseStaticParameter.HOLIDAY_NATURE.equals(timeUnit)) {
                calDate = DateUtil.addDate(date, timeLimit);
            } else {
                // 工作日日期计算，目前设置为自然日，以后扩展
                calDate = dateCalcWork(date, timeLimit);
            }
        } else {
            calDate = new Date(date.getTime());
        }

        // 计算小时数
        if (timeLimitHour != null && timeLimitHour > 0) {
            // 小时数通过分钟进行计算
            calDate = dateCalcHour(calDate, timeUnit, timeLimitHour * 60);
        }

        return calDate;
    }

    /**
     * 为日期添加小时数
     *
     * @param date
     *            日期
     * @param timeUnit
     *            单位，N-自然日，W-工作日
     * @param timeLimitMinute
     *            分钟数
     * @return 计算后的日期
     */
    private static Date dateCalcHour(Date date, String timeUnit,
            Integer timeLimitMinute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int calDateOldHour = cal.get(Calendar.HOUR_OF_DAY); // 原始时间的小时数
        int calDateOldMinute = cal.get(Calendar.MINUTE); // 原始时间的分钟数

        cal.add(Calendar.MINUTE, timeLimitMinute);

        int calDateHour = cal.get(Calendar.HOUR_OF_DAY);
        int calDateMinute = cal.get(Calendar.MINUTE);
        int timeCha = 0;    //时间差

        Calendar calWorkTime = Calendar.getInstance();
        calWorkTime.setTime(date);
        //两班次
        if (BaseStaticParameter.WORK_TIME_2
                .equals(BaseStaticParameter.WORK_TIME_NUM)) {
            // 当小时数大于下午下班时间的小时数,calDateHour > AFTERNOON_WORK_OFF_TIME_HOUR
            // 当小时数相同但分钟数大于时calDateHour == AFTERNOON_WORK_OFF_TIME_HOUR && calDateMinute > AFTERNOON_WORK_OFF_TIME_MINUTE
            // 计算超出的分钟数
            if (!DateUtil.formatDate(cal).equals(DateUtil.formatDate(date))
                    || (calDateHour > AFTERNOON_WORK_OFF_TIME_HOUR)
                    || (calDateHour == AFTERNOON_WORK_OFF_TIME_HOUR
                            && calDateMinute > AFTERNOON_WORK_OFF_TIME_MINUTE)) {
                calWorkTime.set(Calendar.HOUR_OF_DAY,
                        AFTERNOON_WORK_OFF_TIME_HOUR);
                calWorkTime.set(Calendar.MINUTE,
                        AFTERNOON_WORK_OFF_TIME_MINUTE);

                timeCha = DateUtil.dateDiff('m', cal, calWorkTime);
            }
        } else if (BaseStaticParameter.WORK_TIME_4
                .equals(BaseStaticParameter.WORK_TIME_NUM)) {
            // 当小时数大于上午下班时间的小时数,calDateHour > MORNING_WORK_ON_TIME_HOUR
            // 当小时数相同但分钟数大于时calDateHour == MORNING_WORK_ON_TIME_HOUR && calDateMinute > MORNING_WORK_ON_TIME_MINUTE
            // 如果开始时间在上午下班之前，结束时间在上午下班之后，需要将时间在加上中午的休息时间
            if ((calDateOldHour < MORNING_WORK_OFF_TIME_HOUR)
                    || (calDateOldHour == MORNING_WORK_OFF_TIME_HOUR
                            && calDateOldMinute <= MORNING_WORK_OFF_TIME_MINUTE)) { // 原始时间在上班下班之前的

                if (!DateUtil.formatDate(cal).equals(DateUtil.formatDate(date))
                        || (calDateHour > MORNING_WORK_OFF_TIME_HOUR)
                        || (calDateHour == MORNING_WORK_OFF_TIME_HOUR
                                && calDateMinute > MORNING_WORK_OFF_TIME_MINUTE)) {// 计算后的时间在上午下班之后的，加上中午休息时间
                    cal.add(Calendar.MINUTE,
                            BaseStaticParameter.WORK_REST_MINTUS);
                }
            }

            calDateHour = cal.get(Calendar.HOUR_OF_DAY);
            calDateMinute = cal.get(Calendar.MINUTE);
            // 当小时数大于下午下班时间的小时数,calDateHour > AFTERNOON_WORK_OFF_TIME_HOUR
            // 当小时数相同但分钟数大于时calDateHour == AFTERNOON_WORK_OFF_TIME_HOUR && calDateMinute > AFTERNOON_WORK_OFF_TIME_MINUTE
            // 计算超出的分钟数
            if (!DateUtil.formatDate(cal).equals(DateUtil.formatDate(date))
                    || (calDateHour > AFTERNOON_WORK_OFF_TIME_HOUR)
                    || (calDateHour == AFTERNOON_WORK_OFF_TIME_HOUR
                            && calDateMinute > AFTERNOON_WORK_OFF_TIME_MINUTE)) {
                calWorkTime.set(Calendar.HOUR_OF_DAY,
                        AFTERNOON_WORK_OFF_TIME_HOUR);
                calWorkTime.set(Calendar.MINUTE,
                        AFTERNOON_WORK_OFF_TIME_MINUTE);
                timeCha = DateUtil.dateDiff('m', cal, calWorkTime);
            }
        }
        // 如果时间差大于0，说明超过了当天时间，需要将日期添加1后在计算时间
        if (timeCha > 0) {
            //判断计算后的时间是否与原始时间在同一天，如果不在同一天，需要将日期设置为原始的日期
            if (!DateUtil.formatDate(cal).equals(DateUtil.formatDate(date))) {
                Calendar oldCal = Calendar.getInstance();
                oldCal.setTime(date);
                cal.set(Calendar.YEAR, oldCal.get(Calendar.YEAR));
                cal.set(Calendar.MONTH, oldCal.get(Calendar.MONTH));
                cal.set(Calendar.DAY_OF_MONTH,
                        oldCal.get(Calendar.DAY_OF_MONTH));
            }

            // 设置小时分钟为上午上班时间
            cal.set(Calendar.HOUR_OF_DAY, MORNING_WORK_ON_TIME_HOUR);
            cal.set(Calendar.MINUTE, MORNING_WORK_ON_TIME_MINUTE);

            if (BaseStaticParameter.HOLIDAY_NATURE.equals(timeUnit)) {
                cal.setTime(DateUtil.addDate(cal.getTime(), 1));
            } else {
                // 工作日日期计算，目前设置为自然日，以后扩展
                cal.setTime(dateCalcWork(cal.getTime(), 1));
            }

            // 再次添加分钟数
            cal.setTime(dateCalcHour(cal.getTime(), timeUnit, timeCha));
        }
        return cal.getTime();
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
    public static Date dateCalcWork(Date date, Integer timeLimit) {
        Date calDate = null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (timeLimit > 0) {
            for (int i = 0; i < timeLimit; i++) {
                cal.setTime(dateCalcWorkAddOne(cal.getTime()));
            }
            calDate = cal.getTime();
        }else{
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
    private static Date dateCalcWorkAddOne(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        Date calcDate = cal.getTime();
        String calcDateStr = new SimpleDateFormat("yyyy-MM-dd").format(calcDate);
        //String calcDateStr = sdf.format(calcDate);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w <= 5 && w >= 1) { // 周一二三四五
            if (holidayDateHMap.contains(calcDateStr)) {
                return dateCalcWorkAddOne(calcDate);
            }
        } else { // 周六日
            if (!holidayDateWMap.contains(calcDateStr)) {
                return dateCalcWorkAddOne(calcDate);
            }
        }
        return calcDate;
    }
    /**
     * 检查是否是工作日
     * @author dongxl
     * @date 2018年3月23日
     * @param date
     * @return
     */
    public static boolean checkWorkDayForNowDate(Date date) {
		boolean flag = false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Date calcDate = cal.getTime();
		String calcDateStr = sdf.format(calcDate);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w <= 5 && w >= 1) { // 周一二三四五
			if (!holidayDateHMap.contains(calcDateStr)) {
				flag = true;
			}
		} else { // 周六日
			if (holidayDateWMap.contains(calcDateStr)) {
				flag = true;
			}
		}
		return flag;
	}
    /**
     * 求两个日期之间的工作日
     * @author dongxl
     * @date 2017年10月2日
     * @return
     */
    public static int weekDaysBetweenDate(Date startTime,Date endTime){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	 int rs = 0;
		try {
			//格式化时间
			    String startformat=sdf.format(startTime);
			    String endformat=sdf.format(endTime);
		        Date begDate=sdf.parse(startformat);
		        Date endDate = sdf.parse(endformat);
		     // 总天数
		        int days = (int) ((endDate.getTime() - begDate.getTime()) / (24 * 60 * 60 * 1000)) + 1;
		        // 总周数，
		        int weeks = days / 7;
		        // 整数周
		        if (days % 7 == 0) {
		            rs = days - 2 * weeks;
		        }
		        else {
		            Calendar begCalendar = Calendar.getInstance();
		            Calendar endCalendar = Calendar.getInstance();
		            begCalendar.setTime(begDate);
		            endCalendar.setTime(endDate);
		            // 周日为1，周六为7
		            int beg = begCalendar.get(Calendar.DAY_OF_WEEK);
		            int end = endCalendar.get(Calendar.DAY_OF_WEEK);
		            if (beg > end) {
		                rs = days - 2 * (weeks + 1);
		            } else if (beg < end) {
		                if (end == 7) {
		                    rs = days - 2 * weeks - 1;
		                } else {
		                    rs = days - 2 * weeks;
		                }
		            } else {
		                if (beg == 1 || beg == 7) {
		                    rs = days - 2 * weeks - 1;
		                } else {
		                    rs = days - 2 * weeks;
		                }
		            }
		        }
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return rs;
    }


    //计算周六周末
    public static int computeHolidays(Date t1,Date t2) {
        //初始化第一个日期
        Calendar cal1 = Calendar.getInstance();
        //初始化第二个日期，这里的天数可以随便的设置
        Calendar cal2 = Calendar.getInstance();

        // 设置传入的时间格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 指定一个日期
        Date date1 = t1;
        Date date2 = t2;
        // 对 calendar 设置为 date 所定的日期
        cal1.setTime(date1);
        cal2.setTime(date2);

        int holidays = 0;
        //确定一个 大日期
        if(cal1.compareTo(cal2) > 0){
            Calendar temp = cal1;
            cal1 = cal2;
            cal2 = temp;
            temp = null;
        }
        while(cal1.compareTo(cal2)<=0){
            if(cal1.get(Calendar.DAY_OF_WEEK)==1||cal1.get(Calendar.DAY_OF_WEEK)==7){
                holidays++;
                System.out.println("周末："+new SimpleDateFormat("yyyy-MM-dd").format(cal1.getTime()));
            }
            cal1.add(Calendar.DAY_OF_YEAR,1);
        }
        return holidays;
    }
    public static void main(String[] args) {
        Date date = LimitDateCalc.dateCalcWork(new Date(), 16);
        if (date != null) {
            System.out.println(sdf.format(date));
        }
    }
}
