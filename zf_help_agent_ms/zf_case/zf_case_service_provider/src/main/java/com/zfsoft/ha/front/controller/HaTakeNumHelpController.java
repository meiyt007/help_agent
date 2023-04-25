package com.zfsoft.ha.front.controller;


import com.zfsoft.cases.util.DateUtil;
import com.zfsoft.ha.data.HaAppointment;
import com.zfsoft.ha.data.requestData.HaScanHelpInfoRequestData;
import com.zfsoft.ha.data.requestData.HaTakeNumHelpRequestData;
import com.zfsoft.ha.data.responseData.HaAppointmentResponseData;
import com.zfsoft.ha.data.responseData.HaTakeNumHelpResponseData;
import com.zfsoft.ha.data.vo.WinNumbVO;
import com.zfsoft.ha.front.HaTakeNumHelpService;
import com.zfsoft.ha.managers.HaAppointmentManager;
import com.zfsoft.ha.managers.HaVisitManager;
import com.zfsoft.ha.managers.HaWorkQueueManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 扫码填写帮代办信息控制层
 * @author: kangax
 * @date: 2022-07-28 17:57
 **/
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HaTakeNumHelpController implements HaTakeNumHelpService {
    /**
     * 办事队列manager
     */
    private final HaWorkQueueManager haWorkQueueManager;

    /**
     * 来访信息service层
     */
    private final HaVisitManager haVisitManager;

    @Resource
    private HaAppointmentManager haAppointmentManager;

    @Resource
    private HaBusinessController haBusinessController;
    /**
     * @description: 取号分配帮代办人员信息
     * @params：[ requestData 办事队列vo]
     * @author: kangax
     * @date: 2022-07-28 18:00
     * @return
     */
    @Override
    public ApiResultSet takeNumHelpInfo(HaTakeNumHelpRequestData requestData) throws Exception {
        if(!"4".equals(requestData.getTakeNumberType())){
            if(requestData.getCardNo()==null||requestData.getCardNo().isEmpty()){
                return new ApiResultSet(ApiResultSet.DIRTY_DATA_TITLE, "取号失败:请输入正确的身份证号码" );
            }
        }
        log.info("进入扫码填写帮代办信息Controller，参数haWorkQueueVo：{}", requestData);
        List<HaTakeNumHelpResponseData> haTakeNumHelpResponseData = haWorkQueueManager.takeNumHelpInfoFillIn(requestData);
        if(haTakeNumHelpResponseData==null||haTakeNumHelpResponseData.size()==0){
            return new ApiResultSet(ApiResultSet.DIRTY_DATA_TITLE, "取号失败:根据选择的组别和类型没有找到在线的帮办人员" );
        }
        //保持办件来访信息
        HaScanHelpInfoRequestData requestData1 = new HaScanHelpInfoRequestData();
        BeanUtils.copyProperties(requestData, requestData1);
        if(!haVisitManager.saveHavisitByhelpInfo(requestData1)) {
            log.info("进入扫码填写帮代办信息Controller，添加到来访信息时错误");
        }
        //发送旗舰店号票推送
        for (HaTakeNumHelpResponseData haTake : haTakeNumHelpResponseData) {
            WinNumbVO winNumbVO = new WinNumbVO();
            winNumbVO.setCardNo(haTake.getCardNo());
            winNumbVO.setCompanyCode(haTake.getCompanyCode());
            winNumbVO.setCompanyName(haTake.getCompanyName());
            winNumbVO.setName(haTake.getName());
            winNumbVO.setStHallAddress(haTake.getHaWorkUser().getServicePostion());
            winNumbVO.setStNumber(haTake.getWindowsNumber());
            winNumbVO.setStCategoryName("");
            winNumbVO.setStDesc("");
            winNumbVO.setStHallFullName("");
            winNumbVO.setStTakeDate(DateUtil.datetimeFormat.format(new Date()));
            winNumbVO.setWaitCount(haTake.getHaWorkUser().getWaitingNum());
            haBusinessController.winNumbPush(winNumbVO);
        }

        return ApiResultSet.ok("请求成功", haTakeNumHelpResponseData);
    }

    /**
     * @description: 查询预约信息
     * @params：[ HaAppointmentResponseData 办事队列实体]
     * @author: wangyh
     * @date: 2022-08-28 17:53
     */
    @Override
    public ApiResultSet<HaAppointmentResponseData> queryAppointmentInfo(HaAppointment ha) {
        log.info("预约记录：：进入根据预约信息获取预约列表:"+ha.toString());
        List<HaAppointmentResponseData> haAppointmentRespList;
        try {
            List<HaAppointment> haAppointments = haAppointmentManager.selectByHaAppointment(ha,null,null);
            haAppointmentRespList = haAppointments.stream().map(haUserResource -> {
                HaAppointmentResponseData haAppointmentResp = new HaAppointmentResponseData();
                BeanUtils.copyProperties(haUserResource, haAppointmentResp);
                return haAppointmentResp;
            }).collect(Collectors.toList());
            log.info("预约记录：：根据预约信息获取预约列表成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("预约记录：根据预约信息获取预约列表错误："+e.getMessage());
            return  new ApiResultSet<>(500,"预约记录：根据身份证和社会统一信用代码获取预约列表操作错误",e.getMessage());

        }
        return ApiResultSet.ok("预约记录：根据身份证和社会统一信用代码获取预约列表:操作成功",haAppointmentRespList);

    }
}
