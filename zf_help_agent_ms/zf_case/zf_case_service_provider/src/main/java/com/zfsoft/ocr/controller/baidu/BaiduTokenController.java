package com.zfsoft.ocr.controller.baidu;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.zfsoft.ocr.data.pojo.baidu.BaiduTokenRequest;
import com.zfsoft.ocr.data.pojo.baidu.BaiduTokenResponse;
import com.zfsoft.ocr.data.pojo.exception.ServiceException;
import com.zfsoft.ocr.service.baidu.IBaiduTokenService;
import com.zfsoft.ocr.util.BaiduOcrUtil;
import com.zfsoft.ocr.util.BaiduSpeechUtil;
import com.zfsoft.ocr.util.CommonRestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;


/**
 * 百度token service实现
 *
 * @author chenbw
 * @date 2019年6月25日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@RestController
@Api(tags = {"百度token"})
@ApiIgnore
public class BaiduTokenController implements IBaiduTokenService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    @ApiOperation(value = "获取百度token", httpMethod = "POST")
    public BaiduTokenResponse getAuth(@RequestBody(required = false) BaiduTokenRequest baiduTokenRequest) {
        log.info("进入获取百度token方法");
        BaiduTokenResponse ocrAuthResponse = new BaiduTokenResponse();
        String clientId = baiduTokenRequest.getClientId();
        if (StrUtil.isBlank(clientId)) {
            clientId = BaiduOcrUtil.BAI_DU_URL_CLIENT_ID;
        }

        String clientSecret = baiduTokenRequest.getClientSecret();
        if (StrUtil.isBlank(clientSecret)) {
            clientSecret = BaiduOcrUtil.BAI_DU_URL_CLIENT_SECRET;
        }
        // 获取请求的拼接参数字符串
        String getAccessTokenUrl = BaiduOcrUtil.BAI_DU_URL_TOKEN_URL
                // 1. grant_type为固定参数
                + "grant_type=" + BaiduOcrUtil.HOST_URL_OCR_TOKEN_GRANT_TYPE
                // 2. 官网获取的 API Key
                + "&client_id=" + clientId
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + clientSecret;
        String result = CommonRestUtil.sendGet(getAccessTokenUrl);
        if (result == null) {
            log.error("获取百度token,调用百度接口异常。");
            throw new ServiceException("获取百度token,调用百度接口异常。");
        }
        Map<String, Object> map = JSON.parseObject(result);
        if (map.get(BaiduOcrUtil.HOST_URL_OCR_ACCESS_TOKEN) == null) {
            throw new ServiceException("获取百度token,调用百度接口异常。异常信息=" + result);
        }
        String authToken = map.get(BaiduOcrUtil.HOST_URL_OCR_ACCESS_TOKEN).toString();
        ocrAuthResponse.setCode(200);
        ocrAuthResponse.setAuthToken(authToken);
        return ocrAuthResponse;
    }


    @Override
    @ApiOperation(value = "获取unit token", httpMethod = "POST")
    public BaiduTokenResponse getUnitAuth(@RequestBody(required = false) BaiduTokenRequest baiduTokenRequest) {
        log.info("进入获取unit token方法");
        BaiduTokenResponse ocrAuthResponse = new BaiduTokenResponse();
        String clientId = baiduTokenRequest.getClientId();
        if (StrUtil.isBlank(clientId)) {
            clientId = BaiduOcrUtil.BAI_DU_URL_CLIENT_ID;
        }
        String clientSecret = baiduTokenRequest.getClientSecret();
        if (StrUtil.isBlank(clientSecret)) {
            clientSecret = BaiduOcrUtil.BAI_DU_URL_CLIENT_SECRET;
        }
        // 获取请求的拼接参数字符串
        String getAccessTokenUrl = BaiduSpeechUtil.BAIDU_SPEECH_AUTH_HOST
                // 1. grant_type为固定参数
                + "?grant_type=" + BaiduOcrUtil.HOST_URL_OCR_TOKEN_GRANT_TYPE
                // 2. 官网获取的 API Key
                + "&client_id=" + clientId
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + clientSecret;
        String result = CommonRestUtil.sendGet(getAccessTokenUrl);
        if (result == null) {
            log.error("获取百度token,调用百度接口异常。");
            throw new ServiceException("获取百度token,调用百度接口异常。");
        }
        Map<String, Object> map = JSON.parseObject(result);
        if (map.get(BaiduOcrUtil.HOST_URL_OCR_ACCESS_TOKEN) == null) {
            throw new ServiceException("获取百度token,调用百度接口异常。异常信息=" + result);
        }
        String authToken = map.get(BaiduOcrUtil.HOST_URL_OCR_ACCESS_TOKEN).toString();
        ocrAuthResponse.setCode(200);
        ocrAuthResponse.setAuthToken(authToken);
        return ocrAuthResponse;
    }
}
