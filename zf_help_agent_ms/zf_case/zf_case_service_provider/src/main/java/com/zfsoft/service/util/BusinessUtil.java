package com.zfsoft.service.util;

import tk.mybatis.mapper.util.StringUtil;

/**
 * 业务静态工厂类
 * 
 * @author zxx
 * @date 2017年4月8日
 */
public class BusinessUtil {

	/**
	 * 生成版本号
	 * 
	 * @author zxx
	 * @date 2017年4月14日
	 * @param curVersionNumber
	 *            当前版本号
	 * @return
	 */
	public static Integer createVersionNumber(Integer curVersionNumber) {
		if (curVersionNumber != null) {
			curVersionNumber++;
			return curVersionNumber;
		}
		return 1;
	}


	/**
	 * 数字转字符串，前面加0
	 * @param no 数字
	 * @param size 字符串位数
	 * @return 字符串
	 */
	public static String intToString(int no,int size){
		String result = no + "";
		if(result.length()>=size){
			return result;
		}
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = result.length() ;i<size;i++){
			stringBuilder.append("0");
		}
		return stringBuilder.append(result).toString();
	}



	/**
	 * 验证是否满足情形关系
	 *
	 * @Author: yuy
	 * @Date: 2021/8/10
	 * @param selectValOids 已选择的选项值
	 * @param relValOids 情形关联的选项ids
	 **/
	public static boolean checkSelectValAndRel(String selectValOids, String relValOids) {
		// 或者关系，只要满足其中一个条件即可展示
		if (relValOids.contains(",")) {
			for (String one : relValOids.split(",")) {
				if (selectValOids.contains(one)) {
					return true;
				}
			}
		} else {// 满足全部条件展示
			if(selectValOids.contains(relValOids)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证是否满足情形关系（默认不满足）
	 *
	 * @Author: yuy
	 * @Date: 2021/8/31
	 * @param selectValOids 已选择的选项值
	 * @param relValOids 情形关联的选项ids
	 **/
	public static boolean checkSelectValAndRelDefeatFalse(String selectValOids, String relValOids) {
		if (StringUtil.isNotEmpty(relValOids)) {
			// 或者关系，只要满足其中一个条件即可展示
			if (relValOids.contains("-")) {
				for (String one : relValOids.split("-")) {
					if (one.contains(",")) {
						for (String oneVal : one.split(",")) {
							if (!selectValOids.contains(oneVal)) {
								return false;
							}
						}
						return true;
					} else {
						if (selectValOids.contains(one)) {
							return true;
						}
					}
				}
			} else {// 满足全部条件展示
				for (String val : relValOids.split(",")) {
					if (selectValOids.contains(val)) {
						return true;
					}else{
						return false;
					}
				}

			}
		}
		return false;
	}

}
