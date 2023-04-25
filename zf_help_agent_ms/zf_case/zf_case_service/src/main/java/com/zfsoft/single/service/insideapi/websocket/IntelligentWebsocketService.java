package com.zfsoft.single.service.insideapi.websocket;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @（#）: IntelligentWebsocket
 * @description: 智能语音Websocket
 * @author: wangwg
 * @date: 2021/6/23
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/intelligentWebsocket")
public interface IntelligentWebsocketService {

    /**
     * 发送问答信息，调用研究院接口查询返回结果
     *
     * @param message
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/intelligentAsk", method = {RequestMethod.GET})
    ApiResultSet<String> intelligentAsk(String message);

}
