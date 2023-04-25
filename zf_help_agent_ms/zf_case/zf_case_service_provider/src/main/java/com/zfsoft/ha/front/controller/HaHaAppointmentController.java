package com.zfsoft.ha.front.controller;

import com.zfsoft.ha.data.HaAppointment;
import com.zfsoft.ha.data.responseData.HaAppointmentResponseData;
import com.zfsoft.ha.front.HaAppointmentService;
import com.zfsoft.ha.managers.HaAppointmentManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 预约信息表
 *
 * @author zhaobf
 * @version 1.0
 * @date 2022/7/22
 */

@Slf4j
@RestController
public class HaHaAppointmentController implements HaAppointmentService {
    @Resource
    private HaAppointmentManager haAppointmentManager;

    /**
     * 根据身份证和社会统一信用代码获取预约列表
     * @param ha 预约信息，
     * @return
     */
    @Override
    public ApiResultSet<List<HaAppointmentResponseData>> queryAppointmentInfo(HaAppointment ha) {
        log.info("预约记录：：进入根据预约信息获取预约列表:"+ha.toString());
        List<HaAppointmentResponseData> haAppointmentRespList;

        try {
            //根据身份证号码获取预约信息
//            HaAppointment ha = new HaAppointment();
//            ha.setCardNo(cardNo);
//            ha.setCompanyCode(companyCode);
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
