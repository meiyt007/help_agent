package com.zfsoft.microservice.platform.service.sys;

import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SysDistrictService
 * @Description 区划组件服务定义接口
 * @Author wuxx
 * @Date 2020-08-31 11:33
 **/
@RequestMapping("/security/district")
public interface SysDistrictService {
    /**
     * @description:  初始化区划的信息
     * @param  oid 区划实体类主键
     * @param  parentOid 区划实体父类主键
     * @author: wuxx
     * @Date: 2020/9/1 10:14
     **/
    @RequestMapping(value = {"/init"},method = {RequestMethod.GET})
    ApiResultSet initSysDistrict(@RequestParam(value="oid",required=false)Long oid, @RequestParam(value="parentOid",required=false)String parentOid);

    /**
     * @description:  区划的新增或者修改
     * @param sysDistrict 区划实体类
     * @author: wuxx
     * @Date: 2020/9/1 10:14
     **/
    @RequestMapping( value = "/save",method = {RequestMethod.POST})
    ApiResultSet<SysDistrict> saveSysDistrict(@RequestBody SysDistrict sysDistrict);

    /**
     * @description:  区划的信息的删除
     * @param oid 区划实体类主键
     * @author: wuxx
     * @Date: 2020/9/1 10:14
     **/
    @RequestMapping( value = "/delete/{oid}",method = {RequestMethod.POST})
    ApiResultSet<Integer>  deleteSysDistrictById(@PathVariable("oid")Long oid);

    /**
     * @description:  获取区划的信息
     * @param oid 区划实体类主键
     * @author: wuxx
     * @Date: 2020/9/1 10:14
     **/
    @RequestMapping( value = "/getOne/{oid}",method = {RequestMethod.GET})
    ApiResultSet<SysDistrict>  getSysDistrictById(@PathVariable("oid")Long oid);

    /**
     * @description:  获取区划的信息
     * @param districtOid 区划实体类业务主键
     * @author: wuxx
     * @Date: 2020/9/1 10:14
     **/
    @RequestMapping( value = "/getSysDistrictByDistrictOid/{districtOid}",method = {RequestMethod.GET})
    ApiResultSet<SysDistrict>  getSysDistrictByDistrictOid(@PathVariable("districtOid")String districtOid);

    /**
     * @description:  获取区划的信息
     * @param code 区划实体类业务编码
     * @author: wuxx
     * @Date: 2021/2/3 10:14
     **/
    @RequestMapping( value = "/getSysDistrictByDistrictCode",method = {RequestMethod.GET})
    ApiResultSet<SysDistrict>  getSysDistrictByDistrictCode(@RequestParam("code")String code);

    /**
     * @description:  根据父类oid查询区划
     * @param parentOid 父类oid
     * @author: wuxx
     * @Date: 2020/10/31 17:08
     **/
    @RequestMapping( value = "/querySysDistrictListByParentOid",method = {RequestMethod.GET})
    ApiResultSet<List<SysDistrict>>  querySysDistrictListByParentOid(@RequestParam(value="parentOid",required=false) String parentOid);

    /**
     * @description:  查询区划的信息列表
     * @param name 区划名称
     * @param code 区划代码
     * @param parentOid 区划父类oid
     * @author: wuxx
     * @Date: 2020/9/1 10:14
     **/
    @RequestMapping( value = "/page",method = {RequestMethod.GET})
    ApiResultSet querySysDistrictWithPage(@RequestParam(value="name",required=false)String name,
                                          @RequestParam(value="code",required=false)String code,
                                          @RequestParam(value="parentOid",required=false)String parentOid,
                                          @RequestParam(value="pageNum",required=false)Integer pageNum,
                                          @RequestParam(value="pageSize",required=false) Integer pageSize);

    /**
     * @description:  区划的信息的启禁用
     * @param oid 区划实体类主键
     * @author: wuxx
     * @Date: 2020/9/1 10:14
     **/
    @RequestMapping( value = "/able/{oid}",method = {RequestMethod.POST})
    ApiResultSet<Integer> ableSysDistrictById(@PathVariable("oid")Long oid);

    /**
     * @description:  根据path获取区划list
     * @param path 路径
     * @author: wuxx
     * @Date: 2020/10/26 14:14
     **/
    @RequestMapping( value = "/getListByPath",method = {RequestMethod.GET})
    ApiResultSet<List<SysDistrict>> getSysDistrictListByPath(@RequestParam("path")String path);

    /**
     * @description:  获取区划父类对象
     * @author: wuxx
     * @Date: 2020/11/30 10:14
     **/
    @RequestMapping( value = "/getTopSysDistrict",method = {RequestMethod.GET})
    ApiResultSet<SysDistrict>  getTopSysDistrict();

    /**
     * @description:  根据区划级别类型查询区划列表（省级、市级。。）
     * @param levelDictOid
     * @author: wuxx
     * @Date: 2021/1/8 11:23
     **/
    @RequestMapping( value = "/querySysDistrictListByLevelDictOid",method = {RequestMethod.GET})
    ApiResultSet<List<SysDistrict>>  querySysDistrictListByLevelDictOid(@RequestParam(value="levelDictOid",required=false) String levelDictOid);

    /**
     * @description:  根据区划oid查询区划Tree
     * @param districtOid 区划oid
     * @author: wuxx
     * @Date: 2021/1/8 11:23
     **/
    @RequestMapping( value = "/queryDistrictSimpleTreeList",method = {RequestMethod.GET})
    ApiResultSet<List<SysDistrict>>  queryDistrictSimpleTreeList(@RequestParam(value="districtOid",required=false) String districtOid);

    /**
     * @description:  根据区划oid查询区划下级主键集合
     * @param districtOid 区划oid
     * @author: wuxx
     * @Date: 2021/5/20 11:23
     **/
    @RequestMapping( value = "/queryPathListByDistrictOid",method = {RequestMethod.GET})
    ApiResultSet<List<String>>  queryPathListByDistrictOid(@RequestParam(value="districtOid",required=false) String districtOid);


    /**
     * @description:  根据区划oid查询DbSysDistrict对象
     * @param districtOid 区划oid
     * @author: wangyh
     * @Date: 2022/8/2 11:23
     **/
    @RequestMapping( value = "/selectByDistrictOid",method = {RequestMethod.GET})
    ApiResultSet selectByDistrictOid(@RequestParam(value="districtOid",required=true) String districtOid);



}
