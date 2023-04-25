package com.zfsoft.ha.front.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.google.common.base.CaseFormat;
import com.zfsoft.cases.data.*;
import com.zfsoft.cases.data.vo.QlCaseMaterialVo;
import com.zfsoft.cases.data.vo.QlCaseVo;
import com.zfsoft.cases.data.vo.SxFormInfoVo;
import com.zfsoft.cases.feign.FormManagerFeignService;
import com.zfsoft.cases.feign.SysDictFeignService;
import com.zfsoft.cases.manager.*;
import com.zfsoft.cases.service.*;
import com.zfsoft.cases.util.*;
import com.zfsoft.ha.constant.RedisConstants;
import com.zfsoft.ha.data.HaCaseExpress;
import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.HaUserService;
import com.zfsoft.ha.data.requestData.HaAttaPushRequestData;
import com.zfsoft.ha.data.requestData.HaEvalPushRequestData;
import com.zfsoft.ha.data.requestData.HaPolicyPushRequestData;
import com.zfsoft.ha.data.requestData.HaResourcePushRequestData;
import com.zfsoft.ha.data.responseData.HaResourcePushResponseData;
import com.zfsoft.ha.data.responseData.HaUserResourceResponseVoData;
import com.zfsoft.ha.data.vo.OnekeyPushVo;
import com.zfsoft.ha.data.vo.WinNumbVO;
import com.zfsoft.ha.front.HaBusinessService;
import com.zfsoft.ha.managers.HaCaseExpressManager;
import com.zfsoft.ha.managers.HaServiceRegistrarManger;
import com.zfsoft.ha.managers.HaUserResourceManager;
import com.zfsoft.ha.util.ClientServer;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.microservice.form.data.vo.FormDataVo;
import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.ocr.util.CommonRestUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.browser.service.BrowserService;
import com.zfsoft.service.manager.sxService.RefinedMaterialManager;
import com.zfsoft.service.manager.sxService.ReviewPointsManager;
import com.zfsoft.service.manager.sxService.SxServiceMaterialManager;
import com.zfsoft.service.manager.sxSituation.ServiceMaterialManager;
import com.zfsoft.service.manager.sxSituation.SxServiceMateOptRelManager;
import com.zfsoft.service.manager.sxSituation.SxServiceOptionTitleManager;
import com.zfsoft.service.manager.sxSituation.SxServiceOptionValManager;
import com.zfsoft.service.manager.sxSys.SxSysAttaManager;
import com.zfsoft.service.sxService.data.*;
import com.zfsoft.service.sxService.service.*;
import com.zfsoft.service.sxSituation.data.ServiceMaterial;
import com.zfsoft.service.sxSituation.data.SxServiceMateOptRel;
import com.zfsoft.service.sxSituation.data.SxServiceOptionTitle;
import com.zfsoft.service.sxSituation.data.SxServiceOptionVal;
import com.zfsoft.service.sxSituation.data.vo.ServiceMaterialVo;
import com.zfsoft.service.sxSys.data.SxSysAtta;
import com.zfsoft.single.data.formTemplate.TermlateDataDto;
import com.zfsoft.single.dbaccess.data.DbFormFieldRelConfig;
import com.zfsoft.single.manager.yxpz.FormFieldRelConfigManager;
import com.zfsoft.single.service.api.ApiService;
import com.zfsoft.single.service.formTemplate.MaterialTemplateService;
import com.zfsoft.single.service.yxpz.FormFieldRelConfigService;
import com.zfsoft.superwindow.data.clzs.BaseFormField;
import com.zfsoft.superwindow.manager.web.WebManager;
import com.zfsoft.superwindow.service.clzs.BasicFormFieldService;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import com.zfsoft.superwindow.service.easyquickcase.data.TermlateDataSuperDto;
import com.zfsoft.superwindow.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @description: 帮代办服务：前台业务事项办件controller
 * @author: kangax
 * @date: 2022-07-29 10:13
 **/
@RestController
@Slf4j
public class HaBusinessController implements HaBusinessService {
    @Autowired
    private WebManager webManager;

    @Resource
    private FormFieldRelConfigManager formFieldRelConfigManager;

    @Resource
    private SxFillFieldService sxFillFieldFeginService;

    @Resource
    private BasicFormFieldService basicFormFieldFeginService;

    /**
     * 网站通用接口
     */
    @Resource
    private BrowserService browserFeignService;

    /**
     * 实施清单服务定义接口
     */
    @Resource
    private SxServiceService sxServiceFeginService;

    /**
     * 办件基本信息服务定义接口
     */
    @Resource
    private QlCaseService qlCaseServiceFeginService;

    /**
     *
     */
    @Resource
    private SxServiceMaterialService sxServiceMaterialFeignService;

    /**
     * 办件情形标题选项关系服务定义接口
     */
    @Resource
    private QlCaseSituationTitleValRelationService qlCaseSituationTitleValRelationFeignService;

    /**
     * 事项分类表单关联
     */
    @Resource
    private SxFormInfoService sxFormInfoFeignService;

    /**
     * 事项字段填充配置
     */
    @Autowired
    private FormFieldRelConfigService formFieldRelConfigFeignService;
    /**
     * 表单填充模板集合
     */
    @Resource
    private MaterialTemplateService materialTemplateFeignService;

    /**
     * 接口提供类
     */
    @Resource
    private ApiService apiFeignService;

    /**
     * :材料附件信息实现类
     */
    @Resource
    private QlCaseAttaFileService qlCaseAttaFileFeignService;

    @Resource
    private HaServiceRegistrarManger haServiceRegistrarManger;

    @Resource
    private QlCaseApplayService qlCaseApplayService;

    @Resource
    private SysDictFeignService sysDictFeignService;

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
    private QlCaseManager qlCaseManager;

    @Resource
    private SxServiceExtendService sxServiceExtendFeginService;

    @Resource
    private QlCaseMaterialManager qlCaseMaterialManager;

    @Resource
    private QlCaseApplayManager qlCaseApplayManager;

    @Resource
    private QlCaseExtManager qlCaseExtManager;

    @Resource
    private QlCaseTitleValueRelationManager qlCaseTitleValueRelationManager;

    @Resource
    private FormManagerFeignService formManagerFeignService;

    @Resource
    private QlCaseMaterialAttaManager qlCaseMaterialAttaManager;

    @Resource
    private QlCaseMaterialAttaService qlCaseMaterialAttaServiceFeginService;
    /**
     * 办件材料信息服务定义接口
     */
    @Resource
    private QlCaseMaterialService qlCaseMaterialServiceFeginService;

    /**
     * 办件审批环节信息实现类
     */
    @Resource
    private QlCaseLinkResultManager qlCaseLinkResultManager;


    @Resource
    private SxSysAttaManager sxSysAttaManager;

    @Resource
    private SysAttaManager sysAttaManager;

    @Resource
    private HaUserResourceManager haUserResourceManager;

    @Resource
    private HaCaseExpressManager haCaseExpressManager;

    @Value("${zfsoft.inter.url}")
    private String interUrl;


    @Value("${zfsoft.inter.onekeyPush}")
    private String onekeyPushUrl;

    @Value("${zfsoft.inter.attaPush}")
    private String attaPushUrl;

    @Value("${zfsoft.inter.policyPush}")
    private String policyPushUrl;

    @Value("${zfsoft.inter.evalPush}")
    private String evalPushUrl;

    @Value("${zfsoft.inter.winNumbPush}")
    private String winNumbPush;

    @Value("${zfsoft.inter.getWeather}")
    private String getWeather;

    /**
     * redisTemplate
     */
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 将数据保存到redis中，推送到万达获取办件编码接口中，当做入参
     *
     * @param wdDataVo 过渡VO
     * @return 保存结果
     * @author wangyuhang
     * @date 2023年01月12 10:01:29
     */
    @Override
    public ApiResultSet saveData(WdDataVo wdDataVo) throws ServiceException {
        if(wdDataVo !=null){
            redisTemplate.opsForValue().set(wdDataVo.getCaseOid(), wdDataVo,
                    RedisConstants.LOGIN_SESSION_TTL, TimeUnit.HOURS);
            log.info(wdDataVo.getCaseOid(), wdDataVo);
            ApiResultSet apiResultSet = new ApiResultSet<>();
            return apiResultSet.ok("保存成功");

        }
        return new ApiResultSet<>(500,"保存失败");
    }
/********************************俞鹏start**************************/
    /**
     * 按以下参数查询事项列表
     *
     * @param serviceName 事项名称
     * @param districtOid 所属区划
     * @param organOid    所属机构
     * @param handleType  办理类型
     * @param pageNum     分页参数，页码
     * @param pageSize    分页参数，每页数量
     * @return 事项分页数据
     * @author：yupeng
     * @date：2022年07月29 14:36:57
     */
    @Override
    public ApiResultSet<PageResult<SxService>> listSxServicePage(String serviceName, String districtOid, String organOid, String handleType, Integer pageNum, Integer pageSize) {
        log.info("查询事项列表，serviceName:{},districtOid:{},organOid:{},handleType:{},pageNum:{},pageSize:{}", serviceName, districtOid, organOid, handleType, pageNum, pageSize);
        return browserFeignService.listSxServiceSortPage(serviceName, districtOid, organOid, handleType, pageNum, pageSize);
    }


    /**
     * 更新修改办件材料附件
     *
     * @param qlCaseMaterialList 材料附件集合
     * @return 保存结果
     * @author yupeng
     * @date 2022年08月16 10:01:29
     */
    @Override
    public ApiResultSet saveOrUpdateCaseMaterialAttaList(List<QlCaseMaterial> qlCaseMaterialList) {
        log.info("更新材料附件");
        Map<String, Object> modelMap = new HashMap<>();
        ApiResultSet apiResultSet = new ApiResultSet();
        List<QlCaseMaterialAtta> list = qlCaseMaterialAttaManager.saveOrUpdateCaseMaterialAttaList(qlCaseMaterialList);
        modelMap.put("qlCaseMaterialAttaList", list);
        modelMap.put("success", true);
        modelMap.put("message", "保存材料成功");
        apiResultSet.setData(modelMap);
        return apiResultSet;
    }

    /**
     * 办件受理
     *
     * @param caseOid          办件编号
     * @param userOid          用户编号
     * @param userName         用户名称
     * @param finalOpinion     受理状态，1-通过，2-不通过
     * @param finalOpinionDesc 意见说明
     * @param sjStatus         10-收件状态，2-受理状态
     * @return 受理结果
     */
    @Override
    public ApiResultSet<Map<String, String>> saveCaseAccept(String caseOid, String userOid, String userName, String finalOpinion, String finalOpinionDesc, Integer sjStatus) {
        //更新进入受理办件
        QlCase qlCase = qlCaseManager.queryQlCaseByCaseOid(caseOid);
        if (qlCase != null && qlCase.getCaseOid() != null && !"0".equals(qlCase.getCaseOid())) {
            if ("1".equals(finalOpinion)) {//受理通过
                if (sjStatus == null || sjStatus != 2) {
                    //收件状态
                    qlCase.setCaseStatus(10);
                } else {
                    //受理状态
                    qlCase.setCaseStatus(2);
                }
            } else {
                qlCase.setCaseStatus(7);
                qlCase.setConcludeDate(new Date());
            }
            qlCase.setAcceptanceDate(new Date());
            qlCaseManager.saveQlCase(qlCase);
            // 办件申请信息(QlCaseApply)实体类
            QlCaseApplay qlCaseApplay = qlCaseApplayManager.queryQlCaseApplayByCaseOid(caseOid);
            // 办件信息表(QlCaseExt)实体类
            QlCaseExt qlCaseExt = qlCaseExtManager.queryQlCaseExtByCaseOid(caseOid);
        }
        //判断是否已存在记录  如果是就删除
        Map<String, String> map = null;
        QlCaseLinkResult result = qlCaseLinkResultManager.querySlQlCaseLinkResultByCaseOid(caseOid);
        if (result != null) {
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
        if (sjStatus == null || sjStatus != 2) {
            //收件状态
            qlCaseLinkResult.setLinkCode("SJ");
            qlCaseLinkResult.setLinkName("收件");
        } else {
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

    /********************************俞鹏end**************************/


    /********************************王宇航start**************************/


    /**
     * @param districtOid 所属区划
     * @param handleType  办理类型
     * @return ApiResultSet<List < Map < String, Object>>> 根据区划及事项办理类型查询机构列表详细信息
     * @description 根据区划及事项办理类型查询机构列表
     * @author wangyh
     * @date 2022-07-29 10:07
     */
    @Override
    public ApiResultSet<List<Map<String, Object>>> listOrganByDistrictAndService(String districtOid, String handleType) {
        log.info("根据区划及事项办理类型查询机构列表， districtOid:{},handleType:{}", districtOid, handleType);
        ApiResultSet<List<Map<String, Object>>> apiResultSet = browserFeignService.listOrganByDistrictAndService(districtOid, handleType);
        return apiResultSet;
    }

    /**
     * @param serviceOid 事项编号
     * @return piResultSet<List < SxServiceOptionTitle>> 根据serviceOid获取所属事项的所有没有任何关系的选项标题
     * @description 根据serviceOid获取所属事项的所有没有任何关系的选项标题(智能登记)
     * @author wangyh
     * @date 2022-07-29 10:07
     */
    @Override
    public ApiResultSet<List<SxServiceOptionTitle>> getSxServiceTitlesNoRelation(String serviceOid) {
        log.info("根据serviceOid获取所属事项的所有没有任何关系的选项标题， serviceOid:{}", serviceOid);
        ApiResultSet<List<SxServiceOptionTitle>> apiResultSet = sxServiceFeginService.getSxServiceTitlesNoRelation(serviceOid);
        return apiResultSet;
    }

    /**
     * @param serviceOid 事项主键
     * @param caseOid
     * @param optionOids
     * @return ApiResultSet 根据情形选项获取表单字段及预填详细信息
     * @description 根据情形选项获取表单字段及预填信息
     * @author wangyh
     * @date 2022-08-01 10:07
     **/
    @Override
    public ApiResultSet getFormFillInfos(String serviceOid, String caseOid, String optionOids) {
        log.info("根据情形选项获取表单字段及预填详细信息， serviceOid:{},caseOid:{},optionOids:{}", serviceOid, caseOid, optionOids);
        if (StrUtil.isEmpty(optionOids)) {
            ApiResultSet<List<Map<String, String>>> apiResultSet = qlCaseSituationTitleValRelationFeignService.getCaseTitleValueList(caseOid);
            if (null != apiResultSet && null != apiResultSet.getData()) {
                List<Map<String, String>> result = apiResultSet.getData();
                String oids = "";
                for (Map<String, String> map : result) {
                    if (StrUtil.isEmpty(map.get("valueOid"))) {
                        continue;
                    }
                    oids += map.get("valueOid") + ",";
                }
                optionOids = oids;
            }
        }
        return sxFormInfoFeignService.getZcFormInfoList(serviceOid, caseOid, optionOids);
    }

    /**
     * @param caseOid
     * @return ApiResultSet<Map < String, Object>> 获取表单预填信息详细信息
     * @description 表单预填信息
     * @author wangyh
     * @date 2022-08-01 10:07
     **/
    @Override
    public ApiResultSet<Map<String, Object>> getAllBasicFieldByOid(String caseOid) {
        log.info("获取表单预填信息详细信息， caseOid:{}", caseOid);
        return formFieldRelConfigFeignService.getAllBasicFieldByOid(caseOid);
    }

    @Override
    public ApiResultSet<JSONArray> getBasicAndFormFieldRel(String serviceOid) {
        JSONArray jsonArray = new JSONArray();
        ApiResultSet<JSONArray> apiResultSet = new ApiResultSet<>();
        List<DbFormFieldRelConfig> dbFormFieldRelConfigList = formFieldRelConfigManager.getFormFieldRelConfigByType(serviceOid, 1);
        if (dbFormFieldRelConfigList != null && dbFormFieldRelConfigList.size() > 0) {
            for (DbFormFieldRelConfig dbFormFieldRelConfig : dbFormFieldRelConfigList) {
                JSONObject jsonObject = new JSONObject();
                String basicFormFieldOid = dbFormFieldRelConfig.getBasicFormFieldOid(); //基础表单
                String fillFieldOid = dbFormFieldRelConfig.getFillFieldOid(); //电子表单
                if (com.zfsoft.cases.util.StringUtils.isNotEmpty(basicFormFieldOid) && com.zfsoft.cases.util.StringUtils.isNotEmpty(fillFieldOid)) {
                    ApiResultSet<BaseFormField> baseFormFieldApiResultSet = basicFormFieldFeginService.getOneByOid(basicFormFieldOid);
                    if (baseFormFieldApiResultSet != null && baseFormFieldApiResultSet.getData() != null) {
                        BaseFormField baseFormField = baseFormFieldApiResultSet.getData();
                        jsonObject.put("baseFormField", baseFormField.getFieldKey());
                    }

                    ApiResultSet<SxFillField> sxFillFieldApiResultSet = sxFillFieldFeginService.getDbSxFillFieldByOid(fillFieldOid);
                    if (sxFillFieldApiResultSet != null && sxFillFieldApiResultSet.getData() != null) {
                        //需要转化驼峰字段
                        String formFieldCode = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, sxFillFieldApiResultSet.getData().getFieldCode());
                        jsonObject.put("formField", formFieldCode);
                    }
                    jsonArray.add(jsonObject);
                }
            }
        }
        apiResultSet.setData(jsonArray);
        return apiResultSet;
    }

    /**
     * 已办办件列表
     *
     * @param caseNumber  办件业务编码
     * @param serviceName 机构主键
     * @param beginDate   时间段-开始
     * @param endDate     时间段-结束
     * @param pageNum
     * @param pageSize
     * @return ApiResultSet<PageResult < QlCase>> 获取已办办件列表详情
     * @author wangyh
     * @date 2022-8-11 10:29:01
     **/
    @Override
    public ApiResultSet<PageResult<QlCase>> queryDoneQlCasePage(String caseNumber, String serviceName, String beginDate, String endDate, Integer pageNum, Integer pageSize) {
        ApiResultSet<PageResult<QlCase>> apiResultSet = new ApiResultSet<>();
        QlCaseVo doneCaseVo = new QlCaseVo();
        doneCaseVo.setCaseNumber(caseNumber);
        doneCaseVo.setServiceName(serviceName);
        if (beginDate != null && beginDate != "") {
            doneCaseVo.setStartDate(DateUtil.startDateFormat(beginDate));
        }
        if (endDate != null && endDate != "") {
            doneCaseVo.setEndDate(DateUtil.endDateFormat(endDate));
        }
        PageResult<QlCase> pageResult = qlCaseManager.queryDoneQlCasePage(doneCaseVo, pageNum, pageSize);
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    /**
     * @param qlCaseMaterialList
     * @return ApiResultSet<List < Map < String, String>>> 更新材料关联
     * @description 更新材料关联
     * @author wangyh
     * @date 2022-8-11 10:29:01
     */
    @Override
    public ApiResultSet<List<Map<String, String>>> updateQlCaseMaterialListForBrowser(List<QlCaseMaterial> qlCaseMaterialList) {
        ApiResultSet<List<Map<String, String>>> apiResultSet = qlCaseMaterialServiceFeginService.updateQlCaseMaterialListForBrowser(qlCaseMaterialList);
        return apiResultSet;
    }
    /********************************王宇航end**************************/


    /********************************赵冰峰start**************************/
    /**
     * 根据serviceOid和选项值获取所属事项的所有有关系的选项标题
     *
     * @param serviceOid      获取事项有关的选项标题
     * @param valOids
     * @param currentOid
     * @param currentTitleOid
     * @param rootTitleOid
     * @return
     */
    @Override
    public ApiResultSet<List<SxServiceOptionTitle>> querySxServiceSituationRelation(String serviceOid, String valOids, String currentOid, String currentTitleOid, String rootTitleOid) {
        log.info("获取事项有关的选项标题， serviceOid:{},valOids:{},currentOid:{},currentTitleOid:{},rootTitleOid:{}", serviceOid, valOids, currentOid, currentTitleOid, rootTitleOid);
        ApiResultSet<List<SxServiceOptionTitle>> apiResultSet = sxServiceFeginService.querySxServiceSituationRelation(serviceOid, valOids, currentOid, currentTitleOid, rootTitleOid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<ServiceMaterialVo>> getSituationMaterialListByOids(String serviceOid, String optionValOids) {
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
        ApiResultSet<List<ServiceMaterialVo>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(materialVoList);
        return apiResultSet;
    }

    /**
     * 实施清单的信息
     *
     * @param serviceOid
     * @return
     */
    @Override
    public ApiResultSet viewSxService(String serviceOid) {
        log.info("实施清单的信息， serviceOid:{}", serviceOid);
        ApiResultSet apiResultSet = sxServiceFeginService.viewSxService(serviceOid);
        return apiResultSet;
    }

    /**
     * 获取表单填充模板集合
     *
     * @param reportOid  办件OID
     * @param serviceOid 事项信息id
     * @return
     */
    @Override
    public ApiResultSet<List<TermlateDataSuperDto>> getTemplateList(String reportOid, String serviceOid) {
        ApiResultSet<List<TermlateDataDto>> templateListData = materialTemplateFeignService.getTemplateList(reportOid, serviceOid);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        List<TermlateDataSuperDto> list = new ArrayList<>();
        List<TermlateDataDto> templateList = templateListData.getData();
        for (TermlateDataDto termlateDataDto : templateList) {
            TermlateDataSuperDto dto = new TermlateDataSuperDto();
            BeanUtils.copyProperties(termlateDataDto, dto);
            list.add(dto);
        }
        apiResultSet.setData(list);
        return apiResultSet;
    }

    /**
     * @param serviceOids 事项编号
     * @description: 获取用户组列表
     * @author zhaobf
     * @Date: 2022/8/4 10:30
     */
    @Override
    public ApiResultSet serviceType(String serviceOids) {
        log.info("进入用户服务权限，入参serviceOids:{}", serviceOids);
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        List<HaUserService> haUserServices = haServiceRegistrarManger.queryUserServiceByServiceIdsAndUserId(serviceOids, currentHaLoginUserInfo.getId());
        return ApiResultSet.ok("用户服务权限请求成功", haUserServices);
    }

    /**
     * @param caseOid
     * @return
     * @description 根据办件业务主键查询办件申请信息
     * @author: zhaobf
     * @date: 2022-08-05 10:49
     */
    @Override
    public ApiResultSet queryQlCaseApplayByCaseOid(String caseOid) {
        ApiResultSet<QlCaseApplay> apiResultSet = qlCaseApplayService.queryQlCaseApplayByCaseOid(caseOid);
        return apiResultSet;
    }


    @Override
    public ApiResultSet<List<Map<String, String>>> getCaseTitleValueList(String caseOid) {
        ApiResultSet<List<Map<String, String>>> apiResultSet = qlCaseSituationTitleValRelationFeignService.getCaseTitleValueList(caseOid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<SysDict>> getSelectCertificateType(int type) {
        ApiResultSet<SysDict> dictResult = null;
        ApiResultSet<List<SysDict>> listApiResultSet = null;
        if (type == 1) {
            dictResult = sysDictFeignService.getSysDictByCode(BaseStaticParameter.ZZLX_GR_CODE);
        } else {
            dictResult = sysDictFeignService.getSysDictByCode(BaseStaticParameter.ZZLX_FR_CODE);
        }
        if (dictResult != null) {
            SysDict sysDict = dictResult.getData();
            if (sysDict != null) {
                listApiResultSet = sysDictFeignService.querySysDictListByParentOid(sysDict.getDictOid());
            }
        }
        ApiResultSet<List<SysDict>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(listApiResultSet != null ? listApiResultSet.getData() : null);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<PageResult<QlCase>> queryQlCaseByCredentialNumber(String credentialNumber, String applyUserType, String projectName, String caseNumber, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PageResult<QlCase> list = qlCaseManager.queryQlCaseApplayByCredentialNumber(credentialNumber, applyUserType, projectName, caseNumber, pageNum, pageSize);
        return new ApiResultSet<>(list);
    }

    @Override
    public ApiResultSet<List<SxFormInfoVo>> queryFormInfoByCaseOid(String caseOid) {
        List<SxFormInfoVo> sxFormInfoVos = new LinkedList<SxFormInfoVo>();
        QlCase qlCase = qlCaseManager.queryQlCaseByCaseOid(caseOid);
        String formOids = qlCase.getFormOids();
        if (formOids != null) {
            JSONArray array = JSON.parseArray(formOids);
            for (Object obj : array) {
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

    /**
     * 办件更新表单信息
     *
     * @param qlCase
     * @return com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.data.vo.QlCaseVo>
     * @author zhaobf
     * @date 2022/9/2 16:16
     **/
    @Override
    public ApiResultSet<Map<String, Object>> updateQlCaseFormInfo(QlCase qlCase) {
        Map<String, Object> map = qlCaseManager.updateQlCaseFormInfo(qlCase);
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(map);
        return apiResultSet;
    }

    @Override
    public ApiResultSet onekeyPush(OnekeyPushVo pushVo) {
        try {
            String url = interUrl + onekeyPushUrl ;
            log.info("一键推送:pushVo：{},url:{}" ,pushVo,url);
            Map<String,Object> map = JSONObject.parseObject(JSON.toJSONString(pushVo));
            String body = CommonRestUtil.sendPostString(url,map);
            log.info("一键推送调用结果:body={}" ,body);
            JSONObject jsonObject = JSONObject.parseObject(body);
            if (!"200".equals(jsonObject.get("code").toString())) {
                return new ApiResultSet(500,"一键推送失败",null);
            }else{
                return ApiResultSet.ok("一键推送成功",null);
            }
        } catch (Exception e) {
            log.info("一键推送调用错误:" + e.getMessage());
            e.printStackTrace();
            return new ApiResultSet(500,"一键推送失败",null);
        }
    }

    /**
     * 号票推送
     * @param whWinNumbVO
     * @return
     */
    @Override
    public ApiResultSet winNumbPush( WinNumbVO whWinNumbVO) {
        try {
            String url = interUrl + winNumbPush ;
            log.info("号票推送:winNumbVO：{},url:{}" ,whWinNumbVO,url);
            Map<String,Object> map = JSONObject.parseObject(JSON.toJSONString(whWinNumbVO));
            String body = CommonRestUtil.sendPostString(url,map);
            log.info("号票推送调用结果:body={}" ,body);
            JSONObject jsonObject = JSONObject.parseObject(body);
            if (!"200".equals(jsonObject.get("code").toString())) {
                return new ApiResultSet(500,"号票推送失败",null);
            }else{
                return ApiResultSet.ok("号票推送成功",null);
            }
        } catch (Exception e) {
            log.info("一键推送调用错误:" + e.getMessage());
            e.printStackTrace();
            return new ApiResultSet(500,"号票推送失败",null);
        }
    }

    @Override
    public ApiResultSet getWeather() {
        try {
            String url = interUrl + getWeather ;
            log.info("天气获取错误:,url:{}" ,url);
            String body = CommonRestUtil.sendGet(url);
            log.info("天气获取错误调用结果:body={}" ,body);
            JSONObject jsonObject = JSONObject.parseObject(body);
            if (!"200".equals(jsonObject.get("code").toString())) {
                return new ApiResultSet(500,"天气获取错误",null);
            }else{
                return ApiResultSet.ok("天气获取成功",JSON.parseObject(body, HashMap.class).get("data"));
            }
        } catch (Exception e) {
            log.info("天气获取错误:" + e.getMessage());
            e.printStackTrace();
            return new ApiResultSet(500,"天气获取失败",null);
        }
    }

    @Override
    public ApiResultSet<SxSysAtta> getSxSysAttaByOid(String oid) {

        ApiResultSet<SxSysAtta> apiResultSet = new ApiResultSet<>();
        SxSysAtta sxSysAttaByOid = sxSysAttaManager.getSxSysAttaByOid(oid);
//            JSONArray array = jsonObject.getJSONArray("data");
        apiResultSet.setData(sxSysAttaByOid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet attaPush(@Valid HaAttaPushRequestData haAttaPushRequestData) {
        try {
            String url = interUrl + attaPushUrl ;
            log.info("附件推送:,url:{}" ,url);
            String s = JSON.toJSONString(haAttaPushRequestData);
            String body = ClientServer.send(url,s,"utf-8");
            log.info("附件推送调用结果:body={}" ,body);
            JSONObject jsonObject = JSONObject.parseObject(body);
            if (!"200".equals(jsonObject.get("code").toString())) {
                return new ApiResultSet(500,"附件推送错误",null);
            }else{
                return ApiResultSet.ok("附件推送成功",JSON.parseObject(body, HashMap.class).get("data"));
            }
        } catch (Exception e) {
            log.info("附件推送错误:" + e.getMessage());
            e.printStackTrace();
            return new ApiResultSet(500,"附件推送失败",null);
        }
    }

    @Override
    public ApiResultSet policyPush(@RequestBody  @Valid HaPolicyPushRequestData haPolicyPushRequestData) {
        try {
            String url = interUrl + policyPushUrl ;
            log.info("政策推送:,url:{}" ,url);
            String s = JSON.toJSONString(haPolicyPushRequestData);
//            String body = ClientServer.send(url,s,"utf-8");
//            log.info("政策推送调用结果:body={}" ,body);
//            JSONObject jsonObject = JSONObject.parseObject(body);
//            if (!"200".equals(jsonObject.get("code").toString())) {
//                return new ApiResultSet(500,"政策推送错误",null);
//            }else{
//                return ApiResultSet.ok("政策推送成功",JSON.parseObject(body, HashMap.class).get("data"));
//            }
            return ApiResultSet.ok("政策推送成功",s);
        } catch (Exception e) {
            log.info("附件推送错误:" + e.getMessage());
            e.printStackTrace();
            return new ApiResultSet(500,"政策推送失败",null);
        }
    }

    @Override
    public ApiResultSet evalPush(@RequestBody  @Valid HaEvalPushRequestData haEvalPushRequestData) {
        try {
            String url = interUrl + evalPushUrl ;
            log.info("评价推送:,url:{}" ,url);
            String s = JSON.toJSONString(haEvalPushRequestData);
            String body = ClientServer.send(url,s,"utf-8");
            log.info("评价推送调用结果:body={}" ,body);
            JSONObject jsonObject = JSONObject.parseObject(body);
            if (!"200".equals(jsonObject.get("code").toString())) {
                return new ApiResultSet(500,"评价推送错误",null);
            }else{
                return ApiResultSet.ok("评价推送成功",JSON.parseObject(body, HashMap.class).get("data"));
            }
//            return ApiResultSet.ok("政策推送成功",s);
        } catch (Exception e) {
            log.info("附件推送错误:" + e.getMessage());
            e.printStackTrace();
            return new ApiResultSet(500,"评价推送失败",null);
        }
    }


    @Override
    public ApiResultSet resourcePush(@RequestBody  @Valid HaResourcePushRequestData haResourcePushRequestData) {
        try {
            HaResourcePushResponseData result = new HaResourcePushResponseData();
            BeanUtils.copyProperties(haResourcePushRequestData,result);
            String url = interUrl + evalPushUrl ;
            log.info("资料库推送:,url:{}" ,url);
            String s = JSON.toJSONString(haResourcePushRequestData);
            HaUserResourceResponseVoData resourceVo = haUserResourceManager.getHaUserResourceVo(haResourcePushRequestData.getResourceId());
            result.setHaUserResourceResponseVoData(resourceVo);
//            String body = ClientServer.send(url,s,"utf-8");
//            log.info("资料库调用结果:body={}" ,body);
//            JSONObject jsonObject = JSONObject.parseObject(body);
//            if (!"200".equals(jsonObject.get("code").toString())) {
//                return new ApiResultSet(500,"资料库错误",null);
//            }else{
//                return ApiResultSet.ok("资料库成功",JSON.parseObject(body, HashMap.class).get("data"));
//            }
            return ApiResultSet.ok("资料库成功",result);
        } catch (Exception e) {
            log.info("资料库推送错误:" + e.getMessage());
            e.printStackTrace();
            return new ApiResultSet(500,"资料库推送失败",null);
        }
    }

    /********************************赵冰峰end**************************/


    /********************************康翱翔start**************************/

    /**
     * @param designOid 设计主键
     * @param reportOid 表单数据的主键 （API方式可以为null）
     * @description: 根据设计主键和表单数据主键获取数据
     * @author: gaolh
     * @Date: 2021/4/22 13:07
     **/
    @Override
    public ApiResultSet<FormDataVo> getFormData(String authorizeKey, String designOid, String reportOid) {
        return formManagerFeignService.getFormData(authorizeKey, designOid, reportOid);
    }

    /**
     * @param formDataVo authorizeKey 授权key
     * @param formDataVo formMainOid 设计主表的业务主键
     * @param formDataVo designOid 设计表的业务主键
     * @param formDataVo formData 存储对象数据的JSON
     * @description: 存储对象的数据的保存本地、数据库、接口返回
     * @author: gaolh
     * @Date: 2021/4/22 13:07
     **/
    @Override
    public ApiResultSet saveFormData(FormDataVo formDataVo) {
        return formManagerFeignService.saveFormData(formDataVo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResultSet<Map<String, Object>> nextStepSaveQlCase(Object object) {
        Map<String, Object> map = new HashMap<String, Object>();
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        if (object != null) {
            //登录信息
            HaLoginUserInfo loginUser = HaLoginUserHolder.getCurrentHaLoginUserInfo();
            JSONObject jsonObject = JSONObject.parseObject(JSONArray.toJSON(object).toString());
            //解析转化办件信息
            QlCase qlCase = JSON.parseObject(jsonObject.get("qlCase").toString(), QlCase.class);
            //解析转化办件申请信息
            QlCaseApplay qlCaseApplay = JSON.parseObject(jsonObject.get("qlCaseApply").toString(), QlCaseApplay.class);
            //解析转化办件扩展信息
            QlCaseExt qlCaseExt = JSON.parseObject(jsonObject.get("qlCaseExt").toString(), QlCaseExt.class);
            //解析转化材料信息
            JSONArray materialList = jsonObject.getJSONArray("materials");
            //解析转化办件与情形标题选项关系
            JSONArray titleValueList = JSONArray.parseArray(jsonObject.get("titleValues").toString());

            qlCase.setSourceApp(1);
            if (StrUtil.isNotBlank(qlCase.getCaseOid())) {
                QlCase qlCaseOld = qlCaseManager.queryQlCaseByCaseOid(qlCase.getCaseOid());
                if (null == qlCaseOld.getShouldConcludeDate() && null != qlCaseOld.getCreateDate()) {
                    SxServiceExtend extend = sxServiceExtendFeginService.getSxServiceExtendByServiceOid(qlCase.getServiceOid()).getData();
                    if (null != extend) {
                        if (null == qlCase.getShouldConcludeDate() && null != extend.getPromiseLimitType() && !extend.getPromiseLimitType().equals("")) {
                            Integer limit = extend.getPromiseLimit().intValue();
                            Date getDate = new Date();
                            if (extend.getPromiseLimitType().equals("H")) {
                                getDate = LimitDateCalc.dateCalc(qlCaseOld.getCreateDate(), 0, extend.getPromiseLimitType(), limit);
                            } else {
                                getDate = LimitDateCalc.dateCalc(qlCaseOld.getCreateDate(), limit, extend.getPromiseLimitType(), 0);
                            }
                            qlCase.setShouldConcludeDate(getDate);
                        }
                    }
                }
            } else {
                /*SxServiceExtend extend = sxServiceExtendFeginService.getSxServiceExtendByServiceOid(qlCase.getServiceOid()).getData();
                if (null != extend  &&  extend.getPromiseLimit()!=null) {
                    Integer limit = extend.getPromiseLimit().intValue();
                    Date getDate = new Date();
                    if (extend.getPromiseLimitType().equals("H")) {
                        getDate = LimitDateCalc.dateCalc(getDate, 0, extend.getPromiseLimitType(), limit);
                    } else {
                        getDate = LimitDateCalc.dateCalc(getDate, limit, extend.getPromiseLimitType(), 0);

                    }
                    qlCase.setShouldConcludeDate(getDate);
                }*/
            }

            // 赋值创建用户
            if (qlCase.getSourceType() == 9) {
                qlCase.setCreateUserOid(loginUser.getId().toString());
            }

            //办件基础信息
            Map<String, String> qlcaseMap = qlCaseManager.saveQlCase(qlCase);
            if (qlcaseMap != null) {
                String caseOid = qlcaseMap.get("caseOid");
                String caseNumber = qlcaseMap.get("caseNumber");
                map.put("caseNumber", caseNumber);
                String serviceTypeName = qlcaseMap.get("serviceTypeName");
                map.put("serviceTypeName", serviceTypeName);
                String caseStatus = qlcaseMap.get("caseStatus");
                map.put("caseStatus", caseStatus);
                String createDate = qlcaseMap.get("createDate");
                map.put("createDate", createDate);
                if (StringUtil.isNotEmpty(caseOid)) {
                    map.put("caseOid", caseOid);
                    if (StringUtil.isEmpty(qlCaseApplay.getCaseOid())) {
                        qlCaseApplay.setCaseOid(caseOid);
                        qlCaseExt.setCaseOid(caseOid);
                    }
                    if (materialList.size() > 0) {
                        //查询是否已保存办件材料关系
                        List<QlCaseMaterial> list = qlCaseMaterialManager.queryQlCaseMaterialByCaseOid(caseOid);
                        //保存的数量与查询一致
                        boolean flag = false;
                        if (list.size() == materialList.size()) {
                            flag = false;
                        } else {
                            flag = true;
                            for (QlCaseMaterial qlCaseMaterial : list) {
                                qlCaseMaterialManager.deleteQlCaseMaterial(qlCaseMaterial.getId());
                            }
                        }
                        if (flag) {
                            //保存办件材料关系信息
                            List<QlCaseMaterial> qlCaseMaterials = new ArrayList<QlCaseMaterial>();
                            if (materialList.size() > 0) {
                                for (int i = 0; i < materialList.size(); i++) {
                                    ServiceMaterialVo serviceMaterialVo = JSON.parseObject(materialList.get(i).toString(), ServiceMaterialVo.class);
                                    QlCaseMaterial qlCaseMaterial = new QlCaseMaterial();
                                    qlCaseMaterial.setMaterialOid(serviceMaterialVo.getMaterialOid());
                                    qlCaseMaterial.setMaterialName(serviceMaterialVo.getMaterialName());
                                    qlCaseMaterial.setCaseOid(caseOid);
                                    qlCaseMaterial.setCollectionFlag(0);
                                    qlCaseMaterial.setCollectionNumber(0);
                                    qlCaseMaterial.setCreateUserOid(loginUser.getId().toString());
                                    qlCaseMaterial.setMaterialCatalogOid(serviceMaterialVo.getMaterialCatalogOid());
                                    qlCaseMaterial.setMaterialSampleAddr(serviceMaterialVo.getMaterialSampleOid());
                                    qlCaseMaterial.setMaterialSampleAddrYl(serviceMaterialVo.getMaterialSampleUrl());
                                    if (StringUtil.isEmpty(qlCaseMaterial.getMaterialSampleAddr())) {
                                        qlCaseMaterial.setMaterialSampleAddr(serviceMaterialVo.getMaterialSampleAddr());
                                        qlCaseMaterial.setMaterialSampleAddrYl(serviceMaterialVo.getMaterialSimpleAddrYl());
                                    }
                                    qlCaseMaterial.setAuditType(serviceMaterialVo.getAuditType());
                                    if (null != serviceMaterialVo.getMaterialMustFlag()) {
                                        qlCaseMaterial.setMustFlag(Short.parseShort(String.valueOf(serviceMaterialVo.getMaterialMustFlag())));
                                    }
                                    qlCaseMaterial.setModifyDate(new Date());
                                    qlCaseMaterials.add(qlCaseMaterial);
                                }
                            }
                            List<Map<String, String>> caseMaterialmap = qlCaseMaterialManager.saveBatchQlCaseMaterial(qlCaseMaterials);
                            if (caseMaterialmap != null) {
                                map.put("caseMaterialOids", caseMaterialmap);
                            }
                        }
                    }
                    //办件申请信息
                    QlCaseApplay applay = qlCaseApplayManager.queryQlCaseApplayByCaseOid(caseOid);
                    if (applay != null) {
                        qlCaseApplay.setId(applay.getId());
                    }
                    Map<String, Integer> applymap = qlCaseApplayManager.saveQlCaseApplay(qlCaseApplay);
                    if (applymap != null) {
                        String applyOid = applymap.get("applyOid").toString();
                        map.put("applyOid", applyOid);
                    }
                    //办件扩展信息
                    QlCaseExt caseExt = qlCaseExtManager.queryQlCaseExtByCaseOid(caseOid);
                    if (caseExt != null) {
                        qlCaseExt.setId(caseExt.getId());
                    }
                    Map<String, Integer> extmap = qlCaseExtManager.saveQlCaseExt(qlCaseExt);
                    if (extmap != null) {
                        String extOid = extmap.get("extOid").toString();
                        map.put("extOid", extOid);
                    }
                    //办件情形标题选项关系
                    if (titleValueList.size() > 0) {
                        //查询是否已保存办件情形标题选项关系
                        List<QlCaseSituationTitleValRelation> titleValRelations = qlCaseTitleValueRelationManager.queryTitleValueRelationByCaseOid(caseOid);
                        if (titleValRelations.size() > 0) {
                            for (QlCaseSituationTitleValRelation relation : titleValRelations) {
                                qlCaseTitleValueRelationManager.deleteQlCaseTitleValueRelation(relation.getId());
                            }
                        }
                        //插入新数据
                        List<QlCaseSituationTitleValRelation> relations = new ArrayList<QlCaseSituationTitleValRelation>();
                        for (int i = 0; i < titleValueList.size(); i++) {
                            QlCaseSituationTitleValRelation relation = JSON.parseObject(titleValueList.get(i).toString(), QlCaseSituationTitleValRelation.class);
                            relation.setCaseOid(caseOid);
                            relations.add(relation);
                        }
                        List<Map<String, String>> relationsmap = qlCaseTitleValueRelationManager.saveQlCaseTitleValueRelations(relations);
                        if (relationsmap != null) {
                            map.put("relationOids", relationsmap);
                        }
                    }
                }
            }
        }
        apiResultSet.setData(map);
        return apiResultSet;
    }

    /**
     * @description: 专属指南
     * @params：[ serviceOid 事项主键, optionValOids 选项值]
     * @author: kangax
     * @date: 2022-08-01 12:01
     */
    @Override
    public ApiResultSet<Map<String, Object>> getExclusiveGuide(String serviceOid, String optionValOids) {
        log.info("进入查询专属指南信息Controller,参数 serviceOid {} , optionValOids: {} ", serviceOid, optionValOids);
        ApiResultSet<Map<String, Object>> apiResultSet = sxServiceMaterialFeignService.getExclusiveGuide(serviceOid, optionValOids);
        return apiResultSet;
    }

    /**
     * @description: 查询材料信息
     * @params：[ caseOid 办件编号]
     * @return: 材料详细信息list
     * @author: kangax
     * @date: 2022-08-02 10:20
     */
    @Override
    public ApiResultSet<Map<String, List<QlCaseMaterial>>> queryQlCaseMaterialListByCaseOid(String caseOid) {
        log.info("进入查询材料信息Controller，参数caseOid：{} ", caseOid);
        try {
            //TODO zbf  可能是一级缓存脏数据，先休眠处理
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ApiResultSet<Map<String, List<QlCaseMaterial>>> apiResultSet = apiFeignService.queryQlCaseMaterialListByCaseOid(caseOid);
        Map<String, List<QlCaseMaterialVo>> dataVo = new HashMap<>();
        Map<String, List<QlCaseMaterial>> data = apiResultSet.getData();
        Set<String> strings = data.keySet();
        for(String str : strings) {
            List<QlCaseMaterialVo> list = new ArrayList<>();
            List<QlCaseMaterial> qlCaseMaterials = data.get(str);
            for (QlCaseMaterial e1 : qlCaseMaterials) {
                QlCaseMaterialVo vo = new QlCaseMaterialVo();
                BeanUtils.copyProperties(e1, vo);
                if (vo.getMaterialSampleAddr() != null) {
                    SxSysAtta sxSysAttaByOid = sxSysAttaManager.getSxSysAttaByOid(vo.getMaterialSampleAddr());
                    vo.setSimpleOriginName(sxSysAttaByOid.getOriginName());
                }
                if (vo.getMaterialEmptyAttoid() != null) {
                    SxSysAtta sxSysAttaByOid = sxSysAttaManager.getSxSysAttaByOid(vo.getMaterialEmptyAttoid());
                    vo.setEmptyOriginName(sxSysAttaByOid.getOriginName());
                }
                list.add(vo);
            }
            dataVo.put(str, list);
        }

        return new ApiResultSet(dataVo);
    }
    /**
     * @description: 查询材料信息
     * @params：[ caseOid 办件编号]
     * @return: 材料详细信息list
     * @author: kangax
     * @date: 2022-08-02 10:20S
     */
    @Override
    public ApiResultSet<Map<String, List<QlCaseMaterial>>> querySimpleQlCaseMaterialListByCaseOid(String caseOid) {
        log.info("进入查询材料信息Controller，参数caseOid：{} ", caseOid);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 查询办件所有材料
        ApiResultSet<Map<String, List<QlCaseMaterial>>> apiResultSet = qlCaseMaterialServiceFeginService.queryQlCaseMaterialListByCaseOid(caseOid);
        return apiResultSet;
    }


    /**
     * @description: 查询材料信息
     * @params：[ caseOid 办件编号]
     * @return: 材料详细信息list
     * @author: kangax
     * @date: 2022-08-02 10:20S
     */
    @Override
    public ApiResultSet<List<QlCaseMaterialAtta>> queryQlCaseMaterialAttaByCaseMaterialOid(String caseMaterialOid) {
        log.info("进入查询材料信息Controller，caseMaterialOid：{} ", caseMaterialOid);
        // 查询办件所有材料
        ApiResultSet<List<QlCaseMaterialAtta>> materialAttas=qlCaseMaterialAttaServiceFeginService.queryQlCaseMaterialAttaByCaseMaterialOid(caseMaterialOid);
        if(materialAttas.getCode()==200&&materialAttas.getData()!=null&&materialAttas.getData().size()>0){
            for(QlCaseMaterialAtta attaMa:materialAttas.getData()){
                if(StrUtil.isNotEmpty(attaMa.getAttaOid())) {
                    QlSysAtta atta = sysAttaManager.querySysAttaByOid(attaMa.getAttaOid());
                    attaMa.setQlSysAtta(atta);
                }
            }

        }

        return materialAttas;
    }
    /**
     * @description: 材料附件上传
     * @params：[ request, files 文件]
     * @return: <List<QlSysAtta> 上传文件返回类
     * @author: kangax
     * @date: 2022-08-02 10:27
     */
    @Override
    public ApiResultSet<List<QlSysAtta>> uploadCaseMaterialFile(HttpServletRequest request, MultipartFile[] files) {
        ApiResultSet<List<QlSysAtta>> apiResultSet = qlCaseAttaFileFeignService.uploadCaseMaterialFile(request, files);
        return apiResultSet;
    }

    /**
     * @description: 根据caseNumber的办件信息
     * @param caseNumber
     * @author: kangax
     * @date: 2022-09-19 09:45
     */
    @Override
    public ApiResultSet queryQlCaseByCaseNumber(String caseNumber) {
        ApiResultSet<QlCase> apiResultSet = qlCaseServiceFeginService.queryQlCaseByCaseNumber(caseNumber);
        return apiResultSet;
    }


    /**
     * @description: 根据业务主键查询办件信息
     * @param caseOid
     * @author: kangax
     * @date: 2022-09-19 09:45
     */
    @Override
    public ApiResultSet queryQlCaseByCaseOid(String caseOid) {
        // 办件信息
        ApiResultSet<QlCase> qlCaseApiResultSet = qlCaseServiceFeginService.queryQlCaseByCaseOid(caseOid);
        return qlCaseApiResultSet;
    }

    /**
     * @description:办件业务主键获取办件材料
     * @param caseOid
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     * @author: kangax
     * @date: 2022-09-19 10:19
     */
    @Override
    public ApiResultSet queryQlCaseMaterialByCaseOid(String caseOid) {
        ApiResultSet<List<QlCaseMaterial>> apiResultSet = qlCaseMaterialServiceFeginService.queryQlCaseMaterialByCaseOid(caseOid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet sendHPSms(String title, String phone, String message, String workUserId, String workUserName) {
        try {
            SendHPSmsUtil.sendHPSms(title,phone,message,workUserId,workUserName);
            return ApiResultSet.ok("短信发送成功");
        }catch (Exception e){
            return new ApiResultSet<String>(500,"短信发送失败",null);
        }
    }

    @Override
    public ApiResultSet<HaCaseExpress> queryHaCaseExpressByQlCaseId(String qlCaseId) {
        HaCaseExpress haCaseExpress = haCaseExpressManager.selectByQlCaseId(qlCaseId);
        ApiResultSet<HaCaseExpress> apiResultSet = new ApiResultSet<>();
        if(haCaseExpress!=null){
            apiResultSet.setData(haCaseExpress);
        }
        return apiResultSet;
    }
/*******************************康翱翔end**************************/

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
                //返回值增加其他来源
                serviceMaterialVo.setOtherMaterialSource(sxServiceMaterial.getOtherMaterialSource());
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
                serviceMaterialVo.setMaterialSort(sxServiceMaterial.getMaterialSort());
                if(sxServiceMaterial.getMaterialSampleAddr()!=null){
                    SxSysAtta sxSysAttaByOid = sxSysAttaManager.getSxSysAttaByOid(sxServiceMaterial.getMaterialSampleAddr());
                    serviceMaterialVo.setSimpleOriginName(sxSysAttaByOid.getOriginName());
                }
                if(sxServiceMaterial.getMaterialEmptyAddr()!=null){
                    SxSysAtta sxSysAttaByOid = sxSysAttaManager.getSxSysAttaByOid(sxServiceMaterial.getMaterialEmptyAddr());
                    serviceMaterialVo.setEmptyOriginName(sxSysAttaByOid.getOriginName());
                }
                voList.add(serviceMaterialVo);
            }
        }
        return voList;
    }
}
