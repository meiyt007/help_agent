package com.zfsoft.superwindow.controller.easyquickcase;

import com.zfsoft.cases.feign.SysDistrictFeignService;
import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.service.easyquickcase.EasyQuickCommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
public class EasyQuickCommonController implements EasyQuickCommonService {

    @Resource
    private SysDistrictFeignService sysDistrictFeginService;

    @Override
    public ApiResultSet<List<SysDistrict>> getDistListByParentOidForMobile(String parentOid) {
        return sysDistrictFeginService.querySysDistrictListByParentOid(parentOid);
    }
}
