package com.zfsoft.ocr.service.speech;


import com.zfsoft.ocr.data.pojo.speech.*;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import feign.Headers;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 语音处理 service
 *
 * @author chenbw
 * @date 2019年6月24日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
//@FeignAiApi
@Headers({ "Content-Type: application/json", "Accept: application/json" })
public interface ISpeechRestService {

    /**
     * 文字转语音base64
     *
     * @author chenbw
     * @date 2019年6月24日
     * @param speechBase64ByContentRequest
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/speechRestService/getSpeechBase64ByContent", method = RequestMethod.POST)
    SpeechBase64ByContentResponse getSpeechBase64ByContent(
            @RequestBody SpeechBase64ByContentRequest speechBase64ByContentRequest);

    /**
     * 识别语音文字内容
     * @author chenbw
     * @date 2019年6月25日
     * @param speechContentByBase64Request
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/speechRestService/getSpeechContentByBase64", method = RequestMethod.POST)
    SpeechContentByBase64Response getSpeechContentByBase64(
            @RequestBody SpeechContentByBase64Request speechContentByBase64Request);

    /**
     * 获取场景bot理解和应答的信息
     * @description
     * @param speechUtteranceRequest
     * @return
     * @author lmz
     * @date 2019-07-16 13:29:31
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/speechRestService/getUtterance", method = RequestMethod.POST)
    SpeechUtteranceResponse getUtteranceUnit(@RequestBody SpeechUtteranceRequest speechUtteranceRequest);

    /**
     * 闲聊场景接口
     * @description
     * @param speechUtteranceBotRequest
     * @return
     * @author lmz
     * @date 2019-07-16 13:45:20
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/speechRestService/getUtteranceOrbot", method = RequestMethod.POST)
    SpeechUtteranceBotResponse getUtteranceOrbot(@RequestBody SpeechUtteranceBotRequest speechUtteranceBotRequest);



    /**
     * 阿里文字转语音base64
     *
     * @author chenbw
     * @date 2020年3月4日
     * @param aliSpeechBase64ByContentRequest
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/speechRestService/getAliSpeechBase64ByContent", method = RequestMethod.POST)
    AliSpeechBase64ByContentResponse getAliSpeechBase64ByContent(
            @RequestBody AliSpeechBase64ByContentRequest aliSpeechBase64ByContentRequest);

}
