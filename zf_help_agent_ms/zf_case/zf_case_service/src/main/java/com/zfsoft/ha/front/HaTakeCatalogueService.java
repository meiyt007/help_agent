package com.zfsoft.ha.front;

import com.zfsoft.ha.data.requestData.HaTakeNumberRequestData;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * 窗口取号相关接口（万达）
 * @author zhaobf
 * @version 1.0
 * @date 2022/10/28 15:58
 */
@RequestMapping("/ha/takeCatalogue")
public interface HaTakeCatalogueService {

    @ProcessFeignCalledResult
    @PostMapping("/getAndLoadWandaCatalogue")
    ApiResultSet<String> getAndLoadWandaCatalogue() throws Exception;

    @ProcessFeignCalledResult
    @PostMapping("/getWandaCatalogue")
    ApiResultSet<String> getWandaCatalogue() throws Exception;

    @ProcessFeignCalledResult
    @PostMapping("/getAppointCatalogue")
    ApiResultSet<String> getAppointCatalogue() throws Exception;

    @ProcessFeignCalledResult
    @PostMapping("/takeNumber")
    ApiResultSet<Map<String,String>> takeNumber(@RequestBody HaTakeNumberRequestData haTakeNumberRequestData);

}
