package com.zfsoft.ocr.controller.speech;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Base64Util;
import com.zfsoft.ocr.constant.Constants;
import com.zfsoft.ocr.data.pojo.baidu.BaiduTokenRequest;
import com.zfsoft.ocr.data.pojo.baidu.BaiduTokenResponse;
import com.zfsoft.ocr.data.pojo.exception.ServiceException;
import com.zfsoft.ocr.data.pojo.speech.*;
import com.zfsoft.ocr.service.baidu.IBaiduTokenService;
import com.zfsoft.ocr.service.speech.ISpeechRestService;
import com.zfsoft.ocr.util.AliSpeechUtil;
import com.zfsoft.ocr.util.BaiduSpeechUtil;
import com.zfsoft.ocr.util.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.util.HashMap;


/**
 * 语音处理 service实现
 *
 * @author chenbw
 * @date 2019年6月25日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@SuppressWarnings("restriction")
@RestController
@Api(tags = {"语音处理"})
public class SpeechRestController implements ISpeechRestService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IBaiduTokenService baiduTokenService;


    private BaiduTokenRequest getBaiduTokenRequest() {
        BaiduTokenRequest baiduTokenRequest = new BaiduTokenRequest();
        baiduTokenRequest.setToken(Constants.TOKEN_PERPETUAL);
        return baiduTokenRequest;
    }

    @Override
    @ApiOperation(value = "文字转语音base64", httpMethod = "POST")
    public SpeechBase64ByContentResponse getSpeechBase64ByContent(
            @RequestBody SpeechBase64ByContentRequest speechBase64ByContentRequest) {
        log.info("进入文字转语音base64方法");
        if (speechBase64ByContentRequest == null) {
            throw new ServiceException("文字转语音base64,请求信息不能为空。");
        }
        if (StrUtil.isBlank(speechBase64ByContentRequest.getContent())) {
            throw new ServiceException("文字转语音base64,文字内容不能为空。");
        }
        String textContent = speechBase64ByContentRequest.getContent();
        SpeechBase64ByContentResponse speechBase64ByContentResponse = new SpeechBase64ByContentResponse();
        // 调用百度接口 start
        HashMap<String, Object> options = new HashMap<String, Object>(8);
        options.put("spd", BaiduSpeechUtil.AIP_SPEECH_SPD);
        options.put("pit", BaiduSpeechUtil.AIP_SPEECH_PIT);
        AipSpeech as = BaiduSpeechUtil.getAipSpeech();
        TtsResponse res = as.synthesis(textContent, "zh", 1, options);
        org.json.JSONObject result = res.getResult();
        if (result == null) {
            byte[] data = res.getData();
            try {
                // 转为Base64
                String base64Str = Base64Util.encode(data);
                speechBase64ByContentResponse.setCode(200);
                speechBase64ByContentResponse.setBase64(base64Str);
            } catch (Exception e) {
                log.error("文字转语音base64,调用百度接口异常。", e);
                throw new ServiceException("文字转语音base64,调用百度接口异常。异常信息=" + e.getMessage());
            }
        } else {
            throw new ServiceException("文字转语音base64,调用百度接口异常。结果信息=" + res.toString());
        }
        // 调用百度接口 end
        return speechBase64ByContentResponse;
    }

    @Override
    @ApiOperation(value = "识别语音文字内容", httpMethod = "POST")
    public SpeechContentByBase64Response getSpeechContentByBase64(@RequestBody
                                                                          SpeechContentByBase64Request speechContentByBase64Request) {
        log.info("进入识别语音文字内容方法");
        SpeechContentByBase64Response speechContentByBase64Response = new SpeechContentByBase64Response();
        String speechBase64 = speechContentByBase64Request.getBase64();
        AipSpeech client = BaiduSpeechUtil.getAipSpeech();
        // 对语音二进制数据进行识别
        try {
            byte[] bytes = new BASE64Decoder().decodeBuffer(speechBase64);
            org.json.JSONObject result = client.asr(bytes, "wav", 16000, null);
            String errNo = result.get(BaiduSpeechUtil.BAIDU_SPEECH_REQUEST_URL_ERR_NO).toString();
            if (BaiduSpeechUtil.BAIDU_SPEECH_REQUEST_URL_ERR_NO_DATA.equals(errNo)) {
                String content = result.get("result").toString();
                JSONArray ja = JSONArray.parseArray(content);
                String resultStr = "";
                for (int i = 0; i < ja.size(); i++) {
                    resultStr += "|" + ja.getString(i);
                }
                // 对识别结果进行url编码
                //resultStr = URLEncoder.encode(resultStr, "UTF-8");
                speechContentByBase64Response.setCode(200);
                speechContentByBase64Response.setContent(resultStr);
            } else {
                throw new ServiceException("识别语音文字内容,调用百度接口异常。错误编码=" + errNo);
            }
        } catch (Exception e) {
            log.error("识别语音文字内容,调用百度接口异常。", e);
            throw new ServiceException("识别语音文字内容,调用百度接口异常。异常信息=" + e.getMessage());
        }
        return speechContentByBase64Response;
    }


    @Override
    @ApiOperation(value = "获取场景bot理解和应答的信息", httpMethod = "POST")
    public SpeechUtteranceResponse getUtteranceUnit(@RequestBody SpeechUtteranceRequest speechUtteranceRequest) {
        log.info("进入unit接口，获取bot应答语");
        SpeechUtteranceResponse speechUtteranceResponse = new SpeechUtteranceResponse();
        String accessToken = BaiduSpeechUtil.BAIDU_SPEECH_ACCESSTOKEN;
        String talkUrl = BaiduSpeechUtil.BAIDU_SPEECH_TALK_URL + "?access_token=" + accessToken;
        //请求参数
        String params = "{\"log_id\":\"UNITTEST_10000\",\"version\":\"2.0\",\"service_id\":\"S21061\",\"session_id\":\"\",\"request\":{\"query\":\"" + speechUtteranceRequest.getContent() + "\",\"user_id\":\"88888\"},\"dialog_state\":{\"contexts\":{\"SYS_REMEMBERED_SKILLS\":[\"1057\"]}}}";
        String result = "";
        try {
            result = HttpUtil.post(talkUrl, "application/json", params, "UTF-8");
        } catch (Exception e) {
            log.error("语音对话接口异常！");
            speechUtteranceResponse.setCode(203);
            speechUtteranceResponse.setMessage("语音对话接口异常！");
            throw new ServiceException("语音对话接口异常！"+e.getMessage());
        }
        JSONObject json = JSONObject.parseObject(result);
        String errorCode = json.getString("error_code");
        // 错误码为111(过期)110(错误)时，刷新token
        if ("111".equals(errorCode) || "110".equals(errorCode)) {
            BaiduTokenResponse unitAuth = baiduTokenService.getUnitAuth(getBaiduTokenRequest());
            BaiduSpeechUtil.BAIDU_SPEECH_ACCESSTOKEN = unitAuth.getAuthToken();
            return getUtteranceUnit(speechUtteranceRequest);
        } else {
            String str = json.getString("result");
            JSONObject jsonobject2 = JSONObject.parseObject(str);
            // 获得会话ID
            speechUtteranceResponse.setSessionId(jsonobject2.getString("session_id"));
            String response_list = jsonobject2.getString("response_list");
            JSONArray responseJSONArray = JSONArray.parseArray(response_list);
            String main_exe = JSONObject.parseObject(responseJSONArray.getJSONObject(0).getString("schema")).getString("intent");
            speechUtteranceResponse.setMainExe(main_exe);
            speechUtteranceResponse.setCode(200);
            log.info("语音对话内容为=" + speechUtteranceResponse.getVoiceText());
            if("BUILT_CHAT".equals(main_exe)){
                // 动作列表
                String actionList = responseJSONArray.getJSONObject(0).getString("action_list");
                // 首先把字符串转成JSONArray对象
                JSONArray jsonArray = JSONArray.parseArray(actionList);
                if (jsonArray.size() > 0) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject job = jsonArray.getJSONObject(i);
                        String say = job.getString("say");
                        speechUtteranceResponse.setData(say);
                    }
                }
            }
        }

        return speechUtteranceResponse;
    }


    @Override
    @ApiOperation(value = "闲聊场景接口", httpMethod = "POST")
    public SpeechUtteranceBotResponse getUtteranceOrbot(@RequestBody SpeechUtteranceBotRequest speechUtteranceBotRequest) {
        log.info("进入unit闲聊场景接口");
        SpeechUtteranceBotResponse speechUtteranceResponse = new SpeechUtteranceBotResponse();
        String accessToken = BaiduSpeechUtil.BAIDU_SPEECH_ACCESSTOKEN;
        String talkUrl = BaiduSpeechUtil.BAIDU_SPEECH_TALK_URL + "?access_token=" + accessToken;

        // 请求参数
        String params = "{\"scene_id\":" + BaiduSpeechUtil.BAIDU_SPEECH_SCENEID_BOT + ",\"session_id\":\"" + ""
                + "\",\"query\":\"" + speechUtteranceBotRequest.getContent() + "\"}";
        String result="";
        try {
            result = HttpUtil.post(talkUrl, "application/json", params, "UTF-8");
        } catch (Exception e) {
            log.error("访问百度闲聊接口失败");
            speechUtteranceResponse.setCode(203);
            speechUtteranceResponse.setMessage("语音对话接口异常！");
            throw new ServiceException("语音对话接口异常！"+e.getMessage());
        }
        // 将json字符串转成json对象
        String say = "我听不明白";

        JSONObject json = JSONObject.parseObject(result);
        if(result.contains("error_code")){
            String errorCode = json.getString("error_code");
            // 错误码为111(过期)110(错误)时，刷新token
            if ("111".equals(errorCode) || "110".equals(errorCode)) {
                BaiduTokenResponse unitAuth = baiduTokenService.getUnitAuth(getBaiduTokenRequest());
                BaiduSpeechUtil.BAIDU_SPEECH_ACCESSTOKEN = unitAuth.getAuthToken();
                return getUtteranceOrbot(speechUtteranceBotRequest);
            }

        }else{
            String str = json.getString("result");
            JSONObject jsonobject2 = JSONObject.parseObject(str);
            // 动作列表
            String actionList = jsonobject2.getString("action_list");
            // 首先把字符串转成JSONArray对象
            JSONArray jsonArray = JSONArray.parseArray(actionList);
            if (jsonArray.size() > 0) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject job = jsonArray.getJSONObject(i);
                    say = job.getString("say");
                    speechUtteranceResponse.setData(say);
                }
            }
            speechUtteranceResponse.setCode(200);
            log.info("闲聊场景,返回数据为=" + say);
        }
        return speechUtteranceResponse;
    }

    @Override
    @ApiOperation(value = "阿里合成语音接口", httpMethod = "POST")
    public AliSpeechBase64ByContentResponse getAliSpeechBase64ByContent(@RequestBody AliSpeechBase64ByContentRequest aliSpeechBase64ByContentRequest) {
        log.info("进入阿里合成语音接口");

        AliSpeechBase64ByContentResponse aliSpeechBase64ByContentResponse = new AliSpeechBase64ByContentResponse();
        String token = AliSpeechUtil.getToken(AliSpeechUtil.ALIYUN_SPEECH_ACCESSKEYID, AliSpeechUtil.ALIYUN_SPEECH_ACCESSKEYSECRET);

        JSONObject taskObject = new JSONObject();
        taskObject.put("appkey", AliSpeechUtil.ALIYUN_SPEECH_APPKEY);
        taskObject.put("token", token);
        taskObject.put("text", aliSpeechBase64ByContentRequest.getText());

        String format = aliSpeechBase64ByContentRequest.getFormat();
        if(StrUtil.isBlank(format)){
            format = "wav";
        }
        taskObject.put("format", format);

        String voice = aliSpeechBase64ByContentRequest.getVoice();
        if(StrUtil.isBlank(voice)){
            voice = "xiaoyun";
        }
        taskObject.put("voice", voice);

        Integer sampleRate = aliSpeechBase64ByContentRequest.getSampleRate();
        if(sampleRate==null || sampleRate!=8000){
            sampleRate = 16000;
        }
        taskObject.put("sample_rate", sampleRate);
        // volume 音量，范围是0~100，可选，默认50
        Integer volume = aliSpeechBase64ByContentRequest.getVolume();
        if(volume==null ){
            volume = 50;
        }
        taskObject.put("volume", volume);
        // speech_rate 语速，范围是-500~500，可选，默认是0
        Integer speechRate = aliSpeechBase64ByContentRequest.getSpeechRate();
        if(speechRate==null ){
            speechRate = 0;
        }
        taskObject.put("speech_rate", speechRate);
        // pitch_rate 语调，范围是-500~500，可选，默认是0
        Integer pitchRate = aliSpeechBase64ByContentRequest.getPitchRate();
        if(pitchRate==null ){
            pitchRate = 0;
        }
        taskObject.put("pitch_rate", pitchRate);
        String bodyContent = taskObject.toJSONString();
        System.out.println("POST Body Content: " + bodyContent);
        okhttp3.RequestBody reqBody = okhttp3.RequestBody.create(MediaType.parse("application/json"), bodyContent);
        Request request = new Request.Builder()
                .url(AliSpeechUtil.ALIYUN_SPEECH_CREATE_SPEECH_URL)
                .header("Content-Type", "application/json")
                .post(reqBody)
                .build();
        try {
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            String contentType = response.header("Content-Type");
            if ("audio/mpeg".equals(contentType)) {

                byte[] data = response.body().bytes();
                String fileBase64 = new BASE64Encoder().encode(data);

                aliSpeechBase64ByContentResponse.setBase64(fileBase64);
                aliSpeechBase64ByContentResponse.setCode(200);
                /*File f = new File(audioSaveFile);
                FileOutputStream fout = new FileOutputStream(f);
                fout.write(response.body().bytes());
                fout.close();
                System.out.println("The POST request succeed!");*/
            }
            else {
                // ContentType 为 null 或者为 "application/json"
                String errorMessage = response.body().string();
                System.out.println("The POST request failed: " + errorMessage);
                log.error("访问阿里合成语音接口失败");
                aliSpeechBase64ByContentResponse.setCode(203);
                aliSpeechBase64ByContentResponse.setMessage("阿里合成语音接口异常！");
                throw new ServiceException("阿里合成语音接口异常！"+errorMessage);
            }
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("访问阿里合成语音接口失败");
            aliSpeechBase64ByContentResponse.setCode(203);
            aliSpeechBase64ByContentResponse.setMessage("阿里合成语音接口异常！");
            throw new ServiceException("阿里合成语音接口异常！"+e.getMessage());
        }

        return aliSpeechBase64ByContentResponse;
    }
}
