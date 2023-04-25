package com.zfsoft.ocr.util;


import java.util.HashMap;
import java.util.Map;


/**
 * 百度OCR识别错误编号
 * 
 * @description
 * @author chenbw
 * @date 2018年4月12日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
public class BaiduErrorCode {

    /** 服务器内部错误，请再次请求 */
    public static final String CODE_1 = "1";

    /** 服务暂不可用，请再次请求 */
    public static final String CODE_2 = "2";

    /** 调用的API不存在，请检查后重新尝试 */
    public static final String CODE_3 = "3";

    /** 集群超限额 */
    public static final String CODE_4 = "4";

    /** 无权限访问该用户数据 */
    public static final String CODE_6 = "6";

    /** 每天请求量超限额 */
    public static final String CODE_17 = "17";

    /** QPS超限额 */
    public static final String CODE_18 = "18";

    /** 请求总量超限额 */
    public static final String CODE_19 = "19";

    /** 无效的access_token参数，请检查后重新尝试 */
    public static final String CODE_100 = "100";

    /** access_token无效 */
    public static final String CODE_110 = "110";

    /** access token过期 */
    public static final String CODE_111 = "111";

    /** 服务器内部错误，请再次请求 */
    public static final String CODE_282000 = "282000";

    /** 请求中包含非法参数，请检查后重新尝试 */
    public static final String CODE_216100 = "216100";

    /** 缺少必须的参数，请检查参数是否有遗漏 */
    public static final String CODE_216101 = "216101";

    /** 请求了不支持的服务，请检查调用的url */
    public static final String CODE_216102 = "216102";

    /** 请求中某些参数过长，请检查后重新尝试 */
    public static final String CODE_216103 = "216103";

    /** appid不存在，请重新核对信息是否为后台应用列表中的appid */
    public static final String CODE_216110 = "216110";

    /** 图片为空，请检查后重新尝试 */
    public static final String CODE_216200 = "216200";

    /** 上传的图片格式错误，现阶段我们支持的图片格式为：PNG、JPG、JPEG、BMP，请进行转码或更换图片 */
    public static final String CODE_216201 = "216201";

    /** 上传的图片大小错误，现阶段我们支持的图片大小为：base64编码后小于4M，分辨率不高于4096*4096，请重新上传图片 */
    public static final String CODE_216202 = "216202";

    /** 上传的图片base64编码有误，请校验base64编码方式，并重新上传图片 */
    public static final String CODE_216203 = "216203";

    /** 识别错误，请再次请求 */
    public static final String CODE_216630 = "216630";

    /** 识别银行卡错误，出现此问题的原因一般为：您上传的图片非银行卡正面，上传了异形卡的图片或上传的银行卡正品图片不完整 */
    public static final String CODE_216631 = "216631";

    /** 识别身份证错误，出现此问题的原因一般为：您上传了非身份证图片或您上传的身份证图片不完整 */
    public static final String CODE_216633 = "216633";

    /** 检测错误，请再次请求 */
    public static final String CODE_216634 = "216634";

    /** {参数名} 请求参数缺失 */
    public static final String CODE_282003 = "282003";

    /** 处理批量任务时发生部分或全部错误，请根据具体错误码排查 */
    public static final String CODE_282005 = "282005";

    /** 批量任务处理数量超出限制，请将任务数量减少到10或10以下 */
    public static final String CODE_282006 = "282006";

    /** URL长度超过1024字节或为0 */
    public static final String CODE_282114 = "282114";

    /** request id 不存在 */
    public static final String CODE_282808 = "282808";

    /** 返回结果请求错误（不属于excel或json） */
    public static final String CODE_282809 = "282809";

    /** 图像识别错误 */
    public static final String CODE_282810 = "282810";

    /** 入参格式有误，可检查下图片编码、代码格式是否有误 */
    public static final String CODE_283300 = "283300";

    /** 服务器内部错误，请再次请求 */
    public static final String CODE_336000 = "336000";

    /** 入参格式有误，比如缺少必要参数、图片base64编码错误等等，可检查下图片编码、代码格式是否有误 */
    public static final String CODE_336001 = "336001";

    public final static Map<String, String> BAIDU_ERROR_CODE_MAP = new HashMap<String, String>();
    static {
        BAIDU_ERROR_CODE_MAP.put(CODE_1, "服务器内部错误，请再次请求");

        BAIDU_ERROR_CODE_MAP.put(CODE_2, "服务暂不可用，请再次请求");

        BAIDU_ERROR_CODE_MAP.put(CODE_3, "调用的API不存在，请检查后重新尝试");

        BAIDU_ERROR_CODE_MAP.put(CODE_4, "集群超限额");

        BAIDU_ERROR_CODE_MAP.put(CODE_6, "无权限访问该用户数据");

        BAIDU_ERROR_CODE_MAP.put(CODE_17, "每天请求量超限额");

        BAIDU_ERROR_CODE_MAP.put(CODE_18, "QPS超限额");

        BAIDU_ERROR_CODE_MAP.put(CODE_19, "请求总量超限额");

        BAIDU_ERROR_CODE_MAP.put(CODE_100, "无效的access_token参数，请检查后重新尝试");

        BAIDU_ERROR_CODE_MAP.put(CODE_110, "access_token无效");

        BAIDU_ERROR_CODE_MAP.put(CODE_111, "access token过期");

        BAIDU_ERROR_CODE_MAP.put(CODE_282000, "服务器内部错误，请再次请求");

        BAIDU_ERROR_CODE_MAP.put(CODE_216100, "请求中包含非法参数，请检查后重新尝试");

        BAIDU_ERROR_CODE_MAP.put(CODE_216101, "缺少必须的参数，请检查参数是否有遗漏");

        BAIDU_ERROR_CODE_MAP.put(CODE_216102, "请求了不支持的服务，请检查调用的url");

        BAIDU_ERROR_CODE_MAP.put(CODE_216103, "请求中某些参数过长，请检查后重新尝试");

        BAIDU_ERROR_CODE_MAP.put(CODE_216110, "appid不存在，请重新核对信息是否为后台应用列表中的appid");

        BAIDU_ERROR_CODE_MAP.put(CODE_216200, "图片为空，请检查后重新尝试");

        BAIDU_ERROR_CODE_MAP.put(CODE_216201, "上传的图片格式错误，现阶段我们支持的图片格式为：PNG、JPG、JPEG、BMP，请进行转码或更换图片");

        BAIDU_ERROR_CODE_MAP.put(CODE_216202, "上传的图片大小错误，现阶段我们支持的图片大小为：base64编码后小于4M，分辨率不高于4096*4096，请重新上传图片");

        BAIDU_ERROR_CODE_MAP.put(CODE_216203, "上传的图片base64编码有误，请校验base64编码方式，并重新上传图片");

        BAIDU_ERROR_CODE_MAP.put(CODE_216630, "识别错误，请再次请求");

        BAIDU_ERROR_CODE_MAP.put(CODE_216631, "识别银行卡错误，出现此问题的原因一般为：您上传的图片非银行卡正面，上传了异形卡的图片或上传的银行卡正品图片不完整");

        BAIDU_ERROR_CODE_MAP.put(CODE_216633, "识别身份证错误，出现此问题的原因一般为：您上传了非身份证图片或您上传的身份证图片不完整");

        BAIDU_ERROR_CODE_MAP.put(CODE_216634, "检测错误，请再次请求");

        BAIDU_ERROR_CODE_MAP.put(CODE_282003, " {参数名}   请求参数缺失");

        BAIDU_ERROR_CODE_MAP.put(CODE_282005, "处理批量任务时发生部分或全部错误，请根据具体错误码排查 ");

        BAIDU_ERROR_CODE_MAP.put(CODE_282006, "批量任务处理数量超出限制，请将任务数量减少到10或10以下");

        BAIDU_ERROR_CODE_MAP.put(CODE_282114, "URL长度超过1024字节或为0");

        BAIDU_ERROR_CODE_MAP.put(CODE_282808, "request id 不存在 ");

        BAIDU_ERROR_CODE_MAP.put(CODE_282809, "返回结果请求错误（不属于excel或json");

        BAIDU_ERROR_CODE_MAP.put(CODE_282810, "图像识别错误");

        BAIDU_ERROR_CODE_MAP.put(CODE_283300, "入参格式有误，可检查下图片编码、代码格式是否有误");

        BAIDU_ERROR_CODE_MAP.put(CODE_336000, "服务器内部错误，请再次请求");

        BAIDU_ERROR_CODE_MAP.put(CODE_336001, "入参格式有误，比如缺少必要参数、图片base64编码错误等等，可检查下图片编码、代码格式是否有误");

    }
}
