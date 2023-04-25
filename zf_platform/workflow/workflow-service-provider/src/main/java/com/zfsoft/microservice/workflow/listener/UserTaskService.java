package com.zfsoft.microservice.workflow.listener;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.microservice.workflow.data.WorkflowBussFlowStep;
import com.zfsoft.microservice.workflow.feign.SysUserFeignService;
import com.zfsoft.microservice.workflow.manager.WorkflowBussFlowStepManager;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description: 流程图设置，用户岗位、角色、组织机构设置审核人， UserPostTaskService作废
 * @author: wuxx
 * @Date: 2022/3/14 10:34
 **/
@Component
public class UserTaskService {

    @Autowired
    private WorkflowBussFlowStepManager workflowBussFlowStepManager;

    @Autowired
    private SysUserFeignService sysUserFeignService ;

    /**
     * @description: 根据岗位oids获取用户信息
     * @param postOids 岗位oids
     * @author: wuxx
     * @Date: 2021/2/2 10:40
     **/
    public Object getUsersByPostOids(String handleType,String postOids) {
        if(StrUtil.isNotEmpty(postOids)){
            //选择岗位
            try {
                List<SysUser> userList = sysUserFeignService.getSysUserListByPostOids(postOids).getData();
                Set<String> userOidList = new HashSet<>();
                for(SysUser user:userList){
                    userOidList.add(user.getUserOid());
                }
                //环节办理类型 -全部人办理
                if(StrUtil.isNotEmpty(handleType) && BaseStaticParameter.STR_TWO.equals(handleType)){
                    return userOidList;
                }else {
                    //环节办理类型 -任意人办理
                    String handleUserOids = String.join(",", userOidList);
                    return handleUserOids;
                }
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }

        }
        return null;
    }

    /**
     * @description: 根据环节oid获取用户信息
     * @param stepOid 环节oids
     * @author: wuxx
     * @Date: 2021/2/5 10:40
     **/
    public Object getUsersByStepOid(String handleType,String stepOid) {
        if(StrUtil.isNotEmpty(stepOid)){
            //选择岗位
            try {
                Set<String> userOidList = new HashSet<>();
                WorkflowBussFlowStep step = workflowBussFlowStepManager.getWorkflowBussFlowStepByStepOid(stepOid);
                //先查询用户
                String handleUserOids = step.getHandleUserOids();
                if(StrUtil.isNotEmpty(handleUserOids)){
                    List<String> listUserOids= Arrays.asList(handleUserOids.split(","));
                    userOidList.addAll(listUserOids);
                }
                //岗位
                String handlePostOids = step.getHandlePostOids();
                if(StrUtil.isNotEmpty(handlePostOids)){
                    List<SysUser> userList = sysUserFeignService.getSysUserListByPostOids(handlePostOids).getData();
                    if(null!=userList)
                    for(SysUser user:userList){
                        userOidList.add(user.getUserOid());
                    }
                }
                //角色
                String handleRoleOids = step.getHandleRoleOids();
                if(StrUtil.isNotEmpty(handleRoleOids)){
                    List<SysUser> userList = sysUserFeignService.getSysUserListByRoleOids(handleRoleOids).getData();
                    if(null!=userList)
                    for(SysUser user:userList){
                        userOidList.add(user.getUserOid());
                    }
                }
                //组织机构
                String handleOrganOids = step.getHandleOrganOids();
                if(StrUtil.isNotEmpty(handleOrganOids)){
                    List<SysUser> userList = sysUserFeignService.getSysUserListByOrganOids(handleOrganOids).getData();
                    if(null!=userList)
                    for(SysUser user:userList){
                        userOidList.add(user.getUserOid());
                    }
                }
                if(userOidList.size() == 0){
                    throw new ResultInfoException("下一个环节未查询到办理人员，请稍后再试！");
                }
                //环节办理类型 -全部人办理
                if(StrUtil.isNotEmpty(handleType) && BaseStaticParameter.STR_TWO.equals(handleType)){
                    return userOidList;
                }else {
                    //环节办理类型 -任意人办理
                    String handleUserOidsSet = String.join(",", userOidList);
                    return handleUserOidsSet;
                }

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
