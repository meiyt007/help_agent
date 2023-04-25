package com.zfsoft.single.service.insideapi.pbpj;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @（#）: IPbpjService
 * @description: 平板评价接口
 * @author: wangwg
 * @date: 2020/12/02
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/pbpjService")
public interface IPbpjService {

    /**
     * 登录
     *
     * @param code    用户名
     * @param pass    密码
     * @param runCode 设备编号
     * @param name    设备名称
     * @param iP      设备ip
     * @return
     * @author wangwg
     * @date 2020-12-02
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/login", method = {RequestMethod.GET})
    String login(@RequestParam(value = "code", required = false) String code,
                 @RequestParam(value = "pass", required = false) String pass,
                 @RequestParam(value = "runCode", required = false) String runCode,
                 @RequestParam(value = "name", required = false) String name,
                 @RequestParam(value = "iP", required = false) String iP);

    /**
     * 获取评价语音文件
     *
     * @return
     * @author wangwg
     * @date 2020-12-02
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getPbSysList", method = {RequestMethod.GET})
    String getPbSysList();


    /**
     * 获取评价项
     *
     * @return
     * @author wangwg
     * @date 2020-12-02
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getPjType", method = {RequestMethod.GET})
    String getPjType();


    /**
     * 根据主键查找人员信息
     *
     * @param userOid 人员id
     * @return
     * @author wangwg
     * @date 2020-12-02
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getPtUserById", method = {RequestMethod.GET})
    String getPtUserById(@RequestParam(value = "userOid", required = false) String userOid);


    /**
     * 获取事项基本信息
     *
     * @param serviceoid 事项主键
     * @param code 人员id
     * @return
     * @author wangwg
     * @date 2020-12-02
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getPtService", method = {RequestMethod.GET})
    String getPtService(@RequestParam(value = "serviceoid", required = false) String serviceoid,
                        @RequestParam(value = "code", required = false) String code);

    /**
     * 根据案件id查询案件
     *
     * @param number
     * @return
     * @author wangwg
     * @date 2020-12-02
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getCaseByCaseNumber", method = {RequestMethod.GET})
    String getCaseByCaseNumber(@RequestParam(value = "number", required = false) String number);


    /**
     * 判断软件版本是否为最新版
     *
     * @param code 软件版本号
     * @return
     * @author wangwg
     * @date 2020-12-02
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getSVByCode", method = {RequestMethod.GET})
    String getSVByCode(@RequestParam(value = "code", required = false) Integer code);

    /**
     *
     *
     * @param userOid  用户主键
     * @param pass    密码
     * @param runCode 设备编号
     * @return
     * @author wangwg
     * @date 2020-12-02
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/loginOut", method = {RequestMethod.GET})
    String loginOut(@RequestParam(value = "userOid", required = false) String userOid,
                    @RequestParam(value = "pass", required = false) String pass,
                    @RequestParam(value = "runCode", required = false) String runCode);
    /**
     * 实施清单集合信息
     *
     * @param userOid
     * @return
     * @author wangwg
     * @date 2020-12-02
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getServiceList", method = {RequestMethod.GET})
    String getServiceList(@RequestParam(value = "userOid", required = false) String userOid);

    /**
     * 获取平板展示信息
     *
     * @param distOid
     * @return
     * @author wangwg
     * @date 2020-12-02
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getPbInformationList", method = {RequestMethod.GET})
    String getPbInformationList(@RequestParam(value = "distOid", required = false) String distOid);


    /**
     * 获取最新apk 版本信息
     *
     * @param apkType
     * @return
     * @author wangwg
     * @date 2020-12-02
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getApkVersion", method = {RequestMethod.GET})
    String getApkVersion(@RequestParam(value = "apkType", required = false) String apkType);

    /**
     * 办事规定事项基本信息
     *
     * @param serviceOid
     * @return
     * @author wangwg
     * @date 2020-12-02
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getServiceBaseInfo", method = {RequestMethod.GET})
    String getServiceBaseInfo(@RequestParam(value = "serviceOid", required = false) String serviceOid);


    /**
     * 检查平板评价人员是否启用
     *
     * @param
     * @return
     * @author wangwg
     * @date 2020-12-02
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/checkUserLogin", method = {RequestMethod.GET})
    ApiResultSet<Boolean> checkUserLogin();

    /**
     * 信息推送平板评价确认信息
     *
     * @param object
     * @return
     * @author wangwg
     * @date 2020-12-02
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/pushPbpjConfirmInfo", method = {RequestMethod.POST})
    ApiResultSet<Boolean> pushPbpjConfirmInfo(@RequestBody Object object);

    /**
     * 平板评价信息确认点击确认按钮返回信息confirmFlag
     *
     * @param userOid
     * @return
     * @author wangwg
     * @date 2020-12-02
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/pushCallBackInfo", method = {RequestMethod.GET})
    ApiResultSet<Boolean> pushCallBackInfo(@RequestParam(value = "userOid", required = false) String userOid, @RequestParam(value = "returnFlag", required = false) String returnFlag);

    /**
     * 获取平板评价信息确认点击确认按钮返回信息
     *
     * @param userOid
     * @return
     * @author wangwg
     * @date 2020-12-02
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getCallBackInfo", method = {RequestMethod.GET})
    ApiResultSet<String> getCallBackInfo(@RequestParam(value = "userOid", required = false) String userOid);

    /**
     * 获取平板评价信息
     *
     * @param caseNumber
     * @return
     * @author wangwg
     * @date 2020-12-02
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/pushPbpjInfo", method = {RequestMethod.GET})
    ApiResultSet<Boolean> pushPbpjInfo(@RequestParam(value = "caseNumber", required = false) String caseNumber);

    /**
     * 平板评价保存办件信息
     *
     * @param caseOid
     * @return
     * @author wangwg
     * @date 2020-12-08
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/pbpjSaveQlCase", method = {RequestMethod.GET})
    ApiResultSet<String> pbpjSaveQlCase(@RequestParam(value = "caseOid", required = false) String caseOid);




    /**
     * 平板评价推送回调地址
     *
     * @return
     * @author wangwg
     * @date 2020-12-08
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/qlCasePbpjCallBack", method = {RequestMethod.GET})
    ApiResultSet<String> qlCasePbpjCallBack();


    /**
     * 根据用户主键返回用户头像
     *
     * @return
     * @author wangwg
     * @date 2020-12-08
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getLoginUserImgUrl", method = {RequestMethod.GET})
    String getLoginUserImgUrl(@RequestParam(value = "userOid", required = false) String userOid);

}
