package com.zfsoft.outer.inter.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.outer.inter.pojo.ApiResultSet;
import com.zfsoft.outer.inter.pojo.HaInterRecord;
import com.zfsoft.outer.inter.pojo.SmsProperties;
import com.zfsoft.outer.inter.util.ClientServer;
import com.zfsoft.outer.inter.util.InterRecordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
* Description: 短信接口业务
* @author zhaobf
* date: 2023/1/6 10:29
*/
@Slf4j
@RestController
@RequestMapping("/outer/sms")
public class SmsController {
    @Resource
    private SmsProperties properties;

    @PostMapping("/sendMessage")
    public ApiResultSet sendMessage(String phone,String message,String workUserName) {
        HaInterRecord interRecord = new HaInterRecord();
        Map<String, Object> params = new HashMap<>(2);
        String url = properties.getUrl();
        params.put("token", properties.getToken());
        params.put("phone", phone);
        params.put("message", message);
        params.put("displayName", workUserName);
        params.put("displayOu", properties.getDisplayOu());
        params.put("unit", properties.getUnit());
        params.put("msgusage", properties.getMsgusage());
        //封装token请求头
        Long begin1 = System.currentTimeMillis();
        JSONObject jsonObject;
        try {
            String result = ClientServer.sendSms( url, JSON.toJSONString(params),"utf-8");
            interRecord.setName("短信：发送短信");
            interRecord.setUrl(url);
            interRecord.setMethod("post");
            interRecord.setSourceIp("station.huangpuqu.gov.cn");
            interRecord.setParam(params.toString());
            interRecord.setResult(result.substring(0, Math.min(result.length(),3000)));
            interRecord.setResultStatus("1");

            jsonObject = JSONObject.parseObject(result);
            System.out.println("短信：发送短信:"+jsonObject);
            if (jsonObject.get("status").toString()==null||jsonObject.get("status").toString().isEmpty()) {
                throw new Exception("第三方接口调用错误");
            }

        } catch (Exception ex) {
            interRecord.setResultStatus("2");
            interRecord.setResult("短信：发送短信调用失败" + (ex.getMessage().substring(0, Math.min(ex.getMessage().length()-1,3000))));
            return new ApiResultSet<>(500, "短信：发送短信错误", ex.getMessage());
        } finally {
            Long totalTime = System.currentTimeMillis() - begin1;
            interRecord.setTotalTime(totalTime);
            InterRecordUtil.interRecord(interRecord);
        }
        return ApiResultSet.ok("短信：发送短信", jsonObject);
    }
}
