
package com.zfsoft.single.service.fzgl;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.single.data.fzgl.CaseLicenseManage;
import com.zfsoft.single.data.fzgl.EmsParamManage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author: dongxl
 * @create: 2020-11-24
 * @description: 证照签发
 */
@RequestMapping(value = "/caseLicenseManage")
public interface CaseLicenseManageService {

    /**
     * 查询证照签收列表
     * @return
     */
    @PostMapping(value = "/pageList")
    ApiResultSet queryPageList(CaseLicenseManage caseLicenseManage, @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNumber, @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize);

    /**
     * 根据办件主键查询签收信息
     * @param caseOid
     * @return
     */
    @PostMapping(value = "/getOneByCaseOid")
    ApiResultSet getOneByCaseOid(@RequestParam(value = "caseOid") String caseOid);

    /**
     * 保存证照签收
     * @param caseLicenseManage
     * @return
     */
    @PostMapping(value = "/saveOrUpdateSign")
    ApiResultSet saveOrUpdateSign(@RequestBody CaseLicenseManage caseLicenseManage);

    /**
     * 查询证照签发列表
     * @return
     */
    @PostMapping(value = "/pageListIssued")
    ApiResultSet<PageResult<CaseLicenseManage>> queryPageListIssued(CaseLicenseManage caseLicenseManage, @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNumber, @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize);

    /**
     * 保存快递送达信息
     * @return
     */
    @PostMapping(value = "/saveOrUpdateKd")
    ApiResultSet saveOrUpdateKd(EmsParamManage emsParamManage);

    /**
     * 根据办件编号查询信息
     * @param caseNumber
     * @return
     */
    @PostMapping(value = "/selectByCaseNumber")
    ApiResultSet selectByCaseNumber(String caseNumber);

    /**
     * 出证办结插入数据
     * @param caseLicenseManage
     * @return
     */
    @PostMapping(value = "/saveOrUpdateCaseLicenseManage")
    ApiResultSet saveOrUpdateCaseLicenseManage(@RequestBody CaseLicenseManage caseLicenseManage);

    /**
     * 获取ems发证信息
     * @param caseOid
     * @return
     */
    @PostMapping(value = "/getDeliverRecordByCaseOid")
    ApiResultSet getDeliverRecordByCaseOid(@RequestParam(value = "caseOid") String caseOid);


    /**
     * 查询证照签发列表
     * @return
     */
    @PostMapping(value = "/queryIssuedList")
    ApiResultSet<List<CaseLicenseManage>> queryIssuedList(CaseLicenseManage caseLicenseManage);


}
