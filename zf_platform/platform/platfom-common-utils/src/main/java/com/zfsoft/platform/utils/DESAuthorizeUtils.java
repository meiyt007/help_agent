package com.zfsoft.platform.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * @description: 授权加密解密工具类
 * @author: wuxx
 * @Date: 2021/3/11 15:17
 **/
public class DESAuthorizeUtils {

    /**
     * 密钥
     */
    private static String DEFAULT_KEY = "ZFSOFT88";

    /**
     * 加密
     * @param message 需要加密的内容
     * @return
     * @throws Exception
     */
    public static String encrypt(String message){
        return toHexString(encrypt(message, DEFAULT_KEY)).toUpperCase();
    }

    /**
     * 解密
     * @param message 加密后的内容
     * @return
     * @throws Exception
     */
    public static String decrypt(String message){
        try {
            return java.net.URLDecoder.decode(decrypt(message, DEFAULT_KEY), "utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     * @param message 加密后的内容
     * @param key 密钥
     * @return
     * @throws Exception
     */
    private static String decrypt(String message, String key) {
        try {
            byte[] bytesrc = convertHexString(message);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] retByte = cipher.doFinal(bytesrc);
            return new String(retByte);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     *
     * @param message 加密后的内容
     * @param key 密钥
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(String message, String key) {
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            return cipher.doFinal(message.getBytes("UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
       return null;
    }

    private static byte[] convertHexString(String ss) {
        byte digest[] = new byte[ss.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }
        return digest;
    }

    private static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }
        return hexString.toString();
    }

}
