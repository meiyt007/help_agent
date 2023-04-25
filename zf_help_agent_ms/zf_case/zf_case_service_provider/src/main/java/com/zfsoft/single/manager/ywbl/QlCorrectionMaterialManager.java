package com.zfsoft.single.manager.ywbl;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.data.QlCaseMaterialAtta;
import com.zfsoft.cases.data.QlSysAtta;
import com.zfsoft.cases.service.QlCaseMaterialAttaService;
import com.zfsoft.cases.service.QlCaseMaterialService;
import com.zfsoft.cases.service.SysAttaService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.sxService.service.SxServiceMaterialService;
import com.zfsoft.single.data.ywbl.vo.CaseMaterialVo;
import com.zfsoft.single.data.yxpz.SxMaterialElmsConfig;
import com.zfsoft.single.dbaccess.dao.ywbl.DbQlCorrectionMaterialMapper;
import com.zfsoft.single.dbaccess.data.ywbl.DbQlCorrectionMaterial;
import com.zfsoft.single.dbaccess.data.ywbl.DbQlCorrectionMaterialExample;
import com.zfsoft.single.service.sxpz.SxMaterialElmsConfigService;
import com.zfsoft.service.sxService.data.SxServiceMaterial;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: dongxl
 * @create: 2020-11-17
 * @description: 办件补齐补正
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QlCorrectionMaterialManager {

    private final DbQlCorrectionMaterialMapper dbQlCorrectionMaterialMapper;

    private final QlCaseMaterialService qlCaseMaterialServiceFeginService;

    private final QlCaseMaterialAttaService qlCaseMaterialAttaServiceFeginService;

    private final SysAttaService sysAttaFeignService;

    private final SxMaterialElmsConfigService sxMaterialElmsConfigService;

    private final SxServiceMaterialService sxServiceMaterialFeginService;

    public ApiResultSet getMaterialByCorrectionOid(String correctionOid,String caseOid) {
        Assert.hasLength(correctionOid, "办件主键不能为空！");
        //查询办件材料
        List<CaseMaterialVo> correctMaterial=new ArrayList<>();
        if(StrUtil.isNotEmpty(caseOid)){
          ApiResultSet<List<QlCaseMaterial>> materialList=  qlCaseMaterialServiceFeginService.queryQlCaseMaterialByCaseOid(caseOid);
          if(materialList!=null&&materialList.getData()!=null&&materialList.getData().size()>0){
              //查询需要补正的材料
              DbQlCorrectionMaterialExample dbQlCorrectionMaterialExample = new DbQlCorrectionMaterialExample();
              DbQlCorrectionMaterialExample.Criteria criteria = dbQlCorrectionMaterialExample.createCriteria();
              if (StrUtil.isNotEmpty(correctionOid)) {
                  criteria.andCorrectionOidEqualTo(correctionOid);
              }
              List<DbQlCorrectionMaterial> dbCaseCorr = dbQlCorrectionMaterialMapper.selectByExample(dbQlCorrectionMaterialExample);
              if (!CollectionUtils.isEmpty(dbCaseCorr)) { //存在需要补正材料
                  for (DbQlCorrectionMaterial materialCorrect:dbCaseCorr){
                      for (QlCaseMaterial q:materialList.getData()){
                          if(materialCorrect.getCaseMaterialOid().equals(q.getCaseMaterialOid())){
                              CaseMaterialVo vo=new CaseMaterialVo();
                              //判断材料材料形式以及是否配置证照库
                              ApiResultSet<SxServiceMaterial> material = sxServiceMaterialFeginService.getSxServiceMaterialByOid(q.getMaterialOid());
                              SxServiceMaterial  sxServiceMaterial = material.getData();
                              if(sxServiceMaterial!=null){
                                  if(sxServiceMaterial.getMaterialFormat()==3){
                                      ApiResultSet<SxMaterialElmsConfig> config = sxMaterialElmsConfigService.getElecConfigByMaterialOid(q.getMaterialOid());
                                      SxMaterialElmsConfig materialElmsConfig = config.getData();
                                      if(materialElmsConfig !=null){
                                          q.setElecBillOid(materialElmsConfig.getBillOid());
                                          vo.setElecBillOid(materialElmsConfig.getBillOid());
                                      }
                                  }

                              }
                              //查找下面的附件信息
                              vo.setQlCaseMaterial(q);
                              ApiResultSet<List<QlCaseMaterialAtta>> materialAttas=qlCaseMaterialAttaServiceFeginService.queryQlCaseMaterialAttaByCaseMaterialOid(q.getCaseMaterialOid());
                              if(materialAttas.getCode()==200&&materialAttas.getData()!=null&&materialAttas.getData().size()>0){
                                  List<QlSysAtta> attaList=new ArrayList<>();
                                  for(QlCaseMaterialAtta attaMa:materialAttas.getData()){
                                      //查询附件信息,并且把附件信息放入list
                                     ApiResultSet<QlSysAtta> sysAttaResult= sysAttaFeignService.querySysAttaByOid(attaMa.getAttaOid());
                                     if(sysAttaResult!=null&&sysAttaResult.getCode()==200&&sysAttaResult.getData()!=null){
                                         attaList.add(sysAttaResult.getData());
                                     }
                                  }
                                  vo.setMaterialAtta(attaList);

                              }
                              correctMaterial.add(vo);
                              break;
                          }
                      }
                  }
              }
          }
        }
        return new ApiResultSet(correctMaterial);
    }

}
