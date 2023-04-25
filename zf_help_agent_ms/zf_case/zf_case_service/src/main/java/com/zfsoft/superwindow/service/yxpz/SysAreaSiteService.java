package com.zfsoft.superwindow.service.yxpz;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.yxpz.SysAreaSite;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: kkfan
 * @create: 2020-10-22 10:39:38
 * @description: 受理下辖区管理服务层
 */
@RequestMapping(value = "/sysAreaSite")
public interface SysAreaSiteService {

    /**
     * 查询受理辖区列表
     * @param siteName
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/pageList")
    ApiResultSet queryPageList(String siteName,
                               @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum
            , @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize);

    /**
     * 保存/更新受理辖区
     * @param sysAreaSite
     * @return
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/saveOrUpdate")
    ApiResultSet saveOrUpdate(@RequestBody @Validated SysAreaSite sysAreaSite);

    /**
     * 删除受理辖区  支持批量删除  id间,隔开
      * @param ids
     * @return
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/delete")
    ApiResultSet delete(String ids);

    /**
     * 获取站点详情
     * @param id
     * @return
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/getOne")
    ApiResultSet getOne(String id);

    /**
     * 启禁用
     * @param id
     * @return
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/able")
    ApiResultSet ableSite(String id);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryList", method = {RequestMethod.GET})
    ApiResultSet<List<SysAreaSite>> queryList();
}
