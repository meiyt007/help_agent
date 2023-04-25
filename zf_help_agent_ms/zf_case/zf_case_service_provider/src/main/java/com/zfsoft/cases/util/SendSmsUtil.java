package com.zfsoft.cases.util;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;


/**
 *
 * @Description:发送手机短信
 * 岱亿短信官网：http://www.daiyicloud.com/
 * 接口参见：DOC 平台接口说明-HTTP.doc
 * @author:zhujiajian
 * @date 2017年5月23日 上午10:57:36
 * @copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@Component
public class SendSmsUtil {

	/** 普通短信发送 */
	private static String TYPE_1 = "pt";
	/** 岱亿短信官网URL */
	private static final String url = "http://api.daiyicloud.com/asmx/smsservice.aspx";
	/** 名称 */
	private static String name;
	/** 密码 */
	private static String pwd;
	/**  */
	private static String sign;
	/** 是否启用短信通知 */
	private static String status;
	/** 线程池 */
	private static  ExecutorService fixedThreadPool =null;

	@Autowired
	private Environment environment;

	static {
		//fixedThreadPool = Executors.newFixedThreadPool(5);
		fixedThreadPool = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
	}

	@PostConstruct
	public void getConfig() {
		name = environment.getProperty("zfsoft.sms.name");
		pwd = environment.getProperty("zfsoft.sms.pwd");
		sign = environment.getProperty("zfsoft.sms.sign");
		status = environment.getProperty("zfsoft.sms.status");
	}

	/**
	 * 发送短信
	 *
	 * @param phone
	 *            手机号码
	 * @param message
	 *            发送sms信息
	 */
	public static void sendSms(String phone, String message) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				if(!StrUtil.isEmpty(status)||"true".equals(status)){//未启用短信
					HttpRequester req = new HttpRequester();
					Map<String,String> param = new HashMap<>();
					try {
						param.put("name", name);
						param.put("pwd", pwd);
						param.put("content", message);
						param.put("mobile", phone);
						param.put("sign", sign);
						param.put("type", TYPE_1);
						System.out.println("短信发送中。。。"+message);
						HttpRespons resp = req.sendPost(url, param);
						String content = resp.getContent();
						if(StrUtil.isNotEmpty(content)){
							String[] ret = content.split(",");
							if("0".equals(ret[0])){
								System.out.println(ret[0]);
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		fixedThreadPool.execute(thread);
	}

}
