package com.zfsoft.microservice.settings.service;

import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SysDictService
 * @Description 数据字典组件服务定义接口
 * @Author wuxx
 * @Date 2020-09-12 11:33
 * @Version V1.0
 **/
@RequestMapping("/security/dict")
public interface SysDictService{

    /**
     * @description:  初始化数据字典的信息
     * @param id 数据字典实体类
     * @param parentOid 父类id
     * @author: wuxx
     * @Date: 2020/9/1 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = {"/init"},method = {RequestMethod.GET})
    public ApiResultSet initSysDistrict(@RequestParam(value="id",required=false)Long id, @RequestParam(value="parentOid",required=false)String parentOid);

    /**
     * @description:  数字字 典的新增或者修改
     * @param sysDict 数据字典实体类
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/save",method = {RequestMethod.POST})
    public ApiResultSet<SysDict> saveSysDict(@RequestBody SysDict sysDict);

    /**
     * @description:  新增或修改数据字典时，验证同一父节点下，是否存在相同的字典代码
     * @param id 实体类主键
     * @param code 字典代码
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/checkCode",method = {RequestMethod.POST})
    public ApiResultSet<String> checkCode(@RequestParam("id") Long id, @RequestParam("code")String code);

    /**
     * @description:  数据字典的信息的删除
     * @param id 数据字典实体类主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/delete",method = {RequestMethod.POST})
    public ApiResultSet<Integer>  deleteSysDictById(@RequestParam("id")Long id,@RequestParam("dictOid")String dictOid);

    /**
     * @description:  获取数据字典的信息
     * @param id 数据字典实体类主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOne/{id}",method = {RequestMethod.GET})
    public ApiResultSet<SysDict>  getSysDictById(@PathVariable("id")Long id);

    /**
     * @description:  获取数据字典的信息
     * @param code 数据字典实体类code
     * @author: wuxx
     * @Date: 2020/9/14 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSysDictByCode",method = {RequestMethod.GET})
    public ApiResultSet<SysDict>  getSysDictByCode(@RequestParam("code") String code);

    /**
     * @description:  查询数据字典的信息列表
     * @param name 数据字典名称
     * @param code 数据字典编码
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/page",method = {RequestMethod.GET})
    public ApiResultSet<PageResult<SysDict>> querySysDictWithPage(@RequestParam(value="name",required=false) String name,
                                                                  @RequestParam(value="code",required=false) String code,
                                                                  @RequestParam(value="parentOid",required=false) String parentOid,
                                                                  @RequestParam(value="pageNum",required=false) Integer pageNum,
                                                                  @RequestParam(value="pageSize",required=false) Integer pageSize);

    /**
     * @description:  数据字典的信息的启禁用
     * @param id 数据字典实体类主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/able/{id}",method = {RequestMethod.POST})
    public ApiResultSet<Integer> ableSysDictById(@PathVariable("id")Long id);

    /**
     * @description:  获取数据字典的信息
     * @param dictOid 数据字典业务主键
     * @author: wanglei
     * @Date: 2020/10/28 17:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSysDictByDictOid/{dictOid}",method = {RequestMethod.GET})
    ApiResultSet<SysDict>  getSysDictByDictOid(@PathVariable("dictOid")String dictOid);

    /**
     * @description:  根据父类code查询数据字典下级列表
     * @param parentCode 父类code
     * @author: wuxx
     * @Date: 2020/9/14 11:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/querySysDictListByParentCode",method = {RequestMethod.GET})
    ApiResultSet<List<SysDict>> querySysDictListByParentCode(@RequestParam("parentCode") String parentCode);

    /**
     * @description:  根据父类code查询数据字典下级列表
     * @param parentOid 父类parentOid
     * @author: wuxx
     * @Date: 2020/9/14 11:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/querySysDictListByParentOid",method = {RequestMethod.GET})
    ApiResultSet<List<SysDict>> querySysDictListByParentOid(@RequestParam("parentOid") String parentOid);

}
