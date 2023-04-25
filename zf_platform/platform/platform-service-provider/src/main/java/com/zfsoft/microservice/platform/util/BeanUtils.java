package com.zfsoft.microservice.platform.util;

import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import org.springframework.beans.BeansException;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author: kkfan
 * @create: 2020-10-23 17:04:11
 * @description: 扩展springframework包下BeanUtils
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {

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
     * 将源数据复制到目标数据类
     * @author kkfan
     * @param source    源数据
     * @param target    目标数据类
     * @param <T> 目标数据类
     * @return
     * @throws BeansException
     */
    public static <S, T> T copyProperties(S source, Supplier<T> target) throws BeansException {
        T t = target.get();
        copyProperties(source, t);
        return t;
    }

    /**
     * 将源数据复制到目标数据类 带回调
     * @author kkfan
     * @param source    源数据
     * @param target    目标数据类
     * @param <T> 目标数据类
     * @param <S> 源数据类
     * @return
     * @throws BeansException
     */
    public static <S, T> T copyProperties(S source, Supplier<T> target , CallBack<S, T> callBack) throws BeansException {
        T t = copyProperties(source, target);
        callBack.callBack((S) source, t);
        return t;
    }

}
