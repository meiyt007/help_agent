
package com.zfsoft.single.service.ywbl;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.single.data.ywbl.InternetAuditRecord;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: dongxl
 * @create: 2020-11-07
 * @description: 办件预审
 */
@RequestMapping(value = "/internetCasePre")
public interface InternetAuditRecordService {

    /**
     * 保存或者预审记录
     * dongxl
     * @param internetAuditRecord
     * @return
     */
    @PostMapping(value = "/saveOrUpdate")
    ApiResultSet saveOrUpdate(@RequestBody @Validated InternetAuditRecord internetAuditRecord);

    /**
     * 根据办件编号查询信息
     * dongxl
     * @param caseOid
     * @return
     */
    ApiResultSet getOneByCaseOid(String caseOid);

}
