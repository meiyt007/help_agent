package com.zfsoft.single.manager.clzs;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.rest.annotation.CataWordBind;
import com.zfsoft.single.util.FaStaticParam;
import com.zfsoft.superwindow.data.clzs.MaterialCatalog;
import com.zfsoft.superwindow.data.clzs.MaterialCatalogElement;
import com.zfsoft.superwindow.data.clzs.dto.IdcardInfo;
import com.zfsoft.superwindow.service.clzs.MaterialCatalogElementService;
import com.zfsoft.superwindow.util.fa.AiTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class IdcardInfoManager {
    @Resource
    private AiTokenUtil aiTokenUtil;
//    @Resource
//    private IOcrCertificateRestService ocrCertificateRestService;
    @Resource
    private MaterialCatalogElementService materialCatalogElementFeginService;

    /**
     * 获取身份证对象-头像面信息
     * @param picBase64
     * @return
     * @throws Exception
     */
    public IdcardInfo getIdcardFrontInfo(String picBase64) {
        IdcardInfo idcardInfo = null;
        picBase64 = picBase64.replace(" ", "+");

//        OcrIdcardRequest ocrIdcardRequest = aiTokenUtil.getTokenRequest(OcrIdcardRequest.class);
//        ocrIdcardRequest.setImgBase64(picBase64);
//        OcrIdcardResponse ocrIdcardResponse = ocrCertificateRestService.idcardFront(ocrIdcardRequest);
//
//        // 调用成功
//        String code = String.valueOf(ocrIdcardResponse.getCode());
//        if (FaStaticParam.HTTP_REQUEST_CODE_SUCCESS.equals(code)) {
//            idcardInfo = new IdcardInfo();
//            idcardInfo.setName(ocrIdcardResponse.getName());
//            idcardInfo.setSex(ocrIdcardResponse.getSex());
//            idcardInfo.setNation(ocrIdcardResponse.getNation());
//            idcardInfo.setBirth(ocrIdcardResponse.getBirth());
//            idcardInfo.setAddress(ocrIdcardResponse.getAddress());
//            idcardInfo.setNumber(ocrIdcardResponse.getNumber());
//            idcardInfo.setSignDepartment(ocrIdcardResponse.getSignDepartment());
//            idcardInfo.setSignDate(ocrIdcardResponse.getSignDate());
//            idcardInfo.setEndDate(ocrIdcardResponse.getEndDate());
//        }
        return idcardInfo;
    }


    public IdcardInfo getIdcardBackInfo(String picBase64) {
        IdcardInfo idcardInfo = null;
        picBase64 = picBase64.replace(" ", "+");

//        OcrIdcardRequest ocrIdcardRequest = aiTokenUtil.getTokenRequest(OcrIdcardRequest.class);
//        ocrIdcardRequest.setImgBase64(picBase64);
//        OcrIdcardResponse ocrIdcardResponse = ocrCertificateRestService.idcardBack(ocrIdcardRequest);
//
//        // 调用成功
//        String code = String.valueOf(ocrIdcardResponse.getCode());
//        if (FaStaticParam.HTTP_REQUEST_CODE_SUCCESS.equals(code)) {
//            idcardInfo = new IdcardInfo();
//            idcardInfo.setName(ocrIdcardResponse.getName());
//            idcardInfo.setSex(ocrIdcardResponse.getSex());
//            idcardInfo.setNation(ocrIdcardResponse.getNation());
//            idcardInfo.setBirth(ocrIdcardResponse.getBirth());
//            idcardInfo.setAddress(ocrIdcardResponse.getAddress());
//            idcardInfo.setNumber(ocrIdcardResponse.getNumber());
//            idcardInfo.setSignDepartment(ocrIdcardResponse.getSignDepartment());
//            idcardInfo.setSignDate(ocrIdcardResponse.getSignDate());
//            idcardInfo.setEndDate(ocrIdcardResponse.getEndDate());
//        }
        return idcardInfo;
    }

    public IdcardInfo getTempIdcardInfo(String picBase64) {
        IdcardInfo idcardInfo = null;
        picBase64 = picBase64.replace(" ", "+");

//        OcrIdcardRequest ocrIdcardRequest = aiTokenUtil.getTokenRequest(OcrIdcardRequest.class);
//        ocrIdcardRequest.setImgBase64(picBase64);
//        OcrIdcardResponse ocrIdcardResponse = ocrCertificateRestService.tempIdcard(ocrIdcardRequest);
//
//        // 调用成功
//        String code = String.valueOf(ocrIdcardResponse.getCode());
//        if (FaStaticParam.HTTP_REQUEST_CODE_SUCCESS.equals(code)) {
//            idcardInfo = new IdcardInfo();
//            idcardInfo.setName(ocrIdcardResponse.getName());
//            idcardInfo.setSex(ocrIdcardResponse.getSex());
//            idcardInfo.setNation(ocrIdcardResponse.getNation());
//            idcardInfo.setBirth(ocrIdcardResponse.getBirth());
//            idcardInfo.setAddress(ocrIdcardResponse.getAddress());
//            idcardInfo.setNumber(ocrIdcardResponse.getNumber());
//            idcardInfo.setSignDepartment(ocrIdcardResponse.getSignDepartment());
//            idcardInfo.setSignDate(ocrIdcardResponse.getSignDate());
//            idcardInfo.setEndDate(ocrIdcardResponse.getEndDate());
//        }
        return idcardInfo;
    }

    public Map<String, Object> discernIdcardInfo(IdcardInfo idcardInfo, MaterialCatalog cata) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", BaseStaticParameter.N);
        // 获取到目录元素信息
        MaterialCatalogElement materialCatalogElement=new MaterialCatalogElement();
        materialCatalogElement.setMaterialCatalogOid(cata.getMaterialCatalogOid());
       /* List<MaterialCatalogElement> materialCatalogElements = materialCatalogElementManager.queryList(materialCatalogElement);
        resultMap.put("code", FaStaticParam.HTTP_REQUEST_CODE_SUCCESS);
        List<Map<String, String>> resultList = handIdcardInfo(idcardInfo, materialCatalogElements);
        resultMap.put("result", resultList);*/
        ApiResultSet<List<MaterialCatalogElement>> list=materialCatalogElementFeginService.queryList(materialCatalogElement);
        List<MaterialCatalogElement> faMaterialCatalogMetadataList=null;
        if(list!=null&&list.getData()!=null){
            faMaterialCatalogMetadataList=list.getData();
        }
        resultMap.put("code", FaStaticParam.HTTP_REQUEST_CODE_SUCCESS);
        List<Map<String, String>> resultList = handIdcardInfo(idcardInfo, faMaterialCatalogMetadataList);
        resultMap.put("result", resultList);
        return resultMap;
    }

    /**
     * 处理身份证识别结果
     *
     * @author chenbw
     * @date 2019年6月17日
     * @param idcardInfo
     * @param faMaterialCatalogMetadataList
     *            区块列表
     * @return
     * @throws Exception
     */
    private List<Map<String, String>> handIdcardInfo(IdcardInfo idcardInfo,
                                                     List<MaterialCatalogElement> faMaterialCatalogMetadataList) {
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        try {
            // 得到类的属性列表
            Field[] fields = idcardInfo.getClass().getDeclaredFields();
            // key-目录元素 value-值
            Map<String, String> businessLiceInfoMap = new HashMap<>();
            for (Field field : fields) {
                // 这个是检查类中属性是否含有注解
                if (field.isAnnotationPresent(CataWordBind.class)) {
                    // 获取注解
                    CataWordBind annotation = field.getAnnotation(CataWordBind.class);
                    String word = annotation.word();
                    if (StrUtil.isBlank(word)){
                        continue;
                    }
                    field.setAccessible(true); // 设置些属性是可以访问的
                    String val = field.get(idcardInfo) == null ? "" : field.get(idcardInfo).toString();
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


    /***
    * @Description: 获取身份证值和注解
    * @Author:liangss
    * @Date:2021/10/25
    * @Param: [idcardInfo]
    */
    public  Map<String, String> handIdcardInfoNew(IdcardInfo idcardInfo) {
        Map<String, String> businessLiceInfoMap = new HashMap<>();
        try {
            // 得到类的属性列表
            Field[] fields = idcardInfo.getClass().getDeclaredFields();
            // key-目录元素 value-值

            for (Field field : fields) {
                // 这个是检查类中属性是否含有注解
                if (field.isAnnotationPresent(CataWordBind.class)) {
                    // 获取注解
                    CataWordBind annotation = field.getAnnotation(CataWordBind.class);
                    String word = annotation.word();
                    if (StrUtil.isBlank(word)){
                        continue;
                    }
                    field.setAccessible(true); // 设置些属性是可以访问的
                    String val = field.get(idcardInfo) == null ? "" : field.get(idcardInfo).toString();
                    businessLiceInfoMap.put(word, val);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return businessLiceInfoMap;
    }



    /***
    * @Description:  根据base64获取身份证复印件信息和位置
    * @Author:liangss
    * @Date:2021/10/25
    * @Param: [picBase64]
    */
    public List<Map<String, String>> getTempIdcardInfoAndPosition(String picBase64) {
        IdcardInfo idcardInfo = null;
        picBase64 = picBase64.replace(" ", "+");
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
//        OcrIdcardRequest ocrIdcardRequest = aiTokenUtil.getTokenRequest(OcrIdcardRequest.class);
//        ocrIdcardRequest.setImgBase64(picBase64);
//        OcrIdcardResponse ocrIdcardResponse = ocrCertificateRestService.tempIdcard(ocrIdcardRequest);
//
//        // 调用成功
//        String code = String.valueOf(ocrIdcardResponse.getCode());
//        if (FaStaticParam.HTTP_REQUEST_CODE_SUCCESS.equals(code)) {
//            //姓名
//            Map<String, String> nameMap=new HashMap<>();
//            nameMap.put("姓名",ocrIdcardResponse.getName());
//            String  nameLocationJson= ocrIdcardResponse.getNameLocationJson();
//            if(StrUtil.isNotEmpty(nameLocationJson)){
//              JSONObject jsonObject=JSONObject.parseObject(nameLocationJson);
//                nameMap.put("top",jsonObject.getString("top"));
//                nameMap.put("left",jsonObject.getString("left"));
//                nameMap.put("height",jsonObject.getString("height"));
//                nameMap.put("width",jsonObject.getString("width"));
//            }
//            resultList.add(nameMap);
//            //性别
//            Map<String, String> sexMap=new HashMap<>();
//            sexMap.put("性别",ocrIdcardResponse.getSex());
//            String  sexMapLocationJson= ocrIdcardResponse.getSexLocationJson();
//            if(StrUtil.isNotEmpty(sexMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(sexMapLocationJson);
//                sexMap.put("top",jsonObject.getString("top"));
//                sexMap.put("left",jsonObject.getString("left"));
//                sexMap.put("height",jsonObject.getString("height"));
//                sexMap.put("width",jsonObject.getString("width"));
//            }
//            resultList.add(sexMap);
//            //民族
//            Map<String, String> nationMap=new HashMap<>();
//            nationMap.put("民族",ocrIdcardResponse.getNation());
//            String  nationMapLocationJson= ocrIdcardResponse.getNationLocationJson();
//            if(StrUtil.isNotEmpty(nationMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(nationMapLocationJson);
//                nationMap.put("top",jsonObject.getString("top"));
//                nationMap.put("left",jsonObject.getString("left"));
//                nationMap.put("height",jsonObject.getString("height"));
//                nationMap.put("width",jsonObject.getString("width"));
//            }
//            resultList.add(nationMap);
//            //出生
//            Map<String, String> birthMap=new HashMap<>();
//            birthMap.put("出生",ocrIdcardResponse.getBirth());
//            String  birthMapLocationJson= ocrIdcardResponse.getBirthLocationJson();
//            if(StrUtil.isNotEmpty(birthMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(birthMapLocationJson);
//                birthMap.put("top",jsonObject.getString("top"));
//                birthMap.put("left",jsonObject.getString("left"));
//                birthMap.put("height",jsonObject.getString("height"));
//                birthMap.put("width",jsonObject.getString("width"));
//            }
//            resultList.add(birthMap);
//
//            //住址
//            Map<String, String> addressMap=new HashMap<>();
//            addressMap.put("住址",ocrIdcardResponse.getAddress());
//            String  addressMapLocationJson= ocrIdcardResponse.getAddressLocationJson();
//            if(StrUtil.isNotEmpty(addressMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(addressMapLocationJson);
//                addressMap.put("top",jsonObject.getString("top"));
//                addressMap.put("left",jsonObject.getString("left"));
//                addressMap.put("height",jsonObject.getString("height"));
//                addressMap.put("width",jsonObject.getString("width"));
//            }
//            resultList.add(addressMap);
//
//            //公民身份号码
//            Map<String, String> numberMap=new HashMap<>();
//            numberMap.put("公民身份号码",ocrIdcardResponse.getNumber());
//            String  numberMapLocationJson= ocrIdcardResponse.getNumberLocationJson();
//            if(StrUtil.isNotEmpty(numberMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(numberMapLocationJson);
//                numberMap.put("top",jsonObject.getString("top"));
//                numberMap.put("left",jsonObject.getString("left"));
//                numberMap.put("height",jsonObject.getString("height"));
//                numberMap.put("width",jsonObject.getString("width"));
//            }
//            resultList.add(numberMap);
//            //签发机关
//            Map<String, String> signDepartmentMap=new HashMap<>();
//            signDepartmentMap.put("签发机关",ocrIdcardResponse.getSignDepartment());
//            String  signDepartmentMapLocationJson= ocrIdcardResponse.getSignDepartmentLocationJson();
//            if(StrUtil.isNotEmpty(signDepartmentMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(signDepartmentMapLocationJson);
//                signDepartmentMap.put("top",jsonObject.getString("top"));
//                signDepartmentMap.put("left",jsonObject.getString("left"));
//                signDepartmentMap.put("height",jsonObject.getString("height"));
//                signDepartmentMap.put("width",jsonObject.getString("width"));
//            }
//            resultList.add(signDepartmentMap);
//            //签发日期
//            Map<String, String> signDateMap=new HashMap<>();
//            signDateMap.put("签发日期",ocrIdcardResponse.getSignDate());
//            String  signDateMapLocationJson= ocrIdcardResponse.getSignDateLocationJson();
//            if(StrUtil.isNotEmpty(signDateMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(signDateMapLocationJson);
//                signDateMap.put("top",jsonObject.getString("top"));
//                signDateMap.put("left",jsonObject.getString("left"));
//                signDateMap.put("height",jsonObject.getString("height"));
//                signDateMap.put("width",jsonObject.getString("width"));
//            }
//            resultList.add(signDateMap);
//            //失效日期
//            Map<String, String> endDateMap=new HashMap<>();
//            endDateMap.put("失效日期",ocrIdcardResponse.getEndDate());
//            String  endDateMapLocationJson= ocrIdcardResponse.getEndDateLocationJson();
//            if(StrUtil.isNotEmpty(endDateMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(endDateMapLocationJson);
//                endDateMap.put("top",jsonObject.getString("top"));
//                endDateMap.put("left",jsonObject.getString("left"));
//                endDateMap.put("height",jsonObject.getString("height"));
//                endDateMap.put("width",jsonObject.getString("width"));
//            }
//            resultList.add(endDateMap);
//
//        }
        return resultList;
    }


    /***
    * @Description:  获取身份证复印件取值map
    * @Author:liangss
    * @Date:2021/10/25
    * @Param: [picBase64]
    */
    public Map<String,Map<String, String>> getTempIdcardInfoAndPositionMap(String picBase64) {
        IdcardInfo idcardInfo = null;
        picBase64 = picBase64.replace(" ", "+");
        Map<String,Map<String, String>> resultList = new HashMap<String,Map<String, String>>();
//        OcrIdcardRequest ocrIdcardRequest = aiTokenUtil.getTokenRequest(OcrIdcardRequest.class);
//        ocrIdcardRequest.setImgBase64(picBase64);
//        OcrIdcardResponse ocrIdcardResponse = ocrCertificateRestService.tempIdcard(ocrIdcardRequest);
//
//        // 调用成功
//        String code = String.valueOf(ocrIdcardResponse.getCode());
//        if (FaStaticParam.HTTP_REQUEST_CODE_SUCCESS.equals(code)) {
//            //姓名
//            Map<String, String> nameMap=new HashMap<>();
//            nameMap.put("姓名",ocrIdcardResponse.getName());
//            String  nameLocationJson= ocrIdcardResponse.getNameLocationJson();
//            if(StrUtil.isNotEmpty(nameLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(nameLocationJson);
//                nameMap.put("top",jsonObject.getString("top"));
//                nameMap.put("left",jsonObject.getString("left"));
//                nameMap.put("height",jsonObject.getString("height"));
//                nameMap.put("width",jsonObject.getString("width"));
//            }
//            nameMap.put("words",ocrIdcardResponse.getName());
//            resultList.put("姓名",nameMap);
//            //性别
//            Map<String, String> sexMap=new HashMap<>();
//            sexMap.put("性别",ocrIdcardResponse.getSex());
//            String  sexMapLocationJson= ocrIdcardResponse.getSexLocationJson();
//            if(StrUtil.isNotEmpty(sexMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(sexMapLocationJson);
//                sexMap.put("top",jsonObject.getString("top"));
//                sexMap.put("left",jsonObject.getString("left"));
//                sexMap.put("height",jsonObject.getString("height"));
//                sexMap.put("width",jsonObject.getString("width"));
//            }
//            sexMap.put("words",ocrIdcardResponse.getSex());
//            resultList.put("性别",sexMap);
//            //民族
//            Map<String, String> nationMap=new HashMap<>();
//            nationMap.put("民族",ocrIdcardResponse.getNation());
//            String  nationMapLocationJson= ocrIdcardResponse.getNationLocationJson();
//            if(StrUtil.isNotEmpty(nationMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(nationMapLocationJson);
//                nationMap.put("top",jsonObject.getString("top"));
//                nationMap.put("left",jsonObject.getString("left"));
//                nationMap.put("height",jsonObject.getString("height"));
//                nationMap.put("width",jsonObject.getString("width"));
//            }
//            nationMap.put("words",ocrIdcardResponse.getNation());
//            resultList.put("民族",nationMap);
//            //出生
//            Map<String, String> birthMap=new HashMap<>();
//            birthMap.put("出生",ocrIdcardResponse.getBirth());
//            String  birthMapLocationJson= ocrIdcardResponse.getBirthLocationJson();
//            if(StrUtil.isNotEmpty(birthMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(birthMapLocationJson);
//                birthMap.put("top",jsonObject.getString("top"));
//                birthMap.put("left",jsonObject.getString("left"));
//                birthMap.put("height",jsonObject.getString("height"));
//                birthMap.put("width",jsonObject.getString("width"));
//            }
//            birthMap.put("words",ocrIdcardResponse.getBirth());
//            resultList.put("出生",birthMap);
//
//            //住址
//            Map<String, String> addressMap=new HashMap<>();
//            addressMap.put("住址",ocrIdcardResponse.getAddress());
//            String  addressMapLocationJson= ocrIdcardResponse.getAddressLocationJson();
//            if(StrUtil.isNotEmpty(addressMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(addressMapLocationJson);
//                addressMap.put("top",jsonObject.getString("top"));
//                addressMap.put("left",jsonObject.getString("left"));
//                addressMap.put("height",jsonObject.getString("height"));
//                addressMap.put("width",jsonObject.getString("width"));
//            }
//            addressMap.put("words",ocrIdcardResponse.getAddress());
//            resultList.put("住址",addressMap);
//
//            //公民身份号码
//            Map<String, String> numberMap=new HashMap<>();
//            numberMap.put("公民身份号码",ocrIdcardResponse.getNumber());
//            String  numberMapLocationJson= ocrIdcardResponse.getNumberLocationJson();
//            if(StrUtil.isNotEmpty(numberMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(numberMapLocationJson);
//                numberMap.put("top",jsonObject.getString("top"));
//                numberMap.put("left",jsonObject.getString("left"));
//                numberMap.put("height",jsonObject.getString("height"));
//                numberMap.put("width",jsonObject.getString("width"));
//            }
//            numberMap.put("words",ocrIdcardResponse.getNumber());
//            resultList.put("公民身份号码",numberMap);
//            //签发机关
//            Map<String, String> signDepartmentMap=new HashMap<>();
//            signDepartmentMap.put("签发机关",ocrIdcardResponse.getSignDepartment());
//            String  signDepartmentMapLocationJson= ocrIdcardResponse.getSignDepartmentLocationJson();
//            if(StrUtil.isNotEmpty(signDepartmentMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(signDepartmentMapLocationJson);
//                signDepartmentMap.put("top",jsonObject.getString("top"));
//                signDepartmentMap.put("left",jsonObject.getString("left"));
//                signDepartmentMap.put("height",jsonObject.getString("height"));
//                signDepartmentMap.put("width",jsonObject.getString("width"));
//            }
//            signDepartmentMap.put("words",ocrIdcardResponse.getSignDepartment());
//            resultList.put("签发机关",signDepartmentMap);
//            //签发日期
//            Map<String, String> signDateMap=new HashMap<>();
//            signDateMap.put("签发日期",ocrIdcardResponse.getSignDate());
//            String  signDateMapLocationJson= ocrIdcardResponse.getSignDateLocationJson();
//            if(StrUtil.isNotEmpty(signDateMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(signDateMapLocationJson);
//                signDateMap.put("top",jsonObject.getString("top"));
//                signDateMap.put("left",jsonObject.getString("left"));
//                signDateMap.put("height",jsonObject.getString("height"));
//                signDateMap.put("width",jsonObject.getString("width"));
//            }
//            signDateMap.put("words",ocrIdcardResponse.getSignDate());
//            resultList.put("签发日期",signDateMap);
//            //失效日期
//            Map<String, String> endDateMap=new HashMap<>();
//            endDateMap.put("失效日期",ocrIdcardResponse.getEndDate());
//            String  endDateMapLocationJson= ocrIdcardResponse.getEndDateLocationJson();
//            if(StrUtil.isNotEmpty(endDateMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(endDateMapLocationJson);
//                endDateMap.put("top",jsonObject.getString("top"));
//                endDateMap.put("left",jsonObject.getString("left"));
//                endDateMap.put("height",jsonObject.getString("height"));
//                endDateMap.put("width",jsonObject.getString("width"));
//            }
//            endDateMap.put("words",ocrIdcardResponse.getEndDate());
//            resultList.put("失效日期",endDateMap);
//
//        }
        return resultList;
    }


    /***
    * @Description:  获取身份证反面取值
    * @Author:liangss
    * @Date:2021/10/25
    * @Param: [picBase64]
    */
    public Map<String,Map<String, String>> getIdcardBackInfoMap(String picBase64) {
        IdcardInfo idcardInfo = null;
        picBase64 = picBase64.replace(" ", "+");
        Map<String,Map<String, String>> resultList = new HashMap<String,Map<String, String>>();
//        OcrIdcardRequest ocrIdcardRequest = aiTokenUtil.getTokenRequest(OcrIdcardRequest.class);
//        ocrIdcardRequest.setImgBase64(picBase64);
//        OcrIdcardResponse ocrIdcardResponse = ocrCertificateRestService.idcardBack(ocrIdcardRequest);
//        // 调用成功
//        String code = String.valueOf(ocrIdcardResponse.getCode());
//        if (FaStaticParam.HTTP_REQUEST_CODE_SUCCESS.equals(code)) {
//            //姓名
//            Map<String, String> nameMap=new HashMap<>();
//            nameMap.put("姓名",ocrIdcardResponse.getName());
//            String  nameLocationJson= ocrIdcardResponse.getNameLocationJson();
//            if(StrUtil.isNotEmpty(nameLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(nameLocationJson);
//                nameMap.put("top",jsonObject.getString("top"));
//                nameMap.put("left",jsonObject.getString("left"));
//                nameMap.put("height",jsonObject.getString("height"));
//                nameMap.put("width",jsonObject.getString("width"));
//                nameMap.put("words",ocrIdcardResponse.getName());
//                resultList.put("姓名",nameMap);
//            }
//
//            //性别
//            Map<String, String> sexMap=new HashMap<>();
//            sexMap.put("性别",ocrIdcardResponse.getSex());
//            String  sexMapLocationJson= ocrIdcardResponse.getSexLocationJson();
//            if(StrUtil.isNotEmpty(sexMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(sexMapLocationJson);
//                sexMap.put("top",jsonObject.getString("top"));
//                sexMap.put("left",jsonObject.getString("left"));
//                sexMap.put("height",jsonObject.getString("height"));
//                sexMap.put("width",jsonObject.getString("width"));
//                sexMap.put("words",ocrIdcardResponse.getSex());
//                resultList.put("性别",sexMap);
//            }
//
//            //民族
//            Map<String, String> nationMap=new HashMap<>();
//            nationMap.put("民族",ocrIdcardResponse.getNation());
//            String  nationMapLocationJson= ocrIdcardResponse.getNationLocationJson();
//            if(StrUtil.isNotEmpty(nationMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(nationMapLocationJson);
//                nationMap.put("top",jsonObject.getString("top"));
//                nationMap.put("left",jsonObject.getString("left"));
//                nationMap.put("height",jsonObject.getString("height"));
//                nationMap.put("width",jsonObject.getString("width"));
//                nationMap.put("words",ocrIdcardResponse.getNation());
//                resultList.put("民族",nationMap);
//            }
//
//            //出生
//            Map<String, String> birthMap=new HashMap<>();
//            birthMap.put("出生",ocrIdcardResponse.getBirth());
//            String  birthMapLocationJson= ocrIdcardResponse.getBirthLocationJson();
//            if(StrUtil.isNotEmpty(birthMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(birthMapLocationJson);
//                birthMap.put("top",jsonObject.getString("top"));
//                birthMap.put("left",jsonObject.getString("left"));
//                birthMap.put("height",jsonObject.getString("height"));
//                birthMap.put("width",jsonObject.getString("width"));
//                birthMap.put("words",ocrIdcardResponse.getBirth());
//                resultList.put("出生",birthMap);
//            }
//            //住址
//            Map<String, String> addressMap=new HashMap<>();
//            addressMap.put("住址",ocrIdcardResponse.getAddress());
//            String  addressMapLocationJson= ocrIdcardResponse.getAddressLocationJson();
//            if(StrUtil.isNotEmpty(addressMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(addressMapLocationJson);
//                addressMap.put("top",jsonObject.getString("top"));
//                addressMap.put("left",jsonObject.getString("left"));
//                addressMap.put("height",jsonObject.getString("height"));
//                addressMap.put("width",jsonObject.getString("width"));
//                addressMap.put("words",ocrIdcardResponse.getAddress());
//                resultList.put("住址",addressMap);
//            }
//
//
//            //公民身份号码
//            Map<String, String> numberMap=new HashMap<>();
//            numberMap.put("公民身份号码",ocrIdcardResponse.getNumber());
//            String  numberMapLocationJson= ocrIdcardResponse.getNumberLocationJson();
//            if(StrUtil.isNotEmpty(numberMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(numberMapLocationJson);
//                numberMap.put("top",jsonObject.getString("top"));
//                numberMap.put("left",jsonObject.getString("left"));
//                numberMap.put("height",jsonObject.getString("height"));
//                numberMap.put("width",jsonObject.getString("width"));
//                numberMap.put("words",ocrIdcardResponse.getNumber());
//                resultList.put("公民身份号码",numberMap);
//            }
//
//            //签发机关
//            Map<String, String> signDepartmentMap=new HashMap<>();
//            signDepartmentMap.put("签发机关",ocrIdcardResponse.getSignDepartment());
//            String  signDepartmentMapLocationJson= ocrIdcardResponse.getSignDepartmentLocationJson();
//            if(StrUtil.isNotEmpty(signDepartmentMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(signDepartmentMapLocationJson);
//                signDepartmentMap.put("top",jsonObject.getString("top"));
//                signDepartmentMap.put("left",jsonObject.getString("left"));
//                signDepartmentMap.put("height",jsonObject.getString("height"));
//                signDepartmentMap.put("width",jsonObject.getString("width"));
//                signDepartmentMap.put("words",ocrIdcardResponse.getSignDepartment());
//                resultList.put("签发机关",signDepartmentMap);
//            }
//
//            //签发日期
//            Map<String, String> signDateMap=new HashMap<>();
//            signDateMap.put("签发日期",ocrIdcardResponse.getSignDate());
//            String  signDateMapLocationJson= ocrIdcardResponse.getSignDateLocationJson();
//            if(StrUtil.isNotEmpty(signDateMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(signDateMapLocationJson);
//                signDateMap.put("top",jsonObject.getString("top"));
//                signDateMap.put("left",jsonObject.getString("left"));
//                signDateMap.put("height",jsonObject.getString("height"));
//                signDateMap.put("width",jsonObject.getString("width"));
//                signDateMap.put("words",ocrIdcardResponse.getSignDate());
//                resultList.put("签发日期",signDateMap);
//            }
//
//            //失效日期
//            Map<String, String> endDateMap=new HashMap<>();
//            endDateMap.put("失效日期",ocrIdcardResponse.getEndDate());
//            String  endDateMapLocationJson= ocrIdcardResponse.getEndDateLocationJson();
//            if(StrUtil.isNotEmpty(endDateMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(endDateMapLocationJson);
//                endDateMap.put("top",jsonObject.getString("top"));
//                endDateMap.put("left",jsonObject.getString("left"));
//                endDateMap.put("height",jsonObject.getString("height"));
//                endDateMap.put("width",jsonObject.getString("width"));
//                endDateMap.put("words",ocrIdcardResponse.getEndDate());
//                resultList.put("失效日期",endDateMap);
//            }
//
//        }
        return resultList;
    }



    /***
    * @Description: 获取身份证正面信息
    * @Author:liangss
    * @Date:2021/10/25
    * @Param: [picBase64]
    */
    public Map<String,Map<String, String>> getIdcardFrontInfoMap(String picBase64) {
        IdcardInfo idcardInfo = null;
        picBase64 = picBase64.replace(" ", "+");
        Map<String,Map<String, String>> resultList = new HashMap<String,Map<String, String>>();
//        OcrIdcardRequest ocrIdcardRequest = aiTokenUtil.getTokenRequest(OcrIdcardRequest.class);
//        ocrIdcardRequest.setImgBase64(picBase64);
//        OcrIdcardResponse ocrIdcardResponse = ocrCertificateRestService.idcardFront(ocrIdcardRequest);
//        // 调用成功
//        String code = String.valueOf(ocrIdcardResponse.getCode());
//        if (FaStaticParam.HTTP_REQUEST_CODE_SUCCESS.equals(code)) {
//            //姓名
//            Map<String, String> nameMap=new HashMap<>();
//            nameMap.put("姓名",ocrIdcardResponse.getName());
//            String  nameLocationJson= ocrIdcardResponse.getNameLocationJson();
//            if(StrUtil.isNotEmpty(nameLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(nameLocationJson);
//                nameMap.put("top",jsonObject.getString("top"));
//                nameMap.put("left",jsonObject.getString("left"));
//                nameMap.put("height",jsonObject.getString("height"));
//                nameMap.put("width",jsonObject.getString("width"));
//                nameMap.put("words",ocrIdcardResponse.getName());
//                resultList.put("姓名",nameMap);
//            }
//
//            //性别
//            Map<String, String> sexMap=new HashMap<>();
//            sexMap.put("性别",ocrIdcardResponse.getSex());
//            String  sexMapLocationJson= ocrIdcardResponse.getSexLocationJson();
//            if(StrUtil.isNotEmpty(sexMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(sexMapLocationJson);
//                sexMap.put("top",jsonObject.getString("top"));
//                sexMap.put("left",jsonObject.getString("left"));
//                sexMap.put("height",jsonObject.getString("height"));
//                sexMap.put("width",jsonObject.getString("width"));
//                sexMap.put("words",ocrIdcardResponse.getSex());
//                resultList.put("性别",sexMap);
//            }
//
//            //民族
//            Map<String, String> nationMap=new HashMap<>();
//            nationMap.put("民族",ocrIdcardResponse.getNation());
//            String  nationMapLocationJson= ocrIdcardResponse.getNationLocationJson();
//            if(StrUtil.isNotEmpty(nationMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(nationMapLocationJson);
//                nationMap.put("top",jsonObject.getString("top"));
//                nationMap.put("left",jsonObject.getString("left"));
//                nationMap.put("height",jsonObject.getString("height"));
//                nationMap.put("width",jsonObject.getString("width"));
//                nationMap.put("words",ocrIdcardResponse.getNation());
//                resultList.put("民族",nationMap);
//            }
//
//            //出生
//            Map<String, String> birthMap=new HashMap<>();
//            birthMap.put("出生",ocrIdcardResponse.getBirth());
//            String  birthMapLocationJson= ocrIdcardResponse.getBirthLocationJson();
//            if(StrUtil.isNotEmpty(birthMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(birthMapLocationJson);
//                birthMap.put("top",jsonObject.getString("top"));
//                birthMap.put("left",jsonObject.getString("left"));
//                birthMap.put("height",jsonObject.getString("height"));
//                birthMap.put("width",jsonObject.getString("width"));
//                birthMap.put("words",ocrIdcardResponse.getBirth());
//                resultList.put("出生",birthMap);
//            }
//            //住址
//            Map<String, String> addressMap=new HashMap<>();
//            addressMap.put("住址",ocrIdcardResponse.getAddress());
//            String  addressMapLocationJson= ocrIdcardResponse.getAddressLocationJson();
//            if(StrUtil.isNotEmpty(addressMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(addressMapLocationJson);
//                addressMap.put("top",jsonObject.getString("top"));
//                addressMap.put("left",jsonObject.getString("left"));
//                addressMap.put("height",jsonObject.getString("height"));
//                addressMap.put("width",jsonObject.getString("width"));
//                addressMap.put("words",ocrIdcardResponse.getAddress());
//                resultList.put("住址",addressMap);
//            }
//
//
//            //公民身份号码
//            Map<String, String> numberMap=new HashMap<>();
//            numberMap.put("公民身份号码",ocrIdcardResponse.getNumber());
//            String  numberMapLocationJson= ocrIdcardResponse.getNumberLocationJson();
//            if(StrUtil.isNotEmpty(numberMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(numberMapLocationJson);
//                numberMap.put("top",jsonObject.getString("top"));
//                numberMap.put("left",jsonObject.getString("left"));
//                numberMap.put("height",jsonObject.getString("height"));
//                numberMap.put("width",jsonObject.getString("width"));
//                numberMap.put("words",ocrIdcardResponse.getNumber());
//                resultList.put("公民身份号码",numberMap);
//            }
//
//            //签发机关
//            Map<String, String> signDepartmentMap=new HashMap<>();
//            signDepartmentMap.put("签发机关",ocrIdcardResponse.getSignDepartment());
//            String  signDepartmentMapLocationJson= ocrIdcardResponse.getSignDepartmentLocationJson();
//            if(StrUtil.isNotEmpty(signDepartmentMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(signDepartmentMapLocationJson);
//                signDepartmentMap.put("top",jsonObject.getString("top"));
//                signDepartmentMap.put("left",jsonObject.getString("left"));
//                signDepartmentMap.put("height",jsonObject.getString("height"));
//                signDepartmentMap.put("width",jsonObject.getString("width"));
//                signDepartmentMap.put("words",ocrIdcardResponse.getSignDepartment());
//                resultList.put("签发机关",signDepartmentMap);
//            }
//
//            //签发日期
//            Map<String, String> signDateMap=new HashMap<>();
//            signDateMap.put("签发日期",ocrIdcardResponse.getSignDate());
//            String  signDateMapLocationJson= ocrIdcardResponse.getSignDateLocationJson();
//            if(StrUtil.isNotEmpty(signDateMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(signDateMapLocationJson);
//                signDateMap.put("top",jsonObject.getString("top"));
//                signDateMap.put("left",jsonObject.getString("left"));
//                signDateMap.put("height",jsonObject.getString("height"));
//                signDateMap.put("width",jsonObject.getString("width"));
//                signDateMap.put("words",ocrIdcardResponse.getSignDate());
//                resultList.put("签发日期",signDateMap);
//            }
//
//            //失效日期
//            Map<String, String> endDateMap=new HashMap<>();
//            endDateMap.put("失效日期",ocrIdcardResponse.getEndDate());
//            String  endDateMapLocationJson= ocrIdcardResponse.getEndDateLocationJson();
//            if(StrUtil.isNotEmpty(endDateMapLocationJson)){
//                JSONObject jsonObject=JSONObject.parseObject(endDateMapLocationJson);
//                endDateMap.put("top",jsonObject.getString("top"));
//                endDateMap.put("left",jsonObject.getString("left"));
//                endDateMap.put("height",jsonObject.getString("height"));
//                endDateMap.put("width",jsonObject.getString("width"));
//                endDateMap.put("words",ocrIdcardResponse.getEndDate());
//                resultList.put("失效日期",endDateMap);
//            }
//
//        }
        return resultList;
    }

}
