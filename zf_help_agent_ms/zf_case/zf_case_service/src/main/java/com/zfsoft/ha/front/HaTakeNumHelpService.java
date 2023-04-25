package com.zfsoft.ha.front;

import com.zfsoft.ha.data.HaAppointment;
import com.zfsoft.ha.data.requestData.HaTakeNumHelpRequestData;
import com.zfsoft.ha.data.responseData.HaAppointmentResponseData;
import com.zfsoft.ha.data.responseData.HaTakeNumHelpResponseData;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @description: 取号分配帮代办人员信息接口
 * @author: kangax
 * @date: 2022-07-28 17:30
 **/
@RequestMapping("/ha")
public interface HaTakeNumHelpService {

    /**
     * @description: 扫码填写帮代办信息接口
     * @params：[ haWorkQueueVo 办事队列实体]
     * @author: kangax
     * @date: 2022-07-28 17:53
     */
    @PostMapping(value = "/takeNum/helpInfo")
    ApiResultSet<HaTakeNumHelpResponseData> takeNumHelpInfo(HaTakeNumHelpRequestData requestData) throws Exception;

    /**
     * @description: 查询预约信息
     * @params：[ HaAppointmentResponseData 办事队列实体]
     * @author: wangyh
     * @date: 2022-08-28 17:53
     */
    @PostMapping(value = "/takeNum/queryAppointmentInfo")
    ApiResultSet<HaAppointmentResponseData> queryAppointmentInfo(HaAppointment ha);
}
