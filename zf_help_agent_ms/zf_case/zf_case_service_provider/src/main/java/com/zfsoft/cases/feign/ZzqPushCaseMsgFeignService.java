package com.zfsoft.cases.feign;

import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author: WangKe
 * @create: 2022-06-10
 * @description: 办件信息推送
 */
@FeignClient(value ="${zfsoft.feign.middle}", contextId = "middle-case")
public interface ZzqPushCaseMsgFeignService {

    /**
     * 办件信息推送接口
     * @param paramsMap 办件信息和办件材料信息map
     * @return
     */
    @RequestMapping( value = "/web/zcPushData/pushCaseRegisterInfo", method = {RequestMethod.POST})
    ApiResultSet<String> pushCaseRegisterInfo(Map<String, String> paramsMap);

    /**
     * 通过事项实施编码获取办件编号
     * @authon WangKe 2022-06-15
     * @param serviceImplementCode 事项实施编码
     * @return
     */
    @RequestMapping( value = "/web/zcPushData/getCaseNumber", method = {RequestMethod.POST})
    ApiResultSet<String> getCaseNumByServiceImplementCode(@RequestParam("serviceCode") String serviceImplementCode);

}
