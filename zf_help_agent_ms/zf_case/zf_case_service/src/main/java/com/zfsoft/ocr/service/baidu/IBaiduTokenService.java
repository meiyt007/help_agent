package com.zfsoft.ocr.service.baidu;


import com.zfsoft.ocr.data.pojo.baidu.BaiduTokenRequest;
import com.zfsoft.ocr.data.pojo.baidu.BaiduTokenResponse;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import feign.Headers;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 百度token service
 *
 * @author chenbw
 * @date 2019年6月25日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
//@FeignAiApi
@Headers({"Content-Type: application/json", "Accept: application/json"})
public interface IBaiduTokenService {

    /**
     * 获取百度token请求信息
     *
     * @return
     * @author chenbw
     * @date 2019年6月25日
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/baiduTokenService/getAuth", method = RequestMethod.POST)
    BaiduTokenResponse getAuth(@RequestBody(required = false) BaiduTokenRequest baiduTokenRequest);

    /**
     * 获取Unit token请求信息
     *
     * @param baiduTokenRequest
     * @return
     * @description
     * @author lmz
     * @date 2019-07-16 14:45:48
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/baiduTokenService/getUnitAuth", method = RequestMethod.POST)
    BaiduTokenResponse getUnitAuth(@RequestBody(required = false) BaiduTokenRequest baiduTokenRequest);

}
