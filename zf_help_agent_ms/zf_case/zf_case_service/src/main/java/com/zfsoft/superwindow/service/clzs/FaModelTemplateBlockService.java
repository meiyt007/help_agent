package com.zfsoft.superwindow.service.clzs;


import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.clzs.FaModelTemplateBlock;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @description: 识别模板区块接口
 * @author: liangss
 * @date: 2020-11-03 16:45:29
 */
@RequestMapping(value = "/faModelTemplateBlock")
public interface FaModelTemplateBlockService {

    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryCataMetadataBlockByTemplateOid", method = {RequestMethod.GET})
    ApiResultSet queryCataMetadataBlockByTemplateOid(@RequestParam(value = "templateOid", required = false) String templateOid) throws Exception;

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getFeatureFaModelTemplateBlockByTemplateOidNew", method = {RequestMethod.GET})
    ApiResultSet getFeatureFaModelTemplateBlockByTemplateOidNew(@RequestParam(value = "faModelTemplateOid", required = false) String faModelTemplateOid) throws Exception;

    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryFaModelTemplateBlockList", method = {RequestMethod.POST})
    ApiResultSet<List<FaModelTemplateBlock>> queryFaModelTemplateBlockList(@RequestBody FaModelTemplateBlock faModelTemplateBlock);

}
