package com.zfsoft.ha.front;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description //队列接口
 * @Author: Wangyh
 * @Date: 2022/7/20 10:50
 */
@RequestMapping("/ha/df")
public interface QueueService {
    /**
     * @description: 是否有新的办事人员扫码
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/isHaveNewMesses", method = {RequestMethod.GET})
    ApiResultSet isHaveNewMesses();

    /**
     * @description:
     * 查询办事队列
     * @param name 姓名
     * @param cardNo 身份证号码
     * @param companyName 企业名称
     * @param queueStatus  排队状态;1-扫码排队中，2-导服已分配
     * @param serviceStatus 服务状态;1-等待服务，2-服务中，3-服务完成
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryWorkQueueList", method = {RequestMethod.POST})
    ApiResultSet queryWorkQueueList(
                @RequestParam(value = "name", required = false) String name,
                @RequestParam(value = "cardNo", required = false) String cardNo,
                @RequestParam(value = "companyName", required = false) String companyName,
                @RequestParam(value = "queueStatus", required = false) String queueStatus,
                @RequestParam(value = "serviceStatus", required = false) String serviceStatus);

    /**
     * @description:
     * 统计队列数据
     * @param //Token在header中
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getQueueNum", method = {RequestMethod.GET})
    ApiResultSet getQueueNum();



    /**
     * @description:
     * 获取帮代办人员列表
     * @param name 姓名
     * @param haType 帮代办类型，1-快捷帮办，2-圆桌帮办
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getWorkUserList", method = {RequestMethod.POST})
    ApiResultSet getWorkUserList(
                                   @RequestParam(value = "name", required = false) String name,
                                   @RequestParam(value = "haType", required = false) String haType
    );


    /**
     * @description:
     * 分配帮代办人员
     * @param workUserId 说明:帮代办人员编号
     * @param queueId 队列编号
     * @param distributeStatus 分配状态;1-指定人员，2-随机分配，3-窗口办理
     * @param windowsNumbe 排队号(预留字段)
     * @param //Token在header中
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/distributeWorkUser", method = {RequestMethod.POST})
    ApiResultSet distributeWorkUser(
                                      @RequestParam(value = "workUserId", required = false) Long workUserId,
                                      @RequestParam(value = "queueId", required = true) Long queueId,
                                      @RequestParam(value = "distributeStatus", required = true) String distributeStatus,
                                      @RequestParam(value = "windowsNumbe", required = false) String windowsNumbe
                                      );


    /**
     * @description:
     * 窗口叫号
     * @param queueId 队列编号
     * @param windowsNumbe 排队号
     * @param //Token在header中
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/windowCall", method = {RequestMethod.POST})
    ApiResultSet windowCall(
                              @RequestParam(value = "queueId", required = true) Long queueId,
                              @RequestParam(value = "windowsNumbe", required = true) String windowsNumbe
    );

    @RequestMapping(value = "/getQueueById", method = {RequestMethod.POST})
    ApiResultSet getQueueById(Long queueId);


    @RequestMapping(value = "/saveQueueAdviceMemo", method = {RequestMethod.POST})
    ApiResultSet saveQueueAdviceMemo(Long queueId, String adviseMemo);

    @RequestMapping(value = "/saveNameAndCard", method = {RequestMethod.POST})
    ApiResultSet saveNameAndCard( @RequestParam(value = "queueId", required = true) Long queueId,
                                  @RequestParam(value = "name", required = true) String name,
                                  @RequestParam(value = "cardType", required = true) String cardType,
                                  @RequestParam(value = "cardNo", required = true) String cardNo);
}
