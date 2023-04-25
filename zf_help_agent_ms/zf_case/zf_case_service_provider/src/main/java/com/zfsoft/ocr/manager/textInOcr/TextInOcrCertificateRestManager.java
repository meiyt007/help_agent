package com.zfsoft.ocr.manager.textInOcr;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.ocr.data.pojo.common.OcrCallRecord;
import com.zfsoft.ocr.data.pojo.ocr.*;
import com.zfsoft.ocr.data.pojo.textInOcr.TextInOcrRequest;
import com.zfsoft.ocr.data.pojo.textInOcr.TextInOcrResponse;
import com.zfsoft.ocr.manager.common.OcrCallRecordManager;
import com.zfsoft.ocr.util.FileManageUtil;
import com.zfsoft.ocr.util.textIn.CommonUtil;
import com.zfsoft.ocr.util.textIn.TextInOcr;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.bind.DatatypeConverter;


/***
* @Description: TextInOcr卡证证照识别实现类
* @Author:liangss
* @Date:2021/11/1
* @Param:
*/
@Service
@Slf4j
public class TextInOcrCertificateRestManager {
    @Value("${zfsoft.textOcr.appId}")
    public  String appId;

    @Value("${zfsoft.textOcr.secretCode}")
    public  String secretCode;

    @Resource
    private OcrCallRecordManager ocrCallRecordManager;
    private java.lang.Object Object;


    /***
    * @Description: 根据类型识别卡证信息
    * @Author:liangss
    * @Date:2021/11/3
    * @Param: [textInOcrRequest]
    */
    public TextInOcrResponse textInOcrCertificateRestOcr(TextInOcrRequest textInOcrRequest) throws Exception {
        log.info("进入TextIn卡证ocr识别");
        //接口地址
        String  interUrl="textInOcrCertificateRest/textInOcrCertificateRestService/textInOcrCertificateRestOcr";
        //接口code
        String interCode=textInOcrRequest.getType();
        //接口名称
        String interName="合合卡证识别";


        TextInOcr.appId=appId;
        TextInOcr.secretCode=secretCode;
        TextInOcrResponse textInOcrResponse = new TextInOcrResponse();
        String imgUrl=textInOcrRequest.getImgUrl();
        String type=textInOcrRequest.getType();
        String imgBase64=textInOcrRequest.getImgBase64();
        String url="";
        if(type.equals("business_license")){//营业执照
            url = CommonUtil.business_license;
        }else if(type.equals("id_card")){//身份证
            url = CommonUtil.id_card;
        }else if(type.equals("household_register")){//户口本
            url = CommonUtil.household_register;
        }else if(type.equals("bank_card")){//银行卡(不返回位置)
            url = CommonUtil.bank_card;
        }else if(type.equals("business_card")){//名片
            url = CommonUtil.business_card;
        }else if(type.equals("driver_license")){//驾驶证
            url = CommonUtil.driver_license;
        }else if(type.equals("vehicle_license")){//行驶证
            url = CommonUtil.vehicle_license;
        }else if(type.equals("bills_crop")){//国内通用票据识别
            url = CommonUtil.bills_crop;
        }else if(type.equals("train_ticket")){//火车票识别
            url = CommonUtil.train_ticket;
        }else if(type.equals("social_security_card")){//社保卡识别
            url = CommonUtil.social_security_card;
        }else if(type.equals("passport")){//护照识别
            url = CommonUtil.passport;
        }else if(type.equals("taxi_invoice")){//出租车发票识别
            url = CommonUtil.taxi_invoice;
        }else if(type.equals("roll_invoice")){//卷式发票识别
            url = CommonUtil.roll_invoice;
        }else if(type.equals("tax_registration_certificate")){//税务登记证识别
            url = CommonUtil.tax_registration_certificate;
        }else if(type.equals("organization_code_certificate")){//组织机构代码证识别
            url = CommonUtil.organization_code_certificate;
        }else if(type.equals("account_opening_permit")){//开户许可证识别
            url = CommonUtil.account_opening_permit;
        }else if(type.equals("mac_id_card")){//澳门身份证识别
            url = CommonUtil.mac_id_card;
        }else if(type.equals("hk_mac_mainland_travel_permit")){//港澳居民来往内地通行证
            url = CommonUtil.hk_mac_mainland_travel_permit;
        }else if(type.equals("idn_id_card")){//印尼身份证识别
            url = CommonUtil.idn_id_card;
        }else if(type.equals("malaysia_id_card")){//马来西亚身份证识别
            url = CommonUtil.malaysia_id_card;
        }else if(type.equals("marriage_certificate")){//结婚证识别
            url = CommonUtil.marriage_certificate;
        }else if(type.equals("property_certificate")){//房产证识别
            url = CommonUtil.property_certificate;
        }else if(type.equals("military_id_card")){//军官证识别
            url = CommonUtil.military_id_card;
        }else if(type.equals("vehicle_inspection_certificate")){//车辆合格证识别
            url = CommonUtil.vehicle_inspection_certificate;
        }else if(type.equals("vehicle_registration_certificate")){//车辆登记证识别
            url = CommonUtil.vehicle_registration_certificate;
        }else if(type.equals("plate_number")){//车牌号识别
            url = CommonUtil.plate_number;
        }else if(type.equals("vehicle_vin_code")){//车辆VIN码识别
            url = CommonUtil.vehicle_vin_code;
        }else if(type.equals("air_transport_itinerary")){//飞机行程单识别
            url = CommonUtil.air_transport_itinerary;
        }else if(type.equals("vehicle_sales_invoice")){//机动车购车发票识别
            url = CommonUtil.vehicle_sales_invoice;
        }else if(type.equals("quota_invoice")){//定额发票识别
            url = CommonUtil.quota_invoice;
        }else if(type.equals("electr_acceptance_bill")){//电子承兑汇票识别
            url = CommonUtil.electr_acceptance_bill;
        }else if(type.equals("general_machine_invoice")){//通用机打发票识别
            url = CommonUtil.general_machine_invoice;
        }else if(type.equals("toll_fee")){//车辆通行费票据识别
            url = CommonUtil.toll_fee;
        }else if(type.equals("used_vehicle_sales_invoice")){//二手车购车发票识别
            url = CommonUtil.used_vehicle_sales_invoice;
        }else if(type.equals("receipt")){//商铺小票识别
            url = CommonUtil.receipt;
        }else if(type.equals("highway_passenger_invoice")){//公路客运发票识别
            url = CommonUtil.highway_passenger_invoice;
        }else if(type.equals("customs_declaration")){//海关进出口货物报关单识别
            url = CommonUtil.customs_declaration;
        }else if(type.equals("hygiene_license")){//卫生许可证识别
            url = CommonUtil.hygiene_license;
        }else if(type.equals("institution_legal_person")){//事业单位法人证识别
            url = CommonUtil.institution_legal_person;
        }else if(type.equals("hk_mac_exit_entry_permit")){//港澳台通行证识别
            url = CommonUtil.hk_mac_exit_entry_permit;
        }else if(type.equals("taiwan_health_insurance_card")){//台湾健保卡识别
            url = CommonUtil.taiwan_health_insurance_card;
        }else if(type.equals("jpn_driver_license")){//日本驾驶证识别
            url = CommonUtil.jpn_driver_license;
        }else if(type.equals("phl_id_card")){//菲律宾身份证识别
            url = CommonUtil.phl_id_card;
        }else if(type.equals("hk_id_card")){//香港身份证识别
            url = CommonUtil.hk_id_card;
        }else if(type.equals("document")){//办公文档识别
            url = CommonUtil.document;
        }else if(type.equals("birth_certificate")){//出生证明识别
            url = CommonUtil.birth_certificate;
        }else if(type.equals("ps_detection")){//PS检测
            url = CommonUtil.ps_detection;
        }else if(type.equals("real_estate_certificate")){//不动产权证书识别
            url = CommonUtil.real_estate_certificate;
        }else if(type.equals("recognize_stamp")){//印章检测识别
            url = CommonUtil.recognize_stamp;
        }else if(type.equals("bank_receipts")){//银行回执单识别
            url = CommonUtil.bank_receipts;
        }else if(type.equals("vat_invoice")){//增值税发票识别
            url = CommonUtil.vat_invoice;
        }else if(type.equals("hkmctw_residence_permit")){//港澳台居民居住证
            url = CommonUtil.hkmctw_residence_permit;
        } else{
            url = CommonUtil.url;
        }
        byte[] imgData = new byte[0];
        //byte[] imgData = TextInOcrUtil.readfile(imgUrl);
        String md5="";

        if(StringUtils.isNotEmpty(imgUrl)){
               imgData= FileManageUtil.getImgFromUrl(imgUrl);
        }else{
            //base64转byte[]
            if(StringUtils.isNotEmpty(imgBase64)){
                imgData= DatatypeConverter.parseBase64Binary(imgBase64);
            }
        }
        String result = "";
        //获取md5的取值
        md5=TextInOcr.getMd5(imgData);
        OcrCallRecord ocrCallRecordNew=ocrCallRecordManager.queryDbOcrCallRecordByMd5AndInterUrl(md5,interUrl);
        //判断之前是否存储过ocr
        if(null!=ocrCallRecordNew&&StringUtils.isNotEmpty(ocrCallRecordNew.getInterResult())){
            result=ocrCallRecordNew.getInterResult();
            if(StrUtil.isNotEmpty(result)){
                textInOcrResponse = JSONObject.parseObject(result,TextInOcrResponse.class);
            }
        }else {
             result = TextInOcr.textInOcrCertificate(imgData, url);
            if(StrUtil.isNotEmpty(result)){
                textInOcrResponse=TextInOcr.transformOcr(result);
            }
            result= JSONObject.toJSONString(textInOcrResponse);
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
       /* if(StrUtil.isNotEmpty(result)){
            textInOcrResponse=TextInOcr.transformOcr(result);
        }*/
        log.info("TextIn卡证TextInocr结果：" + textInOcrResponse);
        return textInOcrResponse;

    }

    /***
    * @Description:  营业执照的ocr获取
    * @Author:liangss
    * @Date:2021/11/1
    * @Param: [textInOcrRequest]
    */
    public TextInOcrResponse businessLicenseOcr(TextInOcrRequest textInOcrRequest) throws Exception {
        log.info("进入TextIn营业执照ocr识别");
        TextInOcr.appId=appId;
        TextInOcr.secretCode=secretCode;
        TextInOcrResponse textInOcrResponse = new TextInOcrResponse();
        String imgUrl=textInOcrRequest.getImgUrl();
        // 营业执照识别
        String url = CommonUtil.business_license;
        byte[] imgData= FileManageUtil.getImgFromUrl(imgUrl);
        String result=  TextInOcr.textInOcrCertificate(imgData,url);
        if(StrUtil.isNotEmpty(result)){
            textInOcrResponse=TextInOcr.transformOcr(result);
        }
        log.info("TextIn营业执照TextInocr结果：" + textInOcrResponse);
        return textInOcrResponse;

    }

    /***
    * @Description: 身份证的ocr获取
    * @Author:liangss
    * @Date:2021/11/2
    * @Param: [textInOcrRequest]
    */
    public TextInOcrResponse idCardOcr(TextInOcrRequest textInOcrRequest) throws Exception {
        log.info("进入TextIn身份证ocr识别");
        TextInOcrResponse textInOcrResponse = new TextInOcrResponse();
        String imgUrl=textInOcrRequest.getImgUrl();
        // 身份证识别
        String url = CommonUtil.id_card;
        byte[] imgData= FileManageUtil.getImgFromUrl(imgUrl);
        String result=  TextInOcr.textInOcrCertificate(imgData,url);
        if(StrUtil.isNotEmpty(result)){
            textInOcrResponse=TextInOcr.transformOcr(result);
        }
        log.info("TextIn身份证ocr结果：" + textInOcrResponse);
        return textInOcrResponse;


    }

    /***
    * @Description:  户口本的ocr识别
    * @Author:liangss
    * @Date:2021/11/2
    * @Param: [textInOcrRequest]
    */
    public TextInOcrResponse householdRegisterOcr(TextInOcrRequest textInOcrRequest) throws Exception {
        log.info("进入TextIn户口本ocr识别");
        TextInOcr.secretCode=secretCode;
        TextInOcrResponse textInOcrResponse = new TextInOcrResponse();
        String imgUrl=textInOcrRequest.getImgUrl();
        // 身份证识别
        String url = CommonUtil.household_register;
        byte[] imgData= FileManageUtil.getImgFromUrl(imgUrl);
        String result=  TextInOcr.textInOcrCertificate(imgData,url);
        if(StrUtil.isNotEmpty(result)){
            textInOcrResponse=TextInOcr.transformOcr(result);
        }
        log.info("TextIn户口本ocr结果：" + textInOcrResponse);
        return textInOcrResponse;

    }

    /***
    * @Description: TextIn银行卡ocr识别
    * @Author:liangss
    * @Date:2021/11/2
    * @Param: [textInOcrRequest]
    */
    public TextInOcrResponse bankCardrOcr(TextInOcrRequest textInOcrRequest) throws Exception {
        log.info("进入TextIn银行卡ocr识别");
        TextInOcr.appId=appId;
        TextInOcr.secretCode=secretCode;
        TextInOcrResponse textInOcrResponse = new TextInOcrResponse();
        String imgUrl=textInOcrRequest.getImgUrl();
        // 身份证识别
        String url = CommonUtil.bank_card;
        byte[] imgData= FileManageUtil.getImgFromUrl(imgUrl);
        String result=  TextInOcr.textInOcrCertificate(imgData,url);
        if(StrUtil.isNotEmpty(result)){
            textInOcrResponse=TextInOcr.transformOcr(result);
        }
        log.info("TextIn银行卡ocr结果：" + textInOcrResponse);
        return textInOcrResponse;
    }
    /***
    * @Description:  TextIn名片ocr识别
    * @Author:liangss
    * @Date:2021/11/2
    * @Param: [textInOcrRequest]
    */
    public TextInOcrResponse businessCardrOcr(TextInOcrRequest textInOcrRequest) throws Exception {
        log.info("进入TextIn名片ocr识别");
        TextInOcr.appId=appId;
        TextInOcr.secretCode=secretCode;
        TextInOcrResponse textInOcrResponse = new TextInOcrResponse();
        String imgUrl=textInOcrRequest.getImgUrl();
        // 身份证识别
        String url = CommonUtil.business_card;
        byte[] imgData= FileManageUtil.getImgFromUrl(imgUrl);
        String result=  TextInOcr.textInOcrCertificate(imgData,url);
        if(StrUtil.isNotEmpty(result)){
            textInOcrResponse=TextInOcr.transformOcr(result);
        }
        log.info("TextIn名片ocr结果：" + textInOcrResponse);
        return textInOcrResponse;
    }

    /***
    * @Description: TextIn驾驶证ocr识别
    * @Author:liangss
    * @Date:2021/11/2
    * @Param: [textInOcrRequest]
    */
    public TextInOcrResponse driverLicenseOcr(TextInOcrRequest textInOcrRequest) throws Exception {
        log.info("进入TextIn驾驶证ocr识别");
        TextInOcr.appId=appId;
        TextInOcr.secretCode=secretCode;
        TextInOcrResponse textInOcrResponse = new TextInOcrResponse();
        String imgUrl=textInOcrRequest.getImgUrl();
        // 驾驶证识别
        String url = CommonUtil.driver_license;
        byte[] imgData= FileManageUtil.getImgFromUrl(imgUrl);
        String result=  TextInOcr.textInOcrCertificate(imgData,url);
        if(StrUtil.isNotEmpty(result)){
            textInOcrResponse=TextInOcr.transformOcr(result);
        }
        log.info("TextIn驾驶证ocr结果：" + textInOcrResponse);
        return textInOcrResponse;
    }
    /***
    * @Description:TextIn行驶证ocr识别
    * @Author:liangss
    * @Date:2021/11/2
    * @Param: [textInOcrRequest]
    */
    public TextInOcrResponse vehicleLicenseOcr(TextInOcrRequest textInOcrRequest) throws Exception {
        log.info("进入TextIn行驶证ocr识别");
        TextInOcr.appId=appId;
        TextInOcr.secretCode=secretCode;
        TextInOcrResponse textInOcrResponse = new TextInOcrResponse();
        String imgUrl=textInOcrRequest.getImgUrl();
        // 驾驶证识别
        String url = CommonUtil.vehicle_license;
        byte[] imgData= FileManageUtil.getImgFromUrl(imgUrl);
        String result=  TextInOcr.textInOcrCertificate(imgData,url);
        if(StrUtil.isNotEmpty(result)){
            textInOcrResponse=TextInOcr.transformOcr(result);
        }
        log.info("TextIn行驶证ocr结果：" + textInOcrResponse);
        return textInOcrResponse;
    }
    /***
    * @Description: TextIn国内通用票据识别ocr识别
    * @Author:liangss
    * @Date:2021/11/2
    * @Param: [textInOcrRequest]
    */
    public TextInOcrResponse billsCropOcr(TextInOcrRequest textInOcrRequest) throws Exception {
        log.info("进入TextIn国内通用票据识别ocr识别");
        TextInOcr.appId=appId;
        TextInOcr.secretCode=secretCode;
        TextInOcrResponse textInOcrResponse = new TextInOcrResponse();
        String imgUrl=textInOcrRequest.getImgUrl();
        // 驾驶证识别
        String url = CommonUtil.bills_crop;
        byte[] imgData= FileManageUtil.getImgFromUrl(imgUrl);
        String result=  TextInOcr.textInOcrCertificate(imgData,url);
        if(StrUtil.isNotEmpty(result)){
            textInOcrResponse=TextInOcr.transformOcr(result);
        }
        log.info("TextIn国内通用票据识别ocr结果：" + textInOcrResponse);
        return textInOcrResponse;
    }
    /***
    * @Description:TextIn火车票识别ocr识别
    * @Author:liangss
    * @Date:2021/11/2
    * @Param: [textInOcrRequest]
    */
    public TextInOcrResponse trainTicketOcr(TextInOcrRequest textInOcrRequest) throws Exception {
        log.info("进入TextIn火车票识别ocr识别");
        TextInOcr.appId=appId;
        TextInOcr.secretCode=secretCode;
        TextInOcrResponse textInOcrResponse = new TextInOcrResponse();
        String imgUrl=textInOcrRequest.getImgUrl();
        // 驾驶证识别
        String url = CommonUtil.train_ticket;
        byte[] imgData= FileManageUtil.getImgFromUrl(imgUrl);
        String result=  TextInOcr.textInOcrCertificate(imgData,url);
        if(StrUtil.isNotEmpty(result)){
            textInOcrResponse=TextInOcr.transformOcr(result);
        }
        log.info("TextIn火车票识别ocr结果：" + textInOcrResponse);
        return textInOcrResponse;
    }

    /***
    * @Description:TextIn社保卡识别ocr识别
    * @Author:liangss
    * @Date:2021/11/2
    * @Param: [textInOcrRequest]
    */
    public TextInOcrResponse socialSecurityCardOcr(TextInOcrRequest textInOcrRequest) throws Exception {
        log.info("进入TextIn社保卡识别ocr识别");
        TextInOcr.appId=appId;
        TextInOcr.secretCode=secretCode;
        TextInOcrResponse textInOcrResponse = new TextInOcrResponse();
        String imgUrl=textInOcrRequest.getImgUrl();
        // 驾驶证识别
        String url = CommonUtil.social_security_card;
        byte[] imgData= FileManageUtil.getImgFromUrl(imgUrl);
        String result=  TextInOcr.textInOcrCertificate(imgData,url);
        if(StrUtil.isNotEmpty(result)){
            textInOcrResponse=TextInOcr.transformOcr(result);
        }
        log.info("TextIn社保卡识别ocr结果：" + textInOcrResponse);
        return textInOcrResponse;
    }

    /***
    * @Description:  TextIn护照识别ocr识别
    * @Author:liangss
    * @Date:2021/11/2
    * @Param: [textInOcrRequest]
    */
    public TextInOcrResponse passportOcr(TextInOcrRequest textInOcrRequest) throws Exception {
        log.info("进入TextIn护照识别ocr识别");
        TextInOcr.appId=appId;
        TextInOcr.secretCode=secretCode;
        TextInOcrResponse textInOcrResponse = new TextInOcrResponse();
        String imgUrl=textInOcrRequest.getImgUrl();
        // 驾驶证识别
        String url = CommonUtil.passport;
        byte[] imgData= FileManageUtil.getImgFromUrl(imgUrl);
        String result=  TextInOcr.textInOcrCertificate(imgData,url);
        if(StrUtil.isNotEmpty(result)){
            textInOcrResponse=TextInOcr.transformOcr(result);
        }
        log.info("TextIn护照识别ocr结果：" + textInOcrResponse);
        return textInOcrResponse;
    }

}
