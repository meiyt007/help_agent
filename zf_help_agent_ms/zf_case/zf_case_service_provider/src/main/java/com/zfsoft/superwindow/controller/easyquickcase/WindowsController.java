package com.zfsoft.superwindow.controller.easyquickcase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.cases.data.*;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.web.ReviewDto;
import com.zfsoft.superwindow.service.easyquickcase.WindowsService;
import com.zfsoft.superwindow.service.easyquickcase.data.ElectronicSignatureDto;
import com.zfsoft.superwindow.service.easyquickcase.data.MaterialSignPersonDto;
import com.zfsoft.superwindow.service.easyquickcase.data.SignatureFlowRecordSuper;
import com.zfsoft.superwindow.service.easyquickcase.data.TermlateDataSuperDto;
import com.zfsoft.superwindow.service.interapi.data.ApiReqParams;
import com.zfsoft.service.sxConditionalRules.data.SxConditionalRules;
import com.zfsoft.service.sxService.data.SxFormInfo;
import com.zfsoft.service.sxService.data.SxService;
import com.zfsoft.service.sxSituation.data.SxServiceOptionTitle;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author ChangSheng
 * @Date 10:10 2022/6/23
 * @Description 窗口端服务
 **/
@RestController
@Slf4j
@Api(tags = "窗口端通用接口")
public class WindowsController implements WindowsService {


    @Resource
    WebController webController;

    @Override
    public ApiResultSet test() {
        return webController.test();
    }

    @Override
    public ApiResultSet login() {
        return webController.login();
    }

    @Override
    public ApiResultSet<PageResult<SxService>> listSxServicePage(String serviceName, String districtOid, String organOid, String handleType, Integer pageNum, Integer pageSize) {
        return webController.listSxServicePage(serviceName, districtOid, organOid, handleType, pageNum, pageSize);
    }

    @Override
    public ApiResultSet getLoginUrl(String gotoUrl) {
        return webController.getLoginUrl(gotoUrl);
    }

    @Override
    public ApiResultSet reviewsDocking(ReviewDto reviewDto) {
        return webController.reviewsDocking(reviewDto);
    }

    @Override
    public ApiResultSet queryQlCaseByCaseNumber(String caseNumber) {
        return webController.queryQlCaseByCaseNumber(caseNumber);
    }

    @Override
    public ApiResultSet queryQlCaseMaterialByCaseOid(String caseOid) {
        return webController.queryQlCaseMaterialByCaseOid(caseOid);
    }

    @Override
    public ApiResultSet queryQlCaseLinkResultListByCaseOid(String caseOid) {
        return webController.queryQlCaseLinkResultListByCaseOid(caseOid);
    }

    @Override
    public ApiResultSet queryQlCaseExtByCaseOid(String caseOid) {
        return webController.queryQlCaseExtByCaseOid(caseOid);
    }

    @Override
    public ApiResultSet queryQlCaseApplayByCaseOid(String caseOid) {
        return webController.queryQlCaseApplayByCaseOid(caseOid);
    }

    @Override
    public ApiResultSet viewSxService(String serviceOid) {
        return webController.viewSxService(serviceOid);
    }

    @Override
    public ApiResultSet<SxService> getSxServiceByServiceOid(String serviceOid) {
        return webController.getSxServiceByServiceOid(serviceOid);
    }

    @Override
    public ApiResultSet getCaseTitleValueList(String caseOid) {
        return webController.getCaseTitleValueList(caseOid);
    }

    @Override
    public ApiResultSet queryFormInfoByCaseOid(String caseOid) {
        return webController.queryFormInfoByCaseOid(caseOid);
    }

    @Override
    public ApiResultSet getListByCaseOid(String caseOid) {
        return webController.getListByCaseOid(caseOid);
    }

    @Override
    public ApiResultSet getCaseMaterialOutByCaseNumber(String caseNumber) {
        return webController.getCaseMaterialOutByCaseNumber(caseNumber);
    }

    @Override
    public ApiResultSet getOneByCaseOid(String caseOid) {
        return webController.getOneByCaseOid(caseOid);
    }

    @Override
    public ApiResultSet getLicenseIssuedByCaseOid(String caseOid) {
        return webController.getLicenseIssuedByCaseOid(caseOid);
    }

    @Override
    public ApiResultSet getDeliverRecordByCaseOid(String caseOid) {
        return webController.getDeliverRecordByCaseOid(caseOid);
    }

    @Override
    public ApiResultSet<List<Map<String, Object>>> listOrganByDistrictAndService(String districtOid, String handleType) {
        return webController.listOrganByDistrictAndService(districtOid, handleType);
    }

    @Override
    public ApiResultSet getLocationInfoByServiceOid(String serviceOid) {
        return webController.getLocationInfoByServiceOid(serviceOid);
    }

    @Override
    public ApiResultSet<Map<String, Object>> getLoginUser(String token) {
        return webController.getLoginUser(token);
    }

    @Override
    public ApiResultSet<List<SxServiceOptionTitle>> getSxServiceTitlesNoRelation(String serviceOid) {
        return webController.getSxServiceTitlesNoRelation(serviceOid);
    }

    @Override
    public ApiResultSet<List<SxServiceOptionTitle>> querySxServiceSituationRelation(String serviceOid, String valOids, String currentOid, String currentTitleOid, String rootTitleOid) {
        return webController.querySxServiceSituationRelation(serviceOid,valOids,currentOid, currentTitleOid,rootTitleOid);
    }

    @Override
    public ApiResultSet<Map<String, Object>> getExclusiveGuide(String serviceOid, String optionValOids) {
        return webController.getExclusiveGuide(serviceOid, optionValOids);
    }

    @Override
    public ApiResultSet<List<SxConditionalRules>> listSxConditionalRules(String serviceOid) {
        return webController.listSxConditionalRules(serviceOid);
    }

    @Override
    public ApiResultSet<List<SxConditionalRules>> fastListSxConditionalRules(String serviceOid) {
        return webController.fastListSxConditionalRules(serviceOid);
    }

    @Override
    public ApiResultSet<Boolean> executeCondRule(String interApiCode, ApiReqParams reqParams) {
        return webController.executeCondRule(interApiCode, reqParams);
    }

    @Override
    public ApiResultSet<Boolean> executeFastApproval(String interApiCode, ApiReqParams reqParams) {
        return webController.executeFastApproval(interApiCode, reqParams);
    }

    @Override
    public ApiResultSet<Map<String, Object>> browserSaveQlCase(Object object) {
        return webController.browserSaveQlCase(object);
    }

    @Override
    public ApiResultSet<List<SxFormInfo>> getBrowserDesignFormList(String serviceOid) {
        return webController.getBrowserDesignFormList(serviceOid);
    }

    @Override
    public ApiResultSet<Map<String, Object>> browserUpdateFormInfo(QlCase qlCase) {
        return webController.browserUpdateFormInfo(qlCase);
    }

    @Override
    public ApiResultSet<Map<String, List<QlCaseMaterial>>> queryQlCaseMaterialListByCaseOid(String caseOid) {
        return webController.queryQlCaseMaterialListByCaseOid(caseOid);
    }

    @Override
    public ApiResultSet<PageResult<QlCase>> queryDoneQlCasePageForBrowers(String caseNumber, String serviceName, String netUserId, Integer pageNum, Integer pageSize) {
        return webController.queryDoneQlCasePageForBrowers(caseNumber, serviceName, netUserId, pageNum, pageSize);
    }

    @Override
    public ApiResultSet<QlCaseApplay> savePostResultInfoForBrowers(QlCaseApplay qlCaseApplay) {
        return webController.savePostResultInfoForBrowers(qlCaseApplay);
    }

    @Override
    public ApiResultSet<QlSysAtta> querySysAttaByOid(String attaOid) {
        return webController.querySysAttaByOid(attaOid);
    }

    @Override
    public ApiResultSet<String> getPhoneQrScanUuid() {
        return webController.getPhoneQrScanUuid();
    }

    @Override
    public ApiResultSet<String> getPhoneUploadStatus(String uuid) {
        return webController.getPhoneUploadStatus(uuid);
    }

    @Override
    public ApiResultSet<String> initPhoneUpload(String uuid, String caseMaterialOid) {
        return webController.initPhoneUpload(uuid, caseMaterialOid);
    }

    @Override
    public ApiResultSet<Boolean> checkPhoneUploadStatus(String uuid) {
        return webController.checkPhoneUploadStatus(uuid);
    }

    @Override
    public ApiResultSet saveTempPhoneUploadAtta(String uuid, String attaOids) {
        return webController.saveTempPhoneUploadAtta(uuid, attaOids);
    }

    @Override
    public ApiResultSet<List<QlSysAtta>> getTempPhoneUploadAtta(String uuid) {
        return webController.getTempPhoneUploadAtta(uuid);
    }

    @Override
    public ApiResultSet<List<QlSysAtta>> uploadCaseMaterialFile(HttpServletRequest request, MultipartFile[] files) {
        return webController.uploadCaseMaterialFile(request, files);
    }

    @Override
    public ApiResultSet saveOrUpdateCaseMaterialAttaListForBrowser(List<QlCaseMaterial> qlCaseMaterialList) {
        return webController.saveOrUpdateCaseMaterialAttaListForBrowser(qlCaseMaterialList);
    }

    @Override
    public ApiResultSet<List<Map<String, String>>> updateQlCaseMaterialListForBrowser(List<QlCaseMaterial> qlCaseMaterialList) {
        return webController.updateQlCaseMaterialListForBrowser(qlCaseMaterialList);
    }

    @Override
    public ApiResultSet<String> createFlow(ElectronicSignatureDto electronicSignatureDto) {
        return webController.createFlow(electronicSignatureDto);
    }

    @Override
    public ApiResultSet saveOrUpdateSignatureFlow(SignatureFlowRecordSuper signatureFlowRecord) {
        return webController.saveOrUpdateSignatureFlow(signatureFlowRecord);
    }

    @Override
    public ApiResultSet<List<SignatureFlowRecordSuper>> getSignatureFlowRecordByCaseOid(String caseOid) {
        return webController.getSignatureFlowRecordByCaseOid(caseOid);
    }

    @Override
    public ApiResultSet getSignatureFlowRecord(String caseOid, String materialOid) {
        return webController.getSignatureFlowRecord(caseOid, materialOid);
    }

    @Override
    public ApiResultSet<List<TermlateDataSuperDto>> getTemplateList(String reportOid, String serviceOid) {
        return webController.getTemplateList(reportOid, serviceOid);
    }

    @Override
    public ApiResultSet<QlCaseExt> saveQlCaseExtForBrowers(QlCaseExt qlCaseExt) {
        return webController.saveQlCaseExtForBrowers(qlCaseExt);
    }

    @Override
    public ApiResultSet previewImageAndPdf(String fastdfsNginxUrl) {
        return webController.previewImageAndPdf(fastdfsNginxUrl);
    }

    @Override
    public void downloadFileByUrl(String fastdfsNginxUrl) {
        webController.downloadFileByUrl(fastdfsNginxUrl);
    }

    @Override
    public ApiResultSet getFormFillInfos(String serviceOid, String caseOid, String valOids) {
        return webController.getFormFillInfos(serviceOid, caseOid, valOids);
    }

    @Override
    public ApiResultSet<String> deleteZcQlCaseByCaseOid(String caseOid) {
        return webController.deleteZcQlCaseByCaseOid(caseOid);
    }

    @Override
    public ApiResultSet<Map<String, Object>> getAllBasicFieldByOid(String caseOid) {
        return webController.getAllBasicFieldByOid(caseOid);
    }

    @Override
    public ApiResultSet getHtmlType(String materialOid, String caseOid) {
        return webController.getHtmlType(materialOid, caseOid);
    }

    @Override
    public ApiResultSet getSignRole(String materialOid) {
        return webController.getSignRole(materialOid);
    }

    @Override
    public ApiResultSet saveSignaturePerson(MaterialSignPersonDto materialSignPersonDto) {
        return webController.saveSignaturePerson(materialSignPersonDto);
    }

    @Override
    public ApiResultSet getSignaturePerson(String materialOid, String caseOid) {
        return webController.getSignaturePerson(materialOid, caseOid);
    }

    @Override
    public ApiResultSet sendMessage(String id, String phone, String roleId, String signUrl) {
        return webController.sendMessage(id, phone, roleId, signUrl);
    }

    @Override
    public ApiResultSet<JSONArray> getWebsiteDetail(String bmmc) {
        return webController.getWebsiteDetail(bmmc);
    }

    @Override
    public ApiResultSet<JSONObject> getAccumulationAccount(String zjhm, String jcrxm) {
        return webController.getAccumulationAccount(zjhm, jcrxm);
    }

    @Override
    public ApiResultSet<String> getDepositeCertificatePrint(String grzh) {
        return webController.getDepositeCertificatePrint(grzh);
    }

    @Override
    public ApiResultSet<String> getSettleCertificatePrint(String jkrgjjzh, String jkhtbh) {
        return webController.getSettleCertificatePrint(jkrgjjzh, jkhtbh);
    }

    @Override
    public ApiResultSet<JSONObject> getLoanAccount(String jkrzjh, String xingming) {
        return webController.getLoanAccount(jkrzjh, xingming);
    }

    @Override
    public ApiResultSet<JSONArray> getRefundDetail(String zjhm, String xingming) {
        return webController.getRefundDetail(zjhm, xingming);
    }

    @Override
    public ApiResultSet<JSONArray> getLoanSchedule(String zjhm, String xingming) {
        return webController.getLoanSchedule(zjhm, xingming);
    }

    @Override
    public ApiResultSet verificationRules(String interApiCode, ApiReqParams reqParams) {
        return webController.verificationRules(interApiCode, reqParams);
    }
}
