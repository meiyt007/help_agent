package com.zfsoft.service.sxSituation.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxSituation.data.SxServiceOptionVal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author wangns
 * @description 选项值表
 * @date 2020/11/3 11:36
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/affair/sxServiceOptionVal")
public interface SxServiceOptionValService {

    /**
     * @description:  根据选项值业务主健获取选项值信息
     * @param oid 业务主键
     * @author: wangns
     * @Date: 2020/11/3 11:09
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServiceOptionValByOid/{oid}",method = {RequestMethod.GET})
    ApiResultSet<SxServiceOptionVal> getSxServiceOptionValByOid(@PathVariable("oid") String oid);


    /**
     * @description:  根据选项值业务主健获取选项值以及所属选项标题
     * @param oid 业务主键
     * @author: wangwg
     * @Date: 2020/12/17
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServiceOptionTitleValStr",method = {RequestMethod.GET})
    ApiResultSet<Map<String,String>> getSxServiceOptionTitleValStr(@RequestParam("oid") String oid);


}
