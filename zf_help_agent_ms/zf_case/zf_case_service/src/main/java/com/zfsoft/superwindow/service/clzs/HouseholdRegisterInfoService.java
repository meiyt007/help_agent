package com.zfsoft.superwindow.service.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.clzs.MaterialCatalog;
import com.zfsoft.superwindow.data.clzs.dto.HouseholdRegisterInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @description: 户口本信息接口
 * @author: wangwg
 * @create: 2021-01-12
 */
@RequestMapping(value = "/businessLice")
public interface HouseholdRegisterInfoService {

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getHouseholdRegister", method = {RequestMethod.GET})
    ApiResultSet<HouseholdRegisterInfo> getHouseholdRegister(@RequestParam(value = "picBase64", required = false) String picBase64) throws Exception;

    @ProcessFeignCalledResult
    @RequestMapping(value = "/discernHouseholdRegister", method = {RequestMethod.GET})
    ApiResultSet<Map<String, Object>> discernHouseholdRegister(@RequestParam(value = "householdRegisterInfo", required = false) HouseholdRegisterInfo householdRegisterInfo,
                                                               @RequestParam(value = "cata", required = false) MaterialCatalog cata) throws Exception;


}
