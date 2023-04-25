package com.zfsoft.microservice.platform.controller.sys;

import com.zfsoft.microservice.platform.data.sys.SysPermissionLink;
import com.zfsoft.microservice.platform.manager.sys.SysPermissionLinkManager;
import com.zfsoft.microservice.platform.service.sys.SysPermissionLinkService;
import com.zfsoft.platform.common.data.ApiResultSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 权限链接保存
 * @author zxx
 * @date 2020/9/22 10:40 上午
 */
@RestController
@Slf4j
public class SysPermissionLinkController implements SysPermissionLinkService{

    @Autowired
    private SysPermissionLinkManager sysPermissionLinkManager;

    /**
     * 保存权限链接
     * @author zxx
     * @date 2020/9/22 10:41 上午
     * @param linkList
     * @return
     */
    public ApiResultSet save(List<SysPermissionLink> linkList) {
        sysPermissionLinkManager.saveSysPermissionLinks(linkList);
        return new ApiResultSet();
    }
}
