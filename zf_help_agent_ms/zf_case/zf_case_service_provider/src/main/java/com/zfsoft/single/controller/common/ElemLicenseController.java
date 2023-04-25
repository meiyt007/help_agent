package com.zfsoft.single.controller.common;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.service.QlCaseMaterialService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.single.data.yxpz.SxMaterialBill;
import com.zfsoft.single.data.yxpz.SxMaterialElmsConfig;
import com.zfsoft.single.manager.sxpz.SxMaterialBillManager;
import com.zfsoft.single.manager.sxpz.SxMaterialElmsConfigManager;
import com.zfsoft.single.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 电子证照对接方法类
 */
@RestController
@Slf4j
@RequestMapping(value = "/elemLice")
public class ElemLicenseController {
    /**
     * 电子证照授权码
     */
    @Value("${zfsoft.dzzz.token}")
    private  String zzToken;
    /** url */
    @Value("${zfsoft.dzzz.url}")
    private  String urlElec;

    @Resource
    private SxMaterialElmsConfigManager sxMaterialElmsConfigManager;

    @Resource
    private SxMaterialBillManager sxMaterialBillManager;

    @Autowired
    private QlCaseMaterialService qlCaseMaterialServiceFeginService;

    /**
     * 根据证照主键下载或者预览电子证照
     * @param elecLicenOid
     * @return
     */
    @PostMapping(value = "/downloadLicenseByElecLicenOid")
    public ApiResultSet downloadLicenseByElecLicenOid(@RequestParam(value = "elecLicenOid") String elecLicenOid) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("accountId", zzToken);
        params.put("elecLicenOid", elecLicenOid);
        String res= HttpUtil.post(urlElec + "/api/downloadLicenseByElecLicenOid.do", params);
        if(StrUtil.isNotEmpty(res)){
            JSONObject jsonObj = JSONObject.parseObject(res);
            Map<String,Object> map=jsonObj;
            JSONObject resultType= (JSONObject)map.get("head");
            if(resultType.get("status").equals("0")){
                JSONObject eleJson = (JSONObject) map.get("data");
                if (eleJson != null) {
                    return new ApiResultSet(eleJson.get("dataList"));
                }
            }else{
                String message = (String) map.get("message");
                if (message != null) {
                    return new ApiResultSet(message);
                }
            }
        }
        return new ApiResultSet();
    }

    /**
     * 根据用户和身份证获取证照
     * @param materialOid
     * @param userName
     * @param idCard
     * @return
     */
    @PostMapping(value = "/queryElecLicenseByDirCode")
    public ApiResultSet queryElecLicenseByDirCode(@RequestParam(value = "materialOid") String materialOid
            ,@RequestParam(value = "userName")String userName
            ,@RequestParam(value = "idCard")String idCard
            ,@RequestParam(value = "billOid")String billOid
            ,@RequestParam(value = "caseOid")String caseOid) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        SxMaterialBill materialBill =null;
        if(StringUtils.isEmpty(billOid)){
            SxMaterialElmsConfig elmsConfig=sxMaterialElmsConfigManager.getSxMaterialElmsConfigByMaterOid(materialOid,null);
            if(elmsConfig!=null){
                billOid=elmsConfig.getBillOid();
            }
        }
        materialBill = sxMaterialBillManager.getSxMaterialBillByMaterOid(billOid);

        if(StrUtil.isNotEmpty(billOid)&&(!billOid.equals("null")) && (!billOid.equals("undefined"))){
            params.put("token", zzToken);
            params.put("info1",idCard);//"121212"
            params.put("info2",userName);//"综窗测试1130"
            params.put("directoryOid",billOid);//"ff8080816d3d0f36016d6cfa3d530247"
            String res= HttpUtil.post(urlElec + "/ws/elecServeESWs/getElecLicenseByBillOidAndOwnerInfoWs.do", params);
            log.info("电子证照接口返回：{}", res);
           if(StrUtil.isNotEmpty(res)){
               JSONObject jsonObj = JSONObject.parseObject(res);
               String resultType = String.valueOf(jsonObj.get("resultType"));
               String resultData = String.valueOf(jsonObj.get("resultData")==null?"":jsonObj.get("resultData"));
               if (!StrUtil.isEmpty(resultType)
                       && "101".equals(resultType)) {
                   if (StrUtil.isNotEmpty(resultData)) {
                       // jason字符串转map
                       Map maps = (Map) JSONObject.parseObject(resultData);
                       String licenseNumber = maps.get("licenseNumber").toString();
                       String elecLicenOid=maps.get("elecLicenOid").toString();
                       String elecLicenNumber=maps.get("elecLicenseUnique").toString();
                       Map resMap=new HashMap();
                       resMap.put("licenseNumber",licenseNumber);
                       resMap.put("elecLicenOid",elecLicenOid);
                       resMap.put("elecLicenName",materialBill.getDirectoryName());
                       resMap.put("elecLicenNumber",elecLicenNumber);
                       //保存电子证照接口信息
                       ApiResultSet<QlCaseMaterial> qlCaseMaterialApiResultSet = qlCaseMaterialServiceFeginService.queryQlCaseMaterialByOid(caseOid, materialOid);
                       if (qlCaseMaterialApiResultSet !=null && qlCaseMaterialApiResultSet.getData() !=null) {  //单办
                           QlCaseMaterial qlCaseMaterial = qlCaseMaterialApiResultSet.getData();
                           qlCaseMaterial.setElectronicResult(res);
                           qlCaseMaterial.setElecBillOid(billOid);
                           qlCaseMaterialServiceFeginService.updateQlCaseMaterial(qlCaseMaterial);
                       }
                       return new ApiResultSet(resMap);
                   }
               }
           }

        }
        return new ApiResultSet();
    }

    /***
     * @Description: 获取证照类型
     * @Author:liangss
     * @Date:2021/9/19
     * @Param: [materialOid, billOid]
     */
    @PostMapping(value = "/queryElecLicenseType")
    public ApiResultSet queryElecLicenseType(@RequestParam(value = "materialOid") String materialOid
            ,@RequestParam(value = "billOid",required = false)String billOid) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        SxMaterialBill materialBill =null;
        SxMaterialElmsConfig elmsConfig=sxMaterialElmsConfigManager.getSxMaterialElmsConfigByMaterOid(materialOid,null);
        if(elmsConfig!=null && StringUtils.isNotEmpty(elmsConfig.getBillOid())){
            billOid=elmsConfig.getBillOid();
            materialBill = sxMaterialBillManager.getSxMaterialBillByMaterOid(billOid);
        }
        Map resMap=new HashMap();
        String directoryObj="";
        if(null!=materialBill){
            directoryObj=materialBill.getDirectoryObj();
        }
        resMap.put("directoryObj",directoryObj);
        return new ApiResultSet(resMap);
    }

}
