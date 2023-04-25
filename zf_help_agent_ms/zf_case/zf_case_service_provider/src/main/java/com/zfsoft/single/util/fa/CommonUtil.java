package com.zfsoft.single.util.fa;

import cn.hutool.core.date.DateUtil;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

/**
 * 计算机视觉帮助方法
 */
public class CommonUtil {
    /**
     * 对指定字符串进行MD5加密
     *
     * @param piStr
     * @return
     */
    public static String md5(String piStr) {
        String encodeStr = "";
        byte[] digesta = null;
        try {
            MessageDigest alg = MessageDigest.getInstance("MD5");
            alg.update(piStr.getBytes());
            digesta = alg.digest();
            encodeStr = byte2hex(digesta);
        } catch (Exception e) {
        }
        return encodeStr;
    }

    private static String byte2hex(byte[] piByte) {
        String reStr = "";
        for (int i = 0; i < piByte.length; i++) {
            int v = piByte[i] & 0xFF;
            if (v < 16) {
                reStr += "0";
            }
            reStr += Integer.toString(v, 16).toLowerCase();
        }
        return reStr;
    }

    /**
     * 根据时间以及随机数生成文件名
     *
     * @author gaolh
     * @date 2016-8-17
     *
     * @return
     */
    public static String generateNewFileName() {
        return DateUtil.format(new Date(),"yyyyMMddHHmmssSSS") + new Random().nextInt(10000);
    }

    /**
     * 判断请求是否为Ajax请求
     *
     * @param request
     *            请求对象
     * @return Ajax请求-true，否则为false
     */
    public static boolean isAjaxRequest(ServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"));
    }

    /**
     * 判断集合是否为空
     *
     * @param collection
     * @return
     */
    public static boolean collectionIsNullOrSpace(Collection<?> collection) {
        if (collection == null || collection.isEmpty() || collection.size() == 0) {
            return false;
        }
        return true;
    }
}
