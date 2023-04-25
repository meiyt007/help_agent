package com.zfsoft.ocr.util.textIn;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.ocr.data.pojo.textInOcr.TextInOcrItemResponse;
import com.zfsoft.ocr.data.pojo.textInOcr.TextInOcrResponse;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: liangss
 * @date: 2021/10/29 16:47
 * @description:
 */
public class TextInOcr {

    public static String appId = "";


    public static String secretCode = "";

    /***
     * @Description: 根据图片地址和访问地址获取卡证的ocr识别结果
     * @Author:liangss
     * @Date:2021/11/2
     * @Param: [imgUrl：图片地址, url：ocr卡证识别接口地址]
     */
    public static String textInOcrCertificate(byte[] imgData, String url) throws Exception {
        /* String appIdww=appId;*/
        // 请登录后前往 “工作台-账号设置-开发者信息” 查找 x-ti-app-id，示例代码中 x-ti-app-id 非真实数据
        //String appId = CommonUtil.appId;
        // 请登录后前往 “工作台-账号设置-开发者信息” 查找 x-ti-secret-code，示例代码中 x-ti-secret-code 非真实数据
        //String secretCode = CommonUtil.secretCode;
        BufferedReader in = null;
        DataOutputStream out = null;
        String result = "";
        /*  String csUrl="C:\\Users\\Administrator\\Desktop\\tp\\pc-1.jpg";*/
        try {
            // byte[] imgData = readfile(imgUrl); // image
            // byte[] imgData= FileManageUtil.getImgFromUrl(imgUrl);
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("x-ti-app-id", appId);
            conn.setRequestProperty("x-ti-secret-code", secretCode);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST"); // 设置请求方式
            out = new DataOutputStream(conn.getOutputStream());
            out.write(imgData);
            out.flush();
            out.close();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("结果：" + result);
        return result;

    }


    /***
     * @Description: ocr取值转换
     * @Author:liangss
     * @Date:2021/11/1
     * @Param: [result]
     */
    public static TextInOcrResponse transformOcr(String result) {
        TextInOcrResponse textInOcrResponse = new TextInOcrResponse();
        List<TextInOcrItemResponse> textInOcrItemResponseList = new ArrayList<>();
        JSONObject jsonObject = JSONObject.parseObject(result);
        Integer code = jsonObject.getInteger("code");
        String message = jsonObject.getString("message");
        textInOcrResponse.setCode(code);
        textInOcrResponse.setMessage(message);
        if (code == 200) {
            JSONObject resultObject = jsonObject.getJSONObject("result");
            if (null != resultObject) {
                Integer rotated_image_width = resultObject.getInteger("rotated_image_width");
                Integer rotated_image_height = resultObject.getInteger("rotated_image_height");
                String image_angle = resultObject.getString("image_angle");
                String type = resultObject.getString("type");
                textInOcrResponse.setRotated_image_height(rotated_image_height);
                textInOcrResponse.setRotated_image_width(rotated_image_width);
                textInOcrResponse.setImage_angle(image_angle);
                textInOcrResponse.setType(type);
                JSONArray itemArray = resultObject.getJSONArray("item_list");
                for (int i = 0; i < itemArray.size(); i++) {
                    TextInOcrItemResponse textInOcrItemResponse = new TextInOcrItemResponse();
                    JSONObject itemObject = itemArray.getJSONObject(i);
                    if (null != itemObject) {
                        String description = itemObject.getString("description");
                        String value = itemObject.getString("value");
                        String key = itemObject.getString("key");
                        String position = itemObject.getString("position");
                        textInOcrItemResponse.setName(description);
                        textInOcrItemResponse.setWord(value);
                        textInOcrItemResponse.setKey(key);
                        textInOcrItemResponse.setPosition(position);

                        JSONArray positionArray = itemObject.getJSONArray("position");
                        if (null != positionArray && positionArray.size() > 0) {
                            Integer zero = positionArray.getInteger(0);
                            Integer one = positionArray.getInteger(1);
                            Integer two = positionArray.getInteger(2);
                            Integer three = positionArray.getInteger(3);
                            Integer four = positionArray.getInteger(4);
                            Integer five = positionArray.getInteger(5);
                            Integer six = positionArray.getInteger(6);
                            Integer seven = positionArray.getInteger(7);


                            Integer left = zero;
                            Integer top = one;
                            Integer width = two - zero;
                            Integer height = five - one;
                            textInOcrItemResponse.setZero(zero);
                            textInOcrItemResponse.setOne(one);
                            textInOcrItemResponse.setTwo(two);
                            textInOcrItemResponse.setThree(three);
                            textInOcrItemResponse.setFour(four);
                            textInOcrItemResponse.setFive(five);
                            textInOcrItemResponse.setSix(six);
                            textInOcrItemResponse.setSeven(seven);

                            textInOcrItemResponse.setTop(top);
                            textInOcrItemResponse.setLeft(left);
                            textInOcrItemResponse.setWidth(width);
                            textInOcrItemResponse.setHeight(height);
                        }
                    }
                    textInOcrItemResponseList.add(textInOcrItemResponse);
                }
            }

        }
        textInOcrResponse.setTextInOcrItemResponseList(textInOcrItemResponseList);
        return textInOcrResponse;

    }


    /***
     * @Description: 根据附件地址获取byte
     * @Author:liangss
     * @Date:2021/11/1
     * @Param: [path]
     */
    public static byte[] readfile(String path) {
        String imgFile = path;
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


    /***
     * @Description: 根据byte获取每个图片的md5值
     * @Author:liangss
     * @Date:2021/12/31
     * @Param: [bytes]
     */
    public static String getMd5(byte[] bytes) {

        String md5 = "";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] b = md.digest(bytes);
            BigInteger bi = null;
            bi = new BigInteger(1, b);
            md5 = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }


}
