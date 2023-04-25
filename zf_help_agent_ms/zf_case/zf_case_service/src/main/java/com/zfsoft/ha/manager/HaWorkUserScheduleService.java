package com.zfsoft.ha.manager;

import com.zfsoft.ha.data.requestData.HaWorkUserScheduleRequestData;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/9/23 10:25
 */
@RequestMapping("/help/schedule")
public interface HaWorkUserScheduleService {
    /**
     * 初始化排班
     * @param yearMonth
     * @author: zhaobf
     * @date: 2022-09-23 15:38
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/init")
    ApiResultSet init(@RequestParam(value = "yearMonth") @DateTimeFormat(pattern="yyyy-MM") String yearMonth);

    @ProcessFeignCalledResult
    @GetMapping(value = "/delectYearMonth")
    ApiResultSet delectYearMonth(String yearMonth);

    /**
     * 获取排班列表
     * GET /help/schedule/queryScheduleList
     * @author: zhaobf
     * @date: 2022-09-23 15:38
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/queryScheduleList")
    ApiResultSet<PageResult> queryScheduleList(@RequestParam(value = "yearMonth") String yearMonth,
                                               @RequestParam(value = "groupId") Long groupId,
                                               @RequestParam(value = "workUserName") String workUserName,
                                               @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                               @RequestParam(value = "pageSize", required = false) Integer pageSize) throws Exception;

    /**
     * @description: 根据身份证号码获取用户的帮办队列queue记录
     * @author: zhaobf
     * @date: 2022-09-23 15:38
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/update")
    ApiResultSet update(@Valid HaWorkUserScheduleRequestData haWorkUserSchedule);
}
