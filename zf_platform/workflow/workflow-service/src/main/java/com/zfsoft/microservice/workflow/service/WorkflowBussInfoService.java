package com.zfsoft.microservice.workflow.service;

import com.zfsoft.microservice.workflow.data.WorkflowBussInfo;
import com.zfsoft.microservice.workflow.data.WorkflowLink;
import com.zfsoft.microservice.workflow.data.dto.WorkflowBussInfoQueryDto;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author: kkfan
 * @create: 2021-01-25 19:01:14
 * @description: 流程信息管理
 */
@RequestMapping("/security/workflow/bussInfo")
public interface WorkflowBussInfoService {

    /**
     * 增加一个新流程类型操作
     *
     * @param workflowBussInfo 新流程类型操作
     * @return
     */
    @PostMapping(value = "/saveOrUpdate")
    ApiResultSet<WorkflowBussInfo> saveOrUpdateWorkflowBussInfo(@RequestBody WorkflowBussInfo workflowBussInfo) throws UnsupportedEncodingException;

    /**
     * 删除指定Id的流程类型操作信息
     *
     * @param oids 流程类型操作id
     * @return
     */
    @DeleteMapping(value = "/delete/{oids}")
    ApiResultSet<Integer> deleteWorkflowBussInfoByIds(@PathVariable("oids") String oids);


    /**
     * 根据流程类型操作Id获取流程类型操作信息
     * @param oid 流程Id
     * @return
     */
    @GetMapping(value = "/getOne/{oid}")
    ApiResultSet<WorkflowBussInfo> getWorkflowBussInfoById(@PathVariable("oid") Long oid);

    /**
     * 根据流程类型操作业务Id获取流程类型操作信息
     * @param infoOid 流程业务Id
     * @return
     */
    @GetMapping(value = "/getWorkflowBussInfoByInfoOid/{infoOid}")
    ApiResultSet<WorkflowBussInfo> getWorkflowBussInfoByInfoOid(@PathVariable("infoOid") String infoOid);

    /**
     * 查询流程类型操作信息
     * @param queryDto 查询实体
     * @return
     */
    @PostMapping(value = "/page")
    ApiResultSet queryWorkflowBussInfoWithPage(@RequestBody @Validated(WorkflowBussInfo.PAGE_QUERY.class) WorkflowBussInfoQueryDto queryDto);

    /**
     * 流程发布
     * @param oid 流程id
     * @return
     */
    @GetMapping(value = "/deploy")
    ApiResultSet<WorkflowBussInfo> deployByOid(@RequestParam(value = "oid", required = false) Long oid,@RequestParam(value = "modelId", required = false) String modelId) throws Exception;

    /**
     * 通过流程类型查找流程列表
     * @param typeOid   流程类型id
     * @param workflowName 流程名称
     * @param pageNum 流程名称
     * @param pageSize 流程名称
     * @return
     */
    @GetMapping(value = "/queryWorkflowBussInfoByTypeOid")
    ApiResultSet queryWorkflowBussInfoByTypeOid(@RequestParam(name = "typeOid") String typeOid, @RequestParam(name = "workflowName", required = false) String workflowName, @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize);

    /**
     * 流程复制
     * @param infoOid   源流程信息业务主键
     * @param newInfoOid   目标流程信息业务主键
     * @return
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/workflowCopy")
    ApiResultSet workflowCopy(@RequestParam("infoOid") String infoOid, @RequestParam("newInfoOid") String newInfoOid) throws Exception;

    /**
     * @description: 查询所有区划的组织机构、流程信息Tree
     * @author: wuxx
     * @Date: 2021/2/2 10:40
     **/
    @RequestMapping( value = "/queryAllDistrictOrganBussInfoTree",method = {RequestMethod.GET})
    ApiResultSet queryAllDistrictOrganBussInfoTree();

    /**
     * @description:  根据名称、区划、组织机构查询流程的信息列表
     * @param workflowName 流程名称
     * @param districtOid 区划oid
     * @param organOid 组织机构oid
     * @param typeOid 所属类型oid
     * @author: wuxx
     * @Date: 2022/2/2 10:14
     **/
    @RequestMapping( value = "/list",method = {RequestMethod.GET})
    ApiResultSet<List<WorkflowBussInfo>> queryWorkflowBussInfoWithParams(@RequestParam(value="workflowName",required=false)String workflowName,
                                          @RequestParam(value="districtOid",required=false)String districtOid,
                                          @RequestParam(value="organOid",required=false)String organOid,
                                          @RequestParam(value="typeOid",required=false)String typeOid);

    /**
     * @description: 根据infoOid查询流程类型环节集合
     * @param infoOid 流程信息主键
     * @author: wuxx
     * @Date: 2021/2/3 10:53
     **/
    @GetMapping(value = "/queryWorkflowLinkListByInfoOid")
    ApiResultSet<List<WorkflowLink>> queryWorkflowLinkListByInfoOid(@RequestParam("infoOid") String infoOid);

    /**
     * @description: 根据用户oids获取用户名称集合
     * @param oids 用户主键
     * @author: wuxx
     * @Date: 2021/2/3 10:53
     **/
    @GetMapping(value = "/getUserNames")
    ApiResultSet<String> getUserNames(@RequestParam("oids") String oids);

    /**
     * @description: 根据用户oids获取名称集合
     * @param oids 用户主键
     * @author: wuxx
     * @Date: 2022/3/14 10:53
     **/
    @GetMapping(value = "/getRoleAndOrganNames")
    ApiResultSet<String> getRoleAndOrganNames(@RequestParam("oids") String oids,@RequestParam("type") int type);

}
