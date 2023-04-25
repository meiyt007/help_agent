package com.zfsoft.service.common;


import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.service.sxDirectory.data.SxServiceType;
import com.zfsoft.service.sxSys.data.SxSysDict;
import rx.internal.util.LinkedArrayList;

import java.util.*;


/**
 * 静态常量
 * 
 * @author wangxl
 * @date 2020年10月26日
 */
public class SxptBaseStaticParameter extends BaseStaticParameter {

    public static final LinkedHashMap<Short, String> NO_YES_MAP = new LinkedHashMap<Short, String>();
    public static final short ZERO = 0;
    public static final short ONE = 1;

    public static final LinkedHashMap<Short, String> EXPRESS_NO_YES_MAP = new LinkedHashMap<Short, String>();

    // 附件上传参数
    public static Map<String, String> INIT_MODEL_MAP = new HashMap<String, String>();
    // 附件上传参数
    public static String UPLOAD_TYPE = "1"; // 1-存储到项目中，2-存储到磁盘中，3-存储到FTP中
    public static String UPLOAD_PATH = "upload"; // 附件存储的路径，当为项目中时，为文件夹名称，当为磁盘中，需要写全路径，当为FTP中，填写FTP的文件路径
    public static String UPLOAD_FTP_IP = "127.0.0.1"; // FTP存储，服务器地址
    public static String UPLOAD_FTP_PORT = "21"; // FTP存储，服务器端口
    public static String UPLOAD_FTP_USERNAME = "admin";// FTP用户名
    public static String UPLOAD_FTP_PASSWORD = "123";// FTP密码


    /**
     * 事项类型List
     */
    public static List<SxServiceType> serviceTypeList = new ArrayList<SxServiceType>();
    /**
     * 行使层级List
     */
    public static List<SxSysDict> levelList = new ArrayList<SxSysDict>();

    /**
     * 实施主体性质
     */
    public static List<SxSysDict> organPropertys = new ArrayList<SxSysDict>();

    public static Map<String,SxSysDict> organPropertyMaps = new HashMap<>();

    //权力来源
    public static Map<String,SxSysDict> originMaps = new HashMap<>();
    /**
     * 办理深度
     */
    public static List<SxSysDict> handleDepths = new ArrayList<SxSysDict>();
    /**
     * 通办范围
     */
    public static List<SxSysDict> handleScopes = new ArrayList<SxSysDict>();

    /**
     * 收费环节
     */
    public static List<SxSysDict> chargeLink = new ArrayList<SxSysDict>();
    /**
     * 事项来源
     */
    public static  List<SxSysDict> originList = new ArrayList<SxSysDict>();

    /**
     * 服务对象： 自然人
     */
    public static final String PERSONAL = "1";
    /**
     * 服务对象： 企业法人
     */
    public static final String NATIONALIZE_PERSON = "2";
    /**
     *
     * /** 服务对象： 事业法人
     */
    public static final String SOCIAL_ORGANIZATION = "3";
    /**
     * 服务对象： 社会组织法人
     */
    public static final String COLLEGE_PERSON = "4";

    /**
     * 服务对象： 非法人企业
     */
    public static final String PRIVATE_PERSON = "5";
    /**
     * 服务对象：行政机关
     */
    public static final String FOREIGN_PERSON = "6";

    /**
     * 服务对象： 其他组织
     */
    public static final String OTHER_PERSON = "9";
    /**
     * 办理类型： 好办易办
     */
    public static final String HAOBAN = "1";
    /**
     * 办理类型： 秒批秒办
     */
    public static final String KUAIBAN = "2";
    /**
     * 办理类型： 标准
     */
    public static final String BIAOZHUN = "3";
    /**
     * 办件类型
     */
    public static final Map<Short, String> CASE_TYPE_MAP = new LinkedHashMap<>();
    /**
     * 办件类型： 承诺件
     */
    public static final Short CASE_TYPE_PROMISE = 2;
    /**
     * 办件类型： 即办件
     */
    public static final Short CASE_TYPE_ATONCE = 1;

    /**
     * 服务对象
     */
    public static final Map<String, String> SERVICE_OBJECT_MAP = new LinkedHashMap<>();
    /**
     * 办理类型
     */
    public static final Map<String, String> HANDLE_TYPE_MAP = new LinkedHashMap<>();
    /**
     * 主题分类
     */
    public static Map<String,List<SxSysDict>> SUB_CLASS=new HashMap<>();
    /**
     * 生命周期分类
     */
    public static Map<String,SxSysDict> LIFE_CYCLE=new HashMap<>();
    /**
     * 办事群体
     */
    public static Map<String,List<SxSysDict>> SERVICE_GROUP=new HashMap<>();
    /**
     * 办理形式
     */
    public static final Map<String, String> HANDLE_FORM_MAP = new LinkedHashMap<>();
    /**
     * 办理形式： 窗口办理
     */
    public static final String HANDLE_WINDOW = "0";
    /**
     * 办理形式： 网上办理
     */
    public static final String HANDLE_NET = "1";
    /**
     * 办理形式： 一体化办理
     */
    public static final String HANDLE_WIN_AND_NET = "2";
    /**
     * 实施清单状态
     */
    public static final LinkedHashMap<Short, String> SERVICE_STATUS = new LinkedHashMap<Short, String>();
    /**
     * 实施清单状态:待提交审核
     */
    public static final Short SERVICE_PENDING_SUBMIT_AUDIT = 0;
    /**
     * 实施清单状态:待审核
     */
    public static final Short SERVICE_PENDING_AUDIT = 1;
    /**
     * 实施清单状态:待发布
     */
    public static final Short SERVICE_PENDING_PUBLISH = 2;
    /**
     * 实施清单状态:发布
     */
    public static final Short SERVICE_PUBLISH = 3;
    /**
     * 实施清单状态:暂停
     */
    public static final Short SERVICE_PAUSE = 4;
    /**
     * 实施清单状态:取消
     */
    public static final Short SERVICE_CANCEL = 5;
    /**
     * /** 实施清单状态:审核不通过
     */
    public static final Short SERVICE_AUDIT_NOT_PASS = 6;

    /**
     * 行政管辖地
     */
    public static final Map<String, String> ADMIN_JURISDICTION_MAP = new LinkedHashMap<>();
    /**
     * 行政管辖地： 定点办理
     */
    public static final String ADMIN_JURISDICTION_FIXED = "0";
    /**
     * 行政管辖地： 跨区域办理
     */
    public static final String ADMIN_JURISDICTION_CROSS = "1";

    /**
     * 事项性质0行政权力事项1公共服务事项
     */
    public static final Map<String, String> SERVICE_CHARACTER = new LinkedHashMap<String, String>();

    /**
     * 事项性质:公共服务事项
     */
    public static final String COMMON_SERVICE = "0";
    /**
     * 事项性质:行政权力事项
     */
    public static final String POWER_SERVICE = "1";
    /**
     * 公开方式
     */
    public static final Map<Short, String> OPEN_WAY_MAP = new LinkedHashMap<Short, String>();
    public static final Short OPEN_WAY_ONE = 1;
    public static final Short OPEN_WAY_TWO = 2;

    /**
     * 公开渠道
     */
    public static final Map<Short, String> OPEN_CHANNEL_MAP = new LinkedHashMap<Short, String>();
    public static final Short OPEN_CHANNEL_ZFGB = 1;
    public static final Short OPEN_CHANNEL_ZFWZ = 2;
    public static final Short OPEN_CHANNEL_FBH = 3;
    public static final Short OPEN_CHANNEL_DS = 4;
    /**
     * 办理结果送达方式
     */
    public static final Map<String, String> RESULT_DELIVERY_WAY_MAP = new LinkedHashMap<String, String>();

    /**
     * 办理结果送达方式：自取
     */
    public static final String RESULT_DELIVERY_WAY_6 = "6";
    /**
     * 办理结果送达方式：短信通知
     */
    public static final String RESULT_DELIVERY_WAY_5 = "5";
    /**
     * 办理结果送达方式：物流
     */
    public static final String RESULT_DELIVERY_WAY_4 = "4";
    /**
     * 办理结果送达方式：公告
     */
    public static final String RESULT_DELIVERY_WAY_2 = "2";

    /**
     * 办理结果送达方式：直接送达
     */
    public static final String RESULT_DELIVERY_WAY_7 = "7";

    /**
     * 事项咨询投诉方式MAP
     */
    public static final LinkedHashMap<String, String> SX_ZXTS_TYPE_MAP = new LinkedHashMap<String, String>();
    /**
     * 事项咨询投诉方式:大厅窗口
     */
    public static final String SX_ZXTS_TYPE_DTCK = "0";
    /**
     * 事项咨询投诉方式:网络
     */
    public static final String SX_ZXTS_TYPE_WL = "1";
    /**
     * 事项咨询投诉方式:邮件
     */
    public static final String SX_ZXTS_TYPE_YJ = "2";
    /**
     * 事项咨询投诉方式:信件
     */
    public static final String SX_ZXTS_TYPE_XJ = "3";
    /**
     * 事项咨询投诉方式:电话
     */
    public static final String SX_ZXTS_TYPE_DH = "4";

    /**
     * 时限类型
     */
    public static final Map<String, String> LIMIT_TYPE_MAP = new LinkedHashMap<>();
    /**
     * 时限类型： 工作日
     */
    public static final String WORKING_DAY = "W";
    /**
     * 时限类型： 自然日
     */
    public static final String NATURAL_DAY = "N";

    /**
     * 通办范围(审改对接用)
     */
    public static final Map<String, String> HANDLE_SCOPE_MAP = new LinkedHashMap<>();

    /**
     * 办理环节(审改对接用)
     */
    public static final Map<String, String> LINK_MAP = new LinkedHashMap<>();

    /**
     * 收费类型(审改对接用)
     */
    public static final Map<String, String> CHARGE_TYPE_MAP = new LinkedHashMap<>();
    /**
     * 收费类型(审改对接用)
     */
    public static final Map<String, String> implementOrganPropertyMap = new LinkedHashMap<>();

    /**
     * 办理深度(审改对接用)
     */
    public static final Map<String, String> HANDLE_DEPTH_MAP = new LinkedHashMap<>();

    /**
     * 权利来源(审改对接用)
     */
    public static final Map<String, String> ORGIN_QL_MAP = new LinkedHashMap<>();

       static {
        CASE_TYPE_MAP.put(CASE_TYPE_PROMISE, "承诺件");
        CASE_TYPE_MAP.put(CASE_TYPE_ATONCE, "即办件");
        SERVICE_OBJECT_MAP.put(PERSONAL, "自然人");
        SERVICE_OBJECT_MAP.put(SOCIAL_ORGANIZATION, "事业法人");
        SERVICE_OBJECT_MAP.put(NATIONALIZE_PERSON, "企业法人");
        SERVICE_OBJECT_MAP.put(PRIVATE_PERSON, "非法人企业");
        SERVICE_OBJECT_MAP.put(FOREIGN_PERSON, "行政机关");
        SERVICE_OBJECT_MAP.put(COLLEGE_PERSON, "社会组织法人");
        SERVICE_OBJECT_MAP.put(OTHER_PERSON, "其他组织");
        HANDLE_FORM_MAP.put(HANDLE_WINDOW, "窗口办理");
        HANDLE_FORM_MAP.put(HANDLE_NET, "网上办理");
        HANDLE_FORM_MAP.put(HANDLE_WIN_AND_NET, "一体化办理");

        SERVICE_STATUS.put(SERVICE_PENDING_SUBMIT_AUDIT, "待提交审核");
        SERVICE_STATUS.put(SERVICE_PENDING_AUDIT, "待审核");
        SERVICE_STATUS.put(SERVICE_AUDIT_NOT_PASS, "审核不通过");
        SERVICE_STATUS.put(SERVICE_PENDING_PUBLISH, "待发布");
        SERVICE_STATUS.put(SERVICE_PUBLISH, "发布");
        SERVICE_STATUS.put(SERVICE_PAUSE, "暂停");
        SERVICE_STATUS.put(SERVICE_CANCEL, "取消");

        NO_YES_MAP.put(ZERO,"否");
        NO_YES_MAP.put(ONE,"是");

       EXPRESS_NO_YES_MAP.put(ZERO,"支持");
       EXPRESS_NO_YES_MAP.put(ONE,"不支持");



        ADMIN_JURISDICTION_MAP.put(ADMIN_JURISDICTION_FIXED, "定点办理");
        ADMIN_JURISDICTION_MAP.put(ADMIN_JURISDICTION_CROSS, "跨区域办理");

       SERVICE_CHARACTER.put(POWER_SERVICE, "行政权力");
       SERVICE_CHARACTER.put(COMMON_SERVICE, "公共服务");

       OPEN_WAY_MAP.put(OPEN_WAY_ONE, "主动公开");
       OPEN_WAY_MAP.put(OPEN_WAY_TWO, "依申请公开");

       OPEN_CHANNEL_MAP.put(OPEN_CHANNEL_ZFGB, "政府公报");
       OPEN_CHANNEL_MAP.put(OPEN_CHANNEL_ZFWZ, "政府网站");
       OPEN_CHANNEL_MAP.put(OPEN_CHANNEL_FBH, "新闻发布会");
       OPEN_CHANNEL_MAP.put(OPEN_CHANNEL_DS, "报刊、广播、电视");

       RESULT_DELIVERY_WAY_MAP.put(RESULT_DELIVERY_WAY_7, "直接送达");
       RESULT_DELIVERY_WAY_MAP.put(RESULT_DELIVERY_WAY_6, "自取");
       RESULT_DELIVERY_WAY_MAP.put(RESULT_DELIVERY_WAY_4, "物流");
       RESULT_DELIVERY_WAY_MAP.put(RESULT_DELIVERY_WAY_2, "公告");

        /*   RESULT_DELIVERY_WAY_MAP.put(RESULT_DELIVERY_WAY_6, "自行取件");
           RESULT_DELIVERY_WAY_MAP.put(RESULT_DELIVERY_WAY_4, "快递送达");
           RESULT_DELIVERY_WAY_MAP.put(RESULT_DELIVERY_WAY_2, "其他");*/
       SX_ZXTS_TYPE_MAP.put(SX_ZXTS_TYPE_DTCK, "窗口");
       SX_ZXTS_TYPE_MAP.put(SX_ZXTS_TYPE_WL, "网络");
       SX_ZXTS_TYPE_MAP.put(SX_ZXTS_TYPE_YJ, "邮件");
       SX_ZXTS_TYPE_MAP.put(SX_ZXTS_TYPE_XJ, "信件");
       SX_ZXTS_TYPE_MAP.put(SX_ZXTS_TYPE_DH, "电话");

       LIMIT_TYPE_MAP.put(WORKING_DAY, "工作日");
       LIMIT_TYPE_MAP.put(NATURAL_DAY, "自然日");
       HANDLE_SCOPE_MAP.put("1","402881ef5ba8a34e015ba8e076500001");//全国
       HANDLE_SCOPE_MAP.put("2","402881ef5ba8a34e015ba8e0dbec0002");//跨省
       HANDLE_SCOPE_MAP.put("3","402881ef5ba8a34e015ba8e126110003");//跨市
       HANDLE_SCOPE_MAP.put("4","402881ef5ba8a34e015ba8e198390004");//跨县
       HANDLE_SCOPE_MAP.put("5","bb449708694e729d01694e7482200006");//全镇（乡、街道）
       HANDLE_SCOPE_MAP.put("6","bb449708694e729d01694e74fa4d0007");//全村（社区）


           LINK_MAP.put("1","2c287bb66859a90001685a6c81990022");//受理
           LINK_MAP.put("2","2c287bb66859a90001685a6cc7840023");//审核
           LINK_MAP.put("3","402882005d39f5e4015d39fef5fc1245");//核准
           LINK_MAP.put("4","2c287bb66859a90001685a6d02a40024");//办结

           CHARGE_TYPE_MAP.put("0","402881ff5bf658ea015bf6725ec10005");//分段费率累加
           CHARGE_TYPE_MAP.put("1","402881ff5bf658ea015bf6722a4e0004");//分段固定费率
           CHARGE_TYPE_MAP.put("2","402881ff5bf658ea015bf671f2450003");//分段固定单价
           CHARGE_TYPE_MAP.put("3","402881ff5bf658ea015bf671b97d0002");//浮动费率
           CHARGE_TYPE_MAP.put("4","402881ff5bf658ea015bf6718a270001");//浮动单价
           CHARGE_TYPE_MAP.put("5","402881ff5bf56985015bf584c7ff0000");//固定单价

           implementOrganPropertyMap.put("1","402881ef5b9e7d71015b9e9b5f2c0001");// //法定机关
           implementOrganPropertyMap.put("2","402881ef5b9e7d71015b9e9bae920002");//授权组织
           implementOrganPropertyMap.put("3","402881ef5b9e7d71015b9e9bee920003");//受委托授权组织

           HANDLE_DEPTH_MAP.put("1","402881ef5ba7c065015ba82672b90001");//互联网咨询
           HANDLE_DEPTH_MAP.put("2","402881ef5ba7c065015ba82771f10003");//收件
           HANDLE_DEPTH_MAP.put("3","402881ef5ba7c065015ba82708050002");//预审
           HANDLE_DEPTH_MAP.put("4","2c287bb66ae75fe7016ae79fd7850012");//受理
           HANDLE_DEPTH_MAP.put("6","2c287bb66ae75fe7016ae7a0fca40013");//结果反馈
           HANDLE_DEPTH_MAP.put("9","2c287bb66ae75fe7016ae7a214080014");//其他

           ORGIN_QL_MAP.put("1","402881fd5ed59578015ed5a23b32000e");//法定本级行使
           ORGIN_QL_MAP.put("2","5f2feb20f53049c393606404c7c1c994");//上级下放
           ORGIN_QL_MAP.put("3","0fdf7aaf2088421594e0829285854137");//上级授权
           ORGIN_QL_MAP.put("4","b9a8e0dc0a324a0fb4b9bbf676d97bdf");//同级授权
           ORGIN_QL_MAP.put("5","d2920066ae8a479b8df10d5cdd092131");//上级委托
           ORGIN_QL_MAP.put("6","60b2690b789f4dd386a2c6b5bea08bce"); //同级委托

           HANDLE_TYPE_MAP.put(HAOBAN,"好办易办");
           HANDLE_TYPE_MAP.put(KUAIBAN,"秒批秒办");
           HANDLE_TYPE_MAP.put(BIAOZHUN,"标准");
       }
    /**
     * 字典管理项：行使层级ID
     */
    public static final String EXERCISE_LEVEL_CODE = "XSCJ";
    /**
     * 实施主体性质
     */
    public static final String IMPLEMENT_ORGAN_PROPERTY = "SSZTXZ";
    /**
     * 事项来源字典代码
     */
    public static final String SERVICE_ORIGIN = "SERVICE_ORIGIN";
    /**
     * 办理深度
     */
    public static final String HANDLE_DEPTH = "BLSD";
    /**
     * 通办范围
     */
    public static final String HANDLE_SCOPE = "TBFW";
}
