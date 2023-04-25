package com.zfsoft.ha.manager.controller;

import com.zfsoft.ha.data.HaSms;
import com.zfsoft.ha.manager.HaSmsManagerService;
import com.zfsoft.ha.managers.HaSmsManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2023/1/30 9:31
 */
@RestController
@Slf4j
public class HaSmsManagerController  implements HaSmsManagerService {
    @Resource
    private HaSmsManager haSmsManager;

    @Override
    public ApiResultSet<PageResult<HaSms>> queryPage(HaSms haSms, Date beginTime, Date endTime, Integer pageNum, Integer pageSize) {
        log.info("查询短信分页数据，haSms：{}，beginTime：{}，endTime：{}，pageNum：{}，pageSize：{}", haSms, beginTime, endTime, pageNum, pageSize);
        PageResult<HaSms> dbHaAppointmentPage = haSmsManager.queryPage(haSms,beginTime, endTime, pageNum, pageSize);
        return ApiResultSet.ok(dbHaAppointmentPage);
    }
}
