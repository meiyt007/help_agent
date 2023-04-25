package com.zfsoft.cases.service;

import com.zfsoft.cases.data.QlCaseApplay;
import com.zfsoft.ha.data.responseData.HaQlCaseAppayResponseData;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @（#）: QlCaseApplayService
 * @description: 办件申请信息服务定义接口
 * @author: wangwg
 * @date: 2020/10/23
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/qlCaseApplayService")
public interface QlCaseApplayService {

    /**
     * 保存办件申请信息
     *
     * @author wangwg
     * @date 2020/10/24
     * @param qlCaseApplay
     * @return com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.data.QlCaseApplay>
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveQlCaseApplay",method = {RequestMethod.POST})
    ApiResultSet<QlCaseApplay> saveQlCaseApplay(@RequestBody QlCaseApplay qlCaseApplay);


    /**
     *根据办件业务主键查询办件申请信息
     *
     * @author wangwg
     * @date 2020/10/26
     * @param caseOid
     * @return com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.data.QlCaseApplay>
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryQlCaseApplayByCaseOid", method = {RequestMethod.GET})
    ApiResultSet<QlCaseApplay> queryQlCaseApplayByCaseOid(@RequestParam("caseOid") String caseOid);

    /**
     * @description 网站端结果送达
     * @param qlCaseApplay
     * @author meiyt 
     * @date 2022/6/8
     * @return 
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/savePostResultInfoForWeb",method = {RequestMethod.POST})
    ApiResultSet<QlCaseApplay> savePostResultInfoForBrowers(@RequestBody QlCaseApplay qlCaseApplay);

}
