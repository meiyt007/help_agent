package com.zfsoft.single.manager.formTemplate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.cases.util.spring.SpringUtils;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.sxService.service.SxServiceMaterialService;
import com.zfsoft.single.data.formTemplate.TermlateDataDto;
import com.zfsoft.single.util.DateUtil;
import com.zfsoft.service.sxService.data.SxMaterialFormTemp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;

@Service
@Slf4j
public class MaterialTemplateManager {

    @Resource
    private SxServiceMaterialService sxServiceMaterialService;

    @Value("${zfsoft.electronicForm.url}")
    private String formUrl;

    @Value("${zfsoft.electronicForm.exportFormData}")
    private String exportFormData;

    @Value("${zfsoft.electronicForm.getOneDocxTemplateUrl}")
    private String getOneDocxTemplateUrl;

    /**
     * @description 获取表单填充模板集合
     * @param reportOid 办件OID
     * @param serviceOid 事项OID
     * @author meiyt
     * @date 2022/6/2
     * @return
     **/
    public List<TermlateDataDto> getTemplateList(String reportOid, String serviceOid) {
        ApiResultSet<List<SxMaterialFormTemp>> serviceFormTemplateList = sxServiceMaterialService.getServiceFormTemplateList(serviceOid);
        List<SxMaterialFormTemp> formTempList = serviceFormTemplateList.getData();
        System.out.println("获取表单模板reportOid====>" + reportOid + "====serviceOid====>" + serviceOid);
        System.out.println("模板数量====>" + (null == formTempList ? 0 : formTempList.size()));
        CopyOnWriteArrayList<TermlateDataDto> dtoList = new CopyOnWriteArrayList<>();
        try{
        if (CollUtil.isNotEmpty(formTempList)) {

            assert formTempList != null;
            CountDownLatch countDownLatch = new CountDownLatch(formTempList.size());
            for (SxMaterialFormTemp sxMaterialFormTemp : formTempList) {
                    MaterialTemplateManager xxxServiceProxy = SpringUtils.getBean(MaterialTemplateManager.class);
//
                xxxServiceProxy.getTermlateDataDtos(reportOid, dtoList, sxMaterialFormTemp, countDownLatch);
            }
            //同步等待所有线程执行完之后再继续
            countDownLatch.await();
//            System.out.println(dtoList);
//            判断进程是否全部结束 太麻烦，用countDownLatch代替
//            while (!futureList.isEmpty()) {
//                //回调信息空了就结束
//                for (int i = 0; i < futureList.size(); i++) {
//                    if (futureList.get(i).isDone()) {
//                        //判断线程结束，输出回调信息，并将该回调清除
//                        try {
//                            System.out.println(futureList.get(i).get(5, TimeUnit.SECONDS));
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        } catch (TimeoutException e) {
//                            e.printStackTrace();
//                        }catch (ExecutionException e) {
//                            e.printStackTrace();
//                            break;
//                        }
//                        futureList.remove(i);
//                        i--;
//                    }
//                }
//            }

        }
        } catch (Exception e) {
            log.error("errMsg:{}", e.getMessage());
        }

        return dtoList;
    }

    @Async
    public Future<String> getTermlateDataDtos(String reportOid,CopyOnWriteArrayList<TermlateDataDto> dtoList, SxMaterialFormTemp sxMaterialFormTemp,CountDownLatch countDownLatch) {
//        List<TermlateDataDto> dtoList = new ArrayList<>();
        try {
            long start = System.currentTimeMillis();
            Boolean isWord = false;
            String isPdf = null;
            String designOid = sxMaterialFormTemp.getDesignOid();
            String templateOid = sxMaterialFormTemp.getTemplateOid();
            System.out.println("请求表单接口designOid====>" + designOid + "====templateOid====>" + templateOid);
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> initMap = new HashMap<>();
            initMap.put("year", DateUtil.getYear());
            initMap.put("month", DateUtil.getMonth());
            initMap.put("day", DateUtil.getDay());
            map.put("docxTemplateOid", templateOid);
            map.put("designAndReportOid", designOid + "-" + reportOid);
            map.put("initData", JSONObject.toJSONString(initMap));
            String getone = HttpUtil.get(formUrl + getOneDocxTemplateUrl + "/" + templateOid);
            System.out.println("templateUrl ====>" + formUrl + getOneDocxTemplateUrl + "/" + templateOid);
            if (StrUtil.isNotEmpty(getone)) {
                JSONObject jsonObj = JSONObject.parseObject(getone);
                Map<String, Object> mapObject = jsonObj;
                if (mapObject.get("code").equals(200)) {
                    Object data = mapObject.get("data");
                    if (null == data) {
                        return new AsyncResult<>("接口耗时" + (System.currentTimeMillis() - start));
                    }
                    JSONObject jsonObject = JSONObject.parseObject(data.toString());
                    isPdf = jsonObject.get("isPdf") != null ? jsonObject.get("isPdf").toString() : "1";
                    if ("0".equals(isPdf)) isWord = true;
                }
            }
            System.out.println("formUrl + exportFormData====>" + formUrl + exportFormData);
            String res = null;
            if ("2".equals(isPdf)) {
                for (int i = 0; i < 2; i++) {
                    if (i == 0) {
                        isWord = true; //生成word
                        map.put("isWord", isWord);
                    } else {
                        isWord = false;//生成pdf
                        map.put("isWord", isWord);
                    }
                    res = HttpUtil.get(formUrl + exportFormData, map);
                    System.out.println(Thread.currentThread().getName() + "--表单接口返回====>" + res);
                    if (StrUtil.isNotEmpty(res)) {
                        JSONObject jsonObj = JSONObject.parseObject(res);
                        Map<String, Object> mapObject = jsonObj;
                        if (mapObject.get("code").equals(200)) {
                            Object data = mapObject.get("data");
                            if (null == data) {
                                return new AsyncResult<>("接口耗时" + (System.currentTimeMillis() - start));
                            }
                            Object templateData = JSON.parseArray(data.toString()).get(0);
                            JSONObject jsonObject = JSONObject.parseObject(templateData.toString());
                            TermlateDataDto dto = new TermlateDataDto();
                            dto.setTemplateName(sxMaterialFormTemp.getTemplateName());
                            dto.setAttaOid(jsonObject.get("attaOid").toString());
                            dto.setAttaUrl(jsonObject.get("url").toString());
                            dto.setSealStatus(0);
                            dto.setMaterialOid(sxMaterialFormTemp.getMaterialOid());
                            dto.setMaterialName(sxMaterialFormTemp.getMaterialName());
                            dtoList.add(dto);
                        }
                    }
                }
            } else {
                map.put("isWord", isWord);
                res = HttpUtil.get(formUrl + exportFormData, map);
                System.out.println(Thread.currentThread().getName() + "--表单接口返回====>" + res);
                if (StrUtil.isNotEmpty(res)) {
                    JSONObject jsonObj = JSONObject.parseObject(res);
                    Map<String, Object> mapObject = jsonObj;
                    if (mapObject.get("code").equals(200)) {
                        Object data = mapObject.get("data");
                        if (null == data) {
                            return new AsyncResult<>("接口耗时" + (System.currentTimeMillis() - start));
                        }
                        Object templateData = JSON.parseArray(data.toString()).get(0);
                        JSONObject jsonObject = JSONObject.parseObject(templateData.toString());
                        TermlateDataDto dto = new TermlateDataDto();
                        dto.setTemplateName(sxMaterialFormTemp.getTemplateName());
                        dto.setAttaOid(jsonObject.get("attaOid").toString());
                        dto.setAttaUrl(jsonObject.get("url").toString());
                        dto.setSealStatus(0);
                        dto.setMaterialOid(sxMaterialFormTemp.getMaterialOid());
                        dto.setMaterialName(sxMaterialFormTemp.getMaterialName());
                        dtoList.add(dto);
                    }
                }
            }
            System.out.println("接口耗时" + (System.currentTimeMillis() - start));

            return new AsyncResult<>("接口耗时" + (System.currentTimeMillis() - start));
        }finally {
            countDownLatch.countDown();
        }
    }
}
