package com.zfsoft.ha.front;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Description //事项对接-万达
 * @Author: Wangyh
 * @Date: 2022/8/22 15:19
 */
@RequestMapping(value = "/ha/Matter")
public interface HaMatterDockService {

    /**
     * @description:  同步万达的事项相关数据
     * @author: wangyh
     * @Date: 2022/8/22 15:19
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/dataSynchronization", method = {RequestMethod.POST})
    ApiResultSet dataSynchronization() throws IOException;

    /**
     * @description:  同步新点的事项相关数据
     * @author: wangyh
     * @Date: 2023/01/28 15:19
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/dataSynchronizationXinD", method = {RequestMethod.POST})
    ApiResultSet dataSynchronizationXinD(HttpServletRequest request) throws IOException;

    /**
     * @description:  新点补全万达事项信息
     * @author: wangyh
     * @Date: 2023/01/28 15:19
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/depairServiceInfo", method = {RequestMethod.POST})
    ApiResultSet depairServiceInfo(HttpServletRequest request) throws IOException;

}
