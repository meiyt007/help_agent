package com.zfsoft.superwindow.interapi;

import com.zfsoft.superwindow.interapi.query.manager.base.InterApiQueryStrategyContext;
import com.zfsoft.superwindow.interapi.validate.manager.base.InterApiValidateStrategyContext;
import com.zfsoft.superwindow.service.easyquickcase.data.ResponseData;
import com.zfsoft.superwindow.service.interapi.data.ApiReqParams;
import com.zfsoft.superwindow.service.interapi.service.InterApiQueryStrategy;
import com.zfsoft.superwindow.service.interapi.service.InterApiRouteService;
import com.zfsoft.superwindow.service.interapi.service.InterApiValidateStrategy;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: qiaolInterApiService
 * @Description:
 * @Date: 2022/5/16 17:04
 */
@RestController
public class InterApiRouteManager implements InterApiRouteService {

    @Override
    public ResponseData routeQueryApi(String code, ApiReqParams params) {
        InterApiQueryStrategy strategy = InterApiQueryStrategyContext.getStrategy(code);
        if (strategy == null) {
            ResponseData resData = new ResponseData<>();
            resData.error(String.format("接口【%s】不存在", code));
            return resData;
        }
        return strategy.query(params);
    }

    @Override
    public ResponseData routeValidateApi(String code, ApiReqParams params) {
        InterApiValidateStrategy strategy = InterApiValidateStrategyContext.getStrategy(code);
        return strategy.validate(params);
    }
}
