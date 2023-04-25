package com.zfsoft.cases.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.ha.dbaccess.dao.DbHaSmsMapper;
import com.zfsoft.ha.dbaccess.data.DbHaSms;
import com.zfsoft.single.util.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
* Description: 黄浦区短信工具类

* @author zhaobf
* date: 2023/1/9 14:23
* @copyright 版权由上海卓繁信息技术股份有限公司拥有
*/
@Component
@Slf4j
public class SendHPSmsUtil {
	/**  */
	private static String interUrl;
	/** 是否启用短信通知 */
	private static String status;

	private static String sendSms;


	@Autowired
	private Environment environment;

//	@Autowired
	private static DbHaSmsMapper dbHaSmsMapper;

	@Autowired
	private SendHPSmsUtil(DbHaSmsMapper dbHaSmsMapper){
		SendHPSmsUtil.dbHaSmsMapper = dbHaSmsMapper;
	}

	@PostConstruct
	public void getConfig() {
		interUrl = environment.getProperty("zfsoft.inter.url");
		sendSms = environment.getProperty("zfsoft.inter.sendSms");
		status = environment.getProperty("zfsoft.inter.smsStatus");
	}


	/** 线程池 */
	private static final ExecutorService fixedThreadPool;


	static {
		//fixedThreadPool = Executors.newFixedThreadPool(5);
		fixedThreadPool = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
	}


	/**
	 * 发送短信  SendHPSmsUtil.sendHPSms("取号","17172190635","测试","234261805889966080","康翱翔");
	 *
	 *
	 * @param phone
	 *            手机号码
	 * @param message
	 *            发送sms信息
	 */
	public static void sendHPSms(String title,String phone, String message,String workUserId,String workUserName) {
		if("0".equals(status)){
			return;
		}
		Thread thread = new Thread(() -> {
			Map<String,Object> param = new HashMap<>();
			DbHaSms dbHaSms = new DbHaSms();
			try {
				String url = interUrl + sendSms ;
				param.put("phone", phone);
				param.put("message", message);
				param.put("workUserName", workUserName);
				System.out.println("短信发送中。。。"+message);
				String result = HttpRequestUtil.sendPost(url, param);
				dbHaSms.setTitle(title);
				dbHaSms.setWorkUserId(Long.valueOf(workUserId));
				dbHaSms.setWorkUserName(workUserName);
				dbHaSms.setPhone(phone);
				dbHaSms.setMessage(message);
				dbHaSms.setSendDate(new Date());
				dbHaSms.setCreateDate(new Date());
				dbHaSms.setUpdateDate(new Date());
				dbHaSms.setUpdateBy(workUserName);
				dbHaSms.setCreateBy(workUserName);
				log.info("短信结果：result={}",result);
				/**
				 * 封装企业信息
				 */
				JSONObject jsonObject1 = JSON.parseObject(result);
				dbHaSms.setResult("1");
				if(!"200".equals( jsonObject1.get("code").toString())){
					throw new Exception("第三方接口调用错误"+jsonObject1.getString("message"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				dbHaSms.setResult("2");
			}finally {
				dbHaSmsMapper.insertSelective(dbHaSms);
			}
		});
		fixedThreadPool.execute(thread);
	}

}
