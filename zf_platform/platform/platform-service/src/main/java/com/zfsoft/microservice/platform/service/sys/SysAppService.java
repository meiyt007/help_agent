package com.zfsoft.microservice.platform.service.sys;

import com.zfsoft.microservice.platform.data.sys.SysApp;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName SysAppService
 * @Description 应用组件服务定义接口
 * @Author wuxx
 * @Date 2020-09-10 11:33
 * @Version V1.0
 **/
@RequestMapping("/security/app")
public interface SysAppService {
    /**
     * 增加一个新应用
     *
     * @param sysApp 新应用
     * @return
     */
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    ApiResultSet<SysApp> saveSysApp(@RequestBody SysApp sysApp);

    /**
     * 删除指定Id的应用信息
     *
     * @param oid 应用id
     * @return
     */

    @RequestMapping(value = "/delete/{oid}", method = {RequestMethod.POST})
    ApiResultSet<Integer> deleteSysAppById(@PathVariable("oid") Long oid);

    /**
     * 启禁用指定Id的应用信息
     *
     * @param oid 应用id
     * @return
     */
    @RequestMapping(value = "/able/{oid}", method = {RequestMethod.POST})
    ApiResultSet<Integer> ableSysAppById(@PathVariable("oid") Long oid);

    /**
     * 根据应用Id获取应用信息
     *
     * @param oid 应用Id
     * @return
     */
    @RequestMapping(value = "/getOne/{oid}", method = {RequestMethod.GET})
    ApiResultSet<SysApp> getSysAppById(@PathVariable("oid") Long oid);

    /**
     * 根据应用业务OId获取应用信息
     *
     * @param appOid 应用Id
     * @return
     */
    @RequestMapping(value = "/getSysAppByAppOid/{appOid}", method = {RequestMethod.GET})
    ApiResultSet<SysApp> getSysAppByAppOid(@PathVariable("appOid") String appOid);

    /**
     * 查询应用信息
     *
     * @param name     应用
     * @param pageNum 开始页
     * @param pageSize   每页大小
     * @return
     */
    @RequestMapping(value = "/page", method = {RequestMethod.GET})
    ApiResultSet querySysAppWithPage( @RequestParam(value = "name",required = false)String name,
                                      @RequestParam(value = "pageNum",required = false)Integer pageNum,
                                      @RequestParam(value = "pageSize",required = false)Integer pageSize);


    /**
     * 导出应用信息
     *
     * @param name     应用
     * @param pageNum 开始页
     * @param pageSize   每页大小
     * @return
     */
    @RequestMapping(value = "/listExp", method = {RequestMethod.GET})
    void listExp(HttpServletResponse response, @RequestParam(value = "name",required = false)String name,
                 @RequestParam(value = "pageNum",required = false)Integer pageNum,
                 @RequestParam(value = "pageSize",required = false)Integer pageSize);

    /**
     * 查询应用列表
     * @author zxx
     * @date 2020/9/22 4:14 下午
     * @param name 名称
     * @return
     */
    @GetMapping("/list")
    public ApiResultSet querySysApp(@RequestParam(value = "name",required = false)String name);

}
