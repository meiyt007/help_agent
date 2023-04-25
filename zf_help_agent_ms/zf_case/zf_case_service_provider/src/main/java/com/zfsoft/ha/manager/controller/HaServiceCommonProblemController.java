package com.zfsoft.ha.manager.controller;

import com.zfsoft.ha.data.HaServiceCommonProblem;
import com.zfsoft.ha.data.HaUserResource;
import com.zfsoft.ha.data.vo.HaServiceCommonProblemVo;
import com.zfsoft.ha.manager.HaServiceCommonProblemService;
import com.zfsoft.ha.managers.HaServiceCommonProblemManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/9/5 14:37
 */
@Slf4j
@RestController
public class HaServiceCommonProblemController implements HaServiceCommonProblemService {

    @Resource
    private HaServiceCommonProblemManager haServiceCommonProblemManager;

    /**
     * 根据事项oid分页查询事项常见问题
     * @param serviceOid 事项oid
     * @param pageNum 分页参数 页码
     * @param pageSize 分页参数  每页大小
     * @return
     */
    @Override
    public ApiResultSet<PageResult> queryComProWithPage(String serviceOid, Integer pageNum, Integer pageSize) {
        log.info("事项常见问题：进入事项常见问题分页查询controller查询参数 serviceOid: {}", serviceOid);
        HaServiceCommonProblem haServiceCommonProblem = new HaServiceCommonProblem();
        haServiceCommonProblem.setServiceOid(serviceOid);
        PageResult<HaServiceCommonProblem> pageResult = haServiceCommonProblemManager.queryInfoWithPage(haServiceCommonProblem, pageNum, pageSize);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }
    @Override
    public ApiResultSet<List<HaServiceCommonProblemVo>> queryComPro(String serviceOid) {
        log.info("事项常见问题：进入事项常见问题查询controller查询参数 serviceOid: {}", serviceOid);
        HaServiceCommonProblemVo haServiceCommonProblem = new HaServiceCommonProblemVo();
        haServiceCommonProblem.setServiceOid(serviceOid);
        List<HaServiceCommonProblemVo> listResult = haServiceCommonProblemManager.queryInfo(haServiceCommonProblem);
        ApiResultSet<List<HaServiceCommonProblemVo>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(listResult);
        return apiResultSet;
    }


    /**
     * 根据id删除事项常见问题
     * @param id 事项常见问题id
     * @return
     */
    @Override
    public ApiResultSet<HaUserResource> deleteById(String id) {
        log.info("事项常见问题：进入事项常见问题根据id删除查询参数 id: {}", id);
        HaServiceCommonProblem result = null;
        result = haServiceCommonProblemManager.deleteCommonProblemById(id);
        return ApiResultSet.ok("事项常见问题：根据id删除事项常见问题",result);
    }

    /**
     * 保存事项常见问题
     * @param haServiceCommonProblem 事项常见问题
     * @return
     */
    @Override
    public ApiResultSet saveServiceComPro(HaServiceCommonProblem haServiceCommonProblem) {
        log.info("事项常见问题：进入保存事项常见问题");
        String message = null;
        int index = haServiceCommonProblemManager.saveHaServiceCommonProblem(haServiceCommonProblem);
        if(index != 0){
            if(haServiceCommonProblem.getId()!=null){
                //说明有新增或修改
                message="修改成功";
            }else{
                message="新增成功";
            }
        }else{
            message="未更新表数据";
        }
        return ApiResultSet.ok("事项常见问题：保存事项常见问题成功"+message,null);
    }

    @Override
    public ApiResultSet<Map<String, Object>> initCommonProblemInfo(String serviceOid, String id) {
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        Map<String, Object> result = haServiceCommonProblemManager.initCommonProblemInfo(serviceOid, id);
        apiResultSet.setData(result);
        return apiResultSet;
    }

}
