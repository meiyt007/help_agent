package com.zfsoft.microservice.form.util;

import cn.hutool.core.util.StrUtil;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @ClassName FileDownUtil
 * @Description: 读取网络文件 http或者https
 * @Author wuxx
 * @Date 2021/12/9
 **/
public class FileDownUtil {


    /**
     * @description: 读取网络文件 http或者https
     * @param urlStr
     * @author: wuxx
     * @Date: 2021/12/9 11:45
     **/
    public static InputStream getFileInputStream(String urlStr) {
        try {
            if(StrUtil.isNotEmpty(urlStr)){
                if(urlStr.startsWith("https")){
                    return downLoadFromUrlHttps(urlStr);
                }else {
                    return downLoadFromUrlHttp(urlStr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从网络http类型Url中下载文件
     * @param urlStr
     * @throws IOException
     */
    private static InputStream downLoadFromUrlHttp(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        // 防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        conn.connect();

        // 得到输入流
        InputStream inputStream = conn.getInputStream();
        return inputStream;
    }

    /**
     * @description: 读取网络文件 http
     * @param urlStr
     * @author: wuxx
     * @Date: 2021/12/9 11:45
     **/
    private static InputStream downLoadFromUrlHttps(String urlStr) throws Exception {
        // 创建SSLContext
        SSLContext sslContext = SSLContext.getInstance("SSL");
        TrustManager[] tm = { new MyX509TrustManager() };
        // 初始化
        sslContext.init(null, tm, new java.security.SecureRandom());
        // 获取SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        // url对象
        URL url = new URL(urlStr);
        // 打开连接
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        /**
         * 这一步的原因: 当访问HTTPS的网址。您可能已经安装了服务器证书到您的JRE的keystore
         * 但是服务器的名称与证书实际域名不相等。这通常发生在你使用的是非标准网上签发的证书。
         *
         * 解决方法：让JRE相信所有的证书和对系统的域名和证书域名。
         *
         * 如果少了这一步会报错:java.io.IOException: HTTPS hostname wrong: should be <localhost>
         */
        conn.setHostnameVerifier(new MyX509TrustManager().new TrustAnyHostnameVerifier());
        // 设置一些参数
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        // 设置当前实例使用的SSLSoctetFactory
        conn.setSSLSocketFactory(ssf);
        conn.connect();
        // 得到输入流
        InputStream inputStream = conn.getInputStream();
        return inputStream;
    }
}
