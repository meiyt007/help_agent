package com.zfsoft.platform.utils.dict;

import java.util.function.Function;

/**
 * @author: kkfan
 * @create: 2022-03-23 15:19:37
 * @description:    借助函数接口获取字段名
 */
public interface Fn<T, R> extends Function<T, R> {

}
