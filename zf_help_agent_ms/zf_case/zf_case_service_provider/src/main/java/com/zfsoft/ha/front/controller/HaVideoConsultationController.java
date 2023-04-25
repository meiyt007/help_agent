package com.zfsoft.ha.front.controller;

import com.zfsoft.ha.data.HaVideoAccess;
import com.zfsoft.ha.data.HaVideoChatRecord;
import com.zfsoft.ha.data.requestData.HaCRoomTakeRequestData;
import com.zfsoft.ha.data.requestData.HaMessageRequestData;
import com.zfsoft.ha.data.requestData.HaScanHelpInfoRequestData;
import com.zfsoft.ha.data.requestData.HaTakeNumHelpRequestData;
import com.zfsoft.ha.data.responseData.HaTakeNumHelpResponseData;
import com.zfsoft.ha.data.vo.CreateRoomVo;
import com.zfsoft.ha.data.vo.HaVideoChatRecordVo;
import com.zfsoft.ha.data.vo.HelpPersonList;
import com.zfsoft.ha.dbaccess.data.vo.DbWorkOrQueuVo;
import com.zfsoft.ha.front.HaVideoConsultationService;
import com.zfsoft.ha.managers.*;
import com.zfsoft.platform.common.data.ApiResultSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Description //视频咨询控制层
 * @Author: Wangyh
 * @Date: 2023/3/13 11:02
 */
@RestController
@Slf4j
public class HaVideoConsultationController  implements HaVideoConsultationService {
    /**
     * 队列接口实现
     */
    @Resource
    private QueueManager queueManager;
    /**
     * 用户登录service层
     */
    @Resource
    private HaWorkUserServiceManager haWorkUserServiceManager;

    /**
     * 办事队列manager
     */
    @Resource
    private HaWorkQueueManager haWorkQueueManager;

    /**
     * 来访信息service层
     */
    @Resource
    private HaVisitManager haVisitManager;
    /**
     * 视频咨询service层
     */
    @Resource
    private HaVideoConsultationManager haVideoConsultationManager;
    /**
     * redis
     */
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @description:
     * 获取token,用户提供给第三方鉴权使用
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @Override
    public ApiResultSet getToken(String userName, String userId) {
        log.info("获取token,用户提供给第三方鉴权使用");
        //生成token
        String token = UUID.randomUUID().toString().replace("-", "");
         //将token为key value为对象用户信息存入redis中 时间期限是2小时
        redisTemplate.opsForValue().set(token ,userName,
                1, TimeUnit.DAYS);

        return ApiResultSet.ok("获取token成功",token);
    }

    /**
     * @description:
     * 获取每个帮办人员的排队情况
     * @author: Wangyh
     * @Date: 2023/3/13 10:10
     **/
    @Override
    public ApiResultSet queryWorkQueueList(Long groupId, String name, String status) {
        log.info("进入获取每个帮办人员的排队情况");
        List<HelpPersonList> helpPersonList = haWorkUserServiceManager.queryHelpPersonList(groupId, name, status);
        return  ApiResultSet.ok("成功",helpPersonList);
    }

    /**
     * @description:
     * 创建视频帮办房间
     * @param haCRoomTakeRequestData 请求实体类
     * @author: Wangyh
     * @Date: 2023/3/13 10:10
     **/
    @Override
    public ApiResultSet createRoom(HaCRoomTakeRequestData haCRoomTakeRequestData) throws Exception {
        log.info("进入获取每个帮办人员的排队情况, haCRoomTakeRequestData:{} ",haCRoomTakeRequestData);
        HaTakeNumHelpRequestData requestData = new HaTakeNumHelpRequestData();
        BeanUtils.copyProperties(haCRoomTakeRequestData, requestData);
        //取号，加入排队队列
        List<HaTakeNumHelpResponseData> haTakeNumHelpResponseData = haWorkQueueManager.takeNumHelpInfoFillIn(requestData);
        if(haTakeNumHelpResponseData==null||haTakeNumHelpResponseData.size()==0){
            return new ApiResultSet(ApiResultSet.DIRTY_DATA_TITLE, "取号失败:根据选择的组别和类型没有找到帮办人员" );
        }
        //保持办件来访信息
        HaScanHelpInfoRequestData requestData1 = new HaScanHelpInfoRequestData();
        BeanUtils.copyProperties(requestData, requestData1);
        if(!haVisitManager.saveHavisitByhelpInfo(requestData1)) {
            log.info("进入扫码填写帮代办信息Controller，添加到来访信息时错误");
        }
        HaTakeNumHelpResponseData haTakeNumHelpResponseData1 = new HaTakeNumHelpResponseData();
        for(int i=0;i<haTakeNumHelpResponseData.size();i++){
             haTakeNumHelpResponseData1 = haTakeNumHelpResponseData.get(i);
        }
        return ApiResultSet.ok("请求成功",haTakeNumHelpResponseData1.getCreateRoomVo());
    }

    /**
     * @description:
     * 检查排队情况
     * @param queueId  队列主键
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @Override
    public ApiResultSet checkQueueStatus(String queueId) {
        log.info("检查排队情况,queueId:{}", queueId);
        DbWorkOrQueuVo dbWorkOrQueuVo = queueManager.queryWorkQueueVo(Long.valueOf(queueId));
        return ApiResultSet.ok("成功",dbWorkOrQueuVo);
    }

    /**
     * @description:
     * 结束视频帮办
     * @param roomId  房间主键
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @Override
    public ApiResultSet endVideoHelp(String roomId) {
        log.info("结束视频帮办方法，参数roomId：{}", roomId);
        ApiResultSet apiResultSet = haVideoConsultationManager.endVideoHelp(roomId);
        return apiResultSet;
    }

    /**
     * @description:
     * 消息发送
     * @param haMessageRequestData  发送消息接口请求实体类
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @Override
    public ApiResultSet sendMessage(HaMessageRequestData haMessageRequestData) {
        log.info("进入发送消息接口请求实体类，参数 haMessageRequestData:{}", haMessageRequestData);
        //1对t_ha_video_chat_record进行操作
        HaVideoChatRecord haVideoChatRecord = new HaVideoChatRecord();
        BeanUtils.copyProperties(haMessageRequestData, haVideoChatRecord);
        Map<String, Object> map = haVideoConsultationManager.saveOrUpdateHaVideoChatRecord(haVideoChatRecord);
        if(map.get("type").equals("2") && map.get("index").equals(1)){
            return ApiResultSet.ok("发送消息成功");
        }
        return new ApiResultSet<>(500,"发送消息失败");
    }

    /**
     * @description:
     * 是否有新的消息
     * @param roomId  房间主键
     * @author: Wangyh
     * @Date: 2023/3/15 19:10
     **/
    @Override
    public ApiResultSet receiveMessage(String roomId,String videoNum) {
        log.info("是否有新的消息， roomId:{},videoNum:{}",roomId,videoNum);
        List<HaVideoChatRecordVo> videoChatRecordList;
        try {
             videoChatRecordList= haVideoConsultationManager.isHaveNewMesses(Long.valueOf(roomId), videoNum);
        } catch (Exception e) {
            log.error("调用是否有新的消息接口失败：", e);
            return new ApiResultSet<>(500, "调用是否有新的消息接口失败", e.getMessage());
        }
        return ApiResultSet.ok("调用是否有新的消息接口成功", videoChatRecordList);
    }

    /*********************************zhaobf start*********************************/

    @Override
    public ApiResultSet endCall(String accessId){
        log.info("视频帮办：取消呼叫Controller，accessId：{}", accessId);
        Map<String, Object> stringObjectMap = haVideoConsultationManager.endCall(accessId);
        if(!(Boolean) stringObjectMap.get("state")){
            return new ApiResultSet(ApiResultSet.DIRTY_DATA_TITLE, (String) stringObjectMap.get("message"));
        }
        return ApiResultSet.ok("请求成功", "");
    }

    /**
     * Description: 进入房间
     *
     * @param accessId    房间记录主键
     * @param existStatus
     * @author zhaobf
     * date: 2023/3/14 10:27
     * @copyright 版权由上海卓繁信息技术股份有限公司拥有
     */
    @Override
    public ApiResultSet joinRoom(String accessId, String existStatus){
        log.info("视频帮办：进入加入房间Controller，accessId：{}，existStatus：{}", accessId, existStatus);
        Map<String, Object> stringObjectMap = haVideoConsultationManager.joinRoom(accessId, existStatus);
        if(!(Boolean) stringObjectMap.get("state")){
            return new ApiResultSet(ApiResultSet.DIRTY_DATA_TITLE, (String) stringObjectMap.get("message"));
        }
        return ApiResultSet.ok("请求成功", stringObjectMap);
    }
    /**
    * Description: 呼叫组长或委办局老师
    * @param haVideoAccess 出入记录实体类
    * @author zhaobf
    * date: 2023/3/15 11:32
    * @copyright 版权由上海卓繁信息技术股份有限公司拥有
    */
    @Override
    public ApiResultSet callHelp(HaVideoAccess haVideoAccess) throws Exception{
        log.info("视频帮办：呼叫组长或委办局老师Controller，haVideoAccess：{}", haVideoAccess);
        Map<String, Object> stringObjectMap = haVideoConsultationManager.callHelp(haVideoAccess);
        if(!(Boolean) stringObjectMap.get("state")){
            return new ApiResultSet(ApiResultSet.DIRTY_DATA_TITLE, (String) stringObjectMap.get("message"));
        }
        return ApiResultSet.ok("请求成功", stringObjectMap);
    }
    /**
     * Description: 查询组长或委办局老师接通,返回房间记录，人员在房间的状态
     * @param roomId 房间记录主键
     * @author zhaobf
     * date: 2023/3/15 11:32
     * @copyright 版权由上海卓繁信息技术股份有限公司拥有
     */
    @Override
    public ApiResultSet callWait(String roomId){
        log.info("视频帮办：查询组长或委办局老师接通状态，roomId：{}", roomId);
        Map<String, Object> map;
        try {
            map = haVideoConsultationManager.callWait(roomId);
        } catch (Exception e) {
            log.error("查询组长或委办局老师接通状态接口错误：", e);
            return new ApiResultSet(ApiResultSet.UNKNOWN_ERROR, "查询组长或委办局老师接通状态接口错误", e.getMessage());
        }
        return ApiResultSet.ok("请求成功", map);
    }

    /**
     * Description: 退出房间
     * @param accessId 房间记录主键
     * @author zhaobf
     * date: 2023/3/16 10:26
     * @copyright 版权由上海卓繁信息技术股份有限公司拥有
     */
    @Override
    public ApiResultSet exitRoom(String accessId){
        log.info("视频帮办：进入退出房间Controller，accessId：{}", accessId);
        Map<String, Object> stringObjectMap = haVideoConsultationManager.exitRoom(accessId);
        if(!(Boolean) stringObjectMap.get("state")){
            return new ApiResultSet(ApiResultSet.DIRTY_DATA_ERROR, (String) stringObjectMap.get("message"));
        }
        return ApiResultSet.ok("请求成功", stringObjectMap);
    }

    @Override
    public ApiResultSet isHaveNewCall(){
        log.info("视频帮办：进入查询当前帮办人是否有新的呼叫" );
        Map<String, Object> stringObjectMap = haVideoConsultationManager.isHaveNewCall();
        return ApiResultSet.ok("请求成功", stringObjectMap);
    }

    @Override
    public ApiResultSet checkCodeResult(String recordId, String checkCode){
        log.info("视频帮办：进入消息确认接口" );
        return haVideoConsultationManager.checkCodeResult(recordId,checkCode);
    }

    /*********************************zhaobf end*********************************/

    @Override
    public ApiResultSet startHelp(Long workUserId, Long queueId, String userName, String userType) {
        log.info("视频帮办：帮办人员呼叫组长或帮办老师" );
        CreateRoomVo createRoomVo;
        try {
            createRoomVo = haVideoConsultationManager.startHelp(workUserId, queueId, userName, userType);
        } catch (Exception e) {
            log.error("帮办人员呼叫组长或帮办老师接口错误：", e);
            return new ApiResultSet(ApiResultSet.UNKNOWN_ERROR, e.getMessage());
        }
        return ApiResultSet.ok("请求成功", createRoomVo);
    }

    @Override
    public ApiResultSet isHaveExistCall(){
        log.info("视频帮办：组长或委办局判断是否有接通中的视频帮办信息" );
        Map<String, Object> map;
        try {
            map = haVideoConsultationManager.isHaveExistCall();
        } catch (Exception e) {
            log.error("组长或委办局判断是否有接通中的视频帮办信息接口错误：", e);
            return new ApiResultSet(ApiResultSet.UNKNOWN_ERROR, "组长或委办局判断是否有接通中的视频帮办信息接口错误", e.getMessage());
        }
        return ApiResultSet.ok("请求成功", map);
    }

    @Override
    public ApiResultSet queryVideoRecord(Long queueId) {
        log.info("查询视频录制记录,queueId:{}", queueId);
        String result = haVideoConsultationManager.queryVideoRecord(queueId);
        return ApiResultSet.ok("成功",result);
    }
}
