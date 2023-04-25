package com.zfsoft.ha.managers;

import cn.hutool.core.collection.CollUtil;
import com.zfsoft.cases.data.QlCase;
import com.zfsoft.cases.data.QlCaseSituationTitleValRelation;
import com.zfsoft.cases.manager.QlCaseManager;
import com.zfsoft.cases.manager.QlCaseTitleValueRelationManager;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.ha.data.responseData.*;
import com.zfsoft.ha.data.vo.HaServiceCommonProblemVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.manager.sxService.*;
import com.zfsoft.service.manager.sxSituation.ServiceMaterialManager;
import com.zfsoft.service.manager.sxSituation.SxServiceMateOptRelManager;
import com.zfsoft.service.manager.sxSituation.SxServiceOptionTitleManager;
import com.zfsoft.service.manager.sxSituation.SxServiceOptionValManager;
import com.zfsoft.service.manager.sxSys.SxSysAttaManager;
import com.zfsoft.service.sxConditionalRules.data.SxConditionalRules;
import com.zfsoft.service.sxService.data.*;
import com.zfsoft.service.sxSituation.data.*;
import com.zfsoft.service.sxSituation.data.vo.ServiceMaterialVo;
import com.zfsoft.service.sxSituation.service.SxServiceOptionTitleService;
import com.zfsoft.service.sxSituation.service.SxServiceOptionValService;
import com.zfsoft.service.sxSituation.service.SxServiceSituationService;
import com.zfsoft.service.sxSys.data.SxSysAtta;
import com.zfsoft.single.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description // 提供万行接口所支持的manager
 * @Author: Wangyh
 * @Date: 2022/9/27 13:14
 */
@Service
@Slf4j
public class HaViewSxServiceGuideManager {
    /**
     * 实施清单实现类
     */
    @Resource
    private SxServiceManager sxServiceManager;

    @Resource
    private SxServiceExtendManager sxServiceExtendManager;


    @Resource
    private SxServiceQuestionManager sxServiceQuestionManager;

    @Resource
    private SxAcceptConditionManager sxAcceptConditionManager;

    @Resource
    private SxServiceLinkManager sxServiceLinkManager;

    /**
     * 服务对象
     */
    @Resource
    private QlCaseTitleValueRelationManager qlCaseTitleValueRelationManager;

    @Resource
    private SxServiceOptionValService sxServiceOptionValFeginService;

    @Resource
    private SxServiceOptionTitleManager sxServiceOptionTitleManager;



    @Resource
    private SxServiceOptionValManager sxServiceOptionValManager;

    @Resource
    private ServiceMaterialManager serviceMaterialManager;

    @Resource
    private SxServiceMateOptRelManager sxServiceMateOptRelManager;

    @Resource
    private SxServiceMaterialManager sxServiceMaterialManager;

    @Resource
    private RefinedMaterialManager refinedMaterialManager;

    @Resource
    private ReviewPointsManager reviewPointsManager;


    @Resource
    private SxConditionalRulesManager sxConditionalRulesManager;

    @Resource
    private HaServiceCommonProblemManager haServiceCommonProblemManager;


    @Resource
    private SxSysAttaManager sxSysAttaManager;
    /**
     * 获取办件信息
     */
    public ServiceRespVo getServiceInfo(String serviceOid) throws SecurityException{
            ServiceRespVo serviceRespVo = new ServiceRespVo();
            //根据事项id查询事项信息
            SxService sxService = sxServiceManager.viewSxServiceByOid(serviceOid);
            BeanUtils.copyProperties(sxService,serviceRespVo);
            return  serviceRespVo;
    }

    /**
     * 获取扩展信息
     * @param serviceOid
     * @return
     * @throws SecurityException
     */
    public ServiceExtendRespVo getServiceExtendRespVo(String serviceOid)throws SecurityException{
        ServiceExtendRespVo serviceExtendRespVo = new ServiceExtendRespVo();
        SxServiceExtend sxServiceExtend = sxServiceExtendManager.getSxServiceExtendByServiceOid(serviceOid);
        BeanUtils.copyProperties(sxServiceExtend,serviceExtendRespVo);
        return serviceExtendRespVo;
    }

    /**
     * 获取常见问题信息
     * @param serviceOid
     * @return
     * @throws SecurityException
     */
    public List<ServiceQuestionsRespVo> getServiceQuestionsRespVo(String serviceOid)throws SecurityException{
        List<ServiceQuestionsRespVo> questionsRespVoList =  new ArrayList<>();
        List<SxServiceQuestion> sxServiceQuestions = sxServiceQuestionManager.getSxServiceQuestionByServiceOid(serviceOid);
        if(null != sxServiceQuestions && sxServiceQuestions.size()>0){ //遍历常见问题列表
            for(int i=0;i<sxServiceQuestions.size();i++){
                ServiceQuestionsRespVo serviceQuestionsRespVo = new ServiceQuestionsRespVo();
                SxServiceQuestion  sxServiceQuestion= sxServiceQuestions.get(i);
                BeanUtils.copyProperties(sxServiceQuestion,serviceQuestionsRespVo);
                questionsRespVoList.add(serviceQuestionsRespVo);
            }
        }
        return questionsRespVoList;
    }

    /**
     * 获取办件受理信息
     * @param serviceOid
     * @return
     * @throws SecurityException
     */
    public List<AcceptConditionsRespVo> getAcceptConditionsList(String serviceOid)throws SecurityException {
        List<AcceptConditionsRespVo> acceptConditionsRespVoList = new ArrayList<>();//响应万行集合
        List<SxAcceptCondition> sxAcceptConditions = sxAcceptConditionManager.getSxAcceptConditionByServiceOid(serviceOid);
        if (null != sxAcceptConditions && sxAcceptConditions.size() > 0) {
            for (int i = 0; i < sxAcceptConditions.size(); i++) {
                AcceptConditionsRespVo acceptConditionsRespVo = new AcceptConditionsRespVo(); //响应万行实体类
                SxAcceptCondition sxAcceptCondition = sxAcceptConditions.get(i);
                BeanUtils.copyProperties(sxAcceptCondition, acceptConditionsRespVo);
                acceptConditionsRespVoList.add(acceptConditionsRespVo);
            }
        }
        return acceptConditionsRespVoList;
    }

    /**
     * 办理环节信息
     * @param serviceOid
     * @return
     * @throws SecurityException
     */
    public List<ServiceLinksRespVo> getServiceLinksList(String serviceOid)throws SecurityException {
        List<ServiceLinksRespVo> linksRespVoList = new ArrayList<>();
        List<SxServiceLink> sxServiceLinks = sxServiceLinkManager.getSxServiceLinkByServiceOid(serviceOid);
        if(null != sxServiceLinks && sxServiceLinks.size()>0){
            for(int i=0;i<sxServiceLinks.size();i++){
                ServiceLinksRespVo serviceLinksRespVo = new ServiceLinksRespVo();//响应万行实体类
                SxServiceLink sxServiceLink = sxServiceLinks.get(i);
                BeanUtils.copyProperties(sxServiceLink,serviceLinksRespVo);
                linksRespVoList.add(serviceLinksRespVo);
            }
        }
        return linksRespVoList;
    }

    /**
     * 根据办件编号和事项id获取办事指南材料详细信息集合
     * @param caseOid
     * @param serviceOid
     * @return
     * @throws SecurityException
     */
    public List<ServiceMaterialsRespVo> getServiceMaterialsList(String caseOid,String serviceOid)throws SecurityException {
        //1、根据办件编号获取选项信息
        List<QlCaseSituationTitleValRelation> relations  =qlCaseTitleValueRelationManager.queryTitleValueRelationByCaseOid(caseOid);
        StringBuffer optionValOids = new StringBuffer(); //声明办件所选情形值集合
        for (QlCaseSituationTitleValRelation relation : relations) {
            ApiResultSet<SxServiceOptionVal> resultVal = sxServiceOptionValFeginService.getSxServiceOptionValByOid(relation.getValueOid());
            SxServiceOptionVal val = resultVal.getData();
            optionValOids.append(val.getOid()).append(";");
        }
        //2、根据事项编号和选择的情形查询需要的材料
        List<ServiceMaterialVo> serviceMaterialVoList = this.getSituationMaterialListByOids(serviceOid,optionValOids.toString());
        List<ServiceMaterialsRespVo> serviceMaterialsRespVoList = serviceMaterialVoList.stream().map(serviceMaterialVo->{
            ServiceMaterialsRespVo serviceMaterialsRespVo = new ServiceMaterialsRespVo();
            BeanUtils.copyProperties(serviceMaterialVo,serviceMaterialsRespVo);
            return serviceMaterialsRespVo;
        }).collect(Collectors.toList());
        return serviceMaterialsRespVoList;
    }

    /**
     * 获取事项条件预检信息
     * @param serviceOid
     * @return
     * @throws SecurityException
     */
    public List<ConditionalrulesRespVo> getSxConditionalRulesList(String serviceOid)throws SecurityException {
        List<SxConditionalRules> sxConditionalRulesList = sxConditionalRulesManager.selectByserviceOid(serviceOid);
        List<ConditionalrulesRespVo> conditionalrulesRespVoList = sxConditionalRulesList.stream().map(sxConditionalRules1 -> {
            ConditionalrulesRespVo conditionalrulesRespVo = new ConditionalrulesRespVo();
            BeanUtils.copyProperties(sxConditionalRules1,conditionalrulesRespVo);
            return conditionalrulesRespVo;
        }).collect(Collectors.toList());
       return conditionalrulesRespVoList;
    }

    /**
     * 获取情形绑定的填报须知
     * @param serviceOid
     * @return
     * @throws SecurityException
     */
    public List<FillNoticeRespVo>queryComPro(String caseOid,String serviceOid )throws SecurityException {
        HaServiceCommonProblemVo haServiceCommonProblem = new HaServiceCommonProblemVo();
        //1、根据办件编号获取选项信息
        List<QlCaseSituationTitleValRelation> relations  =qlCaseTitleValueRelationManager.queryTitleValueRelationByCaseOid(caseOid);
        StringBuffer optionValOids = new StringBuffer(); //声明办件所选情形值集合
        for (QlCaseSituationTitleValRelation relation : relations) {
            ApiResultSet<SxServiceOptionVal> resultVal = sxServiceOptionValFeginService.getSxServiceOptionValByOid(relation.getValueOid());
            SxServiceOptionVal val = resultVal.getData();
            optionValOids.append(val.getOid()).append(",");
        }
        String[] valueOids = optionValOids.toString().split(",");
        haServiceCommonProblem.setServiceOid(serviceOid);
        //set去重
        Set<HaServiceCommonProblemVo> setResult = new HashSet<>();
        for (String valueOid : valueOids) {
            haServiceCommonProblem.setOptionVal(valueOid);
            setResult.addAll( haServiceCommonProblemManager.queryInfo(haServiceCommonProblem));
        }
        List<HaServiceCommonProblemVo> listResult = new ArrayList<>(setResult);
        List<FillNoticeRespVo> fillNoticeRespVoList = listResult.stream().map(haServiceCommonProblemVo -> {
            FillNoticeRespVo fillNoticeRespVo = new FillNoticeRespVo();
            BeanUtils.copyProperties(haServiceCommonProblemVo,fillNoticeRespVo);
            return fillNoticeRespVo;
        }).collect(Collectors.toList());
        return fillNoticeRespVoList;
    }

    /**
     * 根据事项 情形 选择获取材料和精细化材料
     * @param serviceOid
     * @param optionValOids
     * @return
     */
    public List<ServiceMaterialVo> getSituationMaterialListByOids(String serviceOid, String optionValOids) {
        if (StringUtils.isEmpty(serviceOid)) {
            throw new ResultInfoException("事项主键不能为空");
        }
        List<SxServiceOptionTitle> optionTitles = sxServiceOptionTitleManager.getServiceOptionTitlesByServiceOid(serviceOid);
        List<SxServiceOptionVal> allOptionVal = new ArrayList<>();
        for (SxServiceOptionTitle title : optionTitles) {
            List<SxServiceOptionVal> optionVals = sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(title.getOid());
            allOptionVal.addAll(optionVals);
        }
        Map<String, List<SxServiceMaterial>> materialMap = new HashMap<>();
        Map<String, List<ServiceMaterial>> serviceMaterialMap = new HashMap<>();
        Map<String, Integer> tempMap = new HashMap<>();
        for (SxServiceOptionVal optionVal : allOptionVal) {
            List<SxServiceMateOptRel> optRelList = sxServiceMateOptRelManager.getSxServiceMateOptRelsByOptionValOid(optionVal.getOid());
            List<SxServiceMaterial> sxServiceMaterials = new ArrayList<>();
            List<ServiceMaterial> serviceMaterials = new ArrayList<>();
            for (SxServiceMateOptRel optRel : optRelList) {
                if (StringUtils.isNotEmpty(optRel.getSxMaterialOid())) {
                    SxServiceMaterial sxMaterial = sxServiceMaterialManager.getSxServiceMaterialByOid(optRel.getSxMaterialOid());
                    if (null != sxMaterial) {
                        sxServiceMaterials.add(sxMaterial);
                    }
                }
                if (StringUtils.isNotEmpty(optRel.getMaterialOid())) {
                    ServiceMaterial serviceMaterial = serviceMaterialManager.getServiceMaterialByOid(optRel.getMaterialOid());
                    if (null != serviceMaterial) {
                        serviceMaterials.add(serviceMaterial);
                    }
                }
            }
            tempMap.put(optionVal.getOid(), optRelList.size());
            materialMap.put(optionVal.getOid(), sxServiceMaterials);
            serviceMaterialMap.put(optionVal.getOid(), serviceMaterials);
        }
        // 所有材料
        List<SxServiceMaterial> allSxServiceMaterial = sxServiceMaterialManager.getSxServiceMaterialByServiceOid(serviceOid);
        // 最终展示的材料
        List<SxServiceMaterial> needShowMaterialList = new ArrayList<>();
        // 不展示的材料
        List<SxServiceMaterial> notShowList = new ArrayList<>();
        for (String key : materialMap.keySet()) {
            if (StringUtils.isEmpty(optionValOids)) {
                if (null != tempMap.get(key) && tempMap.get(key) != 0) {
                    List<SxServiceMaterial> list = materialMap.get(key);
                    notShowList.addAll(list);
                }
            } else {
                String[] valueOids = optionValOids.split(";");
                for (String oid : valueOids) {
                    if (oid.equals(key)) {
                        List<SxServiceMaterial> list = materialMap.get(key);
                        needShowMaterialList.addAll(list);
                    } else {
                        if (null != tempMap.get(key) && tempMap.get(key) != 0) {
                            List<SxServiceMaterial> list = materialMap.get(key);
                            notShowList.addAll(list);
                        }
                    }
                }
            }
        }
        for (SxServiceMaterial material : allSxServiceMaterial) {
            if (!notShowList.contains(material)) {
                needShowMaterialList.add(material);
            }
        }
        needShowMaterialList = needShowMaterialList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(SxServiceMaterial::getMaterialOid))), ArrayList::new));
        List<ServiceMaterialVo> materialVoList = this.getServiceMaterialVoBySxMaterial(needShowMaterialList);
        return materialVoList;
    }

    /**
     * 事项材料处理
     *
     * @param list 需要处理的事项材料集合
     * @return
     */
    protected List<ServiceMaterialVo> getServiceMaterialVoBySxMaterial(List<SxServiceMaterial> list) {
        ServiceMaterialVo serviceMaterialVo = null;
        List<ServiceMaterialVo> voList = new LinkedList<ServiceMaterialVo>();
        if (list != null) {
            for (SxServiceMaterial sxServiceMaterial : list) {
                serviceMaterialVo = new ServiceMaterialVo();
                serviceMaterialVo.setMaterialOid(sxServiceMaterial.getMaterialOid());
                serviceMaterialVo.setMaterialName(sxServiceMaterial.getMaterialName());
                serviceMaterialVo.setMaterialType(sxServiceMaterial.getMaterialType() == null ? null : sxServiceMaterial.getMaterialType().intValue());
                serviceMaterialVo.setMaterialMustFlag(sxServiceMaterial.getMustFlag() == null ? null : sxServiceMaterial.getMustFlag().intValue());
                if (sxServiceMaterial.getMaterialSource() != null) {
                    serviceMaterialVo.setMaterialSource(sxServiceMaterial.getMaterialSource() == null ? null : sxServiceMaterial.getMaterialSource().intValue());
                }
                serviceMaterialVo.setMaterialSimpleAddr(sxServiceMaterial.getMaterialSampleAddr());
                serviceMaterialVo.setMaterialServiceOid(sxServiceMaterial.getServiceOid());
                if (sxServiceMaterial.getMaterialFormat() != null) {
                    serviceMaterialVo.setMaterialFormat(sxServiceMaterial.getMaterialFormat() == null ? null : sxServiceMaterial.getMaterialFormat().intValue());
                } else {
                    serviceMaterialVo.setMaterialFormat(1);
                }
                serviceMaterialVo.setMaterialCatalogOid(sxServiceMaterial.getMaterialCatalogOid());
                serviceMaterialVo.setBaiduTemplateIds(sxServiceMaterial.getBaiduTemplateIds());
                serviceMaterialVo.setPaperNumber(sxServiceMaterial.getPaperNumber());
                serviceMaterialVo.setMaterialSampleAddr(sxServiceMaterial.getMaterialSampleAddr());
                serviceMaterialVo.setMaterialSimpleAddrYl(sxServiceMaterial.getMaterialSampleAddrYl());
                serviceMaterialVo.setAuditType(sxServiceMaterial.getAuditType());
                serviceMaterialVo.setMaterialSampleOid(sxServiceMaterial.getMaterialSampleOid());
                serviceMaterialVo.setMaterialSampleUrl(sxServiceMaterial.getMaterialSampleUrl());
                serviceMaterialVo.setMaterialFlag(0);
                serviceMaterialVo.setMadeMaterialType(sxServiceMaterial.getMadeMaterialType());
                serviceMaterialVo.setReviewPointsFlag(0);
                serviceMaterialVo.setRemark(sxServiceMaterial.getRemark());
                List<RefinedMaterial> refinedMaterialList = refinedMaterialManager.getRefinedMaterialListByMaterialOid(sxServiceMaterial.getMaterialOid());
                for (RefinedMaterial refinedMaterial : refinedMaterialList) {
                    List<ReviewPoints> reviewPointsList = reviewPointsManager.getReviewPointsListByRefinedMaterialOid(refinedMaterial.getOid());
                    if (CollUtil.isNotEmpty(reviewPointsList)) {
                        serviceMaterialVo.setReviewPointsFlag(1);
                        break;
                    }
                }
                serviceMaterialVo.setMaterialEmptyAddr(sxServiceMaterial.getMaterialEmptyAddr());
                if(StringUtil.isNotEmpty(sxServiceMaterial.getMaterialEmptyAddr())){
                    SxSysAtta sxSysAtta = sxSysAttaManager.getSxSysAttaByOid(sxServiceMaterial.getMaterialEmptyAddr());
                    serviceMaterialVo.setMaterialEmptyAddrYl(sxSysAtta.getFilePath());
                }
                voList.add(serviceMaterialVo);
            }
        }
        return voList;
    }
}
