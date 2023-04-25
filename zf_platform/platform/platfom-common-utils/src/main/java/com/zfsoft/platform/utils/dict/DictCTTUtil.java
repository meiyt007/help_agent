package com.zfsoft.platform.utils.dict;

import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.zfsoft.platform.utils.bean.BeanUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: kkfan
 * @create: 2022-04-27 13:59:20
 * @description: code to text
 */
@Slf4j
public class DictCTTUtil {

	@SneakyThrows
	public static <T> Object code2MC(T t) {
		if (t == null) {
			return t;
		}
		Class<?> objclass = t.getClass();
		Field[] fields = objclass.getDeclaredFields();
		Field[] parentFields = objclass.getFields();
		Map<String, Object> objectMap = null;
		ArrayList<Field> fieldList = Lists.newArrayList(fields);
		fieldList.addAll(Lists.newArrayList(parentFields));
		for (Field fd : fieldList) {
			if (fd.isAnnotationPresent(DictCTT.class)) {
				DictCTT dictCTT = fd.getAnnotation(DictCTT.class);
				fd.setAccessible(true);
				String code = "";
				String codeName = fd.getName();
				Object temp = fd.get(t);
				if (temp != null) {
					code = temp.toString();
				}
				Field textField = null;
				try {
					textField = objclass.getDeclaredField(dictCTT.dictTextField());
				} catch (Exception e) {}


				if (StringUtils.isNotBlank(dictCTT.emptyText()) && StringUtils.isBlank(code)) {
					if (null == textField) {
						if (null == objectMap) {
							objectMap = BeanUtils.javabeanToMap(t);
						}
						objectMap.put(codeName.endsWith("Code") ? codeName.substring(0, codeName.length() - 4) + "Text" : codeName + "Text", dictCTT.emptyText());
					} else {
						fd.set(textField, dictCTT.emptyText());
					}
					continue;
				}

				String mc = getZdmc(code, dictCTT);

				if (null == textField) {
					if (null == objectMap) {
						objectMap = BeanUtils.javabeanToMap(t);
					}
					objectMap.put(codeName.endsWith("Code") ? codeName.substring(0, codeName.length() - 4) + "Text" : codeName + "Text", mc != null ? mc : StringUtils.EMPTY);
					continue;
				}

				if (null != objectMap) {
					objectMap.put(dictCTT.dictTextField(), mc != null ? mc : StringUtils.EMPTY);
					continue;
				}

				textField.setAccessible(true);

				if (mc != null) {
					textField.set(t, mc);
				} else {
					textField.set(t, StringUtils.EMPTY);
				}
			}
		}
		return null == objectMap ? t : objectMap;
	}

	public static <T> Page<Object> code2MC(Page<T> page) {
		Page resList = new Page(page.getPageNum(), page.getPageSize());
		resList.setTotal(page.getTotal());
		resList.addAll(code2MC(Lists.newArrayList(page.getResult())));
		return resList;
	}

	public static <T> List<Object> code2MC(List<T> list) {
		if (list instanceof Page) {
			return code2MC((Page)list);
		}
		List resList = Lists.newArrayList();
		for (T t : list) {
			resList.add(code2MC(t));
		}
		return resList;
	}

	public static String getZdmc(String codeOrId, DictCTT dictCTT) {
		if (StringUtils.isEmpty(codeOrId) || StringUtils.split(codeOrId, ",").length == 0) {
			return StringUtils.EMPTY;
		}
		if (codeOrId.indexOf(",") != -1) {
			String[] codes = codeOrId.split(",");
			StringBuilder str = new StringBuilder();
			for (int i = 0; i < codes.length; i++) {
				String string = CTTUtil.dealMap.get(dictCTT.typeEnum()).apply(dictCTT, codeOrId);
				if (i != codes.length - 1) {
					str.append(string).append(",");
				} else {
					str.append(string);
				}
			}
			return str.toString();
		} else {
			return CTTUtil.dealMap.get(dictCTT.typeEnum()).apply(dictCTT, codeOrId);
		}
	}

	public static <T, V> Page<T> code2MC(Class<T> t, Page<V> page) {
		Page<T> resultPage = new Page<T>();
		resultPage.setTotal(page.getTotal());
		resultPage.setPageNum(page.getPageNum());
		resultPage.setPageSize(page.getPageSize());
		ArrayList<T> list = new ArrayList<T>();
		for (V v : page.getResult()) {
			T t2 = null;
			try {
				t2 = t.newInstance();
				BeanUtils.copyProperties(t2, v);
				code2MC(t2);
			} catch (Exception e) {
				log.error(e.toString(), e);
			}
			list.add(t2);
		}
		resultPage.addAll(list);
		return resultPage;
	}

	@SneakyThrows
	public static <T, V> T code2MC(Class<T> t, V v) {
		T t2 = t.newInstance();
		org.springframework.beans.BeanUtils.copyProperties(t2, v);
		code2MC(t2);
		return t2;
	}
}