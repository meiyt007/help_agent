package com.zfsoft.ha.manager;


import com.zfsoft.ha.data.HaWorkQueue;
import com.zfsoft.ha.data.vo.HaWorkQueueVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/7/26 13:42
 */
@RequestMapping("/haManager/workQueue")
public interface HaWorkQueueService {
    /**
     * 分页获取事项信息
     * @param
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/page", method = {RequestMethod.POST})
    ApiResultSet<PageResult> queryQueueVoWithPage(HaWorkQueueVo haWorkQueueVO,
                                                @RequestParam(value = "pageNumber", required = false) Integer pageNum,
                                                @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * 保存办事队列信息
     *
     * @param haWorkQueue 办事队列信息
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    ApiResultSet<HaWorkQueue> saveWorkQueue(@RequestBody HaWorkQueue haWorkQueue);

    /**
     * 删除办事队列信息
     * @param id id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteQueueId", method = {RequestMethod.GET})
    ApiResultSet<HaWorkQueue> deleteQueueId(@RequestParam("id") String id);

    /**
     * 删除办事队列信息
     * @param id id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getWorkQueueById", method = {RequestMethod.GET})
    ApiResultSet<HaWorkQueue> getWorkQueueById(@RequestParam("id") String id);

}
