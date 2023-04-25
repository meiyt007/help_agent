package com.zfsoft.single.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Random;

/**
 * 常用方法的处理
 *
 * @author gaolh
 * @date 2016年3月24日
 *
 */
public class CommonUtil {
	/**
	 * 对指定字符串进行MD5加密
	 *
	 * @param piStr
	 * @return
	 */
	public static String md5(String piStr) {
		String encodeStr = "";
		byte[] digesta = null;
		try {
			MessageDigest alg = MessageDigest.getInstance("MD5");
			alg.update(piStr.getBytes());
			digesta = alg.digest();
			encodeStr = byte2hex(digesta);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encodeStr;
	}

	private static String byte2hex(byte[] piByte) {
		String reStr = "";
		for (int i = 0; i < piByte.length; i++) {
			int v = piByte[i] & 0xFF;
			if (v < 16){
				reStr += "0";
			}

			reStr += Integer.toString(v, 16).toLowerCase();
		}
		return reStr;
	}

	/**
	 * 根据时间以及随机数生成文件名
	 *
	 * @author gaolh
	 * @date 2016-8-17
	 *
	 * @return
	 */
	public static String generateNewFileName() {
		return DateUtil.formatDate("yyyyMMddHHmmssSSS") + new Random().nextInt(10000);
	}

	/**
	 * 判断请求是否为Ajax请求
	 *
	 * @param request
	 *            请求对象
	 * @return Ajax请求-true，否则为false
	 */
	public static boolean isAjaxRequest(ServletRequest request) {
		return "XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"));
	}

	/**
	 * 判断集合是否为空
	 * @param collection
	 * @return
	 */
	public static boolean collectionIsNullOrSpace(Collection<?> collection) {
        if (collection == null || collection.isEmpty()
                || collection.size() == 0) {
            return false;
        }
        return true;
    }
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}
	public static String ThreeExpression(Object object) {
		if (object instanceof String) {
			return object == null || object.equals("") ? "" : object.toString();
		} else {
			return object == null ? "" : object.toString();
		}
	}

	public static boolean isWindows() {
		return System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
	}
}
