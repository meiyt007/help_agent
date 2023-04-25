package com.zfsoft.superwindow.controller.yxpz;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.data.yxpz.ReguserInfo;
import com.zfsoft.superwindow.manager.yxpz.ReguserInfoManager;
import com.zfsoft.superwindow.service.yxpz.ReguserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
public class ReguserInfoController implements ReguserInfoService {

    @Resource
    private ReguserInfoManager reguserInfoManager;


    @Override
    public ApiResultSet<ReguserInfo> saveOrUpdate(ReguserInfo reguserInfo) {
        ReguserInfo info= reguserInfoManager.saveOrUpdate(reguserInfo);
        return new ApiResultSet<>(info);
    }

    @Override
    public ApiResultSet<ReguserInfo> getRegUserInfoByUserOid() {
       ReguserInfo info= reguserInfoManager.getRegUserInfoByUserOid();
        return new ApiResultSet<>(info);
    }

}
