package com.zfsoft.microservice.workflow.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.InputStream;
import java.util.Map;

/**
 * @description: 工作流的对外接口服务
 * @author: wuxx
 * @Date: 2021/1/13 9:05
 **/
@RequestMapping("/security/workflow")
public interface WorkflowService {

    /**
     * @description: 通过xml字符串的方式部署流程
     * @param deploymentName
     * @param deployXml
     * @author: wuxx
     * @Date: 2021/1/13 9:15
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/initDeploymentBPMNByXML",method = {RequestMethod.POST})
    ApiResultSet<String> initDeploymentBPMN(@RequestParam("deploymentName") String deploymentName,
                                            @RequestParam("deployXml") String deployXml);

    /**
     * @description: 通过文件流的方式部署流程
     * @param deploymentName 部署流程名称
     * @param fileInputStream 文件流
     * @author: wuxx
     * @Date: 2021/1/13 9:15
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/initDeploymentBPMNByInputStream",method = {RequestMethod.POST})
    ApiResultSet<String> initDeploymentBPMN(@RequestParam("deploymentName") String deploymentName,
                                     @RequestParam("fileInputStream") InputStream fileInputStream);

    /**
     * @description: 查看所有的部署流程
     * @author: wuxx
     * @Date: 2021/1/13 10:48
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryAllDeployments",method = {RequestMethod.GET})
    ApiResultSet<String> queryAllDeployments();

    /**
     * @description:  删除部署的流程
     * @param deploymentId 部署流程id
     * @author: wuxx
     * @Date: 2021/1/13 11:38
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/deleteDeployment",method = {RequestMethod.GET})
    ApiResultSet<Boolean> deleteDeployment(@RequestParam("deploymentId") String deploymentId);

    /**
     * @description: 查看所有的流程定义
     * @author: wuxx
     * @Date: 2021/1/13 10:48
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryAllProcessDefinitions",method = {RequestMethod.GET})
    ApiResultSet<String> queryAllProcessDefinitions();


    /**
     * @description: 查看所有的流程实列,返回JSON 集合
     * @author: wuxx
     * @Date: 2021/1/13 10:48
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryAllProcessInstances",method = {RequestMethod.GET})
    ApiResultSet<String> queryAllProcessInstances(@RequestParam(value="pageNum",required = false)Integer pageNum,
                                                  @RequestParam(value="pageSize",required = false)Integer pageSize);

}
