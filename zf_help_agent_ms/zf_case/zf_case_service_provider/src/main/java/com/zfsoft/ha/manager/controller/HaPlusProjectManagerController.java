package com.zfsoft.ha.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.zfsoft.ha.data.HaPlusProject;
import com.zfsoft.ha.manager.HaPlusProjectManagerService;
import com.zfsoft.ha.managers.HaPlusProjectManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 加分项目维护管理控制层
 * @author dingsn
 * @date 2022/10/27  15:03
 */
@RestController
@Slf4j
public class HaPlusProjectManagerController implements HaPlusProjectManagerService {

    /**
     * 加分项目维护管理实现业务层
     */
    @Resource
    private HaPlusProjectManager haPlusProjectManager;

    /**
     * @description: 查询加分项目维护管理信息分页列表
     * @param name
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public ApiResultSet<PageResult<HaPlusProject>> queryPlusProjectServicePage(String name, Integer pageNumber,
                                                                               Integer pageSize) throws Exception {
        log.info("查询加分项目分页列表信息，查询条件：加分项目名称={}", name);
        PageResult<HaPlusProject> pageResult = haPlusProjectManager.queryPlusProjectServiceWithPage(name, pageNumber,pageSize);
        log.debug("查询加分项目信息结果集：{}", pageResult);
        ApiResultSet<PageResult<HaPlusProject>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    /**
     * @description: 新增或者修改加分项目维护信息
     * @param haPlusProject 加分项目实体类
     * @return
     * @throws Exception
     */
    @Override
    public ApiResultSet savePlusProject(HaPlusProject haPlusProject) throws Exception {
        log.info("新增或者修改加分项目维护信息=={}", haPlusProject);
        JSONObject result = haPlusProjectManager.saveOrUpdateHaPlusProject(haPlusProject);
        int index = (int) result.get("index");
        log.debug("index结果集，index:{}", index);
        if (index == 1) {
            return ApiResultSet.ok("新增成功");
        } else {
            return ApiResultSet.ok("修改成功");
        }
    }

    /**
     * @description: 根据id查询加分项目信息
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public ApiResultSet getPlusProjectById(Long id) throws Exception {
        log.info("根据id查询加分项目信息,id={}", id);
        HaPlusProject haPlusProject = haPlusProjectManager.selectPlusProjectByid(id);
        return ApiResultSet.ok("加分项目接口查询调用成功", haPlusProject);
    }

    /**
     * @description: 删除加分项目信息
     * @param id 主键
     * @return
     * @throws Exception
     */
    @Override
    public ApiResultSet deletePlusProjectById(Long id) throws Exception {
        log.info("根据id删除加分项目信息, id={}", id);
        Integer index = haPlusProjectManager.deletePlusProjectById(id);
        if (1 == index) {
            return ApiResultSet.ok("加分项信息已成功删除");
        } else {
            return new ApiResultSet(500, "加分项信息删除失败");
        }
    }

    /**
     * @description: 根据id查询HaPlusProject对象集合
     * @return
     */
    @Override
    public ApiResultSet<List<HaPlusProject>> queryHaPlusProjectTree() throws Exception {
        log.info("根据id查询HaPlusProject对象集合");
        List<HaPlusProject> list = haPlusProjectManager.queryPlusProjectList();
        return ApiResultSet.ok("查询加分项信息对象列表成功", list);
    }

    /**
     * 批量删除加分项目信息
     * @param ids
     * @return
     * @throws Exception
     */
    @Override
    public ApiResultSet deletePlusProjecttids(List<Long> ids) throws Exception {
        log.info("批量删除加分项目信息，ids={}", ids);
        ApiResultSet<HaPlusProject> apiResultSet = new ApiResultSet<>();
        haPlusProjectManager.deletePlusProjectByIds(ids);
        apiResultSet.setMessage("批量删除成功");
        return null;
    }
}
