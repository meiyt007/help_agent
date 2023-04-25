package com.zfsoft.single.util;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class FileUtils {

    /**
     * @description 根据文件远程路径读取文件（签章专用）
     * @param filePath
     * @author meiyt
     * @date 2022/6/10
     * @return
     **/
    public static MultipartFile transFile (String filePath) {
        File file = null;
        MultipartFile multipartFile = null;
        InputStream inputStream = null;
        if(StrUtil.isNotEmpty(filePath) && filePath.contains("http")) {
            try {
                if(filePath.startsWith("http://")) {
                    file = getHttpFile(filePath);
                } else if(filePath.startsWith("https://")) {
                    file = getSSLFile(filePath);
                }
                inputStream = new FileInputStream(file);
                String fileName = System.currentTimeMillis() + ".pdf";
                multipartFile = new MockMultipartFile(fileName, fileName, "multipart/form-data", inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(null != inputStream) {
                    try {
                        inputStream.close();
                    } catch (Exception e1){
                        e1.printStackTrace();
                    }
                }
                if(null != file && file.exists()) {
                    file.delete();
                }
            }
            return multipartFile;
        }
        return null;
    }

    /**
     * @description 读取http地址文件
     * @param fileUrl
     * @author meiyt
     * @date 2022/5/23
     * @return
     **/
    public static File getHttpFile(String fileUrl) {
        String file_name = reloadFile(fileUrl);
        File file = null;
        DataInputStream in = null;
        DataOutputStream out = null;
        HttpURLConnection httpconn = null;
        try {
            file = File.createTempFile(UUID.randomUUID().toString().replace("-", ""), file_name);
            URL url = new URL(fileUrl);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(6000);
            conn.setReadTimeout(6000);
            httpconn = (HttpURLConnection)conn;
            int code = httpconn.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                System.out.println("文件读取失败resp_code:" + code);
                return null;
            }
            // 读文件流
            in = new DataInputStream(httpconn.getInputStream());
            out = new DataOutputStream(new FileOutputStream(file));
            byte[] buffer = new byte[2048];
            int count = 0;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            out.close();
            in.close();
            httpconn.disconnect();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
                if (null != in) {
                    in.close();
                }
                if(null != httpconn) {
                    httpconn.disconnect();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return file;
    }

    /**
     * @description 读取HTTPS地址文件
     * @param fileUrl
     * @author meiyt
     * @date 2022/5/23
     * @return
     **/
    public static File getSSLFile(String fileUrl) {
        String file_name = reloadFile(fileUrl);
        File file = null;
        DataInputStream in = null;
        DataOutputStream out = null;
        HttpsURLConnection urlCon = null;
        try {
            file = File.createTempFile(UUID.randomUUID().toString().replace("-", ""), file_name);
            SSLContext sslcontext = SSLContext.getInstance("SSL", "SunJSSE");
            sslcontext.init(null, new TrustManager[]{new X509TrustUtil()}, new java.security.SecureRandom());
            URL url = new URL(fileUrl);
            HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslsession) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
            urlCon = (HttpsURLConnection) url.openConnection();
            urlCon.setConnectTimeout(6000);
            urlCon.setReadTimeout(6000);
            int code = urlCon.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                System.out.println("文件读取失败resp_code:" + code);
                return null;
            }
            // 读文件流
            in = new DataInputStream(urlCon.getInputStream());
            out = new DataOutputStream(new FileOutputStream(file));
            byte[] buffer = new byte[2048];
            int count = 0;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            out.close();
            in.close();
            urlCon.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
                if (null != in) {
                    in.close();
                }
                if(null != urlCon) {
                    urlCon.disconnect();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return file;
    }

    private static String reloadFile(String oleFileName) {
        oleFileName = getFileName(oleFileName);
        if (StrUtil.isEmpty(oleFileName)) {
            return oleFileName;
        }
        //得到后缀
        if (oleFileName.indexOf(".") == -1) {
            //对于没有后缀的文件，直接返回重命名
            return IdUtil.randomUUID().toString();
        }
        String[] arr = oleFileName.split("\\.");
        // 根据uuid重命名图片
        String fileName = IdUtil.randomUUID() + "." + arr[arr.length - 1];
        return fileName;
    }

    private static String getFileName(String url) {
        if (StrUtil.isEmpty(url)) {
            return url;
        }
        String newUrl = url;
        newUrl = newUrl.split("[?]")[0];
        String[] bb = newUrl.split("/");
        String fileName = bb[bb.length - 1];
        return fileName;
    }
}
