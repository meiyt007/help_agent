package com.zfsoft.ha.front.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.cases.util.DateUtil;
import com.zfsoft.ha.data.*;
import com.zfsoft.ha.data.responseData.HaAppointmentResponseData;
import com.zfsoft.ha.data.responseData.HaMassesNucleicResponseData;
import com.zfsoft.ha.front.MassesService;
import com.zfsoft.ha.managers.*;
import com.zfsoft.ocr.util.CommonRestUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 预约信息表
 *
 * @author zhaobf
 * @version 1.0
 * @date 2022/7/22
 */
@Slf4j
@RestController
public class MassesController implements MassesService {
    /**
     * 办事队列service层
     */
    @Resource
    private HaWorkQueueManager haWorkQueueManager;
    /**
     * 预约记录service层
     */
    @Resource
    private HaAppointmentManager haAppointmentManager;
    /**
     * 帮代办服务业务service层
     */
    @Resource
    private HaWorkServiceManager haWorkServiceManager;
    /**
     * 核酸记录service层
     */
    @Resource
    private HaMassesNucleicManager haMassesNucleicManager;

    /**
     *
     */
    @Resource
    private HaWorkUserServiceManager haWorkUserServiceManager;

    /**
     * 来访记录service层
     */
    @Resource
    private HaVisitManager haVisitManager;

    @Value("${zfsoft.inter.url}")
    private String interUrl;

    @Value("${zfsoft.inter.nucleicInfo}")
    private String nucleicInfo;

    @Value("${zfsoft.inter.getCompanyInfo}")
    private String getCompanyInfo;

    /**
     * 根据身份证号码获取信息
     *
     * @param queueId 队列ai
     * @return
     */
    @Override
    public ApiResultSet<Map<String, Object>> getMassesInfo(String queueId) {
        log.info("办事群众：进入根据身份证号码获取信息,入参:queueId:{}" + queueId);
        Map<String, Object> mapReq = new HashMap<>();
        Map<String, Object> baseInfo = new HashMap<>();
        List<HaAppointmentResponseData> appointmentInfoResp = new ArrayList<>();
        Map<String, Object> companyInfo = new HashMap<>();
        List< Map<String, Object>> serviceHistoryInfoResp = new ArrayList<>();
        List<HaMassesNucleicResponseData> nucleicInfoResp = new ArrayList<>();
        HaWorkQueue queue = haWorkQueueManager.getWorkQueueById(queueId);
        if(queue.getId()==null){
            return new ApiResultSet(500, "queueId找不到队列信息");
        }
        baseInfo.put("name", queue.getName());
        baseInfo.put("cardNo", queue.getCardNo());
        baseInfo.put("phone", queue.getPhone());
        //根据身份证号码获取基本信
        String cardNo = queue.getCardNo();
        String companyCode = queue.getCompanyCode();
        String name = queue.getName();
        //根据身份证号码获取预约信息
        HaAppointment ha = new HaAppointment();
        ha.setCardNo(cardNo);
        ha.setCompanyCode(companyCode);
        List<HaAppointment> appointmentInfo = haAppointmentManager.selectByHaAppointment(ha,DateUtil.getDate(),DateUtil.getDate());
        if (appointmentInfo != null) {
            appointmentInfoResp = appointmentInfo.stream().map(sourceDate -> {
                HaAppointmentResponseData target = new HaAppointmentResponseData();
                BeanUtils.copyProperties(sourceDate, target);
                return target;
            }).collect(Collectors.toList());
        }

        //根据身份证号获取历史服务信息
        HaWorkQueue hwq = new HaWorkQueue();
        hwq.setCardNo(cardNo);
        hwq.setCompanyCode(companyCode);

        List<HaWorkQueue> haWorkQueues = haWorkQueueManager.selectByHaWorkQueue(hwq);
        for (HaWorkQueue haWorkQueue : haWorkQueues.stream().limit(5).collect(Collectors.toList())) {
            Map<String, Object> map = new HashMap<>();
            map.put("queueId",haWorkQueue.getId());
            map.put("createDate",haWorkQueue.getCreateDate());
            map.put("serviceWorkUserName",haWorkUserServiceManager.getHelpWorkUserById(haWorkQueue.getServiceWorkUserId()).getName());
            map.put("serviceBeginTime",haWorkQueue.getServiceBeginTime());
            map.put("serviceEndTime",haWorkQueue.getServiceEndTime());
            map.put("serviceDuration",haWorkQueue.getServiceDuration());
            map.put("firstServiceBeginTime",haWorkQueue.getFirstServiceBeginTime());
            serviceHistoryInfoResp.add(map);
        };
//        List<Long> wordIds = haWorkQueues.stream().map(HaWorkQueue::getId).collect(Collectors.toList());
//        List<HaWorkServiceVo> serviceHistoryInfo;
//        if (wordIds.size() > 0) {
//            serviceHistoryInfo = haWorkServiceManager.selectByHaWorkService(wordIds);
//        } else {
//            serviceHistoryInfo = new ArrayList<>();
//        }
//        if (serviceHistoryInfo != null) {
//            serviceHistoryInfoResp = serviceHistoryInfo.stream().map(sourceDate -> {
//                HaWorkServiceResponseData target = new HaWorkServiceResponseData();
//                BeanUtils.copyProperties(sourceDate, target);
//                return target;
//            }).collect(Collectors.toList());
//        }

        /**

        //根据身份证号获取核酸信息
        try {
            //调用接口获取核酸信息
            String url = interUrl + nucleicInfo + "?name=" + name + "&cardNo=" + cardNo + "&phone=" + baseInfo.get("phone").toString();
            String body = CommonRestUtil.sendGet(url);
            JSONObject jsonObject = JSONObject.parseObject(body);
            if (!"200".equals(jsonObject.get("code").toString())) {
                throw new Exception("第三方接口获取核酸信息调用错误");
            }
            JSONArray array = jsonObject.getJSONArray("data");
            //遍历获取到的核酸记录
            for (Object obj : array) {
                JSONObject object = (JSONObject) JSONObject.toJSON(obj);
                HaMassesNucleic haMassesNucleic = new HaMassesNucleic();
                int code = haMassesNucleicManager.getIsHaMassesNucleicByNucleicCode((String) object.get("nucleicCode"));
                //核酸检测编码相同的记录不插入表
                if (1 == code) continue;
                haMassesNucleic.setName(name);
                haMassesNucleic.setCardNo(cardNo);
                haMassesNucleic.setNucleicCode((String) object.get("nucleicCode"));
                haMassesNucleic.setNucleicCollectionPoint((String) object.get("nucleicCollectionPoint"));
                haMassesNucleic.setNucleicCollectionTime(DateUtil.datetimeFormat.parse((String) object.get("nucleicCollectionTime")));
                haMassesNucleic.setNucleicTestingTime(DateUtil.datetimeFormat.parse((String) object.get("nucleicTestingTime")));
                haMassesNucleic.setNucleicResult("1".equals(object.get("nucleicResultCode")) ? "阴性" : "阳性");
                haMassesNucleic.setNucleicResultCode((String) object.get("nucleicResultCode"));
                haMassesNucleicManager.saveHaMassesNucleic(haMassesNucleic);
            }
        } catch (Exception e) {
            log.info("办事群众：获取核酸信息报错:" + e.getMessage());
        }         *


        HaMassesNucleic hamn = new HaMassesNucleic();
        hamn.setCardNo(cardNo);
        hamn.setName(name);
        List<HaMassesNucleic> nucleicInfos = haMassesNucleicManager.selectByHaMassesNucleic(hamn, DateUtil.get7DaysStartTime(new Date()), DateUtil.get7DaysEndTime(new Date()));
        if (nucleicInfos != null) {
            nucleicInfoResp = nucleicInfos.stream().limit(5).map(sourceDate -> {
                HaMassesNucleicResponseData target = new HaMassesNucleicResponseData();
                BeanUtils.copyProperties(sourceDate, target);
                return target;
            }).collect(Collectors.toList());
        }
        log.info("办事群众：根据身份证号码获取信息成功");
         */
        /**

        //根据身份证号获取企业信息
        try {
            //调用接口获取企业信息
            String url = interUrl + getCompanyInfo + "?companyName=" + queue.getCompanyName() + "&companyCode=" + companyCode;
            String body = CommonRestUtil.sendGet(url);
            JSONObject jsonObject = JSONObject.parseObject(body);
            if (!"200".equals(jsonObject.get("code").toString())) {
                throw new Exception("第三方接口获取企业信息调用错误");
            }
            companyInfo=jsonObject.getJSONObject("data");
//            companyInfo = com.zfsoft.platform.utils.bean.BeanUtils.javabeanToMap(jsonObject.getJSONObject("data"));
        } catch (Exception e) {
            log.info("办事群众：获取企业信息信息报错:" + e.getMessage());
        }         *
         */

        mapReq.put("baseInfo", baseInfo);
        mapReq.put("appointmentInfo", appointmentInfoResp);
        mapReq.put("companyInfo",companyInfo);
        //返回前五个核酸记录 五个历史服务信息
        mapReq.put("serviceHistoryInfo", serviceHistoryInfoResp);
        mapReq.put("nucleicInfo", nucleicInfoResp);
        return ApiResultSet.ok("办事群众：根据身份证号码获取信息成功", mapReq);
    }

    /**
     * 查询企业信息
     *
     * @param companyName
     * @param companyCode
     * @author zhaobf
     * @Date: 2022/8/4 10:30
     */
    @Override
    public ApiResultSet getCompanyInfo(String companyName, String companyCode) {
        log.info("查询企业信息，companyName:{},companyCode:{}", companyName, companyCode);
        try {

            //调用接口获取企业信息
            String url = interUrl + getCompanyInfo + "?companyName=" + companyName + "&companyCode=" + companyCode;
            String body = CommonRestUtil.sendGet(url);
            JSONObject jsonObject = JSONObject.parseObject(body);
            if (!"200".equals(jsonObject.get("code").toString())) {
                throw new Exception("第三方接口获取企业信息调用错误");
            }
            JSONObject obj = jsonObject.getJSONObject("data");
            HaVisit haVisit = new HaVisit();
            haVisit.setVisitType("2");
            haVisit.setCardNo(companyCode);
            List<HaVisit> haVisits = haVisitManager.queryHaVisitByExampleo(haVisit);
            //更新
            if (haVisits != null && haVisits.size() > 0) {
                haVisit.setId(haVisits.get(0).getId());
                haVisit.setPhone(haVisits.get(0).getPhone());
                haVisit.setName(companyName);
                haVisitManager.saveHaVisit(haVisit);
            }
            return ApiResultSet.ok("查询企业信息成功", obj);
        } catch (Exception e) {
            log.error("查询企业信息方法错误：", e);
//            return new ApiResultSet<>(500, "查询企业信息错误", e.getMessage());
        }
        return ApiResultSet.ok("查询企业信息成功", "");
    }
}
