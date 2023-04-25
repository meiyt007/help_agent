package com.zfsoft.service.sxSituation.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxSituation.data.SxServiceSituation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangns
 * @description 事项情形表
 * @date 2020/11/3 11:53
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/affair/sxServiceSituation")
public interface SxServiceSituationService {

    /**
     * @description:  根据事项情形业务主健获取事项情形信息
     * @param oid 业务主键
     * @author: wangns
     * @Date: 2020/11/3 11:09
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServiceSituationByOid/{oid}",method = {RequestMethod.GET})
    ApiResultSet<SxServiceSituation> getSxServiceSituationByOid(@PathVariable("oid") String oid);


    /**
     * 根据serviceOid获取热门情形配置(智能登记)
     *
     * @author: wangwg
     * @Date: 2021/07/13
     * @param serviceOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getSxServiceHotSituations", method = {RequestMethod.GET})
    ApiResultSet<List<SxServiceSituation>> getSxServiceHotSituations(@RequestParam(value = "serviceOid", required = false) String serviceOid);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryxHotSituationsPag", method = {RequestMethod.POST})
    ApiResultSet<PageResult<SxServiceSituation>> queryxHotSituationsPag(@RequestParam(value = "situationName", required = false) String situationName, @RequestParam(value = "serviceOid", required = false) String serviceOid,
                                                                        @RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * 保存情形
     * @param ServiceMaterial
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveSxServiceSituation",method = {RequestMethod.POST})
    ApiResultSet saveSxServiceSituation(@RequestBody SxServiceSituation ServiceMaterial);

    /**
     *  删除情形
     * @param oid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/delSxServiceSituationByOid/{oid}", method = {RequestMethod.GET})
    ApiResultSet delSxServiceSituationByOid(@PathVariable("oid") String oid);

    /**
     * @description 根据事项主键查询情形列表
     * @param serviceOid
     * @author meiyt
     * @date 2022/5/16
     * @return
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getSituationOptionListByServiceOid", method = {RequestMethod.GET})
    ApiResultSet getSituationOptionListByServiceOid(@RequestParam(value = "serviceOid", required = false) String serviceOid);
}
