package com.zfsoft.outer.inter.controller;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.outer.inter.pojo.ApiResultSet;
import com.zfsoft.outer.inter.pojo.HaInterRecord;
import com.zfsoft.outer.inter.pojo.TaskProperties;
import com.zfsoft.outer.inter.util.ClientServer;
import com.zfsoft.outer.inter.util.JsonUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/10/28 9:46
 */
@RestController
@RequestMapping(value = "/outer/take")
public class TakeNumberInterController {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private TaskProperties taskProperties;

    /**
     * 登录获取token
     */
    public String getToken() throws IOException {
        String token ="";
        Map<String,Object> map = new HashMap<>();
        map.put("account",taskProperties.getAccount());
        map.put("password",taskProperties.getPassword());
        String json = JSON.toJSONString(map);
        String result = ClientServer.send(taskProperties.getUrl()+taskProperties.getGetToken(),json,"utf-8");
        JSONObject jsonObject = JSON.parseObject(result);
        Integer code = (Integer) jsonObject.get("code");
        if(1000 == code){
            JSONObject dataToken = JsonUtil.toJSONObject(jsonObject.get("dataToken"));
            token = (String) dataToken.get("token");
        }
        return token;
    }

    /**
     * 获取取号目录 -万达
     * @Author: zhaobf
     * @Date: 2022-10-28 10:20
     */
    @PostMapping("/getMachineCategoryTreeById")
    public ApiResultSet getMachineCategoryTreeById() throws IOException {
        HaInterRecord interRecord = new HaInterRecord();
//        JSONObject jsonObjectResult = new JSONObject();
//        List<ResponseApplyVo> applyVoList = new ArrayList<>();
        JSONObject jsonObject1 = null;
        Long begin1 = System.currentTimeMillis();
        try {
            String result = ClientServer.sendPost(taskProperties.getUrl()+taskProperties.getGetMachineCategoryTreeById(), "","utf-8",this.getToken());
            jsonObject1 = JSON.parseObject(result);
            String code = (String) jsonObject1.get("code");
            if("1000".equals(code)){
                JSONArray data = (JSONArray )(jsonObject1.get("data"));

            }else{
                return new ApiResultSet<>(500, "取号：获取取号目录错误", (String) jsonObject1.get("msg"));
            }
            interRecord.setResult(result.substring(0, Math.min(result.length(),3000)));
            interRecord.setResultStatus("1");

        } catch (Exception ex) {
            interRecord.setResultStatus("2");
            interRecord.setResult("调用失败" + ex.getMessage());
            return new ApiResultSet<>(500, "取号：获取取号目录错误", ex.getMessage());
        } finally {
            Long totalTime = System.currentTimeMillis() - begin1;

            interRecord.setParam("");
            interRecord.setName("获取取号目录（万达）");
            interRecord.setUrl("/getMachineCategoryTreeById");
            interRecord.setMethod("post");
            interRecord.setSourceIp(taskProperties.getUrl());

            interRecord.setTotalTime(totalTime);
            interRecord(interRecord);
        }
        return ApiResultSet.ok("取号：获取取号目录", jsonObject1.get("data"));
    }

    @PostMapping("/takeNumber")
    public ApiResultSet<JSONObject> takeNumber(@RequestBody  Map<String, Object> map){
        HaInterRecord interRecord = new HaInterRecord();
//        JSONObject jsonObjectResult = new JSONObject();
//        List<ResponseApplyVo> applyVoList = new ArrayList<>();
        JSONObject jsonObject1 = null;
        String url = taskProperties.getUrl()+ taskProperties.getTakeNumber();
        Long begin1 = System.currentTimeMillis();
        try {
            Map<String, String> parameters = new HashMap<>();
            Set<String> strings = map.keySet();
            for (String string : strings) {
                parameters.put(string,map.get(string).toString());
            }
            Map<String, String> requestHeader = new HashMap<>();
            requestHeader.put("Authorization",getToken());

            String result = ClientServer.sendGet( url,requestHeader,parameters,"utf-8");
            jsonObject1 = JSON.parseObject(result);
            String code = (String) jsonObject1.get("code");
            if("1000".equals(code)){
                //暂时没有业务需要处理
                JSONObject data = (JSONObject )(jsonObject1.get("data"));

            }else{
                return new ApiResultSet<>(500, "取号：窗口取号错误", (String) jsonObject1.get("msg"));
            }
            interRecord.setResult(result.substring(0, Math.min(result.length(),3000)));
            interRecord.setResultStatus("1");

        } catch (Exception ex) {
            interRecord.setResultStatus("2");
            interRecord.setResult("调用失败" + ex.getMessage());
            return new ApiResultSet<>(500, "取号：窗口取号错误", ex.getMessage());
        } finally {
            Long totalTime = System.currentTimeMillis() - begin1;

            interRecord.setParam("");
            interRecord.setName("窗口取号（万达）");
            interRecord.setUrl("/takeNumber");
            interRecord.setMethod("post");
            interRecord.setSourceIp(taskProperties.getUrl());

            interRecord.setTotalTime(totalTime);
            interRecord(interRecord);
        }
        return ApiResultSet.ok("取号：窗口取号成功", jsonObject1.get("data"));
    }
    @PostMapping("/takePriorityNumber")
    public ApiResultSet<JSONObject> takePriorityNumber(@RequestBody  Map<String, Object> map){
        HaInterRecord interRecord = new HaInterRecord();
//        JSONObject jsonObjectResult = new JSONObject();
//        List<ResponseApplyVo> applyVoList = new ArrayList<>();
        JSONObject jsonObject1 = null;
        String url = taskProperties.getUrl()+ taskProperties.getTakePriorityNumber();
        Long begin1 = System.currentTimeMillis();
        try {
            Map<String, String> parameters = new HashMap<>();
            Set<String> strings = map.keySet();
            for (String string : strings) {
                parameters.put(string,map.get(string).toString());
            }
            Map<String, String> requestHeader = new HashMap<>();
            requestHeader.put("Authorization",getToken());

            String result = ClientServer.sendGet( url,requestHeader,parameters,"utf-8");
            jsonObject1 = JSON.parseObject(result);
            String code = (String) jsonObject1.get("code");
            if("1000".equals(code)){
                //暂时没有业务需要处理
                JSONObject data = (JSONObject )(jsonObject1.get("data"));

            }else{
                return new ApiResultSet<>(500, "取号：窗口优先取号错误", (String) jsonObject1.get("msg"));
            }
            interRecord.setResult(result.substring(0, Math.min(result.length(),3000)));
            interRecord.setResultStatus("1");

        } catch (Exception ex) {
            interRecord.setResultStatus("2");
            interRecord.setResult("调用失败" + ex.getMessage());
            return new ApiResultSet<>(500, "取号：窗口优先取号错误", ex.getMessage());
        } finally {
            Long totalTime = System.currentTimeMillis() - begin1;

            interRecord.setParam("");
            interRecord.setName("窗口优先取号（万达）");
            interRecord.setUrl("/takePriorityNumber");
            interRecord.setMethod("post");
            interRecord.setSourceIp(taskProperties.getUrl());

            interRecord.setTotalTime(totalTime);
            interRecord(interRecord);
        }
        return ApiResultSet.ok("取号：窗口优先取号成功", jsonObject1.get("data"));
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
