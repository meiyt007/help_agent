package com.zfsoft.microservice.workflow.manager;

import cn.hutool.json.JSONUtil;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 工作流部署接口的实现类
 * @author: wuxx
 * @Date: 2021/1/13 9:16
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "workflow:repository")
public class RepositoryManager {

    @Autowired
    private RepositoryService repositoryService;
    /**
     * @description: 通过xml字符串的方式部署流程
     * @param deploymentName 部署流程名称
     * @param deployXml 部署流程XML
     * @author: wuxx
     * @Date: 2021/1/13 9:15
     **/
    @Transactional
    public String initDeploymentBPMN(@RequestParam("deploymentName") String deploymentName,
                                     @RequestParam("deployXml") String deployXml){
        try {
            /*String filename="BPMN/6.bpmn";
            Deployment deployment=repositoryService.createDeployment()
                    .addClasspathResource(filename)
                  .name(deploymentName+ ".bpmn")
                   .deploy();*/
            Deployment deployment = repositoryService.createDeployment().name(deploymentName)
                    .addString(deploymentName + ".bpmn", deployXml).deploy();
            ProcessDefinitionQuery processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId());
            String key = processDefinition.singleResult().getKey();
            return deploymentToJson(deployment,key);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResultInfoException("部署流程异常！",e);
        }
    }

    /**
     * @description: 通过文件流的方式部署流程
     * @param deploymentName 部署流程名称
     * @param fileInputStream 文件流
     * @author: wuxx
     * @Date: 2021/1/13 9:15
     **/
    public String initDeploymentBPMN(@RequestParam("deploymentName") String deploymentName,
                                     @RequestParam("fileInputStream") InputStream fileInputStream){
        try {
            Deployment deployment = repositoryService.createDeployment().name(deploymentName)
                    .addInputStream(deploymentName+ ".bpmn",fileInputStream).deploy();
            ProcessDefinitionQuery processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId());
            String key = processDefinition.singleResult().getKey();
            return deploymentToJson(deployment,key);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResultInfoException("部署流程异常！",e);
        }
    }

    /**
     * @description: 查看所有的部署流程
     * @author: wuxx
     * @Date: 2021/1/13 10:48
     **/
    public String queryAllDeployments(){
        try {
            List<Deployment> list = repositoryService.createDeploymentQuery().list();
            List<Map<String,Object>> deploymentsList = new ArrayList<>();
            for(Deployment deployment : list){
                Map<String,Object> reulstMap = new HashMap<>();
                reulstMap.put("deploymentId",deployment.getId());
                reulstMap.put("name",deployment.getName());
                reulstMap.put("key",deployment.getKey());
                reulstMap.put("deploymentTime",deployment.getDeploymentTime());
                reulstMap.put("version",deployment.getVersion());
                deploymentsList.add(reulstMap);
            }
            return JSONUtil.toJsonStr(deploymentsList);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResultInfoException("查看所有的部署流程异常！",e);
        }
    }

    /**
     * @description: 查询所有的流程定义
     * @author: wuxx
     * @Date: 2021/1/13 11:00
     **/
    public String queryAllProcessDefinitions(){
        try {
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                    .list();
            list.sort((y,x)->x.getVersion()-y.getVersion());
            List<Map<String,Object>> definitionsList = new ArrayList<>();
            for(ProcessDefinition definition : list){
                Map<String,Object> reulstMap = new HashMap<>();
                reulstMap.put("processDefinitionId",definition.getId());
                reulstMap.put("deploymentId",definition.getDeploymentId());
                reulstMap.put("name",definition.getName());
                reulstMap.put("key",definition.getKey());
                reulstMap.put("version",definition.getVersion());
                definitionsList.add(reulstMap);
            }
            return JSONUtil.toJsonStr(definitionsList);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResultInfoException("查询所有的流程定义异常！",e);
        }
    }

    /**
     * @description:  删除部署的流程
     * @param deploymentId 部署流程id
     * @author: wuxx
     * @Date: 2021/1/13 11:38
     **/
    public boolean deleteDeployment(String deploymentId){
        try {
            repositoryService.deleteDeployment(deploymentId, true);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            throw new ResultInfoException("查询所有的流程定义异常！",e);
        }
    }


    /**
     * @description: Deployment 转 json
     * @author: wuxx
     * @Date: 2021/1/13 10:12
     **/
    private String deploymentToJson(Deployment deployment,String key){
        if(null==deployment){
            throw new ResultInfoException("部署流程异常！");
        }
        Map<String,Object> reulstMap = new HashMap<>();
        reulstMap.put("id",deployment.getId());
        reulstMap.put("name",deployment.getName());
        reulstMap.put("key",key);
        reulstMap.put("deploymentTime",deployment.getDeploymentTime());
        reulstMap.put("version",deployment.getVersion());
        return JSONUtil.toJsonStr(reulstMap);
    }

}
