package com.zfsoft.microservice.workflow.listener;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.microservice.workflow.data.WorkflowBussFlowStep;
import com.zfsoft.microservice.workflow.feign.SysUserFeignService;
import com.zfsoft.microservice.workflow.manager.WorkflowBussFlowStepManager;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.utils.spring.SpringContextHelper;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ClassName UserSignListener
 * @Description: 处理用户全部办理的监听（类似于会签）
 * @Author wuxx
 * @Date 2021/1/27
 **/
@Service
public class UserSignListener  implements  TaskListener, ExecutionListener {

    @Override
    public void notify(DelegateTask delegateTask) {
      //任务环节

    }

    @Override
    public void notify(DelegateExecution execution) {
        String currentActivityId = execution.getCurrentActivityId();
        String processDefinitionId = execution.getProcessDefinitionId();
        WorkflowBussFlowStepManager workflowBussFlowStepManager = SpringContextHelper.getBean(WorkflowBussFlowStepManager.class);
        WorkflowBussFlowStep step = workflowBussFlowStepManager.getWorkflowBussFlowStepByActivityIdAndDefId(currentActivityId,processDefinitionId);
        //任意人审核 按岗位
        if(null!=step && BaseStaticParameter.STR_ONE.equals(step.getHandleType())){
            if(StrUtil.isNotEmpty(step.getHandlePostOids())){//按岗位
                //选择岗位
                SysUserFeignService sysUserFeignService = SpringContextHelper.getBean(SysUserFeignService.class);
                List<SysUser> userList = sysUserFeignService.getSysUserListByPostOids(step.getHandlePostOids()).getData();
                Set<String> userOidList = new HashSet<>();
                for(SysUser user:userList){
                    userOidList.add(user.getUserOid());
                }
                String handleUserOids = String.join(",", userOidList);
                execution.setVariable("handleUserOids",handleUserOids);
            }
        }else if(null!=step && BaseStaticParameter.STR_TWO.equals(step.getHandleType())){//全部任意审核 按岗位
           if(StrUtil.isNotEmpty(step.getHandlePostOids())){//按岗位
                //选择岗位
                SysUserFeignService sysUserFeignService = SpringContextHelper.getBean(SysUserFeignService.class);
                List<SysUser> userList = sysUserFeignService.getSysUserListByPostOids(step.getHandlePostOids()).getData();
                Set<String> userOidList = new HashSet<>();
                for(SysUser user:userList){
                    userOidList.add(user.getUserOid());
                }
                execution.setVariable("handleUserOids",userOidList);
            }
        }

    }
}
