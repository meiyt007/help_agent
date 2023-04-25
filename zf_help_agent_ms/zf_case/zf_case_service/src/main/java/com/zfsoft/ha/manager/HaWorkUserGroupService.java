package com.zfsoft.ha.manager;

import com.zfsoft.ha.data.HaWorkGroup;
import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description 帮代办人员分组接口
 * @Author: Wangyh
 * @Date: 2022/8/4 13:43
 */
@RequestMapping("/work/group")
public interface HaWorkUserGroupService {
    /**
     * @description:  查询帮代办人员分组分页信息列表
     * @param name 组名
     * @param deleteStatus 删除状态
     * @author: wangyh
     * @Date: 2022/8/4 14:04:40
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/groupServicePage", method = {RequestMethod.POST})
    ApiResultSet<PageResult<HaWorkGroup>> queryGroupServicePage(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "deleteStatus", required = false) String deleteStatus,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) throws Exception;

    /**
     * @description:  删除帮代办人员分组信息
     * @param id 主键
     * @author: wangyh
     * @Date: 2022/8/4
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteGroupOid", method = {RequestMethod.GET})
    ApiResultSet deleteGroupOid(@RequestParam("id") Long id) throws Exception;

    /**
     * @description: 新增或者修改帮代办人员分组信息
     * @param haWorkGroup 帮代办人员分组实体类
     * @author: wangyh
     * @Date: 2022/8/4
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveGroup",method = {RequestMethod.POST})
    ApiResultSet saveWorkUserGroup(@RequestBody HaWorkGroup haWorkGroup) throws Exception;

    /**
     * @description:  根据id查询帮代办人员分组信息
     * @param id
     * @author: wangyh
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getGroupById",method = {RequestMethod.GET})
    ApiResultSet getGroupById(@RequestParam("id") Long id) throws Exception;

    /**
     * @description:  根据id查询HaWorkGroup对象集合
     * @author: wangyh
     * @Date: 2022/8/4 11:23
     **/
    @RequestMapping( value = "/queryHaWorkGroupTree",method = {RequestMethod.GET})
    ApiResultSet<List<HaWorkGroup>> queryHaWorkGroupTree(@RequestParam(value = "id",required = false)String id);


}
