package com.zfsoft.service.util;


import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.service.sxDirectory.data.SxServiceType;

import java.util.*;


/**
 * 静态常量
 *
 * @author wangxl
 * @date 2020年10月26日
 */
public class MatterBaseStaticParameter extends BaseStaticParameter {

    //分隔符 逗号
    public static final String REGEX_COMMA = ",";
    //分隔符 分号
    public static final String REGEX_SEMICOLON = ";";


    /**
     * 要素事项类型code
     */
    public static final String SHIXIANGLEIXING = "SHIXIANGLEIXING";

    //要素实施清单code
    public static final String WEIZHI = "WEIZHI";

//    /*
//     * @Description:业务要素维度-申请材料编码
//     * @Author: wangxl
//     * @Date: 2021/4/30
//     **/
//    public static final String MATER_CODE="SXCL";





    public static final LinkedHashMap<Short, String> NO_YES_MAP = new LinkedHashMap<Short, String>();
    public static final short ZERO = 0;
    public static final short ONE = 1;

    public static final Integer NO = 0;
    public static final Integer YES = 1;

    /**
     * Excel导入sheetNum
     */
    public static final Integer SHEET_NUM = 1;

    /**
     * 目录导入Excel标题
     */
    public static final List<String> DIRECTORY_TITLE_LIST =
            Arrays.asList("事项名称","基本编码","设定依据","事项类型","业务指导部门","是否存在子项","是否纳入国家目录","行使层级","主项/子项","主项名称",
                    "法定办结时限","法定办结时限单位","有无数量限制","有无年检","有无年报","服务对象");


    /**工作流定义**/
    public static final String SXDIRE_BZ="SXBZLC";

    public static final String SXDIRE_DT="MLDT";
    /**目录审核节点**/
    public static final String SXDIRE_BZ_SH="sh";
    /**目录发布节点**/
    public static final String SXDIRE_BZ_FB="fb";
    /**t_workflow_type**/
    public static final String SGFLOW_TYPE_Dir="sxDirectory";
    public static final String SGFLOW_TYPE_Serv="sxService";
    public static final String SGFLOW_TYPE_SXBG = "sxservice_bg";
    public static final String SGFLOW_TYPE_SXZT = "sxservice_zt";
    public static final String SGFLOW_TYPE_SXQX = "sxservice_qx";

    /**实施清单编制**/
    public static final String SXSERVICE_BZ="ADDSXSERVICE";

    /***实施清单审核节点*/
    public static final String SXSERVICE_BZ_SH="SXSH";
    /**实施清单发布节点**/
    public static final String SXSERVICE_BZ_FB="SXFB";
    /***实施清单变更审核节点*/
    public static final String SXSERVICE_BG_SH="SXBGSH";
    /**实施清单变更发布节点**/
    public static final String SXSERVICE_BG_FB="SXBGFB";
    /***实施清单暂停节点*/
    public static final String SXSERVICE_BZ_ZT="SXZT";
    /**实施清单取消节点**/
    public static final String SXSERVICE_BZ_QX="SXQX";

    /**上报接口_办事指南**/
    public static final String SHANGBAO_BSZN="banshizhinan";
    /**事项实施编码**/
    public static final String SERVICE_IMPLEMENT_CODE="implementCode";


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
    public static List<SxServiceType> serviceTypeList = new ArrayList<>();

    /**
     * 实施主体性质
     */
   // public static List<SxSysDict> organPropertys = new ArrayList<SxSysDict>();

    public static Map<String,SysDict> organPropertyMaps = new HashMap<>();

    //权力来源
    public static Map<String,SysDict> originMaps = new HashMap<>();
    /**
     * 办理深度
     */
    public static List<SysDict> handleDepths = new ArrayList<>();
    /**
     * 通办范围
     */
    public static List<SysDict> handleScopes = new ArrayList<>();
    /**
     * 事项来源
     */
    public static  List<SysDict> originList = new ArrayList<>();

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
    public static final Map<String, String> SERVICE_OBJECT_MAP_DESC = new LinkedHashMap<>();
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
    public static final Map<Integer, String> SERVICE_CHARACTER = new LinkedHashMap<>();

    /**
     * 事项性质:公共服务事项
     */
    public static final Integer COMMON_SERVICE = 0;
    /**
     * 事项性质:行政权力事项
     */
    public static final Integer POWER_SERVICE = 1;
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
     * 目录清单信息来源:手工录入
     */
    public static final Integer DIRECTORY_INFO_SOURCE_MANUAL_INPUT = 0;

    /**
     * 目录清单信息来源:excel导入
     */
    public static final Integer DIRECTORY_INFO_SOURCE_EXCEL_INPUT = 1;

    /**
     * 目录记录操作状态:新建
     */
    public static final Integer DIRECTORY_RECORD_TYPE_NEW_BUILD = 0;

    /**
     * 目录记录操作状态：变更
     */
    public static final Integer DIRECTORY_RECORD_TYPE_CHANGE = 1;

    /**
     * 目录记录操作状态：拆分
     */
    public static final Integer DIRECTORY_RECORD_TYPE_SPLIT = 2;

    /**
     * 目录记录操作状态:合并
     */
    public static final Integer DIRECTORY_RECORD_TYPE_MERGE = 3;

    /**
     * 目录记录操作状态：暂停
     */
    public static final Integer DIRECTORY_RECORD_TYPE_PAUSE = 4;

    /**
     * 目录记录操作状态：取消
     */
    public static final Integer DIRECTORY_RECORD_TYPE_CANCEL = 5;


    /**
     * 审核状态
     */
    public static final Map<Integer, String> AUDIT_STATUS_MAP = new LinkedHashMap<>();

    /**
     * 未提交审核
     */
    public static final Integer AUDIT_STATUS_1 = 1;
    /**
     * 已提交审核
     */
    public static final Integer AUDIT_STATUS_2 = 2;
    /**
     * 审核通过
     */
    public static final Integer AUDIT_STATUS_3 = 3;
    /**
     * 审核不通过
     */
    public static final Integer AUDIT_STATUS_4 = 4;

    /**
     * 行政许可
     */
    public static final String  ITEM_LABLE_TYPE_GUIDELINE = "402881fd5fb4a7ed015fb4ac939a0002";

    /**
     * 权责清单
     */
    public static final String  ITEM_LABLE_TYPE_RIGHT_DUTY = "4028718171a04f7a0171a0e45fda0026";

    /**
     * 办事指南材料层级
     */
    public static final String  ITEM_LABLE_TYPE_METATIL = "40287181718c312201718c40d4cc0001";

    /**
     * 办理流程
     */
    public static final String  ITEM_LABLE_TYPE_BANLILIUCHENG = "40287181749ee6c401749f32ffe10019";

    /**
     * 办事指南审批证件层级
     */
    public static final String  ITEM_LABLE_TYPE_ZJRESULT="40287181718c312201718c40d54c0004";

    /**
     * 浦东事项管理员role角色
     */
    public static final String ADMIN_ROLE = "40283f816e163f2b016e1647c0080003";

    /**
     * fastDFS地址
     */
    public static String FASTDFS_NGINX_URL = "";


    /**
     * 目录清单状态
     */
    public static final Map<Integer, String> DIRECTORY_STATUS = new LinkedHashMap<>();

    /**
     * 目录清单状态:待提交审核
     */
    public static final Integer DIRECTORY_PENDING_SUBMIT_AUDIT = 0;
    /**
     * 目录清单状态:待审核
     */
    public static final Integer DIRECTORY_PENDING_AUDIT = 4;
    /**
     * 目录清单状态:发布
     */
    public static final Integer DIRECTORY_PUBLISH = 1;
    /**
     * 目录清单状态：不予发布
     */
    public static final Integer DIRECTORY_NOT_PUBLISH = 7;
    /**
     * 目录清单状态：待发布
     */
    public static final Integer DIRECTORY_PENDING_PUBLISH = 8;
    /**
     * 目录清单状态:审核不通过
     */
    public static final Integer DIRECTORY_AUDIT_NOT_PASS = 5;
    /**
     *  目录清单状态:合并
     */
    public static final Integer DIRECTORY_MERGE = 6;
    /**
     * 目录清单状态:暂停
     */
    public static final Integer DIRECTORY_PAUSE = 2;
    /**
     * 目录清单状态:取消
     */
    public static final Integer DIRECTORY_CANCEL = 3;

    /**
     * 目录审核状态 不通过
     */
    public static final Integer DIRECTORY_AUDIT_STATUS_NOT_PASS = 0;
    /**
     * 目录审核状态 通过
     */
    public static final Integer DIRECTORY_AUDIT_STATUS_PASS = 1;

    /**
     * 目录审核状态 发布（发布用）
     */
    public static final Integer DIRECTORY_AUDIT_STATUS_PUBLISH = 2;

    /**
     * 目录审核状态 不予发布（发布用）
     */
    public static final Integer DIRECTORY_AUDIT_STATUS_NOT_PUBLISH = 3;


    /**
     * ES事项库名
     */
    public static final String ES_SXCONTENT_INDEX = "es_sxcontent_index";

    /**
     * ES事项表名
     */
    public static final String ES_SXCONTENT_TYPE = "es_sxcontent_type";

    /**
     * 基础库：申报材料库code
     */
    public static final String BASE_DATA_SBCLK_CODE = "jck_material";

    /**
     * 基础库：电子证照库code
     */
    public static final String BASE_DATA_DZZZK_CODE = "jck_dzzzk";

    /**
     * 基础库：办理地点库code
     */
    public static final String BASE_DATA_BSDZ_CODE = "jck_bldd";


    /**
     * 层级维度-基本信息
     */
    public static final String LABLE_CODE_JBXX = "JBXX";

    /**
     * 层级维度-扩展信息
     */
    public static final String LABLE_CODE_KZXX = "KZXX";

    /**
     * 层级维度-办事地址
     */
    public static final String LABLE_CODE_BSDD = "BSDD";

    /**
     * 层级维度-事项结果
     */
    public static final String LABLE_CODE_SXJG = "SXJG";

    /**
     * 层级维度-办理流程
     */
    public static final String LABLE_CODE_BLLC = "BLLC";

    /**
     * 层级维度-收费设置
     */
    public static final String LABLE_CODE_SFSZ = "SFSZ";

    /**
     * 层级维度-收费信息
     */
    public static final String LABLE_CODE_SFXX = "SFXX";

    /**
     * 层级维度-受理条件
     */
    public static final String LABLE_CODE_SLTJ = "SLTJ";

    /**
     * 层级维度-申请材料
     */
    public static final String LABLE_CODE_SQCL = "SQCL";

    /**
     * 层级维度-常见问题
     */
    public static final String LABLE_CODE_CJWT = "CJWT";

    /**
     * 层级维度-办理环节
     */
    public static final String LABLE_CODE_BLHJ = "BLHJ";


    static {

       DIRECTORY_STATUS.put(DIRECTORY_PENDING_SUBMIT_AUDIT, "待提交审核");
       DIRECTORY_STATUS.put(DIRECTORY_PENDING_PUBLISH, "待发布");
       DIRECTORY_STATUS.put(DIRECTORY_PENDING_AUDIT, "待审核");
       DIRECTORY_STATUS.put(DIRECTORY_AUDIT_NOT_PASS, "审核不通过");
       DIRECTORY_STATUS.put(DIRECTORY_NOT_PUBLISH, "不予发布");
       DIRECTORY_STATUS.put(DIRECTORY_PUBLISH, "发布");
       DIRECTORY_STATUS.put(DIRECTORY_PAUSE, "暂停");
       DIRECTORY_STATUS.put(DIRECTORY_CANCEL, "取消");
       DIRECTORY_STATUS.put(DIRECTORY_MERGE, "合并");


        CASE_TYPE_MAP.put(CASE_TYPE_PROMISE, "承诺件");
        CASE_TYPE_MAP.put(CASE_TYPE_ATONCE, "即办件");
        SERVICE_OBJECT_MAP.put(PERSONAL, "自然人");
        SERVICE_OBJECT_MAP.put(SOCIAL_ORGANIZATION, "事业法人");
        SERVICE_OBJECT_MAP.put(NATIONALIZE_PERSON, "企业法人");
        SERVICE_OBJECT_MAP.put(PRIVATE_PERSON, "非法人企业");
        SERVICE_OBJECT_MAP.put(FOREIGN_PERSON, "行政机关");
        SERVICE_OBJECT_MAP.put(COLLEGE_PERSON, "社会组织法人");
        SERVICE_OBJECT_MAP.put(OTHER_PERSON, "其他组织");
        SERVICE_OBJECT_MAP_DESC.put("自然人",PERSONAL);
        SERVICE_OBJECT_MAP_DESC.put("事业法人",SOCIAL_ORGANIZATION);
        SERVICE_OBJECT_MAP_DESC.put("企业法人",NATIONALIZE_PERSON);
        SERVICE_OBJECT_MAP_DESC.put("非法人企业",PRIVATE_PERSON);
        SERVICE_OBJECT_MAP_DESC.put("行政机关",FOREIGN_PERSON);
        SERVICE_OBJECT_MAP_DESC.put("社会组织法人",COLLEGE_PERSON);
        SERVICE_OBJECT_MAP_DESC.put("其他组织",OTHER_PERSON);
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
       RESULT_DELIVERY_WAY_MAP.put(RESULT_DELIVERY_WAY_6, "自行取件");
       RESULT_DELIVERY_WAY_MAP.put(RESULT_DELIVERY_WAY_4, "快递送达");
       RESULT_DELIVERY_WAY_MAP.put(RESULT_DELIVERY_WAY_2, "其他");

       SX_ZXTS_TYPE_MAP.put(SX_ZXTS_TYPE_DTCK, "窗口");
       SX_ZXTS_TYPE_MAP.put(SX_ZXTS_TYPE_WL, "网络");
       SX_ZXTS_TYPE_MAP.put(SX_ZXTS_TYPE_YJ, "邮件");
       SX_ZXTS_TYPE_MAP.put(SX_ZXTS_TYPE_XJ, "信件");
       SX_ZXTS_TYPE_MAP.put(SX_ZXTS_TYPE_DH, "电话");

       LIMIT_TYPE_MAP.put(WORKING_DAY, "工作日");
       LIMIT_TYPE_MAP.put(NATURAL_DAY, "自然日");

       AUDIT_STATUS_MAP.put(AUDIT_STATUS_1, "未提交审核");
       AUDIT_STATUS_MAP.put(AUDIT_STATUS_2, "已提交审核");
       AUDIT_STATUS_MAP.put(AUDIT_STATUS_3, "审核通过");
       AUDIT_STATUS_MAP.put(AUDIT_STATUS_4, "审核不通过");
    }
    /**
     * 字典管理项：行使层级ID
     */
    public static final String EXERCISE_LEVEL_CODE = "XSCJ";

    /**
     * 字典管理项：区划级别ID
     */
    public static final String DISTRICT_LEVEL_CODE = "QHJB";

    /***
     * @description: 层级map映射，映射数据字典中区划级别和行使层级关系
     * @author: xiayj
     * @date: 2021/6/22
     */
    public static final Map<String,String> LEVEL_MAP = new HashMap<>();
    //分级管理
    public static final String LEVEL_FJGL = "f027a2eaf4394141a100421d9aacbb0f";
    static {
        //国家
        LEVEL_MAP.put("fb83b4c7c2894bb1aa7fbf64d05c5002","883b11beafe0423b8427c3e2461f7250");
        LEVEL_MAP.put("883b11beafe0423b8427c3e2461f7250","fb83b4c7c2894bb1aa7fbf64d05c5002");
        //省
        LEVEL_MAP.put("402881fa56e8b41c0156e8ba5e2a0009","9d48dfe28d044d01a22c4baf863e957d");
        LEVEL_MAP.put("9d48dfe28d044d01a22c4baf863e957d","402881fa56e8b41c0156e8ba5e2a0009");
        //市
        LEVEL_MAP.put("402881fa56e8b41c0156e8ba5e33000a","72952cf83cf94ea5b5dfbb573fc8f0d3");
        LEVEL_MAP.put("72952cf83cf94ea5b5dfbb573fc8f0d3","402881fa56e8b41c0156e8ba5e33000a");
        //县
        LEVEL_MAP.put("402881fa56e8b41c0156e8ba5e46000c","498dcbf5a2564aebb647308e9d89f6b1");
        LEVEL_MAP.put("498dcbf5a2564aebb647308e9d89f6b1","402881fa56e8b41c0156e8ba5e46000c");
        //镇
        LEVEL_MAP.put("40288008594a6e7401594d56b2150000","01db7ffadb58442980fdb435b3dc4c55");
        LEVEL_MAP.put("01db7ffadb58442980fdb435b3dc4c55","40288008594a6e7401594d56b2150000");
        //村
        LEVEL_MAP.put("40288008594a6e7401594d5c17080001","9c3752b0f9a04d9f9e25b69744140e1f");
        LEVEL_MAP.put("9c3752b0f9a04d9f9e25b69744140e1f","40288008594a6e7401594d5c17080001");
    }


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

    /**
     * 顶级区划业务主键
     */
    public static final String TOP_DISTRICT_OID = "00000000000000000000000000000000";

    /**
     * 有子项的主项3位子项代码
     */
    public static final String MAIN_CHILD_CODE_1 = "00Y";

    /**
     * 无子项的主项3位子项代码
     */
    public static final String MAIN_CHILD_CODE_2 = "000";

    /**
     * 目录清单动态管理状态
     */
    public static final Map<Integer, String> DIRECTORY_CHANGE_STATUS = new LinkedHashMap<>();

    /**
     * 目录清单动态管理状态:待申请
     */
    public static final Integer DIRECTORY_CHANGE_APPLY = 1;
    /**
     * 目录清单动态管理状态:待提交审核
     */
    public static final Integer DIRECTORY_CHANGE_SUBMIT_AUDIT = 2;
    /**
     * 目录清单动态管理状态:待审核
     */
    public static final Integer DIRECTORY_CHANGE_AUDIT = 3;
    /**
     * 目录清单动态管理状态：通过
     */
    public static final Integer DIRECTORY_CHANGE_PASS = 4;
    /**
     * 目录清单动态管理状态：不通过
     */
    public static final Integer DIRECTORY_CHANGE_NOT_PASS = 5;

    /**
     * 事项状态
     */
    public static final Map<Integer, String> CONTENT_STATUS = new HashMap<>();

    /**
     * 新建
     */
    public static final Integer CONTENT_STATUS_0 = 0;

    /**
     * 已提交
     */
    public static final Integer CONTENT_STATUS_1 = 1;

    /**
     * 审核未通过
     */
    public static final Integer CONTENT_STATUS_2 = 2;

    /**
     * 审核通过
     */
    public static final Integer CONTENT_STATUS_3 = 3;

    /**
     * 变更未提交
     */
    public static final Integer CONTENT_STATUS_4 = 4;

    /**
     * 变更已提交
     */
    public static final Integer CONTENT_STATUS_5 = 5;

    /**
     * 变更未通过
     */
    public static final Integer CONTENT_STATUS_6 = 6;

    /**
     * 变更通过
     */
    public static final Integer CONTENT_STATUS_7 = 7;

    /**
     * 已发布
     */
    public static final Integer CONTENT_STATUS_8 = 8;

    /**
     * 暂停已提交
     */
    public static final Integer CONTENT_STATUS_10 = 10;

    /**
     * 暂停未通过
     */
    public static final Integer CONTENT_STATUS_11 = 11;

    /**
     * 取消暂停
     */
    public static final Integer CONTENT_STATUS_12 = 12;

    /**
     * 已暂停
     */
    public static final Integer CONTENT_STATUS_13 = 13;

    /**
     * 取消已提交
     */
    public static final Integer CONTENT_STATUS_15 = 15;

    /**
     * 取消未通过
     */
    public static final Integer CONTENT_STATUS_16 = 16;

    /**
     * 已取消
     */
    public static final Integer CONTENT_STATUS_17 = 17;

    /**
     * 事项操作类型
     */
    public static final Map<Integer, String> OPERATE_STATUS = new HashMap<>();


    /**
     * 新建
     */
    public static final Integer OPERATE_STATUS_0 = 0;

    /**
     * 变更
     */
    public static final Integer OPERATE_STATUS_1 = 1;

    /**
     * 暂停
     */
    public static final Integer OPERATE_STATUS_2 = 2;

    /**
     * 取消
     */
    public static final Integer OPERATE_STATUS_3 = 3;

    static {
        DIRECTORY_CHANGE_STATUS.put(DIRECTORY_CHANGE_APPLY, "待申请");
        DIRECTORY_CHANGE_STATUS.put(DIRECTORY_CHANGE_SUBMIT_AUDIT, "待提交审核");
        DIRECTORY_CHANGE_STATUS.put(DIRECTORY_CHANGE_AUDIT, "待审核");
        DIRECTORY_CHANGE_STATUS.put(DIRECTORY_CHANGE_PASS, "审核通过");
        DIRECTORY_CHANGE_STATUS.put(DIRECTORY_CHANGE_NOT_PASS, "审核不通过");

        CONTENT_STATUS.put(CONTENT_STATUS_0, "新建");
        CONTENT_STATUS.put(CONTENT_STATUS_1, "待审核");
        CONTENT_STATUS.put(CONTENT_STATUS_2, "审核未通过");
        CONTENT_STATUS.put(CONTENT_STATUS_3, "审核通过");
        CONTENT_STATUS.put(CONTENT_STATUS_4, "变更未提交");
        CONTENT_STATUS.put(CONTENT_STATUS_5, "变更待审核");
        CONTENT_STATUS.put(CONTENT_STATUS_6, "变更未通过");
        CONTENT_STATUS.put(CONTENT_STATUS_7, "变更通过");
        CONTENT_STATUS.put(CONTENT_STATUS_8, "已发布");
        CONTENT_STATUS.put(CONTENT_STATUS_10, "暂停待审核");
        CONTENT_STATUS.put(CONTENT_STATUS_11, "暂停未通过");
        CONTENT_STATUS.put(CONTENT_STATUS_12, "取消暂停");
        CONTENT_STATUS.put(CONTENT_STATUS_13, "已暂停");
        CONTENT_STATUS.put(CONTENT_STATUS_15, "取消待审核");
        CONTENT_STATUS.put(CONTENT_STATUS_16, "取消未通过");
        CONTENT_STATUS.put(CONTENT_STATUS_17, "已取消");


        OPERATE_STATUS.put(OPERATE_STATUS_0, "新建");
        OPERATE_STATUS.put(OPERATE_STATUS_1, "变更");
        OPERATE_STATUS.put(OPERATE_STATUS_2, "暂停");
        OPERATE_STATUS.put(OPERATE_STATUS_3, "取消");
    }

    public static final Map<Integer, String> DIRECTORY_AUDIT_STATUS = new LinkedHashMap<>();
    static {
        DIRECTORY_AUDIT_STATUS.put(DIRECTORY_PENDING_AUDIT, "待审核");
        DIRECTORY_AUDIT_STATUS.put(DIRECTORY_PENDING_PUBLISH, "待发布");
        DIRECTORY_AUDIT_STATUS.put(DIRECTORY_AUDIT_NOT_PASS, "审核不通过");
        DIRECTORY_AUDIT_STATUS.put(DIRECTORY_PUBLISH, "发布");
    }

    //统计分析类型：通用要素
    public static final Integer ANALYSIS_NOMAL_TYPE = 1;
    //统计分析类型：数字比较要素:废弃
    public static final Integer ANALYSIS_NUMBER_TYPE = 2;
    //统计分析类型：时间压缩比率
    public static final Integer ANALYSIS_TIME_REDUCE_TYPE = 3;
    //统计分析类型：跑动次数
    public static final Integer ANALYSIS_TIMES_TYPE = 4;
    //统计分析类型：免交材料比率
    public static final Integer  ANALYSIS_MATERIAL_TYPE = 5;


    //业务要素类型
    public static final LinkedHashMap<Integer, String> ELEMENT_TYPE_MAP = new LinkedHashMap<Integer, String>();
    public static final Integer ELEMENT_TYPE_TEXT=1;
    /**
     * 下拉菜单
     */
    public static final  Integer ELEMENT_TYPE_XL=3;
    /**
     * 单选框
     */
    public static final  Integer ELEMENT_TYPE_RADIO=4;
    /**
     * 复选框
     */
    public static final  Integer ELEMENT_TYPE_CHECK=5;
    /**
     * 下拉多选控件
     */
    public static final  Integer ELEMENT_TYPE_MORECHECK=10;
    static {
        ELEMENT_TYPE_MAP.put(ELEMENT_TYPE_TEXT, "文本框");
        ELEMENT_TYPE_MAP.put(ELEMENT_TYPE_XL, "下拉菜单");
        ELEMENT_TYPE_MAP.put(ELEMENT_TYPE_RADIO, "单选框");
        ELEMENT_TYPE_MAP.put(ELEMENT_TYPE_CHECK, "复选框");
        ELEMENT_TYPE_MAP.put(ELEMENT_TYPE_MORECHECK, "下拉多选控件");
    }


    public static final  Map<String,String> SHOW_TONGJI_MAP = new HashMap<>();
    static {
        SHOW_TONGJI_MAP.put("跑动次数", "{'1':'跑动1次','0':'跑动0次'}");
        SHOW_TONGJI_MAP.put("时间压缩比率", "{'时间压缩比率':'时间压缩比率'}");
        SHOW_TONGJI_MAP.put("办理深度", "{'互联网咨询':'互联网咨询'}");
    }


    /**
     * 行政许可 事项主键
     */
    public static String XZXK_ELEMENT_OID = "402881fd5fb4a7ed015fb4ac939a0002";


    public static Integer NOT_CLAIM = 4;

    /***
     *  认领待审核
     */
    public static Integer CLAIM_AUDIT = 0;

    /***
     *  取消认领/未认领
     */
    public static Integer CLAIM_CANCEL = 1;

    /***
     *  认领不通过
     */
    public static Integer CLAIM_NOT_PASS = 2;

    /***
     *  认领通过
     */
    public static Integer CLAIM_PASS = 3;


    public static final Map<Integer, String> DIRECTORY_CLAIM_FLAG = new LinkedHashMap<>();
    static {
        DIRECTORY_CLAIM_FLAG.put(CLAIM_AUDIT, "认领待审核");
        DIRECTORY_CLAIM_FLAG.put(CLAIM_CANCEL, "取消认领");
        DIRECTORY_CLAIM_FLAG.put(CLAIM_NOT_PASS, "认领不通过");
        DIRECTORY_CLAIM_FLAG.put(CLAIM_PASS, "认领通过");
        DIRECTORY_CLAIM_FLAG.put(NOT_CLAIM, "未认领");
    }

    public static final String USER_TYPE_MANAGER = "管理人员";


    /***
     * @description: 用于事项统计的lableList,对应SxFactorConStatistic的facLableOid字段,key是事项类型
     * @author: xiayj
     * @date: 2021/6/16
     */
    public static Map<String,List<String>> STATISTIC_LABLE_MAP = new HashMap<>();

}
