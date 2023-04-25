package com.zfsoft.service.controller.sxService;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServiceLocation;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.service.manager.sxService.SxServiceExtendManager;
import com.zfsoft.service.manager.sxService.SxServiceLocationManager;
import com.zfsoft.service.sxService.data.SxServiceExtend;
import com.zfsoft.service.sxService.data.SxServiceLocation;
import com.zfsoft.service.sxService.service.SxServiceLocationService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @（#）: SxServiceLocationController
 * @description: 实施清单办理地点服务定义接口实现
 * @author: wangwg
 * @date: 2020/12/04
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
public class SxServiceLocationController  implements SxServiceLocationService {

    @Resource
    private SxServiceLocationManager sxServiceLocationManager;

    @Resource
    private SxServiceExtendManager sxServiceExtendManager;

    @Override
    public ApiResultSet<List<SxServiceLocation>> getSxServicLocationByExtendOid(String extendOid) {
        List<SxServiceLocation> location = sxServiceLocationManager.getSxServiceLocationByExtendOid(extendOid);
        ApiResultSet<List<SxServiceLocation>> apiResultSet = new ApiResultSet<List<SxServiceLocation>>();
        apiResultSet.setData(location);
        return apiResultSet;
    }

    @Override
    public ApiResultSet saveOrUpdateSxServiceLocation(SxServiceLocation sxServiceLocation) {
        this.sxServiceLocationManager.saveOrUpdateSxServiceLocation(sxServiceLocation);
        return new ApiResultSet(sxServiceLocation);
    }

    @Override
    public ApiResultSet pageList(SxServiceLocation sxServiceLocation,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<DbSxServiceLocation> dbSxServiceLocationList = this.sxServiceLocationManager.queryList(sxServiceLocation);
        PageResult<DbSxServiceLocation> pageResult = new PageResult<>(
                ((Page) dbSxServiceLocationList).getPageNum(),
                ((Page) dbSxServiceLocationList).getPageSize(),
                ((Page) dbSxServiceLocationList).getTotal()
        );
        pageResult.setData(dbSxServiceLocationList);
        log.info("获取办理地点信息列表调用成功结果为：{}", JSON.toJSONString(pageResult));
        return new ApiResultSet(pageResult);
    }

    @Override
    public ApiResultSet delete(String ids) {
        this.sxServiceLocationManager.delete(ids);
        log.info("删除成功：{}", ids);
        return new ApiResultSet(ids);
    }

    @Override
    public ApiResultSet getSxServiceLocationById(String id) {
        DbSxServiceLocation dbSxServiceLocation = this.sxServiceLocationManager.getDetail(id);
        log.info("查询成功：{}", id);
        return new ApiResultSet(dbSxServiceLocation);
    }

    @Override
    @ApiOperation(value = "查询事项办理地点")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "serviceOid", value = "事项oid", dataType = "string", example = "")})
    public ApiResultSet getLocationInfoByServiceOid(String serviceOid) {
        List<Map<String, Object>> locationInfo = null;
        SxServiceExtend sxServiceExtend = sxServiceExtendManager.getSxServiceExtendByServiceOid(serviceOid);
        if (sxServiceExtend !=null) {
            locationInfo = this.sxServiceLocationManager.getLocationInfo(sxServiceExtend.getExtendOid());
        }
        return new ApiResultSet(locationInfo);
    }

    @Override
    @ApiOperation(value = "查询事项办理地点")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "serviceOid", value = "事项oid", dataType = "string", example = ""),
            @ApiImplicitParam(paramType = "query", name = "address", value = "终端点位", dataType = "string", example = "")})
    public ApiResultSet getLocationInfo(String serviceOid, String address) {
        List<Map<String, Object>> locationInfo = null;
        SxServiceExtend sxServiceExtend = sxServiceExtendManager.getSxServiceExtendByServiceOid(serviceOid);
        if (sxServiceExtend !=null) {
            locationInfo = this.sxServiceLocationManager.getLocationInfoByExtendOidAndAddress(sxServiceExtend.getExtendOid(),address);
        }
        return new ApiResultSet(locationInfo);
    }
}
