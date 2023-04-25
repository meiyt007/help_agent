package com.zfsoft.superwindow.controller.easyquickcase;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.cases.data.*;
import com.zfsoft.cases.data.vo.SxFormInfoVo;
import com.zfsoft.cases.service.*;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.rest.pojo.sms.SmsSendRequest;
import com.zfsoft.rest.service.sms.ISmsRestService;
import com.zfsoft.service.browser.service.BrowserService;
import com.zfsoft.service.scSign.data.SxSign;
import com.zfsoft.service.scSign.service.SxSignService;
import com.zfsoft.service.sxConditionalRules.service.SxConditionalRulesService;
import com.zfsoft.service.sxService.service.SxFormInfoService;
import com.zfsoft.service.sxService.service.SxServiceLocationService;
import com.zfsoft.service.sxService.service.SxServiceMaterialService;
import com.zfsoft.service.sxService.service.SxServiceService;
import com.zfsoft.single.data.api.SignatureFlowRecord;
import com.zfsoft.single.data.formTemplate.TermlateDataDto;
import com.zfsoft.single.service.api.ApiService;
import com.zfsoft.single.service.clzs.ClassifyService;
import com.zfsoft.single.service.clzs.IntelligentPreTrialService;
import com.zfsoft.single.service.formTemplate.MaterialTemplateService;
import com.zfsoft.single.service.fzgl.CaseLicenseManageService;
import com.zfsoft.single.service.fzgl.LicenseIssuedService;
import com.zfsoft.single.service.ywbl.CaseMaterialOutOfStockService;
import com.zfsoft.single.service.ywbl.QlCaseCorrectionService;
import com.zfsoft.single.service.yxpz.FormFieldRelConfigService;
import com.zfsoft.superwindow.data.sx.SxRecordMsg;
import com.zfsoft.superwindow.data.web.ReviewDto;
import com.zfsoft.superwindow.dbaccess.dao.DbMaterialSignPersonMapper;
import com.zfsoft.superwindow.dbaccess.dao.DbSxRecordMsgMapper;
import com.zfsoft.superwindow.dbaccess.data.DbInterApi;
import com.zfsoft.superwindow.dbaccess.data.DbMaterialSignPerson;
import com.zfsoft.superwindow.dbaccess.data.DbSxRecordMsg;
import com.zfsoft.superwindow.dbaccess.data.DbSxRecordMsgExample;
import com.zfsoft.superwindow.feign.settings.*;
import com.zfsoft.superwindow.manager.clzs.InterApiManager;
import com.zfsoft.superwindow.manager.sign.MaterialSignManager;
import com.zfsoft.superwindow.manager.sso.NatureUserManager;
import com.zfsoft.superwindow.manager.web.WebManager;
import com.zfsoft.superwindow.service.easyquickcase.WebService;
import com.zfsoft.superwindow.service.easyquickcase.data.*;
import com.zfsoft.superwindow.service.interapi.data.ApiReqParams;
import com.zfsoft.superwindow.service.interapi.service.InterApiRouteService;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.StringUtils;
import com.zfsoft.superwindow.util.fa.AiTokenUtil;
import com.zfsoft.service.sxConditionalRules.data.SxConditionalRules;
import com.zfsoft.service.sxService.data.SxFormInfo;
import com.zfsoft.service.sxService.data.SxService;
import com.zfsoft.service.sxSituation.data.SxServiceOptionTitle;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * @author hut
 * @date 2022/5/31
 */
@RestController
@Slf4j
@Api(tags = "网站端通用接口")
public class WebController implements WebService {

    @Resource
    private AiTokenUtil aiTokenUtil;

    @Resource
    private ISmsRestService smsRestService;

    @Resource
    private SxSignService sxSignFeignService;

    @Resource
    private MaterialSignManager materialSignManager;

    @Resource
    private DbMaterialSignPersonMapper dbMaterialSignPersonMapper;

    @Autowired
    private FormFieldRelConfigService formFieldRelConfigFeignService;

    @Autowired
    private WebManager webManager;

    @Resource
    private BrowserService browserFeignService;

    @Resource
    private IdsLoginFeignService idsLoginFeignService;

    @Resource
    private ReviewsDockingService reviewsDockingService;

    @Resource
    private QlCaseService qlCaseServiceFeginService;

    @Resource
    private QlCaseMaterialService qlCaseMaterialServiceFeginService;

    @Resource
    private QlCaseLinkResultService qlCaseLinkResultServiceFeginService;

    @Resource
    private QlCaseExtService qlCaseExtServiceFeginService;

    @Resource
    private QlCaseApplayService qlCaseApplayServiceFeginService;

    @Resource
    private SxServiceService sxServiceFeginService;

    @Resource
    private QlCaseSituationTitleValRelationService qlCaseSituationTitleValRelationFeignService;

    @Resource
    private QlCaseCorrectionService qlCaseCorrectionFeignService;

    @Resource
    private CaseMaterialOutOfStockService caseMaterialOutOfStockFeignService;

    @Resource
    private CaseLicenseManageService caseLicenseManageFeignService;

    @Resource
    private LicenseIssuedService licenseIssuedFeignService;

    @Resource
    private SxServiceLocationService sxServiceLocationFeignService;

    @Resource
    private NatureUserManager natureUserManager;

    @Resource
    private SxServiceMaterialService sxServiceMaterialFeignService;

    @Resource
    private SxFormInfoService sxFormInfoFeignService;

    @Resource
    private ApiService apiFeignService;

    @Resource
    private SysAttaService sysAttaCaseFeignService;

    @Resource
    private QlCaseAttaFileService qlCaseAttaFileFeignService;

    @Resource
    private QlCaseMaterialAttaService qlCaseMaterialAttaFeignService;

    @Resource
    private MaterialTemplateService materialTemplateFeignService;

    @Resource
    private InterApiManager interApiManager;

    @Resource
    private DbSxRecordMsgMapper dbSxRecordMsgMapper;

    @Resource
    private SxConditionalRulesService sxConditionalRulesFeignService;

    @Resource
    private InterApiRouteService interApiRouteService;

    @Resource
    private ClassifyService classifyFeignService;

    @Resource
    private IntelligentPreTrialService intelligentPreTrialFeignService;

    /**
     * 成功值
     */
    private final Integer CODE = 200;


    @Override
    public ApiResultSet test() {
        return ApiResultSet.ok("测试接口拦截");
    }

    @Override
    public ApiResultSet login() {
        String token = webManager.login();
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(token);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<PageResult<SxService>> listSxServicePage(String serviceName, String districtOid,
                                                                 String organOid,
                                                                 String handleType, Integer pageNum, Integer pageSize) {
        ApiResultSet<PageResult<SxService>> resultApiResultSet = browserFeignService.listSxServicePage(serviceName,
                districtOid, organOid, handleType, pageNum, pageSize);
        return resultApiResultSet;
    }

    @Override
    public ApiResultSet getLoginUrl(String gotoUrl) {
        String callType = "1";
        String interId = "";
        Map<String, String> loginUrl = idsLoginFeignService.getLoginUrl(gotoUrl, callType, interId);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(loginUrl);
        return apiResultSet;
    }

    @Override
    public ApiResultSet reviewsDocking(ReviewDto reviewDto) {
        reviewDto.setCallType("1");
        reviewDto.setInterId("");
        ApiResultSet<String> apiResultSet = reviewsDockingService.projectDataCollection(reviewDto);
        return apiResultSet;
    }

    @Override
    public ApiResultSet queryQlCaseByCaseNumber(String caseNumber) {
        ApiResultSet<QlCase> apiResultSet = qlCaseServiceFeginService.queryQlCaseByCaseNumber(caseNumber);
        return apiResultSet;
    }

    @Override
    public ApiResultSet queryQlCaseMaterialByCaseOid(String caseOid) {
        ApiResultSet<List<QlCaseMaterial>> apiResultSet = qlCaseMaterialServiceFeginService.queryQlCaseMaterialByCaseOid(caseOid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet queryQlCaseLinkResultListByCaseOid(String caseOid) {
        ApiResultSet<List<QlCaseLinkResult>> apiResultSet = qlCaseLinkResultServiceFeginService.queryQlCaseLinkResultListByCaseOid(caseOid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet queryQlCaseExtByCaseOid(String caseOid) {
        ApiResultSet<QlCaseExt> apiResultSet = qlCaseExtServiceFeginService.queryQlCaseExtByCaseOid(caseOid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet queryQlCaseApplayByCaseOid(String caseOid) {
        ApiResultSet<QlCaseApplay> apiResultSet = qlCaseApplayServiceFeginService.queryQlCaseApplayByCaseOid(caseOid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet viewSxService(String serviceOid) {
        ApiResultSet apiResultSet = sxServiceFeginService.viewSxService(serviceOid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<SxService> getSxServiceByServiceOid(String serviceOid) {
        return sxServiceFeginService.getSxServiceByOid(serviceOid);
    }

    @Override
    public ApiResultSet getCaseTitleValueList(String caseOid) {
        ApiResultSet<List<Map<String, String>>> apiResultSet = qlCaseSituationTitleValRelationFeignService.getCaseTitleValueList(caseOid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet queryFormInfoByCaseOid(String caseOid) {
        ApiResultSet<List<SxFormInfoVo>> apiResultSet = qlCaseServiceFeginService.queryFormInfoByCaseOid(caseOid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet getListByCaseOid(String caseOid) {
        ApiResultSet apiResultSet = qlCaseCorrectionFeignService.getListByCaseOid(caseOid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet getCaseMaterialOutByCaseNumber(String caseNumber) {
        ApiResultSet apiResultSet = caseMaterialOutOfStockFeignService.getCaseMaterialOutByCaseNumber(caseNumber);
        return apiResultSet;
    }

    @Override
    public ApiResultSet getOneByCaseOid(String caseOid) {
        ApiResultSet apiResultSet = caseLicenseManageFeignService.getOneByCaseOid(caseOid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet getLicenseIssuedByCaseOid(String caseOid) {
        ApiResultSet apiResultSet = licenseIssuedFeignService.getLicenseIssuedByCaseOid(caseOid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet getDeliverRecordByCaseOid(String caseOid) {
        ApiResultSet apiResultSet = caseLicenseManageFeignService.getDeliverRecordByCaseOid(caseOid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<Map<String, Object>>> listOrganByDistrictAndService(String districtOid, String handleType) {
        ApiResultSet<List<Map<String, Object>>> apiResultSet = browserFeignService.listOrganByDistrictAndService(districtOid, handleType);
        return apiResultSet;
    }

    @Override
    public ApiResultSet getLocationInfoByServiceOid(String serviceOid) {
        ApiResultSet apiResultSet = sxServiceLocationFeignService.getLocationInfoByServiceOid(serviceOid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Map<String, Object>> getLoginUser(String token) {
        Map<String, Object> loginUser = natureUserManager.getLoginUser(token);
        return new ApiResultSet<>(loginUser);
    }

    @Override
    public ApiResultSet<List<SxServiceOptionTitle>> getSxServiceTitlesNoRelation(String serviceOid) {
        ApiResultSet<List<SxServiceOptionTitle>> apiResultSet = sxServiceFeginService.getSxServiceTitlesNoRelation(serviceOid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<SxServiceOptionTitle>> querySxServiceSituationRelation(String serviceOid, String valOids, String currentOid, String currentTitleOid, String rootTitleOid) {
        ApiResultSet<List<SxServiceOptionTitle>> apiResultSet = sxServiceFeginService.querySxServiceSituationRelation(serviceOid, valOids, currentOid, currentTitleOid, rootTitleOid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Map<String, Object>> getExclusiveGuide(String serviceOid, String optionValOids) {
        ApiResultSet<Map<String, Object>> apiResultSet = sxServiceMaterialFeignService.getExclusiveGuide(serviceOid, optionValOids);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<SxConditionalRules>> listSxConditionalRules(String serviceOid) {
        // 查询条件预检规则
        ApiResultSet<List<SxConditionalRules>> apiResultSet = browserFeignService.listSxConditionalRules(serviceOid,
                SxConditionalRules.RULETYPE_CONDITION);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<SxConditionalRules>> fastListSxConditionalRules(String serviceOid) {
        // 查询秒批规则
        ApiResultSet<List<SxConditionalRules>> apiResultSet = browserFeignService.listSxConditionalRules(serviceOid,
                SxConditionalRules.RULETYPE_APPROVAL);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Boolean> executeCondRule(String interApiCode, ApiReqParams reqParams) {
        ApiResultSet<Boolean> resultSet = new ApiResultSet<>();
        DbInterApi interApi = interApiManager.getInterApiByCode(interApiCode);
        if (interApi != null) {
            reqParams.setInterId(interApi.getInterfaceOid());
            if(StringUtils.isEmpty(reqParams.getCaseOid())){
                resultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
                resultSet.setData(false);
                resultSet.setMessage("办件主键不能为空！");
                return resultSet;
            }
        }
        ResponseData<Boolean> booleanApiResultSet = interApiRouteService.routeValidateApi(interApiCode, reqParams);
        if(booleanApiResultSet.getCode().intValue() != CODE){
            resultSet.setData(booleanApiResultSet.getData());
            resultSet.setMessage(booleanApiResultSet.getMessage());
            //resultSet.setCode(booleanApiResultSet.getCode());
            return resultSet;
        }

        SxConditionalRules sxConditionalRules = new SxConditionalRules();
        // 事项主键
        sxConditionalRules.setServiceOid(reqParams.getSxServiceOid());
        // interApiCode 值
        sxConditionalRules.setInterApiCode(interApiCode);
        // 查询条件预检配置项
        sxConditionalRules.setRuleType(SxConditionalRules.RULETYPE_CONDITION);
        // 获取配置项值
        ApiResultSet<SxConditionalRules> apiResultSet = sxConditionalRulesFeignService.queryRulesByCodeAndServiceOid(sxConditionalRules);
        if(apiResultSet.getCode() != CODE){
            resultSet.setMessage(apiResultSet.getMessage());
            resultSet.setData(false);
            return resultSet;
        }

        // 如果存在删除该条记录
        DbSxRecordMsg record = dbSxRecordMsgMapper.selectByCaseOid(reqParams.getCaseOid(),
                SxConditionalRules.RULETYPE_CONDITION, apiResultSet.getData().getRulesOid());
        if(record != null){
            dbSxRecordMsgMapper.deleteByPrimaryKey(record.getId());
        }

        DbSxRecordMsg dbSxRecordMsg = new DbSxRecordMsg();
        // 条件预检记录
        dbSxRecordMsg.setRuleType(SxConditionalRules.RULETYPE_CONDITION);
        insertRecordMsg(booleanApiResultSet, dbSxRecordMsg, interApiCode, reqParams.getCaseOid(), apiResultSet.getData().getRulesOid());
        resultSet.setMessage(booleanApiResultSet.getMessage());
        resultSet.setData(booleanApiResultSet.getData());
        return resultSet;
    }

    @Override
    public ApiResultSet<Boolean> executeFastApproval(String interApiCode, ApiReqParams reqParams) {
        ApiResultSet<Boolean> resultSet = new ApiResultSet<>();
        DbInterApi interApi = interApiManager.getInterApiByCode(interApiCode);
        if (interApi != null) {
            reqParams.setInterId(interApi.getInterfaceOid());
            if(StringUtils.isEmpty(reqParams.getCaseOid())){
                resultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
                resultSet.setData(false);
                resultSet.setMessage("办件主键不能为空！");
                return resultSet;
            }
        }
        ResponseData<Boolean> booleanApiResultSet = interApiRouteService.routeValidateApi(interApiCode, reqParams);
        if(booleanApiResultSet.getCode().intValue() != CODE){
            resultSet.setData(booleanApiResultSet.getData());
            resultSet.setMessage(booleanApiResultSet.getMessage());
            //resultSet.setCode(booleanApiResultSet.getCode());
            return resultSet;
        }

        SxConditionalRules sxConditionalRules = new SxConditionalRules();
        // 事项主键
        sxConditionalRules.setServiceOid(reqParams.getSxServiceOid());
        // interApiCode 值
        sxConditionalRules.setInterApiCode(interApiCode);
        // 查询秒批配置项
        sxConditionalRules.setRuleType(SxConditionalRules.RULETYPE_APPROVAL);
        // 获取配置项值
        ApiResultSet<SxConditionalRules> apiResultSet = sxConditionalRulesFeignService.queryRulesByCodeAndServiceOid(sxConditionalRules);
        if(apiResultSet.getCode() != CODE){
            resultSet.setMessage(apiResultSet.getMessage());
            resultSet.setData(false);
            return resultSet;
        }

        // 如果存在删除该条记录
        DbSxRecordMsg record = dbSxRecordMsgMapper.selectByCaseOid(reqParams.getCaseOid(),
                SxConditionalRules.RULETYPE_APPROVAL, apiResultSet.getData().getRulesOid());
        if(record != null){
            dbSxRecordMsgMapper.deleteByPrimaryKey(record.getId());
        }

        DbSxRecordMsg dbSxRecordMsg = new DbSxRecordMsg();
        // 秒批记录
        dbSxRecordMsg.setRuleType(SxConditionalRules.RULETYPE_APPROVAL);
        insertRecordMsg(booleanApiResultSet, dbSxRecordMsg, interApiCode, reqParams.getCaseOid(), apiResultSet.getData().getRulesOid());
        resultSet.setMessage(booleanApiResultSet.getMessage());
        resultSet.setData(booleanApiResultSet.getData());
        return resultSet;
    }

    private void insertRecordMsg(ResponseData<Boolean> result,
                DbSxRecordMsg dbSxRecordMsg, String interApiCode, String caseOid, String rulesOid) {
        // 办件主键
        dbSxRecordMsg.setCaseOid(caseOid);
        // 预检规则OID
        dbSxRecordMsg.setRulesOid(rulesOid);
        // interApiCode 值
        dbSxRecordMsg.setInterApiCode(interApiCode);
        // 预检或秒批记录主键
        dbSxRecordMsg.setRecordOid(UUID.randomUUID().toString().replaceAll("-", ""));
        // 数据创建时间
        dbSxRecordMsg.setCreateDate(new Date());
        // 数据修改时间
        dbSxRecordMsg.setModifyDate(new Date());
        if(CODE == result.getCode().intValue()){
            // 成功
            dbSxRecordMsg.setStatus(SxRecordMsg.RECORD_STATUS_SUCCESS);
        }else{
            // 失败
            dbSxRecordMsg.setStatus(SxRecordMsg.RECORD_STATUS_FAILED);
            // 失败原因
            dbSxRecordMsg.setMessage(result.getMessage());
        }
        dbSxRecordMsgMapper.insert(dbSxRecordMsg);
    }

    @Override
    public ApiResultSet<Map<String, Object>> browserSaveQlCase(Object object) {
        ApiResultSet<Map<String, Object>> apiResultSet = qlCaseServiceFeginService.browserSaveQlCase(object);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<SxFormInfo>> getBrowserDesignFormList(String serviceOid) {
        ApiResultSet<List<SxFormInfo>> apiResultSet = sxFormInfoFeignService.getBrowserDesignFormList(serviceOid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Map<String, Object>> browserUpdateFormInfo(QlCase qlCase) {
        ApiResultSet<Map<String, Object>> apiResultSet = qlCaseServiceFeginService.browserUpdateFormInfo(qlCase);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Map<String, List<QlCaseMaterial>>> queryQlCaseMaterialListByCaseOid(String caseOid) {
        ApiResultSet<Map<String, List<QlCaseMaterial>>> apiResultSet = apiFeignService.queryQlCaseMaterialListByCaseOid(caseOid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<PageResult<QlCase>> queryDoneQlCasePageForBrowers(String caseNumber, String serviceName, String netUserId, Integer pageNum, Integer pageSize) {
        ApiResultSet<PageResult<QlCase>> apiResultSet = qlCaseServiceFeginService.queryDoneQlCasePageForBrowers(caseNumber, serviceName, netUserId, pageNum, pageSize);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<QlCaseApplay> savePostResultInfoForBrowers(QlCaseApplay qlCaseApplay) {
        ApiResultSet<QlCaseApplay> apiResultSet = qlCaseApplayServiceFeginService.savePostResultInfoForBrowers(qlCaseApplay);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<QlSysAtta> querySysAttaByOid(String attaOid) {
        ApiResultSet<QlSysAtta> apiResultSet = sysAttaCaseFeignService.querySysAttaByOid(attaOid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<String> getPhoneQrScanUuid() {
        ApiResultSet<String> apiResultSet = qlCaseAttaFileFeignService.getPhoneQrScanUuid();
        return apiResultSet;
    }

    @Override
    public ApiResultSet<String> getPhoneUploadStatus(String uuid) {
        ApiResultSet<String> apiResultSet = qlCaseAttaFileFeignService.getPhoneUploadStatus(uuid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<String> initPhoneUpload(String uuid, String caseMaterialOid) {
        ApiResultSet<String> apiResultSet = qlCaseAttaFileFeignService.initPhoneUpload(uuid, caseMaterialOid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Boolean> checkPhoneUploadStatus(String uuid) {
        ApiResultSet<Boolean> apiResultSet = qlCaseAttaFileFeignService.checkPhoneUploadStatus(uuid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet saveTempPhoneUploadAtta(String uuid, String attaOids) {
        ApiResultSet apiResultSet = qlCaseAttaFileFeignService.saveTempPhoneUploadAtta(uuid, attaOids);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<QlSysAtta>> getTempPhoneUploadAtta(String uuid) {
        ApiResultSet<List<QlSysAtta>> apiResultSet = qlCaseAttaFileFeignService.getTempPhoneUploadAtta(uuid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<QlSysAtta>> uploadCaseMaterialFile(HttpServletRequest request, MultipartFile[] files) {
        ApiResultSet<List<QlSysAtta>> apiResultSet = qlCaseAttaFileFeignService.uploadCaseMaterialFile(request, files);
        return apiResultSet;
    }

    @Override
    public ApiResultSet saveOrUpdateCaseMaterialAttaListForBrowser(List<QlCaseMaterial> qlCaseMaterialList) {
        ApiResultSet apiResultSet = qlCaseMaterialAttaFeignService.saveOrUpdateCaseMaterialAttaListForBrowser(qlCaseMaterialList);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<Map<String, String>>> updateQlCaseMaterialListForBrowser(List<QlCaseMaterial> qlCaseMaterialList) {
        ApiResultSet<List<Map<String, String>>> apiResultSet = qlCaseMaterialServiceFeginService.updateQlCaseMaterialListForBrowser(qlCaseMaterialList);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<String> createFlow(ElectronicSignatureDto electronicSignatureDto) {
        ApiResultSet apiResultSet = new ApiResultSet<>();
        if (StrUtil.isEmpty(electronicSignatureDto.getCaseOid()) || StrUtil.isEmpty(electronicSignatureDto.getMaterialOid())) {
            throw new ResultInfoException("材料或办件主键不能为空");
        }
        ApiResultSet oldFlowSet = apiFeignService.getSignatureFlowRecord(electronicSignatureDto.getCaseOid(), electronicSignatureDto.getMaterialOid());
        if (null != oldFlowSet && null != oldFlowSet.getData()) {
            // 单人签章
            Map<String, Object> resultMap = new HashMap<>();
            Map<String, Object> info = new HashMap<>();
            SignatureFlowRecord record = (SignatureFlowRecord) oldFlowSet.getData();
            info.put("flowId", record.getFlowId());
            info.put("status", record.getStatus());
            List<Map<String, Object>> objList = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("signUrl", record.getFileKey());
            objList.add(map);
            info.put("signTasks", objList);
            resultMap.put("data", info);
            apiResultSet.setData(JSON.toJSONString(resultMap));
            return apiResultSet;
        }
        electronicSignatureDto.setCallType("1");
        electronicSignatureDto.setInterId("");
        ResponseData<String> data = idsLoginFeignService.createFlow(electronicSignatureDto);
        apiResultSet.setCode(data.getCode());
        apiResultSet.setMessage(data.getMessage());
        apiResultSet.setData(data.getData());
        apiResultSet.setTime(DateUtil.now());
        return apiResultSet;
    }

    @Override
    public ApiResultSet saveOrUpdateSignatureFlow(SignatureFlowRecordSuper signatureFlowRecord) {
        SignatureFlowRecord record = new SignatureFlowRecord();
        BeanUtils.copyProperties(signatureFlowRecord, record);
        ApiResultSet apiResultSet = apiFeignService.saveOrUpdateSignatureFlow(record);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<SignatureFlowRecordSuper>> getSignatureFlowRecordByCaseOid(String caseOid) {
        ApiResultSet<List<SignatureFlowRecord>> listApiResultSet = apiFeignService.getSignatureFlowRecordByCaseOid(caseOid);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        List<SignatureFlowRecordSuper> list = new ArrayList<>();
        List<SignatureFlowRecord> signatureFlowRecordList = listApiResultSet.getData();
        for (SignatureFlowRecord signatureFlowRecord : signatureFlowRecordList) {
            SignatureFlowRecordSuper record = new SignatureFlowRecordSuper();
            BeanUtils.copyProperties(signatureFlowRecord, record);
            list.add(record);
        }
        apiResultSet.setData(list);
        return apiResultSet;
    }

    @Override
    public ApiResultSet getSignatureFlowRecord(String caseOid, String materialOid) {
        return apiFeignService.getSignatureFlowRecord(caseOid, materialOid);
    }

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

    @Override
    public ApiResultSet<QlCaseExt> saveQlCaseExtForBrowers(QlCaseExt qlCaseExt) {
        ApiResultSet<QlCaseExt> apiResultSet = qlCaseExtServiceFeginService.saveQlCaseExtForBrowers(qlCaseExt);
        return apiResultSet;
    }

    @Override
    public ApiResultSet previewImageAndPdf(String fastdfsNginxUrl) {
        webManager.previewImageAndPdf(fastdfsNginxUrl);
        return new ApiResultSet<>();
    }

    @Override
    public void downloadFileByUrl(String fastdfsNginxUrl) {
        webManager.downloadFileByUrl(fastdfsNginxUrl);
    }

    @Override
    public ApiResultSet getFormFillInfos(String serviceOid, String caseOid, String optionOids) {
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

    @Override
    public ApiResultSet<String> deleteZcQlCaseByCaseOid(String caseOid) {
        ApiResultSet<String> apiResultSet = qlCaseServiceFeginService.deleteZcQlCaseByCaseOid(caseOid);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Map<String, Object>> getAllBasicFieldByOid(String caseOid) {
        return formFieldRelConfigFeignService.getAllBasicFieldByOid(caseOid);
    }

    //判断下这个办件下的材料是否有配置过
    @Override
    public ApiResultSet getHtmlType(String materialOid, String caseOid) {
        log.info("查询该办件是否已经配置过签章人，caseOid：{},materialOid:{}", caseOid, materialOid);
        ApiResultSet data = new ApiResultSet();
        List<MaterialSignPerson> personList = materialSignManager.getSignaturePerson(caseOid, materialOid);
        if (personList.size() > 0) {
            //已有配置，进入签署通知
            data.setData(1);
        } else {
            data.setData(0);
        }
        return data;
    }

    //根据材料oid查询配置的签名角色
    @Override
    public ApiResultSet<List<SxSign>> getSignRole(String materialOid) {
        log.info("根据材料查询签章角色：materialOid:{}", materialOid);
        ApiResultSet<List<SxSign>> list = sxSignFeignService.getSignListByMaterialOid(materialOid);
        return list;
    }

    //新增签章人
    @Override
    public ApiResultSet saveSignaturePerson(MaterialSignPersonDto materialSignPersonDto) {
        log.info("材料新增多角色签章配置");
        ApiResultSet data = new ApiResultSet();
        boolean flag = materialSignManager.saveSignaturePerson(materialSignPersonDto);
        if (flag) {
            data.setCode(200);
        } else {
            data.setCode(201);
        }
        return data;
    }

    //根据材料id查询该材料下配置的签章人
    @Override
    public ApiResultSet<List<MaterialSignPerson>> getSignaturePerson(String materialOid, String caseOid) {
        log.info("查询签章人信息：caseOid:{},materialOid:{}", caseOid, materialOid);
        ApiResultSet<List<MaterialSignPerson>> data = new ApiResultSet();
        List<MaterialSignPerson> personList = materialSignManager.getSignaturePerson(caseOid, materialOid);
        data.setData(personList);
        return data;
    }

    //发短信
    @Override
    public ApiResultSet sendMessage(String id, String phone, String roleId, String signUrl) {
        ApiResultSet apiResultSet = new ApiResultSet();
        apiResultSet.setCode(200);
        apiResultSet.setMessage("发送成功");
        //查询签章角色配置的短信内容 -- 这个暂且废弃，换成模板发送
        ApiResultSet<SxSign> data = sxSignFeignService.getSignById(roleId);
        //拼接短信内容
//        String message = serviceName+","+signUrl;
        String message = data.getData().getMessage() + signUrl;
        try {
            SmsSendRequest smsSendRequest = aiTokenUtil.getTokenRequest(SmsSendRequest.class);
            smsSendRequest.setPhone(phone);
            smsSendRequest.setMessage(message);
            smsRestService.sendSms(smsSendRequest);
//            String templetid = "";//短信模板id
//            boolean flag = middleWebFeignService.sendAccessSms(phone,message,templetid);
            //发送完还要更新一下字表
//            if(flag){
            DbMaterialSignPerson dbMaterialSignPerson = new DbMaterialSignPerson();
            dbMaterialSignPerson.setId(Long.valueOf(id));
            dbMaterialSignPerson.setMailStatus("Y");
            dbMaterialSignPersonMapper.updateByPrimaryKeySelective(dbMaterialSignPerson);
//            }else{
//                throw new Exception("调用短信接口失败");
//            }
        } catch (Exception e) {
            log.warn("发送短信失败：{}", e.getMessage());
            apiResultSet.setCode(201);
            apiResultSet.setMessage("发送短信提示失败");
        }
        return apiResultSet;
    }

    @Override
    public ApiResultSet<JSONArray> getWebsiteDetail(String bmmc) {
        String callType = "1";
        String interId = "";
        ResponseData<JSONArray> websiteDetail = idsLoginFeignService.getWebsiteDetail(bmmc, callType, interId);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        if (200 == websiteDetail.getCode()) {
            apiResultSet.setData(websiteDetail.getData());
        } else {
            apiResultSet.setData(new JSONArray());
        }
        apiResultSet.setMessage(websiteDetail.getMessage());
        return apiResultSet;
    }

    @Override
    public ApiResultSet<JSONObject> getAccumulationAccount(String zjhm, String jcrxm) {
        String callType = "1";
        String interId = "";
        ResponseData<JSONObject> accumulationAccount = idsLoginFeignService.getAccumulationAccount(zjhm, jcrxm, callType, interId);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        if (200 == accumulationAccount.getCode()) {
            apiResultSet.setData(accumulationAccount.getData());
        } else {
            apiResultSet.setData(new JSONObject());
        }
        apiResultSet.setMessage(accumulationAccount.getMessage());
        return apiResultSet;
    }

    @Override
    public ApiResultSet<String> getDepositeCertificatePrint(String grzh) {
        String callType = "1";
        String interId = "";
        ApiResultSet apiResultSet = new ApiResultSet<>();
        ResponseData<String> depositeCertificatePrint = idsLoginFeignService.getDepositeCertificatePrint(grzh, callType, interId);
        if (200 == depositeCertificatePrint.getCode()) {
            try {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                byte[] bytes = Base64.getDecoder().decode(depositeCertificatePrint.getData());
                InputStream inputStream = new ByteArrayInputStream(bytes);
                MultipartFile file = new MockMultipartFile("File", grzh + System.currentTimeMillis() + ".pdf", "text/plain", inputStream);
                UploadUtil uploadUtil = new UploadUtil(request);
                String filePath = uploadUtil.uploadFile(file);
                apiResultSet.setData(BaseStaticParameter.FASTDFS_NGINX_URL + "/" + filePath);
            } catch (Exception e) {
                apiResultSet.setCode(500);
                apiResultSet.setMessage(e.getMessage());
            }
        } else {
            apiResultSet.setData("");
        }
        return apiResultSet;
    }

    @Override
    public ApiResultSet<String> getSettleCertificatePrint(String jkrgjjzh, String jkhtbh) {
        String callType = "1";
        String interId = "";
        ResponseData<String> settleCertificatePrint = idsLoginFeignService.getSettleCertificatePrint(jkrgjjzh, jkhtbh, callType, interId);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        if (200 == settleCertificatePrint.getCode()) {
            try {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                byte[] bytes = Base64.getDecoder().decode(settleCertificatePrint.getData());
                InputStream inputStream = new ByteArrayInputStream(bytes);
                MultipartFile file = new MockMultipartFile("File", jkrgjjzh + System.currentTimeMillis() + ".pdf", "text/plain", inputStream);
                UploadUtil uploadUtil = new UploadUtil(request);
                String filePath = uploadUtil.uploadFile(file);
                apiResultSet.setData(BaseStaticParameter.FASTDFS_NGINX_URL + "/" + filePath);
            } catch (Exception e) {
                apiResultSet.setCode(500);
                apiResultSet.setMessage(e.getMessage());
            }
        } else {
            apiResultSet.setData("");
        }
        return apiResultSet;
    }

    @Override
    public ApiResultSet<JSONObject> getLoanAccount(String jkrzjh, String xingming) {
        String callType = "1";
        String interId = "";
        ResponseData<JSONObject> loanAccount = idsLoginFeignService.getLoanAccount(jkrzjh, xingming, callType, interId);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        if (200 == loanAccount.getCode()) {
            apiResultSet.setData(loanAccount.getData());
        } else {
            apiResultSet.setData(new JSONObject());
        }
        apiResultSet.setMessage(loanAccount.getMessage());
        return apiResultSet;
    }

    @Override
    public ApiResultSet<JSONArray> getRefundDetail(String zjhm, String xingming) {
        String callType = "1";
        String interId = "";
        ResponseData<JSONArray> refundDetail = idsLoginFeignService.getRefundDetail(zjhm, xingming, callType, interId);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        if (200 == refundDetail.getCode()) {
            apiResultSet.setData(refundDetail.getData());
        } else {
            apiResultSet.setData(new JSONArray());
        }
        apiResultSet.setMessage(refundDetail.getMessage());
        return apiResultSet;
    }

    @Override
    public ApiResultSet<JSONArray> getLoanSchedule(String zjhm, String xingming) {
        String callType = "1";
        String interId = "";
        ResponseData<JSONArray> loanSchedule = idsLoginFeignService.getLoanSchedule(zjhm, xingming, callType, interId);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        if (200 == loanSchedule.getCode()) {
            apiResultSet.setData(loanSchedule.getData());
        } else {
            apiResultSet.setData(new JSONArray());
        }
        apiResultSet.setMessage(loanSchedule.getMessage());
        return apiResultSet;
    }

    @Override
    public ApiResultSet verificationRules(String interApiCode, ApiReqParams reqParams) {
        ApiResultSet apiResultSet = new ApiResultSet<>();
        if(StringUtils.isEmpty(interApiCode)){
            apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
            apiResultSet.setMessage("服务的 interApiCode 不能为空");
            return apiResultSet;
        }
        int count = 0;
        if(reqParams != null){
            if(StringUtils.isEmpty(reqParams.getCaseOid())){
                apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
                apiResultSet.setMessage("办件主键不能为空");
                return apiResultSet;
            }
            if(StringUtils.isEmpty(reqParams.getSxServiceOid())){
                apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
                apiResultSet.setMessage("事项主键不能为空");
                return apiResultSet;
            }
            if(StringUtils.isEmpty(reqParams.getUniqueCode())){
                apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
                apiResultSet.setMessage("主体代码（身份证号或统一信用代码）不能为空");
                return apiResultSet;
            }
            if(StringUtils.isEmpty(reqParams.getType())){
                apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
                apiResultSet.setMessage("办事人类型不能为空");
                return apiResultSet;
            }
            // 查询该事项对应的秒批配置-这里有多条
            ApiResultSet<List<SxConditionalRules>> sxCondSet = browserFeignService.listSxConditionalRules(reqParams.getSxServiceOid(), SxConditionalRules.RULETYPE_APPROVAL);
            DbSxRecordMsgExample dbSxRecordMsgExample = new DbSxRecordMsgExample();
            DbSxRecordMsgExample.Criteria criteria = dbSxRecordMsgExample.createCriteria();
            criteria.andCaseOidEqualTo(reqParams.getCaseOid());
            // 秒批记录
            criteria.andRuleTypeEqualTo(SxConditionalRules.RULETYPE_APPROVAL);
            // 查询该事项对应的秒批记录，判断是否和该事项秒批配置相同，要求配置都得一样，全部调用成功
            List<DbSxRecordMsg> dbSxRecordMsgList = dbSxRecordMsgMapper.selectByExample(dbSxRecordMsgExample);
            if(dbSxRecordMsgList.size() > 0 && sxCondSet.getData().size() > 0){
                if(dbSxRecordMsgList.size() != sxCondSet.getData().size()){
                    apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
                    apiResultSet.setMessage("事项秒批配置和秒批运行成功记录数不匹配");
                    return apiResultSet;
                }
                for(SxConditionalRules sxConditionalRules : sxCondSet.getData()){
                    for(DbSxRecordMsg dbSxRecordMsg : dbSxRecordMsgList){
                        if(sxConditionalRules.getRulesOid().equals(dbSxRecordMsg.getRulesOid())
                                && SxRecordMsg.RECORD_STATUS_SUCCESS.equals(dbSxRecordMsg.getStatus())){
                            count++;
                        }
                    }
                }
                if(sxCondSet.getData().size() != count){
                    apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
                    apiResultSet.setMessage("事项秒批配置和秒批运行成功记录数不匹配");
                    return apiResultSet;
                }
            }else{
                apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
                apiResultSet.setMessage("事项秒批配置为空或秒批运行成功记录为空");
                return apiResultSet;
            }
        }else{
            apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
            apiResultSet.setMessage("必要的参数不能为空");
            return apiResultSet;
        }
        ResponseData responseData = interApiRouteService.routeQueryApi(interApiCode, reqParams);
        apiResultSet.setData(responseData.getData());
        apiResultSet.setMessage(responseData.getMessage());
        apiResultSet.setCode(responseData.getCode());
        return apiResultSet;
    }

    @Override
    public ApiResultSet getUploadMaterialClassification(String caseOid, String caseMaterialOid, String attaOid) {
        return classifyFeignService.getUploadMaterialClassification(caseOid, caseMaterialOid, attaOid);
    }

    @Override
    public ApiResultSet getIntelligentAuditMaterialList(String caseOid, String serviceOid) {
        return intelligentPreTrialFeignService.getIntelligentAuditMaterialList(caseOid, serviceOid);
    }

    @Override
    public ApiResultSet viewMaterialAuditDetailResult(String caseOid, String caseMaterialOids) {
        ApiResultSet apiResultSet = new ApiResultSet<>();
        List list = new ArrayList();
        try {
            String[] caseMaterialOidArr = caseMaterialOids.split(",");
            for (String caseMaterialOid : caseMaterialOidArr) {
                ApiResultSet result = intelligentPreTrialFeignService.viewDetailResult(caseOid, caseMaterialOid, null, null);
                if (result != null && ApiResultSet.SUCCESS == result.getCode()){
                    list.add(result.getData());
                }
            }
            apiResultSet.setCode(ApiResultSet.SUCCESS);
            apiResultSet.setData(list);
        } catch (Exception e) {
            log.error("获取材料智审结果列表失败：{}", e.getMessage());
            apiResultSet.setCode(201);
            apiResultSet.setMessage("获取材料智审结果列表失败");
        }
        return apiResultSet;
    }

    @Override
    public ApiResultSet intelligentAuditByCaseMaterialOid(String caseMaterialOid) {
        return intelligentPreTrialFeignService.intelligentAuditByCaseMaterialOid(caseMaterialOid);
    }
}
