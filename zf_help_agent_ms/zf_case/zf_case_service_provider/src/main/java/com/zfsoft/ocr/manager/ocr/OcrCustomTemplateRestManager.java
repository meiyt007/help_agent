package com.zfsoft.ocr.manager.ocr;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.ocr.data.pojo.baidu.BaiduTokenResponse;
import com.zfsoft.ocr.data.pojo.common.OcrCallRecord;
import com.zfsoft.ocr.data.pojo.exception.ServiceException;
import com.zfsoft.ocr.data.pojo.ocr.OcrCustomTemplateItemResponse;
import com.zfsoft.ocr.data.pojo.ocr.OcrCustomTemplateRequest;
import com.zfsoft.ocr.data.pojo.ocr.OcrCustomTemplateResponse;
import com.zfsoft.ocr.manager.common.OcrCallRecordManager;
import com.zfsoft.ocr.util.*;
import com.zfsoft.ocr.util.textIn.TextInOcr;
import com.zfsoft.single.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.xml.bind.DatatypeConverter;
import java.util.*;


/***
 * @Description: ocr自定义模板识别
 * @Author:liangss
 * @Date:2021/10/27
 * @Param:
 */
@Service
@Slf4j
public class OcrCustomTemplateRestManager {

    @Value("${zfsoft.baiduOcr.clientId}")
    public String BAI_DU_URL_CLIENT_ID;

    @Value("${zfsoft.baiduOcr.clientSecret}")
    public String BAI_DU_URL_CLIENT_SECRET;

    @Resource
    private OcrCallRecordManager ocrCallRecordManager;

    public String cs() throws Exception {
        String csUrl2 = "https://hf.zhuofansoft.com:12102/group1/M00/1A/E0/rKj6BmGOFNeAJm3oAAM4kfpcz0Q863.jpg";
        byte[] ts = FileManageUtil.getImgFromUrl(csUrl2);
        System.out.println("1111");
        return null;
    }


    /***
     * @Description: 自定义模板识别方法
     * @Author:liangss
     * @Date:2021/10/28
     * @Param: [ocrCustomTemplateRequest]
     */
    public OcrCustomTemplateResponse customTemplate(@RequestBody OcrCustomTemplateRequest ocrCustomTemplateRequest) {
        log.info("进入ocr自定义模板识别方法");
        OcrCustomTemplateResponse ocrCustomTemplateResponse = new OcrCustomTemplateResponse();
        try {
            // 获取百度token
            if (StrUtil.isNotBlank(ocrCustomTemplateRequest.getTemplateIdAli())) {
                ocrCustomTemplateResponse = aliOcr(ocrCustomTemplateRequest, ocrCustomTemplateResponse);
            } else if (StrUtil.isNotBlank(ocrCustomTemplateRequest.getTemplateId())) {
                ocrCustomTemplateResponse = baiduOcr(ocrCustomTemplateRequest, ocrCustomTemplateResponse);
            } else {
                log.error("ocr自定义模板识别,模板id不能为空。");
                throw new ServiceException("ocr自定义模板识别,模板id不能为空。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("ocr自定义模板识别,调用百度接口异常。", e);
            throw new ServiceException("" + e);
        }
        return ocrCustomTemplateResponse;
    }

    /**
     * 百度ocr自定义模板识别 service实现
     *
     * @author cbc
     * @date 2020年3月4日
     * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
     */
    public OcrCustomTemplateResponse aliOcr(OcrCustomTemplateRequest ocrCustomTemplateRequest, OcrCustomTemplateResponse ocrCustomTemplateResponse) throws Exception {
        String host = AliyunOcrUtil.AILIYUN_TEMPLATE_URL;
        String path = AliyunOcrUtil.AILIYUN_TEMPLATE_URL_OCR;
        String method = AliyunOcrUtil.AILIYUN_TEMPLATE_URL_METHOD;
        String appcode = AliyunOcrUtil.AILIYUN_TEMPLATE_APP_CODE;

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/json; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        String bodys = "";
        String templateOid = ocrCustomTemplateRequest.getTemplateIdAli();
        String[] template_list = {templateOid};
        JSONObject relJo = new JSONObject();
        JSONObject jo = new JSONObject();
        jo.put("template_list", template_list);
        relJo.put("image", ocrCustomTemplateRequest.getImgBase64());
        relJo.put("configure", jo);
        bodys = relJo.toString();

        HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
        String resultJson = EntityUtils.toString(response.getEntity());
        if (StrUtil.isBlank(resultJson)) {
            log.error("ocr自定义模板识别,异常。");
            throw new ServiceException("ocr自定义模板识别,异常。" + response.toString());
        }
        if (resultJson.indexOf(AliyunOcrUtil.SUCCESS) == -1) {
            log.error("ocr自定义模板识别,异常。");
            throw new ServiceException("ocr自定义模板识别,异常。" + response.toString());
        }
        JSONObject resultJo = JSONObject.parseObject(resultJson);
        Object relObj = resultJo.get(AliyunOcrUtil.SUCCESS);
        if (relObj == null) {
            log.error("ocr自定义模板识别,异常。");
            throw new ServiceException("ocr自定义模板识别,异常。" + resultJson);
        }
        if (!"true".equals(relObj.toString())) {
            log.error("ocr自定义模板识别,异常。");
            throw new ServiceException("ocr自定义模板识别,异常。");
        }

        // 获取识别内容
        JSONArray array = JsonUtil.objToJSONArray(resultJo.get(AliyunOcrUtil.ITEMS));

        List<OcrCustomTemplateItemResponse> list = new ArrayList<OcrCustomTemplateItemResponse>();
        for (Object object : array) {
            if (null == object) {
                continue;
            }
            JSONObject jsonObject = JsonUtil.objToJSONObject(object);
            Set<Map.Entry<String, Object>> it = jsonObject.entrySet();//jsonObject.keys();
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                OcrCustomTemplateItemResponse ocrCustomTemplateItemResponse = new OcrCustomTemplateItemResponse();
                ocrCustomTemplateItemResponse.setName(entry.getKey());
                if (entry.getValue() != null) {
                    ocrCustomTemplateItemResponse.setWord(String.valueOf(entry.getValue()));
                }
                list.add(ocrCustomTemplateItemResponse);
            }
        }
        ocrCustomTemplateResponse.setCode(200);
        ocrCustomTemplateResponse.setOcrCustomTemplateItemResponseList(list);
        return ocrCustomTemplateResponse;
    }


    /**
     * 百度ocr自定义模板识别 service实现
     *
     * @author cbc
     * @date 2020年3月4日
     * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
     */
    public OcrCustomTemplateResponse baiduOcr(OcrCustomTemplateRequest ocrCustomTemplateRequest, OcrCustomTemplateResponse ocrCustomTemplateResponse) throws Exception {
        // 获取百度token

        BaiduOcrUtil.BAI_DU_URL_CLIENT_ID = BAI_DU_URL_CLIENT_ID;
        BaiduOcrUtil.BAI_DU_URL_CLIENT_SECRET = BAI_DU_URL_CLIENT_SECRET;
        BaiduTokenResponse baiduTokenResponse = BaiduOcrUtil.getCommonAuth();
        Integer code = baiduTokenResponse.getCode();
        //接口地址
        String interUrl = "ocrCertificateRest/ocrCertificateRestService/baiduOcr";
        //接口code
        String interCode = "baiduOcr";
        //接口名称
        String interName = "百度ocr自定义模板识别";
        String imgBase64 = ocrCustomTemplateRequest.getImgBase64();

        String result = "";
        byte[] imgData = new byte[0];
        //byte[] imgData = TextInOcrUtil.readfile(imgUrl);
        String md5 = "";
        //base64转byte[]
        if (StringUtils.isNotEmpty(imgBase64)) {
            imgData = DatatypeConverter.parseBase64Binary(imgBase64);
        }
        //获取md5的取值
        md5 = TextInOcr.getMd5(imgData);
        OcrCallRecord ocrCallRecordNew = ocrCallRecordManager.queryDbOcrCallRecordByMd5AndInterUrl(md5, interUrl);
        //判断之前是否存储过ocr
        if (null != ocrCallRecordNew && StringUtils.isNotEmpty(ocrCallRecordNew.getInterResult())) {
            result = ocrCallRecordNew.getInterResult();
            if (StrUtil.isNotEmpty(result)) {
                ocrCustomTemplateResponse = JSONObject.parseObject(result, OcrCustomTemplateResponse.class);
            }
        } else {


            if (200 != code) {
                log.error("ocr自定义模板识别,百度授权异常。");
                throw new ServiceException("ocr自定义模板识别,百度授权异常。");
            }
            String accessToken = baiduTokenResponse.getAuthToken();
            // 获取请求的拼接参数字符串
            Map<String, Object> params = new HashMap<String, Object>(8);
            Map<String, Object> map = new HashMap<String, Object>(8);
            // 可选参数
            params.put("access_token", accessToken);
            params.put("image", ocrCustomTemplateRequest.getImgBase64().replace(" ", "+"));
            params.put("templateSign", ocrCustomTemplateRequest.getTemplateId());
            map = CommonRestUtil.sendPost(BaiduOcrUtil.HOST_URL_OCR_HOST_CUSTOM_TEMPLATE, params);
            String errorCode = map.get(BaiduOcrUtil.BAI_DU_URL_ERROR_CODE).toString();
            String error_msg = map.get("error_msg").toString();

            // 调用成功

            if (!BaiduOcrUtil.BAIDU_CUSTOM_TEMPLATE_CODE_SUCCESS.equals(errorCode)) {
                log.error("ocr自定义模板识别,调用百度接口异常。errorCode ：" + errorCode);
                throw new ServiceException("ocr自定义模板识别,调用百度接口异常。errorCode ：" + errorCode + ";error_msg:" + error_msg);
            }
            JSONObject resultJsonObj = JsonUtil.objToJSONObject(map.get("data"));
            JSONArray array = JsonUtil.objToJSONArray(resultJsonObj.get("ret"));
            List<OcrCustomTemplateItemResponse> list = new ArrayList<OcrCustomTemplateItemResponse>();
            for (Object object : array) {
                if (null == object) {
                    continue;
                }
                OcrCustomTemplateItemResponse ocrCustomTemplateItemResponse = new OcrCustomTemplateItemResponse();
                JSONObject obj = JsonUtil.objToJSONObject(object);
                ocrCustomTemplateItemResponse.setName(obj.getString("word_name"));
                ocrCustomTemplateItemResponse.setWord(obj.getString("word"));
                // 获取识别区域位置信息
                Object locationObj = obj.get("location");
                if (locationObj != null) {
                    JSONObject location = JsonUtil.objToJSONObject(locationObj);
                    ocrCustomTemplateItemResponse.setHeight(location.getString("height"));
                    ocrCustomTemplateItemResponse.setWidth(location.getString("width"));
                    ocrCustomTemplateItemResponse.setTop(location.getString("top"));
                    ocrCustomTemplateItemResponse.setLeft(location.getString("left"));
                }
                list.add(ocrCustomTemplateItemResponse);
            }
            ocrCustomTemplateResponse.setCode(200);
            ocrCustomTemplateResponse.setOcrCustomTemplateItemResponseList(list);
            log.info("百度自定义模板获取ocr结果：" + ocrCustomTemplateResponse);

            result = JSONObject.toJSONString(ocrCustomTemplateResponse);
            if (StringUtils.isNotEmpty(interUrl) && StringUtils.isNotEmpty(result)) {
                OcrCallRecord ocrCallRecord = new OcrCallRecord();
                ocrCallRecord.setInterUrl(interUrl);
                ocrCallRecord.setInterName(interName);
                ocrCallRecord.setInterResult(result);
                ocrCallRecord.setInterCode(interCode);
                ocrCallRecord.setMd5(md5);
                ocrCallRecordManager.saveOrUpdateOcrCallRecord(ocrCallRecord);
            }
        }
        return ocrCustomTemplateResponse;
    }

}
