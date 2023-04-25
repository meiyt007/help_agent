package com.zfsoft.ha.manager.controller;

import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.util.DateUtil;
import com.zfsoft.ha.data.*;
import com.zfsoft.ha.data.responseData.HaWorkServiceResponseData;
import com.zfsoft.ha.data.vo.HaWorkServiceVo;
import com.zfsoft.ha.manager.HaVisitService;
import com.zfsoft.ha.managers.*;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.helper.DataUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/8/11 19:42
 */
@RestController
@Slf4j
public class HaVisitController  implements HaVisitService {
    /**
     * 来访
     */
    @Resource
    HaVisitManager haVisitManager;
    /**
     * 办事队列
     */
    @Resource
    HaWorkQueueManager haWorkQueueManager;
    /**
     * 服务
     */
    @Resource
    HaWorkServiceManager haWorkServiceManager;
    /**
     * 预约
     */
    @Resource
    HaAppointmentManager haAppointmentManager;
    /**
     * 核酸
     */
    @Resource
    HaMassesNucleicManager haMassesNucleicManager;

    /**
     * 获取分页访问列表
     * @param name 姓名
     * @param visitType 类型 1 个人2 法人
     * @param pageNumber 页码
     * @param pageSize 每页数量
     * @return
     */
    @Override
    public ApiResultSet<PageResult<HaVisit>> queryHaVisitByNameAndVisitType(String name, String visitType, Integer pageNumber, Integer pageSize) {
        log.info("来访人员管理：获取来访人员分页，入参：name:{},visitType:{}", name, visitType);
        PageHelper.startPage(pageNumber, pageSize);
        PageResult<HaVisit> resultSet = null;
        try {
            resultSet = haVisitManager.queryHaVisitByNameAndVisitType(name, visitType);
            log.info("来访人员管理：获取来访人员分页成功");
        } catch (ServiceException e) {
            e.printStackTrace();
            log.info("来访人员管理：获取来访人员分页失败:" + e.getMessage());
            return new ApiResultSet<>(500, "用户资源管理：进入根据资源名称和工作人员名称获取用户资源信息失败", e.getMessage());
        }
        return ApiResultSet.ok("来访人员管理：获取来访人员分页成功", resultSet);
    }

    /**
     * 获取来访人员信息
     * @param workQueueId
     * @return
     */
    public ApiResultSet getVisitMassageByWorkQueueId(String workQueueId){
        log.info("来访人员管理：获取来访人员详细信息，入参：workQueueId:{}", workQueueId);
        Map<String,Object> mapReq = new HashMap<>();
        Map<String,Object> baseInfo = new HashMap<>();
        List<HaWorkServiceVo> serviceHistoryInfo;
        try{

            /**
             * 获取办事队列信息
             */
            HaWorkQueue workQueueById = haWorkQueueManager.getWorkQueueById(workQueueId);

            /**
             * 获取来访人员信息
             */
            HaVisit haVisit = haVisitManager.queryHaVisitByCardNo(workQueueById.getCardNo()).get(0);
            /**
             * 该来访人员的所有办事记录
             */
            HaWorkQueue hwq = new HaWorkQueue();
            hwq.setCardNo(workQueueById.getCardNo());
            List<HaWorkQueue> haWorkQueues = haWorkQueueManager.selectByHaWorkQueue(hwq);

            /**
             * 预约记录
             */
            HaAppointment ha = new HaAppointment();
            ha.setCardNo(workQueueById.getCardNo());
            List<HaAppointment> appointmentInfo = haAppointmentManager.selectByHaAppointment(ha, DateUtil.getDateStartTime(workQueueById.getCreateDate()),DateUtil.getDateEndTime(workQueueById.getCreateDate()));
            /**
             * 根据身份证号获取核酸信息
             */
            HaMassesNucleic hamn = new HaMassesNucleic();
            hamn.setCardNo(workQueueById.getCardNo());
            List<HaMassesNucleic> nucleicInfo=haMassesNucleicManager.selectByHaMassesNucleic(hamn, DateUtil.getDateStartTime(workQueueById.getCreateDate()),DateUtil.getDateStartTime(workQueueById.getCreateDate()));


            /**
             * 该来访人员的所有事项记录
             */
//            List<Long> wordIds = haWorkQueues.stream().map(HaWorkQueue::getId).collect(Collectors.toList());
            serviceHistoryInfo = haWorkServiceManager.selectByHaWorkService(Collections.singletonList(workQueueById.getId()));
            HaWorkServiceVo haWorkServiceVo = new HaWorkServiceVo();
            if(serviceHistoryInfo!=null&&serviceHistoryInfo.size()>0){
                haWorkServiceVo = serviceHistoryInfo.stream()
                        .max(Comparator.comparing(HaWorkServiceVo::getCreateDate)).get();
            }
            baseInfo.put("serviceType",haWorkServiceVo.getServiceType());
            baseInfo.put("id",haVisit.getId());
            baseInfo.put("name",workQueueById.getName());
            baseInfo.put("cardNo",workQueueById.getCardNo());
            baseInfo.put("phone",workQueueById.getPhone());
            baseInfo.put("isAppointment",appointmentInfo.size()>0?"是":"否");
            baseInfo.put("nucleicStatus",(nucleicInfo!=null&&nucleicInfo.size()>0)?nucleicInfo.get(0).getNucleicResultCode():"");
            baseInfo.put("visitNumb",haWorkQueues.size());
            baseInfo.put("visitTime",workQueueById.getCreateDate());
            baseInfo.put("companyName",workQueueById.getCompanyName());
            baseInfo.put("companyCode",workQueueById.getCompanyCode());


            log.info("来访人员管理：获取来访人员详细信息成功");
        }catch (Exception e){
            e.printStackTrace();
            log.info("来访人员管理：获取来访人员详细信息错误:{}",e.getMessage());
            return  new ApiResultSet<>(500,"来访人员管理：获取来访人员详细信息错误",e.getMessage());
        }
        mapReq.put("baseInfo",baseInfo);
        mapReq.put("serviceHistoryInfo",serviceHistoryInfo);
        return ApiResultSet.ok("办事群众：根据身份证号码获取信息成功",mapReq);
    }
}
