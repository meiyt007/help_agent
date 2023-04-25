package com.zfsoft.single.util.fa;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;

/**
 * @description 工具类
 * @author zhujiajian
 * @date 2018-02-08 15:20:37
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
public class FaCommonUtil extends CommonUtil{

    /**
     *
     *<p>
     *@description copy javabean ,将from中的属性值赋值给to，from属性有值，才会覆盖
     *</p>
     *@param to 基准类,被赋值对象
     *@param from 提供数据的对象
     *@throws Exception
     * @see
     */
    public static void copyJavaBean(Object from, Object to) {
        Class<?> clazz1 = from.getClass();
        Class<?> clazz2 = to.getClass();
        Field[] fields1 = clazz1.getDeclaredFields();
        Field[] fields2 = clazz2.getDeclaredFields();
        for (int i = 0; i < fields1.length; i++) {
            try {
                fields1[i].setAccessible(true);
                fields2[i].setAccessible(true);
                if("serialVersionUID".equals(fields1[i].getName())){
                    continue;
                }
                Object obg1 = fields1[i].get(from);

                if (null != obg1) {
                    fields2[i].set(to, fields1[i].get(from));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 比较2个对象内容是否一致
     * @param obj1 对象1
     * @param obj2 对象2
     * @param args 跳过比较的字段
     */
    public static boolean equalsJavaBean(Object obj1, Object obj2, String[] args) {
        Class<?> clazz1 = obj1.getClass();
        Class<?> clazz2 = obj2.getClass();
        Field[] fields1 = clazz1.getDeclaredFields();
        Field[] fields2 = clazz2.getDeclaredFields();
        List<String> exceptFildNames = args == null ? null : Arrays.asList(args);
        boolean ret = true;
        for (int i = 0; i < fields1.length; i++) {
            try {
                fields1[i].setAccessible(true);
                fields2[i].setAccessible(true);

                String fildName = fields1[i].getName();
                if("serialVersionUID".equals(fildName)
                        || (exceptFildNames != null && exceptFildNames.contains(fildName))){
                    continue;
                }

                Object val1 = fields1[i].get(obj1);
                Object val2 = fields2[i].get(obj2);

                if ((null != val1 && !val1.equals(val2)) ||
                        (null != val2 && !val2.equals(val1))) {
                    ret = false;
                    break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * 对type数组进行MD5加密
     *
     * @param byteArray 图片对应byte数组
     * @return md5字符串
     */
    public static String md5(byte[] byteArray) {
        String encodeStr = "";
        byte[] digesta = null;
        try {

            MessageDigest alg = MessageDigest.getInstance("MD5");
            alg.update(byteArray);
            digesta = alg.digest();
            encodeStr = byte2hex(digesta);

        } catch (Exception e) {

        }
        return encodeStr;
    }
    public static String byte2hex(byte[] piByte) {
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

}

