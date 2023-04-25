package com.zfsoft.service.sxSys.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxSys.data.SxSysDict;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName SxSysDictService
 * @Description 数据字典接口
 * @Author wangxl
 * @Date 2020-10-25
 * @Version V1.0
 **/
@RequestMapping("/sxSys/dict")
public interface SxSysDictService {

    /**
     * @description:  初始化数据字典信息
     * @param oid 实体类业务主键
     * @author: wangxl
     * @Date: 2020/10/25
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/init"},method = {RequestMethod.GET})
    ApiResultSet initSxSysDict(@RequestParam(value = "oid", required = false) String oid);

    /**
     * @description:  获取数据字典的信息
     * @param oid 实体类业务主键
     * @author: wangxl
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOne/{oid}",method = {RequestMethod.GET})
    ApiResultSet<SxSysDict>  getSxSysDictByOId(@PathVariable("oid") String oid);

}
