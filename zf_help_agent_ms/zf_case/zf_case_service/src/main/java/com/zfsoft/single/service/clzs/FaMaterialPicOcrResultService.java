package com.zfsoft.single.service.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.single.data.clzs.FaMaterialPicOcrResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @description: 材料目录图片识别结果接口
 * @author: liangss
 * @date: 2020-11-03 16:45:29
 */
@RequestMapping("/faMaterialPicOcrResultService")
public interface FaMaterialPicOcrResultService {

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getFaMaterialPicOcrResultByFaMaterialPicOcrResult", method = {RequestMethod.POST})
    ApiResultSet getFaMaterialPicOcrResultByFaMaterialPicOcrResult(FaMaterialPicOcrResult faMaterialPicOcrResult);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/delFaMaterialPicOcrResult", method = {RequestMethod.GET})
    ApiResultSet delFaMaterialPicOcrResult(String resultId);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    ApiResultSet saveOrUpdate(FaMaterialPicOcrResult faMaterialPicOcrResult);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getFaMaterialPicOcrResultByFmpor", method = {RequestMethod.POST})
    ApiResultSet<List<FaMaterialPicOcrResult>> getFaMaterialPicOcrResultByFmpor(FaMaterialPicOcrResult faMaterialPicOcrResult);

}
