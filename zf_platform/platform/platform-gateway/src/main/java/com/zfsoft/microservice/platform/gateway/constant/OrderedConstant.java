package com.zfsoft.microservice.platform.gateway.constant;

/**
 * @ClassName OrderedConstant
 * @Description
 * @Author
 * @Date2020-09-29 19:17
 * @Version V1.0
 **/
import org.springframework.core.Ordered;

import static org.springframework.cloud.gateway.filter.NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER;

/**
 * filter排序码
 */
public interface OrderedConstant extends Ordered {

    /**
     * 日志记录
     */
    int LOGGING_FILTER = WRITE_RESPONSE_FILTER_ORDER - 1;

    /**
     * 跨站点请求伪造
     */
    int HOST_FILTER = WRITE_RESPONSE_FILTER_ORDER - 2;

    /**
     * request
     */
    int REQUEST_FILTER = HIGHEST_PRECEDENCE;

}
