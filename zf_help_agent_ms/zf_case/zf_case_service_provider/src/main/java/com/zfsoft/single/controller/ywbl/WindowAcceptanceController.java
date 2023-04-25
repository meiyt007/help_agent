package com.zfsoft.single.controller.ywbl;

import com.zfsoft.cases.data.QlCase;
import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.feign.SysDictFeignService;
import com.zfsoft.cases.service.QlCaseMaterialService;
import com.zfsoft.cases.service.QlCaseService;
import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.service.sxService.service.SxServiceMaterialService;
import com.zfsoft.single.data.yxpz.AhsSamplePicInfo;
import com.zfsoft.single.data.yxpz.SxMaterialElmsConfig;
import com.zfsoft.single.feign.settings.*;
import com.zfsoft.single.manager.sxpz.SxMaterialElmsConfigManager;
import com.zfsoft.single.manager.ywbl.WindowAcceptanceManager;
import com.zfsoft.single.manager.yxpz.AhsSamplePicInfoManager;
import com.zfsoft.single.service.ywbl.WindowAcceptanceService;
import com.zfsoft.single.util.PageUtil;
import com.zfsoft.single.util.StringUtils;
import com.zfsoft.superwindow.data.clzs.FaModelTemplate;
import com.zfsoft.superwindow.data.clzs.MaterialCatalog;
import com.zfsoft.superwindow.service.clzs.FaModelTemplateService;
import com.zfsoft.superwindow.service.clzs.MaterialCatalogService;
import com.zfsoft.service.sxService.data.SxService;
import com.zfsoft.service.sxService.data.SxServiceMaterial;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @（#）: WindowAcceptanceController
 * @description: 窗口办理接口实现类
 * @author: wangwg
 * @date: 2020/10/27
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
public class WindowAcceptanceController implements WindowAcceptanceService {

    @Resource
    private WindowAcceptanceManager windowAcceptanceManager;

    @Resource
    private SysDictFeignService sysDictFeignService;

    @Resource
    private QlCaseMaterialService qlCaseMaterialServiceFeginService;

    @Resource
    private SxServiceMaterialService sxServiceMaterialFeginService;

    @Resource
    private MaterialCatalogService materialCatalogFeginService;

    @Resource
    private FaModelTemplateService faModelTemplateFeginService;

    @Autowired
    private SxMaterialElmsConfigManager sxMaterialElmsConfigManager;

    @Autowired
    private QlCaseService qlCaseServiceFeginService;

    @Autowired
    private AhsSamplePicInfoManager ahsSamplePicInfoManager;

    @Override
    public ApiResultSet<PageResult<SxService>> listWindowAcceptancePage(String serviceName, String organOid, String serviceType, String serviceOids, Integer pageNum, Integer pageSize) {
        //获取当前登录用户信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
       List<SxService> list = windowAcceptanceManager.listWindowAcceptancePage(serviceName,loginUser.getOrganOid(),serviceType,serviceOids,loginUser.getUserOid(),loginUser.getDistrictOid(),pageNum,pageSize);
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageResult pageResult = new PageResult(pageNum,pageSize,list.size());
        if (pageNum > 1) {
            if (list.size() < (pageNum-1)*pageSize) {
                pageNum = 1;
            }
        }
        list= PageUtil.startPage(list, pageNum,pageSize);
        pageResult.setData(list);
        return new ApiResultSet<>(pageResult);
    }

    @Override
    public ApiResultSet<SysDict> querySysDictByOid(String dictOid) {
        ApiResultSet<SysDict> dict = sysDictFeignService.getSysDictByDictOid(dictOid);
        ApiResultSet<SysDict> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(dict.getData());
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<QlCaseMaterial>> queryMaterialAttaListByCaseOid(String caseOid) {
        List<QlCaseMaterial> list = new LinkedList<QlCaseMaterial>();
        ApiResultSet<List<QlCaseMaterial>> resultSet = qlCaseMaterialServiceFeginService.queryQlCaseMaterialByCaseOid(caseOid);
        List<QlCaseMaterial> qlCaseMaterialList = resultSet.getData();
        if(qlCaseMaterialList !=null){
            for (QlCaseMaterial qlCaseMaterial:qlCaseMaterialList) {

                ApiResultSet<QlCase> qlresultSet=qlCaseServiceFeginService.queryQlCaseByCaseOid(caseOid);
                QlCase qlCase = null;
                if(resultSet.getData() != null){
                    qlCase=qlresultSet.getData();
                }
                //查询材料证照配置
               SxMaterialElmsConfig elecConfig= sxMaterialElmsConfigManager.getElecConfigByMaterialOid(qlCaseMaterial.getMaterialOid());
                if(elecConfig!=null){
                    qlCaseMaterial.setElecBillOid(elecConfig.getBillOid());
                }
                String serviceOid = qlCase.getServiceOid();
                //审查要点
                AhsSamplePicInfo ahsSamplePicInfo1 = new AhsSamplePicInfo();
                ahsSamplePicInfo1.setMateriaOid(qlCaseMaterial.getMaterialOid());
                ahsSamplePicInfo1.setSampleInfoOid(serviceOid);
                List<AhsSamplePicInfo> ahsSamplePicInfoList = this.ahsSamplePicInfoManager.queryAhsSamplePicInfoList(ahsSamplePicInfo1);
                qlCaseMaterial.setAttaListwgl(ahsSamplePicInfoList);
              /*  if(ahsSamplePicInfoList.size()>0){
                    AhsSamplePicInfo ahsSamplePicInfo=ahsSamplePicInfoList.get(0);
                    qlCaseMaterial.setMaterialSampleAddrYl(ahsSamplePicInfo.getComparePicFile());
                    qlCaseMaterial.setMaterialSampleAddr(ahsSamplePicInfo.getAttaOid());
                }*/

                ApiResultSet<SxServiceMaterial> materialresult = sxServiceMaterialFeginService.getSxServiceMaterialByOid(qlCaseMaterial.getMaterialOid());
                if(materialresult.getData() !=null){
                    if(StringUtils.isNotEmpty(materialresult.getData().getMaterialCatalogOid())){
                        ApiResultSet<MaterialCatalog> cataLog=materialCatalogFeginService.getMaterialCatalogOid(materialresult.getData().getMaterialCatalogOid());
                        MaterialCatalog parentCatalog =null;
                        if(cataLog.getData()!=null){
                            parentCatalog=cataLog.getData();
                        }

                        if(parentCatalog !=null){
                            //查询子目录
                            ApiResultSet<List<MaterialCatalog>> listMaterial=materialCatalogFeginService.queryList(parentCatalog.getMaterialCatalogOid());
                            if(listMaterial.getData()!=null&&listMaterial.getData().size()>0){
                                List<MaterialCatalog> catalogList =new ArrayList<>();
                                //循环判断只有有启用的模板子目录才显示
                                for(MaterialCatalog cl: listMaterial.getData()){
                                    String  baidutemplateId=cl.getBaiduTemplateId();
                                    String materialIdentificationTypeOid=cl.getMaterialIdentificationTypeOid();
                                    ApiResultSet<FaModelTemplate> tempate=faModelTemplateFeginService.getPulishedFaModelTemplateByCataOid(cl.getMaterialCatalogOid());
                                    if((StringUtils.isEmpty(materialIdentificationTypeOid)&&StringUtils.isNotEmpty(baidutemplateId))||tempate!=null&&tempate.getData()!=null){
                                        catalogList.add(cl);
                                    }
                                    /*ApiResultSet<FaModelTemplate> tempate=faModelTemplateFeginService.getPulishedFaModelTemplateByCataOid(cl.getMaterialCatalogOid());
                                    if(tempate!=null&&tempate.getData()!=null){
                                        catalogList.add(cl);
                                    }*/
                                }
                                if(catalogList !=null){
                                    qlCaseMaterial.setMaterialCatalogList(catalogList);
                                }

                            }
                        }
                    }
                }
                list.add(qlCaseMaterial);
            }
        }
        ApiResultSet<List<QlCaseMaterial>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(list);
        return apiResultSet;
    }
}
