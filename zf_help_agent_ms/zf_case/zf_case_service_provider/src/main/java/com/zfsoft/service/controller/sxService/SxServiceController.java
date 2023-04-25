package com.zfsoft.service.controller.sxService;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.cases.feign.SysOrganFeginService;
import com.zfsoft.cases.feign.SysUserFeginService;
import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.service.common.SxptBaseStaticParameter;
import com.zfsoft.service.manager.sxService.*;
import com.zfsoft.service.manager.sxSituation.ServiceMaterialManager;
import com.zfsoft.service.manager.sxSys.SxSysAttaManager;
import com.zfsoft.service.sxConditionalRules.data.SxConditionalRules;
import com.zfsoft.service.sxService.data.*;
import com.zfsoft.service.sxService.service.SxServiceService;
import com.zfsoft.service.sxSituation.data.ServiceMaterial;
import com.zfsoft.service.sxSituation.data.SxServiceOptionTitle;
import com.zfsoft.service.sxSys.data.SxSysAtta;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @ClassName SxServiceController
 * @Description 实施清单控制类
 * @Author wangxl
 * @Date 2020-10-26
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SxServiceController implements SxServiceService {
    @Resource
    private SxServiceManager sxServiceManager;
    @Resource
    private SxServiceExtendManager sxServiceExtendManager;
    @Resource
    private SxServiceLocationManager sxServiceLocationManager;
    @Resource
    private SxServiceChargeManager sxServiceChargeManager;
    @Resource
    private SxAcceptConditionManager sxAcceptConditionManager;
    @Resource
    private SxServiceMaterialManager sxServiceMaterialManager;
    @Resource
    private SxServiceQuestionManager sxServiceQuestionManager;
    @Resource
    private  SxServiceLinkManager sxServiceLinkManager;
    @Resource
    private ServiceMaterialManager serviceMaterialManager;
    @Resource
    private SysUserFeginService sysUserFeignService;
    @Resource
    private RefinedMaterialManager refinedMaterialManager;
    @Resource
    private SxSysAttaManager sxSysAttaManager;
    @Resource
    private SxConditionalRulesManager sxConditionalRulesManager;

    /**
     * 部门信息外部模块调用接口
     */
    @Resource
    private SysOrganFeginService sysOrganFeginService;

    /**
     * @description:  初始化实施清单的信息
     * @param serviceOid 实体类业务主键
     * @author: wangxl
     * @Date: 2020/10/26
     **/
    @Override
    public ApiResultSet viewSxService(String serviceOid) {
        Map<String, Object> resultMap = new HashMap<>();
        if(null!=serviceOid){
            SxService sxService = sxServiceManager.viewSxServiceByOid(serviceOid);
            resultMap.put("sxService",sxService);
            //扩展信息
            SxServiceExtend sxServiceExtend = sxServiceExtendManager.getSxServiceExtendByServiceOid(serviceOid);
            resultMap.put("sxServiceExtend",sxServiceExtend);
            //办理地址
            if(null != sxServiceExtend){
                List<SxServiceLocation> sxServiceLocations = sxServiceLocationManager.getSxServiceLocationByExtendOid(sxServiceExtend.getExtendOid());
                if(null != sxServiceLocations && sxServiceLocations.size()>0){
                    resultMap.put("sxServiceLocations",sxServiceLocations);
                }
            }
            //收费信息
            if(null != sxService.getChargeFlag() && sxService.getChargeFlag() == SxptBaseStaticParameter.ONE){
                List<SxServiceCharge>  sxServiceCharges = sxServiceChargeManager.getSxServiceChargeByServiceOid(serviceOid);
                if(null != sxServiceCharges && sxServiceCharges.size()>0){
                    resultMap.put("sxServiceCharges",sxServiceCharges);
                }
            }
            //受理信息
            List<SxAcceptCondition> sxAcceptConditions = sxAcceptConditionManager.getSxAcceptConditionByServiceOid(serviceOid);
            if(null != sxAcceptConditions && sxAcceptConditions.size()>0){
                resultMap.put("sxAcceptConditions",sxAcceptConditions);
            }
            //申请材料
            List<SxServiceMaterial> sxServiceMaterials = sxServiceMaterialManager.getSxServiceMaterialByServiceOid(serviceOid);
            if(null != sxServiceMaterials && sxServiceMaterials.size()>0){
                resultMap.put("sxServiceMaterials",sxServiceMaterials);
            }else{
                resultMap.put("sxServiceMaterials","");
            }
            //颗粒材料
            List<ServiceMaterial>  serviceMaterialList= serviceMaterialManager.getServiceMaterialsByServiceOid(serviceOid);
            if(null != serviceMaterialList && serviceMaterialList.size()>0){
                resultMap.put("serviceMaterialList",serviceMaterialList);
            }else{
                resultMap.put("serviceMaterialList","");
            }
            //常见问题
            List<SxServiceQuestion> sxServiceQuestions = sxServiceQuestionManager.getSxServiceQuestionByServiceOid(serviceOid);
            if(null != sxServiceQuestions && sxServiceQuestions.size()>0){
                resultMap.put("sxServiceQuestions",sxServiceQuestions);
            }
            //办理环节
            List<SxServiceLink> sxServiceLinks = sxServiceLinkManager.getSxServiceLinkByServiceOid(serviceOid);
            if(null != sxServiceLinks && sxServiceLinks.size()>0){
                resultMap.put("sxServiceLinks",sxServiceLinks);
            }
            /**
             * 20220905 09:55  zhaobf  修改
             * 新增条件预检配置 start
             */
            //条件预检配置
            List<SxConditionalRules> sxConditionalRules = sxConditionalRulesManager.selectByserviceOid(serviceOid);
            if(null != sxConditionalRules){
                resultMap.put("sxConditionalRules",sxConditionalRules);
            }else{
                resultMap.put("sxConditionalRules","");
            }
            /**
             * 新增条件预检配置 end
             */
        }
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(resultMap);
        return apiResultSet;
    }

    /**
     * 获取事项主表信息
     * @param serviceOid 业务主键
     * @return
     */
    @Override
    public ApiResultSet<SxService> getSxServiceByOid(String serviceOid) {
        SxService sxService = sxServiceManager.getSxServiceByOid(serviceOid);
        ApiResultSet<SxService> apiResultSet = new ApiResultSet<SxService>();
        apiResultSet.setData(sxService);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<SxService>> getSxRecommendServiceListByOid(String serviceOid) {
        SxService sxService = sxServiceManager.getSxServiceByOid(serviceOid);
        String recommendedServiceOids = sxService.getRecommendedServiceOids();
        List<SxService> serviceList = new ArrayList<>();
        if(StringUtils.isNotEmpty(recommendedServiceOids)){
            String[] serviceOids = recommendedServiceOids.split(",");
            serviceList = sxServiceManager.getSxRecommendServiceListByOid(serviceOids);
        }
        ApiResultSet<List<SxService> > apiResultSet = new ApiResultSet<List<SxService>>();
        apiResultSet.setData(serviceList);
        return apiResultSet;
    }


    /**
     * 实施清单分页列表
     * @param serviceName
     * @param basicCode
     * @param serviceTypeOid
     * @param serviceStatus
     * @param implementCode
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ApiResultSet<PageResult<SxService>> querySxServiceWithPage(String serviceName, String basicCode
            , String serviceTypeOid, Integer serviceStatus, String implementCode
            ,String districtOid,String organOid,String existChildItem, String caseType,String handleType, Integer pageNum, Integer pageSize) {
        SxService sxService = new SxService();
        if(StrUtil.isNotEmpty(serviceName)) {
            sxService.setServiceName(serviceName.trim());
        }
        if(StrUtil.isNotEmpty(basicCode)) {
            sxService.setBasicCode(basicCode.trim());
        }
        if(StrUtil.isNotEmpty(serviceTypeOid)) {
            sxService.setServiceTypeOid(serviceTypeOid.trim());
        }
        if(null != serviceStatus){
            sxService.setServiceStatus(serviceStatus.shortValue());
        }
        if(StrUtil.isNotEmpty(implementCode)) {
            sxService.setImplementCode(implementCode.trim());
        }
        if(StrUtil.isNotEmpty(districtOid)) {
            sxService.setDistrictOid(districtOid.trim());
        }
        if(StrUtil.isNotEmpty(organOid)) {
            sxService.setOrganOid(organOid.trim());
        }
        if(StrUtil.isNotEmpty(existChildItem)){
            sxService.setExistChildItem(existChildItem.trim());
        }
        if(StrUtil.isNotEmpty(caseType)){
            sxService.setCaseType(Short.valueOf(caseType));
        }
        if(StrUtil.isNotEmpty(handleType)){
            sxService.setHandleType(handleType);
        }
        PageResult<SxService> pageResult = sxServiceManager.querySxServiceWithPage(sxService,pageNum,pageSize);
        ApiResultSet<PageResult<SxService>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<SxService>> getSxServicList(String organOid) {
        SxService service = null;
        if(StrUtil.isNotEmpty(organOid)){
            service = new SxService();
            service.setOrganOid(organOid);
        }
        List<SxService> sxServices = sxServiceManager.getSxServicList(service);
        ApiResultSet<List<SxService>> apiResultSet = new ApiResultSet<List<SxService>>();
        apiResultSet.setData(sxServices);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<SxService>> getSxServicListByDistrictOid(String districtOid) {
        SxService service = null;
        if(StrUtil.isNotEmpty(districtOid)){
            service = new SxService();
            service.setDistrictOid(districtOid);
        }
        List<SxService> sxServices = sxServiceManager.getSxServicList(service);
        ApiResultSet<List<SxService>> apiResultSet = new ApiResultSet<List<SxService>>();
        apiResultSet.setData(sxServices);
        return apiResultSet;
    }

    /**
     * 根据serviceOid获取事项情形列表
     *
     * @param serviceOid
     * @return
     */
    @Override
    public ApiResultSet<Map<String, Object>> getSxServiceSituationList(String serviceOid) {
        //返回结果集
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        Map<String, Object> returnMap = sxServiceManager.getStringObjectMap(serviceOid);
        apiResultSet.setData(returnMap);
        return apiResultSet;
    }

    /**
     * 根据serviceOid获取事项的 所有情形和所有选项
     *
     * @param serviceOid
     * @return
     */
    @Override
    public ApiResultSet<Map<String, Object>> getSxServiceSituationAndOptions(String serviceOid) {
        //返回结果集
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        Map<String, Object> returnMap = sxServiceManager.getSxServiceSituationAndOptionMap(serviceOid);
        apiResultSet.setData(returnMap);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<PageResult<SxService>> querySxServicePage(SxService sxService, Integer pageNum, Integer pageSize) {
        PageResult<SxService> pageResult = sxServiceManager.querySxServiceWithPage(sxService,pageNum,pageSize);
        ApiResultSet<PageResult<SxService>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }
    private String autoClassificationStatus;

    /**
     * 分类器id
     */
    private String classifierId;

    @Override
    public ApiResultSet saveOrUpdateSxServiceMaterialClassifier(SxService sxService) {
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        Map<String, Object>  map= sxServiceManager.saveOrUpdateSxServiceMaterialClassifier(sxService);
        apiResultSet.setData(map);
        return apiResultSet;
    }

    @Override
    public ApiResultSet saveOrUpdateSxServiceMaterial(SxService sxService) {
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        Map<String, Object>  map= sxServiceManager.saveOrUpdateSxServiceMaterial(sxService);
        apiResultSet.setData(map);
        return apiResultSet;
    }

    @Override
    public ApiResultSet saveOrUpdateServiceMaterial(SxService sxService) {
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        Map<String, Object>  map= sxServiceManager.saveOrUpdateServiceMaterial(sxService);
        apiResultSet.setData(map);
        return apiResultSet;
    }

    @Override
    public ApiResultSet geSxServiceAndMaterial(String serviceOid) {
        Map<String, Object> resultMap = new HashMap<>();
        if(null!=serviceOid){


            List<RefinedMaterial> refinedMaterials=new ArrayList<>();


            SxService sxService = sxServiceManager.viewSxServiceByOid(serviceOid);
            resultMap.put("sxService",sxService);
            //扩展信息
            SxServiceExtend sxServiceExtend = sxServiceExtendManager.getSxServiceExtendByServiceOid(serviceOid);
            resultMap.put("sxServiceExtend",sxServiceExtend);
            //申请材料
            List<SxServiceMaterial> sxServiceMaterials = sxServiceMaterialManager.getSxServiceMaterialByServiceOid(serviceOid);
            if(null != sxServiceMaterials && sxServiceMaterials.size()>0){
                resultMap.put("sxServiceMaterials",sxServiceMaterials);
                //细化材料
                for(SxServiceMaterial sxServiceMaterial:sxServiceMaterials){
                    String materialOid=sxServiceMaterial.getMaterialOid();
                    List<RefinedMaterial> refinedMaterialList=refinedMaterialManager.getRefinedMaterialListByMaterialOid(materialOid);
                    for(RefinedMaterial refinedMaterial:refinedMaterialList){
                        refinedMaterial.setMaterialName(sxServiceMaterial.getMaterialName());
                    }
                    sxServiceMaterial.setRefinedMaterialList(refinedMaterialList);
                    refinedMaterials.addAll(refinedMaterialList);
                }

            }else{
                resultMap.put("sxServiceMaterials","");
            }

            //细化材料
            resultMap.put("refinedMaterials",refinedMaterials);

            //颗粒材料
            List<ServiceMaterial>  serviceMaterialList= serviceMaterialManager.getServiceMaterialsByServiceOid(serviceOid);
            if(null != serviceMaterialList && serviceMaterialList.size()>0){
                resultMap.put("serviceMaterialList",serviceMaterialList);
            }else{
                resultMap.put("serviceMaterialList","");
            }

        }
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(resultMap);
        return apiResultSet;
    }


    @Override
    @ApiOperation(value = "查询事项情形选项和选项值")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "serviceOid", value = "事项oid", dataType = "string", example = "")})
    public ApiResultSet<List<SxServiceOptionTitle>>  getSxServiceTitlesNoRelation(String serviceOid) {
        ApiResultSet<List<SxServiceOptionTitle>> apiResultSet = new ApiResultSet<>();
        //List<SxServiceOptionTitle>  list = sxServiceManager.getSxServiceTitlesNoRelation(serviceOid);
        //添加默认选中逻辑。
        List<SxServiceOptionTitle>  list = sxServiceManager.getSxServiceTitlesNoRelationDefault(serviceOid);
        apiResultSet.setData(list);
        return apiResultSet;
    }



    @Override
    @ApiOperation(value = "查询选项值下面关联的情形选项")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "serviceOid", value = "事项oid", dataType = "string", example = ""),
            @ApiImplicitParam(paramType = "query", name = "valOids", value = "所有选中的选项值oid", dataType = "string", example = ""),
            @ApiImplicitParam(paramType = "query", name = "currentOid", value = "当前选项值oid", dataType = "string", example = ""),
            @ApiImplicitParam(paramType = "query", name = "currentTitleOid", value = "当前选项oid", dataType = "string", example = ""),
            @ApiImplicitParam(paramType = "query", name = "rootTitleOid", value = "根选项oid", dataType = "string", example = "")})
    public ApiResultSet<List<SxServiceOptionTitle>> querySxServiceSituationRelation(String serviceOid, String valOids, String currentOid, String currentTitleOid, String rootTitleOid) {
        ApiResultSet<List<SxServiceOptionTitle>> apiResultSet = new ApiResultSet<>();
        List<SxServiceOptionTitle> list = sxServiceManager.querySxServiceSituationRelation(serviceOid,valOids,currentOid,currentTitleOid,rootTitleOid);
        apiResultSet.setData(list);
        return apiResultSet;
    }


    @Override
    public ApiResultSet<List<SxService>> getSxServiceListBySxServiceName(String serviceName) {
        return sxServiceManager.getSxServiceListBySxServiceName(serviceName);
    }

    @Override
    public ApiResultSet<SxService> saveSxService(SxService sxService) {
        if(sxService!=null){
          SxService info=  sxServiceManager.saveSxService(sxService);
          return new ApiResultSet<>(info);
        }
        return null;
    }

    @Override
    public ApiResultSet<Boolean> delSxServiceByOid(String serviceOid) {
       Boolean res= sxServiceManager.delSxServiceByOid(serviceOid);
        return new ApiResultSet<>(res);
    }

    @Override
    public ApiResultSet<Map<String, Object>> getDistInfo() {
       Map<String, Object> map= sxServiceManager.getDistInfo();
        return new ApiResultSet<>(map);
    }

    @Override
    public ApiResultSet<SxService> getSxServiceAndExtend(String serviceOid) {
        SxService sxservice = sxServiceManager.viewSxServiceByOid(serviceOid);
        if (sxservice != null) {
            //扩展信息
            SxServiceExtend sxServiceExtend = sxServiceExtendManager.getSxServiceExtendByServiceOid(serviceOid);
            if (sxServiceExtend != null) {
                if (StringUtil.isNotEmpty(sxServiceExtend.getResultSampleAddr())) {
                    SxSysAtta resultAtta = sxSysAttaManager.getSxSysAttaByOid(sxServiceExtend.getResultSampleAddr());
                    sxServiceExtend.setResultSampleAddrFile(resultAtta);
                }
                if (StringUtil.isNotEmpty(sxServiceExtend.getHandleFlow())) {
                    SxSysAtta resultAtta = sxSysAttaManager.getSxSysAttaByOid(sxServiceExtend.getHandleFlow());
                    sxServiceExtend.setHandleFlowFile(resultAtta);
                }
                if (StringUtil.isNotEmpty(sxServiceExtend.getChargeEvidence())) {
                    SxSysAtta resultAtta = sxSysAttaManager.getSxSysAttaByOid(sxServiceExtend.getChargeEvidence());
                    sxServiceExtend.setChargeEvidenceFile(resultAtta);
                }
            }
            sxservice.setSxServiceExtend(sxServiceExtend);
            return new ApiResultSet<>(sxservice);
        }
        return null;
    }

    @Override
    public ApiResultSet<List<SxServiceOptionTitle>> querySxServiceSituationRelationBySituationAndTitle(String serviceOid, String titleOid, String situationId) {
        List<SxServiceOptionTitle> lists = sxServiceManager.querySxServiceSituationRelationBySituationAndTitle(serviceOid,titleOid,situationId);
        return new ApiResultSet<>(lists);
    }

    @Override
    public ApiResultSet queryConsultUser(@RequestParam(value = "serviceOid") String serviceOid){
        SxServiceExtend extend = sxServiceExtendManager.getSxServiceExtendByServiceOid (serviceOid);
        ApiResultSet<SysUser> apiResultSet
                = sysUserFeignService.getSysUserByUserOid(extend.getConsultUserOid());
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<SysOrgan>> queryOrganSelectOptions() {
        //获取机构列表
        ApiResultSet<List<SysOrgan>> organList = sysOrganFeginService.getOrganList();
        return organList;
    }

    @Override
    public ApiResultSet<PageResult<SxService>> querySxServiceList(Integer pageNum, Integer pageSize) {
        PageResult<SxService> pageResult = sxServiceManager.querySxServiceList(pageNum,pageSize);
        ApiResultSet<PageResult<SxService>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }
}
