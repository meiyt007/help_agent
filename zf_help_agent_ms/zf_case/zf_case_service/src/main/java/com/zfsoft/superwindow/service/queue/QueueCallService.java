package com.zfsoft.superwindow.service.queue;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.queue.QhjhWaitNumRequest;
import com.zfsoft.superwindow.data.queue.SendQhJhInfoRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description: 排队叫号接口
 * @author: sky
 * @date: 2021-07-15
 */
@RequestMapping(value = "/queueCall")
public interface QueueCallService {

    /**
     * 获取当前等待人数总数
     *
     * @param userOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getTotalWaitNum", method = {RequestMethod.GET})
    ApiResultSet getTotalWaitNum(@RequestParam(value = "userOid", required = false) String userOid);


    /**
     * 查询当前叫号、等待人数
     *
     * @param qhjhWaitNumRequest
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getQhjhWaitNum", method = {RequestMethod.POST})
    ApiResultSet getQhjhWaitNum(@RequestBody QhjhWaitNumRequest qhjhWaitNumRequest);


    /**
     * 推送当前叫号、等待人数到显示屏
     *
     * @param sendQhJhInfoRequest
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/sendQhJhInfo", method = {RequestMethod.POST})
    ApiResultSet sendQhJhInfo(@RequestBody SendQhJhInfoRequest sendQhJhInfoRequest);


    @ProcessFeignCalledResult
    @RequestMapping(value = "/handleCallNumber", method = {RequestMethod.GET})
    ApiResultSet handleCallNumber(@RequestParam(value = "nOid", required = true) String nOid);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/completeCallNumber", method = {RequestMethod.GET})
    ApiResultSet completeCallNumber(@RequestParam(value = "nOid", required = true) String nOid);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/skipCallNumber", method = {RequestMethod.GET})
    ApiResultSet skipCallNumber(@RequestParam(value = "nOid", required = true) String nOid);

}
