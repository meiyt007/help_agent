package com.zfsoft.ha.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Base64.Encoder;
import java.util.zip.Deflater;


@SuppressWarnings("deprecation")
@Component
public class CalSig {
    //private static final String appId ="TfVBOgkSCDKNNLCGulCcjCnKUr6pBak0";
    //private static final String appSecret ="94GTkOZZhWxxL1idGdVtM00gIFHTcKef";
    private static String appId;
    private static String appSecret;
    @Value("${baijiayun.appId}")
    public void setAppId(String appId) {CalSig.appId = appId;}
    @Value("${baijiayun.appSecret}")
    public void setAppSecret(String appSecret) {CalSig.appSecret = appSecret;}

    public static String getAppid() {
        return appId;
    }

    final static Encoder base64_Encoder = Base64.getEncoder();
//    public CalSig(String secret, String appid) {
//        super();
//        this.secret = secret;
//        this.appid = appid;
//    }
    /**
     * @param str 需要编码的字符串数据
     * @return string 编码后的base64串
     * @throws Exception
     * @throws \Exception
     */
    public static String base64_url_encode(String str) throws Exception {
        Map<String,String> characterMap=new HashMap<String,String>();
        characterMap.put("\\+", "*");
        characterMap.put("\\/", "-");
        characterMap.put("\\=", "_");
        //将字符串中的+,/,=替换成*,-,_；可利用正则表达式
        for(Map.Entry<String,String> entry : characterMap.entrySet()) {
            str=str.replaceAll(entry.getKey(),entry.getValue());
        }
        return str;
    }
    /**
     * 使用 hmac sha256 生成 sig 字段内容,经过 base64 编码
     * @param  identifier 用户名,utf-8 编码
     * @param  curr_time 当前生成 sig 的 unix 时间戳
     * @param  expire 有效期,单位秒
     * @return string base64 后的 sig
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     */
    public static String hmacsha256(String identifier,String room,long curr_time,int expire) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String content_to_be_signed="TLS.identifier:"+identifier+"\n"
                +"TLS.room:"+room+"\n"
                +"TLS.sdkappid:"+appId+"\n"
                +"TLS.time:"+curr_time+"\n"
                +"TLS.expire:"+expire+"\n";
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(appSecret.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array=sha256_HMAC.doFinal(content_to_be_signed.getBytes("UTF-8"));
        return new String(base64_Encoder.encode(array));
    }

    /**
     * @param identifier 用户身份标识
     * @param expire 过期时间,单位秒,默认 1天
     * @return string 签名字符串
     * @throws Exception
     * @throws \Exception
     */
    public static String genSign(String identifier, String room, int expire) throws Exception  {
        System.out.println("百家云加密内容roomId：" + room + "，userId：" + identifier + "，appId：" + appId);
        long curr_time=System.currentTimeMillis();
        String hashhmacStr=hmacsha256(identifier,room,curr_time,expire);
        //为了使输出键的顺序和输入的顺序保持一致，使用LinkedHashMap
        LinkedHashMap<String,Object> signMap=new LinkedHashMap<String,Object>();
        signMap.put("TLS.ver", "2.0");
        signMap.put("TLS.identifier", identifier);
        signMap.put("TLS.room", room);
        signMap.put("TLS.sdkappid", appId);
        signMap.put("TLS.expire", expire);
        signMap.put("TLS.time", curr_time);
        signMap.put("TLS.sig", hashhmacStr.replaceAll("/", "\\\\/"));
        byte[] _compressed = compress((StringEscapeUtils.unescapeJava(JSONObject.toJSONString(signMap))).getBytes());
        byte[] _encodeCompress = Base64.getEncoder().encode(_compressed);
        String str=new String(_encodeCompress);
        return base64_url_encode(str);
    }
    public static byte[] compress(byte[] bytes){
        byte[] output = new byte[1024];
        Deflater compresser = new Deflater();
        compresser.setInput(bytes);
        compresser.finish();
        int compressedDataLength = compresser.deflate(output);
        return Arrays.copyOf(output,compressedDataLength);
    }

    public static void main(String[] args) throws Exception {
        String sig = genSign("1000","1000",1000);
        String as = "";
    }
}
