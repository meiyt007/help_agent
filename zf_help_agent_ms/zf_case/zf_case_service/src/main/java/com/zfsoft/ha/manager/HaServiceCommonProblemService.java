package com.zfsoft.ha.manager;

import com.zfsoft.ha.data.HaServiceCommonProblem;
import com.zfsoft.ha.data.HaUserResource;
import com.zfsoft.ha.data.vo.HaServiceCommonProblemVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/9/5 14:32
 */
@RequestMapping("/help/service/commonProblem")
public interface HaServiceCommonProblemService {


    /**
     * 根据事项oid分页查询事项常见问题
     * @param serviceOid 事项oid
     * @param pageNum 分页参数 页码
     * @param pageSize 分页参数  每页大小
     * @date 2022/9/5 15:14
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/page", method = {RequestMethod.POST})
    ApiResultSet<PageResult> queryComProWithPage(@RequestParam(value = "serviceOid", required = false) String serviceOid,
                                                   @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * 根据事项oid查询事项常见问题
     * @param serviceOid 事项oid
     * @date 2022/9/5 15:14
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryComPro", method = {RequestMethod.GET})
    ApiResultSet<List<HaServiceCommonProblemVo>> queryComPro(@RequestParam(value = "serviceOid", required = false) String serviceOid);


    /**
     * 根据id删除事项常见问题
     * @param id 事项常见问题id
     * @date 2022/9/5 15:14
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteById", method = {RequestMethod.GET})
    ApiResultSet<HaUserResource> deleteById(@RequestParam("id") String id);

    /**
     * 保存事项常见问题
     * @param haServiceCommonProblem 事项常见问题
     * @date 2022/9/5 15:14
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveServiceComPro",method = {RequestMethod.POST})
    ApiResultSet saveServiceComPro(@RequestBody HaServiceCommonProblem haServiceCommonProblem);

    /**
     * @description 初始化选项常见问题
     * @param serviceOid 事项oid
     * @param id 问题id
     * @author zhaobf
     * @date 2022/9/6
     * @return
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/initCommonProblemInfo",method = {RequestMethod.GET})
    ApiResultSet<Map<String, Object>> initCommonProblemInfo(
            @RequestParam(value = "serviceOid", required = false) String serviceOid,
            @RequestParam(value = "id", required = false) String id);
}
