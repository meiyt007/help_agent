package com.zfsoft.service.scSign.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.scSign.data.SxSign;
import com.zfsoft.service.scSign.data.SxSignDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author ChangSheng
 * @Date 14:01 2022/6/16
 * @Description 签名配置
 **/
@RequestMapping("/sxSign")
public interface SxSignService {

    /**
     * 查看列表
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/getList"},method = {RequestMethod.GET})
    ApiResultSet<List<SxSign>> getSignList(SxSign sxSign);

    /**
     * 修改太麻烦了，重新写个给综窗用吧
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/getSignListByMaterialOid"},method = {RequestMethod.GET})
    ApiResultSet<List<SxSign>> getSignListByMaterialOid(@RequestParam("materialOid") String materialOid);

    /**
     * 根据id查询签署角色数据
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/getSignById"},method = {RequestMethod.GET})
    ApiResultSet<SxSign> getSignById(@RequestParam("id") String id);

    /**
     * 新增/修改
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    ApiResultSet<SxSign> saveOrUpdateSxSign(@RequestBody SxSignDto sxSignDto);

    /**
     * 逻辑删除
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    ApiResultSet<Integer> deleteSxSignById(@RequestParam("id") String id);

}
