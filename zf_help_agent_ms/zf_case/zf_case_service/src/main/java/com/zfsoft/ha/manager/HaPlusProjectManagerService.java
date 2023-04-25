package com.zfsoft.ha.manager;

import com.zfsoft.ha.data.HaPlusProject;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description 加分项目维护管理接口
 * @author dingsn
 * @date 2022/10/27  14:48
 */
@RequestMapping("/plusProjectManager")
public interface HaPlusProjectManagerService {
    /**
     * @description: 查询加分项目维护管理接口
     * @param name
     * @param pageNumber
     * @param pageSize
     * @author dingsn
     * @return
     * @throws Exception
     * @Date: 2022/10/27 14:56:40
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/plusProjectServicePage", method = {RequestMethod.POST})
    ApiResultSet<PageResult<HaPlusProject>> queryPlusProjectServicePage(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) throws Exception;


    /**
     * @description: 新增或者修改加分项目维护信息
     * @param haPlusProject 加分项目实体类
     * @author: dingsn
     * @Date: 2022/10/27
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/savePlusProject",method = {RequestMethod.POST})
    ApiResultSet savePlusProject(@RequestBody HaPlusProject haPlusProject) throws Exception;
    /**
     * @description:  根据id查询加分项目信息
     * @param id
     * @author: dingsn
     * @Date: 2022/10/27
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getPlusProjectById",method = {RequestMethod.GET})
    ApiResultSet getPlusProjectById(@RequestParam("id") Long id) throws Exception;

    /**
     * @description:  删除加分项目信息
     * @param id 主键
     * @author: dingsn
     * @Date: 2022/10/27
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deletePlusProjectById", method = {RequestMethod.GET})
    ApiResultSet deletePlusProjectById(@RequestParam("id") Long id) throws Exception;

    /**
     * @description:  根据id查询HaPlusProject对象list集合
     * @author: dingsn
     * @Date: 2022/10/27
     **/
    @RequestMapping( value = "/queryHaPlusProjectTree",method = {RequestMethod.GET})
    ApiResultSet<List<HaPlusProject>> queryHaPlusProjectTree() throws Exception;

    /**
     * 批量删除加分项目信息
     * @param ids
     * @return
     * @throws Exception
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deletePlusProjectListid", method = {RequestMethod.GET})
    ApiResultSet deletePlusProjecttids(@RequestParam(value = "ids")List<Long> ids) throws Exception;


}
