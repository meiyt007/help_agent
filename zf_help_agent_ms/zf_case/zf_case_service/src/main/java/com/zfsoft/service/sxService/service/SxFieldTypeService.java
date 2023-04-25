package com.zfsoft.service.sxService.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxService.data.SxFieldType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 字段类别管理
 * @author: wuxx
 * @Date: 2021/7/14 13:14
 **/
@RequestMapping("/dataChange/fieldType")
public interface SxFieldTypeService {

    /**
     * @description:  查询字段类别的信息列表
     * @param fieldTypeName 字段类别名称
     * @author: wuxx
     * @Date: 2021/7/14 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/page",method = {RequestMethod.GET})
    ApiResultSet querySxFieldTypeWithPage(@RequestParam(value = "fieldTypeName", required = false) String fieldTypeName,
                                          @RequestParam(value = "isAble", required = false) Integer isAble,
                                          @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                          @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * @description:  初始化字段类别的信息
     * @param id 字段类别实体类主键
     * @author: wuxx
     * @Date: 2021/7/14 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/init/{id}"},method = {RequestMethod.GET})
    ApiResultSet initSxFieldType(@PathVariable("id") Long id);

    /**
     * @description:  字段类别的新增或者修改
     * @param sxFieldType 字段类别实体类
     * @author: wuxx
     * @Date: 2021/7/14 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/save",method = {RequestMethod.POST})
    ApiResultSet<SxFieldType> saveSxFieldType(@RequestBody SxFieldType sxFieldType);

    /**
     * @description:  获取字段类别的信息
     * @param id 字段类别实体类主键
     * @author: wuxx
     * @Date: 2021/7/14 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOne/{id}",method = {RequestMethod.GET})
    ApiResultSet<SxFieldType> getSxFieldTypeById(@PathVariable("id") Long id);

    /**
     * @description:  获取字段类别的信息
     * @param fieldTypeOid 字段类别主键
     * @author: wuxx
     * @Date: 2021/7/14 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxFieldTypeByFieldTypeOid/{fieldTypeOid}",method = {RequestMethod.GET})
    ApiResultSet<SxFieldType> getSxFieldTypeByFieldTypeOid(@PathVariable("fieldTypeOid") String fieldTypeOid);

    /**
     * 删除指定Id的字段类别信息
     * @param id 字段类别ids
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/delete/{id}",method = {RequestMethod.POST})
    ApiResultSet<Integer>  deleteSxFieldTypeById(@PathVariable("id") Long id);

    /**
     * @description:  字段类别的信息的启禁用
     * @param id 字段类别ids
     * @author: wuxx
     * @Date: 2021/7/14 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/able/{id}",method = {RequestMethod.POST})
    ApiResultSet<Integer> ableSxFieldTypeById(@PathVariable("id") Long id);

    /**
     * @description:  根据类别主键查询字段类别下级列表
     * @param parentOid 父类类别主键
     * @author: wuxx
     * @Date: 2021/7/14 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/querySxFieldTypeList",method = {RequestMethod.GET})
    ApiResultSet<List<SxFieldType>> querySxFieldTypeList(@RequestParam(value = "parentOid", required = false) String parentOid,
                                                         @RequestParam(value = "isAble", required = false) Integer isAble);

    /**
     * @description:  根据类别主键查询一级字段类别
     * @author: wuxx
     * @Date: 2021/7/14 16:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryTopSxFieldTypeList",method = {RequestMethod.GET})
    ApiResultSet<List<SxFieldType>> queryTopSxFieldTypeList(@RequestParam(value = "fieldTypeName", required = false) String fieldTypeName,
                                                            @RequestParam(value = "isAble", required = false) Integer isAble);


    /**
     * 根据事项主键查询标签所属的所有分类
     * @param serviceOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/querySxFieldTypeListByServiceOid",method = {RequestMethod.GET})
    ApiResultSet<List<SxFieldType>> querySxFieldTypeListByServiceOid(@RequestParam(value = "serviceOid", required = false) String serviceOid);


    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryComboFieldTypeListByThingOid",method = {RequestMethod.GET})
    ApiResultSet<List<SxFieldType>> queryComboFieldTypeListByThingOid(@RequestParam(value = "thingOid", required = false) String thingOid);

}
