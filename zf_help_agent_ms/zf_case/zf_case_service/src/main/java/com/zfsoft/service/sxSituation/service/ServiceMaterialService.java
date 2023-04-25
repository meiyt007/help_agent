package com.zfsoft.service.sxSituation.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxSituation.data.ServiceMaterial;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangns
 * @description 事项颗粒材料
 * @date 2020/11/3 10:00
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/affair/serviceMaterial")
public interface ServiceMaterialService {

    /**
     * @description:  根据事项颗粒材料业务主健获取事项颗粒材料消息
     * @param oid 业务主键
     * @author: wangns
     * @Date: 2020/11/3 10:00
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getServiceMaterialByOid/{oid}",method = {RequestMethod.GET})
    ApiResultSet<ServiceMaterial> getServiceMaterialByOid(@PathVariable("oid") String oid);


    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryServiceMaterialWithPage",method = {RequestMethod.GET})
    ApiResultSet<PageResult<ServiceMaterial>> queryServiceMaterialWithPage(
            @RequestParam(value = "serviceOid", required = false) String serviceOid,
            @RequestParam(value = "materialName", required = false) String materialName,
            @RequestParam(value = "baiduTemplateIds", required = false) String baiduTemplateIds,
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize);



    @ProcessFeignCalledResult
    @RequestMapping( value = "/updateServiceMaterial",method = {RequestMethod.POST})
    ApiResultSet<ServiceMaterial> updateServiceMaterial(@RequestBody ServiceMaterial ServiceMaterial);


    @ProcessFeignCalledResult
    @RequestMapping( value = "/getServiceMaterialsByServiceOid",method = {RequestMethod.GET})
    ApiResultSet<List<ServiceMaterial>> getServiceMaterialsByServiceOid(@PathVariable("serviceOid") String serviceOid);



}
