package com.zfsoft.service.sxService.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxService.data.SxServiceLocation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @（#）: SxServiceLocationService
 * @description: 实施清单办理地点服务定义接口
 * @author: wangwg
 * @date: 2020/12/04
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/affair/sxServiceLocation")
public interface SxServiceLocationService {

    /**
     * @description:  根据实施清单扩展信息业务主健获取实施清单办理地点信息
     * @param extendOid 业务主键
     * @author: wangwg
     * @Date: 2020/12/04
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServicLocationByExtendOid",method = {RequestMethod.GET})
    ApiResultSet<List<SxServiceLocation>> getSxServicLocationByExtendOid(@RequestParam(value = "extendOid", required = false) String extendOid);

    /**
     * 新增或修改实施清单办理地点信息
     * @param sxServiceLocation
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveOrUpdateSxServiceLocation",method = {RequestMethod.POST})
    ApiResultSet saveOrUpdateSxServiceLocation(SxServiceLocation sxServiceLocation);

    /**
     * 分页查询办理地点信息
     * @param sxServiceLocation
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getLocationList",method = {RequestMethod.POST})
    ApiResultSet pageList(SxServiceLocation sxServiceLocation,
                          @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize);

    /**
     * 删除操作
     * @param ids
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/delete",method = {RequestMethod.GET})
    ApiResultSet delete(@RequestParam(value = "id", required = false) String ids);

    /**
     * 查询详细信息
     * @param id
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/detail",method = {RequestMethod.GET})
    ApiResultSet getSxServiceLocationById(@RequestParam(value = "id", required = false) String id);

    /**
     * 获取事项办理地点
     * @param serviceOid
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getLocationInfoByServiceOid",method = {RequestMethod.GET})
    ApiResultSet getLocationInfoByServiceOid(@RequestParam(value = "serviceOid", required = false) String serviceOid);

    /**
     * 获取事项办理地点
     * @param serviceOid
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getLocationInfo",method = {RequestMethod.GET})
    ApiResultSet getLocationInfo(@RequestParam(value = "serviceOid") String serviceOid,
                                 @RequestParam(value = "address") String address);


}
