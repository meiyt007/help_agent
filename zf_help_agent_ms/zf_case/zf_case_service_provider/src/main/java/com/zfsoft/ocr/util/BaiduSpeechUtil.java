package com.zfsoft.ocr.util;

import com.baidu.aip.speech.AipSpeech;
import com.zfsoft.cases.util.PropertiesUtil;

/**
 * 语音服务功能核心类
 *
 * @author cbc
 * @date 2019年2月27日
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
public class BaiduSpeechUtil {

	/** 设置APPID/AK/SK 如有问题 请联系 研发中心-黎英明 QQ:563356602 */
	public static final String APP_ID;
	public static final String API_KEY;
	public static final String SECRET_KEY;
	public static final String AIP_SPEECH_SPD = "5";
	public static final String AIP_SPEECH_PIT = "5";
	/** 语音识别接口 */
	public static final String BAIDU_SPEECH_REQUEST_URL_VOICE_TEXT = "voiceTextBySdk";
	/** 错误码，10000表示成功 */
	public static final String BAIDU_SPEECH_REQUEST_URL_VOICE_TEXT_CODE = "code";
	/** 错误提示信息 */
	public static final String BAIDU_SPEECH_REQUEST_URL_VOICE_TEXT_MESSAGE = "msg";
	/** 返回结果的内容 */
	public static final String BAIDU_SPEECH_REQUEST_URL_VOICE_TEXT_DATA = "data";
	/** 10000表示成功 */
	public static final String BAIDU_SPEECH_REQUEST_URL_VOICE_TEXT_SUCCESS = "10000";
	/** -1表示失败 */
	public static final String BAIDU_SPEECH_REQUEST_URL_VOICE_TEXT_NEGATIVE_ONE = "-1";
	/** 错误提示信息 */
	public static final String BAIDU_SPEECH_REQUEST_URL_ERR_MSG = "err_msg";
	/** 错误标示 */
	public static final String BAIDU_SPEECH_REQUEST_URL_ERR_NO = "err_no";
	/** 0表示成功 */
	public static final String BAIDU_SPEECH_REQUEST_URL_ERR_NO_DATA = "0";

	/********************** 以下为请求公共接口服务系统百度语音参数 ******************/

	/** Unit token */
	public static String BAIDU_SPEECH_ACCESSTOKEN ="";
	/** #Unit API接口地址 */
    public static final String BAIDU_SPEECH_TALK_URL;
    /** #Unit sceneId */
    public static final String BAIDU_SPEECH_SCENEID;
    /** #Unit TOKEN地址 */
    public static final String BAIDU_SPEECH_AUTH_HOST;
    /** #Unit sceneId bot */
    public static final String BAIDU_SPEECH_SCENEID_BOT;

	/** 百度配置文件配置异常提示 */
	public static final String BAIDU_CONFIG_MESSAGE = "请按照部署手册配置百度语音接口参数";

	static {
		PropertiesUtil pu = new PropertiesUtil("interface_param.properties");
		APP_ID = pu.readProperty("appId");
		API_KEY = pu.readProperty("clientId");
		SECRET_KEY = pu.readProperty("clientSecret");

//		BAIDU_SPEECH_ACCESSTOKEN = pu.readProperty("unit.accessToken");
		BAIDU_SPEECH_TALK_URL = pu.readProperty("unit.talkUrl");
		BAIDU_SPEECH_SCENEID = pu.readProperty("unit.sceneId");
		BAIDU_SPEECH_AUTH_HOST = pu.readProperty("unit.authHost");
		BAIDU_SPEECH_SCENEID_BOT = pu.readProperty("unit.sceneIdBot");
	}

	/**
	 * 初始化一个FaceClient
	 *
	 * @author cbc
	 * @date 2019年2月28日
	 * @return 返回FaceClient
	 */
	public static AipSpeech getAipSpeech() {
		return new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
	}

}
