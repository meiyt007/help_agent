package com.zfsoft.cases.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.data.QlCaseMaterialAtta;
import com.zfsoft.cases.dbaccess.dao.QlCaseMaterialMapper;
import com.zfsoft.cases.dbaccess.data.DbQlCaseMaterial;
import com.zfsoft.cases.manager.QlCaseMaterialAttaManager;
import com.zfsoft.cases.service.QlCaseMaterialAttaService;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * @（#）: QlCaseMaterialAttaController
 * @description: 材料附件信息实现类
 * @author: wangwg
 * @date: 2020/10/24
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
public class QlCaseMaterialAttaController implements QlCaseMaterialAttaService {

    @Resource
    private QlCaseMaterialAttaManager qlCaseMaterialAttaManager;

    @Resource
    private QlCaseMaterialMapper qlCaseMaterialMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResultSet<List<Map<String, String>>> saveQlCaseMaterialAtta(Object object) {
        List<QlCaseMaterialAtta> qlCaseMaterialAttaList=new ArrayList<QlCaseMaterialAtta>();
        JSONObject jsonObject = JSONObject.parseObject(JSONArray.toJSON(object).toString());
        //解析转化材料附件对应信息
        JSONArray materialsAttaArray = JSONArray.parseArray(jsonObject.get("materialAttaList").toString());
        //解析转化材料以及上传方式
        JSONArray collectionTypeArray = JSONArray.parseArray(jsonObject.get("collectionTypeList").toString());
        //附件和目录材料关联信息
        JSONArray catalogAttaArray =null;
        if(jsonObject.get("materialCatalogAttaList") !=null){
             catalogAttaArray = JSONArray.parseArray(jsonObject.get("materialCatalogAttaList").toString());
        }
        //保存材料上传方式
        if(collectionTypeArray.size()>0) {
            for (int j = 0; j < collectionTypeArray.size();j++) {
                JSONObject collectionType = collectionTypeArray.getJSONObject(j);
                String type = collectionType.get("collectionType")==null?null:collectionType.get("collectionType").toString();
                String number=collectionType.get("collectNumber").toString();
                String caseMaterialOid = collectionType.get("caseMaterialOid").toString();
                DbQlCaseMaterial dbQlCaseMaterial = qlCaseMaterialMapper.queryMaterialByCaseMaterialOid(caseMaterialOid);
                if(dbQlCaseMaterial !=null){
                    if(StringUtil.isNotEmpty(type)&&type.equals("5")){//证照
                        //证照信息
                        if(jsonObject.get("elemLicenseList")!=null){
                            JSONArray elemLicenseArray = JSONArray.parseArray(jsonObject.get("elemLicenseList").toString());
                            for(int e=0;e<elemLicenseArray.size();e++){
                                JSONObject elem = elemLicenseArray.getJSONObject(e);
                                if(elem.get("materialOid").toString().equals(caseMaterialOid)){
                                    dbQlCaseMaterial.setElemLicenseOid(elem.get("attaOid").toString());
                                    dbQlCaseMaterial.setElemNumber(elem.get("originName").toString());
                                    break;
                                }
                            }
                        }
                    }
                    if(StringUtil.isNotEmpty(type)&&!type.equals("4")){//荣却后补
                        dbQlCaseMaterial.setCollectionFlag(1);
                        if(dbQlCaseMaterial.getCollectionDate()!=null){
                        }else{
                            dbQlCaseMaterial.setCollectionDate(new Date());
                        }
                    }else{
                        dbQlCaseMaterial.setCollectionFlag(0);
                    }
                    dbQlCaseMaterial.setCollectionNumber(Integer.valueOf(number));
                    dbQlCaseMaterial.setCollectionType(type);
                    dbQlCaseMaterial.setModifyDate(new Date());
                    qlCaseMaterialMapper.update(dbQlCaseMaterial);
                    //删除材料下面的附件
                    qlCaseMaterialAttaManager.deleteByCaseMaterialOid(dbQlCaseMaterial.getCaseMaterialOid());
                }
            }
        }
        //设置材料附件信息
        if(materialsAttaArray.size()>0) {
            String materialCatalogOid =null;
            QlCaseMaterialAtta qlCaseMaterialAtta =null;
            for (int i = 0; i < materialsAttaArray.size(); i++) {
                JSONObject materialsAtta = materialsAttaArray.getJSONObject(i);
                String attaOid = materialsAtta.get("attaOid").toString();
                String caseMaterialOid = materialsAtta.get("caseMaterialOid").toString();
                //qlCaseMaterialAtta = qlCaseMaterialAttaManager.getSelectoneByCaseMaterialOid(attaOid,caseMaterialOid);
                //if(qlCaseMaterialAtta ==null){
                   qlCaseMaterialAtta = new QlCaseMaterialAtta();
               // }
                if(catalogAttaArray !=null){
                    for (int k = 0; k < catalogAttaArray.size(); k++) {
                        JSONObject catalogAtta = catalogAttaArray.getJSONObject(k);
                        String oid = catalogAtta.get("attaOid").toString();
                        if(attaOid.equals(oid)){
                            materialCatalogOid=catalogAtta.get("materialCatalogOid").toString();
                            qlCaseMaterialAtta.setMaterialCatalogOid(materialCatalogOid);
                        }
                    }
                }
                qlCaseMaterialAtta.setCaseMaterialOid(caseMaterialOid);
                qlCaseMaterialAtta.setAttaOid(attaOid);
                qlCaseMaterialAttaList.add(qlCaseMaterialAtta);
            }
        }
        List<Map<String, String>> list=qlCaseMaterialAttaManager.saveBatchQlCaseMaterialAtta(qlCaseMaterialAttaList);
        ApiResultSet<List<Map<String, String>>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(list);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<QlCaseMaterialAtta>> queryQlCaseMaterialAttaByCaseMaterialOid(String caseMaterialOid) {
        List<QlCaseMaterialAtta> list = qlCaseMaterialAttaManager.queryQlCaseMaterialAttaByCaseMaterialOid(caseMaterialOid);
        ApiResultSet<List<QlCaseMaterialAtta>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(list);
        return apiResultSet;
    }

    @Override
    public ApiResultSet deleteQlCaseMaterialAttaByCaseMaterialOid(String caseMaterialOid) {
        qlCaseMaterialAttaManager.deleteByCaseMaterialOid(caseMaterialOid);
        return new ApiResultSet();
    }

    @Override
    public ApiResultSet<List<Map<String, String>>> saveQlCaseMaterialAttaList(List<QlCaseMaterialAtta> qlCaseMaterialAttaList) {
        List<Map<String, String>> list=qlCaseMaterialAttaManager.saveBatchQlCaseMaterialAtta(qlCaseMaterialAttaList);
        ApiResultSet<List<Map<String, String>>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(list);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<QlCaseMaterialAtta> queryQlCaseMaterialAttaByOid(String materialAttaOid) {
        QlCaseMaterialAtta qlCaseMaterialAtta = qlCaseMaterialAttaManager.queryQlCaseMaterialAttaByOid(materialAttaOid);
        ApiResultSet<QlCaseMaterialAtta> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(qlCaseMaterialAtta);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<QlCaseMaterialAtta> queryQlCaseMaterialAttaByCatalogOid(String caseMaterialOid, String materialCatalogOid) {
        QlCaseMaterialAtta qlCaseMaterialAtta = qlCaseMaterialAttaManager.queryQlCaseMaterialAttaByCatalogOid(caseMaterialOid,materialCatalogOid);
        ApiResultSet<QlCaseMaterialAtta> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(qlCaseMaterialAtta);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<Map<String, String>>> saveQlCaseMaterialList(List<QlCaseMaterial> qlCaseMaterialList) {
        List<Map<String, String>> list=qlCaseMaterialAttaManager.saveQlCaseMaterialList(qlCaseMaterialList);
        ApiResultSet<List<Map<String, String>>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(list);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<QlCaseMaterialAtta> queryQlCaseMaterialAttaByCatalogOidAndAttaOid(String caseMaterialOid, String attaOid) {
        QlCaseMaterialAtta qlCaseMaterialAtta = qlCaseMaterialAttaManager.queryQlCaseMaterialAttaByCatalogOidAndAttaOid(caseMaterialOid,attaOid);
        ApiResultSet<QlCaseMaterialAtta> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(qlCaseMaterialAtta);
        return apiResultSet;
    }

    @Override
    public ApiResultSet deleteByOid(String materialAttaOid) {
        qlCaseMaterialAttaManager.deleteByOid(materialAttaOid);
        return null;
    }

    @Override
    public ApiResultSet<List<QlCaseMaterialAtta>> queryQlCaseMaterialAttaByRefinedMaterialOid(String refinedMaterialOid,String caseMaterialOid) {
        List<QlCaseMaterialAtta> list = qlCaseMaterialAttaManager.queryQlCaseMaterialAttaByRefinedMaterialOid(refinedMaterialOid,caseMaterialOid);
        ApiResultSet<List<QlCaseMaterialAtta>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(list);
        return apiResultSet;
    }

    @Override
    public ApiResultSet saveOrUpdateCaseMaterialAttaList(List<QlCaseMaterial> qlCaseMaterialList)  {
        Map<String, Object> modelMap=new HashMap<>();
        ApiResultSet apiResultSet=new ApiResultSet();
        try {
            List<QlCaseMaterialAtta> list=qlCaseMaterialAttaManager.saveOrUpdateCaseMaterialAttaList(qlCaseMaterialList);
            modelMap.put("qlCaseMaterialAttaList",list);
            modelMap.put("success", true);
            modelMap.put("message", "保存材料成功");
            apiResultSet.setData(modelMap);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("message", "保存材料失败");
            apiResultSet.setData(modelMap);
        }
        return apiResultSet;
    }

    @Override
    public ApiResultSet updateCaseMaterialAttaList(List<QlCaseMaterialAtta> qlCaseMaterialAttaList) {
        Map<String, Object> modelMap=new HashMap<>();
        ApiResultSet apiResultSet=new ApiResultSet();
        try {
            List<QlCaseMaterialAtta> list=qlCaseMaterialAttaManager.updateCaseMaterialAttaList(qlCaseMaterialAttaList);
            modelMap.put("qlCaseMaterialAttaList",list);
            modelMap.put("success", true);
            modelMap.put("message", "保存材料成功");
            apiResultSet.setData(modelMap);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("message", "保存材料失败");
            apiResultSet.setData(modelMap);
        }
        return apiResultSet;
    }

    @Override
    public ApiResultSet saveOrUpdateCaseMaterialAttaListForBrowser(List<QlCaseMaterial> qlCaseMaterialList) {
        ApiResultSet apiResultSet = new ApiResultSet();
        List<QlCaseMaterialAtta> list = qlCaseMaterialAttaManager.saveOrUpdateCaseMaterialAttaListForBrowser(qlCaseMaterialList);
        apiResultSet.setData(list);
        return apiResultSet;
    }

}
