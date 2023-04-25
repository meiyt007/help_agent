package com.zfsoft.single.util;

import cn.hutool.core.codec.Base64;

/**
 * 字符串 base64转换
 * @author dusd
 * @date 2016年12月17日
 */
public class Base64Utils {

	/**
	 * <p>
	 * BASE64字符串解码为二进制数据
	 * </p>
	 *
	 * @param base64
	 * @return
	 * @throws Exception
	 */
	public static byte[] decode(String base64) throws Exception {
		return Base64.decode(base64.getBytes());
	}

	/**
	 * <p>
	 * 二进制数据编码为BASE64字符串
	 * </p>
	 *
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public static String encode(byte[] bytes) throws Exception {
		return new String(Base64.encode(bytes));
	}

}
