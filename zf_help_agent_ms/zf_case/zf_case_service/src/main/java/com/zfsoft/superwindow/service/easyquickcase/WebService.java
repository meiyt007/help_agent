package com.zfsoft.superwindow.service.easyquickcase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.cases.data.*;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.web.ReviewDto;
import com.zfsoft.superwindow.service.easyquickcase.data.ElectronicSignatureDto;
import com.zfsoft.superwindow.service.easyquickcase.data.MaterialSignPersonDto;
import com.zfsoft.superwindow.service.easyquickcase.data.SignatureFlowRecordSuper;
import com.zfsoft.superwindow.service.easyquickcase.data.TermlateDataSuperDto;
import com.zfsoft.superwindow.service.interapi.data.ApiReqParams;
import com.zfsoft.service.sxConditionalRules.data.SxConditionalRules;
import com.zfsoft.service.sxService.data.SxFormInfo;
import com.zfsoft.service.sxService.data.SxService;
import com.zfsoft.service.sxSituation.data.SxServiceOptionTitle;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author hut
 * @date 2022/5/31
 */
@RequestMapping("/web")
public interface WebService {

    @RequestMapping(value = "/test", method = {RequestMethod.GET})
    ApiResultSet test();

    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    ApiResultSet login();

    @RequestMapping(value = "/listSxServicePage", method = {RequestMethod.POST})
    ApiResultSet<PageResult<SxService>> listSxServicePage(
            @RequestParam(value = "serviceName", required = false) String serviceName,
            @RequestParam(value = "districtOid", required = false) String districtOid,
            @RequestParam(value = "organOid", required = false) String organOid,
            @RequestParam(value = "handleType", required = false) String handleType,
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * @description 获得登录注册自治区统一身份认证跳转接口地址
     * @param gotoUrl
     * @author wangyg
     * @date 2022/6/8
     * @return
     */
    @RequestMapping(value = "/getLoginUrl", method = {RequestMethod.POST})
    ApiResultSet getLoginUrl(@RequestParam(value = "gotoUrl") String gotoUrl);

    /**
     * @description 推送办件信息到好差评
     * @param reviewDto
     * @author meiyt
     * @date 2022/6/8
     * @return
     **/
    @RequestMapping(value = "/projectDataCollection", method = RequestMethod.POST)
    ApiResultSet reviewsDocking(@RequestBody ReviewDto reviewDto);

    /**
     * @description 根据caseNumber的办件信息
     * @param caseNumber
     * @author wangyg
     * @date 2022/6/11
     * @return
     */
    @RequestMapping(value = "/queryQlCaseByCaseNumber", method = {RequestMethod.GET})
    ApiResultSet queryQlCaseByCaseNumber(@RequestParam(value = "caseNumber") String caseNumber);

    /**
     * @description 办件业务主键获取办件材料
     * @param caseOid
     * @author wangyg
     * @date 2022/6/11
     * @return
     */
    @RequestMapping(value = "/queryQlCaseMaterialByCaseOid", method = {RequestMethod.GET})
    ApiResultSet queryQlCaseMaterialByCaseOid(@RequestParam(value = "caseOid") String caseOid);

    /**
     * @description 通过办件业务主键获取办件环节审核列表信息
     * @param caseOid
     * @author wangyg
     * @date 2022/6/11
     * @return
     */
    @RequestMapping(value = "/queryQlCaseLinkResultListByCaseOid", method = {RequestMethod.GET})
    ApiResultSet queryQlCaseLinkResultListByCaseOid(@RequestParam(value = "caseOid") String caseOid);

    /**
     * @description 办件业务主键查询办件扩展信息
     * @param caseOid
     * @author wangyg
     * @date 2022/6/11
     * @return
     */
    @RequestMapping(value = "/queryQlCaseExtByCaseOid", method = {RequestMethod.GET})
    ApiResultSet queryQlCaseExtByCaseOid(@RequestParam(value = "caseOid") String caseOid);

    /**
     * @description 根据办件业务主键查询办件申请信息
     * @param caseOid
     * @author wangyg
     * @date 2022/6/11
     * @return
     */
    @RequestMapping(value = "/queryQlCaseApplayByCaseOid", method = {RequestMethod.GET})
    ApiResultSet queryQlCaseApplayByCaseOid(@RequestParam(value = "caseOid") String caseOid);

    /**
     * @description 初始化实施清单的信息
     * @param serviceOid
     * @author wangyg
     * @date 2022/6/11
     * @return
     */
    @RequestMapping(value = "/viewSxService", method = {RequestMethod.GET})
    ApiResultSet viewSxService(@RequestParam(value = "serviceOid") String serviceOid);

    /**
     * @description 单独查询事项信息
     * @param serviceOid
     * @author meiyt
     * @date 2022/6/27
     * @return
     **/
    @RequestMapping(value = "/getSxServiceByServiceOid", method = {RequestMethod.GET})
    ApiResultSet<SxService> getSxServiceByServiceOid(@RequestParam(value = "serviceOid") String serviceOid);

    /**
     * @description 根据办件获取情形标题选项信息
     * @param caseOid
     * @author wangyg
     * @date 2022/6/11
     * @return
     */
    @RequestMapping(value = "/getCaseTitleValueList", method = {RequestMethod.GET})
    ApiResultSet getCaseTitleValueList(@RequestParam(value = "caseOid") String caseOid);

    /**
     * @description 根据办件获取表单信息
     * @param caseOid
     * @author wangyg
     * @date 2022/6/11
     * @return
     */
    @RequestMapping(value = "/queryFormInfoByCaseOid", method = {RequestMethod.GET})
    ApiResultSet queryFormInfoByCaseOid(@RequestParam(value = "caseOid") String caseOid);


    /**
     * @description 根据办件编号查询信息
     * @param caseOid
     * @author wangyg
     * @date 2022/6/11
     * @return
     */
    @RequestMapping(value = "/getListByCaseOid", method = {RequestMethod.POST})
    ApiResultSet getListByCaseOid(@RequestParam(value = "caseOid") String caseOid);

    /**
     * @description 获取材料出库信息包括材料名称
     * @param caseNumber
     * @author wangyg
     * @date 2022/6/11
     * @return
     */
    @RequestMapping(value = "/getCaseMaterialOutByCaseNumber", method = {RequestMethod.POST})
    ApiResultSet getCaseMaterialOutByCaseNumber(@RequestParam(value = "caseNumber") String caseNumber);

    /**
     * @description 根据办件主键查询签收信息
     * @param caseOid
     * @author wangyg
     * @date 2022/6/11
     * @return
     */
    @RequestMapping(value = "/getOneByCaseOid", method = {RequestMethod.POST})
    ApiResultSet getOneByCaseOid(@RequestParam(value = "caseOid") String caseOid);

    /**
     * @description 根据办件主键查询签收信息
     * @param caseOid
     * @author wangyg
     * @date 2022/6/11
     * @return
     */
    @RequestMapping(value = "/getLicenseIssuedByCaseOid", method = {RequestMethod.POST})
    ApiResultSet getLicenseIssuedByCaseOid(@RequestParam(value = "caseOid") String caseOid);

    /**
     * @description 获取ems发证信息
     * @param caseOid
     * @author wangyg
     * @date 2022/6/11
     * @return
     */
    @RequestMapping(value = "/getDeliverRecordByCaseOid", method = {RequestMethod.POST})
    ApiResultSet getDeliverRecordByCaseOid(@RequestParam(value = "caseOid") String caseOid);

    /**
     * @description 根据区划及事项办理类型查询机构列表
     * @param districtOid
     * @param handleType
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping(value = "/listOrganByDistrictAndService", method = {RequestMethod.POST})
    ApiResultSet<List<Map<String, Object>>> listOrganByDistrictAndService(@RequestParam(value = "districtOid", required = false) String districtOid,
                                                                          @RequestParam(value = "handleType", required = false) String handleType);

    /**
     * @description 获取事项办理地点
     * @param serviceOid
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping( value = "/getLocationInfoByServiceOid",method = {RequestMethod.GET})
    ApiResultSet getLocationInfoByServiceOid(@RequestParam(value = "serviceOid", required = false) String serviceOid);

    /**
     * @description 获取用户类型及用户信息
     * @param token
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @GetMapping("/getLoginUser")
    ApiResultSet<Map<String, Object>> getLoginUser(@RequestHeader("access_token") String token);

    /**
     * @description 根据serviceOid获取所属事项的所有没有任何关系的选项标题(智能登记)
     * @param serviceOid
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping(value = "/getSxServiceTitlesNoRelation", method = {RequestMethod.GET})
    ApiResultSet<List<SxServiceOptionTitle>> getSxServiceTitlesNoRelation(@RequestParam(value = "serviceOid") String serviceOid);

    /**
     * @description 根据serviceOid和选项值获取所属事项的所有有关系的选项标题
     * @param serviceOid
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping(value = "/querySxServiceSituationRelation", method = {RequestMethod.GET})
    ApiResultSet<List<SxServiceOptionTitle>> querySxServiceSituationRelation(@RequestParam(value = "serviceOid") String serviceOid,
                                                                             @RequestParam(value = "valOids") String valOids,
                                                                             @RequestParam(value = "currentOid") String currentOid,
                                                                             @RequestParam(value = "currentTitleOid") String currentTitleOid,
                                                                             @RequestParam(value = "rootTitleOid") String rootTitleOid);

    /**
     * @description 专属指南
     * @param serviceOid
     * @param optionValOids
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping( value = "/getExclusiveGuide",method = {RequestMethod.GET})
    ApiResultSet<Map<String, Object>>  getExclusiveGuide(@RequestParam(value = "serviceOid", required = false) String serviceOid,
                                                         @RequestParam(value = "optionValOids", required = false) String optionValOids);

    /**
     * @description 根据事项oid查询事项条件预检列表
     * @param serviceOid
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping(value="/listSxConditionalRules",method = RequestMethod.POST)
    ApiResultSet<List<SxConditionalRules>> listSxConditionalRules(@RequestParam(value = "serviceOid", required = false) String serviceOid);

    /**
     * @description 根据事项oid查询事项条件秒批列表
     * @param serviceOid
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping(value="/fastApproval/listSxConditionalRules",method = RequestMethod.POST)
    ApiResultSet<List<SxConditionalRules>> fastListSxConditionalRules(@RequestParam(value = "serviceOid", required = false) String serviceOid);

    /**
     *  执行预检接口
     * @param interApiCode
     * @param reqParams
     * @return
     */
    @PostMapping("/execute/{interApiCode}")
    ApiResultSet<Boolean> executeCondRule(@PathVariable String interApiCode, @RequestBody ApiReqParams reqParams);

    /**
     *  执行秒批接口
     * @param interApiCode
     * @param reqParams
     * @return
     */
    @PostMapping("/fastApproval/execute/{interApiCode}")
    ApiResultSet<Boolean> executeFastApproval(@PathVariable String interApiCode, @RequestBody ApiReqParams reqParams);



    /**
     * @description 网站端保存基本信息
     * @param object
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping(value = "/saveQlCase", method = {RequestMethod.POST})
    ApiResultSet<Map<String, Object>> browserSaveQlCase(@RequestBody Object object);

    /**
     * @description 网站端获取已设计发布的表单列表
     * @param serviceOid
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping(value = "/getDesignFormList", method = {RequestMethod.GET})
    ApiResultSet<List<SxFormInfo>> getBrowserDesignFormList(@RequestParam(value = "serviceOid") String serviceOid);

    /**
     * @description 网站办件关联更新表单信息
     * @param qlCase
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping(value = "/updateFormInfo", method = {RequestMethod.POST})
    ApiResultSet<Map<String, Object>> browserUpdateFormInfo(@RequestBody QlCase qlCase);

    /**
     * @description 查询材料信息（网站端）
     * @param caseOid
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping(value = "/queryQlCaseMaterialListByCaseOid", method = {RequestMethod.GET})
    ApiResultSet<Map<String, List<QlCaseMaterial>>> queryQlCaseMaterialListByCaseOid(@RequestParam(value = "caseOid", required = false) String caseOid);


    /**
     * @description 网站端历史办件列表
     * @param caseNumber
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping( value = "/queryDoneQlCasePage",method = {RequestMethod.GET})
    ApiResultSet<PageResult<QlCase>> queryDoneQlCasePageForBrowers(@RequestParam(value = "caseNumber", required = false) String caseNumber,
                                                                   @RequestParam(value = "serviceName", required = false) String serviceName,
                                                                   @RequestParam(value = "netUserId", required = false) String netUserId,
                                                                   @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * @description 网站端结果送达
     * @param qlCaseApplay
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping( value = "/savePostResultInfo",method = {RequestMethod.POST})
    ApiResultSet<QlCaseApplay> savePostResultInfoForBrowers(@RequestBody QlCaseApplay qlCaseApplay);

    /**
     * @description 附件业务主键查看附件信息
     * @param attaOid
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping(value = "/querySysAttaByOid", method = {RequestMethod.GET})
    ApiResultSet<QlSysAtta> querySysAttaByOid(@RequestParam("attaOid") String attaOid);

    /**
     * @description 获取二维码uuid
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping( value = "/getPhoneQrScanRandom",method = {RequestMethod.GET})
    ApiResultSet<String> getPhoneQrScanUuid();

    /**
     * @description 获取扫码上传状态
     * @param uuid
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping( value = "/getPhoneUploadStatus",method = {RequestMethod.GET})
    ApiResultSet<String> getPhoneUploadStatus(@RequestParam(value = "uuid", required = false) String uuid);

    /**
     * @description 初始化上传信息
     * @param uuid
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping( value = "/initPhoneUpload",method = {RequestMethod.POST})
    ApiResultSet<String> initPhoneUpload(@RequestParam(value = "uuid", required = false) String uuid,
                                         @RequestParam(value = "caseMaterialOid", required = false) String caseMaterialOid);

    /**
     * @description 检测上传状态
     * @param uuid
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping( value = "/checkPhoneUploadStatus",method = {RequestMethod.GET})
    ApiResultSet<Boolean> checkPhoneUploadStatus(@RequestParam(value = "uuid", required = false) String uuid);

    /**
     * @description 保存上传信息
     * @param uuid
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping( value = "/saveTempPhoneUploadAtta",method = {RequestMethod.POST})
    ApiResultSet saveTempPhoneUploadAtta(@RequestParam(value = "uuid", required = false) String uuid,
                                         @RequestParam(value = "attaOids", required = false) String attaOids);

    /**
     * @description 获取上传信息
     * @param uuid
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping( value = "/getTempPhoneUploadAtta",method = {RequestMethod.GET})
    ApiResultSet<List<QlSysAtta>> getTempPhoneUploadAtta(@RequestParam(value = "uuid", required = false) String uuid);

    /**
     * @description 上传办件材料附件
     * @param request
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping( value = "/uploadCaseMaterialFile",method = {RequestMethod.POST}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResultSet<List<QlSysAtta>> uploadCaseMaterialFile(HttpServletRequest request,
                                                         @RequestPart(value = "files", required = false) MultipartFile[] files);

    /**
     * @description 网站端更新材料附件
     * @param qlCaseMaterialList
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping( value = "/saveOrUpdateCaseMaterialAttaList",method = {RequestMethod.POST})
    ApiResultSet saveOrUpdateCaseMaterialAttaListForBrowser(@RequestBody List<QlCaseMaterial> qlCaseMaterialList);

    /**
     * @description 网站端更新材料关联
     * @param qlCaseMaterialList
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping(value = "/updateQlCaseMaterialList", method = {RequestMethod.POST})
    ApiResultSet<List<Map<String, String>>> updateQlCaseMaterialListForBrowser(@RequestBody List<QlCaseMaterial> qlCaseMaterialList);

    /**
     * @description 创建签署流程
     * @param electronicSignatureDto
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping(value = "/createFlow", method = {RequestMethod.POST})
    ApiResultSet<String> createFlow(@RequestBody ElectronicSignatureDto electronicSignatureDto);

    /**
     * @description 保存或更新签章流程记录
     * @param signatureFlowRecord
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateSignatureFlow", method = {RequestMethod.POST})
    ApiResultSet saveOrUpdateSignatureFlow(@RequestBody SignatureFlowRecordSuper signatureFlowRecord);

    /**
     * @description 根据办件编号查询签章流程记录
     * @param caseOid
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping(value = "/getSignatureFlowRecordByCaseOid", method = {RequestMethod.GET})
    ApiResultSet<List<SignatureFlowRecordSuper>> getSignatureFlowRecordByCaseOid(@RequestParam(value = "caseOid", required = false) String caseOid);

    /**
     * @description 根据办件编号和材料获取流程状态
     * @param caseOid
     * @param materialOid
     * @author meiyt
     * @date 2022/6/23
     * @return
     **/
    @RequestMapping(value = "/getSignatureFlowRecord", method = {RequestMethod.GET})
    ApiResultSet getSignatureFlowRecord(@RequestParam(value = "caseOid", required = false) String caseOid,
                                        @RequestParam(value = "materialOid", required = false) String materialOid);

    /**
     * @description 获取表单填充模板集合
     * @param reportOid
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping(value = {"/getTemplateList"}, method = {RequestMethod.GET})
    ApiResultSet<List<TermlateDataSuperDto>> getTemplateList(@RequestParam(value = "reportOid") String reportOid,
                                                             @RequestParam(value = "serviceOid") String serviceOid);

    /**
     * @description 网站端保存办件扩展信息
     * @param qlCaseExt
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping( value = "/saveQlCaseExt",method = {RequestMethod.POST})
    ApiResultSet<QlCaseExt> saveQlCaseExtForBrowers(@RequestBody QlCaseExt qlCaseExt);

    /**
     * @description 图片或pdf预览
     * @param
     * @author meiyt
     * @date 2022/6/17
     * @return
     **/
    @RequestMapping( value = "/previewImageAndPdf",method = {RequestMethod.GET})
    ApiResultSet previewImageAndPdf(@RequestParam(value = "fastdfsNginxUrl") String fastdfsNginxUrl);


    /**
     * @description fast文件下载
     * @param
     * @author zhujj
     * @date 2022/6/17
     * @return
     **/
    @RequestMapping( value = "/downloadFileByUrl",method = {RequestMethod.GET})
    void downloadFileByUrl(@RequestParam(value = "fastdfsNginxUrl") String fastdfsNginxUrl);

    /**
     * @description 根据情形选项获取表单字段及预填信息
     * @param serviceOid
     * @param caseOid
     * @param valOids
     * @author meiyt
     * @date 2022/6/22
     * @return
     **/
    @RequestMapping(value = "/getFormFillInfos", method = {RequestMethod.GET})
    ApiResultSet getFormFillInfos(@RequestParam(value = "serviceOid", required = false) String serviceOid,
                                  @RequestParam(value = "caseOid", required = false) String caseOid,
                                  @RequestParam(value = "valOids", required = false) String valOids);

    /**
     * @description 删除指定caseOid的暂存办件信息
     * @param caseOid
     * @author wangyg
     * @date 2022/6/20
     * @return
     */
    @RequestMapping( value = "/deleteZcQlCaseByCaseOid",method = {RequestMethod.GET})
    ApiResultSet<String> deleteZcQlCaseByCaseOid(@RequestParam("caseOid") String caseOid);

    /**
     * @description 表单预填信息
     * @param caseOid
     * @author meiyt
     * @date 2022/6/25
     * @return
     **/
    @GetMapping(value="/getAllBasicFieldByOid")
    ApiResultSet<Map<String,Object>> getAllBasicFieldByOid(@RequestParam(value = "caseOid") String caseOid);

    /**
     * 要不多加一个接口，用来判断是进保存还是进通知页面
     */
    @RequestMapping(value = "/getHtmlType", method = {RequestMethod.GET})
    ApiResultSet getHtmlType(@RequestParam(value = "materialOid") String materialOid,
                             @RequestParam(value = "caseOid") String caseOid);

    /**
     * 根据材料oid查询配置的签名角色
     */
    @RequestMapping(value = "/getSignRole", method = {RequestMethod.GET})
    ApiResultSet getSignRole(@RequestParam(value = "materialOid") String materialOid);

    /**
     * 新增签章人 -- 修改暂时不做，特殊情况先做新增
     * 同时还需要修改材料表的 是否配置签章状态
     * *是否能获取到签章地址
     */
    @RequestMapping(value = "/saveSignaturePerson", method = {RequestMethod.POST})
    ApiResultSet saveSignaturePerson(@RequestBody MaterialSignPersonDto materialSignPersonDto);

    /**
     * 根据材料id查询该材料下配置的签章人
     * 查询出列表，然后再拼接array
     */
    @RequestMapping(value = "/getSignaturePerson", method = {RequestMethod.GET})
    ApiResultSet getSignaturePerson(@RequestParam(value = "materialOid") String materialOid,
                                    @RequestParam(value = "caseOid") String caseOid);


    /**
     * 发送短信
     * 根据材料oid获取短信模板
     * 拼接签章地址
     */
    @RequestMapping(value = "/sendMessage", method = {RequestMethod.POST})
    ApiResultSet sendMessage(@RequestParam(value = "id") String id,
                             @RequestParam(value = "phone") String phone,
                             @RequestParam(value = "roleId") String roleId,
                             @RequestParam(value = "signUrl") String signUrl);

    /**
     * @description 网点明细查询
     * @param bmmc
     * @author wangyg
     * @date 2022/6/28
     * @return
     */
    @RequestMapping( value = "/getWebsiteDetail",method = {RequestMethod.GET})
    ApiResultSet<JSONArray> getWebsiteDetail(@RequestParam("bmmc") String bmmc);

    /**
     * @description 公积金账户查询
     * @param zjhm
     * @author wangyg
     * @date 2022/6/28
     * @return
     */
    @RequestMapping( value = "/getAccumulationAccount",method = {RequestMethod.GET})
    ApiResultSet<JSONObject> getAccumulationAccount(@RequestParam("zjhm") String zjhm, @RequestParam("jcrxm") String jcrxm);

    /**
     * @description 缴存证明打印接口
     * @param grzh
     * @author wangyg
     * @date 2022/6/28
     * @return
     */
    @RequestMapping( value = "/getDepositeCertificatePrint",method = {RequestMethod.GET})
    ApiResultSet<String> getDepositeCertificatePrint(@RequestParam("grzh") String grzh);

    /**
     * @description 结清证明打印接口
     * @param jkrgjjzh
     * @author wangyg
     * @date 2022/6/28
     * @return
     */
    @RequestMapping( value = "/getSettleCertificatePrint",method = {RequestMethod.GET})
    ApiResultSet<String> getSettleCertificatePrint(@RequestParam("jkrgjjzh") String jkrgjjzh, @RequestParam("jkhtbh") String jkhtbh);

    /**
     * @description 贷款账户查询
     * @param jkrzjh
     * @author wangyg
     * @date 2022/6/28
     * @return
     */
    @RequestMapping( value = "/getLoanAccount",method = {RequestMethod.GET})
    ApiResultSet<JSONObject> getLoanAccount(@RequestParam("jkrzjh") String jkrzjh, @RequestParam("xingming") String xingming);

    /**
     * @description 贷款还款明细查询
     * @param zjhm
     * @author wangyg
     * @date 2022/6/28
     * @return
     */
    @RequestMapping( value = "/getRefundDetail",method = {RequestMethod.GET})
    ApiResultSet<JSONArray> getRefundDetail(@RequestParam("zjhm") String zjhm, @RequestParam("xingming") String xingming);

    /**
     * @description 贷款进度查询
     * @param zjhm
     * @author wangyg
     * @date 2022/6/28
     * @return
     */
    @RequestMapping( value = "/getLoanSchedule",method = {RequestMethod.GET})
    ApiResultSet<JSONArray> getLoanSchedule(@RequestParam("zjhm") String zjhm, @RequestParam("xingming") String xingming);

    /**
     * 通过 interApiCode 查询接口信息
     * 查询该事项对应的秒批配置
     * 查询该办件对应的秒批规则是否均执行完毕
     * @author WangKe
     * @date 2022/6/30
     * @param interApiCode 接口code值
     * @param reqParams 参数类
     * @return
     */
    @PostMapping("/fastApproval/final/execute/{interApiCode}")
    ApiResultSet verificationRules(@PathVariable String interApiCode, @RequestBody ApiReqParams reqParams);

    @RequestMapping(value = "/classify/getUploadMaterialClassification", method = {RequestMethod.POST})
    @ApiOperation(value = "获取上传材料分类结果")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "caseOid", value = "办件id", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "caseMaterialOid", value = "办件材料id", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "attaOid", value = "附件id", dataType = "string")})
    ApiResultSet getUploadMaterialClassification(@RequestParam(value = "caseOid") String caseOid,
                                                 @RequestParam(value = "caseMaterialOid") String caseMaterialOid,
                                                 @RequestParam(value = "attaOid") String attaOid);

    @RequestMapping(value = "/preTrial/getIntelligentAuditMaterialList", method = {RequestMethod.GET})
    @ApiOperation(value = "获取智审材料列表")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "caseOid", value = "办件id", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "serviceOid", value = "事项id", dataType = "string")})
    ApiResultSet getIntelligentAuditMaterialList(@RequestParam(value = "caseOid") String caseOid,
                                                 @RequestParam(value = "serviceOid") String serviceOid);

    @RequestMapping(value = "/preTrial/viewMaterialAuditDetailResult", method = {RequestMethod.GET})
    @ApiOperation(value = "查询智审结果信息")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "caseOid", value = "办件id", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "caseMaterialOids", value = "办件材料ids", dataType = "string")})
    ApiResultSet viewMaterialAuditDetailResult(@RequestParam(value = "caseOid") String caseOid,
                                               @RequestParam(value = "caseMaterialOids") String caseMaterialOids);

    @RequestMapping(value = "/preTrial/intelligentAuditByCaseMaterialOid", method = {RequestMethod.GET})
    @ApiOperation(value = "根据办件材料id进行智审")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "caseMaterialOid", value = "办件材料id", dataType = "string")})
    ApiResultSet intelligentAuditByCaseMaterialOid(@RequestParam(value = "caseMaterialOid") String caseMaterialOid);
}
