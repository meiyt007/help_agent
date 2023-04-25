package com.zfsoft.microservice.platform.gateway.utils;

/**
 * @ClassName FormatUtils
 * @Description
 * @Author
 * @Date2020-09-29 17:49
 * @Version V1.0
 **/
/**
 * 格式化工具类
 * @author: Li Fengdi
 * @date: 2020/3/13 17:42
 */
public interface FormatUtils {

    /**
     * 将字符串用中括号括起来
     * @param s 字符串
     * @return [s]
     */
    static String wrapStringWithBracket(String s) {
        return "[" + s + "] ";
    }

}
