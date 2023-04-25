package com.zfsoft.microservice.platform.util;

import org.springframework.util.StringUtils;

import java.util.Calendar;

public class IdCardUtil {
    /** 中国公民身份证号码旧证长度。 */
    private  final int CHINA_ID_OLD_LENGTH = 15;

    /** 中国公民身份证号码新证长度。 */
    private  final int CHINA_ID_NEW_LENGTH = 18;

    /**
     * 检查身份证号是否合格(当前仅检查位数和校验码位)
     * @param idCardNO
     * @return true-合格  false-不合格
     * @author zje
     */
    public static boolean checkIdCard(String idCardNO){
        if (StringUtils.isEmpty(idCardNO)){
            return false;
        }
        String regxId = "([0-9]{17}([0-9]|X|x))|([0-9]{15})";
        if (!idCardNO.matches(regxId)){
            return false;
        }
        if (idCardNO.length() == 15){
            idCardNO = transIDCard15to18(idCardNO);
        }
        return checkIdCardLastNum(idCardNO);
    }

    /**
     * 15位身份证转化为18位标准证件号
     * @param idCardNO
     * @return 18位标准身份证号
     * @author zje
     */
    private static String transIDCard15to18(String idCardNO){
        String cardNo=null;
        if(null!=idCardNO&&idCardNO.trim().length()==15){
            idCardNO=idCardNO.trim();
            StringBuffer sb=new StringBuffer(idCardNO);
            sb.insert(6, "19");
            sb.append(transCardLastNo(sb.toString()));
            cardNo=sb.toString();
        }
        return cardNo;
    }
    /**
     * 15位补全‘19’位后的身份证号码
     * @param newCardId
     * @return 返回带验证码(最后一位)的身份证号
     * @author zje
     */
    private static String transCardLastNo(String newCardId){
        char[] ch=newCardId.toCharArray();
        int m=0;
        int [] co={7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};
        char [] verCode=new char[]{'1','0','X','9','8','7','6','5','4','3','2'};
        for (int i = 0; i < newCardId.length(); i++) {
            m+=(ch[i]-'0')*co[i];
        }
        int residue=m%11;
        return String.valueOf(verCode[residue]);
    }

    /**
     * 校验身份证最后一位校验码是否正确(基于18位号码)
     *
     * @param idCard 身份编号
     * @return true-正确   false-校验码错误
     */
    private static boolean checkIdCardLastNum(String idCard){
        String preStr = idCard.substring(0,17);
        String endStr = idCard.substring(17);
        return transCardLastNo(preStr).equalsIgnoreCase(endStr);
    }

    /**
     * 根据身份编号获取年龄(基于18位号码)
     *
     * @param idCard 身份编号
     * @return 年龄
     */
    public static int getAgeByIdCard(String idCard) {
        int iAge = 0;
        Calendar cal = Calendar.getInstance();
        String year = idCard.substring(6, 10);
        int iCurrYear = cal.get(Calendar.YEAR);
        iAge = iCurrYear - Integer.valueOf(year);
        return iAge;
    }

    /**
     * 根据身份编号获取生日(基于18位号码)
     *
     * @param idCard 身份编号
     * @return 生日(yyyyMMdd)
     */
    public static String getBirthByIdCard(String idCard) {
        return idCard.substring(6, 14);
    }

    /**
     * 根据身份编号获取生日年(基于18位号码)
     *
     * @param idCard 身份编号
     * @return 生日(yyyy)
     */
    public static Short getYearByIdCard(String idCard) {
        return Short.valueOf(idCard.substring(6, 10));
    }

    /**
     * 根据身份编号获取生日月(基于18位号码)
     *
     * @param idCard 身份编号
     * @return 生日(MM)
     */
    public static Short getMonthByIdCard(String idCard) {
        return Short.valueOf(idCard.substring(10, 12));
    }

    /**
     * 根据身份编号获取生日天(基于18位号码)
     *
     * @param idCard 身份编号
     * @return 生日(dd)
     */
    public static Short getDateByIdCard(String idCard) {
        return Short.valueOf(idCard.substring(12, 14));
    }

    /**
     * 根据身份编号获取性别(基于18位号码)
     *
     * @param idCard 身份编号
     * @return 性别(M-男，F-女，N-未知)
     */
    public static String getGenderByIdCard(String idCard) {
        String sGender = "未知";

        String sCardNum = idCard.substring(16, 17);
        if (Integer.parseInt(sCardNum) % 2 != 0) {
            sGender = "1";//男
        } else {
            sGender = "2";//女
        }
        return sGender;
    }
}
