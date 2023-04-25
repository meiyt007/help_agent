package com.zfsoft.superwindow.util.fa;

import java.util.HashMap;
import java.util.Map;

public class FrontBaseStaticParameter {

    /** 服务对象类型 MAP */
    public static final Map<String, String> SERVICE_OBJECT_TYPE_MAP = new HashMap<String, String>();
    /** 服务对象类型-个人 */
    public static final String PERSON = "1";
    /** 服务对象类型-法人 */
    public static final String LEGAL_PERSON = "2";
    /** 首页显示数量 */
    public static final Integer INDEX_SHOW_NUMBER = 20;
    
    /** 事项类型 MAP */
    public static final Map<String, String> SERVICE_TYPE_MAP = new HashMap<String, String>();
    /** 服务对象类型-审批 */
    public static final String SERVICE_TYPE_EXAMINATION = "1";
    /** 服务对象类型-服务 */
    public static final String SERVICE_TYPE_SERVICE = "2";
    /** 其他 */
    public static final String SERVICE_TYPE_OTHER = "3";

    /** 智能填表记录主键*/
    public static final String INTELLIGENT_FORM_REC_INFO = "INTELLIGENT_FORM_REC_INFO";
    
    /** 登录的办件用户*/
    public static final String LOGIN_AUTH_INFO = "LOGIN_AUTH_INFO";
    
    /** 待绑定办件实人认证信息列表   List<PersonAuthInfo> */
    public static final String PERSON_AUTH_INFO_LIST = "PERSON_AUTH_INFO_LIST";
    
    /** 待绑定材料目录图片识别结果信息   FaMaterialPicOcrResult */
    public static final String BUSINESS_LICE_FA_OCR_RESULT = "BUSINESS_LICE_FA_OCR_RESULT";
    
    /** 待绑定材料识别记录表   OcrRecord */
    public static final String BUSINESS_LICE_OCR_RECORD = "BUSINESS_LICE_OCR_RECORD";
    
    
    /** 区划级别levelDictOid */
    /**
     * 省、自治区、直辖市、特别行政区
     */
    public static final String LEVEL_DICT_OID_PROVINCE = "402881fa56e8b41c0156e8ba5e2a0009";
    /**
     * 地级市、自治州、地区、盟
     */
    public static final String LEVEL_DICT_OID_CITY = "402881fa56e8b41c0156e8ba5e33000a";
    /**
     * 县、市辖区、县级市、旗
     */
    public static final String LEVEL_DICT_OID_COUNTY = "402881fa56e8b41c0156e8ba5e46000c";
    /**
     * 镇、乡、街道办事处、民族乡、区公所
     */
    public static final String LEVEL_DICT_OID_TOWN = "40288008594a6e7401594d56b2150000";
    /**
     * 自然村、居委会、社区
     */
    public static final String LEVEL_DICT_OID_VILLAGE = "40288008594a6e7401594d5c17080001";
    /**
     * 区划json
     */
    public static String DISTRICT_JSON = "districtJson";
    /**
     * 选项OID集合
     */
    public static String OPTION_OIDS = "optionOids";
    /**
     * 办件信息主键
     */
    public static String CASE_OID = "caseOid";
    /**
     * 当前区划
     */
    public static String CUR_SYS_DISTRICT = "curSysDistrict";
    /**
     * 叫号标识
     */
    public static final String CALL_SIGN = "callSign";
    /**
     * 证件类型
     */
    public static final Map<String, String> LICENSE_TYPE_MAP = new HashMap<String, String>();
    
    /**
     * 证件类型 身份证
     */
    public static final String LICENSE_TYPE_ID_CARD = "1";
    /**
     * 取证方式
     */
    public static final Map<String, String> TAKE_CERTIFICATE_TYPE_MAP = new HashMap<String, String>();
    /**
     * 取证方式 自助取件
     */
    public static final String TAKE_CERTIFICATE_TYPE_SELF = "1";
    /**
     * 取证方式 快递送达
     */
    public static final String TAKE_CERTIFICATE_TYPE_EXPRESS = "2";
    /** 智能填表状态 */

    /** 未完成 */
    public static final String INTELLIGENT_FORM_STAGE_0 = "0";
    /** 完成 */
    public static final String INTELLIGENT_FORM_STAGE_1 = "1";

    /** 办件状态 */
    public static Map<String, String> CASE_STATUS = new HashMap<String, String>();
    /** 暂存 */
    public static final String CASE_STATUS_ZC = "0";
    /** 已申报 */
    public static final String CASE_STATUS_YSB = "1";
    /** 办理中 */
    public static final String CASE_STATUS_BLZ = "2";
    /** 已办结 */
    public static final String CASE_STATUS_YBJ = "3";
    /** 不通过 */
    public static final String CASE_STATUS_BTG = "4";
    /** 补齐补正*/
    public static final String CASE_STATUS_BQBZ = "5";
    /** 预审通过 */
    public static final String CASE_STATUS_YSTG = "6";
    /** 不予受理 */
    public static final String CASE_STATUS_BYSL = "7";
    /** 撤销申报 */
    public static final String CASE_STATUS_CXSB = "8";
    /** 时间年月日时分秒*/
    public static final String CUR_TIME_FORMAT = "yyyyMMddHHmmss";
    /** 热敏打印机打印成功*/
    public static final String EP_HOT_PRINTER_PRINT_SUCCESS = "打印成功";
    /** 热敏打印打开失败*/
    public static final String EP_HOT_PRINTER_OPEN_FAIL = "热敏打印打开失败";
    /** 热敏打印关闭失败*/
    public static final String EP_HOT_PRINTER_CLOSE_FAIL = "热敏打印关闭失败";
    /** 热敏打印打开失败*/
    public static final String EP_HIGHT_OPEN_FAIL = "高拍仪打开失败";
    
    /** 材料来源 MAP*/
    public static Map<String, String> FILE_SOURCE_TYPE_MAP = new HashMap<String, String>();
    /** 材料来源 高拍仪拍照*/
    public static final String FILE_SOURCE_TYPE_HIGH_PHOTO = "1";
    /** 材料来源 个人空间*/
    public static final String FILE_SOURCE_TYPE_PERSONAL_FILE = "2";
    /** 材料来源 手机扫描上传*/
    public static final String FILE_SOURCE_TYPE_TELEPHONE_SCAN_FILE = "3";
    /** 材料来源 动态表单上传*/
    public static final String FILE_SOURCE_TYPE_FROM_FILE = "4";
    /**
     * 电子证照
     */
    public static final String FILE_SOURCE_TYPE_FROM_ELEC = "5";
    /**
     * 打开方式
     */
    public static final Map<String, String> OPEN_MODE_MAP = new HashMap<String, String>();
    /**
     * 打开方式 NB是内部 WB是外部
     */
    public static final String OPEN_MODE_NB = "NB";
    public static final String OPEN_MODE_WB = "WB";
    
    /**
     * 预审结果状态map
     */
    public static final Map<String, String> PRE_TRIAL_RESULT_STATUS_MAP = new HashMap<String, String>();
    /**  预审结果标识 0:通过 */
    public static final String PRE_TRIAL_RESULT_STATUS_PASS = "0";
    /**  预审结果标识  1:不通过 */
    public static final String PRE_TRIAL_RESULT_STATUS_NOT_PASS = "1";
    /**  预审结果标识  2:无需审核*/
    public static final String PRE_TRIAL_RESULT_STATUS_NOT_NEED = "2";
    /**  屏幕通信-动画服务*/
    public static final String SCREEN_MESSAGE_CARTOON_SERVICE = "cartoonService";
    /**  屏幕通信-打开语音服务*/
    public static final String SCREEN_MESSAGE_SPEECH_SERVICE = "speechService";
    /**  屏幕通信-人脸服务*/
    public static final String SCREEN_MESSAGE_LIVING_CAM_SERVICE = "livingCamService";
    /**  屏幕通信-人脸确认拍照*/
    public static final String SCREEN_MESSAGE_LIVING_CAM_AFFIRM = "livingCamAffirm";
    /**  屏幕通信-人脸确认活体*/
    public static final String SCREEN_MESSAGE_LIVING_CAM_ALIVE = "livingCamAlive";
    /**  屏幕通信-类型*/
    public static final String SCREEN_MESSAGE_TYPE = "type";
    /**  屏幕通信-内容*/
    public static final String SCREEN_MESSAGE_DATA = "data";
    /**  屏幕通信-拍照*/
    public static final String SCREEN_MESSAGE_TAKE_PHOTO = "takePhoto";
    /**  屏幕通信-活体检测*/
    public static final String SCREEN_MESSAGE_ALIVE = "alive";
    /**  屏幕通信-打开动画*/
//    public static final String SCREEN_MESSAGE_CARTOON = "cartoon";
    /** 耗材名称集合 MAP */
    public static final Map<String, String> CONSUME_NAME_MAP = new HashMap<String, String>();
    /**  打印机*/
    public static final String CONSUME_NAME_DYJ = "print_machine";
    /**  制卡机*/
    public static final String CONSUME_NAME_ZKJ = "card_machine";
    /**  墨盒*/
    public static final String CONSUME_NAME_MH = "ink_box";
    /**  就医记录册*/
    public static final String CONSUME_NAME_MEDICAL_BOOK = "medical_book";
    /**  就医记录册墨盒*/
    public static final String CONSUME_NAME_MEDICAL_BOOK_INK = "book_ink_box";
    /**  统计本年*/
    public static final String CONSUME_PRINT_STATI_YEAR = "year";
    /**  统计本季*/
    public static final String CONSUME_PRINT_STATI_QUARTER = "quarter";
    /**  统计本月*/
    public static final String CONSUME_PRINT_STATI_MONTH = "month";
    /**  统计本日*/
    public static final String CONSUME_PRINT_STATI_DAY = "day";
    
    static {

        SERVICE_OBJECT_TYPE_MAP.put(PERSON, "个人");
        SERVICE_OBJECT_TYPE_MAP.put(LEGAL_PERSON, "法人");
        
        LICENSE_TYPE_MAP.put(LICENSE_TYPE_ID_CARD, "身份证");
        
        TAKE_CERTIFICATE_TYPE_MAP.put(TAKE_CERTIFICATE_TYPE_SELF, "自助取件");
        TAKE_CERTIFICATE_TYPE_MAP.put(TAKE_CERTIFICATE_TYPE_EXPRESS, "快递送达");
        
        CASE_STATUS.put(CASE_STATUS_ZC, "暂存");
        CASE_STATUS.put(CASE_STATUS_YSB, "已申报");
        CASE_STATUS.put(CASE_STATUS_BLZ, "办理中");
        CASE_STATUS.put(CASE_STATUS_YBJ, "已办结");
        CASE_STATUS.put(CASE_STATUS_BTG, "不通过");
        CASE_STATUS.put(CASE_STATUS_BQBZ, "补齐补正");
        CASE_STATUS.put(CASE_STATUS_YSTG, "预审通过");
        CASE_STATUS.put(CASE_STATUS_BYSL, "不予受理");
        CASE_STATUS.put(CASE_STATUS_CXSB, "撤销申报");
        
        FILE_SOURCE_TYPE_MAP.put(FILE_SOURCE_TYPE_HIGH_PHOTO, "高拍仪拍照");
        FILE_SOURCE_TYPE_MAP.put(FILE_SOURCE_TYPE_PERSONAL_FILE, "个人空间");
        FILE_SOURCE_TYPE_MAP.put(FILE_SOURCE_TYPE_TELEPHONE_SCAN_FILE, "扫描上传");
        FILE_SOURCE_TYPE_MAP.put(FILE_SOURCE_TYPE_FROM_FILE, "动态表单上传");
        FILE_SOURCE_TYPE_MAP.put(FILE_SOURCE_TYPE_FROM_ELEC, "电子证照上传");

        OPEN_MODE_MAP.put(OPEN_MODE_NB, "内部");
        OPEN_MODE_MAP.put(OPEN_MODE_WB, "外部");
        
        PRE_TRIAL_RESULT_STATUS_MAP.put(PRE_TRIAL_RESULT_STATUS_PASS, "审核通过");
        PRE_TRIAL_RESULT_STATUS_MAP.put(PRE_TRIAL_RESULT_STATUS_NOT_PASS, "审核不通过");
        PRE_TRIAL_RESULT_STATUS_MAP.put(PRE_TRIAL_RESULT_STATUS_NOT_NEED, "无需审核");

        CONSUME_NAME_MAP.put(CONSUME_NAME_DYJ, "纸张");
        CONSUME_NAME_MAP.put(CONSUME_NAME_ZKJ, "卡片");
        CONSUME_NAME_MAP.put(CONSUME_NAME_MH, "墨盒");
        CONSUME_NAME_MAP.put(CONSUME_NAME_MEDICAL_BOOK, "就医记录册");
        CONSUME_NAME_MAP.put(CONSUME_NAME_MEDICAL_BOOK_INK, "就医记录册墨盒");
    }
    
}
