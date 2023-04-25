package com.zfsoft.ocr.manager.ocr;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.ocr.data.pojo.baidu.BaiduTokenResponse;
import com.zfsoft.ocr.data.pojo.common.OcrCallRecord;
import com.zfsoft.ocr.data.pojo.exception.ServiceException;
import com.zfsoft.ocr.data.pojo.ocr.*;
import com.zfsoft.ocr.manager.common.OcrCallRecordManager;
import com.zfsoft.ocr.util.*;
import com.zfsoft.ocr.util.textIn.TextInOcr;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/***
 * @Description: ocr卡证证照识别实现类
 * @Author:liangss
 * @Date:2021/10/27
 * @Param:
 */
@Service
@Slf4j
public class OcrCertificateRestManager {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Value("${zfsoft.baiduOcr.clientId}")
    public String BAI_DU_URL_CLIENT_ID;

    @Value("${zfsoft.baiduOcr.clientSecret}")
    public String BAI_DU_URL_CLIENT_SECRET;


    @Resource
    private OcrCallRecordManager ocrCallRecordManager;

    /***
     * @Description: ocr营业执照识别
     * @Author:liangss
     * @Date:2021/10/28
     * @Param: [ocrBusinessLicenseRequest]
     */
    public OcrCustomTemplateResponse businessLicense(
            @RequestBody OcrBusinessLicenseRequest ocrBusinessLicenseRequest) {
        log.info("进入ocr营业执照识别");
        BaiduOcrUtil.BAI_DU_URL_CLIENT_ID = BAI_DU_URL_CLIENT_ID;
        BaiduOcrUtil.BAI_DU_URL_CLIENT_SECRET = BAI_DU_URL_CLIENT_SECRET;
        OcrCustomTemplateResponse ocrCustomTemplateResponse = new OcrCustomTemplateResponse();
        List<OcrCustomTemplateItemResponse> ocrCustomTemplateItemResponseList = new ArrayList<>();

        //接口地址
        String interUrl = "ocrCertificateRest/ocrCertificateRestService/businessLicense";
        //接口code
        String interCode = "businessLicense";
        //接口名称
        String interName = "百度营业执照识别";
        String imgBase64 = ocrBusinessLicenseRequest.getImgBase64();

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


            try {

                BaiduTokenResponse baiduTokenResponse = BaiduOcrUtil.getCommonAuth();
                Integer code = baiduTokenResponse.getCode();
                if (200 != code) {
                    log.error("ocr营业执照识别,百度授权异常。");
                    throw new ServiceException("ocr营业执照识别,百度授权异常。");
                }
                String accessToken = baiduTokenResponse.getAuthToken();
                // 获取请求的拼接参数字符串
                Map<String, Object> params = new HashMap<String, Object>(8);
                // 可选参数
                params.put("access_token", accessToken);
                //可选值 true,false是否检测图像朝向，默认不检测，即：false。可选值包括true - 检测朝向；false - 不检测朝向。朝向是指输入图像是正常方向、逆时针旋转90/180/270度
                params.put("detect_direction", "true");
                params.put("image", ocrBusinessLicenseRequest.getImgBase64());
                Map<String, Object> map = CommonRestUtil.sendPost(BaiduOcrUtil.HOST_URL_OCR_HOST_BUSINESS_LICENSE, params);
                // 调用成功
                if (null == map.get(BaiduOcrUtil.BAI_DU_URL_ERROR_CODE)) {
                    // 解析百度识别营业执照信息
                    Object relObj = map.get(BaiduOcrUtil.BAI_DU_URL_WORDS_RESULT);
                    ocrCustomTemplateResponse.setCode(200);
                    AnalysisBaiduUtil.analysisOcrCertificateRest(relObj, ocrCustomTemplateItemResponseList);
                    ocrCustomTemplateResponse.setOcrCustomTemplateItemResponseList(ocrCustomTemplateItemResponseList);
                    /*AnalysisBaiduUtil.analysisBaiduBusinessLicense(ocrBusinessLicenseResponse, relObj);*/
                }
                // 百度营业执照识别不成功或者信息不完整，识别图片中的二维码信息
         /*   if (StrUtil.isBlank(ocrBusinessLicenseResponse.getSocialCode())) {
                ocrBusinessLicenseResponse = RecognizeQrCode.readHtmlByQRCodeBase64(ocrBusinessLicenseRequest.getImgBase64());
                if (null != ocrBusinessLicenseResponse) {
                    ocrBusinessLicenseResponse.setCode(200);
                } else {
                    // 获取错误code
                    Object o = map.get(BaiduOcrUtil.BAI_DU_URL_ERROR_CODE);
                    //百度营业执照识别没有错误信息，但又识别不到社会信用代码，上传的不是营业执照
                    if (o == null) {
                        OcrBusinessLicenseResponse response = new OcrBusinessLicenseResponse();
                        response.setCode(201);
                        return response;
                    } else {
                        String errorCode = o.toString();
                        // 获取错误提示信息
                        String message = BaiduErrorCode.BAIDU_ERROR_CODE_MAP.get(errorCode);
                        log.error(message);
                        throw new ServiceException("ocr营业执照识别,调用百度接口异常。错误代码=" + errorCode+",错误信息="+message);
                    }
                }
            }*/
            } catch (Exception e) {
                log.error("ocr营业执照识别,调用百度接口异常。", e.getMessage());
                throw new ServiceException("ocr营业执照识别,调用百度接口异常。异常消息=" + e.getMessage());
            }
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


    /***
     * @Description: ocr身份证头像面识别
     * @Author:liangss
     * @Date:2021/10/28
     * @Param: [ocrIdcardRequest]
     */
    public OcrCustomTemplateResponse idcardFront(@RequestBody OcrIdcardRequest ocrIdcardRequest) {
        log.info("进入ocr身份证头像面识别");
        BaiduOcrUtil.BAI_DU_URL_CLIENT_ID = BAI_DU_URL_CLIENT_ID;
        BaiduOcrUtil.BAI_DU_URL_CLIENT_SECRET = BAI_DU_URL_CLIENT_SECRET;
        OcrCustomTemplateResponse ocrCustomTemplateResponse = new OcrCustomTemplateResponse();
        /* List<OcrCustomTemplateItemResponse> ocrCustomTemplateItemResponseList =new ArrayList<>();*/
        //接口地址
        String interUrl = "ocrCertificateRest/ocrCertificateRestService/idcardFront";
        //接口code
        String interCode = "idcardFront";
        //接口名称
        String interName = "百度ocr身份证头像面识别";
        String imgBase64 = ocrIdcardRequest.getImgBase64();

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

            try {
                BaiduTokenResponse baiduTokenResponse = BaiduOcrUtil.getCommonAuth();
                Integer code = baiduTokenResponse.getCode();
                if (200 != code) {
                    log.error("ocr身份证头像面识别,百度授权异常。");
                    throw new ServiceException("ocr身份证头像面识别,百度授权异常。异常代码=" + code);
                }
                String accessToken = baiduTokenResponse.getAuthToken();
                ocrCustomTemplateResponse = idcard(ocrIdcardRequest.getImgBase64(), BaiduOcrUtil.IDCARD_FRONT, accessToken);
            } catch (Exception e) {
                log.error("ocr身份证头像面识别,调用百度接口异常。", e);
                throw new ServiceException("ocr身份证头像面识别,调用百度接口异常。异常消息=" + e.getMessage());
            }

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


    /***
     * @Description:ocr身份证国徽面识别
     * @Author:liangss
     * @Date:2021/10/28
     * @Param: [ocrIdcardRequest]
     */
    public OcrCustomTemplateResponse idcardBack(@RequestBody OcrIdcardRequest ocrIdcardRequest) {
        log.info("进入ocr身份证国徽面识别");
        BaiduOcrUtil.BAI_DU_URL_CLIENT_ID = BAI_DU_URL_CLIENT_ID;
        BaiduOcrUtil.BAI_DU_URL_CLIENT_SECRET = BAI_DU_URL_CLIENT_SECRET;
        OcrCustomTemplateResponse ocrCustomTemplateResponse = new OcrCustomTemplateResponse();

        //接口地址
        String interUrl = "ocrCertificateRest/ocrCertificateRestService/idcardBack";
        //接口code
        String interCode = "idcardBack";
        //接口名称
        String interName = "百度ocr身份证国徽面识别";
        String imgBase64 = ocrIdcardRequest.getImgBase64();

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
            try {
                BaiduTokenResponse baiduTokenResponse = BaiduOcrUtil.getCommonAuth();
                Integer code = baiduTokenResponse.getCode();
                if (200 != code) {
                    log.error("ocr身份证国徽面识别,百度授权异常。");
                    throw new ServiceException("ocr身份证国徽面识别,百度授权异常。异常代码=" + code);
                }
                String accessToken = baiduTokenResponse.getAuthToken();
                ocrCustomTemplateResponse = idcard(ocrIdcardRequest.getImgBase64(), BaiduOcrUtil.IDCARD_BACK, accessToken);
            } catch (Exception e) {
                log.error("ocr身份证国徽面识别,调用百度接口异常。", e);
                throw new ServiceException("ocr身份证国徽面识别,调用百度接口异常。异常消息=" + e.getMessage());
            }
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


    public OcrCustomTemplateResponse tempIdcardOLD(@RequestBody OcrIdcardRequest ocrIdcardRequest) {
        log.info("进入ocr临时身份证、复印件识别");
        BaiduOcrUtil.BAI_DU_URL_CLIENT_ID = BAI_DU_URL_CLIENT_ID;
        BaiduOcrUtil.BAI_DU_URL_CLIENT_SECRET = BAI_DU_URL_CLIENT_SECRET;
        OcrCustomTemplateResponse ocrCustomTemplateResponse = new OcrCustomTemplateResponse();
        List<OcrCustomTemplateItemResponse> ocrCustomTemplateItemResponseList = new ArrayList<>();
        try {
            BaiduTokenResponse baiduTokenResponse = BaiduOcrUtil.getCommonAuth();
            Integer code = baiduTokenResponse.getCode();
            if (200 != code) {
                log.error("ocr临时身份证、复印件识别,百度授权异常。");
                throw new ServiceException("ocr临时身份证、复印件识别,百度授权异常。");
            }
            String accessToken = baiduTokenResponse.getAuthToken();
            // 获取请求的拼接参数字符串
            Map<String, Object> params = new HashMap<String, Object>(8);
            // 可选参数
            params.put("access_token", accessToken);
            /*     params.put("id_card_side", BaiduOcrUtil.IDCARD_FRONT);*/
            params.put("image", ocrIdcardRequest.getImgBase64());
            params.put("detect_risk", true);
            Map<String, Object> frontResultmap = CommonRestUtil.sendPost(BaiduOcrUtil.HOST_URL_OCR_HOST_MULTI_IDCARD, params);
            // 调用成功
            if (frontResultmap.get(BaiduOcrUtil.BAI_DU_URL_ERROR_CODE) != null) {
                log.error("ocr临时身份证、复印件识别,调用百度接口异常。");
                throw new ServiceException("ocr临时身份证、复印件识别,调用百度接口异常。异常信息=" + frontResultmap.get(BaiduOcrUtil.BAI_DU_URL_ERROR_CODE));
            }
            Object frontResultObj = frontResultmap.get(BaiduOcrUtil.BAI_DU_URL_WORDS_RESULT);
           /* // 解析百度识别身份证信息

            AnalysisBaiduUtil.analysisOcrCertificateRest(frontResultObj,ocrCustomTemplateItemResponseList);
            params.put("id_card_side", BaiduOcrUtil.IDCARD_BACK);
            Map<String, Object> backResultmap = CommonRestUtil.sendPost(BaiduOcrUtil.HOST_URL_OCR_HOST_IDCARD, params);
            if (backResultmap.get(BaiduOcrUtil.BAI_DU_URL_ERROR_CODE) != null) {
                log.error("ocr临时身份证、复印件识别,调用百度接口异常。");
                throw new ServiceException("ocr临时身份证、复印件识别,调用百度接口异常。异常信息=" + frontResultmap.get(BaiduOcrUtil.BAI_DU_URL_ERROR_CODE));
            }
            Object backResultObj = backResultmap.get(BaiduOcrUtil.BAI_DU_URL_WORDS_RESULT);*/
            AnalysisBaiduUtil.analysisOcrCertificateRest(frontResultObj, ocrCustomTemplateItemResponseList);
            ocrCustomTemplateResponse.setOcrCustomTemplateItemResponseList(ocrCustomTemplateItemResponseList);
            ocrCustomTemplateResponse.setCode(200);
        } catch (Exception e) {
            log.error("ocr临时身份证、复印件识别,调用百度接口异常。", e);
            throw new ServiceException("ocr临时身份证、复印件识别,调用百度接口异常。异常信息=" + e.getMessage());
        }
        return ocrCustomTemplateResponse;
    }


    /***
     * @Description:ocr临时身份证、复印件识别
     * @Author:liangss
     * @Date:2021/10/28
     * @Param: [ocrIdcardRequest]
     */
    public OcrCustomTemplateResponse tempIdcard(@RequestBody OcrIdcardRequest ocrIdcardRequest) {
        log.info("进入ocr临时身份证、复印件识别");
        BaiduOcrUtil.BAI_DU_URL_CLIENT_ID = BAI_DU_URL_CLIENT_ID;
        BaiduOcrUtil.BAI_DU_URL_CLIENT_SECRET = BAI_DU_URL_CLIENT_SECRET;
        OcrCustomTemplateResponse ocrCustomTemplateResponse = new OcrCustomTemplateResponse();
        List<OcrCustomTemplateItemResponse> ocrCustomTemplateItemResponseList = new ArrayList<>();

        //接口地址
        String interUrl = "ocrCertificateRest/ocrCertificateRestService/tempIdcard";
        //接口code
        String interCode = "tempIdcard";
        //接口名称
        String interName = "百度ocr临时身份证、复印件识别";
        String imgBase64 = ocrIdcardRequest.getImgBase64();

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
            try {
                BaiduTokenResponse baiduTokenResponse = BaiduOcrUtil.getCommonAuth();
                Integer code = baiduTokenResponse.getCode();
                if (200 != code) {
                    log.error("ocr临时身份证、复印件识别,百度授权异常。");
                    throw new ServiceException("ocr临时身份证、复印件识别,百度授权异常。");
                }
                String accessToken = baiduTokenResponse.getAuthToken();
                // 获取请求的拼接参数字符串
                Map<String, Object> params = new HashMap<String, Object>(8);
                // 可选参数
                params.put("access_token", accessToken);
                params.put("id_card_side", BaiduOcrUtil.IDCARD_FRONT);
                params.put("image", ocrIdcardRequest.getImgBase64());
                params.put("detect_risk", true);
                Map<String, Object> frontResultmap = CommonRestUtil.sendPost(BaiduOcrUtil.HOST_URL_OCR_HOST_IDCARD, params);
                // 调用成功
                if (frontResultmap.get(BaiduOcrUtil.BAI_DU_URL_ERROR_CODE) != null) {
                    log.error("ocr临时身份证、复印件识别,调用百度接口异常。");
                    throw new ServiceException("ocr临时身份证、复印件识别,调用百度接口异常。异常信息=" + frontResultmap.get(BaiduOcrUtil.BAI_DU_URL_ERROR_CODE));
                }
                // 解析百度识别身份证信息
                Object frontResultObj = frontResultmap.get(BaiduOcrUtil.BAI_DU_URL_WORDS_RESULT);
                AnalysisBaiduUtil.analysisOcrCertificateRest(frontResultObj, ocrCustomTemplateItemResponseList);
                params.put("id_card_side", BaiduOcrUtil.IDCARD_BACK);
                Map<String, Object> backResultmap = CommonRestUtil.sendPost(BaiduOcrUtil.HOST_URL_OCR_HOST_IDCARD, params);
                if (backResultmap.get(BaiduOcrUtil.BAI_DU_URL_ERROR_CODE) != null) {
                    log.error("ocr临时身份证、复印件识别,调用百度接口异常。");
                    throw new ServiceException("ocr临时身份证、复印件识别,调用百度接口异常。异常信息=" + frontResultmap.get(BaiduOcrUtil.BAI_DU_URL_ERROR_CODE));
                }
                Object backResultObj = backResultmap.get(BaiduOcrUtil.BAI_DU_URL_WORDS_RESULT);
                AnalysisBaiduUtil.analysisOcrCertificateRest(backResultObj, ocrCustomTemplateItemResponseList);
                ocrCustomTemplateResponse.setOcrCustomTemplateItemResponseList(ocrCustomTemplateItemResponseList);
                ocrCustomTemplateResponse.setCode(200);
            } catch (Exception e) {
                log.error("ocr临时身份证、复印件识别,调用百度接口异常。", e);
                throw new ServiceException("ocr临时身份证、复印件识别,调用百度接口异常。异常信息=" + e.getMessage());
            }
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


    /***
     * @Description:ocr户口本识别
     * @Author:liangss
     * @Date:2021/10/28
     * @Param: [householdRegisterRequest]
     */
    public OcrCustomTemplateResponse householdRegister(@RequestBody HouseholdRegisterRequest householdRegisterRequest) {
        log.info("进入ocr户口本识别");
        BaiduOcrUtil.BAI_DU_URL_CLIENT_ID = BAI_DU_URL_CLIENT_ID;
        BaiduOcrUtil.BAI_DU_URL_CLIENT_SECRET = BAI_DU_URL_CLIENT_SECRET;
        OcrCustomTemplateResponse ocrCustomTemplateResponse = new OcrCustomTemplateResponse();
        List<OcrCustomTemplateItemResponse> ocrCustomTemplateItemResponseList = new ArrayList<>();

        //接口地址
        String interUrl = "ocrCertificateRest/ocrCertificateRestService/householdRegister";
        //接口code
        String interCode = "householdRegister";
        //接口名称
        String interName = "百度ocr户口本识别";
        String imgBase64 = householdRegisterRequest.getImgBase64();

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
            try {
                BaiduTokenResponse baiduTokenResponse = BaiduOcrUtil.getCommonAuth();
                Integer code = baiduTokenResponse.getCode();
                if (200 != code) {
                    log.error("ocr户口本识别,百度授权异常。");
                    throw new ServiceException("ocr户口本识别,百度授权异常。异常代码=" + code);
                }
                String accessToken = baiduTokenResponse.getAuthToken();
                // 获取请求的拼接参数字符串
                Map<String, Object> params = new HashMap<String, Object>(8);
                // 可选参数
                params.put("access_token", accessToken);
                params.put("image", householdRegisterRequest.getImgBase64());
                Map<String, Object> resultmap = CommonRestUtil.sendPost(BaiduOcrUtil.HOST_URL_OCR_HOST_HOUSEHOLD_REGISTER,
                        params);
                // 调用成功
                if (resultmap.get(BaiduOcrUtil.BAI_DU_URL_ERROR_CODE) != null) {
                    log.error("ocr户口本识别,调用百度接口异常。");
                    throw new ServiceException("ocr户口本识别,调用百度接口异常。异常消息=" + resultmap.get(BaiduOcrUtil.BAI_DU_URL_ERROR_CODE));
                }
                // 解析百度识别户口本信息
                Object resultObj = resultmap.get(BaiduOcrUtil.BAI_DU_URL_WORDS_RESULT);
                /* AnalysisBaiduUtil.analysisHouseholdRegister(resultObj, householdRegisterResponse);*/
                AnalysisBaiduUtil.analysisOcrCertificateRest(resultObj, ocrCustomTemplateItemResponseList);
                ocrCustomTemplateResponse.setOcrCustomTemplateItemResponseList(ocrCustomTemplateItemResponseList);
                ocrCustomTemplateResponse.setCode(200);
            } catch (Exception e) {
                log.error("ocr临时身份证、复印件识别,调用百度接口异常。", e);
                throw new ServiceException("ocr临时身份证、复印件识别,调用百度接口异常。异常消息=" + e.getMessage());
            }
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

    /**
     * 身份证识别私有方法
     *
     * @param imgBase64
     * @param side
     * @param accessToken
     * @return
     * @throws Exception
     * @author chenbw
     * @date 2019年6月25日
     */
    private OcrCustomTemplateResponse idcard(String imgBase64, String side, String accessToken) throws Exception {
        OcrCustomTemplateResponse ocrCustomTemplateResponse = new OcrCustomTemplateResponse();
        List<OcrCustomTemplateItemResponse> ocrCustomTemplateItemResponseList = new ArrayList<>();
        // 获取请求的拼接参数字符串
        Map<String, Object> params = new HashMap<String, Object>(8);
        // 可选参数
        params.put("access_token", accessToken);
        params.put("id_card_side", side);
        //可选值 true,false是否检测图像朝向，默认不检测，即：false。可选值包括true - 检测朝向；false - 不检测朝向。朝向是指输入图像是正常方向、逆时针旋转90/180/270度
        params.put("detect_direction", "true");
        params.put("image", imgBase64);
        Map<String, Object> frontResultmap = CommonRestUtil.sendPost(BaiduOcrUtil.HOST_URL_OCR_HOST_IDCARD, params);
        // 调用成功
        if (frontResultmap.get(BaiduOcrUtil.BAI_DU_URL_ERROR_CODE) != null) {
            throw new ServiceException("ocr身份证识别,调用百度接口异常。");
        }
        Object frontResultObj = frontResultmap.get(BaiduOcrUtil.BAI_DU_URL_WORDS_RESULT);
        /* AnalysisBaiduUtil.analysisBaiduIdCard(frontResultObj, ocrIdcardResponse);*/
        AnalysisBaiduUtil.analysisOcrCertificateRest(frontResultObj, ocrCustomTemplateItemResponseList);
        ocrCustomTemplateResponse.setOcrCustomTemplateItemResponseList(ocrCustomTemplateItemResponseList);
        ocrCustomTemplateResponse.setCode(200);
        return ocrCustomTemplateResponse;
    }


    /***
     * @Description:银行卡识别方法
     * @Author:liangss
     * @Date:
     * @Param: [ocrCardRequest]
     */
    public OcrCardResponse bankcard(@RequestBody OcrCardRequest ocrCardRequest) {
        log.info("进入ocr银行卡识别");
        BaiduOcrUtil.BAI_DU_URL_CLIENT_ID = BAI_DU_URL_CLIENT_ID;
        BaiduOcrUtil.BAI_DU_URL_CLIENT_SECRET = BAI_DU_URL_CLIENT_SECRET;
        OcrCardResponse ocrCardResponse = new OcrCardResponse();
        List<OcrCardItemResponse> ocrCardItemResponseList = new ArrayList<>();
        try {
            BaiduTokenResponse baiduTokenResponse = BaiduOcrUtil.getCommonAuth();
            Integer code = baiduTokenResponse.getCode();
            if (200 != code) {
                log.error("ocr银行卡识别,百度授权异常。");
                throw new ServiceException("ocr户口本识别,百度授权异常。异常代码=" + code);
            }
            String accessToken = baiduTokenResponse.getAuthToken();
            // 获取请求的拼接参数字符串
            Map<String, Object> params = new HashMap<String, Object>(8);
            // 可选参数
            params.put("access_token", accessToken);
            params.put("image", ocrCardRequest.getImgBase64());
            Map<String, Object> resultmap = CommonRestUtil.sendPost(BaiduOcrUtil.HOST_URL_OCR_HOST_BANK_CARD,
                    params);
            // 调用成功
            if (resultmap.get(BaiduOcrUtil.BAI_DU_URL_ERROR_CODE) != null) {
                log.error("ocr银行卡识别,调用百度接口异常。");
                throw new ServiceException("ocr银行卡识别,调用百度接口异常。异常消息=" + resultmap.get(BaiduOcrUtil.BAI_DU_URL_ERROR_CODE));
            }
            // 解析百度识别户口本信息
            Object resultObj = resultmap.get(BaiduOcrUtil.BAI_DU_URL_WORDS_RESULT);
            /* AnalysisBaiduUtil.analysisHouseholdRegister(resultObj, householdRegisterResponse);*/
            AnalysisBaiduUtil.analyOcrCardItem(resultObj, ocrCardItemResponseList);
            ocrCardResponse.setOcrCardItemResponseList(ocrCardItemResponseList);
            ocrCardResponse.setCode(200);
        } catch (Exception e) {
            log.error("ocr银行卡识别,调用百度接口异常。", e);
            throw new ServiceException("ocr银行卡识别,调用百度接口异常。异常消息=" + e.getMessage());
        }
        return ocrCardResponse;
    }


}
