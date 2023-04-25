package com.zfsoft.single.service.common;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @（#）: CommonService
 * @description: 通用方法接口
 * @author: wangwg
 * @date: 2020/10/31
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/common")
public interface CommonService {

    /**
     * 事项类型树
     *
     * @author wangwg
     * @date 2020-10-31
     * @param serviceType 事项类型
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/queryServiceTypeSimpleTree"}, method = {RequestMethod.GET})
    ApiResultSet queryServiceTypeSimpleTree(@RequestParam(value = "serviceType", required = false) String serviceType);


    /**
     * 获取用户
     *
     * @author wangwg
     * @date 2021-06-16
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/getGrantUserType"})
    ApiResultSet<Integer> getGrantUserType();
}
