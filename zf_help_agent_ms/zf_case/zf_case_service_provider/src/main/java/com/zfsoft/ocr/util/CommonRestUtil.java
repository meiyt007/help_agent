package com.zfsoft.ocr.util;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 接口通用类
 *
 * @author cbc
 * @date 2019年3月1日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
public class CommonRestUtil {

    /**
     * 发起GET请求
     *
     * @author cbc
     * @date 2019年2月21日
     * @param url
     *            请求地址
     * @param param
     *            请求参数
     * @throws IOException
     * @return 返回请求结果
     */
    public static Map<String, Object> sendGet(String url, Map<String, Object> params) throws IOException {
        String result = HttpUtil.get(url, params);
        Map<String, Object> map = JSON.parseObject(result);
        return map;
    }

    /**
     * 发起get请求
     *
     * @author cbc
     * @date 2019年3月1日
     * @param getUrl
     *            请求地址
     * @return 返回结果描述
     */
    public static String sendGet(String getUrl) {
        return HttpUtil.get(getUrl);
    }

    /**
     * 发起POST请求
     *
     * @author cbc
     * @date 2019年2月21日
     * @param url
     *            请求地址
     * @param param
     *            请求参数
     * @throws IOException
     * @return 返回请求结果
     */
    public static Map<String, Object> sendPost(String url, Map<String, Object> params) throws IOException {
        String result = HttpUtil.post(url, params);
        Map<String, Object> map = JSONObject.parseObject(result);
        return map;
    }

    /**
     * 发起post请求
     *
     * @author cbc
     * @date 2019年3月1日
     * @param postUrl
     *            请求地址
     * @return 返回结果描述
     */
    public static String sendPost(String postUrl) {
        Map<String, Object> params = new HashMap<String, Object>();
        return HttpUtil.post(postUrl, params);
    }

    /**
     * 发起POST请求
     *
     * @author cbc
     * @date 2019年2月21日
     * @param url
     *            请求地址
     * @param param
     *            请求参数
     * @throws IOException
     * @return 返回请求结果
     */
    public static String sendPostString(String url, Map<String, Object> params) throws IOException {
        String result = HttpUtil.post(url, params);
        return result;
    }

}
