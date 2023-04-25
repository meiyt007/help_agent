package com.zfsoft.microservice.workflow.controller;

import com.zfsoft.microservice.workflow.data.ProAcceptExample;
import com.zfsoft.microservice.workflow.data.ProProcessExample;
import com.zfsoft.microservice.workflow.data.WorkflowBussFlowStep;
import com.zfsoft.microservice.workflow.manager.ProAcceptExampleManager;
import com.zfsoft.microservice.workflow.service.ProAcceptExampleService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
@RestController
public class ProAcceptExampleController implements ProAcceptExampleService {

    @Autowired
    private ProAcceptExampleManager proAcceptExampleManager;

    @Override
    public ApiResultSet<ProAcceptExample> saveProAcceptExample(ProAcceptExample proAcceptExample) {
        proAcceptExampleManager.save(proAcceptExample);
        return new ApiResultSet<>();
    }

    @Override
    public ApiResultSet<PageResult> page(String name, Integer pageNum, Integer pageSize) {
        PageResult page = proAcceptExampleManager.page(name, pageNum, pageSize);
        return new ApiResultSet<>(page);
    }

    @Override
    public ApiResultSet<PageResult> completedTasksPage(String name, Integer pageNum, Integer pageSize) {
        PageResult page = proAcceptExampleManager.completedTasksPage(name, pageNum, pageSize);
        return new ApiResultSet<>(page);
    }

    @Override
    public ApiResultSet<String> next(ProProcessExample proProcessExample) {
        if(null!=proProcessExample){
            //通过
            if(proProcessExample.getEventName().startsWith("通过")){
                return proAcceptExampleManager.nextStep(proProcessExample);
            }else if(proProcessExample.getEventName().startsWith("不通过")){
                return new ApiResultSet<>(proAcceptExampleManager.noPassProcessInstance(proProcessExample.getProcessInstanceId(),proProcessExample.getTaskId()));
            }else if(proProcessExample.getEventName().startsWith("驳回")){
                String rejectTaskId = proProcessExample.getEventName().split("~")[1];
                proProcessExample.setRejectTaskId(rejectTaskId);
                return proAcceptExampleManager.rejectTaskStep(proProcessExample);
            }else if(proProcessExample.getEventName().startsWith("退回")){
                String fallbackTaskId = proProcessExample.getEventName().split("~")[1];
                proProcessExample.setFallbackTaskId(fallbackTaskId);
                return proAcceptExampleManager.fallbackTaskStep(proProcessExample);
            }
        }
        return null;
    }

    @Override
    public ApiResultSet getTaskCondition(String taskId) {
         Map<String, Object> map = proAcceptExampleManager.getTaskCondition(taskId);
         return new ApiResultSet<>(map);
    }

}
