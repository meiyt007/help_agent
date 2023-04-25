package com.zfsoft.superwindow.controller.yxpz;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.yxpz.SysAreaSite;
import com.zfsoft.superwindow.manager.yxpz.SysAreaSiteManager;
import com.zfsoft.superwindow.service.yxpz.SysAreaSiteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: kkfan
 * @create: 2020-10-22 09:22:56
 * @description: 受理辖区管理控制层
 */
@Slf4j
@RestController

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysAreaSiteController implements SysAreaSiteService {

    private final SysAreaSiteManager sysAreaSiteManager;


    @Override
    public ApiResultSet queryPageList(String siteName,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        /*List<SysAreaSite> sysAreaSiteList = this.sysAreaSiteManager.queryList(siteName);
        PageResult<SysAreaSite> pageResult = new PageResult<>(((Page) sysAreaSiteList).getPageNum(), ((Page) sysAreaSiteList).getPageSize(), ((Page) sysAreaSiteList).getTotal());
        pageResult.setData(sysAreaSiteList);*/
        PageResult<SysAreaSite> pageResult = this.sysAreaSiteManager.querySysAreaSiteWithPage(siteName,pageNum,pageSize);
                log.info("获取受理辖区列表调用成功结果为：{}", JSON.toJSONString(pageResult));
        return new ApiResultSet(pageResult);
    }


    @Override
    public ApiResultSet saveOrUpdate(SysAreaSite sysAreaSite) {
        this.sysAreaSiteManager.saveOrUpdate(sysAreaSite);
        log.info("受理辖区信息新增/更新成功：{}", JSON.toJSONString(sysAreaSite));
        return new ApiResultSet(sysAreaSite);
    }


    @Override
    public ApiResultSet delete(String ids) {
        this.sysAreaSiteManager.delete(ids);
        log.info("删除成功：{}", ids);
        return new ApiResultSet(ids);
    }


    @Override
    public ApiResultSet getOne(String id) {
        SysAreaSite sysAreaSite = this.sysAreaSiteManager.getOne(id);
        log.info("详情获取成功：{}", JSON.toJSONString(sysAreaSite));
        return new ApiResultSet(sysAreaSite);
    }

    /**
     *  禁用操作
     * @param id
     * @return
     */

    @Override
    public ApiResultSet ableSite(String id) {
        this.sysAreaSiteManager.ableSite(id);
        log.info("操作成功：{}", id);
        return new ApiResultSet(id);
    }

    @Override
    public ApiResultSet<List<SysAreaSite>> queryList() {
        ApiResultSet<List<SysAreaSite>> list=this.sysAreaSiteManager.queryList();
        return list;
    }
}
