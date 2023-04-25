package com.zfsoft.platform.utils.bean;

import lombok.extern.log4j.Log4j2;
import org.springframework.lang.NonNull;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: kkfan
 * @create: 2021-10-23 17:04:11
 * @description: 反射工具类
 */
@Log4j2
public class ReflectionUtils extends org.springframework.util.ReflectionUtils {

    /**
     * 实体类转Map
     * @param <T> 实体类
     * @return
     */
    public static<T> Map<String,Object> beanToMap(@NonNull T t) {
        Map<String,Object> map=new HashMap<String,Object>();
        Field[] declaredFields = t.getClass().getDeclaredFields();
        for(Field f:declaredFields){
            try {
                //打开可见性，要不然获取不到值
                f.setAccessible(true);
                map.put(f.getName(),f.get(t));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                //关闭可见性
                f.setAccessible(false);
            }
        }
        return map;
    }

    /**
     * 反射从实体中取出字段值
     * @param t
     * @param field
     * @param <T>
     * @return
     */
    public static<T> Object getField(@NonNull T t, @NonNull String field) {
        return beanToMap(t).get(field);
    }

    /**
     * 根据属性名设置属性值
     *
     * @param fieldName
     * @param t
     * @return
     */
    public static<T> boolean setFieldValueByFieldName(@NonNull T t, @NonNull String fieldName, @NonNull Object value) {
        try {
            if(t instanceof Map) {
                ((Map)t).put(fieldName, value);
                return true;
            }
            // 获取obj类的字节文件对象
            Class c = t.getClass();
            // 获取该类的成员变量
            Field f = c.getDeclaredField(fieldName);
            // 取消语言访问检查
            f.setAccessible(true);
            // 给变量赋值
            f.set(t, value);
            return true;
        } catch (Exception e) {
            log.error("反射取值异常",e);
            return false;
        }
    }
}