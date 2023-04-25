package com.zfsoft.outer.interWeb.controller;

import com.alibaba.fastjson.JSON;
import com.zfsoft.outer.interWeb.pojo.smsParams;
import com.zfsoft.outer.interWeb.util.ClientServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value = "/web/inter")
public class WebController {
    @Value("${weather.url}")
    private  String weatherUrl;

    @Value("${weather.areacode}")
    private  String areacode;

    @Value("${weather.key}")
    private  String key;

    @Value("${sms.url}")
    private  String smsUrl;

    @Value("${sms.token}")
    private  String smsToken;

    @ResponseBody
    @GetMapping(value = "/getWeather")
    public String getWeather(){
        Map<String, String> parameters = new HashMap<>();
        //4.如果逻辑过期或第一次获取天气，则查询天气接口获取当前天气
        parameters.put("areacode",areacode);
        parameters.put("key",key);
        Map<String, String> requestHeader = new HashMap<>();

        return  ClientServer.sendGet( weatherUrl,requestHeader,parameters,"utf-8");
    }


    @ResponseBody
    @PostMapping(value = "/shhpewb")
    public String sendSms(@RequestBody smsParams params){
        String result = null;
        try {
            result = ClientServer.sendSms( smsUrl, JSON.toJSONString(params),"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
