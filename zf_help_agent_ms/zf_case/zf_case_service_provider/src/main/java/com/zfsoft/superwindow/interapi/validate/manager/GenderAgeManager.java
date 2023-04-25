package com.zfsoft.superwindow.interapi.validate.manager;

import com.zfsoft.superwindow.interapi.validate.manager.base.AbstractInterApiValidateStrategy;
import com.zfsoft.superwindow.service.easyquickcase.data.ResponseData;
import com.zfsoft.superwindow.service.interapi.data.ApiReqParams;
import com.zfsoft.superwindow.service.interapi.service.InterApiValidateStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author ChangSheng
 * @Date 9:57 2022/6/24
 * @Description 公积金验证
 **/
@Service
@Slf4j
public class GenderAgeManager extends AbstractInterApiValidateStrategy implements InterApiValidateStrategy {

    private List<String> okay;

    @Override
    public String getApiCode() {
        return "genderAge";
    }

    public GenderAgeManager() { register(); }

    @Override
    public ResponseData validate(ApiReqParams params) {
        ResponseData<Boolean> response = new ResponseData<>();

        if (StringUtils.isEmpty(params.getUniqueCode())) {
            response.error("身份证号不能为空");
            response.setData(false);
            return response;
        }

        //直接通过
        if(okay.contains(params.getUniqueCode())){
            response.success(true);
            return response;
        }

        //第一个判断身份证号填进来，性别年龄是否符合
        String cardNum = params.getUniqueCode();
        boolean flag = false;
        try {
            Map<String ,Object> data;
            //判断身份证位数选择调用那个方法
            if(cardNum.length()>16){
                data = getCarInfoEiw(cardNum);
            }else{
                data = getCarInfoFiw(cardNum);
            }
            //得到结果获取性别和年龄
            String sex = data.get("sex").toString();
            int age = (Integer) data.get("age");
            if("男".equals(sex)){
                if(age>60){
                    flag = true;
                }else{
                    response.setCode(200);
                    response.setSuccess(true);
                    response.setMessage("检测身份证信息，性别男，年龄不符合大于60周岁以上条件");
                    response.setData(flag);
                    return response;
                }
            }else{
                if(age>55){
                    flag = true;
                } else {
                    response.setCode(200);
                    response.setSuccess(true);
                    response.setMessage("检测身份证信息，性别女，年龄不符合大于55周岁以上条件");
                    response.setData(flag);
                    return response;
                }

            }
        }catch (Exception e){
            response.error("性别年龄判断报错："+e.getMessage());
            response.setData(false);
            return response;
        }
        response.success(flag);
        return response;
    }

    /**
     * 18位身份证
     * 根据身份证的号码算出当前身份证持有者的性别和年龄
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getCarInfoEiw(String CardCode) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String year = CardCode.substring(6).substring(0, 4);// 得到年份
        String yue = CardCode.substring(10).substring(0, 2);// 得到月份
        String day = CardCode.substring(12).substring(0, 2);//得到日
        String sex;
        if (Integer.parseInt(CardCode.substring(16).substring(0, 1)) % 2 == 0) {// 判断性别
            sex = "女";
        } else {
            sex = "男";
        }
        Date date = new Date();// 得到当前的系统时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fyear = format.format(date).substring(0, 4);// 当前年份
        String fyue = format.format(date).substring(5, 7);// 月份
        String fday = format.format(date).substring(8, 10);
        int age = 0;

        if (Integer.parseInt(fyear) > Integer.parseInt(year)) {
            // 当前年份大于计算年份
            if (Integer.parseInt(fyue) > Integer.parseInt(yue)) {
                // 当前月份大于计算月份，年龄为两年之差
                age = Integer.parseInt(fyear) - Integer.parseInt(year);
            } else if (Integer.parseInt(fyue) < Integer.parseInt(yue)) {
                // 当前月份小于计算月份，年龄为两年之差 - 1
                age = Integer.parseInt(fyear) - Integer.parseInt(year) - 1;
            } else if (Integer.parseInt(fyue) == Integer.parseInt(yue)) {
                // 当前月份等于计算月份
                if (Integer.parseInt(fday) > Integer.parseInt(day)) {
                    // 当前日大于计算日，年龄为两年之差
                    age = Integer.parseInt(fyear) - Integer.parseInt(year);
                } else {
                    // 当前日小于或等于计算日，年龄为两年之差 - 1
                    age = Integer.parseInt(fyear) - Integer.parseInt(year) - 1;
                }
            }
        }
        map.put("sex", sex);
        map.put("age", age);
        return map;
    }

    /**
     * 15位身份证的验证
     * 根据身份证的号码算出当前身份证持有者的性别和年龄
     *
     * @param
     * @throws Exception
     */
    public static Map<String, Object> getCarInfoFiw(String card)
            throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String uyear = "19" + card.substring(6, 8);// 年份
        String uyue = card.substring(8, 10);// 月份
        String uday = card.substring(10, 12);//日
        String usex = card.substring(14, 15);// 用户的性别
        String sex;
        if (Integer.parseInt(usex) % 2 == 0) {
            sex = "女";
        } else {
            sex = "男";
        }
        Date date = new Date();// 得到当前的系统时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fyear = format.format(date).substring(0, 4);// 当前年份
        String fyue = format.format(date).substring(5, 7);// 月份
        String fday = format.format(date).substring(8, 10);
        int age = 0;
        if (Integer.parseInt(fyear) > Integer.parseInt(uyear)) {
            // 当前年份大于计算年份
            if (Integer.parseInt(fyue) > Integer.parseInt(uyue)) {
                // 当前月份大于计算月份，年龄为两年之差
                age = Integer.parseInt(fyear) - Integer.parseInt(uyear);
            } else if (Integer.parseInt(fyue) < Integer.parseInt(uyue)) {
                // 当前月份小于计算月份，年龄为两年之差 - 1
                age = Integer.parseInt(fyear) - Integer.parseInt(uyear) - 1;
            } else if (Integer.parseInt(fyue) == Integer.parseInt(uyue)) {
                // 当前月份等于计算月份
                if (Integer.parseInt(fday) > Integer.parseInt(uday)) {
                    // 当前日大于计算日，年龄为两年之差
                    age = Integer.parseInt(fyear) - Integer.parseInt(uyear);
                } else {
                    // 当前日小于或等于计算日，年龄为两年之差 - 1
                    age = Integer.parseInt(fyear) - Integer.parseInt(uyear) - 1;
                }
            }
        }
        map.put("sex", sex);
        map.put("age", age);
        return map;
    }
}
