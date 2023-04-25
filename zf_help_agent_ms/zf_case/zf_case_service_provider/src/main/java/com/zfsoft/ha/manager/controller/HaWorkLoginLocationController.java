package com.zfsoft.ha.manager.controller;

import com.zfsoft.ha.data.HaWorkLoginLocation;
import com.zfsoft.ha.manager.HaWorkUserLoginLocationService;
import com.zfsoft.ha.managers.HaWorkLoginLocationServiceManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description 帮代办人员登录地点表控制层
 * @author: zhaobingfeng
 * @Date: 2023/3/9 13:58
 */
@RestController
@Slf4j
public class HaWorkLoginLocationController implements HaWorkUserLoginLocationService {
    /**
     * 帮代办人员登录地点表实现层
     */
    @Resource
    private HaWorkLoginLocationServiceManager haWorkLoginLocationServiceManager;
    /**
     * @description:  查询帮代办人员登录地点分页信息列表
     * @param name 组名
     * @param deleteStatus 删除状态
     * @author: zhaobingfeng
     * @Date: 2022年7月26日14:04:40
     * @return  ApiResultSet<PageResult<HaWorkGroup>> 获取帮代办人员登录地点分页信息列表详情
     **/
    @Override
    public ApiResultSet<PageResult<HaWorkLoginLocation>> queryPage(String name, String deleteStatus, Integer pageNumber, Integer pageSize) throws Exception {
        log.info("查询帮代办人员登录地点分页信息列表， name:{},deleteStatus:{},pageNumber:{},pageSize:{}",name,deleteStatus,pageNumber,pageSize);
        PageResult<HaWorkLoginLocation> pageResult = haWorkLoginLocationServiceManager.queryWithPage(name,deleteStatus,pageNumber,pageSize);
        log.debug("pageResult结果集：pageResult:{}",pageResult);
        ApiResultSet<PageResult<HaWorkLoginLocation>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    /**
     * @description:  删除帮代办人员登录地点信息
     * @param id 主键
     * @author: zhaobingfeng
     * @Date: 2023/3/9
     * @return  ApiResultSet 获取删除帮代办人员登录地点信息标识
     **/
    @Override
    public ApiResultSet deleteOid(Long id) throws Exception {
        log.info("删除帮代办人员登录地点信息， id:{}",id);
        Integer index = haWorkLoginLocationServiceManager.deleteGroupid(id);
        if (index == 1) {
            return ApiResultSet.ok("帮代办人员登录地点信息删除成功");
        } else {
            return new ApiResultSet(500, "帮代办人员登录地点信息删除失败");
        }
    }

    /**
     * @description: 新增或者修改帮代办人员登录地点信息
     * @param haWorkGroup 帮代办人员登录地点实体类
     * @author: zhaobingfeng
     * @Date: 2023/3/9
     * @return  ApiResultSet 获取新增或者修改帮代办人员登录地点信息标识
     **/
    @Override
    public ApiResultSet save(HaWorkLoginLocation haWorkGroup) throws Exception {
        log.info("新增或者修改帮代办人员登录地点信息， haWorkGroup:{}",haWorkGroup);
        Map<String, Object> map = haWorkLoginLocationServiceManager.saveOrUpdateHaWorkGroup(haWorkGroup);
        int index = (int) map.get("index");
        log.debug("index结果集，index:{}", index);
        if (index == 1) {
            return ApiResultSet.ok("新增成功");
        } else {
            return ApiResultSet.ok("修改成功");
        }
    }

    /**
     * @description:  根据id查询帮代办人员登录地点信息
     * @param id
     * @author: zhaobingfeng
     * @Date: 2023/3/9
     * @return  ApiResultSet 获取根据id查询帮代办人员登录地点信息
     */
    @Override
    public ApiResultSet getById(Long id) throws Exception {
        log.info("根据id查询帮代办人员登录地点信息， id:{}",id);
        HaWorkLoginLocation haWorkGroup = haWorkLoginLocationServiceManager.selectByid(id);
        return ApiResultSet.ok("接口调用成功", haWorkGroup);
    }

    /**
     * @description:  查询HaWorkGroup对象集合
     * @author: zhaobingfeng
     * @Date: 2023/3/9 11:23
     **/
    @Override
    public ApiResultSet<List<HaWorkLoginLocation>> queryTree() {
        log.info("查询HaWorkGroup对象集合，");
        List<HaWorkLoginLocation> haWorkGroupList = haWorkLoginLocationServiceManager.haWorkGroupList();
        return ApiResultSet.ok("获取登录地点列表成功",haWorkGroupList);
    }
}
