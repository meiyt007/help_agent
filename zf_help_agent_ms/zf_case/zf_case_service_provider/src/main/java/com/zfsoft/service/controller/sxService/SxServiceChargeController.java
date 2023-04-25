package com.zfsoft.service.controller.sxService;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.manager.sxService.SxServiceChargeManager;
import com.zfsoft.service.sxService.data.SxServiceCharge;
import com.zfsoft.service.sxService.service.SxServiceChargeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @（#）: SxServiceChargeController
 * @description: 事项收费信息控制类
 * @author: wangwg
 * @date: 2021/6/10
 * @version: 1.1
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
public class SxServiceChargeController implements SxServiceChargeService {

    @Resource
    private SxServiceChargeManager sxServiceChargeManager;

    @Override
    public ApiResultSet<List<SxServiceCharge>> querSxServiceChargeListByServiceOid(String serviceOid) {
        List<SxServiceCharge> sxServiceChargeList =sxServiceChargeManager.getSxServiceChargeByServiceOid(serviceOid);
        ApiResultSet<List<SxServiceCharge>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(sxServiceChargeList);
        return apiResultSet;
    }
}
