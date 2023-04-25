package com.zfsoft.cases.util;

import java.util.Date;

/**
 * @（#）: BaseStaticParameter
 * @description: 常量
 * @author: wangwg
 * @date: 2020/10/23
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
public class BaseStaticParameter {
    /**
     * 自然日
     **/
    public static final String HOLIDAY_NATURE = "N";
    /**
     * 工作日
     **/
    public static final String HOLIDAY_WORK = "W";
    /**
     * 每天打2次卡
     **/
    public static final String WORK_TIME_2 = "2";
    /**
     * 每天打4次卡
     **/
    public static final String WORK_TIME_4 = "4";
    /**
     * 班次
     **/
    public static String WORK_TIME_NUM = "4";
    /**
     * 上午上班时间
     **/
    public static Date MORNING_WORK_ON_TIME = null;
    /**
     * 上午下班时间
     **/
    public static Date MORNING_WORK_OFF_TIME = null;
    /**
     * 下午上班时间
     **/
    public static Date AFTERNOON_WORK_ON_TIME = null;
    /**
     * 下午下班时间
     **/
    public static Date AFTERNOON_WORK_OFF_TIME = null;
    /**
     * 每天工作的小时数
     **/
    public static int WORK_HOUR = 0;
    /**
     * 每天工作的分钟数
     **/
    public static int WORK_MINTUS = 0;
    /**
     * 当班次为4时，每天休息的分钟数
     **/
    public static int WORK_REST_MINTUS = 0;


    /**
     * 证照类型 个人
     */
    public static String ZZLX_GR_CODE = "ZJLXGR";

    /**
     * 证照类型 法人
     */
    public static String ZZLX_FR_CODE = "ZJLXFR";

    /**
     * 材料提交方式：0-智能制作材料，1-免于提交材料，2-自备材料/上传
     */
    public static short MADE_MATERIAL_TYPE_AUTO = 0;

    public static short MADE_MATERIAL_TYPE_NO_SUBMIT = 1;

    public static short MADE_MATERIAL_TYPE_UPLOAD = 2;

    /**
     * 用户类型;1-导服人员，2-帮代办人员，3-委办局
     */
    public static String USER_TYPE_DF = "1";

    public static String USER_TYPE_BDB = "2";

    public static String USER_TYPE_WBJ = "3";

    public static String USER_TYPE_DFZW = "导服人员";

    public static String USER_TYPE_BDBZW = "帮代办人员";

    public static String USER_TYPE_WBJZW = "委办局人员";

    /**
     * 帮代办人员类型;1-快捷帮办，2-圆桌帮办
     */
    public static String HA_TYPE_KJ = "1";

    public static String HA_TYPE_YZ = "2";

    public static String HA_TYPE_KJZW = "快捷帮办";

    public static String HA_TYPE_YZZW = "圆桌帮办";

    /**
     * 状态;1-离线、2-忙碌、3-空闲
     */
    public static String STATUS_LX = "1";

    public static String STATUS_ML = "2";

    public static String STATUS_KX = "3";

    public static String STATUS_FUWZ = "4";
    public static String STATUS_LXZW = "离线";
    public static String STATUS_MLZW = "忙碌";
    public static String STATUS_KXZW = "空闲";

    public static String STATUS_FUWZZW = "服务中";

    /**
     * 1：互联网申请
     */
    public static String CASE_RESOURCE_ONLINE = "1";

    /**
     * 3：综窗登记
     */
    public static String CASE_RESOURCE_ONE_WINDOW = "3";

    /**
     * 4：蒙速办
     */
    public static String CASE_RESOURCE_APP = "4";

    /**
     * 5：一体机
     */
    public static String CASE_RESOURCE_MACHINE = "5";

    /**
     * 9：自建通过综窗登记
     * （指和自治区垂建业务系统和盟市自建业务系统对接后，对接方式是嵌入式的，
     * 即通过综窗直接跳转到自治区垂建业务系统页面或者盟市自建业务系统的事项办件)
     */
    public static String CASE_RESOURCE_ONESELF_BUILD_ONE_WINDOW = "9";

    /**
     * 10：自建系统登记
     * （指通过自治区垂建业务系统和盟市本级自建业务系统登记的办件，包含盟市自建综窗的办件）
     */
    public static String CASE_RESOURCE_ONESELF_BUILD_SYSTEM = "10";

    /**
     * 申请数量默认：1
     */
    public static String APPLY_NUMBER_ONE = "1";

    /**
     * 是否为代理人：0：否
     */
    public static String PROXY_FLAG_ZERO = "0";

    /**
     * 是否为代理人：1：是
     */
    public static String PROXY_FLAG_ONE = "1";

    /**
     * 自治区综窗申请人类型；自然人：1
     */
    public static String APPLY_USER_TYPE_ONE = "1";

    /**
     * 自治区综窗申请人类型；企业法人：2
     */
    public static String APPLY_USER_TYPE_TWO = "2";

    /**
     * 自治区综窗申请人类型；事业法人：3
     */
    public static String APPLY_USER_TYPE_THREE = "3";

    /**
     * 自治区综窗申请人类型；社会组织法人：4
     */
    public static String APPLY_USER_TYPE_FOUR = "4";

    /**
     * 自治区综窗申请人类型；非法人企业：5
     */
    public static String APPLY_USER_TYPE_FIVE = "5";

    /**
     * 自治区综窗申请人类型；行政机关：6
     */
    public static String APPLY_USER_TYPE_SIXE = "6";

    /**
     * 自治区综窗申请人类型；其他组织：9
     */
    public static String APPLY_USER_TYPE_NINE = "9";

    /**
     * 自治区办件类型，1：权力办件
     */
    public static String REG_CASE_TYPE_ONE = "1";

    /**
     * 自治区办件类型，0：服务办件
     */
    public static String REG_CASE_TYPE_ZERO = "0";
}
