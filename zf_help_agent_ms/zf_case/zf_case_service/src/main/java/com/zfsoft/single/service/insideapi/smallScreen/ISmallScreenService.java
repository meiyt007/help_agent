package com.zfsoft.single.service.insideapi.smallScreen;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/smallScreen")
public interface ISmallScreenService {

    /**
     * 推送信息
     * @param userOid
     * @param returnFlag
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/pushCallBackInfo", method = {RequestMethod.GET})
    ApiResultSet<Boolean> pushCallBackInfo(@RequestParam(value = "userOid", required = false) String userOid, @RequestParam(value = "returnFlag", required = false) String returnFlag);

    /**
     * 获取确认点击确认按钮返回信息
     *
     * @param userOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getCallBackInfo", method = {RequestMethod.GET})
    ApiResultSet<String> getCallBackInfo(@RequestParam(value = "userOid", required = false) String userOid);

}
