package com.zfsoft.superwindow.feign.settings;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.superwindow.controller.easyquickcase.data.UserAuthInfo;
import com.zfsoft.superwindow.service.easyquickcase.data.ElectronicSignatureDto;
import com.zfsoft.superwindow.service.easyquickcase.data.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "middle-web", contextId = "middleweb1")
public interface IdsLoginFeignService {

    @RequestMapping(value = {"/web/ids/login/getLoginUrl"}, method = {RequestMethod.POST})
    Map<String, String> getLoginUrl(@RequestParam("gotoUrl") String gotoUrl, @RequestParam("callType") String callType, @RequestParam(value = "interId", required = false) String interId);

    @RequestMapping(value = {"/web/electronicSignature/createFlow"}, method = {RequestMethod.POST})
    ResponseData<String> createFlow(@RequestBody ElectronicSignatureDto electronicSignatureDto);

    @RequestMapping(value = {"/web/mobile/login"}, method = {RequestMethod.POST})
    ResponseData<UserAuthInfo> login(@RequestParam("code") String code, @RequestParam("callType") String callType, @RequestParam(value = "interId", required = false) String interId);

    @RequestMapping(value = {"/web/gold/getWebsiteDetail"}, method = {RequestMethod.GET})
    ResponseData<JSONArray> getWebsiteDetail(@RequestParam("bmmc") String bmmc, @RequestParam("callType") String callType, @RequestParam(value = "interId", required = false) String interId);

    @RequestMapping(value = {"/web/gold/getAccumulationAccount"}, method = {RequestMethod.GET})
    ResponseData<JSONObject> getAccumulationAccount(@RequestParam("zjhm") String zjhm, @RequestParam("jcrxm") String jcrxm, @RequestParam("callType") String callType, @RequestParam(value = "interId", required = false) String interId);

    @RequestMapping(value = {"/web/gold/getDepositeCertificatePrint"}, method = {RequestMethod.GET})
    ResponseData<String> getDepositeCertificatePrint(@RequestParam("grzh") String grzh, @RequestParam("callType") String callType, @RequestParam(value = "interId", required = false) String interId);

    @RequestMapping(value = {"/web/gold/getSettleCertificatePrint"}, method = {RequestMethod.GET})
    ResponseData<String> getSettleCertificatePrint(@RequestParam("jkrgjjzh") String jkrgjjzh, @RequestParam("jkhtbh") String jkhtbh, @RequestParam("callType") String callType, @RequestParam(value = "interId", required = false) String interId);

    @RequestMapping(value = {"/web/gold/getLoanAccount"}, method = {RequestMethod.GET})
    ResponseData<JSONObject> getLoanAccount(@RequestParam("jkrzjh") String jkrzjh, @RequestParam("xingming") String xingming, @RequestParam("callType") String callType, @RequestParam(value = "interId", required = false) String interId);

    @RequestMapping(value = {"/web/gold/getRefundDetail"}, method = {RequestMethod.GET})
    ResponseData<JSONArray> getRefundDetail(@RequestParam("zjhm") String zjhm, @RequestParam("xingming") String xingming, @RequestParam("callType") String callType, @RequestParam(value = "interId", required = false) String interId);

    @RequestMapping(value = {"/web/gold/getLoanSchedule"}, method = {RequestMethod.GET})
    ResponseData<JSONArray> getLoanSchedule(@RequestParam("zjhm") String zjhm, @RequestParam("xingming") String xingming, @RequestParam("callType") String callType, @RequestParam(value = "interId", required = false) String interId);
}
