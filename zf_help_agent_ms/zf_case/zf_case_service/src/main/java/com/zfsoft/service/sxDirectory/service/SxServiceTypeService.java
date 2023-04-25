package com.zfsoft.service.sxDirectory.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxDirectory.data.SxServiceType;
import com.zfsoft.service.sxService.data.vo.ServiceTypeNum;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName SxServiceTypeService
 * @Description 事项类型服务定义接口
 * @Author wangxl
 * @Date 2020-10-25
 * @Version V1.0
 **/
@RequestMapping("/affair/sxServiceType")
public interface SxServiceTypeService {

    /**
     * @description:  初始化事项类型的信息
     * @param serviceTypeOid 业务主键
     * @author: wangxl
     * @Date: 2020/10/25
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/init"},method = {RequestMethod.GET})
    ApiResultSet initSxServiceType(@RequestParam(value = "serviceTypeOid", required = false) String serviceTypeOid);

    /**
     * @description:  获取事项类型的信息
     * @param serviceTypeOid 业务主键
     * @author: wangxl
     * @Date: 2020/10/25
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServiceTypeByOid/{serviceTypeOid}",method = {RequestMethod.GET})
    ApiResultSet<SxServiceType>  getSxServiceTypeByOid(@PathVariable("serviceTypeOid") String serviceTypeOid);

    /**
     * 查询事项类型lsit
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getDbSxServiceTypeList",method = {RequestMethod.GET})
    ApiResultSet<List<SxServiceType>> getDbSxServiceTypeList();

    /**
     * @description:  查询事项类型分页信息列表
     * @param serviceTypeOid 事项业务主健
     * @param serviceTypeName 事项类型名称
     * @param serviceTypeCode 事项编码
     * @author: wangxl
     * @Date: 2020/10/26
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/sxServicePage",method = {RequestMethod.GET})
    ApiResultSet<PageResult<SxServiceType>> querySxServiceTypeWithPage(
            @RequestParam(value = "serviceTypeOid", required = false) String serviceTypeOid,
            @RequestParam(value = "serviceTypeName", required = false) String serviceTypeName,
            @RequestParam(value = "serviceTypeCode", required = false) String serviceTypeCode,
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize);


    /**
     * @description:  获取事项类型的发布事项数量
     * @param serviceStatus 事项状态
     * @author: wangwg
     * @Date: 2021/8/26
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getServiceTypeAndNumber",method = {RequestMethod.GET})
    ApiResultSet<List<ServiceTypeNum>> getServiceTypeAndNumber(
            @RequestParam(value = "serviceStatus", required = false) Integer serviceStatus);
}
