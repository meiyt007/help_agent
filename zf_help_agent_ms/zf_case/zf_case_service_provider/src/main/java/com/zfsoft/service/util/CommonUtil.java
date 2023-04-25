package com.zfsoft.service.util;

import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import org.apache.commons.lang.RandomStringUtils;

/**
 * 工具类
 *
 * @author yuy
 * @date 2021年6月21日
 */
public class CommonUtil {

	/**
	 * 判断是否管理员
	 *
	 * @author yuy
	 * @date 2017年4月14日
	 * @return
	 */
	public static boolean isAdmin() {
		return CurrentLoginUserHolder.getCurrentLoginUser() != null &&
				BaseStaticParameter.DEFAULT_ADMIN_OID.equals(
						CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
	}


	public static boolean isWindows() {
		return System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
	}


	public static String random(int bit) {
		if (bit == 0) {
			// 默认6位
			bit = 6;
		}
		// 因为o和0,l和1很难区分,所以,去掉大小写的o和l
		// 初始化种子
		String str = "";
		str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz";
		// 返回6位的字符串
		return RandomStringUtils.random(bit, str);
	}

}
