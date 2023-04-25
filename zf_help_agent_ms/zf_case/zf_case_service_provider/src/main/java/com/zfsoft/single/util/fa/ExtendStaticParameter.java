package com.zfsoft.single.util.fa;

import java.util.HashMap;
import java.util.Map;

/**
 * 扩展静态信息
 * 
 * @author cbc   
 * @date 2019年6月17日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
public class ExtendStaticParameter {

    /**
     * 是否开通老年人乘车功能。
     */
    public static final Map<String, String> CAR_FUN_TYPE_MAP = new HashMap<String, String>();
    /**
     * 开通
     */
    public static final String CAR_FUN_TYPE_YES = "Y";
    /**
     * 不开通
     */
    public static final String CAR_FUN_TYPE_NO = "N";
    /**
     * 按钮类型-文本框
     */
    public static final String CAR_FUN_BUTTON_TYPE_TEXT = "0";
    /**
     * 按钮类型-单选框
     */
    public static final String CAR_FUN_BUTTON_TYPE_RADIO = "1";
    /**
     * 是否本人办理---否【待办：年龄>80】
     */
    public static String CASE_OPTION_SELF_NO_OID = "2c287ba76b2fa80f016b30fa3f24000d";
    /**
     * 有无暂住证-----无【地区是外地的：地区不包含黄石市】
     */
    public static String CASE_LICE_OPTION_NO_OID = "2c287ba76b63ee99016b640dca8b0000";
    /**
     * 有无暂住证-----有【地区是外地的：地区不包含黄石市】
     */
    public static String CASE_LICE_OPTION_YES_OID = "2c287ba76b63ee99016b640dcb490001";
    /**
     * 扩展字段-是否开通乘车-是【开通：年龄>65】
     */
    public static String CASE_COLUMN_EXTEND_CAR_FUN_STATUS = "CAR_FUN_STATUS";
    /**
     * 扩展字段-经办人名称
     */
    public static String CASE_COLUMN_EXTEND_JBR_NAME = "JBR_NAME";
    /**
     * 扩展字段-经办人身份证
     */
    public static String CASE_COLUMN_EXTEND_JBR_ID_CARD = "JBR_ID_CARD";
    /**
     * 老年人优待事项OID
     */
    public static String SERVICE_OLD_OID = "8a850241677792fd01677861cde136b5";
    /**
     * 当前办件校验的年龄条件
     */
    public static String CASE_OPTION_AGE_CONDITION = "CASE_OPTION_AGE_CONDITION";

    static {
        CAR_FUN_TYPE_MAP.put(CAR_FUN_TYPE_YES, "开通");
        CAR_FUN_TYPE_MAP.put(CAR_FUN_TYPE_NO, "不开通");
        
        PropertiesUtil pu = new PropertiesUtil("application.properties");
        CASE_OPTION_SELF_NO_OID = pu.readProperty("CASE_OPTION_SELF_NO_OID");
        CASE_LICE_OPTION_NO_OID = pu.readProperty("CASE_LICE_OPTION_NO_OID");
        CASE_LICE_OPTION_YES_OID = pu.readProperty("CASE_LICE_OPTION_YES_OID");
        CASE_COLUMN_EXTEND_CAR_FUN_STATUS = pu.readProperty("CASE_COLUMN_EXTEND_CAR_FUN_STATUS");
        SERVICE_OLD_OID = pu.readProperty("SERVICE_OLD_OID");
        CASE_COLUMN_EXTEND_JBR_NAME = pu.readProperty("CASE_COLUMN_EXTEND_JBR_NAME");
        CASE_COLUMN_EXTEND_JBR_ID_CARD = pu.readProperty("CASE_COLUMN_EXTEND_JBR_ID_CARD");
    }
    
}
