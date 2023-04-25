package com.zfsoft.single.service.api;
import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.single.data.api.SignatureFlowRecord;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 接口提供类
 */
@RequestMapping(value = "/interface")
public interface ApiService {

    @ProcessFeignCalledResult
    @RequestMapping(value = "/batchSaveDishonestPerson", method = {RequestMethod.GET})
    ApiResultSet batchSaveDishonestPerson();

    /**
     * @description 保存或更新签章流程记录
     * @param signatureFlowRecord
     * @author meiyt
     * @date 2022/5/25
     * @return
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdateSignatureFlow", method = {RequestMethod.POST})
    ApiResultSet saveOrUpdateSignatureFlow(@RequestBody SignatureFlowRecord signatureFlowRecord);

    /**
     * @description 电子签章接口回调
     * @param request
     * @author meiyt
     * @date 2022/5/25
     * @return
     **/
    @RequestMapping(value = "/electronicSignatureCallBack")
    ApiResultSet electronicSignatureCallBack(HttpServletRequest request);

    /**
     * @description 根据办件编号查询签章流程记录
     * @param caseOid
     * @author meiyt
     * @date 2022/5/25
     * @return
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getSignatureFlowRecordByCaseOid", method = {RequestMethod.GET})
    ApiResultSet<List<SignatureFlowRecord>> getSignatureFlowRecordByCaseOid(@RequestParam(value = "caseOid", required = false) String caseOid);

    /**
     * @description 根据材料、办件查询流程信息
     * @param caseOid
     * @param materialOid
     * @author meiyt
     * @date 2022/6/23
     * @return
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getSignatureFlowRecord", method = {RequestMethod.GET})
    ApiResultSet<SignatureFlowRecord> getSignatureFlowRecord(@RequestParam(value = "caseOid", required = false) String caseOid,
                                                             @RequestParam(value = "materialOid", required = false) String materialOid);

    /**
     * @description 查询材料信息
     * @param caseOid
     * @author meiyt
     * @date 2022/6/2
     * @return
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryQlCaseMaterialListByCaseOid", method = {RequestMethod.GET})
    ApiResultSet<Map<String, List<QlCaseMaterial>>> queryQlCaseMaterialListByCaseOid(@RequestParam(value = "caseOid", required = false) String caseOid);

    /**
     * @description 窗口端查询材料列表（新）
     * @param caseOid
     * @author meiyt
     * @date 2022/6/17
     * @return
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryQlCaseMaterialListByCaseOidForZC", method = {RequestMethod.GET})
    ApiResultSet<Map<String, List<QlCaseMaterial>>> queryQlCaseMaterialListByCaseOidForZC(@RequestParam(value = "caseOid", required = false) String caseOid);
}
