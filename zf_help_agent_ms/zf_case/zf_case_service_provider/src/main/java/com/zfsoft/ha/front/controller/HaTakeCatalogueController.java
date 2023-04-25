package com.zfsoft.ha.front.controller;


import com.alibaba.fastjson.JSONObject;
import com.zfsoft.ha.data.requestData.HaTakeNumberRequestData;
import com.zfsoft.ha.data.vo.HaTakeCatalogueVo;
import com.zfsoft.ha.front.HaTakeCatalogueService;
import com.zfsoft.ha.managers.HaTakeCatalogueManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 窗口取号目录控制层
 * @author zhaobf
 * @version 1.0
 * @date 2022/10/28 15:50
 */
@RestController
@Slf4j
public class HaTakeCatalogueController implements HaTakeCatalogueService {

    @Resource
    HaTakeCatalogueManager haTakeCatalogueManager;

    @Override
    public ApiResultSet<String> getAndLoadWandaCatalogue() throws Exception {
        log.info("获取万达取号目录并保存到本地数据库：");
        boolean wandaCatalogue = haTakeCatalogueManager.getAndLoadWandaCatalogue();
        if (wandaCatalogue) {
            return ApiResultSet.ok();
        } else {
            return new ApiResultSet(ApiResultSet.PARAM_VALID_ERROR, "获取万达取号目录错误");
        }

    }

    @Override
    public ApiResultSet<String> getWandaCatalogue() throws Exception {
        log.info("获取万达取号目录：返回二楼取号机的目录信息");
        List<HaTakeCatalogueVo> wandaCatalogue = haTakeCatalogueManager.getWandaCatalogue();
        if (wandaCatalogue!=null) {
            return ApiResultSet.ok("获取万达取号目录：返回二楼取号机的目录信息成功",wandaCatalogue);
        } else {
            return new ApiResultSet(ApiResultSet.PARAM_VALID_ERROR, "获取万达取号目录错误");
        }

    }

    @Override
    public ApiResultSet<String> getAppointCatalogue() throws Exception {
        log.info("获取指定取号目录：返回二楼取号机的目录信息");
        List<HaTakeCatalogueVo> wandaCatalogue = haTakeCatalogueManager.getAppointCatalogue();
        if (wandaCatalogue!=null) {
            return ApiResultSet.ok("获取万达取号目录：返回二楼取号机的目录信息成功",wandaCatalogue);
        } else {
            return new ApiResultSet(ApiResultSet.PARAM_VALID_ERROR, "获取万达取号目录错误");
        }
    }

    @Override
    public ApiResultSet takeNumber(HaTakeNumberRequestData haTakeNumberRequestData) {
        log.info("万达窗口取号：haTakeNumberRequestData={}",haTakeNumberRequestData);
        JSONObject jsonObject = haTakeCatalogueManager.takeNumber(haTakeNumberRequestData);
        if(jsonObject!=null){
            return ApiResultSet.ok(jsonObject);
        }
        else {
            return new ApiResultSet(ApiResultSet.PARAM_VALID_ERROR, "获取万达取号错误");
        }
    }
}
