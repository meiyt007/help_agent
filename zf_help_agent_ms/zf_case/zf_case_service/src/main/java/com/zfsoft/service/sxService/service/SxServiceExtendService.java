package com.zfsoft.service.sxService.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxService.data.SxServiceExtend;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName SxServiceExtendService
 * @Description 实施清单扩展服务定义接口
 * @Author wangxl
 * @Date 2020-11-14
 * @Version V1.0
 **/
@RequestMapping("/affair/sxServiceExtend")
public interface SxServiceExtendService {
    /**
     * @description:  获取实施清单的信息
     * @param serviceOid 业务主键
     * @author: wangxl
     * @Date: 2020/11/14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServiceExtendBySericeOid/{serviceOid}",method = {RequestMethod.GET})
    ApiResultSet<SxServiceExtend>  getSxServiceExtendByServiceOid(@PathVariable("serviceOid") String serviceOid);

    /**
     * 根据业务主健获取对象
     * @param oid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServiceExtendByOid/{oid}",method = {RequestMethod.GET})
    ApiResultSet<SxServiceExtend>  getSxServiceExtendByOid(@PathVariable("oid") String oid);

    /***
    * @Description:  根据结果oid查询
    * @Author:liangss
    * @Date:2021/9/15
    * @Param: [resultOid]
    */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServiceExtendByResultOid/{resultOid}",method = {RequestMethod.GET})
    ApiResultSet<SxServiceExtend>  getSxServiceExtendByResultOid(@PathVariable("resultOid") String resultOid);



}
