package com.zfsoft.microservice.platform.service.sys;

import com.zfsoft.microservice.platform.data.sys.SysPermissionLink;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 权限链接service接口
 *
 * @author zxx
 * @date 2020/9/10 10:59 上午
 */
@RequestMapping("/security")
public interface SysPermissionLinkService {
    /**
     * 保存权限链接
     * @author zxx
     * @date 2020/9/22 10:41 上午
     * @param linkList
     * @return
     */
    @PostMapping(value = "/syspermissionlink")
    ApiResultSet save(@RequestBody List<SysPermissionLink> linkList);
}
