package com.zfsoft.single.util;


import com.zfsoft.platform.common.data.BaseStaticParameter;

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

    {
        Calendar cal = Calendar.getInstance();
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
    public Date dateCalcWork(Date date, Integer timeLimit) {
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
    private Date dateCalcWorkAddOne(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        Date calcDate = cal.getTime();
        String calcDateStr = sdf.format(calcDate);
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

	/**
	 * 计算加上几天后的工作日日期
	 * @param currentDate
	 * @param days
	 * @return
	 */
	public Date getDate(Date currentDate,int days){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(currentDate);
		int i=0;
		while(i<days){
			calendar.add(Calendar.DATE,1);
			i++;
		 if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY|| calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
			i--;
				}
			}
			return calendar.getTime();
		}
}
