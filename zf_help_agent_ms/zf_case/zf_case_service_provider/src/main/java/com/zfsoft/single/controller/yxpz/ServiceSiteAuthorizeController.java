package com.zfsoft.single.controller.yxpz;

import com.alibaba.fastjson.JSON;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.common.data.TreeSelect;
import com.zfsoft.single.data.yxpz.ServiceSiteAuthorize;
import com.zfsoft.single.manager.yxpz.ServiceSiteAuthorizeManager;
import com.zfsoft.single.service.yxpz.ServiceSiteAuthorizeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * dongxl
 * 事项辖区授权
 */
@Slf4j
@RestController

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ServiceSiteAuthorizeController implements ServiceSiteAuthorizeService {

    private final ServiceSiteAuthorizeManager serviceSiteAuthorizeManager;

    /**
     * dongxl
     * 辖区授权
     * @param serviceSiteAuthorize
     * @return
     */

    @Override
    public ApiResultSet saveOrUpdate( ServiceSiteAuthorize serviceSiteAuthorize) {
        this.serviceSiteAuthorizeManager.saveOrUpdate(serviceSiteAuthorize);
        log.info("辖区授权信息新增/更新成功：{}", JSON.toJSONString(serviceSiteAuthorize));
        return new ApiResultSet(serviceSiteAuthorize);
    }
    /**
     * dongxl
     * 根据事项主键获取所有授权辖区id
     * @param id
     * @return
     */

    @Override
    public ApiResultSet getSiteOidsByServiceOid(String id) {
        String str = this.serviceSiteAuthorizeManager.getSiteOidsByServiceOid(id);
        log.info("详情获取成功：{}", str);
        return new ApiResultSet(str);
    }

    /**
     * dongxl
     * 辖区树
     * @return
     */

    @Override
    public ApiResultSet queryServiceTree() {
        List<TreeSelect> siteTree=this.serviceSiteAuthorizeManager.querySiteAuthorizeTree();
        return new ApiResultSet(siteTree);
    }



    @Override
    public ApiResultSet<Integer> deleteServiceSiteAuth(String serviceOid) {
        Integer  rows = serviceSiteAuthorizeManager.delServiceSiteAuthorizeByServiceOid(serviceOid);
        log.info("取消授权成功：{}", serviceOid);
        return new ApiResultSet<Integer>(rows);
    }
}
