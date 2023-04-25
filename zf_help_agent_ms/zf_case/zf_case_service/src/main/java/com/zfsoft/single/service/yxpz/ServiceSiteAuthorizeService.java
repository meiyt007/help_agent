
package com.zfsoft.single.service.yxpz;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.single.data.yxpz.ServiceSiteAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author: dongxl
 * @create: 2020-11-2
 * @description: 事项辖区授权
 */
@RequestMapping(value = "/serviceSiteAuthorize")
public interface ServiceSiteAuthorizeService{

    /**
     * 保存/更新
     * @param serviceSiteAuthorize
     * @return
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/saveOrUpdate")
    ApiResultSet saveOrUpdate(@RequestBody @Validated ServiceSiteAuthorize serviceSiteAuthorize);

   

    /**
     * 获取详情
     * @param id
     * @return
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/getSiteOidsByServiceOid")
    ApiResultSet getSiteOidsByServiceOid(String id);

    @ProcessFeignCalledResult
    @PostMapping(value = "/querySiteAuthorizeTree")
    ApiResultSet queryServiceTree();

    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteServiceSiteAuth", method = {RequestMethod.DELETE})
    ApiResultSet<Integer> deleteServiceSiteAuth(String serviceOid);

}
