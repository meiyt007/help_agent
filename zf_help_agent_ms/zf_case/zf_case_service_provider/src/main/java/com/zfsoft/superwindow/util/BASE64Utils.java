package com.zfsoft.superwindow.util;

import java.io.IOException;
import java.util.Base64;

/**
 * @ClassName BASE64Utils
 * @Description TODO
 * @Author chenjm
 * @Date 2021/4/14 10:21
 * @Version 1.0
 */
public class BASE64Utils {
    // 已经直接写成了最简单直接的方法，丢在工具类 ，无脑调用就行
    public static String base64Encode(String text)  {
        String returnStr="";
        try{
            returnStr= Base64.getEncoder().encodeToString(text.getBytes("UTF-8"));
        }catch (Exception e){

        }
        return returnStr;
    }

    public static String base64Decode(String text){
        String returnStr="";
        try{
            returnStr= new String(Base64.getDecoder().decode(text), "UTF-8");
        }catch (Exception e){

        }
        return returnStr;
    }
}
