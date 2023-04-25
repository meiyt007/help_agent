package com.zfsoft.ha.front.controller;

import com.zfsoft.ha.data.responseData.HaBannerResponseData;
import com.zfsoft.ha.front.HaBannerService;
import com.zfsoft.ha.managers.HaBannerServiceManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description //banner控制层
 * @Author: Wangyh
 * @Date: 2022/7/29 10:24
 */
@RestController
@Slf4j
public class HaBannerControllerFront implements HaBannerService {
    /**
     * banner实现业务接口层
     */
    @Resource
    private HaBannerServiceManager haBannerServiceManager;
    @Override

    /**
     * @param //Token在header中
     * @description: 查询banner列表
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     * @return ApiResultSet 获取banner列表详情
     **/
    public ApiResultSet queryMessageList() throws Exception {
        log.info("查询banner列表");
        List<HaBannerResponseData> haBannerList = haBannerServiceManager.queryBannerServiceList();
        return ApiResultSet.ok("查询banner列表", haBannerList);
    }
}
