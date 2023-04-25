package com.zfsoft.superwindow.util;

import java.util.UUID;

/**
 * @author: kkfan
 * @create: 2020-10-22 15:35:54
 * @description: UUID帮助类
 */
public class UUIDUtil {

    public static String randomUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
