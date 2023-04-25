package com.zfsoft.ha.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by Bo on 2017/7/19.
 */
public class AESUtil {

    //加密
    public String Encrypt(String str) {
        try{
            String key = "9d9a09411b884815b7fc2e5abd29b007"; //密钥
            byte[] kb = key.getBytes("utf-8");
            SecretKeySpec sks = new SecretKeySpec(kb, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//算法/模式/补码方式
            cipher.init(Cipher.ENCRYPT_MODE, sks);
            byte[] eb = cipher.doFinal(str.getBytes("utf-8"));
            return new Base64().encodeToString(eb);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //解密
    public String Decrypt(String str) {
        try{
            String key = "11223344556677889900112233445566"; //密钥
            byte[] kb = key.getBytes("utf-8");
            SecretKeySpec sks = new SecretKeySpec(kb, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, sks);
            byte[] by = new Base64().decode(str);
            byte[] db = cipher.doFinal(by);
            return new String(db);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws Exception
    {
        AESUtil aes=new AESUtil();
        String en = aes.Encrypt("2544ad72-cf27-47e6-95dc-71f4561c57abfb87ebe7-3bf6-45b0-b880-aba4db1684db");
        System.out.println("加密:"+en);

        String de = aes.Decrypt("yCzl6qlHZ4bBaMenO5s6Jw==");
        System.out.println("解密:"+de);
    }
}
