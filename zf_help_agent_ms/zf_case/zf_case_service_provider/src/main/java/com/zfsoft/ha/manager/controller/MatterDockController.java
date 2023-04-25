package com.zfsoft.ha.manager.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.zfsoft.ha.manager.HaMatterDockService;
import com.zfsoft.ha.managers.HaMatterDockManager;
import com.zfsoft.ha.managers.HaSynServiceXdManager;
import com.zfsoft.ha.managers.HaWorkQueueManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.single.util.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description //万达对接事项控制层
 * @Author: Wangyh
 * @Date: 2022/8/22 15:27
 */
@RestController
@Slf4j
public class MatterDockController implements HaMatterDockService {

    @Resource
    private HaMatterDockManager haMatterDockManager;

    @Resource
    private HaSynServiceXdManager haSynServiceXdManager;
    /**
     * @description:  同步万达的事项相关数据
     * @author: wangyh
     * @Date: 2022/8/22 15:19
     **/
    @Override
    public ApiResultSet dataSynchronization()  {
        log.info("同步万达的事项相关数据");
        try {
            haMatterDockManager.MatterSynchronization();
        } catch (IOException e) {
            log.error("同步万达的事项相关数据失败：",e);
            return  new ApiResultSet<>(500,"同步万达的事项相关数据失败",e.getMessage());
        }
        return ApiResultSet.ok("同步万达的事项相关数据成功");
    }

    /**
     * @description:  同步新点的事项相关数据
     * @author: wangyh
     * @Date: 2023/01/28 15:19
     **/
    @Override
    public ApiResultSet dataSynchronizationXinD(HttpServletRequest request) {
        log.info("同步新点的事项相关数据");
        try {
            haSynServiceXdManager.ServiceInfoSyn(request);
        } catch (Exception e) {
            log.error("同步新点的事项相关数据失败：",e);
            return  new ApiResultSet<>(500,"同步新点的事项相关数据失败",e.getMessage());
        }
        return ApiResultSet.ok("同步新点的事项相关数据成功");
    }

    /**
     * @description:  同步新点的事项相关数据
     * @author: wangyh
     * @Date: 2023/01/28 15:19
     **/
    @Override
    public ApiResultSet depairServiceInfo(HttpServletRequest request) {
        log.info("同步新点的事项相关数据");
        try {
            haSynServiceXdManager.repairServiceInfo(request);
        } catch (Exception e) {
            log.error("同步新点的事项相关数据失败：",e);
            return  new ApiResultSet<>(500,"同步新点的事项相关数据失败",e.getMessage());
        }
        return ApiResultSet.ok("同步新点的事项相关数据成功");
    }

}
