package com.zfsoft.outer.inter.controller;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSON;
import com.zfsoft.outer.inter.pojo.ApiResultSet;
import com.zfsoft.outer.inter.pojo.HaInterRecord;
import com.zfsoft.outer.inter.util.ClientServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2023/1/5 10:58
 */
@Slf4j
@Controller
@RequestMapping("/outer/weather")
public class WeatherController {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private RedisTemplate redisTemplate;

    @Value("${weather.url}")
    private  String weatherUrl;

    @Value("${weather.areacode}")
    private  String areacode;

    @Value("${weather.key}")
    private  String key;

    private static  final String WEATHER_REDIS = "Inter:Weather";

    @ResponseBody
    @GetMapping(value = "/getWeather")
    public ApiResultSet getWeather() {
        HaInterRecord interRecord = new HaInterRecord();
        Map<Object, Object> stringObjectMap;
        Map<String, String> parameters = new HashMap<>();
        String url = weatherUrl;
        long begin1 = System.currentTimeMillis();
        try {
            //1.先查询redis中天气的过期时间(逻辑过期)
            if (redisTemplate.hasKey(WEATHER_REDIS)) {
                //2.如果redis存在天气
                long timestamp = (long) redisTemplate.opsForHash().get(WEATHER_REDIS, "timestamp");
                if (timestamp >= new Date().getTime()) {
                    //3.如果没有逻辑过期，则返回redis的天气
                    stringObjectMap = redisTemplate.opsForHash().entries(WEATHER_REDIS);
                    return ApiResultSet.ok("天气：获取黄浦天气成功", stringObjectMap);
                }
            }
            //4.如果逻辑过期或第一次获取天气，则查询天气接口获取当前天气
            parameters.put("areacode",areacode);
            parameters.put("key",key);
            Map<String, String> requestHeader = new HashMap<>();

            String result = ClientServer.sendGet( url,requestHeader,parameters,"utf-8");
            stringObjectMap = JSON.parseObject(result, HashMap.class);
            long time = new Date().getTime() + 1000 * 60;
            stringObjectMap.put("timestamp",time);
            String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
            stringObjectMap.put("overdueTime",nowTime);
//            JSONObject jsonObject1 = JSON.parseObject(result);
            int status = (int) stringObjectMap.get("status");
            if(0==status){
                //5.如果获取到天气信息，则将天气信息存入redis中，更新redis天下
                redisTemplate.opsForHash().putAll(WEATHER_REDIS,stringObjectMap);
            }else{
                //6.如果没有获取到天气信息，则返回之前redis中的天气（已经过期）
                stringObjectMap = redisTemplate.opsForHash().entries(WEATHER_REDIS);
            }
            interRecord.setResult(result.substring(0, Math.min(result.length(),3000)));
            interRecord.setResultStatus("1");

        } catch (Exception ex) {
            interRecord.setResultStatus("2");
            interRecord.setResult("调用失败" + ex.getMessage());
            return new ApiResultSet<>(500, "天气：获取黄浦天气失败", ex.getMessage());
        } finally {
            Long totalTime = System.currentTimeMillis() - begin1;

            interRecord.setParam(parameters.toString());
            interRecord.setName("获取天气");
            interRecord.setUrl(weatherUrl);
            interRecord.setMethod("get");
            interRecord.setSourceIp("gfapi.mlogcn.com");

            interRecord.setTotalTime(totalTime);
            interRecord(interRecord);
        }
        return ApiResultSet.ok("天气：获取黄浦天气成功", stringObjectMap);
    }

    /**
     * 获取利用反射获取类里面的值和名称
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    private static Map<Object, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<Object, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }

    /**
     * 雪花算法生成器
     */
    private Snowflake snowflake = new Snowflake(0, 0);

    /**
     * 日志记录
     *
     * @param interRecord
     */
    private void interRecord(HaInterRecord interRecord) {
        interRecord.setId(snowflake.nextId());
        String sql = "insert into t_ha_inter_record (ID, NAME, URL, SOURCE_IP, METHOD, PARAM, RESULT_STATUS, RESULT, " +
                "TOTAL_TIME, CREATE_DATE) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, interRecord.getId(), interRecord.getName(), interRecord.getUrl(),
                interRecord.getSourceIp(), interRecord.getMethod(), interRecord.getParam(),
                interRecord.getResultStatus(), interRecord.getResult(), interRecord.getTotalTime(), new Date());
    }
}
