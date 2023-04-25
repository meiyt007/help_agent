package com.zfsoft.ha.util;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.io.HTMLWriter;
import org.dom4j.io.OutputFormat;
import sun.misc.BASE64Encoder;

import java.beans.XMLDecoder;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理及转换工具类
 *
 * @author gaolh
 * @date 2016-3-24
 */
@SuppressWarnings(value = { "rawtypes", "resource" })
public class StringUtil {
    private static Pattern numericPattern = Pattern.compile("^[0-9\\-]+$");
    private static Pattern numericStringPattern = Pattern.compile("^[0-9\\-\\-]+$");
    private static Pattern floatNumericPattern = Pattern.compile("^[0-9\\-\\.]+$");
    private static Pattern abcPattern = Pattern.compile("^[a-z|A-Z]+$");
    private static Pattern abcAndNumPattern = Pattern.compile("^[a-z|A-Z|0-9]+$");
    private static Pattern emailPattern = Pattern.compile("\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?");
    private static Pattern chPattern = Pattern.compile("^[\u4E00-\u9FA5]+$");
    private static Pattern mobPattern = Pattern.compile("(\\+\\d+)?1[34578]\\d{9}$");
    private static Pattern phoPattern = Pattern.compile("(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$");
    private static Pattern ic15Pattern = Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");
    private static Pattern ic18Pattern = Pattern
            .compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$");
    private static Pattern urlPattern = Pattern
            .compile("(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
    public static final String SPLIT_STR_PATTERN = ",|，|;|；|、|\\.|。|-|_|\\(|\\)|\\[|\\]|\\{|\\}|\\\\|/| |　|\"";
    private static Log logger = LogFactory.getLog(StringUtil.class);

    /**
     * 判断是否数字表示
     *
     * @param src
     *            源字符串
     * @return 是否数字的标志
     */
    public static boolean isNumeric(String src) {
        boolean returnValue = false;
        if (src != null && src.length() > 0) {
            Matcher m = numericPattern.matcher(src);
            if (m.find()) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    /**
     * 判断是否数字表示
     *
     * @param src
     *            源字符串
     * @return 是否数字的标志
     */
    public static boolean isNumericString(String src) {
        boolean returnValue = false;
        if (src != null && src.length() > 0) {
            Matcher m = numericStringPattern.matcher(src);
            if (m.find()) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    /**
     * 判断是否纯字母组合
     *
     * @param src
     *            源字符串
     * @return 是否纯字母组合的标志
     */
    public static boolean isABC(String src) {
        boolean returnValue = false;
        if (src != null && src.length() > 0) {
            Matcher m = abcPattern.matcher(src);
            if (m.find()) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    /**
     * 判断是否为字母数字组合
     *
     * @param src
     *            源字符串
     * @return 是否为字母数字标志
     */
    public static boolean isABCAndNum(String src) {
        boolean returnValue = false;
        if (src != null && src.length() > 0) {
            Matcher m = abcAndNumPattern.matcher(src);
            if (m.find()) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    /**
     * 判断是否浮点数字表示
     *
     * @param src
     *            源字符串
     * @return 是否数字的标志
     */
    public static boolean isFloatNumeric(String src) {
        boolean returnValue = false;
        if (src != null && src.length() > 0) {
            Matcher m = floatNumericPattern.matcher(src);
            if (m.find()) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    /**
     * 判断是否是EMAIL
     *
     * @param src
     *            源字符串
     * @return 是否数字的标志
     */
    public static boolean isEmail(String src) {
        boolean returnValue = false;
        if (src != null && src.length() > 0) {
            Matcher m = emailPattern.matcher(src);
            if (m.find()) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    /**
     * 判断是否是中文
     *
     * @param src
     *            源字符串
     * @return 是否数字的标志
     */
    public static boolean isChinese(String src) {
        boolean returnValue = false;
        if (src != null && src.length() > 0) {
            Matcher m = chPattern.matcher(src);
            if (m.find()) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    /**
     * 判断是否是手机号码
     *
     * @param src
     *            源字符串
     * @return 是否数字的标志
     */
    public static boolean isMobile(String src) {
        boolean returnValue = false;
        if (src != null && src.length() > 0) {
            Matcher m = mobPattern.matcher(src);
            if (m.find()) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    /**
     * 判断是否是电话号码（座机）
     *
     * @param src
     *            源字符串
     * @return 是否数字的标志
     */
    public static boolean isTelPhone(String src) {
        boolean returnValue = false;
        if (src != null && src.length() > 0) {
            Matcher m = phoPattern.matcher(src);
            if (m.find()) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    /**
     * 判断是否是居民身份证 居民身份证号码15位或18位，最后一位可能是数字或字母
     *
     * @param src
     *            源字符串
     * @return 是否数字的标志
     */
    public static boolean isIdCard(String src) {
        boolean returnValue = false;
        if (src != null && src.length() > 0) {
            Matcher m15 = ic15Pattern.matcher(src);
            Matcher m18 = ic18Pattern.matcher(src);
            if (m15.find() || m18.find()) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    /**
     * 判断是否是URL地址 格式：http://www.zhuofansoft.com:99 或 http://www.zhuofansoft.com
     *
     * @param src
     *            源字符串
     * @return 是否数字的标志
     */
    public static boolean isUrl(String src) {
        boolean returnValue = false;
        if (src != null && src.length() > 0) {
            Matcher m = urlPattern.matcher(src);
            if (m.find()) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    /**
     * 把string array or list用给定的符号symbol连接成一个字符串
     *
     * @param array
     * @param symbol
     * @return
     */
    public static String joinString(List array, String symbol) {
        String result = "";
        if (array != null) {
            for (int i = 0; i < array.size(); i++) {
                String temp = array.get(i).toString();
                if (temp != null && temp.trim().length() > 0) {
                    result += (temp + symbol);
                }
            }
            if (result.length() > 1) {
                result = result.substring(0, result.length() - 1);
            }
        }
        return result;
    }

    /**
     * 把string array or list用给定的符号symbol连接成一个字符串
     *
     * @param array
     * @param symbol
     * @return
     */
    public static String joinString(Object[] array, String symbol) {
        String result = "";
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                Object temp = array[i];
                if (temp != null && temp.toString().trim().length() > 0) {
                    result += (temp + symbol);
                }
            }
            if (result.length() > 1) {
                result = result.substring(0, result.length() - 1);
            }
        }
        return result;
    }

    /**
     * 判断字符串是否超过一定长度，超过部分转化为...
     *
     * @author gaolh
     * @date 2016-3-24
     *
     * @param subject
     *            需要转化的字符串
     * @param size
     *            长度
     * @return 转化后的字符串
     */
    public static String subStringNotEncode(String subject, int size) {
        if (subject != null && subject.length() > size) {
            subject = subject.substring(0, size) + "...";
        }
        return subject;
    }

    /**
     * 截取字符串 超出的字符用symbol代替
     *
     * @param len
     *            字符串长度 长度计量单位为一个GBK汉字 两个英文字母计算为一个单位长度
     * @param str
     * @param symbol
     * @return
     */
    public static String getLimitLengthString(String str, int len, String symbol) {
        int iLen = len * 2;
        int counterOfDoubleByte = 0;
        String strRet = "";
        try {
            if (str != null) {
                byte[] b = str.getBytes("GBK");
                if (b.length <= iLen) {
                    return str;
                }
                for (int i = 0; i < iLen; i++) {
                    if (b[i] < 0) {
                        counterOfDoubleByte++;
                    }
                }
                int charOne = 2;
                if (counterOfDoubleByte % charOne == 0) {
                    strRet = new String(b, 0, iLen, "GBK") + symbol;
                    return strRet;
                } else {
                    strRet = new String(b, 0, iLen - 1, "GBK") + symbol;
                    return strRet;
                }
            } else {
                return "";
            }
        } catch (Exception ex) {
            return str.substring(0, len);
        } finally {
            strRet = null;
        }
    }

    /**
     * 截取字符串 超出的字符用symbol代替
     *
     * @param len
     *            字符串长度 长度计量单位为一个GBK汉字 两个英文字母计算为一个单位长度
     * @param str
     * @return12
     */
    public static String getLimitLengthString(String str, int len) {
        return getLimitLengthString(str, len, "...");
    }

    /**
     *
     * 截取字符，不转码
     *
     * @param subject
     * @param size
     * @return
     */
    public static String subStrNotEncode(String subject, int size) {
        if (subject.length() > size) {
            subject = subject.substring(0, size);
        }
        return subject;
    }

    /**
     * 把string array or list用给定的符号symbol连接成一个字符串
     *
     * @param array
     * @param symbol
     * @return
     */
    public static String joinString(String[] array, String symbol) {
        String result = "";
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                String temp = array[i];
                if (temp != null && temp.trim().length() > 0) {
                    result += (temp + symbol);
                }
            }
            if (result.length() > 1) {
                result = result.substring(0, result.length() - 1);
            }
        }
        return result;
    }

    /**
     * 取得字符串的实际长度（考虑了汉字的情况）
     *
     * @param srcStr
     *            源字符串
     * @return 字符串的实际长度
     */
    public static int getStringLen(String srcStr) {
        int returnValue = 0;
        if (srcStr != null) {
            char[] theChars = srcStr.toCharArray();
            for (int i = 0; i < theChars.length; i++) {
                returnValue += (theChars[i] <= 255) ? 1 : 2;
            }
        }
        return returnValue;
    }

    /**
     * 检查数据串中是否包含非法字符集('")
     *
     * @param str
     * @return [true]|[false] 包含|不包含
     */
    public static boolean check(String str) {
        String sIllegal = "'\"";
        int len = sIllegal.length();
        if (null == str) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (str.indexOf(sIllegal.charAt(i)) != -1) {
                return true;
            }
        }

        return false;
    }

    /***************************************************************************
     * getHideEmailPrefix - 隐藏邮件地址前缀。
     *
     * @param email
     *            - EMail邮箱地址 例如: linwenguo@koubei.com 等等...
     * @return 返回已隐藏前缀邮件地址, 如 *********@koubei.com.
     * @version 1.0 (2006.11.27) Wilson Lin
     **************************************************************************/
    public static String getHideEmailPrefix(String email) {
        if (null != email) {
            int index = email.lastIndexOf('@');
            if (index > 0) {
                email = repeat("*", index).concat(email.substring(index));
            }
        }
        return email;
    }

    /**
     * repeat - 通过源字符串重复生成N次组成新的字符串。
     *
     * @param src
     *            - 源字符串 例如: 空格(" "), 星号("*"), "浙江" 等等...
     * @param num
     *            - 重复生成次数
     * @return 返回已生成的重复字符串
     */
    public static String repeat(String src, int num) {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < num; i++) {
            s.append(src);
        }
        return s.toString();
    }

    /**
     * 根据指定的字符把源字符串分割成一个数组
     *
     * @param pattern
     *            指定的字符
     * @param src
     *            源字符串
     * @return
     */
    public static List<String> parseString2ListByCustomerPattern(String pattern, String src) {

        if (src == null) {
            return null;
        }
        List<String> list = new ArrayList<String>();
        String[] result = src.split(pattern);
        for (int i = 0; i < result.length; i++) {
            list.add(result[i]);
        }
        return list;
    }

    /**
     * 根据指定的字符(，,、。)把源字符串分割成一个数组
     *
     * @param src
     *            源字符串
     * @return
     */
    public static List<String> parseString2ListByPattern(String src) {
        String pattern = "，|,|、|。";
        return parseString2ListByCustomerPattern(pattern, src);
    }

    /**
     * 格式化一个float
     *
     * @param format
     *            要格式化成的格式 such as #.00, #.#
     */

    public static String formatFloat(float f, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(f);
    }

    /**
     * 自定义的分隔字符串函数 例如: 1,2,3 =>[1,2,3] 3个元素 ,2,3=>[,2,3] 3个元素 ,2,3,=>[,2,3,]
     * 4个元素 ,,,=>[,,,] 4个元素
     *
     * 5.22算法修改，为提高速度不用正则表达式 两个间隔符,,返回""元素
     *
     * @param split
     *            分割字符 默认,
     * @param src
     *            输入字符串
     * @return 分隔后的list
     * @author Robin
     */
    public static List<String> splitToList(String split, String src) {
        if (src == null) {
            return new ArrayList<String>();
        }
        // 默认,
        String sp = ",";
        if (split != null && split.length() == 1) {
            sp = split;
        }
        List<String> r = new ArrayList<String>();
        int lastIndex = -1;
        int index = src.indexOf(sp);
        if (-1 == index && src != null) {
            r.add(src);
            return r;
        }
        while (index >= 0) {
            if (index > lastIndex) {
                r.add(src.substring(lastIndex + 1, index));
            } else {
                r.add("");
            }

            lastIndex = index;
            index = src.indexOf(sp, index + 1);
            if (index == -1) {
                r.add(src.substring(lastIndex + 1, src.length()));
            }
        }
        return r;
    }

    /**
     * 把Map转换成名=值的字符串 (a=1,b=2 =>a=1&b=2)
     *
     * @param map
     * @return
     */
    public static String linkedHashMapToString(LinkedHashMap<String, String> map) {
        if (map != null && map.size() > 0) {
            String result = "";
            Iterator it = map.keySet().iterator();
            while (it.hasNext()) {
                String name = (String) it.next();
                String value = (String) map.get(name);
                result += ("".equals(result)) ? "" : "&";
                result += String.format("%s=%s", name, value);
            }
            return result;
        }
        return null;
    }

    /**
     * 解析字符串返回 名称=值的参数表 (a=1&b=2 => a=1,b=2)
     *
     * @param str
     * @return
     */
    @SuppressWarnings("unchecked")
    public static LinkedHashMap<String, String> toLinkedHashMap(String str) {
        String strOne = "=";
        if (str != null && !str.equals("") && str.indexOf(strOne) > 0) {
            LinkedHashMap result = new LinkedHashMap();

            String name = null;
            StringBuilder value = null;
            int i = 0;
            while (i < str.length()) {
                char c = str.charAt(i);
                switch (c) {
                // =
                case 61:
                    value = new StringBuilder();
                    break;
                // &
                case 38:
                    if (name != null && !name.equals("")) {
                        result.put(name, value.toString());
                    }
                    name = null;
                    value = null;
                    break;
                default:
                    if (value != null) {
                        value.append(c);
                    } else {
                        value = new StringBuilder("" + c);
                        name = "" + c;
                    }
                }
                i++;

            }

            if (name != null && !name.equals("")) {
                result.put(name, value.toString());
            }

            return result;

        }
        return null;
    }

    /**
     * 根据输入的多个解释和下标返回一个值
     *
     * @param captions
     *            例如:"无,爱干净,一般,比较乱"
     * @param index
     *            2
     * @return 一般
     */
    public static String getCaption(String captions, int index) {
        if (index > 0 && captions != null && !captions.equals("")) {
            String[] ss = captions.split(",");
            if (ss != null && ss.length > 0 && index < ss.length) {
                return ss[index];
            }
        }
        return null;
    }

    /**
     * 数字转字符串,如果num<=0 则输出"";
     *
     * @param num
     * @return
     */
    public static String numberToString(Object num) {
        if (num == null) {
            return null;
        } else if (num instanceof Integer && (Integer) num > 0) {
            return Integer.toString((Integer) num);
        } else if (num instanceof Long && (Long) num > 0) {
            return Long.toString((Long) num);
        } else if (num instanceof Float && (Float) num > 0) {
            return Float.toString((Float) num);
        } else if (num instanceof Double && (Double) num > 0) {
            return Double.toString((Double) num);
        } else {
            return "";
        }
    }

    /**
     * 货币转字符串
     *
     * @param money
     * @param style
     *            样式 [default]要格式化成的格式 such as #.00, #.#
     * @return
     */

    public static String moneyToString(Object money, String style) {
        boolean boolOne = money instanceof Double || money instanceof Float;
        if (money != null && style != null && boolOne) {
            Double num = (Double) money;
            String strOne = "default";
            if (style.equalsIgnoreCase(strOne)) {
                // 缺省样式 0 不输出 ,如果没有输出小数位则不输出.0
                int charOne = 10;
                if (num == 0) {
                    // 不输出0
                    return "";
                } else if ((num * charOne % charOne) == 0) {
                    // 没有小数
                    return Integer.toString((int) num.intValue());
                } else {
                    // 有小数
                    return num.toString();
                }

            } else {
                DecimalFormat df = new DecimalFormat(style);
                return df.format(num);
            }
        }
        return null;
    }

    /**
     * 在sou中是否存在finds 如果指定的finds字符串有一个在sou中找到,返回true;
     *
     * @param sou
     *            源字符串
     * @param finds
     *            需要查找的字符串
     * @return
     */
    public static boolean strPos(String sou, String... finds) {
        if (sou != null && finds != null && finds.length > 0) {
            for (int i = 0; i < finds.length; i++) {
                if (sou.indexOf(finds[i]) > -1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 在sou中是否存在finds 如果指定的finds字符串有一个在sou中找到,返回true;
     *
     * @param sou
     *            源字符串
     * @param finds
     *            需要查找的字符串
     * @return
     */
    public static boolean strPos(String sou, List<String> finds) {
        if (sou != null && finds != null && finds.size() > 0) {
            for (String s : finds) {
                if (sou.indexOf(s) > -1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 在sou中是否存在finds 如果指定的finds字符串有一个在sou中找到,返回true;
     *
     * @param sou
     *            源字符串
     * @param finds
     *            需要查找的字符串
     * @return
     */
    public static boolean strPos(String sou, String finds) {
        List<String> t = splitToList(",", finds);
        return strPos(sou, t);
    }

    /**
     * 判断两个字符串是否相等 如果都为null则判断为相等,一个为null另一个not null则判断不相等 否则如果s1=s2则相等
     *
     * @param s1
     * @param s2
     * @return
     */
    public static boolean equals(String s1, String s2) {
        if (StrUtil.isBlank(s1) && StrUtil.isBlank(s2)) {
            return true;
        } else if (!StrUtil.isBlank(s1) && !StrUtil.isBlank(s2)) {
            return s1.equals(s2);
        }
        return false;
    }

    /**
     * 将字符串转化为int，如果不能转化，返回0
     *
     * @author gaolh
     * @date 2016年3月24日
     *
     * @param s
     *            需要转化的字符串
     * @return
     */
    public static int toInt(String s) {
        if (s != null && !"".equals(s.trim())) {
            try {
                return Integer.parseInt(s);
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    /**
     * 将字符串转化为double，如果不能转化，返回0
     *
     * @author gaolh
     * @date 2016年3月24日
     *
     * @param s
     *            需要转化的字符串
     * @return
     */
    public static double toDouble(String s) {
        if (s != null && !"".equals(s.trim())) {
            return Double.parseDouble(s);
        }
        return 0;
    }

    /**
     * 将字符串转化为long，如果不能转化，返回0
     *
     * @author gaolh
     * @date 2016年3月24日
     *
     * @param s
     *            需要转化的字符串
     * @return
     */
    public static long toLong(String s) {
        try {
            if (s != null && !"".equals(s.trim())) {
                return Long.parseLong(s);
            }
        } catch (Exception exception) {
        }
        return 0L;
    }

    /**
     * 把xml 转为object
     *
     * @param xml
     * @return
     */
    public static Object xmlToObject(String xml) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes("UTF8"));
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(in));
            return decoder.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 过滤用户输入的URL地址（防治用户广告） 目前只针对以http或www开头的URL地址
     * 本方法调用的正则表达式，不建议用在对性能严格的地方例如:循环及list页面等
     *
     * @author gaolh
     * @param str
     *            需要处理的字符串
     * @return 返回处理后的字符串
     */
//    public static String removeURL(String str) {
//        if (str != null) {
//            str = str.toLowerCase().replaceAll("(http|www|com|cn|org|\\.)+", "");
//        }
//        return str;
//    }

    /**
     * 随即生成指定位数的含数字验证码字符串
     *
     * @author gaolh
     * @date 2016-3-24
     * @param bit
     *            指定生成数字验证码位数
     * @return String
     */
    public static String numRandom(int bit) {
        // 默认6位
        if (bit == 0) {
            bit = 6;
        }
        String str = "";
        // 初始化种子
        str = "0123456789";
        // 返回6位的字符串
        return RandomStringUtils.random(bit, str);
    }

    /**
     * 随即生成指定位数的验证码含字母
     *
     * @author gaolh
     * @date 2016-3-24
     * @param bit
     *            指定生成验证码位数
     * @return String
     */
    public static String random(int bit) {
        if (bit == 0) {
            // 默认6位
            bit = 6;
        }
        // 因为o和0,l和1很难区分,所以,去掉大小写的o和l
        // 初始化种子
        String str = "";
        str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz";
        // 返回6位的字符串
        return RandomStringUtils.random(bit, str);
    }

    /**
     * Wap页面的非法字符检查
     *
     * @author hugh115
     * @date 2007-06-29
     * @param str
     * @return
     */
    public static String replaceWapStr(String str) {
        if (str != null) {
            str = str.replaceAll("<span class=\"keyword\">", "");
            str = str.replaceAll("</span>", "");
            str = str.replaceAll("<strong class=\"keyword\">", "");
            str = str.replaceAll("<strong>", "");
            str = str.replaceAll("</strong>", "");
            str = str.replace('$', '＄');
            str = str.replaceAll("&amp;", "＆");
            str = str.replace('&', '＆');
            str = str.replace('<', '＜');
            str = str.replace('>', '＞');
        }
        return str;
    }

    /**
     * 字符串转float 如果异常返回0.00
     *
     * @param s
     *            输入的字符串
     * @return 转换后的float
     */
    public static Float toFloat(String s) {
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {
            return new Float(0);
        }
    }

    /**
     * 页面中去除字符串中的空格、回车、换行符、制表符
     *
     * @author gaolh
     * @date 2016-3-24
     * @param str
     * @return
     */
//    public static String replaceBlank(String str) {
//        if (str != null) {
//            String el = "\\s*|\n";
//            Pattern p = Pattern.compile(el);
//            Matcher m = p.matcher(str);
//            str = m.replaceAll("");
//        }
//        return str;
//    }

    /**
     * 全角生成半角
     *
     * @author gaolh
     * @date 2016-3-24
     * @param qJstr
     * @return
     */
    public static String q2b(String qJstr) {
        String outStr = "";
        String tstr = "";
        byte[] b = null;
        for (int i = 0; i < qJstr.length(); i++) {
            try {
                tstr = qJstr.substring(i, i + 1);
                b = tstr.getBytes("unicode");
            } catch (UnsupportedEncodingException e) {
                if (logger.isErrorEnabled()) {
                    logger.error(e);
                }
            }
            if(b != null){
                if (b[3] == -1) {
                    b[2] = (byte) (b[2] + 32);
                    b[3] = 0;
                    try {
                        outStr = outStr + new String(b, "unicode");
                    } catch (UnsupportedEncodingException ex) {
                        if (logger.isErrorEnabled()) {
                            logger.error(ex);
                        }
                    }
                } else {
                    outStr = outStr + tstr;
                }
            }
        }
        return outStr;
    }

    /**
     *
     * 转换编码
     *
     * @param s
     *            源字符串
     * @param fencode
     *            源编码格式
     * @param bencode
     *            目标编码格式
     * @return 目标编码
     */
    public static String changCoding(String s, String fencode, String bencode) {
        String str;
        try {
            if (StrUtil.isNotBlank(s)) {
                str = new String(s.getBytes(fencode), bencode);
            } else {
                str = "";
            }
            return str;
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }

    /**
     * 移除字符串的html标签
     *
     * @param str
     * @return
     */
    public static String removeHTMLLableExe(String str) {
        str = stringReplace(str, ">\\s*<", "><");
        // 替换空格
        str = stringReplace(str, "&nbsp;", " ");
        // 去<br><br />
        str = stringReplace(str, "<br ?/?>", "\n");
        // 去掉<>内的字符
        str = stringReplace(str, "<([^<>]+)>", "");
        // 将多个空白变成一个空格
        str = stringReplace(str, "\\s\\s\\s*", " ");
        // 去掉头的空白
        str = stringReplace(str, "^\\s*", "");
        // 去掉尾的空白
        str = stringReplace(str, "\\s*$", "");
        str = stringReplace(str, " +", " ");
        return str;
    }

    /**
     * 除去html标签
     *
     * @param str
     *            源字符串
     * @return 目标字符串
     */
    public static String removeHTMLLable(String str) {
        // 去掉页面上看不到的字符
        str = stringReplace(str, "\\s", "");
        // 去<br><br />
        str = stringReplace(str, "<br ?/?>", "\n");
        // 去掉<>内的字符
        str = stringReplace(str, "<([^<>]+)>", "");
        // 替换空格
        str = stringReplace(str, "&nbsp;", " ");
        // 去<br><br />
        str = stringReplace(str, "&(\\S)(\\S?)(\\S?)(\\S?);", "");
        return str;
    }

    /**
     * 去掉HTML标签之外的字符串
     *
     * @param str
     *            源字符串
     * @return 目标字符串
     */
    public static String removeOutHTMLLable(String str) {
        str = stringReplace(str, ">([^<>]+)<", "><");
        str = stringReplace(str, "^([^<>]+)<", "<");
        str = stringReplace(str, ">([^<>]+)$", ">");
        return str;
    }

    /**
     *
     * 字符串替换
     *
     * @param str
     *            源字符串
     * @param sr
     *            正则表达式样式
     * @param sd
     *            替换文本
     * @return 结果串
     */
    public static String stringReplace(String str, String sr, String sd) {
        String regEx = sr;
        Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        str = m.replaceAll(sd);
        return str;
    }

    /**
     *
     * 将html的省略写法替换成非省略写法
     *
     * @param str
     *            html字符串
     * @param pt
     *            标签如table
     * @return 结果串
     */
    public static String fomateToFullForm(String str, String pt) {
        String regEx = "<" + pt + "\\s+([\\S&&[^<>]]*)/>";
        Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        String[] sa = null;
        String sf = "";
        String sf2 = "";
        String sf3 = "";
        for (; m.find();) {
            sa = p.split(str);
            if (sa == null) {
                break;
            }
            sf = str.substring(sa[0].length(), str.indexOf("/>", sa[0].length()));
            sf2 = sf + "></" + pt + ">";
            sf3 = str.substring(sa[0].length() + sf.length() + 2);
            str = sa[0] + sf2 + sf3;
            sa = null;
        }
        return str;
    }

    /**
     *
     * 得到字符串的子串位置序列
     *
     * @param str
     *            字符串
     * @param sub
     *            子串
     * @param b
     *            true子串前端,false子串后端
     * @return 字符串的子串位置序列
     */
    public static int[] getSubStringPos(String str, String sub, boolean b) {
        String[] sp = null;
        int l = sub.length();
        sp = splitString(str, sub);
        if (sp == null) {
            return null;
        }
        int[] ip = new int[sp.length - 1];
        for (int i = 0; i < sp.length - 1; i++) {
            ip[i] = sp[i].length() + l;
            if (i != 0) {
                ip[i] += ip[i - 1];
            }
        }
        if (b) {
            for (int j = 0; j < ip.length; j++) {
                ip[j] = ip[j] - l;
            }
        }
        return ip;
    }

    /**
     *
     * 根据正则表达式分割字符串
     *
     * @param str
     *            源字符串
     * @param ms
     *            正则表达式
     * @return 目标字符串组
     */
    public static String[] splitString(String str, String ms) {
        String regEx = ms;
        Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        String[] sp = p.split(str);
        return sp;
    }

    /**
     * 根据正则表达式提取字符串,相同的字符串只返回一个
     *
     * @param str源字符串
     * @param pattern
     *            正则表达式
     * @return 目标字符串数据组
     *************************************************************************
     */

    /**
     * ★传入一个字符串，把符合pattern格式的字符串放入字符串数组
     * java.util.regex是一个用正则表达式所订制的模式来对字符串进行匹配工作的类库包
     *
     * @param str
     * @param pattern
     * @return
     */
    public static String[] getStringArrayByPattern(String str, String pattern) {
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(str);
        // 范型
        // 目的是：相同的字符串只返回一个。。。 不重复元素
        Set<String> result = new HashSet<String>();
        // boolean find() 尝试在目标字符串里查找下一个匹配子串。
        while (matcher.find()) {
            for (int i = 0; i < matcher.groupCount(); i++) {
                // int groupCount()
                // 返回当前查找所获得的匹配组的数量。
                // org.jeecgframework.core.util.LogUtil.info(matcher.group(i));
                result.add(matcher.group(i));

            }
        }
        String[] resultStr = null;
        if (result.size() > 0) {
            resultStr = new String[result.size()];
            // 将Set result转化为String[] resultStr
            return result.toArray(resultStr);
        }
        return resultStr;

    }

    /**
     * 得到第一个b,e之间的字符串,并返回e后的子串
     *
     * @param s
     *            源字符串
     * @param b
     *            标志开始
     * @param e
     *            标志结束
     * @return b,e之间的字符串
     */

    /*
     * String aaa="abcdefghijklmn"; String[] bbb=StringProcessor.midString(aaa,
     * "b","l");
     * org.jeecgframework.core.util.LogUtil.info("bbb[0]:"+bbb[0]);//cdefghijk
     * org.jeecgframework.core.util.LogUtil.info("bbb[1]:"+bbb[1]);//lmn
     * ★这个方法是得到第二个参数和第三个参数之间的字符串,赋给元素0;然后把元素0代表的字符串之后的,赋给元素1
     */

    /**
     * 元素0 元素1
     *
     * @param s
     * @param b
     * @param e
     * @return
     */
    public static String[] midString(String s, String b, String e) {
        int i = s.indexOf(b) + b.length();
        int j = s.indexOf(e, i);
        String[] sa = new String[2];
        if (i < b.length() || j < i + 1 || i > j) {
            sa[1] = s;
            sa[0] = null;
            return sa;
        } else {
            sa[0] = s.substring(i, j);
            sa[1] = s.substring(j);
            return sa;
        }
    }

    /**
     * 带有前一次替代序列的正则表达式替代
     *
     * @param s
     * @param pf
     * @param pb
     * @param start
     * @return
     */
    public static String stringReplace(String s, String pf, String pb, int start) {
        Pattern patternHand = Pattern.compile(pf);
        Matcher matcherHand = patternHand.matcher(s);
        int gc = matcherHand.groupCount();
        int pos = start;
        String sf1 = "";
        String sf2 = "";
        String sf3 = "";
        int if1 = 0;
        String strr = "";
        while (matcherHand.find(pos)) {
            sf1 = matcherHand.group();
            if1 = s.indexOf(sf1, pos);
            if (if1 >= pos) {
                strr += s.substring(pos, if1);
                pos = if1 + sf1.length();
                sf2 = pb;
                for (int i = 1; i <= gc; i++) {
                    sf3 = "\\" + i;
                    sf2 = replaceAll(sf2, sf3, matcherHand.group(i));
                }
                strr += sf2;
            } else {
                return s;
            }
        }
        strr = s.substring(0, start) + strr;
        return strr;
    }

    /**
     * 存文本替换
     *
     * @param s
     *            源字符串
     * @param sf
     *            子字符串
     * @param sb
     *            替换字符串
     * @return 替换后的字符串
     */
    public static String replaceAll(String s, String sf, String sb) {
        int i = 0, j = 0;
        int l = sf.length();
        boolean b = true;
        boolean o = true;
        String str = "";
        do {
            j = i;
            i = s.indexOf(sf, j);
            if (i > j) {
                str += s.substring(j, i);
                str += sb;
                i += l;
                o = false;
            } else {
                str += s.substring(j);
                b = false;
            }
        } while (b);
        if (o) {
            str = s;
        }
        return str;
    }

    /**
     * 字符串的替换
     *
     * @param strSource
     * @param strOld
     * @param strNew
     * @return
     */
    public static String replace(String strSource, String strOld, String strNew) {
        if (strSource == null) {
            return null;
        }
        int i = 0;
        if ((i = strSource.indexOf(strOld, i)) >= 0) {
            char[] cSrc = strSource.toCharArray();
            char[] cTo = strNew.toCharArray();
            int len = strOld.length();
            StringBuffer buf = new StringBuffer(cSrc.length);
            buf.append(cSrc, 0, i).append(cTo);
            i += len;
            int j = i;
            while ((i = strSource.indexOf(strOld, i)) > 0) {
                buf.append(cSrc, j, i - j).append(cTo);
                i += len;
                j = i;
            }
            buf.append(cSrc, j, cSrc.length - j);
            return buf.toString();
        }
        return strSource;
    }

    /**
     * 判断是否与给定字符串样式匹配
     *
     * @param str
     *            字符串
     * @param pattern
     *            正则表达式样式
     * @return 是否匹配是true,否false
     */
    public static boolean isMatch(String str, String pattern) {
        Pattern patternHand = Pattern.compile(pattern);
        Matcher matcherHand = patternHand.matcher(str);
        boolean b = matcherHand.matches();
        return b;
    }

    /**
     * 截取字符串
     *
     * @param s
     *            源字符串
     * @param jmp
     *            跳过jmp
     * @param sb
     *            取在sb
     * @param se
     *            于se
     * @return 之间的字符串
     */
    public static String subStringExe(String s, String jmp, String sb, String se) {
        if (StrUtil.isBlank(s)) {
            return "";
        }
        int i = s.indexOf(jmp);
        if (i >= 0 && i < s.length()) {
            s = s.substring(i + 1);
        }
        i = s.indexOf(sb);
        if (i >= 0 && i < s.length()) {
            s = s.substring(i + 1);
        }
        if (StrUtil.isBlank(se)) {
            return s;
        } else {
            i = s.indexOf(se);
            if (i >= 0 && i < s.length()) {
                s = s.substring(i + 1);
            }
            return s;
        }
    }

    /**
     * *************************************************************************
     * 用要通过URL传输的内容进行编码
     *
     * @param src
     * @return 经过编码的内容
     *************************************************************************
     */
    public static String urlEncode(String src) {
        String returnValue = "";
        try {
            if (src != null) {
                returnValue = URLEncoder.encode(src, "GBK");

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            returnValue = src;
        }

        return returnValue;
    }

    /**
     * *************************************************************************
     *
     * @author 李锋 2007.4.18
     * @param str
     *            &#31119;test&#29031;&#27004;&#65288;&#21271;&#22823;&#38376;&#
     *            24635 ;&#24215;&#65289;&#31119;
     * @return 经过解码的内容
     *************************************************************************
     */
    public static String getGBK(String str) {

        return transfer(str);
    }

    public static String transfer(String str) {
        String el = "&#\\d+;";
        Pattern p = Pattern.compile(el);
        Matcher m = p.matcher(str);
        while (m.find()) {
            String old = m.group();
            str = str.replaceAll(old, getChar(old));
        }
        return str;
    }

    public static String getChar(String str) {
        String dest = str.substring(2, str.length() - 1);
        char ch = (char) Integer.parseInt(dest);
        return "" + ch;
    }

    /**
     * yahoo首页中切割字符串.
     *
     * @author yxg
     * @date 2007-09-17
     * @param subject
     * @param size
     * @return
     */
    public static String subYhooString(String subject, int size) {
        subject = subject.substring(1, size);
        return subject;
    }

    public static String subYhooStringDot(String subject, int size) {
        subject = subject.substring(1, size) + "...";
        return subject;
    }

    /**
     * 泛型方法(通用)，把list转换成以“,”相隔的字符串 调用时注意类型初始化（申明类型） 如：List<Integer> intList =
     * new ArrayList<Integer>(); 调用方法：StringUtil.listTtoString(intList);
     * 效率：list中4条信息，1000000次调用时间为850ms左右
     *
     * @author fengliang
     * @serialData 2008-01-09
     * @param <T>
     *            泛型
     * @param list
     *            list列表
     * @return 以“,”相隔的字符串
     */
    public static <T> String listTtoString(List<T> list) {
        if (list == null || list.size() < 1) {
            return "";
        }
        Iterator<T> i = list.iterator();
        if (!i.hasNext()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (;;) {
            T e = i.next();
            sb.append(e);
            if (!i.hasNext()) {
                return sb.toString();
            }
            sb.append(",");
        }
    }

    /**
     * 把整形数组转换成以“,”相隔的字符串
     *
     * @author fengliang
     * @serialData 2008-01-08
     * @param a
     *            数组a
     * @return 以“,”相隔的字符串
     */
    public static String intArraytoString(int[] a) {
        if (a == null) {
            return "";
        }
        int iMax = a.length - 1;
        if (iMax == -1) {
            return "";
        }
        StringBuilder b = new StringBuilder();
        for (int i = 0;; i++) {
            b.append(a[i]);
            if (i == iMax) {
                return b.toString();
            }
            b.append(",");
        }
    }

    /**
     * 判断文字内容重复
     *
     * @author 沙枣
     * @Date 2008-04-17
     */
    public static boolean isContentRepeat(String content) {
        int similarNum = 0;
        int forNum = 0;
        int subNum = 0;
        int thousandNum = 0;
        String startStr = "";
        String nextStr = "";
        boolean result = false;
        float endNum = (float) 0.0;
        if (content != null && content.length() > 0) {
            int charOne = 1000;
            if (content.length() % charOne > 0) {
                thousandNum = (int) Math.floor((double)content.length() / 1000) + 1;
            } else {
                thousandNum = (int) Math.floor((double)content.length() / 1000);
            }
            int charTow = 3;
            int charThree = 6;
            int charFour = 9;
            if (thousandNum < charTow) {
                subNum = 100 * thousandNum;
            } else if (thousandNum < charThree) {
                subNum = 200 * thousandNum;
            } else if (thousandNum < charFour) {
                subNum = 300 * thousandNum;
            } else {
                subNum = 3000;
            }
            for (int j = 1; j < subNum; j++) {
                if (content.length() % j > 0) {
                    forNum = (int) Math.floor((double)content.length() / j) + 1;
                } else {
                    forNum = (int) Math.floor((double)content.length() / j);
                }
                if (result || j >= content.length()) {
                    break;
                } else {
                    for (int m = 0; m < forNum; m++) {
                        if (m * j > content.length() || (m + 1) * j > content.length()
                                || (m + 2) * j > content.length()) {
                            break;
                        }
                        startStr = content.substring(m * j, (m + 1) * j);
                        nextStr = content.substring((m + 1) * j, (m + 2) * j);
                        if (startStr.equals(nextStr)) {
                            similarNum = similarNum + 1;
                            endNum = (float) similarNum / forNum;
                            if (endNum > 0.4) {
                                result = true;
                                break;
                            }
                        } else {
                            similarNum = 0;
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 判断是否是空字符串 null和"" null返回result,否则返回字符串
     *
     * @param s
     * @return
     */
    public static String isEmpty(String s, String result) {
        if (s != null && !s.equals("")) {
            return s;
        }
        return result;
    }

    /**
     * 判断是否是空字符串 null和"" 都返回 true
     *
     * @author gaolh
     * @param s
     *            源字符串
     * @return
     */
    public static boolean isBlank(String s) {
        if (s != null && !s.trim().equals("")) {
            return false;
        }
        return true;
    }

    /**
     * 全角字符变半角字符
     *
     * @author shazao
     * @date 2008-04-03
     * @param str
     * @return
     */
    public static String full2Half(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (c >= 65281 && c < 65373) {
                sb.append((char) (c - 65248));
            } else {
                sb.append(str.charAt(i));
            }
        }

        return sb.toString();

    }

    /**
     * 全角括号转为半角
     *
     * @author shazao
     * @date 2007-11-29
     * @param str
     * @return
     */
    public static String replaceBracketStr(String str) {
        if (str != null && str.length() > 0) {
            str = str.replaceAll("（", "(");
            str = str.replaceAll("）", ")");
        }
        return str;
    }

    /**
     * 解析字符串返回map键值对(例：a=1&b=2 => a=1,b=2)
     *
     * @param query
     *            源参数字符串
     * @param split1
     *            键值对之间的分隔符（例：&）
     * @param split2
     *            key与value之间的分隔符（例：=）
     * @param dupLink
     *            重复参数名的参数值之间的连接符，连接后的字符串作为该参数的参数值，可为null
     *            null：不允许重复参数名出现，则靠后的参数值会覆盖掉靠前的参数值。
     * @return map
     * @author sky
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> parseQuery(String query, char split1, char split2, String dupLink) {
        if (!StrUtil.isBlank(query) && query.indexOf(split2) > 0) {
            Map<String, String> result = new HashMap(16);

            String name = null;
            String value = null;
            String tempValue = "";
            int len = query.length();
            for (int i = 0; i < len; i++) {
                char c = query.charAt(i);
                if (c == split2) {
                    value = "";
                } else if (c == split1) {
                    if (!StrUtil.isBlank(name) && value != null) {
                        if (dupLink != null) {
                            tempValue = result.get(name);
                            if (tempValue != null) {
                                value += dupLink + tempValue;
                            }
                        }
                        result.put(name, value);
                    }
                    name = null;
                    value = null;
                } else if (value != null) {
                    value += c;
                } else {
                    name = (name != null) ? (name + c) : "" + c;
                }
            }

            if (!StrUtil.isBlank(name) && value != null) {
                if (dupLink != null) {
                    tempValue = result.get(name);
                    if (tempValue != null) {
                        value += dupLink + tempValue;
                    }
                }
                result.put(name, value);
            }

            return result;
        }
        return null;
    }

    /**
     * 将list 用传入的分隔符组装为String
     *
     * @param list
     * @param slipStr
     * @return String
     */
    public static String listToStringSlipStr(List list, String slipStr) {
        StringBuffer returnStr = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                returnStr.append(list.get(i)).append(slipStr);
            }
        }
        if (returnStr.toString().length() > 0) {
            return returnStr.toString().substring(0, returnStr.toString().lastIndexOf(slipStr));
        } else {
            return "";
        }
    }

    /**
     * 获取从start开始用*替换len个长度后的字符串
     *
     * @param str
     *            要替换的字符串
     * @param start
     *            开始位置
     * @param len
     *            长度
     * @return 替换后的字符串
     */
    public static String getMaskStr(String str, int start, int len) {
        if (StrUtil.isBlank(str)) {
            return str;
        }
        if (str.length() < start) {
            return str;
        }

        // 获取*之前的字符串
        String ret = str.substring(0, start);

        // 获取最多能打的*个数
        int strLen = str.length();
        if (strLen < start + len) {
            len = strLen - start;
        }

        // 替换成*
        for (int i = 0; i < len; i++) {
            ret += "*";
        }

        // 加上*之后的字符串
        if (strLen > start + len) {
            ret += str.substring(start + len);
        }

        return ret;
    }

    /**
     * 根据传入的分割符号,把传入的字符串分割为List字符串
     *
     * @param slipStr
     *            分隔的字符串
     * @param src
     *            字符串
     * @return 列表
     */
    public static List<String> stringToStringListBySlipStr(String slipStr, String src) {

        if (src == null) {
            return null;
        }
        List<String> list = new ArrayList<String>();
        String[] result = src.split(slipStr);
        for (int i = 0; i < result.length; i++) {
            list.add(result[i]);
        }
        return list;
    }

    /**
     * 截取字符串
     *
     * @param str
     *            原始字符串
     * @param len
     *            要截取的长度
     * @param tail
     *            结束加上的后缀
     * @return 截取后的字符串
     */
    public static String getHtmlSubString(String str, int len, String tail) {
        if (str == null || str.length() <= len) {
            return str;
        }
        int length = str.length();
        char c = ' ';
        String tag = null;
        String name = null;
        int size = 0;
        String result = "";
        boolean isTag = false;
        List<String> tags = new ArrayList<String>();
        int i = 0;
        for (int end = 0, spanEnd = 0; i < length && len > 0; i++) {
            c = str.charAt(i);
            if (c == '<') {
                end = str.indexOf('>', i);
            }

            if (end > 0) {
                // 截取标签
                tag = str.substring(i, end + 1);
                int n = tag.length();
                if (tag.endsWith("/>")) {
                    isTag = true;
                    // 结束符
                } else if (tag.startsWith("</")) {
                    name = tag.substring(2, end - i);
                    size = tags.size() - 1;
                    // 堆栈取出html开始标签
                    if (size >= 0 && name.equals(tags.get(size))) {
                        isTag = true;
                        tags.remove(size);
                    }
                } else { // 开始符
                    spanEnd = tag.indexOf(' ', 0);
                    spanEnd = spanEnd > 0 ? spanEnd : n;
                    name = tag.substring(1, spanEnd);
                    if (name.trim().length() > 0) {
                        // 如果有结束符则为html标签
                        spanEnd = str.indexOf("</" + name + ">", end);
                        if (spanEnd > 0) {
                            isTag = true;
                            tags.add(name);
                        }
                    }
                }
                // 非html标签字符
                if (!isTag) {
                    if (n >= len) {
                        result += tag.substring(0, len);
                        break;
                    } else {
                        len -= n;
                    }
                }

                result += tag;
                isTag = false;
                i = end;
                end = 0;
            } else { // 非html标签字符
                len--;
                result += c;
            }
        }
        // 添加未结束的html标签
        for (String endTag : tags) {
            result += "</" + endTag + ">";
        }
        if (i < length) {
            result += tail;
        }
        return result;
    }

    public static String getProperty(String property) {
        String strOne = "_";
        if (property.contains(strOne)) {
            return property.replaceAll("_", "\\.");
        }
        return property;
    }

    /**
     * 解析前台encodeURIComponent编码后的参数
     *
     * @param property
     *            (encodeURIComponent(no))
     * @return
     */
    public static String getEncodePra(String property) {
        String trem = "";
        if (StrUtil.isNotBlank(property)) {
            try {
                trem = URLDecoder.decode(property, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return trem;
    }

    /**
     * 判断一个字符串是否都为数字
     *
     * @param strNum
     * @return
     */
    public boolean isDigit(String strNum) {
        String el = "[0-9]{1,}";
        Pattern pattern = Pattern.compile(el);
        Matcher matcher = pattern.matcher((CharSequence) strNum);
        return matcher.matches();
    }

    /**
     * 截取数字
     *
     * @param content
     * @return
     */
//    public String getNumbers(String content) {
//        String el = "\\d+";
//        Pattern pattern = Pattern.compile(el);
//        Matcher matcher = pattern.matcher(content);
//        while (matcher.find()) {
//            return matcher.group(0);
//        }
//        return "";
//    }

    /**
     * 截取非数字
     *
     * @param content
     * @return
     */
//    public String splitNotNumber(String content) {
//        String el = "\\D+";
//        Pattern pattern = Pattern.compile(el);
//        Matcher matcher = pattern.matcher(content);
//        while (matcher.find()) {
//            return matcher.group(0);
//        }
//        return "";
//    }

    /**
     * 判断某个字符串是否存在于数组中
     *
     * @param stringArray
     *            原数组
     * @param source
     *            查找的字符串
     * @return 是否找到
     */
    public static boolean contains(String[] stringArray, String source) {
        // 转换为list
        List<String> tempList = Arrays.asList(stringArray);

        // 利用list的包含方法,进行判断
        if (tempList.contains(source)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * html 必须是格式良好的
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String formatHtml(String str) throws Exception {
        org.dom4j.Document document = null;
        document = DocumentHelper.parseText(str);

        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        StringWriter writer = new StringWriter();

        HTMLWriter htmlWriter = new HTMLWriter(writer, format);

        htmlWriter.write(document);
        htmlWriter.close();
        return writer.toString();
    }

    /**
     * 首字母大写
     *
     * @param realName
     * @return
     */
    public static String firstUpperCase(String realName) {
        return StringUtils.replaceChars(realName, realName.substring(0, 1), realName.substring(0, 1).toUpperCase());
    }

    /**
     * 首字母小写
     *
     * @param realName
     * @return
     */
    public static String firstLowerCase(String realName) {
        return StringUtils.replaceChars(realName, realName.substring(0, 1), realName.substring(0, 1).toLowerCase());
    }

    /**
     * 判断这个类是不是java自带的类
     *
     * @param clazz
     * @return
     */
    public static boolean isJavaClass(Class<?> clazz) {
        boolean isBaseClass = false;
        String strOne = "java.lang";
        if (clazz.isArray()) {
            isBaseClass = false;
        } else if (clazz.isPrimitive() || clazz.getPackage() == null || clazz.getPackage().getName().equals(strOne)
                || clazz.getPackage().getName().equals("java.math")
                || clazz.getPackage().getName().equals("java.util")) {
            isBaseClass = true;
        }
        return isBaseClass;
    }

    /**
     * 判断这个类是不是java自带的类
     *
     * @return
     */
    public static String getEmptyString() {
        return "";
    }

    public static boolean isTopURL(String str) {
        // 转换为小写
        str = str.toLowerCase();
        String domainRules = "com.cn|net.cn|org.cn|gov.cn|com.hk|公司|中国|网络|com|net|org|int|edu|gov|mil|arpa|Asia|biz|info|name|pro|coop|aero|museum|ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cf|cg|ch|ci|ck|cl|cm|cn|co|cq|cr|cu|cv|cx|cy|cz|de|dj|dk|dm|do|dz|ec|ee|eg|eh|es|et|ev|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gh|gi|gl|gm|gn|gp|gr|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|in|io|iq|ir|is|it|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|ml|mm|mn|mo|mp|mq|mr|ms|mt|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nt|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|pt|pw|py|qa|re|ro|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sk|sl|sm|sn|so|sr|st|su|sy|sz|tc|td|tf|tg|th|tj|tk|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|va|vc|ve|vg|vn|vu|wf|ws|ye|yu|za|zm|zr|zw";
        String regex = "^((https|http|ftp|rtsp|mms)?://)" + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" // ftp的user@
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
                + "|" // 允许IP和DOMAIN（域名）
                + "(([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]+\\.)?" // 域名- www.
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
                + "(" + domainRules + "))" // first level domain- .com or
                                           // .museum
                + "(:[0-9]{1,4})?" // 端口- :80
                + "((/?)|)"; // a slash isn't required if there is no file name
                //+ "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher isUrl = pattern.matcher(str);
        return isUrl.matches();
    }

    /**
     * 通过code隐藏content的结果
     * code - 禁止纯数字
     * @Author dusd
     * @Date 2019/8/11 19:26
     * @param
     * @return
     */
    public static String replaceViewTagHtml(String content,List<String> notShowCodeList,List<String> showCodeList) {
        if(StrUtil.isBlank(content))
            return "";
        if(notShowCodeList == null)
            notShowCodeList = new ArrayList<>();
        if(showCodeList == null)
            showCodeList = new ArrayList<>();

        for (String code : showCodeList) {
            notShowCodeList.add("!"+code);
        }

        for (String code : notShowCodeList) {
            if(code.contains("!"))
                continue;
            showCodeList.add("!"+code);
        }


        for (String code : notShowCodeList) {
            content = content.replace("&lt;"+code+"&gt;", "<"+code+">").replace("&lt;/"+code+"&gt;", "</"+code+">");
        }

        for (String code : showCodeList) {
            content = content.replace("&lt;"+code+"&gt;", "<"+code+">").replace("&lt;/"+code+"&gt;", "</"+code+">");
        }

//        Document doc = Jsoup.parse(content);
        for (String code : notShowCodeList) {
            String regex = "<"+code+">[\\s\\S]*?<\\/"+code+">";
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(content);
            content = matcher.replaceAll("");
        }
//        content = doc.body().html();
        for (String code : showCodeList) {
            content = content.replace("<"+code+">", "").replace("</"+code+">", "");
        }
//        System.out.println(content);
        return content;
    }

    /**
     * 过滤字符中的中文
     * @param words
     * @return
     */
    public static String filterChineseCharacters(String words) {
        if (StringUtil.isBlank(words) || Objects.equals("无", words)) return words;
        String reg = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(reg);
        Matcher mat = pat.matcher(words);
        return mat.replaceAll("");
    }

//    public static void main(String[] args) {
//        String content = "<A1><E10>5tian</E10><!E10>10tian</!E10></A1>";
//        List<String> notShowCodeList = new ArrayList<>();
//        List<String> showCodeList = new ArrayList<>();
//        showCodeList.add("A1");
//        notShowCodeList.add("E10");
//        content = StringUtil.replaceViewTagHtml(content,notShowCodeList,showCodeList);
//        System.out.println(content);
//    }

    public static String getBase64FromInputStream(InputStream in) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;
        // 读取图片字节数组
        try {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int rc = 0;
            while ((rc = in.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            data = swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new BASE64Encoder().encode(data);
    }

}
