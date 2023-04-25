package com.zfsoft.single.controller.insideapi;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.single.service.insideapi.websocket.IntelligentWebsocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

/**
 * @（#）: IntelligentWebsocket
 * @description: 智能语音Websocket
 * @author: wangwg
 * @date: 2021/6/23
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
public class IntelligentWebsocketController implements IntelligentWebsocketService {

    @Value("${zfsoft.smartAsk.askUrl}")
    private String askUrl;

    @Value("${zfsoft.smartAsk.authUrl}")
    private String authUrl;

    @Value("${zfsoft.smartAsk.secretKey}")
    private String  secretKey;

/*    @Override
    public ApiResultSet<String> intelligentAsk(String message) {
        String responseBody = null;
        try {
            String json ="{\n" +
                    "  \"XX\": {\n" +
                    "    \"IPID\": \"3322221\",\n" +
                    "    \"MCID\": \"\",\n" +
                    "    \"SFID\": \"\",\n" +
                    "    \"DHID\": \"\"\n" +
                    "  },\n" +
                    "  \"ZL\": \"\",\n" +
                    "  \"MC\": \"\",\n" +
                    "  \"YW\": \"\",\n" +
                    "  \"JH\": {\n" +
                    "    \"ANSWER\": \"\",\n" +
                    "    \"XM\": \"上海市\",\n" +
                    "    \"ASK\": \""+URLDecoder.decode(message, "utf-8")+"\"\n" +
                    "  }\n" +
                    "}";
            System.out.println(json);
            RestTemplate restTemplate = new RestTemplate();
            //创建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<String>(json, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(askUrl, entity, String.class);
            responseBody = responseEntity.getBody();
            System.out.println(convert(responseBody));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  new ApiResultSet(convert(responseBody));
    }*/

    public static String convert(String utfString){
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;
        int  iint=0;
        while((i=utfString.indexOf("\\u", pos)) != -1){
            String sd = utfString.substring(pos, i);
            sb.append(sd);
            iint = i+5;

            if(iint < utfString.length()){
                pos = i+6;
                sb.append((char)Integer.parseInt(utfString.substring(i+2, i+6), 16));
            }
        }
        String endStr = utfString.substring(iint+1, utfString.length());
        return sb+""+endStr;
    }

    @Override
    public ApiResultSet<String> intelligentAsk(String message) {
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        String  clientIp;
        if(currentLoginUser == null){
            clientIp = "0000000000000000000000010";//测试用的
            //clientIp = "1111";
        }else{
            clientIp =  currentLoginUser.getUserOid();
        }
/*        Map<String, Object> authMap = new HashMap<String, Object>();
        authMap.put("secretKey",secretKey);*/
        JSONObject authJson = new JSONObject();
        authJson.put("secretKey",secretKey);
        //String authBody =  HttpRequestUtil.sendPost(authUrl,authMap);
        log.info("调用智能咨询系统-鉴权接口参数 authUrl = " +authUrl + " secretKey = "+secretKey);
        String authBody= HttpUtil.post(authUrl, authJson.toString());
        log.info("authBody  -----  "+ authBody);
        JSONObject authBodyResult = JSON.parseObject(authBody);
        String code =  authBodyResult.getString("code");
        String token = authBodyResult.getString("token");
        if(!"200".equalsIgnoreCase(code)){
            log.error("调用鉴权接口失败 url = "+authUrl+" secretKey =  "+secretKey);
            ApiResultSet<String> authResult = new ApiResultSet("调用鉴权接口失败 url = "+authUrl+" secretKey =  "+secretKey);
            authResult.setCode(ApiResultSet.UNKNOWN_ERROR);
            return authResult;
        }
        JSONObject consolutionJson = new JSONObject();
        consolutionJson.put("token",token);
        consolutionJson.put("question",message);
        consolutionJson.put("clientIp",clientIp);
        consolutionJson.put("distriction","");
        log.info("调用智能咨询系统-咨询接口参数 askUrl = " +askUrl + " consolutionJson = "+consolutionJson.toString());
        String consolutionBody = HttpUtil.post(askUrl, consolutionJson.toString());
        log.info("consolutionBody  -----  "+ consolutionBody);
        return new ApiResultSet(consolutionBody);
    }



}
