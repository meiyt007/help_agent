package com.zfsoft.service.sxService.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxService.data.SxOptionFieldVal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@RequestMapping("/matter/sxOptionFieldVal")
public interface SxOptionFieldValService {

    /**
     * @description 初始化选项字段值填充
     * @param serviceOid
     * @param oid
     * @author meiyt 
     * @date 2022/6/20
     * @return 
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/initOptionFieldValInfo",method = {RequestMethod.GET})
    ApiResultSet<Map<String, Object>> initOptionFieldValInfo(
            @RequestParam(value = "serviceOid", required = false) String serviceOid,
            @RequestParam(value = "oid", required = false) String oid);

    /**
     * @description 根据事项查询字段填充列表
     * @param serviceOid
     * @author meiyt
     * @date 2022/6/20
     * @return
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getFieldFillValList",method = {RequestMethod.GET})
    ApiResultSet getFieldFillValList(@RequestParam(value = "serviceOid", required = false) String serviceOid);

    /**
     * @description 保存选项值填充信息
     * @param sxOptionFieldVal
     * @author meiyt
     * @date 2022/6/21
     * @return
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveSxOptionFieldValInfo",method = {RequestMethod.POST})
    ApiResultSet<SxOptionFieldVal> saveSxOptionFieldValInfo(@RequestBody SxOptionFieldVal sxOptionFieldVal);

    /**
     * @description 根据主键删除
     * @param oid
     * @author meiyt
     * @date 2022/6/21
     * @return
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/delSxOptionFieldVal",method = {RequestMethod.GET})
    ApiResultSet delSxOptionFieldVal(@RequestParam(value = "oid", required = false) String oid);
}
