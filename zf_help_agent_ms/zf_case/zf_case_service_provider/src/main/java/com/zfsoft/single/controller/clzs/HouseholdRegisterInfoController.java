package com.zfsoft.single.controller.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.single.data.clzs.dto.HouseholdRegisterInfo;
import com.zfsoft.single.manager.clzs.HouseholdRegisterInfoManager;
import com.zfsoft.single.service.clzs.HouseholdRegisterInfoService;
import com.zfsoft.superwindow.data.clzs.MaterialCatalog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author: wangwg
 * @create: 2021-01-12
 * @description: 户口本信息控制层
 */
@Slf4j
@RestController
public class HouseholdRegisterInfoController implements HouseholdRegisterInfoService {

    @Resource
    private HouseholdRegisterInfoManager householdRegisterInfoManager;

    @Override
    public ApiResultSet<HouseholdRegisterInfo> getHouseholdRegister(String picBase64){
        ApiResultSet apiResultSet = new ApiResultSet<>();
        try {
            HouseholdRegisterInfo registerInfo = householdRegisterInfoManager.getHouseholdRegister(picBase64);
            apiResultSet.setData(registerInfo);
        }catch (Exception e){
            e.printStackTrace();
        }
        return apiResultSet;
    }
    @Override
    public ApiResultSet<Map<String, Object>> discernHouseholdRegister(HouseholdRegisterInfo householdRegisterInfo, MaterialCatalog cata) {
        ApiResultSet apiResultSet = new ApiResultSet<>();
        try {
            Map<String, Object> objectMap = householdRegisterInfoManager.discernHouseholdRegister(householdRegisterInfo, cata);
            apiResultSet.setData(objectMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return apiResultSet;
    }
}
