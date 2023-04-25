package com.zfsoft.microservice.settings.service;

import com.zfsoft.microservice.settings.data.SysConfig;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * @ClassName SysConfigService
 * @Description 参数配置组件服务定义接口
 * @Author wuxx
 * @Date 2020-09-12 11:33
 * @Version V1.0
 **/
@RequestMapping("/security/config")
public interface SysConfigService {
    /**
     * @description:  初始化参数配置的信息
     * @param id 参数配置实体类主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/init"},method = {RequestMethod.GET})
    ApiResultSet initSysConfig(@RequestParam(value="id",required=false)Long id, @RequestParam(value="parentOid",required=false)String parentOid);

    /**
     * 删除指定Id的参数配置信息
     * @param id 参数配置id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/delete/{id}",method = {RequestMethod.POST})
    ApiResultSet<Integer>  deleteSysConfigById(@PathVariable("id")Long id);

    /**
     * @description:  参数配置的新增或者修改
     * @param sysConfig 参数配置实体类
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/save",method = {RequestMethod.POST})
    ApiResultSet<SysConfig> saveSysConfig(@RequestBody SysConfig sysConfig);

    /**
     * @description:  获取参数配置的信息
     * @param id 参数配置实体类主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOne/{id}",method = {RequestMethod.GET})
    ApiResultSet<SysConfig>  getSysConfigById(@PathVariable("id")Long id);

    /**
     * @description:  获取参数配置的信息
     * @param configOid 参数配置实体类业务主键
     * @author: wanglei
     * @Date: 2020/10/29 11:40
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSysConfigByConfigOId/{configOid}",method = {RequestMethod.GET})
    ApiResultSet<SysConfig>  getSysConfigByConfigOid(@PathVariable("configOid")String configOid);

    /**
     * @description:  获取参数配置的信息
     * @param code 参数配置实体类code
     * @author: wuxx
     * @Date: 2020/9/14 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSysConfigByCode",method = {RequestMethod.GET})
    ApiResultSet<SysConfig>  getSysConfigByCode(@RequestParam("code") String code);

    /**
     * @description:  查询参数配置的信息列表
     * @param name 参数配置名称
     * @param code 参数配置编码
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/page",method = {RequestMethod.GET})
    ApiResultSet<PageResult<SysConfig>> querySysConfigWithPage(@RequestParam(value="name") String name,
                                                               @RequestParam(value="code")String code,
                                                               @RequestParam(value="parentOid")String parentOid,
                                                               @RequestParam(value="pageNum")Integer pageNum,
                                                               @RequestParam(value="pageSize")Integer pageSize);

    /**
     * @description:  参数配置的信息的启禁用
     * @param id 参数配置实体类主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/able/{id}",method = {RequestMethod.POST})
    ApiResultSet<Integer> ableSysConfigById(@PathVariable("id")Long id);

    /**
     * @description:  根据父类code查询参数配置下级列表
     * @param parentCode 父类code
     * @author: wuxx
     * @Date: 2020/9/14 11:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/querySysConfigListByParentCode",method = {RequestMethod.GET})
    ApiResultSet<List<SysConfig>> querySysConfigListByParentCode(@RequestParam("parentCode") String parentCode);

    /**
     * @description:  根据父类code查询参数配置下级列表
     * @param parentOid 父类parentOid
     * @author: wuxx
     * @Date: 2020/9/14 11:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/querySysConfigListByparentOid",method = {RequestMethod.GET})
    ApiResultSet<List<SysConfig>> querySysConfigListByParentOid(@RequestParam("parentOid") String parentOid);

    /**
     * @description:  根据父类id获取参数配置列表，用于生成参数配置树
     * @param parentOid 父类id
     * @author: wuxx
     * @Date: 2020/9/9 18:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryConfigTree",method = {RequestMethod.GET})
    ApiResultSet queryConfigTree(@RequestParam(value="parentOid")String parentOid);

}
