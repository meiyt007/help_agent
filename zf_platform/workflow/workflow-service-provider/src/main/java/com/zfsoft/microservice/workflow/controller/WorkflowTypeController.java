package com.zfsoft.microservice.workflow.controller;

import com.zfsoft.microservice.workflow.data.WorkflowType;
import com.zfsoft.microservice.workflow.manager.WorkflowTypeManager;
import com.zfsoft.microservice.workflow.service.WorkflowTypeService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName WorkflowTypeController
 * @Description 流程类型管理的实现类
 * @Author wuxx
 * @Date 2020-09-12 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class WorkflowTypeController implements WorkflowTypeService {

    @Resource
    private WorkflowTypeManager workflowTypeManager;

    /**
     * @param oid 流程类型主键
     * @description: 初始化流程类型
     * @author: wuxx
     * @Date: 2021/01/22 15:14
     **/
    @RequestMapping(value = {"/init/{oid}"}, method = {RequestMethod.GET})
    public ApiResultSet<WorkflowType> initWorkflowType(@PathVariable(value="oid",required=false) Long oid) {
        WorkflowType workflowType = workflowTypeManager.getWorkflowTypeById(oid);
        return new ApiResultSet<>(workflowType);
    }

    /**
     * @param workflowType 流程类型实体类
     * @description: 流程类型的新增或者修改
     * @author: wuxx
     * @Date: 2021/01/22 15:14
     **/
    @Override
    public ApiResultSet<WorkflowType> saveWorkflowType(WorkflowType workflowType) {
        workflowTypeManager.saveWorkflowType(workflowType);
        ApiResultSet<WorkflowType> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(workflowType);
        return apiResultSet;
    }

    /**
     * @param oid 流程类型实体类主键
     * @description: 流程类型的信息的删除
     * @author: wuxx
     * @Date: 2021/01/22 15:14
     **/
    @Override
    public ApiResultSet<Integer> deleteWorkflowTypeById(Long oid) {
        int rows = workflowTypeManager.deleteWorkflowTypeById(oid);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<Integer>();
        if (BaseStaticParameter.Y == rows){
            return apiResultSet;
        }else {
            apiResultSet.setCode(ApiResultSet.DIRTY_DATA_TITLE);
            apiResultSet.setMessage("删除失败！");
            return apiResultSet;
        }
    }

    /**
     * @param oid 流程类型实体类主键
     * @description: 获取流程类型的信息
     * @author: wuxx
     * @Date: 2021/01/22 15:14
     **/
    @Override
    public ApiResultSet<WorkflowType> getWorkflowTypeById(Long oid) {
        WorkflowType workflowType = workflowTypeManager.getWorkflowTypeById(oid);
        ApiResultSet<WorkflowType> apiResultSet = new ApiResultSet<WorkflowType>();
        apiResultSet.setData(workflowType);
        return apiResultSet;
    }

    /**
     * 查询流程类型信息
     * @param appOid     应用主键
     * @param code     流程类型编码
     * @param name     流程名称
     * @param pageNum 开始页
     * @param pageSize   每页大小
     * @return
     */
    @Override
    public ApiResultSet queryWorkflowTypeWithPage(String appOid,String code,String name, Integer pageNum,Integer pageSize) {
        WorkflowType workflowType = new WorkflowType();
        workflowType.setName(name);
        workflowType.setAppOid(appOid);
        workflowType.setCode(code);
        PageResult<WorkflowType> pageResult = workflowTypeManager.queryWorkflowTypeWithPage(workflowType, pageNum, pageSize);
        return new ApiResultSet<>(pageResult);
    }

    /**
     * @param oid 流程类型实体类主键
     * @description: 流程类型的信息的启禁用
     * @author: wuxx
     * @Date: 2021/01/22 15:14
     **/
    @Override
    public ApiResultSet<Integer> ableWorkflowTypeById(@PathVariable("oid") Long oid) {
        int rows = workflowTypeManager.ableWorkflowTypeById(oid);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<Integer>();
        if (BaseStaticParameter.Y == rows){
            return apiResultSet;
        }else {
            apiResultSet.setCode(ApiResultSet.DIRTY_DATA_TITLE);
            apiResultSet.setMessage("禁用失败！");
            return apiResultSet;
        }
    }

    /**
     * @description:  查询流程类型列表
     * @param appOid  应用主键
     * @author: wuxx
     * @Date: 2021/1/22 15:01
     **/
    @Override
    public ApiResultSet queryWorkflowTypeList(String appOid) {
        WorkflowType workflowType = new WorkflowType();
        workflowType.setAppOid(appOid);
        List<WorkflowType> workflowTypes = workflowTypeManager.queryWorkflowType(workflowType);
        return new ApiResultSet<>(workflowTypes);
    }
}
