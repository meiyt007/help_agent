package com.zfsoft.ha.manager;


import com.zfsoft.ha.data.HaTakeCatalogue;
import com.zfsoft.ha.data.requestData.CataloguePageRequestData;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;

/**
 * 窗口取号目录配置
 */
@RequestMapping("/catalogue/config")
public interface HaCatalogueConfigService {

    /**
     * 同步线上万达窗口取号目录到本地
     * @return
     * @throws Exception
     */
    @ProcessFeignCalledResult
    @PostMapping("/getAndLoadWandaCatalogue")
    ApiResultSet<String> getAndLoadWandaCatalogue() throws Exception;

    /**
     * 获取目录列表（分页
     * @param cataloguePageRequestData
     * @return
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/queryCataloguePage")
    ApiResultSet queryCataloguePage(CataloguePageRequestData cataloguePageRequestData);

    /**
     * 新增修改目录信息
     * @param haTakeCatalogue
     * @return
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/saveAndUpload")
    ApiResultSet saveAndUpload(@RequestBody HaTakeCatalogue haTakeCatalogue);

    /**
     * 删除用户资源
     * @param id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteById", method = {RequestMethod.GET})
    ApiResultSet<HaTakeCatalogue> deleteById(String id);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getById", method = {RequestMethod.GET})
    ApiResultSet getById(String id);
}
