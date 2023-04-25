package com.zfsoft.service.sxConditionalRules.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxConditionalRules.data.SxConditionalRules;
import com.zfsoft.service.sxConditionalRules.data.SxConditionalRulesDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName SxConditionalRulesService
 * @Description 条件配置service
 * @Author WangKe
 * @Date 2022-05-24
 * @Version V1.0
 **/
@RequestMapping("/rules")
public interface SxConditionalRulesService {

    /**
     * 保存或更新
     * @param sxConditionalRulesDto
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdateSxConditionalRules", method = {RequestMethod.POST})
    ApiResultSet saveOrUpdateSxConditionalRules(@RequestBody SxConditionalRulesDto sxConditionalRulesDto);

    /**
     * 获取所有的信息列表
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryConditionalRulesList", method = {RequestMethod.POST})
    ApiResultSet<SxConditionalRulesDto> queryAllConditionalRules(@RequestBody SxConditionalRules sxConditionalRules);

    /**
     * 获取所有的信息列表
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryRulesByCodeAndServiceOid", method = {RequestMethod.POST})
    ApiResultSet<SxConditionalRules> queryRulesByCodeAndServiceOid(@RequestBody SxConditionalRules sxConditionalRules);

    /**
     * 通过 id 删除条件配置信息
     * @param id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/delete",method = {RequestMethod.GET})
    ApiResultSet delete(String id);
}
