package com.zfsoft.superwindow.service.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.clzs.MaterialCatalog;
import com.zfsoft.superwindow.data.clzs.dto.IdcardInfo;
import com.zfsoft.superwindow.data.clzs.vo.IdCardInfoVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @description: 身份证信息接口
 * @author: wangwg
 * @date: 2021-01-11
 */
@RequestMapping(value = "/faMaterialPicOcrResult")
public interface IdcardInfoService {

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getIdcardFrontInfo", method = {RequestMethod.POST})
    ApiResultSet<IdcardInfo> getIdcardFrontInfo(@RequestBody String picBase64) throws Exception;

    @ProcessFeignCalledResult
    @RequestMapping(value = "/discernIdcardInfo", method = {RequestMethod.POST})
    ApiResultSet<Map<String, Object>> discernIdcardInfo(@RequestParam(value = "idcardInfo", required = false) IdcardInfo idcardInfo, @RequestParam(value = "cata", required = false) MaterialCatalog cata) throws Exception;

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getTempIdcardInfo", method = {RequestMethod.POST})
    ApiResultSet<IdcardInfo> getTempIdcardInfo(@RequestBody String picBase64) throws Exception;

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getIdcardBackInfo", method = {RequestMethod.POST})
    ApiResultSet<IdcardInfo> getIdcardBackInfo(@RequestBody String picBase64) throws Exception;

    @ProcessFeignCalledResult
    @RequestMapping(value = "/discernIdcardInfoVo", method = {RequestMethod.POST})
    ApiResultSet<Map<String, Object>> discernIdcardInfo(@RequestBody IdCardInfoVo idCardInfoVo) throws Exception;


}
