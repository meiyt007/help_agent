package com.zfsoft.ha.front;

import com.zfsoft.ha.data.HaOnline;
import com.zfsoft.ha.data.HaWorkUser;
import com.zfsoft.ha.data.requestData.HaOnlineRequestData;
import com.zfsoft.ha.data.requestData.HaWorkUserRequestData;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description //在线时长表接口
 * @Author: Wangyh
 * @Date: 2022/8/12 10:59
 */
@RequestMapping("/online")
public interface HaOnlineService {
    /**
     * @description:  查询在线时长表分页信息列表
     * @param haOnlineRequestData 在线时长表请求封装实体类
     * @param pageNum 页码
     * @param pageSize 当前页展示数量
     * @author: wangyh
     * @Date: 2022/8/12
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/OnlineServicePage", method = {RequestMethod.POST})
    ApiResultSet<PageResult<HaOnline>> queryOnlineServiceWithPage(
            HaOnlineRequestData haOnlineRequestData,
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) throws Exception;
}
