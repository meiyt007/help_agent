package com.zfsoft.single.manager.yxpz;

import com.alibaba.fastjson.JSONObject;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.data.clzs.InterApi;
import com.zfsoft.superwindow.data.clzs.InterApiResponse;
import com.zfsoft.superwindow.service.clzs.InterApiService;
import com.zfsoft.superwindow.service.easyquickcase.data.ResponseData;
import com.zfsoft.superwindow.service.interapi.data.ApiReqParams;
import com.zfsoft.superwindow.service.interapi.service.InterApiRouteService;
import com.zfsoft.service.sxService.data.SxFillField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @Author: qiaol
 * @Description: 填充表单字段
 * @Date: 2022/6/21 10:07
 */
@Service
@Slf4j
@EnableAsync
public class FormFieldFillManager {

    @Autowired
    private InterApiService interApiFeginService;

    @Autowired
    private InterApiRouteService interApiRouteFeignService;

    /**
     *  填充表单字段
     * @param formFieldResult
     * @param apiReqParams
     * @param fieldList
     * @return
     */
    @Async("asyncSingleCasePool")
    public Future<String> fillFormField(JSONObject formFieldResult, ApiReqParams apiReqParams,
                                        String interId, List<SxFillField> fieldList) {
        // 查询接口定义信息以及接口响应信息
        ApiResultSet<InterApi> apiInfoResult = interApiFeginService.getInfoByOid(Long.valueOf(interId));
        InterApi apiInfo = apiInfoResult.getData();

        // 调用接口定义进行调用接口获取数据
        apiReqParams.setInterId(interId);
        Map<String, Object> resultMap = execApi(apiInfo, apiReqParams);

        if (resultMap != null) {
            // 处理返回值
            List<InterApiResponse> responseList = apiInfo.getResponseList();
            Map<Long, InterApiResponse> responseMap = responseList.stream().collect(Collectors.toMap(InterApiResponse::getId, response -> response));
            InterApiResponse interApiResponse = null;
            for (SxFillField field : fieldList) {
                interApiResponse = responseMap.get(Long.valueOf(field.getInterApiValId()));
                formFieldResult.put(field.getFieldCode(), resultMap.get(interApiResponse.getResponseCode()));
            }
        }

        return new AsyncResult<>("");
    }

    /**
     *  调用接口中台查询数据
     * @param apiInfo
     * @param apiReqParams
     * @return
     */
    private Map<String, Object> execApi(InterApi apiInfo, ApiReqParams apiReqParams) {

        ResponseData<Map<String, Object>> apiResultSet = interApiRouteFeignService.routeQueryApi(apiInfo.getCode(), apiReqParams);

        if (apiResultSet.getCode() != 200 || apiResultSet.getData() == null) {
            log.error(String.format("调用接口【%s】失败：【%s】", apiResultSet.getCode(), apiResultSet.getMessage()));
            return null;
        }

        return apiResultSet.getData();
    }

}
