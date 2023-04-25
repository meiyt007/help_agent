package com.zfsoft.cases.manager;

import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.data.QlCaseMaterialAtta;
import com.zfsoft.cases.dbaccess.dao.QlCaseMaterialAttaMapper;
import com.zfsoft.cases.dbaccess.dao.QlCaseMaterialMapper;
import com.zfsoft.cases.dbaccess.data.DbQlCaseMaterial;
import com.zfsoft.cases.dbaccess.data.DbQlCaseMaterialAtta;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.cases.util.StringUtils;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceMaterialMapper;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServiceMaterial;
import com.zfsoft.service.manager.sxService.SxServiceMaterialManager;
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
 * @（#）: QlCaseMaterialAttaManager
 * @description: 材料附件信息实现类
 * @author: wangwg
 * @date: 2020/10/24
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "case:qlcasematerialatta")
public class QlCaseMaterialAttaManager {

    @Resource
    private QlCaseMaterialAttaMapper qlCaseMaterialAttaMapper;


    @Resource
    private QlCaseMaterialMapper qlCaseMaterialMapper;

    @Resource
    private DbSxServiceMaterialMapper dbSxServiceMaterialMapper;


    /**
     * 更新修改办件材料附件
     * @param qlCaseMaterialList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public List<QlCaseMaterialAtta> saveOrUpdateCaseMaterialAttaList(List<QlCaseMaterial> qlCaseMaterialList) {
        List<QlCaseMaterialAtta> list=new ArrayList<QlCaseMaterialAtta>();
       // Map<String, Object> map=new HashMap<>();
        List<QlCaseMaterialAtta> qlCaseMaterialAttaList=new ArrayList<>();
        if(qlCaseMaterialList.size()>0){
            for (QlCaseMaterial qlCaseMaterial:qlCaseMaterialList){
                //删除之前的附件关联
                this.deleteByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
                qlCaseMaterialAttaList=qlCaseMaterial.getQlCaseMaterialAttaList();
                if(qlCaseMaterialAttaList.size()>0) {
                    for (QlCaseMaterialAtta qlCaseMaterialAtta : qlCaseMaterialAttaList) {
                        if (StringUtil.isNotEmpty(qlCaseMaterialAtta.getAttaOid())) {
                            if (!"0".equals(qlCaseMaterialAtta.getCaseMaterialOid())) {
                                if (null == qlCaseMaterialAtta.getId()) {
                                    qlCaseMaterialAtta.setId(null);
                                }
                                qlCaseMaterialAtta.setCreateDate(new Date());
                                qlCaseMaterialAtta.setModifyDate(new Date());
                                String materialAttaOid = qlCaseMaterialAtta.getMaterialAttaOid();
                                qlCaseMaterialAtta.setMaterialAttaOid(UUID.randomUUID().toString().replaceAll("-", ""));
                                DbQlCaseMaterialAtta dbQlCaseMaterialAtta = new DbQlCaseMaterialAtta();
                                BeanUtils.copyProperties(qlCaseMaterialAtta, dbQlCaseMaterialAtta);
                                int index = 0;
                                index = qlCaseMaterialAttaMapper.insert(dbQlCaseMaterialAtta);
                               /*if (null == qlCaseMaterialAtta.getId()) {
                                    index = qlCaseMaterialAttaMapper.insert(dbQlCaseMaterialAtta);
                                } else {
                                    index = qlCaseMaterialAttaMapper.update(dbQlCaseMaterialAtta);
                                }*/
                                //保存成功
                                if (index > 0) {
                                    list.add(qlCaseMaterialAtta);
                                }
                            }
                        }
                    }
                    DbQlCaseMaterial dbQlCaseMaterial = qlCaseMaterialMapper.queryMaterialByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
                    DbSxServiceMaterial sxServiceMaterial = dbSxServiceMaterialMapper.getSxServiceMaterialByOid(qlCaseMaterial.getMaterialOid());
                    String materialType = "";
                    if (sxServiceMaterial != null && null != sxServiceMaterial.getMaterialType()) {
                        materialType = sxServiceMaterial.getMaterialType().toString();
                    }
                    dbQlCaseMaterial.setMaterialType(materialType);
                    qlCaseMaterialMapper.update(dbQlCaseMaterial);
                }
            }
        }
        return list;
    }





    /***
    * @Description:更新办件附件用于补正
    * @Author:liangss
    * @Date:2021/8/9
    * @Param: [qlCaseMaterialAttaList]
    */
    @Transactional(rollbackFor = Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public List<QlCaseMaterialAtta> updateCaseMaterialAttaList(List<QlCaseMaterialAtta> qlCaseMaterialAttaList) {
        String caseMaterialOid="";
                //删除之前的附件关联
   /*             this.deleteByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
                qlCaseMaterialAttaList=qlCaseMaterial.getQlCaseMaterialAttaList();*/
                if(qlCaseMaterialAttaList.size()>0){
                    for (QlCaseMaterialAtta qlCaseMaterialAtta:qlCaseMaterialAttaList) {
                        caseMaterialOid=qlCaseMaterialAtta.getCaseMaterialOid();
                        if(!"0".equals(qlCaseMaterialAtta.getCaseMaterialOid())) {
                            if (null == qlCaseMaterialAtta.getId()) {
                                qlCaseMaterialAtta.setId(null);
                                qlCaseMaterialAtta.setCreateDate(new Date());
                            }
                            String materialAttaOid=qlCaseMaterialAtta.getMaterialAttaOid();
                            if (StringUtils.isEmpty(materialAttaOid)||materialAttaOid.equals("")) {
                                qlCaseMaterialAtta.setMaterialAttaOid(UUID.randomUUID().toString().replaceAll("-",""));
                            }
                            DbQlCaseMaterialAtta dbQlCaseMaterialAtta = new DbQlCaseMaterialAtta();
                            BeanUtils.copyProperties(qlCaseMaterialAtta, dbQlCaseMaterialAtta);
                            int index = 0;
                           // index = qlCaseMaterialAttaMapper.insert(dbQlCaseMaterialAtta);
                            if (null == qlCaseMaterialAtta.getId()) {
                                index = qlCaseMaterialAttaMapper.insert(dbQlCaseMaterialAtta);
                            } else {
                                index = qlCaseMaterialAttaMapper.update(dbQlCaseMaterialAtta);
                            }

                        }
                    }
                    DbQlCaseMaterial dbQlCaseMaterial = qlCaseMaterialMapper.queryMaterialByCaseMaterialOid(caseMaterialOid);
                   /* dbQlCaseMaterial.setCollectionType("3");*/
                  /*  dbQlCaseMaterial.setCollectionFlag(1);
                    dbQlCaseMaterial.setCollectionDate(new Date());*/
                    qlCaseMaterialMapper.update(dbQlCaseMaterial);

                }


        return qlCaseMaterialAttaList;
    }


    @Transactional(rollbackFor = Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public List<Map<String, String>> saveQlCaseMaterialList(List<QlCaseMaterial> qlCaseMaterialList) {
        List<Map<String, String>> list=new ArrayList<Map<String, String>>();
        Map<String, String> map=null;
        List<QlCaseMaterialAtta> qlCaseMaterialAttaList=new ArrayList<>();
        if(qlCaseMaterialList.size()>0){
            for (QlCaseMaterial qlCaseMaterial:qlCaseMaterialList){
                qlCaseMaterialAttaList=qlCaseMaterial.getQlCaseMaterialAttaList();
                if(qlCaseMaterialAttaList.size()>0){
                    //删除之前的附件关联
                    this.deleteByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
                    for (QlCaseMaterialAtta qlCaseMaterialAtta:qlCaseMaterialAttaList) {
                        if(!"0".equals(qlCaseMaterialAtta.getCaseMaterialOid())) {
                            if (null == qlCaseMaterialAtta.getId()) {
                                qlCaseMaterialAtta.setId(null);
                                qlCaseMaterialAtta.setCreateDate(new Date());
                           }
                            String materialAttaOid=qlCaseMaterialAtta.getMaterialAttaOid();
                            if (StringUtils.isNotEmpty(materialAttaOid)||materialAttaOid.equals("")) {
                                qlCaseMaterialAtta.setMaterialAttaOid(UUID.randomUUID().toString().replaceAll("-",""));
                            }
                            DbQlCaseMaterialAtta dbQlCaseMaterialAtta = new DbQlCaseMaterialAtta();
                            BeanUtils.copyProperties(qlCaseMaterialAtta, dbQlCaseMaterialAtta);
                            int index = 0;
                           if (null == qlCaseMaterialAtta.getId()) {
                                index = qlCaseMaterialAttaMapper.insert(dbQlCaseMaterialAtta);
                            } else {
                                index = qlCaseMaterialAttaMapper.update(dbQlCaseMaterialAtta);
                            }
                            //保存成功
                            if(index > 0){
                                //修改为只返回目录主键不为空的，防止材料智审无目录选择的时候展示不出来
                                if(StringUtil.isNotEmpty(qlCaseMaterialAtta.getMaterialCatalogOid())){
                                    map=new HashMap<String, String>();
                                    map.put("materialAttaOid",qlCaseMaterialAtta.getMaterialAttaOid());
                                    map.put("caseMaterialOid",qlCaseMaterialAtta.getCaseMaterialOid());
                                    map.put("materialCatalogOid",qlCaseMaterialAtta.getMaterialCatalogOid());
                                    list.add(map);
                                }
                            }
                        }
                    }
                    DbQlCaseMaterial dbQlCaseMaterial = qlCaseMaterialMapper.queryMaterialByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
                    qlCaseMaterial.setCollectionFlag(1);
                    qlCaseMaterial.setCollectionType("3");
                    qlCaseMaterialMapper.update(dbQlCaseMaterial);

                }



            }

        }
       return list;
    }



    @Transactional(rollbackFor = Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public List<Map<String, String>> saveBatchQlCaseMaterialAtta(List<QlCaseMaterialAtta> qlCaseMaterialAttaList) {
        List<Map<String, String>> list=new ArrayList<Map<String, String>>();
        Map<String, String> map=null;
        if(qlCaseMaterialAttaList.size() > 0){
            for (QlCaseMaterialAtta qlCaseMaterialAtta:qlCaseMaterialAttaList) {
                if(!"0".equals(qlCaseMaterialAtta.getCaseMaterialOid())) {
                    if (null == qlCaseMaterialAtta.getId()) {
                        qlCaseMaterialAtta.setId(null);
                        qlCaseMaterialAtta.setCreateDate(new Date());
                        qlCaseMaterialAtta.setModifyDate(new Date());
                    }
                    if (StringUtil.isEmpty(qlCaseMaterialAtta.getMaterialAttaOid())) {
                        qlCaseMaterialAtta.setMaterialAttaOid(UUID.randomUUID().toString().replaceAll("-",""));
                    }
                    DbQlCaseMaterialAtta dbQlCaseMaterialAtta = new DbQlCaseMaterialAtta();
                    BeanUtils.copyProperties(qlCaseMaterialAtta, dbQlCaseMaterialAtta);
                    int index = 0;
                    if (null == qlCaseMaterialAtta.getId()) {
                        index = qlCaseMaterialAttaMapper.insert(dbQlCaseMaterialAtta);
                    } else {
                        dbQlCaseMaterialAtta.setModifyDate(new Date());
                        index = qlCaseMaterialAttaMapper.update(dbQlCaseMaterialAtta);
                    }
                    //保存成功
                    if(index > 0){
                        //修改为只返回目录主键不为空的，防止材料智审无目录选择的时候展示不出来
                        if(StringUtil.isNotEmpty(qlCaseMaterialAtta.getMaterialCatalogOid())){
                            map=new HashMap<String, String>();
                            map.put("materialAttaOid",qlCaseMaterialAtta.getMaterialAttaOid());
                            map.put("caseMaterialOid",qlCaseMaterialAtta.getCaseMaterialOid());
                            map.put("materialCatalogOid",qlCaseMaterialAtta.getMaterialCatalogOid());
                            list.add(map);
                        }
                    }
                }
            }
        }
        return list;
    }

    public List<QlCaseMaterialAtta> queryQlCaseMaterialAttaByCaseMaterialOid(String caseMaterialOid) {
        if(StringUtil.isEmpty(caseMaterialOid)){
            throw new ResultInfoException("办件材料业务主键为空！");
        }
        List<DbQlCaseMaterialAtta> dbQlCaseMaterialAttas = qlCaseMaterialAttaMapper.queryQlCaseMaterialAttaByCaseMaterialOid(caseMaterialOid);
        List<QlCaseMaterialAtta> qlCaseMaterialAttaList = dbQlCaseMaterialAttas.stream().map(dbQlCaseMaterialAtta -> {
            QlCaseMaterialAtta materialAtta = new QlCaseMaterialAtta();
            BeanUtils.copyProperties(dbQlCaseMaterialAtta,materialAtta);
            return materialAtta;
        }).collect(Collectors.toList());
        return qlCaseMaterialAttaList;
    }

    /**
     * 删除办件材料信息
     * @param caseMaterialOid
     */
    public void  deleteByCaseMaterialOid(String caseMaterialOid) {
        qlCaseMaterialAttaMapper.deleteByCaseMaterialOid(caseMaterialOid);
    }

    public QlCaseMaterialAtta queryQlCaseMaterialAttaByOid(String materialAttaOid) {
        DbQlCaseMaterialAtta dbQlCaseMaterialAtta = qlCaseMaterialAttaMapper.queryQlCaseMaterialByOid(materialAttaOid);
        QlCaseMaterialAtta materialAtta =null;
        if(dbQlCaseMaterialAtta !=null){
            materialAtta = new QlCaseMaterialAtta();
            BeanUtils.copyProperties(dbQlCaseMaterialAtta,materialAtta);
        }
        return materialAtta;
    }

    /**
     * 根据id查询一条数据
     * @param attaOid
     * @param caseMaterialOid
     * @return
     */
    public QlCaseMaterialAtta getSelectoneByCaseMaterialOid(String attaOid, String caseMaterialOid) {
        DbQlCaseMaterialAtta dbQlCaseMaterialAtta = qlCaseMaterialAttaMapper.getSelectoneByCaseMaterialOid(attaOid,caseMaterialOid);
        QlCaseMaterialAtta materialAtta =null;
        if(dbQlCaseMaterialAtta !=null){
            materialAtta = new QlCaseMaterialAtta();
            BeanUtils.copyProperties(dbQlCaseMaterialAtta,materialAtta);
        }
        return materialAtta;
    }

    /**
     * 根据目录材料查询一条数据
     * @param caseMaterialOid
     * @param materialCatalogOid
     * @return
     */
    public QlCaseMaterialAtta queryQlCaseMaterialAttaByCatalogOid(String caseMaterialOid, String materialCatalogOid) {
        DbQlCaseMaterialAtta dbQlCaseMaterialAtta = qlCaseMaterialAttaMapper.queryQlCaseMaterialAttaByCatalogOid(caseMaterialOid,materialCatalogOid);
        QlCaseMaterialAtta materialAtta =null;
        if(dbQlCaseMaterialAtta !=null){
            materialAtta = new QlCaseMaterialAtta();
            BeanUtils.copyProperties(dbQlCaseMaterialAtta,materialAtta);
        }
        return materialAtta;
    }

    /**
     * 根据材料id和附件id查询附件关联表
     * @param caseMaterialOid
     * @param attaOid
     * @return
     */
    public QlCaseMaterialAtta queryQlCaseMaterialAttaByCatalogOidAndAttaOid(String caseMaterialOid, String attaOid) {
        DbQlCaseMaterialAtta dbQlCaseMaterialAtta = qlCaseMaterialAttaMapper.queryQlCaseMaterialAttaByCatalogOidAndAttaOid(caseMaterialOid,attaOid);
        QlCaseMaterialAtta materialAtta =null;
        if(dbQlCaseMaterialAtta !=null){
            materialAtta = new QlCaseMaterialAtta();
            BeanUtils.copyProperties(dbQlCaseMaterialAtta,materialAtta);
        }
        return materialAtta;
    }


    /**
     * 根据业务主键删除
     * @param materialAttaOid
     */
    public void  deleteByOid(String materialAttaOid) {
        qlCaseMaterialAttaMapper.deleteByOid(materialAttaOid);
    }


    /**
     *根据精细化材料oid查询办件关联列表
     * @param refinedMaterialOid
     * @return
     */
    public List<QlCaseMaterialAtta> queryQlCaseMaterialAttaByRefinedMaterialOid(String refinedMaterialOid,String caseMaterialOid) {
        if(StringUtil.isEmpty(refinedMaterialOid)){
            throw new ResultInfoException("办件材料业务主键为空！");
        }
        List<DbQlCaseMaterialAtta> dbQlCaseMaterialAttas = qlCaseMaterialAttaMapper.queryQlCaseMaterialAttaByRefinedMaterialOid(refinedMaterialOid,caseMaterialOid);
        List<QlCaseMaterialAtta> qlCaseMaterialAttaList = dbQlCaseMaterialAttas.stream().map(dbQlCaseMaterialAtta -> {
            QlCaseMaterialAtta materialAtta = new QlCaseMaterialAtta();
            BeanUtils.copyProperties(dbQlCaseMaterialAtta,materialAtta);
            return materialAtta;
        }).collect(Collectors.toList());
        return qlCaseMaterialAttaList;
    }


    /**
     * @description 网站端更新材料附件
     * @param qlCaseMaterialList
     * @author meiyt
     * @date 2022/6/7
     * @return
     **/
    @Transactional(rollbackFor = Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<QlCaseMaterialAtta> saveOrUpdateCaseMaterialAttaListForBrowser(List<QlCaseMaterial> qlCaseMaterialList) {
        List<QlCaseMaterialAtta> list=new ArrayList<QlCaseMaterialAtta>();
        List<QlCaseMaterialAtta> qlCaseMaterialAttaList = new ArrayList<>();
        if(qlCaseMaterialList.size()>0){
            for (QlCaseMaterial qlCaseMaterial : qlCaseMaterialList){
                //删除之前的附件关联
                this.deleteByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
                qlCaseMaterialAttaList = qlCaseMaterial.getQlCaseMaterialAttaList();
                if(null == qlCaseMaterialAttaList || qlCaseMaterialAttaList.size() == 0) {
                    continue;
                }
                for (QlCaseMaterialAtta qlCaseMaterialAtta : qlCaseMaterialAttaList) {
                    if (StringUtil.isNotEmpty(qlCaseMaterialAtta.getAttaOid())) {
                        if (!"0".equals(qlCaseMaterialAtta.getCaseMaterialOid())) {
                            if (null == qlCaseMaterialAtta.getId()) {
                                qlCaseMaterialAtta.setId(null);
                            }
                            qlCaseMaterialAtta.setCreateDate(new Date());
                            qlCaseMaterialAtta.setModifyDate(new Date());
                            qlCaseMaterialAtta.setMaterialAttaOid(UUID.randomUUID().toString().replaceAll("-", ""));
                            DbQlCaseMaterialAtta dbQlCaseMaterialAtta = new DbQlCaseMaterialAtta();
                            BeanUtils.copyProperties(qlCaseMaterialAtta, dbQlCaseMaterialAtta);
                            int index = 0;
                            index = qlCaseMaterialAttaMapper.insert(dbQlCaseMaterialAtta);
                            //保存成功
                            if (index > 0) {
                                list.add(qlCaseMaterialAtta);
                            }
                        }
                    }
                }
                DbQlCaseMaterial dbQlCaseMaterial = qlCaseMaterialMapper.queryMaterialByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
                if(null == dbQlCaseMaterial) {
                    continue;
                }
                dbQlCaseMaterial.setCollectionFlag(1);
                dbQlCaseMaterial.setCollectionType(qlCaseMaterial.getCollectionType());
                dbQlCaseMaterial.setCollectionDate(new Date());
                dbQlCaseMaterial.setConfirmStatus(null);
                qlCaseMaterialMapper.update(dbQlCaseMaterial);
            }
        }
        return list;
    }


}
