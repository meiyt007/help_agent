package com.zfsoft.platform.common.data;


import java.util.*;

/**
 * 静态参数配置
 */
public class BaseStaticParameter{

    public static int N = 0;
    public static int Y = 1;

    public static Byte BYTE_N = 0;
    public static Byte BYTE_Y = 1;

    public static String NO = "N";
    public static String YES = "Y";

    public static final String STR_ZERO = "0";
    public static final String STR_ONE = "1";
    public static final String SHIELD_YES = "0";
    public static final String SHIELD_NO = "1";
    public static final String STR_TWO = "2";
    public static final String STR_THREE = "3";
    public static final String STR_FOUR = "4";
    public static final String STR_FIVE = "5";
    public static final String STR_SIX = "6";
    public static final String STR_SEVEN = "7";
    public static final String STR_EIGHT  = "8";
    public static final String STR_NINE = "9";
    public static final String STR_TEN = "10";
    public static final Long DEFAULT_OID = 1L;

    /**
     * 默认区划编号
     */
    public static final String DEFAULT_DISTRICT_OID = "00000000000000000000000000000000";

    /**
     * 默认组织机构的编号
     */
    public static final String DEFAULT_ORGAN_OID = "00000000000000000000000000000000";

    /**
     * 默认用户的编号
     */
    public static final String DEFAULT_ADMIN_OID = "00000000000000000000000000000000";

    // 字典数据 区划等级 省
    public static final String DISTRICTLEVEL_SHENGJI_OID = "402881fa56e8b41c0156e8ba5e2a0009";
    // 字典数据 区划等级 市
    public static final String DISTRICTLEVEL_SHIJI_OID = "402881fa56e8b41c0156e8ba5e33000a";
    // 字典数据 区划等级 区
    public static final String DISTRICTLEVEL_QUXIAN_OID = "402881fa56e8b41c0156e8ba5e46000c";
    //镇、乡、街道办事处、民族乡、区公所
    public static final String DISTRICTLEVEL_JIEDAO_OID = "40288008594a6e7401594d56b2150000";
    //自然村、居委会、社区
    public static final String DISTRICTLEVEL_SHEQU_OID = "40288008594a6e7401594d5c17080001";

    //实施人员数据字典code
    public static final String IMPLEMENT_PERSONEL="QY-D1487902330767";

    //管理人员数据字典code
    public static final String ADMIN_PERSONEL="GR-D1487902326132";

    //业务人员数据字典code
    public static final String BUSINESS_PERSONEL="GR-D1487902326168";

    //梳理人员数据字典code
    public static final String CARDING_PERSONEL="GR-D1487902365897";

    //ACtiviti7 用户缓存前缀
    public static String  WORKFLOW_ACTIVITIUSER="workflow:activitiUser:";

    /**
     * 当前用户区划编号
     */
    public static String CURRENT_DISTRICT_OID = "";
    /**
     * 数据字典中保存区划级别的数据项
     */
    public static final String DICT_DISTRICT_LEVEL = "QHJB";
    /**
     * 数据字典中保存组织机构类型的数据项
     */
    public static final String DICT_ORGAN_TYPE = "ZZJGLX";
    /**
     * 数据字典中保存用户类型的数据项
     */
    public static final String DICT_USER_TYPE = "YHLX";
    /**
     * 分页参数，默认每页展示数量
     */
    public static final Integer DEFAULT_PAGE_SIZE = 10;

    /**
     * 总数统计
     */
    public static boolean PAGE_TOTAL_COUNT = true;

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 权限类型 L链接 B功能按钮
     */

    public static final String L = "L";
    public static final String B = "B";

    /**
     * 授权开始时间
     */
    public static Date STARTDATE_ = new Date();
    /**
     * 授权到期时间
     */
    public static Date DUEDATE_ = new Date();
    /**
     * 授权到期预警时间
     */
    public static Date DUEDATE_WARN = new Date();
    /**
     * 系统授权状态
     */
    public static String AUTH_STATE = "N";

    /**
     * 授权错误信息
     */
    public static String AUTHORIZATION_ERROR_MESSAGE = "初始化角色权限信息失败：无效授权文件，请与上海卓繁信息技术股份有限公司联系，联系电话:02160748199 QQ:1041844974。";

    /**
     * 已注册区划代码
     */
    public static Set<String> REGISTER_CODE = new HashSet<>();

    /** 启用禁用状态*/
    public static final Map<Integer,String> ABLE_MAP = new HashMap<>();

    /** 删除状态*/
    public static final Map<Integer, String> DELETE_MAP = new HashMap<>();
    /** 锁定状态*/
    public static final Map<String, String> LOCK_MAP = new HashMap<String, String>();
    /** 权限类型*/
    public static final Map<String, String> FUNCTION_TYPE_MAP = new HashMap<String, String>();
    /** 性别*/
    public static final Map<String, String> SEX_MAP = new HashMap<String, String>();
    /** 消息状态*/
    public static final Map<String, String> READ_MAP = new HashMap<String, String>();
    /** 日志级别*/
    public static final Map<String, String> LOG_LEVEL_MAP = new HashMap<String, String>();

    /** 性别-女 */
    public static final String SEX_FEMALE = "F";
    /** 性别-男 */
    public static final String SEX_MALE = "M";

    /** 安康码场景观E(易)分在线考试---考题类型（a.单选题 b.判断题）*/
    public static final Map<String, String> EXAM_QUE_TYPE_MAP = new HashMap<String, String>();

    /** 图片扩展名集合*/
    public final static Set<String> ATTA_IMAGE_EXT_SET = new HashSet<String>(5);

    public static final Map<String, String> HOLIDAY_DATE_MAP = new HashMap<String, String>();
    /** 节假日 */
    public static final String HOLIDAY_DATE_1 = "H";
    /** 工作日 */
    public static final String HOLIDAY_DATE_2 = "W";

    public static final Map<String, String> HOLIDAY_MAP = new HashMap<String, String>();
    /** 自然日*/
    public static final String HOLIDAY_NATURE = "N";
    /** 工作日*/
    public static final String HOLIDAY_WORK = "W";

    public final static List<String> PERSONEL_LIST = new ArrayList<>(5);

    /** 数据权限（1全部 2本人 3本部门 4本区划） */
    public static final Integer DATA_AUTHORITY_1 = 1;

    /** 数据权限（1全部 2本人 3本部门 4本区划） */
    public static final Integer DATA_AUTHORITY_2 = 2;

    /** 数据权限（1全部 2本人 3本部门 4本区划） */
    public static final Integer DATA_AUTHORITY_3 = 3;

    /** 数据权限（1全部 2本人 3本部门 4本区划） */
    public static final Integer DATA_AUTHORITY_4 = 4;

    public static final Map<Integer, String> DATA_AUTHORITY_MAP = new HashMap<>();

    static {

        PERSONEL_LIST.add(IMPLEMENT_PERSONEL);
        PERSONEL_LIST.add(ADMIN_PERSONEL);
        PERSONEL_LIST.add(CARDING_PERSONEL);
        PERSONEL_LIST.add(BUSINESS_PERSONEL);

        ABLE_MAP.put(Y, "启用");
        ABLE_MAP.put(N, "禁用");

        DELETE_MAP.put(Y, "已删除");
        DELETE_MAP.put(N, "未删除");

        LOCK_MAP.put(YES, "锁定");
        LOCK_MAP.put(NO, "解锁");

        READ_MAP.put("0", "未读");
        READ_MAP.put("1", "已读");
        SEX_MAP.put(SEX_MALE, "男");
        SEX_MAP.put(SEX_FEMALE, "女");

        FUNCTION_TYPE_MAP.put(L, "功能权限");
        FUNCTION_TYPE_MAP.put(B, "按钮权限");

        LOG_LEVEL_MAP.put("INFO", "一般");
        LOG_LEVEL_MAP.put("WARN", "警告");

        //考题类型（a.单选题 b.判断题 ）
        EXAM_QUE_TYPE_MAP.put("a", "单选题");
        EXAM_QUE_TYPE_MAP.put("b", "判断题");

        ATTA_IMAGE_EXT_SET.add("jpg");
        ATTA_IMAGE_EXT_SET.add("jpeg");
        ATTA_IMAGE_EXT_SET.add("gif");
        ATTA_IMAGE_EXT_SET.add("png");
        ATTA_IMAGE_EXT_SET.add("bmp");

        HOLIDAY_DATE_MAP.put(HOLIDAY_DATE_1, "节假日");
        HOLIDAY_DATE_MAP.put(HOLIDAY_DATE_2, "工作日");

        HOLIDAY_MAP.put(HOLIDAY_NATURE, "自然日");
        HOLIDAY_MAP.put(HOLIDAY_WORK, "工作日");

        /** 数据权限（1全部 2本人 3本部门 4本区划） */
        DATA_AUTHORITY_MAP.put(DATA_AUTHORITY_1,"全部");
        DATA_AUTHORITY_MAP.put(DATA_AUTHORITY_2,"本人");
        DATA_AUTHORITY_MAP.put(DATA_AUTHORITY_3,"本部门");
        DATA_AUTHORITY_MAP.put(DATA_AUTHORITY_4,"本区划");
    }


    /** 文件上传方式 */
    public interface UPLOAD_TYPE {
        /** 项目中 */
        String IN_PROJECT = "1";

        /** 磁盘中 */
        String IN_DISK = "2";

        /** ftp文件服务 */
        String FTP = "3";

        /** 统一文件服务 */
        String UNIFIED_FILES = "4";
    }

    // fastDFS,nginx预览的前缀url
    public static String FASTDFS_NGINX_URL = null;
    //网关url
    public static String ZUUL_URL = "";

    /** Layout组件标识 */
    public final static String LAYOUT = "Layout";

    /**
     * 登录加密配置
     */
    public final static String CONFIG_LOGIN_ENCRYPT="CONFIG_LOGIN_ENCRYPT";
    /**
     * rsaKey session存放
     * @author zxx
     * @date 2020/10/12 9:07 上午
     */
    public final static String RSA_KEY="rsaKey";
    /**
     * 私钥
     */
    public final static String PRIVATE_KEY="privateKey";

    /**
     * 公钥
     */
    public final static String PUBLIC_KEY="publicKey";

    /**
     * 短信码
     */
    public final static String SMS_CODE="SMS_CODE";

    public final static String SMS_SESSION_DATE="SMS_SESSION_DATE";
}
