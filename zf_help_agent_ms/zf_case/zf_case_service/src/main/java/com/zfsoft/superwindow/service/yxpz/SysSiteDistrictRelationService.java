package com.zfsoft.superwindow.service.yxpz;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.yxpz.SysSiteDistrictRelation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author: kkfan
 * @create: 2020-10-26 10:33:56
 * @description:    辖区区划管理表
 */
@RequestMapping(value = "/sysSiteDistrictRelation")
public interface SysSiteDistrictRelationService{

    @ProcessFeignCalledResult
    @RequestMapping(value = "/querySiteDistrictRelByDistOid",method = {RequestMethod.POST})
    ApiResultSet<List<SysSiteDistrictRelation>> querySiteDistrictRelByDistOid(Map<String, Object> paramRelMap);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/siteDistrictRelListByDistOid",method = {RequestMethod.POST})
    ApiResultSet<List<SysSiteDistrictRelation>> siteDistrictRelListByDistOid(@RequestParam(value = "districtOid") String districtOid);


}
