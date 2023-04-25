package com.zfsoft.ha.front;

import com.zfsoft.ha.data.requestData.HaScanHelpInfoRequestData;
import com.zfsoft.ha.data.responseData.HaScanHelpInfoResponseData;
import com.zfsoft.ha.data.vo.HaWorkQueueVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @description: 扫码填写帮代办信息接口
 * @author: kangax
 * @date: 2022-07-28 17:30
 **/
@RequestMapping("/ha/")
public interface HaScanHelpInfoService {

    /**
     * @description: 扫码填写帮代办信息接口
     * @params：[ haWorkQueueVo 办事队列实体]
     * @author: kangax
     * @date: 2022-07-28 17:53
     */
    @PostMapping(value = "scan/helpInfo")
    ApiResultSet<HaScanHelpInfoResponseData> scanHelpInfo(HaScanHelpInfoRequestData requestData);

}
