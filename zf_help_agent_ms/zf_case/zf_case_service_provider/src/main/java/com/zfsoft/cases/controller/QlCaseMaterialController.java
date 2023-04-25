package com.zfsoft.cases.controller;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.data.QlCaseMaterialAtta;
import com.zfsoft.cases.data.QlSysAtta;
import com.zfsoft.cases.manager.QlCaseMaterialAttaManager;
import com.zfsoft.cases.manager.QlCaseMaterialManager;
import com.zfsoft.cases.manager.SysAttaManager;
import com.zfsoft.cases.service.QlCaseMaterialService;
import com.zfsoft.cases.util.BaseStaticParameter;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.dbaccess.dao.sxSys.DbSxSysAttaMapper;
import com.zfsoft.service.dbaccess.data.sxSys.DbSxSysAtta;
import com.zfsoft.service.sxService.data.SxServiceMaterial;
import com.zfsoft.service.sxService.service.SxServiceMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * @（#）: QlCaseMaterialController
 * @description: 办件材料信息实现类
 * @author: wangwg
 * @date: 2020/10/24
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
public class QlCaseMaterialController implements QlCaseMaterialService {

    @Resource
    private QlCaseMaterialManager qlCaseMaterialManager;

    @Resource
    private QlCaseMaterialAttaManager qlCaseMaterialAttaManager;

    @Resource
    private SysAttaManager sysAttaManager;

    @Resource
    private SxServiceMaterialService sxServiceMaterialFeignService;

    @Resource
    private DbSxSysAttaMapper dbSxSysAttaMapper;
    @Override
    public ApiResultSet<List<Map<String, String>>> saveQlCaseMaterial(List<QlCaseMaterial> qlCaseMaterialList) {
        List<Map<String, String>> list=qlCaseMaterialManager.saveBatchQlCaseMaterial(qlCaseMaterialList);
        ApiResultSet<List<Map<String, String>>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(list);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<QlCaseMaterial>> queryQlCaseMaterialByCaseOid(String caseOid) {
        List<QlCaseMaterial> list = qlCaseMaterialManager.queryQlCaseMaterialByCaseOid(caseOid);
        List<QlCaseMaterial> qlCaseMaterials = new LinkedList<QlCaseMaterial>() ;
        List attas =null;
        if(list != null){
            for (QlCaseMaterial qlCaseMaterial:list) {
                //判断如果是附件上传且收取查询附件
                //if(qlCaseMaterial.getCollectionFlag()==1&&(qlCaseMaterial.getCollectionType().equals("2")||qlCaseMaterial.getCollectionType().equals("3"))){
                if(StringUtil.isNotEmpty(qlCaseMaterial.getCollectionType())){
                    if(StringUtil.isNotEmpty(qlCaseMaterial.getCollectionType())&&(qlCaseMaterial.getCollectionType().equals("2")||qlCaseMaterial.getCollectionType().equals("3"))){
                        attas = new LinkedList();
                        List<QlCaseMaterialAtta> materialAttaList = qlCaseMaterialAttaManager.queryQlCaseMaterialAttaByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
                        if(materialAttaList != null){
                            for (QlCaseMaterialAtta qlCaseMaterialAtta : materialAttaList) {
                                if(StringUtil.isNotEmpty(qlCaseMaterialAtta.getAttaOid())){
                                    QlSysAtta atta = sysAttaManager.querySysAttaByOid(qlCaseMaterialAtta.getAttaOid());
                                    if(atta !=null){
                                        if(!StringUtil.isEmpty(qlCaseMaterialAtta.getMaterialCatalogOid())){
                                            atta.setCatalogOid(qlCaseMaterialAtta.getMaterialCatalogOid());
                                        }
                                        attas.add(atta);
                                    }
                                }

                            }
                        }
                        qlCaseMaterial.setAttaList(attas);
                    }
                }

                qlCaseMaterials.add(qlCaseMaterial);
            }
        }
        ApiResultSet<List<QlCaseMaterial>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(qlCaseMaterials);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<QlCaseMaterial> getQlCaseMaterialByCaseMaterialOid(String caseMaterialOid) {
        QlCaseMaterial qlCaseMaterial = qlCaseMaterialManager.queryMaterialByCaseMaterialOid(caseMaterialOid);
        List attas =null;
        if(qlCaseMaterial != null){
                //判断如果是附件上传且收取查询附件
                //if(qlCaseMaterial.getCollectionFlag()==1&&(qlCaseMaterial.getCollectionType().equals("2")||qlCaseMaterial.getCollectionType().equals("3"))){
                if(StringUtil.isNotEmpty(qlCaseMaterial.getCollectionType())){
                    if(StringUtil.isNotEmpty(qlCaseMaterial.getCollectionType())&&(qlCaseMaterial.getCollectionType().equals("2")||qlCaseMaterial.getCollectionType().equals("3"))){
                        attas = new LinkedList();
                        List<QlCaseMaterialAtta> materialAttaList = qlCaseMaterialAttaManager.queryQlCaseMaterialAttaByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
                        if(materialAttaList != null){
                            for (QlCaseMaterialAtta qlCaseMaterialAtta : materialAttaList) {
                                if(StringUtil.isNotEmpty(qlCaseMaterialAtta.getAttaOid())){
                                    QlSysAtta atta = sysAttaManager.querySysAttaByOid(qlCaseMaterialAtta.getAttaOid());
                                    if(atta !=null){
                                        if(!StringUtil.isEmpty(qlCaseMaterialAtta.getMaterialCatalogOid())){
                                            atta.setCatalogOid(qlCaseMaterialAtta.getMaterialCatalogOid());
                                        }
                                        attas.add(atta);
                                    }
                                }

                            }
                        }
                        qlCaseMaterial.setAttaList(attas);
                    }
                }
        }
        ApiResultSet<QlCaseMaterial> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(qlCaseMaterial);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Map<String, List<QlCaseMaterial>>> queryQlCaseMaterialListByCaseOid(String caseOid) {
        if(StringUtil.isEmpty(caseOid)) {
            throw new ResultInfoException("办件编号不能为空");
        }
        ApiResultSet<Map<String, List<QlCaseMaterial>>> apiResultSet = new ApiResultSet<>();
        Map<String, List<QlCaseMaterial>> qlCaseMaterialMap = new HashMap<>();
        List<QlCaseMaterial> list = qlCaseMaterialManager.queryQlCaseMaterialByCaseOid(caseOid);
        //
        List<QlCaseMaterial> autoList = new ArrayList<>();
        List<QlCaseMaterial> noSubmitList = new ArrayList<>();
        List<QlCaseMaterial> uploadList = new ArrayList<>();
        //
        for(QlCaseMaterial qlCaseMaterial : list) {
            if(StringUtil.isEmpty(qlCaseMaterial.getMaterialOid())) {
                continue;
            }
            ApiResultSet<SxServiceMaterial> result = sxServiceMaterialFeignService.getSxServiceMaterialByOid(qlCaseMaterial.getMaterialOid());
            if(null == result || null == result.getData()) {
                continue;
            }
            SxServiceMaterial sxServiceMaterial = result.getData();
            if(null == sxServiceMaterial.getMadeMaterialType()) {
                continue;
            }
            List<QlCaseMaterialAtta> qlCaseMaterialAttas = qlCaseMaterialAttaManager.queryQlCaseMaterialAttaByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
            for(QlCaseMaterialAtta qlCaseMaterialAtta : qlCaseMaterialAttas) {
                if(StrUtil.isNotEmpty(qlCaseMaterialAtta.getAttaOid())) {
                    QlSysAtta atta = sysAttaManager.querySysAttaByOid(qlCaseMaterialAtta.getAttaOid());
                    qlCaseMaterialAtta.setQlSysAtta(atta);
                }
            }
            qlCaseMaterial.setRoleType(sxServiceMaterial.getRoleType());
            qlCaseMaterial.setMemo(sxServiceMaterial.getMemo());
            //2022 09 20  新增 材料类型，材料形式，材料份数
            qlCaseMaterial.setMaterialType(sxServiceMaterial.getMaterialType());
            qlCaseMaterial.setMaterialFormat(sxServiceMaterial.getMaterialFormat());
            qlCaseMaterial.setPaperNumber(sxServiceMaterial.getPaperNumber());
            qlCaseMaterial.setMaterialSort(sxServiceMaterial.getMaterialSort());
            qlCaseMaterial.setQlCaseMaterialAttaList(qlCaseMaterialAttas);
            if(sxServiceMaterial.getMadeMaterialType() == BaseStaticParameter.MADE_MATERIAL_TYPE_AUTO) {
                //2022 11 25 zhaobf自动生成材料也显示空表样表
                qlCaseMaterial.setMaterialSampleAddrYl(sxServiceMaterial.getMaterialSampleAddrYl());
                qlCaseMaterial.setMaterialSampleAddr(sxServiceMaterial.getMaterialSampleAddr());
                qlCaseMaterial.setMaterialEmptyAttoid(sxServiceMaterial.getMaterialEmptyAddr());
                DbSxSysAtta dbSxSysAtta = dbSxSysAttaMapper.getDbSxSysAttaByOid(sxServiceMaterial.getMaterialEmptyAddr());
                if(dbSxSysAtta !=null && StringUtil.isNotEmpty(dbSxSysAtta.getFilePath())){
                    qlCaseMaterial.setMaterialEmptyAddrUrl(dbSxSysAtta.getFilePath());
                }else{
                    qlCaseMaterial.setMaterialEmptyAddrUrl(null);
                }
                autoList.add(qlCaseMaterial);
            } else if (sxServiceMaterial.getMadeMaterialType() == BaseStaticParameter.MADE_MATERIAL_TYPE_NO_SUBMIT) {
                //免提交材料也显示空表样表
                qlCaseMaterial.setMaterialSampleAddrYl(sxServiceMaterial.getMaterialSampleAddrYl());
                qlCaseMaterial.setMaterialSampleAddr(sxServiceMaterial.getMaterialSampleAddr());
                qlCaseMaterial.setMaterialEmptyAttoid(sxServiceMaterial.getMaterialEmptyAddr());
                DbSxSysAtta dbSxSysAtta = dbSxSysAttaMapper.getDbSxSysAttaByOid(sxServiceMaterial.getMaterialEmptyAddr());
                if(dbSxSysAtta !=null && StringUtil.isNotEmpty(dbSxSysAtta.getFilePath())){
                    qlCaseMaterial.setMaterialEmptyAddrUrl(dbSxSysAtta.getFilePath());
                }else{
                    qlCaseMaterial.setMaterialEmptyAddrUrl(null);
                }
                noSubmitList.add(qlCaseMaterial);
            } else if (sxServiceMaterial.getMadeMaterialType() == BaseStaticParameter.MADE_MATERIAL_TYPE_UPLOAD) {
                qlCaseMaterial.setMaterialSampleAddrYl(sxServiceMaterial.getMaterialSampleAddrYl());
                qlCaseMaterial.setMaterialSampleAddr(sxServiceMaterial.getMaterialSampleAddr());
                qlCaseMaterial.setMaterialEmptyAttoid(sxServiceMaterial.getMaterialEmptyAddr());
                DbSxSysAtta dbSxSysAtta = dbSxSysAttaMapper.getDbSxSysAttaByOid(sxServiceMaterial.getMaterialEmptyAddr());
                if(dbSxSysAtta !=null && StringUtil.isNotEmpty(dbSxSysAtta.getFilePath())){
                    qlCaseMaterial.setMaterialEmptyAddrUrl(dbSxSysAtta.getFilePath());
                }else{
                    qlCaseMaterial.setMaterialEmptyAddrUrl(null);
                }
                uploadList.add(qlCaseMaterial);
            }
        }
        qlCaseMaterialMap.put("autoProduceMaterialList", autoList);
        qlCaseMaterialMap.put("noSubmissionMaterialList", noSubmitList);
        qlCaseMaterialMap.put("needUploadMaterialList", uploadList);
        apiResultSet.setData(qlCaseMaterialMap);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<QlCaseMaterial>> queryRqhbMaterialByCaseOid(String caseOid) {
        List<QlCaseMaterial> list = qlCaseMaterialManager.queryRqhbMaterialByCaseOid(caseOid);
        ApiResultSet<List<QlCaseMaterial>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(list);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<QlCaseMaterial> queryQlCaseMaterialByOid(String caseOid, String materialOid) {
        QlCaseMaterial qlCaseMaterial = qlCaseMaterialManager.queryQlCaseMaterialByOid(caseOid, materialOid);
        ApiResultSet<QlCaseMaterial> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(qlCaseMaterial);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<Map<String, String>>> updateQlCaseMaterialList(List<QlCaseMaterial> qlCaseMaterialList) {
        List<Map<String, String>> list=qlCaseMaterialManager.updateQlCaseMaterialList(qlCaseMaterialList);
        ApiResultSet<List<Map<String, String>>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(list);
        return apiResultSet;
    }

    @Override
    public ApiResultSet updateQlCaseMaterial(QlCaseMaterial QlCaseMaterial) {
        QlCaseMaterial qlCaseMaterialNew=qlCaseMaterialManager.updateQlCaseMaterial(QlCaseMaterial);
        ApiResultSet<QlCaseMaterial> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(qlCaseMaterialNew);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<QlCaseMaterial>> queryCngzMaterialByCaseOid(String caseOid) {
        List<QlCaseMaterial> list = qlCaseMaterialManager.queryCngzMaterialByCaseOid(caseOid);
        ApiResultSet<List<QlCaseMaterial>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(list);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<QlCaseMaterial>> queryMaterialByCaseOidNotAttaFile(String caseOid) {
        List<QlCaseMaterial> list = qlCaseMaterialManager.queryMaterialByCaseOidNotAttaFile(caseOid);
        ApiResultSet<List<QlCaseMaterial>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(list);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<QlCaseMaterial>> queryQlCaseMaterialByBillOid(String caseOid, String billOid) {
        List<QlCaseMaterial> list = qlCaseMaterialManager.queryQlCaseMaterialByBillOid(caseOid, billOid);
        ApiResultSet<List<QlCaseMaterial>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(list);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<QlCaseMaterial> queryMaterialByCaseMaterialOid(String caseMaterialOid) {
        QlCaseMaterial list = qlCaseMaterialManager.queryMaterialByCaseMaterialOid(caseMaterialOid);
        return new ApiResultSet(list);
    }

    @Override
    public ApiResultSet<List<Map<String, String>>> updateQlCaseMaterialListForBrowser(List<QlCaseMaterial> qlCaseMaterialList) {
        List<Map<String, String>> list = qlCaseMaterialManager.updateQlCaseMaterialListForBrowser(qlCaseMaterialList);
        ApiResultSet<List<Map<String, String>>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(list);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Map<String, List<QlCaseMaterial>>> queryQlCaseMaterialListByCaseOidForZC(String caseOid) {
        if(StringUtil.isEmpty(caseOid)) {
            throw new ResultInfoException("办件编号不能为空");
        }
        ApiResultSet<Map<String, List<QlCaseMaterial>>> apiResultSet = new ApiResultSet<>();
        Map<String, List<QlCaseMaterial>> qlCaseMaterialMap = new HashMap<>();
        List<QlCaseMaterial> list = qlCaseMaterialManager.queryQlCaseMaterialByCaseOid(caseOid);
        //
        List<QlCaseMaterial> autoList = new ArrayList<>();
        List<QlCaseMaterial> uploadList = new ArrayList<>();
        //
        for(QlCaseMaterial qlCaseMaterial : list) {
            if(StringUtil.isEmpty(qlCaseMaterial.getMaterialOid())) {
                continue;
            }
            ApiResultSet<SxServiceMaterial> result = sxServiceMaterialFeignService.getSxServiceMaterialByOid(qlCaseMaterial.getMaterialOid());
            if(null == result || null == result.getData()) {
                continue;
            }
            SxServiceMaterial sxServiceMaterial = result.getData();
            if(null == sxServiceMaterial.getMadeMaterialType()) {
                continue;
            }
            List<QlCaseMaterialAtta> qlCaseMaterialAttas = qlCaseMaterialAttaManager.queryQlCaseMaterialAttaByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
            qlCaseMaterial.setRoleType(sxServiceMaterial.getRoleType());
            qlCaseMaterial.setQlCaseMaterialAttaList(qlCaseMaterialAttas);
            qlCaseMaterial.setMemo(sxServiceMaterial.getMemo());
            if(sxServiceMaterial.getMadeMaterialType() == BaseStaticParameter.MADE_MATERIAL_TYPE_AUTO) {
                autoList.add(qlCaseMaterial);
            } else {
                uploadList.add(qlCaseMaterial);
            }
        }
        qlCaseMaterialMap.put("autoProduceMaterialList", autoList);// 智能制作
        qlCaseMaterialMap.put("needUploadMaterialList", uploadList);// 上传
        apiResultSet.setData(qlCaseMaterialMap);
        return apiResultSet;
    }
}
