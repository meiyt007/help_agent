
package com.zfsoft.single.service.fzgl;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.single.data.fzgl.LicenseIssued;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: dongxl
 * @create: 2020-11-24
 * @description: 证照签发
 */
@RequestMapping(value = "/licenseIssued")
public interface LicenseIssuedService {

    /**
     * 根据办件主键查询签收信息
     * @param caseOid
     * @return
     */
    @PostMapping(value = "/getLicenseIssuedByCaseOid")
    ApiResultSet getLicenseIssuedByCaseOid(@RequestParam(value = "caseOid") String caseOid);

    /**
     * 保存证照签发
     * @return
     */
    @PostMapping(value = "/saveLicenseIssued")
    ApiResultSet saveLicenseIssued(@RequestBody LicenseIssued licenseIssued);

    /**
     * 根据业务主键查询签收信息
     * @param oid
     * @return
     */
    @PostMapping(value = "/getLicenseIssuedByOid")
    ApiResultSet getLicenseIssuedByOid(String oid);

    /**
     * 保存签发信息
     * @param oid
     * @return
     */
    @PostMapping(value = "/saveOrUpOutOfStock")
    ApiResultSet saveOrUpOutOfStock(@RequestParam(value = "oid") String oid, @RequestParam(value = "barCode") String barCode);
}
