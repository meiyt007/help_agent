package com.zfsoft.ha.front;


import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.data.QlCaseMaterialAtta;
import com.zfsoft.cases.data.QlSysAtta;
import com.zfsoft.ha.data.HaVisit;
import com.zfsoft.ha.data.HaWorkLoginLocation;
import com.zfsoft.ha.data.requestData.HaAppointmentRequestData;
import com.zfsoft.ha.data.requestData.HaQueueRequestData;
import com.zfsoft.ha.data.responseData.HaWorkUserGroupResponseData;
import com.zfsoft.ha.data.responseData.HaWorkUserResponseData;
import com.zfsoft.ha.data.vo.HaAppointmentVo;
import com.zfsoft.ha.data.vo.ScanParam;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxService.data.SxService;
import com.zfsoft.service.sxSituation.data.vo.ServiceMaterialVo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 数据推送  提供给第三方调用，无需获取token
 *
 * @author yupeng
 * @version 1.0
 * @date 2022/7/28 14:30
 */
@RequestMapping("/ha/outer")
public interface HaOuterPushService {
    /**
     * 根据身份证查询当前办事人的排队情况
     * GET /ha/outer/querylineUpByIdNo
     * @date 2022/9/27 13:31
     */
    @ProcessFeignCalledResult
    @GetMapping("/querylineUpByIdNo")
    ApiResultSet querylineUpByIdNo(@RequestParam (value = "IdNo", required = true) String IdNo) throws ParseException;
    /**
     * 帮办区域整体排队情况
     * GET /ha/outer/getWorkUserVo
     * @date 2022/9/27 13:31
     */
    @ProcessFeignCalledResult
    @GetMapping("/queryAssistantRegionlineUp")
    ApiResultSet queryAssistantRegionlineUp() throws ParseException;

    /**
     * 排队人数及预计耗时接口
     * GET /ha/outer/getWorkUserVo
     * @param workUserID 员工id
     * @date 2022/9/27 13:31
     */
    @ProcessFeignCalledResult
    @GetMapping("/getWorkUserVo")
    ApiResultSet getWorkUserVo(@RequestParam (value = "workUserID", required = true) String workUserID);


    /**
     * 一键推送办事指南查询
     * GET /ha/outer/viewSxServiceGuide
     * @param caseOid 办件业务主键
     * @date 2022/9/27 13:31
     */
    @ProcessFeignCalledResult
    @GetMapping("/viewSxServiceGuide")
    ApiResultSet getWorkUserSchedule(String caseOid);

    /**
     * 接收旗舰店的预约数据
     *
     * @param haAppointmentRequestData 预约推送请求参数
     * @return ApiResultSet 返回预约结果信息
     * @author yupeng
     * @date 2022/7/28 14:31
     */
    @ProcessFeignCalledResult
    @PostMapping("/appointment/push")
    ApiResultSet pushAppointment(HaAppointmentRequestData haAppointmentRequestData);

    /**
     * 获取帮办人员七天的预约排班数据
     * GET /ha/outer/getWorkUserSchedule
     * @param workUserId 员工id
     * @date 2022/9/22 13:31
     */
    @ProcessFeignCalledResult
    @GetMapping("/getWorkUserSchedule")
    ApiResultSet getWorkUserSchedule(Long workUserId);

    /**
     * 获取预约数据
     * GET /ha/outer/getWorkUserAppoinment
     * @param cardNo 身份证号码
     * @date 2022/9/22 13:31
     */
    @ProcessFeignCalledResult
    @GetMapping("/getWorkUserAppoinment")
    ApiResultSet<PageResult<HaAppointmentVo>> getWorkUserAppoinment(String  cardNo,
                                                                    Integer pageNum,
                                                                    Integer pageSize);

    /**
     * 取消预约
     * GET /ha/outer/appointment/remove
     */
    @ProcessFeignCalledResult
    @GetMapping("/appointment/remove")
    ApiResultSet appointmentRemove(Long appointmentId);

    @ProcessFeignCalledResult
    @PostMapping(value = "/queryAppointmentInfo")
    ApiResultSet<List<HaAppointmentVo>> queryAppointmentInfo(String cardNo);

    /**
     * 排队叫号推送
     *
     * @param haQueueRequestData 排队叫号推送请求参数
     * @return ApiResultSet 返回排队叫号推送结果信息
     * @author zhaobf
     * @date 2022/7/28  15:00
     */
    @ProcessFeignCalledResult
    @PostMapping("/number/push")
    ApiResultSet pushNumber(@RequestBody HaQueueRequestData haQueueRequestData);


    /**
     * 获取帮代办服务的列表
     *
     * @param cardNo      身份证号码
     * @param name        姓名
//     * @param companyName 企业名称
//     * @param companyCode 统一社会信用代码证
     * @return 帮代办服务的列表
     * @author yupeng
     * @date 2022年08月09 14:27:06
     */
    @ProcessFeignCalledResult
    @PostMapping("/getHaServiceList")
    ApiResultSet getHaServiceList(String cardNo, String name, String companyName, String companyCode);


    /**
     * 材料上传
     *
     * @param files   材料信息
     * @param request HttpServletRequest
     * @return 上传结果信息
     * @author yupeng
     * @date 2022年08月11 18:17:33
     */
    @RequestMapping(value = "/uploadCaseMaterialFile", method = {RequestMethod.POST})
    ApiResultSet<List<QlSysAtta>> uploadCaseMaterialFile(HttpServletRequest request, MultipartFile[] files);


    /**
     * 获取办件信息
     *
     * @param caseOid 办件oid
     * @return 返回办件信息
     * @author yupeng
     * @date 2022年08月11 18:26:53
     */
    @GetMapping(value = "/getCaseInfo")
    ApiResultSet getCaseInfo(@RequestParam(value = "caseOid") String caseOid);


    /**
     * 更新材料关联
     *
     * @param qlCaseMaterialList 材料信息
     * @return 返回更新材料结果
     * @author yupeng
     * @date 2022年08月11 18:31:26
     */
    @PostMapping(value = "/updateQlCaseMaterialList")
    ApiResultSet<List<Map<String, String>>> updateQlCaseMaterialListForBrowser(@RequestBody List<QlCaseMaterial> qlCaseMaterialList);


    /**
     * @description: 获取分组列表信息
     * @author: zhaobf
     * @date: 2022-08-26 10:22
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getGroupList", method = {RequestMethod.GET})
    ApiResultSet<List<HaWorkUserGroupResponseData>> getGroupList();

    /**
     * @description: 获取分组列表 将帮办类型和组别区分，用于取号页面显示
     * @author: zhaobf
     * @date: 2022-09-19 10:12
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getGroupListSub", method = {RequestMethod.GET})
    ApiResultSet<Map<String,List<Map<String,Object>>>> getGroupListSub();

    /**
     * @description: 根据身份证号获取手机号
     * @author: zhaobf
     * @date: 2022-09-19 10:12
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getPhoneByCardNo", method = {RequestMethod.GET})
    ApiResultSet<HaVisit> getPhoneByCardNo(String cardNo);

    /**
     * @description: 获取分组列表信息
     * @author: zhaobf
     * @date: 2022-08-26 10:45
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getWorkUserList", method = {RequestMethod.GET})
    ApiResultSet<List<HaWorkUserResponseData>> getWorkUserList(Long groupId);


    /**
     * 获取工作人员的平均评价得分
     *
     * @return 工作人员萍爵分
     * @author yupeng
     * @date 2022年08月15 14:03:09
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/getWorkUserEvalScore")
    ApiResultSet getWorkUserEvalScore(Long workUserId);

    /**
     * 更新修改办件材料附件-给旗舰店调用的
     *
     * @param qlCaseMaterialList 材料附件集合
     * @return 保存结果
     * @author wangyh
     * @date 2022年09月06 10:01:29
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdateCaseMaterialAttaList", method = {RequestMethod.POST})
    ApiResultSet saveOrUpdateCaseMaterialAttaList(@RequestBody List<QlCaseMaterial> qlCaseMaterialList);

    /**
     * @description: 根据办件编号查询材料信息 -给旗舰店调用的
     * @params：[ caseOid 办件编号]
     * @return: 材料list详细信息
     * @author wangyh
     * @date 2022年09月06 10:01:29
     */
    @RequestMapping(value = "/queryQlCaseMaterialListByCaseOid", method = {RequestMethod.GET})
    ApiResultSet<Map<String, List<QlCaseMaterial>>> queryQlCaseMaterialListByCaseOid(@RequestParam(value = "caseOid", required = false) String caseOid);

    /**
     * 根据事项 情形 选择获取材料和精细化材料
     *
     * @param serviceOid    所属事项主健
     * @param optionValOids 选项主键
     * @author: wangyh
     * @Date: 2022-9-06 13:40:06
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getSituationMaterialListByOids", method = {RequestMethod.GET})
    ApiResultSet<List<ServiceMaterialVo>> getSituationMaterialListByOids(@RequestParam(value = "serviceOid", required = false) String serviceOid, @RequestParam(value = "optionValOids", required = false) String optionValOids);

    /**
     * @description 单独查询事项信息
     * @param serviceOid
     * @author: wangyh
     * @Date: 2022-9-06 13:40:06
     * @return
     **/
    @RequestMapping(value = "/getSxServiceByServiceOid", method = {RequestMethod.GET})
    ApiResultSet<SxService> getSxServiceByServiceOid(@RequestParam(value = "serviceOid") String serviceOid);

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

    @RequestMapping(value = "/getQlCaseMaterialByCaseMaterialOid", method = {RequestMethod.GET})
    ApiResultSet getQlCaseMaterialByCaseMaterialOid(String caseMaterialOid);

    /**
     * 根据办件材料oid查询当前办件的材料附件情况
     * @param caseMaterialOid
     * @return
     */
    @RequestMapping(value = "/queryQlCaseMaterialAttaByCaseMaterialOid", method = {RequestMethod.GET})
    ApiResultSet<List<QlCaseMaterialAtta>> queryQlCaseMaterialAttaByCaseMaterialOid(String caseMaterialOid);

    /**
     * 调用随身办获取用户信息
     * @param param
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/scanCertQrCode")
    ApiResultSet scanCertQrCode(ScanParam param) throws Exception;


    /**
     * 获取登录地点列表
     * @return
     */
    @RequestMapping(value = "/queryLoginLocation", method = {RequestMethod.GET})
    ApiResultSet<List<HaWorkLoginLocation>> queryLoginLocation();

    /**
     * 获取登录地点列表
     * @return
     */
    @RequestMapping(value = "/saveLoingLocationName", method = {RequestMethod.GET})
    ApiResultSet saveLoingLocationName(@RequestParam(value = "id", required = true)Long id, String loginLocationName);



}
