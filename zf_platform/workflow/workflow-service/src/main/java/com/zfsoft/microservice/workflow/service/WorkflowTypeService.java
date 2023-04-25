package com.zfsoft.microservice.workflow.service;

import com.zfsoft.microservice.workflow.data.WorkflowType;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName WorkflowTypeService
 * @Description 流程类型组件服务定义接口
 * @Author wuxx
 * @Date 2021-01-22 14:33
 * @Version V1.0
 **/
@RequestMapping("/security/workflow/type")
public interface WorkflowTypeService {
    /**
     * 增加一个新流程类型
     *
     * @param workflowType 新流程类型
     * @return
     */
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    ApiResultSet<WorkflowType> saveWorkflowType(@RequestBody WorkflowType workflowType);

    /**
     * 删除指定Id的流程类型信息
     *
     * @param oid 流程类型id
     * @return
     */
    @RequestMapping(value = "/delete/{oid}", method = {RequestMethod.DELETE})
    ApiResultSet<Integer> deleteWorkflowTypeById(@PathVariable("oid") Long oid);

    /**
     * 启禁用指定Id的流程类型信息
     *
     * @param oid 流程类型id
     * @return
     */
    @RequestMapping(value = "/able/{oid}", method = {RequestMethod.POST})
    ApiResultSet<Integer> ableWorkflowTypeById(@PathVariable("oid") Long oid);

    /**
     * 根据流程类型Id获取流程类型信息
     *
     * @param oid 流程类型Id
     * @return
     */
    @RequestMapping(value = "/getOne/{oid}", method = {RequestMethod.GET})
    ApiResultSet<WorkflowType> getWorkflowTypeById(@PathVariable("oid") Long oid);

    /**
     * 查询流程类型信息
     * @param appOid     应用主键
     * @param code     流程类型编码
     * @param name     流程名称
     * @param pageNum 开始页
     * @param pageSize   每页大小
     * @return
     */
    @RequestMapping(value = "/page", method = {RequestMethod.GET})
    ApiResultSet queryWorkflowTypeWithPage(@RequestParam(value = "appOid", required = false) String appOid,
                                           @RequestParam(value = "code", required = false) String code,
                                           @RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                     @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * @description:  查询流程类型列表
     * @param appOid  应用主键
     * @author: wuxx
     * @Date: 2021/1/22 15:01
     **/
    @GetMapping("/list")
    ApiResultSet queryWorkflowTypeList(@RequestParam(value = "appOid", required = false) String appOid);

}
