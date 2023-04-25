package com.zfsoft.ha.util;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class HttpComponentUtil {

    private static final String DEFAULT_ENCODING = "GBK";//编码
    private static final int PROTECTED_LENGTH = 1024000;// 输入流保护 50KB

    public static final ContentType TEXT_PLAIN = ContentType.create("text/plain", StandardCharsets.UTF_8);
    //从连接时获取时间
    public static int CONNECTION_REQUEST_TIMEOUT = 1000;
    //建立连接时间（三次握手时间）
    public static int CONNECTION_TIMEOUT = 2000;
    //服务器返回数据时间
    public static int SOCKET_TIMEOUT = 50000;
    /**
     * HttpClient 连接池
     */
    private static PoolingHttpClientConnectionManager cm = null;

    static {
        // 初始化连接池，可用于请求HTTP/HTTPS（信任所有证书）
        cm = new PoolingHttpClientConnectionManager(getRegistry());
        // 整个连接池最大连接数
        cm.setMaxTotal(100);
        // 每路由最大连接数，默认值是2
        cm.setDefaultMaxPerRoute(5);
    }

    /**
     * 获取 HTTPClient注册器
     *
     * @return
     * @throws Exception
     */
    private static Registry<ConnectionSocketFactory> getRegistry() {
        Registry<ConnectionSocketFactory> registry = null;

        try {
            registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", new PlainConnectionSocketFactory()).register("https", getSSLFactory()).build();
        } catch (Exception e) {
            System.out.println("获取 HTTPClient注册器失败" + e);
        }
        return registry;
    }

    /**
     * 获取HTTPS SSL连接工厂
     * <p>跳过证书校验，即信任所有证书</p>
     *
     * @return
     * @throws Exception
     */
    private static SSLConnectionSocketFactory getSSLFactory() throws Exception {
        // 设置HTTPS SSL证书信息，跳过证书校验，即信任所有证书请求HTTPS
        SSLContextBuilder sslBuilder = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                return true;
            }
        });

        // 获取HTTPS SSL证书连接上下文
        SSLContext sslContext = sslBuilder.build();
        // 获取HTTPS连接工厂
        SSLConnectionSocketFactory sslCsf = new SSLConnectionSocketFactory(sslContext, new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
        return sslCsf;
    }

    /**
     * 发送 HTTP GET请求
     * <p>不带请求参数和请求头</p>
     *
     * @param url 地址
     * @return
     * @throws Exception
     */
    public static String httpGet(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);

        return doHttp(httpGet);
    }

    /**
     * 发送 HTTP GET请求
     * <p>带请求参数，不带请求头</p>
     *
     * @param url    地址
     * @param params 参数
     * @return
     * @throws Exception
     * @throws Exception
     */
    public static String httpGet(String url, Map<String, Object> params) throws Exception {
        // 转换请求参数
        List<NameValuePair> pairs = covertParams2NVPS(params);

        // 装载请求地址和参数
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);
        ub.setParameters(pairs);

        HttpGet httpGet = new HttpGet(ub.build());

        return doHttp(httpGet);
    }

    /**
     * 发送 HTTP GET请求
     * <p>带请求参数和请求头</p>
     *
     * @param url     地址
     * @param headers 请求头
     * @param params  参数
     * @return
     * @throws Exception
     * @throws Exception
     */
    public static String httpGet(String url, Map<String, Object> headers, Map<String, Object> params) throws Exception {
        // 转换请求参数
        List<NameValuePair> pairs = covertParams2NVPS(params);

        // 装载请求地址和参数
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);
        ub.setParameters(pairs);

        HttpGet httpGet = new HttpGet(ub.build());
        // 设置请求头
        for (Entry<String, Object> param : headers.entrySet())
            httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));

        return doHttp(httpGet);
    }

    /**
     * 发送 HTTP POST请求
     * <p>不带请求参数和请求头</p>
     *
     * @param url 地址
     * @return
     * @throws Exception
     */
    public static String httpPost(String url) throws Exception {
        HttpPost httpPost = new HttpPost(url);

        return doHttp(httpPost);
    }

    /**
     * 发送 HTTP POST请求
     * <p>带请求参数和请求头、参数为json格式</p>
     *
     * @param url     地址
     * @param map 请求头参数
     * @param data  json参数
     * @return
     * @throws Exception
     */
    public static String httpPostJsonStr(String url, Map<String, Object> map, String data) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        // 设置请求参数
        httpPost.setEntity(new StringEntity(data, StandardCharsets.UTF_8.name()));
        // 设置请求头
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        for (Entry<String, Object> param : map.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        return doHttp(httpPost);
    }

    /**
     * 发送 HTTP POST请求
     * <p>带请求参数，不带请求头</p>
     *
     * @param url    地址
     * @param params 参数
     * @return
     * @throws Exception
     */
    public static String httpPost(String url, Map<String, Object> params) throws Exception {
        // 转换请求参数
        List<NameValuePair> pairs = covertParams2NVPS(params);

        HttpPost httpPost = new HttpPost(url);
        // 设置请求参数
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8.name()));

        return doHttp(httpPost);
    }

    /**
     * 发送 HTTP POST请求
     * <p>带请求参数和请求头</p>
     *
     * @param url     地址
     * @param headers 请求头
     * @param params  参数
     * @return
     * @throws Exception
     */
    public static String httpPost(String url, Map<String, Object> headers, Map<String, Object> params) throws Exception {
        // 转换请求参数
        List<NameValuePair> pairs = covertParams2NVPS(params);

        HttpPost httpPost = new HttpPost(url);
        // 设置请求参数
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8.name()));
        // 设置请求头
        for (Entry<String, Object> param : headers.entrySet())
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));

        return doHttp(httpPost);
    }

    /**
     * 发送 HTTP POST请求，参数格式JSON
     * <p>请求参数是JSON格式，数据编码是UTF-8</p>
     *
     * @param url
     * @param param
     * @return
     * @throws Exception
     */
    public static String httpPostJson(String url, String param) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        // 设置请求头
        httpPost.addHeader("Content-Type", "application/json; charset=UTF-8");
        // 设置请求参数
        httpPost.setEntity(new StringEntity(param, StandardCharsets.UTF_8.name()));

        return doHttp(httpPost);
    }

    /**
     * application/x-www-form-urlencoded
     * @param url
     * @param map
     * @return
     * @throws Exception
     */
    public static String httpPostXwwwform(String url,  Map<String, String> map) throws Exception{
    	 HttpPost httpPost = new HttpPost(url);
         // 设置请求头
    	 httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
         // 设置请求参数
         List<NameValuePair> nvps = new ArrayList<NameValuePair>();
         if(map!=null){
             for (Entry<String, String> entry : map.entrySet()) {
                 nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
             }
         }
         httpPost.setEntity(new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8.name()));
         return doHttp(httpPost);
    }

    /**
     * 将Map键值对拼接成QueryString字符串，UTF-8编码
     *
     * @param params
     * @return
     * @throws Exception
     */
    public static String getQueryStr(Map<String, Object> params) throws Exception {
        return URLEncodedUtils.format(covertParams2NVPS(params), StandardCharsets.UTF_8.name());
    }

    /**
     * 发送 HTTP 请求
     *
     * @param request
     * @return
     * @throws Exception
     */
    private static String doHttp(HttpRequestBase request) throws Exception {
    	Builder builder = RequestConfig.custom();
    	RequestConfig config = builder.setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).build();
        // 通过连接池获取连接对象
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config).setConnectionManager(cm).build();

        return doRequest(httpClient, request);
    }

    /**
     * 处理Http/Https请求，并返回请求结果
     * <p>注：默认请求编码方式 UTF-8</p>
     *
     * @param httpClient
     * @param request
     * @return 请求结果
     * @throws Exception
     */
    private static String doRequest(CloseableHttpClient httpClient, HttpRequestBase request) throws Exception {
        String result = null;
        CloseableHttpResponse response = null;

        try {
            // 获取请求结果
            response = httpClient.execute(request);
            // 解析请求结果
            HttpEntity entity = response.getEntity();
            // 转换结果
            result = EntityUtils.toString(entity, StandardCharsets.UTF_8.name());
            // 关闭IO流
            EntityUtils.consume(entity);
        } finally {
            if (null != response)
                response.close();
        }

        return result;
    }

    /**
     * 转换请求参数
     *
     * @param params map参数
     * @return nameValuePair集合
     */
    private static List<NameValuePair> covertParams2NVPS(Map<String, Object> params) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();

        for (Entry<String, Object> param : params.entrySet())
            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));

        return pairs;
    }

    /**
     * 下载文件方法 InputStream
     * @param url 接口地址
     * @param params 请求参数
     * @param headers 请求头集合
     * @param path 文件流输出地址
     * @param FilenNme 文件名称
     * @param contenttype 文件后缀名
     * @return
     * @throws Exception
     */
    public static String httpPostJsonFile(String url, Map<String,Object> params, Map<String,Object> headers,String path,String FilenNme,String contenttype) throws Exception {
        // 转换请求参数
        List<NameValuePair> pairs = covertParams2NVPS(params);

        HttpPost httpPost = new HttpPost(url);
        // 设置请求参数
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8.name()));
        // 设置请求头
        for (Entry<String, Object> param : headers.entrySet())
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        //创建链接
        Builder builder = RequestConfig.custom();
    	RequestConfig config = builder.setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).build();
        // 通过连接池获取连接对象
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config).setConnectionManager(cm).build();
        InputStream out = null;
        CloseableHttpResponse response = null;
        FileOutputStream fos = null;
        try {
            // 获取请求结果
            response = httpClient.execute(httpPost);
            // 解析请求结果
            HttpEntity entity = response.getEntity();
            // 获取文件流
            out=entity.getContent();
            File file = new File(path, FilenNme+contenttype);// 创建文件夹
    		fos = new FileOutputStream(file);
    		byte[] b = new byte[1024];
    		int nRead = 0;
    		while ((nRead = out.read(b)) != -1) {
    		    fos.write(b, 0, nRead);
    		}
    		fos.flush();
            // 关闭IO流
            EntityUtils.consume(entity);
        } finally {
            if(fos != null){
                fos.close();
            }
            if (response != null){
                response.close();
            }
            if(out != null){
                out.close();
            }

        }
        return path+FilenNme+contenttype;
    }

    /**
     * 下载文件方法 InputStream
     */
    public static Map<String,Object> httpPostJsonFile(String url,Map<String,Object> map,Map<String,Object> params,String fileName,String filePath) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        // 设置请求参数
        for (Entry<String, Object> param : params.entrySet()) {
            httpPost.setEntity(new StringEntity(String.valueOf(param.getValue()), "UTF-8"));
        }
        // 设置请求头
        httpPost.addHeader("Content-Type", "application/json; charset=UTF-8");
        for (Entry<String, Object> param : map.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        Builder builder = RequestConfig.custom();
        RequestConfig config = builder.setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).build();
        // 通过连接池获取连接对象
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config).setConnectionManager(cm).build();
        InputStream out = null;
        CloseableHttpResponse response = null;
        FileOutputStream fos = null;
        String result = "failure";
        ByteArrayOutputStream boa=new ByteArrayOutputStream();
        Map<String,Object> mapReult = new HashMap<>();
        String strRes= "";
        byte[] read = new byte[0];
        try {
            // 获取请求结果
            response = httpClient.execute(httpPost);
            // 解析请求结果
            HttpEntity entity = response.getEntity();
            Header contentType = entity.getContentType();

            System.out.println(contentType);
            out=entity.getContent();

//            byte[] dataByte = new byte[1024];
//            int nRead = 0;
//            while ((nRead = out.read(dataByte)) != -1) {
////                fos.write(b, 0, nRead);
//            }
////			is.read(data);
//            strRes = new BASE64Encoder().encode(dataByte);
//            strRes = strRes.replace("\n", "").replace("\t", "").replace("\r", "");
//            strRes = URLEncoder.encode(strRes, "GBK");

            strRes = StringUtil.getBase64FromInputStream(out);
//            strRes = strRes.replace("\n", "").replace("\t", "").replace("\r", "");
//            strRes = URLEncoder.encode(strRes, "UTF-8");

//             获取文件流
            /*File file = new File(filePath, fileName);
            fos = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int nRead = 0;
            while ((nRead = is.read(b)) != -1) {
                fos.write(b, 0, nRead);
            }
            fos.flush();*/

            // 关闭IO流
            out.close();
            boa.close();
            EntityUtils.consume(entity);
            result= "success";

        }catch (Exception e){
            System.out.println(e);
            result= "failure";
        }finally {
            mapReult.put("result",result);
            mapReult.put("is",strRes);

        }
        return mapReult;
    }



    /**
     *  用于提交 application/json 和handers作为参数的 post请求
     * @param url
     * @param headers
     * @param params
     * @return
     * @throws Exception
     */
    public static String httpPostJson(String url,Map<String,Object> headers,Map<String,Object> params) throws Exception {
        // 转换请求参数
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json; charset=UTF-8");
        String signature = new ObjectMapper().writeValueAsString(params);
        // 设置请求参数
        httpPost.setEntity(new StringEntity(signature, StandardCharsets.UTF_8.name()));
        // 设置请求头
        for (Entry<String, Object> param : headers.entrySet())
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        return doHttp(httpPost);
    }


    /**
     * @Author xf
     * @Description: 有请求头、文件流接口
     * @Date 19:13 2021/4/21
     * @Param [url, headers, params, filePath]
     * @return java.lang.String
     **/
    public static String httpPostFile(String url,Map<String,Object> headers,
                                      Map<String,String> params,String filePath) throws Exception {
        String requestJson = "";

        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try{

            for (Entry<String, Object> param : headers.entrySet()) {
                httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
            }

            //添加文件参数
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            //设置请求的编码格式
            builder.setCharset(Consts.UTF_8);
            File f = new File(filePath);
            //添加文件
            builder.addBinaryBody("file", f);
            // 设置请求参数
            if(params!=null){
                for (Entry<String, String> entry : params.entrySet()) {
                    builder.addTextBody(entry.getKey(), entry.getValue());
                }
            }
            HttpEntity reqEntity = builder.build();
            httpPost.setEntity(reqEntity);
            httpResponse = httpClient.execute(httpPost);
            HttpEntity responseEntity = httpResponse.getEntity();
            if (responseEntity != null) {
                // 将响应内容转换为字符串
                requestJson = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
            }
        }catch (Exception e){
            throw new Exception();
        }finally {
            if(httpResponse != null){
                httpResponse.close();
            }
            if(httpClient != null){
                httpClient.close();
            }
        }
        return requestJson;
    }
    public static byte[] read(InputStream inputStream) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[10240];
            int num = inputStream.read(buffer);
            while (num != -1) {
                baos.write(buffer, 0, num);
                num = inputStream.read(buffer);
            }
            baos.flush();
            return baos.toByteArray();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
