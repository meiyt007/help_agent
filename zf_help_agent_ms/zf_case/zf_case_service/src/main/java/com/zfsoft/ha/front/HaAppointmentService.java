package com.zfsoft.ha.front;

import com.zfsoft.ha.data.HaAppointment;
import com.zfsoft.ha.data.responseData.HaAppointmentResponseData;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RequestMapping("/ha/appointment")
public interface HaAppointmentService {

    /**
     * 查询预约信息
     *
     * @param
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryAppointmentInfo", method = {RequestMethod.POST})
    ApiResultSet<List<HaAppointmentResponseData>> queryAppointmentInfo(HaAppointment ha);

}
