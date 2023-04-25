package com.zfsoft.es.service.util;

/**
 * @author: kkfan
 * @create: 2021-01-24 14:39:25
 * @description: 对象帮助类
 */
public class ObjectUtil {

    /**
     *
     * @param object
     * @return
     */
    public static Boolean isPackageObject(Object object) {
        return !object.getClass().isPrimitive() && (ReflectionUtil.typeMap.get(object.getClass().getName()) == null);
    }

}
