package com.zfsoft.single.service.ywbl;

import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxService.data.SxService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @（#）: WindowAcceptanceService
 * @description: 窗口办理接口
 * @author: wangwg
 * @date: 2020/10/27
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/windowAcceptance")
public interface WindowAcceptanceService {


    /**
     * 窗口受理list
     *
     * @author wangwg
     * @date 2020-10-27
     * @param serviceName 事项名称
     * @param organOid 机构
     * @param serviceType 事项类型
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/listWindowAcceptancePage",method = {RequestMethod.GET})
    ApiResultSet<PageResult<SxService>> listWindowAcceptancePage(@RequestParam(value = "serviceName", required = false) String serviceName,
                                                                 @RequestParam(value = "organOid", required = false) String organOid,
                                                                 @RequestParam(value = "serviceType", required = false) String serviceType,
                                                                 @RequestParam(value = "serviceOids", required = false) String serviceOids,
                                                                 @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                 @RequestParam(value = "pageSize", required = false) Integer pageSize);


    /**
     * 查询字典信息
     *
     * @author wangwg
     * @date 2020-12-09
     * @param dictOid 字典id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/querySysDictByOid",method = {RequestMethod.GET})
    ApiResultSet<SysDict> querySysDictByOid(@RequestParam(value = "dictOid", required = false) String dictOid);

    /**
     * 办件业务主键获取办件材料附件
     *
     * @author wangwg
     * @date 2020-12-17
     * @param caseOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryMaterialAttaListByCaseOid",method = {RequestMethod.GET})
    ApiResultSet<List<QlCaseMaterial>> queryMaterialAttaListByCaseOid(@RequestParam(value = "caseOid", required = false) String caseOid);
}
