package com.zfsoft.superwindow.util;

import com.google.common.collect.Maps;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: kkfan
 * @create: 2020-10-22 14:28:42
 * @description: 系统静态参数
 */
public class SysCode {

    /** 是否状态    1 -- YES   0 -- NO */
    public interface STATUS {
        /** 是 */
        Integer YES = 1;
        /** 否 */
        Integer NO = 0;

        Map<Integer, String> STATUS_MAP = Maps.newHashMap();
    }

    /** 启禁用状态    1 -- 启用   0 -- 禁用  2--暂存 */
    public interface ABLE_STATUS {
        /** 启用 */
        Integer YES = 1;
        /** 禁用 */
        Integer NO = 0;
        /** 暂存 */
        Integer ZC = 2;
    }

    /** 删除状态    1 -- 已删除   0 -- 未删除 */
    public interface DELETE_STATUS {
        /** 已删除 */
        Integer YES = 1;
        /** 未删除 */
        Integer NO = 0;
    }

    /** 人员状态    1 -- 忙碌   0 -- 空闲 */
    public interface ADVI_STATE {
        /** 空闲 */
        Integer FREE = 0;
        /** 忙碌 */
        Integer BUSY = 1;
        Map<Integer, String> ADVI_STATE_MAP = Maps.newHashMap();
    }

    /** 后援呼叫状态    1 -- 忙碌   0 -- 空闲 */
    public interface REPLY_STATE {
        /** 等待 */
        Integer WAIT = 0;
        /** 接受 */
        Integer ACCEPT = 1;
        /** 拒绝 */
        Integer REFUSE = 2;
        Map<Integer, String> REPLY_STATE_MAP = Maps.newHashMap();
    }

    public static final String ZHYC_FLAG_PLAT = "3";

//办件状态：办件中
    public static final Integer CASE_STATUS_ING = 2;
    /**
     * 办件待预审
     */
    public static final Integer CASE_STATUS_DAI = 1;
    /**
     * 办件办结
     */
    public static final Integer CASE_STATUS_OVER = 3;
    /**
     * 办件作废
     */
    public static final Integer CASE_STATUS_ABOLISH = 4;


    public interface COMBO_CASE_FINAL{
        Map<String, String> COMBO_CASE_FINAL_MAP = Maps.newHashMap();
        Integer COMBO_CASE_FINAL_OK = 1; // 受理通过
        Integer COMBO_CASE_FINAL_NO = 2; // 不予受理
    }

    public interface HOLIDAY_TYPE{
        Map<String, String> HOLIDAY_MAP = Maps.newHashMap();
        String HOLIDAY_NATURE = "N"; // 自然日
        String HOLIDAY_WORK = "W"; // 工作日
    }
    /**
     * 服务对象
     */
//    public interface  SERVICE_OBJECT{
    public static Map<String, String> SERVICE_OBJECT_MAP = Maps.newHashMap();
        /** 服务对象： 自然人 **/
        public static final String PERSONAL = "1";
        /** 服务对象： 企业法人 **/
        public static final String NATIONALIZE_PERSON = "2";
        /**服务对象： 事业法人**/
        public static final String SOCIAL_ORGANIZATION = "3";
        /**服务对象： 社会组织法人**/
        public static final String COLLEGE_PERSON = "4";
        /** 服务对象： 非法人企业**/
        public static final String PRIVATE_PERSON = "5";
        /** 服务对象：行政机关**/
        public static final String FOREIGN_PERSON = "6";
        /**服务对象： 其他组织**/
        public static final String OTHER_PERSON = "9";
//    }

    //行使层级
    public static final Map<String, String> LEVEL_MAP = new LinkedHashMap<>();
    /**
     * 区划层级：省、自治区、直辖市、特别行政区
     */
    public static final String FIRST_LEVEL = "402881fa56e8b41c0156e8ba5e2a0009";
    /**
     * 区划层级：地级市、自治州、地区、盟
     */
    public static final String SECOND_LEVEL = "402881fa56e8b41c0156e8ba5e33000a";
    /**
     * 区划层级：县、市辖区、县级市、旗
     */
    public static final String THIRD_LEVEL = "402881fa56e8b41c0156e8ba5e46000c";
    /**
     * 区划层级：镇、乡、街道办事处、民族乡、区公所
     */
    public static final String FOURTH_LEVEL = "40288008594a6e7401594d56b2150000";
    /**
     * 区划层级：自然村、居委会、社区
     */
    public static final String FIFTH_LEVEL = "40288008594a6e7401594d5c17080001";

    /**
     * 国家级/局(署、会)
     */
    public static final String NATION_LEVEL ="402881f35aa67918015aa7b8e3b0005e";
    /**
     * 省级/直属
     */
    public static final String PROVINCE_LEVEL = "402881f35aa67918015aa7b992b7005f";
    /**
     * 市级/隶属
     */
    public static final String CITY_LEVEL = "402881f35aa67918015aa7ba27490060";
    /**
     * 县级
     */
    public static final String COUNTY_LEVEL = "402881f35aa67918015aa7bad17a0061";
    /**
     * 镇（乡、街道）级
     */
    public static final String TOWN_LEVEL = "402881f35aa67918015aa7bb5f1f0062";
    /**
     * 村（社区）级
     */
    public static final String VILLAGE_LEVEL = "402881f35aa67918015aa7bbe0c40063";
    /**
     * 分级管理
     */
    public static final String PART_LEVEL = "2c287bb6689d76b101689d7b81800004";

    //实施清单 发布状态
    public static final Short SERVICE_PUBLISH = 3;

    /**
     * 到现场次数
     */
    public interface COUNT_TO_LIVE{
        Map<Integer, String> COUNT_TO_LIVE_MAP  = Maps.newHashMap();
        //零次
        Integer  ZERO= 0;
        //一次
        Integer  ONE= 1;
        //两次
        Integer  TWO= 2;
        //多次
        Integer  MORE= 3;
    }

    /**
     * 办理形式
     */
    public interface HANDLE_FORM {
         Map<String, String> HANDLE_FORM_MAP = Maps.newHashMap();
        /**办理形式： 窗口办理*/
         String HANDLE_WINDOW = "0";
        /**办理形式： 网上办理*/
         String HANDLE_NET = "1";
        /**办理形式： 一体化办理*/
        String HANDLE_WIN_AND_NET = "2";
    }

    /**
     * 目录状态
     */
    public interface DIRECTORY_STATUS {
        /**未发布*/
        Integer DIRECTORY_STATUS_NO = 0;
        /**发布*/
        Integer DIRECTORY_STATUS_YES = 1;
        /**取消待发布*/
        Integer DIRECTORY_STATUS_CANCEL = 4;
    }

    /** 初始化 */
   static  {
        ADVI_STATE.ADVI_STATE_MAP.put(ADVI_STATE.FREE, "空闲");
        ADVI_STATE.ADVI_STATE_MAP.put(ADVI_STATE.BUSY, "忙碌");

        STATUS.STATUS_MAP.put(STATUS.YES, "是");
        STATUS.STATUS_MAP.put(STATUS.NO, "否");

        REPLY_STATE.REPLY_STATE_MAP.put(REPLY_STATE.WAIT, "等待");
        REPLY_STATE.REPLY_STATE_MAP.put(REPLY_STATE.ACCEPT, "接受");
        REPLY_STATE.REPLY_STATE_MAP.put(REPLY_STATE.REFUSE, "拒绝");
        HOLIDAY_TYPE.HOLIDAY_MAP.put(HOLIDAY_TYPE.HOLIDAY_NATURE, "自然日");
        HOLIDAY_TYPE.HOLIDAY_MAP.put(HOLIDAY_TYPE.HOLIDAY_WORK, "工作日");

        SERVICE_OBJECT_MAP.put(PERSONAL, "自然人");
        SERVICE_OBJECT_MAP.put(SOCIAL_ORGANIZATION, "事业法人");
        SERVICE_OBJECT_MAP.put(NATIONALIZE_PERSON, "企业法人");
        SERVICE_OBJECT_MAP.put(PRIVATE_PERSON, "非法人企业");
        SERVICE_OBJECT_MAP.put(FOREIGN_PERSON, "行政机关");
        SERVICE_OBJECT_MAP.put(COLLEGE_PERSON, "社会组织法人");
        SERVICE_OBJECT_MAP.put(OTHER_PERSON, "其他组织");

        COUNT_TO_LIVE.COUNT_TO_LIVE_MAP.put(COUNT_TO_LIVE.ZERO, "0次");
        COUNT_TO_LIVE.COUNT_TO_LIVE_MAP.put(COUNT_TO_LIVE.ONE, "1次");
        COUNT_TO_LIVE.COUNT_TO_LIVE_MAP.put(COUNT_TO_LIVE.TWO, "2次");
        COUNT_TO_LIVE.COUNT_TO_LIVE_MAP.put(COUNT_TO_LIVE.MORE, "多次");

        HANDLE_FORM.HANDLE_FORM_MAP.put(HANDLE_FORM.HANDLE_WINDOW, "窗口办理");
        HANDLE_FORM.HANDLE_FORM_MAP.put(HANDLE_FORM.HANDLE_NET, "网上办理");
        HANDLE_FORM.HANDLE_FORM_MAP.put(HANDLE_FORM.HANDLE_WIN_AND_NET, "一体化办理");

        LEVEL_MAP.put(FIRST_LEVEL, PROVINCE_LEVEL);
        LEVEL_MAP.put(SECOND_LEVEL, CITY_LEVEL);
        LEVEL_MAP.put(THIRD_LEVEL, COUNTY_LEVEL);
        LEVEL_MAP.put(FOURTH_LEVEL, TOWN_LEVEL);
        LEVEL_MAP.put(FIFTH_LEVEL, VILLAGE_LEVEL);
    }
}
