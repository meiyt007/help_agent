package com.zfsoft.service.sxService.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.service.sxService.data.RepeatFieldConfig;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @ClassName RepeatFieldConfigService
 * @Author xiayj
 * @Date 2021/7/19 18:02
 **/
@RequestMapping("/service/repeatField")
public interface RepeatFieldConfigService {
    /**
     * @description:  查询字段重复配置的信息列表
     * @author: xiayj
     * @Date: 2021/7/19 16:14
     **/
    @RequestMapping( value = "/page",method = {RequestMethod.GET})
    ApiResultSet<PageResult<RepeatFieldConfig>> queryRepeatFieldWithPage(
            @RequestParam(value = "isAble", required = false) Integer isAble,
            @RequestParam(value = "masterField", required = false) String masterField,
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     *
     * @param configOid
     * @description: 获取重复字段配置详细信息
     * @return: com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.data.repeatField.RepeatField>
     * @author: xiayj
     * @date: 2021/7/20
     */
    @RequestMapping(value = {"/getRepeatFieldConfig/{configOid}"},method = {RequestMethod.GET})
    ApiResultSet<RepeatFieldConfig> getRepeatFieldConfig(@PathVariable("configOid") String configOid);


    /**
     * @param configOid
     * @description: 删除重复字段配置信息
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.lang.Integer>
     * @author: xiayj
     * @date: 2021/7/20
     */
    @RequestMapping( value = "/delete/{configOid}",method = {RequestMethod.POST})
    ApiResultSet<String>  deleteRepeatField(@PathVariable("configOid") String configOid);


    /**
     * @param configOid
     * @description: 启用/禁用重复字段配置。
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.lang.Integer>
     * @author: xiayj
     * @date: 2021/7/20
     */
    @RequestMapping( value = "/able/{configOid}",method = {RequestMethod.POST})
    ApiResultSet<String> ableRepeatFieldByOid(@PathVariable("configOid") String configOid);

    /**
     * @param repeatFieldConfig
     * @description: 保存重复字段配置
     * @return: com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.data.repeatField.RepeatField>
     * @author: xiayj
     * @date: 2021/7/20
     */
    @RequestMapping( value = "/save",method = {RequestMethod.POST})
    ApiResultSet<String> saveRepeatFieldConfig(@RequestBody RepeatFieldConfig repeatFieldConfig);

    @RequestMapping( value = "/queryConfigMapInfo",method = {RequestMethod.GET})
    ApiResultSet<Map<String,String>> queryConfigMapInfo();



}
