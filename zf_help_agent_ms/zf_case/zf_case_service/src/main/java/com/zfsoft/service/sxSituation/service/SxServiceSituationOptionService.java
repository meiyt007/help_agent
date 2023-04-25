package com.zfsoft.service.sxSituation.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxSituation.data.SxServiceSituationOption;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author wangns
 * @description 情形、选项关系表
 * @date 2020/11/3 13:24
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/affair/sxServiceSituationOption")
public interface SxServiceSituationOptionService {

    /**
     * @description:  根据情形、选项关系业务主健获取情形、选项关系信息
     * @param situationOid 业务主键
     * @author: wangns
     * @Date: 2020/11/3 13:24
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServiceSituationOptionBySituationOid/{situationOid}",method = {RequestMethod.GET})
    ApiResultSet getSxServiceSituationOptionBySituationOid(@PathVariable("situationOid") String situationOid);


    /**
     * 根据 情形业务主键 获取情形下的 所有选项和选项值
     * @param situationOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getOptionTitleAndValsOfSituation",method = {RequestMethod.GET})
    ApiResultSet<Map<String, Object>> getOptionTitleAndValsOfSituation(
            @RequestParam(value = "situationOid", required = false) String situationOid);

    /**
     * 保存情形关联选项关系
     * @param sxServiceSituationOptions
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveSxServiceSituations",method = {RequestMethod.POST})
    ApiResultSet saveSxServiceSituations(@RequestBody List<SxServiceSituationOption> sxServiceSituationOptions);



}
