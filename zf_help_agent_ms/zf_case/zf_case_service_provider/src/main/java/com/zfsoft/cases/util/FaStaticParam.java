package com.zfsoft.cases.util;

import com.zfsoft.platform.common.data.BaseStaticParameter;

import java.util.*;

/**
 * @author: liangss
 * @create: 2020-11-05 19:28:42
 * @description: 材料审核平台-公共静态变量类
 */
public class FaStaticParam {
    private FaStaticParam() {
    }
    /*  public static final String CODE = "code";*/
    /** 待绑定材料目录图片识别结果信息   FaMaterialPicOcrResult */
    public static final String BUSINESS_LICE_FA_OCR_RESULT = "BUSINESS_LICE_FA_OCR_RESULT";
    /** 待绑定材料识别记录表   OcrRecord */
    public static final String BUSINESS_LICE_OCR_RECORD = "BUSINESS_LICE_OCR_RECORD";
    /** 成功 */
    public static final String HTTP_REQUEST_CODE_SUCCESS = "200";
    public static final String HTTP_REQUEST_CODE_SUCCESS_MESSAGE = "成功";
    /**  预审结果标识 0:通过 */
    public static final String PRE_TRIAL_RESULT_STATUS_PASS = "0";
    /**  预审结果标识  1:不通过 */
    public static final String PRE_TRIAL_RESULT_STATUS_NOT_PASS = "1";
    /**  预审结果标识  2:无需审核*/
    public static final String PRE_TRIAL_RESULT_STATUS_NOT_NEED = "2";
    /**  预审结果标识  3:无验证规则*/
    public static final String PRE_TRIAL_RESULT_STATUS_NOT_RULE = "3";

    /**
     * properties 加密的盐值
     */
    public static final String PROPERTIES_SALT = "DFIJCPO6";

    /** 验证类型集合 */
    public static final Map<String, String> VALIDATION_TYPE = new LinkedHashMap<String, String>();
    /** 不能为空，内容不能为空 */
    public static final String VALIDATION_TYPE_NOTNULL = "NOTNULL";
    /** 必须为空，内容必须为空 */
    public static final String VALIDATION_TYPE_NULL = "NULL";
    /** 内容验证，内容必须为什么文字 */
    public static final String VALIDATION_TYPE_CONTENT = "CONTENT";
    /** 内容验证，内容中必须包含什么文字 */
    public static final String VALIDATION_TYPE_STR_CONTAIN = "STR_CONTAINS";
    /** 内容验证，数字必须在某个范围内 */
    public static final String VALIDATION_TYPE_RANGE = "RANGE";
    /** 内容验证，日期比较 */
    public static final String VALIDATION_TYPE_DATE_COMPARE = "DATE_COMPARE";
    /** 内容验证，手写验证 */
    public static final String VALIDATION_TYPE_HAND_SIGN = "HAND_SIGN";
    /** 文字内容比对验证，两个区域中文字必须相同 */
    public static final String VALIDATION_TYPE_CONTENT_THAN = "CONTENT_THAN";
    /** 相似度验证，两个区域的相识度必须超过多少 */
    public static final String VALIDATION_TYPE_SIMILARITY = "SIMILARITY";
    /** 特征块，相似度 */
    public static final double TRAIT_SIMILARITY = 40;

    /**
     * 外部服务查看验证私钥
     */
    public static final String SER_PRO_ACCOUNT_PRIVATE_KEY = "SER_PRO_ACCOUNT_PRIVATE_KEY";

    /** 支持的图片扩展名集合 */
    public static final Set<String> PIC_EXT_SET = new HashSet<String>();
    /** JPG图片 */
    public static final String PIC_EXT_JPG = "JPG";
    /** JPEG图片 */
    public static final String PIC_EXT_JPEG = "JPEG";
    /** PNG图片 */
    public static final String PIC_EXT_PNG = "PNG";
    /** BMP图片 */
    public static final String PIC_EXT_BMP = "BMP";
    /** 编制状态 */
    public static final Map<String, String> COMPILE_STATUS = new LinkedHashMap<String, String>();
    /** 编制状态，未编制 */
    public static final String COMPILE_STATUS_NO = "0";
    /** 编制状态，已编制 */
    public static final String COMPILE_STATUS_YES = "1";

    /** 成功 */
    public static final String INTER_STATUS_CODE_SUCCESS = "200";
    /** 参数异常 */
    public static final String INTER_STATUS_CODE_PARAM_EXCEPTION = "201";

    public static final String INTER_STATUS_CODE_NOT_RULE = "207";
    /** 授权信息异常 */
    public static final String INTER_STATUS_CODE_AUTHOR_EXCEPTION = "202";
    /** 文件图片异常 */
    public static final String INTER_STATUS_CODE_FILE_EXCEPTION = "203";
    /** 接口未授权 */
    public static final String INTER_STATUS_CODE_NOT_ACCREDIT = "204";
    /** 授权应用被禁用 */
    public static final String INTER_STATUS_ACCREDIT_NOT_ABLE = "205";
    /** 其他 */
    public static final String INTER_STATUS_CODE_OTHER = "300";
    /** secretId不正确 */
    public static final String INTER_STATUS_CODE_UNKNOWN_CLIENT_ID = "301";
    /** secretKey不正确 */
    public static final String INTER_STATUS_CODE_CLIENT_AUTH_FAILED = "302";
    /** QPS上限 */
    public static final String INTER_STATUS_CODE_QPS_TOPLIMIT = "303";

    /** 当前项目部署路径 */
    public static final String PROJECT_PATH;

    public static final Map<String, String> RECOGNITION_TYPE = new LinkedHashMap<String, String>();
    /** 区块识别类型-印刷 */
    public static final String RECOGNITION_TYPE_YS = "1";
    /** 区块识别类型-手写 */
    public static final String RECOGNITION_TYPE_SX = "2";
    /** 区块识别类型-手写（单行） */
    public static final String RECOGNITION_TYPE_SX_SINGLE = "7";
    /** 区块识别类型-图像：不需要识别 */
    public static final String RECOGNITION_TYPE_TX = "3";
    /** 区块识别类型-特征块：不需要识别 */
    public static final String RECOGNITION_TYPE_TZ = "4";
    /** 区块识别类型-选择框（印刷） */
    public static final String RECOGNITION_TYPE_MARQUEE_YS = "5";
    /** 区块识别类型-选择框（手写） */
    public static final String RECOGNITION_TYPE_MARQUEE_SX = "6";

    /** 角度类型 */
    public static final Map<String, String> ANGLE_TYPE = new HashMap<String, String>();
    /** 角度类型-直角 */
    public static final String ANGLE_TYPE_RIGHT = "1";
    /** 角度类型-圆角 */
    public static final String ANGLE_TYPE_ROUNDED = "2";

    /** 使用AES生成accessToken */
    public static final String AES_KEY_TOKEN = "ACCESS_TOKEN_AES";

    /** 授权应用 */
    public static final String ACCREDIT_OBJECT = "ACCREDIT_OBJECT";

    /** 授权控制 */
    public static final String ACCREDIT_CONFIG = "ACCREDIT_CONFIG";

    /** 外部服务MAP */
    public static final String SER_PRO_ACCOUNT_MAP = "SER_PRO_ACCOUNT_MAP";

    /** 上传文件类型 - 识别模板底图 */
    public static final String FILE_TYPE_1 = "1";
    /**
     * 通用是否集合 Y：是 N：否
     */
    public static Map<String, String> WHETHER_MAP = new HashMap<String, String>();

    static {
        String pp = FaStaticParam.class.getResource("/").toString();
        // 去掉最后16位，即：/WEB-INF/classes
        if (pp.indexOf("file:/") == 0) {
            pp = pp.substring(6, pp.length() - 16);
        } else {
            pp = pp.substring(0, pp.length() - 16);
        }
        PROJECT_PATH = pp;

        WHETHER_MAP.put(BaseStaticParameter.YES, "是");
        WHETHER_MAP.put(BaseStaticParameter.NO, "否");

        RECOGNITION_TYPE.put(RECOGNITION_TYPE_YS, "印刷");
        RECOGNITION_TYPE.put(RECOGNITION_TYPE_SX, "手写");
        RECOGNITION_TYPE.put(RECOGNITION_TYPE_SX_SINGLE, "手写（单行）");
        RECOGNITION_TYPE.put(RECOGNITION_TYPE_TX, "图像");
        RECOGNITION_TYPE.put(RECOGNITION_TYPE_MARQUEE_YS, "选择框（印刷）");
        RECOGNITION_TYPE.put(RECOGNITION_TYPE_MARQUEE_SX, "选择框（手写）");

        ANGLE_TYPE.put(ANGLE_TYPE_RIGHT, "直角");
        ANGLE_TYPE.put(ANGLE_TYPE_ROUNDED, "圆角");

        COMPILE_STATUS.put(COMPILE_STATUS_NO, "未编制");
        COMPILE_STATUS.put(COMPILE_STATUS_YES, "已编制");

        PIC_EXT_SET.add(PIC_EXT_PNG);
        PIC_EXT_SET.add(PIC_EXT_JPEG);
        PIC_EXT_SET.add(PIC_EXT_JPG);
        PIC_EXT_SET.add(PIC_EXT_BMP);

        VALIDATION_TYPE.put(VALIDATION_TYPE_NOTNULL, "不能为空");
        VALIDATION_TYPE.put(VALIDATION_TYPE_NULL, "必须为空");
        VALIDATION_TYPE.put(VALIDATION_TYPE_CONTENT, "文本验证");
        VALIDATION_TYPE.put(VALIDATION_TYPE_STR_CONTAIN, "文本包含");
        VALIDATION_TYPE.put(VALIDATION_TYPE_RANGE, "数字范围");
        VALIDATION_TYPE.put(VALIDATION_TYPE_HAND_SIGN, "手写验证");
        VALIDATION_TYPE.put(VALIDATION_TYPE_DATE_COMPARE, "日期比较");
        VALIDATION_TYPE.put(VALIDATION_TYPE_CONTENT_THAN, "关联对比");
        VALIDATION_TYPE.put(VALIDATION_TYPE_SIMILARITY, "相似度");
    }

    /**
     * 接口类别
     */
    public static final String INTF_TYPE_BASIC = "1";
    public static final String INTF_TYPE_EXTEND = "2";

    /**
     * 真伪验证接口类别
     */
    public static final String VALIDATE_TYPE_POPULATION = "0";
    public static final String VALIDATE_TYPE_LEGAL = "1";
    public static final String VALIDATE_TYPE_LICENCE = "2";

    /**
     * 外部服务类型字典code
     */
    public static final String WBFWLX_DICT_CODE = "WBFWLX";
    /** 接口类型 */
    public static final Map<String, String> INTER_TYPE = new HashMap<String, String>();

    /** 接口类别 */
    public static final Map<String, String> INTF_CATEGORY = new HashMap<String, String>();

    /** 真伪验证接口类别 */
    public static final Map<String, String> VALIDATE_TYPE_MAP = new HashMap<String, String>();

    /**
     * OCR帐号类型
     */
    public final static Map<String, String> OCR_LOGIN_TYPE_MAY = new HashMap<String, String>();

    /**
     * OCR帐号类型-百度
     */
    public final static String OCR_LOGIN_TYPE_BD = "1";

    /**
     * OCR帐号类型-腾讯
     */
    public final static String OCR_LOGIN_TYPE_TX = "2";

    /**
     * OCR帐号-百度类型OID
     */
    public final static String BD_OID = "402881b1614acabc01614adda5bb0001";

    /**
     * OCR帐号-腾讯类型OID
     */
    public final static String TX_OID = "402881b1614acabc01614adde9390002";

    /**
     * OCR帐号-综合证照验证服务类型OID
     */
    public final static String ELE_LICENCE_OID = "402881b16172f4ec01617355d20c0001";

    /** 接口分类：1-识别验证接口 */
    public final static String INTF_CATEGORY_OCR = "1";
    /** 接口分类：2-基础数据接口 */
    public final static String INTF_CATEGORY_BASEDATA = "2";
    /** 接口分类：3-真伪验证接口 */
    public final static String INTF_CATEGORY_VALI = "3";

    /** 获取accessToken接口 */
    public final static String INTF_CODE_TOKEN = "00000000000809615119";

    /** 指定区块内容识别接口编号 */
    public final static String INTF_CODE_SINGLE_BLOCK_RECG = "00000000000809615769";
    /** 指定区块内容是否为空验证接口编号 */
    public final static String INTF_CODE_SINGLE_BLOCK_ISNULL = "00000000000809615759";
    /** 根据模板编号获取区块信息接口编号 */
    public final static String INTF_CODE_BLOCK_RECG_BY_CATACODE = "00000000000809615749";
    /** 指定区块识别结果是否为指定字符串接口编号 */
    public final static String INTF_CODE_BLOCK_RECG_IS_TEXT = "00000000000809615789";
    /** 指定区块内容一致对比接口编号 */
    public final static String INTF_CODE_TWO_IMG_BLOCK_RECG_EQUAL = "00000000000809615779";
    /** 材料高拍仪图片处理 */
    public final static String INTF_CODE_MATERIAL_HIGHSHOT_HANDLE = "00000000000809615379";
    /** 材料高拍仪图片处理 */
    public final static String INTF_CODE_MATERIAL_CATA_RELATIONSHIP = "00000000000829615379";
    /** 材料高拍仪图片处理结果验证 */
    public final static String INTF_CODE_MATERIAL_HIGHSHOT_VALID = "00000000000809615479";
    /** 手写体内容识别接口 */
    public final static String INTF_CODE_HANDWRITTEN_RECG = "00000000000809615454";

    /** 获取目录类型接口编号 */
    public final static String INTF_CODE_LIST_MATERIAL_TYPE = "00000000000809515471";
    /** 获取材料目录接口编号 */
    public final static String INTF_CODE_LIST_MATERIAL_CATALOG = "00000000000809515472";
    /** 获取材料目录元素接口编号 */
    public final static String INTF_CODE_LIST_MATERIAL_CATALOG_METADATA = "00000000000809515473";
    /** 获取目录识别模板接口编号 */
    public final static String INTF_CODE_GET_TEMPLATE_BY_CATACODE = "00000000000809515474";
    /** 获取识别模板区块接口编号 */
    public final static String INTF_CODE_LIST_TEMPLATEBLOCK_BY_TEMPLATECODE = "00000000000809515475";
    /** 获取百度识别图片信息 */
    public final static String INTF_CODE_BAIDU_OCR = "00000000000809515476";

    /**
     * 验证电子证照真伪接口编号
     */
    public final static String INTF_CODE_LICE = "00000000001809615459";
    /**
     * 验证电子证照真伪接口名称
     */
    public final static String INTF_NAME_LICE = "验证电子证照真伪";

    static {

        INTER_TYPE.put(INTF_TYPE_BASIC, "OCR基础接口");
        INTER_TYPE.put(INTF_TYPE_EXTEND, "OCR扩展接口");

        INTF_CATEGORY.put(INTF_CATEGORY_OCR, "识别验证接口");
        INTF_CATEGORY.put(INTF_CATEGORY_BASEDATA, "基础数据接口");
        INTF_CATEGORY.put(INTF_CATEGORY_VALI, "真伪验证接口");

        VALIDATE_TYPE_MAP.put(VALIDATE_TYPE_POPULATION, "人口库");
        VALIDATE_TYPE_MAP.put(VALIDATE_TYPE_LEGAL, "法人库");
        VALIDATE_TYPE_MAP.put(VALIDATE_TYPE_LICENCE, "电子证照库");

        OCR_LOGIN_TYPE_MAY.put(OCR_LOGIN_TYPE_BD, "百度");
        OCR_LOGIN_TYPE_MAY.put(OCR_LOGIN_TYPE_TX, "腾讯");
    }

    /** 百度接口访问地址 */
    public static String BAI_DU_URL = "https://aip.baidubce.com/";

    /** A3、A4大小类别 */
    public static final String SIZE_TYPE_A3 = "0";
    public static final String SIZE_TYPE_A4 = "1";
    public static final String SIZE_TYPE_WIDTH = "width";
    public static final String SIZE_TYPE_HEIGHT = "height";
    /**
     * A3长宽集合
     */
    public final static Map<String, String> SIZE_TYPE_A3_MAP = new HashMap<String, String>();
    /**
     * A4长宽集合
     */
    public final static Map<String, String> SIZE_TYPE_A4_MAP = new HashMap<String, String>();
    static {
        SIZE_TYPE_A3_MAP.put(SIZE_TYPE_WIDTH, "297");
        SIZE_TYPE_A3_MAP.put(SIZE_TYPE_HEIGHT, "420");

        SIZE_TYPE_A4_MAP.put(SIZE_TYPE_WIDTH, "210");
        SIZE_TYPE_A4_MAP.put(SIZE_TYPE_HEIGHT, "297");
    }

    /** 电子证照webService地址 */
    public static String ELMS_WEBSERVICE_URL = "http://192.168.1.39:8080/elms/cxf/";

    /** 电子证照：正常；请求已完成，电子证照存在 */
    public static final String LICE_STATUS_CODE_SUCCESS = "100";
    /** 电子证照：异常；服务不可用 */
    public static final String LICE_STATUS_CODE_NO_USE = "200";
    /** 电子证照：异常；参数不正确 */
    public static final String LICE_STATUS_CODE_PARAM_EXCEPTION = "201";
    /** 电子证照：异常；没有授权的访问 */
    public static final String LICE_STATUS_CODE_NO_POWER = "202";
    /** 电子证照：服务器内部错误 */
    public static final String LICE_STATUS_CODE_INNER_ERROR = "203";
    /** 电子证照：业务异常：电子证照文件不存在 */
    public static final String LICE_STATUS_CODE_NO_EXIST = "304";

    /**
     * 电子证照接口返回参数
     */
    public final static Map<String, String> LICE_STATUS_CODE_MAP = new HashMap<String, String>();
    static {
        LICE_STATUS_CODE_MAP.put(LICE_STATUS_CODE_SUCCESS, "请求已完成，电子证照存在");
        LICE_STATUS_CODE_MAP.put(LICE_STATUS_CODE_NO_USE, "异常；服务不可用");
        LICE_STATUS_CODE_MAP.put(LICE_STATUS_CODE_PARAM_EXCEPTION, "异常；参数不正确 ");
        LICE_STATUS_CODE_MAP.put(LICE_STATUS_CODE_NO_POWER, "异常；没有授权的访问");
        LICE_STATUS_CODE_MAP.put(LICE_STATUS_CODE_INNER_ERROR, "服务器内部错误");
        LICE_STATUS_CODE_MAP.put(LICE_STATUS_CODE_NO_EXIST, "业务异常：电子证照文件不存在");
    }

    /** 材料预览参数，材料名称 */
    public static String CL_NAME = "烟草专卖零售许可证新办申请表";
    /** 材料预览参数，材料编号 */
    public static String CL_CODE = "ef4e24ea056b11e89b08989096dd4f04";
    /** 材料预览参数，材料图片附件 */
    public static String CL_ATTA_OID = "2c93afe161bca9690161bcad35fa0001";

    /**
     * code：代码（成功、失败等代码） message：提示信息，如果有错误，就是错误信息 result：结果信息，正常处理的结果 比如图片识别：
     * 成功：{"result":{"blockCode":"0001","blockName":"姓名","words":"高利华"},"code":200}
     * 失败：{"code":201,"message":"材料编号不能为空！"}
     */

    /** code：代码（成功、失败等代码） */
    public static final String CODE = "code";
    /** message：提示信息，如果有错误，就是错误信息 */
    public static final String MESSAGE = "message";
    /** result：结果信息，正常处理的结果 */
    public static final String RESULT = "result";

    /** 接口访问日志 返回字符串 */
    public static final String RESPONSE_INFO = "RESPONSE_INFO";
    /** 接口访问日志 接口返回状态 */
    public static final String RESPONSE_STATUS = "RESPONSE_STATUS";

    /** 外部服务主键 */
    public static final String ACCOUNT_OID = "ACCOUNT_OID";
    /** 百度：SECRETID */
    public static final String BAIDU_SECRETID = "SECRETID";
    /** 百度：SECRETKEY */
    public static final String BAIDU_SECRETKEY = "SECRETKEY";
    /** 百度：TOKEN */
    public static final String BAIDU_TOKEN = "TOKEN";
    /** 百度授权信息集合：BAIDU_AUTH_INFO */
    public static Map<String, Map<String, String>> BAIDU_AUTH_INFO = new HashMap<String, Map<String, String>>();

    /** 腾讯：APPID */
    public static final String TENCENT_APPID = "APPID";
    /** 腾讯：SECRETID */
    public static final String TENCENT_SECRETID = "SECRETID";
    /** 腾讯：SECRETKEY */
    public static final String TENCENT_SECRETKEY = "SECRETKEY";
    /** 腾讯：BUCKETNAME */
    public static final String TENCENT_BUCKETNAME = "BUCKETNAME";
    /** 腾讯授权信息集合：TENCENT_AUTH_INFO */
  /*  public static Map<String, TencentAcountVo> TENCENT_AUTH_INFO = new HashMap<String, TencentAcountVo>();*/

    /** 识别模板状态 */
    public static final Map<String, String> TEMPLATE_STATUS_MAP = new HashMap<String, String>();

    public static final int ZCZT = 2;


    /** 暂存状态 */
    public static final String ZC = "Z";

    /** 外部服务不可用 */
    public static final String SER_PRO_EXCEPTION_NOT_USE = "外部识别服务不可用！";

    static {

        TEMPLATE_STATUS_MAP.put(BaseStaticParameter.YES, "启用");
        TEMPLATE_STATUS_MAP.put(BaseStaticParameter.NO, "禁用");
        TEMPLATE_STATUS_MAP.put(ZC, "暂存");

    }
    /** ftp标识 */
    public static final String FTP_REC = "ftp://";

    /**
     * 百度OCR识别营业执照需识别元素
     * <p>
     * 社会信用代码
     * <p>
     * 单位名称
     * <p>
     * 法人
     */
    public static List<String> RECOGNITION_ELEMENT = new ArrayList<>();

    static {
        RECOGNITION_ELEMENT.add("社会信用代码");
        RECOGNITION_ELEMENT.add("单位名称");
        RECOGNITION_ELEMENT.add("法人");
    }

    /**
     * 百度OCR识别身份证需要识别元素
     * <p>
     * 姓名
     * <p>
     * 公民身份号码
     */
    public static List<String> IDCARD_ELEMENT = new ArrayList<>();
    static {
        IDCARD_ELEMENT.add("姓名");
        IDCARD_ELEMENT.add("公民身份号码");
    }


    public final static Map<String, String> LICE_OCR_TYPE_MAP = new HashMap<String, String>();

    //** 证照识别类型-营业执照 *//*
    public static final String LICE_OCR_TYPE_BUSINESSLICE = "1";

    //** 证照识别类型-身份证 *//*
    public static final String LICE_OCR_TYPE_IDCARD_FRONT = "2";

    //** 证照识别类型-身份证 *//*
    public static final String LICE_OCR_TYPE_IDCARD_BACK = "3";

    //** 证照识别类型-临时身份证 *//*
    public static final String LICE_OCR_TYPE_TEMP_IDCARD = "4";

    //** 证照识别类型-户口本 *//*
    public static final String LICE_OCR_TYPE_HOUSEHOLD_REGISTER = "5";


    static {

        ANGLE_TYPE.put(ANGLE_TYPE_RIGHT, "直角");
        ANGLE_TYPE.put(ANGLE_TYPE_ROUNDED, "圆角");

        LICE_OCR_TYPE_MAP.put(LICE_OCR_TYPE_BUSINESSLICE, "营业执照");
        LICE_OCR_TYPE_MAP.put(LICE_OCR_TYPE_IDCARD_FRONT, "身份证头像面");
        LICE_OCR_TYPE_MAP.put(LICE_OCR_TYPE_IDCARD_BACK, "身份证国徽面");
        LICE_OCR_TYPE_MAP.put(LICE_OCR_TYPE_TEMP_IDCARD, "临时身份证");
        LICE_OCR_TYPE_MAP.put(LICE_OCR_TYPE_HOUSEHOLD_REGISTER, "户口本");

      /*  VALIDATION_TYPE.put(VALIDATION_TYPE_NOTNULL, "不能为空");
        VALIDATION_TYPE.put(VALIDATION_TYPE_NULL, "必须为空");
        VALIDATION_TYPE.put(VALIDATION_TYPE_CONTENT, "文本验证");
        VALIDATION_TYPE.put(VALIDATION_TYPE_STR_CONTAIN, "文本包含");
        VALIDATION_TYPE.put(VALIDATION_TYPE_RANGE, "数字范围");
        VALIDATION_TYPE.put(VALIDATION_TYPE_HAND_SIGN, "手写验证");
        VALIDATION_TYPE.put(VALIDATION_TYPE_DATE_COMPARE, "日期比较");
        VALIDATION_TYPE.put(VALIDATION_TYPE_CONTENT_THAN, "关联对比");
        VALIDATION_TYPE.put(VALIDATION_TYPE_SIMILARITY, "相似度");

        RECOGNITION_TYPE.put(RECOGNITION_TYPE_YS, "印刷");
        RECOGNITION_TYPE.put(RECOGNITION_TYPE_SX, "手写");
        RECOGNITION_TYPE.put(RECOGNITION_TYPE_SX_SINGLE, "手写（单行）");
        RECOGNITION_TYPE.put(RECOGNITION_TYPE_TX, "图像");
        RECOGNITION_TYPE.put(RECOGNITION_TYPE_MARQUEE_YS, "选择框（印刷）");
        RECOGNITION_TYPE.put(RECOGNITION_TYPE_MARQUEE_SX, "选择框（手写）");*/
    }

  /*  *//** 验证类型集合 *//*
    public static final Map<String, String> VALIDATION_TYPE = new LinkedHashMap<String, String>();
    *//** 不能为空，内容不能为空 *//*
    public static final String VALIDATION_TYPE_NOTNULL = "NOTNULL";
    *//** 必须为空，内容必须为空 *//*
    public static final String VALIDATION_TYPE_NULL = "NULL";
    *//** 内容验证，内容必须为什么文字 *//*
    public static final String VALIDATION_TYPE_CONTENT = "CONTENT";
    *//** 内容验证，内容中必须包含什么文字 *//*
    public static final String VALIDATION_TYPE_STR_CONTAIN = "STR_CONTAINS";
    *//** 内容验证，数字必须在某个范围内 *//*
    public static final String VALIDATION_TYPE_RANGE = "RANGE";
    *//** 内容验证，日期比较 *//*
    public static final String VALIDATION_TYPE_DATE_COMPARE = "DATE_COMPARE";
    *//** 内容验证，手写验证 *//*
    public static final String VALIDATION_TYPE_HAND_SIGN = "HAND_SIGN";
    *//** 文字内容比对验证，两个区域中文字必须相同 *//*
    public static final String VALIDATION_TYPE_CONTENT_THAN = "CONTENT_THAN";
    *//** 相似度验证，两个区域的相识度必须超过多少 *//*
    public static final String VALIDATION_TYPE_SIMILARITY = "SIMILARITY";
    *//** 特征块，相似度 *//*
    public static final double TRAIT_SIMILARITY = 40;

    *//** A3、A4大小类别 *//*
    public static final String SIZE_TYPE_A3 = "0";
    public static final String SIZE_TYPE_A4 = "1";
    public static final String SIZE_TYPE_WIDTH = "width";
    public static final String SIZE_TYPE_HEIGHT = "height";
    *//**
     * A3长宽集合
     *//*
    public final static Map<String, String> SIZE_TYPE_A3_MAP = new HashMap<String, String>();
    *//**
     * A4长宽集合
     *//*
    public final static Map<String, String> SIZE_TYPE_A4_MAP = new HashMap<String, String>();
    static {
        SIZE_TYPE_A3_MAP.put(SIZE_TYPE_WIDTH, "297");
        SIZE_TYPE_A3_MAP.put(SIZE_TYPE_HEIGHT, "420");

        SIZE_TYPE_A4_MAP.put(SIZE_TYPE_WIDTH, "210");
        SIZE_TYPE_A4_MAP.put(SIZE_TYPE_HEIGHT, "297");
    }

    *//** 角度类型 *//*
    public static final Map<String, String> ANGLE_TYPE = new HashMap<String, String>();
    *//** 角度类型-直角 *//*
    public static final String ANGLE_TYPE_RIGHT = "1";
    *//** 角度类型-圆角 *//*
    public static final String ANGLE_TYPE_ROUNDED = "2";




    *//**
     * 证照识别类型
     *//*

    *//** 暂存状态*//*
    public static final int ZC = 2;

    public static final Map<String, String> RECOGNITION_TYPE = new LinkedHashMap<String, String>();
    *//** 区块识别类型-印刷 *//*
    public static final String RECOGNITION_TYPE_YS = "1";
    *//** 区块识别类型-手写 *//*
    public static final String RECOGNITION_TYPE_SX = "2";
    *//** 区块识别类型-手写（单行） *//*
    public static final String RECOGNITION_TYPE_SX_SINGLE = "7";
    *//** 区块识别类型-图像：不需要识别 *//*
    public static final String RECOGNITION_TYPE_TX = "3";
    *//** 区块识别类型-特征块：不需要识别 *//*
    public static final String RECOGNITION_TYPE_TZ = "4";
    *//** 区块识别类型-选择框（印刷） *//*
    public static final String RECOGNITION_TYPE_MARQUEE_YS = "5";
    *//** 区块识别类型-选择框（手写） *//*
    public static final String RECOGNITION_TYPE_MARQUEE_SX = "6";






*/
}
