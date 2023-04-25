package com.zfsoft.single.util;

import com.zfsoft.ha.util.CalenderUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.zfsoft.cases.util.DateUtil.PATTEN_DEFAULT_YMD;

public class CalculateWorkDaysUtil {

    /*
      特殊的工作日(星期六、日工作)
              */
    public static List<String> SPECIAL_WORK_DAYS = new ArrayList<>();


    /*   特殊的休息日(星期一到五休息)
     */
    public static List<String> SPECIAL_REST_DAYS = new ArrayList<>();


    public static int getworkDays(String strStartDate, String strEndDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cl1 = Calendar.getInstance();
        Calendar cl2 = Calendar.getInstance();

        try {
            cl1.setTime(df.parse(strStartDate));
            cl2.setTime(df.parse(strEndDate));

        } catch (ParseException e) {
            System.out.println("日期格式非法");
            e.printStackTrace();
        }

        int count = 0;
        while (cl1.compareTo(cl2) <= 0) {
            //如果不是周六或者周日则工作日+1
            if (cl1.get(Calendar.DAY_OF_WEEK) != 7 && cl1.get(Calendar.DAY_OF_WEEK) != 1) {
                count++;
                //如果不是周六或者周日，但是该日属于国家法定节假日或者特殊放假日则-1
                if (SPECIAL_REST_DAYS.contains(DateFormatUtils.format(cl1.getTime(), "yyyy-MM-dd"))) {
                    count--;
                }
            }
            //如果是周六或者周日，但是该日属于需要工作的日子则 +1
            if (SPECIAL_WORK_DAYS.contains(DateFormatUtils.format(cl1.getTime(), "yyyy-MM-dd"))) {
                count++;
            }
            cl1.add(Calendar.DAY_OF_MONTH, 1);
        }
        return count;
    }

    /**
     * 获取当前时间之后n日中有多少个工作日和节假日
     *
     * @param strStartDate 当前日期
     * @param num          后n日
     * @return
     * @throws Exception
     */
    public static String getWorkDayStart(String strStartDate, int num) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(df.parse(strStartDate));
        calendar.add(Calendar.DATE, num);
        //节假日数
        int holidayNum = 0;
        //工作日数
        int workdayNum = 0;
        int number = (num + 1) % 7;
        if (number != 0) {
            for (int i = 0; i < number; i++) {
                if (calendar.get(Calendar.DAY_OF_WEEK) == 7 || calendar.get(Calendar.DAY_OF_WEEK) == 1) {
                    holidayNum++;
                } else {
                    workdayNum++;
                }
                calendar.add(Calendar.DATE, -1);
            }
        }
        //当前未排除法定节假日和法定工作日。
        holidayNum = holidayNum + 2 * (num - number + 1) / 7;
        workdayNum = workdayNum + 5 * (num - number + 1) / 7;
        return holidayNum + "," + workdayNum;
    }

    /**
     * java获取一段时间内的法定工作日
     *
     * @param strStartDate
     * @param num
     * @return
     */
    public static int FDGZR(String strStartDate, int num) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(df.parse(strStartDate));
//        calendar.add(Calendar.DATE, num);
        //节假日数
        int holidayNum = 0;
        //工作日数
        int workdayNum = 0;
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        //http://timor.tech/api/holiday api文档地址
        //获取国家法定节假日和周末
        Set<String> jjr = CalenderUtils.JJR(year, month - 1);
        for (int i = 1; i <= num; i++) {
            calendar.set(Calendar.DATE, i);
            SimpleDateFormat sf = new SimpleDateFormat(PATTEN_DEFAULT_YMD);
            //获取今天的日期
            String nowDay = sf.format(calendar.getTime());
            if (jjr.contains(nowDay)) {
                holidayNum++;
            } else {
                workdayNum++;
            }
        }
//        System.out.println(workdayNum);
//        System.out.println(holidayNum);
        return workdayNum;
    }

    /**
     * 获取一段时间内的天数
     *
     * @param strStartDate
     * @param strEndDate
     * @return
     */
    public static int addDates(String strStartDate, String strEndDate) {
        List<String> list = new ArrayList<>();
        String[] dateBegs = strStartDate.split("-");
        String[] dateEnds = strEndDate.split("-");
        Calendar start = Calendar.getInstance();
        start.set(Integer.valueOf(dateBegs[0]), Integer.valueOf(dateBegs[1]) - 1, Integer.valueOf(dateBegs[2]));
        Long startTIme = start.getTimeInMillis();
        Calendar end = Calendar.getInstance();
        end.set(Integer.valueOf(dateEnds[0]), Integer.valueOf(dateEnds[1]) - 1, Integer.valueOf(dateEnds[2]));
        Long endTime = end.getTimeInMillis();
        Long oneDay = 1000 * 60 * 60 * 24l;
        Long time = startTIme;
        while (time <= endTime) {
            Date d = new Date(time);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            time += oneDay;
            list.add(df.format(d));
        }
        return list.size();
    }

    public static void main(String[] args) throws Exception {
        String strStartDate = "2022-10-01";
        String strEndDate = "2022-10-31";
        int dayCount = addDates(strStartDate, strEndDate);
        System.out.println(dayCount);
        System.out.println(getWorkDayStart(strStartDate, dayCount - 1));
        System.out.println(FDGZR(strStartDate, dayCount));
    }
}
