package com.zfsoft.ocr.util;

import com.zfsoft.cases.util.PropertiesUtil;

/**
 * 阿里云识别工具类
 * 
 * @author chenbw
 * @date 2019年6月6日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
public class AliyunOcrUtil {

    /** 阿里云地址 */
    public static final String AILIYUN_URL = "https://stamp.market.alicloudapi.com/";

    /** 签章接口 */
    public static final String AILIYUN_URL_SEAL_URL = "api/predict/ocr_official_seal";

    /** 无印章返回code */
    public static String ALIYUN_OCR_NO_STAMP = "464";

    /** 成功标识 */
    public static String SUCCESS = "success";

    /** 识别结果 */
    public static String ITEMS = "items";

    /** 百度配置文件配置异常提示 */
    public static final String BAIDU_CONFIG_MESSAGE = "请按照部署手册配置百度接口参数";

    /** 阿里云自定义ocr模板识别地址 */
    public static final String AILIYUN_TEMPLATE_URL = "https://ocrdiy.market.alicloudapi.com";

    public static final String AILIYUN_TEMPLATE_URL_OCR = "/api/predict/ocr_sdt";

    public static final String AILIYUN_TEMPLATE_URL_METHOD = "POST";

    public static String AILIYUN_TEMPLATE_APP_CODE = "";

    static {
        PropertiesUtil pu = new PropertiesUtil("interface_param.properties");
        AILIYUN_TEMPLATE_APP_CODE = pu.readProperty("aliyun.ocr.appCode");
    }

}
