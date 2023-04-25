package com.zfsoft.superwindow.controller.wgpj;

import com.zfsoft.microservice.settings.data.SysConfig;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.feign.settings.SysConfigFeignService;
import com.zfsoft.superwindow.manager.wgpj.HandleServiceStarManager;
import com.zfsoft.superwindow.service.wgpj.HandleServiceStarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author maker
 * @version 1.0
 * @date 2021/8/18 16:51
 */
@RestController
@Slf4j
public class HandleServiceStarController implements HandleServiceStarService {

    @Resource
    private HandleServiceStarManager handleServiceStarManager;

    @Resource
    private SysConfigFeignService sysConfigFeignService;

    @Override
    public ApiResultSet getPushStar() {
        ApiResultSet<SysConfig> config = sysConfigFeignService.getSysConfigByCode("XJTS");
        String type ="3";
        if(config.getData() !=null){
            type = config.getData().getValue();
        }
        ApiResultSet<String> apiResultSet = new ApiResultSet<String>();
        apiResultSet.setData(type);
        return apiResultSet;
    }

    @Override
    public ApiResultSet getStarAndScoreByOid(String virtualBusinessOid) {
        Integer score = handleServiceStarManager.getStarAndScoreByOid(virtualBusinessOid);
        Map<String, String> modelMap = new HashMap<>();
        if (score != null) {
            ApiResultSet<List<SysConfig>> config = sysConfigFeignService.querySysConfigListByParentCode("PJXJ");
            if (null != config.getData()) {
                List<SysConfig> SysConfigList = config.getData();
                if (SysConfigList.size() > 0) {
                    for (SysConfig sysConfig : SysConfigList) {
                        String[] codes = sysConfig.getCode().split("-");
                        if (score >= Integer.parseInt(codes[0]) && score <= Integer.parseInt(codes[1])) {
                            modelMap.put("score", score.toString());
                            modelMap.put("star", sysConfig.getValue());
                        }
                    }
                }
            }
        }
        return new ApiResultSet(modelMap);
    }
}
