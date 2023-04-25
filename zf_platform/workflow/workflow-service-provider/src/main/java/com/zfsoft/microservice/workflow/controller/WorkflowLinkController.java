package com.zfsoft.microservice.workflow.controller;

import com.zfsoft.microservice.workflow.data.WorkflowLink;
import com.zfsoft.microservice.workflow.manager.WorkflowLinkManager;
import com.zfsoft.microservice.workflow.service.WorkflowLinkService;
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
 * @ClassName WorkflowLinkController
 * @Description 流程类型管理的实现类
 * @Author wuxx
 * @Date 2020-09-12 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class WorkflowLinkController implements WorkflowLinkService {

    @Resource
    private WorkflowLinkManager workflowLinkManager;

    /**
     * @param oid 流程类型主键
     * @description: 初始化流程类型
     * @author: wuxx
     * @Date: 2021/01/22 15:14
     **/
    @RequestMapping(value = {"/init/{oid}"}, method = {RequestMethod.GET})
    public ApiResultSet<WorkflowLink> initWorkflowLink(@PathVariable(value="oid",required=false) Long oid) {
        WorkflowLink workflowLink = workflowLinkManager.getWorkflowLinkById(oid);
        return new ApiResultSet<>(workflowLink);
    }

    /**
     * @param workflowLink 流程类型实体类
     * @description: 流程类型的新增或者修改
     * @author: wuxx
     * @Date: 2021/01/22 15:14
     **/
    public ApiResultSet<WorkflowLink> saveWorkflowLink(WorkflowLink workflowLink) {
        workflowLinkManager.saveWorkflowLink(workflowLink);
        ApiResultSet<WorkflowLink> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(workflowLink);
        return apiResultSet;
    }

    /**
     * @param oid 流程类型实体类主键
     * @description: 流程类型的信息的删除
     * @author: wuxx
     * @Date: 2021/01/22 15:14
     **/
    @Override
    public ApiResultSet<Integer> deleteWorkflowLinkById(Long oid) {
        int rows = workflowLinkManager.deleteWorkflowLinkById(oid);
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
    public ApiResultSet<WorkflowLink> getWorkflowLinkById(Long oid) {
        WorkflowLink workflowLink = workflowLinkManager.getWorkflowLinkById(oid);
        ApiResultSet<WorkflowLink> apiResultSet = new ApiResultSet<WorkflowLink>();
        apiResultSet.setData(workflowLink);
        return apiResultSet;
    }

    /**
     * 查询流程类型信息
     * @param typeOid   流程类型主键
     * @param code     流程类型编码
     * @param name     流程名称
     * @param pageNum 开始页
     * @param pageSize   每页大小
     * @return
     */
    @RequestMapping(value = "/page", method = {RequestMethod.GET})
    public ApiResultSet queryWorkflowLinkWithPage(String typeOid,String code,String name, Integer pageNum,Integer pageSize) {
        WorkflowLink workflowLink = new WorkflowLink();
        workflowLink.setName(name);
        workflowLink.setTypeOid(typeOid);
        workflowLink.setCode(code);
        PageResult<WorkflowLink> pageResult = workflowLinkManager.queryWorkflowLinkWithPage(workflowLink, pageNum, pageSize);
        return new ApiResultSet<>(pageResult);
    }

    /**
     * @param oid 流程类型实体类主键
     * @description: 流程类型的信息的启禁用
     * @author: wuxx
     * @Date: 2021/01/22 15:14
     **/
    @Override
    public ApiResultSet<Integer> ableWorkflowLinkById(@PathVariable("oid") Long oid) {
        int rows = workflowLinkManager.ableWorkflowLinkById(oid);
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
     * @param typeOid 流程主键
     * @author: wuxx
     * @Date: 2021/1/22 15:01
     **/
    public ApiResultSet queryWorkflowLinkList(String typeOid) {
        WorkflowLink workflowLink = new WorkflowLink();
        workflowLink.setTypeOid(typeOid);
        List<WorkflowLink> workflowLinks = workflowLinkManager.queryWorkflowLink(workflowLink);
        return new ApiResultSet<>(workflowLinks);
    }
}
