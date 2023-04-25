package com.zfsoft.superwindow.util;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Random;
import java.util.UUID;

public class FileUtils {
    /**
     * @param filePath
     * @return
     * @description 根据文件远程路径读取文件流对象
     * @author wangyh
     * @date 2022/6/10
     **/
    public static MultipartFile tranFile(String filePath) {
        MultipartFile multipartFile = null;
        InputStream inputStream = null;
        if (StrUtil.isNotEmpty(filePath) && filePath.contains("http")) {
            try {
                if (filePath.startsWith("http://")) {
                    inputStream = getHttpFileInputStream(filePath);
                } else if (filePath.startsWith("https://")) {
                    inputStream = getHttpsFileInputStream(filePath);
                }
                multipartFile = new MockMultipartFile("file","", "multipart/form-data", inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != inputStream) {
                    try {
                        inputStream.close();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
            return multipartFile;
        }
        return null;
    }

    /**
     * @param filePath
     * @return
     * @description 根据文件远程路径读取文件（签章专用）
     * @author meiyt
     * @date 2022/6/10
     **/
    public static MultipartFile transFile(String filePath) {
        File file = null;
        MultipartFile multipartFile = null;
        InputStream inputStream = null;
        if (StrUtil.isNotEmpty(filePath) && filePath.contains("http")) {
            try {
                if (filePath.startsWith("http://")) {
                    file = getHttpFile(filePath);
                } else if (filePath.startsWith("https://")) {
                    file = getSSLFile(filePath);
                }
                inputStream = new FileInputStream(file);
                String fileName = System.currentTimeMillis() + ".pdf";
                multipartFile = new MockMultipartFile(fileName, fileName, "multipart/form-data", inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != inputStream) {
                    try {
                        inputStream.close();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                if (null != file && file.exists()) {
                    file.delete();
                }
            }
            return multipartFile;
        }
        return null;
    }
    /**
     * @param filePath
     * @return
     * @description 本地文件路径转为流
     * @author wuqy
     * @date 2022/9/27
     **/
    public static MultipartFile tranInputStream(String filePath,String fileName,String originIFilename) {
        MultipartFile multipartFile = null;
        InputStream inputStream = null;
        try{
            inputStream = new FileInputStream(filePath);
            multipartFile = new MockMultipartFile(fileName,originIFilename, "multipart/form-data", inputStream);
        }catch (Exception e){
            e.printStackTrace();
        }

        return multipartFile;
    }

    /**
     * @param filePath
     * @return
     * @description 本地文件路径转为流
     * @author wuqy
     * @date 2022/9/27
     **/
    public static MultipartFile tranInputStream(String filePath) {
        MultipartFile multipartFile = null;
        InputStream inputStream = null;
       try{
           inputStream = new FileInputStream(filePath);
           multipartFile = new MockMultipartFile("file","", "multipart/form-data", inputStream);
       }catch (Exception e){
           e.printStackTrace();
       }

       return multipartFile;
    }


    /**
     * @param filePath
     * @return
     * @description 删除文件夹
     * @author wuqy
     * @date 2022/9/27
     **/
    public static void deleteFile(File file) {
        if(!file.exists()){//判断文件是否存在
            System.out.println("文件不存在！");
            return ;
        }else{
            File files[]=file.listFiles();
            for(File newfile:files){//遍历文件夹下的目录
                if(newfile.isFile()){//如果是文件而不是文件夹==>可直接删除
                    newfile.delete();
                }else{
                    deleteFile(newfile);//是文件夹,递归调用方法
                }
            }
            System.out.println("已删除"+file.getName());
            file.delete();
        }
    }



    /**
     * @param fileUrl
     * @return
     * @description 读取http地址文件
     * @author wangyh
     * @date 2022/9/23
     **/
    public static DataInputStream getHttpsFileInputStream(String fileUrl) {
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
            httpconn = (HttpURLConnection) conn;
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
                if (null != httpconn) {
                    httpconn.disconnect();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return in;
    }

    /**
     * @param fileUrl
     * @return
     * @description 读取http地址文件
     * @author wangyh
     * @date 2022/9/23
     **/
    public static DataInputStream getHttpFileInputStream(String fileUrl) {
//        String file_name = reloadFile(fileUrl);
//        File file = null;
        DataInputStream in = null;
//        DataOutputStream out = null;
        HttpURLConnection httpconn = null;
        try {
//            file = File.createTempFile(UUID.randomUUID().toString().replace("-", ""), file_name);
            URL url = new URL(fileUrl);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(6000);
            conn.setReadTimeout(6000);
            httpconn = (HttpURLConnection) conn;
            int code = httpconn.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                System.out.println("文件读取失败resp_code:" + code);
                return null;
            }
            // 读文件流
            in = new DataInputStream(httpconn.getInputStream());
//            out = new DataOutputStream(new FileOutputStream(file));
//            byte[] buffer = new byte[2048];
//            int count = 0;
//            while ((count = in.read(buffer)) > 0) {
//                out.write(buffer, 0, count);
//            }
//            out.close();
//            in.close();
//            httpconn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
//                if (null != out) {
//                    out.close();
//                }
//                if (null != in) {
//                    in.close();
//                }
//                if (null != httpconn) {
//                    httpconn.disconnect();
//                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return in;
    }

    /**
     * @param fileUrl
     * @return
     * @description 读取http地址文件
     * @author meiyt
     * @date 2022/5/23
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
            httpconn = (HttpURLConnection) conn;
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
                if (null != httpconn) {
                    httpconn.disconnect();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return file;
    }

    /**
     * @param fileUrl
     * @return
     * @description 读取HTTPS地址文件
     * @author meiyt
     * @date 2022/5/23
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
                if (null != urlCon) {
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

    /**
     * 根据远程文件地址下载文件到浏览器
     *
     * @param urlStr
     * @param response
     * @throws IOException
     */
    public static void downloadFromUrl(String urlStr, HttpServletResponse response) throws IOException {
        OutputStream out = null;
        ByteArrayOutputStream bas = null;
        InputStream is = null;

        try {
            String fileNam = URLEncoder.encode( "file" + new Random(1000).nextInt() + urlStr.substring(urlStr.lastIndexOf(".")), "UTF-8");
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int fileLen = connection.getContentLength();
            connection.setConnectTimeout(3000);
            connection.connect();
            int code = connection.getResponseCode();

            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/octet-stream;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileNam);
            out = response.getOutputStream();

            if (code == 200) {
                is = connection.getInputStream();
                byte[] b = new byte[1024];
                bas = new ByteArrayOutputStream();
                int len;
                while ((len = is.read(b)) != -1) {
                    bas.write(b, 0, len);
                }
                byte[] src = bas.toByteArray();
                byte[] dest = new byte[fileLen];
                System.arraycopy(src, 0, dest, 0, fileLen);
                out.write(dest);
                out.flush();

            }
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //先关闭外层，后里层
            out.close();
            bas.close();
            is.close();

        }
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

    public static void main(String[] args) {
        MultipartFile file = FileUtils.tranFile("http://139.9.123.180/group1/M00/00/28/iwl7tGMrArCAeHC2AACGuUyWFLU635.jpg");
        String as ="";
        //        System.out.printf("file" + new Random(1000).nextInt() + urlStr.substring(urlStr.lastIndexOf(".")));
    }
}
