package com.zfsoft.microservice.workflow.controller;

import com.zfsoft.microservice.workflow.manager.ProcessRuntimeManager;
import com.zfsoft.microservice.workflow.manager.RepositoryManager;
import com.zfsoft.microservice.workflow.service.WorkflowService;
import com.zfsoft.platform.common.data.ApiResultSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

/**
 * @ClassName WorkflowController
 * @Description 工作流接口管理的实现类
 * @Author wuxx
 * @Date 2021-01-13 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class WorkflowController implements WorkflowService {

    @Autowired
    private RepositoryManager repositoryManager;

    @Autowired
    private ProcessRuntimeManager processRuntimeManager;

    /**
     * @description: 通过xml字符串的方式部署流程
     * @param deploymentName
     * @param deployXml
     * @author: wuxx
     * @Date: 2021/1/13 9:15
     **/
    @Override
    public ApiResultSet<String> initDeploymentBPMN(String deploymentName, String deployXml) {
        String deploymentJson = repositoryManager.initDeploymentBPMN(deploymentName, deployXml);
        return new ApiResultSet<>(deploymentJson);
    }

    /**
     * @description: 通过文件流的方式部署流程
     * @param deploymentName 部署流程名称
     * @param fileInputStream 文件流
     * @author: wuxx
     * @Date: 2021/1/13 9:15
     **/
    @Override
    public ApiResultSet<String> initDeploymentBPMN(String deploymentName, InputStream fileInputStream) {
        String deploymentJson = repositoryManager.initDeploymentBPMN(deploymentName, fileInputStream);
        return new ApiResultSet<>(deploymentJson);
    }

    /**
     * @description: 查看所有的部署流程
     * @author: wuxx
     * @Date: 2021/1/13 10:48
     **/
    @Override
    public ApiResultSet<String> queryAllDeployments() {
        String deploymentJson = repositoryManager.queryAllDeployments();
        return new ApiResultSet<>(deploymentJson);
    }

    /**
     * @description:  删除部署的流程
     * @param deploymentId 部署流程id
     * @author: wuxx
     * @Date: 2021/1/13 11:38
     **/
    @Override
    public ApiResultSet<Boolean> deleteDeployment(String deploymentId) {
        Boolean flag = repositoryManager.deleteDeployment(deploymentId);
        return new ApiResultSet<>(flag);
    }

    /**
     * @description: 查看所有的流程定义
     * @author: wuxx
     * @Date: 2021/1/13 10:48
     **/
    @Override
    public ApiResultSet<String> queryAllProcessDefinitions() {
        String deploymentJson = repositoryManager.queryAllProcessDefinitions();
        return new ApiResultSet<>(deploymentJson);
    }
    /**
     * @description: 查看所有的流程实列
     * @author: wuxx
     * @Date: 2021/1/13 10:48
     **/
    @Override
    public ApiResultSet<String> queryAllProcessInstances(Integer pageNumber, Integer pageSize) {
        return new ApiResultSet<>(processRuntimeManager.queryAllProcessInstances(pageNumber,pageSize));
    }

}
