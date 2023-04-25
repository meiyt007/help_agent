package com.zfsoft.superwindow.controller.yxpz;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.data.yxpz.SysSiteDistrictRelation;
import com.zfsoft.superwindow.manager.yxpz.SysSiteDistrictRelationManager;
import com.zfsoft.superwindow.service.yxpz.SysSiteDistrictRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author: kkfan
 * @create: 2020-10-26 10:33:00
 * @description: 辖区区划管理控制层
 */
@Slf4j
@RestController
public class SysSiteDistrictRelationController implements SysSiteDistrictRelationService {
    @Resource
    private SysSiteDistrictRelationManager sysSiteDistrictRelationManager;


    @Override
    public ApiResultSet<List<SysSiteDistrictRelation>> querySiteDistrictRelByDistOid(Map<String, Object> paramRelMap) {
       List<SysSiteDistrictRelation> list= sysSiteDistrictRelationManager.querySiteDistrictRelByDistOid(paramRelMap);
        return new ApiResultSet(list);
    }

    @Override
    public ApiResultSet<List<SysSiteDistrictRelation>> siteDistrictRelListByDistOid(String districtOid) {
        List<SysSiteDistrictRelation> list= sysSiteDistrictRelationManager.siteDistrictRelListByDistOid(districtOid);
        return new ApiResultSet(list);
    }
}
