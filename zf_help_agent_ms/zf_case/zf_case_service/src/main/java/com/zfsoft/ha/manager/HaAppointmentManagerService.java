package com.zfsoft.ha.manager;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 预约人员查询service
 *
 * @author yupeng
 * @version 1.0
 * @date 2022年08月11日 20:00:05
 */
@RequestMapping("/appointmentManger")
public interface HaAppointmentManagerService {


    /**
     * 查询所有的预约的工作人员
     *
     * @return 工作人员集合
     * @author yupeng
     * @date 2022年08月11 20:04:04
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/getAllHelpWorkUser")
    ApiResultSet getAllHelpWorkUser();


    /**
     * 查询预约人员分页数据
     *
     * @param name       预约人姓名
     * @param workUserId 预约的工作人员id
     * @param status     预约状态
     * @param pageNum    分页参数
     * @param pageSize   分页参数
     * @return 分页结果集
     * @author yupeng
     * @date 2022年08月12 10:33:51
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/queryAppointmentPage")
    ApiResultSet queryAppointmentPage(String name, String workUserId, String status, Integer pageNum, Integer pageSize);

}
