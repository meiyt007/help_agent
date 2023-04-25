package com.zfsoft.single.manager.clzs;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.rest.annotation.CataWordBind;
import com.zfsoft.service.sxService.data.SxServiceMaterial;
import com.zfsoft.service.sxService.service.SxServiceMaterialService;
import com.zfsoft.single.data.clzs.FaMaterialPicOcrResult;
import com.zfsoft.single.data.clzs.dto.BusinessLiceInfo;
import com.zfsoft.single.util.CommonUtil;
import com.zfsoft.single.util.FaStaticParam;
import com.zfsoft.single.util.JsonUtil;
import com.zfsoft.single.util.fa.atta.AttaBase64Result;
import com.zfsoft.single.util.fa.atta.FileManageUtil;
import com.zfsoft.superwindow.data.clzs.MaterialCatalog;
import com.zfsoft.superwindow.data.clzs.MaterialCatalogElement;
import com.zfsoft.superwindow.data.clzs.OcrRecord;
import com.zfsoft.superwindow.manager.clzs.OcrRecordManager;
import com.zfsoft.superwindow.service.clzs.MaterialCatalogElementService;
import com.zfsoft.superwindow.service.clzs.MaterialCatalogService;
import com.zfsoft.superwindow.util.fa.AiTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @ClassName OcrRecordManager
 * @Description: 营业执照识别
 * @Author liangss
 * @Date 2020-12-14 15:46:29
 **/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BusinessLiceManager {
    @Resource
    private MaterialCatalogService materialCatalogFeginService;
    @Resource
    private AiTokenUtil aiTokenUtil;

//    @Resource
//    private IOcrCertificateRestService ocrCertificateRestService;

    @Resource
    private SxServiceMaterialService sxServiceMaterialFeginService;

    @Resource
    private MaterialCatalogElementService materialCatalogElementFeginService;
    @Resource
    private  FaMaterialPicOcrResultManager faMaterialPicOcrResultManager;

    @Resource
    private OcrRecordManager ocrRecordManager;
    /**
     * 获取营业执照对象信息
     * @param picBase64 图片base64
     * @return
     * @throws Exception
     */
    public BusinessLiceInfo getBusinessLiceInfo(String picBase64){
        try {
            BusinessLiceInfo businessLiceInfo = null;
            picBase64 = picBase64.replace(" ", "+");

//            OcrBusinessLicenseRequest ocrBusinessLicenseRequest = aiTokenUtil.getTokenRequest(OcrBusinessLicenseRequest.class);
//            ocrBusinessLicenseRequest.setImgBase64(picBase64);
//            OcrBusinessLicenseResponse ocrBusinessLicenseResponse = ocrCertificateRestService.businessLicense(ocrBusinessLicenseRequest);
//
//            String successCode = String.valueOf(ocrBusinessLicenseResponse.getCode());
//            if (FaStaticParam.HTTP_REQUEST_CODE_SUCCESS.equals(successCode)) {
//                businessLiceInfo = new BusinessLiceInfo();
//                businessLiceInfo.setAddress(ocrBusinessLicenseResponse.getAddress());
//                businessLiceInfo.setCompanyName(ocrBusinessLicenseResponse.getCompanyName());
//                businessLiceInfo.setCompositionForm(ocrBusinessLicenseResponse.getCompositionForm());
//                businessLiceInfo.setEffectiveDate(ocrBusinessLicenseResponse.getEffectiveDate());
//                businessLiceInfo.setEstablishDate(ocrBusinessLicenseResponse.getEstablishDate());
//                businessLiceInfo.setIdNumber(ocrBusinessLicenseResponse.getIdNumber());
//                businessLiceInfo.setLawRepresentative(ocrBusinessLicenseResponse.getLawRepresentative());
//                businessLiceInfo.setRegisteredCapital(ocrBusinessLicenseResponse.getRegisteredCapital());
//                businessLiceInfo.setSocialCode(ocrBusinessLicenseResponse.getSocialCode());
//                businessLiceInfo.setType(ocrBusinessLicenseResponse.getType());
//            }
            return businessLiceInfo;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public Map<String,Object> discernBusiness(BusinessLiceInfo businessLiceInfo, String materialOid, MaterialCatalog materialCatalog, HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", false);
        SxServiceMaterial serviceMaterial=null;
        ApiResultSet<SxServiceMaterial> sxmResult=sxServiceMaterialFeginService.getSxServiceMaterialByOid(materialOid);
        if(sxmResult.getData()!=null){
            serviceMaterial=sxmResult.getData();
        }
        MaterialCatalog cata = new MaterialCatalog();
        if (serviceMaterial != null) {
            MaterialCatalog faModelMaterialCatalog=null;
            ApiResultSet<MaterialCatalog> catalog=this.materialCatalogFeginService.getMaterialCatalogOid(serviceMaterial.getMaterialCatalogOid());
            if(catalog!=null && catalog.getData()!=null){
                faModelMaterialCatalog=catalog.getData();
            }
            if (faModelMaterialCatalog != null) {
                ApiResultSet<List<MaterialCatalog>> list=this.materialCatalogFeginService.queryList(faModelMaterialCatalog.getMaterialCatalogOid());
                List<MaterialCatalog> cataList=null;
                if(list!=null&&list.getData()!=null){
                    cataList=list.getData();
                }
                if (cataList != null && cataList.size() > 0) {
                    cata = cataList.get(0);
                }
            }
        }
        String picBase64 = "";
        AttaBase64Result attaBase64Result = FileManageUtil.getAttaBase64(materialOid);
        if (FaStaticParam.HTTP_REQUEST_CODE_SUCCESS.equals(attaBase64Result.getCode())) {
            picBase64 = attaBase64Result.getResult();
        }else{
            map.put("data", "未获取附件base64信息!");
            return map;
        }
        businessLiceInfo = getBusinessLiceInfo(picBase64);
        Map<String, Object> businessLiceMap = this.discernBusinessBaiDu(businessLiceInfo, materialOid, cata);
        String code = (String) businessLiceMap.get(FaStaticParam.CODE);
        if (FaStaticParam.HTTP_REQUEST_CODE_SUCCESS.equals(code)) {
            String resultJson = new JSONObject(businessLiceMap).toString();
            saveResult(request, materialOid, cata.getCatalogCode(), materialOid, CommonUtil.md5(picBase64), resultJson,businessLiceInfo);
            map.put("success", true);
        }else{
            map.put("data", businessLiceMap.get(FaStaticParam.MESSAGE));
            return map;
        }
        return map;
    }


    /**
     * 调用百度接口识别营业执照
     * @param businessLiceInfo
     * @param materialOid
     * @param cata
     * @return
     * @throws Exception
     */
    public Map<String, Object> discernBusinessBaiDu(BusinessLiceInfo businessLiceInfo, String materialOid, MaterialCatalog cata){
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap.put("code", BaseStaticParameter.N);
            if (businessLiceInfo == null) {
                log.error("材料预审", "调用百度营业执照未获取到信息，验证失败!");
                resultMap.put("message", "调用百度营业执照未获取到信息，验证失败。");
                return resultMap;
            }
            MaterialCatalogElement materialCatalogElement=new MaterialCatalogElement();
            materialCatalogElement.setMaterialCatalogOid(cata.getMaterialCatalogOid());
            // 获取到目录元素信息
            ApiResultSet<List<MaterialCatalogElement>> list=materialCatalogElementFeginService.queryList(materialCatalogElement);
            List<MaterialCatalogElement> faMaterialCatalogMetadataList=null;
            if(list!=null&&list.getData()!=null){
                faMaterialCatalogMetadataList=list.getData();
            }
            resultMap.put("code", FaStaticParam.HTTP_REQUEST_CODE_SUCCESS);
            List<Map<String, String>> resultList = handBusinessLice(businessLiceInfo, faMaterialCatalogMetadataList);
            resultMap.put("result", resultList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }


    /**
     * 处理营业执照信息
     * @param businessLiceInfo
     * @param faMaterialCatalogMetadataList
     * @return
     * @throws Exception
     */
    private List<Map<String, String>> handBusinessLice(BusinessLiceInfo businessLiceInfo, List<MaterialCatalogElement> faMaterialCatalogMetadataList) {
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        try {

            // 得到类的属性列表
            Field[] fields = businessLiceInfo.getClass().getDeclaredFields();
            // key-目录元素 value-值
            Map<String, String> businessLiceInfoMap = new HashMap<>();
            for (Field field : fields) {
                // 这个是检查类中属性是否含有注解
                if (field.isAnnotationPresent(CataWordBind.class)) {
                    // 获取注解
                    CataWordBind annotation = field.getAnnotation(CataWordBind.class);
                    String word = annotation.word();
                    if (StrUtil.isBlank(word)) {
                        continue;
                    }
                    field.setAccessible(true); // 设置些属性是可以访问的
                    String val = field.get(businessLiceInfo) == null ? "" : field.get(businessLiceInfo).toString();
                    businessLiceInfoMap.put(word, val);
                }
            }
            for (MaterialCatalogElement faMaterialCatalogMetadata : faMaterialCatalogMetadataList) {
                String catalogCode = faMaterialCatalogMetadata.getElementCode();
                String catalogName = faMaterialCatalogMetadata.getElementName();
                Map<String, String> cataMap = new HashMap<String, String>();
                cataMap.put("code", catalogCode);
                cataMap.put("name", catalogName);
                cataMap.put("words",
                        businessLiceInfoMap.get(catalogName) == null ? "" : businessLiceInfoMap.get(catalogName));
                resultList.add(cataMap);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultList;
    }



    private void saveResult(HttpServletRequest request, String materialOid, String cataCode, String attaOid,
                            String base64MD5, String resultJson, BusinessLiceInfo businessLiceInfo)
            throws Exception {
        HttpSession session = request.getSession();
        FaMaterialPicOcrResult faMaterialPicOcrResult = new FaMaterialPicOcrResult();
        faMaterialPicOcrResult.setMaterialOid(materialOid);
        faMaterialPicOcrResult.setCataCode(cataCode);
        faMaterialPicOcrResult.setPicBase64Md5(base64MD5);
        faMaterialPicOcrResult.setResultJson(resultJson);
        // 将处理结果保存到数据库中
        faMaterialPicOcrResultManager.saveOrUpdate(faMaterialPicOcrResult);
        log.info("保存或修改材料目录图片识别结果信息", "保存成功！材料主键为：{}，目标编号为：{}，图片Base64的MD5值为：{}，识别结果json为：{}", materialOid, cataCode, base64MD5, resultJson);
        session.setAttribute(FaStaticParam.BUSINESS_LICE_FA_OCR_RESULT, faMaterialPicOcrResult);
        // OCRRecord记录保存
        OcrRecord ocrRecord = new OcrRecord();
        ocrRecord.setAttaOid(attaOid);
        ocrRecord.setBusinessLicenseStatus(BaseStaticParameter.YES);
        ocrRecord.setOcrResult(JsonUtil.objToJsonStr(businessLiceInfo));
        ocrRecordManager.saveOrUpdate(ocrRecord);
        session.setAttribute(FaStaticParam.BUSINESS_LICE_OCR_RECORD, ocrRecord);
    }


    /***
    * @Description: 获取营业执照信息
    * @Author:liangss
    * @Date:2021/10/25
    * @Param: [picBase64]
    */
    public Map<String,Map<String, String>> getBusinessLiceInfoMap(String picBase64){
        try {
            BusinessLiceInfo businessLiceInfo = null;
            picBase64 = picBase64.replace(" ", "+");
            Map<String,Map<String, String>> resultList = new HashMap<String,Map<String, String>>();
//            OcrBusinessLicenseRequest ocrBusinessLicenseRequest = aiTokenUtil.getTokenRequest(OcrBusinessLicenseRequest.class);
//            ocrBusinessLicenseRequest.setImgBase64(picBase64);
//            OcrBusinessLicenseResponse ocrBusinessLicenseResponse = ocrCertificateRestService.businessLicense(ocrBusinessLicenseRequest);
//            String successCode = String.valueOf(ocrBusinessLicenseResponse.getCode());
//            if (FaStaticParam.HTTP_REQUEST_CODE_SUCCESS.equals(successCode)) {
//                //单位名称
//                Map<String, String> companyNameMap=new HashMap<>();
//                companyNameMap.put("名称",ocrBusinessLicenseResponse.getCompanyName());
//                String  companyNameLocationJson= ocrBusinessLicenseResponse.getCompanyNameLocationJson();
//                if(StrUtil.isNotEmpty(companyNameLocationJson)){
//                    JSONObject jsonObject=JSONObject.parseObject(companyNameLocationJson);
//                    companyNameMap.put("top",jsonObject.getString("top"));
//                    companyNameMap.put("left",jsonObject.getString("left"));
//                    companyNameMap.put("height",jsonObject.getString("height"));
//                    companyNameMap.put("width",jsonObject.getString("width"));
//                    companyNameMap.put("words",ocrBusinessLicenseResponse.getCompanyName());
//                    resultList.put("名称",companyNameMap);
//                }
//                //统一社会信用代码
//                Map<String, String> socialCodeNameMap=new HashMap<>();
//                socialCodeNameMap.put("统一社会信用代码",ocrBusinessLicenseResponse.getSocialCode());
//                String  socialCodeLocationJson= ocrBusinessLicenseResponse.getSocialCodeLocationJson();
//                if(StrUtil.isNotEmpty(socialCodeLocationJson)){
//                    JSONObject jsonObject=JSONObject.parseObject(socialCodeLocationJson);
//                    socialCodeNameMap.put("top",jsonObject.getString("top"));
//                    socialCodeNameMap.put("left",jsonObject.getString("left"));
//                    socialCodeNameMap.put("height",jsonObject.getString("height"));
//                    socialCodeNameMap.put("width",jsonObject.getString("width"));
//                    socialCodeNameMap.put("words",ocrBusinessLicenseResponse.getSocialCode());
//                    resultList.put("统一社会信用代码",socialCodeNameMap);
//                }
//                //地址
//                Map<String, String> addressMap=new HashMap<>();
//                addressMap.put("地址",ocrBusinessLicenseResponse.getAddress());
//                String  addressLocationJson= ocrBusinessLicenseResponse.getAddressLocationJson();
//                if(StrUtil.isNotEmpty(addressLocationJson)){
//                    JSONObject jsonObject=JSONObject.parseObject(addressLocationJson);
//                    addressMap.put("top",jsonObject.getString("top"));
//                    addressMap.put("left",jsonObject.getString("left"));
//                    addressMap.put("height",jsonObject.getString("height"));
//                    addressMap.put("width",jsonObject.getString("width"));
//                    addressMap.put("words",ocrBusinessLicenseResponse.getAddress());
//                    resultList.put("地址",addressMap);
//                }
//
//                //成立日期
//                Map<String, String> establishDateMap=new HashMap<>();
//                establishDateMap.put("成立日期",ocrBusinessLicenseResponse.getEstablishDate());
//                String  establishDateLocationJson= ocrBusinessLicenseResponse.getEstablishDateLocationJson();
//                if(StrUtil.isNotEmpty(establishDateLocationJson)){
//                    JSONObject jsonObject=JSONObject.parseObject(establishDateLocationJson);
//                    establishDateMap.put("top",jsonObject.getString("top"));
//                    establishDateMap.put("left",jsonObject.getString("left"));
//                    establishDateMap.put("height",jsonObject.getString("height"));
//                    establishDateMap.put("width",jsonObject.getString("width"));
//                    establishDateMap.put("words",ocrBusinessLicenseResponse.getEstablishDate());
//                    resultList.put("成立日期",establishDateMap);
//                }
//                //有效期
//                Map<String, String> effectiveDateMap=new HashMap<>();
//                effectiveDateMap.put("有效期",ocrBusinessLicenseResponse.getEffectiveDate());
//                String  effectiveDateLocationJson= ocrBusinessLicenseResponse.getEffectiveDateLocationJson();
//                if(StrUtil.isNotEmpty(effectiveDateLocationJson)){
//                    JSONObject jsonObject=JSONObject.parseObject(effectiveDateLocationJson);
//                    effectiveDateMap.put("top",jsonObject.getString("top"));
//                    effectiveDateMap.put("left",jsonObject.getString("left"));
//                    effectiveDateMap.put("height",jsonObject.getString("height"));
//                    effectiveDateMap.put("width",jsonObject.getString("width"));
//                    effectiveDateMap.put("words",ocrBusinessLicenseResponse.getEffectiveDate());
//                    resultList.put("有效期",effectiveDateMap);
//                }
//
//                //法人
//                Map<String, String> lawRepresentativeMap=new HashMap<>();
//                lawRepresentativeMap.put("法定代表人",ocrBusinessLicenseResponse.getLawRepresentative());
//                String  lawRepresentativeLocationJson=ocrBusinessLicenseResponse.getLawRepresentativeLocationJson();
//                if(StrUtil.isNotEmpty(lawRepresentativeLocationJson)){
//                    JSONObject jsonObject=JSONObject.parseObject(lawRepresentativeLocationJson);
//                    lawRepresentativeMap.put("top",jsonObject.getString("top"));
//                    lawRepresentativeMap.put("left",jsonObject.getString("left"));
//                    lawRepresentativeMap.put("height",jsonObject.getString("height"));
//                    lawRepresentativeMap.put("width",jsonObject.getString("width"));
//                    lawRepresentativeMap.put("words",ocrBusinessLicenseResponse.getLawRepresentative());
//                    resultList.put("法定代表人",lawRepresentativeMap);
//                }
//                //注册资本
//                Map<String, String> registeredCapitalMap=new HashMap<>();
//                registeredCapitalMap.put("注册资本",ocrBusinessLicenseResponse.getRegisteredCapital());
//                String  registeredCapitalLocationJson= ocrBusinessLicenseResponse.getRegisteredCapitalLocationJson();
//                if(StrUtil.isNotEmpty(registeredCapitalLocationJson)){
//                    JSONObject jsonObject=JSONObject.parseObject(registeredCapitalLocationJson);
//                    registeredCapitalMap.put("top",jsonObject.getString("top"));
//                    registeredCapitalMap.put("left",jsonObject.getString("left"));
//                    registeredCapitalMap.put("height",jsonObject.getString("height"));
//                    registeredCapitalMap.put("width",jsonObject.getString("width"));
//                    registeredCapitalMap.put("words",ocrBusinessLicenseResponse.getRegisteredCapital());
//                    resultList.put("注册资本",registeredCapitalMap);
//                }
//
//                //类型
//                Map<String, String> typeMap=new HashMap<>();
//                typeMap.put("姓名",ocrBusinessLicenseResponse.getType());
//                String  typeLocationJson= ocrBusinessLicenseResponse.getTypeLocationJson();
//                if(StrUtil.isNotEmpty(typeLocationJson)){
//                    JSONObject jsonObject=JSONObject.parseObject(typeLocationJson);
//                    typeMap.put("top",jsonObject.getString("top"));
//                    typeMap.put("left",jsonObject.getString("left"));
//                    typeMap.put("height",jsonObject.getString("height"));
//                    typeMap.put("width",jsonObject.getString("width"));
//                    typeMap.put("words",ocrBusinessLicenseResponse.getType());
//                    resultList.put("姓名",typeMap);
//                }
//                //组成形式
//                Map<String, String> compositionFormMap=new HashMap<>();
//                compositionFormMap.put("组成形式",ocrBusinessLicenseResponse.getCompositionForm());
//                String  compositionFormLocationJson= ocrBusinessLicenseResponse.getCompositionFormLocationJson();
//                if(StrUtil.isNotEmpty(compositionFormLocationJson)){
//                    JSONObject jsonObject=JSONObject.parseObject(compositionFormLocationJson);
//                    compositionFormMap.put("top",jsonObject.getString("top"));
//                    compositionFormMap.put("left",jsonObject.getString("left"));
//                    compositionFormMap.put("height",jsonObject.getString("height"));
//                    compositionFormMap.put("width",jsonObject.getString("width"));
//                    compositionFormMap.put("words",ocrBusinessLicenseResponse.getCompositionForm());
//                    resultList.put("组成形式",compositionFormMap);
//                }
//
//                //证件编号
//                Map<String, String> idNumberMap=new HashMap<>();
//                idNumberMap.put("证件编号",ocrBusinessLicenseResponse.getIdNumber());
//                String  idNumberLocationJson= ocrBusinessLicenseResponse.getIdNumberLocationJson();
//                if(StrUtil.isNotEmpty(idNumberLocationJson)){
//                    JSONObject jsonObject=JSONObject.parseObject(idNumberLocationJson);
//                    idNumberMap.put("top",jsonObject.getString("top"));
//                    idNumberMap.put("left",jsonObject.getString("left"));
//                    idNumberMap.put("height",jsonObject.getString("height"));
//                    idNumberMap.put("width",jsonObject.getString("width"));
//                    idNumberMap.put("words",ocrBusinessLicenseResponse.getIdNumber());
//                    resultList.put("证件编号",idNumberMap);
//                }
//
//            }
            return resultList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
