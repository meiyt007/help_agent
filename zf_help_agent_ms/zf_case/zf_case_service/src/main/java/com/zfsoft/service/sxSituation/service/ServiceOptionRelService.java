package com.zfsoft.service.sxSituation.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxSituation.data.ServiceOptionRel;
import com.zfsoft.service.sxSituation.data.vo.ServiceOptionRelDto;
import org.springframework.web.bind.annotation.*;

/**
 * @author wangns
 * @description 大综窗选项关联表
 * @date 2020/11/3 10:44
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/affair/serviceOptionRel")
public interface ServiceOptionRelService {

    /**
     * @description:  根据大综窗选项关联业务主健获取大综窗选项关联消息
     * @param oid 业务主键
     * @author: wangns
     * @Date: 2020/11/3 10:44
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getServiceOptionRelByOid/{oid}",method = {RequestMethod.GET})
    ApiResultSet<ServiceOptionRel> getServiceOptionRelByOid(@PathVariable("oid") String oid);

    /**
     * 根据这两 oid 查询和该选项值关联的选项标题和选项值
     * @param serviceOid
     * @param optionValOid
     * @param optionTitleOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOptionRelationDataById",method = {RequestMethod.GET})
    ApiResultSet getOptionRelationDataById(@RequestParam(value = "serviceOid", required = false) String serviceOid,
                                           @RequestParam(value = "optionValOid", required = false) String optionValOid,
                                           @RequestParam(value = "optionTitleOid", required = false) String optionTitleOid);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/relationList", method = {RequestMethod.POST})
    ApiResultSet<PageResult<ServiceOptionRelDto>> relationList(
            @RequestParam(value = "serviceOid", required = false) String serviceOid,
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdateServiceOptionRel", method = {RequestMethod.POST})
    ApiResultSet<String> saveOrUpdateServiceOptionRel(@RequestBody ServiceOptionRel serviceOptionRel);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/delBatchRel", method = {RequestMethod.GET})
    ApiResultSet<String> delBatchRel(@RequestParam(value = "relOids") String relOids);




}
