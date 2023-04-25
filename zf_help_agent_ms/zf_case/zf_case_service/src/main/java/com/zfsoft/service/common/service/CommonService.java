package com.zfsoft.service.common.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.common.data.ElementTreeSelect;
import com.zfsoft.service.common.data.TreeStrSelect;
import com.zfsoft.service.sxDirectory.data.SxServiceType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName CommonService
 * @Description 公共常量参数定义接口
 * @Author xiayj
 * @Date 2020-10-25
 * @Version V1.0
 **/
@RequestMapping("/matter/common")
public interface CommonService {
    /**
     * 查询事项类型lsit
     * @return
     */
    @ProcessFeignCalledResult
    @CrossOrigin
    @RequestMapping( value = "/getDbSxServiceTypeList",method = {RequestMethod.GET})
    ApiResultSet<List<SxServiceType>> getDbSxServiceTypeList();

    /**
     * 获取行使层级列表
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getLevelList",method = {RequestMethod.GET})
    ApiResultSet getLevelList();

    /**
     * 获取行使层级列表
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getImplementDeptList",method = {RequestMethod.GET})
    ApiResultSet<List> getImplementDeptList();

    /**
     * 事项类型多选结构
     * @throws
     * @author meiyt
     * @date 2021/3/15 14:36
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServiceTypeTree",method = {RequestMethod.GET})
    ApiResultSet<List<SxServiceType>> getSxServiceTypeTree();

    /**
     * 根据主键ID查询
     * @throws
     * @author meiyt
     * @date 2021/3/15 14:36
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServiceType",method = {RequestMethod.POST})
    ApiResultSet<SxServiceType> getSxServiceType(@RequestBody SxServiceType sxServiceType);

    /**
     * @description:  获取业务层级SimpleJson 树
     * @param oid
     * @author: wangxl
     * @Date: 2021/1/15
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/querySxFactorConReportTree",method = {RequestMethod.GET})
    ApiResultSet<List<TreeStrSelect>> querySxFactorConReportTree(String oid);

    /**
     * @description:  验证超级管理员用户
     * @author: yuy
     * @Date: 2021/3/13
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/checkAdminUser",method = {RequestMethod.GET})
    String checkAdminUser();

    /**
     * @description:  获取机构
     * @author: yuy
     * @Date: 2021/3/18
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getOrganByOid",method = {RequestMethod.GET})
    ApiResultSet<String> getOrganByOid();

    /**
     * @Author gaoyc
     * @Description 获取服务对象
     * @Date 2021/4/8 15:05
     * @Param
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getServiceObjectList",method = {RequestMethod.GET})
    ApiResultSet<Object[]> getServiceObjectList();



    /**
     * @description:  获取业务层级SimpleJson 树
     * @param oid
     * @author: chenjm
     * @Date: 2021/1/15
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryFactorBaseSimpleTree",method = {RequestMethod.GET})
    ApiResultSet<List<ElementTreeSelect>> queryFactorBaseSimpleTree(
            @RequestParam(value = "oid", required = false) String oid);


    /**
     * @Description:根据基础库要素层级获取基础库要素树
     * @Author: chenjm
     * @Date: 2021/2/01 11:32
     * @param lableOid:
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/querySxLibraryFactorElementTree",method = {RequestMethod.GET})
    ApiResultSet<List<ElementTreeSelect>> querySxLibraryFactorElementTree(
            @RequestParam(value = "lableOid", required = false) String lableOid,
            @RequestParam(value = "elementOid", required = false) String elementOid);

    /**
     * 用于定时任务：数据质检同步
     * @throws
     * @author yuy
     * @date 2021/6/11
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = {"/sxFactorRuleRecordStatis"},method = {RequestMethod.GET})
    ApiResultSet sxFactorRuleRecordStatis(@RequestParam(value = "time", required = false) String time);

}
