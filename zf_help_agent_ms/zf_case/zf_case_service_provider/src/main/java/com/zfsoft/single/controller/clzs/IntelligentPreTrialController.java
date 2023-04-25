package com.zfsoft.single.controller.clzs;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.cases.data.QlCase;
import com.zfsoft.cases.data.QlCaseApplay;
import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.data.QlCaseMaterialAtta;
import com.zfsoft.cases.service.QlCaseApplayService;
import com.zfsoft.cases.service.QlCaseMaterialAttaService;
import com.zfsoft.cases.service.QlCaseMaterialService;
import com.zfsoft.cases.service.QlCaseService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.sxService.data.SxServiceMaterial;
import com.zfsoft.service.sxService.service.SxServiceMaterialService;
import com.zfsoft.single.data.clzs.FaMaterialPicOcrResult;
import com.zfsoft.single.data.clzs.dto.PreMaterialAttaVo;
import com.zfsoft.single.data.yxpz.FaModelRuleValidation;
import com.zfsoft.single.manager.clzs.AuditResultManager;
import com.zfsoft.single.manager.clzs.FaMaterialPicOcrResultManager;

import com.zfsoft.single.manager.clzs.IntelligentPreTrialManager;
import com.zfsoft.single.manager.yxpz.FaModelRuleValidationManager;
import com.zfsoft.single.service.clzs.IntelligentPreTrialService;
import com.zfsoft.single.util.FaStaticParam;
import com.zfsoft.single.util.fa.atta.AttaBase64Result;
import com.zfsoft.single.util.fa.atta.FileManageUtil;
import com.zfsoft.superwindow.dbaccess.data.DbFaMaterialPicOcrResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;


/**
 * @author: liangss
 * @create: 2020-12-10 17:01:29
 * @description: 智能预审控制层
 */
@Slf4j
@RestController
public class IntelligentPreTrialController implements IntelligentPreTrialService {

    //智审
    @Resource
    private IntelligentPreTrialManager intelligentPreTrialManager;
    @Resource
    private QlCaseMaterialService qlCaseMaterialServiceFeginService;
    @Resource
    private SxServiceMaterialService sxServiceMaterialFeginService;
    //办件信息
    @Resource
    private QlCaseService qlCaseServiceFeginService;
    @Resource
    private QlCaseMaterialAttaService qlCaseMaterialAttaServiceFeginService;
    @Resource
    private FaMaterialPicOcrResultManager faMaterialPicOcrResultManager;

    @Resource
    private QlCaseApplayService qlCaseApplayServiceFeginService;

    @Resource
    private AuditResultManager auditResultManager;
    @Resource
    private FaModelRuleValidationManager faModelRuleValidationManager;


    @Value("${zfsoft.electronicForm.url}")
    private String requestUrl;

    @Value("${zfsoft.electronicForm.getFormApiDataUrl}")
    private String getFormApiDataUrl;

    @Value("${zfsoft.electronicForm.formPageUrl}")
    private String formPageUrl;

    @Value("${zfsoft.electronicForm.authorizeKey}")
    private String authorizeKey;


    @GetMapping(value = "/getFormApiData")
    public ApiResultSet getFormApiData(String reportOid, String designOid, String authorizeKey) {
        //解析办件信息
        Map<String, Object> map = new HashMap<>();
        String uriString = requestUrl + getFormApiDataUrl + "?reportOid=" + reportOid + "&designOid=" + designOid + "&authorizeKey=" + authorizeKey;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> resEntity = restTemplate.exchange(uriString.toString(), HttpMethod.GET, requestEntity, String.class);
        String body = resEntity.getBody();
        if (StringUtils.isNotEmpty(body)) {
            JSONObject json_test = JSONObject.parseObject(body);
            JSONObject json = json_test.getJSONObject("data");
            String formData = json.getString("formData");
            if (StringUtils.isNotEmpty(formData)) {
                JSONObject json_formData = JSONObject.parseObject(formData);
                Map<String, Object> formExmap = (Map<String, Object>) json_formData;
                for (String key : formExmap.keySet()) {//keySet获取map集合key的集合  然后在遍历key即可
                    Object value = formExmap.get(key);
                    if (value instanceof String) {
                        map.put(key, value.toString());
                    } else if (value instanceof Boolean) {
                        map.put(key, value.toString());
                    } else if (value instanceof JSONArray) {
                        if (((JSONArray) value).size() == 1) {
                            map.put(key, ((JSONArray) value).get(0).toString());
                        } else if (((JSONArray) value).size() > 1) {
                            String blqz = "";
                            for (int i = 0; i < ((JSONArray) value).size(); i++) {
                                blqz = blqz + ((JSONArray) value).get(i).toString() + ";";
                            }
                            map.put(key, blqz);
                        }
                    } else if (value instanceof JSONObject) {
                        Map<String, Object> map2 = (Map<String, Object>) value;
                        for (String key2 : map2.keySet()) {
                            Object value2 = map2.get(key2);
                            if (value2 instanceof String) {
                                map.put(key2, value2.toString());
                            } else if (value2 instanceof Boolean) {
                                map.put(key2, value2.toString());
                            } else if (value2 instanceof JSONArray) {//数组取第一条数据
                                if (((JSONArray) value2).size() > 0) {
                                    Object array3 = ((JSONArray) value2).get(0);
                                    if (array3 instanceof JSONObject) {
                                        Map<String, Object> child3 = (Map<String, Object>) array3;
                                        for (String key33 : child3.keySet()) {
                                            Object child3a = child3.get(key33);//
                                            if (child3a instanceof JSONArray || child3a instanceof JSONObject) {
                                            } else {
                                                map.put(key33, child3a.toString());
                                            }
                                        }
                                    }
                                }
                            } else if (value2 instanceof JSONObject) {
                                Map<String, Object> child3 = (Map<String, Object>) value2;
                                for (String key3 : child3.keySet()) {
                                    Object child3a = child3.get(key3);
                                    if (child3a instanceof JSONArray || child3a instanceof JSONObject) {
                                    } else {
                                        map.put(key3, child3a.toString());
                                    }
                                }
                            } else {
                                map.put(key2, value2.toString());
                            }
                        }
                    } else {
                        map.put(key, value.toString());
                    }

                }
            }

        }

        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<Map<String, Object>>();
        apiResultSet.setData(map);
        return apiResultSet;
    }


    /***
     * @Description: 获取验证规则列表根据目录元素oid（用于目录元素修改验证）
     * @Author:liangss
     * @Date:2021/10/20
     * @Param: []
     */
    @GetMapping(value = "/queryFaModelRuleValidationList")
    public ApiResultSet queryFaModelRuleValidationList(String templateMetadataOid) {
        FaModelRuleValidation faModelRuleValidation = new FaModelRuleValidation();
        faModelRuleValidation.setTemplateMetadataOid(templateMetadataOid);
        List<FaModelRuleValidation> faModelRuleValidationList = faModelRuleValidationManager.queryFaModelRuleValidationList(faModelRuleValidation);
        return new ApiResultSet(faModelRuleValidationList);
    }


    @GetMapping(value = "/intelligentPretrialmaterialPrePrialAllNewXN")
    public ApiResultSet intelligentPretrialmaterialPrePrialAllNewXN(HttpServletRequest request, String caseOid) {
        log.info("办件：{" + caseOid + "},进入intelligentPretrialmaterialPrePrialAllNew预审方法");
        Map<String, Object> modelMap = new HashMap<>();
        Map<String, Object> modelMapSH = new HashMap<>();
        List<Map<String, Object>> xxmap = new ArrayList<>();
        String serviceOid = "";
        try {
            if (StringUtils.isNotEmpty(caseOid)) {
                //删除之前的智审信息
                faMaterialPicOcrResultManager.updateDbFaMaterialPicOcrResultByCaselOid(caseOid);
                //办件信息
                ApiResultSet<QlCase> resultSet = qlCaseServiceFeginService.queryQlCaseByCaseOid(caseOid);
                QlCase qlCase = null;
                if (resultSet.getData() != null) {
                    qlCase = resultSet.getData();
                }
                serviceOid = qlCase.getServiceOid();
                log.info("办件idcaseOid=" + caseOid + "事项oid=" + serviceOid);
                //上传材料信息
                ApiResultSet<List<QlCaseMaterial>> qlcmResult = qlCaseMaterialServiceFeginService.queryQlCaseMaterialByCaseOid(caseOid);
                List<QlCaseMaterial> qlCaseMaterialList = qlcmResult.getData();
                if (null != qlCaseMaterialList) {
                    log.info("材料数量=" + qlCaseMaterialList.size());
                    for (QlCaseMaterial qlCaseMaterial : qlCaseMaterialList) {
                        //根据办件材料oid删除
                        ApiResultSet<List<QlCaseMaterialAtta>> qlCaseMaterialAttaResult = qlCaseMaterialAttaServiceFeginService.queryQlCaseMaterialAttaByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
                        if (null != qlCaseMaterialAttaResult.getData()) {
                            log.info("材料" + qlCaseMaterial.getMaterialName() + "有" + qlCaseMaterialAttaResult.getData().size() + "附件");
                            List<QlCaseMaterialAtta> qlCaseMaterialAttaList = qlCaseMaterialAttaResult.getData();
                            for (QlCaseMaterialAtta qlCaseMaterialAtta : qlCaseMaterialAttaList) {
                                String materialAttaOid = qlCaseMaterialAtta.getMaterialAttaOid();
                                String materialCatalogOid = qlCaseMaterialAtta.getMaterialCatalogOid();
                                String refinedMaterialOid = qlCaseMaterialAtta.getRefinedMaterialOid();
                                String attaOid = qlCaseMaterialAtta.getAttaOid();
                                //有细化材料和目录关联oid的数据参与智审
                                if (StringUtils.isNotEmpty(materialCatalogOid) && StringUtils.isNotEmpty(refinedMaterialOid) && StringUtils.isNotEmpty(attaOid)) {
                                    log.info("开始预审MaterialName=" + qlCaseMaterial.getMaterialName());
                                    Map<String, Object> parmMap = new HashMap<>();
                                    parmMap.put("caseOid", caseOid);
                                    parmMap.put("attaOid", attaOid);
                                    parmMap.put("materialOid", qlCaseMaterial.getMaterialOid());
                                    parmMap.put("caseMaterialOid", qlCaseMaterial.getCaseMaterialOid());
                                    parmMap.put("refinedMaterialOid", refinedMaterialOid);
                                    parmMap.put("materialCatalogOid", materialCatalogOid);
                                    parmMap.put("materialAttaOid", materialAttaOid);
                                    parmMap.put("materialName", qlCaseMaterial.getMaterialName());
                                    xxmap.add(parmMap);
                                    /*   intelligentPretrialmaterialPrePrialBaiduOcr   intelligentPretrialmaterialPrePrialOLDO  intelligentPretrialmaterialPrePrial*/
                                    modelMapSH = intelligentPreTrialManager.intelligentPretrialmaterialPrePrialXn(caseOid, attaOid,
                                            qlCaseMaterial.getMaterialOid(), qlCaseMaterial.getCaseMaterialOid(),
                                            refinedMaterialOid, materialCatalogOid, materialAttaOid, qlCaseMaterial.getMaterialName()
                                    );
                                    modelMap.put(qlCaseMaterialAtta.getMaterialAttaOid() + "-" + qlCaseMaterial.getMaterialName(), modelMapSH.get("message"));
                                    log.info("预审结束MaterialName=" + qlCaseMaterial.getMaterialName());
                                } else {
                                    modelMap.put(qlCaseMaterialAtta.getMaterialAttaOid() + "-" + qlCaseMaterial.getMaterialName(), "不参与智审");
                                }

                            }

                        }
                    }


                    //预审走线程
                   /* List<List< Map<String, Object> >> xxmapList=groupList(xxmap);
                    for(List< Map<String, Object> >  mapList:  xxmapList){
                        String jj= AuditUtil.intelligentPretrial(mapList);
                    }
                    log.info("线程获取ocr数据结束");*/

                    //线程预审
                  /* String jj= AuditUtil.intelligentPretrial(xxmap);
                   log.info(jj+"线程结束");*/
                }
                //保存审核结果信息
                /*log.info("保存办件：{"+caseOid+"},审核结果信息AuditResult开始");
                List<QlCaseMaterial> qlCaseMaterials=intelligentPreTrialManager.getQlCaseMaterialListAndAuditResult(request, caseOid,serviceOid);
                log.info("保存办件：{"+caseOid+"},审核结果信息AuditResult结束");*/
            }

            log.info("办件：{" + caseOid + "},结束intelligentPretrialmaterialPrePrialAllNew预审方法");
            modelMap.put("success", true);

        } catch (Exception e) {
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("message", e.getMessage());
            log.error("intelligentPretrialmaterialPrePrialAllNewXN : {}", e.getMessage());
        }
        return new ApiResultSet(modelMap);
    }


    /***
     * @Description: 通过caseOid整体预审并保存预审结果、保存材料审核状态
     * @Author:liangss
     * @Date:2021/7/08
     * @Param: [request, caseOid]
     */
    @GetMapping(value = "/intelligentPretrialmaterialPrePrialAllNew")
    public ApiResultSet intelligentPretrialmaterialPrePrialAllNew(HttpServletRequest request, String caseOid) {

        Long beginTime = System.currentTimeMillis();


        log.info("办件：{" + caseOid + "},进入intelligentPretrialmaterialPrePrialAllNew预审方法");
        Map<String, Object> modelMap = new HashMap<>();
        Map<String, Object> modelMapSH = new HashMap<>();
        List<Map<String, Object>> xxmap = new ArrayList<>();
        String serviceOid = "";
        try {
            if (StringUtils.isNotEmpty(caseOid)) {
                //删除之前的智审信息
                faMaterialPicOcrResultManager.updateDbFaMaterialPicOcrResultByCaselOid(caseOid);
                //办件信息
                ApiResultSet<QlCase> resultSet = qlCaseServiceFeginService.queryQlCaseByCaseOid(caseOid);
                QlCase qlCase = null;
                if (resultSet.getData() != null) {
                    qlCase = resultSet.getData();
                }
                serviceOid = qlCase.getServiceOid();
                log.info("办件idcaseOid=" + caseOid + "事项oid=" + serviceOid);
                //上传材料信息
                ApiResultSet<List<QlCaseMaterial>> qlcmResult = qlCaseMaterialServiceFeginService.queryQlCaseMaterialByCaseOid(caseOid);
                List<QlCaseMaterial> qlCaseMaterialList = qlcmResult.getData();
                if (null != qlCaseMaterialList) {
                    log.info("材料数量=" + qlCaseMaterialList.size());
                    for (QlCaseMaterial qlCaseMaterial : qlCaseMaterialList) {
                        //根据办件材料oid删除
                        ApiResultSet<List<QlCaseMaterialAtta>> qlCaseMaterialAttaResult = qlCaseMaterialAttaServiceFeginService.queryQlCaseMaterialAttaByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
                        if (null != qlCaseMaterialAttaResult.getData()) {
                            log.info("材料" + qlCaseMaterial.getMaterialName() + "有" + qlCaseMaterialAttaResult.getData().size() + "附件");
                            List<QlCaseMaterialAtta> qlCaseMaterialAttaList = qlCaseMaterialAttaResult.getData();
                            for (QlCaseMaterialAtta qlCaseMaterialAtta : qlCaseMaterialAttaList) {
                                String materialAttaOid = qlCaseMaterialAtta.getMaterialAttaOid();
                                String materialCatalogOid = qlCaseMaterialAtta.getMaterialCatalogOid();
                                String refinedMaterialOid = qlCaseMaterialAtta.getRefinedMaterialOid();
                                String attaOid = qlCaseMaterialAtta.getAttaOid();
                                //有细化材料和目录关联oid的数据参与智审
                                if (StringUtils.isNotEmpty(materialCatalogOid) && StringUtils.isNotEmpty(refinedMaterialOid) && StringUtils.isNotEmpty(attaOid)) {
                                    log.info("开始预审MaterialName=" + qlCaseMaterial.getMaterialName());
                                    Map<String, Object> parmMap = new HashMap<>();
                                    parmMap.put("caseOid", caseOid);
                                    parmMap.put("attaOid", attaOid);
                                    parmMap.put("materialOid", qlCaseMaterial.getMaterialOid());
                                    parmMap.put("caseMaterialOid", qlCaseMaterial.getCaseMaterialOid());
                                    parmMap.put("refinedMaterialOid", refinedMaterialOid);
                                    parmMap.put("materialCatalogOid", materialCatalogOid);
                                    parmMap.put("materialAttaOid", materialAttaOid);
                                    parmMap.put("materialName", qlCaseMaterial.getMaterialName());
                                    xxmap.add(parmMap);
                                    /*   intelligentPretrialmaterialPrePrialBaiduOcr   intelligentPretrialmaterialPrePrialOLDO  intelligentPretrialmaterialPrePrial*/
                                    modelMapSH = intelligentPreTrialManager.intelligentPretrialmaterialPrePrialBaiduOcr(caseOid, attaOid,
                                            qlCaseMaterial.getMaterialOid(), qlCaseMaterial.getCaseMaterialOid(),
                                            refinedMaterialOid, materialCatalogOid, materialAttaOid, qlCaseMaterial.getMaterialName()
                                    );
                                    modelMap.put(qlCaseMaterialAtta.getMaterialAttaOid() + "-" + qlCaseMaterial.getMaterialName(), modelMapSH.get("message"));
                                    log.info("预审结束MaterialName=" + qlCaseMaterial.getMaterialName());
                                } else {
                                    modelMap.put(qlCaseMaterialAtta.getMaterialAttaOid() + "-" + qlCaseMaterial.getMaterialName(), "不参与智审");
                                }

                            }

                        }
                    }

                    /* String jj= AuditUtil.intelligentPretrial(xxmap);*/
                    //预审走线程
         /*      List<List< Map<String, Object> >> xxmapList=groupList(xxmap);
                for(List< Map<String, Object> >  mapList:  xxmapList){
                    String jj= AuditUtil.intelligentPretrial(mapList);
                }
                log.info("线程获取ocr数据结束");*/
                    //线程预审
                  /* String jj= AuditUtil.intelligentPretrial(xxmap);
                   log.info(jj+"线程结束");*/
                }
                //保存审核结果信息
               /* log.info("保存办件：{"+caseOid+"},审核结果信息AuditResult开始");
                List<QlCaseMaterial> qlCaseMaterials=intelligentPreTrialManager.getQlCaseMaterialListAndAuditResult(request, caseOid,serviceOid);
                log.info("保存办件：{"+caseOid+"},审核结果信息AuditResult结束");*/
            }

            //接口调用时间
            Long totalConsumedTime = System.currentTimeMillis() - beginTime;
            modelMap.put("totalConsumedTime", totalConsumedTime);

            log.info("办件：{" + caseOid + "},结束intelligentPretrialmaterialPrePrialAllNew预审方法");
            modelMap.put("success", true);

        } catch (Exception e) {
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("message", e.getMessage());
            log.error("intelligentPretrialmaterialPrePrialAllNew : {}", e.getMessage());
        }
        return new ApiResultSet(modelMap);
    }

    /***
     * @Description: 保存之后进行规则比对获取数据
     * @Author:liangss
     * @Date:2021/11/25
     * @Param: [request, caseOid, serviceOid]
     */
    @GetMapping(value = "/getRuleAuditResult")
    public ApiResultSet getRuleAuditResult(HttpServletRequest request, String caseOid, String serviceOid) throws Exception {
        Map<String, Object> modelMap = new HashMap<>();
        List<QlCaseMaterial> qlCaseMaterials = intelligentPreTrialManager.getQlCaseMaterialListAndAuditResult(request, caseOid, serviceOid);
        return new ApiResultSet(modelMap);
    }

    public List<List<Map<String, Object>>> groupList(List<Map<String, Object>> list) {
        List<List<Map<String, Object>>> listGroup = new ArrayList<List<Map<String, Object>>>();
        int listSize = list.size();
        //子集合的长度
        int toIndex = 2;
        for (int i = 0; i < list.size(); i += 2) {
            if (i + 2 > listSize) {
                toIndex = listSize - i;
            }
            List<Map<String, Object>> newList = list.subList(i, i + toIndex);
            listGroup.add(newList);
        }
        return listGroup;
    }

    /**
     * 获取智审材料列表
     *
     * @param caseOid
     * @param serviceOid
     * @return
     */
    @Override
    public ApiResultSet getIntelligentAuditMaterialList(String caseOid, String serviceOid) {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            // 智审材料列表
            List<QlCaseMaterial> qlCaseMaterials = intelligentPreTrialManager
                    .getIntelligentAuditMaterialList(caseOid, serviceOid);
            modelMap.put("qlCaseMaterials", qlCaseMaterials);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("message", e.getMessage());
            log.info(e.getMessage());
        }
        return new ApiResultSet(modelMap);
    }


    /**
     * 获取办件材料列表以及审核结果(用于智能审核页面列表查看)(根据结果获取)
     *
     * @param request
     * @param caseOid
     * @return
     */
    @GetMapping(value = "/getQlCaseMaterialListAndAuditResult")
    public ApiResultSet getQlCaseMaterialListAndAuditResult(HttpServletRequest request, String caseOid, String serviceOid) {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            //材料列表
            List<QlCaseMaterial> qlCaseMaterials = intelligentPreTrialManager.getQlCaseMaterialListAndAuditResult(request, caseOid, serviceOid);
           /* ApiResultSet<List<QlCaseMaterial>> qlcmResult=qlCaseMaterialServiceFeginService.queryQlCaseMaterialByCaseOid(caseOid);
            List<QlCaseMaterial> qlCaseMaterials = null;
            if(null!=qlcmResult.getData()){
                qlCaseMaterials=qlcmResult.getData();
                if(qlCaseMaterials.size()>0){
                    qlCaseMaterials= intelligentPreTrialManager.getQlCaseMaterialListAndAuditResult(qlCaseMaterials);
                }
            }*/
            //List<QlCaseMaterial> qlCaseMaterials=intelligentPreTrialManager.getQlCaseMaterialListAndAuditResult(request, caseOid,serviceOid);
            modelMap.put("qlCaseMaterials", qlCaseMaterials);
            // log.info("获取qlCaseMaterials成功：{}", JSON.toJSONString(qlCaseMaterials));
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("message", e.getMessage());
            log.info(e.getMessage());
        }
        return new ApiResultSet(modelMap);
    }


    /***
     * @Description: 查询审核信息信息(用于智能审核页面列表查看)
     * @Author:liangss
     * @Date:2021/7/9
     * @Param: [caseOid, caseMaterialOid, materialOid, serviceOid]
     */
    @Override
    public ApiResultSet viewDetailResult(
            String caseOid,
            String caseMaterialOid,
            String materialOid,
            String serviceOid) {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            modelMap = intelligentPreTrialManager.viewDetailResult(caseOid, caseMaterialOid, materialOid, serviceOid);
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("message", e.getMessage());
        }
        return new ApiResultSet(modelMap);
    }

    /**
     * 根据材料id查看材料审查要点信息
     *
     * @param materialOid
     * @return
     */
    @GetMapping(value = "/viewDetailRefinedMaterial")
    public ApiResultSet viewDetailRefinedMaterial(String materialOid) {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            modelMap = intelligentPreTrialManager.viewDetailRefinedMaterial(materialOid);
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("message", e.getMessage());
        }
        return new ApiResultSet(modelMap);
    }


    /***
     * @Description: 确认材料无误
     * @Author:liangss
     * @Date:2021/7/30
     * @Param: [caseMaterialOid 办件材料oid]
     */
    @GetMapping(value = "/confirmQlCaseMaterialt")
    public ApiResultSet confirmQlCaseMaterialt(String caseMaterialOid) {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            //根据材料oid获取办件材料
            ApiResultSet<QlCaseMaterial> qlCaseMaterialApiResultSet = qlCaseMaterialServiceFeginService.queryMaterialByCaseMaterialOid(caseMaterialOid);
            if (null != qlCaseMaterialApiResultSet.getData()) {
                QlCaseMaterial qlCaseMaterial = qlCaseMaterialApiResultSet.getData();
                qlCaseMaterial.setConfirmStatus("Y");
                qlCaseMaterialServiceFeginService.updateQlCaseMaterial(qlCaseMaterial);
                modelMap.put("qlCaseMaterials", qlCaseMaterial);
            }
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("message", e.getMessage());
            log.info(e.getMessage());
        }
        return new ApiResultSet(modelMap);
    }


    /***
     * @Description: 根据办件材料oid获取ocr信息（纠正材料时用）
     * @Author:liangss
     * @Date:2021/7/30
     * @Param: [caseMaterialOid]
     */
    @GetMapping(value = "/intelligentPretrialmaterialPrePrialByCaseMaterialOid")
    public ApiResultSet intelligentPretrialmaterialPrePrialByCaseMaterialOid(HttpServletRequest request, String caseMaterialOid) {
        Map<String, Object> modelMap = new HashMap<>();
        Map<String, Object> modelMapSH = new HashMap<>();
        QlCaseMaterial qlCaseMaterial = null;
        String serviceOid = "";
        try {
            if (StringUtils.isNotEmpty(caseMaterialOid)) {
                //删除之前的智审信息
                faMaterialPicOcrResultManager.updateDbFaMaterialPicOcrResultByCaseMaterialOid(caseMaterialOid);
                //上传材料信息
                //根据材料oid获取办件材料
                ApiResultSet<QlCaseMaterial> qlCaseMaterialApiResultSet = qlCaseMaterialServiceFeginService.queryMaterialByCaseMaterialOid(caseMaterialOid);
                if (null != qlCaseMaterialApiResultSet.getData()) {
                    qlCaseMaterial = qlCaseMaterialApiResultSet.getData();
                }
                //办件信息
                ApiResultSet<QlCase> resultSet = qlCaseServiceFeginService.queryQlCaseByCaseOid(qlCaseMaterial.getCaseOid());
                QlCase qlCase = null;
                if (resultSet.getData() != null) {
                    qlCase = resultSet.getData();
                }
                serviceOid = qlCase.getServiceOid();
                //根据办件材料oid删除
                ApiResultSet<List<QlCaseMaterialAtta>> qlCaseMaterialAttaResult = qlCaseMaterialAttaServiceFeginService.queryQlCaseMaterialAttaByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
                if (null != qlCaseMaterialAttaResult.getData()) {
                    List<QlCaseMaterialAtta> qlCaseMaterialAttaList = qlCaseMaterialAttaResult.getData();
                    for (QlCaseMaterialAtta qlCaseMaterialAtta : qlCaseMaterialAttaList) {
                        String materialAttaOid = qlCaseMaterialAtta.getMaterialAttaOid();
                        String materialCatalogOid = qlCaseMaterialAtta.getMaterialCatalogOid();
                        String refinedMaterialOid = qlCaseMaterialAtta.getRefinedMaterialOid();
                        String attaOid = qlCaseMaterialAtta.getAttaOid();
                        //有细化材料和目录关联oid的数据参与智审
                        if (StringUtils.isNotEmpty(materialCatalogOid) && StringUtils.isNotEmpty(refinedMaterialOid) && StringUtils.isNotEmpty(attaOid)) {
                            log.info("开始获取调用百度ocr接口=" + qlCaseMaterial.getMaterialName());
                        /*   modelMapSH=intelligentPreTrialManager.intelligentPretrialmaterialPrePrial(qlCaseMaterial.getCaseOid(),attaOid,
                                    qlCaseMaterial.getMaterialOid(),qlCaseMaterial.getCaseMaterialOid(),
                                    refinedMaterialOid,materialCatalogOid,materialAttaOid,qlCaseMaterial.getMaterialName() );*/

                            /*  intelligentPretrialmaterialPrePrialBaiduOcr  intelligentPretrialmaterialPrePrialOLDO  intelligentPretrialmaterialPrePrialOLDO*/

                            modelMapSH = intelligentPreTrialManager.intelligentPretrialmaterialPrePrialBaiduOcr(qlCaseMaterial.getCaseOid(), attaOid,
                                    qlCaseMaterial.getMaterialOid(), qlCaseMaterial.getCaseMaterialOid(),
                                    refinedMaterialOid, materialCatalogOid, materialAttaOid, qlCaseMaterial.getMaterialName()
                            );
                            modelMap.put(qlCaseMaterialAtta.getMaterialAttaOid() + "-" + qlCaseMaterial.getMaterialName(), modelMapSH.get("message"));

                            log.info("获取调用百度ocr接口结束=" + qlCaseMaterial.getMaterialName() + "****" + modelMapSH.get("message"));
                        } else {
                            modelMap.put(qlCaseMaterialAtta.getMaterialAttaOid() + "-" + qlCaseMaterial.getMaterialName(), "不参与智审");
                        }

                    }

                }
                //保存审核结果信息
                QlCaseMaterial qlCaseMaterials = intelligentPreTrialManager.getQlCaseMaterialListAndAuditResultByCaseMaterialOid
                        (request, qlCaseMaterial.getCaseOid(), serviceOid, caseMaterialOid, qlCaseMaterial.getMaterialOid(), qlCaseMaterial);

            }
            modelMap.put("success", true);

        } catch (Exception e) {

            modelMap.put("success", false);
            modelMap.put("message", e.getMessage());
        }
        return new ApiResultSet(modelMap);
    }

    @Override
    public ApiResultSet intelligentAuditByCaseMaterialOid(String caseMaterialOid) {
        Map<String, Object> modelMap = new HashMap<>();
        Map<String, Object> modelMapSH = new HashMap<>();
        QlCaseMaterial qlCaseMaterial = null;
        String serviceOid = "";
        try {
            if (StringUtils.isNotEmpty(caseMaterialOid)) {
                //删除之前的智审信息
                faMaterialPicOcrResultManager.updateDbFaMaterialPicOcrResultByCaseMaterialOid(caseMaterialOid);
                //上传材料信息
                //根据材料oid获取办件材料
                ApiResultSet<QlCaseMaterial> qlCaseMaterialApiResultSet = qlCaseMaterialServiceFeginService.queryMaterialByCaseMaterialOid(caseMaterialOid);
                if (null != qlCaseMaterialApiResultSet.getData()) {
                    qlCaseMaterial = qlCaseMaterialApiResultSet.getData();
                }
                //办件信息
                ApiResultSet<QlCase> resultSet = qlCaseServiceFeginService.queryQlCaseByCaseOid(qlCaseMaterial.getCaseOid());
                QlCase qlCase = null;
                if (resultSet.getData() != null) {
                    qlCase = resultSet.getData();
                }
                serviceOid = qlCase.getServiceOid();
                //根据办件材料oid删除
                ApiResultSet<List<QlCaseMaterialAtta>> qlCaseMaterialAttaResult = qlCaseMaterialAttaServiceFeginService.queryQlCaseMaterialAttaByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
                if (null != qlCaseMaterialAttaResult.getData()) {
                    List<QlCaseMaterialAtta> qlCaseMaterialAttaList = qlCaseMaterialAttaResult.getData();
                    for (QlCaseMaterialAtta qlCaseMaterialAtta : qlCaseMaterialAttaList) {
                        String materialAttaOid = qlCaseMaterialAtta.getMaterialAttaOid();
                        String materialCatalogOid = qlCaseMaterialAtta.getMaterialCatalogOid();
                        String refinedMaterialOid = qlCaseMaterialAtta.getRefinedMaterialOid();
                        String attaOid = qlCaseMaterialAtta.getAttaOid();
                        //有细化材料和目录关联oid的数据参与智审
                        if (StringUtils.isNotEmpty(materialCatalogOid) && StringUtils.isNotEmpty(refinedMaterialOid) && StringUtils.isNotEmpty(attaOid)) {
                            log.info("开始获取调用百度ocr接口=" + qlCaseMaterial.getMaterialName());
                            modelMapSH = intelligentPreTrialManager.intelligentPretrialmaterialPrePrialBaiduOcr(qlCaseMaterial.getCaseOid(), attaOid,
                                    qlCaseMaterial.getMaterialOid(), qlCaseMaterial.getCaseMaterialOid(),
                                    refinedMaterialOid, materialCatalogOid, materialAttaOid, qlCaseMaterial.getMaterialName()
                            );
                            modelMap.put(qlCaseMaterialAtta.getMaterialAttaOid() + "-" + qlCaseMaterial.getMaterialName(), modelMapSH.get("message"));

                            log.info("获取调用百度ocr接口结束=" + qlCaseMaterial.getMaterialName() + "****" + modelMapSH.get("message"));
                        } else {
                            modelMap.put(qlCaseMaterialAtta.getMaterialAttaOid() + "-" + qlCaseMaterial.getMaterialName(), "不参与智审");
                        }

                    }

                }
                //保存审核结果信息
                QlCaseMaterial qlCaseMaterials = intelligentPreTrialManager.getQlCaseMaterialListAndAuditResultByCaseMaterialOid
                        (null, qlCaseMaterial.getCaseOid(), serviceOid, caseMaterialOid, qlCaseMaterial.getMaterialOid(), qlCaseMaterial);

            }
            modelMap.put("success", true);

        } catch (Exception e) {

            modelMap.put("success", false);
            modelMap.put("message", e.getMessage());
        }
        return new ApiResultSet(modelMap);
    }


    @GetMapping(value = "/intelligentPretrialmaterialPrePrialByCaseMaterialOidCs")
    public ApiResultSet intelligentPretrialmaterialPrePrialByCaseMaterialOidCs(HttpServletRequest request, String caseMaterialOid) {
        Map<String, Object> modelMap = new HashMap<>();
        Map<String, Object> modelMapSH = new HashMap<>();
        QlCaseMaterial qlCaseMaterial = null;
        String serviceOid = "";
        try {
            if (StringUtils.isNotEmpty(caseMaterialOid)) {
                //删除之前的智审信息
                /* faMaterialPicOcrResultManager.updateDbFaMaterialPicOcrResultByCaseMaterialOid(caseMaterialOid);*/
                //上传材料信息
                //根据材料oid获取办件材料
                ApiResultSet<QlCaseMaterial> qlCaseMaterialApiResultSet = qlCaseMaterialServiceFeginService.queryMaterialByCaseMaterialOid(caseMaterialOid);
                if (null != qlCaseMaterialApiResultSet.getData()) {
                    qlCaseMaterial = qlCaseMaterialApiResultSet.getData();
                }
                //办件信息
                ApiResultSet<QlCase> resultSet = qlCaseServiceFeginService.queryQlCaseByCaseOid(qlCaseMaterial.getCaseOid());
                QlCase qlCase = null;
                if (resultSet.getData() != null) {
                    qlCase = resultSet.getData();
                }
                serviceOid = qlCase.getServiceOid();
                //根据办件材料oid删除
                ApiResultSet<List<QlCaseMaterialAtta>> qlCaseMaterialAttaResult = qlCaseMaterialAttaServiceFeginService.queryQlCaseMaterialAttaByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
                if (null != qlCaseMaterialAttaResult.getData()) {
                    List<QlCaseMaterialAtta> qlCaseMaterialAttaList = qlCaseMaterialAttaResult.getData();
                    for (QlCaseMaterialAtta qlCaseMaterialAtta : qlCaseMaterialAttaList) {
                        String materialAttaOid = qlCaseMaterialAtta.getMaterialAttaOid();
                        String materialCatalogOid = qlCaseMaterialAtta.getMaterialCatalogOid();
                        String refinedMaterialOid = qlCaseMaterialAtta.getRefinedMaterialOid();
                        String attaOid = qlCaseMaterialAtta.getAttaOid();
                        //有细化材料和目录关联oid的数据参与智审
                        if (StringUtils.isNotEmpty(materialCatalogOid) && StringUtils.isNotEmpty(refinedMaterialOid) && StringUtils.isNotEmpty(attaOid)) {
                            log.info("开始获取调用百度ocr接口=" + qlCaseMaterial.getMaterialName());
                        /*   modelMapSH=intelligentPreTrialManager.intelligentPretrialmaterialPrePrial(qlCaseMaterial.getCaseOid(),attaOid,
                                    qlCaseMaterial.getMaterialOid(),qlCaseMaterial.getCaseMaterialOid(),
                                    refinedMaterialOid,materialCatalogOid,materialAttaOid,qlCaseMaterial.getMaterialName() );*/

                            /*  intelligentPretrialmaterialPrePrialBaiduOcr  intelligentPretrialmaterialPrePrialOLDO  intelligentPretrialmaterialPrePrialOLDO*/
/*
                            modelMapSH=intelligentPreTrialManager.intelligentPretrialmaterialPrePrialBaiduOcr(qlCaseMaterial.getCaseOid(),attaOid,
                                    qlCaseMaterial.getMaterialOid(),qlCaseMaterial.getCaseMaterialOid(),
                                    refinedMaterialOid,materialCatalogOid,materialAttaOid,qlCaseMaterial.getMaterialName()
                            );
                            modelMap.put(qlCaseMaterialAtta.getMaterialAttaOid()+"-"+qlCaseMaterial.getMaterialName(),modelMapSH.get("message"));*/

                            log.info("获取调用百度ocr接口结束=" + qlCaseMaterial.getMaterialName() + "****" + modelMapSH.get("message"));
                        } else {
                            modelMap.put(qlCaseMaterialAtta.getMaterialAttaOid() + "-" + qlCaseMaterial.getMaterialName(), "不参与智审");
                        }

                    }

                }

                //保存审核结果信息
                QlCaseMaterial qlCaseMaterials = intelligentPreTrialManager.getQlCaseMaterialListAndAuditResultByCaseMaterialOid
                        (request, qlCaseMaterial.getCaseOid(), serviceOid, caseMaterialOid, qlCaseMaterial.getMaterialOid(), qlCaseMaterial);

            }
            modelMap.put("success", true);

        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("message", e.getMessage());
        }
        return new ApiResultSet(modelMap);
    }


    /**
     * 获取办件材料列表以及审核结果
     *
     * @param request
     * @param caseOid
     * @return
     */
    @GetMapping(value = "/getQlCaseMaterialListAndPreResult")
    public ApiResultSet getQlCaseMaterialListAndPreResult(HttpServletRequest request, String caseOid, String serviceOid) {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            //材料列表
            List<QlCaseMaterial> qlCaseMaterials = intelligentPreTrialManager.getQlCaseMaterialListAndAuditResult(request, caseOid, serviceOid);
            modelMap.put("qlCaseMaterials", qlCaseMaterials);
            log.info("获取qlCaseMaterials成功：{}", JSON.toJSONString(qlCaseMaterials));
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("message", e.getMessage());
            log.info(e.getMessage());
        }
        return new ApiResultSet(modelMap);
    }


    /**
     * 根据业务oid把审核状态改为忽略状态
     *
     * @param oid
     * @return
     */
    @GetMapping(value = "/updateIsIgnoreByOid")
    public ApiResultSet updateIsIgnoreByOid(String oid) {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            auditResultManager.updateIsIgnoreByOid(oid);
            modelMap.put("success", true);
            modelMap.put("message", "忽略成功");
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("message", e.getMessage());
        }
        return new ApiResultSet(modelMap);
    }


    public static void mainold1(String[] args) throws Exception {
        SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDay = simFormat.parse("1994-04-23");
        /* Date birthDay=new S    '1994-04-23';*/
        int age = getAgeByBirth(birthDay);

        log.info("age=" + age);
    }


    public static int getAgeByBirth(Date birthDay) {
        int age = 0;
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);  //当前年份
        int monthNow = cal.get(Calendar.MONTH);  //当前月份
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        age = yearNow - yearBirth;   //计算整岁数
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                } //当前日期在生日之前，年龄减一
            } else {
                age--;//当前月份在生日之前，年龄减一
            }
        }
        return age;
    }


    public static void main(String[] args) throws Exception {
        //BufferedImage image = ImageIO.read(new File("C:\\Users\\Administrator\\Desktop\\pc-2.jpg"));
        //String fastdfsNginxUrl="http://172.168.250.6:8888/group1/M00/00/22/rKj6BWDw8TOAC-rhABs7QS-2AxA937.jpg";
        /* 3d552b45a49a4b3e814bd6fd2afae283*/
        //D:/Project/zf_dzcpt_ms_soft2.0/commonservice/case_service_provider\modelTemples\model\pic\clshjg\3d552b45a49a4b3e814bd6fd2afae283_ubp7k6new.jpg
       /* String fastdfsNginxUrl=" http://172.168.250.6:8888/group1/M00/00/22/rKj6BWDvktmAR-gdAAiDiK3MVk0667.jpg";
        String materialOid="ea7f37fc8a7b4dfaac3db013f51c72c1";
        String fileUrl= FaStaticParam.PROJECT_PATH+"\\modelTemples\\model";
        String dirPath = File.separator + "pic" + File.separator + "clshjg" + File.separator;
        String picNameQZ = materialOid + "_"  + StringUtil.random(6) ;
        String picName=picNameQZ+"new"+ ".jpg";
        String picNameNew=picNameQZ+ ".jpg";

        String  picPath = fileUrl + dirPath + picName;
        String  picPathNew = fileUrl + dirPath + picNameNew;
        File file = new File(picPath);
        if(!file.exists()){
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //复制文件系统中的附件到本地系统
            HttpUtil.downloadFile(fastdfsNginxUrl, cn.hutool.core.io.FileUtil.file(picPath));
        }*/
        // String picPath="D:\\Project\\zf_dzcpt_ms_soft2.0\\commonservice\\case_service_provider\\modelTemples\\model\\pic\\clshjg\\d79c6b4af1734f6a9df3db71caeecc32new.jpg";
        //String picPathNew="D:\\Project\\zf_dzcpt_ms_soft2.0\\commonservice\\case_service_provider\\modelTemples\\model\\pic\\clshjg\\"+StringUtil.random(6)+ ".jpg" ;

        String picPath = "D:\\ceshi\\ceshi111111.jpg";
        String picPathNew = "D:\\ceshi\\d79c6b4af1734f6a9df3db71caeecc32new.jpg";
        BufferedImage image = ImageIO.read(new File(picPath));
        int imageHeight = image.getHeight();
        int imageWedth = image.getWidth();
        //imageHeight=2338*imageWedth=1653
        log.info("imageHeight=" + imageHeight + "*imageWedth=" + imageWedth);

        List<Map<String, Object>> notPassList = new ArrayList<Map<String, Object>>();
        JSONObject notPassresultObj = new JSONObject();
        notPassresultObj.put("resultStatus", "1");
        notPassresultObj.put("name", "法定代表人");
        notPassresultObj.put("valid", false);
        notPassresultObj.put("memo", "必须为空");
        notPassresultObj.put("top", 1197);
        notPassresultObj.put("left", 509);
        notPassresultObj.put("width", 67);
        notPassresultObj.put("height", 44);
        notPassresultObj.put("auditPointStatus", "1");
        notPassresultObj.put("reviewPointsName", "法定代表人不能为空");
        notPassresultObj.put("serialNumber", "2");
        notPassList.add(notPassresultObj);

        JSONObject notPassresultObj2 = new JSONObject();
        notPassresultObj2.put("resultStatus", "1");
        notPassresultObj2.put("name", "盖章");
        notPassresultObj2.put("valid", false);
        notPassresultObj2.put("memo", "必须盖章");
        notPassresultObj2.put("top", 1807);
        notPassresultObj2.put("left", 1024);
        notPassresultObj2.put("width", 351);
        notPassresultObj2.put("height", 311);
        notPassresultObj2.put("auditPointStatus", "1");
        notPassresultObj2.put("reviewPointsName", "必须盖章");
        notPassresultObj2.put("serialNumber", "5");
        notPassList.add(notPassresultObj2);


        List<Map<String, Object>> passList = new ArrayList<Map<String, Object>>();

        JSONObject passresultObj001 = new JSONObject();
        passresultObj001.put("resultStatus", "0");
        passresultObj001.put("name", "法定代表人");
        passresultObj001.put("valid", true);
        passresultObj001.put("memo", "必须为空");
        passresultObj001.put("top", 1325);
        passresultObj001.put("left", 505);
        passresultObj001.put("width", 310);
        passresultObj001.put("height", 50);
        passresultObj001.put("auditPointStatus", "2");
        passresultObj001.put("reviewPointsName", "日期比较");
        passresultObj001.put("serialNumber", "4");
        passresultObj001.put("mem0", "法定代表人不能为空");


        JSONObject passresultObj0012 = new JSONObject();
        passresultObj0012.put("resultStatus", "0");
        passresultObj0012.put("name", "统一社会信用代码");
        passresultObj0012.put("valid", true);
        passresultObj0012.put("memo", "必须为空");
        passresultObj0012.put("top", 902);
        passresultObj0012.put("left", 1158);
        passresultObj0012.put("width", 289);
        passresultObj0012.put("height", 41);
        passresultObj0012.put("auditPointStatus", "2");
        passresultObj0012.put("reviewPointsName", "统一社会信用代码不能为空");
        passresultObj0012.put("serialNumber", "1");


        JSONObject passresultObj003 = new JSONObject();
        passresultObj003.put("resultStatus", "0");
        passresultObj003.put("name", "名称");
        passresultObj003.put("valid", true);
        passresultObj003.put("memo", "不能为空");
      /*  passresultObj003.put("top",1006);
        passresultObj003.put("left", 529);
        passresultObj003.put("width",520);
        passresultObj003.put("height",49);*/

        passresultObj003.put("top", 902);
        passresultObj003.put("left", 1158);
        passresultObj003.put("width", 289);
        passresultObj003.put("height", 41);
        passresultObj003.put("auditPointStatus", "2");
        passresultObj003.put("reviewPointsName", "名称不能为空");
        passresultObj003.put("serialNumber", "5");

        /* passList.add(passresultObj001);*/
        passList.add(passresultObj003);
        passList.add(passresultObj0012);

        Graphics g;
        for (Map<String, Object> objectMap : notPassList) {
            String serialNumber = (String) objectMap.get("serialNumber");
            int x = (int) objectMap.get("left");
            int y = (int) objectMap.get("top");
            int width = (int) objectMap.get("width");
            int height = (int) objectMap.get("height");
            String reviewPointsName = (String) objectMap.get("reviewPointsName");
            g = image.getGraphics();
            g.setColor(Color.RED);//画笔颜色
            // 设置线的粗细
            BasicStroke stokeLine = new BasicStroke(10.0f);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(stokeLine);
            // 坐标+尺寸
            g.drawRect(x, y, width, height);//矩形框(原点x坐标，原点y坐标，矩形的长，矩形的宽)
            g.setFont(new Font("宋体", Font.BOLD, 50));//设置字体样式(3个参数分别表示：字体名称，字体样式，字体大小)
            g.drawString(serialNumber + reviewPointsName, x, y);
        }

        String xyAll = "";


        for (Map<String, Object> objectMap : passList) {
            String serialNumber = (String) objectMap.get("serialNumber");
            int x = (int) objectMap.get("left");
            int y = (int) objectMap.get("top");
            int width = (int) objectMap.get("width");
            int height = (int) objectMap.get("height");
            String reviewPointsName = (String) objectMap.get("reviewPointsName");
            g = image.getGraphics();
            g.setColor(Color.GREEN);//画笔颜色
            // 设置线的粗细
            BasicStroke stokeLine = new BasicStroke(10.0f);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(stokeLine);
            String num = FaStaticParam.SERNUM_MAP.get(serialNumber);
            // 坐标+尺寸
            g.drawRect(x, y, width, height);//矩形框(原点x坐标，原点y坐标，矩形的长，矩形的宽)
            g.setFont(new Font("宋体", Font.BOLD, 60));//设置字体样式(3个参数分别表示：字体名称，字体样式，字体大小)

            String xy = "x=" + x + "y=" + (y - 10) + ";";
            if (xyAll.contains(xy)) {
                xyAll = xyAll + "x=" + (x - 60) + "y=" + (y - 10) + ";";
                g.drawString(num, x - 60, y - 10);
            } else {
                g.drawString(num, x, y - 10);
                xyAll = xyAll + xy;
            }


        }
        log.info("picPathNew" + picPathNew);
        //g.dispose();
        FileOutputStream out = new FileOutputStream(picPathNew);//输出图片的地址
        ImageIO.write(image, "jpeg", out);


    }


    /**
     * 材料预审
     *
     * @param caseOid 办件表主键
     * @param cataOid 子项目录主键
     * @return
     * @author liangss
     * @date 2020-12-16 13:45:00
     */
    @GetMapping(value = "/intelligentPretrialmaterialPrePrial")
    public ApiResultSet intelligentPretrialmaterialPrePrial(HttpServletRequest request, String caseOid, String caseMaterialOid, String caseFileRecOid,
                                                            String attaOid, String cataOid, String materialOid) {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            //判断附件材料关系表id，目录管理id，办件id是否存在
            /*if(StringUtils.isEmpty(cataOid)){
                modelMap.put("success", false);
                modelMap.put("message","请选择目录！");
            }*/
            if (StringUtils.isNotEmpty(cataOid) && StringUtils.isNotEmpty(caseOid) && StringUtils.isNotEmpty(caseFileRecOid)) {
                FaMaterialPicOcrResult faMaterialPicOcrResult = new FaMaterialPicOcrResult();
                faMaterialPicOcrResult.setUuid(caseOid);
                //faMaterialPicOcrResult.setMaterialAttaOid(caseFileRecOid);
                DbFaMaterialPicOcrResult dbFaMaterialPicOcrResult = faMaterialPicOcrResultManager.
                        getFaMaterialPicOcrResultByFaMaterialPicOcrResult(faMaterialPicOcrResult);
                if (null != dbFaMaterialPicOcrResult) {
                    faMaterialPicOcrResultManager.del(String.valueOf(dbFaMaterialPicOcrResult.getId()));
                }
                //最新添加调用百度接口识别
                modelMap = intelligentPreTrialManager.materialPrePrialNew(request, caseOid, caseMaterialOid, caseFileRecOid, attaOid, cataOid, materialOid);
                //String message= intelligentPreTrialManager.materialPrePrial(request, caseOid,caseMaterialOid, caseFileRecOid, attaOid,cataOid,materialOid);
               /* if(StrUtil.isNotEmpty(message)){
                    modelMap.put("success", false);
                    modelMap.put("message",message);
                }else{
                    modelMap.put("success", true);
                }*/
            } else {
                modelMap.put("success", false);
                modelMap.put("message", "请检查是否选择智审目录(注:颗粒化材料不支持智审!)");
            }

        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("message", e.getMessage());
        }
        return new ApiResultSet(modelMap);
    }


    @GetMapping(value = "/materialPrePrialNew")
    public ApiResultSet materialPrePrialNew(HttpServletRequest request, String caseOid, String caseMaterialOid, String caseFileRecOid,
                                            String attaOid, String cataOid, String materialOid) {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            modelMap = intelligentPreTrialManager.materialPrePrialNew(request, caseOid, caseMaterialOid, caseFileRecOid, attaOid, cataOid, materialOid);


        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("message", e.getMessage());
        }
        return new ApiResultSet(modelMap);
    }


    @PostMapping(value = "/getClassifilerByYjy")
    public ApiResultSet checkSealCountBYYJY(String attaOid) {
        Map<String, Object> modelMap = new HashMap<>();
        ApiResultSet apiResultSet = new ApiResultSet();
        try {
            List<Map<String, Object>> map = intelligentPreTrialManager.checkSealCountByYjy(attaOid);
            List<Map<String, Object>> map2 = intelligentPreTrialManager.checkSignCountByYjy(attaOid);
            modelMap.put("map1", map);
            modelMap.put("map2", map2);
            apiResultSet.setData(modelMap);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("message", "材料分类失败");
            apiResultSet.setData(modelMap);
        }
        return apiResultSet;
    }


    /**
     * 智能预审
     *
     * @param request
     * @param caseOid
     * @return
     */
    @GetMapping(value = "/intelligentPretrial")
    public ApiResultSet intelligentPretrial(HttpServletRequest request, String caseOid) {
        /*  Map<String, Object> modelMap = new HashMap<>();*/
        Map<String, Object> modelMap = new HashMap<>();
        try {
            //办件信息
            ApiResultSet<QlCase> resultSet = qlCaseServiceFeginService.queryQlCaseByCaseOid(caseOid);
            QlCase qlCase = null;
            if (resultSet.getData() != null) {
                qlCase = resultSet.getData();
            }
            modelMap.put("qlCase", qlCase);


            ApiResultSet<QlCaseApplay> qlCaseApplayApiResultSet = qlCaseApplayServiceFeginService.queryQlCaseApplayByCaseOid(caseOid);
            QlCaseApplay qlCaseApplay = null;
            if (qlCaseApplayApiResultSet.getData() != null) {
                qlCaseApplay = qlCaseApplayApiResultSet.getData();
            }
            modelMap.put("qlCaseApplay", qlCaseApplay);
            // List<PreTrialResultVo> preTrialResultVoList= intelligentPreTrialManager.getPreTrialResultVo(request, caseOid);
            //List<PreTrialResultVo> preTrialResultVoList= intelligentPreTrialManager.getPreTrialResultVoNew(request, caseOid);
            List<QlCaseMaterial> qlCaseMaterials = intelligentPreTrialManager.getPreTrialResultVoNew(request, caseOid, qlCase, qlCaseApplay);

            modelMap.put("qlCaseMaterials", qlCaseMaterials);
            log.info("获取preTrialResultVoList成功：{}", JSON.toJSONString(qlCaseMaterials));
            //判断最终智能审核结果
          /*  String validateResult = intelligentPreTrialManager.getValidateResult(preTrialResultVoList);
            modelMap.put("validateResult", validateResult);*/

            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("message", e.getMessage());
            log.info(e.getMessage());
        }
        return new ApiResultSet(modelMap);
    }


    /**
     * 查询预审
     *
     * @param request
     * @param caseOid
     * @param materialAttaOid
     * @param materialCatalogOid
     * @return
     */
    @GetMapping(value = "/viewResult")
    public ApiResultSet viewResult(HttpServletRequest request, String caseOid, String caseMaterialOid, String materialAttaOid, String materialCatalogOid) {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            if (StringUtils.isNotEmpty(caseOid) && StringUtils.isNotEmpty(caseMaterialOid)) {
                if (StringUtils.isNotEmpty(materialCatalogOid) && StringUtils.isNotEmpty(materialAttaOid)) {
                    modelMap = intelligentPreTrialManager.viewResultNew(request, caseOid, caseMaterialOid, materialAttaOid, materialCatalogOid);
                } else {
                    modelMap = intelligentPreTrialManager.viewResultJTYM(request, caseOid, caseMaterialOid, materialAttaOid, materialCatalogOid);
                }

            } else {
                modelMap.put("success", false);
                modelMap.put("message", "没有审核信息");
            }

        } catch (Exception e) {
            /*   TerminalLoggerUtil.error("材料预审", e);*/
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("message", e.getMessage());
        }
        return new ApiResultSet(modelMap);
    }


    /**
     * 判断跳转哪个页面
     *
     * @param request
     * @param caseOid
     * @param caseMaterialOid
     * @param materialAttaOid
     * @param materialCatalogOid
     * @return
     */
    @GetMapping(value = "/viewResultJTYM")
    public ApiResultSet viewResultJTYM(HttpServletRequest request, String caseOid, String caseMaterialOid, String materialAttaOid, String materialCatalogOid) {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            if (StringUtils.isNotEmpty(caseOid) && StringUtils.isNotEmpty(caseMaterialOid) && StringUtils.isNotEmpty(materialAttaOid) && StringUtils.isNotEmpty(materialCatalogOid)) {
                modelMap = intelligentPreTrialManager.viewResultJTYM(request, caseOid, caseMaterialOid, materialAttaOid, materialCatalogOid);
            } else {
                modelMap.put("success", false);
                modelMap.put("message", "没有信息");
            }

        } catch (Exception e) {
            /*   TerminalLoggerUtil.error("材料预审", e);*/
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("message", e.getMessage());
        }
        return new ApiResultSet(modelMap);
    }


    @GetMapping(value = "/getAttaBase64ByUrl")
    public ApiResultSet getAttaBase64ByUrl() {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            AttaBase64Result attaBase64Result = getAttaBase64ByUrl("http://192.168.0.134:8899/group1/M00/00/00/wKgAhmDga6yAQ6bnAAdWTTDxc4A015.jpg");
            String picBase64 = attaBase64Result.getResult();
            modelMap.put("picBase64", picBase64);
            log.info("picBase64=" + picBase64);

        } catch (Exception e) {
            /*   TerminalLoggerUtil.error("材料预审", e);*/
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("message", e.getMessage());
        }
        return new ApiResultSet(modelMap);
    }

    public AttaBase64Result getAttaBase64ByUrl(String url) throws Exception {
        log.info(url);
        AttaBase64Result base64Result = new AttaBase64Result();
        Map<String, Object> param = new HashMap<>(6);
        String base64 = FileManageUtil.getImgFromUrl(url);
        if (null != base64) {
            String result = base64;
            result = result.replace("\n", "").replace("\t", "").replace("\r", "");
            result = URLDecoder.decode(result, "UTF-8");
            String jsonResult = result;
            base64Result.setResult(result);
            String resultNew = base64Result.getResult();
            String strOne = " ";
            String strTwo = "+";
            resultNew = resultNew.replaceAll(strOne, strTwo);
            base64Result.setResult(resultNew);
        }
        return base64Result;
    }

    @PostMapping(value = "/intelligentPretrialmaterialPrePrialAllOld")
    public ApiResultSet intelligentPretrialmaterialPrePrialAllOld(@RequestBody PreMaterialAttaVo preMaterialAttaVo) {
        log.info("进入intelligentPretrialmaterialPrePrialAllold预审方法");

        HttpServletRequest request = null;
        Map<String, Object> modelMap = new HashMap<>();
        Map<String, Object> modelMapSH = new HashMap<>();
        String serviceOid = "";
        String caseOid = preMaterialAttaVo.getCaseOid();
        List<QlCaseMaterialAtta> qlCaseMaterialAttaList = preMaterialAttaVo.getQlCaseMaterialAttaList();
        try {
            if (StringUtils.isNotEmpty(caseOid)) {
                //删除之前的智审信息
                faMaterialPicOcrResultManager.updateDbFaMaterialPicOcrResultByCaselOid(caseOid);
                log.info("办件idcaseOid=" + caseOid);
                //办件信息
                ApiResultSet<QlCase> resultSet = qlCaseServiceFeginService.queryQlCaseByCaseOid(caseOid);
                QlCase qlCase = null;
                if (resultSet.getData() != null) {
                    qlCase = resultSet.getData();
                }
                log.info("事项oid" + serviceOid);
                serviceOid = qlCase.getServiceOid();
                log.info("qlCaseMaterialAttaList" + qlCaseMaterialAttaList.size() + "附件");
                if (null != qlCaseMaterialAttaList) {
                    for (QlCaseMaterialAtta qlCaseMaterialAtta : qlCaseMaterialAttaList) {
                        String materialAttaOid = qlCaseMaterialAtta.getMaterialAttaOid();
                        String materialCatalogOid = qlCaseMaterialAtta.getMaterialCatalogOid();
                        String refinedMaterialOid = qlCaseMaterialAtta.getRefinedMaterialOid();
                        String attaOid = qlCaseMaterialAtta.getAttaOid();
                        String caseMaterialOid = qlCaseMaterialAtta.getCaseMaterialOid();
                        ApiResultSet<QlCaseMaterial> qlCaseMaterialApiResultSet = qlCaseMaterialServiceFeginService.queryMaterialByCaseMaterialOid(caseMaterialOid);
                        QlCaseMaterial qlCaseMaterial = new QlCaseMaterial();
                        if (null != qlCaseMaterialApiResultSet.getData()) {
                            qlCaseMaterial = qlCaseMaterialApiResultSet.getData();
                        }

                        modelMap.put(qlCaseMaterialAtta.getMaterialAttaOid() + "-" + qlCaseMaterial.getMaterialName(), "不参与智审");

                    }

                }
                //保存审核结果信息
                log.info("保存审核结果信息AuditResult开始");
                List<QlCaseMaterial> qlCaseMaterials = intelligentPreTrialManager.getQlCaseMaterialListAndAuditResult(request, caseOid, serviceOid);
                log.info("保存审核结果信息AuditResult结束");
            }


            modelMap.put("success", true);

        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("message", e.getMessage());
        }
        return new ApiResultSet(modelMap);
    }

}
