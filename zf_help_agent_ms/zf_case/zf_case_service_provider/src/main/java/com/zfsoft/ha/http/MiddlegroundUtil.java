package com.zfsoft.ha.http;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description //类名称
 * @Author: Wangyh
 * @Date: 2023/1/31 13:43
 */
public class MiddlegroundUtil {
    private static final byte[] SECRET_KEY = "1674f0185bb64eccac7fdafe11432f07".getBytes();

    /**
     * 加密请求头方法
     * @param str
     * @return
     * @throws Exception
     */
    private static String encrypt(String str ) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] bytes = cipher.doFinal(str.getBytes("UTF-8"));
        return new Base64().encodeToString(bytes);
    }

    public static void main(String[] args) throws Exception {
        String APP_ID ="3b9cd761-0634-4afd-af5c-b77a2306e2dc";
        Map map = new HashMap<>();
        String apiName = "d1f15aa3-6cf1-4ae2-8e94-03847b008ad6";
        String content = APP_ID + apiName + System.currentTimeMillis() / 1000;
        String signature = MiddlegroundUtil.encrypt(content);
        map.put("appid", APP_ID);
        map.put("apiname", apiName);
        map.put("signature", signature);
        System.out.println("map"+map);
    }

}

