package com.zfsoft.ocr.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


/***
* @Description: ocr卡证证照识别
* @Author:liangss
* @Date:2021/10/27 
* @Param: 
*/
@RestController
@Slf4j
public class CeshiController {

    public static void main(String[] args) throws Exception {
        log.info("开始测试");
        // 签字测试
        String cs="";
        String url = "https://api.textin.com/robot/v1.0/api/387808e16375162e3e2e291b8bbea562";
        // 请登录后前往 “工作台-账号设置-开发者信息” 查找 x-ti-app-id，示例代码中 x-ti-app-id 非真实数据
        String appId = "2c8b8df4cdc023139a0ecb7c785ac99b";
        // 请登录后前往 “工作台-账号设置-开发者信息” 查找 x-ti-secret-code，示例代码中 x-ti-secret-code 非真实数据
        String secretCode = "428f09d3f5c99855f3c83f14c24998af";

        BufferedReader in = null;
        DataOutputStream out = null;
        String result = "";
        try {
           /*  // image*/
            byte[] imgData = readfile("C:\\Users\\Administrator\\Desktop\\tp\\020170502163059411.jpg"); // image
           // byte[] imgData = transformBase64("example.jpg");
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("x-ti-app-id", appId);
            conn.setRequestProperty("x-ti-secret-code", secretCode);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST"); // 设置请求方式
            out = new DataOutputStream(conn.getOutputStream());
            out.write(imgData);
            out.flush();
            out.close();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println(result);
    }
    public static byte[] readfile(String path)
    {
        String imgFile = path;
        InputStream in = null;
        byte[] data = null;
        try
        {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private  static byte[] transformBase64(String str) {
        BASE64Decoder decode = new BASE64Decoder();
        byte[] b = null;
        try {
            b = decode.decodeBuffer(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }


}
