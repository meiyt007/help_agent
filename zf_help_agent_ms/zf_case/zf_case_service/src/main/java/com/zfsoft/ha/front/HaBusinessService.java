package com.zfsoft.ha.front;

import com.alibaba.fastjson.JSONArray;
import com.zfsoft.cases.data.*;
import com.zfsoft.cases.data.vo.SxFormInfoVo;
import com.zfsoft.ha.data.HaCaseExpress;
import com.zfsoft.ha.data.requestData.HaAttaPushRequestData;
import com.zfsoft.ha.data.requestData.HaEvalPushRequestData;
import com.zfsoft.ha.data.requestData.HaPolicyPushRequestData;
import com.zfsoft.ha.data.requestData.HaResourcePushRequestData;
import com.zfsoft.ha.data.vo.OnekeyPushVo;
import com.zfsoft.ha.data.vo.WinNumbVO;
import com.zfsoft.microservice.form.data.vo.FormDataVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxService.data.SxService;
import com.zfsoft.service.sxSituation.data.SxServiceOptionTitle;
import com.zfsoft.service.sxSituation.data.vo.ServiceMaterialVo;
import com.zfsoft.service.sxSys.data.SxSysAtta;
import com.zfsoft.superwindow.service.easyquickcase.data.TermlateDataSuperDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @description: 帮代办服务：前台业务、事项、办件相关接口
 * @author: kangax
 * @date: 2022-07-29 10:07
 **/
@RequestMapping(value = "/ha/web")
public interface HaBusinessService {

    /**
     * 将数据保存到redis中，推送到万达获取办件编码接口中，当做入参
     *
     * @param wdDataVo 过渡VO
     * @return 保存结果
     * @author wangyuhang
     * @date 2023年01月12 10:01:29
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveData", method = {RequestMethod.POST})
    ApiResultSet saveData(WdDataVo wdDataVo);

    /*规范写日志、规范写注释*/


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
     * @date：2022年07月29 14:23:22
     */
    @RequestMapping(value = "/listSxServicePage")
    ApiResultSet<PageResult<SxService>> listSxServicePage(@RequestParam(value = "serviceName", required = false) String serviceName,
                                                          @RequestParam(value = "districtOid", required = false) String districtOid,
                                                          @RequestParam(value = "organOid", required = false) String organOid,
                                                          @RequestParam(value = "handleType", required = false) String handleType,
                                                          @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                          @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * 更新修改办件材料附件
     *
     * @param qlCaseMaterialList 材料附件集合
     * @return 保存结果
     * @author yupeng
     * @date 2022年08月16 10:01:29
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdateCaseMaterialAttaList", method = {RequestMethod.POST})
    ApiResultSet saveOrUpdateCaseMaterialAttaList(@RequestBody List<QlCaseMaterial> qlCaseMaterialList);

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
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveCaseAccpet", method = {RequestMethod.POST})
    ApiResultSet<Map<String, String>> saveCaseAccept(@RequestParam("caseOid") String caseOid, @RequestParam("userOid") String userOid, @RequestParam("userName") String userName, @RequestParam("finalOpinion") String finalOpinion, @RequestParam("finalOpinionDesc") String finalOpinionDesc, @RequestParam(value = "sjStatus", required = false) Integer sjStatus);
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
    @RequestMapping(value = "/listOrganByDistrictAndService", method = {RequestMethod.POST})
    ApiResultSet<List<Map<String, Object>>> listOrganByDistrictAndService(@RequestParam(value = "districtOid", required = false) String districtOid, @RequestParam(value = "handleType", required = false) String handleType);

    /**
     * @param serviceOid 事项编号
     * @return piResultSet<List < SxServiceOptionTitle>> 根据serviceOid获取所属事项的所有没有任何关系的选项标题
     * @description 根据serviceOid获取所属事项的所有没有任何关系的选项标题(智能登记)
     * @author wangyh
     * @date 2022-07-29 10:07
     */
    @RequestMapping(value = "/getSxServiceTitlesNoRelation", method = {RequestMethod.GET})
    ApiResultSet<List<SxServiceOptionTitle>> getSxServiceTitlesNoRelation(@RequestParam(value = "serviceOid") String serviceOid);

    /**
     * @param serviceOid
     * @param caseOid
     * @param valOids
     * @return ApiResultSet 根据情形选项获取表单字段及预填详细信息
     * @description 根据情形选项获取表单字段及预填信息
     * @author wangyh
     * @date 2022-08-01 10:07
     **/
    @RequestMapping(value = "/getFormFillInfos", method = {RequestMethod.GET})
    ApiResultSet getFormFillInfos(@RequestParam(value = "serviceOid", required = false) String serviceOid, @RequestParam(value = "caseOid", required = false) String caseOid, @RequestParam(value = "valOids", required = false) String valOids);

    /**
     * @param caseOid
     * @return ApiResultSet<Map < String, Object>> 获取表单预填信息详细信息
     * @description 表单预填信息
     * @author wangyh
     * @date 2022-08-01 10:07
     **/
    @GetMapping(value = "/getAllBasicFieldByOid")
    ApiResultSet<Map<String, Object>> getAllBasicFieldByOid(@RequestParam(value = "caseOid") String caseOid);

    /**
     *  基础表单和电子表单关联字段
     * @param serviceOid
     * @return
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/getBasicAndFormFieldRel")
    ApiResultSet<JSONArray> getBasicAndFormFieldRel(@RequestParam(value = "serviceOid") String serviceOid);

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
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryDoneQlCasePage", method = {RequestMethod.POST})
    ApiResultSet<PageResult<QlCase>> queryDoneQlCasePage(@RequestParam(value = "caseNumber", required = false) String caseNumber, @RequestParam(value = "serviceName", required = false) String serviceName, @RequestParam(value = "beginDate", required = false) String beginDate, @RequestParam(value = "endDate", required = false) String endDate, @RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * @param qlCaseMaterialList
     * @return ApiResultSet<List < Map < String, String>>> 更新材料关联
     * @description 更新材料关联
     * @author wangyh
     * @date 2022-8-11 10:29:01
     */
    @RequestMapping(value = "/updateQlCaseMaterialList", method = {RequestMethod.POST})
    ApiResultSet<List<Map<String, String>>> updateQlCaseMaterialListForBrowser(@RequestBody List<QlCaseMaterial> qlCaseMaterialList);
    /********************************王宇航end**************************/


    /********************************赵冰峰start**************************/
    /**
     * 根据serviceOid和选项值获取所属事项的所有有关系的选项标题
     *
     * @param serviceOid      事项信息id
     * @param valOids
     * @param currentOid
     * @param currentTitleOid
     * @param rootTitleOid
     * @author: zhaobf
     * @date: 2022-08-01 11:37
     */
    @RequestMapping(value = "/querySxServiceSituationRelation", method = {RequestMethod.POST})
    ApiResultSet<List<SxServiceOptionTitle>> querySxServiceSituationRelation(String serviceOid, String valOids, String currentOid, String currentTitleOid, String rootTitleOid);

    /**
     * 根据事项 情形 选择获取材料和精细化材料
     *
     * @param serviceOid    所属事项主健
     * @param optionValOids 选项主键
     * @author: gaolh
     * @Date: 2022-8-13 13:40:06
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getSituationMaterialListByOids", method = {RequestMethod.GET})
    ApiResultSet<List<ServiceMaterialVo>> getSituationMaterialListByOids(@RequestParam(value = "serviceOid", required = false) String serviceOid, @RequestParam(value = "optionValOids", required = false) String optionValOids);

    /**
     * 实施清单的信息
     *
     * @param serviceOid 事项信息id
     * @author: zhaobf
     * @date: 2022-08-01 11:37
     */
    @RequestMapping(value = "/viewSxService", method = {RequestMethod.GET})
    ApiResultSet viewSxService(String serviceOid);

    /**
     * 获取表单填充模板集合
     *
     * @param reportOid  办件OID
     * @param serviceOid 事项信息id
     * @author: zhaobf
     * @date: 2022-08-01 11:37
     */
    @RequestMapping(value = "/getTemplateList", method = {RequestMethod.GET})
    ApiResultSet<List<TermlateDataSuperDto>> getTemplateList(String reportOid, String serviceOid);

    /**
     * 用户服务类型权限 获取用户在该事项编码种是否又权限
     *
     * @param serviceOids 事项编号
     * @author: zhaobf
     * @date: 2022-08-05 10:49
     */
    @RequestMapping(value = "/serviceType", method = {RequestMethod.GET})
    ApiResultSet serviceType(@RequestParam(value = "serviceOids") String serviceOids);

    /**
     * @param caseOid
     * @return
     * @description 根据办件业务主键查询办件申请信息
     * @author: zhaobf
     * @date: 2022-08-05 10:49
     */
    @RequestMapping(value = "/queryQlCaseApplayByCaseOid", method = {RequestMethod.GET})
    ApiResultSet queryQlCaseApplayByCaseOid(@RequestParam(value = "caseOid") String caseOid);

    /**
     * @param caseOid 办件主键
     * @return
     * @description 根据办件获取情形标题选项信息
     * @author zhaobf
     * @date 2022/8/11 09:17
     */
    @RequestMapping(value = "/getCaseTitleValueList", method = {RequestMethod.GET})
    ApiResultSet<List<Map<String, String>>> getCaseTitleValueList(@RequestParam(value = "caseOid") String caseOid);

    /**
     * 获取证件类型信息
     *
     * @param type 证件类型
     * @return
     * @author zhaobf
     * @date 2022/8/11 09:17
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getSelectCertificateType", method = {RequestMethod.GET})
    ApiResultSet<List<com.zfsoft.microservice.settings.data.SysDict>> getSelectCertificateType(@RequestParam("type") int type);


    /**
     * 根据证件号查询办件和申请人信息
     *
     * @param credentialNumber 证件号码
     * @param applyUserType    申请人类型
     * @param projectName      项目名称
     * @param caseNumber       办件编号
     * @param pageNum
     * @param pageSize
     * @return
     * @date 2022/8/11 09:17
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryQlCaseByCredentialNumber", method = {RequestMethod.GET})
    ApiResultSet<PageResult<QlCase>> queryQlCaseByCredentialNumber(@RequestParam(value = "credentialNumber") String credentialNumber, @RequestParam(value = "applyUserType") String applyUserType, @RequestParam(value = "projectName", required = false) String projectName, @RequestParam(value = "caseNumber", required = false) String caseNumber, @RequestParam(value = "pageNum") Integer pageNum, @RequestParam(value = "pageSize") Integer pageSize);

    /**
     * 根据办件编号获取电子表单信息
     * @param caseOid 办件编号
     * @author: zhaobf
     * @date 2022/8/29 16:16
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryFormInfoByCaseOid", method = {RequestMethod.GET})
    ApiResultSet<List<SxFormInfoVo>> queryFormInfoByCaseOid(@RequestParam(value = "caseOid") String caseOid);


    /**
     * 办件更新表单信息
     * @param qlCase
     * @return com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.data.vo.QlCaseVo>
     * @author zhaobf
     * @date 2022/9/2 16:16
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/updateFormInfo", method = {RequestMethod.POST})
    ApiResultSet<Map<String, Object>> updateQlCaseFormInfo(@RequestBody QlCase qlCase);

    /**
     * 一键推送接口（万行）
     * @param pushVo
     * @return
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/onekeyPush")
    ApiResultSet onekeyPush(@RequestBody @Valid OnekeyPushVo pushVo);

    /**
     * 号票推送接口（万行）
     * @param winNumbVO
     * @return
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/winNumbPush")
    ApiResultSet winNumbPush(@RequestBody @Valid WinNumbVO winNumbVO);

    @ProcessFeignCalledResult
    @GetMapping(value = "/getWeather")
    ApiResultSet getWeather();

    /**
     * @description 附件业务主键查看附件信息
     * @param oid
     * @author wangyg
     * @date 2022/6/13
     * @return
     */
    @RequestMapping(value = "/querySysAttaByOid", method = {RequestMethod.GET})
    ApiResultSet<SxSysAtta> getSxSysAttaByOid(@RequestParam("oid") String oid);

    @PostMapping(value = "/attaPush")
    ApiResultSet attaPush(@RequestBody @Valid HaAttaPushRequestData haAttaPushRequestData);

    @PostMapping(value = "/policyPush")
    ApiResultSet policyPush(@RequestBody @Valid HaPolicyPushRequestData haPolicyPushRequestData) ;

    @PostMapping(value = "/evalPush")
    ApiResultSet evalPush(@RequestBody @Valid HaEvalPushRequestData haEvalPushRequestData) ;

    @PostMapping(value = "/resourcePush")
    ApiResultSet resourcePush(@RequestBody @Valid HaResourcePushRequestData haResourcePushRequestData) ;
    /********************************赵冰峰end**************************/


    /********************************康翱翔start**************************/

    /**
     * @param designOid 设计主键
     * @param reportOid 存储类型为表单系统，reportOid是存储数据的主键，存储类型为数据库，reportOid是数据库主键 （API方式可以为null）
     * @description: 根据设计主键和表单数据主键获取数据
     * @author: wuxx
     * @Date: 2021/4/23 9:43
     **/
    @RequestMapping(value = "/getFormData", method = {RequestMethod.GET})
    ApiResultSet<FormDataVo> getFormData(@RequestParam("authorizeKey") String authorizeKey, @RequestParam("designOid") String designOid, @RequestParam(value = "reportOid", required = false) String reportOid);

    /**
     * 保存表单信息
     *
     * @param formDataVo authorizeKey 授权key
     * @param formDataVo formMainOid 设计主表的业务主键
     * @param formDataVo designOid 设计表的业务主键
     * @param formDataVo formData 存储对象数据的JSON
     * @description: 存储对象的数据的保存本地、数据库、接口返回
     * @author: gaolh
     * @Date: 2022-8-13 16:59:32
     **/
    @RequestMapping(value = "/saveFormData", method = {RequestMethod.POST})
    ApiResultSet<Map<String, Object>> saveFormData(@RequestBody FormDataVo formDataVo);

    /**
     * 办件下一步暂存基本信息
     *
     * @param object
     * @author gaolh
     * @date 2022-8-13 16:10:43
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/nextStepSaveQlCase", method = {RequestMethod.POST})
    ApiResultSet<Map<String, Object>> nextStepSaveQlCase(@RequestBody Object object);

    /**
     * @description: 专属指南
     * @params：[ serviceOid 事项主键, optionValOids 选项值 ]
     * @author: kangax
     * @date: 2022-08-01 11:59
     */
    @RequestMapping(value = "/getExclusiveGuide", method = {RequestMethod.GET})
    ApiResultSet<Map<String, Object>> getExclusiveGuide(@RequestParam(value = "serviceOid", required = false) String serviceOid, @RequestParam(value = "optionValOids", required = false) String optionValOids);

    /**
     * @description: 查询材料信息
     * @params：[ caseOid 办件编号]
     * @return: 材料list详细信息
     * @author: kangax
     * @date: 2022-08-02 10:17
     */
    @RequestMapping(value = "/queryQlCaseMaterialListByCaseOid", method = {RequestMethod.GET})
    ApiResultSet<Map<String, List<QlCaseMaterial>>> queryQlCaseMaterialListByCaseOid(@RequestParam(value = "caseOid", required = false) String caseOid);

    /**
     * @description: 查询简单材料信息
     * @params：[ caseOid 办件编号]
     * @return: 材料list详细信息
     * @author: zhaobf
     * @date: 2022-08-02 10:17
     */
    @RequestMapping(value = "/querySimpleQlCaseMaterialListByCaseOid", method = {RequestMethod.GET})
    ApiResultSet<Map<String, List<QlCaseMaterial>>> querySimpleQlCaseMaterialListByCaseOid(String caseOid);

    /**
     * 根据办件材料oid查询当前办件的材料附件情况
     * @param caseMaterialOid
     * @return
     */
    @RequestMapping(value = "/queryQlCaseMaterialAttaByCaseMaterialOid", method = {RequestMethod.GET})
    ApiResultSet<List<QlCaseMaterialAtta>> queryQlCaseMaterialAttaByCaseMaterialOid(String caseMaterialOid);

    /**
     * @description: 材料附件上传
     * @params：[ request, files 文件]
     * @author: kangax
     * @date: 2022-08-02 10:24
     */
    @RequestMapping(value = "/uploadCaseMaterialFile", method = {RequestMethod.POST}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResultSet<List<QlSysAtta>> uploadCaseMaterialFile(HttpServletRequest request, @RequestPart(value = "files", required = false) MultipartFile[] files);

    /**
     * @description: 根据caseNumber查询办件信息
     * @param caseNumber
     * @return
     * @author: kangax
     * @date: 2022-09-19 09:43
     */
    @RequestMapping(value = "/queryQlCaseByCaseNumber", method = {RequestMethod.GET})
    ApiResultSet queryQlCaseByCaseNumber(@RequestParam(value = "caseNumber") String caseNumber);

    /**
     * @description: 根据业务主键查询办件信息
     * @param caseOid
     * @return
     * @author: kangax
     * @date: 2022-09-19 09:43
     */
    @RequestMapping(value = "/queryQlCaseByCaseOid", method = {RequestMethod.GET})
    ApiResultSet queryQlCaseByCaseOid(@RequestParam(value = "caseOid") String caseOid);

    /**
     * @description: 办件业务主键获取办件材料
     * @param caseOid
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     * @author: kangax
     * @date: 2022-09-19 10:18
     */
    @RequestMapping(value = "/queryQlCaseMaterialByCaseOid", method = {RequestMethod.GET})
    ApiResultSet queryQlCaseMaterialByCaseOid(@RequestParam(value = "caseOid") String caseOid);

    @RequestMapping(value = "/sendHPSms", method = {RequestMethod.GET})
    ApiResultSet sendHPSms(String title,String phone, String message,String workUserId,String workUserName);

    /**
     * Description: 根据办件编号查询办件快递
     * @param qlCaseId
     * @author zhaobf
     * date: 2023/4/7 16:09
     * @copyright 版权由上海卓繁信息技术股份有限公司拥有
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryHaCaseExpressByQlCaseId", method = {RequestMethod.GET})
    ApiResultSet<HaCaseExpress> queryHaCaseExpressByQlCaseId(@RequestParam("qlCaseId") String qlCaseId);

    /********************************康翱翔end**************************/

}
