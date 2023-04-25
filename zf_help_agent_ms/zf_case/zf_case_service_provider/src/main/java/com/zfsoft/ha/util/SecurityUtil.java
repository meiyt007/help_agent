package com.zfsoft.ha.util;

import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author HHUA
 */
public class SecurityUtil {
    /**
     * optional value AES/DES/DESede
     */
    public static String DES = "AES";

    /**
     * optional value AES/DES/DESede
     */
    public static String CIPHER_ALGORITHM = "AES";

    /**
     * 生成待签名字符串
     * @param appId 应用ID app_ID
     * @param appKey 应用秘钥app_KEY
     * @param apiName 应用调用api的APIID
     * @return
     */
    public static String getSignature(String appId,String appKey,String apiName){
        // String signature= appid+ apiname+timestamp
        long time = (DateTime.now().toDate().getTime())/1000;
        String timestamp = String.valueOf(time);
        String signature = appId+apiName+timestamp;
        return signature;
    }

    /**
     * 计算机签名：对待签名字符串进行AES加密(统一服务支撑平台接口调用参数处理)
     * @param appKey 应用密钥
     * @param signature 签名字符串
     * @return
     * @throws Exception
     */
    @SuppressWarnings("restriction")
    public static String encryptSignature(String appKey,String signature) throws Exception{
        appKey = appKey.replaceAll("-", "");
        SecretKeySpec keySpec = new SecretKeySpec(appKey.getBytes("utf-8"), DES);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] bytes = cipher.doFinal(signature.getBytes("utf-8"));
        return new Base64().encodeToString(bytes);
//        String signatureOfEncrypt = new BASE64Encoder().encode(bytes);
//        signatureOfEncrypt = signatureOfEncrypt.replace("\r\n", "");
//        return signatureOfEncrypt;
    }

    public static Key getSecretKey(String key) throws Exception {
        SecretKey securekey = null;
        if (key == null) {
            key = "";
        }
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(key.getBytes());
        keyGenerator.init(128, secureRandom);
        securekey = keyGenerator.generateKey();
        return securekey;
        /*
         * KeyGenerator keyGenerator = KeyGenerator.getInstance(DES);
         * keyGenerator.init(new SecureRandom(key.getBytes())); securekey =
         * keyGenerator.generateKey(); return securekey;
         */
    }

    public static String encrypt(String data, String key) throws Exception {
        SecureRandom sr = new SecureRandom();
        Key securekey = getSecretKey(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        byte[] bt = cipher.doFinal(data.getBytes());
        String strs = new BASE64Encoder().encode(bt);
        return strs;
    }

    public static String detrypt(String message, String key) throws Exception {
        SecureRandom sr = new SecureRandom();
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        Key securekey = getSecretKey(key);
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        byte[] res = new BASE64Decoder().decodeBuffer(message);
        res = cipher.doFinal(res);
        return new String(res);
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) throws Exception {
        String message = "customer:质控中心发布的版本名称,version:UEGPv8.0,serialNumber:0179917fa5674667ad32cb7c6b1070d4,ip:192.168.0.139,TIME:1478759170688";
        String key = "zfsoft888";
        String entryptedMsg = encrypt(message, key).replaceAll(System.getProperty("line.separator"), "");
        String test = "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";
        String decryptedMsg = detrypt(test, key);
    }
}
