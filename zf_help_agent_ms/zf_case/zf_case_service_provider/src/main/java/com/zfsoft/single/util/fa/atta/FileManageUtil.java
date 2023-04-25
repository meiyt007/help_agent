package com.zfsoft.single.util.fa.atta;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.zfsoft.single.util.Base64Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
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
     * 初始化动态参数
     *
     * @author zhujiajian
     * @date 2019-04-01 13:29:44
     */
/*    public static void init(SysConfigParamProxy proxy) {
        FileManageUtil.proxy = proxy;
    }*/

    /**
     * 上传文件，文件流
     *
     * @param originName 原始文件名称
     * @param userId     上传用户
     * @param is         文件流
     * @return 返回上传的附件对象
     * @author cbc
     * @date 2019年2月24日
     */
    /*public static AttaInfoResult uploadFileStream(String originName, String userId, InputStream is) throws Exception {
        originName = URLEncoder.encode(originName, DEFAULT_CHARACTER_SET);
        String jsonResult = HttpConnectionUtil.uploadFile(getInnerFilePlatAddr() + UPLOAD_SINGLE_FILE, originName, userId, is);
        //jsonResult = URLDecoder.decode(jsonResult, DEFAULT_CHARACTER_SET);
        return JSON.parseObject(jsonResult, AttaInfoResult.class);
    }*/

    /**
     * 上传文件，Base64字符串
     *
     * @param originName 原始文件名称
     * @param base64     文件Base64字符串
     * @return 返回上传的附件对象
     * @throws Exception
     * @author cbc
     * @date 2019年2月24日
     */
    public static AttaInfoResult uploadFileBase64(String originName, String base64) throws Exception {
        return uploadFileBase64(originName, null, base64);
    }

    /**
     * 上传文件，Base64字符串
     *
     * @param originName 原始文件名称
     * @param userId     上传用户编号
     * @param base64     文件Base64字符串
     * @return 返回上传的附件对象
     * @author cbc
     * @date 2019年2月24日
     */
    public static AttaInfoResult uploadFileBase64(String originName, String userId, String base64) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("fileBase64", base64);
            param.put("userId", userId);
            param.put("originName", originName);

            String jsonResult = sendPost(UPLOAD_BASE64_FILE, param);
            return JSON.parseObject(jsonResult, AttaInfoResult.class);
        }catch (Exception e){
            e.printStackTrace();
        }
       return null;
    }

    /**
     * 根据附件编号获取附件信息
     *
     * @param oid 附件主键
     * @return 返回附件对象
     */
    public static AttaInfoResult getAttaInfo(String oid) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("oid", oid);
            String jsonResult = sendPost(GET_ATTA_JSON_BY_OID, param);
            return JSON.parseObject(jsonResult, AttaInfoResult.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过附件编号listoids查询附件对象list
     *
     * @param oids 附件编号集合
     * @return 附件对象集合
     * @throws Exception
     */
    public static AttaListResult getAttaList(String oids) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("oids", oids);
            String jsonResult = sendPost(QUERY_ATTA_LIST_BY_OIDS, param);
            return JSON.parseObject(jsonResult, AttaListResult.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public static AttaListResult getAttaList(List <String> oidList){
        try {
            StringBuffer oids = new StringBuffer();
            if(CollUtil.isEmpty(oidList)){
                return null;
            }
            for (int i = 0; i < oidList.size(); i++) {
                if (i == 0) {
                    oids.append(oidList.get(i));
                } else {
                    oids.append(";");
                    oids.append(oidList.get(i));
                }
            }
            Map<String, Object> param = new HashMap<>();
            param.put("oids", oids.toString());
            String jsonResult = sendPost(QUERY_ATTA_LIST_BY_OIDS, param);
            return JSON.parseObject(jsonResult, AttaListResult.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }


    /**
     * 获取文件的Base64字符串
     *
     * @param attaOid 附件文件主键
     * @return 返回附件Base64字符串
     * @author cbc
     * @date 2019年2月24日
     */
    public static AttaBase64Result getAttaBase64(String attaOid) {
        try {
            Map<String, Object> param = new HashMap<>(6);
            param.put("attaOid", attaOid);
            String jsonResult = sendPost(GET_FILE_BASE64, param);
            AttaBase64Result base64Result = JSON.parseObject(jsonResult, AttaBase64Result.class);
            if (base64Result.isSuccess()) {
                String result = base64Result.getResult();
                String strOne = " ";
                String strTwo = "+";
                result = result.replaceAll(strOne, strTwo);
                base64Result.setResult(result);
            }
            return base64Result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

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

    public static String getImgFromUrl(String imgUrl)
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
            return Base64Util.encode(outStream.toByteArray());

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


    public static  byte[]  getByteFromUrl(String imgUrl)
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
