package com.zfsoft.superwindow.controller.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.clzs.InterApi;
import com.zfsoft.superwindow.data.clzs.InterApiResponse;
import com.zfsoft.superwindow.data.feign.Server;
import com.zfsoft.superwindow.data.feign.ServerInterface;
import com.zfsoft.superwindow.dbaccess.data.DbInterApi;
import com.zfsoft.superwindow.feign.settings.MiddleWebFeignService;
import com.zfsoft.superwindow.manager.clzs.InterApiManager;
import com.zfsoft.superwindow.manager.clzs.InterApiResponseManager;
import com.zfsoft.superwindow.service.clzs.InterApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: qiaol
 * @Description: 内部接口定义
 * @Date: 2022/5/17 17:39
 */
@Slf4j
@RestController
public class InterApiController implements InterApiService {

    @Resource
    private InterApiManager interApiManager;

    @Resource
    private InterApiResponseManager interApiResponseManager;

    @Autowired
    private MiddleWebFeignService middleWebFeignService;

    @Override
    public ApiResultSet<PageResult> queryInfoWithPage(InterApi interApi) {
        PageResult<InterApi> electronicLicensePageResult = interApiManager.queryInfoWithPage(interApi, interApi.getPageNum(), interApi.getPageSize());
        ApiResultSet apiResultSet  = new ApiResultSet();
        apiResultSet.setData(electronicLicensePageResult);

        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<InterApi>> queryAllInterApi() {
        List<InterApi> interApiList = new ArrayList<>();
        List<DbInterApi> interDbApiList = interApiManager.queryAllInterApi();
        for(DbInterApi dbInterApi : interDbApiList){
            InterApi interApi = new InterApi();
            org.springframework.beans.BeanUtils.copyProperties(dbInterApi, interApi);
            interApiList.add(interApi);
        }
        ApiResultSet<List<InterApi>> listApiResultSet = new ApiResultSet<>(interApiList);
        return listApiResultSet;
    }

    @Override
    public ApiResultSet<List<InterApiResponse>> queryInterApiValById(Long id) {
        List<InterApiResponse> interDbApiList = interApiManager.queryInterApiValById(id);
        ApiResultSet<List<InterApiResponse>> listApiResultSet = new ApiResultSet<>(interDbApiList);
        return listApiResultSet;
    }

    @Override
    public ApiResultSet<InterApiResponse> getInterApiReo(Long id) {
        ApiResultSet apiResultSet = new ApiResultSet();
        InterApiResponse interApiReo = interApiResponseManager.getInterApiResponseById(id);
        apiResultSet.setData(interApiReo);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<InterApiResponse> getInterApiReponse(Long id) {
        ApiResultSet apiResultSet = new ApiResultSet();
        InterApiResponse interApiReo = interApiResponseManager.getInterApiResponseById(id);
        apiResultSet.setData(interApiReo);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<InterApi> getInterApi(Long id) {
        ApiResultSet apiResultSet = new ApiResultSet();
        InterApi interApi = interApiManager.selectByPrimaryKey(id);
        apiResultSet.setData(interApi);
        return apiResultSet;
    }

    @Override
    public ApiResultSet saveOrUpdate(InterApi interApi) throws Exception {
        ApiResultSet apiResultSet = new ApiResultSet();
        interApiManager.saveOrUpdate(interApi);
        apiResultSet.setCode(200);
        return apiResultSet;
    }

    @Override
    public ApiResultSet getInfoByOid(Long id) {
        ApiResultSet apiResultSet = new ApiResultSet();
        InterApi interApi = new InterApi();
        DbInterApi dbInterApi = interApiManager.getInterApiByOid(id);
        List<InterApiResponse> subList=interApiResponseManager.getInterApiResponseList(id);
        org.springframework.beans.BeanUtils.copyProperties(dbInterApi,interApi);
        interApi.setResponseList(subList);
        apiResultSet.setData(interApi);
        return apiResultSet;
    }

    @Override
    public ApiResultSet deleteByOid(Long id) {
        ApiResultSet apiResultSet = new ApiResultSet();
        interApiManager.deleteByOid(id);
        apiResultSet.setCode(200);
        return apiResultSet;
    }

    @Override
    public ApiResultSet checkHasRepeat(Long id, String code) {
        ApiResultSet apiResultSet = new ApiResultSet();
        String result = interApiManager.checkHasRepeat(id, code);
        apiResultSet.setCode(200);
        apiResultSet.setData(result);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<Server>> queryServerList() {
        return middleWebFeignService.queryServerList();
    }

    @Override
    public ApiResultSet<List<ServerInterface>> queryForeignServerInterfaceList(String serverOid) {
        return middleWebFeignService.queryForeignServerInterfaceList(serverOid);
    }
}
