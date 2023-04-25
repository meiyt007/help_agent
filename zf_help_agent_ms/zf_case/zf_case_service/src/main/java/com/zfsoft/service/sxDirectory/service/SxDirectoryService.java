package com.zfsoft.service.sxDirectory.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxDirectory.data.SxDirectory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName SxDirectoryService
 * @Description 目录清单服务定义接口
 * @Author wangxl
 * @Date 2020-10-25
 * @Version V1.0
 **/
@RequestMapping("/affair/sxDirectory")
public interface SxDirectoryService {

    /**
     * @description:  初始化目录清单的信息
     * @param directoryOid 业务主键
     * @author: wangxl
     * @Date: 2020/10/25
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/init"},method = {RequestMethod.GET})
    ApiResultSet initSxDirectory(@RequestParam(value = "directoryOid", required = false) String directoryOid);

    /**
     * @description:  获取目录清单的信息
     * @param directoryOid 业务主键
     * @author: wangxl
     * @Date: 2020/10/25
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxDirectoryByOid/{directoryOid}",method = {RequestMethod.GET})
    ApiResultSet<SxDirectory>  getSxDirectoryByOid(@PathVariable("directoryOid") String directoryOid);

    /**
     * 根据业务主健获取对象json
     * @param directoryOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxDirectoryJsonByOid/{directoryOid}",method = {RequestMethod.GET})
    ApiResultSet<String>  getSxDirectoryJsonByOid(@PathVariable("directoryOid") String directoryOid);

    /**
     *目录清单分页列表
     * @param directoryName
     * @param basicCode
     * @param serviceTypeOid
     * @param directoryStatus
     * @param levelDicts
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "",method = {RequestMethod.GET})
    ApiResultSet<PageResult<SxDirectory>> querySxDirectoryWithPage(
            @RequestParam(value = "directoryName", required = false) String directoryName,
            @RequestParam(value = "basicCode", required = false) String basicCode,
            @RequestParam(value = "serviceTypeOid", required = false) String serviceTypeOid,
            @RequestParam(value = "directoryStatus", required = false) Integer directoryStatus,
            @RequestParam(value = "levelDicts", required = false) String levelDicts,
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize);
}
