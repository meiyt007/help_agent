package com.zfsoft.microservice.platform.service.sys;

import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.microservice.platform.data.vo.SysOrganVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SysOrganService
 * @Description 组织机构组件服务定义接口
 * @Author wuxx
 * @Date 2020-09-07 11:33
 * @Version V1.0
 **/
@RequestMapping("/security/organ")
public interface SysOrganService {
    /**
     * @description:  初始化区划的信息
     * @param oid 组织机构主键
     * @author: wuxx
     * @Date: 2020/9/1 10:14
     **/
    @RequestMapping( value = {"/init"},method = {RequestMethod.GET})
     ApiResultSet initSysDistrict(@RequestParam(value="oid",required=false)Long oid);

    /**
     * @description:  组织机构的新增或者修改
     * @param sysOrgan 组织机构实体类
     * @author: wuxx
     * @Date: 2020/9/2 10:14
     **/
    @RequestMapping( value = "/save",method = {RequestMethod.POST})
     ApiResultSet<SysOrgan> saveSysOrgan(@RequestBody SysOrgan sysOrgan);

    /**
     * @description:  组织机构的信息的删除
     * @param oid 组织机构实体类主键
     * @author: wuxx
     * @Date: 2020/9/2 10:14
     **/
    @RequestMapping( value = "/delete/{oid}",method = {RequestMethod.POST})
     ApiResultSet<Integer>  deleteSysOrganById(@PathVariable("oid")Long oid);

    /**
     * @description:  获取组织机构的信息
     * @param organOid 组织机构实体类业务主键
     * @author: wuxx
     * @Date: 2020/9/2 10:14
     **/
    @RequestMapping( value = "/getSysOrganByOrganOid/{organOid}",method = {RequestMethod.GET})
     ApiResultSet<SysOrgan>  getSysOrganByOrganOid(@PathVariable("organOid")String organOid);

    /**
     * @description:  根据父类oid查询机构
     * @param parentOid 父类oid
     * @author: wuxx
     * @Date: 2020/10/31 17:08
     **/
    @RequestMapping( value = "/querySysOrganListByOrganOid",method = {RequestMethod.GET})
    ApiResultSet<List<SysOrgan>>  querySysOrganListByParentOid(@RequestParam(value="parentOid",required=false) String parentOid);

    /**
     * @description:  根据编码查询机构
     * @param code 编码
     * @author: wuxx
     * @Date: 2021/7/16 17:08
     **/
    @RequestMapping( value = "/querySysOrganByCode",method = {RequestMethod.GET})
    ApiResultSet<SysOrgan>  querySysOrganByCode(@RequestParam(value="code") String code);
    /**
     * @description:  查询前端展示的组织机构列表组织机构的信息列表
     * @param name 组织机构名称
     * @param districtOid 区划id
     * @param parentOid 组织机构父类oid
     * @author: wangyh
     * @Date: 2020/9/2 10:14
     **/
    @RequestMapping( value = "/querySysOrganShieldWithPage",method = {RequestMethod.GET})
    ApiResultSet querySysOrganShieldWithPage(@RequestParam(value = "name",required = false) String name,
                                       @RequestParam(value ="districtOid",required = false)String districtOid,
                                       @RequestParam(value = "parentOid",required = false)String parentOid,
                                       @RequestParam(value = "shield",required = false)String shield,
                                       @RequestParam("pageNum")Integer pageNum,
                                       @RequestParam("pageSize")Integer pageSize);

    /**
     * @description:  查询组织机构的信息列表
     * @param name 组织机构名称
     * @param districtOid 区划id
     * @param parentOid 组织机构父类oid
     * @author: wuxx
     * @Date: 2020/9/2 10:14
     **/
    @RequestMapping( value = "/page",method = {RequestMethod.GET})
     ApiResultSet querySysOrganWithPage(@RequestParam(value = "name",required = false) String name,
                                        @RequestParam(value ="districtOid",required = false)String districtOid,
                                        @RequestParam(value = "parentOid",required = false)String parentOid,
                                        @RequestParam("pageNum")Integer pageNum,
                                        @RequestParam("pageSize")Integer pageSize);

    /**
     * @description:  组织机构的信息的启禁用
     * @param oid 组织机构实体类主键
     * @author: wuxx
     * @Date: 2020/9/2 10:14
     **/
    @RequestMapping( value = "/able/{oid}",method = {RequestMethod.POST})
     ApiResultSet<Integer> ableSysOrganById(@PathVariable("oid")Long oid);

    /**
     * @description:  查看组织机构管理用户
     * @param organOid 组织机构oid
     * @author: wuxx
     * @Date: 2020/9/10 10:00
     **/
    @RequestMapping( value = "/userListByOrgan",method = {RequestMethod.GET})
     ApiResultSet userListByOrgan(
             @RequestParam(value = "organOid",required = false) String organOid,
             @RequestParam("pageNum") Integer pageNum,
             @RequestParam("pageSize") Integer pageSize);

    /**
     * @description:  导出成excel
     * @param name 组织机构名称
     * @param districtOid 区划id
     * @param parentOid 组织机构父类oid
     * @author: wuxx
     * @Date: 2020/9/10 10:00
     **/
    @RequestMapping( value = "/listExp",method = {RequestMethod.GET})
     void listExp(@RequestParam(value = "name",required = false) String name,
                  @RequestParam(value = "districtOid",required = false) String districtOid,
                  @RequestParam(value = "parentOid",required = false) String parentOid);


    /**
     * @description: 根据组织机构的oids集合获取名称集合
     * @param oids 组织机构的oids集合
     * @author: wuxx
     * @Date: 2020/11/6 9:56
     **/
    @RequestMapping( value = {"/getOrganNameListByOids"},method = {RequestMethod.POST})
    ApiResultSet<List<String>> getOrganNameListByOids(@RequestBody List<String> oids);

    /**
     * @description:  根据区划查询组织机构
     * @param districtOid
     * @author: wuxx
     * @Date: 2021/1/8 11:23
     **/
    @RequestMapping( value = "/querySysOrganListByDistrictOid",method = {RequestMethod.GET})
    ApiResultSet<List<SysOrgan>>  querySysOrganListByDistrictOid(@RequestParam(value="districtOid",required=false) String districtOid);


    /**
     * @description:  同步万达数据源组织机构的新增或者修改
     * @param sysOrganVo 组织机构实体类
     * @author: wuxx
     * @Date: 2020/9/2 10:14
     **/
    @RequestMapping( value = "/synSave",method = {RequestMethod.POST})
    ApiResultSet synSaveSysOrgan(@RequestBody SysOrganVo sysOrganVo);

    /**
     * 获取机构分组列表
     * @author wangyh
     * @Date: 2020/9/09 14:14
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryOrganSelectOptions", method = {RequestMethod.GET})
    ApiResultSet <List<SysOrgan>>queryOrganSelectOptions();

    /**
     * @description:  批量屏蔽部门
     * @param ids 主键
     * @author: wangyh
     * @Date: 2022/11/11
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/shieldOrganListid", method = {RequestMethod.GET})
    ApiResultSet shieldOrganListid(@RequestParam(value = "ids")List<Long> ids,
                                   @RequestParam("isshield") String isshield) throws Exception;

    /**
     * @description:  屏蔽单条部门
     * @param id 账号
     * @author: wangyh
     * @Date: 2022/11/11
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/shieldById", method = {RequestMethod.GET})
    ApiResultSet shieldById(@RequestParam("id") Long id,
                            @RequestParam("isshield") String isshield) throws Exception;
}

