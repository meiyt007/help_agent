package com.zfsoft.platform.utils.bean;

import com.github.pagehelper.Page;
import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author: kkfan
 * @create: 2020-10-23 17:04:11
 * @description: 扩展springframework包下BeanUtils
 * @see BeanUtils
 * @since 2022-04-19
 */
@Deprecated
public class BeanUtil extends org.springframework.beans.BeanUtils {

    /**
     * List<Bean> 复制
     * @author kkfan
     * @param sources   源数据列表
     * @param target    目标数据类
     * @param <S>   源数据类
     * @param <T>   目标数据类
     * @return  bean复制后得List<T>
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        if(sources instanceof Page) {
            return copyPageListProperties((Page<S>)sources, target, null);
        } else {
            return copyListProperties(sources, target, null);
        }
    }

    /**
     * Page<Bean> 复制
     * @author kkfan
     * @param sources   源数据列表
     * @param target    目标数据类
     * @param callBack  数据回调函数
     * @param <S>   源数据类
     * @param <T>   目标数据类
     * @return  bean复制后得Page<T>
     */
    public static <S, T> Page<T> copyPageListProperties(Page<S> sources, Supplier<T> target, CallBack<S, T> callBack) {
        List<S> sourceList = sources.getResult();
        List<T> targetList = copyProperties(sourceList, target, callBack);
        Page<T> targetPage =new Page<>();
        copyProperties(sources, targetPage);
        targetPage.addAll(targetList);
        return targetPage;
    }

    /**
     * Lits<Bean> 复制 有回调
     * @author kkfan
     * @param sources   源数据列表
     * @param target    目标数据类
     * @param callBack  数据回调函数
     * @param <S>   源数据类
     * @param <T>   目标数据类
     * @return  bean复制后得List<T>
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target, CallBack<S, T> callBack) {
        if(sources instanceof Page) {
            return copyPageListProperties((Page<S>)sources, target, callBack);
        }
        return copyProperties(sources, target, callBack);
    }

    /**
     * 负责bean
     * @author kkfan
     * @param source    源
     * @param target    目标
     * @param <T>   目标类
     * @return  T 目标类复制实例
     */
    public static <T> T copyProperties(Object source, Supplier<T> target) {
        T t = target.get();
        copyProperties(source, t);
        return t;
    }

    /**
     * 复制数组 带回调
     * @param sources   源list
     * @param target    目标类实例
     * @param callBack  回调
     * @param <S>   源类
     * @param <T>   目标类
     * @return  复制后的list<T>
     */
    private static <S, T> List<T> copyProperties(List<S> sources, Supplier<T> target, CallBack<S, T> callBack) {
        return Optional.ofNullable(sources).orElseGet(Lists::newArrayList).stream().map(source -> {
            T t = target.get();
            copyProperties(source, t);
            if (callBack != null) {
                callBack.callBack(source, t);
            }
            return t;
        }).collect(Collectors.toList());
    }

    /**
     * 函数回调
     * @author kkfan
     * @param <S>   源数据
     * @param <T>   目标数据
     */
    @FunctionalInterface
    public interface CallBack<S, T> {
        void callBack(S t, T s);
    }

    /**
     *
     * @author kkfan
     * @param bean  bean
     * @return Map
     */
    public static Map<String, Object> javabeanToMap(Object bean) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (bean == null) {
            return result;
        }

        Field[] fields = bean.getClass().getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return result;
        }

        for (Field field : fields) {
            // 重置属性可见(而且一般属性都是私有的)，否则操作无效
            boolean accessible = field.isAccessible();
            if (!accessible) {
                field.setAccessible(true);
            }

            String key = field.getName();
            try {
                // 获取子类属性

                Object value = field.get(bean);
                if (value instanceof List) {
                    List list = (List) value;
                    List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
                    Object obj;
                    for (int i = 0; i < list.size(); i++) {
                        obj = list.get(i);
                        // list里是map或String，不会存在list里直接是list的，
                        Field[] fieldChilds = obj.getClass().getDeclaredFields();
                        Map<String, Object> resultChild = new HashMap<String, Object>();
                        for (Field field2 : fieldChilds) {
                            // 重置属性可见(而且一般属性都是私有的)，否则操作无效
                            boolean accessible2 = field2.isAccessible();
                            if (!accessible2) {
                                field2.setAccessible(true);
                            }
                            try {
                                // 获取属性名称及值存入Map
                                String key1 = field2.getName();
                                Object ooo = field2.get(obj);
                                System.out.println("==key " + key1 + ";;;;;" + ooo);
                                resultChild.put(key1, field2.get(obj));
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                System.out.println(e.getMessage());
                                e.printStackTrace();
                            }

                            // 还原属性标识
                            field.setAccessible(accessible2);
                        }
                        mapList.add(resultChild);
                    }

                    result.put(key, mapList);

                } else {

                    result.put(key, field.get(bean));
                }

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            // 还原属性标识
            field.setAccessible(accessible);
        }

        // 获取父类属性
        fields = bean.getClass().getSuperclass().getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return result;
        }

        for (Field field : fields) {
            // 重置属性可见(而且一般属性都是私有的)，否则操作无效
            boolean accessible = field.isAccessible();
            if (!accessible) {
                field.setAccessible(true);
            }

            // 获取属性名称及值存入Map
            String key = field.getName();
            try {
                result.put(key, field.get(bean));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            // 还原属性标识
            field.setAccessible(accessible);
        }

        return result;
    }

}
