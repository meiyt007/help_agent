package com.zfsoft.single.service.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.single.data.clzs.dto.HouseholdRegisterInfo;
import com.zfsoft.superwindow.data.clzs.MaterialCatalog;
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
    ApiResultSet<HouseholdRegisterInfo> getHouseholdRegister(@RequestParam(value = "picBase64", required = false) String picBase64);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/discernHouseholdRegister", method = {RequestMethod.GET})
    ApiResultSet<Map<String, Object>> discernHouseholdRegister(@RequestParam(value = "householdRegisterInfo", required = false) HouseholdRegisterInfo householdRegisterInfo,
                                                               @RequestParam(value = "cata", required = false) MaterialCatalog cata);


}
