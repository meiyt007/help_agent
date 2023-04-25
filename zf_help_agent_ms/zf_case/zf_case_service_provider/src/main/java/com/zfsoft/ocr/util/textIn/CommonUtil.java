package com.zfsoft.ocr.util.textIn;

/***
* @Description:TextIn通用使用参数
* @Author:liangss
* @Date:2021/11/1
* @Param:
*/
public class CommonUtil {

    //String url = "https://api.textin.com/robot/v1.0/api/387808e16375162e3e2e291b8bbea562";

    //地址
    public static final  String url = "https://api.textin.com/robot/v1.0/api/";

    //通用文字识别
    public static final  String tywzsb_url = "https://api.textin.com/robot/v1.0/api/text_recognize_3d1";

    // 请登录后前往 “工作台-账号设置-开发者信息” 查找 x-ti-app-id，示例代码中 x-ti-app-id 非真实数据
   // public static final String appId = "2c8b8df4cdc023139a0ecb7c785ac99b";
    // 请登录后前往 “工作台-账号设置-开发者信息” 查找 x-ti-secret-code，示例代码中 x-ti-secret-code 非真实数据
    //public static final String secretCode = "428f09d3f5c99855f3c83f14c24998af";

    // 营业执照识别
    public static final String business_license = "https://api.textin.com/robot/v1.0/api/business_license";
    // 身份证识别
    public static final String id_card = "https://api.textin.com/robot/v1.0/api/id_card";
    // 户口本识别
    public static final String household_register = "https://api.textin.com/robot/v1.0/api/household_register";
    //银行卡
    public static final String bank_card = "https://api.textin.com/robot/v1.0/api/bank_card";
    //社保卡识别
    public static final String social_security_card = "https://api.textin.com/robot/v1.0/api/social_security_card";
    //护照识别
    public static final String passport = "https://api.textin.com/robot/v1.0/api/passport";
    //驾驶证
    public static final String driver_license = "https://api.textin.com/robot/v1.0/api/driver_license";
    //行驶证
    public static final String vehicle_license = "https://api.textin.com/robot/v1.0/api/vehicle_license";

    //港澳台通行证识别
    public static final String hk_mac_exit_entry_permit = "https://api.textin.com/robot/v1.0/api/hk_mac_exit_entry_permit";
    //出生证明识别
    public static final String birth_certificate = "https://api.textin.com/robot/v1.0/api/birth_certificate";
    //税务登记证识别
    public static final String tax_registration_certificate = "https://api.textin.com/robot/v1.0/api/tax_registration_certificate";
    //组织机构代码证识别
    public static final String organization_code_certificate = "https://api.textin.com/robot/v1.0/api/organization_code_certificate";
    //开户许可证识别
    public static final String account_opening_permit = "https://api.textin.com/robot/v1.0/api/account_opening_permit";
    //澳门身份证识别
    public static final String mac_id_card = "https://api.textin.com/robot/v1.0/api/mac_id_card";
    //港澳居民来往内地通行证
    public static final String hk_mac_mainland_travel_permit = "https://api.textin.com/robot/v1.0/api/hk_mac_mainland_travel_permit";
    //结婚证识别
    public static final String marriage_certificate = "https://api.textin.com/robot/v1.0/api/marriage_certificate";
    //房产证识别
    public static final String property_certificate = "https://api.textin.com/robot/v1.0/api/property_certificate";
    //军官证识别
    public static final String military_id_card = "https://api.textin.com/robot/v1.0/api/military_id_card";
    //车辆合格证识别
    public static final String vehicle_inspection_certificate = "https://api.textin.com/robot/v1.0/api/vehicle_inspection_certificate";
    //车辆登记证识别
    public static final String vehicle_registration_certificate = "https://api.textin.com/robot/v1.0/api/vehicle_registration_certificate";
    //车牌号识别
    public static final String plate_number = "https://api.textin.com/robot/v1.0/api/plate_number";
    //车辆VIN码识别
    public static final String vehicle_vin_code = "https://api.textin.com/robot/v1.0/api/vehicle_vin_code";
    //卫生许可证识别
    public static final String hygiene_license = "https://api.textin.com/robot/v1.0/api/hygiene_license";
    //事业单位法人证识别
    public static final String institution_legal_person = "https://api.textin.com/robot/v1.0/api/institution_legal_person";
    //不动产权证书识别
    public static final String real_estate_certificate = "https://api.textin.com/robot/v1.0/api/real_estate_certificate";
    //港澳台居民居住证
    public static final String hkmctw_residence_permit = "https://api.textin.com/robot/v1.0/api/hkmctw_residence_permit";




    //名片
    public static final String business_card = "https://api.textin.com/robot/v1.0/api/business_card";
    //台湾健保卡识别
    public static final String taiwan_health_insurance_card = "https://api.textin.com/robot/v1.0/api/taiwan_health_insurance_card";
    //香港身份证识别
    public static final String hk_id_card = "https://api.textin.com/robot/v1.0/api/hk_id_card";
    //国内通用票据识别
    public static final String bills_crop = "https://api.textin.com/robot/v1.0/api/bills_crop";
    //火车票识别
    public static final String train_ticket = "https://api.textin.com/robot/v1.0/api/train_ticket";
    //出租车发票识别
    public static final String taxi_invoice = "https://api.textin.com/robot/v1.0/api/taxi_invoice";
    //卷式发票识别
    public static final String roll_invoice = "https://api.textin.com/robot/v1.0/api/roll_invoice";
    //印尼身份证识别
    public static final String idn_id_card = "https://api.textin.com/robot/v1.0/api/idn_id_card";
    //马来西亚身份证识别
    public static final String malaysia_id_card = "https://api.textin.com/robot/v1.0/api/malaysia_id_card";
    //飞机行程单识别
    public static final String air_transport_itinerary = "https://api.textin.com/robot/v1.0/api/air_transport_itinerary";
    //机动车购车发票识别
    public static final String vehicle_sales_invoice = "https://api.textin.com/robot/v1.0/api/vehicle_sales_invoice";
    //定额发票识别
    public static final String quota_invoice = "https://api.textin.com/robot/v1.0/api/quota_invoice";
    //电子承兑汇票识别
    public static final String electr_acceptance_bill = "https://api.textin.com/robot/v1.0/api/electr_acceptance_bill";
    //通用机打发票识别
    public static final String general_machine_invoice = "https://api.textin.com/robot/v1.0/api/general_machine_invoice";
    //车辆通行费票据识别
    public static final String toll_fee = "https://api.textin.com/robot/v1.0/api/toll_fee";
    //二手车购车发票识别
    public static final String used_vehicle_sales_invoice = "https://api.textin.com/robot/v1.0/api/used_vehicle_sales_invoice";
    //商铺小票识别
    public static final String receipt = "https://api.textin.com/robot/v1.0/api/receipt";
    //公路客运发票识别
    public static final String highway_passenger_invoice = "https://api.textin.com/robot/v1.0/api/highway_passenger_invoice";
    //海关进出口货物报关单识别
    public static final String customs_declaration = "https://api.textin.com/robot/v1.0/api/customs_declaration";
    //日本驾驶证识别
    public static final String jpn_driver_license = "https://api.textin.com/robot/v1.0/api/jpn_driver_license";
    //菲律宾身份证识别
    public static final String phl_id_card = "https://api.textin.com/robot/v1.0/api/phl_id_card";
    //办公文档识别
    public static final String document = "https://api.textin.com/ai/service/v2/recognize/document";
    //PS检测
    public static final String ps_detection = "https://api.textin.com/robot/v1.0/api/ps_detection";
    //印章检测识别
    public static final String recognize_stamp = "https://api.textin.com/robot/v1.0/api/recognize_stamp";
    //银行回执单识别
    public static final String bank_receipts = "https://api.textin.com/robot/v1.0/api/bank_receipts";
    //增值税发票识别
    public static final String vat_invoice = "https://api.textin.com/robot/v1.0/api/vat_invoice";



}
