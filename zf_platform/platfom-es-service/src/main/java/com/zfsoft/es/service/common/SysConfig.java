package com.zfsoft.es.service.common;

/**
 * @author: kkfan
 * @create: 2020-08-28 13:21:59
 * @description: 静态变量
 */
public class SysConfig {

    /** 常见分词器 */
    public interface ANALYZER {
        interface IK {
            /** 最细粒度划分 */
            String IK_MAX_WORD = "ik_max_word";

            /** 最少切分 */
            String IK_SMART = "ik_smart";
        }
    }

    /** es常见字段类型 6.x */
    public interface ES_COMMON_FIELD_TYPE {
        /** 字符串 */
        interface STRING {
            /** 文本 */
            String TEXT = "text";
            /** 关键字 */
            String KEYWORD = "keyword";
        }
        /** 整数 */
        interface WHOLE_NUMBER {
            String BYTE = "byte";
            String SHORT = "short";
            String INTEGER = "integer";
            String LONG = "long";
        }
        /** 浮点数 */
        interface FLOAT_NUMBER {
            String DOUBLE = "double";
            String FLOAT = "float";
            String HALF_FLOAT = "half_float";
            String SCALE_FLOAT = "scale_float";
        }
        /** 逻辑 */
        String BOOLEAN = "boolean";
        /** 日期 */
        String DATE = "date";
        /** 范围 */
        String RANGE = "range";
        /** 二进制 */
        String BINARY = "binary";
        /** 数组 */
        String ARRAY = "array";
        /** 对象 */
        String OBJECT = "object";
        /** 嵌套 */
        String NESTED = "nested";
    }

    public static String FIELD_MODIFIED = "modified";

    /** 排序方式 */
    public interface SORT_TYPE {
        /** 倒叙 */
        String DESC = "desc";

        /** 正序 */
        String ASC = "asc";
    }
}
