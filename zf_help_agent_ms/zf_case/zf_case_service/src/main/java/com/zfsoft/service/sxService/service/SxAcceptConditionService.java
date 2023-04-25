package com.zfsoft.service.sxService.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxService.data.SxAcceptCondition;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SxAcceptConditionService
 * @Description 实施清单受理条件信息服务定义接口
 * @Author wangxl
 * @Date 2020-10-26
 * @Version V1.0
 **/
@RequestMapping("/affair/sxAcceptCondition")
public interface SxAcceptConditionService {
    /**
     * @description:  根据业务主健获取实施清单受理详细信息
     * @param conditionOid 业务主键
     * @author: wangxl
     * @Date: 2020/10/26
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxAcceptConditionByOid/{conditionOid}",method = {RequestMethod.GET})
    ApiResultSet<SxAcceptCondition>  getSxAcceptConditionByOid(@PathVariable("conditionOid") String conditionOid);

    /**
     * @description:  根据实施清单业务主健获取施清单受理条件
     * @param serviceOid 业务主键
     * @author: wanwgwg
     * @Date: 2020/12/04
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxAcceptConditionListByServiceOid",method = {RequestMethod.GET})
    ApiResultSet<List<SxAcceptCondition>>  getSxAcceptConditionListByServiceOid(@RequestParam(value = "serviceOid", required = false) String serviceOid);

    /**
     * 分页查询受理条件信息
     * @param sxAcceptCondition
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getAcceptConditionList",method = {RequestMethod.POST})
    ApiResultSet pageList(SxAcceptCondition sxAcceptCondition,
                          @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) ;

    /**
     * 新增或修改实施清单受理条件信息
     * @param sxAcceptCondition
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveOrUpdateSxAcceptCondition",method = {RequestMethod.POST})
    ApiResultSet saveOrUpdateSxAcceptCondition(@RequestBody SxAcceptCondition sxAcceptCondition);

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
    ApiResultSet getSxAcceptConditionById(@RequestParam(value = "id", required = false) String id);

}
