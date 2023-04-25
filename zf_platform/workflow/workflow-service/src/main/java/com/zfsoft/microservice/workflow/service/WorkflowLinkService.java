package com.zfsoft.microservice.workflow.service;

import com.zfsoft.microservice.workflow.data.WorkflowLink;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName WorkflowLinkService
 * @Description 流程类型操作组件服务定义接口
 * @Author wuxx
 * @Date 2021-1-22 14:33
 * @Version V1.0
 **/
@RequestMapping("/security/workflow/link")
public interface WorkflowLinkService {
    /**
     * 增加一个新流程类型操作
     *
     * @param workflowLink 新流程类型操作
     * @return
     */
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    ApiResultSet<WorkflowLink> saveWorkflowLink(@RequestBody WorkflowLink workflowLink);

    /**
     * 删除指定Id的流程类型操作信息
     *
     * @param oid 流程类型操作id
     * @return
     */
    @RequestMapping(value = "/delete/{oid}", method = {RequestMethod.DELETE})
    ApiResultSet<Integer> deleteWorkflowLinkById(@PathVariable("oid") Long oid);

    /**
     * 启禁用指定Id的流程类型操作信息
     *
     * @param oid 流程类型操作id
     * @return
     */
    @RequestMapping(value = "/able/{oid}", method = {RequestMethod.POST})
    ApiResultSet<Integer> ableWorkflowLinkById(@PathVariable("oid") Long oid);

    /**
     * 根据流程类型操作Id获取流程类型操作信息
     *
     * @param oid 流程类型操作Id
     * @return
     */
    @RequestMapping(value = "/getOne/{oid}", method = {RequestMethod.GET})
    ApiResultSet<WorkflowLink> getWorkflowLinkById(@PathVariable("oid") Long oid);

    /**
     * 查询流程类型操作信息
     * @param typeOid   流程类型主键
     * @param code     流程类型编码
     * @param name     流程类型操作
     * @param pageNum 开始页
     * @param pageSize   每页大小
     * @return
     */
    @RequestMapping(value = "/page", method = {RequestMethod.GET})
    ApiResultSet queryWorkflowLinkWithPage(@RequestParam(value = "typeOid", required = false) String typeOid,
                                           @RequestParam(value = "code", required = false) String code,
                                           @RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                     @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * @description:  查询流程类型操作列表
     * @param typeOid 流程类型主键
     * @author: wuxx
     * @Date: 2021/1/22 15:08
     **/
    @GetMapping("/list")
    ApiResultSet queryWorkflowLinkList(@RequestParam(value = "typeOid", required = false) String typeOid);

}
