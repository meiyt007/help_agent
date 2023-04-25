package com.zfsoft.ha.front;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 办事群众
 * @author: zhaobf
 * @Date: 2022/7/20
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/ha/masses")
public interface MassesService {
    /**
     * 获取办事群众的信息
     *
     * @param queueId
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getMassesInfo",method = RequestMethod.GET)
    ApiResultSet<Map<String, Object>> getMassesInfo(String queueId);

    /**
     * 获取办事群众的信息
     * @author: zhaobf
     * @Date: 2022/8/5 09:30
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getCompanyInfo",method = RequestMethod.GET)
    ApiResultSet getCompanyInfo(
            @RequestParam(value = "companyName", required = false) String companyName ,
            @RequestParam(value = "companyCode", required = false) String companyCode) ;
}
