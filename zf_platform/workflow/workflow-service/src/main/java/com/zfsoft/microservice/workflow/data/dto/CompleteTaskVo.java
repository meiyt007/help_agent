package com.zfsoft.microservice.workflow.data.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName TaskVo
 * @Description: 流程任务vo
 * @Author wuxx
 * @Date 2022/3/10
 **/
@Data
public class CompleteTaskVo implements Serializable {

    /**
     *  流程任务id
     **/
    private String taskId;

    /**
     * 办理人主键
     **/
    private String userId;


    /**
     *  传递参数map
     **/
    private Map<String, Object> variableMap;

}
