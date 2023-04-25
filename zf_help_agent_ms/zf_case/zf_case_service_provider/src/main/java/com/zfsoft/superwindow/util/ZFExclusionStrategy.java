package com.zfsoft.superwindow.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义Gson生成生成Json策略配置类
 * 
 * @author zly
 * @date 2017-6-22
 */
public class ZFExclusionStrategy implements ExclusionStrategy {
	/**
	 * 目标对象Class类型
	 */
	private Class<?>[] targetClazz;

	/**
	 * 目标Class类型对应的处理属性
	 */
	private Map<Class<?>, String[]> fields = new HashMap<Class<?>, String[]>();

	/**
	 * 默认是排除策略，设置reverse=true为包含策略。
	 */
	private boolean reverse;

	public ZFExclusionStrategy(Class<?>[] targetClazz) {
		super();
		this.targetClazz = targetClazz;
	}

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}

	@Override
	public boolean shouldSkipField(FieldAttributes fieldAttributes) {
		Class<?> owner = fieldAttributes.getDeclaringClass();
		String fieldName = fieldAttributes.getName();
		boolean isSkip = false;

		if (ArrayUtils.contains(targetClazz, owner)) {
			String[] field = fields.get(owner);

			if (field == null || field.length == 0) {
				isSkip = true;
			}

			if (ArrayUtils.contains(field, fieldName)) {
				isSkip = true;
			}
		}

		if (reverse) {
			isSkip = !isSkip;
		}

		return isSkip;
	}

	/**
	 * 设置待处理的Class的属性
	 * 
	 * @param clazz
	 *            要处理的Class
	 * @param fields
	 *            属性数组
	 */
	public void setFields(Class<?> clazz, String[] fields) {
		this.fields.put(clazz, fields);
	}

	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}
}
