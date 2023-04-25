package com.zfsoft.service.controller.browser;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.cases.feign.SysOrganFeginService;
import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.browser.service.BrowserService;
import com.zfsoft.service.manager.sxService.SxConditionalRulesManager;
import com.zfsoft.service.manager.sxService.SxServiceManager;
import com.zfsoft.service.sxConditionalRules.data.SxConditionalRules;
import com.zfsoft.service.sxService.data.SxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hut
 * @date 2022/5/30
 */
@RestController
@Slf4j
@Api(tags = "网站端通用接口")
public class BrowserController implements BrowserService {

    @Resource
    private SxServiceManager sxServiceManager;

    @Resource
    private SxConditionalRulesManager sxConditionalRulesManager;

    @Resource
    private SysOrganFeginService sysOrganFeignService;

    @Override
    @ApiOperation(value = "根据区划及事项办理类型查询机构列表")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "districtOid", value = "区划id", dataType = "string", example = "")
            , @ApiImplicitParam(paramType = "query", name = "handleType", value = "办理类型（1-好办快办，2-秒办，3-标准）", dataType = "string", example = "1")})
    public ApiResultSet<List<Map<String, Object>>> listOrganByDistrictAndService(String districtOid, String handleType) {
        List<SysOrgan> organs = sysOrganFeignService.querySysOrganListByDistrictOid(districtOid).getData();
        //List<Map<String, Object>> organList = sxServiceManager.listOrganByDistrictAndService(districtOid, handleType);
        List<Map<String, Object>> organList = new ArrayList<>();
        organs.forEach(sysOrgan -> {
            Map<String, Object> map = new HashMap<>(2);
            map.put("organOid", sysOrgan.getOrganOid());
            map.put("name", sysOrgan.getName());
            organList.add(map);
        });
        ApiResultSet<List<Map<String, Object>>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(organList);
        return apiResultSet;
    }

    @Override
    @ApiOperation(value = "查询事项分页列表")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "serviceName", value = "事项名称", dataType = "string", example = "")
            , @ApiImplicitParam(paramType = "query", name = "districtOid", value = "区划id", dataType = "string", example = "")
            , @ApiImplicitParam(paramType = "query", name = "organOid", value = "部门id", dataType = "string", example = "")
            , @ApiImplicitParam(paramType = "query", name = "handleType", value = "办理类型（1-好办快办，2-秒办，3-标准）", dataType = "string", example = "1")
            , @ApiImplicitParam(paramType = "query", name = "pageNum", value = "页号", dataType = "int", example = "1")
            , @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页条数", dataType = "int", example = "10")})
    public ApiResultSet<PageResult<SxService>> listSxServicePage(String serviceName, String districtOid, String organOid, String handleType, Integer pageNum, Integer pageSize) {
        SxService sxService = new SxService();
        if (StrUtil.isNotEmpty(serviceName)) {
            sxService.setServiceName(serviceName.trim());
        }
        if (StrUtil.isNotEmpty(districtOid)) {
            sxService.setDistrictOid(districtOid);
        }
        if (StrUtil.isNotEmpty(organOid) && !"0".equals(organOid)) {
            sxService.setOrganOid(organOid.trim());
        }
        if (StrUtil.isNotEmpty(handleType)) {
            sxService.setHandleType(handleType.trim());
        }
        PageResult<SxService> pageResult = sxServiceManager.querySxServiceWithPage(sxService, pageNum, pageSize);
        ApiResultSet<PageResult<SxService>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    @Override
    @ApiOperation(value = "查询事项分页列表")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "serviceName", value = "事项名称", dataType = "string", example = "")
            , @ApiImplicitParam(paramType = "query", name = "districtOid", value = "区划id", dataType = "string", example = "")
            , @ApiImplicitParam(paramType = "query", name = "organOid", value = "部门id", dataType = "string", example = "")
            , @ApiImplicitParam(paramType = "query", name = "handleType", value = "办理类型（1-好办快办，2-秒办，3-标准）", dataType = "string", example = "1")
            , @ApiImplicitParam(paramType = "query", name = "pageNum", value = "页号", dataType = "int", example = "1")
            , @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页条数", dataType = "int", example = "10")})
    public ApiResultSet<PageResult<SxService>> listSxServiceSortPage(String serviceName, String districtOid, String organOid, String handleType, Integer pageNum, Integer pageSize) {
        SxService sxService = new SxService();
        if (StrUtil.isNotEmpty(serviceName)) {
            sxService.setServiceName(serviceName.trim());
        }
        if (StrUtil.isNotEmpty(districtOid)) {
            sxService.setDistrictOid(districtOid);
        }
        if (StrUtil.isNotEmpty(organOid) && !"0".equals(organOid)) {
            sxService.setOrganOid(organOid.trim());
        }
        if (StrUtil.isNotEmpty(handleType)) {
            sxService.setHandleType(handleType.trim());
        }
        PageResult<SxService> pageResult = sxServiceManager.querySxServiceSortWithPage(sxService, pageNum, pageSize);
        ApiResultSet<PageResult<SxService>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    @Override
    @ApiOperation(value = "根据事项oid查询事项条件预检列表")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "serviceOid", value = "事项id", dataType = "string", example = "")})
    public ApiResultSet<List<SxConditionalRules>> listSxConditionalRules(String serviceOid, String ruleType) {
        if (StrUtil.isBlank(serviceOid)) {
            throw new ResultInfoException("事项id不能为空！");
        }
        SxConditionalRules sxConditionalRules = new SxConditionalRules();
        sxConditionalRules.setServiceOid(serviceOid);
        sxConditionalRules.setRuleType(ruleType);
        List<SxConditionalRules> list = sxConditionalRulesManager.queryAllSxConditionalRules(sxConditionalRules);
        ApiResultSet<List<SxConditionalRules>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(list);
        return apiResultSet;
    }
}
