package com.zfsoft.single.controller.yxpz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CaseFormat;
import com.zfsoft.cases.data.QlCase;
import com.zfsoft.cases.data.QlCaseApplay;
import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.service.QlCaseApplayService;
import com.zfsoft.cases.service.QlCaseExtService;
import com.zfsoft.cases.service.QlCaseMaterialService;
import com.zfsoft.cases.service.QlCaseService;
import com.zfsoft.cases.util.StringUtils;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.service.sxService.data.SxFillField;
import com.zfsoft.service.sxService.service.SxFillFieldService;
import com.zfsoft.single.data.clzs.FaMaterialPicOcrResult;
import com.zfsoft.single.data.yxpz.FormFieldRelConfig;
import com.zfsoft.single.data.yxpz.vo.CascaderTreeVo;
import com.zfsoft.single.data.yxpz.vo.CopyVo;
import com.zfsoft.single.dbaccess.data.DbFormFieldRelConfig;
import com.zfsoft.single.manager.clzs.FaMaterialPicOcrResultManager;
import com.zfsoft.single.manager.yxpz.FormFieldFillManager;
import com.zfsoft.single.manager.yxpz.FormFieldRelConfigManager;
import com.zfsoft.single.service.yxpz.FormFieldRelConfigService;
import com.zfsoft.superwindow.data.clzs.BaseFormField;
import com.zfsoft.superwindow.data.clzs.ElectronicLicenseElement;
import com.zfsoft.superwindow.dbaccess.data.DbFaMaterialPicOcrResult;
import com.zfsoft.superwindow.service.clzs.BasicFormFieldService;
import com.zfsoft.superwindow.service.clzs.ElectronicLicenseService;
import com.zfsoft.superwindow.service.interapi.data.ApiReqParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.ListUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Future;


/**
 * dongxl
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormFieldRelConfigController implements FormFieldRelConfigService {

    private final FormFieldRelConfigManager formFieldRelConfigManager;

    @Resource
    private QlCaseService qlCaseServiceFeginService;

    @Resource
    private QlCaseApplayService qlCaseApplayServiceFeginService;

    @Resource
    private QlCaseExtService qlCaseExtServiceFeginService;

    @Resource
    private BasicFormFieldService basicFormFieldFeginService;

    @Resource
    private ElectronicLicenseService electronicLicenseFeginService;

    @Resource
    private QlCaseMaterialService qlCaseMaterialServiceFeginService;

    @Resource
    private FaMaterialPicOcrResultManager faMaterialPicOcrResultManager;

    @Resource
    private SxFillFieldService sxFillFieldFeginService;

    @Autowired
    private FormFieldFillManager formFieldFillManager;

    @Value("${zfsoft.fieldFill.timeout:10000}")
    private long fieldFillTimeout;

    @Override
    public ApiResultSet queryPageList(FormFieldRelConfig formFieldRelConfig, Integer pageNum, Integer pageSize) {
        PageResult<FormFieldRelConfig> page=formFieldRelConfigManager.queryPageList(formFieldRelConfig,pageNum,pageSize);
        return new ApiResultSet(page);
    }

    @Override
    public ApiResultSet<String> saveOrUpdate(FormFieldRelConfig formFieldRelConfig) {
        String str= formFieldRelConfigManager.saveOrUpdate(formFieldRelConfig);
        return new ApiResultSet<>(str);
    }

    @Override
    public ApiResultSet<FormFieldRelConfig> getOneRelConfig(Long id) {
        FormFieldRelConfig field=formFieldRelConfigManager.getOneRelConfig(id);
        return new ApiResultSet<>(field);
    }

    @Override
    public ApiResultSet delRelConfig(Long id) {
        formFieldRelConfigManager.delRelConfig(id);
        return new ApiResultSet();
    }

    @Override
    public ApiResultSet<List<CascaderTreeVo>> queryElecAndElementTree(String serviceOid) {
        List<CascaderTreeVo> list= formFieldRelConfigManager.queryElecAndElementTree(serviceOid);
        return new ApiResultSet<>(list);
    }

    @Override
    public ApiResultSet<List<CascaderTreeVo>> queryCatalogAndCataLogElementTree(String serviceOid) {
        List<CascaderTreeVo> list= formFieldRelConfigManager.queryCatalogAndCataLogElementTree(serviceOid);
        return new ApiResultSet<>(list);
    }

    @Override
    public ApiResultSet checkHasRepeat(String serviceOid, Integer fillType, String fillFieldOid, String oid) {
        String result = formFieldRelConfigManager.checkHasRepeat(serviceOid, fillType, fillFieldOid, oid);
        ApiResultSet apiResultSet = new ApiResultSet();
        apiResultSet.setData(result);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Map<String, Object>> getAllBasicFieldByOid(String caseOid) {
        Map<String,Object> map =new HashMap<String,Object>();
        ApiResultSet<QlCase> qlCaseApiResultSet = qlCaseServiceFeginService.queryQlCaseByCaseOid(caseOid);
        QlCase qlCase = qlCaseApiResultSet.getData();
        log.info("qlCase: {}", qlCase.toString());
        //填充基础表单
        JSONObject basicFieldResult = getBaseInfo(qlCase.getServiceOid(), caseOid);
        map.put("basicFieldResult", basicFieldResult);
        //填充电子表单信息
        JSONObject formFieldResult = getFormInfo(qlCase);
        map.put("formFieldResult", formFieldResult);
        ApiResultSet<Map<String,Object>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(map);
        return apiResultSet;
    }

    /**
     * 电子表单字段填充
     * @param  qlCase
     * @return
     */
    private JSONObject getFormInfo(QlCase qlCase) {
        log.info("serviceOid: {}, caseOid: {}", qlCase.getServiceOid(), qlCase.getCaseOid());
        JSONObject formFieldResult = new JSONObject();
        //获取配置的基础字段
        List<DbFormFieldRelConfig> dbFormFieldRelConfigList = formFieldRelConfigManager.getFormFieldRelConfigByType(qlCase.getServiceOid(), 1);
        Map<String, List<SxFillField>> groupByApiIdFiledMap = new HashMap<>();
        if (dbFormFieldRelConfigList !=null && dbFormFieldRelConfigList.size()>0) {
            for (DbFormFieldRelConfig dbFormFieldRelConfig: dbFormFieldRelConfigList) {
                ApiResultSet<SxFillField> dbSxFillField= sxFillFieldFeginService.getDbSxFillFieldByOid(dbFormFieldRelConfig.getFillFieldOid());
                SxFillField sxFillField = dbSxFillField.getData();
                if (sxFillField !=null) {   // 判断字段是否被删除
                    String fieldKey = sxFillField.getFieldCode();  //被填充字段
                    String fieldName = sxFillField.getFieldName();
                    Integer dataType = sxFillField.getDataType();
                    log.info("电子表单 fieldKey: {}, fieldName: {}, columnType: {}", fieldKey, fieldName, dataType);
                    String interApiId = dbFormFieldRelConfig.getInterApiId();
                    sxFillField.setInterApiValId(dbFormFieldRelConfig.getInterApiValId());
                    if (StringUtils.isNotEmpty(interApiId)) {  //电子证照信息提取
                        if (groupByApiIdFiledMap.containsKey(interApiId)) {
                            groupByApiIdFiledMap.get(interApiId).add(sxFillField);
                        } else {
                            List<SxFillField> sxList = new ArrayList<>();
                            sxList.add(sxFillField);
                            groupByApiIdFiledMap.put(interApiId, sxList);
                        }
                    }
                }
            }
            // 处理表单对应字段接口
            dealInterApi(formFieldResult, groupByApiIdFiledMap, qlCase);
        }
        return formFieldResult;
    }

    private void dealInterApi(JSONObject formFieldResult, Map<String, List<SxFillField>> groupByApiIdFiledMap,
                              QlCase qlCase) {

        // 封装请求参数
        ApiReqParams apiReqParams = new ApiReqParams();
        QlCaseApplay applay = qlCase.getApplay();
        apiReqParams.setSxServiceOid(qlCase.getServiceOid());
        apiReqParams.setUniqueCode(applay.getCredentialNumber());
        String interId = null;
        List<SxFillField> fieldList = null;
        List<Future<String>> resultList = new ArrayList<>(groupByApiIdFiledMap.size());
        Future<String> resultFuture = null;
        long xmlSt = System.currentTimeMillis();
        for (Map.Entry<String, List<SxFillField>> entry : groupByApiIdFiledMap.entrySet()) {
            interId = entry.getKey();
            fieldList = entry.getValue();
            apiReqParams.setInterId(interId);
            // 进行表单字段填充
            resultFuture = formFieldFillManager.fillFormField(formFieldResult, apiReqParams, interId, fieldList);
            resultList.add(resultFuture);
        }

        // 当执行成功移除map中数据，全部移除说明执行完成
        Future<String> tempFuture = null;
        Iterator<Future<String>> iterator = null;
        try {
            while (true) {
                // 避免死循环，设置超时时间
                long xmlCur = System.currentTimeMillis();
                if (xmlCur - xmlSt > fieldFillTimeout) {
                    log.error("表单填充超时！");
                    throw new RuntimeException("表单填充超时！");
                }
                if (ListUtils.isEmpty(resultList)) {
                    long xmlEt = System.currentTimeMillis();
                    log.info(String.format("表单填充总耗时：【%s】ms", xmlEt - xmlSt));
                    break;
                }
                iterator = resultList.iterator();
                while (iterator.hasNext()) {
                    tempFuture = iterator.next();
                    if (tempFuture.isDone()) {
                        iterator.remove();
                    }
                }
            }
        } catch (Exception e) {
            // ignore
            log.error(String.format("表单填充异常，【%s】", e.getMessage()), e);
        }
    }

    /**
     *  处理字符串年月日
     * @param words
     * @return
     */
    private String handleWords(String words) {
        words = words.replace("年", "-").replace("月", "-").replace("日", "");
        String[] wordsArray = words.split("-");
        if (wordsArray !=null && wordsArray.length> 2) {
            String year = wordsArray[0];
            String month = wordsArray[1];
            String day = wordsArray[2];
            if (month.length() ==1) {
                month = "0" + month;
            }
            if (day.length() ==1) {
                day = "0" + day;
            }
            words = year + "-" + month + "-" + day;
        }
        return words;
    }

    /**
     *  基础表单字段填充
     * @param serviceOid
     * @param caseOid
     * @return
     */
    private JSONObject getBaseInfo(String serviceOid, String caseOid) {
        JSONObject basicFieldResult = new JSONObject();
        //获取配置的基础字段
        List<DbFormFieldRelConfig> dbFormFieldRelConfigList = formFieldRelConfigManager.getFormFieldRelConfigByType(serviceOid, 0);
        if (dbFormFieldRelConfigList !=null && dbFormFieldRelConfigList.size()>0) {
            for (DbFormFieldRelConfig dbFormFieldRelConfig: dbFormFieldRelConfigList) {
                ApiResultSet<BaseFormField> baseFormFieldApiResultSet = basicFormFieldFeginService.getOneByOid(dbFormFieldRelConfig.getBasicFormFieldOid());
                BaseFormField data = baseFormFieldApiResultSet.getData();
                if (data !=null) {  //判断字段是否被删除
                    String fieldKey = data.getFieldKey();  //被填充字段
                    String fieldName = data.getFieldName();
                    log.info("基础表单 fieldKey: {}, fieldName: {}", fieldKey, fieldName);
                    String ocrFieldOid = dbFormFieldRelConfig.getOcrFieldOid();
                    String elecLiecenseFieldOid = dbFormFieldRelConfig.getElecLiecenseFieldOid();
                    if (StringUtils.isNotEmpty(ocrFieldOid)) {  //填充ocr内容字段
                        String materialOid = ocrFieldOid.split(",")[0].split(";")[0]; //材料oid
                        String refineOid = ocrFieldOid.split(",")[1].split(";")[0]; //细化材料
                        String ocrFieldKey = ocrFieldOid.split(",")[2].split(";")[1];
                        ApiResultSet<QlCaseMaterial> qlCaseMaterialApiResultSet = qlCaseMaterialServiceFeginService.queryQlCaseMaterialByOid(caseOid, materialOid);
                        QlCaseMaterial qlCaseMaterial = qlCaseMaterialApiResultSet.getData();
                        if (qlCaseMaterial !=null) {
                            FaMaterialPicOcrResult faMaterialPicOcrResult = new FaMaterialPicOcrResult();
                            faMaterialPicOcrResult.setRefinedMaterialOid(refineOid);
                            faMaterialPicOcrResult.setCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
                            DbFaMaterialPicOcrResult dbFaMaterialPicOcrResult = faMaterialPicOcrResultManager.getFaMaterialPicOcrResultByFaMaterialPicOcrResult(faMaterialPicOcrResult);
                            if (dbFaMaterialPicOcrResult !=null) {
                                String resultJson = dbFaMaterialPicOcrResult.getResultJson();
                                log.info("ocr识别结果： {}", resultJson);
                                if (StringUtils.isNotEmpty(resultJson)) {
                                    JSONObject jsonObject = JSONObject.parseObject(resultJson);
                                    JSONArray resultArray = jsonObject.getJSONArray("result");
                                    if (resultArray !=null && resultArray.size()>0) {
                                        for (Object object: resultArray) {
                                            JSONObject item = (JSONObject) object;
                                            if(item.getString("code").equals(ocrFieldKey)) {
                                                basicFieldResult.put(fieldKey, item.getString("words"));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (StringUtils.isNotEmpty(elecLiecenseFieldOid)) {  //电子证照信息提取
                        ApiResultSet<ElectronicLicenseElement> electronicLicenseElementApiResultSet = electronicLicenseFeginService.queryElectronicElementListByOid(elecLiecenseFieldOid);
                        ElectronicLicenseElement electronicLicenseElement = electronicLicenseElementApiResultSet.getData();
                        if (electronicLicenseElement !=null) {
                            String code = electronicLicenseElement.getElectronicLicenseElementCode();
                            log.info("电子证照electronicLicenseElement code: {}", code);
                            ApiResultSet apiResultSet = electronicLicenseFeginService.getInfoByOid(electronicLicenseElement.getElectronicLicenseInterfaceOid());
                            LinkedHashMap linkedHashMap = (LinkedHashMap)apiResultSet.getData();
                            String billOid = linkedHashMap.get("electronicLicenseOid").toString();
                            //billOid
                            ApiResultSet<List<QlCaseMaterial>> listApiResultSet = qlCaseMaterialServiceFeginService.queryQlCaseMaterialByBillOid(caseOid, billOid);
                            List<QlCaseMaterial> qlCaseMaterials = listApiResultSet.getData();
                            if (qlCaseMaterials !=null && qlCaseMaterials.size()>0) {
                                QlCaseMaterial qlCaseMaterial = qlCaseMaterials.get(0);
                                String electronicResult = qlCaseMaterial.getElectronicResult();
                                log.info("电子证照结果信息： {}", electronicResult);
                                if (StringUtils.isNotEmpty(electronicResult)) {
                                    JSONObject jsonObject = JSONObject.parseObject(electronicResult);
                                    JSONObject resultData = jsonObject.getJSONObject("resultData");
                                    String codeResult = resultData.getString(code);
                                    basicFieldResult.put(fieldKey, codeResult);
                                }
                            }
                        }
                    }
                }
            }
        }
        return basicFieldResult;
    }

    @Override
    public ApiResultSet<JSONArray> getBasicAndFormFieldRel(String serviceOid) {
        JSONArray jsonArray = new JSONArray();
        ApiResultSet<JSONArray> apiResultSet = new ApiResultSet<>();
        List<DbFormFieldRelConfig> dbFormFieldRelConfigList = formFieldRelConfigManager.getFormFieldRelConfigByType(serviceOid, 1);
        if (dbFormFieldRelConfigList !=null && dbFormFieldRelConfigList.size()>0) {
            for (DbFormFieldRelConfig dbFormFieldRelConfig: dbFormFieldRelConfigList) {
                JSONObject jsonObject = new JSONObject();
                String basicFormFieldOid = dbFormFieldRelConfig.getBasicFormFieldOid(); //基础表单
                String fillFieldOid = dbFormFieldRelConfig.getFillFieldOid(); //电子表单
                if (StringUtils.isNotEmpty(basicFormFieldOid) && StringUtils.isNotEmpty(fillFieldOid)) {
                    ApiResultSet<BaseFormField> baseFormFieldApiResultSet = basicFormFieldFeginService.getOneByOid(basicFormFieldOid);
                    if (baseFormFieldApiResultSet !=null && baseFormFieldApiResultSet.getData() !=null) {
                        BaseFormField baseFormField = baseFormFieldApiResultSet.getData();
                        jsonObject.put("baseFormField", baseFormField.getFieldKey());
                    }

                    ApiResultSet<SxFillField> sxFillFieldApiResultSet=sxFillFieldFeginService.getDbSxFillFieldByOid(fillFieldOid);
                    if(sxFillFieldApiResultSet!=null && sxFillFieldApiResultSet.getData()!=null){
                        //需要转化驼峰字段
                        String formFieldCode =  CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, sxFillFieldApiResultSet.getData().getFieldCode());
                        jsonObject.put("formField", formFieldCode);
                    }
                    jsonArray.add(jsonObject);
                }
            }
        }
        apiResultSet.setData(jsonArray);
        return apiResultSet;
    }

    @Override
    public ApiResultSet checkIsHasRel(String thanTemplateMetadataOid) {
        ApiResultSet apiResultSet = new ApiResultSet(200);
        Map<String, Object> modelMap=new HashMap<>();
        List<DbFormFieldRelConfig> dbFormFieldRelConfigList = formFieldRelConfigManager.getFormFieldRelConfigByBasicFieldOid(thanTemplateMetadataOid);
        if (dbFormFieldRelConfigList !=null && dbFormFieldRelConfigList.size()>0) {
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
        }
        apiResultSet.setData(modelMap);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<String> copyFormFieldRelConfig(CopyVo vo){
        ApiResultSet apiResultSet = new ApiResultSet(200);
        formFieldRelConfigManager.copyFormFieldRelConfig(vo.getMap());
        return apiResultSet;
    }

}
