package com.zfsoft.ha.manager;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 帮代办消息管理
 *
 * @author yupeng
 * @version 1.0
 * @date 2022年08月12日 15:06:29
 */
@RequestMapping("/messageManager")
public interface HaMessageManagementService {

    /**
     * 查询消息列表
     *
     * @param messageReceiver 消息发送者
     * @param messageSource   消息接收者
     * @param messageTitle    消息标题
     * @param pageNum         分页参数
     * @param pageSize        分页参数
     * @return 返回消息列表
     * @author yupeng
     * @date 2022年08月12 15:09:50
     */
    @ResponseBody
    @ProcessFeignCalledResult
    @PostMapping(value = "/queryMessagePage")
    ApiResultSet queryMessagePage(String messageReceiver, String messageSource, String messageTitle, Integer pageNum, Integer pageSize);
}
