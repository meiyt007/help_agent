package com.zfsoft.superwindow.util;

import com.zfsoft.platform.utils.fileUtil.SslUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author: liangss
 * @create: 2020-12-05
 * @description: Base64 操作类
 */
public class Base64FileUitl {

    public static String Image2Base64(String imgUrl) {
        String result = null;
        URL url = null;
        InputStream inputStream = null;
        ByteArrayOutputStream outStream = null;
        HttpURLConnection connection = null;
        try{
            url = new URL(imgUrl);
            if(imgUrl.startsWith("https")) {
                SslUtils.ignoreSsl();
                connection = (HttpsURLConnection)url.openConnection();
            } else {
                connection = (HttpURLConnection)url.openConnection();
            }
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            inputStream = connection.getInputStream();
            outStream = new ByteArrayOutputStream();
            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while( (len=inputStream.read(buffer)) != -1 ){
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            // 对字节数组Base64编码
            result= new BASE64Encoder().encode(outStream.toByteArray());
        }catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(inputStream != null)
            {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outStream != null)
            {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null)
            {
                connection.disconnect();
            }
        }
        return result;
    }

    /**
     * 将文件转成base64 字符串
     *
     * @parampath文件路径
     * @return
     * @throws Exception
     */
    public static String encodeBase64File(String path) {

        byte[] buffer = null;
        FileInputStream inputFile = null;
        String pdfBase64Str = null;
        try {
            File file = new File(path);
            inputFile = new FileInputStream(file);
            buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            pdfBase64Str = new BASE64Encoder().encode(buffer);
            pdfBase64Str = pdfBase64Str.replace("\n", "").replace("\t", "").replace("\r", "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pdfBase64Str;
    }

    /**
     * 将base64字符解码保存文件
     *
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */
    public static void decoderBase64File(String base64Code, String targetPath) throws Exception {
        FileOutputStream out = null;
        try {
            byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
            out = new FileOutputStream(targetPath);
            out.write(buffer);
        } finally {
            if (out != null){
                out.close();
            }

        }

    }

    /**
     * 将base64字符保存文本文件
     *
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */
    public static void toFile(String base64Code, String targetPath) throws Exception {

        byte[] buffer = base64Code.getBytes();
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }

}
