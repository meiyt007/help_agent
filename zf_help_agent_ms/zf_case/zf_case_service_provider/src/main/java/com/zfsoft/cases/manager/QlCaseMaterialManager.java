package com.zfsoft.cases.manager;

import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.dbaccess.dao.QlCaseMaterialMapper;
import com.zfsoft.cases.dbaccess.data.DbQlCaseMaterial;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.sxService.data.SxServiceMaterial;
import com.zfsoft.service.sxService.service.SxServiceMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @（#）: QlCaseMaterialManager
 * @description: 办件材料信息实现类
 * @author: wangwg
 * @date: 2020/10/24
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "case:material")
public class QlCaseMaterialManager {

    @Resource
    private QlCaseMaterialMapper qlCaseMaterialMapper;

    @Resource
    private SxServiceMaterialService sxServiceMaterialFeignService;

    @Transactional(rollbackFor = Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<Map<String, String>> saveBatchQlCaseMaterial(List<QlCaseMaterial> qlCaseMaterialList) {
        List<Map<String, String>> list  = null;
         if(qlCaseMaterialList.size() > 0){
             list  = new ArrayList<Map<String, String>>();
            for (QlCaseMaterial qlCaseMaterial:qlCaseMaterialList) {
                if(!"0".equals(qlCaseMaterial.getCaseOid())) {
                    if (null == qlCaseMaterial.getId()) {
                        qlCaseMaterial.setId(null);
                        qlCaseMaterial.setCreateDate(new Date());
                    }
                    if(StringUtil.isEmpty(qlCaseMaterial.getCaseMaterialOid())){
                        qlCaseMaterial.setCaseMaterialOid(UUID.randomUUID().toString().replaceAll("-",""));
                    }
                    qlCaseMaterial.setDelFlag(0);
                    DbQlCaseMaterial dbQlCaseMaterial = new DbQlCaseMaterial();
                    BeanUtils.copyProperties(qlCaseMaterial, dbQlCaseMaterial);
                    int index = 0;
                    if (null == qlCaseMaterial.getId()) {
                        index = qlCaseMaterialMapper.insert(dbQlCaseMaterial);
                    } else {
                        dbQlCaseMaterial.setModifyDate(new Date());
                        index = qlCaseMaterialMapper.update(dbQlCaseMaterial);
                    }
                    //保存成功
                    if(index > 0){
                        Map<String, String> map=new HashMap<String, String>();
                        map.put("caseMaterialOid",qlCaseMaterial.getCaseMaterialOid());
                        map.put("materialOid",qlCaseMaterial.getMaterialOid());
                        list.add(map);
                    }
                }
            }
        }
        return list;
    }

    public List<QlCaseMaterial> queryQlCaseMaterialByCaseOid(String caseOid) {
        if(StringUtil.isEmpty(caseOid)){
            throw new ResultInfoException("办件业务编码为空！");
        }
        List<DbQlCaseMaterial> dbQlCaseMaterials = qlCaseMaterialMapper.queryQlCaseLinkResultListByCaseOid(caseOid);
        List<QlCaseMaterial> qlCaseMaterialList = dbQlCaseMaterials.stream().map(dbQlCaseMaterial -> {
            QlCaseMaterial material = new QlCaseMaterial();
            BeanUtils.copyProperties(dbQlCaseMaterial,material);
            return material;
        }).collect(Collectors.toList());
        return qlCaseMaterialList;
    }

    public List<QlCaseMaterial> queryRqhbMaterialByCaseOid(String caseOid) {
        if(StringUtil.isEmpty(caseOid)){
            throw new ResultInfoException("办件业务编码为空！");
        }
        List<DbQlCaseMaterial> dbQlCaseMaterials = qlCaseMaterialMapper.queryRqhbMaterialByCaseOid(caseOid);
        List<QlCaseMaterial> qlCaseMaterialList = dbQlCaseMaterials.stream().map(dbQlCaseMaterial -> {
            QlCaseMaterial material = new QlCaseMaterial();
            BeanUtils.copyProperties(dbQlCaseMaterial,material);
            return material;
        }).collect(Collectors.toList());
        return qlCaseMaterialList;
    }

    public QlCaseMaterial queryQlCaseMaterialByOid(String caseOid, String materialOid) {
        if (StringUtil.isEmpty(caseOid)) {
            throw new ResultInfoException("办件业务编码为空！");
        }
        if (StringUtil.isEmpty(materialOid)) {
            throw new ResultInfoException("办件所属材料业务编码为空！");
        }
        QlCaseMaterial material=null;
        DbQlCaseMaterial dbQlCaseMaterial = qlCaseMaterialMapper.queryQlCaseMaterialByOid(caseOid, materialOid);
        if(dbQlCaseMaterial !=null){
            material = new QlCaseMaterial();
            BeanUtils.copyProperties(dbQlCaseMaterial,material);
        }
        return material;
    }

    public QlCaseMaterial queryMaterialByCaseMaterialOid(String caseMaterialOid) {
        if(StringUtil.isEmpty(caseMaterialOid)){
            throw new ResultInfoException("办件业务编码为空！");
        }
        DbQlCaseMaterial dbQlCaseMaterial = qlCaseMaterialMapper.queryMaterialByCaseMaterialOid(caseMaterialOid);
        QlCaseMaterial material = null;
        if(dbQlCaseMaterial != null){
            material = new QlCaseMaterial();
            BeanUtils.copyProperties(dbQlCaseMaterial,material);
        }
        return material;
    }

    @Transactional(rollbackFor = Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteQlCaseMaterial(Long oid) {
        int index = qlCaseMaterialMapper.deleteById(oid);
        if(index==0){
            throw new ResultInfoException("参数删除失败，请稍后再试！");
        }
        return index;
    }

    public List<QlCaseMaterial> queryMaterialListByMaterialOid(String materialOid, String caseOid) {
        if(StringUtil.isEmpty(materialOid)){
            throw new ResultInfoException("材料业务编码为空！");
        }
        List<DbQlCaseMaterial> dbQlCaseMaterials = qlCaseMaterialMapper.queryMaterialListByMaterialOid(materialOid,caseOid);
        List<QlCaseMaterial> qlCaseMaterialList = dbQlCaseMaterials.stream().map(dbQlCaseMaterial -> {
            QlCaseMaterial material = new QlCaseMaterial();
            BeanUtils.copyProperties(dbQlCaseMaterial,material);
            return material;
        }).collect(Collectors.toList());
        return qlCaseMaterialList;
    }



    @Transactional(rollbackFor = Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<Map<String, String>> updateQlCaseMaterialList(List<QlCaseMaterial> qlCaseMaterialList) {
        List<Map<String, String>> list  = null;
        if(qlCaseMaterialList.size() > 0){
            list  = new ArrayList<Map<String, String>>();
            for (QlCaseMaterial qlCaseMaterial:qlCaseMaterialList) {
                if(!"0".equals(qlCaseMaterial.getCaseOid())) {
                    if (null != qlCaseMaterial.getId()) {
                        DbQlCaseMaterial dbQlCaseMaterial = qlCaseMaterialMapper.queryMaterialByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
                        if (dbQlCaseMaterial !=null) {
                            if (qlCaseMaterial.getCollectionType().equals("1")) {//纸质收取
                                dbQlCaseMaterial.setCollectionFlag(1);//已收取
                                dbQlCaseMaterial.setCollectionDate(new Date());
                            } else if (qlCaseMaterial.getCollectionType().equals("5")) {//证照
                                dbQlCaseMaterial.setCollectionFlag(1);//已收取
                                dbQlCaseMaterial.setCollectionDate(new Date());
                                /*qlCaseMaterial.getElecBillOid()*/
                                dbQlCaseMaterial.setElecBillOid(qlCaseMaterial.getElecBillOid());
                                dbQlCaseMaterial.setElemLicenseOid(qlCaseMaterial.getElemLicenseOid());
                                dbQlCaseMaterial.setElemNumber(qlCaseMaterial.getElemNumber());
                                dbQlCaseMaterial.setElecLicenName(qlCaseMaterial.getElecLicenName());
                                dbQlCaseMaterial.setElecLicenNumber(qlCaseMaterial.getElecLicenNumber());
                            } else if (qlCaseMaterial.getCollectionType().equals("4")) {//容缺
                                dbQlCaseMaterial.setCollectionFlag(0);//已收取
                                dbQlCaseMaterial.setCollectionDate(null);
                            } else if (qlCaseMaterial.getCollectionType().equals("2")) {//附件上传
                                if (qlCaseMaterial.getAttaList() != null && qlCaseMaterial.getAttaList().size() > 0) {
                                    dbQlCaseMaterial.setCollectionFlag(1);
                                } else {
                                    dbQlCaseMaterial.setCollectionFlag(0);
                                }
                            } else {
                                dbQlCaseMaterial.setCollectionFlag(qlCaseMaterial.getCollectionFlag());
                            }
                            dbQlCaseMaterial.setCollectionType(qlCaseMaterial.getCollectionType());
                            dbQlCaseMaterial.setCollectionNumber(qlCaseMaterial.getCollectionNumber());
                        /*if(null!=qlCaseMaterial.getMustFlag()){
                            dbQlCaseMaterial.setMustFlag(qlCaseMaterial.getMustFlag());
                        }*/
                            /*BeanUtils.copyProperties(qlCaseMaterial, dbQlCaseMaterial);*/
                            int index = 0;
                            index = qlCaseMaterialMapper.update(dbQlCaseMaterial);
                            //保存成功
                            if(index > 0){
                                Map<String, String> map=new HashMap<String, String>();
                                map.put("caseMaterialOid",qlCaseMaterial.getCaseMaterialOid());
                                map.put("materialOid",qlCaseMaterial.getMaterialOid());
                                list.add(map);
                            }
                        }
                    }
                }
            }
        }
        return list;
    }


    /**
    * @Description:  更新办件材料
    * @Author:liangss
    * @Date:2021/7/28
    * @Param: [qlCaseMaterial]
    */
    @Transactional(rollbackFor = Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public QlCaseMaterial updateQlCaseMaterial(QlCaseMaterial qlCaseMaterial){
        if(null!=qlCaseMaterial){
            DbQlCaseMaterial dbQlCaseMaterial = new DbQlCaseMaterial();
            BeanUtils.copyProperties(qlCaseMaterial, dbQlCaseMaterial);
            qlCaseMaterialMapper.update(dbQlCaseMaterial);
        }
        return  qlCaseMaterial;
    }

    public List<QlCaseMaterial> queryCngzMaterialByCaseOid(String caseOid) {
        if(StringUtil.isEmpty(caseOid)){
            throw new ResultInfoException("办件业务编码为空！");
        }
        List<DbQlCaseMaterial> dbQlCaseMaterials = qlCaseMaterialMapper.queryCngzMaterialByCaseOid(caseOid);
        List<QlCaseMaterial> qlCaseMaterialList = dbQlCaseMaterials.stream().map(dbQlCaseMaterial -> {
            QlCaseMaterial material = new QlCaseMaterial();
            BeanUtils.copyProperties(dbQlCaseMaterial,material);
            return material;
        }).collect(Collectors.toList());
        return qlCaseMaterialList;
    }

    public List<QlCaseMaterial> queryMaterialByCaseOidNotAttaFile(String caseOid) {
        if(StringUtil.isEmpty(caseOid)){
            throw new ResultInfoException("办件业务编码为空！");
        }
        List<DbQlCaseMaterial> dbQlCaseMaterials = qlCaseMaterialMapper.queryMaterialByCaseOidNotAttaFile(caseOid);
        List<QlCaseMaterial> qlCaseMaterialList = dbQlCaseMaterials.stream().map(dbQlCaseMaterial -> {
            QlCaseMaterial material = new QlCaseMaterial();
            BeanUtils.copyProperties(dbQlCaseMaterial,material);
            return material;
        }).collect(Collectors.toList());
        return qlCaseMaterialList;
    }

    public List<QlCaseMaterial> queryQlCaseMaterialByBillOid(String caseOid, String billOid) {
        List<DbQlCaseMaterial> dbQlCaseMaterials = qlCaseMaterialMapper.queryQlCaseMaterialByBillOid(caseOid, billOid);
        List<QlCaseMaterial> qlCaseMaterialList = dbQlCaseMaterials.stream().map(dbQlCaseMaterial -> {
            QlCaseMaterial material = new QlCaseMaterial();
            BeanUtils.copyProperties(dbQlCaseMaterial,material);
            return material;
        }).collect(Collectors.toList());
        return qlCaseMaterialList;
    }


    @Transactional(rollbackFor = Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<Map<String, String>> updateQlCaseMaterialListForBrowser(List<QlCaseMaterial> qlCaseMaterialList) {
        List<Map<String, String>> list  = null;
        if(qlCaseMaterialList.size() > 0){
            list = new ArrayList<Map<String, String>>();
            for (QlCaseMaterial qlCaseMaterial : qlCaseMaterialList) {
                if(!"0".equals(qlCaseMaterial.getCaseOid())) {
                    if (null != qlCaseMaterial.getId()) {
                        DbQlCaseMaterial dbQlCaseMaterial = qlCaseMaterialMapper.queryMaterialByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
                        if (dbQlCaseMaterial !=null) {
                            if (qlCaseMaterial.getCollectionType().equals("1")) {//纸质收取
                                dbQlCaseMaterial.setCollectionFlag(1);//已收取
                                dbQlCaseMaterial.setCollectionDate(new Date());
                            } else if (qlCaseMaterial.getCollectionType().equals("5")) {//证照
                                dbQlCaseMaterial.setCollectionFlag(1);//已收取
                                dbQlCaseMaterial.setCollectionDate(new Date());
                                dbQlCaseMaterial.setElecBillOid(qlCaseMaterial.getElecBillOid());
                                dbQlCaseMaterial.setElemLicenseOid(qlCaseMaterial.getElemLicenseOid());
                                dbQlCaseMaterial.setElemNumber(qlCaseMaterial.getElemNumber());
                                dbQlCaseMaterial.setElecLicenName(qlCaseMaterial.getElecLicenName());
                                dbQlCaseMaterial.setElecLicenNumber(qlCaseMaterial.getElecLicenNumber());
                            } else if (qlCaseMaterial.getCollectionType().equals("4")) {//容缺
                                dbQlCaseMaterial.setCollectionFlag(0);//已收取
                                dbQlCaseMaterial.setCollectionDate(null);
                            } else if (qlCaseMaterial.getCollectionType().equals("2")) {//附件上传
                                if (qlCaseMaterial.getAttaList() != null && qlCaseMaterial.getAttaList().size() > 0) {
                                    dbQlCaseMaterial.setCollectionFlag(1);
                                } else {
                                    dbQlCaseMaterial.setCollectionFlag(0);
                                }
                            } else {
                                dbQlCaseMaterial.setCollectionFlag(qlCaseMaterial.getCollectionFlag());
                            }
                            ApiResultSet<SxServiceMaterial> sxServiceMaterialApiResultSet = sxServiceMaterialFeignService.getSxServiceMaterialByOid(qlCaseMaterial.getMaterialOid());
                            String materialType = "";
                            if (null != sxServiceMaterialApiResultSet.getData().getMaterialType()) {
                                materialType = sxServiceMaterialApiResultSet.getData().getMaterialType().toString();
                            }
                            dbQlCaseMaterial.setMaterialType(materialType);
                            dbQlCaseMaterial.setCollectionType(qlCaseMaterial.getCollectionType());
                            dbQlCaseMaterial.setCollectionNumber(qlCaseMaterial.getCollectionNumber());
                            int index = 0;
                            index = qlCaseMaterialMapper.update(dbQlCaseMaterial);
                            //保存成功
                            if(index > 0){
                                Map<String, String> map=new HashMap<String, String>();
                                map.put("caseMaterialOid",qlCaseMaterial.getCaseMaterialOid());
                                map.put("materialOid",qlCaseMaterial.getMaterialOid());
                                list.add(map);
                            }
                        }
                    }
                }
            }
        }
        return list;
    }
}
