package com.zfsoft.outer.inter.controller;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.outer.inter.pojo.ApiResultSet;
import com.zfsoft.outer.inter.pojo.HaInterRecord;
import com.zfsoft.outer.inter.pojo.ScanParam;
import com.zfsoft.outer.inter.pojo.ZzkProperties;
import com.zfsoft.outer.inter.util.CaseCodeGenerator;
import com.zfsoft.outer.inter.util.ClientServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/9/21 16:18
 */
@Slf4j
@RestController
@RequestMapping(value = "/zzk/inter")
public class ZzkInterController {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private ZzkProperties zzkProperties;

    /**
     * 一键推送
     */
    @PostMapping(value = "/session")
    public ApiResultSet getSessionId() {
        HaInterRecord interRecord = new HaInterRecord();
        Map<String, String> params = new HashMap<>(2);
        String url = zzkProperties.getUrl()+zzkProperties.getLogin();
        params.put("account", zzkProperties.getAccount());
        params.put("password", zzkProperties.getPassword());
        Long begin1 = System.currentTimeMillis();
        JSONObject jsonObject;
        try {
            String result = ClientServer.send( url,JSONObject.toJSONString(params),"utf-8");
            interRecord.setName("证照库：获取sessionId");
            interRecord.setUrl(url);
            interRecord.setMethod("post");
            interRecord.setSourceIp(zzkProperties.getLogin());
            interRecord.setParam("{'account': '" + zzkProperties.getAccount() + "', 'password': '******'}");
            interRecord.setResult(result);
            interRecord.setResultStatus("1");

            jsonObject = JSONObject.parseObject(result);
            log.info("证照库：获取sessionId:{}",jsonObject);
            if (jsonObject.get("sessionId").toString()==null||jsonObject.get("sessionId").toString().isEmpty()) {
                throw new Exception("第三方接口调用错误");
            }

        } catch (Exception ex) {
            interRecord.setResultStatus("2");
            interRecord.setResult("调用失败" + ex.getMessage());
            return new ApiResultSet<>(500, "证照库：获取sessionId错误", ex.getMessage());
        } finally {
            Long totalTime = System.currentTimeMillis() - begin1;
            interRecord.setTotalTime(totalTime);
            interRecord(interRecord);
        }
        return ApiResultSet.ok("证照库：获取sessionId", jsonObject);
    }

    @PostMapping(value = "/scanCertQrCode")
    public ApiResultSet scanCertQrCode(@RequestBody ScanParam param) throws Exception {
        HaInterRecord interRecord = new HaInterRecord();
        JSONObject jsonObject;
        Long begin1 = System.currentTimeMillis();
        Map<String, String> map = new HashMap<>();
        try {
            ApiResultSet apiResult = getSessionId();
            if (apiResult.getCode() != 200) {
                return new ApiResultSet(ApiResultSet.DIRTY_DATA_TITLE, "取号失败:获取万达sessionId失败");
            }
            JSONObject ses = JSONObject.parseObject(apiResult.getData().toString());
            String sessionId = (String) ses.get("sessionId");
            Map<String, Object> params = new HashMap<>();
            params.put("sessionId", sessionId);
            params.put("pos", param.getPos());
            params.put("use", param.getUse());
            params.put("certQrCode", param.getQrcode().replace("\r", ""));
            params.put("orgName", zzkProperties.getOrgName());
            params.put("username", zzkProperties.getUserName());
            params.put("itemName", "");
            String itemCode = CaseCodeGenerator.randomOID(12, 4);
            params.put("itemCode", itemCode);
            String businessCode = CaseCodeGenerator.randomOID(15, 4);
            params.put("businessCode", businessCode);
            Map<String, Object> heads = new HashMap<>();
            heads.put("Content-Type", "application/json; charset=UTF-8");
            String url = zzkProperties.getUrl() + zzkProperties.getScanCertQrCode();
            String result = ClientServer.send(url, JSONObject.toJSONString(params), "utf-8");
            interRecord.setName("证照库：扫随申码获得用户信息");
            interRecord.setUrl(url);
            interRecord.setMethod("post");
            interRecord.setSourceIp(zzkProperties.getLogin());
            interRecord.setParam(params.toString());
            interRecord.setResult(result.substring(0, Math.min(result.length(),3000)));
            interRecord.setResultStatus("1");

            jsonObject = JSONObject.parseObject(result);
//            System.out.println("证照库：扫随申码获得用户信息返回:"+result);
            if (jsonObject.get("xm").toString()==null||jsonObject.get("xm").toString().isEmpty()) {
                throw new RuntimeException("扫码未获取到用户信息，请重新扫码");
            }
            map.put("xm", (String) jsonObject.get("xm"));
            map.put("mobile", (String) jsonObject.get("mobile"));
            map.put("zjhm", (String) jsonObject.get("zjhm"));
            log.info("证照库：扫随申码获得用户信息返回参数：{}",result);

        } catch (Exception ex) {
            interRecord.setResultStatus("2");
            interRecord.setResult("调用失败" + (ex.getMessage().substring(0, Math.min(ex.getMessage().length()-1,3000))));
            return new ApiResultSet<>(500, "证照库：扫随申码获得用户信息失败", ex.getMessage());
//            String re = "{\n" +
//                    "\t\"xm\": \"赵冰峰\",\n" +
//                    "\t\"catMainCode\": \"310105109000100\",\n" +
//                    "\t\"mobile\": \"17172190635\",\n" +
//                    "\t\"zjhm\": \"330411199805010635\",\n" +
//                    "\t\"encodeCertCode\": \"FUWj4+xtXZRmZXWfOn+/+VdhGokH/gbf+Ze9wjTbPnAZOC4IwlN6mRzcwkvmemRd\"\n" +
//                    "}";
//            return ApiResultSet.ok("证照库：扫随申码获得用户信息成功", re);

        } finally {
            Long totalTime = System.currentTimeMillis() - begin1;
            interRecord.setTotalTime(totalTime);
            interRecord(interRecord);
        }
        return ApiResultSet.ok("证照库：扫随申码获得用户信息成功", map);
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
