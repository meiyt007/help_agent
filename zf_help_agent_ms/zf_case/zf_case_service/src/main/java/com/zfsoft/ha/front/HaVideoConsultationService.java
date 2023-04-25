package com.zfsoft.ha.front;

import com.zfsoft.ha.data.HaVideoAccess;
import com.zfsoft.ha.data.requestData.HaCRoomTakeRequestData;
import com.zfsoft.ha.data.requestData.HaMessageRequestData;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;

/**
 * @Description //视频咨询类接口
 * @Author: Wangyh
 * @Date: 2023/3/13 9:39
 */
@RequestMapping("/ha/video")
public interface HaVideoConsultationService {
    /*********************************wangyuhang start**********************************/
    /**
     * @description:
     * 获取token,用户提供给第三方鉴权使用
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getToken", method = {RequestMethod.GET})
    ApiResultSet getToken(String userName,  String userId);
    /**
     * @description:
     * 查询办事队列
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/helpPersonList", method = {RequestMethod.GET})
    ApiResultSet queryWorkQueueList(Long groupId, String name, String status);


    /**
     * @description:
     * 创建视频帮办房间
     * @param haCRoomTakeRequestData 请求实体类
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/createRoom", method = {RequestMethod.POST})
    ApiResultSet createRoom(@RequestBody HaCRoomTakeRequestData haCRoomTakeRequestData) throws Exception;

    /**
     * @description:
     * 检查排队情况
     * @param queueId  队列主键
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/checkQueueStatus", method = {RequestMethod.POST})
    ApiResultSet checkQueueStatus(
            @RequestParam(value = "queueId", required = false) String queueId
    );

    /**
     * @description:
     * 结束视频帮办
     * @param roomId  房间主键
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/endVideoHelp", method = {RequestMethod.GET})
    ApiResultSet endVideoHelp( String roomId);


    /**
     * @description:
     * 消息发送
     * @param haMessageRequestData  发送消息接口请求实体类
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/sendMessage", method = {RequestMethod.POST})
    ApiResultSet sendMessage(@RequestBody HaMessageRequestData haMessageRequestData);


    /**
     * @description:
     * 是否有新的消息
     * @param roomId  房间主键
     * @author: Wangyh
     * @Date: 2023/3/15 19:10
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/receiveMessage", method = {RequestMethod.GET})
    ApiResultSet receiveMessage(String roomId,String videoNum);


    /*********************************wangyuhang end*********************************************/


    /*********************************zhaobf start*****************************************/
    @ProcessFeignCalledResult
    @GetMapping(value = "/endCall")
    ApiResultSet endCall(@RequestParam(value = "accessId") String accessId);

    /**
     * Description: 进入房间
     *
     * @param accessId    房间记录主键
     * @param existStatus
     * @author zhaobf
     * date: 2023/3/14 10:27
     * @copyright 版权由上海卓繁信息技术股份有限公司拥有
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/joinRoom")
    ApiResultSet joinRoom(@RequestParam(value = "accessId") String accessId,
                          @RequestParam(value = "existStatus") String existStatus);

    /**
     * Description: 呼叫组长或委办局老师
     * @param haVideoAccess 出入记录实体类
     * @author zhaobf
     * date: 2023/3/15 11:32
     * @copyright 版权由上海卓繁信息技术股份有限公司拥有
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/callHelp")
    ApiResultSet callHelp(HaVideoAccess haVideoAccess) throws Exception;

    /**
     * Description: 查询组长或委办局老师接通,返回房间记录，人员在房间的状态
     * @param roomId 房间主键
     * @author zhaobf
     * date: 2023/3/15 11:32
     * @copyright 版权由上海卓繁信息技术股份有限公司拥有
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/callWait")
    ApiResultSet callWait(@RequestParam(value = "roomId") String roomId) throws Exception;

    /**
    * Description: 退出房间
    * @param accessId 房间记录主键
    * @author zhaobf
    * date: 2023/3/16 10:26
    * @copyright 版权由上海卓繁信息技术股份有限公司拥有
    */
    @ProcessFeignCalledResult
    @GetMapping(value = "/exitRoom")
    ApiResultSet exitRoom(@RequestParam(value = "accessId") String accessId);

    /**
    * Description: 查询当前帮办人是否有新的呼叫
    * @author zhaobf
    * date: 2023/3/16 13:54
    * @copyright 版权由上海卓繁信息技术股份有限公司拥有
    */
    @ProcessFeignCalledResult
    @GetMapping(value = "/isHaveNewCall")
    ApiResultSet isHaveNewCall();

    @ProcessFeignCalledResult
    @GetMapping(value = "/checkCodeResult")
    ApiResultSet checkCodeResult(@RequestParam(value = "recordId") String recordId,
                                 @RequestParam(value = "checkCode") String checkCode);

    /*********************************zhaobf end*****************************************/

    /**
     * 帮办老师发起帮办,创建房间，并呼叫组长或委办局老师
     * @param workUserId 组长或委办局老师的主键
     * @param queueId 队列主键
     * @param userName 用户名称
     * @param userType 用户类型 1-帮办人员，2-帮办组长，3-委办局老师，4-办事人
     * @return
     * @throws Exception
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/startHelp")
    ApiResultSet startHelp(@RequestParam(value = "workUserId") Long workUserId,
                           @RequestParam(value = "queueId") Long queueId,
                           @RequestParam(value = "userName") String userName,
                           @RequestParam(value = "userType") String userType) throws Exception;

    /**
     * 组长或委办局判断是否有接通中的视频帮办信息
     * @return
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/isHaveExistCall")
    ApiResultSet isHaveExistCall();

    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryVideoRecord", method = {RequestMethod.GET})
    ApiResultSet queryVideoRecord(
            @RequestParam(value = "queueId") Long queueId
    );
}
