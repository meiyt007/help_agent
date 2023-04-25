package com.zfsoft.superwindow.manager.front;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.cases.feign.SysUserFeginService;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.microservice.settings.data.SysConfig;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.data.front.ManualEvaluation;
import com.zfsoft.superwindow.dbaccess.dao.DbEmotionRecognitionRecordMapper;
import com.zfsoft.superwindow.dbaccess.dao.DbManualEvaluationMapper;
import com.zfsoft.superwindow.dbaccess.data.DbEmotionRecognitionRecord;
import com.zfsoft.superwindow.dbaccess.data.DbManualEvaluation;
import com.zfsoft.superwindow.feign.settings.SysConfigFeignService;
import com.zfsoft.superwindow.util.*;
import com.zfsoft.superwindow.util.SM2Utils.SM2Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.Bag;
import org.apache.commons.collections.bag.HashBag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class ManualEvaluationManager {

    @Value("${zfsoft.pbpj.key}")
    private String pbpjKey;

    @Value("${zfsoft.pbpj.secret}")
    private String pbpjSecret;

    @Value("${zfsoft.pbpj.privateKey}")
    private String pbpjPrivateKey;

    @Value("${zfsoft.pbpj.publicKey}")
    private String pbpjPublicKey;

    @Value("${zfsoft.pbpj.url}")
    private String pbpjUrl;

    @Value("${zfsoft.pbpj.saveEvaluateInfo}")
    private String saveEvaluateInfo;

    @Resource
    private DbManualEvaluationMapper dbManualEvaluationMapper;

    @Resource
    private SysUserFeginService sysUserFeginService;

    @Resource
    private SysConfigFeignService sysConfigFeignService;

    @Resource
    private DbEmotionRecognitionRecordMapper dbEmotionRecognitionRecordMapper;

    @Value("${zfsoft.pbpj.getEvaluateContentUrl}")
    private String getEvaluateContentUrl;


    public DbManualEvaluation queryById(String virtualBusinessNum) {
        DbManualEvaluation dbManualEvaluation = this.dbManualEvaluationMapper.queryById(virtualBusinessNum);
        return dbManualEvaluation;
    }

    public void updateManualEvaluation(ManualEvaluation manualEvaluation) {
        DbManualEvaluation dbManualEvaluation = this.dbManualEvaluationMapper.queryById(manualEvaluation.getOid());
        if (dbManualEvaluation != null) {
            dbManualEvaluation.setServiceEvaluation(manualEvaluation.getServiceEvaluation());
            dbManualEvaluation.setModifyDate(new Date());
            this.dbManualEvaluationMapper.update(dbManualEvaluation);
        }
    }

    public String pushManualEvaluation(String virtualBusinessNum) {
        DbManualEvaluation dbManualEvaluation = this.dbManualEvaluationMapper.queryById(virtualBusinessNum);
        String body = null;
        if (dbManualEvaluation != null) {
            try {
                dbManualEvaluation.setPushFlag(0);
                dbManualEvaluation.setModifyDate(new Date());
                this.dbManualEvaluationMapper.update(dbManualEvaluation);
                //解析办件信息
                Map<String, Object> requestDataMap = new HashMap<String, Object>();
                requestDataMap.put("pjTerm", dbManualEvaluation.getServiceEvaluation());
                requestDataMap.put("content", dbManualEvaluation.getEvaluationItem());
                requestDataMap.put("pf", "ff8080817a17b124017b5c2a7f864d31");
                requestDataMap.put("evalDetail", dbManualEvaluation.getEvaluationCode());
                requestDataMap.put("projectNo", dbManualEvaluation.getCaseNumber());
                requestDataMap.put("appStatus", "2");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(new Date());
                requestDataMap.put("assessTime", dateString);
                String requestData = JSON.toJSONString(requestDataMap);
                String encData = SM2Util.sm2Encrypt(pbpjPublicKey, requestData);
                String dataSign = SM2Util.sm2Sign(pbpjPrivateKey, pbpjKey, encData);
                Map<String, Object> map = new HashMap<String, Object>();
                String url = pbpjUrl + saveEvaluateInfo;
                map.put("apiKey", pbpjKey);
                map.put("systemPwd", pbpjSecret);
                map.put("requestData", encData);
                map.put("dataSign", dataSign);
                body  = HttpRequestUtil.sendPost(url, map);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return body;
    }

    public ApiResultSet saveManualEvaluation(ManualEvaluation manualEvaluation) {
        Map<String, String> modelMap = new HashMap<>();
        String oid =  UUIDUtil.randomUUID();
        modelMap.put("oid", oid);
        if(StringUtils.isEmpty(manualEvaluation.getOid())) {
            if (!StringUtils.isEmpty(manualEvaluation.getVirtualBusinessNum())) {
                ApiResultSet<SysConfig> config = sysConfigFeignService.getSysConfigByCode("WGPJ");
                if (config != null) {
                    if ("0".equals(config.getData().getValue())) {
                        //sky add
                        List<DbEmotionRecognitionRecord> caseOidIsNullLists = dbEmotionRecognitionRecordMapper.queryEmotionRecognitionListByVirtualBusinessOidAndCaseOidIsNull(manualEvaluation.getVirtualBusinessNum());
                        for(DbEmotionRecognitionRecord dbEmotionRecognitionRecord : caseOidIsNullLists){
                            dbEmotionRecognitionRecord.setCaseOid(manualEvaluation.getCaseOid());
                            dbEmotionRecognitionRecordMapper.update(dbEmotionRecognitionRecord);
                        }
                        //sky add end
                        //List<DbEmotionRecognitionRecord> list = dbEmotionRecognitionRecordMapper.queryEmotionRecognitionListByVirtualBusinessOid(manualEvaluation.getVirtualBusinessNum());
                        Integer totalStar = 0;
                        Integer averageStar = 0;
                        String star = "1";
                        if(caseOidIsNullLists !=null&&caseOidIsNullLists.size()>0){
                            for(DbEmotionRecognitionRecord dbEmotionRecognitionRecord : caseOidIsNullLists){
                                totalStar = totalStar + Integer.parseInt(dbEmotionRecognitionRecord.getScore());
                            }
                            averageStar = Math.round(totalStar/caseOidIsNullLists.size());
                        }
                        ApiResultSet<List<SysConfig>> configData = sysConfigFeignService.querySysConfigListByParentCode("PJXJ");
                        if (null != configData.getData()) {
                            List<SysConfig> SysConfigList = configData.getData();
                            if (SysConfigList.size() > 0) {
                                for (SysConfig sysConfig : SysConfigList) {
                                    String[] codes = sysConfig.getCode().split("-");
                                    if (averageStar >= Integer.parseInt(codes[0]) && averageStar <= Integer.parseInt(codes[1])) {
                                        if (averageStar <= 0) {
                                            modelMap.put("star", "5");
                                        } else {
                                            modelMap.put("star", sysConfig.getValue());
                                        }
                                        star = sysConfig.getValue();
                                    }
                                }
                            }
                        }
                        CurrentLoginUser user = CurrentLoginUserHolder.getCurrentLoginUser();
                        ApiResultSet<SysUser> sysUser =  sysUserFeginService.getSysUserByUserOid(user.getUserOid());
                        DbManualEvaluation dbManualEvaluation = new DbManualEvaluation();
                        BeanUtils.copyProperties(manualEvaluation, dbManualEvaluation);
                        if (queryEvaluateContent() != null && queryEvaluateContent() != "") {
                            JSONArray jsonArray = JSONArray.parseArray(JSONObject.parseObject(queryEvaluateContent()).getString("result"));
                            List contentList = new ArrayList();
                            if (averageStar <= 0) {
                                ApiResultSet<SysConfig> fcmyConfig = sysConfigFeignService.getSysConfigByCode("FCMY");
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    if (jsonObject.getString("grade").equals(fcmyConfig.getData().getConfigOid())) {
                                        contentList.add(jsonObject);
                                    }
                                }
                            } else {
                                ApiResultSet<List<SysConfig>> mycsConfig = sysConfigFeignService.querySysConfigListByParentCode("MYCS");
                                String configOid = "";
                                if (null != mycsConfig.getData()) {
                                    List<SysConfig> mycsConfigList = mycsConfig.getData();
                                    if (mycsConfigList.size() > 0) {
                                        for (SysConfig sysConfig : mycsConfigList) {
                                            if (sysConfig.getValue().equals(star)) {
                                                configOid = sysConfig.getConfigOid();
                                            }
                                        }
                                    }
                                }
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    if (jsonObject.getString("grade").equals(configOid)) {
                                        contentList.add(jsonObject);
                                    }
                                }
                            }
                            if (contentList.size() > 0) {
                                Random random = new Random();
                                int num = random.nextInt(contentList.size());
                                JSONObject jsonObject = (JSONObject) contentList.get(num);
                                dbManualEvaluation.setEvaluationCode((String) jsonObject.get("contentCode"));
                                modelMap.put("contentCode", (String) jsonObject.get("contentCode"));
                                modelMap.put("content", (String) jsonObject.get("content"));
                                dbManualEvaluation.setEvaluationItem((String) jsonObject.get("content"));
                            }
                        }
                        dbManualEvaluation.setCreateDate(new Date());
                        dbManualEvaluation.setDelFlag(SysCode.DELETE_STATUS.NO);
                        dbManualEvaluation.setModifyDate(new Date());
                        dbManualEvaluation.setCreateUserOid(user.getUserOid());
                        dbManualEvaluation.setCreateUserName(sysUser.getData().getName());
                        dbManualEvaluation.setOid(oid);
                        if (averageStar <= 0) {
                            dbManualEvaluation.setServiceEvaluation("5");
                            dbManualEvaluation.setSatisfactionNum("100");
                            dbManualEvaluation.setEmotionEvaluation("5");
                        } else {
                            dbManualEvaluation.setServiceEvaluation(star);
                            dbManualEvaluation.setSatisfactionNum(averageStar.toString());
                            dbManualEvaluation.setEmotionEvaluation(star);
                        }
                        dbManualEvaluation.setPushFlag(1);
                        dbManualEvaluation.setEvaluateFlag(0);
                        this.dbManualEvaluationMapper.insert(dbManualEvaluation);
                    } else {
                        CurrentLoginUser user = CurrentLoginUserHolder.getCurrentLoginUser();
                        ApiResultSet<SysUser> sysUser =  sysUserFeginService.getSysUserByUserOid(user.getUserOid());
                        DbManualEvaluation dbManualEvaluation = new DbManualEvaluation();
                        BeanUtils.copyProperties(manualEvaluation, dbManualEvaluation);
                        if (queryEvaluateContent() != null && queryEvaluateContent() != "") {
                            JSONArray jsonArray = JSONArray.parseArray(JSONObject.parseObject(queryEvaluateContent()).getString("result"));
                            List contentList = new ArrayList();
                            ApiResultSet<SysConfig> fcmyConfig = sysConfigFeignService.getSysConfigByCode("FCMY");
                            for (int i = 0; i < jsonArray.size(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                if (jsonObject.getString("grade").equals(fcmyConfig.getData().getConfigOid())) {
                                    contentList.add(jsonObject);
                                }
                            }
                            if (contentList.size() > 0) {
                                Random random = new Random();
                                int num = random.nextInt(contentList.size());
                                JSONObject jsonObject = (JSONObject) contentList.get(num);
                                dbManualEvaluation.setEvaluationCode((String) jsonObject.get("contentCode"));
                                modelMap.put("contentCode", (String) jsonObject.get("contentCode"));
                                modelMap.put("content", (String) jsonObject.get("content"));
                                dbManualEvaluation.setEvaluationItem((String) jsonObject.get("content"));
                            }
                        }
                        dbManualEvaluation.setCreateDate(new Date());
                        dbManualEvaluation.setDelFlag(SysCode.DELETE_STATUS.NO);
                        dbManualEvaluation.setModifyDate(new Date());
                        dbManualEvaluation.setCreateUserOid(user.getUserOid());
                        dbManualEvaluation.setCreateUserName(sysUser.getData().getName());
                        dbManualEvaluation.setOid(oid);
                        dbManualEvaluation.setServiceEvaluation("5");
                        dbManualEvaluation.setSatisfactionNum("100");
                        dbManualEvaluation.setEmotionEvaluation("5");
                        dbManualEvaluation.setPushFlag(1);
                        dbManualEvaluation.setEvaluateFlag(0);

                        this.dbManualEvaluationMapper.insert(dbManualEvaluation);
                        modelMap.put("star", "5");
                    }
                }
            } else {
                CurrentLoginUser user = CurrentLoginUserHolder.getCurrentLoginUser();
                ApiResultSet<SysUser> sysUser =  sysUserFeginService.getSysUserByUserOid(user.getUserOid());
                DbManualEvaluation dbManualEvaluation = new DbManualEvaluation();
                BeanUtils.copyProperties(manualEvaluation, dbManualEvaluation);
                if (queryEvaluateContent() != null && queryEvaluateContent() != "") {
                    JSONArray jsonArray = JSONArray.parseArray(JSONObject.parseObject(queryEvaluateContent()).getString("result"));
                    List contentList = new ArrayList();
                    ApiResultSet<SysConfig> fcmyConfig = sysConfigFeignService.getSysConfigByCode("FCMY");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        if (jsonObject.getString("grade").equals(fcmyConfig.getData().getConfigOid())) {
                            contentList.add(jsonObject);
                        }
                    }
                    if (contentList.size() > 0) {
                        Random random = new Random();
                        int num = random.nextInt(contentList.size());
                        JSONObject jsonObject = (JSONObject) contentList.get(num);
                        dbManualEvaluation.setEvaluationCode((String) jsonObject.get("contentCode"));
                        modelMap.put("contentCode", (String) jsonObject.get("contentCode"));
                        modelMap.put("content", (String) jsonObject.get("content"));
                        dbManualEvaluation.setEvaluationItem((String) jsonObject.get("content"));
                    }
                }
                dbManualEvaluation.setCreateDate(new Date());
                dbManualEvaluation.setDelFlag(SysCode.DELETE_STATUS.NO);
                dbManualEvaluation.setModifyDate(new Date());
                dbManualEvaluation.setCreateUserOid(user.getUserOid());
                dbManualEvaluation.setCreateUserName(sysUser.getData().getName());
                dbManualEvaluation.setOid(oid);
                dbManualEvaluation.setServiceEvaluation("5");
                dbManualEvaluation.setSatisfactionNum("100");
                dbManualEvaluation.setEmotionEvaluation("5");
                dbManualEvaluation.setPushFlag(1);
                dbManualEvaluation.setEvaluateFlag(0);
                this.dbManualEvaluationMapper.insert(dbManualEvaluation);
                modelMap.put("star", "5");
            }
        } else {
            DbManualEvaluation dbManualEvaluation = this.dbManualEvaluationMapper.queryById(manualEvaluation.getOid());
            if (dbManualEvaluation != null) {
                dbManualEvaluation.setServiceEvaluation(manualEvaluation.getServiceEvaluation());
                dbManualEvaluation.setEvaluationItem(manualEvaluation.getEvaluationItem());
                dbManualEvaluation.setEvaluationCode(manualEvaluation.getEvaluationCode());
                dbManualEvaluation.setPushFlag(manualEvaluation.getPushFlag());
                dbManualEvaluation.setEvaluateFlag(manualEvaluation.getEvaluateFlag());
                dbManualEvaluation.setModifyDate(new Date());
                this.dbManualEvaluationMapper.update(dbManualEvaluation);
            }
        }
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(modelMap);
        return apiResultSet;
    }

    private String queryEvaluateContent() {
        String body = null;
        try {
            //解析办件信息
            Map<String, Object> requestDataMap = new HashMap<String, Object>();
            requestDataMap.put("pageNumber", "1");
            requestDataMap.put("pageSize", "100");
            String requestData = JSON.toJSONString(requestDataMap);
            String encData = SM2Util.sm2Encrypt(pbpjPublicKey, requestData);
            String dataSign = SM2Util.sm2Sign(pbpjPrivateKey, pbpjKey, encData);
            Map<String, Object> map = new HashMap<String, Object>();
            String url = pbpjUrl + getEvaluateContentUrl;
            map.put("apiKey", pbpjKey);
            map.put("systemPwd", pbpjSecret);
            map.put("requestData", encData);
            map.put("dataSign", dataSign);
            body  = HttpRequestUtil.sendPost(url, map);
        }catch (Exception e){
            e.printStackTrace();
        }
        return body;
    }


    public List getSatisfaction() {
        CurrentLoginUser user = CurrentLoginUserHolder.getCurrentLoginUser();
        List<DbManualEvaluation> dbManualEvaluationList = dbManualEvaluationMapper.getSatisfaction(user.getUserOid());
        List<String> list = new ArrayList<>();
        List dataList = new ArrayList();
        for (DbManualEvaluation dbManualEvaluation : dbManualEvaluationList) {
            list.add(dbManualEvaluation.getServiceEvaluation());
        }
        Bag bag = new HashBag(list);
        if (list.size() > 0) {
            for (int i = 1; i < 6; i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("statisfactionMark", i+"");
                map.put("count", bag.getCount(i+""));
                map.put("percent", String.format("%.1f", ((float)bag.getCount(i+"")/(float)list.size())*100));
                dataList.add(map);
            }
        } else {
            for (int i = 1; i < 6; i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("statisfactionMark", i+"");
                map.put("count", "0");
                map.put("percent", "0");
                dataList.add(map);
            }
        }
        return dataList;
    }

    public List<DbManualEvaluation> queryByVirtualBusinessNum(String virtualBusinessNum) {
        List<DbManualEvaluation> dbManualEvaluationList = this.dbManualEvaluationMapper.queryByVirtualBusinessNum(virtualBusinessNum);
        return dbManualEvaluationList;
    }

    public String frontPushManualEvaluation(DbManualEvaluation dbManualEvaluation) {
        String body = null;
        if (dbManualEvaluation != null) {
            try {
                dbManualEvaluation.setPushFlag(0);
                dbManualEvaluation.setModifyDate(new Date());
                this.dbManualEvaluationMapper.update(dbManualEvaluation);
                //解析办件信息
                Map<String, Object> requestDataMap = new HashMap<String, Object>();
                requestDataMap.put("pjTerm", dbManualEvaluation.getServiceEvaluation());
                requestDataMap.put("content", "");
                requestDataMap.put("pf", "ff8080817a17b124017b5c2a7f864d31");
                requestDataMap.put("evalDetail", dbManualEvaluation.getEvaluationCode());
                requestDataMap.put("projectNo", dbManualEvaluation.getCaseNumber());
                requestDataMap.put("appStatus", "2");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(new Date());
                requestDataMap.put("assessTime", dateString);
                String requestData = JSON.toJSONString(requestDataMap);
                String encData = SM2Util.sm2Encrypt(pbpjPublicKey, requestData);
                String dataSign = SM2Util.sm2Sign(pbpjPrivateKey, pbpjKey, encData);
                Map<String, Object> map = new HashMap<String, Object>();
                String url = pbpjUrl + saveEvaluateInfo;
                map.put("apiKey", pbpjKey);
                map.put("systemPwd", pbpjSecret);
                map.put("requestData", encData);
                map.put("dataSign", dataSign);
                body  = HttpRequestUtil.sendPost(url, map);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return body;
    }

    public List<DbManualEvaluation> queryList(ManualEvaluation manualEvaluation) {
        DbManualEvaluation dbManualEvaluation = new DbManualEvaluation();
        BeanUtils.copyProperties(manualEvaluation, dbManualEvaluation);
        List<DbManualEvaluation> dbManualEvaluationList = this.dbManualEvaluationMapper.queryAll(dbManualEvaluation);
        return dbManualEvaluationList;
    }
}
