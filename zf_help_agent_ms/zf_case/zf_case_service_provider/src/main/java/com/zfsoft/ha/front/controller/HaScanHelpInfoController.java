package com.zfsoft.ha.front.controller;


import com.zfsoft.ha.data.requestData.HaScanHelpInfoRequestData;
import com.zfsoft.ha.data.responseData.HaScanHelpInfoResponseData;
import com.zfsoft.ha.front.HaScanHelpInfoService;
import com.zfsoft.ha.managers.HaVisitManager;
import com.zfsoft.ha.managers.HaWorkQueueManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 扫码填写帮代办信息控制层
 * @author: kangax
 * @date: 2022-07-28 17:57
 **/
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HaScanHelpInfoController implements HaScanHelpInfoService {
    /**
     * 办事队列manager
     */
    private final HaWorkQueueManager haWorkQueueManager;

    /**
     * 来访信息service层
     */
    private final HaVisitManager haVisitManager;

    /**
     * @description: 扫码填写帮代办信息
     * @params：[ requestData 办事队列vo]
     * @author: kangax
     * @date: 2022-07-28 18:00
     * @return
     */
    @Override
    public ApiResultSet scanHelpInfo(HaScanHelpInfoRequestData requestData) {
        log.info("进入扫码填写帮代办信息Controller，参数haWorkQueueVo：{}", requestData);
        HaScanHelpInfoResponseData haScanHelpInfoResponseData = haWorkQueueManager.scanHelpInfoFillIn(requestData);
        if(!haVisitManager.saveHavisitByhelpInfo(requestData)) {
            log.info("进入扫码填写帮代办信息Controller，添加到来访信息时错误");
        }
        return ApiResultSet.ok("请求成功", haScanHelpInfoResponseData);
    }
}
