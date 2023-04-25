package com.zfsoft.cases.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.data.*;
import com.zfsoft.cases.data.vo.QlCaseVo;
import com.zfsoft.cases.data.vo.SxFormInfoVo;
import com.zfsoft.cases.dbaccess.data.DbQlCase;
import com.zfsoft.cases.dbaccess.data.vo.DbQlCaseVo;
import com.zfsoft.cases.feign.SysDistrictFeignService;
import com.zfsoft.cases.feign.SysOrganFeginService;
import com.zfsoft.cases.feign.SysUserFeginService;
import com.zfsoft.cases.feign.ZzqPushCaseMsgFeignService;
import com.zfsoft.cases.manager.*;
import com.zfsoft.cases.service.QlCaseService;
import com.zfsoft.cases.util.DateUtil;
import com.zfsoft.cases.util.LimitDateCalc;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.cases.util.StringUtils;
import com.zfsoft.ha.data.HaCaseExpress;
import com.zfsoft.ha.managers.HaCaseExpressManager;
import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.bean.BeanUtils;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.sxService.data.SxServiceExtend;
import com.zfsoft.service.sxService.service.SxServiceExtendService;
import com.zfsoft.service.sxService.service.SxServiceService;
import com.zfsoft.service.sxSituation.data.vo.ServiceMaterialVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @（#）: QlCaseController
 * @description: 办件基本信息实现类
 * @author: wangwg
 * @date: 2020/10/23
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
public class QlCaseController implements QlCaseService {

    @Resource
    private QlCaseManager qlCaseManager;

    @Resource
    private QlCaseApplayManager qlCaseApplayManager;

    @Resource
    private QlCaseExtManager qlCaseExtManager;

    @Resource
    private QlCaseMaterialManager qlCaseMaterialManager;

    @Resource
    private QlCaseLinkResultManager qlCaseLinkResultManager;

    @Resource
    private SysUserFeginService sysUserFeginService;

    @Resource
    private SysOrganFeginService sysOrganFeginService;

    @Resource
    private QlCaseTitleValueRelationManager qlCaseTitleValueRelationManager;

    @Resource
    private SxServiceExtendService sxServiceExtendFeginService;

    @Resource
    private SxServiceService sxServiceFeginService;

    @Resource
    private QlCaseMaterialAttaManager qlCaseMaterialAttaManager;

    @Resource
    private SysAttaManager sysAttaManager;

    @Resource
    private SysDistrictFeignService sysDistrictFeignService;

    @Resource
    private ZzqPushCaseMsgFeignService zzqPushCaseMsgFeignService;

    @Override
    public ApiResultSet<PageResult<QlCase>> queryQlCaseWithPage(String caseNumber, String organOid, Integer pageNum, Integer pageSize) {
        QlCase qlCase = new QlCase();
        qlCase.setCaseNumber(caseNumber);
        qlCase.setOrganOid(organOid);
        PageResult<QlCase> pageResult = qlCaseManager.queryQlCaseWithPage(qlCase, pageNum, pageSize);
        ApiResultSet<PageResult<QlCase>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<PageResult<QlCase>> queryQlCaseWithPageByService(String serviceOid, String serviceType, Integer pageNum, Integer pageSize) {
        QlCase qlCase = new QlCase();
        qlCase.setServiceOid(serviceOid);
        qlCase.setServiceType(serviceType);
        PageResult<QlCase> pageResult = qlCaseManager.queryQlCaseWithPage(qlCase, pageNum, pageSize);
        ApiResultSet<PageResult<QlCase>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<PageResult<QlCase>> queryQlCaseWithPageByPackage(String packageCaseOid, Integer pageNum, Integer pageSize) {
        QlCase qlCase = new QlCase();
        qlCase.setPackageCaseOid(packageCaseOid);
        PageResult<QlCase> pageResult = qlCaseManager.queryQlCaseWithPage(qlCase, pageNum, pageSize);
        ApiResultSet<PageResult<QlCase>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<PageResult<QlCase>> queryDoneQlCasePage(String caseNumber, String organOid, String applyUserName, Integer caseStatus, String startDate, String endDate,Integer sourceApp,String serviceOids, String credentialNumber, Integer pageNum, Integer pageSize) {
        ApiResultSet<PageResult<QlCase>> apiResultSet = new ApiResultSet<>();
        QlCaseVo doneCaseVo = new QlCaseVo();
        doneCaseVo.setCaseNumber(caseNumber);
        doneCaseVo.setApplyUserName(applyUserName);
        doneCaseVo.setCaseStatus(caseStatus);
        doneCaseVo.setCredentialNumber(credentialNumber);
        if(startDate !=null){
            doneCaseVo.setStartDate(DateUtil.startDateFormat(startDate));
        }
        if(endDate !=null){
            doneCaseVo.setEndDate(DateUtil.endDateFormat(endDate));
        }
        doneCaseVo.setSourceApp(sourceApp);
        if(!CurrentLoginUserHolder.getIsAdminUser()){
            if(StringUtil.isNotEmpty(serviceOids)){
                doneCaseVo.setServiceOids(Arrays.asList(serviceOids.split(",").clone()));
            }else{
                return apiResultSet;
            }
        }

        PageResult<QlCase> pageResult = qlCaseManager.queryDoneQlCasePage(doneCaseVo, pageNum, pageSize);

        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<PageResult<QlCase>> queryTemporaryQlCasePage(String caseNumber, String projectName, String applyUserName,String serviceType, String startDate, String endDate,Integer sourceApp,String serviceOids, Integer pageNum, Integer pageSize) {
        ApiResultSet<PageResult<QlCase>> apiResultSet = new ApiResultSet<>();
        QlCaseVo caseVo = new QlCaseVo();
        caseVo.setCaseNumber(caseNumber);
        caseVo.setProjectName(projectName);
        caseVo.setApplyUserName(applyUserName);
        caseVo.setServiceType(serviceType);
        if(StringUtil.isNotEmpty(startDate)){
            caseVo.setStartDate(DateUtil.startDateFormat(startDate));
        }
        if(StringUtil.isNotEmpty(endDate)){
            caseVo.setEndDate(DateUtil.endDateFormat(endDate));
        }
        caseVo.setSourceApp(sourceApp);
        if(!CurrentLoginUserHolder.getIsAdminUser()){//不是管理员
            if(StringUtil.isNotEmpty(serviceOids)){//授权事项不为空则查询，为空则直接返回
                caseVo.setServiceOids(Arrays.asList(serviceOids.split(",").clone()));
            }else{
                return apiResultSet;
            }
        }
        PageResult<QlCase> pageResult = qlCaseManager.queryTemporaryQlCasePage(caseVo, pageNum, pageSize);
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }
    @Override
    public ApiResultSet<PageResult<QlCase>> queryRqhbQlCasePage(String caseOid,String caseNumber, String organOid, String applyUserName, String projectName, Integer caseStatus, String startDate, String endDate,Integer sourceApp,String serviceOids, Integer pageNum, Integer pageSize) {
        ApiResultSet<PageResult<QlCase>> apiResultSet = new ApiResultSet<>();
        QlCaseVo doneCaseVo = new QlCaseVo();
        doneCaseVo.setCaseNumber(caseNumber);
        doneCaseVo.setOrganOid(organOid);
        doneCaseVo.setApplyUserName(applyUserName);
        doneCaseVo.setProjectName(projectName);
//        doneCaseVo.setCaseStatus(caseStatus);
        if(!StringUtil.isEmpty(caseOid)){
            doneCaseVo.setCaseOid(caseOid);
        }else {
            if(startDate !=null){
                doneCaseVo.setStartDate(DateUtil.startDateFormat(startDate));
            }
            if(endDate !=null){
                doneCaseVo.setEndDate(DateUtil.endDateFormat(endDate));
            }
        }
        doneCaseVo.setSourceApp(sourceApp);
        if(!CurrentLoginUserHolder.getIsAdminUser()){
            if(StringUtil.isNotEmpty(serviceOids)){
                doneCaseVo.setServiceOids(Arrays.asList(serviceOids.split(",").clone()));
            }else{
                return apiResultSet;
            }
        }
        PageResult<QlCase> pageResult = qlCaseManager.queryRqhbQlCasePage(doneCaseVo, pageNum, pageSize);
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<PageResult<QlCase>> queryQlCasePage(String caseOid,String caseNumber, String organOid, String applyUserName, Integer caseStatus, String startDate, String endDate,String sourceType,Integer sourceApp,Integer dataAuthor,String serviceOids,Integer pageNum, Integer pageSize) {
        ApiResultSet<PageResult<QlCase>> apiResultSet = new ApiResultSet<>();
        DbQlCaseVo doneCaseVo = new DbQlCaseVo();
        doneCaseVo.setCaseNumber(caseNumber);
        doneCaseVo.setApplyUserName(applyUserName);
        doneCaseVo.setCaseStatus(caseStatus);
        if(!StringUtil.isEmpty(caseOid)){
            doneCaseVo.setCaseOid(caseOid);
        }else {
            if(startDate !=null){
                doneCaseVo.setStartDate(DateUtil.startDateFormat(startDate));
            }
            if(endDate !=null){
                doneCaseVo.setEndDate(DateUtil.endDateFormat(endDate));
            }
        }
        doneCaseVo.setSourceApp(sourceApp);
        if(StringUtil.isNotEmpty(sourceType)){
            doneCaseVo.setSourceType(Integer.parseInt(sourceType));
        }
        if(!CurrentLoginUserHolder.getIsAdminUser()){
            if(StringUtil.isNotEmpty(serviceOids)){
                doneCaseVo.setServiceOids(Arrays.asList(serviceOids.split(",").clone()));
            }else{
                return apiResultSet;
            }
        }

        //doneCaseVo.setOrganOid(loginUser.getOrganOid());
        PageResult<QlCase> pageResult = qlCaseManager.queryQlCasePage(doneCaseVo, pageNum, pageSize);
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    @Override
	public ApiResultSet<PageResult<QlCase>> queryKstbQlCasePage(String caseNumber, String organOid,
			String applyUserName, Integer caseStatus, String startDate, String endDate, String sourceType,
			Integer sourceApp, Integer dataAuthor, String loginUserOid, Integer pageNum, Integer pageSize) {

    	 DbQlCaseVo doneCaseVo = new DbQlCaseVo();
         doneCaseVo.setCaseNumber(caseNumber);
         doneCaseVo.setOrganOid(organOid);
         doneCaseVo.setApplyUserName(applyUserName);
         doneCaseVo.setCaseStatus(caseStatus);
         if(StringUtils.isNotEmpty(startDate)) {
        	 doneCaseVo.setStartDate(DateUtil.startDateFormat(startDate));
         }

         if(StringUtils.isNotEmpty(endDate)) {
        	 doneCaseVo.setEndDate(DateUtil.endDateFormat(endDate));
         }
         doneCaseVo.setSourceApp(sourceApp);
         if(StringUtil.isNotEmpty(sourceType)){
             doneCaseVo.setSourceType(Integer.parseInt(sourceType));
         }
         PageResult<QlCase> pageResult = qlCaseManager.queryKstbQlCasePage(doneCaseVo, loginUserOid, pageNum, pageSize);
         ApiResultSet<PageResult<QlCase>> apiResultSet = new ApiResultSet<>();
         apiResultSet.setData(pageResult);
         return apiResultSet;
	}


    @Override
    public ApiResultSet<PageResult<QlCase>> queryWebQlCasePage(String caseNumber,String credentialNumber, String organOid, String applyUserName, Integer caseStatus, String startDate, String endDate,String sourceType,Integer sourceApp,Integer dataAuthor,Integer pageNum, Integer pageSize) {
        DbQlCaseVo doneCaseVo = new DbQlCaseVo();
        doneCaseVo.setCaseNumber(caseNumber);
        doneCaseVo.setCredentialNumber(credentialNumber);
        doneCaseVo.setOrganOid(organOid);
        doneCaseVo.setApplyUserName(applyUserName);
        doneCaseVo.setCaseStatus(caseStatus);
        if(startDate !=null){
            doneCaseVo.setStartDate(DateUtil.startDateFormat(startDate));
        }
        if(endDate !=null){
            doneCaseVo.setEndDate(DateUtil.endDateFormat(endDate));
        }
        doneCaseVo.setSourceApp(sourceApp);
        if(StringUtil.isNotEmpty(sourceType)){
            doneCaseVo.setSourceType(Integer.parseInt(sourceType));
        }
        if(dataAuthor!=null){
            if(dataAuthor==1){

            }
        }
        PageResult<QlCase> pageResult = qlCaseManager.queryQlCasePage(doneCaseVo, pageNum, pageSize);
        ApiResultSet<PageResult<QlCase>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<PageResult<QlCase>> selectQlCaseSuperviseList(QlCaseVo doneCaseVo, Integer pageNum, Integer pageSize) {
        PageResult<QlCase> pageResult = qlCaseManager.selectQlCaseSuperviseList(doneCaseVo, pageNum, pageSize);
        ApiResultSet<PageResult<QlCase>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResultSet<Map<String, Object>> nextStepSaveQlCase(Object object) {
        Map<String, Object> map =new HashMap<String, Object>();
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        if(object != null){
            //登录信息
            CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
            JSONObject jsonObject = JSONObject.parseObject(JSONArray.toJSON(object).toString());
            //解析转化办件信息
            QlCase qlCase = JSON.parseObject(jsonObject.get("qlCase").toString(),QlCase.class);
            //解析转化办件申请信息
            QlCaseApplay qlCaseApplay = JSON.parseObject(jsonObject.get("qlCaseApply").toString(),QlCaseApplay.class);
            //解析转化办件扩展信息
            QlCaseExt qlCaseExt = JSON.parseObject(jsonObject.get("qlCaseExt").toString(),QlCaseExt.class);
            //解析转化材料信息
            JSONArray materialList = JSONArray.parseArray(jsonObject.get("materials").toString());
            //解析转化办件与情形标题选项关系
            JSONArray titleValueList = JSONArray.parseArray(jsonObject.get("titleValues").toString());

            if(null!=qlCase.getCaseOid()){
                QlCase qlCaseOld = qlCaseManager.queryQlCaseByCaseOid(qlCase.getCaseOid());
                if(null==qlCaseOld.getShouldConcludeDate()&&null!=qlCaseOld.getCreateDate()){
                    SxServiceExtend extend=sxServiceExtendFeginService.getSxServiceExtendByServiceOid(qlCase.getServiceOid()).getData();
                    if(null!=extend){
                        if(null==qlCase.getShouldConcludeDate()&&null!=extend.getPromiseLimitType()&&!extend.getPromiseLimitType().equals("")){
                            Integer limit=extend.getPromiseLimit().intValue();
                            Date getDate=new Date();
                            if(extend.getPromiseLimitType().equals("H")){
                                getDate= LimitDateCalc.dateCalc(qlCaseOld.getCreateDate(),0,extend.getPromiseLimitType(),limit);
                            }else{
                                getDate= LimitDateCalc.dateCalc(qlCaseOld.getCreateDate(),limit,extend.getPromiseLimitType(),0);
                            }
                            qlCase.setShouldConcludeDate(getDate);
                        }
                    }
                }
            }else{
                SxServiceExtend extend=sxServiceExtendFeginService.getSxServiceExtendByServiceOid(qlCase.getServiceOid()).getData();
                if(null!=extend){
                    Integer limit=extend.getPromiseLimit().intValue();
                    Date getDate=new Date();
                    if(extend.getPromiseLimitType().equals("H")){
                        getDate= LimitDateCalc.dateCalc(getDate,0,extend.getPromiseLimitType(),limit);
                    }else{
                        getDate= LimitDateCalc.dateCalc(getDate,limit,extend.getPromiseLimitType(),0);

                    }
                    qlCase.setShouldConcludeDate(getDate);
                }
            }

            // 赋值创建用户
            if (qlCase.getSourceType() == 9) {
                qlCase.setCreateUserOid(loginUser.getUserOid());
            }

            //办件基础信息
            Map<String, String> qlcaseMap = qlCaseManager.saveQlCase(qlCase);
            if(qlcaseMap !=null){
                 String caseOid= qlcaseMap.get("caseOid");
                 String caseNumber= qlcaseMap.get("caseNumber");
                 map.put("caseNumber",caseNumber);
                 String serviceTypeName= qlcaseMap.get("serviceTypeName");
                 map.put("serviceTypeName",serviceTypeName);
                 String caseStatus= qlcaseMap.get("caseStatus");
                 map.put("caseStatus",caseStatus);
                 String createDate= qlcaseMap.get("createDate");
                 map.put("createDate",createDate);
                 if(StringUtil.isNotEmpty(caseOid)){
                     map.put("caseOid",caseOid);
                     if(StringUtil.isEmpty(qlCaseApplay.getCaseOid())){
                         qlCaseApplay.setCaseOid(caseOid);
                         qlCaseExt.setCaseOid(caseOid);
                     }
                     if(materialList.size() >0){
                         //查询是否已保存办件材料关系
                         List<QlCaseMaterial> list = qlCaseMaterialManager.queryQlCaseMaterialByCaseOid(caseOid);
                         //保存的数量与查询一致
                         boolean flag=false;
                         if(list.size() == materialList.size()){
                             flag=false;
                         }else{
                             flag=true;
                             for (QlCaseMaterial qlCaseMaterial: list) {
                                 qlCaseMaterialManager.deleteQlCaseMaterial(qlCaseMaterial.getId());
                             }
                         }
                         if(flag){
                             //保存办件材料关系信息
                             List<QlCaseMaterial> qlCaseMaterials=new ArrayList<QlCaseMaterial>();
                             if(materialList.size() > 0){
                                 for (int i=0; i < materialList.size() ;i++) {
                                     ServiceMaterialVo serviceMaterialVo =JSON.parseObject(materialList.get(i).toString(), ServiceMaterialVo.class);
                                     QlCaseMaterial qlCaseMaterial=new QlCaseMaterial();
                                     qlCaseMaterial.setMaterialOid(serviceMaterialVo.getMaterialOid());
                                     qlCaseMaterial.setMaterialName(serviceMaterialVo.getMaterialName());
                                     qlCaseMaterial.setCaseOid(caseOid);
                                     qlCaseMaterial.setCollectionFlag(0);
                                     qlCaseMaterial.setCollectionNumber(0);
                                     qlCaseMaterial.setCreateUserOid(loginUser.getUserOid());
                                     qlCaseMaterial.setBaiduTemplateIds(serviceMaterialVo.getBaiduTemplateIds());
                                     qlCaseMaterial.setMaterialCatalogOid(serviceMaterialVo.getMaterialCatalogOid());
                                     qlCaseMaterial.setMaterialSampleAddr(serviceMaterialVo.getMaterialSampleOid());
                                     qlCaseMaterial.setMaterialSampleAddrYl(serviceMaterialVo.getMaterialSampleUrl());
                                     if(StringUtil.isEmpty(qlCaseMaterial.getMaterialSampleAddr())){
                                         qlCaseMaterial.setMaterialSampleAddr(serviceMaterialVo.getMaterialSampleAddr());
                                         qlCaseMaterial.setMaterialSampleAddrYl(serviceMaterialVo.getMaterialSimpleAddrYl());
                                     }
                                     qlCaseMaterial.setAuditType(serviceMaterialVo.getAuditType());
                                     if(null!=serviceMaterialVo.getMaterialMustFlag()){
                                         qlCaseMaterial.setMustFlag(Short.parseShort(String.valueOf(serviceMaterialVo.getMaterialMustFlag())));
                                     }
                                     qlCaseMaterial.setModifyDate(new Date());
                                     qlCaseMaterials.add(qlCaseMaterial);
                                 }
                             }
                             List<Map<String, String>> caseMaterialmap=  qlCaseMaterialManager.saveBatchQlCaseMaterial(qlCaseMaterials);
                             if(caseMaterialmap !=null) {
                                 map.put("caseMaterialOids",caseMaterialmap);
                             }
                         }
                     }
                     //办件申请信息
                     QlCaseApplay applay = qlCaseApplayManager.queryQlCaseApplayByCaseOid(caseOid);
                     if(applay !=null){
                         qlCaseApplay.setId(applay.getId());
                     }
                     Map<String, Integer> applymap =  qlCaseApplayManager.saveQlCaseApplay(qlCaseApplay);
                     if(applymap !=null) {
                         String applyOid = applymap.get("applyOid").toString();
                         map.put("applyOid",applyOid);
                     }
                     //办件扩展信息
                     QlCaseExt caseExt = qlCaseExtManager.queryQlCaseExtByCaseOid(caseOid);
                     if(caseExt !=null){
                         qlCaseExt.setId(caseExt.getId());
                     }
                     Map<String, Integer> extmap = qlCaseExtManager.saveQlCaseExt(qlCaseExt);
                     if(extmap !=null) {
                         String extOid = extmap.get("extOid").toString();
                         map.put("extOid",extOid);
                     }
                     //办件情形标题选项关系
                     if(titleValueList.size() > 0){
                         //查询是否已保存办件情形标题选项关系
                         List<QlCaseSituationTitleValRelation> titleValRelations = qlCaseTitleValueRelationManager.queryTitleValueRelationByCaseOid(caseOid);
                         if(titleValRelations.size() > 0){
                             for (QlCaseSituationTitleValRelation relation: titleValRelations) {
                                 qlCaseTitleValueRelationManager.deleteQlCaseTitleValueRelation(relation.getId());
                             }
                         }
                         //插入新数据
                         List<QlCaseSituationTitleValRelation> relations=new ArrayList<QlCaseSituationTitleValRelation>();
                         for (int i=0; i < titleValueList.size() ;i++) {
                             QlCaseSituationTitleValRelation relation =JSON.parseObject(titleValueList.get(i).toString(), QlCaseSituationTitleValRelation.class);
                             relation.setCaseOid(caseOid);
                             relations.add(relation);
                         }
                         List<Map<String, String>> relationsmap = qlCaseTitleValueRelationManager.saveQlCaseTitleValueRelations(relations);
                         if(relationsmap !=null) {
                             map.put("relationOids",relationsmap);
                         }
                     }
                 }
            }
        }
        apiResultSet.setData(map);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Map<String, String>> saveCaseAccpet(String caseOid,String userOid,String userName,String finalOpinion,String finalOpinionDesc,String rqbzDueDate,Integer sjStatus) throws Exception {
        //更新进入受理办件
        QlCase qlCase = qlCaseManager.queryQlCaseByCaseOid(caseOid);
        if(qlCase !=null && qlCase.getCaseOid() !=null && !"0".equals(qlCase.getCaseOid())){
            if("1".equals(finalOpinion)){//受理通过
                if(sjStatus==null||sjStatus!=2){
                    //收件状态
                    qlCase.setCaseStatus(10);
                }else{
                    //受理状态
                    qlCase.setCaseStatus(2);
                }
            }else{
                qlCase.setCaseStatus(7);
                qlCase.setConcludeDate(new Date());
            }
            if(StringUtils.isNotEmpty(rqbzDueDate)){
                SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
                qlCase.setRqbzDueDate(sf.parse(rqbzDueDate));
            }
            qlCase.setAcceptanceDate(new Date());
            qlCaseManager.saveQlCase(qlCase);
            // 办件申请信息(QlCaseApply)实体类
            QlCaseApplay qlCaseApplay = qlCaseApplayManager.queryQlCaseApplayByCaseOid(caseOid);
            // 办件信息表(QlCaseExt)实体类
            QlCaseExt qlCaseExt = qlCaseExtManager.queryQlCaseExtByCaseOid(caseOid);
        }
        //判断是否已存在记录  如果是就删除
        Map<String, String> map =null;
        QlCaseLinkResult result= qlCaseLinkResultManager.querySlQlCaseLinkResultByCaseOid(caseOid);
        if(result !=null){
            qlCaseLinkResultManager.deleteQlCaseLinkResult(result.getId());
        }
        //插入进入受理环节信息
        QlCaseLinkResult qlCaseLinkResult = new QlCaseLinkResult();
        qlCaseLinkResult.setCaseOid(caseOid);
        if ("1".equals(finalOpinion)) {
            qlCaseLinkResult.setFinalStatus(1);
        } else {
            qlCaseLinkResult.setFinalStatus(0);
        }
        qlCaseLinkResult.setPersonOid(userOid);
        qlCaseLinkResult.setPersonName(userName);
        if(sjStatus==null||sjStatus!=2){
            //收件状态
            qlCaseLinkResult.setLinkCode("SJ");
            qlCaseLinkResult.setLinkName("收件");
        }else{
            //受理状态
            qlCaseLinkResult.setLinkCode("SL");
            qlCaseLinkResult.setLinkName("受理");
        }

        qlCaseLinkResult.setFinalOpinion(finalOpinion);
        qlCaseLinkResult.setFinalOpinionDesc(finalOpinionDesc);
        qlCaseLinkResult.setFinalDate(new Date());
        map = qlCaseLinkResultManager.saveQlCaseLinkResult(qlCaseLinkResult);
        ApiResultSet<Map<String, String>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(map);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Map<String, String>> saveQlCase(QlCase qlCase) {
        Map<String, String> map = qlCaseManager.saveQlCase(qlCase);
        ApiResultSet<Map<String, String>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(map);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Map<String, Object>> updateQlCaseFormInfo(QlCase qlCase) {
        Map<String, Object> map = qlCaseManager.updateQlCaseFormInfo(qlCase);
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(map);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Map<String, String>> yuShenQlCase(String caseOid, Integer caseStatus) {
        QlCase qlCase = qlCaseManager.queryQlCaseByCaseOid(caseOid);
        qlCase.setCaseStatus(caseStatus);
        if (caseStatus == 3) {
            qlCase.setConcludeDate(new Date());
        }
        Map<String, String> map = qlCaseManager.saveQlCase(qlCase);
        ApiResultSet<Map<String, String>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(map);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<QlCase> queryQlCaseByCaseOid(String caseOid) {
        QlCase qlCase = qlCaseManager.queryQlCaseByCaseOid(caseOid);
        ApiResultSet<QlCase> apiResultSet = new ApiResultSet<>();
        if(qlCase!=null){
            QlCaseApplay qlCaseApplay = qlCaseApplayManager.queryQlCaseApplayByCaseOid(caseOid);
            qlCase.setApplay(qlCaseApplay);
            apiResultSet.setData(qlCase);
        }
        return apiResultSet;
    }

    @Override
    public ApiResultSet<QlCase> queryQlCaseByCaseNumber(String caseNumber) {
        QlCase qlCase =  qlCaseManager.queryQlCaseByCaseNumber(caseNumber);
        ApiResultSet<QlCase> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(qlCase);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<String> deleteQlCaseByCaseOid(String caseOid) {
        DbQlCase dbQlCase = qlCaseManager.deleteQlCaseByCaseOid(caseOid);
        ApiResultSet<String> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(dbQlCase.getCaseOid());
        return apiResultSet;
    }

    @Override
    public ApiResultSet<String> deleteQlCaseByCaseNumber(String caseNumber) {
        DbQlCase dbQlCase = qlCaseManager.deleteQlCaseByCaseNumber(caseNumber);
        ApiResultSet<String> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(dbQlCase.getCaseNumber());
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Map<String,String>> getLoginUser() {
        //登录信息
        Map<String,String> map =new HashMap<String,String>();
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        ApiResultSet<SysUser> resultUser = sysUserFeginService.getSysUserByUserOid(loginUser.getUserOid());
        ApiResultSet<SysOrgan> resultOrgan = sysOrganFeginService.getSysOrganByOrganOid(loginUser.getOrganOid());
        map.put("userOid",loginUser.getUserOid());
        map.put("userName",resultUser.getData().getName());
        map.put("organOid",loginUser.getOrganOid());
        map.put("organName",resultOrgan.getData().getFullName());
        ApiResultSet<Map<String,String>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(map);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Map<String, String>> createCaseNumber(String serviceOid) {
        Map<String,String> map =new HashMap<String,String>();
        Map<String,String> numberMap = qlCaseManager.createCaseNumber(serviceOid);
        ApiResultSet<Map<String,String>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(numberMap);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Map<String, Object>> getAllQlCaseByCaseOid(String caseOid) {
        Map<String,Object> map =new HashMap<String,Object>();
        QlCase qlCase = qlCaseManager.queryQlCaseByCaseOid(caseOid);
        QlCaseApplay qlCaseApplay = qlCaseApplayManager.queryQlCaseApplayByCaseOid(caseOid);
        QlCaseExt qlCaseExt = qlCaseExtManager.queryQlCaseExtByCaseOid(caseOid);
        map.put("qlCase",qlCase);
        map.put("qlCaseApplay",qlCaseApplay);
        map.put("qlCaseExt",qlCaseExt);
        ApiResultSet<Map<String,Object>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(map);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<QlCase>> getQlCaseByPackageCaseOid(String packageCaseOid) {
        List<QlCase> qlCaseList = qlCaseManager.getQlCaseByPackageCaseOid(packageCaseOid);
        ApiResultSet<List<QlCase>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(qlCaseList);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<QlCase>> getQlCaseByOid(String packageCaseOid, String serviceOid) {
        List<QlCase> qlCaseList = qlCaseManager.getQlCaseByOid(packageCaseOid,serviceOid);
        ApiResultSet<List<QlCase>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(qlCaseList);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Map<String, String>> saveCaseInvalid(String caseOid, String invalidReason,Integer caseStatus) {
        QlCase qlCase = qlCaseManager.queryQlCaseByCaseOid(caseOid);
        if(null == qlCase) {
            throw new ResultInfoException("办件信息为空！");
        }
        if(caseStatus!=null){
            qlCase.setCaseStatus(caseStatus);
        }else{
            qlCase.setCaseStatus(3);
        }
        qlCaseManager.saveQlCase(qlCase);

        //插入办件作废信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        QlCaseLinkResult qlCaseLinkResult = new QlCaseLinkResult();
        qlCaseLinkResult.setCaseOid(caseOid);
        qlCaseLinkResult.setFinalStatus(45);
        qlCaseLinkResult.setPersonOid(loginUser.getUserOid());
        qlCaseLinkResult.setPersonName(loginUser.getUserName());
        qlCaseLinkResult.setLinkCode("zf");
        qlCaseLinkResult.setLinkName("作废");
        qlCaseLinkResult.setFinalOpinion("作废已作废");
        qlCaseLinkResult.setFinalOpinionDesc(invalidReason);
        qlCaseLinkResult.setFinalDate(new Date());
        Map<String, String> map = qlCaseLinkResultManager.saveQlCaseLinkResult(qlCaseLinkResult);
        ApiResultSet<Map<String, String>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(map);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<PageResult<QlCase>> queryQlCaseWithPageForZcsl(String caseNumber,  String startDate, String endDate,String projectName,String applyUserName,String serviceType, Integer pageNum, Integer pageSize) {
        QlCaseVo doneCaseVo = new QlCaseVo();
        doneCaseVo.setCaseNumber(caseNumber);
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        doneCaseVo.setOrganOid(loginUser.getOrganOid());
        doneCaseVo.setProjectName(projectName);
        doneCaseVo.setApplyUserName(applyUserName);
        doneCaseVo.setStartDate(DateUtil.startDateFormat(startDate));
        doneCaseVo.setEndDate(DateUtil.endDateFormat(endDate));
        doneCaseVo.setServiceType(serviceType);
        PageResult<QlCase> pageResult = qlCaseManager.queryQlCaseWithPageForZcsl(doneCaseVo, pageNum, pageSize);
        ApiResultSet<PageResult<QlCase>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    @Override
    public ApiResultSet updateQlCase(String caseOid, Integer caseStatus) {
    	   QlCase qlcase=qlCaseManager.queryQlCaseByCaseOid(caseOid);
           qlcase.setCaseStatus(caseStatus);
           DbQlCase dbQlCase=new DbQlCase();
           BeanUtils.copyProperties(qlcase,dbQlCase);
           qlCaseManager.updateQlcase(dbQlCase);
           //保存环节信息
           if(caseStatus==5){//异常办结
               QlCaseLinkResult result=new QlCaseLinkResult();
               result.setCaseOid(caseOid);
               result.setLinkResultOid(UUID.randomUUID().toString().replaceAll("-",""));
               result.setFinalDate(new Date());
               result.setFinalOpinion("办件异常终止");
               result.setCreateDate(new Date());
               result.setFinalStatus(46);//终止办件
               result.setLinkCode("zz");
               result.setFinalOpinionDesc("办件异常终止");
               result.setLinkName("终止");
               CurrentLoginUser loninUser  = CurrentLoginUserHolder.getCurrentLoginUser();
               if(loninUser == null) {
            	   result.setPersonOid("00000000000000000000000000000000");
                   result.setPersonName("管理员");
               } else {
            	   result.setPersonOid(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
                   result.setPersonName(CurrentLoginUserHolder.getCurrentLoginUser().getUserName());
               }
               Map<String,String> map = qlCaseLinkResultManager.saveQlCaseLinkResult(result);
               return new ApiResultSet(map);
           }
           return new ApiResultSet();
    }

    @Override
    public ApiResultSet<List<QlCase>> getOverDueAllCase() {
        List<QlCase> list=qlCaseManager.getOverDueAllCase();
        return new ApiResultSet(list);
    }

    @Override
    public ApiResultSet<List<QlCase>> queryQlCaseFinishListByCaseOid(String caseOid) {
        List<QlCase> qlCaseList = qlCaseManager.queryQlCaseFinishListByCaseOid(caseOid);
        List<QlCase> list =  new ArrayList<QlCase>();
        if(qlCaseList !=null){
            for (QlCase qlCase: qlCaseList ) {
                QlCaseLinkResult linkResult = qlCaseLinkResultManager.queryBjQlCaseLinkResultByCaseOid(qlCase.getCaseOid());
                if(linkResult !=null){
                    list.add(qlCase);
                }
            }
        }
        ApiResultSet<List<QlCase>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(list);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<QlCase>> queryQlCaseListByPackageCaseOid(QlCaseVo qlCaseVo) {
        List<QlCase> qlCaseList = qlCaseManager.queryQlCaseListByPackageCaseOid(qlCaseVo);
        ApiResultSet<List<QlCase>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(qlCaseList);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<QlCase>> getFzOnthingCase(String packageCaseOid) {
        List<QlCase> list=qlCaseManager.getFzOnthingCase(packageCaseOid);
        ApiResultSet<List<QlCase>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(list);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<PageResult<QlCase>> queryQlCaseByCredentialNumber(String credentialNumber,String applyUserType,String projectName,String caseNumber, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        PageResult<QlCase> list= qlCaseManager.queryQlCaseApplayByCredentialNumber(credentialNumber,applyUserType,projectName,caseNumber,pageNum,pageSize);
        return new ApiResultSet<>(list);
    }

    @Override
    public ApiResultSet<Map<String, Integer>> getCaseTjInfo(String userOid) {
       ApiResultSet<Map<String, Integer>> res= qlCaseManager.getCaseTjInfo(userOid);
        return res;
    }

    @Override
    public ApiResultSet<PageResult<QlCase>> taskYsCase(List<String> serviceOids,Integer pageNum, Integer pageSize) {
        PageResult<QlCase> list= qlCaseManager.taskYsCase(serviceOids,pageNum,pageSize);
        return new ApiResultSet(list);

    }

    @Override
    public ApiResultSet taskRqhbCaseList(List<String> serviceOids) {
        List<QlCase> list= qlCaseManager.taskRqhbCaseList(serviceOids);
        return new ApiResultSet(list);
    }

    @Override
    public ApiResultSet<PageResult<QlCase>> taskRqhbCasePage(List<String> serviceOids, Integer pageNum, Integer pageSize) {
        PageResult<QlCase> list= qlCaseManager.taskRqhbCasePage(serviceOids,pageNum,pageSize);
        return new ApiResultSet(list);
    }

    @Override
    public ApiResultSet queryQlCaseInfo(String queueNum, String date) {


        return null;
    }

    @Override
    public ApiResultSet<List<SxFormInfoVo>> queryFormInfoByCaseOid(String caseOid) {
        List<SxFormInfoVo>  sxFormInfoVos =new LinkedList<SxFormInfoVo>();
        QlCase qlCase = qlCaseManager.queryQlCaseByCaseOid(caseOid);
        String formOids = qlCase.getFormOids();
        if(formOids !=null){
            JSONArray array = JSON.parseArray(formOids);
            for (Object obj: array) {
                JSONObject object = (JSONObject) JSONObject.toJSON(obj);
                String designOid = (String) object.get("designOid");
                String authorizeKey = (String) object.get("authorizeKey");
                String formCode = (String) object.get("formCode");
                String formName = (String) object.get("formName");
                SxFormInfoVo sxFormInfoVo = new SxFormInfoVo();
                sxFormInfoVo.setDesignOid(designOid);
                sxFormInfoVo.setAuthorizeKey(authorizeKey);
                sxFormInfoVo.setFormCode(formCode);
                sxFormInfoVo.setFormName(formName);
                sxFormInfoVos.add(sxFormInfoVo);
            }
        }
        return new ApiResultSet(sxFormInfoVos);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "网站端保存办件相关信息")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "object", value = "办件信息", dataType = "json", example = "")})
    public ApiResultSet<Map<String, Object>> browserSaveQlCase(Object object) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, String> qlcaseMap = new HashMap<>();
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        if(object != null){
            //登录信息
            JSONObject jsonObject = JSONObject.parseObject(JSONArray.toJSON(object).toString());
            // 办件信息
            QlCase qlCase = null;
            if(null != jsonObject.get("qlCase")) {
                qlCase = JSON.parseObject(jsonObject.get("qlCase").toString(), QlCase.class);
                if(null == qlCase.getShouldConcludeDate()) {
                    if(null != qlCase.getCaseOid()){
                        QlCase qlCaseOld = qlCaseManager.queryQlCaseByCaseOid(qlCase.getCaseOid());
                        if(null == qlCaseOld.getShouldConcludeDate()&& null != qlCaseOld.getCreateDate()){
                            SxServiceExtend extend=sxServiceExtendFeginService.getSxServiceExtendByServiceOid(qlCase.getServiceOid()).getData();
                            if(null != extend){
                                if(null == qlCase.getShouldConcludeDate()&& null != extend.getPromiseLimitType()&&!extend.getPromiseLimitType().equals("")){
                                    Integer limit = extend.getPromiseLimit().intValue();
                                    Date getDate = new Date();
                                    if(extend.getPromiseLimitType().equals("H")){
                                        getDate = LimitDateCalc.dateCalc(qlCaseOld.getCreateDate(),0,extend.getPromiseLimitType(),limit);
                                    }else{
                                        getDate = LimitDateCalc.dateCalc(qlCaseOld.getCreateDate(),limit,extend.getPromiseLimitType(),0);
                                    }
                                    qlCase.setShouldConcludeDate(getDate);
                                }
                            }
                        }
                    } else {
                        SxServiceExtend extend = sxServiceExtendFeginService.getSxServiceExtendByServiceOid(qlCase.getServiceOid()).getData();
                        if(null != extend){
                            Integer limit = extend.getPromiseLimit().intValue();
                            Date getDate = new Date();
                            if(extend.getPromiseLimitType().equals("H")){
                                getDate = LimitDateCalc.dateCalc(getDate,0,extend.getPromiseLimitType(),limit);
                            }else{
                                getDate = LimitDateCalc.dateCalc(getDate,limit,extend.getPromiseLimitType(),0);

                            }
                            qlCase.setShouldConcludeDate(getDate);
                        }
                    }
                }
                qlcaseMap = qlCaseManager.saveQlCase(qlCase);
            }
            // 情形信息
            JSONArray titleValueList = null;
            if(null != jsonObject.get("titleValues")) {
                titleValueList = JSONArray.parseArray(jsonObject.get("titleValues").toString());
                //办件情形标题选项关系
                if(null != titleValueList && titleValueList.size() > 0){
                    //查询是否已保存办件情形标题选项关系
                    List<QlCaseSituationTitleValRelation> titleValRelations = qlCaseTitleValueRelationManager.queryTitleValueRelationByCaseOid(qlCase.getCaseOid());
                    if(titleValRelations.size() > 0){
                        for (QlCaseSituationTitleValRelation relation: titleValRelations) {
                            qlCaseTitleValueRelationManager.deleteQlCaseTitleValueRelation(relation.getId());
                        }
                    }
                    //插入新数据
                    List<QlCaseSituationTitleValRelation> relations = new ArrayList<QlCaseSituationTitleValRelation>();
                    for (int i=0; i < titleValueList.size() ;i++) {
                        QlCaseSituationTitleValRelation relation = JSON.parseObject(titleValueList.get(i).toString(), QlCaseSituationTitleValRelation.class);
                        relation.setCaseOid(qlCase.getCaseOid());
                        relations.add(relation);
                    }
                    List<Map<String, String>> relationsmap = qlCaseTitleValueRelationManager.saveQlCaseTitleValueRelations(relations);
                    if(relationsmap != null) {
                        map.put("relationOids",relationsmap);
                    }
                }
            }
            // 办件材料信息
            JSONArray materialList = null;
            if(null != jsonObject.get("materials")) {
                materialList = JSONArray.parseArray(jsonObject.get("materials").toString());
                // 判断是否存在办件材料信息，先删除，在重新添加
                List<QlCaseMaterial> list = qlCaseMaterialManager.queryQlCaseMaterialByCaseOid(qlCase.getCaseOid());
                if(null != list && list.size() != 0) {
                    for(QlCaseMaterial qlCaseMaterial : list) {
                        qlCaseMaterialManager.deleteQlCaseMaterial(qlCaseMaterial.getId());
                    }
                }
                //保存办件材料关系信息
                List<QlCaseMaterial> qlCaseMaterials = new ArrayList<QlCaseMaterial>();
                if(null != materialList && materialList.size() > 0){
                    for (int i = 0; i < materialList.size(); i++) {
                        ServiceMaterialVo serviceMaterialVo =JSON.parseObject(materialList.get(i).toString(), ServiceMaterialVo.class);
                        QlCaseMaterial qlCaseMaterial=new QlCaseMaterial();
                        qlCaseMaterial.setMaterialOid(serviceMaterialVo.getMaterialOid());
                        qlCaseMaterial.setMaterialName(serviceMaterialVo.getMaterialName());
                        qlCaseMaterial.setCaseOid(qlCase.getCaseOid());
                        qlCaseMaterial.setCollectionFlag(0);
                        qlCaseMaterial.setCollectionNumber(0);
                        qlCaseMaterial.setCreateUserOid("00000000000000000000000000000010");
                        qlCaseMaterial.setBaiduTemplateIds(serviceMaterialVo.getBaiduTemplateIds());
                        qlCaseMaterial.setMaterialCatalogOid(serviceMaterialVo.getMaterialCatalogOid());
                        qlCaseMaterial.setMaterialSampleAddr(serviceMaterialVo.getMaterialSampleOid());
                        qlCaseMaterial.setMaterialSampleAddrYl(serviceMaterialVo.getMaterialSampleUrl());
                        if(StringUtil.isEmpty(qlCaseMaterial.getMaterialSampleAddr())){
                            qlCaseMaterial.setMaterialSampleAddr(serviceMaterialVo.getMaterialSampleAddr());
                            qlCaseMaterial.setMaterialSampleAddrYl(serviceMaterialVo.getMaterialSimpleAddrYl());
                        }
                        qlCaseMaterial.setAuditType(serviceMaterialVo.getAuditType());
                        if(null!=serviceMaterialVo.getMaterialMustFlag()){
                            qlCaseMaterial.setMustFlag(Short.parseShort(String.valueOf(serviceMaterialVo.getMaterialMustFlag())));
                        }
                        qlCaseMaterial.setModifyDate(new Date());
                        qlCaseMaterials.add(qlCaseMaterial);
                    }
                }
                List<Map<String, String>> caseMaterialmap = qlCaseMaterialManager.saveBatchQlCaseMaterial(qlCaseMaterials);
                if(caseMaterialmap != null) {
                    map.put("caseMaterialOids",caseMaterialmap);
                }
            }
            // 办件申请信息
            QlCaseApplay qlCaseApplay = null;
            if(null != jsonObject.get("qlCaseApply")) {
                qlCaseApplay = JSON.parseObject(jsonObject.get("qlCaseApply").toString(), QlCaseApplay.class);
                if(StringUtil.isEmpty(qlCaseApplay.getCaseOid())){
                    qlCaseApplay.setCaseOid(qlCase.getCaseOid());
                }
                Map<String, Integer> applymap = qlCaseApplayManager.saveQlCaseApplay(qlCaseApplay);
                if(applymap != null) {
                    String applyOid = applymap.get("applyOid").toString();
                    map.put("applyOid",applyOid);
                }
            }
            // 办件扩展信息
            QlCaseExt qlCaseExt = null;
            if(null != jsonObject.get("qlCaseExt")) {
                qlCaseExt = JSON.parseObject(jsonObject.get("qlCaseExt").toString(),QlCaseExt.class);
                if(StringUtil.isEmpty(qlCaseExt.getCaseOid())){
                    qlCaseExt.setCaseOid(qlCase.getCaseOid());
                }else {
                    // 对于重复保存的处理
                    QlCaseExt qlCaseExtend = qlCaseExtManager.queryQlCaseExtByCaseOid(qlCaseExt.getCaseOid());
                    if (null != qlCaseExtend) {
                        qlCaseExt.setId(qlCaseExtend.getId());
                    }
                }
                Map<String, Integer> extmap = qlCaseExtManager.saveQlCaseExt(qlCaseExt);
                if(extmap !=null) {
                    String extOid = extmap.get("extOid").toString();
                    map.put("extOid",extOid);
                }
            }
            // 返回办件基础信息
            if(qlcaseMap != null){
                map.put("caseOid", qlcaseMap.get("caseOid"));
                map.put("caseNumber", qlcaseMap.get("caseNumber"));
                map.put("serviceTypeName", qlcaseMap.get("serviceTypeName"));
                map.put("caseStatus", qlcaseMap.get("caseStatus"));
                map.put("createDate", qlcaseMap.get("createDate"));
            }
        }
        apiResultSet.setData(map);
        return apiResultSet;
    }

    @Override
    @ApiOperation(value = "网站办件关联更新表单信息")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "qlCase", value = "办件表单信息", dataType = "json", example = "")})
    public ApiResultSet<Map<String, Object>> browserUpdateFormInfo(QlCase qlCase) {
        Map<String, Object> map = qlCaseManager.updateQlCaseFormInfo(qlCase);
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(map);
        return apiResultSet;
    }

    @Override
    @ApiOperation(value = "网站端查询办件列表")
    public ApiResultSet<PageResult<QlCase>> queryDoneQlCasePageForBrowers(String caseNumber, String serviceName,
        String netUserId, Integer pageNum, Integer pageSize) {
        ApiResultSet<PageResult<QlCase>> apiResultSet = new ApiResultSet<>();
        QlCaseVo doneCaseVo = new QlCaseVo();
        if(StrUtil.isNotEmpty(caseNumber)) {
            doneCaseVo.setCaseNumber(caseNumber);
        }
        if(StrUtil.isNotEmpty(serviceName)) {
            doneCaseVo.setServiceName(serviceName);
        }
        doneCaseVo.setNetUserId(netUserId);
        PageResult<QlCase> pageResult = qlCaseManager.queryDoneQlCasePage(doneCaseVo, pageNum, pageSize);
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<String> deleteZcQlCaseByCaseOid(String caseOid) {
        DbQlCase dbQlCase = qlCaseManager.deleteZcQlCaseByCaseOid(caseOid);
        ApiResultSet<String> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(dbQlCase.getCaseOid());
        return apiResultSet;
    }
}
