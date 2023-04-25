package com.zfsoft.ocr.util;


import cn.hutool.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;

/**
 * 文件调用类
 *
 * @author cbc
 * @date 2019年2月21日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
public class FileManageUtil {

    /**
     * 将日志记录到文件中，日志文件路径在log4j2.xml中配置
     */
    public static Logger fileLog = LoggerFactory.getLogger("FILE_LOGGER");

    /**
     * 默认字符集，UTF-8
     */
    public final static String DEFAULT_CHARACTER_SET = "UTF-8";

    /**
     * 获取系统参数接口
     */
/*    private static SysConfigParamProxy proxy;*/
    /**
     * 根据附件编号获取附件信息地址
     */
    public static String GET_ATTA_JSON_BY_OID = "atta/getAttaJsonByOid.do";
    /**
     * 通过附件编号集合查询附件对象集合地址
     */
    public static String QUERY_ATTA_LIST_BY_OIDS = "atta/queryAttaListByOids.do";
    /**
     * 单文件上传地址
     */
    public static String UPLOAD_SINGLE_FILE = "atta/uploadSingleFile.do";
    /**
     * base64格式附件上传地址
     */
    public static String UPLOAD_BASE64_FILE = "atta/uploadBase64File.do";
    /**
     * 获取附件base64地址
     */
    public static String GET_FILE_BASE64 = "atta/getFileBase64.do";


    /**
     * 发起请求
     *
     * @param url   请求地址
     * @param param 请求参数
     * @return 返回请求结果
     * @author cbc
     * @date 2019年2月21日
     */
    public static String sendPost(String url, Map<String, Object> param){
        try {
            /*  url = getInnerFilePlatAddr() + url;*/
            String result = HttpUtil.post(url, param);
            result = result.replace("\n", "").replace("\t", "").replace("\r", "");
            result = URLDecoder.decode(result, DEFAULT_CHARACTER_SET);
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 获取文件平台内网地址
     *
     * @return 文件平台内网地址
     * @author chenbw
     * @date 2019-09-27
     */
   /* public static String getInnerFilePlatAddr() {
        return proxy.getIntranetFilePlatAddr();
    }*/

    /**
     * 获取转换文件平台地址
     *
     * @author gaolh
     * @date 2019-11-11 10:45:38
     */
   /* public static String getConvertFilePlatAddr() {
        return proxy.getConvertFilePlatAddr();
    }*/

    public static  byte[]  getImgFromUrl(String imgUrl)
            throws Exception
    {
        URL url = null;
        InputStream is = null;
        ByteArrayOutputStream outStream = null;
        HttpURLConnection httpUrl = null;

        try
        {
            url = new URL(imgUrl);
            httpUrl = (HttpURLConnection)url.openConnection();
            httpUrl.connect();
            httpUrl.getInputStream();
            is = httpUrl.getInputStream();
            outStream = new ByteArrayOutputStream();
            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while ((len = is.read(buffer)) != -1)
            {
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);

            }
            System.out.println("outStream长度=***"+outStream.size());
            // 对字节数组Base64编码

            return  outStream.toByteArray();
           /* return Base64Util.encode(outStream.toByteArray());*/

        }
        catch (Exception e)
        {
            throw new Exception(e);
        }
        finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                }
            }

            if (outStream != null)
            {
                try
                {
                    outStream.close();
                }
                catch (IOException e)
                {
                }
            }
            if (httpUrl != null)
            {
                httpUrl.disconnect();
            }
        }

    }



}
