package com.zfsoft.microservice.workflow.controller;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.microservice.platform.data.sys.SysRole;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.microservice.workflow.data.WorkflowBussInfo;
import com.zfsoft.microservice.workflow.data.WorkflowLink;
import com.zfsoft.microservice.workflow.data.dto.WorkflowBussInfoQueryDto;
import com.zfsoft.microservice.workflow.feign.SysOrganFeignService;
import com.zfsoft.microservice.workflow.feign.SysRoleFeignService;
import com.zfsoft.microservice.workflow.feign.SysUserFeignService;
import com.zfsoft.microservice.workflow.manager.WorkflowBussInfoManager;
import com.zfsoft.microservice.workflow.service.WorkflowBussInfoService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.web.TreeSelect;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: kkfan
 * @create: 2021-01-25 19:01:50
 * @description: 流程信息管理
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkflowBussInfoController implements WorkflowBussInfoService {

    private final WorkflowBussInfoManager workflowBussInfoManager;

    private final SysUserFeignService sysUserFeignService;

    private final SysRoleFeignService sysRoleFeignService;

    private final SysOrganFeignService sysOrganFeignService;

    @Override
    public ApiResultSet saveOrUpdateWorkflowBussInfo(WorkflowBussInfo workflowBussInfo) throws UnsupportedEncodingException {
        this.workflowBussInfoManager.saveOrUpdateWorkflowBussInfo(workflowBussInfo);
        return new ApiResultSet<>();
    }

    @Override
    public ApiResultSet deleteWorkflowBussInfoByIds(String oids) {
        this.workflowBussInfoManager.deleteWorkflowBussInfoByIds(oids);
        return new ApiResultSet<>();
    }

    @Override
    public ApiResultSet<WorkflowBussInfo> getWorkflowBussInfoById(Long oid) {
        WorkflowBussInfo workflowBussInfo = this.workflowBussInfoManager.getWorkflowBussInfoById(oid);
        return new ApiResultSet<>(workflowBussInfo);
    }

    @Override
    public ApiResultSet<WorkflowBussInfo> getWorkflowBussInfoByInfoOid(String infoOid) {
        WorkflowBussInfo workflowBussInfo = workflowBussInfoManager.getWorkflowBussInfoByInfoOid(infoOid);
        return new ApiResultSet<>(workflowBussInfo);
    }

    @Override
    public ApiResultSet queryWorkflowBussInfoWithPage(WorkflowBussInfoQueryDto queryDto) {
        if(StrUtil.isEmpty(queryDto.getDistrictOid())){
            queryDto.setDistrictOid(CurrentLoginUserHolder.getCurrentLoginUser().getDistrictOid());
        }
        List<WorkflowBussInfo> workflowBussInfoList = this.workflowBussInfoManager.queryWorkflowBussInfoWithPage(queryDto);
        PageResult<WorkflowBussInfo> pageResult = new PageResult<>(((Page) workflowBussInfoList).getPageNum(), ((Page) workflowBussInfoList).getPageSize(), ((Page) workflowBussInfoList).getTotal());
        pageResult.setData(workflowBussInfoList);
        return new ApiResultSet(pageResult);
    }

    @Override
    public ApiResultSet<WorkflowBussInfo> deployByOid(Long oid, String modelId) throws Exception {
        this.workflowBussInfoManager.deployByOid(oid, modelId);
        return new ApiResultSet<>();
    }

    @Override
    public ApiResultSet queryWorkflowBussInfoByTypeOid(String typeOid, String workflowName, Integer pageNum, Integer pageSize) {
        List<WorkflowBussInfo> workflowBussInfoList = this.workflowBussInfoManager.queryWorkflowBussInfoByTypeOid(typeOid, workflowName, pageNum, pageSize);
        PageResult<WorkflowBussInfo> pageResult = new PageResult<>(((Page) workflowBussInfoList).getPageNum(), ((Page) workflowBussInfoList).getPageSize(), ((Page) workflowBussInfoList).getTotal());
        pageResult.setData(workflowBussInfoList);
        return new ApiResultSet(pageResult);
    }

    @Override
    public ApiResultSet workflowCopy(String infoOid, String newInfoOid) throws Exception {
        this.workflowBussInfoManager.workflowCopy(infoOid, newInfoOid);
        return new ApiResultSet();
    }

    @Override
    public ApiResultSet<List<TreeSelect>> queryAllDistrictOrganBussInfoTree() {
        List<TreeSelect> treeSelectList = workflowBussInfoManager.queryAllDistrictOrganBussInfoTree();
        return new ApiResultSet<>(treeSelectList);
    }

    @Override
    public ApiResultSet<List<WorkflowBussInfo>> queryWorkflowBussInfoWithParams(String workflowName, String districtOid, String organOid, String typeOid) {
        List<WorkflowBussInfo> workflowBussInfoList = workflowBussInfoManager.queryWorkflowBussInfoWithParams(workflowName, districtOid, organOid, typeOid);
        return new ApiResultSet<>(workflowBussInfoList);
    }

    /**
     * @description: 根据infoOid查询流程类型环节集合
     * @param infoOid 流程信息主键
     * @author: wuxx
     * @Date: 2021/2/3 10:53
     **/
    @Override
    public ApiResultSet<List<WorkflowLink>> queryWorkflowLinkListByInfoOid(String infoOid) {
        List<WorkflowLink> workflowLinkList = workflowBussInfoManager.queryWorkflowLinkListByInfoOid(infoOid);
        return new ApiResultSet<>(workflowLinkList);
    }

    @Override
    public ApiResultSet<String> getUserNames(String oids) {
        String[] splitOids = null;
        if(oids.contains("~")){
            splitOids= oids.split("~");
        }else {
            splitOids= oids.split(",");
        }
        String usernames = "";
        for (String userOid : splitOids){
            userOid = userOid.replaceAll("USER-","");
            SysUser sysUser = sysUserFeignService.getSysUserByUserOid(userOid).getData();
            String name = null!=sysUser?sysUser.getName():"";
            usernames = usernames + name +",";
        }
        if(StrUtil.isNotBlank(usernames)){
            usernames = usernames.substring(0,usernames.length()-1);
        }
        return new ApiResultSet<>(usernames);
    }

    @Override
    public ApiResultSet<String> getRoleAndOrganNames(String oids,int type) {
        String[] splitOids = null;
        if(oids.contains("~")){
            splitOids= oids.split("~");
        }else {
            splitOids= oids.split(",");
        }
        List<String> namesList = new ArrayList<>();
        //角色
        if(1 == type){
            for (String oid : splitOids){
                oid = oid.replaceAll("ROLE-","");
                SysRole data = sysRoleFeignService.getSysRoleByRoleOid(oid).getData();
                String name = null!=data?data.getName():"";
                namesList.add(name);
            }
        }
        //机构
        if(2 == type){
            for (String oid : splitOids){
                oid = oid.replaceAll("ORGAN-","");
                SysOrgan data = sysOrganFeignService.getSysOrganByOrganOid(oid).getData();
                String name = null!=data?data.getName():"";
                namesList.add(name);
            }
        }

        String names = String.join(",",namesList);
        return new ApiResultSet<>(names);
    }
}
