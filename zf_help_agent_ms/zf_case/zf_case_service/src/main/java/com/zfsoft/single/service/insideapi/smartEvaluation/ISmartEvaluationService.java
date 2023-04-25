package com.zfsoft.single.service.insideapi.smartEvaluation;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @（#）: ISmartEvaluationService
 * @description: 智能评价系统接口
 * @author: wangwg
 * @date: 2020/12/09
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/martService")
public interface ISmartEvaluationService {

    /**
     * 查询所有用户信息
     *
     * @return
     * @author wangwg
     * @date 2020-12-08
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryUserListByOrganOid", method = {RequestMethod.GET})
    String queryUserListByOrganOid(@RequestParam(value = "organOid", required = false) String organOid);


    /**
     * 通过窗口号码
     *
     * @return
     * @author wangwg
     * @date 2020-12-14
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryUseByWindowsNo", method = {RequestMethod.GET})
    String queryUseByWindowsNo(@RequestParam(value = "windoNo", required = false) String windoNo);

    /**
     * 智能评价信息确认点击确认按钮返回信息
     *
     * @param userOid
     * @return
     * @author wangwg
     * @date 2020-12-14
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getCallBackInfo", method = {RequestMethod.GET})
    String getCallBackInfo(@RequestParam(value = "userOid", required = false) String userOid, @RequestParam(value = "status", required = false) String status);


    /**
     * 获取智能评价信息确认返回信息
     *
     * @param userOid
     * @return
     * @author wangwg
     * @date 2020-12-15
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryConfirmFlag", method = {RequestMethod.GET})
    ApiResultSet<String> queryConfirmFlag(@RequestParam(value = "userOid", required = false) String userOid);


    /**
     * 评价评价启动类型
     *
     * @return
     * @author wangwg
     * @date 2020-12-14
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getEvaluationType", method = {RequestMethod.GET})
    ApiResultSet<String> getEvaluationType();


    /**
     * 判断办件操作用户是否上授权到窗口
     *
     * @param userOid
     * @return
     * @author wangwg
     * @date 2020-12-15
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/grantWindowUser", method = {RequestMethod.GET})
    ApiResultSet<Boolean> grantWindowUser(@RequestParam(value = "userOid", required = false) String userOid);



    /**
     * 发送办件基本信息到智能评价系统
     *
     * @param object
     * @return
     * @author wangwg
     * @date 2020-12-15
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/sendServiceBaseInfo", method = {RequestMethod.POST})
    ApiResultSet<String> sendServiceBaseInfo(@RequestBody Object object);


    /**
     * 发送办件确认信息到智能评价系统
     *
     * @param object
     * @return
     * @author wangwg
     * @date 2020-12-15
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/sendConfirmCaseInfo", method = {RequestMethod.POST})
    ApiResultSet<String> sendConfirmCaseInfo(@RequestBody Object object);

    /**
     * 办件评价到智能评价系统
     *
     * @param caseOid
     * @return
     * @author wangwg
     * @date 2020-12-15
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/sendSmartPj", method = {RequestMethod.GET})
    ApiResultSet<String> sendSmartPj(@RequestParam(value = "caseOid", required = false) String caseOid);

}
