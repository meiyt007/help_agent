package com.zfsoft.ha.util;

import com.alibaba.fastjson.JSONObject;
import com.zfsoft.platform.utils.data.SysConfig;
import com.zfsoft.platform.utils.feign.SysConfigFeignService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.zfsoft.cases.constant.Constants.HA_JJR;
import static com.zfsoft.cases.constant.Constants.HA_SCHE_APPOIN_NUM_MAX;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/9/23 12:11
 */
@Component
public class CalenderUtils {


//    @Resource
    private static SysConfigFeignService sysConfigFeignService;

    //将@Autowired加到构造方法上
    @Autowired
    private CalenderUtils(SysConfigFeignService config){
        CalenderUtils.sysConfigFeignService = config;
    }
    public static final Map<String, String> WEEKMAP = new HashMap<>(7);

    public static final Map<String, String> SCHEDULE = new HashMap<>(7);
    static {
        WEEKMAP.put("1", "星期日");
        WEEKMAP.put("2", "星期一");
        WEEKMAP.put("3", "星期二");
        WEEKMAP.put("4", "星期三");
        WEEKMAP.put("5", "星期四");
        WEEKMAP.put("6", "星期五");
        WEEKMAP.put("7", "星期六");
        //周一到到五默认全年，周六周日默认休息   '午别;1-上午，2-下午，3-全天，4休息'
        SCHEDULE.put("1", "4");
        SCHEDULE.put("2", "3");
        SCHEDULE.put("3", "3");
        SCHEDULE.put("4", "3");
        SCHEDULE.put("5", "3");
        SCHEDULE.put("6", "3");
        SCHEDULE.put("7", "4");
    }

    /**
     * 根据年 月 获取对应的月份 天数
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        //上一个月的月份 日数-1，就是当月的
        a.set(Calendar.MONTH, month);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        return a.get(Calendar.DATE);
    }

    /**
     * java获取国家法定节假日和周末
     *
     * @param year
     * @param month
     * @return
     */
    public static Set<String> JJR(int year, int month) {
        //获取所有的周末
        Set<String> monthWekDay = getMonthWekDay(year, month);
        //http://timor.tech/api/holiday api文档地址
        Map jjr = getJjr(year, month + 1);
        Integer code = (Integer) jjr.get("code");
        if (code != 0) {
            return monthWekDay;
        }
        Map<String, Map<String, Object>> holiday = (Map<String, Map<String, Object>>) jjr.get("holiday");
        Set<String> strings = holiday.keySet();
        for (String str : strings) {
            Map<String, Object> stringObjectMap = holiday.get(str);
            Integer wage = (Integer) stringObjectMap.get("wage");
            String date = (String) stringObjectMap.get("date");
            //筛选掉补班
            if (wage.equals(1)) {
                monthWekDay.remove(date);
            } else {
                monthWekDay.add(date);
            }
        }
        return monthWekDay;
    }

    /**
     * 获取节假日不含周末  互联网方式获取
     *
     * @param year
     * @param month
     * @return

    private static Map getJjr(int year, int month) {
        String url = "http://timor.tech/api/holiday/year/";
        OkHttpClient client = new OkHttpClient();
        Response response;
        //解密数据
        String rsa = null;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                //模拟postman请求，不加会被识别为恶意攻击
                .addHeader("User-Agent","PostmanRuntime/7.26.8")
//        connection.setRequestProperty("User-Agent", "PostmanRuntime/7.26.8");
                .build();
        try {
            response = client.newCall(request).execute();
            rsa = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSONObject.parseObject(rsa, Map.class);
    }     */

    private static Map getJjr(int year, int month) {
        String jjr = "";
        try {
            //获取系统配置的帮办人员每日各时间段最大的预约数
            SysConfig sysConfig = sysConfigFeignService.getSysConfigByCode(HA_JJR+year).getData();

            if(null!=sysConfig){
                jjr = sysConfig.getMemo();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONObject.parseObject(jjr, Map.class);
    }
    /**
     * 获取周末  月从0开始
     *
     * @param year
     * @param month
     * @return
     */
    public static Set<String> getMonthWekDay(int year, int month) {
        Set<String> dateList=new HashSet<String>();
        SimpleDateFormat simdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = new GregorianCalendar(year, month, 1);
        int i = 1;
        while (calendar.get(Calendar.YEAR) < year + 1) {
            calendar.set(Calendar.WEEK_OF_YEAR, i++);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            if (calendar.get(Calendar.YEAR) == year) {
//                System.out.println("周日："+simdf.format(calendar.getTime()));
                dateList.add(simdf.format(calendar.getTime()));
            }
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            if (calendar.get(Calendar.YEAR) == year) {
//                System.out.println("周六："+simdf.format(calendar.getTime()));
                dateList.add(simdf.format(calendar.getTime()));
            }
        }
        return dateList;
    }

}
