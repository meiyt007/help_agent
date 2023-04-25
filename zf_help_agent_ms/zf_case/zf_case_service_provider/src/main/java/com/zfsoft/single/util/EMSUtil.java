package com.zfsoft.single.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.MessageDigest;
import java.util.*;

/**
 * EMS工具类
 */
@Component
public class EMSUtil {
	/**
	 * EMS接口基础路径
	 */
	private static String baseURL;
	/**
	 * 版本号（由EMS提供）
	 */
	private static String version;
	/**
	 * 授权码MAP(由EMS提供)
	 */
	public static Map<String, String> authorization_map = new HashMap<String, String>();;
	/**
	 * appSecret值(由EMS提供)
	 */
	private static String appSecret;
	/**
	 * appkey值(由EMS提供)
	 */
	private static String appkey;
	/**
	 * 普通订单导入接口
	 */
	private static String orderMethod;
	/**
	 * 普通订单导入接口
	 */
	private static String traceMethod;
	/**
	 * 授权码：地区,数据库区划主键,授权码
	 */
	private static String authorizations;
	//变量
	private static String[] arr;
	private static String[] authorizationArr;

	@Value("${zfsoft.ems.baseURL}")
	public void setBaseURL(String baseURL) {
		EMSUtil.baseURL = baseURL;
	}

	@Value("${zfsoft.ems.version}")
	public void setVersion(String version) {
		EMSUtil.version = version;
	}

	@Value("${zfsoft.ems.appSecret}")
	public void setAppSecret(String appSecret) {
		EMSUtil.appSecret = appSecret;
	}

	@Value("${zfsoft.ems.appkey}")
	public void setAppkey(String appkey) {
		EMSUtil.appkey = appkey;
	}

	@Value("${zfsoft.ems.orderMethod}")
	public void setOrderMethod(String orderMethod) {
		EMSUtil.orderMethod = orderMethod;
	}

	@Value("${zfsoft.ems.traceMethod}")
	public void setTraceMethod(String traceMethod) {
		EMSUtil.traceMethod = traceMethod;
	}

	@Value("${zfsoft.ems.authorizations}")
	public void setAuthorizations(String authorizations) {
		EMSUtil.authorizations = authorizations;
	}

	@PostConstruct
	public void init() {
		arr = authorizations.split(";");
		for (int i = 0; i < arr.length; i++) {
			authorizationArr = arr[i].split(",");
			authorization_map.put(authorizationArr[1], authorizationArr[2]);
		}
	}

	/**
	 * 调用统一物流接口方法
	 *
	 * @author sunch
	 * @date 2017年11月27日
	 * @param map
	 *            请求参数
	 */
	public static String doGet(Map<String, String> map) {
		// 云上api签名
//		map.put("authorization", authorization_map.get("8a5107085fbaea01015fbaecc01b0005"));
		map.put("app_key", appkey);
		map.put("version", version);
		map.put("method", orderMethod);
		map.put("format", "json");
		map.put("size", "1");
		map.put("timestamp", DateUtil.getDateAndTime("yyyy-MM-dd HH:mm:ss"));
		// 排序
		String content = getSortParams(map) + appSecret;
		// 加密
		String sign = sign(content);
//		System.out.println(sign);
		map.put("sign", sign);

		String body = toKey(map);
//		System.out.println("拼接的字符串>>>>>>>>>>>>>>>" + body);
		String reslut = null;
		try {
			reslut = HttpRequestUtil.sendHttpPost(baseURL, body, "UTF-8");
//			System.out.println("返回的结果为>>>>>>" + reslut);
			return reslut;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 运单状态查询接口
	 *
	 * @author fangga
	 * @date 2017年11月30日
	 * @param map
	 *            请求参数
	 * @return
	 */
	public static String queryTrace(Map<String, String> map) {
		// 云上api签名
//		map.put("authorization", authorization_map.get("8a5107085fbaea01015fbaecc01b0005"));
		map.put("app_key", appkey);
		map.put("version", version);
		map.put("method", traceMethod);
		map.put("format", "json");
		map.put("size", "1");
		map.put("timestamp", DateUtil.getDateAndTime("yyyy-MM-dd HH:mm:ss"));
		// 排序
		String content = getSortParams(map) + appSecret;
		// 加密
		String sign = sign(content);
//		System.out.println(sign);
		map.put("sign", sign);

		String body = toKey(map);
//		System.out.println("拼接的字符串>>>>>>>>>>>>>>>" + body);
		String reslut = null;
		try {
			reslut = HttpRequestUtil.sendHttpPost(baseURL, body, "UTF-8");
//			System.out.println("返回的结果为>>>>>>" + reslut);
			return reslut;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 加密
	 *
	 * @author sunch
	 * @date 2017年11月24日
	 * @param content
	 *            加密内容
	 * @param content
	 *            编码格式
	 * @return
	 */
	public static String sign(String content) {
		String sign = "";
		try {
			try {
				// ↓web调用下面转码会出现签名有误
				// content = new String(content.getBytes(), "UTF-8");
				// MD5
				MessageDigest essageDigest = MessageDigest.getInstance("SHA-256");
				sign = Base64Utils.encode(essageDigest.digest(content.getBytes("UTF-8")));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}

	/**
	 * 拼接参数
	 *
	 * @author sunch
	 * @date 2017年11月24日
	 * @param params
	 *            排序参数
	 * @return
	 */
	public static String toKey(Map<String, String> params) {
		String content = "";
		Set<String> keySet = params.keySet();
		List<String> keyList = new ArrayList<String>();
		// 这个接口做sign签名时，值为空的参数也传
		for (String key : keySet) {
			keyList.add(key);
		}
		// 将参数和参数值按照排序顺序拼装成字符串
		for (int i = 0; i < keyList.size(); i++) {
			String key = keyList.get(i);
			if (i == keyList.size() - 1) {
				content += key + "=" + params.get(key);
				return content;
			}
			content += key + "=" + params.get(key) + "&";

		}
		return content;
	}

	/**
	 * 排序
	 *
	 * @author sunch
	 * @date 2017年11月24日
	 * @param params
	 *            排序参数
	 * @return
	 */
	public static String getSortParams(Map<String, String> params) {
		params.remove("sign");
		String contnt = "";
		Set<String> keySet = params.keySet();
		List<String> keyList = new ArrayList<String>();
		// 这个接口做sign签名时，值为空的参数也传
		for (String key : keySet) {
			keyList.add(key);
		}
		Collections.sort(keyList, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				int length = Math.min(o1.length(), o2.length());
				for (int i = 0; i < length; i++) {
					char c1 = o1.charAt(i);
					char c2 = o2.charAt(i);
					int r = c1 - c2;
					if (r != 0) {
						// char值小的排前边
						return r;
					}
				}
				// 2个字符串关系是str1.startsWith(str2)==true
				// 取str2排前边
				return o1.length() - o2.length();
			}
		});
		// 将参数和参数值按照排序顺序拼装成字符串
		for (int i = 0; i < keyList.size(); i++) {
			String key = keyList.get(i);
			contnt += key + params.get(key);
		}
		return contnt;
	}

	public static void main(String[] args) {
		// //云上api签名
		Map<String, String> map = new HashMap<String, String>();
		// map.put("waybill",
		// "{\"waybills\":[{\"collectionMoney\":0,\"commodityMoney\":0,\"custCode\":\"\",\"deliveryTime\":\"\",\"feeWeight\":0,\"insuredAmount\":0,\"items\":[{\"itemName\":\"商品01\",\"itemValue\":0,\"number\":0}],\"mailNum\":\"1100231620100_TEST\",\"orderNo\":\"BSLE1709061642\",\"orderType\":\"06\",\"postage\":0,\"receiver\":{\"address\":\"万荣路120号\",\"city\":\"上海市\",\"county\":\"静安区\",\"mobile\":\"18800000000\",\"name\":\"袁一文\",\"phone\":\"\",\"postCode\":\"200120\",\"prov\":\"上海\"},\"receiverPay\":0,\"remark\":\"\",\"revertBill\":\"\",\"revertMailNo\":\"\",\"sendType\":\"\",\"sender\":{\"address\":\"上海市闸北区万荣路1268号\",\"city\":\"上海市闸北区万荣路1268号\",\"county\":\"\",\"mobile\":\"432324342\",\"name\":\"Levis官方商城\",\"phone\":\"432324342\",\"postCode\":\"424323\",\"prov\":\"上海市闸北区万荣路1268号\"},\"serviceType\":0,\"state\":\"\",\"subMails\":\"\",\"txLogisticID\":\"S600059982026\",\"volumeWeight\":0,\"weight\":3300,\"ypdjpayment\":0}]}");
//		map.put("orderNormal",
//				"{\"orderNormals\":[{\"sender\":{\"name\":\"fsgf\",\"postCode\":\"111111\",\"phone\":\"010-1000000\",\"mobile\":\"\",\"prov\":\"安徽省\",\"city\":\"合肥市\",\"address\":\"政务服务中心\"},\"receiver\":{\"name\":\"丽水\",\"postCode\":\"222222\",\"phone\":\"010-1000000\",\"mobile\":\"\",\"prov\":\"安徽省\",\"city\":\"安庆市\",\"address\":\"测试路测试小区9栋21号\"},\"serviceType\":1,\"txLogisticID\":\"4e353a0f85f445928bab3a1e221ffac1\"}]}");
		map.put("mailNo", "1030008149224");
		try {
//			doGet(map);
			queryTrace(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
