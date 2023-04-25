package com.zfsoft.microservice.workflow.service;

import com.zfsoft.microservice.workflow.data.ProAcceptExample;
import com.zfsoft.microservice.workflow.data.ProProcessExample;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 办件受理服务定义接口
 */
@RequestMapping("/security/accept")
public interface ProAcceptExampleService {

    /**
     * 受理办件
     * @param proAcceptExample
     * @return
     */
    @PostMapping(value = "/save")
    ApiResultSet<ProAcceptExample> saveProAcceptExample(@RequestBody ProAcceptExample proAcceptExample);

    /**
     * 分页查询
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/page")
    ApiResultSet<PageResult> page(String name, Integer pageNum, Integer pageSize);

    /**
     * 分页查询已办
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/completedTasksPage")
    ApiResultSet<PageResult> completedTasksPage(String name, Integer pageNum, Integer pageSize);


    /**
     * 下一步
     * @param proProcessExample
     * @return
     */
    @PostMapping(value = "/next")
    ApiResultSet<String> next(@RequestBody ProProcessExample proProcessExample);

    /**
     * 获取任务办理
     * @param taskId
     * @return
     */
    @GetMapping(value = "/getTaskCondition/{taskId}")
    ApiResultSet getTaskCondition(@PathVariable("taskId") String taskId);

}
