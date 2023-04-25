package com.zfsoft.ha.manager.controller;

import com.zfsoft.ha.data.HaWorkGroupSplit;
import com.zfsoft.ha.manager.HaWorkUserGroupSplitService;
import com.zfsoft.ha.managers.HaWorkGroupSplitServiceManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description 帮代办人员小组(组内分组）表控制层
 * @Author: zhaobf
 * @Date: 2023/3/20 13:58
 */
@RestController
@Slf4j
public class HaWorkGroupSplitController implements HaWorkUserGroupSplitService {
    /**
     * 帮代办人员小组(组内分组）表实现层
     */
    @Resource
    private HaWorkGroupSplitServiceManager haWorkGrouoServiceManager;
    /**
     * @description:  查询帮代办人员小组(组内分组）分页信息列表
     * @param name 组名
     * @param deleteStatus 删除状态
     * @author: zhaobf
     * @Date: 2022年7月26日14:04:40
     * @return  ApiResultSet<PageResult<HaWorkGroup>> 获取帮代办人员小组(组内分组）分页信息列表详情
     **/
    @Override
    public ApiResultSet<PageResult<HaWorkGroupSplit>> queryGroupServicePage(String name,String groupId, String deleteStatus, Integer pageNumber, Integer pageSize) throws Exception {
        log.info("查询帮代办人员小组(组内分组）分页信息列表， name:{},deleteStatus:{},pageNumber:{},pageSize:{}",name,deleteStatus,pageNumber,pageSize);
        PageResult<HaWorkGroupSplit> pageResult = haWorkGrouoServiceManager.queryGroupServiceWithPage(name,groupId,deleteStatus,pageNumber,pageSize);
        log.debug("pageResult结果集：pageResult:{}",pageResult);
        ApiResultSet<PageResult<HaWorkGroupSplit>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    /**
     * @description:  删除帮代办人员小组(组内分组）信息
     * @param id 主键
     * @author: zhaobf
     * @Date: 2023/3/20
     * @return  ApiResultSet 获取删除帮代办人员小组(组内分组）信息标识
     **/
    @Override
    public ApiResultSet deleteGroupOid(Long id) throws Exception {
        log.info("删除帮代办人员小组(组内分组）信息， id:{}",id);
        Integer index = haWorkGrouoServiceManager.deleteGroupid(id);
        if (index == 1) {
            return ApiResultSet.ok("帮代办人员小组(组内分组）信息删除成功");
        } else {
            return new ApiResultSet(500, "帮代办人员小组(组内分组）信息删除失败");
        }
    }

    /**
     * @description: 新增或者修改帮代办人员小组(组内分组）信息
     * @param haWorkGroup 帮代办人员小组(组内分组）实体类
     * @author: zhaobf
     * @Date: 2023/3/20
     * @return  ApiResultSet 获取新增或者修改帮代办人员小组(组内分组）信息标识
     **/
    @Override
    public ApiResultSet saveWorkUserGroup(HaWorkGroupSplit haWorkGroup) throws Exception {
        log.info("新增或者修改帮代办人员小组(组内分组）信息， haWorkGroup:{}",haWorkGroup);
        Map<String, Object> map = haWorkGrouoServiceManager.saveOrUpdateHaWorkGroup(haWorkGroup);
        int index = (int) map.get("index");
        log.debug("index结果集，index:{}", index);
        if (index == 1) {
            return ApiResultSet.ok("新增成功");
        } else {
            return ApiResultSet.ok("修改成功");
        }
    }

    /**
     * @description:  根据id查询帮代办人员小组(组内分组）信息
     * @param id
     * @author: zhaobf
     * @Date: 2023/3/20
     * @return  ApiResultSet 获取根据id查询帮代办人员小组(组内分组）信息
     */
    @Override
    public ApiResultSet getGroupById(Long id) throws Exception {
        log.info("根据id查询帮代办人员小组(组内分组）信息， id:{}",id);
        HaWorkGroupSplit haWorkGroup = haWorkGrouoServiceManager.selectByid(id);
        return ApiResultSet.ok("接口调用成功", haWorkGroup);
    }

    /**
     * @description:  根据id查询HaWorkGroup对象集合
     * @author: zhaobf
     * @Date: 2023/3/20 11:23
     **/
    @Override
    public ApiResultSet<List<HaWorkGroupSplit>> queryHaWorkGroupTree(String groupId) {
        log.info("根据id查询HaWorkGroup对象集合， groupId:{}",groupId);
        List<HaWorkGroupSplit> haWorkGroupList = haWorkGrouoServiceManager.haWorkGroupList(groupId);
        return ApiResultSet.ok("获取区划表成功",haWorkGroupList);
    }
}
