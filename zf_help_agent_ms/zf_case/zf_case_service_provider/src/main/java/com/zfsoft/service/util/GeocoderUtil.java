package com.zfsoft.service.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import tk.mybatis.mapper.util.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName GeocoderUtil
 * @Description 用于计算经纬度
 * @Author xiayj
 * @Date 2021/5/12 13:30
 **/
public class GeocoderUtil {

    private final static String OK = "1";

    /***
     * @param addr
     * @description: 通过地址获取经纬度
     * @return: java.util.Map<java.lang.String, java.lang.String>
     * @author: xiayj
     * @date: 2021/5/12
     */
    public static Map<String,String> getCoordinate(String addr){
        String key = "27b1f84101e6199a91542d5c0576948d";
        Map<String,String> map = new HashMap<>(2);
        if (StringUtil.isEmpty(addr)) {
            return map;
        }
        // 不使用代理
        try {
            String address =  java.net.URLEncoder.encode(addr, "UTF-8");
            String url = String .format("http://restapi.amap.com/v3/geocode/geo?address=%s&key=%s", address, key);
            URL myurl = new URL(url);
            URLConnection httpsConn = myurl.openConnection();
            try(InputStreamReader insr = new InputStreamReader( httpsConn.getInputStream(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(insr)){
                String data;
                if((data= br.readLine())!=null){
                    JSONObject jsonObject = JSON.parseObject(data);
                    String status = jsonObject.getString("status");
                    if(OK.equals(status)){
                        JSONArray jsonArray = jsonObject.getJSONArray("geocodes");
                        if(!jsonArray.isEmpty()){
                            JSONObject result = jsonArray.getJSONObject(0);
                            map.put("formattedAddress",result.getString("formatted_address"));
                            map.put("country",result.getString("country"));
                            map.put("province",result.getString("province"));
                            map.put("citycode",result.getString("citycode"));
                            map.put("city",result.getString("city"));
                            map.put("district",result.getString("district"));
                            map.put("location",result.getString("location"));
                            map.put("level",result.getString("level"));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }





    public static void main(String[] args) {
        Map<String, String> map = GeocoderUtil.getCoordinate("成都市天府四街2号");
        System.out.println(map.toString());
    }
}
