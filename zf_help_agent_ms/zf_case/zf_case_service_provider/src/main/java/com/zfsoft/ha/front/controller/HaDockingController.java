package com.zfsoft.ha.front.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.zfsoft.cases.data.*;
import com.zfsoft.cases.manager.QlCaseManager;
import com.zfsoft.cases.manager.QlCaseMaterialAttaManager;
import com.zfsoft.cases.manager.SysAttaManager;
import com.zfsoft.cases.service.*;
import com.zfsoft.cases.util.BaseStaticParameter;
import com.zfsoft.cases.util.DateUtil;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.ha.constant.Constants;
import com.zfsoft.ha.data.*;
import com.zfsoft.ha.data.TripartiteVo.*;
import com.zfsoft.ha.data.TripartiteVo.HaWorkUserVo;
import com.zfsoft.ha.data.requestData.*;
import com.zfsoft.ha.data.responseData.*;
import com.zfsoft.ha.data.vo.*;
import com.zfsoft.ha.dbaccess.dao.DbHaVisitMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkQueueMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkUserMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkUserScheduleMapper;
import com.zfsoft.ha.dbaccess.data.DbHaWorkUser;
import com.zfsoft.ha.dbaccess.data.example.DbHaWorkUserExample;
import com.zfsoft.ha.dbaccess.data.vo.DbWorkOrQueuVo;
import com.zfsoft.ha.dbaccess.data.vo.HaWorkQueueResponseVo;
import com.zfsoft.ha.front.HaBusinessService;
import com.zfsoft.ha.front.HaDockingService;
import com.zfsoft.ha.managers.*;
import com.zfsoft.ha.util.ClientServer;
import com.zfsoft.microservice.platform.service.sys.SysDistrictService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.data.SysConfig;
import com.zfsoft.platform.utils.feign.SysConfigFeignService;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.manager.sxService.RefinedMaterialManager;
import com.zfsoft.service.manager.sxService.ReviewPointsManager;
import com.zfsoft.service.manager.sxService.SxServiceMaterialManager;
import com.zfsoft.service.manager.sxSituation.ServiceMaterialManager;
import com.zfsoft.service.manager.sxSituation.SxServiceMateOptRelManager;
import com.zfsoft.service.manager.sxSituation.SxServiceOptionTitleManager;
import com.zfsoft.service.manager.sxSituation.SxServiceOptionValManager;
import com.zfsoft.service.sxService.data.RefinedMaterial;
import com.zfsoft.service.sxService.data.ReviewPoints;
import com.zfsoft.service.sxService.data.SxService;
import com.zfsoft.service.sxService.data.SxServiceMaterial;
import com.zfsoft.service.sxService.service.SxServiceService;
import com.zfsoft.service.sxSituation.data.ServiceMaterial;
import com.zfsoft.service.sxSituation.data.SxServiceMateOptRel;
import com.zfsoft.service.sxSituation.data.SxServiceOptionTitle;
import com.zfsoft.service.sxSituation.data.SxServiceOptionVal;
import com.zfsoft.service.sxSituation.data.vo.ServiceMaterialVo;
import com.zfsoft.single.service.api.ApiService;
import com.zfsoft.single.util.BeanUtils;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.zfsoft.cases.constant.Constants.HA_SCHE_APPOIN_NUM_MAX;
import static com.zfsoft.ha.constant.RedisConstants.LOGIN_DOCKING_SESSION_TOKEN;
import static com.zfsoft.ha.constant.RedisConstants.LOGIN_DOCKING_SESSION_TTL;

/**
 * 数据接收与推送控制层
 *
 * @author yupeng
 * @version 1.0
 * @date 2022/7/28 14:40
 */
@Slf4j
@RestController
public class HaDockingController implements HaDockingService {
    /**
     * 用户db层接口
     */
    @Resource
    private DbHaWorkUserMapper dbHaWorkUserMapper;

    /**
     * 办件基本信息服务定义接口
     */
    @Resource
    private QlCaseService qlCaseServiceFeginService;

    /**
     * 预约信息manager
     */
    @Resource
    private HaAppointmentManager haAppointmentManager;

    /**
     * 队列接口manager
     */
    @Resource
    private QueueManager queueManager;

    @Resource
    private HaWorkQueueManager haWorkQueueManager;
    /**
     * 材料附件信息实现类
     */
    @Resource
    private QlCaseAttaFileService qlCaseAttaFileFeignService;

    /**
     * 办件申请信息服务实现类
     */
    @Resource
    private QlCaseApplayService qlCaseApplayService;

    /**
     * 办件材料信息服务定义接口
     */
    @Resource
    private QlCaseMaterialService qlCaseMaterialServiceFeginService;


    @Resource
    private HaVisitManager haVisitManager;

    /**
     * 帮代办人员分组表实现层
     */
    @Resource
    private HaWorkGroupServiceManager haWorkGrouoServiceManager;

    /**
     * 人员用户表实现
     */
    @Resource
    private HaWorkUserServiceManager haWorkUserServiceManager;
    /**
     * 服务表实现层
     */
    @Resource
    private HaWorkServiceManager haWorkServiceManager;

    /**
     * 帮代办评价manager
     */
    @Resource
    private HaEvalManager haEvalManager;

    @Resource
    private QlCaseMaterialAttaManager qlCaseMaterialAttaManager;

    @Resource
    private SxServiceOptionTitleManager sxServiceOptionTitleManager;

    @Resource
    private SxServiceOptionValManager sxServiceOptionValManager;

    @Resource
    private SxServiceMateOptRelManager sxServiceMateOptRelManager;

    @Resource
    private SxServiceMaterialManager sxServiceMaterialManager;

    @Resource
    private ServiceMaterialManager serviceMaterialManager;

    @Resource
    private RefinedMaterialManager refinedMaterialManager;

    @Resource
    private ReviewPointsManager reviewPointsManager;

    @Resource
    private SxServiceService sxServiceFeginService;

    @Resource
    private HaWorkUserScheduleManager haWorkUserScheduleManager;

    /**
     * 办件基本信息实现类
     */
    @Resource
    private QlCaseManager qlCaseManager;
    /**
     * 接口提供类
     */
    @Resource
    private ApiService apiFeignService;
    /*--------------------旗舰店预约相关接口 start-------------------------**/

    @Resource
    private HaViewSxServiceGuideManager haViewSxServiceGuideManager;

    @Resource
    private DbHaWorkQueueMapper dbHaWorkQueueMapper;
    /**
     * 工作人员排班
     */
    @Resource
    DbHaWorkUserScheduleMapper dbHaWorkUserScheduleMapper;

    @Resource
    private DbHaVisitMapper dbHaVisitMapper;

    @Resource
    private DbHaWorkUserMapper haWorkUserMapper;

    @Resource
    private SysConfigFeignService sysConfigFeignService;

    @Resource
    private HaBusinessService haBusinessService;

    @Resource
    private SysAttaManager sysAttaManager;

    @Resource
    private QlCaseMaterialAttaService qlCaseMaterialAttaServiceFeginService;

    @Resource
    private HaWorkLoginLocationServiceManager haWorkLoginLocationServiceManager;
    /**
     * 视频咨询service层
     */
    @Resource
    private HaVideoConsultationManager haVideoConsultationManager;

    /**
     * 办件快递service
     */
    @Resource
    private HaCaseExpressManager haCaseExpressManager;


    @Value("${zfsoft.inter.url}")
    private String interUrl;

    @Resource
    private SysDistrictService sysDistrictService;
    @Value("${zfsoft.inter.scanCertQrCode}")
    private String scanCertQrCode;
    /**
     * redis
     */
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * @description:
     * 10+N个营商服务点的在线状态及视频通讯
     * @author: Wangyh
     * @Date: 2023/4/11 13:10
     **/
    @Override
    public ApiResultSet getYsfuStatusAndVideo() {
        log.info("进入10+N个营商服务点的在线状态及视频通讯方法");
        List<DoingBusinessVo> doingBusinessVoList = haWorkUserServiceManager.queryTenUserList();
        return ApiResultSet.ok("查询成功", doingBusinessVoList);
    }

    /**
     * @description:
     * 9个政务窗口的在线状态及视频通讯
     * @author: Wangyh
     * @Date: 2023/4/11 13:10
     **/
    @Override
    public ApiResultSet getXzWindowStatus() {
        log.info("进入9个政务窗口的在线状态及视频通讯方法");
        List<ZwWindowStatusVo> zwWindowStatusVoList = haWorkUserServiceManager.queryNineUserList();
       /* List<ZwWindowStatusVo> list = new ArrayList<>();
        ZwWindowStatusVo zwWindowStatusVo1 = new ZwWindowStatusVo();
        zwWindowStatusVo1.setName("张咪");
        zwWindowStatusVo1.setDistrictName("南京东路街道");
        zwWindowStatusVo1.setServicePostion("C区");
        zwWindowStatusVo1.setIsStatus("2");
        zwWindowStatusVo1.setIsVideoStatus("1");
        zwWindowStatusVo1.setRoomId("312982218892083200");
        zwWindowStatusVo1.setVideoNum("100009");
        zwWindowStatusVo1.setVideoSig("eJw1zLkOgkAUheF3mdrgqGFYEgsYEgsQRHApbAgM5DJsQRaD8d0FxPJ8J-nfyLc8oWM1UtFWwGg1b4hY0UAMM28wxuJy1GWZj1S0WbbIM*JBVUE0qh9fdSfhHjVM27booc1omNLCvNSk0gP*j7NXBTVD6tT9SQP5tIkkE0UmooJlaYlDMnbPOmgx6bWex11xEoc*gMea7dJbeHwNrpMbLXN7qgTm-cL36PMF171AAg__");
        list.add(zwWindowStatusVo1);

        ZwWindowStatusVo zwWindowStatusVo2 = new ZwWindowStatusVo();
        zwWindowStatusVo2.setName("张初");
        zwWindowStatusVo2.setDistrictName("外滩街道");
        zwWindowStatusVo2.setServicePostion("C区");
        zwWindowStatusVo2.setIsStatus("2");
        zwWindowStatusVo2.setIsVideoStatus("1");
        zwWindowStatusVo2.setRoomId("312982218892083200");
        zwWindowStatusVo2.setVideoNum("100009");
        zwWindowStatusVo2.setVideoSig("eJw1zLkOgkAUheF3mdrgqGFYEgsYEgsQRHApbAgM5DJsQRaD8d0FxPJ8J-nfyLc8oWM1UtFWwGg1b4hY0UAMM28wxuJy1GWZj1S0WbbIM*JBVUE0qh9fdSfhHjVM27booc1omNLCvNSk0gP*j7NXBTVD6tT9SQP5tIkkE0UmooJlaYlDMnbPOmgx6bWex11xEoc*gMea7dJbeHwNrpMbLXN7qgTm-cL36PMF171AAg__");
        list.add(zwWindowStatusVo2);

        ZwWindowStatusVo zwWindowStatusVo3 = new ZwWindowStatusVo();
        zwWindowStatusVo3.setName("张宁");
        zwWindowStatusVo3.setDistrictName("瑞金二路街道");
        zwWindowStatusVo3.setServicePostion("C区");
        zwWindowStatusVo3.setIsStatus("2");
        zwWindowStatusVo3.setIsVideoStatus("1");
        zwWindowStatusVo3.setRoomId("312982218892083200");
        zwWindowStatusVo3.setVideoNum("100009");
        zwWindowStatusVo3.setVideoSig("eJw1zLkOgkAUheF3mdrgqGFYEgsYEgsQRHApbAgM5DJsQRaD8d0FxPJ8J-nfyLc8oWM1UtFWwGg1b4hY0UAMM28wxuJy1GWZj1S0WbbIM*JBVUE0qh9fdSfhHjVM27booc1omNLCvNSk0gP*j7NXBTVD6tT9SQP5tIkkE0UmooJlaYlDMnbPOmgx6bWex11xEoc*gMea7dJbeHwNrpMbLXN7qgTm-cL36PMF171AAg__");
        list.add(zwWindowStatusVo3);

        ZwWindowStatusVo zwWindowStatusVo4 = new ZwWindowStatusVo();
        zwWindowStatusVo4.setName("王琦");
        zwWindowStatusVo4.setDistrictName("淮海中路街道");
        zwWindowStatusVo4.setServicePostion("C区");
        zwWindowStatusVo4.setIsStatus("2");
        zwWindowStatusVo4.setIsVideoStatus("1");
        zwWindowStatusVo4.setRoomId("312982218892083200");
        zwWindowStatusVo4.setVideoNum("100009");
        zwWindowStatusVo4.setVideoSig("eJw1zLkOgkAUheF3mdrgqGFYEgsYEgsQRHApbAgM5DJsQRaD8d0FxPJ8J-nfyLc8oWM1UtFWwGg1b4hY0UAMM28wxuJy1GWZj1S0WbbIM*JBVUE0qh9fdSfhHjVM27booc1omNLCvNSk0gP*j7NXBTVD6tT9SQP5tIkkE0UmooJlaYlDMnbPOmgx6bWex11xEoc*gMea7dJbeHwNrpMbLXN7qgTm-cL36PMF171AAg__");
        list.add(zwWindowStatusVo4);

        ZwWindowStatusVo zwWindowStatusVo5 = new ZwWindowStatusVo();
        zwWindowStatusVo5.setName("李策");
        zwWindowStatusVo5.setDistrictName("豫园街道");
        zwWindowStatusVo5.setServicePostion("C区");
        zwWindowStatusVo5.setIsStatus("2");
        zwWindowStatusVo5.setIsVideoStatus("1");
        zwWindowStatusVo5.setRoomId("312982218892083200");
        zwWindowStatusVo5.setVideoNum("100009");
        zwWindowStatusVo5.setVideoSig("eJw1zLkOgkAUheF3mdrgqGFYEgsYEgsQRHApbAgM5DJsQRaD8d0FxPJ8J-nfyLc8oWM1UtFWwGg1b4hY0UAMM28wxuJy1GWZj1S0WbbIM*JBVUE0qh9fdSfhHjVM27booc1omNLCvNSk0gP*j7NXBTVD6tT9SQP5tIkkE0UmooJlaYlDMnbPOmgx6bWex11xEoc*gMea7dJbeHwNrpMbLXN7qgTm-cL36PMF171AAg__");
        list.add(zwWindowStatusVo5);

        ZwWindowStatusVo zwWindowStatusVo6 = new ZwWindowStatusVo();
        zwWindowStatusVo6.setName("刘武");
        zwWindowStatusVo6.setDistrictName("打浦桥街道");
        zwWindowStatusVo6.setServicePostion("C区");
        zwWindowStatusVo6.setIsStatus("2");
        zwWindowStatusVo6.setIsVideoStatus("1");
        zwWindowStatusVo6.setRoomId("312982218892083200");
        zwWindowStatusVo6.setVideoNum("100009");
        zwWindowStatusVo6.setVideoSig("eJw1zLkOgkAUheF3mdrgqGFYEgsYEgsQRHApbAgM5DJsQRaD8d0FxPJ8J-nfyLc8oWM1UtFWwGg1b4hY0UAMM28wxuJy1GWZj1S0WbbIM*JBVUE0qh9fdSfhHjVM27booc1omNLCvNSk0gP*j7NXBTVD6tT9SQP5tIkkE0UmooJlaYlDMnbPOmgx6bWex11xEoc*gMea7dJbeHwNrpMbLXN7qgTm-cL36PMF171AAg__");
        list.add(zwWindowStatusVo6);

        ZwWindowStatusVo zwWindowStatusVo7 = new ZwWindowStatusVo();
        zwWindowStatusVo7.setName("武清");
        zwWindowStatusVo7.setDistrictName("老西门街道");
        zwWindowStatusVo7.setServicePostion("C区");
        zwWindowStatusVo7.setIsStatus("2");
        zwWindowStatusVo7.setIsVideoStatus("1");
        zwWindowStatusVo7.setRoomId("312982218892083200");
        zwWindowStatusVo7.setVideoNum("100009");
        zwWindowStatusVo7.setVideoSig("eJw1zLkOgkAUheF3mdrgqGFYEgsYEgsQRHApbAgM5DJsQRaD8d0FxPJ8J-nfyLc8oWM1UtFWwGg1b4hY0UAMM28wxuJy1GWZj1S0WbbIM*JBVUE0qh9fdSfhHjVM27booc1omNLCvNSk0gP*j7NXBTVD6tT9SQP5tIkkE0UmooJlaYlDMnbPOmgx6bWex11xEoc*gMea7dJbeHwNrpMbLXN7qgTm-cL36PMF171AAg__");
        list.add(zwWindowStatusVo7);

        ZwWindowStatusVo zwWindowStatusVo8 = new ZwWindowStatusVo();
        zwWindowStatusVo8.setName("聂耳");
        zwWindowStatusVo8.setDistrictName("小东门街道");
        zwWindowStatusVo8.setServicePostion("C区");
        zwWindowStatusVo8.setIsStatus("2");
        zwWindowStatusVo8.setIsVideoStatus("1");
        zwWindowStatusVo8.setRoomId("312982218892083200");
        zwWindowStatusVo8.setVideoNum("100009");
        zwWindowStatusVo8.setVideoSig("eJw1zLkOgkAUheF3mdrgqGFYEgsYEgsQRHApbAgM5DJsQRaD8d0FxPJ8J-nfyLc8oWM1UtFWwGg1b4hY0UAMM28wxuJy1GWZj1S0WbbIM*JBVUE0qh9fdSfhHjVM27booc1omNLCvNSk0gP*j7NXBTVD6tT9SQP5tIkkE0UmooJlaYlDMnbPOmgx6bWex11xEoc*gMea7dJbeHwNrpMbLXN7qgTm-cL36PMF171AAg__");
        list.add(zwWindowStatusVo8);

        ZwWindowStatusVo zwWindowStatusVo9 = new ZwWindowStatusVo();
        zwWindowStatusVo9.setName("朱明");
        zwWindowStatusVo9.setDistrictName("五里桥街道");
        zwWindowStatusVo9.setServicePostion("C区");
        zwWindowStatusVo9.setIsStatus("2");
        zwWindowStatusVo9.setIsVideoStatus("1");
        zwWindowStatusVo9.setRoomId("312982218892083200");
        zwWindowStatusVo9.setVideoNum("100009");
        zwWindowStatusVo9.setVideoSig("eJw1zLkOgkAUheF3mdrgqGFYEgsYEgsQRHApbAgM5DJsQRaD8d0FxPJ8J-nfyLc8oWM1UtFWwGg1b4hY0UAMM28wxuJy1GWZj1S0WbbIM*JBVUE0qh9fdSfhHjVM27booc1omNLCvNSk0gP*j7NXBTVD6tT9SQP5tIkkE0UmooJlaYlDMnbPOmgx6bWex11xEoc*gMea7dJbeHwNrpMbLXN7qgTm-cL36PMF171AAg__");
        list.add(zwWindowStatusVo9);*/
        return ApiResultSet.ok("查询成功", zwWindowStatusVoList);
    }

    /**
     * @description:
     * 快捷帮办及圆桌帮办的今日、今年接待人数
     * @author: Wangyh
     * @Date: 2023/4/10 13:10
     **/
    @Override
    public ApiResultSet getTodayAnaToyearNum() {
        log.info("快捷帮办及圆桌帮办的今日、今年接待人数");
        NumberOfServiceVo numberOfServiceVo = haWorkQueueManager.getTodayAnaToyearNum();
        return ApiResultSet.ok("查询成功", numberOfServiceVo);
    }

    /**
     * @description:
     * 获取圆桌，快捷当前等待人数
     * @author: Wangyh
     * @Date: 2023/4/10 13:10
     **/
    @Override
    public ApiResultSet getQuickAndRoundNum() {
        log.info("进入获取圆桌，快捷当前等待人数方法");
        QuickAndRoundNumVo queueNumVo = haWorkQueueManager.getQuickAndRoundNum();
        return ApiResultSet.ok("查询成功", queueNumVo);
    }

    /**
     * @description:
     * 获取所有的圆桌办事人员名字及其目前所登录的帮办区域
     * @author: Wangyh
     * @Date: 2023/4/7 13:10
     **/
    @Override
    public ApiResultSet getWorkUserList() {
        log.info("获取所有的圆桌办事人员名字及其目前所登录的帮办区域");
        List<HaWorkUserList> workUserLists = null;
        try {
            workUserLists = queueManager.getWorkUserListDp();
        } catch (Exception e) {
            log.error("获取所有的圆桌办事人员名字及其目前所登录的帮办区域方法错误：", e);
            return new ApiResultSet(500, "获取所有的圆桌办事人员名字及其目前所登录的帮办区域方法错误", e.getMessage());
        }
        return ApiResultSet.ok("获取所有的圆桌办事人员名字及其目前所登录的帮办区域列表成功", workUserLists);
    }

    /**
     * @description:
     * 获取token,用户提供给第三方鉴权使用
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @Override
    public ApiResultSet getToken(String userName, String userId) {
        log.info("获取token,用户提供给第三方鉴权使用");
        if(StringUtils.isEmpty(userName)){
            return new ApiResultSet<>(500,"userName不能为空");
        }

        //生成token
        String token = UUID.randomUUID().toString().replace("-", "");
        //将token为key value为对象用户信息存入redis中 时间期限是2小时
        redisTemplate.opsForValue().set(LOGIN_DOCKING_SESSION_TOKEN+token ,userName,
                LOGIN_DOCKING_SESSION_TTL, TimeUnit.DAYS);

        return ApiResultSet.ok("获取token成功",token);
    }
    /**
     * 根据身份证查询当前办事人的排队情况
     * GET /ha/outer/querylineUpByIdNo
     * @param IdNo 身份证号码
     * @date 2022/11/2 13:31
     */
    @Override
    public ApiResultSet querylineUpByIdNo(String IdNo) {
        log.info("根据身份证查询当前办事人的排队情况");
        if(StringUtil.isNotEmpty(IdNo)){
            List<HaWorkQueue> workQueueList = haWorkQueueManager.selectByIdNo(IdNo);
            List<Map<String,Object>> list = new ArrayList<>();
            for (HaWorkQueue workQueue : workQueueList) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", workQueue.getName());
                map.put("phone", workQueue.getPhone());
                //等待服务人数
                Integer WaitServiceNum = dbHaWorkQueueMapper.countWaitService(DateUtil.getTodayStartTime(), workQueue.getDistributeTime()) - 1; //减一是因为查询的时候，起止的结束时间条件是自己分配的时间，所有查的时候连自己算在内了，这里减一是减去自己。因为其他业务也用到同一个sql，所有代码里进行了减一操作
                map.put("WaitServiceNum", WaitServiceNum);
                HaWorkUser haWorkUser = haWorkUserServiceManager.selectByid(workQueue.getServiceWorkUserId());
                Integer estimatedWaitingTime = haWorkUser.getAvgServiceTime() * WaitServiceNum; //预计等待时长
                map.put("estimatedWaitingTime", estimatedWaitingTime + "m");
                list.add(map);
            }
            return ApiResultSet.ok("接口调用成功",list);
        }
        return new ApiResultSet<>(500,"IdNo不能为空");

    }

    /**
     * 帮办区域整体排队情况
     * GET /ha/outer/getWorkUserVo
     * @date 2022/9/27 13:31
     */
    @Override
    public ApiResultSet queryAssistantRegionlineUp() throws ServiceException {
        log.info("帮办区域整体排队情况");
        ApiResultSet apiResultSet = new ApiResultSet();
        try{
            Map<String,Object> map = new HashMap<>();
            //获取帮办服务人数
            Integer assistantNum = dbHaWorkUserScheduleMapper.countWorkUsers(DateUtil.getNYR());
            //查询开始时间00:00:00
            Date startTime = DateUtil.getTodayStartTime();
            //查询结束时间23:59:59
            Date endTime = DateUtil.getTodayEndTime();
            //等待服务人数
            Integer WaitServiceNum = dbHaWorkQueueMapper.countWaitService(startTime,endTime);
            //正在服务人数
            Integer inServiceNum = dbHaWorkQueueMapper.inServiceNum(startTime,endTime);
            //累计服务人数
            Integer cumulativeServiceNum = dbHaWorkQueueMapper.completeServiceNum(startTime,endTime);
            //来访人数
            Integer visitNum = dbHaVisitMapper.countVist();
            //平均服务时长
            Integer avgServiceTime = haWorkUserMapper.avgServiceTime(); //获取所有员工总时长
            Integer countServicenum = haWorkUserMapper.countServicenum();
            Integer avgServiceNum = avgServiceTime/countServicenum;
            //平均等待时长
            Integer avgWaitTime =queueManager.queryWorkUserScheduleList(DateUtil.getNYR());
            map.put("assistantNum",assistantNum); //帮办服务人数
            map.put("WaitServiceNum",WaitServiceNum);//等待服务人数
            map.put("inServiceNum",inServiceNum);//正在服务人数
            map.put("cumulativeServiceNum",cumulativeServiceNum);//累计服务人数
            map.put("visitNum",visitNum);//来访人数
            map.put("avgServiceNum",avgServiceNum);//平均服务时长
            map.put("avgWaitTime",avgWaitTime);//平均等待时长
            apiResultSet.setData(map);
        }catch (Exception e){
            return new ApiResultSet<>(501,"接口异常");
        }
        return apiResultSet;
    }

    @Override
    public ApiResultSet getWorkUserVo(String workUserID) {
        HaWorkQueueResponseVo haWorkQueueResponseVo =queueManager.queryWorkUserVo(workUserID);
        WorkUserRespVo workUserRespVo = new WorkUserRespVo();
        BeanUtils.copyProperties(haWorkQueueResponseVo,workUserRespVo);
        ApiResultSet apiResultSet = new ApiResultSet();
        apiResultSet.setData(workUserRespVo);
        apiResultSet.setMessage("接口调用成功");
        return apiResultSet;
    }

    /**
     * 一键推送办事指南查询
     * GET /ha/outer/viewSxServiceGuide
     * @param caseOid 办件业务主键
     * @date 2022/9/27 13:31
     */
    @Override
    public ApiResultSet getWorkUserSchedule(String caseOid) {
        log.info("一键推送办事指南查询:{}",caseOid);
        Map<String,Object> mapResult = new HashMap<>();
        //办件信息
        QlCase qlCaseOld = qlCaseManager.queryQlCaseByCaseOid(caseOid);
        if(qlCaseOld!=null){
            //获取办件信息
            ServiceRespVo serviceRespVo = haViewSxServiceGuideManager.getServiceInfo(qlCaseOld.getServiceOid());
            //扩展信息
            ServiceExtendRespVo serviceExtendRespVo =haViewSxServiceGuideManager.getServiceExtendRespVo(qlCaseOld.getServiceOid());
            //常见问题
            List<ServiceQuestionsRespVo> serviceQuestionsRespVoList = haViewSxServiceGuideManager.getServiceQuestionsRespVo(qlCaseOld.getServiceOid());
            //受理信息
            List<AcceptConditionsRespVo> acceptConditionsList = haViewSxServiceGuideManager.getAcceptConditionsList(qlCaseOld.getServiceOid());
            //办理环节
//        List<ServiceLinksRespVo> serviceLinksRespVoList = haViewSxServiceGuideManager.getServiceLinksList(qlCaseOld.getServiceOid());
            //申请材料
            List<ServiceMaterialsRespVo> serviceMaterialsRespVoList =haViewSxServiceGuideManager.getServiceMaterialsList(caseOid,qlCaseOld.getServiceOid());
            //条件预检
            List<ConditionalrulesRespVo> conditionalrulesRespVoList = haViewSxServiceGuideManager.getSxConditionalRulesList(qlCaseOld.getServiceOid());
            //填报须知
            List<FillNoticeRespVo> fillNoticeRespVoList = haViewSxServiceGuideManager.queryComPro(caseOid,qlCaseOld.getServiceOid());
            mapResult.put("sxServiceQuestions",serviceQuestionsRespVoList); //常见问题
            mapResult.put("sxAcceptConditions",acceptConditionsList);//受理条件
            mapResult.put("sxService",serviceRespVo); //事项信息
            mapResult.put("sxServiceMaterials",serviceMaterialsRespVoList); //申请材料
            mapResult.put("sxServiceExtend",serviceExtendRespVo);//申请材料
            mapResult.put("sxConditionalrules",conditionalrulesRespVoList); //条件预检
            mapResult.put("fillNotice",fillNoticeRespVoList );//填报须知
        }else{
           return new ApiResultSet<>(500,"没有办件信息");
        }
        ApiResultSet apiResultSet = new ApiResultSet();
        apiResultSet.setData(mapResult);
        return apiResultSet;
        }



    /**
     * 接收旗舰店的预约数据
     *
     * @param appReq 预约推送请求参数
     * @return ApiResultSet 返回预约结果信息
     * @author yupeng
     * @date 2022/7/28 15:00
     */
    @Override
    public ApiResultSet pushAppointment(@RequestBody @Valid HaAppointmentRequestData appReq) {
        log.info("旗舰店预约推送数据：{}", appReq);
        //判断当前排班是否可预约
        HaWorkUserSchedule schedule = haWorkUserScheduleManager.getByScheduleId(appReq.getScheduleId());
        if(schedule==null){
            return new ApiResultSet(ApiResultSet.PARAM_VALID_ERROR,"根据排班id获取不到排班信息");
        }
        //根据预约状态判断
        if(!"1".equals(schedule.getScheduleStatus())){
            return new ApiResultSet(ApiResultSet.PARAM_VALID_ERROR,"当天不能预约");
        }
        //根据当天排班情况判断
        if("4".equals(schedule.getAmOrPm())){
            return new ApiResultSet(ApiResultSet.PARAM_VALID_ERROR,"当天帮办人员休息，不能预约");
        }
        //如果帮办上午值班，不能预约下午时间段
        if("1".equals(schedule.getAmOrPm())&&"4,5".contains(appReq.getAppointmentTimePeriod())){
            return new ApiResultSet(ApiResultSet.PARAM_VALID_ERROR,"当天帮办人员下午休息，不能预约");
        }
        if("2".equals(schedule.getAmOrPm())&&"1,2,3".contains(appReq.getAppointmentTimePeriod())){
            return new ApiResultSet(ApiResultSet.PARAM_VALID_ERROR,"当天帮办人员上午休息，不能预约");
        }
        // 判断当前排班日期是否约满
        HaAppointment dbHaAppointment = new HaAppointment();
        dbHaAppointment.setScheduleId(appReq.getScheduleId());
        dbHaAppointment.setAppointmentTimePeriod(appReq.getAppointmentTimePeriod());
        List<HaAppointment> HaAppointments = haAppointmentManager.queryAppointmentList(dbHaAppointment);
        List<HaAppointment> collect = HaAppointments.stream().filter(item -> Boolean.TRUE.equals("1,2".contains(item.getAppointmentStatus()))).collect(Collectors.toList());
        if(collect.size()>=2){
            return new ApiResultSet(ApiResultSet.PARAM_VALID_ERROR,"当前时段帮办人员已约满，不能预约");
        }
        //  判断当前身份证是否已经预约过当前日期的当前帮办人
        List<HaAppointment> collect2 = HaAppointments.stream().filter(item ->
                item.getCardNo().equals(appReq.getCardNo())&&
                item.getAppointmentWorkUserId().equals(appReq.getAppointmentWorkUserId()))
                .collect(Collectors.toList());
        if(collect2.size()>0){
            return new ApiResultSet(ApiResultSet.PARAM_VALID_ERROR,"当天帮办人员已预约过该时段，不能重复预约");
        }
        //保存预约信息
        String result = haAppointmentManager.savePushAppointment(appReq);
        if (StringUtil.isEmpty(result)) {
            return ApiResultSet.ok();
        } else {
            return new ApiResultSet(ApiResultSet.PARAM_VALID_ERROR, result);
        }
    }




    /**
     * @return
     * @description: 获取分组列表
     * @author: zhaobf
     * @date: 2022-08-26 10:22
     */
    @Override
    public ApiResultSet getGroupList() {
        log.info("进入获取分组列表Controller");
        List<HaWorkUserGroupResponseData> haWorkUserGroupResponseDataList = haWorkGrouoServiceManager.getGroupList("");
        return ApiResultSet.ok("查询成功", haWorkUserGroupResponseDataList);
    }

    /**
     * 根据组编号获取帮代办人员列表  可预约
     * @param groupId 组别id
     * @date 2022/9/22 13:31:09 zhaobf
     */
    @Override
    public ApiResultSet getWorkUserList(Long groupId) {
        log.info("进入获取分组列表Controller");
        List<HaWorkUserVo> haWorkUsers = haWorkUserServiceManager.selectByGroupId(groupId);
        return ApiResultSet.ok("查询成功", haWorkUsers);
    }


    /**
     * 获取工作人员的平均评价得分
     *
     * @return 工作人员萍爵分
     * @author yupeng
     * @date 2022年08月15 14:03:09
     */
    @Override
    public ApiResultSet getWorkUserEvalScore(Long workUserId) {
        log.info("获取工作人员的平均评价得分");
        BigDecimal evalScore = haEvalManager.getWorkUserEvalScore(workUserId);
        JSONObject result = new JSONObject();
        result.put("evalScore", evalScore);
        return ApiResultSet.ok(result);
    }


    /**
     * 获取帮办人员七天的预约排班数据
     * @param workUserId 员工id
     * @date 2022/9/22 13:31:09 zhaobf
     */
    @Override
    public ApiResultSet getWorkUserSchedule(Long workUserId) {
        if(workUserId==null){
            return new ApiResultSet(ApiResultSet.PARAM_VALID_ERROR,"帮办人员id不能为空");
        }
        String startDate = cn.hutool.core.date.DateUtil.format(DateUtil.getBeginADay(), "yyyy-MM-dd HH:mm:ss");
        String endDate = cn.hutool.core.date.DateUtil.format(DateUtil.getTimeByDayNumb(7), "yyyy-MM-dd HH:mm:ss");
        List<HaWorkUserSchedule_AppVo> schedules = haWorkUserScheduleManager.queryScheAppList(workUserId,startDate,endDate,null);
        //获取系统配置的帮办人员每日各时间段最大的预约数
        SysConfig sysConfig = sysConfigFeignService.getSysConfigByCode(HA_SCHE_APPOIN_NUM_MAX).getData();
        String maxNumb = "";
        if(null!=sysConfig){
            maxNumb = sysConfig.getValue();
        }
        String finalMaxNumb = maxNumb;
        //遍历，获取每天的时间段预约数据
        List<HaWorkUserScheduleResponseData> haWorkUserSchedules = schedules.stream().map(schedule_appVo -> {

            HaWorkUserScheduleResponseData responseData = new HaWorkUserScheduleResponseData();
            com.zfsoft.platform.utils.bean.BeanUtils.copyProperties(schedule_appVo, responseData);

            responseData.setTimePeriod(getTimePeriod(schedule_appVo, finalMaxNumb));
            return responseData;
        }).collect(Collectors.toList());
        return ApiResultSet.ok("获取帮办人员七天的预约排班数据请求成功", haWorkUserSchedules);
    }

    private List<Map<String,String>> getTimePeriod(HaWorkUserSchedule_AppVo schedule_appVo,String maxNumb) {
        List<Map<String,String>> item =  new ArrayList<>();
        Map<String,String> map1 = new HashMap<>();
//        map1.put("appointmentTimePeriod","1");
        map1.put("appoTimePeriod","8:30-9:29");
        map1.put("appoStatus", this.getAppoStatus(schedule_appVo,"am"));
        map1.put("appoNumber", schedule_appVo.getT_8_9());
        map1.put("appoNumberMax",maxNumb);
        item.add(map1);
        Map<String,String> map2 = new HashMap<>();
//        map2.put("appointmentTimePeriod","2");
        map2.put("appoTimePeriod","9:30-10:29");
        map2.put("appoStatus", this.getAppoStatus(schedule_appVo,"am"));
        map2.put("appoNumber", schedule_appVo.getT_9_10());
        map2.put("appoNumberMax",maxNumb);
        item.add(map2);
        Map<String,String> map3 = new HashMap<>();
//        map4.put("appointmentTimePeriod","3");
        map3.put("appoTimePeriod","10:30-11:29");
        map3.put("appoStatus", this.getAppoStatus(schedule_appVo,"am"));
        map3.put("appoNumber", schedule_appVo.getT_10_11());
        map3.put("appoNumberMax",maxNumb);
        item.add(map3);
        Map<String,String> map4 = new HashMap<>();
//        map4.put("appointmentTimePeriod","4");
        map4.put("appoTimePeriod","13:30-14:29");
        map4.put("appoStatus", this.getAppoStatus(schedule_appVo,"pm"));
        map4.put("appoNumber", schedule_appVo.getT_13_14());
        map4.put("appoNumberMax",maxNumb);
        item.add(map4);
        Map<String,String> map5 = new HashMap<>();
//        map5.put("appointmentTimePeriod","5");
        map5.put("appoTimePeriod","14:30-15:29");
        map5.put("appoStatus", this.getAppoStatus(schedule_appVo,"pm"));
        map5.put("appoNumber", schedule_appVo.getT_14_15());
        map5.put("appoNumberMax",maxNumb);
        item.add(map5);
        return item;
    }
    //获取办事人预约状态
    private String getAppoStatus(HaWorkUserSchedule_AppVo schedule_appVo,String amOrPm) {
        if(!"1".equals(schedule_appVo.getScheduleStatus())){
            return "不可预约";
        }
        if("4".equals(schedule_appVo.getAmOrPm())){
            return "不可预约";
        }
        if("3".equals(schedule_appVo.getAmOrPm())){
            return "可以预约";
        }
        if("2".equals(schedule_appVo.getAmOrPm())&&"pm".equals(amOrPm)){
            return "可以预约";
        }
        if("1".equals(schedule_appVo.getAmOrPm())&&"am".equals(amOrPm)){
            return "可以预约";
        }
        return "不可预约";

    }

    /**
     * 获取预约数据
     * GET /ha/outer/getWorkUserAppoinment
     * @param cardNo 身份证号
     * @date 2022/9/22 13:31 zhaobf
     */
    @Override
    public ApiResultSet<PageResult<HaAppointmentVo>> getWorkUserAppoinment(String cardNo, Integer pageNum, Integer pageSize) {
        PageResult<HaAppointment> resultSet = haAppointmentManager.queryAppointmentPage(null,cardNo, null, null, pageNum, pageSize);
        List<HaAppointment> data = resultSet.getData();

        PageResult<HaAppointmentVo> result = new PageResult<>(resultSet.getPageNum(), resultSet.getPageSize(), resultSet.getTotal());

        List<HaAppointmentVo> haAppointmentList = data.stream().map(dbHaAppointmentVo -> {
            HaAppointmentVo haAppointment = new HaAppointmentVo();
            com.zfsoft.platform.utils.bean.BeanUtils.copyProperties(dbHaAppointmentVo, haAppointment);
            return haAppointment;
        }).collect(Collectors.toList());
        result.setData(haAppointmentList);

        return ApiResultSet.ok("获取预约数据成功", result);
    }

    @Override
    public ApiResultSet appointmentRemove(Long appointmentId) {
        if(appointmentId==null){
            return new ApiResultSet(ApiResultSet.PARAM_VALID_ERROR,"预约id不能为空");
        }
        int i= haAppointmentManager.remove(appointmentId);
        if(i==0){
            return ApiResultSet.ok("取消预约失败", i);
        }
        if(i<0){
            return ApiResultSet.ok("取消预约失败,没找到指定的预约记录,可能已经取消", i);
        }
        return ApiResultSet.ok("取消预约成功", i);
    }


    /**
     * 根据身份证查询当天的预约信息
     * @param cardNo 身份证号码
     * @return
     */
    @Override
    public ApiResultSet<List<HaAppointmentVo>> queryAppointmentInfo(String cardNo) {
        log.info("预约记录：：进入根据预约信息获取预约列表:"+cardNo);
//        List<HaAppointmentResponseData> haAppointmentRespList;
        List<HaAppointmentVo> haAppointments;
        try {
            //根据身份证号码获取预约信息
//            HaAppointment ha = new HaAppointment();
//            ha.setCardNo(cardNo);
//            ha.setCompanyCode(companyCode);
             haAppointments = haAppointmentManager.queryAppointmentByCardNo(cardNo, new Date(), new Date(),"1");
//            haAppointmentRespList = haAppointments.stream().map(haUserResource -> {
//                HaAppointmentResponseData haAppointmentResp = new HaAppointmentResponseData();
//                org.springframework.beans.BeanUtils.copyProperties(haUserResource, haAppointmentResp);
//                return haAppointmentResp;
//            }).collect(Collectors.toList());
            if(haAppointments==null||haAppointments.size()==0){
                return  new ApiResultSet<>(501,"根据预约记录查询不到预约信息");
            }
            log.info("预约记录：：根据预约信息获取预约列表成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("预约记录：根据预约信息获取预约列表错误："+e.getMessage());
            return  new ApiResultSet<>(500,"预约记录：根据身份证和社会统一信用代码获取预约列表操作错误",e.getMessage());

        }
        return ApiResultSet.ok("预约记录：根据身份证和社会统一信用代码获取预约列表:操作成功",haAppointments);

    }
    /*--------------------旗舰店预约相关接口 end-------------------------**/

    /**
     * 排队叫号推送
     *
     * @param haQueueRequestData 排队叫号推送请求参数
     * @return ApiResultSet 返回排队叫号推送结果信息
     * @author zhaobf
     * @date 2022/7/28  15:00
     */
    @Override
    public ApiResultSet pushNumber(HaQueueRequestData haQueueRequestData) {
        log.info("排队叫号推送数据：{}", haQueueRequestData);
        String result = queueManager.savePushNumber(haQueueRequestData);
        HaScanHelpInfoRequestData haScanHelpInfoRequestData = new HaScanHelpInfoRequestData();
        BeanUtils.copyProperties(haQueueRequestData,haScanHelpInfoRequestData);
        if(!haVisitManager.saveHavisitByhelpInfo(haScanHelpInfoRequestData)) {
            log.info("排队叫号推送数据，添加到来访信息时错误");
        }
        if (StringUtil.isEmpty(result)) {
            return ApiResultSet.ok();
        } else {
            return new ApiResultSet(ApiResultSet.PARAM_VALID_ERROR, result);
        }
    }

    /**
     * 获取帮代办服务的列表
     *
     * @param cardNo      身份证号码
     * @param name        姓名
     * @param companyName 企业名称
     * @param companyCode 统一社会信用代码证
     * @return 帮代办服务的列表
     * @author yupeng
     * @date 2022年08月09 14:27:06
     */
    @Override
    public ApiResultSet getHaServiceList(String cardNo, String name, String companyName, String companyCode) {
        log.info("获取帮代办服务的列表，cardNo：{}，name：{}，companyName：{}，companyCode：{}", cardNo, name, companyName, companyCode);
        return haAppointmentManager.getHaServiceList(cardNo, name, companyName, companyCode);
    }


    /**
     * 材料上传
     *
     * @param files   材料信息
     * @param request HttpServletRequest
     * @return 上传结果信息
     * @author yupeng
     * @date 2022年08月11 18:17:33
     */
    @Override
    public ApiResultSet<List<QlSysAtta>> uploadCaseMaterialFile(HttpServletRequest request, MultipartFile[] files) {
        ApiResultSet<List<QlSysAtta>> apiResultSet = qlCaseAttaFileFeignService.uploadCaseMaterialFile(request, files);
        return apiResultSet;
    }


    /**
     * 获取办件信息
     *
     * @param caseOid 办件oid
     * @return 返回办件信息
     * @author yupeng
     * @date 2022年08月11 18:26:53
     */
    @Override
    public ApiResultSet getCaseInfo(String caseOid) {
        log.info("获取办件信，caseOid：{}", caseOid);
        ApiResultSet<QlCaseApplay> apiResultSet = qlCaseApplayService.queryQlCaseApplayByCaseOid(caseOid);
        return apiResultSet;
    }

    /**
     * 更新材料关联
     *
     * @param qlCaseMaterialList 材料信息
     * @return 返回更新材料结果
     * @author yupeng
     * @date 2022年08月11 18:31:26
     */
    @Override
    public ApiResultSet<List<Map<String, String>>> updateQlCaseMaterialListForBrowser(List<QlCaseMaterial> qlCaseMaterialList) {
        ApiResultSet<List<Map<String, String>>> apiResultSet = qlCaseMaterialServiceFeginService.updateQlCaseMaterialListForBrowser(qlCaseMaterialList);
        return apiResultSet;
    }



    /**
     * @return
     * @description: 获取分组列表 将帮办类型和组别区分，用于取号页面显示
     * @author: zhaobf
     * @date: 2022-09-19 10:12
     */
    @Override
    public ApiResultSet<Map<String,List<Map<String,Object>>>> getGroupListSub() {
        log.info("进入获取分组列表Controller");
        Map<String,List<Map<String,Object>>> map = haWorkGrouoServiceManager.getGroupListSub();
        return ApiResultSet.ok("查询成功", map);
    }
    @Override
    public ApiResultSet<HaVisit> getPhoneByCardNo(String cardNo) {
        HaVisit haVisit = haVisitManager.getPhoneByCardNo(cardNo);
        return ApiResultSet.ok("查询成功", haVisit);
    }


    /**
     * 更新修改办件材料附件
     *
     * @param qlCaseMaterialList 材料附件集合
     * @return 保存结果
     * @author wangyh
     * @date 2022年09月06 10:01:29
     */
    @Override
    public ApiResultSet saveOrUpdateCaseMaterialAttaList(List<QlCaseMaterial> qlCaseMaterialList) {
        log.info("更新材料附件");
        Map<String, Object> modelMap = new HashMap<>();
        ApiResultSet apiResultSet = new ApiResultSet();
        List<QlCaseMaterialAtta> list = qlCaseMaterialAttaManager.saveOrUpdateCaseMaterialAttaList(qlCaseMaterialList);
        modelMap.put("qlCaseMaterialAttaList", list);
        modelMap.put("success", true);
        modelMap.put("message", "保存材料成功");
        apiResultSet.setData(modelMap);
        return apiResultSet;
    }


    /**
     * @description: 查询材料信息
     * @params：[ caseOid 办件编号]
     * @return: 材料详细信息list
     * @author wangyh
     * @date 2022年09月06 10:01:29
     */
    @Override
    public ApiResultSet<Map<String, List<QlCaseMaterial>>> queryQlCaseMaterialListByCaseOid(String caseOid) {
        log.info("进入查询材料信息Controller，参数caseOid：{} ", caseOid);
        ApiResultSet<Map<String, List<QlCaseMaterial>>> apiResultSet = apiFeignService.queryQlCaseMaterialListByCaseOid(caseOid);
        return apiResultSet;
    }

    /**
     * 根据事项 情形 选择获取材料和精细化材料
     *
     * @param serviceOid    所属事项主健
     * @param optionValOids 选项主键
     * @author: wangyh
     * @Date: 2022-9-06 13:40:06
     **/
    @Override
    public ApiResultSet<List<ServiceMaterialVo>> getSituationMaterialListByOids(String serviceOid, String optionValOids) {
        if (StringUtils.isEmpty(serviceOid)) {
            throw new ResultInfoException("事项主键不能为空");
        }
        List<SxServiceOptionTitle> optionTitles = sxServiceOptionTitleManager.getServiceOptionTitlesByServiceOid(serviceOid);
        List<SxServiceOptionVal> allOptionVal = new ArrayList<>();
        for (SxServiceOptionTitle title : optionTitles) {
            List<SxServiceOptionVal> optionVals = sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(title.getOid());
            allOptionVal.addAll(optionVals);
        }
        Map<String, List<SxServiceMaterial>> materialMap = new HashMap<>();
        Map<String, List<ServiceMaterial>> serviceMaterialMap = new HashMap<>();
        Map<String, Integer> tempMap = new HashMap<>();
        for (SxServiceOptionVal optionVal : allOptionVal) {
            List<SxServiceMateOptRel> optRelList = sxServiceMateOptRelManager.getSxServiceMateOptRelsByOptionValOid(optionVal.getOid());
            List<SxServiceMaterial> sxServiceMaterials = new ArrayList<>();
            List<ServiceMaterial> serviceMaterials = new ArrayList<>();
            for (SxServiceMateOptRel optRel : optRelList) {
                if (StringUtils.isNotEmpty(optRel.getSxMaterialOid())) {
                    SxServiceMaterial sxMaterial = sxServiceMaterialManager.getSxServiceMaterialByOid(optRel.getSxMaterialOid());
                    if (null != sxMaterial) {
                        sxServiceMaterials.add(sxMaterial);
                    }
                }
                if (StringUtils.isNotEmpty(optRel.getMaterialOid())) {
                    ServiceMaterial serviceMaterial = serviceMaterialManager.getServiceMaterialByOid(optRel.getMaterialOid());
                    if (null != serviceMaterial) {
                        serviceMaterials.add(serviceMaterial);
                    }
                }
            }
            tempMap.put(optionVal.getOid(), optRelList.size());
            materialMap.put(optionVal.getOid(), sxServiceMaterials);
            serviceMaterialMap.put(optionVal.getOid(), serviceMaterials);
        }
        // 所有材料
        List<SxServiceMaterial> allSxServiceMaterial = sxServiceMaterialManager.getSxServiceMaterialByServiceOid(serviceOid);
        // 最终展示的材料
        List<SxServiceMaterial> needShowMaterialList = new ArrayList<>();
        // 不展示的材料
        List<SxServiceMaterial> notShowList = new ArrayList<>();
        for (String key : materialMap.keySet()) {
            if (StringUtils.isEmpty(optionValOids)) {
                if (null != tempMap.get(key) && tempMap.get(key) != 0) {
                    List<SxServiceMaterial> list = materialMap.get(key);
                    notShowList.addAll(list);
                }
            } else {
                String[] valueOids = optionValOids.split(";");
                for (String oid : valueOids) {
                    if (oid.equals(key)) {
                        List<SxServiceMaterial> list = materialMap.get(key);
                        needShowMaterialList.addAll(list);
                    } else {
                        if (null != tempMap.get(key) && tempMap.get(key) != 0) {
                            List<SxServiceMaterial> list = materialMap.get(key);
                            notShowList.addAll(list);
                        }
                    }
                }
            }
        }
        for (SxServiceMaterial material : allSxServiceMaterial) {
            if (!notShowList.contains(material)) {
                needShowMaterialList.add(material);
            }
        }
        needShowMaterialList = needShowMaterialList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(SxServiceMaterial::getMaterialOid))), ArrayList::new));
        List<ServiceMaterialVo> materialVoList = this.getServiceMaterialVoBySxMaterial(needShowMaterialList);
        ApiResultSet<List<ServiceMaterialVo>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(materialVoList);
        return apiResultSet;
    }

    /**
     * @description 单独查询事项信息
     * @param serviceOid
     * @author: wangyh
     * @Date: 2022-9-06 13:40:06
     * @return
     **/
    @Override
    public ApiResultSet<SxService> getSxServiceByServiceOid(String serviceOid) {
        return sxServiceFeginService.getSxServiceByOid(serviceOid);
    }

    /**
     * @description: 根据业务主键查询办件信息
     * @param caseOid
     * @author: kangax
     * @date: 2022-09-19 09:45
     */
    @Override
    public ApiResultSet queryQlCaseByCaseOid(String caseOid) {
        ApiResultSet<Object> resultSet = new ApiResultSet<>();
        Map<String,Object> resultDate = new HashMap<>();
        // 办件信息
        ApiResultSet<QlCase> qlCaseApiResultSet = qlCaseServiceFeginService.queryQlCaseByCaseOid(caseOid);
        resultSet.setCode(qlCaseApiResultSet.getCode());
        QlCase qlCase = qlCaseApiResultSet.getData();
        HaWorkService haWorkService = haWorkServiceManager.selectByCaseOid(qlCase.getCaseOid());
        ApiResultSet<List<Map<String, String>>> apiResultSet = haBusinessService.getCaseTitleValueList(caseOid);
        List<Map<String, String>> title = apiResultSet.getData().stream().filter(e->
            "1".equals(e.get("noticeFormFlag"))).collect(Collectors.toList());
        resultDate.put("operatorName",haWorkService.getOperatorName());
        resultDate.put("operatorPhone",haWorkService.getOperatorPhone());
        resultDate.put("qlCase",qlCase);
        resultDate.put("title",title);
        resultSet.setData(resultDate);
        return resultSet;
    }

    /**
     * @description:办件业务主键获取办件材料
     * @param caseOid
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     * @author: kangax
     * @date: 2022-09-19 10:19
     */
    @Override
    public ApiResultSet queryQlCaseMaterialByCaseOid(String caseOid) {
        ApiResultSet<List<QlCaseMaterial>> apiResultSet = qlCaseMaterialServiceFeginService.queryQlCaseMaterialByCaseOid(caseOid);
        //新增QlCaseMaterialAttaList返回
        List<QlCaseMaterial> qlCaseMaterials = apiResultSet.getData();
        for(QlCaseMaterial qlCaseMaterial : qlCaseMaterials){
            List<QlCaseMaterialAtta> qlCaseMaterialAttas = qlCaseMaterialAttaManager.queryQlCaseMaterialAttaByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
            for(QlCaseMaterialAtta qlCaseMaterialAtta : qlCaseMaterialAttas) {
                if(StrUtil.isNotEmpty(qlCaseMaterialAtta.getAttaOid())) {
                    QlSysAtta atta = sysAttaManager.querySysAttaByOid(qlCaseMaterialAtta.getAttaOid());
                    qlCaseMaterialAtta.setQlSysAtta(atta);
                }
            }
            qlCaseMaterial.setQlCaseMaterialAttaList(qlCaseMaterialAttas);
        }
        return apiResultSet;
    }

    /**
     * @description:办件业务主键获取办件材料
     * @param caseMaterialOid
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     * @author: zhaobf
     * @date: 2022-12-08 10:11
     */
    @Override
    public ApiResultSet getQlCaseMaterialByCaseMaterialOid(String caseMaterialOid) {
        ApiResultSet<QlCaseMaterial> apiResultSet = qlCaseMaterialServiceFeginService.getQlCaseMaterialByCaseMaterialOid(caseMaterialOid);
        QlCaseMaterial qlCaseMaterial = apiResultSet.getData();
        List<QlCaseMaterialAtta> qlCaseMaterialAttas = qlCaseMaterialAttaManager.queryQlCaseMaterialAttaByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
        for(QlCaseMaterialAtta qlCaseMaterialAtta : qlCaseMaterialAttas) {
            if(StrUtil.isNotEmpty(qlCaseMaterialAtta.getAttaOid())) {
                QlSysAtta atta = sysAttaManager.querySysAttaByOid(qlCaseMaterialAtta.getAttaOid());
                qlCaseMaterialAtta.setQlSysAtta(atta);
            }
        }
        qlCaseMaterial.setQlCaseMaterialAttaList(qlCaseMaterialAttas);
        return apiResultSet;
    }


    /**
     * @description: 查询材料信息
     * @params：[ caseOid 办件编号]
     * @return: 材料详细信息list
     * @author: kangax
     * @date: 2022-08-02 10:20S
     */
    @Override
    public ApiResultSet<List<QlCaseMaterialAtta>> queryQlCaseMaterialAttaByCaseMaterialOid(String caseMaterialOid) {
        log.info("进入查询材料信息Controller，caseMaterialOid：{} ", caseMaterialOid);
        // 查询办件所有材料
        ApiResultSet<List<QlCaseMaterialAtta>> materialAttas=qlCaseMaterialAttaServiceFeginService.queryQlCaseMaterialAttaByCaseMaterialOid(caseMaterialOid);
        if(materialAttas.getCode()==200&&materialAttas.getData()!=null&&materialAttas.getData().size()>0){
            for(QlCaseMaterialAtta attaMa:materialAttas.getData()){
                if(StrUtil.isNotEmpty(attaMa.getAttaOid())) {
                    QlSysAtta atta = sysAttaManager.querySysAttaByOid(attaMa.getAttaOid());
                    attaMa.setQlSysAtta(atta);
                }
            }

        }

        return materialAttas;
    }

    /**
     * 事项材料处理
     *
     * @param list 需要处理的事项材料集合
     * @return
     */
    protected List<ServiceMaterialVo> getServiceMaterialVoBySxMaterial(List<SxServiceMaterial> list) {
        ServiceMaterialVo serviceMaterialVo = null;
        List<ServiceMaterialVo> voList = new LinkedList<ServiceMaterialVo>();
        if (list != null) {
            for (SxServiceMaterial sxServiceMaterial : list) {
                serviceMaterialVo = new ServiceMaterialVo();
                serviceMaterialVo.setMaterialOid(sxServiceMaterial.getMaterialOid());
                serviceMaterialVo.setMaterialName(sxServiceMaterial.getMaterialName());
                serviceMaterialVo.setMaterialType(sxServiceMaterial.getMaterialType() == null ? null : sxServiceMaterial.getMaterialType().intValue());
                serviceMaterialVo.setMaterialMustFlag(sxServiceMaterial.getMustFlag() == null ? null : sxServiceMaterial.getMustFlag().intValue());
                if (sxServiceMaterial.getMaterialSource() != null) {
                    serviceMaterialVo.setMaterialSource(sxServiceMaterial.getMaterialSource() == null ? null : sxServiceMaterial.getMaterialSource().intValue());
                }
                serviceMaterialVo.setMaterialSimpleAddr(sxServiceMaterial.getMaterialSampleAddr());
                serviceMaterialVo.setMaterialServiceOid(sxServiceMaterial.getServiceOid());
                if (sxServiceMaterial.getMaterialFormat() != null) {
                    serviceMaterialVo.setMaterialFormat(sxServiceMaterial.getMaterialFormat() == null ? null : sxServiceMaterial.getMaterialFormat().intValue());
                } else {
                    serviceMaterialVo.setMaterialFormat(1);
                }
                serviceMaterialVo.setMaterialCatalogOid(sxServiceMaterial.getMaterialCatalogOid());
                serviceMaterialVo.setBaiduTemplateIds(sxServiceMaterial.getBaiduTemplateIds());
                serviceMaterialVo.setPaperNumber(sxServiceMaterial.getPaperNumber());
                serviceMaterialVo.setMaterialSampleAddr(sxServiceMaterial.getMaterialSampleAddr());
                serviceMaterialVo.setMaterialSimpleAddrYl(sxServiceMaterial.getMaterialSampleAddrYl());
                serviceMaterialVo.setAuditType(sxServiceMaterial.getAuditType());
                serviceMaterialVo.setMaterialSampleOid(sxServiceMaterial.getMaterialSampleOid());
                serviceMaterialVo.setMaterialSampleUrl(sxServiceMaterial.getMaterialSampleUrl());
                serviceMaterialVo.setMaterialFlag(0);
                serviceMaterialVo.setMadeMaterialType(sxServiceMaterial.getMadeMaterialType());
                serviceMaterialVo.setReviewPointsFlag(0);
                List<RefinedMaterial> refinedMaterialList = refinedMaterialManager.getRefinedMaterialListByMaterialOid(sxServiceMaterial.getMaterialOid());
                for (RefinedMaterial refinedMaterial : refinedMaterialList) {
                    List<ReviewPoints> reviewPointsList = reviewPointsManager.getReviewPointsListByRefinedMaterialOid(refinedMaterial.getOid());
                    if (CollUtil.isNotEmpty(reviewPointsList)) {
                        serviceMaterialVo.setReviewPointsFlag(1);
                        break;
                    }
                }
                serviceMaterialVo.setMaterialEmptyAddr(sxServiceMaterial.getMaterialEmptyAddr());
                voList.add(serviceMaterialVo);
            }
        }
        return voList;
    }

    @Override
    public ApiResultSet scanCertQrCode(ScanParam param) throws Exception {
        log.info("查询随身办信息，param:{}", param);
        JSONObject obj = null;
        try {
            //调用接口获取企业信息
            String url = interUrl + scanCertQrCode ;
            String result = ClientServer.send(url, JSONObject.toJSONString(param), "utf-8");
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (!"200".equals(jsonObject.get("code").toString())) {
                throw new Exception("第三方接口查询随身办信息错误"+jsonObject.get("message").toString());
            }
            obj = jsonObject.getJSONObject("data");
        } catch (Exception ex) {
            log.error("证照库：扫随申码获得用户信息失败:"+ ex.getMessage());
            return new ApiResultSet<>(500, "证照库：扫随申码获得用户信息失败", ex.getMessage());
        }
        log.error("证照库：扫随申码获得用户信息成功:"+ obj);
        return ApiResultSet.ok("证照库：扫随申码获得用户信息成功", obj);
    }


    @Override
    public ApiResultSet<List<HaWorkLoginLocation>> queryLoginLocation() {
        log.info("查询HaWorkGroup对象集合，");
        List<HaWorkLoginLocation> haWorkGroupList = haWorkLoginLocationServiceManager.haWorkGroupList();
        return ApiResultSet.ok("获取登录地点列表成功",haWorkGroupList);
    }

    @Override
    public ApiResultSet<String> saveLoingLocationName(Long id, String loginLocationName) {
        log.info("保存帮办登录地点");
        haWorkUserServiceManager.saveLoingLocationName(id,loginLocationName);
        return ApiResultSet.ok("保存登录状态成功","");
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
     *
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
        org.springframework.beans.BeanUtils.copyProperties(haCRoomTakeRequestData, requestData);
        //取号，加入排队队列
        List<HaTakeNumHelpResponseData> haTakeNumHelpResponseData = haWorkQueueManager.takeNumHelpInfoFillIn(requestData);
        if(haTakeNumHelpResponseData==null||haTakeNumHelpResponseData.size()==0){
            return new ApiResultSet(ApiResultSet.DIRTY_DATA_TITLE, "取号失败:根据选择的组别和类型没有找到帮办人员" );
        }
        //保持办件来访信息
        HaScanHelpInfoRequestData requestData1 = new HaScanHelpInfoRequestData();
        org.springframework.beans.BeanUtils.copyProperties(requestData, requestData1);
        if(!haVisitManager.saveHavisitByhelpInfo(requestData1)) {
            log.info("进入扫码填写帮代办信息Controller，添加到来访信息时错误");
        }
        HaTakeNumHelpResponseData haTakeNumHelpResponseData1 = new HaTakeNumHelpResponseData();
        for(int i=0;i<haTakeNumHelpResponseData.size();i++){
            haTakeNumHelpResponseData1 = haTakeNumHelpResponseData.get(i);
        }
        return ApiResultSet.ok("请求成功",haTakeNumHelpResponseData1.getCreateRoomVo());
    }

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
        //判断是否是信息确认  确认状态 1-待确认，2-信息无误，3-信息有误
        if (haMessageRequestData.getContentType().equals("4") ) {
            haVideoConsultationManager.checkCodeResult(haMessageRequestData.getReceiveMessageOid(),haMessageRequestData.getCheckCode());
        }

        //1对t_ha_video_chat_record进行操作
        HaVideoChatRecord haVideoChatRecord = new HaVideoChatRecord();
        org.springframework.beans.BeanUtils.copyProperties(haMessageRequestData, haVideoChatRecord);
        Map<String, Object> map = haVideoConsultationManager.saveOrUpdateHaVideoChatRecord(haVideoChatRecord);
        if(map.get("type").equals("2") && map.get("index").equals(1)){
            return ApiResultSet.ok("发送消息成功");
        }
        return new ApiResultSet<>(500,"发送消息失败");
    }

    @Override
    public ApiResultSet checkCodeResult(String recordId, String checkCode){
        log.info("视频帮办：进入消息确认接口" );
        return haVideoConsultationManager.checkCodeResult(recordId,checkCode);
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


    /**
     * @param haEvalResultRequestData
     * @description:保存评价信息内容
     * @author: kangax
     * @date: 2022-08-12 17:48
     */
    @Override
    public ApiResultSet updateEvalResult(HaEvalResultRequestData haEvalResultRequestData) {
        log.info("进入保存评价信息Controller,参数：haEvalResultRequestData: {} ", haEvalResultRequestData);
        try {
            haEvalManager.updateEvalResult(haEvalResultRequestData);
            return ApiResultSet.ok("请求成功");
        } catch (ServiceException e) {
            log.error("保存帮代办评价信息失败，errorMessage: {}", e.getMessage());
            return new ApiResultSet(ApiResultSet.UNKNOWN_ERROR, "保存帮代办评价信息失败: " + e.getMessage());
        }
    }


    @Override
    public ApiResultSet saveCaseExpress(HaCaseExpressRequestData haCaseExpressRequestData) {
        log.info("进入保存办件快递Controller,参数：haCaseExpressRequestData: {} ", haCaseExpressRequestData);
        try {
            HaCaseExpress haCaseExpress = new HaCaseExpress();
            BeanUtils.copyProperties(haCaseExpressRequestData,haCaseExpress);
            haCaseExpressManager.saveOrUpdateCaseExpress(haCaseExpress);
            return ApiResultSet.ok("请求成功");
        } catch (ServiceException e) {
            log.error("保存办件快递信息失败，errorMessage: {}", e.getMessage());
            return new ApiResultSet(ApiResultSet.UNKNOWN_ERROR, "保存办件快递信息失败: " + e.getMessage());
        }
    }




}
