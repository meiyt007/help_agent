package com.zfsoft.ocr.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.ocr.data.pojo.face.FaceAddResponse;
import com.zfsoft.ocr.data.pojo.face.FaceSearchResponse;
import com.zfsoft.ocr.data.pojo.face.FaceUpdateResponse;
import com.zfsoft.ocr.data.pojo.ocr.*;
import com.zfsoft.single.util.JsonUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 机器人接口调用结果返回解析类
 *
 * @author cbc
 * @date 2019年2月27日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
public class AnalysisBaiduUtil {

    /**
     * 文字位置-宽
     */
    public static final String BAIDU_OCR_POS_WIDTH = "width";

    /**
     * 文字位置-顶端
     */
    public static final String BAIDU_OCR_POS_TOP = "top";

    /**
     * 文字位置-左端
     */
    public static final String BAIDU_OCR_POS_LEFT = "left";

    /**
     * 文字位置-高
     */
    public static final String BAIDU_OCR_POS_HEIGHT = "height";

    /**
     * 识别内容
     */
    public static final String BAIDU_OCR_WORDS = "words";

    /**
     * 识别位置信息
     */
    public static final String BAIDU_OCR_LOCATION = "location";

    /**
     * 人脸注册-文字位置-旋转
     */
    public static final String BAIDU_OCR_ROTATION = "rotation";

    /**
     * 人脸注册-token
     */
    public static final String BAIDU_OCR_FACE_TOKEN = "face_token";

    /**
     * 人脸搜索-人脸库组id
     */
    public static final String BAIDU_FACE_SEARCH_GROUP_ID = "group_id";

    /**
     * 人脸搜索-人脸比对分数
     */
    public static final String BAIDU_FACE_SEARCH_SCORE = "score";

    /**
     * 人脸搜索-人脸库用户id
     */
    public static final String BAIDU_FACE_SEARCH_USER_ID = "user_id";

    /**
     * 人脸搜索-人脸库人脸信息
     */
    public static final String BAIDU_FACE_SEARCH_USER_INFO = "user_info";

    /**
     * 人脸搜索-人脸库人脸信息集合
     */
    public static final String BAIDU_FACE_SEARCH_USER_LIST = "user_list";

    /**
     * 营业执照识别-信息
     */
    public static final String BAIDU_WORDS = "words";

    /**
     * 无，营业执照识别不到信息的时候会默认返回“无”
     */
    public static final String NOT_HAVE = "无";


    /**
     * 营业执照识别-单位名称
     */
    public static final String BAIDU_BUSINESS_LICENSE_KEY_DWMC = "单位名称";

    /**
     * 营业执照识别-社会信用代码
     */
    public static final String BAIDU_BUSINESS_LICENSE_KEY_SHXYDM = "社会信用代码";

    /**
     * 营业执照识别-地址
     */
    public static final String BAIDU_BUSINESS_LICENSE_KEY_DZ = "地址";

    /**
     * 营业执照识别-成立日期
     */
    public static final String BAIDU_BUSINESS_LICENSE_KEY_CLRQ = "成立日期";

    /**
     * 营业执照识别-有效期
     */
    public static final String BAIDU_BUSINESS_LICENSE_KEY_YXQ = "有效期";

    /**
     * 营业执照识别-法人
     */
    public static final String BAIDU_BUSINESS_LICENSE_KEY_FR = "法人";

    /**
     * 营业执照识别-注册资本
     */
    public static final String BAIDU_BUSINESS_LICENSE_KEY_ZCZB = "注册资本";

    /**
     * 营业执照识别-类型
     */
    public static final String BAIDU_BUSINESS_LICENSE_KEY_LX = "类型";

    /**
     * 营业执照识别-组成形式
     */
    public static final String BAIDU_BUSINESS_LICENSE_KEY_ZCXS = "组成形式";

    /**
     * 营业执照识别-证件编号
     */
    public static final String BAIDU_BUSINESS_LICENSE_KEY_ZJBH = "证件编号";

    /**
     * 身份证-姓名
     */
    public static final String BAIDU_IDCARD_NAME = "姓名";

    /**
     * 身份证-性别
     */
    public static final String BAIDU_IDCARD_SEX = "性别";

    /**
     * 身份证-民族
     */
    public static final String BAIDU_IDCARD_NATION = "民族";

    /**
     * 身份证-出生
     */
    public static final String BAIDU_IDCARD_BIRTH = "出生";

    /**
     * 身份证-住址
     */
    public static final String BAIDU_IDCARD_ADDRESS = "住址";

    /**
     * 身份证-公民身份号码
     */
    public static final String BAIDU_IDCARD_NUMBER = "公民身份号码";

    /**
     * 身份证-签发机关
     */
    public static final String BAIDU_IDCARD_SIGN_DEPARTMENT = "签发机关";

    /**
     * 身份证-签发日期
     */
    public static final String BAIDU_IDCARD_SIGN_DATE = "签发日期";

    /**
     * 身份证-失效日期
     */
    public static final String BAIDU_IDCARD_END_DATE = "失效日期";

    /**
     * 户口本-姓名
     */
    public static final String BAIDU_HOUSEHOLD_REGISTER_NAME = "Name";

    /**
     * 户口本-性别
     */
    public static final String BAIDU_HOUSEHOLD_REGISTER_SEX = "Sex";

    /**
     * 户口本-民族
     */
    public static final String BAIDU_HOUSEHOLD_REGISTER_NATION = "Nation";

    /**
     * 户口本-生日
     */
    public static final String BAIDU_HOUSEHOLD_REGISTER_BIRTHDAY = "Birthday";

    /**
     * 户口本-出生地
     */
    public static final String BAIDU_HOUSEHOLD_REGISTER_BIRTHADDRESS = "BirthAddress";

    /**
     * 户口本-身份证号
     */
    public static final String BAIDU_HOUSEHOLD_REGISTER_CARDNO = "CardNo";

    /**
     * 户口本-与户主关系
     */
    public static final String BAIDU_HOUSEHOLD_REGISTER_RELATIONSHIP = "Relationship";


    /**
     * 解析百度文字识别信息
     *
     * @param baiduOcrInfoJson 文字识别JSON集合
     * @return 返回解析结果
     * @author chenbw
     * @date 2019年6月26日
     */
    public static List<OcrHandPrintItemResponse> analysisBaiduOcrInfo(Object baiduOcrInfoJson) {
        if (baiduOcrInfoJson == null) {
            return null;
        }
        List<OcrHandPrintItemResponse> ocrHandPrintItemResponseList = new ArrayList<OcrHandPrintItemResponse>();
        JSONArray ja = JsonUtil.objToJSONArray(baiduOcrInfoJson);
        for (int i = 0; i < ja.size(); i++) {
            JSONObject jo = ja.getJSONObject(i);
            OcrHandPrintItemResponse ocrHandPrintItemResponse = new OcrHandPrintItemResponse();
            ocrHandPrintItemResponse.setWords(jo.getString(BAIDU_OCR_WORDS));
            ocrHandPrintItemResponseList.add(ocrHandPrintItemResponse);
        }
        return ocrHandPrintItemResponseList;
    }


    /**
     * 解析百度文字识别信息（包含位置信息）
     *
     * @param baiduOcrInfoJson 文字识别JSON集合
     * @return 返回解析结果
     * @author chenbw
     * @date 2019年6月26日
     */
    public static List<OcrHandPrintLocationItemResponse> analysisBaiduOcrLocationInfo(Object baiduOcrInfoJson) {
        if (baiduOcrInfoJson == null) {
            return null;
        }
        List<OcrHandPrintLocationItemResponse> ocrHandPrintLocationItemResponseList = new ArrayList<OcrHandPrintLocationItemResponse>();
        JSONArray ja = JsonUtil.objToJSONArray(baiduOcrInfoJson);
        for (int i = 0; i < ja.size(); i++) {
            JSONObject jo = ja.getJSONObject(i);
            JSONObject pos = JsonUtil.objToJSONObject(jo.get(BAIDU_OCR_LOCATION));
            OcrHandPrintLocationItemResponse ocrHandPrintLocationItemResponse = new OcrHandPrintLocationItemResponse();
            ocrHandPrintLocationItemResponse.setHeight(pos.getString(BAIDU_OCR_POS_HEIGHT));
            ocrHandPrintLocationItemResponse.setLeft(pos.getString(BAIDU_OCR_POS_LEFT));
            ocrHandPrintLocationItemResponse.setTop(pos.getString(BAIDU_OCR_POS_TOP));
            ocrHandPrintLocationItemResponse.setWidth(pos.getString(BAIDU_OCR_POS_WIDTH));
            ocrHandPrintLocationItemResponse.setWords(jo.getString(BAIDU_OCR_WORDS));
            ocrHandPrintLocationItemResponseList.add(ocrHandPrintLocationItemResponse);
        }
        return ocrHandPrintLocationItemResponseList;
    }

    /**
     * 解析人脸注册信息
     *
     * @param baiduFaceAddInfoJson 文字识别JSON集合
     * @return 返回解析结果
     * @author cbc
     * @date 2019年2月27日
     */
    public static FaceAddResponse analysisBaiduFaceAddInfo(Object baiduFaceAddInfoJson) {
        if (baiduFaceAddInfoJson == null) {
            return null;
        }
        JSONObject jo = JsonUtil.objToJSONObject(baiduFaceAddInfoJson);
        JSONObject pos = JsonUtil.objToJSONObject(jo.get(BAIDU_OCR_LOCATION));
        FaceAddResponse boi = new FaceAddResponse();
        boi.setPosHeight(pos.getString(BAIDU_OCR_POS_HEIGHT));
        boi.setPosLeft(pos.getString(BAIDU_OCR_POS_LEFT));
        boi.setPosTop(pos.getString(BAIDU_OCR_POS_TOP));
        boi.setPosWidth(pos.getString(BAIDU_OCR_POS_WIDTH));
        boi.setFaceToken(jo.getString(BAIDU_OCR_FACE_TOKEN));
        boi.setRotation(pos.getString(BAIDU_OCR_ROTATION));
        return boi;
    }

    /**
     * 解析人脸更新信息
     *
     * @param baiduFaceUpdateInfoJson 文字识别JSON集合
     * @return 返回解析结果
     * @author chenbw
     * @date 2019年12月11日
     */
    public static FaceUpdateResponse analysisBaiduFaceUpdateInfo(Object baiduFaceUpdateInfoJson) {
        if (baiduFaceUpdateInfoJson == null) {
            return null;
        }
        JSONObject jo = JsonUtil.objToJSONObject(baiduFaceUpdateInfoJson);
        JSONObject pos = JsonUtil.objToJSONObject(jo.get(BAIDU_OCR_LOCATION));
        FaceUpdateResponse boi = new FaceUpdateResponse();
        boi.setPosHeight(pos.getString(BAIDU_OCR_POS_HEIGHT));
        boi.setPosLeft(pos.getString(BAIDU_OCR_POS_LEFT));
        boi.setPosTop(pos.getString(BAIDU_OCR_POS_TOP));
        boi.setPosWidth(pos.getString(BAIDU_OCR_POS_WIDTH));
        boi.setFaceToken(jo.getString(BAIDU_OCR_FACE_TOKEN));
        boi.setRotation(pos.getString(BAIDU_OCR_ROTATION));
        return boi;
    }

    /**
     * 解析人脸搜索识别信息
     *
     * @param baiduFaceSerachInfoJson 人脸搜索JSON集合
     * @return 返回解析结果
     * @author cbc
     * @date 2019年2月27日
     */
    public static FaceSearchResponse analysisBaiduFaceSearchInfo(Object baiduFaceSerachInfoJson) {
        if (baiduFaceSerachInfoJson == null) {
            return null;
        }
        JSONObject joRel = JsonUtil.objToJSONObject(baiduFaceSerachInfoJson);
        JSONArray ja = JsonUtil.objToJSONArray(joRel.get(BAIDU_FACE_SEARCH_USER_LIST));
        JSONObject jo = new JSONObject();
        Integer score = 0;
        for (int i = 0; i < ja.size(); i++) {
            JSONObject joObj = ja.getJSONObject(i);
            // 获取识别分数,并获取分数最大识别对象
            Integer joScore = joObj.getInteger(BAIDU_FACE_SEARCH_SCORE);
            if (joScore > score) {
                score = joScore;
                jo = joObj;
            }
        }
        FaceSearchResponse fsi = new FaceSearchResponse();
        fsi.setFaceToken(joRel.getString(BAIDU_OCR_FACE_TOKEN));
        fsi.setGroupId(jo.getString(BAIDU_FACE_SEARCH_GROUP_ID));
        fsi.setScore(Double.valueOf(jo.getString(BAIDU_FACE_SEARCH_SCORE)));
        fsi.setUserId(jo.getString(BAIDU_FACE_SEARCH_USER_ID));
        fsi.setUserInfo(jo.getString(BAIDU_FACE_SEARCH_USER_INFO));
        return fsi;
    }

    /**
     * 解析营业执照识别信息
     *
     * @param baiduFaceSerachInfoJsonJson 营业执照识别信息
     * @return 返回解析结果
     * @throws UnsupportedEncodingException
     * @author cbc
     * @date 2019年2月27日
     */
    public static OcrBusinessLicenseResponse analysisBaiduBusinessLicense(OcrBusinessLicenseResponse bli, Object baiduFaceSerachInfoJsonJson) throws UnsupportedEncodingException {
        if (baiduFaceSerachInfoJsonJson == null) {
            return null;
        }
        //JSONObject joObj = new JSONObject();
        JSONObject joRel = JsonUtil.objToJSONObject(baiduFaceSerachInfoJsonJson);

        // 单位名称
        Object companyNameObj = joRel.get(BAIDU_BUSINESS_LICENSE_KEY_DWMC);
        if (companyNameObj != null) {
            JSONObject joObj = JsonUtil.objToJSONObject(companyNameObj);
            String words = joObj.get(BAIDU_WORDS).toString();
            bli.setCompanyName(filterNotHave(words));
            JSONObject locationObj = joObj.getJSONObject("location");
            if (locationObj != null) {
                bli.setCompanyNameLocationJson(locationObj.toJSONString());
            }
        }
        //地址
        Object addressObj = joRel.get(BAIDU_BUSINESS_LICENSE_KEY_DZ);
        if (addressObj != null) {
            JSONObject joObj = JsonUtil.objToJSONObject(addressObj);
            String words = joObj.get(BAIDU_WORDS).toString();
            bli.setAddress(filterNotHave(words));
            JSONObject locationObj = joObj.getJSONObject("location");
            if (locationObj != null) {
                bli.setAddressLocationJson(locationObj.toJSONString());
            }
        }
        //成立日期
        Object establishDateObj = joRel.get(BAIDU_BUSINESS_LICENSE_KEY_CLRQ);
        if (establishDateObj != null) {
            JSONObject joObj = JsonUtil.objToJSONObject(establishDateObj);
            String words = joObj.get(BAIDU_WORDS).toString();
            bli.setEstablishDate(filterNotHave(words));
            JSONObject locationObj = joObj.getJSONObject("location");
            if (locationObj != null) {
                bli.setEstablishDateLocationJson(locationObj.toJSONString());
            }
        }
        //法人
        Object lawRepresentativeObj = joRel.get(BAIDU_BUSINESS_LICENSE_KEY_FR);
        if (lawRepresentativeObj != null) {
            JSONObject joObj = JsonUtil.objToJSONObject(lawRepresentativeObj);
            String words = joObj.get(BAIDU_WORDS).toString();
            bli.setLawRepresentative(filterNotHave(words));
            JSONObject locationObj = joObj.getJSONObject("location");
            if (locationObj != null) {
                bli.setLawRepresentativeLocationJson(locationObj.toJSONString());
            }
        }
        Object registeredCapitalObj = joRel.get(BAIDU_BUSINESS_LICENSE_KEY_ZCZB);
        if (registeredCapitalObj != null) {
            JSONObject joObj = JsonUtil.objToJSONObject(registeredCapitalObj);
            String words = joObj.get(BAIDU_WORDS).toString();
            bli.setRegisteredCapital(filterNotHave(words));
            JSONObject locationObj = joObj.getJSONObject("location");
            if (locationObj != null) {
                bli.setRegisteredCapitalLocationJson(locationObj.toJSONString());
            }
        }
        Object socialCodeObj = joRel.get(BAIDU_BUSINESS_LICENSE_KEY_SHXYDM);
        if (socialCodeObj != null) {
            JSONObject joObj = JsonUtil.objToJSONObject(socialCodeObj);
            String words = joObj.get(BAIDU_WORDS).toString();
            bli.setSocialCode(filterNotHave(words));
            JSONObject locationObj = joObj.getJSONObject("location");
            if (locationObj != null) {
                bli.setSocialCodeLocationJson(locationObj.toJSONString());
            }
        }
        Object typeObj = joRel.get(BAIDU_BUSINESS_LICENSE_KEY_LX);
        if (typeObj != null) {
            JSONObject joObj = JsonUtil.objToJSONObject(typeObj);
            String words = joObj.get(BAIDU_WORDS).toString();
            bli.setType(filterNotHave(words));
            JSONObject locationObj = joObj.getJSONObject("location");
            if (locationObj != null) {
                bli.setTypeLocationJson(locationObj.toJSONString());
            }
        }
        Object compositionFormObj = joRel.get(BAIDU_BUSINESS_LICENSE_KEY_ZCXS);
        if (compositionFormObj != null) {
            JSONObject joObj = JsonUtil.objToJSONObject(compositionFormObj);
            String words = joObj.get(BAIDU_WORDS).toString();
            bli.setCompositionForm(filterNotHave(words));
            JSONObject locationObj = joObj.getJSONObject("location");
            if (locationObj != null) {
                bli.setCompositionFormLocationJson(locationObj.toJSONString());
            }
        }
        Object idNumberObj = joRel.get(BAIDU_BUSINESS_LICENSE_KEY_ZJBH);
        if (idNumberObj != null) {
            JSONObject joObj = JsonUtil.objToJSONObject(idNumberObj);
            String words = joObj.get(BAIDU_WORDS).toString();
            bli.setIdNumber(filterNotHave(words));
            JSONObject locationObj = joObj.getJSONObject("location");
            if (locationObj != null) {
                bli.setIdNumberLocationJson(locationObj.toJSONString());
            }
        }
        return bli;
    }

    /**
     * 解析身份证正面
     *
     * @param json
     * @return
     * @throws UnsupportedEncodingException
     * @author chenbw
     * @date 2019年6月14日
     */
    public static OcrIdcardResponse analysisBaiduIdCard(Object json, OcrIdcardResponse ocrIdcardResponse) throws Exception {
        if (json == null) {
            return null;
        }
        if (ocrIdcardResponse == null) {
            ocrIdcardResponse = new OcrIdcardResponse();
        }
        JSONObject joRel = JsonUtil.objToJSONObject(json);
        String words = "";
        // 姓名
        Object nameObj = joRel.get(BAIDU_IDCARD_NAME);
        if (nameObj != null) {
            JSONObject nameJsonObj = JsonUtil.objToJSONObject(nameObj);
            words = nameJsonObj.get(BAIDU_WORDS).toString();
            ocrIdcardResponse.setName(words);
            JSONObject locationObj = nameJsonObj.getJSONObject("location");
            if (locationObj != null) {
                ocrIdcardResponse.setNameLocationJson(locationObj.toJSONString());
            }
        }
        // 性别
        Object sexObj = joRel.get(BAIDU_IDCARD_SEX);
        if (sexObj != null) {
            JSONObject sexJsonObj = JsonUtil.objToJSONObject(sexObj);
            words = sexJsonObj.get(BAIDU_WORDS).toString();
            ocrIdcardResponse.setSex(words);
            JSONObject locationObj = sexJsonObj.getJSONObject("location");
            if (locationObj != null) {
                ocrIdcardResponse.setSexLocationJson(locationObj.toJSONString());
            }
        }
        // 性别
        Object nationObj = joRel.get(BAIDU_IDCARD_NATION);
        if (nationObj != null) {
            JSONObject nationJsonObj = JsonUtil.objToJSONObject(nationObj);
            words = nationJsonObj.get(BAIDU_WORDS).toString();
            ocrIdcardResponse.setNation(words);
            JSONObject locationObj = nationJsonObj.getJSONObject("location");
            if (locationObj != null) {
                ocrIdcardResponse.setNationLocationJson(locationObj.toJSONString());
            }
        }
        // 出生
        Object birthObj = joRel.get(BAIDU_IDCARD_BIRTH);
        if (birthObj != null) {
            JSONObject birthJsonObj = JsonUtil.objToJSONObject(birthObj);
            words = birthJsonObj.get(BAIDU_WORDS).toString();
            ocrIdcardResponse.setBirth(words);
            JSONObject locationObj = birthJsonObj.getJSONObject("location");
            if (locationObj != null) {
                ocrIdcardResponse.setBirthLocationJson(locationObj.toJSONString());
            }
        }
        // 住址
        Object addressObj = joRel.get(BAIDU_IDCARD_ADDRESS);
        if (addressObj != null) {
            JSONObject addressJsonObj = JsonUtil.objToJSONObject(addressObj);
            words = addressJsonObj.get(BAIDU_WORDS).toString();
            ocrIdcardResponse.setAddress(words);
            JSONObject locationObj = addressJsonObj.getJSONObject("location");
            if (locationObj != null) {
                ocrIdcardResponse.setAddressLocationJson(locationObj.toJSONString());
            }
        }
        // 公民身份号码
        Object numberObj = joRel.get(BAIDU_IDCARD_NUMBER);
        if (numberObj != null) {
            JSONObject numberJsonObj = JsonUtil.objToJSONObject(numberObj);
            words = numberJsonObj.get(BAIDU_WORDS).toString();
            ocrIdcardResponse.setNumber(words);
            JSONObject locationObj = numberJsonObj.getJSONObject("location");
            if (locationObj != null) {
                ocrIdcardResponse.setNumberLocationJson(locationObj.toJSONString());
            }
        }
        // 签发机关
        Object signDepartmentObj = joRel.get(BAIDU_IDCARD_SIGN_DEPARTMENT);
        if (signDepartmentObj != null) {
            JSONObject signDepartmentJsonObj = JsonUtil.objToJSONObject(signDepartmentObj);
            words = signDepartmentJsonObj.get(BAIDU_WORDS).toString();
            ocrIdcardResponse.setSignDepartment(words);
            JSONObject locationObj = signDepartmentJsonObj.getJSONObject("location");
            if (locationObj != null) {
                ocrIdcardResponse.setSignDepartmentLocationJson(locationObj.toJSONString());
            }
        }
        // 签发日期
        Object signDateObj = joRel.get(BAIDU_IDCARD_SIGN_DATE);
        if (signDateObj != null) {
            JSONObject signDateJsonObj = JsonUtil.objToJSONObject(signDateObj);
            words = signDateJsonObj.get(BAIDU_WORDS).toString();
            ocrIdcardResponse.setSignDate(words);
            JSONObject locationObj = signDateJsonObj.getJSONObject("location");
            if (locationObj != null) {
                ocrIdcardResponse.setSignDateLocationJson(locationObj.toJSONString());
            }
        }
        // 失效日期
        Object endDateObj = joRel.get(BAIDU_IDCARD_END_DATE);
        if (endDateObj != null) {
            JSONObject endDateJsonObj = JsonUtil.objToJSONObject(endDateObj);
            words = endDateJsonObj.get(BAIDU_WORDS).toString();
            ocrIdcardResponse.setEndDate(words);
            JSONObject locationObj = endDateJsonObj.getJSONObject("location");
            if (locationObj != null) {
                ocrIdcardResponse.setEndDateLocationJson(locationObj.toJSONString());
            }
        }
        return ocrIdcardResponse;
    }


    /**
     * 解析户口本
     *
     * @param resultObj
     * @return
     * @throws Exception
     * @author chenbw
     * @date 2019年6月18日
     */
    public static HouseholdRegisterResponse analysisHouseholdRegister(Object resultObj, HouseholdRegisterResponse householdRegisterResponse) throws Exception {
        if (resultObj == null) {
            return null;
        }
        JSONObject joRel = JsonUtil.objToJSONObject(resultObj);
        String words = "";
        // 姓名
        Object nameObj = joRel.get(BAIDU_HOUSEHOLD_REGISTER_NAME);
        if (nameObj != null) {
            JSONObject nameJsonObj = JsonUtil.objToJSONObject(nameObj);
            words = nameJsonObj.get(BAIDU_WORDS).toString();
            householdRegisterResponse.setName(words);
        }
        // 性别
        Object sexObj = joRel.get(BAIDU_HOUSEHOLD_REGISTER_SEX);
        if (sexObj != null) {
            JSONObject sexJsonObj = JsonUtil.objToJSONObject(sexObj);
            words = sexJsonObj.get(BAIDU_WORDS).toString();
            householdRegisterResponse.setSex(words);
        }
        // 民族
        Object nationObj = joRel.get(BAIDU_HOUSEHOLD_REGISTER_NATION);
        if (nationObj != null) {
            JSONObject nationJsonObj = JsonUtil.objToJSONObject(nationObj);
            words = nationJsonObj.get(BAIDU_WORDS).toString();
            householdRegisterResponse.setSex(words);
        }
        // 生日
        Object birthdayObj = joRel.get(BAIDU_HOUSEHOLD_REGISTER_BIRTHDAY);
        if (birthdayObj != null) {
            JSONObject birthdayJsonObj = JsonUtil.objToJSONObject(birthdayObj);
            words = birthdayJsonObj.get(BAIDU_WORDS).toString();
            householdRegisterResponse.setSex(words);
        }
        // 出生地
        Object birthaddressObj = joRel.get(BAIDU_HOUSEHOLD_REGISTER_BIRTHADDRESS);
        if (birthaddressObj != null) {
            JSONObject birthaddressJsonObj = JsonUtil.objToJSONObject(birthaddressObj);
            words = birthaddressJsonObj.get(BAIDU_WORDS).toString();
            householdRegisterResponse.setSex(words);
        }
        // 身份证号
        Object cardnoObj = joRel.get(BAIDU_HOUSEHOLD_REGISTER_CARDNO);
        if (cardnoObj != null) {
            JSONObject cardnoJsonObj = JsonUtil.objToJSONObject(cardnoObj);
            words = cardnoJsonObj.get(BAIDU_WORDS).toString();
            householdRegisterResponse.setSex(words);
        }
        // 与户主关系
        Object relationshipObj = joRel.get(BAIDU_HOUSEHOLD_REGISTER_RELATIONSHIP);
        if (relationshipObj != null) {
            JSONObject relationshipJsonObj = JsonUtil.objToJSONObject(relationshipObj);
            words = relationshipJsonObj.get(BAIDU_WORDS).toString();
            householdRegisterResponse.setSex(words);
        }
        return householdRegisterResponse;
    }


    /**
     * 处理营业执照识别不到信息返回“无”的情况
     *
     * @param: str
     * @return: String
     * @Auther: chenbw
     * @Date: 2020/11/19 14:53
     */
    public static String filterNotHave(String str) {
        if (StrUtil.isBlank(str) || NOT_HAVE.equals(str)) {
            return null;
        }
        return str;
    }


    /***
     * @Description: 解析卡证结果
     * @Author:liangss
     * @Date:2021/10/28
     * @Param: [json, ocrCustomTemplateItemResponseList]
     */
    public static List<OcrCustomTemplateItemResponse> analysisOcrCertificateRest
    (Object json, List<OcrCustomTemplateItemResponse> ocrCustomTemplateItemResponseList) throws Exception {
        JSONObject jsonObject = JsonUtil.objToJSONObject(json);
        Set<Map.Entry<String, Object>> it = jsonObject.entrySet();//jsonObject.keys();
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            OcrCustomTemplateItemResponse ocrCustomTemplateItemResponse = new OcrCustomTemplateItemResponse();
            ocrCustomTemplateItemResponse.setName(entry.getKey());
            if (entry.getValue() != null) {
                JSONObject value = JsonUtil.objToJSONObject(entry.getValue());
                if (value != null) {
                    ocrCustomTemplateItemResponse.setWord(String.valueOf(value.get("words")));
                    JSONObject locationObject = value.getJSONObject("location");
                    if (null != locationObject) {
                        ocrCustomTemplateItemResponse.setHeight(locationObject.getString("height"));
                        ocrCustomTemplateItemResponse.setWidth(locationObject.getString("width"));
                        ocrCustomTemplateItemResponse.setLeft(locationObject.getString("left"));
                        ocrCustomTemplateItemResponse.setTop(locationObject.getString("top"));
                    }
                }
            }
            ocrCustomTemplateItemResponseList.add(ocrCustomTemplateItemResponse);
        }

        return ocrCustomTemplateItemResponseList;
    }


    /***
     * @Description: 解析卡证识别结果
     * @Author:liangss
     * @Date:2021/10/29
     * @Param: [json, ocrCardItemResponseList]
     */
    public static List<OcrCardItemResponse> analyOcrCardItem(Object json, List<OcrCardItemResponse> ocrCardItemResponseList) throws Exception {
        JSONObject jsonObject = JsonUtil.objToJSONObject(json);
        Set<Map.Entry<String, Object>> it = jsonObject.entrySet();//jsonObject.keys();
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            OcrCardItemResponse ocrCustomTemplateItemResponse = new OcrCardItemResponse();
            ocrCustomTemplateItemResponse.setName(entry.getKey());
            if (entry.getValue() != null) {
                JSONObject value = JsonUtil.objToJSONObject(entry.getValue());
                if (value != null) {
                    ocrCustomTemplateItemResponse.setWord(String.valueOf(value.get("words")));
                    JSONObject locationObject = value.getJSONObject("location");
                    if (null != locationObject) {
                        ocrCustomTemplateItemResponse.setHeight(locationObject.getString("height"));
                        ocrCustomTemplateItemResponse.setWidth(locationObject.getString("width"));
                        ocrCustomTemplateItemResponse.setLeft(locationObject.getString("left"));
                        ocrCustomTemplateItemResponse.setTop(locationObject.getString("top"));
                    }
                }
            }
            ocrCardItemResponseList.add(ocrCustomTemplateItemResponse);
        }

        return ocrCardItemResponseList;
    }
}
