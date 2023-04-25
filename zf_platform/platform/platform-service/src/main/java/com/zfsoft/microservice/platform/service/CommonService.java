package com.zfsoft.microservice.platform.service;

import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.web.TreeSelect;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName CommonService
 * @Description
 * @Author
 * @Date2020-10-20 18:26
 * @Version V1.0
 **/
@RequestMapping("/security/common")
public interface CommonService {
    /**
     * @description:  根据区划oid查询区划对象集合
     * @author: wangyh
     * @Date: 2022/8/2 11:23
     **/
    @RequestMapping( value = "/queryDistrictTree",method = {RequestMethod.GET})
    ApiResultSet<List<SysDistrict>> queryDistrictTree(@RequestParam(value = "districtOid",required = false)String districtOid);


    /**
     * @description:  根据当前登录人区划下级的区划SimpleJson 树
     * 当districtOid为空时，查询所有的记录，当districtOid对应的编号查询不到区划信息时，无法查询到下级区划信息
     * @param districtOid 区划实体类主键
     * @param districtOids 在当前区划外的区划 用于编辑时回显区划
     * @author: wuxx
     * @Date: 2020/9/1 10:14
     **/
    @RequestMapping( value = "/queryDistrictSimpleTree",method = {RequestMethod.GET})
    ApiResultSet<List<TreeSelect>> queryDistrictSimpleTree(@RequestParam(value = "districtOid",required = false)String districtOid, @RequestParam(value = "districtOids",required = false) String districtOids);

    /**
     * @description:  查询所有的记录
     * @author: wuxx
     * @Date: 2020/9/1 10:14
     **/
    @RequestMapping( value = "/queryAllDistrictSimpleTree",method = {RequestMethod.GET})
    ApiResultSet<List<TreeSelect>> queryAllDistrictSimpleTree();


    /**
     * @description:  根据区划oid的查询区划树(可设置排除在外的区划)
     * 当districtOid为空时，查询所有的记录，当districtOid对应的编号查询不到区划信息时，无法查询到下级区划信息
     * @param districtOid 区划实体类主键
     * @param noIncludeOids 区划树不包含的主键以逗号隔开1,2,3
     * @author: wuxx
     * @Date: 2020/9/1 10:14
     **/
    @RequestMapping( value = "/queryDistrictTreeNoInclude",method = {RequestMethod.GET})
    ApiResultSet<List<TreeSelect>>  queryDistrictTreeNoInclude(@RequestParam("districtOid")String districtOid,@RequestParam("noIncludeOids")String noIncludeOids);

    /**
     * @description:  根据区划编号获取组织机构列表，用于生成组织机构树
     * @param districtOid 区划oid
     * @author: wuxx
     * @Date: 2020/9/9 18:14
     **/
    @RequestMapping( value = "/queryOrganTree",method = {RequestMethod.GET})
    ApiResultSet<List<TreeSelect>> querySysOrganWithPage(@RequestParam("districtOid")String districtOid);

    /**
     * 异步区划机构用户树
     * @author zxx
     * @date 2020/9/24 7:56 下午
     * @param oid
     * @return
     */
    @RequestMapping( value = "/queryDistrictOrganUserTree",method = {RequestMethod.GET})
    ApiResultSet queryDistrictAndOrganAndUserTreeSelect(@RequestParam("oid")String oid,@RequestParam("identity")String identity);

    /**
     * @description:  根据区划编号获取组织机构列表，用于生成区划组织机构树
     * @param districtOid 区划oid
     * @author: wuxx
     * @Date: 2021/01/04 10:14
     **/
    @RequestMapping( value = "/queryDistrictOrganTree",method = {RequestMethod.GET})
    ApiResultSet<List<TreeSelect>> queryDistrictOrganTree(@RequestParam(value = "districtOid",required = false)String districtOid);

    /**
     * @description: 查询所有区划的组织机构、用户信息Tree
     * @author: wuxx
     * @Date: 2021/1/26 10:40
     **/
    @RequestMapping( value = "/queryAllDistrictOrganUserTree",method = {RequestMethod.GET})
    ApiResultSet<List<TreeSelect>> queryAllDistrictOrganUserTree();

    /**
     * @description: 查询所有区划的组织机构、岗位信息Tree
     * @author: wuxx
     * @Date: 2021/1/26 10:40
     **/
    @RequestMapping( value = "/queryAllDistrictOrganPostTree",method = {RequestMethod.GET})
    ApiResultSet<List<TreeSelect>> queryAllDistrictOrganPostTree();

    /**
     * @description:  查询应用下角色Tree
     * @author: wuxx
     * @Date: 2022/03/11 10:14
     **/
    @RequestMapping( value = "/queryAppRoleTree",method = {RequestMethod.GET})
    ApiResultSet<List<TreeSelect>> queryAppRoleTree();

    /**
     * @description:  查询区划下组件机构Tree
     * @author: wuxx
     * @Date: 2022/03/11 10:14
     **/
    @RequestMapping( value = "/queryWorkFlowDistrictOrganTree",method = {RequestMethod.GET})
    ApiResultSet<List<TreeSelect>> queryWorkFlowDistrictOrganTree();

}
