package com.zfsoft.superwindow.service.easyquickcase;

import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author ChangSheng
 * @Date 16:39 2022/6/27
 * @Description 公积金办件推送
 **/
@RequestMapping("/pre")
public interface PreInspectService {
    /**
     * 推送办件
     */
    @RequestMapping(value = "/pushCase", method = {RequestMethod.POST})
    public ApiResultSet pushCase(String serviceOid, String caseOid);

}
