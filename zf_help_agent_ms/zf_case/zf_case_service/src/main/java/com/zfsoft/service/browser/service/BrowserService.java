package com.zfsoft.service.browser.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.service.sxConditionalRules.data.SxConditionalRules;
import com.zfsoft.service.sxService.data.SxService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


/**
 * @author hut
 * @date 2022/5/30
 * 网站通用接口
 */
@RequestMapping("/browser")
public interface BrowserService {

    /**
     * 根据区划及事项办理类型查询机构列表
     *
     * @param districtOid
     * @param handleType
     * @return
     */
    @RequestMapping(value = "/listOrganByDistrictAndService", method = {RequestMethod.POST})
    ApiResultSet<List<Map<String, Object>>> listOrganByDistrictAndService(@RequestParam(value = "districtOid", required = false) String districtOid,
                                                                          @RequestParam(value = "handleType", required = false) String handleType);

    /**
     * 查询事项分页列表
     *
     * @param serviceName
     * @param organOid
     * @param handleType
     * @param pageNum
     * @param pageSize
     * @return
     */
//    @RequestMapping(value = "/listSxServicePage", method = {RequestMethod.POST})
    ApiResultSet<PageResult<SxService>> listSxServicePage(@RequestParam(value = "serviceName", required = false) String serviceName,
                                                          @RequestParam(value = "districtOid", required = false) String districtOid,
                                                          @RequestParam(value = "organOid", required = false) String organOid,
                                                          @RequestParam(value = "handleType", required = false) String handleType,
                                                          @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                          @RequestParam(value = "pageSize", required = false) Integer pageSize);

    @ApiOperation(value = "查询事项分页列表")
    @RequestMapping(value = "/listSxServicePage", method = {RequestMethod.POST})
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "serviceName", value = "事项名称", dataType = "string", example = "")
            , @ApiImplicitParam(paramType = "query", name = "districtOid", value = "区划id", dataType = "string", example = "")
            , @ApiImplicitParam(paramType = "query", name = "organOid", value = "部门id", dataType = "string", example = "")
            , @ApiImplicitParam(paramType = "query", name = "handleType", value = "办理类型（1-好办快办，2-秒办，3-标准）", dataType = "string", example = "1")
            , @ApiImplicitParam(paramType = "query", name = "pageNum", value = "页号", dataType = "int", example = "1")
            , @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页条数", dataType = "int", example = "10")})
    ApiResultSet<PageResult<SxService>> listSxServiceSortPage(String serviceName, String districtOid, String organOid, String handleType, Integer pageNum, Integer pageSize);

    /**
     * 根据事项oid查询事项条件预检列表
     *
     * @param serviceOid
     * @return
     */
    @RequestMapping(value="/listSxConditionalRules",method = RequestMethod.POST)
    ApiResultSet<List<SxConditionalRules>> listSxConditionalRules(@RequestParam(value = "serviceOid", required = false) String serviceOid,
                                                                  @RequestParam(value = "ruleType", required = false) String ruleType);


}
