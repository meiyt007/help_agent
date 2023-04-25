package com.zfsoft.service.sxService.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.sxService.data.SxServicePrecheck;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * 前置事项核验信息接口
 * @author chenyq
 * @date 20220324
 */
@RequestMapping(value="/affair/sxServicePrecheck")
public interface SxServicePrecheckService {
    /**
     * 查询列表
     */

    @RequestMapping(value="querySxServicePrecheckList",method = RequestMethod.POST)
    ApiResultSet querySxServicePrecheckList(@RequestBody SxServicePrecheck sxServicePrecheck);
    /**
     * 查看详情
     */
    @RequestMapping(value="getSxServicePrecheck",method = RequestMethod.GET)
    ApiResultSet getSxServicePrecheck(@RequestParam(value = "precheckOid", required = false) String precheckOid);
    /**
     * 保存或者更新
     */
    @RequestMapping(value="saveOrUpdate",method = RequestMethod.POST)
    ApiResultSet saveOrUpdate(@RequestBody SxServicePrecheck sxServicePrecheck);

    /**
     * 保存或者更新集合
     */
    @RequestMapping(value="saveOrUpdateList",method = RequestMethod.POST)
    ApiResultSet saveOrUpdateList(@RequestBody List<SxServicePrecheck> sxServicePrechecks);
    /**
     * 删除
     */
    @RequestMapping(value="delete",method = RequestMethod.GET)

    ApiResultSet delete(@RequestParam(value = "precheckOid", required = false) String precheckOid);
}
