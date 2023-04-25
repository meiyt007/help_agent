package com.zfsoft.ha.manager;

import com.zfsoft.ha.data.HaSms;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2023/1/30 9:35
 */
@RequestMapping("/smsManager")
public interface HaSmsManagerService {

    @ProcessFeignCalledResult
    @PostMapping(value = "/queryPage")
    ApiResultSet<PageResult<HaSms>> queryPage(HaSms haSms, Date beginTime, Date endTime, Integer pageNum, Integer pageSize);

}
