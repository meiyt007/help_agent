package com.zfsoft.platform.utils.web;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description:  常用方法的处理
 * @author: wuxx
 * @Date: 2020/10/29 20:16
 **/
public class CommonUtil {

    /**
     * 身份证号混淆
     * @param idCard
     * @return
     */
    public static  String idCardEncode(String idCard){
        if(StrUtil.isEmpty(idCard)) return "";
        if(idCard.toUpperCase().endsWith("X")){
            return idCard.substring(0,4)+"********"+idCard.substring(idCard.length()-4);
        }
        return idCard.replaceAll("(\\d{4})\\d{10}(\\d{4})", "$1********$2");
    }

    /**
     * 手机号混淆
     * @param phone
     * @return
     */
    public static  String mobileEncode(String phone){
        if(StrUtil.isEmpty(phone)) return "";
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
    }

    /**
     * 邮箱混淆
     * @param email
     * @return
     */
    public static  String emailEncode(String email){
        if(StrUtil.isEmpty(email)) return "";
        return email.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
    }

    /**
     * 座机混淆
     * @param phone
     * @return
     */
    public static  String phoneEncode(String phone){
        if(StrUtil.isEmpty(phone)) return "";
        if(phone.contains("-")){
            return phone.substring(0,phone.indexOf("-")+3)+"****"+phone.substring(phone.length()-2);
        }
        if(phone.length()<4){
            return phone;
        }
        if(phone.length()<11){
            return phone.substring(0,3)+"****"+phone.substring(phone.length()-2);
        }
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
    }

    /*
     *  正则：手机号（简单）, 1字头＋10位数字即可.
     * @param in
     * @return
     */
    public static boolean validateMobilePhone(String in) {
        Pattern pattern = Pattern.compile("^[1]\\d{10}$");
        return pattern.matcher(in).matches();
    }

    /**
     * 名字脱敏
     *规则，张三丰，脱敏为：张*丰
     *@param name
     *@return
     *      
     */
    public static String nameDesensitization(String name) {
        if (StrUtil.isEmpty(name)) {
            return "";
        }
        // 获取姓名长度
        String custName = name;
        int length = custName.length();

        String reg = ".{1}";
        StringBuffer sb = new StringBuffer();
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(name);
        int i = 0;
        while (m.find()) {
            i++;
            if (i == length)
                continue;
            m.appendReplacement(sb, "*");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    
    /**
	 * 获取两个日期间的日期
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<Date> getBetweenDates(Date start, Date end) {
		List<Date> result = new ArrayList<Date>();
        while (start.getTime() <= end.getTime()) {//包含开始与结束时间
            result.add(start);
            start = DateUtil.offsetDay(start, 1);
        }
        return result;
	}
	
	/**
	 * 根据日期获取星期
	 * @param date
	 * @return
	 */
    public static String dateToWeek(Date date) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        //一周的第几天
        int w = DateUtil.dayOfWeek(date);
        return weekDays[w - 1];
    }


    /**
     * 判断请求是否为Ajax请求
     *
     * @param request
     *            请求对象
     * @return Ajax请求-true，否则为false
     */
    public static boolean isAjaxRequest(ServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"));
    }
}
