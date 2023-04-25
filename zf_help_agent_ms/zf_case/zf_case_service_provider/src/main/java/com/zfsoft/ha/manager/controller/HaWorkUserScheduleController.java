package com.zfsoft.ha.manager.controller;

import com.zfsoft.ha.data.HaWorkUserSchedule;
import com.zfsoft.ha.data.requestData.HaWorkUserScheduleRequestData;
import com.zfsoft.ha.data.vo.HaWorkUserVo_Schedule;
import com.zfsoft.ha.manager.HaWorkUserScheduleService;
import com.zfsoft.ha.managers.HaWorkUserScheduleManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.single.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.beans.Beans;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/9/23 10:33
 */
@RestController
@Slf4j
public class HaWorkUserScheduleController implements HaWorkUserScheduleService  {

    @Resource
    private HaWorkUserScheduleManager haWorkUserScheduleManager;

    @Override
    public ApiResultSet init(String yearMonth) {
        int count = haWorkUserScheduleManager.init(yearMonth);
        if(count<0){
            return ApiResultSet.ok("初始化排班信息失败，当前月份已经初始化过了",count );
        }

        return ApiResultSet.ok("初始化排班信息成功", count);
    }

    @Override
    public ApiResultSet delectYearMonth(String yearMonth) {
        int count = haWorkUserScheduleManager.delect(yearMonth);
        return ApiResultSet.ok("删除当前"+yearMonth+"排班成功", count);
    }

    @Override
    public ApiResultSet<PageResult> queryScheduleList(String yearMonth, Long groupId, String workUserName, Integer pageNum, Integer pageSize) throws Exception {
        PageResult<HaWorkUserVo_Schedule> resultSet = haWorkUserScheduleManager.queryScheduleList(yearMonth,groupId,  workUserName, pageNum,pageSize);
        return ApiResultSet.ok("获取排班列表成功", resultSet);
    }

    @Override
    public ApiResultSet update(HaWorkUserScheduleRequestData requestData) {
        HaWorkUserSchedule haWorkUserSchedule = new HaWorkUserSchedule();
        BeanUtils.copyProperties(requestData,haWorkUserSchedule);
        int coint  = haWorkUserScheduleManager.update(haWorkUserSchedule);
        return ApiResultSet.ok("更新排班信息成功", coint);
    }
}
