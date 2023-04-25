package com.zfsoft.superwindow.service.easyquickcase;

import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @description 四办公共接口服务
 * @author meiyt
 * @date 2022/6/15
 **/
@RequestMapping("/easyQuickCommon")
public interface EasyQuickCommonService {

    /**
     * @description 移动端获取区划信息
     * @param
     * @author meiyt
     * @date 2022/6/15
     * @return
     **/
    @RequestMapping(value = "/getDistListByParentOid", method = RequestMethod.GET)
    ApiResultSet<List<SysDistrict>> getDistListByParentOidForMobile(String parentOid);
}
