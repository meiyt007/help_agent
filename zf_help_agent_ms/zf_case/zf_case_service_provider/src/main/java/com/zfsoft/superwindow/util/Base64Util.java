package com.zfsoft.superwindow.util;

import cn.hutool.core.codec.Base64;

/**
 * @description: Treeselect树结构实体类
 * @author: wangxl
 * @Date: 2020/10/29
 **/
public class Base64Util {
    /**
     * BASE64字符串解码为二进制数据
     *
     * @author chenbw
     * @date 2019-2-21 13:20:00
     * @param base64
     * @return
     * @throws Exception
     */
    public static byte[] decode(String base64) throws Exception {
        return Base64.decode(base64.getBytes());
    }

    /**
     * 二进制数据编码为BASE64字符串
     *
     * @author chenbw
     * @date 2019-2-21 13:20:00
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String encode(byte[] bytes) throws Exception {
        return new String(Base64.encode(bytes));
    }
}
