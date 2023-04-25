package com.zfsoft.service.util;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;

public class MapUtil {
    public static String key = "40022eb66e75fb0a5c075a0411d5553f";//个人申请
    public static void main(String[] args) {
        //输入地址查询坐标 - 地理编码
        JSONObject addParam = new JSONObject();
        addParam.put("address","南京市建邺区江东中路265号");
        JSONObject addData = getLocation(addParam);
        String location = addData.getJSONArray("geocodes").getJSONObject(0).getString("location");
        System.out.println(location);

        //输入坐标查询地址 - 逆地理编码
        JSONObject locParam = new JSONObject();
        locParam.put("location","118.731052,32.00490");
        locParam.put("radius","1000");
        locParam.put("extensions","base");
        JSONObject locData = getAddress(locParam);
        System.out.println(locData);

        //距离测试参数
        JSONObject disParam = new JSONObject();
        disParam.put("origins","116.481028,39.989643");
        disParam.put("destination","114.465302,40.004717");
        disParam.put("type","1");
        JSONObject disData = getDistance(disParam);
        String distance = disData.getJSONArray("results").getJSONObject(0).getString("distance");
        double result = Integer.valueOf(distance)*1.0/1000;
        System.out.println(distance);
    }

    /**
     * 距离测量
     * @param param
     * @return
     */
    public static JSONObject getDistance(JSONObject param){
        //距离测量
        String disURL= "https://restapi.amap.com/v3/distance";
        //解析参数
        String origins = param.getString("origins");//出发点坐标：支持100个坐标对，坐标对见用“| ”分隔；经度和纬度用","分隔
        String destination = param.getString("destination");//规则： lon，lat（经度，纬度）， “,”分割如117.500244, 40.417801     经纬度小数点不超过6位
        String type = param.getString("type");//0：直线距离 1：驾车导航距离（仅支持国内坐标,当为1时会考虑路况，故在不同时间请求返回结果可能不同）3：步行规划距离（仅支持5km之间的距离）
        //拼接url
        String url = disURL+"?origins="+origins+"&destination="+destination+"&type="+type+"&key="+key;
        //请求接口
        //String dataStr = HttpReqUtil.sendGet(url, "UTF-8");
        String dataStr = HttpUtil.get(url);
        //结果转化
        return JSONObject.parseObject(dataStr);
    }

    /**
     * 输入坐标查询地址 - 逆地理编码
     * @param param
     * @return
     */
    public static JSONObject getLocation(JSONObject param){
        //距离测量
        String disURL= "https://restapi.amap.com/v3/geocode/geo";
        //解析参数
        String address = param.getString("address");//填写结构化地址信息:省份＋城市＋区县＋城镇＋乡村＋街道＋门牌号码
        //拼接url
        String url = disURL+"?address="+address+"&key="+key;
        //请求接口
        //String dataStr = HttpReqUtil.sendGet(url, "UTF-8");
        String dataStr = HttpUtil.get(url);
        //结果转化
        return JSONObject.parseObject(dataStr);
    }

    /**
     * 输入坐标查询地址 - 逆地理编码
     * @param param
     * @return
     */
    public static JSONObject getAddress(JSONObject param){
        //距离测量
        String disURL= "https://restapi.amap.com/v3/geocode/regeo";
        //解析参数
        String location = param.getString("location");//传入内容规则：经度在前，纬度在后，经纬度间以“,”分割，经纬度小数点后不要超过 6 位。
        String radius = param.getString("radius");//搜索半径：radius取值范围在0~3000，默认是1000。单位：米
        String extensions = param.getString("extensions");//extensions 参数默认取值是 base，也就是返回基本地址信息；
                                                              //extensions 参数取值为 all 时会返回基本地址信息、附近 POI 内容、道路信息以及道路交叉口信息。
        //拼接url
        String url = disURL+"?location="+location+"&radius="+radius+"&extensions="+extensions+"&key="+key;
        //请求接口
        //String dataStr = HttpReqUtil.sendGet(url, "UTF-8");
        String dataStr = HttpUtil.get(url);
        //结果转化
        return JSONObject.parseObject(dataStr);
    }
}
