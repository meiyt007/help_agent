package com.zfsoft.single.controller.insideapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.cases.data.QlCase;
import com.zfsoft.cases.data.QlCaseApplay;
import com.zfsoft.cases.feign.SysDictFeignService;
import com.zfsoft.cases.feign.SysOrganFeginService;
import com.zfsoft.cases.service.QlCaseApplayService;
import com.zfsoft.cases.service.QlCaseService;
import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.microservice.settings.data.SysConfig;
import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.sxService.service.SxServiceService;
import com.zfsoft.single.feign.settings.*;
import com.zfsoft.single.service.insideapi.workEvaluate.WorkEvaluateService;
import com.zfsoft.single.util.HttpRequestUtil;
import com.zfsoft.single.util.SM2Utils.SM2Util;
import com.zfsoft.service.sxService.data.SxService;
import com.zfsoft.superwindow.feign.settings.SysConfigFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @（#）: WorkEvaluateController
 * @description: 智能登记好差评对接Controller
 * @author: wangwg
 * @date: 2021/7/15
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
public class WorkEvaluateController implements WorkEvaluateService {

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

    @Value("${zfsoft.pbpj.saveEvaluateCase}")
    private String pbpjSaveCase;

    @Value("${zfsoft.pbpj.saveEvaluateInfo}")
    private String saveEvaluateInfo;

    @Value("${zfsoft.pbpj.getEvaluateUrl}")
    private String getEvaluateUrl;

    @Value("${zfsoft.pbpj.getEvaluateContentUrl}")
    private String getEvaluateContentUrl;

    @Value("${zfsoft.pbpj.isEvaluateUrl}")
    private String isEvaluateUrl;

    @Resource
    private SysDictFeignService sysDictFeignService;

    @Resource
    private SxServiceService sxServiceFeginService;

    @Resource
    private SysOrganFeginService sysOrganFeginService;

    @Resource
    private QlCaseService qlCaseServiceFeginService;

    @Resource
    private QlCaseApplayService qlCaseApplayServiceFeginService;

    @Resource
    private SysConfigFeignService sysConfigFeignService;


    @Override
    public ApiResultSet queryEvaluateContent(String star) {
        if (star == "" || star == null) {
            star = "5";
        }
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
        JSONArray jsonArray = JSONArray.parseArray(JSONObject.parseObject(body).getString("result"));
        List contentList = new ArrayList();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            if (jsonObject.getString("grade").equals(configOid)) {
                contentList.add(jsonObject);
            }
        }
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(contentList);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<String> evaluateSaveQlCase(String caseOid) {
        String body = null;
        try {
            ApiResultSet<QlCase> resultSet = qlCaseServiceFeginService.queryQlCaseByCaseOid(caseOid);
            if (resultSet.getData() != null) {
                ApiResultSet<QlCaseApplay> resultApplay = qlCaseApplayServiceFeginService.queryQlCaseApplayByCaseOid(resultSet.getData().getCaseOid());
                ApiResultSet<SysOrgan> resultOrgan = sysOrganFeginService.getSysOrganByOrganOid(resultSet.getData().getOrganOid());
                ApiResultSet<SxService> resultService = sxServiceFeginService.getSxServiceByOid(resultSet.getData().getServiceOid());
                //解析办件信息
                Map<String, Object> requestDataMap = new HashMap<String, Object>();
                requestDataMap.put("caseNum", resultSet.getData().getCaseNumber());
                requestDataMap.put("serviceName", resultSet.getData().getServiceName());
                if (resultApplay.getData() != null) {
                    ApiResultSet<SysDict> resultDict = sysDictFeignService.getSysDictByDictOid(resultApplay.getData().getCredentialType());
                    requestDataMap.put("applyName", resultApplay.getData().getApplyUserName());
                    requestDataMap.put("applyTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(resultApplay.getData().getCreateDate()));
                    requestDataMap.put("cardNo", resultApplay.getData().getCredentialNumber());
                    requestDataMap.put("lxrPhone", resultApplay.getData().getApplyUserPhone());
                    if ("0".equals(resultApplay.getData().getApplyUserType())) {//申请对象类型
                        requestDataMap.put("objectType", "1");
                    } else {
                        requestDataMap.put("objectType", "0");
                    }
                    if (resultDict.getData() != null) {//证件名称
                        requestDataMap.put("cardName", resultDict.getData().getName());
                    }
                }
                requestDataMap.put("blStatus", "2");
                if (resultService.getData() != null) {
                    if ("2c287bb66859a90001685a5612f20007".equals(resultSet.getData().getServiceType())) {//公共服务
                        requestDataMap.put("serviceType", "0");
                    } else {
                        requestDataMap.put("serviceType", "1");
                    }
                    requestDataMap.put("serviceCode", resultService.getData().getImplementCode());
                }
                if (resultOrgan.getData() != null) {
                    requestDataMap.put("organCode", resultOrgan.getData().getUniteCode());
                }
                requestDataMap.put("projectName", resultSet.getData().getProjectName());
                String requestData = JSON.toJSONString(requestDataMap);
                String encData = SM2Util.sm2Encrypt(pbpjPublicKey, requestData);
                String dataSign = SM2Util.sm2Sign(pbpjPrivateKey, pbpjKey, encData);
                Map<String, Object> map = new HashMap<String, Object>();
                String uriString = pbpjUrl + pbpjSaveCase;
                map.put("apiKey", pbpjKey);
                map.put("systemPwd", pbpjSecret);
                map.put("requestData", encData);
                map.put("dataSign", dataSign);
                body = HttpRequestUtil.sendPost(uriString, map);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        ApiResultSet<String> apiResultSet = new ApiResultSet<String>();
        apiResultSet.setData(body);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<String> getEvaluateInfo(String caseNum) {
        String body = null;
        try {
            //解析办件信息
            Map<String, Object> requestDataMap = new HashMap<String, Object>();
            requestDataMap.put("caseNum", caseNum);
            String requestData = JSON.toJSONString(requestDataMap);
            String encData = SM2Util.sm2Encrypt(pbpjPublicKey, requestData);
            String dataSign = SM2Util.sm2Sign(pbpjPrivateKey, pbpjKey, encData);
            Map<String, Object> map = new HashMap<String, Object>();
            String url = pbpjUrl + getEvaluateUrl;
            map.put("apiKey", pbpjKey);
            map.put("systemPwd", pbpjSecret);
            map.put("requestData", encData);
            map.put("dataSign", dataSign);
            body  = HttpRequestUtil.sendPost(url, map);
        }catch (Exception e){
            e.printStackTrace();
        }
        ApiResultSet<String> apiResultSet = new ApiResultSet<String>();
        apiResultSet.setData(body);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<String> isEvaluate(String caseNum) {
        String body = null;
        try {
            //解析办件信息
            Map<String, Object> requestDataMap = new HashMap<String, Object>();
            requestDataMap.put("caseNum", caseNum);
            String requestData = JSON.toJSONString(requestDataMap);
            String encData = SM2Util.sm2Encrypt(pbpjPublicKey, requestData);
            String dataSign = SM2Util.sm2Sign(pbpjPrivateKey, pbpjKey, encData);
            Map<String, Object> map = new HashMap<String, Object>();
            String url = pbpjUrl + isEvaluateUrl;
            map.put("apiKey", pbpjKey);
            map.put("systemPwd", pbpjSecret);
            map.put("requestData", encData);
            map.put("dataSign", dataSign);
            body  = HttpRequestUtil.sendPost(url, map);
        }catch (Exception e){
            e.printStackTrace();
        }
        ApiResultSet<String> apiResultSet = new ApiResultSet<String>();
        apiResultSet.setData(body);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<String> saveQLCaseEvaluation(String caseNUm, String starNUm, String contentOids, String content) {
        String body = null;
        try {
            //解析办件信息
            Map<String, Object> requestDataMap = new HashMap<String, Object>();
            requestDataMap.put("pjTerm", starNUm);
            requestDataMap.put("content", content);
            requestDataMap.put("pf", "ff8080817a17b124017b5c2a7f864d31");
            requestDataMap.put("evalDetail", contentOids);
            requestDataMap.put("projectNo", caseNUm);
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
        ApiResultSet<String> apiResultSet = new ApiResultSet<String>();
        apiResultSet.setData(body);
        return apiResultSet;
    }


}
