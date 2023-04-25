package com.zfsoft.single.controller.fzgl;

import com.alibaba.fastjson.JSON;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.single.data.fzgl.LicenseIssued;
import com.zfsoft.single.manager.fzgl.LicenseIssuedManager;
import com.zfsoft.single.service.fzgl.LicenseIssuedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 证照签发
 * dongxl
 */
@Slf4j
@RestController
public class LicenseIssuedController implements LicenseIssuedService {
    @Resource
    private LicenseIssuedManager licenseIssuedManager;

    @Override
    public ApiResultSet getLicenseIssuedByCaseOid(String caseOid) {
        LicenseIssued license=this.licenseIssuedManager.getLicenseIssuedByCaseOid(caseOid);
        return new ApiResultSet(license);
    }

    @Override
    public ApiResultSet saveLicenseIssued( LicenseIssued licenseIssued) {
        log.info("证照签发信息新增/更新成功：{}", JSON.toJSONString(licenseIssued));
        this.licenseIssuedManager.saveLicenseIssued(licenseIssued);
        return new ApiResultSet(licenseIssued);
    }

    @Override
    public ApiResultSet getLicenseIssuedByOid(String oid) {
        LicenseIssued license=this.licenseIssuedManager.getLicenseIssuedByOid(oid);
        return new ApiResultSet(license);
    }

    @Override
    public ApiResultSet saveOrUpOutOfStock(String oid, String barCode) {
       ApiResultSet res= this.licenseIssuedManager.saveOrUpOutOfStock(oid,barCode);
        return res;
    }
}
