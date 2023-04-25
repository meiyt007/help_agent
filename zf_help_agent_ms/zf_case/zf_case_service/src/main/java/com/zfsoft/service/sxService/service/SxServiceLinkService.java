package com.zfsoft.service.sxService.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxService.data.SxServiceLink;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SxServiceLinkService
 * @Description 实施清单环节信息服务定义接口
 * @Author wangxl
 * @Date 2020-10-26
 * @Version V1.0
 **/
@RequestMapping("/affair/sxServiceLink")
public interface SxServiceLinkService {
    /**
     * @description:  根据业务主健获取实施清单环节信息
     * @param serviceLinkOid 业务主键
     * @author: wangxl
     * @Date: 2020/10/26
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServiceLinkByOid/{serviceLinkOid}",method = {RequestMethod.GET})
    ApiResultSet<SxServiceLink>  getSxServiceLinkByOid(@PathVariable("serviceLinkOid") String serviceLinkOid);

    /**
     * @description:  根据实施清单业务主健获取施清单环节信息
     * @param serviceOid 业务主键
     * @author: wanwgwg
     * @Date: 2020/12/04
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServiceLinkByOid",method = {RequestMethod.GET})
    ApiResultSet<List<SxServiceLink>>  getSxServiceLinkListByServiceOid(@RequestParam(value = "serviceOid", required = false) String serviceOid);

    /**
     * 分页查询办理环节信息
     * @param sxServiceLink
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getLinkList",method = {RequestMethod.POST})
    ApiResultSet pageList(SxServiceLink sxServiceLink,
                          @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNumber,
                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize);

    /**
     * 新增或修改实施清单常见问题信息
     * @param sxServiceLink
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveOrUpdateSxServiceLink",method = {RequestMethod.POST})
    ApiResultSet saveOrUpdateSxServiceLink(@RequestBody SxServiceLink sxServiceLink);

    /**
     * 删除操作
     * @param id
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/delete",method = {RequestMethod.GET})
    ApiResultSet delete(@RequestParam(value = "id", required = false) String id);

    /**
     * 查询详细信息
     * @param id
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/detail",method = {RequestMethod.GET})
    ApiResultSet getSxServiceQuestionById(@RequestParam(value = "id", required = false) String id);

}
