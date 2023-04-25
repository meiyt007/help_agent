package com.zfsoft.superwindow.service.easyquickcase;

import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @description 移动端接口服务
 * @author meiyt
 * @date 2022/6/15
 **/
@RequestMapping("/mobile")
public interface MobileService {

    /**
     * @description 移动端获取区划信息
     * @param
     * @author meiyt
     * @date 2022/6/15
     * @return
     **/
    @RequestMapping(value = "/getDistListByParentOid", method = RequestMethod.GET)
    ApiResultSet<List<SysDistrict>> getDistListByParentOidForMobile(String parentOid);


    /**
     * @description 用户登录
     * @param code
     * @author wangyg
     * @date 2022/6/21
     * @return
     */
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    ApiResultSet<String> login(@RequestParam(value = "code") String code);
}
