package com.zfsoft.service.sxService.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxService.data.SxServiceCharge;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @（#）: SxServiceChargeService
 * @description: 事项收费信息
 * @author: wangwg
 * @date: 2021/06/10
 * @version: 1.1
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/affair/sxServiceCharge")
public interface SxServiceChargeService {

    /**
     * @description:  根据事项主健获取收费信息
     * @param serviceOid 事项主健
     * @author: wangwg
     * @Date: 2021/06/10
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/querSxServiceChargeListByServiceOid/{serviceOid}",method = {RequestMethod.GET})
    ApiResultSet<List<SxServiceCharge>> querSxServiceChargeListByServiceOid(@PathVariable("serviceOid") String serviceOid);
}
